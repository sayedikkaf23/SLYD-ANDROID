package com.kotlintestgradle.interactor

import io.reactivex.Single

/**
 * @author 3Embed
 *
 * for single use case
 *
 * @since 1.0 (23-Aug-2019)
 */
interface SingleUsecase<REQ : UseCase.RequestValues, RES : UseCase.ResponseValue> {
    fun buildUseCaseSingle(requestValues: REQ? = null): Single<RES>
}