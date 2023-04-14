package com.appscrip.video.call

import android.annotation.TargetApi
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import chat.hola.com.app.AppController
import chat.hola.com.app.Utilities.Constants
import com.appscrip.myapplication.utility.Constants.Companion.CALL_TOPIC
import com.appscrip.myapplication.utility.Constants.Companion.NO_VALUE
import com.appscrip.myapplication.utility.Utility
import com.appscrip.video.UiAction
import com.ezcall.android.R
import com.google.gson.JsonParseException
import com.jio.consumer.domain.interactor.user.handler.MqttHandler
import com.jio.consumer.domain.interactor.user.handler.UserHandler
import com.kotlintestgradle.CallDisconnectType
import com.kotlintestgradle.UseCaseHandler
import com.kotlintestgradle.interactor.calling.AnswerCallUseCase
import com.kotlintestgradle.interactor.calling.DisconnectCallUseCase
import com.kotlintestgradle.interactor.calling.InviteMembersToCallUseCase
import com.kotlintestgradle.interactor.calling.PostCallUseCase
import com.kotlintestgradle.interactor.dashboard.GetParticipantsInCallUseCase
import com.kotlintestgradle.model.PostCallRecord
import com.kotlintestgradle.remote.MqttSubscriptionInteractor
import com.kotlintestgradle.remote.model.response.calling.CallerDetailsResponse
import com.kotlintestgradle.remote.util.CallStatus
import com.kotlintestgradle.remote.util.Constants.Companion.ONE
import com.kotlintestgradle.remote.util.Constants.Companion.THRICE
import com.kotlintestgradle.remote.util.Constants.Companion.TWO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject


