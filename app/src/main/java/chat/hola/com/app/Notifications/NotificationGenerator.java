package chat.hola.com.app.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;

import androidx.core.app.NotificationCompat;

import com.ezcall.android.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.manager.session.SessionManager;


public class NotificationGenerator {

    /*
     *
     * For clubbing of the notifications
     */

    private static final int NOTIFICATION_SIZE = 5;
    private Context context = AppController.getInstance();

private SessionManager sessionManager;
    private AppController instance = AppController.getInstance();

    @SuppressWarnings("unchecked")
    public void generatePushNotificationLocal(String notificationId, String messageType, String senderName, String actualMessage, Intent intent, long dTime, String secretId,
                                              String receiverUid, String toId, String replyType, String amount, String transferStatus, String transferStatusText) {

sessionManager= new SessionManager(context);
        if ((!instance.isForeground()) || (instance.getActiveReceiverId().isEmpty()) || (!(instance.getActiveReceiverId().equals(receiverUid))) || (!(instance.getActiveReceiverId().equals(receiverUid) && instance.getActiveSecretId().equals(secretId)))) {
            try {

                String pushMessage = "";
                switch (Integer.parseInt(messageType)) {


                    case 0: {
                        try {
                            pushMessage = new String(Base64.decode(actualMessage, Base64.DEFAULT), "UTF-8");


                            if (pushMessage.trim().isEmpty()) {


                                /*
                                 *Intentionally written the code twice,as this is not the common case
                                 */

                                if (dTime != -1) {
                                    String message_dTime = String.valueOf(dTime);

                                    for (int i = 0; i < instance.getdTimeForDB().length; i++) {
                                        if (message_dTime.equals(instance.getdTimeForDB()[i])) {

                                            if (i == 0) {


                                                pushMessage = context.getString(R.string.Timer_set_off);
                                            } else {


                                                pushMessage = context.getString(R.string.Timer_set_to) + " " + instance.getdTimeOptions()[i];

                                            }
                                            break;
                                        }
                                    }
                                } else {

                                    pushMessage = context.getResources().getString(R.string.youAreInvited) + " " + senderName + " " +
                                           context.getResources().getString(R.string.JoinSecretChat);
                                }

                            }


                        } catch (UnsupportedEncodingException e) {

                        }
                        break;
                    }
                    case 1: {

                        pushMessage = "Image";
                        break;
                    }
                    case 2: {
                        pushMessage = "Video";
                        break;
                    }
                    case 3: {
                        pushMessage = "Location";

                        break;
                    }
                    case 4: {
                        pushMessage = "Follow";

                        break;
                    }
                    case 5: {
                        pushMessage = "Audio";

                        break;
                    }
                    case 6: {

                        pushMessage = "Sticker";

                        break;
                    }
                    case 7: {

                        pushMessage = "Doodle";

                        break;

                    }
                    case 8: {

                        pushMessage = "Gif";

                        break;
                    }
                    case 10: {

                        switch (Integer.parseInt(replyType)) {


                            case 0: {
                                try {
                                    pushMessage = new String(Base64.decode(actualMessage, Base64.DEFAULT), "UTF-8");


                                } catch (UnsupportedEncodingException e) {

                                }
                                break;
                            }
                            case 1: {

                                pushMessage = "Image";
                                break;
                            }

                            case 2: {
                                pushMessage = "Video";
                                break;
                            }
                            case 3: {
                                pushMessage = "Location";

                                break;
                            }
                            case 4: {
                                pushMessage = "Follow";

                                break;
                            }
                            case 5: {
                                pushMessage = "Audio";

                                break;
                            }
                            case 6: {

                                pushMessage = "Sticker";

                                break;
                            }
                            case 7: {

                                pushMessage = "Doodle";

                                break;

                            }
                            case 8: {

                                pushMessage = "Gif";

                                break;
                            }
                            //added here 15/1/2019
                            case 13: {

                                pushMessage = "Post";

                                break;
                            }
                            default: {
                                pushMessage = "Document";
                            }
                        }
                        break;
                    }
                    //added here 15/1/2019
                    case 13: {

                        pushMessage = "Post";

                        break;
                    }

                    // transfer

                    case 15: {
                        if (!toId.equals(instance.getUserId())) {
                            switch (transferStatus) {
                                case "1":
                                    pushMessage = context.getString(R.string.remind) + " " + senderName + " " + context.getString(R.string.to_confirm_receipt);
                                    return;
                                case "2":
                                    pushMessage = senderName + " " + context.getString(R.string.accept_your_payment) + " " + context.getString(R.string.of) + " " + sessionManager.getCurrencySymbol() + " " + amount;
                                    break;
                                case "3":
                                    pushMessage = senderName + " " + context.getString(R.string.did_not_respond_to_your_payment) + " " + context.getString(R.string.of) + " " + sessionManager.getCurrencySymbol() + " " + amount;
                                    break;
                                case "4":
                                    pushMessage = senderName + " " + context.getString(R.string.denied_your_payment) + " " + context.getString(R.string.of) + " " + sessionManager.getCurrencySymbol() + " " + amount;
                                    break;
                                case "5":
                                    pushMessage = context.getString(R.string.you_are_cancel_this_payment);
                                    return;
                            }
                        } else {
                            switch (transferStatus) {
                                case "1":
                                    pushMessage = context.getString(R.string.you_received_payment) + " " + context.getString(R.string.of) + " " + sessionManager.getCurrencySymbol() + " " + amount + " " + context.getString(R.string.from) + " " + senderName;
                                    break;
                                case "2":
                                    pushMessage = context.getString(R.string.you_are_accepted_this_payment);
                                    return;
                                case "3":
                                    pushMessage = context.getString(R.string.the_payment_sent_by) + " " + senderName + " " + context.getString(R.string.has_now_expired);
                                    break;
                                case "4":
                                    pushMessage = context.getString(R.string.you_are_rejected_this_payment);
                                    return;
                                case "5":
                                    pushMessage = senderName + " " + context.getString(R.string.canceled_the_payment_of) + " " + sessionManager.getCurrencySymbol() + " " + amount;
                                    break;
                            }
                        }
                        break;
                    }

                    // Missed Call
                    case 16:
                        pushMessage = context.getString(R.string.missed_call);
                        break;
                    // Call Message
                    case 17:
                        pushMessage = context.getString(R.string.call);
                        break;

                    default: {
                        pushMessage = "Document";
                    }


                }


                /*
                 * For clubbing of the notifications
                 */

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();


                Map<String, Object> notificationInfo = fetchNotificationInfo(notificationId);


                int unreadMessageCount;
                int systemNotificationId;

                ArrayList<String> messages;
                if (notificationInfo == null) {

                    /*
                     * No previous notifications for the chat
                     *
                     */
                    notificationInfo = new HashMap<>();
                    messages = new ArrayList<>();

                    messages.add(pushMessage);
                    notificationInfo.put("notificationMessages", messages);

                    notificationInfo.put("notificationId", notificationId);

                    systemNotificationId = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(9));
                    notificationInfo.put("systemNotificationId", systemNotificationId);


                    unreadMessageCount = 0;
                } else {
                    messages = (ArrayList<String>) notificationInfo.get("notificationMessages");
                    messages.add(0, pushMessage);

                    if (messages.size() > NOTIFICATION_SIZE) {
                        messages.remove(messages.size() - 1);
                    }
                    systemNotificationId = (int) notificationInfo.get("systemNotificationId");
                    notificationInfo.put("notificationMessages", messages);
                    unreadMessageCount = (int) notificationInfo.get("messagesCount");

                }
                notificationInfo.put("messagesCount", unreadMessageCount + 1);
                addOrUpdateNotification(notificationInfo, notificationId);


                for (int i = 0; i < messages.size(); i++) {


                    inboxStyle.addLine(messages.get(i));


                }

                if (unreadMessageCount > (NOTIFICATION_SIZE - 1)) {
                    inboxStyle.setSummaryText("+" + (unreadMessageCount - (NOTIFICATION_SIZE - 1)) + " " + context.getString(R.string.more_message));
                }


                PendingIntent pendingIntent = PendingIntent.getActivity(instance, systemNotificationId, intent, PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                String title, tickerText;
                if (messages.size() == 1) {

                    pushMessage = senderName + ": " + pushMessage;
                    tickerText = pushMessage;
                    title = context.getString(R.string.app_name);
                } else {
                    tickerText = senderName + ": " + pushMessage;
                    title = senderName;
                }


                inboxStyle.setBigContentTitle(title);
                NotificationCompat.Builder
                        notificationBuilder = new NotificationCompat.Builder(instance, "com.howdoo.chat.THREE")
                        .setSmallIcon(R.drawable.notification)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setContentText(pushMessage)
                        .setTicker(tickerText)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setPriority(Notification.PRIORITY_HIGH);

                if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannel notificationChannel;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = new NotificationChannel("com.howdoo.chat.THREE", "Channel Three", notificationManager.IMPORTANCE_HIGH);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.setShowBadge(true);
                    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                /*
                 *
                 * Notification id is used to notify the same notification
                 *
                 * */

                notificationManager.notify(notificationId, systemNotificationId, notificationBuilder.build());

            } catch (Exception e) {

            }
        }


    }


