package com.kotlintestgradle.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author 3Embed
 *
 * Mapper for login data to sent to api'
 *
 * @since 1.0 (23-Aug-2019)
 */
data class PostCallRecord(
    @Expose
    @SerializedName("type") // audio or video
    val type: String,
    @Expose
    @SerializedName("room")
    val room: String,
    @Expose
    @SerializedName("to")
    val to: ArrayList<String>,
    @Expose
    @SerializedName("sessionId")
    val sessionId: String
)