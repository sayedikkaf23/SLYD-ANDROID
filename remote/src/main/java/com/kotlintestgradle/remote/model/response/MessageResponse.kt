package com.kotlintestgradle.remote.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kotlintestgradle.remote.core.ValidItem

data class MessageResponse(
    @SerializedName("message") @Expose val message: String
) : ValidItem {
    override fun isValid(): Boolean {
        return true
    }
}
