package com.kotlintestgradle.interactor.calling

import com.kotlintestgradle.executor.PostExecutionThread
import com.kotlintestgradle.executor.ThreadExecutor
import com.kotlintestgradle.interactor.UseCase
import com.kotlintestgradle.model.CallDetails
import com.kotlintestgradle.model.PostCallRecord
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
class PostCallUseCase @Inject constructor(
    private val callingRepository: CallingRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : UseCase<PostCallUseCase.RequestValues, PostCallUseCase.ResponseValues>(
    threadExecutor, postExecutionThread
) {
    // call api
    override fun buildUseCaseObservable(requestValues: RequestValues?): Single<ResponseValues> {
        return callingRepository.postCall(requestValues!!.postCallRecord)
    }

    /**
     * values we have to send to api
     * @param loginRecord details for login
     */
    data class RequestValues(
        val postCallRecord: PostCallRecord
    ) : UseCase.RequestValues

    /**
     * map response we get from api
     * @param callDetails response for api
     */
    data class ResponseValues(val callDetails: CallDetails) : ResponseValue
}