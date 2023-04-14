package com.kotlintestgradle.remote

import io.reactivex.Observable

/**
 * @author 3Embed
 *used for mqtt manager
 * @since 1.0 (17-Dec-2019)
 */
interface MqttManager {
    fun startMqttConnection(userId: String?, willTopic: String?): Observable<String>
    fun subscribeMqttTopic(mqttTopic: String): Observable<String>
    fun unSubscribeMqttTopic(mqttTopic: String): Observable<String>
    fun publishMessage(mqttTopic: String, payLoad: String): Observable<String>
    fun listenMqttData(userId: String?)
    fun disconnectMqtt(isReconnectAllow: Boolean)
}