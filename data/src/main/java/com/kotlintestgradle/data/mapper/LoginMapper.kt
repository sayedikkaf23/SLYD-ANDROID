package com.kotlintestgradle.data.mapper

import com.kotlintestgradle.model.LoginDataDetails
import com.kotlintestgradle.model.PhoneData
import com.kotlintestgradle.remote.model.response.login.LoginResponse
import javax.inject.Inject

/**
 * @author 3Embed
 *
 * used to map login data what we get from login response
 *
 * @since 1.0 (23-Aug-2019)
 */
class LoginMapper @Inject constructor() {
    fun map(response: LoginResponse): LoginDataDetails {
        return with(response) {
            LoginDataDetails(
                id = id,
                email = email,
                firstName = firstName,
                lastName = lastName,
                phone = PhoneData(
                    phone.countryCode,
                    phone.phone
                ),
                profilePic = profilePic,
                fcmTopic = fcmTopic,
                mqttTopic = mqttTopic,
                authToken = authToken,
                willTopic = willTopic,
                status = status
            )
        }
    }
}