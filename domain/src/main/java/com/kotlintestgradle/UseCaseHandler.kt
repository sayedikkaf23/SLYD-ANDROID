package com.kotlintestgradle

import com.jio.consumer.domain.interactor.ObservableUsecase
import com.kotlintestgradle.interactor.UseCase
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

/**
 * @author 3Embed
 *
 *to manage request and respose of api
 *
 * @since 1.0 (23-Aug-2019)
 */
class UseCaseHandler @Inject constructor() {
    fun <REQ : UseCase.RequestValues, RES : UseCase.ResponseValue> execute(
        useCase: UseCase<REQ, RES>,
        values: REQ,
        singleObserver: DisposableSingleObserver<RES>
    ) {
        useCase.requestValues = values
        useCase.execute(singleObserver)
    }

    fun <REQ : UseCase.RequestValues, RES : UseCase.ResponseValue> execute(
        useCase: UseCase<REQ, RES>,
        values: REQ,
        singleObserver: DisposableSingleObserver<RES>,
        scheduler: Scheduler
    ) {
        useCase.requestValues = values
        useCase.execute(singleObserver, scheduler)
    }

    fun <REQ : UseCase.RequestValues, RES : UseCase.ResponseValue> execute(
        useCase: ObservableUsecase<REQ, RES>,
        values: REQ,
        disposableObserver: DisposableObserver<RES>
    ) {
        useCase.execute(disposableObserver, values)
    }

    companion object {
        private var INSTANCE: UseCaseHandler? = null
        val getInstance: UseCaseHandler
            get() {
                if (INSTANCE == null) {
                    INSTANCE = UseCaseHandler()
                }
                return INSTANCE!!
            }
    }
}