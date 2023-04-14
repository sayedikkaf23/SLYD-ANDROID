package chat.hola.com.app.coin.pack

import chat.hola.com.app.AppController
import chat.hola.com.app.Networking.HowdooService
import chat.hola.com.app.Utilities.Constants
import chat.hola.com.app.coin.model.CoinPackage
import chat.hola.com.app.coin.model.CoinPackageResponse
import chat.hola.com.app.manager.session.SessionManager
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class CoinPackPresenter @Inject constructor() : CoinPackContract.Presenter {
    @Inject
    lateinit var service: HowdooService

    @Inject
    lateinit var sessionManager: SessionManager

    private var mView: CoinPackContract.View? = null

    override fun balance() {
        mView!!.balance(sessionManager.coinBalance.toDouble())
    }

    override fun coinPackage(skip: Int, limit: Int) {
        mView!!.showLoader()
        service.coinPlans(AppController.getInstance().apiToken, Constants.LANGUAGE, skip, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Response<CoinPackageResponse>>() {
                    override fun onNext(response: Response<CoinPackageResponse>) {
                        if (response.code() == 200) {
                            mView!!.coinPackage(response.body()!!.data!!.packages)
                        }
                        mView!!.hideLoader()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView!!.hideLoader()
                    }

                    override fun onComplete() {
                        mView!!.hideLoader()
                    }
                })
    }

    override fun acknowledgePurchase(purchase: Purchase, billingResult: BillingResult, coinPackage: CoinPackage) {
        mView!!.showLoader()
        val map: MutableMap<String, Any?> = HashMap()
        map["userId"] = AppController.getInstance().userId
        map["userType"] = Constants.APP_TYPE
        map["planId"] = coinPackage.id
        map["pgTxnId"] = purchase.orderId
        map["pgName"] = Constants.PAYMENT_GATEWAY
        map["notes"] = coinPackage.description
        map["description"] = coinPackage.description
        map["trigger"] = coinPackage.description
        map["currency"] = coinPackage.currency
        map["amount"] = coinPackage.planCost
        service.acknowledgePurchase(AppController.getInstance().apiToken, Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Response<ResponseBody>>() {
                    override fun onNext(response: Response<ResponseBody>) {
                        if (response.code() == 200) {
                            val balance = (sessionManager.coinBalance).toDouble() + (coinPackage.coins)!!.toDouble()
                            sessionManager.coinBalance = balance.toString()
                            mView!!.balance(balance)
                        }
                        mView!!.hideLoader()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mView!!.hideLoader()
                    }

                    override fun onComplete() {
                        mView!!.hideLoader()
                    }
                })
    }

    override fun attach(view: CoinPackContract.View) {
        mView = view
    }

    override fun detach() {
        mView = null
    }
}