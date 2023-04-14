package com.appscrip.stripe

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.appscrip.stripe.Constants.GOOGLE_PAY_RC
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import com.stripe.android.ApiResultCallback
import com.stripe.android.GooglePayJsonFactory
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.PaymentMethodCreateParams
import org.json.JSONObject

class GooglePaySettings(private val context: Activity) {
    private val stripe: Stripe by lazy {
        Stripe(context, publishableKey = PaymentConfiguration.getInstance(context).publishableKey)
    }
    private val paymentsClient: PaymentsClient by lazy {
        Wallet.getPaymentsClient(
            context,
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build()
        )
    }

    private val googlePayJsonFactory: GooglePayJsonFactory by lazy {
        GooglePayJsonFactory(context)
    }

    /**
     * Check that Google Pay is available and ready
     */
    fun initialize(accountsDelegate: AccountsDelegate) {
        PaymentConfiguration.init(context, Settings(context).publishableKey)
        val request = IsReadyToPayRequest.fromJson(
            googlePayJsonFactory.createIsReadyToPayRequest().toString()
        )

        paymentsClient.isReadyToPay(request)
            .addOnCompleteListener { task ->
                try {
                    if (task.isSuccessful) {
                        accountsDelegate.onSuccess(task.isSuccessful)
                    } else {
                        accountsDelegate.onSuccess(task.isSuccessful)
                    }
                } catch (exception: ApiException) {
                    Log.e("StripeExample", "Exception in isReadyToPay", exception)
                    //showSnackbar("Exception: ${exception.localizedMessage}")
                }
            }
    }

    fun payWithGoogle() {
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(
                PaymentDataRequest.fromJson(
                    googlePayJsonFactory.createPaymentDataRequest(
                        transactionInfo = GooglePayJsonFactory.TransactionInfo(
                            currencyCode = "USD",
                            totalPrice = 10000,
                            totalPriceStatus = GooglePayJsonFactory.TransactionInfo.TotalPriceStatus.Final
                        ),
                        merchantInfo = GooglePayJsonFactory.MerchantInfo(
                            merchantName = "Widget Store"
                        ),
                        shippingAddressParameters = GooglePayJsonFactory.ShippingAddressParameters(
                            isRequired = true,
                            allowedCountryCodes = setOf("US", "DE"),
                            phoneNumberRequired = true
                        ),
                        billingAddressParameters = GooglePayJsonFactory.BillingAddressParameters(
                            isRequired = true,
                            format = GooglePayJsonFactory.BillingAddressParameters.Format.Full,
                            isPhoneNumberRequired = true
                        )
                    ).toString()
                )
            ),
            context,
            GOOGLE_PAY_RC
        )
    }

//    fun handleGooglePayResult(data: Intent, accountsDelegate: AccountsDelegate) {
//        val paymentData = PaymentData.getFromIntent(data) ?: return
//        val paymentDataJson = JSONObject(paymentData.toJson())
//        val paymentMethodCreateParams =
//            PaymentMethodCreateParams.createFromGooglePay(paymentDataJson)
//
//        stripe.createPaymentMethod(paymentMethodCreateParams,
//                object : ApiResultCallback<PaymentMethod> {
//                    override fun onSuccess(result: PaymentMethod) {
//                        Log.d("StripeExample", "Exception while creating PaymentMethod ${result.id}")
//                        result.id?.let { accountsDelegate.onSuccess(it) }
//                    }
//
//                    override fun onError(e: Exception) {
//                        Log.e("StripeExample", "Exception while creating PaymentMethod", e)
//                        accountsDelegate.onFailure(e.localizedMessage)
//                    }
//                }.toString())
//    }
}