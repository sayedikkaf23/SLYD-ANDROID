package com.kotlintestgradle.interactor.dashboard

import com.kotlintestgradle.executor.PostExecutionThread
import com.kotlintestgradle.executor.ThreadExecutor
import com.kotlintestgradle.interactor.UseCase
import com.kotlintestgradle.model.ParticipantsListDataDetails
import com.kotlintestgradle.repository.CallingRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author 3Embed
 *
 *A use case is a single purpose class that contains one public method execute()
 * used to retrive data from api
 *
 * @since 1.0 (23-Aug-2019)
 */
class GetParticipantsInCallUseCase @Inject constructor(
        private val callingRepository: CallingRepository,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
) : UseCase<GetParticipantsInCallUseCase.RequestValues, GetParticipantsInCallUseCase.ResponseValues>(
    threadExecutor, postExecutionThread
) {
    // call api
    override fun buildUseCaseObservable(requestValues: RequestValues?): Single<ResponseValues> {
        return callingRepository.getParticipantsInCall(requestValues!!.callId)
    }

    /**
     * values we have to send to api
     * @param loginRecord details for login
     */
    data class RequestValues(
        val callId: String
    ) : UseCase.RequestValues

    /**
     * map response we get from api
     * @param loginDataDetails response for api
     */
    data class ResponseValues(val participantsListDataDetails: ParticipantsListDataDetails) :
        ResponseValue
}