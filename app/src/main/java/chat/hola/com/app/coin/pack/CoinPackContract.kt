package chat.hola.com.app.coin.pack

import chat.hola.com.app.base.BasePresenter
import chat.hola.com.app.base.BaseView
import chat.hola.com.app.coin.model.CoinPackage
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase

interface CoinPackContract {
    interface View : BaseView {
        /**
         * To display current coin wallet balance
         *
         * @param balance : coin balance
         */
        fun balance(balance: Double)

        /**
         * To set coin package to the adapter
         *
         * @param packages : list of data
         */
        fun coinPackage(packages: ArrayList<CoinPackage>)
    }

    interface Presenter : BasePresenter<View> {
        /**
         * Gets coin wallet balance
         */
        fun balance()

        /**
         * Gets coin packages
         */
        fun coinPackage(skip: Int, limit: Int)

        /**
         * To acknowlege the coin purchase to the server
         */
        fun acknowledgePurchase(purchase: Purchase, billingResult: BillingResult, selectedCoinPackage: CoinPackage)
    }
}