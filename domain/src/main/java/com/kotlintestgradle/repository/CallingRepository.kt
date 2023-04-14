package com.kotlintestgradle.repository

import com.kotlintestgradle.interactor.calling.AnswerCallUseCase
import com.kotlintestgradle.interactor.calling.DisconnectCallUseCase
import com.kotlintestgradle.interactor.calling.InviteMembersToCallUseCase
import com.kotlintestgradle.interactor.calling.PostCallUseCase
import com.kotlintestgradle.interactor.dashboard.GetParticipantsInCallUseCase
import com.kotlintestgradle.model.PostCallRecord
import io.reactivex.Single

/**
 * @author 3Embed
 *
 *to call api
 *
 * @since 1.0 (23-Aug-2019)
 */
interface CallingRepository {
    fun postCall(callRequestRecord: PostCallRecord): Single<PostCallUseCase.ResponseValues>
    fun answerCall(callId: String, sessionId: String): Single<AnswerCallUseCase.ResponseValues>
    fun inviteMembers(
        callId: String,
        toUser: String
    ): Single<InviteMembersToCallUseCase.ResponseValues>

    fun disconnectCall(
        callId: String,
        disconnectType: String
    ): Single<DisconnectCallUseCase.ResponseValues>

    fun getParticipantsInCall(callId: String): Single<GetParticipantsInCallUseCase.ResponseValues>
}