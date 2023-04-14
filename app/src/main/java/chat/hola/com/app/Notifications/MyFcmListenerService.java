package chat.hola.com.app.Notifications;

/*
 * Created by moda on 9/12/15.
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import chat.hola.com.app.calling.video.call.CallingActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.acceptRequest.AcceptRequestActivity;
import chat.hola.com.app.calling.myapplication.utility.CallStatus;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.activity.youTab.channelrequesters.ChannelRequestersActivity;
import chat.hola.com.app.home.activity.youTab.followrequest.FollowRequestActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.WebRTCStreamPlayerActivity;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import chat.hola.com.app.trendingDetail.TrendingDetail;

import static com.appscrip.myapplication.utility.Constants.AUDIO;
import static com.appscrip.myapplication.utility.Constants.CALL_ID;
import static com.appscrip.myapplication.utility.Constants.CALL_STATUS;
import static com.appscrip.myapplication.utility.Constants.CALL_TYPE;
import static com.appscrip.myapplication.utility.Constants.ROOM_ID;
import static com.appscrip.myapplication.utility.Constants.USER_ID;
import static com.appscrip.myapplication.utility.Constants.USER_IMAGE;
import static com.appscrip.myapplication.utility.Constants.USER_NAME;
import static com.appscrip.myapplication.utility.Constants.VIDEO;


/***
 *  FirebaseMessagingService to receive and display the push notifications received via firebase
 *
 */

