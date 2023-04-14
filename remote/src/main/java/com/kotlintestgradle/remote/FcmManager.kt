package com.kotlintestgradle.remote

import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author 3Embed
 *used for mqtt manager
 * @since 1.0 (17-Dec-2019)
 */
interface FcmManager {
    fun initializeFirebase(): Single<String>
    fun subscribeUserTopic(fcmTopic: String): Observable<String>
}