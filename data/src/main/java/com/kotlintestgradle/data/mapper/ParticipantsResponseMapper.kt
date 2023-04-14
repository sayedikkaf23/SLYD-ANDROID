package com.kotlintestgradle.data.mapper

import com.kotlintestgradle.model.LoginDataDetails
import com.kotlintestgradle.model.ParticipantsListDataDetails
import com.kotlintestgradle.model.PhoneData
import com.kotlintestgradle.remote.model.response.dashboard.ParticipantsListResponse
import javax.inject.Inject

/**
 * @author 3Embed
 *
 * used to map login data what we get from login response
 *
 * @since 1.0 (23-Aug-2019)
 */
class ParticipantsResponseMapper @Inject constructor() {
    fun map(response: ParticipantsListResponse): ParticipantsListDataDetails {
        val usersList = arrayListOf<LoginDataDetails>()
        for (loginList in response.participantsList) {
            usersList.add(
                LoginDataDetails(
                    id = loginList.id,
                    email = "",
                    firstName = "",
                    lastName = "",
                    phone = PhoneData("", ""),
                    profilePic = "",
                    fcmTopic = "",
                    mqttTopic = "",
                    authToken = "",
                    willTopic = "",
                    status = ""
                )
            )
        }
        return ParticipantsListDataDetails(
            users = usersList
        )
    }
}