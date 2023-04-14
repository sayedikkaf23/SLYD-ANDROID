package com.appscrip.stripe

/*Acts as callback for payment methods */
interface AccountsDelegate {
    /*returns success */
    fun onSuccess(successData: Any)

    /*returns failure*/
    fun onFailure(failure: String)
}