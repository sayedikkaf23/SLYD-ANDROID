package com.kotlintestgradle.remote.model.response.login

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
data class LoginResponse(
    /*"id":"5d6fc4dbb009f741b201acaa",
  "email":"akbar@mobifyi.com",
  "firstName":"akbar",
  "lastName":"A",
  "phone":{},
  "profilePic":"https://admin.loademup.xyz/pics/user.jpg",
  "fcmTopic":"5d6fc4dbb009f741b201acaa",
  "mqttTopic":"5d6fc4dbb009f741b201acaa",
  "authToken":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ZDZmYzRkYmIwMDlmNzQxYjIwMWFjYWEiLCJrZXkiOiJhY2MiLCJhY2Nlc3NDb2RlIjo1NDE4LCJpYXQiOjE1Njc3NjE4MTAsImV4cCI6MTU2Nzc2MTg5Niwic3ViIjoidXNlciJ9.Db2XijG0ME__Mj05bykqU4cj7VnhB1MEyU4bgbBpgj4",
  "willTopic":"lastWill"*/
    @SerializedName("id") @Expose val id: String,
    @SerializedName("email") @Expose val email: String,
    @SerializedName("firstName") @Expose val firstName: String,
    @SerializedName("lastName") @Expose val lastName: String,
    @SerializedName("phone") @Expose val phone: PhoneData,
    @SerializedName("profilePic") @Expose val profilePic: String,
    @SerializedName("fcmTopic") @Expose val fcmTopic: String,
    @SerializedName("mqttTopic") @Expose val mqttTopic: String,
    @SerializedName("authToken") @Expose val authToken: String,
    @SerializedName("willTopic") @Expose val willTopic: String,
    @SerializedName("status") @Expose val status: String
) : ValidItem {
    override fun isValid(): Boolean {
        return true
    }
}

/*"countryCode":"+91",
"phone":"9620407863"*/
data class PhoneData(
    @SerializedName("countryCode") @Expose val countryCode: String,
    @SerializedName("phone") @Expose val phone: String
) : ValidItem {
    override fun isValid(): Boolean {
        return true
    }
}