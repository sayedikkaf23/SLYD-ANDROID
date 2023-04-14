package chat.hola.com.app.notification;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.gson.Gson;
import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.calling.model.Data;
import chat.hola.com.app.manager.session.SessionManager;

public class NotificationServiceExtension implements OneSignal.OSRemoteNotificationReceivedHandler {
    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent notificationReceivedEvent) {
        OSNotification notification = notificationReceivedEvent.getNotification();
        Log.i("NOTIFICATION", "data: " + notification.toJSONObject());

        JSONObject jsonObject = notification.getAdditionalData();
//        Data data = null;
//        if (jsonObject != null) {
//            data = new Gson().fromJson(jsonObject.toString(), Data.class);
//        }

        // Example of modifying the notification's accent color
        OSMutableNotification mutableNotification = notification.mutableCopy();
        try {
            /*
            * Bug Title: Multiple users can log into the same user id and send message, user is not logged out automatically- user has to click on the logged out notification in order to log out
            * Bug Id: DUBAND106
            * Fix desc: when ons signal received forcefully session expiring
            * Fix dev: Hardik
            * Fix date: 27/4/21
            * */
            String type = "";
            if(jsonObject.has("type"))
                type = jsonObject.getString("type");

            if(type.equals("login")){
                /*New login occurs*/
                String deviceId = jsonObject.getString("deviceId");
                if (!deviceId.equals(AppController.getInstance().getDeviceId())) {
                    new SessionManager(context).sessionExpiredFCM(context);
                }
            }

            if (jsonObject.has("action")) {
//                int callNotificationId = Math.abs(Integer.parseInt(jsonObject.getString("callId").substring(0, 8), 16));
//                mutableNotification.setAndroidNotificationId(callNotificationId);
//                OneSignal.removeNotification(callNotificationId);

                mutableNotification.setExtender(builder -> {

                    builder.setSmallIcon(R.drawable.notification)
                            .setContentTitle(notification.getTitle())
                            .setContentText(notification.getBody())
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setPriority(NotificationCompat.PRIORITY_MAX);

                    try {
                        if (jsonObject.getInt("action") == 1) {
                            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                            builder.setSound(sound);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                String channelId = BuildConfig.APPLICATION_ID.concat("call_notification");
                                builder.setChannelId(channelId);
                            }
                        } else {
                            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    return builder;
                });

            }
        } catch (Exception ignore) {
        }

        //            Bitmap image = null;
//            try {
//                URL url = new URL(finalData.getUserImage());
//                image = Utilities.getCroppedBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // If null is passed to complete
        notificationReceivedEvent.complete(mutableNotification);
    }
}
