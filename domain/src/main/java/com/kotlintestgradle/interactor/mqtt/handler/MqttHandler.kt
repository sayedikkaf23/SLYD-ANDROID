package com.jio.consumer.domain.interactor.user.handler

import io.reactivex.Observable

/**
 * Created by 3Embed on 29/03/19.
 */
interface MqttHandler {
    fun startMqttConnection(): Observable<String>
    fun subscribeMqttTopic(mqttTopic: String): Observable<String>
    fun unSubscribeMqttTopic(mqttTopic: String): Observable<String>
    fun publishMessage(mqttTopic: String, payLoad: String): Observable<String>
    fun listenForData(userId: String?)
    fun disconnectMqtt(isReconnectAllow: Boolean)
}