package com.kotlintestgradle.interactor.calling

import com.kotlintestgradle.CallDisconnectType
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
class DisconnectCallUseCase @Inject constructor(
    private val callingRepository: CallingRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : UseCase<DisconnectCallUseCase.RequestValues, DisconnectCallUseCase.ResponseValues>(
    threadExecutor, postExecutionThread
) {
    // call api
    override fun buildUseCaseObservable(requestValues: RequestValues?): Single<ResponseValues> {
        return callingRepository.disconnectCall(
            requestValues!!.callId,
            requestValues.disconnectType.tag
        )
    }

    /**
     * values we have to send to api
     * @param loginRecord details for login
     */
    data class RequestValues(
        val callId: String,
        val disconnectType: CallDisconnectType
    ) : UseCase.RequestValues

    /**
     * map response we get from api
     * @param callDetails response for api
     */
    data class ResponseValues(val callDetails: CallDetails) : ResponseValue
}