    /**
     * To fetch a particular notification info
     */
    public Map<String, Object> fetchNotificationInfo(String notificationId) {
        ArrayList<Map<String, Object>> notifications = instance.fetchNotificationsList();
        for (int i = 0; i < notifications.size(); i++) {

            if (notifications.get(i).get("notificationId").equals(notificationId)) {

                return notifications.get(i);
            }
        }
        return null;
    }

    /**
     * To add or update the content of the notifications
     */
    public void addOrUpdateNotification(Map<String, Object> notification, String notificationId) {

        instance.getDbController().addOrUpdateNotificationContent(instance.getNotificationDocId(), notificationId, notification);

        ArrayList<Map<String, Object>> notifications = instance.fetchNotificationsList();

        for (int i = 0; i < notifications.size(); i++) {

            if (notifications.get(i).get("notificationId").equals(notificationId)) {
                notifications.set(i, notification);
                return;
            }
        }


        notifications.add(0, notification);
    }


    /*
     * To remove a particular notification
     */

    public void removeNotification(String notificationId) {

        ArrayList<Map<String, Object>> notifications = instance.fetchNotificationsList();

        boolean found = false;
        for (int i = 0; i < notifications.size(); i++) {

            if (notifications.get(i).get("notificationId").equals(notificationId)) {
                notifications.remove(i);
                found = true;
                break;
            }
        }

        if (found) {


            //  db.getParticularNotificationId(notificationDocId, notificationId);

            int systemNotificationId = instance.getDbController().removeNotification(instance.getNotificationDocId(), notificationId);
            if (systemNotificationId != -1) {

                NotificationManager nMgr = (NotificationManager) instance.getSystemService(Context.NOTIFICATION_SERVICE);


                nMgr.cancel(notificationId, systemNotificationId);
            }
        }


    }

}
