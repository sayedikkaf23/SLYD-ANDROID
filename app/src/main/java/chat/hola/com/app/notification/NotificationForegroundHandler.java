package chat.hola.com.app.notification;

import android.util.Log;

import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.manager.session.SessionManager;

public class NotificationForegroundHandler implements OneSignal.OSNotificationWillShowInForegroundHandler {
    /*
     * Bug Title: Was logged into the iOS device using my id. Logged in from the Android device with the same id.
     * Was not logged out of the iOS device immediately. Was also able to receive calls. When I opened the app and
     *  clicked on tabs like chat, explore etc the page was loading. After somtime it showed be the login page.
     * Bug Id: DUBAND040
     * Fix Description: check on specific device id and login type
     * Developer Name: Hardik
     * Fix Date: 6/4/2021
     * */
    @Override
    public void notificationWillShowInForeground(OSNotificationReceivedEvent notificationReceivedEvent) {
        // Get custom additional data you sent with the notification
        JSONObject data = notificationReceivedEvent.getNotification().getAdditionalData();
        /*
         * Bug Title: Business Profile->After Approving the business profile getting these errors.
         * Business Profile->Switch to Business profile->When we click on the profile application is crashing.
         * Bug Id: DUBAND119,DUBAND120
         * Fix Description: setup new observer to listen user action
         * Developer Name: Hardik
         * Fix Date: 6/4/2021
         * */
        if(data!=null) {
            Log.i("NOTIFICATION", "NotificationForegroundHandler: " + data.toString());
            String type = "";
            try {
                type = data.getString("type");
            } catch (JSONException ignore) {
            }

            switch (type) {
                case "audio":
                    // Complete with null means don't show a notification.
                    notificationReceivedEvent.complete(null);
                    break;
                case "video":
                    notificationReceivedEvent.complete(null);
                    break;
                case "login":
                    try {
                        if (data.getString("deviceId").equals(AppController.getInstance().getDeviceId()))
                            notificationReceivedEvent.complete(null);
                        else
                            notificationReceivedEvent.complete(notificationReceivedEvent.getNotification());

                        /*
                         * Bug Title: Multiple users can log into the same user id and send message, user is not logged out automatically- user has to click on the logged out notification in order to log out
                         * Bug Id: DUBAND106
                         * Fix desc: when ons signal received forcefully session expiring
                         * Fix dev: Hardik
                         * Fix date: 27/4/21
                         * */

                        /*New login occurs*/
                        String deviceId = data.getString("deviceId");
                        if (!deviceId.equals(AppController.getInstance().getDeviceId())) {
                            new SessionManager(AppController.getInstance()).sessionExpiredFCM(AppController.getInstance());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    // Complete with a notification means it will show
                    notificationReceivedEvent.complete(notificationReceivedEvent.getNotification());

            }
        }
    }
}
