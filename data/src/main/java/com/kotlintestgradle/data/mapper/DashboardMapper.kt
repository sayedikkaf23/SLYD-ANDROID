package com.kotlintestgradle.data.mapper

import com.kotlintestgradle.model.LoginDataDetails
import com.kotlintestgradle.model.PhoneData
import com.kotlintestgradle.model.UsersListDataDetails
import com.kotlintestgradle.remote.model.response.dashboard.UsersListResponse
import javax.inject.Inject

/**
 * @author 3Embed
 *
 * used to map login data what we get from login response
 *
 * @since 1.0 (23-Aug-2019)
 */
class DashboardMapper @Inject constructor() {
    fun map(response: UsersListResponse): UsersListDataDetails {
        val usersList = arrayListOf<LoginDataDetails>()
        for (loginList in response.usersList) {
            usersList.add(
                LoginDataDetails(
                    id = loginList.id,
                    email = loginList.email,
                    firstName = loginList.firstName,
                    lastName = loginList.lastName,
                    phone = PhoneData(loginList.phone.countryCode, loginList.phone.phone),
                    profilePic = loginList.profilePic,
                    fcmTopic = loginList.fcmTopic,
                    mqttTopic = loginList.mqttTopic,
                    authToken = loginList.authToken,
                    willTopic = loginList.willTopic,
                    status = loginList.status
                )
            )
        }
        return UsersListDataDetails(
            data = usersList
        )
    }
}