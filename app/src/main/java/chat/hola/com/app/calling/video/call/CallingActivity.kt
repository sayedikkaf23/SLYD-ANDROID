package chat.hola.com.app.calling.video.call

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.telecom.TelecomManager
import android.util.Log
import android.util.Rational
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import chat.hola.com.app.AppController
import chat.hola.com.app.BlurTransformation.BlurTransformation
import chat.hola.com.app.calling.myapplication.utility.CallStatus
import chat.hola.com.app.calling.myapplication.utility.CallingViewUtil
import chat.hola.com.app.calling.video.janus.JanusConnection
import chat.hola.com.app.calling.video.janus.JanusRTCInterface
import chat.hola.com.app.calling.video.janus.PeerConnectionClient
import chat.hola.com.app.calling.video.janus.WebSocketChannel
import chat.hola.com.app.home.connect.ContactActivity
import com.appscrip.myapplication.utility.Constants.Companion.AUDIO
import com.appscrip.myapplication.utility.Constants.Companion.CALL_ID
import com.appscrip.myapplication.utility.Constants.Companion.CALL_STATUS
import com.appscrip.myapplication.utility.Constants.Companion.CALL_TYPE
import com.appscrip.myapplication.utility.Constants.Companion.COMING_FROM
import com.appscrip.myapplication.utility.Constants.Companion.FINISH_ACTIVITY
import com.appscrip.myapplication.utility.Constants.Companion.NO_VALUE
import com.appscrip.myapplication.utility.Constants.Companion.ONE
import com.appscrip.myapplication.utility.Constants.Companion.RC_ADD_PARTICIPANTS
import com.appscrip.myapplication.utility.Constants.Companion.RC_CAMERA_PERM
import com.appscrip.myapplication.utility.Constants.Companion.ROOM_ID
import com.appscrip.myapplication.utility.Constants.Companion.THRICE
import com.appscrip.myapplication.utility.Constants.Companion.TWO
import com.appscrip.myapplication.utility.Constants.Companion.USER_ID
import com.appscrip.myapplication.utility.Constants.Companion.USER_IMAGE
import com.appscrip.myapplication.utility.Constants.Companion.USER_NAME
import com.appscrip.myapplication.utility.HeadsetIntentReceiver
import com.appscrip.myapplication.utility.Utility
import com.appscrip.myapplication.utility.Utility.longToast
import com.appscrip.myapplication.utility.Utility.shortToast
import com.appscrip.video.UiAction
import com.appscrip.video.call.CallingViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.ezcall.android.BuildConfig
import com.ezcall.android.BuildConfig.SOCKET_URL
import com.ezcall.android.R
import com.ezcall.android.databinding.ActivityCallingBinding
import com.kotlintestgradle.remote.util.Constants
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_calling.*
import org.json.JSONObject
import org.webrtc.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.math.BigInteger
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class CallingActivity : AppCompatActivity(), JanusRTCInterface,
        PeerConnectionClient.PeerConnectionEvents, VideosAdapter.ClickEvent,
        EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks, SensorEventListener {
    override fun muteOpponent(handleId: BigInteger) {
        mWebSocketChannel!!.muteOpponent(handleId)
    }

    private var isConnected: Boolean = false
    private var isDurationSet: Boolean = false

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: ActivityCallingBinding
    private lateinit var callingViewModel: CallingViewModel
    private var peerConnectionClient: PeerConnectionClient? = null
    private var peerConnectionParameters: PeerConnectionClient.PeerConnectionParameters? = null
    private var videoCapturer: VideoCapturer? = null
    private var rootEglBase: EglBase? = null
    private var mWebSocketChannel: WebSocketChannel? = null
    private val callUserDetailsList = ArrayList<CallUserDetails?>()
    private var videosAdapter: VideosAdapter? = null
    private lateinit var localViewRenderer: SurfaceViewRenderer

    //private lateinit var groupUsersDialog: GroupUsersDialog
    private var notifiedItems = NO_VALUE
    private lateinit var callingViewUtil: CallingViewUtil
    private var isMirrorFeed: Boolean = false
    private var micEnabled: Boolean = true
    private var videoEnabled: Boolean = true
    private var roomId: Long = 0
    private lateinit var callStatus: CallStatus
    private lateinit var userId: String
    private lateinit var callId: String
    private lateinit var userName: String
    private lateinit var broadcastReceiver: BroadcastReceiver
    private var headsetReceiver = HeadsetIntentReceiver()
    private var onStopCalled: Boolean = false
    private lateinit var callType: String
    private var isVideoEnable: Boolean = false
    private var isConnectionClose: Boolean = false
    val MAX_RECONNECT_TIME: Long = 30000

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        // Activity onCreate Window Flags before setContentView()
        val window = this.window
        window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        } else {
            window.decorView.systemUiVisibility =
                    (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        super.onCreate(savedInstanceState)

        initializeView()
        initializeViewModel()
        fetchValues()
        initProximitySensor()
        subscribeUsersList()
        subscribeParticipants()
        subscribeRemoteSubscription()
        subscribePostCall()
        subscribeMqttTopicSubscribed()
        subscribeDisconnectCall()
        subscribeDisconnectCallFromUserId()
        createLocalRender()
        subscribeInviteCall()
        cameraTask()
        subscribeBroadCast()
    }

    private lateinit var mySensorManager: SensorManager
    private lateinit var myProximitySensor: Sensor

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (!Build.MANUFACTURER.equals("samsung")) {
            val params: WindowManager.LayoutParams = this.window.attributes
            if (event!!.sensor!!.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    //near
                    params.screenBrightness = 0f
                    window.attributes = params
                    window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    if (!isVideoEnable) {
                        callingViewModel.setSpeaker(false)
                        binding.fbCallingTurnOffVideo.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        Color.TRANSPARENT
                                )
                        )
                    }

                } else {
                    //away
                    params.screenBrightness = -1f
                    window.attributes = params
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            }
        }
    }

    private fun initProximitySensor() {

        try {
            mySensorManager = getSystemService(
                    Context.SENSOR_SERVICE
            ) as SensorManager;
            myProximitySensor = mySensorManager.getDefaultSensor(
                    Sensor.TYPE_PROXIMITY
            );
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::myProximitySensor.isInitialized) {
            mySensorManager.registerListener(
                    this,
                    myProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    override fun onPause() {
        super.onPause()
        mySensorManager.unregisterListener(this)
//        if (Utility.isCallActive(this)) {
//            val tm: TelecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    if (tm.isInCall || tm.isInManagedCall) {
//                        callingViewModel.disconnectCall(TWO, true)
//                    }
//                }
//            }
//        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    fun cameraTask() {
        if (hasCameraPermission()) {
            // Have permission, do the thing!
            when (callStatus) {
                CallStatus.CALLING -> {
                    binding.grpCallingUI.visibility = View.VISIBLE
                    callingViewModel.defineCallReceived(false)
                    binding.grpCallingReceiver.visibility = GONE
                    roomId = Utility.generateGroupId(this)
                    //callingViewModel.postCallAPI(userId, userName, roomId,callType)
                    callingViewModel.startCallerRing()
                    initConnectionWithJanus(roomId)
                    AppController.getInstance().setActiveOnACall(true, true)
                    peerConnectionClient!!.startVideoSource()
                    peerConnectionClient!!.setAudioEnabled(true)
                    tvCallingStatus.text = getString(R.string.callingCallingSmall)

                    tvCallInStatus.visibility = GONE
                    tvCallerName.visibility = GONE
                    ivFullScreen.visibility = GONE
                    viewFullScreen.visibility = VISIBLE
                }
                CallStatus.NEW_CALL -> {
                    callingViewModel.defineCallReceived(true)
                    callingViewModel.playRingTone()
                    binding.grpCallingConfiguration.visibility = GONE
                    fbCallingFlipCamera.visibility = GONE
                    binding.grpCallingReceiver.visibility = VISIBLE

                    tvCallingStatus.text = getString(R.string.incoming_call)

                  binding.tvCallingStatus.visibility = GONE
                    binding.tvCallingName.visibility = GONE

                    binding.tvCallInStatus.visibility = VISIBLE
                    binding.tvCallerName.visibility = VISIBLE
                    binding.viewFullScreen.visibility = GONE


                }
                else -> {
                }
            }
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.calling_rationale_camera),
                    RC_CAMERA_PERM,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        Log.d(TAG, "onRequestPermissionsResult: + $requestCode + : + $grantResults")
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size)
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            rejectCallIfDenied()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {
        Log.d(TAG, "onRationaleAccepted:$requestCode")
    }

    override fun onRationaleDenied(requestCode: Int) {
        Log.d(TAG, "onRationaleDenied:$requestCode")
    }

    /**
     * used to reject the call if permission denied
     */
    private fun rejectCallIfDenied() {
        Toast.makeText(this, R.string.calling_mandate, Toast.LENGTH_SHORT).show()
        when (callStatus) {
            CallStatus.CALLING -> finishAndRemoveTask()
            CallStatus.NEW_CALL -> callingViewModel.disconnectCall(TWO, true)
            else -> {
            }
        }
    }

    /**
     * checks if the camera permission is allowed
     */
    private fun hasCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(
                this,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
                //, Manifest.permission.READ_PHONE_STATE
        )
    }

    private fun subscribeBroadCast() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, intent: Intent) {
                val action = intent.getAction()
                if (action == FINISH_ACTIVITY) {

                    mWebSocketChannel!!.disconnectPeerConnection()
                    peerConnectionClient!!.close()
                    finishAndRemoveTask()
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(FINISH_ACTIVITY))
        // register headset connection receiver
        val receiverFilter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
        registerReceiver(headsetReceiver, receiverFilter)
    }

    /**
     * initConnectionWithJanus
     * used to initialize the connection with janus
     */
    private fun initConnectionWithJanus(roomId: Long) {
        println("${CallingActivity::class.java} group id $roomId")
        mWebSocketChannel = WebSocketChannel()
        mWebSocketChannel!!.initConnection(SOCKET_URL, roomId, callStatus) { handleId ->
            if (!handleId.toString().isEmpty()) {
                if (callStatus == CallStatus.CALLING) {
                    callingViewModel.postCallAPI(userId, userName, roomId, callType, handleId.toString())
                } else
                    callingViewModel.answerCall(handleId.toString())
            }
        }
        mWebSocketChannel!!.setDelegate(this)

        peerConnectionParameters = PeerConnectionClient.PeerConnectionParameters(
                false,
                0,
                0,
                20,
                "VP8", // H264,VP9
                true,
                0,
                "opus",
                false,
                false,
                false,
                false,
                false
        )

//        peerConnectionParameters = PeerConnectionClient.PeerConnectionParameters(
//                false,
//                360,
//                480,
//                20,
//                "H264",
//                true,
//                0,
//                "opus",
//                false,
//                false,
//                false,
//                false,
//                false
//        )
        peerConnectionClient = PeerConnectionClient.getInstance()
        peerConnectionClient!!.createPeerConnectionFactory(this, peerConnectionParameters, this)
    }

    /**
     * fetchValues
     * used to fetch the values from previous activity
     */
    @SuppressLint("RestrictedApi")
    private fun fetchValues() {

        callingViewModel.setUserData();

        userName = intent.getStringExtra(USER_NAME).orEmpty()
        val userImage = intent.getStringExtra(USER_IMAGE)
        userId = intent.getStringExtra(USER_ID).orEmpty()
        callStatus = intent.getSerializableExtra(CALL_STATUS) as CallStatus
        callId = intent.getStringExtra(CALL_ID).orEmpty()
        roomId = intent.getStringExtra(ROOM_ID)?.toLong()?:0
        callType = intent.getStringExtra(CALL_TYPE).orEmpty()
        isVideoEnable = !callType.equals(AUDIO)


        callingViewModel.setSpeaker(isVideoEnable)

        /*
         * BugId: DUBAND038
         * Bug Title: For video call, change the icon to video on the call receiving page. Currently is shows a phone icon and user can mistake it for an audio call.
         * Fix Description: set float icon dynamically
         * Developer Name: Hardik
         * Fix Date: 8/4/2021
         * */

        if (isVideoEnable)
            fbCallingAcceptCall.setImageResource(R.drawable.ic_videocam)
        else
            fbCallingAcceptCall.setImageResource(R.drawable.calling_call_accept_white_24dp)

//        Glide.with(this)
//            .load(userImage)
//            .apply(RequestOptions.circleCropTransform())
//            .listener(
//                object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any,
//                        target: Target<Drawable>,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        binding.pbCallingProgress.visibility = GONE
//                        return false
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable,
//                        model: Any,
//                        target: Target<Drawable>,
//                        dataSource: DataSource,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        binding.pbCallingProgress.visibility = GONE
//                        return false
//                    }
//                })
//            .into(binding.ivCallingImage)

        if (isVideoEnable) {
            binding.tvCallingTitle.setText(getString(R.string.video_call))
            binding.ivFullScreen.visibility = GONE
            binding.ivCallingImage.visibility = VISIBLE

            Glide.with(this)
                    .load(userImage)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.default_profile)
                    .into(object : BitmapImageViewTarget(binding.ivCallingImage) {

                        override fun setResource(resource: Bitmap?) {
                            super.setResource(resource)
                            binding.pbCallingProgress.visibility = GONE
                            val circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource)
                            circularBitmapDrawable.isCircular = true
                            binding.ivCallingImage.setImageDrawable(circularBitmapDrawable)
                        }
                    })

            Glide.with(this)
                    .load(userImage)
                    .bitmapTransform(CenterCrop(this), BlurTransformation(this))
                    .placeholder(R.drawable.default_profile)
                    .into(binding.ivFullScreen)

        } else {
            binding.tvCallingTitle.setText(getString(R.string.audio_call))
            binding.ivFullScreen.visibility = VISIBLE
            binding.ivCallingImage.visibility = VISIBLE
            binding.pbCallingProgress.visibility = GONE
            tvAudioCallingName.setText(userName)
            tvAudioTitle.setText(getString(R.string.audio_call))

            // Display an image on image view from resource
            binding.fbCallingTurnOffVideo.setImageResource(R.drawable.ic_call_speaker)
            binding.fbCallingFlipCamera.visibility = GONE

            Glide.with(this)
                    .load(userImage)
                    .asBitmap()
                  .centerCrop()
                    .placeholder(R.drawable.default_profile)
                    .into(binding.ivFullScreen)

            Glide.with(this)
                .load(userImage)
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.default_profile)
                .into(object : BitmapImageViewTarget(binding.ivCallingImage) {

                    override fun setResource(resource: Bitmap?) {
                        super.setResource(resource)
                        binding.pbCallingProgress.visibility = GONE
                        val circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource)
                        circularBitmapDrawable.isCircular = true
                        binding.ivCallingImage.setImageDrawable(circularBitmapDrawable)
                    }
                })

        }




        if (callStatus == CallStatus.NEW_CALL) {
            callingViewModel.setCallIdForCallReceived(callId, userName)
            if (isVideoEnable)
                binding.ivFullScreen.visibility = VISIBLE;
        }
    }

    /**
     * Initialising the View using Data Binding
     */
    private fun initializeView() {
        binding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_calling
        )

        callingViewUtil = CallingViewUtil()
        //groupUsersDialog = GroupUsersDialog()
        rootEglBase = EglBase.create()
        videosAdapter = VideosAdapter(rootEglBase, callUserDetailsList, this)
        binding.rvRemoteViews.adapter = videosAdapter
        fbCallingAcceptCall.isEnabled = false
        grpAudioCallUI.visibility = GONE
    }

    /**
     * initializing the view model class
     */
    private fun initializeViewModel() {
        callingViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(CallingViewModel::class.java)

        binding.viewModel = callingViewModel // used to connect binding to viewModel
    }

    /**
     * <h2>subscribeLoginValidation()</h2>
     * used to subscribe the data received from API
     */
    private fun subscribePostCall() {
        callingViewModel.getPostCallLiveData().observe(this, Observer { result ->
            result.toString().shortToast(this)
        })
    }

    /*
     * BugId: DUBAND034
     * Bug Title: User 1 Calls User 2. User 2 does not pick the call at all for 60 seconds. User 1 on their screen
     *  gets a message saying "Caller is not available......" instead of saying "Receiver is not available....."
     * Fix Description: check the caller end in method{subscribeDisconnectCall}
     * Developer Name: Hardik
     * Fix Date: 8/4/2021
     * */

    /**
     * <h2>subscribeDisconnectCall()</h2>
     * used to subscribe the data received from API
     */
    private fun subscribeDisconnectCall() {
        callingViewModel.getDisconnectCallLiveData().observe(this, Observer { result ->
            println("disconnect message  ${result.second}");
            val isForBusy = result.second.contains(getString(R.string.calling_user_busy))
            if (isForBusy) {
                setBusyViewAndTone(result.second)
            } else {
                if (result.second != "" && !callingViewModel.isCallReceived) {
                    result.second.longToast(this)
                }
                if (result.first) {
                    if (!isConnectionClose)
                        closeConnection()
                } else {
                    mWebSocketChannel!!.disconnectPeerConnection()
                    peerConnectionClient!!.close()
                    finishAndRemoveTask()
                    Log.d(TAG, "finish call activity6")
                }
            }
        })

        callingViewModel.getAnswerLiveData().observe(this, Observer { result ->
            /*
            * Bug Title: Call: The call is disconnected when we click on the call disconnect button multiple times
            * Bug Id: DUBAND105
            * Fix desc: disable button
            * Fix dev: hardik
            * Fix date: 28/4/21
            * */
            fbCallingAcceptCall.isEnabled = false // not get multiple clicks on accept button
            initConnectionWithJanus(roomId)
            AppController.getInstance().setActiveOnACall(true, true)
            peerConnectionClient!!.startVideoSource()
            peerConnectionClient!!.setAudioEnabled(false)
        })
    }

    /**
     * <h2>subscribeD()</h2>
     * used to subscribe the data received from API
     */
    private fun subscribeMqttTopicSubscribed() {
        callingViewModel.getMqttTopicSubscribed().observe(this, Observer { result ->
            if (result.first) {
                //TODO successfully subscribe mqtt topic for callid
                fbCallingAcceptCall.isEnabled = true
            } else {
                Log.d(TAG, "finish call in mqtt subscribe")
                callingViewModel.disconnectCall(Constants.THRICE, true)
            }
        })
    }

    /**
     * <h2>subscribeDisconnectCallFromUserId()</h2>
     * It will used incase callid topic not subscribed.
     * used to subscribe the data received from mqtt userId topic for disconnect
     */
    private fun subscribeDisconnectCallFromUserId() {
        AppController.getInstance().getDisconnectCallLiveData().observe(this, Observer { result ->
            //            if (result.second != "") {
//                result.second.shortToast(this)
//            }
            if (result.first) {
                if (result.second.equals(callId) && !isConnectionClose)
                    closeConnection()
            } else {
                mWebSocketChannel!!.disconnectPeerConnection()

                peerConnectionClient!!.close()
                finishAndRemoveTask()
                Log.d(TAG, "finish call activity12")
            }
        })
    }

    /**
     * closeConnection
     * used to close the connection
     */
    private fun closeConnection() {
        if (peerConnectionClient != null) {
            Log.d(TAG, "finish call activity4")
            peerConnectionClient!!.close()
            mWebSocketChannel!!.closeSocket()
            isConnectionClose = true;
            try {
                if (rootEglBase != null) {
                    rootEglBase!!.release()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                if (rootEglBase != null && rootEglBase!!.hasSurface()) {
                    rootEglBase!!.releaseSurface()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            rootEglBase = null
        }
        finishAndRemoveTask()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        /*To handle screen removed from task*/
        if (!callingViewModel.isAnswered) {
            if (callingViewModel.isCallReceived) {
            }
            //callingViewModel.disconnectCall(TWO, false);
            else {
                Log.d(TAG, "finish call in destroyed")
                callingViewModel.disconnectCall(ONE, false);
            }
        }

        AppController.getInstance().setActiveOnACall(false, false)
        callingViewModel.stopRingTone()
        callingViewModel.stopCallerRing()
        callingViewModel.setAudioModeInNormal()
        callingViewModel.unsubscribeTopic()
        callingViewModel.disposeObservable()
        callingViewModel.disconnectMqtt()
        unregisterReceiver(broadcastReceiver)
        unregisterReceiver(headsetReceiver)
        super.onDestroy()
    }

    private fun createLocalRender() {
        localViewRenderer = binding.svLocalVideoRenderer
        localViewRenderer.init(rootEglBase!!.eglBaseContext, null)
        localViewRenderer.setEnableHardwareScaler(true)
        swapFeedMirror(true)
        localViewRenderer.setZOrderMediaOverlay(true) // for OREO fix i.e view over view

        // handle call type in local render view
        if (isVideoEnable)
            localViewRenderer.visibility = VISIBLE
        else
            localViewRenderer.visibility = INVISIBLE
    }

    private fun useCamera2(): Boolean {
        return Camera2Enumerator.isSupported(this)
    }

    private fun captureToTexture(): Boolean {
        return true
    }

    private fun createCameraCapturer(enumerator: CameraEnumerator): VideoCapturer? {
        val deviceNames = enumerator.deviceNames
        // First, try to find front facing camera
        Log.d(TAG, "Looking for front facing cameras.")
        for (deviceName in deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Log.d(TAG, "Creating front facing camera capturer.")
                val videoCapturer = enumerator.createCapturer(deviceName, null)

                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }
        // Front facing camera not found, try something else
        Log.d(TAG, "Looking for other cameras.")
        for (deviceName in deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Log.d(TAG, "Creating other camera capturer.")
                val videoCapturer = enumerator.createCapturer(deviceName, null)

                if (videoCapturer != null) {
                    return videoCapturer
                }
            }
        }

        return null
    }

    private fun createVideoCapturer(): VideoCapturer? {
        val videoCapturer: VideoCapturer?
        if (useCamera2()) {
            Log.d(TAG, "Creating capturer using camera2 API.")
            videoCapturer = createCameraCapturer(Camera2Enumerator(this))
        } else {
            Log.d(TAG, "Creating capturer using camera1 API.")
            videoCapturer = createCameraCapturer(Camera1Enumerator(captureToTexture()))
        }
        if (videoCapturer == null) {
            Log.e(TAG, "Failed to open camera")
            return null
        }
        return videoCapturer
    }

    private fun offerPeerConnection(handleId: BigInteger) {
        videoCapturer = createVideoCapturer()
        peerConnectionClient!!.createPeerConnection(
                rootEglBase!!.eglBaseContext,
                localViewRenderer, // videosAdapter!!.localRender,
                videoCapturer,
                handleId
        )
        peerConnectionClient!!.createOffer(handleId)
    }

    // interface JanusRTCInterface
    override fun onPublisherJoined(handleId: BigInteger) {
        Log.d(TAG, " callback onPublisherJoined: $handleId")
        offerPeerConnection(handleId)
    }

    override fun onPublisherRemoteJsep(handleId: BigInteger, jsep: JSONObject) {
        val type = SessionDescription.Type.fromCanonicalForm(jsep.optString("type"))
        val sdp = jsep.optString("sdp")
        val sessionDescription = SessionDescription(type, sdp)
        peerConnectionClient!!.setRemoteDescription(handleId, sessionDescription)
    }

    override fun subscriberHandleRemoteJsep(handleId: BigInteger, jsep: JSONObject) {
        val type = SessionDescription.Type.fromCanonicalForm(jsep.optString("type"))
        val sdp = jsep.optString("sdp")
        val sessionDescription = SessionDescription(type, sdp)
        peerConnectionClient!!.subscriberHandleRemoteJsep(handleId, sessionDescription)
    }

    override fun onLeaving(handleId: BigInteger) {
        println("TAG callback onLeaving: $handleId")
        runOnUiThread {
            for (callUserDetails in callUserDetailsList) {
                if (callUserDetails!!.janusConnection != null) {
                    if (callUserDetails.janusConnection!!.handleId == handleId) {
                        callUserDetails.janusConnection!!.videoTrack.removeSink(
                                videosAdapter!!.getLostRemote(
                                        handleId
                                )
                        )
                        if (callUserDetails.janusConnection!!.videoRender != null) {
                            callUserDetails.janusConnection!!.videoRender.release()
                        }
                        callUserDetailsList.remove(callUserDetails)
                        callingViewUtil.setViewDimen(
                                callUserDetailsList.size,
                                this,
                                localViewRenderer,
                                videosAdapter,
                                rvRemoteViews,
                                clCallingRoot,
                                glCenterGuideLine,
                                glCenterHorizontalGuideLine
                        )
                        break
                    }
                }
            }
        }
    }

    override fun onLocalDescription(sdp: SessionDescription, handleId: BigInteger) {
        Log.d(TAG, " callback onLocalDescription: " + sdp.type.toString())
        mWebSocketChannel!!.publisherCreateOffer(handleId, sdp, isVideoEnable)
    }

    override fun onBackPressed() {
        if (callingViewModel.isAnswered)
            minimize()
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (callUserDetailsList.size == ONE && notifiedItems == ONE) {
            minimize()
        }
    }

    override fun onRemoteDescription(sdp: SessionDescription, handleId: BigInteger) {
        Log.d(TAG, " callback onIceCandidate: " + sdp.type.toString())
        mWebSocketChannel!!.subscriberCreateAnswer(handleId, sdp, roomId)
    }

    override fun onIceCandidate(candidate: IceCandidate?, handleId: BigInteger) {
        Log.d(TAG, " callback onIceCandidate: $handleId")
        if (candidate != null) {
            mWebSocketChannel!!.trickleCandidate(handleId, candidate)
        } else {
            mWebSocketChannel!!.trickleCandidateComplete(handleId)
        }
    }

    override fun onIceCandidatesRemoved(candidates: Array<IceCandidate>) {
        Log.d(TAG, " callback onIceCandidatesRemoved: $candidates")
    }

    override fun onIceConnected() {

        isConnected = true;
        tvReconnecting.visibility = GONE
    }

    override fun onIceDisconnected() {

        isConnected = false;

        if (callingViewModel.isAnswered)
            tvReconnecting.visibility = VISIBLE

        Timer("ReconnectingTimeOut", false).schedule(MAX_RECONNECT_TIME) {
            if (!isConnected) {
                Log.d(TAG, "finish call activity5")
                if (!isConnectionClose)
                    closeConnection()
            }
        }
    }

    override fun onPeerConnectionClosed() {
        Log.d(TAG, " callback onPeerConnectionClosed: ")
    }

    override fun onPeerConnectionStatsReady(reports: Array<StatsReport>) {
        Log.d(TAG, " callback onPeerConnectionStatsReady: ")
    }

    override fun onPeerConnectionError(description: String) {

    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, " onStop:")

        onStopCalled = true

        /*
       * Bug Title: user a calls user b , user b dimisses thr push , sound keeps playing ,user a then ends the call , sound still keeps playing on user b instead of stopping
       * pending call api
       * Fix Description: have implemented new pending call api when this activity launch
       * Developer Name: Hardik
       * Fix Date: 5/4/2021
       * */
        if (callingViewModel.isCallReceived && !callingViewModel.isAnswered)
            callingViewModel.stopRingTone()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, " onStart:")

        onStopCalled = false

        /*
      * Bug Title: user a calls user b , user b dimisses thr push , sound keeps playing ,user a then ends the call , sound still keeps playing on user b instead of stopping
      * pending call api
      * Fix Description: have implemented new pending call api when this activity launch
      * Developer Name: Hardik
      * Fix Date: 5/4/2021
      * */
        if (callingViewModel.isCallReceived && !callingViewModel.isAnswered)
            callingViewModel.playRingTone()

    }

    /**
     * <h2>subscribeUsersList()</h2>
     * used to subscribe the users list received from API
     */
    private fun subscribeUsersList() {
        callingViewModel.getUsersList().observe(this, Observer { result ->
            val isResult: Boolean? = result.first as Boolean?
            if (isResult == false) {
                Toast.makeText(this, getString(R.string.calling_miss_call), Toast.LENGTH_SHORT)
                        .show()
                finishAndRemoveTask()
            }
        })
    }

    /**
     * <h2>subscribeUsersList()</h2>
     * used to subscribe the users list received from API
     */
    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private fun subscribeParticipants() {
        callingViewModel.getParticipantsData().observe(this, Observer { result ->
            when (result) {
                UiAction.OPEN_PARTICIPANTS_SCREEN -> {
                    //TODO need to open the screen where we show list of users
                    val intent = Intent(this, ContactActivity::class.java)
                    intent.putExtra(COMING_FROM, CallingActivity::class.java.simpleName)
                    intent.putExtra(CALL_ID, callingViewModel.callId)
                    intent.putExtra(CALL_TYPE, callType)
                    startActivityForResult(intent, RC_ADD_PARTICIPANTS)
                }
                UiAction.SWITCH_CAMERA -> {
                    if (peerConnectionClient != null) {
                        peerConnectionClient!!.switchCamera()
                        swapFeedMirror(!isMirrorFeed)
                    }
                }
                UiAction.TOGGLE_MIC -> {
                    when (onToggleMic()) {
                        false -> binding.fbCallingMuteMic.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        ContextCompat.getColor(this, R.color.star_grey)
                                )
                        )
                        true -> binding.fbCallingMuteMic.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        ContextCompat.getColor(this, R.color.transparent)
                                )
                        )
                    }
                }
                UiAction.TOGGLE_VIDEO -> {

                    if (isVideoEnable) {
                        when (onToggleVideo()) {
                            false -> binding.fbCallingTurnOffVideo.setBackgroundTintList(
                                    ColorStateList.valueOf(
                                            ContextCompat.getColor(this, R.color.star_grey)
                                    )
                            )
                            true -> binding.fbCallingTurnOffVideo.setBackgroundTintList(
                                    ColorStateList.valueOf(
                                            ContextCompat.getColor(this, R.color.transparent)
                                    )
                            )
                        }
                    } else {
                        when (callingViewModel.audioManager.isSpeakerphoneOn) {
                            false -> {
                                callingViewModel.setSpeaker(true);
                                binding.fbCallingTurnOffVideo.setBackgroundTintList(
                                        ColorStateList.valueOf(
                                                ContextCompat.getColor(this, R.color.star_grey)
                                        )
                                )
                            }
                            true -> {
                                callingViewModel.setSpeaker(false);
                                binding.fbCallingTurnOffVideo.setBackgroundTintList(
                                        ColorStateList.valueOf(
                                                ContextCompat.getColor(this, R.color.transparent)
                                        )
                                )
                            }
                        }
                    }
                }
            }
        })
    }

    private fun swapFeedMirror(mirror: Boolean) {
        isMirrorFeed = mirror
        localViewRenderer.setMirror(mirror)
    }

    private fun onToggleMic(): Boolean {
        if (peerConnectionClient != null) {
            micEnabled = !micEnabled
            peerConnectionClient!!.setAudioEnabled(micEnabled)
        }
        return micEnabled
    }

    private fun onToggleVideo(): Boolean {
        if (peerConnectionClient != null) {
            videoEnabled = !videoEnabled
            peerConnectionClient!!.setVideoEnabled(videoEnabled)
        }
        return videoEnabled
    }

    /**
     * <h2>subscribeUsersList()</h2>
     * used to subscribe the users list received from API
     */
    private fun subscribeInviteCall() {
        callingViewModel.getInviteCalledLiveData().observe(this, Observer { result ->
            println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} invite notified items :$notifiedItems")
            when (callUserDetailsList.size) {
                ONE -> {
                    println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} invite items inside:ONE")
                    val callUserDetails = CallUserDetails(null, result, true)
                    callUserDetailsList.add(callUserDetails)
                }
                TWO -> {
                    println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} invite items inside:TWO")
                    if (callUserDetailsList.get(ONE)!!.janusConnection == null) {
                        println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} invite items inside:if")
                        callUserDetailsList.get(ONE)!!.userDetails = result
                    } else {
                        println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} invite items inside:else")
                        val callUserDetails = CallUserDetails(null, result, true)
                        callUserDetailsList.add(callUserDetails)
                    }
                }
                THRICE -> {
                    println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} invite items inside:THRICE")
                    callUserDetailsList.get(TWO)!!.userDetails = result
                }
            }
            callingViewUtil.setViewDimen(
                    callUserDetailsList.size,
                    this,
                    localViewRenderer,
                    videosAdapter,
                    rvRemoteViews,
                    clCallingRoot,
                    glCenterGuideLine,
                    glCenterHorizontalGuideLine
            )
        })
    }

    override fun onRemoteRender(connection: JanusConnection) {
        Log.d(TAG, "callback onRemoteRender")
        tvCallingStatus.text = ""
        runOnUiThread {
            val callUserDetails: CallUserDetails?
            println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} onRemote items ")
            when (callUserDetailsList.size) {
                NO_VALUE -> {
                    println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} onRemote items :NO VALUE")
                    callUserDetails = CallUserDetails(connection, null, false)
                    callUserDetailsList.add(callUserDetails)
                }
                ONE -> {
                    println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} onRemote items :ONE")
                    callUserDetails = CallUserDetails(connection, null, true)
                    callUserDetailsList.add(callUserDetails)
                }
                TWO -> {
                    println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} onRemote items :TWO")
                    if (callUserDetailsList.get(ONE)!!.janusConnection == null) {
                        println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} onRemote items :TWO IF")
                        callUserDetailsList.get(ONE)!!.janusConnection = connection
                    } else {
                        println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} onRemote items :TWO ELSE")
                        callUserDetails = CallUserDetails(connection, null, true)
                        callUserDetailsList.add(callUserDetails)
                    }
                }
                THRICE -> {
                    println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} onRemote items :THREE")
                    if (callUserDetailsList.get(TWO)!!.janusConnection == null) {
                        println("${CallingActivity::class.java.simpleName} :${callUserDetailsList.size} onRemote items :if")
                        callUserDetailsList.get(TWO)!!.janusConnection = connection
                    }
                }
            }


            println("${CallingActivity::class.java.simpleName} :list size  :${callUserDetailsList.size} onRemote items :$notifiedItems")
            if (callUserDetailsList.size == notifiedItems || callUserDetailsList.size == TWO ||
                    callUserDetailsList.size == THRICE
            ) { // for late joining after remote acknowledgment
                if (!callingViewModel.isCallReceived) {
                    peerConnectionClient!!.setAudioEnabled(true)
                    tvStopWatch2.visibility = VISIBLE

//                    tvAudioTitle.visibility = INVISIBLE
//                    tvAudioCallingName.visibility = INVISIBLE
                    callingViewUtil.setViewDimen(
                            callUserDetailsList.size,
                            this,
                            localViewRenderer,
                            videosAdapter,
                            rvRemoteViews,
                            clCallingRoot,
                            glCenterGuideLine,
                            glCenterHorizontalGuideLine
                    )
                }
            }
        }
    }

    /**
     * <h2>subscribeLoginValidation()</h2>
     * used to subscribe the data received from API
     */
    @SuppressLint("RestrictedApi")
    private fun subscribeRemoteSubscription() {
        callingViewModel.getRemoteSubscription().observe(this, Observer { result ->
            grpCallingUI.visibility = GONE
            //tvCallingAddMembers.visibility = VISIBLE
            grpCallingReceiver.visibility = GONE
            grpCallingConfiguration.visibility = VISIBLE
            binding.ivCallingImage.visibility = VISIBLE

            if (isVideoEnable) {
                fbCallingFlipCamera.visibility = VISIBLE
                binding.ivFullScreen.visibility = GONE;
                binding.viewFullScreen.visibility = GONE
                binding.tvCallInStatus.visibility = GONE
                binding.tvCallerName.visibility = GONE
                binding.ivCallingImage.visibility = GONE

            } else
                fbCallingFlipCamera.visibility = GONE

            callingViewModel.isAnswered = true

            tvCallingStatus.text = getString(R.string.connecting)

            println("${CallingActivity::class.java.simpleName}: list size in notify :${callUserDetailsList.size}: is call received ${callingViewModel.isCallReceived}")

            callingViewModel.setAudioModeInCall()
            callingViewModel.stopCallerRing()

            if (!isVideoEnable && !isDurationSet) {
                setCallDuration()
            }

            when (callUserDetailsList.size) {
                TWO -> {
                    println("${CallingActivity::class.java.simpleName}: list size in notify : TWO")
                    callUserDetailsList.get(ONE)!!.isToShowPlaceHodler =
                            callUserDetailsList.get(ONE)!!.janusConnection == null
                }
                THRICE -> {
                    callUserDetailsList.get(ONE)!!.isToShowPlaceHodler =
                            callUserDetailsList.get(ONE)!!.janusConnection == null
                    callUserDetailsList.get(TWO)!!.isToShowPlaceHodler =
                            callUserDetailsList.get(TWO)!!.janusConnection == null
                    println(
                            "${CallingActivity::class.java.simpleName}: list size in notify : THRICE" + "${
                                callUserDetailsList.get(
                                        TWO
                                )!!.isToShowPlaceHodler
                            }"
                    )
                }
            }
            peerConnectionClient!!.setAudioEnabled(true)
            if (!isVideoEnable) {
                binding.tvCallInStatus.visibility = GONE
                binding.tvCallerName.visibility = GONE
                binding.viewFullScreen.visibility = VISIBLE
                grpCallingUI.visibility = VISIBLE
                tvCallingTitle.visibility = GONE
                tvCallingStatus.visibility = GONE
//                grpAudioCallUI.visibility = VISIBLE

            }
                callingViewUtil.setViewDimen(
                    callUserDetailsList.size,
                    this,
                    localViewRenderer,
                    videosAdapter,
                    rvRemoteViews,
                    clCallingRoot,
                    glCenterGuideLine,
                    glCenterHorizontalGuideLine
            )
            notifiedItems++
        })
    }

    /**
     * Enters Picture-in-Picture mode.
     */
    private fun minimize() {
        // Calculate the aspect ratio of the PiP screen.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /** The arguments to be used for Picture-in-Picture mode.  */
            val mPictureInPictureParamsBuilder = PictureInPictureParams.Builder()
            mPictureInPictureParamsBuilder.setAspectRatio(
                    Rational(
                            localViewRenderer.width,
                            localViewRenderer.height
                    )
            )
            handleUiForMinimize(GONE)

            try {
                enterPictureInPictureMode(mPictureInPictureParamsBuilder.build())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_ADD_PARTICIPANTS -> {
                handleUiForMinimize(VISIBLE)
                if (resultCode == Activity.RESULT_OK) {
                    val userId = data!!.getStringExtra(USER_ID)
                    callingViewModel.inviteMembers(userId!!)
                }
            }
            AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE -> {
                Log.d(TAG, "onActivityResult: for permission  + ${hasCameraPermission()}")
                if (!hasCameraPermission()) {
                    rejectCallIfDenied()
                } else {
                    cameraTask()
                }
            }
        }
    }

    override fun onPictureInPictureModeChanged(
            isInPictureInPictureMode: Boolean,
            newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        Log.d(TAG, "onPictureInPictureModeChanged: " + isInPictureInPictureMode)
        if (isInPictureInPictureMode) {
            // Starts receiving events from action items in PiP mode.
            handleUiForMinimize(GONE)
        } else {
            // We are out of PiP mode. We can stop receiving events from it.
            handleUiForMinimize(VISIBLE)
            if (onStopCalled) {
                Log.d(TAG, "finish call in pip")
                callingViewModel.disconnectCall(ONE, true)
            }
        }
        videosAdapter?.setRemoteRenderSize(rvRemoteViews, isInPictureInPictureMode)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        adjustFullScreen(newConfig)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            adjustFullScreen(resources.configuration)
        }
    }

    /**
     * Adjusts immersive full-screen flags depending on the screen orientation.

     * @param config The current [Configuration].
     */
    private fun adjustFullScreen(config: Configuration) {
        val decorView = window.decorView
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    private fun handleUiForMinimize(visibility: Int) {
        grpCallingConfiguration.visibility = visibility
        ivGradientColor.visibility = visibility
        //tvCallingAddMembers.visibility = visibility
        if (isVideoEnable) {
            localViewRenderer.visibility = visibility
            fbCallingFlipCamera.visibility = visibility
        } else {
            fbCallingFlipCamera.visibility = GONE
        }
    }

    companion object {
        private val TAG = CallingActivity::class.java.simpleName
    }

    private fun setCallDuration() {
        try {
            val cTimer: CountDownTimer = object : CountDownTimer(3600000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val milliSec = (3600000 - millisUntilFinished) / 1000
                    val sec = milliSec % 60
                    val min = milliSec / 60
                    if (min < 10) {
                        if (sec < 10) {
                            tvStopWatch.setText("00:0$min:0$sec")
                            tvStopWatch2.setText("00:0$min:0$sec")
                        } else {
                            tvStopWatch.setText("00:0$min:$sec")
                            tvStopWatch2.setText("00:0$min:$sec")
                        }
                    } else {
                        if (sec < 10) {
                            tvStopWatch.setText("00:$min:0$sec")
                            tvStopWatch2.setText("00:$min:0$sec")
                        } else {
                            tvStopWatch.setText("00:$min:$sec")
                            tvStopWatch2.setText("00:$min:$sec")
                        }
                    }
                }

                override fun onFinish() {}
            }
            isDurationSet = true;
            cTimer.start()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun openCallScreenForSameIntent(): PendingIntent? {
        val notifyIntent = intent;
        return PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun clearNotification() {
        val notificationManager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)!!
        notificationManager!!.cancel(callId, chat.hola.com.app.Utilities.Constants.CallAndroid10.notification_id)
    }

    private fun setBusyViewAndTone(second: String) {
        tvCallingStatus.text = second
        callingViewModel.stopCallerRing()
        callingViewModel.startBusyTone()
        Handler().postDelayed({
            callingViewModel.stopBusyTone()
            closeConnection()
        }, 6000)
    }

}
