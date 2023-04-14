package com.kotlintestgradle.remote

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Observable
import io.reactivex.Single

/**
 * @author 3Embed
 *used to implement network manager
 * @since 1.0 (23-Aug-2019)
 */
class FcmManagerImpl : FcmManager {
    override fun initializeFirebase(): Single<String> {
        return Single.create { subscriber ->
            // Get token
            // [START retrieve_current_token]
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG.toString(), "getInstanceId failed", task.exception)
                        task.exception!!.cause?.let { subscriber.onError(it) }
                        return@OnCompleteListener
                    }
                    // Get new Instance ID token
                    val token = task.result?.token
                    // Log and toast
                    val msg = "token"
                    subscriber.onSuccess("token success")
                    Log.d(TAG.toString(), msg)
                })
            // [END retrieve_current_token]
        }
    }

    override fun subscribeUserTopic(
        fcmTopic: String
    ): Observable<String> {
        return Observable.create { subscriber ->
            FirebaseMessaging.getInstance()
                .subscribeToTopic(fcmTopic)
                .addOnCompleteListener { task ->
                    println("$TAG : is fcm topic subscribed ${task.isSuccessful}")
                    if (task.isSuccessful) {
                        subscriber.onNext("success")
                    } else {
                        task.exception!!.cause?.let { subscriber.onError(it) }
                    }
                }
        }
    }

    companion object {
        private val TAG = FcmManagerImpl::class.java
    }
}