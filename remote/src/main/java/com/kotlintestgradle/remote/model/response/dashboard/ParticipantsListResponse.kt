package com.kotlintestgradle.remote.model.response.dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kotlintestgradle.remote.core.ValidItem
import com.kotlintestgradle.remote.model.response.login.LoginResponse

/**
 * @author 3Embed
 *
 * model class for participants api response
 *
 * @since 1.0 (23-Aug-2019)
 */
data class ParticipantsListResponse(
    /*"message":"Login Success.",
  "data":{}*/
    @SerializedName("users") @Expose val participantsList: List<LoginResponse>
) : ValidItem {
    override fun isValid(): Boolean {
        return true
    }
}