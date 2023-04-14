package com.jio.consumer.domain.interactor.user.handler

import com.kotlintestgradle.repository.MqttRepository
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by 3Embed on 29/03/19.
 */
class MqttHandlerImpl @Inject constructor(private var mqttRepository: MqttRepository) :
    MqttHandler {
    override fun startMqttConnection(): Observable<String> {
        return mqttRepository.startMqttServer()
    }

    override fun subscribeMqttTopic(mqttTopic: String): Observable<String> {
        return mqttRepository.subscribeMqttTopic(mqttTopic)
    }

    override fun unSubscribeMqttTopic(mqttTopic: String): Observable<String> {
        return mqttRepository.unSubscribeMqttTopic(mqttTopic)
    }

    override fun publishMessage(mqttTopic: String, payLoad: String): Observable<String> {
        return mqttRepository.publishMessage(mqttTopic, payLoad)
    }

    override fun listenForData(userId: String?) {
        return mqttRepository.listenMqttData(userId)
    }

    override fun disconnectMqtt(isReconnectAllow: Boolean) {
        return mqttRepository.disconnectMqtt(isReconnectAllow)
    }
}