package com.kotlintestgradle.remote.model.response.calling

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kotlintestgradle.remote.core.ValidItem

/**
 * @author 3Embed
 *
 * model class for login response
 *
 * @since 1.0 (23-Aug-2019)
 */
data class CallingResponse(
    @SerializedName("callId") @Expose val callId: String
) : ValidItem {
    override fun isValid(): Boolean {
        return true
    }
}