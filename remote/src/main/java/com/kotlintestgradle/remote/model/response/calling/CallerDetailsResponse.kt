package com.kotlintestgradle.remote.model.response.calling

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kotlintestgradle.remote.core.ValidItem

data class CallerDetailsResponse(
    @SerializedName("action") @Expose val action: Int,
    @SerializedName("userId") @Expose val userId: String,
    @SerializedName("userName") @Expose val userName: String,
    @SerializedName("userImage") @Expose val userImage: String,
    @SerializedName("callId") @Expose val callId: String,
    @SerializedName("room") @Expose val room: String,
    @SerializedName("message") @Expose val message: String?,
    @SerializedName("userBusy") @Expose val userBusy: Boolean
) : ValidItem {
    override fun isValid(): Boolean {
        return true
    }
}