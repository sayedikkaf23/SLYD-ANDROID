package com.jio.consumer.domain.interactor

import com.kotlintestgradle.interactor.UseCase
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver

/**
 * @author 3Embed
 *
 * execute and observe api response
 *
 * @since 1.0 (23-Aug-2019)
 */
interface ObservableUsecase<REQ : UseCase.RequestValues, RES : UseCase.ResponseValue> {
    fun execute(disposableObserver: DisposableObserver<RES>, requestValues: REQ): Observable<RES>
}