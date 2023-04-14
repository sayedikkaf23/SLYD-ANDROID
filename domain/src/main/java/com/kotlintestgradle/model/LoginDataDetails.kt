package com.kotlintestgradle.model

/**
 * @author 3Embed
 * Mapper for login response'
 *
 * @since 1.0 (23-Aug-2019)
 */
data class LoginDataDetails(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: PhoneData?,
    val profilePic: String,
    val fcmTopic: String?,
    val mqttTopic: String?,
    val authToken: String?,
    val willTopic: String?,
    val status: String?
) {
    var isUserAddedInCall: Boolean = false
}

class PhoneData(
    val countryCode: String,
    val phone: String
)