public class MyFcmListenerService extends FirebaseMessagingService {
    private static final String TAG = MyFcmListenerService.class.getSimpleName();
    private static final int NOTIFICATION_SIZE = 5;
    private static final int NOTIFICATION_ID = 1001;
    final String[] dTimeForDB = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "30", "60", "3600", "86400", "604800"};//getResources().getStringArray(R.array.dTimeForDB);
    final String[] dTimeOptions = {"off", "1 second", "2 seconds", "3 seconds", "4 seconds", "5 seconds", "6 seconds", "7 seconds",
            "8 seconds", "9 seconds",
            "10 seconds", "15 seconds", "30 seconds", "1 minute", "1 hour", "1 day", "1 week"};//getResources().getStringArray(R.array.dTimeOptions);

    public static final String CHANNEL_ONE_ID = "com.howdoo.chat.ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if(AppController.getInstance().getSignedIn())
            FirebaseMessaging.getInstance().subscribeToTopic(AppController.getInstance().getUserId());
        if(AppController.getInstance().isForeground()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    public void onMessageReceived(final RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d(TAG, "onMessageReceived: "+message.toString());
        Log.d(TAG, "onMessageReceived: "+message.getFrom().toString());
        Log.d(TAG, "onMessageReceived: "+message.getData().toString());

        String topic = "/topics/" + AppController.getInstance().getUserId();
        if (message.getFrom().equals(topic)) {
            if(message.getData().containsKey("action")) {
                if(!AppController.getInstance().isForeground())
                    handleCall(message.getData());
            }else {
                showNotification(message);
            }
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                if (!AppController.getInstance().isForeground() || !(AppController.getInstance().getActiveReceiverId().equals(message.getData().get("senderId")))) {
                    // createChatNotification(message.getData());
                }
            });
        }
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleCall(Map<String, String> pushData) {

        if ("1".equals(Objects.requireNonNull(pushData.get("action")))) {// new call received
            startActivity(pushData);
        } else if ("4".equals(Objects.requireNonNull(pushData.get("action")))) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(pushData.get(CALL_ID), Constants.CallAndroid10.notification_id);
        }
    }

    private void showNotification(RemoteMessage data) {
        Log.i("NOTIFICATION", "" + data.getData().toString());

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                .setSmallIcon(R.drawable.notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(data.getData().get("title"))
                .setContentText(data.getData().get("msg"))
                .setAutoCancel(true)
                .setSound(soundUri);

        try {
            Intent intent;
            JSONObject object = new JSONObject(data.getData().get("data"));//new Gson().fromJson(object, JSONObject.class);

            NotificationCompat.BigTextStyle style1;
            switch (object.getString("type")) {
                case "new Friend":
                    intent = new Intent(this, AcceptRequestActivity.class);
                    intent.putExtra("userId", object.getString("userId"));
                    break;
                case "newFriendRequest":
                    intent = new Intent(this, AcceptRequestActivity.class);
                    intent.putExtra("userId", object.getString("userId"));
                    intent.putExtra("userName", object.getString("userName"));
                    intent.putExtra("firstName", object.getString("firstName"));
                    intent.putExtra("lastName", object.getString("lastName"));
                    intent.putExtra("profilePic", object.getString("profilePic"));
                    intent.putExtra("mobileNumber", object.getString("mobileNumber"));
                    intent.putExtra("message", "");
                    intent.putExtra("call", "receive");
                    break;
                case "liveStream":
                    AllStreamsData data1 = new AllStreamsData();
                    data1.setStreamName(object.getString("streamName"));
                    data1.setStreamId(object.getString("streamId"));
                    data1.setViewers(object.getString("viewers"));
                    data1.setStarted(object.getLong("startTime"));
                    data1.setFollowing(object.getBoolean("following"));
                    data1.setPrivate(object.getBoolean("isPrivate"));
                    data1.setFollowStatus(object.getInt("followStatus"));

                    intent = new Intent(this, WebRTCStreamPlayerActivity.class);
                    intent.putExtra("streamName", object.getString("streamName"));
                    intent.putExtra("streamId", object.getString("streamId"));
                    intent.putExtra("viewers", object.getString("viewers"));
                    intent.putExtra("startTime", object.getString("startTime"));
                    intent.putExtra("data", data1);
                    style1 = new NotificationCompat.BigTextStyle().bigText(data.getData().get("msg"));
                    builder.setStyle(style1);
                    break;
                case "channelSubscribe":
                    intent = new Intent(this, TrendingDetail.class);
                    String channelId = object.getString("channelId");
                    intent.putExtra("channelId", channelId);
                    intent.putExtra("call", "channel");
                    style1 = new NotificationCompat.BigTextStyle().bigText(data.getData().get("msg"));
                    builder.setStyle(style1);
                    break;
                case "channelRequest":
                    intent = new Intent(this, ChannelRequestersActivity.class);
                    intent.putExtra("call", "notification");
                    style1 = new NotificationCompat.BigTextStyle().bigText(data.getData().get("msg"));
                    builder.setStyle(style1);
                    break;
                case "postLiked":
                case "postCommented":
                case "newPost":
                    intent = new Intent(this, SocialDetailActivity.class);
                    String postId = object.getString("postId");
                    intent.putExtra("postId", postId);
                    break;
                case "followRequest":
                    intent = new Intent(this, FollowRequestActivity.class);
                    intent.putExtra("to", "youFrag");
                    style1 = new NotificationCompat.BigTextStyle().bigText(data.getData().get("msg"));
                    builder.setStyle(style1);
                    break;
                case "followed":
                case "following":
                    intent = new Intent(this, ProfileActivity.class);
                    String userId = object.getString("userId");
                    intent.putExtra("userId", userId);
                    style1 = new NotificationCompat.BigTextStyle().bigText(data.getData().get("msg"));
                    builder.setStyle(style1);
                    break;
                case "login":
                    /*New login occurs*/
                    String deviceId = object.getString("deviceId");
                    if(!deviceId.equals(AppController.getInstance().getDeviceId())){
                        new SessionManager(this).sessionExpiredFCM(this);
                    }
                    return;
                default:
                    intent = new Intent(this, LandingActivity.class);
                    style1 = new NotificationCompat.BigTextStyle().bigText(data.getData().get("msg"));
                    builder.setStyle(style1);
                    break;
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentIntent(pendingIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, notificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    public Bitmap getBitmapFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return Bitmap.createScaledBitmap(BitmapFactory.decodeStream(input), 630, 357, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void startActivity(Map<String, String> pushData) {

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !AppController.getInstance().isForeground()) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();

            String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("call_notification");
            String CHANNEL_NAME = getString(R.string.call_notificaiton);
            assert notificationManager != null;

            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                mChannel.setSound(sound, attributes);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                    mChannel.setAllowBubbles(true);
                }
                notificationManager.createNotificationChannel(mChannel);
            }

            String callTitle;

            if (pushData.get(CALL_TYPE).equals(AUDIO)) {
                callTitle = getString(R.string.incoming_audio_call) + " " + pushData.get(USER_NAME);
            } else if (pushData.get(CALL_TYPE).equals(VIDEO)) {
                callTitle = getString(R.string.incoming_video_call) + " " + pushData.get(USER_NAME);
            } else {
                callTitle = getString(R.string.call) + " " + pushData.get(USER_NAME);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.notification)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setContentTitle(callTitle)
                    .setContentText(getString(R.string.tap_to_open))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setContentIntent(openCallScreen(pushData))
                    .setAutoCancel(true)
                    .setTimeoutAfter(60000)
                    .setOngoing(true)
                    .setSound(sound);

            Notification notification = builder.build();
            notificationManager.notify(pushData.get(CALL_ID), Constants.CallAndroid10.notification_id, notification);
        } else {
            //todo below android 10
            try {
                openCallScreen(pushData).send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private PendingIntent openCallScreen(Map<String, String> pushData) {

        Intent notifyIntent = new Intent(this, CallingActivity.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifyIntent.putExtra(USER_NAME, pushData.get(USER_NAME));
        notifyIntent.putExtra(USER_IMAGE, pushData.get(USER_IMAGE));
        notifyIntent.putExtra(USER_ID, pushData.get(USER_ID));
        notifyIntent.putExtra(CALL_STATUS, CallStatus.NEW_CALL);
        notifyIntent.putExtra(CALL_TYPE, pushData.get(CALL_TYPE));
        notifyIntent.putExtra(CALL_ID, pushData.get(CALL_ID));
        notifyIntent.putExtra(ROOM_ID, pushData.get(ROOM_ID));
        return PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );

    }
}


