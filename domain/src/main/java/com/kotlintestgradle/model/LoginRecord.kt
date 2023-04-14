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
data class LoginRecord(
    @Expose
    @SerializedName("mobileOrEmail")
    val mobileOrEmail: String,
    @Expose
    @SerializedName("countryCode")
    val countryCode: String?,
    @Expose
    @SerializedName("pushKitToken")
    val pushKitToken: String?,
    @Expose
    @SerializedName("deviceType") // 1-Android, 2-IOS, 3-Web
    val deviceType: String
)