class CallingViewModel @Inject constructor(
        private var handler: UseCaseHandler,
        private var postCallUseCase: PostCallUseCase,
        private var inviteMembersToCallUseCase: InviteMembersToCallUseCase,
        private var answerCallUseCase: AnswerCallUseCase,
        private var diconnectCallUseCase: DisconnectCallUseCase,
        private var userHandler: UserHandler,
        private var mqttHandler: MqttHandler,
        private val getParticipantsInCallUseCase: GetParticipantsInCallUseCase,
        private val context: Context
) : ViewModel() {
    private lateinit var sessionId: String
    public var audioManager:AudioManager
    var callerName: ObservableField<String>? = null
    private val remoteSubscriptionLiveData = MutableLiveData<Boolean>()
    private val inviteCallingLiveData = MutableLiveData<CallerDetailsResponse>()
    private val postErrorLiveData = MutableLiveData<String>()
    private val disconnectCallLiveData =
            MutableLiveData<Pair<Boolean, String>>() // true if to end the call ,false if to end single user call
    private val subscribeMqttTopicLiveData =
            MutableLiveData<Pair<Boolean, String>>() // true if successful else false, string is callid
    lateinit var callId: String
    private val getParticipantsLiveData = MutableLiveData<Pair<*, *>>()
    private val uiElementsLiveData = MutableLiveData<UiAction>()
    var isCallReceived: Boolean = false
    var isAnswered: Boolean = false
    private val disposable = CompositeDisposable()

    private val answerLiveData =
            MutableLiveData<Boolean>() // true if to end the call ,false if to end single user call

    private lateinit var r: Ringtone
    /* Media Player Class to enable ring sound */
    public lateinit var  mediaPlayer: MediaPlayer

    init {
        callerName = ObservableField("")
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager;
    }

    companion object {
        private val TAG = CallingViewModel::class.java
    }

    /**
     * postCallAPi
     * used to place a call to user
     * @return response of login api
     */
    fun postCallAPI(userID: String, userName: String, roomId: Long, callType: String, sessionId: String) {
        println(" token stored " + AppController.getInstance().apiToken)
        this.callerName!!.set(userName)
        this.sessionId=sessionId
        val callingIdsList = ArrayList<String>()
        callingIdsList.add(userID)
        val disposableSingleObserver =
                object : DisposableSingleObserver<PostCallUseCase.ResponseValues>() {
                    override fun onSuccess(responseValues: PostCallUseCase.ResponseValues) {
                        // getting response from api if it is successful
                        println("call Id " + responseValues.callDetails.callId)
                        callId = responseValues.callDetails.callId
                        connectMQTT()
                    }

                    override fun onError(e: Throwable) {
                        // if there is any error in response
                        disconnectCallLiveData.postValue(Pair(true, userName+" "+context.getString(R.string.calling_user_busy)))// finish ui on error
                        println("error in placing call ${e.localizedMessage}")

                        when(e.localizedMessage){
                            "409" -> postErrorLiveData.postValue(userName+" "+context.getString(R.string.calling_user_busy))
                            "411" -> postErrorLiveData.postValue(userName+" "+context.getString(R.string.calling_user_busy))
                            else -> {
                                postErrorLiveData.postValue(e.localizedMessage)
                            }
                        }

                    }
                }
        // calling the api through UseCase
        handler.execute(
                postCallUseCase,
                PostCallUseCase.RequestValues(
                        PostCallRecord(
                                callType, roomId.toString(), callingIdsList,sessionId
                        )
                ),
                disposableSingleObserver
        )
    }

    /**
     *get users which are in call
     */
    private fun getParticipantsInCall() {
        val disposableSingleObserver =
                object : DisposableSingleObserver<GetParticipantsInCallUseCase.ResponseValues>() {
                    override fun onSuccess(responseValues: GetParticipantsInCallUseCase.ResponseValues) {
                        // getting response from api if it is successful
                        Log.i(TAG.simpleName,"print get successs")
                        if (responseValues.participantsListDataDetails.users.size != NO_VALUE) {
                            //if (!Utility.isCallActive(context)) {
                                connectMQTT()
//                            } else {
//                                disconnectCall(THRICE, true)
//                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        // if there is any error in response
                        Log.i(TAG.simpleName,"print get error ${e.localizedMessage}")
                        getParticipantsLiveData.postValue(Pair(false, e.localizedMessage))
                        e.printStackTrace()
                    }
                }
        // calling the api through UseCase
        handler.execute(
                getParticipantsInCallUseCase,
                GetParticipantsInCallUseCase.RequestValues(callId),
                disposableSingleObserver
        )
    }

    fun defineCallReceived(callReceived: Boolean) {
        isCallReceived = callReceived
    }

    /**
     * invite members
     * used to invite members to a call to user
     * @return response of invite call
     */
    fun inviteMembers(userID: String) {
        val disposableSingleObserver =
                object : DisposableSingleObserver<InviteMembersToCallUseCase.ResponseValues>() {
                    override fun onSuccess(responseValues: InviteMembersToCallUseCase.ResponseValues) {
                        // getting response from api if it is successful
                        println("call Id ${responseValues.callDetails.callId}")
                    }

                    override fun onError(e: Throwable) {
                        // if there is any error in response
                        println("error in placing call ${e.localizedMessage}")
                        postErrorLiveData.postValue(e.localizedMessage)
                    }
                }
        // calling the api through UseCase
        handler.execute(
                inviteMembersToCallUseCase,
                InviteMembersToCallUseCase.RequestValues(
                        callId, userID
                ),
                disposableSingleObserver
        )
    }

    /**
     * connectMQTT
     * used to connect MQTT
     */
    fun connectMQTT() {
        disposable.add(
                mqttHandler
                        .startMqttConnection()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                object : DisposableObserver<String>() {
                                    override fun onNext(mqttStatus: String) {
                                        Log.d(
                                                TAG.toString(),
                                                "onNext mqtt status: $mqttStatus"
                                        )
                                        subscribeToCallTopic()
                                    }

                                    override fun onError(e: Throwable) {
                                        Log.d(
                                                TAG.toString(),
                                                "onError: " + e.localizedMessage
                                        )
                                    }

                                    override fun onComplete() {}
                                })
        )
    }

    /**
     * used to subscribe to call topic
     */
    private fun subscribeToCallTopic() {
        disposable.add(
                mqttHandler
                        .subscribeMqttTopic("$CALL_TOPIC$callId")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                object : DisposableObserver<String>() {
                                    override fun onNext(mqttStatus: String) {
                                        Log.d(
                                                TAG.toString(),
                                                "onNext mqtt subscribed: $mqttStatus"
                                        )
                                        listenForMqttData()
                                        subscribeMqttTopicLiveData.postValue(Pair(true, callId))
                                    }

                                    override fun onError(e: Throwable) {
                                        Log.d(
                                                TAG.toString(),
                                                "onError: subscription + ${e.localizedMessage}"
                                        )
                                        postErrorLiveData.postValue(e.localizedMessage)
                                        subscribeMqttTopicLiveData.postValue(Pair(false, callId))
                                    }

                                    override fun onComplete() {}
                                })
        )
    }

    /**
     * used to listen the data from MQTT
     */
    private fun listenForMqttData() {
        disposable.add(
                MqttSubscriptionInteractor
                        .observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                object : DisposableObserver<Pair<*, *>>() {
                                    override fun onNext(response: Pair<*, *>) {
                                        Log.d(
                                                TAG.toString(),
                                                "onNext mqtt data: ${response.first}"
                                        )
                                        when (response.first) {
                                            CallStatus.NEW_CALL -> {
                                                val callerDetailsResponse =
                                                        response.second as CallerDetailsResponse
                                                inviteCallingLiveData.postValue(
                                                        callerDetailsResponse
                                                )
                                            }
                                            CallStatus.CALLING -> {
                                                remoteSubscriptionLiveData.postValue(true)
                                            }
                                            CallStatus.DISCONNECT -> {
                                                val callerDetailsResponse: CallerDetailsResponse =
                                                        response.second as CallerDetailsResponse
                                                if (callerDetailsResponse.userBusy) {
                                                    val busyString =
                                                            "${callerDetailsResponse.userName} ${context.getString(
                                                                    R.string.calling_user_busy
                                                            )}"
                                                    disconnectCallLiveData.postValue(Pair(true, busyString))
                                                } else {
                                                    disconnectCallLiveData.postValue(Pair(true, callerDetailsResponse.message
                                                            ?:""))
                                                }
                                            }
                                            CallStatus.SINGLE_USER_LEAVE -> {
                                                val callerDetailsResponse: CallerDetailsResponse =
                                                        response.second as CallerDetailsResponse
                                                if (userHandler.getUserId()!! == response.second) {
                                                    disconnectCallLiveData.postValue(Pair(false, callerDetailsResponse.message
                                                            ?:""))
                                                }
                                            }
                                            else -> {
                                            }
                                        }
                                    }

                                    override fun onError(e: Throwable) {
                                        Log.d(
                                                TAG.toString(),
                                                "onError: subscription + ${e.localizedMessage}"
                                        )
                                        postErrorLiveData.postValue(e.localizedMessage)
                                    }

                                    override fun onComplete() {}
                                })
        )
    }

    fun disposeObservable() {
        disposable.clear()
    }

    /**
     * used to get the call for the received call
     */
    fun setCallIdForCallReceived(callId: String, userName: String) {
        this.callId = callId
        this.callerName!!.set(userName)
        getParticipantsInCall()
    }

    fun unsubscribeTopic() {
        if (::callId.isInitialized) {
            disposable.add(
                    mqttHandler
                            .unSubscribeMqttTopic("$CALL_TOPIC$callId")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(
                                    object : DisposableObserver<String>() {
                                        override fun onNext(mqttStatus: String) {
                                            Log.d(
                                                    TAG.toString(),
                                                    "onNext mqtt unSubscribed: $mqttStatus"
                                            )
                                        }

                                        override fun onError(e: Throwable) {
                                            Log.d(
                                                    TAG.toString(),
                                                    "onError: unSubscribed + ${e.localizedMessage}"
                                            )
                                            postErrorLiveData.postValue(e.localizedMessage)
                                        }

                                        override fun onComplete() {}
                                    })
            )
        }
    }

    /*This is play ringtone while user will response to call*/
    @TargetApi(Build.VERSION_CODES.P)
    fun playRingTone() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            if(!::r.isInitialized)
                r = RingtoneManager.getRingtone(context, notification)
            if(!r.isPlaying) {
                r.isLooping = true
                r.play()
            }
            setAudioModeInRingTone()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /*This is used to stop ringtone while disconnect or answer or activitiy destroyed*/

    fun stopRingTone() {
        try {
            if(::r.isInitialized)
                r.stop()
            setAudioModeInNormal()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * answerCall
     * used to answer a call from user
     */

    /**
     * answerCall
     * used to answer a call from user
     */
    fun answerCall() {
        stopRingTone()
        answerLiveData.postValue(true)
    }

    fun answerCall(session: String) {
        sessionId = session
        val disposableSingleObserver =
                object : DisposableSingleObserver<AnswerCallUseCase.ResponseValues>() {
                    override fun onSuccess(responseValues: AnswerCallUseCase.ResponseValues) {
// getting response from api if it is successful
                        println("CallingViewModel ${responseValues.callDetails.callId}")
                        isCallReceived = false
                        isAnswered = true
// answerLiveData.postValue(true)
                    }

                    override fun onError(e: Throwable) {
// if there is any error in response
                        /*
            * Bug Title: Call: The call is disconnected when we click on the call disconnect button multiple times
            * Bug Id: DUBAND105
            * Fix desc: check
            * Fix dev: hardik
            * Fix date: 28/4/21
            * */
                        if(!isAnswered)
                            disconnectCallLiveData.postValue(Pair(true, ""))
//postErrorLiveData.postValue(e.localizedMessage)

                    }
                }
// calling the api through UseCase
        handler.execute(
                answerCallUseCase,
                AnswerCallUseCase.RequestValues(
                        callId,sessionId),
                disposableSingleObserver
        )

    }

    /*fun answerCall(toString: String) {

        stopRingTone()

        val disposableSingleObserver =
                object : DisposableSingleObserver<AnswerCallUseCase.ResponseValues>() {
                    override fun onSuccess(responseValues: AnswerCallUseCase.ResponseValues) {
                        // getting response from api if it is successful
                        println("CallingViewModel ${responseValues.callDetails.callId}")
                        isCallReceived = false
                        isAnswered = true
                    }

                    override fun onError(e: Throwable) {
                        // if there is any error in response
                        disconnectCallLiveData.postValue(Pair(true, ""))
                        //postErrorLiveData.postValue(e.localizedMessage)

                    }
                }
        // calling the api through UseCase
        handler.execute(
            answerCallUseCase,
            AnswerCallUseCase.RequestValues(
                callId
            ),
            disposableSingleObserver
        )
    }*/

    fun openParticipantsScreen() {
        uiElementsLiveData.postValue(UiAction.OPEN_PARTICIPANTS_SCREEN)
    }

    /**
     * <h2>getUsersList</h2>
     * used to validate the callerName and password
     * @return returns true if valid else false
     */
    fun getUsersList(): LiveData<Pair<*, *>> {
        return getParticipantsLiveData
    }

    fun getParticipantsData(): LiveData<UiAction> {
        return uiElementsLiveData
    }

    /**
     * disconnectCall
     * used to disconnect a callc from user
     */
    fun disconnectCall(isCallRunning: Int, isToFinishUi: Boolean) {
        stopRingTone()
        stopCallerRing()
        if(!::callId.isInitialized)
            callId=""

        val callDisconnectType: CallDisconnectType = when (isCallRunning) {
            ONE -> CallDisconnectType.CALL
            TWO -> CallDisconnectType.REQUEST
            else -> CallDisconnectType.BUSY
        }

        println("$TAG isCallRunning :$isCallRunning")
        val disposableSingleObserver =
                object : DisposableSingleObserver<DisconnectCallUseCase.ResponseValues>() {
                    override fun onSuccess(responseValues: DisconnectCallUseCase.ResponseValues) {
                        // getting response from api if it is successful

                        println("CallingViewModel ${responseValues.callDetails.callId}")
                        if (isToFinishUi) {
                            disconnectCallLiveData.postValue(Pair(true, ""))
                        }
                    }

                    override fun onError(e: Throwable) {
                        // if there is any error in response

                        disconnectCallLiveData.postValue(Pair(true, ""))
                        //postErrorLiveData.postValue(e.localizedMessage)

                    }
                }
        // calling the api through UseCase

        handler.execute(
                diconnectCallUseCase,
                DisconnectCallUseCase.RequestValues(
                        callId, callDisconnectType
                ),
                disposableSingleObserver
        )

    }

    /**
     * used to switch the camera back and front
     */
    fun switchCamera() {
        uiElementsLiveData.postValue(UiAction.SWITCH_CAMERA)
    }

    /**
     * used to switch the video on and off
     */
    fun toggleVideo() {
        uiElementsLiveData.postValue(UiAction.TOGGLE_VIDEO)
    }

    /**
     * used to keep on mic an speaker
     */
    fun toggleMic() {
        uiElementsLiveData.postValue(UiAction.TOGGLE_MIC)
    }

    /**`
     * used to get the live data object for post call
     */
    fun getPostCallLiveData(): LiveData<String> {
        return postErrorLiveData
    }

    /**
     * used to get the live data object for invited call
     */
    fun getInviteCalledLiveData(): LiveData<CallerDetailsResponse> {
        return inviteCallingLiveData
    }

    /**
     * used to get the live data object for dicoonect call
     */
    fun getDisconnectCallLiveData(): LiveData<Pair<Boolean, String>> {
        return disconnectCallLiveData
    }

    /**
     * used to get the live data object for dicoonect call
     */
    fun getAnswerLiveData(): LiveData<Boolean> {
        return answerLiveData
    }

    /**
     * used to get the live data object for subscribed mqtt topic
     */
    fun getMqttTopicSubscribed(): LiveData<Pair<Boolean,String>> {
        return subscribeMqttTopicLiveData;
    }


    /**
     * <h2>getUsersList</h2>
     * used to validate the callerName and password
     * @return returns true if valid else false
     */
    fun getRemoteSubscription(): LiveData<Boolean> {
        return remoteSubscriptionLiveData
    }

    fun setSpeaker(on: Boolean) {
        audioManager.setSpeakerphoneOn(on)
    }

    fun setAudioModeInCall(){
        audioManager.setMode(AudioManager.MODE_IN_CALL);
    }

    fun setAudioModeInRingTone(){
        audioManager.setMode(AudioManager.MODE_RINGTONE);
    }

    fun setAudioModeInNormal() {
        audioManager.setMode(AudioManager.MODE_NORMAL);
    }

    fun startCallerRing(){
        mediaPlayer = MediaPlayer.create(context, R.raw.calling)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    fun stopCallerRing(){
        /* Stop the calling sound */
        if (::mediaPlayer.isInitialized) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    fun disconnectMqtt(){
        mqttHandler.disconnectMqtt(false)
    }

    fun setUserData() {
        userHandler.setAuthToken(AppController.getInstance().apiToken)
        userHandler.setLanguageCode(Constants.LANGUAGE)
        userHandler.setUserId(AppController.getInstance().userId)
    }

    fun startBusyTone(){
        mediaPlayer = MediaPlayer.create(context, R.raw.busy_tone_android)
        mediaPlayer.isLooping = false
        mediaPlayer.start()
    }

    fun stopBusyTone(){
        if (::mediaPlayer.isInitialized) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }
}