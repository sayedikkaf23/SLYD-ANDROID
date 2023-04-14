package com.kotlintestgradle.data.repository

import com.kotlintestgradle.data.DataSource
import com.kotlintestgradle.data.preference.PreferenceManager
import com.kotlintestgradle.remote.MqttManager
import com.kotlintestgradle.repository.MqttRepository
import io.reactivex.Observable
import javax.inject.Inject

class MqttRepositoryImpl @Inject constructor(dataSource: DataSource) :
    BaseRepository(dataSource), MqttRepository {
    private val mqtt: MqttManager = dataSource.mqtt()
    private val preference: PreferenceManager = dataSource.preference()

    override fun startMqttServer(): Observable<String> {
        return mqtt.startMqttConnection(preference.userId, preference.userId)
    }

    override fun subscribeMqttTopic(mqttTopic: String): Observable<String> {
        return mqtt.subscribeMqttTopic(mqttTopic)
    }

    override fun unSubscribeMqttTopic(mqttTopic: String): Observable<String> {
        return mqtt.unSubscribeMqttTopic(mqttTopic)
    }

    override fun listenMqttData(userId: String?) {
        return mqtt.listenMqttData(userId)
    }

    override fun disconnectMqtt(isReconnectAllow: Boolean) {
        return mqtt.disconnectMqtt(isReconnectAllow)
    }

    override fun publishMessage(mqttTopic: String, payLoad: String): Observable<String> {
        return mqtt.publishMessage(mqttTopic, payLoad)
    }
}