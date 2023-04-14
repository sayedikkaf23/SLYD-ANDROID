package com.kotlintestgradle.data.mapper

import com.kotlintestgradle.model.CallDetails
import com.kotlintestgradle.remote.model.response.calling.CallingResponse
import javax.inject.Inject

/**
 * @author 3Embed
 *
 * used to map login data what we get from login response
 *
 * @since 1.0 (23-Aug-2019)
 */
class PostCallMapper @Inject constructor() {
    fun map(response: CallingResponse): CallDetails {
        return CallDetails(
            callId = response.callId
        )
    }
}