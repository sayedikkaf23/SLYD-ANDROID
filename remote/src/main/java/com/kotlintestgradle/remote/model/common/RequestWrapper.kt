package com.jio.consumer.http.model.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * @author 3Embed
 *used for retrofit
 * @since 1.0 (23-Aug-2019)
 */
data class RequestWrapper<REQ>(@Expose @SerializedName("Request") var request: REQ)