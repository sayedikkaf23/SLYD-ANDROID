package com.kotlintestgradle.data.repository

import com.kotlintestgradle.data.DataSource
import com.kotlintestgradle.data.mapper.ParticipantsResponseMapper
import com.kotlintestgradle.data.mapper.PostCallMapper
import com.kotlintestgradle.data.preference.PreferenceManager
import com.kotlintestgradle.interactor.calling.AnswerCallUseCase
import com.kotlintestgradle.interactor.calling.DisconnectCallUseCase
import com.kotlintestgradle.interactor.calling.InviteMembersToCallUseCase
import com.kotlintestgradle.interactor.calling.PostCallUseCase
import com.kotlintestgradle.interactor.dashboard.GetParticipantsInCallUseCase
import com.kotlintestgradle.model.PostCallRecord
import com.kotlintestgradle.remote.model.request.PostCallRequest
import com.kotlintestgradle.repository.CallingRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author 3Embed
 *
 * This repository is responsible for
 * calling the loginApi
 * getting the response and mapping the data
 *
 * injecting data source for dataBase
 *
 * @since 22-Aug-2019
 */
open class CallingRepositoryImpl @Inject constructor(private val dataSource: DataSource) :
        BaseRepository(dataSource), CallingRepository {
    private val participantsResponseMapper =
            ParticipantsResponseMapper() // mapper cal for participants api
    override fun inviteMembers(
            callId: String,
            toUser: String
    ): Single<InviteMembersToCallUseCase.ResponseValues> {
        val preference: PreferenceManager = dataSource.preference()
        return dataSource.api()
                .handler()
                .inviteMembers(
                        preference.languageCode, preference.authToken, callId, toUser
                )
                .flatMap { Single.just(InviteMembersToCallUseCase.ResponseValues(postCallMapper.map(it))) }
    }

    private val postCallMapper = PostCallMapper()
    /**
     * Used to call loginApi
     * @param loginRecord - all the data for calling logon api
     * @return response form login api
     */
    override fun postCall(callRequestRecord: PostCallRecord): Single<PostCallUseCase.ResponseValues> {
        val preference: PreferenceManager = dataSource.preference()
        return dataSource.api()
                .handler()
                .postCall(
                        preference.languageCode, preference.authToken, PostCallRequest(
                        callRequestRecord.type,
                        callRequestRecord.room,
                        callRequestRecord.to,
                        callRequestRecord.sessionId
                )
                )
                .flatMap { Single.just(PostCallUseCase.ResponseValues(postCallMapper.map(it))) }
    }

    /**
     * Used to call loginApi
     * @param loginRecord - all the data for calling logon api
     * @return response form login api
     */
    override fun answerCall(callId: String,sessionId: String): Single<AnswerCallUseCase.ResponseValues> {
        val preference: PreferenceManager = dataSource.preference()
        return dataSource.api()
                .handler()
                .answerCall(
                        preference.languageCode, preference.authToken, callId,sessionId)
                .flatMap { Single.just(AnswerCallUseCase.ResponseValues(postCallMapper.map(it))) }
    }

    /**
     * Used to call loginApi
     * @param loginRecord - all the data for calling logon api
     * @return response form login api
     */
    override fun disconnectCall(
            callId: String,
            disconnectType: String
    ): Single<DisconnectCallUseCase.ResponseValues> {
        val preference: PreferenceManager = dataSource.preference()
        return dataSource.api()
                .handler()
                .disconnectCall(
                        preference.languageCode, preference.authToken, callId, disconnectType
                )
                .flatMap { Single.just(DisconnectCallUseCase.ResponseValues(postCallMapper.map(it))) }
    }

    /**
     * used to get the participants in call
     * @param callId call id of that call
     * @return response from api
     */
    override fun getParticipantsInCall(callId: String): Single<GetParticipantsInCallUseCase.ResponseValues> {
        val preference: PreferenceManager = dataSource.preference()
        return dataSource.api()
                .handler()
                .getParticipantsInCall(
                        preference.languageCode, preference.authToken, callId
                )
                .flatMap {
                    Single.just(
                            GetParticipantsInCallUseCase.ResponseValues(
                                    participantsResponseMapper.map(it)
                            )
                    )
                }
    }
}