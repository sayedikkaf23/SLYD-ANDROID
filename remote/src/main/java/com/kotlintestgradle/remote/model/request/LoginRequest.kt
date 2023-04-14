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
data class LoginRequest(
    @Expose
    @SerializedName("mobileOrEmail")
    val mobileOrEmail: String,
    @Expose
    @SerializedName("countryCode")
    private val countryCode: String?,
    @Expose
    @SerializedName("pushKitToken")
    private val pushKitToken: String?,
    @Expose
    @SerializedName("deviceType") // 1-Android, 2-IOS, 3-Web
    private val deviceType: String
)