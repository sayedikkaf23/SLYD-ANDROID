package com.kotlintestgradle.data.interactor

import io.reactivex.Observable
import io.reactivex.Observer

class SessionExpireObserver : Observable<String>() {
    override fun subscribeActual(observer: Observer<in String>?) {
        this.observer = observer
    }

    private var observer: Observer<in String>? = null
    /**
     * <h2>publishData</h2>
     * This method is used to publish the data for network
     * @param data data to be pushed
     */
    fun publishData(data: String) {
        if (observer != null) {
            observer!!.onNext(data)
        }
    }

    companion object {
        private var INSTANCE: SessionExpireObserver? = null
        val getInstance: SessionExpireObserver
            get() {
                if (INSTANCE == null) {
                    INSTANCE = SessionExpireObserver()
                }
                return INSTANCE!!
            }
    }
}