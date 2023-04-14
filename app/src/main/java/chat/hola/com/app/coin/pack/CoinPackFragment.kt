package chat.hola.com.app.coin.pack

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import chat.hola.com.app.Utilities.Loader
import chat.hola.com.app.Utilities.TypefaceManager
import chat.hola.com.app.base.BaseFragment
import chat.hola.com.app.coin.adapter.CoinPackageAdapter
import chat.hola.com.app.coin.model.CoinPackage
import chat.hola.com.app.wallet.transaction.TransactionActivity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.ezcall.android.R
import javax.inject.Inject

class CoinPackFragment @Inject constructor() : BaseFragment(), CoinPackContract.View,
        CoinPackageAdapter.OnClickListener, PurchasesUpdatedListener {

    @Inject
    lateinit var mPresenter: CoinPackPresenter

    @Inject
    lateinit var mAdapter: CoinPackageAdapter

    @Inject
    lateinit var mFont: TypefaceManager

    var counter: Int = 0
    private lateinit var ibBack: ImageButton
    private lateinit var tvCoinBalance: TextView
    private lateinit var tvTransactions: TextView
    private lateinit var tvMyBalanceText: TextView
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var rvPackage: RecyclerView
    private lateinit var mLoader: Loader
    private lateinit var refresh: SwipeRefreshLayout
    private var isBillingAvailable: Boolean = false
    private lateinit var billingClient: BillingClient
    private val skuList = mutableListOf<String>()
    private var packages: ArrayList<CoinPackage> = ArrayList()
    private lateinit var selectedCoinPackage: CoinPackage

    override fun getLayoutId(): Int {
        return R.layout.fragment_coin_package
    }

    override fun initView(view: View) {
        super.initView(view)
        mLoader = context?.let { Loader(it) }!!
        tvMyBalanceText = view.findViewById(R.id.tvMyBalanceText)
        tvTransactions = view.findViewById(R.id.tvTransactions)
        tvCoinBalance = view.findViewById(R.id.tvCoinBalance)
        rvPackage = view.findViewById(R.id.rvPackage)
        refresh = view.findViewById(R.id.refresh)
        ibBack = view.findViewById(R.id.ibBack)

        refresh.setOnRefreshListener {
            refreshCoinPackage()
            refresh.isRefreshing = false
        }

        mLayoutManager = GridLayoutManager(context, 3)
        rvPackage.layoutManager = mLayoutManager
        mAdapter.setListener(this)
        rvPackage.adapter = mAdapter

        mPresenter.attach(this)
        mPresenter.balance()

        setupBillingClient()

        ibBack.setOnClickListener {
            activity?.finish()
        }

        tvTransactions.setOnClickListener {
            startActivity(Intent(activity, TransactionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
//        val billingResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
//        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//            if (billingResult.purchasesList != null) {
//                for (purchase in billingResult.purchasesList!!) {
//                    acknowledgePurchase(purchase, purchase.purchaseToken)
//                    Log.e("purchased query", "product purchased")
//                }
//            }
//            else {
//                val p = billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList;
//                Log.i("buy-3", "" + p!!.size)
//                for (purchase in p) {
//                    acknowledgePurchase(purchase, purchase.purchaseToken)
//                }
//            }
//        }
    }

    /**
     * calls coin package api
     */
    private fun refreshCoinPackage() {
        mPresenter.coinPackage(0, 20)
    }

    override fun setTypeface() {
        super.setTypeface()
        tvCoinBalance.typeface = mFont.boldFont
        tvTransactions.typeface = mFont.semiboldFont
        tvMyBalanceText.typeface = mFont.semiboldFont
    }

    override fun balance(balance: Double) {
        tvCoinBalance.text = balance.toString()
    }

    override fun coinPackage(packages: ArrayList<CoinPackage>) {
        this.packages = packages
        skuList.clear()

        for (p in packages) {
            p.playstoreId?.let {
                skuList.add(it)
            }
//            Log.i("sku", "" + p.playstoreId)
        }
        if (isBillingAvailable)
            loadAllSKUs()
    }

    override fun message(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun message(message: Int) {
        message(getString(message))
    }

    override fun showLoader() {
        if (activity.isAlive())
            mLoader.show()
    }

    override fun hideLoader() {
        if (activity.isAlive() && mLoader.isShowing) mLoader.dismiss()
    }

    override fun onPackageSelect(coinPackage: CoinPackage) {
        selectedCoinPackage = coinPackage
        val billingFlowParams = coinPackage.skuDetails?.let {
            BillingFlowParams.newBuilder().setSkuDetails(it).build()
        }
        if (billingFlowParams != null) {
//            Log.i("counter-1", "" + counter)
            if (counter == 0) {
                counter++
                billingClient.launchBillingFlow(context as Activity, billingFlowParams)
            } else {
                message("Your last purchasing is under process, please wait")
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {

//        Log.i("buy-1", "" + billingResult.responseCode)
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            if (purchases != null) {
//                Log.i("buy-2", "" + purchases.size)
                for (purchase in purchases) {
                    acknowledgePurchase(purchase, purchase.purchaseToken)
                }
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
//            val p = billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList;
//            Log.i("buy-3", "" + p!!.size)
//            for (purchase in p) {
//                acknowledgePurchase(purchase, purchase.purchaseToken)
//            }
        } else {
            counter--
        }
    }

    private fun setupBillingClient() {
        billingClient = context?.let {
            BillingClient.newBuilder(it)
                    .enablePendingPurchases()
                    .setListener(this)
                    .build()
        }!!
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is setup successfully
                    isBillingAvailable = true
                    refreshCoinPackage()
//                    Log.i("in-app", "billing connected")
                }
            }

            override fun onBillingServiceDisconnected() {
                isBillingAvailable = false
//                Log.i("in-app", "billing disconnected")
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }

        })


    }

    private fun loadAllSKUs() = if (billingClient.isReady) {
        val params = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(BillingClient.SkuType.INAPP).build()
        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // Process the result.
            Log.d("log111-billing res", billingResult.responseCode.toString())
            Log.d("log111-billing res", skuDetailsList!!.size.toString())
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList.isNotEmpty()) {
                skuList.clear()
                for (skuDetails in skuDetailsList) {
                    //this will return both the SKUs from Google Play Console
                    skuList.add(skuDetails.sku)
                }
                val tempList: ArrayList<CoinPackage> = ArrayList(skuList.size)
                for (pack in packages) {
                    if (skuList.contains(pack.playstoreId)) {
                        tempList.add(pack)
                    }
                }

                var i = 0
                for (sk in skuDetailsList)
                    tempList[i++].skuDetails = sk
                mAdapter.setData(tempList)
            }
        }

    } else {
        println("Billing Client not ready")
    }

    private fun acknowledgePurchase(purchase: Purchase, purchaseToken: String) {
//        Log.i("counter-1.1", "" + purchaseToken)
        val params = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchaseToken).build()
        billingClient.acknowledgePurchase(params) { billingResult ->
// val responseCode = billingResult.responseCode
// val debugMessage = billingResult.debugMessage
        }

        val consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
//        Log.i("counter-params", "" + consumeParams.purchaseToken)
//        Log.i("counter-params-1", "" + purchase.purchaseState)
//        Log.i("counter-params-2", "" + purchase.isAcknowledged)

        billingClient.consumeAsync(consumeParams) { billingResult, _ ->
//             billingClient.queryPurchases(BillingClient.SkuType.INAPP)
//            Log.i("counter-2", "" + counter)
//            Log.i("counter-3", "" + billingResult.responseCode)
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                counter--
//                Log.i("PURCHASE", "consumed " + consumeParams.purchaseToken)
                mPresenter.acknowledgePurchase(purchase, billingResult, selectedCoinPackage)
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ERROR) {
                counter--
            }
        }
    }

    // This callback will only be called when MyFragment is at least Started.
    var callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
        override fun handleOnBackPressed() {
            activity?.finish()
        }
    }
    private fun Activity?.isAlive(): Boolean = !(this?.isFinishing ?: true)
}