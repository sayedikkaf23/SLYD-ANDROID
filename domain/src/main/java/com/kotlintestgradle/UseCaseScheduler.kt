package com.kotlintestgradle

/**
 * @author 3Embed
 *
 *
 * @since 1.0 (23-Aug-2019)
 */
interface UseCaseScheduler {
    fun execute(runnable: Runnable)
    // fun <V : UseCase.ResponseValue> notifyResponse(response: V,
    //                                               useCaseCallback: UseCase.UseCaseCallback<V>)
    // fun <V : UseCase.ResponseValue> onError(
    //        useCaseCallback: UseCase.UseCaseCallback<V>)
}