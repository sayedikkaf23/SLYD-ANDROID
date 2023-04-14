package chat.hola.com.app.calling.video

//package chat.hola.com.app.calling.video
//
//import android.app.PendingIntent
//import android.content.Intent
//import android.util.Log
//import com.appscrip.myapplication.BaseApplication
//import com.appscrip.myapplication.utility.Constants.Companion.CALL_ID
//import com.appscrip.myapplication.utility.Constants.Companion.CALL_STATUS
//import com.appscrip.myapplication.utility.Constants.Companion.ROOM_ID
//import com.appscrip.myapplication.utility.Constants.Companion.USER_ID
//import com.appscrip.myapplication.utility.Constants.Companion.USER_IMAGE
//import com.appscrip.myapplication.utility.Constants.Companion.USER_NAME
//import chat.hola.com.app.calling.video.call.CallingActivity
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import com.kotlintestgradle.remote.util.CallStatus
//import com.kotlintestgradle.remote.util.Constants.Companion.ONE
//
//class FirebaseMessagingService : FirebaseMessagingService() {
//    /**
//     * Called when message is received.
//     *
//     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
//     */
//    // [START receive_message]
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        // [START_EXCLUDE]
//        // There are two types of messages data messages and notification messages. Data messages are handled
//        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
//        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
//        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
//        // When the user taps on the notification they are returned to the app. Messages containing both notification
//        // and data payloads are treated as notification messages. The Firebase console always sends notification
//        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
//        // [END_EXCLUDE]
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: ${remoteMessage.from}")
//        // Check if message contains a data payload.
//        remoteMessage.data.isNotEmpty().let {
//            Log.d(TAG, "Message data payload: " + remoteMessage.data)
//
//            if (/* Check if data needs to be processed by long running job */ false) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                // scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                if (!BaseApplication.instance.appInForeground) {
//                    handleNow(remoteMessage.data)
//                }
//            }
//        }
//        // Check if message contains a notification payload.
//        remoteMessage.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
//        }
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }
//    // [END receive_message]
//    // [START on_new_token]
//    /**
//     * Called if InstanceID token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the InstanceID token
//     * is initially generated so this is where you would retrieve the token.
//     */
//    override fun onNewToken(token: String) {
//        Log.d(TAG, "Refreshed token: $token")
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(token)
//    }
//    // [END on_new_token]
//    /**
//     * Schedule async work using WorkManager.
//     */
//    /* private fun scheduleJob() {
//         // [START dispatch_job]
//         val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
//         WorkManager.getInstance().beginWith(work).enqueue()
//         // [END dispatch_job]
//     }*/
//    /**
//     * Handle time allotted to BroadcastReceivers.
//     */
//    private fun handleNow(pushData: Map<String, String>) {
//        Log.d(TAG, "Short lived task is done.")
//        when (pushData.getValue("action").toInt()) {
//            ONE -> { // new call received
//                val notifyIntent = Intent(this, CallingActivity::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
//                notifyIntent.putExtra(USER_NAME, pushData.getValue(USER_NAME))
//                notifyIntent.putExtra(USER_IMAGE, pushData.getValue(USER_IMAGE))
//                notifyIntent.putExtra(USER_ID, pushData.getValue(USER_ID))
//                notifyIntent.putExtra(CALL_STATUS, CallStatus.NEW_CALL)
//                notifyIntent.putExtra(CALL_ID, pushData.getValue(CALL_ID))
//                notifyIntent.putExtra(ROOM_ID, pushData.getValue(ROOM_ID))
//                val notifyPendingIntent = PendingIntent.getActivity(
//                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
//                )
//                notifyPendingIntent.send()
//            }
//        }
//    }
//
//    /**
//     * Persist token to third-party servers.
//     *
//     * Modify this method to associate the user's FCM InstanceID token with any server-side account
//     * maintained by your application.
//     *
//     * @param token The new token.
//     */
//    private fun sendRegistrationToServer(token: String?) {
//        // TODO: Implement this method to send token to your app server.
//        Log.d(TAG, "sendRegistrationTokenToServer($token)")
//    }
//
//    /**
//     * Create and show a simple notification containing the received FCM message.
//     *
//     * @param messageBody FCM message body received.
//     */
///*
//    private fun sendNotification(messageBody: String) {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0 */
///* Request code *//*
//, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//        val channelId = getString(R.string.default_notification_channel_id)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_stat_ic_notification)
//            .setContentTitle(getString(R.string.fcm_message))
//            .setContentText(messageBody)
//            .setAutoCancel(true)
//            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)
//        val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Channel human readable title",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(0 */
///* ID of notification *//*
//, notificationBuilder.build())
//    }
//*/
//
//    companion object {
//        private const val TAG = "MyFirebaseMsgService"
//    }
//}
