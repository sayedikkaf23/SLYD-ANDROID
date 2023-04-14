package com.kotlintestgradle.interactor

import com.kotlintestgradle.executor.PostExecutionThread
import com.kotlintestgradle.executor.ThreadExecutor
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

/**
 * @author 3Embed
 *
 * execute data receives for api response
 *
 * @since 1.0 (23-Aug-2019)
 */
abstract class UseCase<REQ : UseCase.RequestValues, RES : UseCase.ResponseValue> constructor(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) {
    private val disposables = CompositeDisposable()

    protected abstract fun buildUseCaseObservable(requestValues: REQ? = null): Single<RES>

    open fun execute(singleObserver: DisposableSingleObserver<RES>) {
        val single = this.buildUseCaseObservable(requestValues).subscribeOn(
            Schedulers.from(threadExecutor)
        ).observeOn(postExecutionThread.scheduler) as Single<RES>
        addDisposable(single.subscribeWith(singleObserver))
    }

    open fun execute(disposableObserver: DisposableObserver<RES>) {
        val observable = this.buildUseCaseObservable(requestValues).subscribeOn(
            Schedulers.from(threadExecutor)
        ).observeOn(postExecutionThread.scheduler) as Observable<RES>
        addDisposable(observable.subscribeWith(disposableObserver))
    }

    open fun execute(singleObserver: DisposableSingleObserver<RES>, scheduler: Scheduler) {
        val single = this.buildUseCaseObservable(requestValues).subscribeOn(
            Schedulers.from(threadExecutor)
        ).observeOn(postExecutionThread.scheduler) as Single<RES>
        addDisposable(single.subscribeWith(singleObserver))
    }

    // single
    open fun execute(singleObserver: DisposableSingleObserver<RES>, single: Single<RES>) {
        addDisposable(single.compose(applySingleExecutorSchedulers()).subscribeWith(singleObserver))
    }

    open fun execute(
        disposableObserver: DisposableObserver<RES>,
        observable: Observable<RES>
    ): Observable<RES> {
        addDisposable(
            observable.compose(applyObservableExecutorSchedulers()).subscribeWith(
                disposableObserver
            )
        )
        return observable
    }

    private fun <T> applySingleExecutorSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(Schedulers.from(threadExecutor)).observeOn(postExecutionThread.scheduler)
        }
    }

    private fun <T> applyObservableExecutorSchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.from(threadExecutor)).observeOn(postExecutionThread.scheduler)
        }
    }

    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    var requestValues: REQ? = null
    var useCaseCallback: UseCaseCallback<RES>? = null

    internal fun run() {
        executeUseCase(requestValues)
    }

    open fun executeUseCase(requestValues: REQ?) {
    }

    /**
     * Data passed to a request.
     */
    interface RequestValues

    /**
     * Data received from a request.
     */
    interface ResponseValue

    interface UseCaseCallback<R> {
        fun onSuccess(response: R)
        fun onError()
    }
}