package com.kotlintestgradle.remote

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.observables.ConnectableObservable

object MqttSubscriptionInteractor {
    val observable: ConnectableObservable<Pair<*, *>>
    private var emitor: ObservableEmitter<Pair<*, *>>? = null

    init {
        val observable = Observable.create<Pair<*, *>> { e -> emitor = e }
        this.observable = observable.publish()
        this.observable.share()
        this.observable.replay()
        this.observable.connect()
    }

    fun postData(flag: Pair<*, *>) {
        if (emitor != null) emitor!!.onNext(flag)
    }
}
