package chat.hola.com.app.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.acceptRequest.AcceptRequestActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.activity.youTab.channelrequesters.ChannelRequestersActivity;
import chat.hola.com.app.home.activity.youTab.followrequest.FollowRequestActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.WebRTCStreamPlayerActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SocialObserver;
import chat.hola.com.app.onboarding.OnBoardingActivity;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import chat.hola.com.app.trendingDetail.TrendingDetail;

import com.ezcall.android.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.onesignal.OSPermissionObserver;
import com.onesignal.OSPermissionStateChanges;
import com.onesignal.OneSignal;

import dagger.android.support.DaggerAppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends DaggerAppCompatActivity implements OSPermissionObserver {

    @Inject
    HowdooService howdooService;
    @Inject
    SocialObserver socialShareObserver;
    private String postId;
    JSONObject obj = null;
    String d;
    //VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        fullScreenFrame();
        //videoView = findViewById(R.id.videoView);

        printHashKey(this);

        OneSignal.addPermissionObserver(this);

        Intent intent = getIntent();
        if (!wasLaunchedFromRecents()) {
            Bundle bundle = intent.getExtras();
            try {
                //assert bundle != null;
                if (bundle != null) {
                    d = bundle.getString("data");
                    if (d != null) {
                        obj = new JSONObject(d);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Request republish of posts
        AppController.getInstance().createNewPostListener(howdooService, socialShareObserver);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            postId = deepLink.getLastPathSegment();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                jump();
            }
        }, 50);

    }

    private void jump() {
        boolean isOnBoardingDone = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isOnBoardingDone = prefs.getBoolean("isOnBoardingDone", false);
        if (isOnBoardingDone) {
            if (AppController.getInstance().getSignedIn() && AppController.getInstance().profileSaved() && AppController.getInstance()
                    .getSharedPreferences()
                    .getBoolean("isOnBoardingDone", false)) {
                Intent i2;
                try {
                    if (obj != null && !obj.getString("type").isEmpty()) {
                        showNotification(d);
                    } else {
                        if (postId != null && !postId.isEmpty()) {
                            i2 = new Intent(MainActivity.this, SocialDetailActivity.class);
                            i2.putExtra("postId", postId);
                        } else {
                            i2 = new Intent(MainActivity.this, LandingActivity.class);
                            i2.putExtra("userId", AppController.getInstance().getUserId());
                        }

                        i2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i2);
                        finish();
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Intent intent = new Intent(MainActivity.this, SelectLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        } else {
            Intent intent2 = new Intent(this, OnBoardingActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent2);
            finish();
        }

      /*  SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean("isOnBoardingDone", false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("isOnBoardingDone", Boolean.TRUE);
            editor.apply();
        } else {
            Intent intent2 = new Intent(this, OnBoardingActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent2);
            finish();
        }*/

        /*if (AppController.getInstance().getSignedIn() && AppController.getInstance().profileSaved() && AppController.getInstance()
                .getSharedPreferences()
                .getBoolean("isOnBoardingDone", false)) {
            Intent i2;
            try {
                if (obj != null && !obj.getString("type").isEmpty()) {
                    showNotification(d);
                } else {
                    if (postId != null && !postId.isEmpty()) {
                        i2 = new Intent(MainActivity.this, SocialDetailActivity.class);
                        i2.putExtra("postId", postId);
                    } else {
                        i2 = new Intent(MainActivity.this, LandingActivity.class);
                        i2.putExtra("userId", AppController.getInstance().getUserId());
                    }

                    i2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i2);
                    finish();
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

//            if (AppController.getInstance()
//                    .getSharedPreferences()
//                    .getBoolean("isOnBoardingDone", false)) {
//                AppController.getInstance().openSignInDialog(MainActivity.this);
////                supportFinishAfterTransition();
//                finish();
//            } else {
                Intent intent2 = new Intent(this, OnBoardingActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
                finish();
//            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected boolean wasLaunchedFromRecents() {
        return (getIntent().getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY;
    }

    private void showNotification(String data) {

        try {
            Intent intent;

            JSONObject object = new JSONObject(data);//new Gson().fromJson(object, JSONObject.class);

            switch (object.getString("type")) {
                case "new Friend":
                    intent = new Intent(this, AcceptRequestActivity.class);
                    intent.putExtra("from", "notification");
                    intent.putExtra("userId", object.getString("userId"));
                    break;
                case "newFriendRequest":
                    intent = new Intent(this, AcceptRequestActivity.class);
                    intent.putExtra("from", "notification");
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
                    intent = new Intent(this, WebRTCStreamPlayerActivity.class);
                    intent.putExtra("from", "notification");
                    intent.putExtra("call", "liveStream");
                    intent.putExtra("streamName", object.getString("streamName"));
                    intent.putExtra("streamId", object.getString("streamId"));
                    intent.putExtra("viewers", object.getInt("viewers"));
                    intent.putExtra("startTime", object.getLong("startTime"));

                    intent.putExtra("following", object.getBoolean("following"));
                    intent.putExtra("isPrivate", object.getBoolean("isPrivate"));
                    intent.putExtra("followStatus", object.getInt("followStatus"));

                    break;
                case "channelSubscribe":
                    intent = new Intent(this, TrendingDetail.class);
                    String channelId = object.getString("channelId");
                    intent.putExtra("channelId", channelId);
                    intent.putExtra("from", "notification");
                    intent.putExtra("call", "channel");
                    break;
                case "channelRequest":
                    intent = new Intent(this, ChannelRequestersActivity.class);
                    intent.putExtra("call", "notification");
                    intent.putExtra("from", "notification");
                    break;
                case "postLiked":
                case "postCommented":
                case "newPost":
                    intent = new Intent(this, SocialDetailActivity.class);
                    String postId = object.getString("postId");
                    intent.putExtra("from", "notification");
                    intent.putExtra("postId", postId);
                    break;
                case "followRequest":
                    intent = new Intent(this, FollowRequestActivity.class);
                    intent.putExtra("to", "youFrag");
                    intent.putExtra("from", "notification");
                    break;
                case "followed":
                case "following":
                    intent = new Intent(this, ProfileActivity.class);
                    String userId = object.getString("userId");
                    intent.putExtra("from", "notification");
                    intent.putExtra("userId", userId);
                    break;
                case "login":
                    /*New login occurs*/
                    String deviceId = object.getString("deviceId");
                    if (!deviceId.equals(AppController.getInstance().getDeviceId())) {
                        new SessionManager(this).sessionExpiredFCM(this);
                    }
                    return;
                default:
                    intent = new Intent(this, LandingActivity.class);
                    break;
            }
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Log.e("notification", "showNotification: 123");
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("HASH", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("HASH", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("HASH", "printHashKey()", e);
        }
    }

    @Override
    public void onOSPermissionChanged(OSPermissionStateChanges stateChanges) {
        if (stateChanges.getFrom().areNotificationsEnabled() && !stateChanges.getTo().areNotificationsEnabled()) {
            new AlertDialog.Builder(this).setMessage("Notifications Disabled!").show();
        }

        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }

    public void fullScreenFrame() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
}
