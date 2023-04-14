package com.kotlintestgradle.repository

import io.reactivex.Observable

/**
 * @author 3Embed
 *
 *to call api
 *
 * @since 1.0 (23-Aug-2019)
 */
interface MqttRepository {
    fun startMqttServer(): Observable<String>
    fun subscribeMqttTopic(mqttTopic: String): Observable<String>
    fun unSubscribeMqttTopic(mqttTopic: String): Observable<String>
    fun publishMessage(mqttTopic: String, payLoad: String): Observable<String>
    fun listenMqttData(userId: String?)
    fun disconnectMqtt(isReconnectAllow: Boolean)
}