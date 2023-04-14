package com.kotlintestgradle.interactor.calling

import com.kotlintestgradle.executor.PostExecutionThread
import com.kotlintestgradle.executor.ThreadExecutor
import com.kotlintestgradle.interactor.UseCase
import com.kotlintestgradle.model.CallDetails
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
class AnswerCallUseCase @Inject constructor(
    private val callingRepository: CallingRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : UseCase<AnswerCallUseCase.RequestValues, AnswerCallUseCase.ResponseValues>(
    threadExecutor, postExecutionThread
) {
    // call api
    override fun buildUseCaseObservable(requestValues: RequestValues?): Single<ResponseValues> {
        return callingRepository.answerCall(requestValues!!.callId, requestValues.sessionId)
    }

    /**
     * values we have to send to api
     * @param loginRecord details for login
     */
    data class RequestValues(
            val callId: String,
            val sessionId: String
    ) : UseCase.RequestValues

    /**
     * map response we get from api
     * @param callDetails response for api
     */
    data class ResponseValues(val callDetails: CallDetails) : ResponseValue
}