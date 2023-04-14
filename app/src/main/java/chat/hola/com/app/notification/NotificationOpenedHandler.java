package chat.hola.com.app.notification;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.kotlintestgradle.CallDisconnectType;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import chat.hola.com.app.Activities.MainActivity;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.CallApiServiceGenerator;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.acceptRequest.AcceptRequestActivity;
import chat.hola.com.app.activities_user.UserActivitiesActivity;
import chat.hola.com.app.calling.model.Data;
import chat.hola.com.app.calling.myapplication.utility.CallStatus;
import chat.hola.com.app.calling.video.call.CallingActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.activity.youTab.channelrequesters.ChannelRequestersActivity;
import chat.hola.com.app.home.activity.youTab.followrequest.FollowRequestActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.WebRTCStreamPlayerActivity;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.appscrip.myapplication.utility.Constants.CALL_ID;
import static com.appscrip.myapplication.utility.Constants.CALL_STATUS;
import static com.appscrip.myapplication.utility.Constants.CALL_TYPE;
import static com.appscrip.myapplication.utility.Constants.ROOM_ID;
import static com.appscrip.myapplication.utility.Constants.USER_ID;
import static com.appscrip.myapplication.utility.Constants.USER_IMAGE;
import static com.appscrip.myapplication.utility.Constants.USER_NAME;


public class NotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {

    Context context;

    public NotificationOpenedHandler(Context context) {
        this.context = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenedResult result) {
        // Printing out the full OSNotification object to the logcat for easier debugging.
        Log.i("NOTIFICATION", "result.notification.toJSONObject(): " + result.getNotification().toJSONObject());
        JSONObject data = result.getNotification().getAdditionalData();
        try {
//            OSNotificationAction.ActionType actionType = result.getAction().getType();
//            if (actionType == OSNotificationAction.ActionType.ActionTaken) {
//
//                switch (result.getAction().getActionId()) {
//                    case "action_accept":
//                        if (data != null) {
//                            Data d = new Gson().fromJson(data.toString(), Data.class);
//                            accept(d, true);
//                        }
//                        break;
//                    case "action_reject":
//                        if (data != null) {
//                            Data d = new Gson().fromJson(data.toString(), Data.class);
//                            reject(d.getId());
//                        }
//                        break;
//                    default:
//                        Intent intent1 = new Intent(context, MainActivity.class);
//                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(intent1);
//                        break;
//                }
//
//            } else {
            //on tap of notification
            if (data != null) {
                handleNotification(data);
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNotification(JSONObject data) throws JSONException {
        Intent intent;
        String type = data.getString("type");
        if (type.equals("audio") || type.equals("video")) {
            Data d = new Gson().fromJson(data.toString(), Data.class);
            if(d.getAction()!=null && d.getAction()==1)
                accept(d, false);
        } else {
            switch (data.getString("type")) {
                case "new Friend":
                    intent = new Intent(context, AcceptRequestActivity.class);
                    intent.putExtra("userId", data.getString("userId"));
                    break;
                case "newFriendRequest":
                    intent = new Intent(context, AcceptRequestActivity.class);
                    intent.putExtra("userId", data.getString("userId"));
                    intent.putExtra("userName", data.getString("userName"));
                    intent.putExtra("firstName", data.getString("firstName"));
                    intent.putExtra("lastName", data.getString("lastName"));
                    intent.putExtra("profilePic", data.getString("profilePic"));
                    intent.putExtra("mobileNumber", data.getString("mobileNumber"));
                    intent.putExtra("message", "");
                    intent.putExtra("call", "receive");
                    break;
                case "liveStream":
                    AllStreamsData data1 = new AllStreamsData();
                    data1.setStreamName(data.getString("streamName"));
                    data1.setStreamId(data.getString("streamId"));
                    data1.setViewers(data.getString("viewers"));
                    data1.setStarted(data.getLong("startTime"));
                    data1.setFollowing(data.getBoolean("following"));
                    data1.setPrivate(data.getBoolean("isPrivate"));
                    data1.setFollowStatus(data.getInt("followStatus"));

                    intent = new Intent(context, WebRTCStreamPlayerActivity.class);
                    intent.putExtra("streamName", data.getString("streamName"));
                    intent.putExtra("streamId", data.getString("streamId"));
                    intent.putExtra("viewers", data.getString("viewers"));
                    intent.putExtra("startTime", data.getString("startTime"));
                    intent.putExtra("data", data1);
                    break;
                case "channelSubscribe":
                    intent = new Intent(context, TrendingDetail.class);
                    String channelId = data.getString("channelId");
                    intent.putExtra("channelId", channelId);
                    intent.putExtra("call", "channel");
                    break;
                case "channelRequest":
                    intent = new Intent(context, ChannelRequestersActivity.class);
                    intent.putExtra("call", "notification");
                    break;
                case "postLiked":
                case "postCommented":
                case "newPost":
                    if(data.has("body") && data.getString("body").contains("tagged")){
                        /*user has tagged in new post*/
                        intent = new Intent(context, UserActivitiesActivity.class);
                    }else {
                        intent = new Intent(context, SocialDetailActivity.class);
                        String postId = data.getString("postId");
                        intent.putExtra("postId", postId);
                    }
                    break;
                case "followRequest":
                    intent = new Intent(context, FollowRequestActivity.class);
                    intent.putExtra("to", "youFrag");
                    break;
                case "followed":
                case "following":
                    intent = new Intent(context, ProfileActivity.class);
                    String userId = data.getString("userId");
                    intent.putExtra("userId", userId);
                    break;
                case "login":
                    /*New login occurs*/
                    String deviceId = data.getString("deviceId");
                    if (!deviceId.equals(AppController.getInstance().getDeviceId())) {
                        new SessionManager(context).sessionExpiredFCM(context);
                    }
                    return;
                default:
                    intent = new Intent(context, LandingActivity.class);
                    break;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /*
     * Bug Title: we cannot cut the call once we minimize the application and maximize the application on call,
     * caller press home button calling screen gone not able to retrieve again
     * Fix Description: launch flags are set in activity
     * Developer Name: Hardik
     * Fix Date: 5/4/2021
     * */
    private void accept(Data data, boolean accept) {
        Intent notifyIntent = new Intent(context, CallingActivity.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        notifyIntent.putExtra(USER_NAME, data.getUserName());
        notifyIntent.putExtra(USER_IMAGE, data.getUserImage());
        notifyIntent.putExtra(USER_ID, data.getUserId());
        notifyIntent.putExtra(CALL_STATUS, CallStatus.NEW_CALL);
        notifyIntent.putExtra(CALL_TYPE, data.getType());
        notifyIntent.putExtra(CALL_ID, data.getId());
        notifyIntent.putExtra(ROOM_ID, data.getRoom());
        notifyIntent.putExtra("accept", accept);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(notifyIntent);
    }

    private void reject(String callId) {
        HowdooService service = CallApiServiceGenerator.createService(HowdooService.class);
        Call<ResponseBody> call = service.rejectCall(AppController.getInstance().getApiToken(), Constants.LANGUAGE, callId, CallDisconnectType.BUSY.getTag());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

            }
        });

    }
}
