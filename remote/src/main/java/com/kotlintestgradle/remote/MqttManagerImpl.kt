package com.kotlintestgradle.remote

//import com.kotlintestgradle.remote.MQtt.CustomMQtt.*
//import com.kotlintestgradle.remote.MQtt.MqttAndroidClient
import android.content.Context
import com.google.gson.Gson
import com.kotlintestgradle.remote.model.response.calling.CallerDetailsResponse
import com.kotlintestgradle.remote.util.CallStatus
import com.kotlintestgradle.remote.util.Constants.Companion.ONE
import com.kotlintestgradle.remote.util.Constants.Companion.QUADRA
import com.kotlintestgradle.remote.util.Constants.Companion.THRICE
import com.kotlintestgradle.remote.util.Constants.Companion.TWO
import io.reactivex.Observable
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.IllegalArgumentException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.UnrecoverableKeyException
import java.security.cert.CertificateException

/**
 * @author 3Embed
 *used to implement network manager
 * @since 1.0 (23-Aug-2019)
 */
class MqttManagerImpl(val context: Context) : MqttManager {
    private lateinit var mqttConnectOptions: MqttConnectOptions
    private lateinit var mqttAndroidClient: MqttAndroidClient

    override fun startMqttConnection(
            userId: String?,
            willTopic: String?
    ): Observable<String> {
        return Observable.create { subscriber ->
            if (!::mqttAndroidClient.isInitialized) {
                val mqttId = "${userId}_${System.currentTimeMillis()}"
                mqttAndroidClient = MqttAndroidClient(context, BuildConfig.MQTT_URL, mqttId)
                try {
                    println("$TAG MQTT trying to connect: $mqttId")
                    mqttAndroidClient.connect(
                            mqttConfiguration(userId, willTopic),
                            null,
                            object : IMqttActionListener {
                                override fun onSuccess(asyncActionToken: IMqttToken) {
                                    println("$TAG MQTT onSuccess to connect to: $mqttId")
                                    val disconnectedBufferOptions = DisconnectedBufferOptions()
                                    disconnectedBufferOptions.isBufferEnabled = true
                                    disconnectedBufferOptions.bufferSize = 100
                                    disconnectedBufferOptions.isPersistBuffer = false
                                    disconnectedBufferOptions.isDeleteOldestMessages = false
                                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions)
                                    subscriber.onNext("success")
                                    listenMqttData(userId)
                                }

                                override fun onFailure(
                                        asyncActionToken: IMqttToken,
                                        exception: Throwable
                                ) {
                                    try {
                                        println("$TAG MQTT Failed to connect to :$exception")
                                        exception.cause?.let { subscriber.onError(it) }
                                    } catch (ex: IllegalArgumentException) {
                                    }
                                }
                            })
                } catch (ex: MqttException) {
                    ex.cause?.let { subscriber.onError(it) }
                } catch (ex: IllegalArgumentException) {
                    ex.cause?.let { subscriber.onError(it) }
                }
            } else {
                subscriber.onNext("success")
            }
        }
    }

    override fun subscribeMqttTopic(
            mqttTopic: String
    ): Observable<String> {
        return Observable.create { subscriber ->
            try {
                mqttAndroidClient.subscribe(mqttTopic, 0, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken) {
                        println("$TAG MQTT Subscribed! $mqttTopic")
                        subscriber.onNext("success")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                        println("$TAG MQTT Failed to subscribe")
                        exception.cause?.let { subscriber.onError(it) }
                    }
                })
            } catch (ex: MqttException) {
                ex.cause?.let { subscriber.onError(it) }
            }
        }
    }

    override fun unSubscribeMqttTopic(
            mqttTopic: String
    ): Observable<String> {
        return Observable.create { subscriber ->
            try {
                if (::mqttAndroidClient.isInitialized)
                    mqttAndroidClient.unsubscribe(mqttTopic)
                subscriber.onNext("success")
            } catch (ex: MqttException) {
                ex.cause?.let { subscriber.onError(it) }
            }
        }
    }

    /**
     * used to listen the data received by MQTT
     */
    override fun listenMqttData(userId: String?) {
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                if (reconnect) {
                    mqttAndroidClient.publish("clientConnected", userId?.toByteArray(), 2, false)
                    println("$TAG MQTT Reconnected to : $serverURI")
                } else {
                    println("$TAG MQTT Connected to: $serverURI")
                }
            }

            override fun connectionLost(exception: Throwable?) {
                if (exception != null) {
                    println("$TAG MQTT The Connection was lost. ${exception.printStackTrace()}")
                }
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                println("$TAG MQTT Incoming message: " + String(message.payload))
                val jsonObject = JSONObject(String(message.payload))
                val dataObject = jsonObject.getJSONObject("data")
                val gson = Gson()
                val callerDetailsResponse =
                        gson.fromJson(dataObject.toString(), CallerDetailsResponse::class.java)
                when (callerDetailsResponse.action) {
                    ONE -> {
                        MqttSubscriptionInteractor.postData(
                                Pair(
                                        CallStatus.NEW_CALL,
                                        callerDetailsResponse
                                )
                        )
                    } // new call sent
                    THRICE -> {
                        MqttSubscriptionInteractor.postData(
                                Pair(
                                        CallStatus.CALLING,
                                        callerDetailsResponse.userId
                                )
                        )
                    } // call received
                    TWO -> {
                        MqttSubscriptionInteractor.postData(
                                Pair(
                                        CallStatus.SINGLE_USER_LEAVE,
                                        callerDetailsResponse.userId
                                )
                        )
                    } // single user left the call
                    QUADRA -> {
                        MqttSubscriptionInteractor.postData(
                                Pair(
                                        CallStatus.DISCONNECT,
                                        callerDetailsResponse
                                )
                        )
                    } // call disconnect
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
            }
        })
    }

    override fun disconnectMqtt(isReconnectAllow: Boolean) {

        try {
            if (::mqttConnectOptions.isInitialized) {
                mqttConnectOptions.isAutomaticReconnect = isReconnectAllow;
                println("$TAG MQTT CLOSE1")
            }
            if (::mqttAndroidClient.isInitialized) {
                mqttAndroidClient.disconnect()
                println("$TAG MQTT CLOSE2")
            }
            println("$TAG MQTT CLOSE3")
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    /**
     * configuration for MQTT
     */
    private fun mqttConfiguration(userId: String?, willTopic: String?): MqttConnectOptions {
        mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = true

        /* Use below code of socket factory when ssl required in MQTT*/
//        val socketFactoryOptions = SocketFactory.SocketFactoryOptions()
//        try {
//            socketFactoryOptions.withCaInputStream(context.getResources().openRawResource(R.raw.dochat_mqtt))
//            //socketFactoryOptions.withClientP12Password("");
//            //socketFactoryOptions.withClientP12InputStream(getResources().openRawResource(R.raw.dochat_mqtt));
//            mqttConnectOptions.socketFactory = SocketFactory(socketFactoryOptions)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } catch (e: NoSuchAlgorithmException) {
//            e.printStackTrace()
//        } catch (e: KeyStoreException) {
//            e.printStackTrace()
//        } catch (e: KeyManagementException) {
//            e.printStackTrace()
//        } catch (e: UnrecoverableKeyException) {
//            e.printStackTrace()
//        } catch (e: CertificateException) {
//            e.printStackTrace()
//        }
        //------------------------------end------------------------------//

        mqttConnectOptions.userName = BuildConfig.MQTT_USERNAME
        mqttConnectOptions.password = BuildConfig.MQTT_PASSWORD.toCharArray()
        mqttConnectOptions.keepAliveInterval = 15
        mqttConnectOptions.setWill("lastWill", userId!!.toByteArray(), 1, true)
        return mqttConnectOptions
    }

    override fun publishMessage(mqttTopic: String, payLoad: String): Observable<String> {
        return Observable.create { subscriber ->
            try {
                val message = MqttMessage()
                message.payload = payLoad.toByteArray()
                mqttAndroidClient.publish(mqttTopic, message)
                println("$TAG MQTT Message Published")
                if (!mqttAndroidClient.isConnected) {
                    println(mqttAndroidClient.bufferedMessageCount.toString() + " messages in buffer.")
                }
                subscriber.onNext("success")
            } catch (exception: MqttException) {
                println("$TAG MQTT Error Publishing: " + exception.message)
                exception.cause?.let { subscriber.onError(it) }
            }
        }
    }

    companion object {
        private val TAG = MqttManagerImpl::class.java

    }
}