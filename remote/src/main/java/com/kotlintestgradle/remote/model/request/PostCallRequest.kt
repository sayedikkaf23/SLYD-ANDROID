package com.kotlintestgradle.remote.model.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author 3Embed
 *
 * model class for login request class
 *
 * @since 1.0 (23-Aug-2019)
 */
data class PostCallRequest(
        @Expose
        @SerializedName("type") // audio or video
        val type: String,
        @Expose
        @SerializedName("room")
        private val room: String,
        @Expose
        @SerializedName("to")
        private val to: ArrayList<String>,
        @Expose
        @SerializedName("sessionId")
        val sessionId: String
)