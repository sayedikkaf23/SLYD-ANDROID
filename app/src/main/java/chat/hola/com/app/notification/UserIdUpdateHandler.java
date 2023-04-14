package chat.hola.com.app.notification;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class UserIdUpdateHandler implements OneSignal.OSExternalUserIdUpdateCompletionHandler{
    @Override
    public void onSuccess(JSONObject results) {
    // The results will contain push and email success statuses
      OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id done with results: " + results.toString());
      try {
        if (results.has("push") && results.getJSONObject("push").has("success")) {
            boolean isPushSuccess = results.getJSONObject("push").getBoolean("success");
            OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id for push status: " + isPushSuccess);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
      try {
        if (results.has("email") && results.getJSONObject("email").has("success")) {
            boolean isEmailSuccess = results.getJSONObject("email").getBoolean("success");
            OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Sets external user id for email status: " + isEmailSuccess);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
}
    @Override
    public void onFailure(OneSignal.ExternalIdError error) {
        // The results will contain push and email failure statuses
        // Use this to detect if external_user_id was not set and retry when a better network connection is made
        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id done with error: " + error.toString());
    }
}
