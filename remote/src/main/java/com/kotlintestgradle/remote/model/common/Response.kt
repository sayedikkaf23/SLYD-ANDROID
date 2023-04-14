package com.kotlintestgradle.remote.model.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author 3Embed
 *used for retrofit
 * @since 1.0 (23-Aug-2019)
 */
data class Response(
    @Expose @SerializedName("Success") var success: Boolean = false,
    @Expose @SerializedName("message") var message: String? = null,
    @Expose @SerializedName("errorCode")
    var errorCode: String? = null
)
