package chat.hola.com.app;

import static com.appscrip.myapplication.utility.Constants.CALL_ID;
import static com.appscrip.myapplication.utility.Constants.CALL_STATUS;
import static com.appscrip.myapplication.utility.Constants.CALL_TYPE;
import static com.appscrip.myapplication.utility.Constants.ROOM_ID;
import static com.appscrip.myapplication.utility.Constants.USER_ID;
import static com.appscrip.myapplication.utility.Constants.USER_IMAGE;
import static com.appscrip.myapplication.utility.Constants.USER_NAME;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.MediaScannerConnection;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.multidex.MultiDex;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.android.MediaManager;
import com.couchbase.lite.android.AndroidContext;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kotlintestgradle.CallDisconnectType;
import com.kotlintestgradle.model.LoginDataDetails;
import com.onesignal.OneSignal;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import chat.hola.com.app.Activities.PreLoginActivity;
import chat.hola.com.app.AppStateChange.AppStateListener;
import chat.hola.com.app.AppStateChange.AppStateMonitor;
import chat.hola.com.app.AppStateChange.RxAppStateMonitor;
import chat.hola.com.app.ContentObserver.ContactAddedOrUpdatedObserver;
import chat.hola.com.app.ContentObserver.ContactDeletedObserver;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.DownloadFile.FileUploadService;
import chat.hola.com.app.DownloadFile.FileUtils;
import chat.hola.com.app.DownloadFile.ServiceGenerator;
import chat.hola.com.app.MQtt.CustomMQtt.IMqttActionListener;
import chat.hola.com.app.MQtt.CustomMQtt.IMqttDeliveryToken;
import chat.hola.com.app.MQtt.CustomMQtt.IMqttToken;
import chat.hola.com.app.MQtt.CustomMQtt.MqttCallback;
import chat.hola.com.app.MQtt.CustomMQtt.MqttConnectOptions;
import chat.hola.com.app.MQtt.CustomMQtt.MqttMessage;
import chat.hola.com.app.MQtt.MqttAndroidClient;
import chat.hola.com.app.MessagesHandler.Acknowledgement.AcknowledgementHandler;
import chat.hola.com.app.MessagesHandler.FetchChats.FetchChatsHandler;
import chat.hola.com.app.MessagesHandler.FetchMessages.FetchMessageHandler;
import chat.hola.com.app.MessagesHandler.GroupChatMessages.GroupChatDatabaseHelper;
import chat.hola.com.app.MessagesHandler.GroupChatMessages.GroupChatMessageHandler;
import chat.hola.com.app.MessagesHandler.PeerToPeerMessage.PeerToPeerMessageDatabaseHandler;
import chat.hola.com.app.MessagesHandler.PeerToPeerMessage.PeerToPeerMessageHandler;
import chat.hola.com.app.MessagesHandler.UserUpdate.UserUpdateHandler;
import chat.hola.com.app.MessagesHandler.Utilities.DatabaseDocumentHelper;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.UnsafeOkHttpClient;
import chat.hola.com.app.Networking.connection.NetworkCheckerService;
import chat.hola.com.app.Notifications.NotificationGenerator;
import chat.hola.com.app.Notifications.RegistrationIntentService;
import chat.hola.com.app.Service.AppKilled;
import chat.hola.com.app.Service.MQTT_constants;
import chat.hola.com.app.Service.OreoJobService;
import chat.hola.com.app.Service.PublishPost;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.ClearGlideCacheAsyncTask;
import chat.hola.com.app.Utilities.ConnectivityReceiver;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.DeviceUuidFactory;
import chat.hola.com.app.Utilities.FileHelper;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.calling.model.Data;
import chat.hola.com.app.calling.model.GetParticipantResponse;
import chat.hola.com.app.calling.model.PendingCallsResponse;
import chat.hola.com.app.calling.model.User;
import chat.hola.com.app.calling.myapplication.utility.CallStatus;
import chat.hola.com.app.calling.video.call.CallerDetailsResponse;
import chat.hola.com.app.calling.video.call.CallingActivity;
import chat.hola.com.app.dagger.AppComponent;
import chat.hola.com.app.dagger.AppUtilModule;
import chat.hola.com.app.dagger.DaggerAppComponent;
import chat.hola.com.app.dagger.NetworkModule;
import chat.hola.com.app.iceserver.IceServerResponse;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.ConnectionObserver;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.SocialObserver;
import chat.hola.com.app.notification.NotificationForegroundHandler;
import chat.hola.com.app.notification.NotificationOpenedHandler;
import chat.hola.com.app.post.model.PostData;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by moda on 29/06/17.
 */
public class AppController extends DaggerApplication
        implements Application.ActivityLifecycleCallbacks {

    private static final String ONESIGNAL_APP_ID = "dadde2e3-06f6-4af8-b4c6-4bd4c5095fee";
    public static final String TAG = AppController.class.getSimpleName();
    private SessionApiCall sessionApiCall;
    private static final int NOTIFICATION_SIZE = 5;
    public static Bus bus = new Bus(ThreadEnforcer.ANY);
    public static Bus liveStreamBus = MQTTManager.getBus();
    private static AppController mInstance;
    /**
     * Arrays for the secret chats dTag message received
     */
    final String[] dTimeForDB = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "30", "60", "3600", "86400",
            "604800"
    };//getResources().getStringArray(R.array.dTimeForDB);
    final String[] dTimeOptions = {
            "off", "1 second", "2 seconds", "3 seconds", "4 seconds", "5 seconds", "6 seconds",
            "7 seconds", "8 seconds", "9 seconds", "10 seconds", "15 seconds", "30 seconds", "1 minute",
            "1 hour", "1 day", "1 week"
    };//getResources().getStringArray(R.array.dTimeOptions);
    public boolean applicationKilled;
    @Inject
    ConnectionObserver connectionObserver;
    @Inject
    NetworkConnector networkConnector;
    ContactAddedOrUpdatedObserver contactUpdateObserver =
            new ContactAddedOrUpdatedObserver(new Handler());
    ContactDeletedObserver contactDeletedObserver = new ContactDeletedObserver(new Handler());
    private AppComponent appComponent;
    private boolean previewImagesForFiltersToBeGenerated = true;
    private boolean fullScreenCameraTobeOpened = true;
    private SharedPreferences sharedPref;
    private boolean foreground;
    private String groupChatsDocId;
    private String chatDocId;
    private String unsentMessageDocId;
    private String mqttTokenDocId;
    private String notificationDocId;
    private String statusDocId;

    private String mutedDocId;
    private String blockedDocId;
    private String viewPostDocId;
    private String callsDocId;
    private String contactsDocId;
    private String allContactsDocId; /*activeTimersDocId,*/
    private String userName;
    public static Activity activity;

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    private String apiToken;
    private String userId;
    private String userImageUrl;
    private String userIdentifier;
    private String friendsDocId;
    /**
     * logintype-0-email
     * 1-phone  number
     */

    private int signInType;
    private String indexDocId, pushToken;
    private CouchDbController db;
    private boolean filtersUpdated = false;
    private boolean signStatusChanged = false;
    private ArrayList<Integer> excludedIds;
    private boolean signedIn = false;
    /*
     * Have put this value intentionally
     */
    private String activeReceiverId = "";
    private RequestQueue mRequestQueue;
    private ArrayList<String> colors;
    private String userDocId;
    private int activeActivitiesCount;
    /*
     * For the unread chats count
     */
    //    private int unreadChatCount = 0;
    private MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mqttConnectOptions;
    private IMqttActionListener listener;
    private boolean flag = true;
    private ArrayList<HashMap<String, Object>> tokenMapping = new ArrayList<>();
    private HashSet<IMqttDeliveryToken> set = new HashSet<>();
    private Typeface tfRegularFont, tfMediumFont, tfSemiboldFont, tfRegularRoboto, tfBold, avenyT;
    private long timeDelta = 0;
    private String defaultProfilePicUrl =
            "http://truesample.com/wp-content/themes/truesample/img/icon-Respondent020716v2.png";
    private boolean newSignupOrLogin = false;
    private boolean activeOnACall = false;
    private String activeCallId, activeCallerId;
    private String activeSecretId = "";
    private boolean profileSaved = false;
    /**
     * Initialize for callback in the chatlist or chatmessage screen activity
     */

    private boolean callMinimized = false;
    private boolean firstTimeAfterCallMinimized = false;
    private String deviceId;
    /*
     * Utilities for the resending of the unsent messages
     */
    private Intent notificationService;
    private boolean contactSynced = false;
    private boolean registeredObserver = false;
    public ArrayList<Map<String, Object>> dirtyContacts = new ArrayList<>();
    public ArrayList<Map<String, Object>> newContacts = new ArrayList<>();
    public ArrayList<Map<String, Object>> inactiveContacts = new ArrayList<>();
    public int pendingContactUpdateRequests = 0;
    private String userStatus;
    private boolean chatSynced;
    private ArrayList<Map<String, Object>> notifications = new ArrayList<>();

    /*
     *
     * For clubbing of the notifications
     */
    private boolean activeOnVideoCall = false;
    // private boolean autoDownloadAllowed;
    private WifiManager wifiMgr;
    private WifiInfo wifiInfo;

    /*
     *For auto download of the media messages
     *
     */
    //changed here
    private boolean serviceAlreadyScheduled = false;
    /*
     *For message auto download
     */
    private String removedMessageString = "The message has been removed";
    private PublishPost publishPost;

    private boolean friendsFetched;

    public boolean isFriendsFetched() {
        return friendsFetched;
    }

    public void setFriendsFetched(boolean friendsFetched) {
        this.friendsFetched = friendsFetched;
        sharedPref.edit().putBoolean("friendsFetched", friendsFetched).apply();
    }

    public static Bus getLiveStreamBus() {
        return liveStreamBus;
    }

    public static Bus getBus() {
        return bus;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    //------------- Chats message handler START-----------//
    private static DatabaseDocumentHelper databaseDocumentHelper;

    public static DatabaseDocumentHelper getDatabaseDocumentHelper() {
        if (databaseDocumentHelper == null) databaseDocumentHelper = new DatabaseDocumentHelper();
        return databaseDocumentHelper;
    }

    private PeerToPeerMessageDatabaseHandler peerToPeerMessageDatabaseHandler;

    public PeerToPeerMessageDatabaseHandler getPeerToPeerMessageDatabaseHandler() {
        if (peerToPeerMessageDatabaseHandler == null) {
            peerToPeerMessageDatabaseHandler = new PeerToPeerMessageDatabaseHandler();
        }
        return peerToPeerMessageDatabaseHandler;
    }

    private GroupChatDatabaseHelper groupChatDatabaseHelper;

    public GroupChatDatabaseHelper getGroupChatDatabaseHelper() {
        if (groupChatDatabaseHelper == null)
            groupChatDatabaseHelper = new GroupChatDatabaseHelper();
        return groupChatDatabaseHelper;
    }

    private PeerToPeerMessageHandler peerToPeerMessageHandler;

    public PeerToPeerMessageHandler getPeerToPeerMessageHandler() {
        if (peerToPeerMessageHandler == null)
            peerToPeerMessageHandler = new PeerToPeerMessageHandler();
        return peerToPeerMessageHandler;
    }

    private AcknowledgementHandler acknowledgementHandler;

    public AcknowledgementHandler getAcknowledgementHandler() {
        if (acknowledgementHandler == null) acknowledgementHandler = new AcknowledgementHandler();
        return acknowledgementHandler;
    }

    public FileHelper getFileHelper() {
        if (fileHelper == null) fileHelper = new FileHelper();
        return fileHelper;
    }

    private FileHelper fileHelper;

    public String[] getdTimeForDB() {
        return dTimeForDB;
    }

    public String[] getdTimeOptions() {
        return dTimeOptions;
    }

    public int getsignInType() {
        return signInType;
    }

    public NotificationGenerator getNotificationGenerator() {
        if (notificationGenerator == null) notificationGenerator = new NotificationGenerator();
        return notificationGenerator;
    }

    private NotificationGenerator notificationGenerator;

    public String getNotificationDocId() {
        return notificationDocId;
    }

    private UserUpdateHandler userUpdateHandler;

    public UserUpdateHandler getUserUpdateHandler() {
        if (userUpdateHandler == null) userUpdateHandler = new UserUpdateHandler();
        return userUpdateHandler;
    }

    public FetchChatsHandler fetchChatsHandler;

    public FetchChatsHandler getFetchChatsHandler() {
        if (fetchChatsHandler == null) fetchChatsHandler = new FetchChatsHandler();
        return fetchChatsHandler;
    }

    public FetchMessageHandler fetchMessageHandler;

    public FetchMessageHandler getFetchMessageHandler() {
        if (fetchMessageHandler == null) fetchMessageHandler = new FetchMessageHandler();
        return fetchMessageHandler;
    }

    public GroupChatMessageHandler groupChatMessageHandler;

    public GroupChatMessageHandler getGroupChatMessageHandler() {
        if (groupChatMessageHandler == null)
            groupChatMessageHandler = new GroupChatMessageHandler();
        return groupChatMessageHandler;
    }

    //------------- Chats message handler END-----------//

    /**
     * Convert image or video or audio to byte[] so that it can be sent on socket(Unsetn messages)
     */
    @SuppressWarnings("TryWithIdenticalCatches")

    private static byte[] convertFileToByteArray(File f) {
        byte[] byteArray = null;
        byte[] b;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {

            InputStream inputStream = new FileInputStream(f);
            b = new byte[2663];

            int bytesRead;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        } catch (IOException e) {

        } catch (OutOfMemoryError e) {

        } finally {
            b = null;

            try {
                bos.close();
            } catch (IOException e) {

            }
        }

        return byteArray;
    }

    /**
     * use this method instead of above while user is star
     */
    @SuppressWarnings("unchecked")
    public static String findDocumentIdOfReceiver(String receiverUid, String timestamp,
                                                  String receiverName, String receiverImage, String secretId, boolean invited,
                                                  String receiverIdentifier, String chatId, boolean groupChat, boolean isStar) {

        return getDatabaseDocumentHelper().findDocumentIdOfReceiver(receiverUid, timestamp,
                receiverName, receiverImage, secretId, invited, receiverIdentifier, chatId, groupChat,
                isStar);
    }

    public boolean isPreviewImagesForFiltersToBeGenerated() {
        return previewImagesForFiltersToBeGenerated;
    }

    public boolean isFullScreenCameraTobeOpened() {
        return fullScreenCameraTobeOpened;
    }

    /*
     *Starting the network service for the internet checking. */
    private void initNetworkService() {
        startService(new Intent(this, NetworkCheckerService.class));
    }

    private SessionManager sessionManager;

    @Override
    public void onCreate() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate();

        /*BugId:DUBAND094
        * BugTitle:Crash while posting/live
        * Bug desc:Media manager initialized
        * Develoeper name:Ankit K Tiwary
        * Fixed date:22-April-2021*/
        try {
            MediaManager.init(this);
        } catch (IllegalStateException e) {
            e.fillInStackTrace();
        }

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.ERROR, OneSignal.LOG_LEVEL.ERROR);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.unsubscribeWhenNotificationsAreDisabled(false);
        OneSignal.setNotificationOpenedHandler(new NotificationOpenedHandler(this));
        OneSignal.setNotificationWillShowInForegroundHandler(new NotificationForegroundHandler());

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, getString(R.string.admob_appid));
        mInstance = this;
        try {
            new ClearGlideCacheAsyncTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * For Isometrik groupstreaming
         */
//        IsometrikUiSdk.getInstance().sdkInitialize(this);
        //initNetworkService();
        sessionApiCall = new SessionApiCall();
        AppStateMonitor appStateMonitor = RxAppStateMonitor.create(this);
        appStateMonitor.addListener(new AppStateListener() {
            @Override
            public void onAppDidEnterForeground() {
                updatePresence(1, false);
                foreground = true;
                //if(!isActiveOnACall())
                  //  getPendingCalls();

            }

            @Override
            public void onAppDidEnterBackground() {

                updatePresence(0, false);
                foreground = false;

                updateTokenMapping();
            }
        });
        appStateMonitor.start();
        setBackgroundColorArray();

        sharedPref = this.getSharedPreferences("defaultPreferences", Context.MODE_PRIVATE);

        applicationKilled = sharedPref.getBoolean("applicationKilled", false);

        friendsFetched = sharedPref.getBoolean("friendsFetched", false);

        indexDocId = sharedPref.getString("indexDoc", null);
        db = new CouchDbController(new AndroidContext(this));

        if (indexDocId == null) {

            indexDocId = db.createIndexDocument();

            sharedPref.edit().putString("indexDoc", indexDocId).apply();
        }

        //autoDownloadAllowed = sharedPref.getBoolean("autoDownloadAllowed", true);
        pushToken = sharedPref.getString("pushToken", null);
        if (pushToken == null) {

            if (checkPlayServices() && isForeground()) {
                notificationService = new Intent(this, RegistrationIntentService.class);
                //                try {
                startService(notificationService);
                //                }catch (Exception e){
                //                    e.fillInStackTrace();
                //                }

            }
        }
        sessionManager = new SessionManager(mInstance);
        contactSynced = sharedPref.getBoolean("contactSynced", false);
        chatSynced = sharedPref.getBoolean("chatSynced", false);
        listener = new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

                Log.d("log22", "connect");
                if (!contactSynced) {
                    JSONObject obj = new JSONObject();

                    try {
                        obj.put("eventName", "SyncContacts");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //    Log.d("log53", "connectionsuccess");
                if (newSignupOrLogin) {

                    newSignupOrLogin = false;
                    try {
                        JSONObject tempObj = new JSONObject();
                        tempObj.put("status", 1);


                        /*
                         * Have to retain the message of currently being available for call
                         */

                        AppController.getInstance()
                                .publish(MqttEvents.CallsAvailability.value + "/" + userId, tempObj, 0, true);
                        AppController.getInstance().setActiveOnACall(false, true);
                    } catch (JSONException e) {

                    }
                } else {

                    /*
                     * Have put just for testing
                     */

                    /*TESTING*/

                    if (signedIn) {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("status", 1);
                            publish(MqttEvents.CallsAvailability.value + "/" + userId, obj, 0, true);
                            AppController.getInstance().setActiveOnACall(false, true);
                        } catch (JSONException e) {

                        }
                    }
                }

                if (!applicationKilled) {
                    updatePresence(1, false);
                } else {

                    updatePresence(0, true);
                }

                /*
                 *
                 * Also have to make user available for call
                 *
                 */

                try {

                    JSONObject obj = new JSONObject();
                    obj.put("eventName", MqttEvents.Connect.value);
                    networkConnector.setConnected(true);
                    connectionObserver.isConnected(networkConnector);

                    bus.post(obj);
                } catch (Exception e) {

                }





                /*
                 * To stop the internet checking service
                 */

                if (flag) {

                    flag = false;
                    subscribeToTopic(MqttEvents.Message.value + "/" + userId, 1);
                    subscribeToTopic(MqttEvents.Acknowledgement.value + "/" + userId, 2);
                    subscribeToTopic(MqttEvents.Calls.value + "/" + userId, 0);

                    subscribeToTopic(MqttEvents.UserUpdates.value + "/" + userId, 1);

                    subscribeToTopic(MqttEvents.GroupChats.value + "/" + userId, 1);


                    /*
                     *
                     * Will have to control their subscription to be requirement based
                     */

                    subscribeToTopic(MqttEvents.FetchMessages.value + "/" + userId, 1);
                    subscribeToTopic(userId, 1);
                    checkActiveCalls();
                }

                resendUnsentMessages();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                Log.d("log54", "connectionfailed" + exception.toString());
            }
        };

        Map<String, Object> signInDetails = db.isSignedIn(indexDocId);

        signedIn = (boolean) signInDetails.get("isSignedIn");

        if (signedIn) {
            signInType = (int) signInDetails.get("signInType");

            userId = (String) signInDetails.get("signedUserId");

            profileSaved = (boolean) signInDetails.get("profileSaved");

            getUserDocIdsFromDb(userId);

            createMQttConnection(userId, true);

            getIceServersApi(null);

//            createIsometrikConfiguration(false, null, null, null, null, null, null, null, null, null,
//                    null);
        }

        registerActivityLifecycleCallbacks(mInstance);

        tfRegularFont =
                Typeface.createFromAsset(mInstance.getAssets(), "fonts/proxima-nova-soft-regular.ttf");
        tfMediumFont = Typeface.createFromAsset(getAssets(), "fonts/ProximaNovaSoft-Medium.otf");
        tfSemiboldFont = Typeface.createFromAsset(getAssets(), "fonts/ProximaNovaSoft-Semibold.otf");
        tfRegularRoboto = Typeface.createFromAsset(getAssets(), "fonts/RobotoRegular.ttf");
        avenyT = Typeface.createFromAsset(getAssets(), "fonts/AvenyTRegular.otf");
        if (sharedPref.getBoolean("deltaRequired", true)) {
            getCurrentTime();
        } else {

            timeDelta = sharedPref.getLong("timeDelta", 0);
        }

        deviceId = new DeviceUuidFactory(this).getDeviceUuid();

        activeActivitiesCount = 0;

        //  Log.d("log23", applicationKilled + " app started");

        databaseDocumentHelper = new DatabaseDocumentHelper();
        acknowledgementHandler = new AcknowledgementHandler();
        peerToPeerMessageDatabaseHandler = new PeerToPeerMessageDatabaseHandler();
        groupChatDatabaseHelper = new GroupChatDatabaseHelper();
        peerToPeerMessageHandler = new PeerToPeerMessageHandler();
        groupChatMessageHandler = new GroupChatMessageHandler();
        userUpdateHandler = new UserUpdateHandler();
        fetchChatsHandler = new FetchChatsHandler();
        fetchMessageHandler = new FetchMessageHandler();
        fileHelper = new FileHelper();
        notificationGenerator = new NotificationGenerator();
    }

    private void copyInputStreamToOutputStream(InputStream in, PrintStream out) {
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        appComponent = DaggerAppComponent.builder()
                .application(this)
                .netModule(new NetworkModule())
                .appUtilModule(new AppUtilModule())
                .build();
        appComponent.inject(this);
        return appComponent;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public String getGroupChatsDocId() {
        return groupChatsDocId;
    }

    public void getUserDocIdsFromDb(String userId) {

        userDocId = db.getUserInformationDocumentId(indexDocId, userId);

        ArrayList<String> docIds = db.getUserDocIds(userDocId);

        chatDocId = docIds.get(0);

        unsentMessageDocId = docIds.get(1);
        mqttTokenDocId = docIds.get(2);
        //        unackMessageDocId = docIds.get(3);

        callsDocId = docIds.get(3);
        contactsDocId = docIds.get(4);
        //activeTimersDocId = docIds.get(5);
        allContactsDocId = docIds.get(5);
        statusDocId = docIds.get(6);

        notificationDocId = docIds.get(7);

        groupChatsDocId = docIds.get(8);
        mutedDocId = docIds.get(9);

        blockedDocId = docIds.get(10);

        friendsDocId = docIds.get(11);

        viewPostDocId = docIds.get(12);

        getUserInfoFromDb(userDocId);

        tokenMapping = db.fetchMqttTokenMapping(mqttTokenDocId);
    }

    public void getUserInfoFromDb(String docId) {

        Map<String, Object> userInfo = db.getUserInfo(docId);

        excludedIds = (ArrayList<Integer>) userInfo.get("excludedFilterIds");
        userName = (String) userInfo.get("userName");
        userIdentifier = (String) userInfo.get("userIdentifier");

        apiToken = (String) userInfo.get("apiToken");

        userStatus = (String) userInfo.get("socialStatus");

        userId = (String) userInfo.get("userId");

        userImageUrl = (String) userInfo.get("userImageUrl");

        //  Log.d("log71", apiToken + " " + userName + " " + userIdentifier + " " + userId + " " + userImageUrl);

        //        if (userImageUrl == null || userImageUrl.isEmpty()) {
        //
        //
        //            /*
        //             * Have put it as of now just for the sake of convenience, although needs to be removed later
        //             */
        //
        //
        //            userImageUrl = defaultProfilePicUrl;
        //
        //        }

        //        unreadChatCount = db.getUnreadChatsReceiverUidsCount(chatDocId);

        notifications = db.fetchAllNotifications(notificationDocId);
    }

    public ArrayList<Map<String, Object>> fetchNotificationsList() {

        return notifications;
    }

    public void setNotificationsList(ArrayList<Map<String, Object>> notificationsList) {

        notifications = notificationsList;
    }

    /**
     * To check play services before starting service for generating push token
     */

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }

    public String getPushToken() {

        return pushToken;
    }

    public void setPushToken(String token) {

        this.pushToken = token;

        if (notificationService != null) {
            stopService(notificationService);
        }
    }

    /**
     * Prepare image or audio or video file for upload
     */
    @SuppressWarnings("all")

    public File convertByteArrayToFileToUpload(byte[] data, String name, String extension) {

        File file = null;

        try {

            File folder = new File(
                    getInstance().getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER);

            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }

            file = new File(
                    getInstance().getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER,
                    name + extension);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {

        }

        return file;
    }

    /**
     * To prepare image file for upload
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromResource(String pathName, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    /**
     * To upload image or video or audio to server using multipart upload to avoid OOM exception
     */
    @SuppressWarnings("TryWithIdenticalCatches,all")

    private void uploadFile(final Uri fileUri, final String name, final int messageType,
                            final JSONObject obj, final String receiverUid, final String id,
                            final HashMap<String, Object> mapTemp, final String secretId, final String extension,
                            final boolean toDelete, final boolean isGroupMessage, final Object groupMembersDocId) {

        FileUploadService service = ServiceGenerator.createService(FileUploadService.class);

        final File file = FileUtils.getFile(this, fileUri);

        String url = null;
        if (messageType == 1) {

            url = name + ".jpg";
        } else if (messageType == 2) {

            url = name + ".mp4";
        } else if (messageType == 5) {

            url = name + ".3gp";
        } else if (messageType == 7) {

            url = name + ".jpg";
        } else if (messageType == 9) {

            url = name + "." + extension;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", url, requestFile);

        String descriptionString = "Dubly File Uploading";
        RequestBody description =
                RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                /**
                 *
                 *
                 * has to get url from the server in response
                 *
                 *
                 * */

                try {

                    if (response.code() == 200) {

                        String url = null;
                        if (messageType == 1) {

                            url = name + ".jpg";
                        } else if (messageType == 2) {

                            url = name + ".mp4";
                        } else if (messageType == 5) {

                            url = name + ".3gp";
                        } else if (messageType == 7) {

                            url = name + ".jpg";
                        } else if (messageType == 9) {

                            url = name + "." + extension;
                        }

                        obj.put("payload",
                                Base64.encodeToString((ApiOnServer.CHAT_UPLOAD_PATH + url).getBytes("UTF-8"),
                                        Base64.DEFAULT));
                        obj.put("dataSize", file.length());
                        obj.put("timestamp", new Utilities().gmtToEpoch(Utilities.tsInGmt()));

                        if (toDelete) {
                            File fdelete = new File(fileUri.getPath());
                            if (fdelete.exists()) fdelete.delete();
                        }
                    }
                } catch (JSONException e) {

                } catch (IOException e) {

                }

                /**
                 *
                 *
                 * emitting to the server the values after the file has been uploaded
                 *
                 **/

                String tsInGmt = Utilities.tsInGmt();
                String docId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId);
                db.updateMessageTs(docId, id, tsInGmt);

                if (isGroupMessage) {

                    AppController.getInstance()
                            .publishGroupChatMessage((String) groupMembersDocId, obj, mapTemp);
                } else {

                    AppController.getInstance()
                            .publishChatMessage(MqttEvents.Message.value + "/" + receiverUid, obj, 1, false,
                                    mapTemp);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void setSignedIn(final boolean signedIn, final String userId, final String userName,
                            final String userIdentifier, String userProfilePic, int signInType, String accountId,
                            String projectId, String keysetId, String licenseKey, String rtcAppId, String arFiltersAppId,
                            String isometrikUserId) {

        this.signedIn = signedIn;

        if (signedIn) {
            /*
             *
             * A temporary fix for now,have to commented out later,once things are sorted from server side
             *
             *
             * */

            AppController.getInstance().setGuest(false);

            mInstance.userId = userId;
            mInstance.userIdentifier = userIdentifier;
            mInstance.signInType = signInType;

            mInstance.userName = userName;
            newSignupOrLogin = true;

            getUserDocIdsFromDb(userId);
            createMQttConnection(userId, true);
            getIceServersApi(null);
            postIosPush();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    /*
                     * for checking internet availability
                     * */

                    if (sharedPref.getString("chatNotificationArray", null) == null) {
                        SharedPreferences.Editor prefsEditor = sharedPref.edit();

                        prefsEditor.putString("chatNotificationArray",
                                new Gson().toJson(new ArrayList<Map<String, String>>()));
                        prefsEditor.apply();
                    }
                }
            }, 1000);
            System.out.println("isometrikUserId:" + isometrikUserId);
//            createIsometrikConfiguration(true, accountId, projectId, keysetId, licenseKey, rtcAppId,
//                    arFiltersAppId, isometrikUserId, userName, userIdentifier, userProfilePic);

//            sessionManager.setIsometrikConfigurationKeys(accountId, projectId, keysetId, licenseKey,
//                    rtcAppId, arFiltersAppId, isometrikUserId);
        } else {
            //  deleteNewPostListener();
            flag = true;
            mInstance.userId = null;

            /*
             *
             * although userid is set to null but deviceid is still made to persist
             *
             *
             * */

            //
            //            new Handler().postDelayed(new Runnable() {
            //                @Override
            //                public void run() {
            //
            //
            //                    if (intent != null)
            //                        stopService(intent);
            //
            //
            //                }
            //            }, 1000);
        }
    }

    public long getTimeDelta() {
        return timeDelta;
    }

    public boolean getSignedIn() {
        return this.signedIn;
    }

    /**
     * Update change in signin status
     * If signed in then have to connect socket,start listening on various socket events and
     * disconnect socket in case of signout, stop listening on various socket events
     */
    public void setSignedIn(boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getChatDocId() {

        return chatDocId;
    }

    public String getViewPostDocId() {
        return viewPostDocId;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public String getApiToken() {
        return apiToken;
    }

    public boolean isForeground() {
        return foreground;
    }

    public SharedPreferences getSharedPreferences() {

        return sharedPref;
    }

    public boolean isActiveOnVideoCall() {
        return activeOnVideoCall;
    }

    public boolean isSignStatusChanged() {
        return signStatusChanged;
    }

    public void setSignStatusChanged(boolean signStatusChanged) {
        this.signStatusChanged = signStatusChanged;
    }

    public String getActiveReceiverId() {

        return activeReceiverId;
    }

    public void setActiveReceiverId(String receiverId) {

        this.activeReceiverId = receiverId;
    }

    public String getActiveSecretId() {

        return activeSecretId;
    }

    public void setActiveSecretId(String secretId) {
        this.activeSecretId = secretId;
    }

    public boolean isCallMinimized() {
        return callMinimized;
    }

    public void setCallMinimized(boolean callMinimized) {
        this.callMinimized = callMinimized;
    }

    public boolean getContactSynced() {

        return contactSynced;
    }

    public void setContactSynced(boolean synced) {

        contactSynced = synced;

        sharedPref.edit().putBoolean("contactSynced", synced).apply();
        String time = String.valueOf(Utilities.getGmtEpoch());
        sharedPref.edit().putString("lastUpdated", time).apply();

        sharedPref.edit().putString("lastDeleted", time).apply();
    }

    public boolean getChatSynced() {

        return chatSynced;
    }

    public void setChatSynced(boolean synced) {
        chatSynced = synced;
        sharedPref.edit().putBoolean("chatSynced", synced).apply();
    }

    public boolean isActiveOnACall() {
        return activeOnACall;
    }

    public void setActiveOnACall(boolean activeOnACall, boolean notCallCut) {
        this.activeOnACall = activeOnACall;

        if (!activeOnACall && notCallCut) {

            this.callMinimized = false;
        }
    }

    public boolean isFirstTimeAfterCallMinimized() {
        return firstTimeAfterCallMinimized;
    }

    public void setFirstTimeAfterCallMinimized(boolean firstTimeAfterCallMinimized) {
        this.firstTimeAfterCallMinimized = firstTimeAfterCallMinimized;
    }

    public CouchDbController getDbController() {

        return db;
    }

    public void setApplicationKilled(boolean applicationKilled) {

        this.applicationKilled = applicationKilled;
        //        sharedPref.edit().putBoolean("applicationKilled", applicationKilled).commit();

        sharedPref.edit().putBoolean("applicationKilled", applicationKilled).apply();

        if (applicationKilled) {

            unregisterContactsObserver();
            sharedPref.edit().putString("lastSeenTime", Utilities.tsInGmt()).apply();
        }
    }

    public boolean profileSaved() {
        return profileSaved;
    }

    public void setProfileSaved(boolean profileSaved) {
        this.profileSaved = profileSaved;
    }


    public int getSignInType() {
        return signInType;
    }

    //typefaces changed..
    public Typeface getRegularFont() {
        return tfRegularFont;
    }

    public Typeface getBoldFont() {
        return tfBold;
    }

    public Typeface getSemiboldFont() {
        return tfSemiboldFont;
    }

    public Typeface getAvenyT() {
        return avenyT;
    }

    public Typeface getMediumFont() {
        return tfMediumFont;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;

        db.updateUserName(db.getUserDocId(userId, indexDocId), userName);
    }

    public String getUserId() {
        return userId;
    }

    public String getUserStatus() {
        return userStatus;
    }

    /**
     * To update the user status
     */

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;

        db.updateUserStatus(db.getUserDocId(userId, indexDocId), userStatus);
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;

        db.updateUserImageUrl(db.getUserDocId(userId, indexDocId), userImageUrl);
    }

    public String getDefaultUserImageUrl() {
        return defaultProfilePicUrl;
    }

    public int getActiveActivitiesCount() {
        return activeActivitiesCount;
    }

    public String getDeviceId() {

        return deviceId;
    }

    public void setActiveCallId(String activeCallId) {
        this.activeCallId = activeCallId;
    }

    public void setActiveCallerId(String activeCallerId) {

        this.activeCallerId = activeCallerId;
    }
    /*
     * volley methods to hit api on server
     */

    public String getunsentMessageDocId() {
        return unsentMessageDocId;
    }

    public String getContactsDocId() {
        return contactsDocId;
    }

    public String getFriendsDocId() {
        return friendsDocId;
    }

    //    public void decrementUnreadChatCount() {
    //        unreadChatCount--;
    //    }
    //
    //    public void incrementChatCount() {
    //
    //
    //        unreadChatCount++;
    //    }

    public String getAllContactsDocId() {
        return allContactsDocId;
    }

    public String getCallsDocId() {
        return callsDocId;
    }

    public String getMutedDocId() {
        return mutedDocId;
    }

    public String getBlockedDocId() {
        return blockedDocId;
    }

    public String getIndexDocId() {

        return indexDocId;
    }

    public String getStatusDocId() {

        return statusDocId;
    }

    /**
     * To search if there exists any document containing chat for a particular receiver and if founde
     * returns its document
     * id else return empty string
     */
    @SuppressWarnings("unchecked")
    public String findDocumentIdOfReceiver(String ReceiverUid, String secretId) {
        return getDatabaseDocumentHelper().findDocumentIdOfReceiver(ReceiverUid, secretId);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), hurlStack);
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    private void setBackgroundColorArray() {
        colors = new ArrayList<>();
        colors.add("#FFCDD2");
        colors.add("#D1C4E9");
        colors.add("#B3E5FC");
        colors.add("#C8E6C9");
        colors.add("#FFF9C4");
        colors.add("#FFCCBC");
        colors.add("#CFD8DC");
        colors.add("#F8BBD0");
        colors.add("#C5CAE9");
        colors.add("#B2EBF2");
        colors.add("#DCEDC8");
        colors.add("#FFECB3");
        colors.add("#D7CCC8");
        colors.add("#F5F5F5");
        colors.add("#FFE0B2");
        colors.add("#F0F4C3");
        colors.add("#B2DFDB");
        colors.add("#BBDEFB");
        colors.add("#E1BEE7");
    }

    /*
     * To save the message received to the local couchdb for the normal and the group chat
     */

    public String getColorCode(int position) {
        return colors.get(position);
    }

    /**
     * @param clientId is same as the userId
     */
    @SuppressWarnings("unchecked")
    public MqttAndroidClient createMQttConnection(String clientId, boolean notFromJobScheduler) {

        //Log.d(TAG ,"log22 connect to: "+clientId);
        if (mqttAndroidClient == null || mqttConnectOptions == null) {
            String serverUri = ApiOnServer.HOST_API_MQTT + ":" + ApiOnServer.MQTT_PORT;
            mqttAndroidClient = new MqttAndroidClient(mInstance, serverUri, clientId);
            //            try {
            //
            //                mqttConnectOptions.setSocketFactory(getSocketFactory(R.raw.dochat,""));
            //            }catch(Exception e){e.printStackTrace();}
            //

            mqttAndroidClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                    Log.d("log54", "connectionlost" + cause.toString());
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("eventName", MqttEvents.Disconnect.value);
                        networkConnector.setConnected(false);
                        connectionObserver.isConnected(networkConnector);
                        bus.post(obj);
                    } catch (Exception e) {

                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    Log.d("log21", topic + " " + message.toString());
                    JSONObject obj = convertMessageToJsonObject(message);

                    if (topic.equals(MqttEvents.Acknowledgement.value + "/" + userId)) {
                        /*
                         * For an acknowledgement message received
                         */
                        try {
                            obj.put("topic", topic);
                            getAcknowledgementHandler().handleAcknowledgement(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (topic.equals(MqttEvents.Message.value + "/" + userId)) {
                        try {
                            obj.put("topic", topic);
                            getPeerToPeerMessageHandler().handlePeerToPeerMessage(obj);
                        } catch (Exception e) {

                        }
                    } else if (topic.substring(0, 3).equals("Onl")) {

                        /*
                         * For a message received on the online status topic
                         */


                        /*
                         * To check for the online status
                         */

                        try {

                            obj.put("eventName", topic);
                            bus.post(obj);
                        } catch (Exception e) {

                        }
                    } else if (topic.substring(0, 4).equals(MqttEvents.Typing.value)) {

                        /*
                         * For a message received on the Typing status topic
                         */

                        if (!activeReceiverId.isEmpty()) {

                            try {

                                obj.put("eventName", topic);
                                bus.post(obj);
                            } catch (Exception e) {

                            }
                        }
                    } else if (topic.equals(MqttEvents.Calls.value + "/" + userId)) {

                        /*
                         * For message received on the actual call event
                         */

                        if (obj.getInt("type") == 0) {

                            /*
                             * For receiving of the call request,will set mine status to be busy as well and open the incoming call screen
                             */

                            JSONObject tempObj = new JSONObject();
                            tempObj.put("status", 0);


                            /*
                             * Have to retain the message of currently being busy
                             */
                            AppController.getInstance()
                                    .publish(MqttEvents.CallsAvailability.value + "/" + userId, tempObj, 0, true);
                            AppController.getInstance().setActiveOnACall(true, true);

//                            if (signInType == 0) {
//
//                                CallingApis.OpenIncomingCallScreen(obj, mInstance);
//                            } else {
//
//                                OpenIncomingCallScreen(obj, mInstance);
//                            }
                        }

                        obj.put("eventName", topic);

                        bus.post(obj);
                    } else if (topic.substring(0, 6).equals(MqttEvents.Calls.value + "A")) {

                        obj.put("eventName", topic);
                        /*
                         * Will have to eventually unsubscribe from this topic,as only use  of this is to check if opponent is available to receive the call
                         */

                        bus.post(obj);

                        /*
                         * For message received on the call event to check for the availability
                         */

                        unsubscribeToTopic(topic);
                    } else if (topic.equals(MqttEvents.ContactSync.value + "/" + userId)) {
                        Log.v("123456: ","MqttContactSync");
                        Toast.makeText(AppController.this, "Appcontroller: Mqtt API Hit ", Toast.LENGTH_SHORT).show();

                        /*
                         * To receive the result of the posty contacts api
                         */

                        try {

                            obj.put("eventName", topic);
                            bus.post(obj);
                        } catch (Exception ignored) {

                        }
                    } else if (topic.equals(MqttEvents.UserUpdates.value + "/" + userId)) {

                        try {
                            obj.put("topic", topic);
                            getUserUpdateHandler().handleUserUpdate(obj);
                        } catch (Exception ignored) {
                        }
                    } else if (topic.equals(MqttEvents.FetchChats.value + "/" + userId)) {
                        try {
                            obj.put("topic", topic);
                            getFetchChatsHandler().handleFetchChatResult(obj);
                        } catch (Exception ignored) {
                        }
                    } else if (topic.equals(MqttEvents.FetchMessages.value + "/" + userId)) {
                        try {
                            obj.put("topic", topic);
                            getFetchMessageHandler().handleFetchMessageResult(obj);
                        } catch (Exception ignored) {
                        }
                    } else if (topic.equals(MqttEvents.GroupChats.value + "/" + userId)) {

                        /*
                         *
                         * For the group chat
                         */
                        try {
                            getGroupChatMessageHandler().processGroupMessageReceived(obj);
                        } catch (Exception ignored) {
                        }
                    } else if (topic.equals(userId)) {
                        if (obj.has("data") && isForeground()) {
                            /*This is for calling events*/
                            JSONObject dataObject = obj.getJSONObject("data");
                            Gson gson = new Gson();
                            CallerDetailsResponse callerDetailsResponse =
                                    gson.fromJson(dataObject.toString(), CallerDetailsResponse.class);

                            switch (callerDetailsResponse.getAction()) {
                                case 1: {
                                    LoginDataDetails loginDataDetails =
                                            new LoginDataDetails(callerDetailsResponse.getUserId(), "",
                                                    callerDetailsResponse.getUserName(), "", null,
                                                    callerDetailsResponse.getUserImage(), "", "", "", "", "");

                                    if (!callerDetailsResponse.getUserId().equals(userId)) {
                                        getParticipant(callerDetailsResponse.getCallId(), true, loginDataDetails,
                                                callerDetailsResponse);
                                    }

                                    break;
                                }
                                case 2: {
                                    break;
                                }
                                case 3: {
                                    break;
                                }
                                case 4: {
                                    if (callerDetailsResponse.getUserBusy()) {
                                        String busyString = callerDetailsResponse.getUserName() + " " + getString(
                                                R.string.calling_user_busy);
                                        disconnectCallLiveData.postValue(
                                                new Pair(true, callerDetailsResponse.getCallId()));
                                    } else {
                                        disconnectCallLiveData.postValue(
                                                new Pair(true, callerDetailsResponse.getCallId()));
                                    }
                                    if (callerDetailsResponse.getMessage() != null && !callerDetailsResponse.getMessage().isEmpty())
                                        Toast.makeText(getApplicationContext(), callerDetailsResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                        } else {
                            /*This is for other events*/
                            if (obj.has("type")) {
                                String type = obj.getString("type");
                                try {
                                    obj.put("eventName", type);
                                    bus.post(obj);
                                } catch (Exception e) {

                                }

                                if (type != null && !type.isEmpty()) {

                                    switch (type) {
                                        case "1":
                                            //like
                                        case "2":
                                            //star like
                                        case "3":
                                            //comment
                                        case "4":
                                            //star comment
                                        case "5":
                                            //view post
                                        case "6":
                                            //view star post
                                        case "7":
                                            //follow
                                        case "8":
                                            //follow star
                                        case "9":
                                            //sign up
                                        case "10":
                                            //referral user
                                        case "11":
                                            //referral normal user
                                        case "12":
                                            //referral star user
                                            rewardNotification(obj);
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                    if (set.contains(token)) {

                        String id = null, docId = null;

                        boolean removedMessage = false;

                        int size = tokenMapping.size();
                        HashMap<String, Object> map;

                        for (int i = 0; i < size; i++) {

                            map = tokenMapping.get(i);

                            if (map.get("MQttToken").equals(token)) {

                                id = (String) map.get("messageId");
                                docId = (String) map.get("docId");
                                removedMessage = map.containsKey("messageType");

                                tokenMapping.remove(i);

                                set.remove(token);
                                break;
                            }
                        }

                        if (!removedMessage) {

                            if (docId != null && id != null) {

                                try {
                                    JSONObject obj = new JSONObject();
                                    obj.put("messageId", id);
                                    obj.put("docId", docId);
                                    obj.put("eventName", MqttEvents.MessageResponse.value);

                                    bus.post(obj);
                                } catch (JSONException e) {

                                }
                                db.updateMessageStatus(docId, id, 0, null);
                                db.removeUnsentMessage(AppController.getInstance().unsentMessageDocId, id);
                            }
                        } else {



                            /*
                             *Have to update only incase the message was removed before sending due to no internet
                             */

                            db.updateMessageStatus(docId, id, 0, null);
                            db.removeUnsentMessage(AppController.getInstance().unsentMessageDocId, id);
                        }
                    }
                }
            });

            mqttConnectOptions = new

                    MqttConnectOptions();

            mqttConnectOptions.setCleanSession(false);

            mqttConnectOptions.setMaxInflight(1000);
            mqttConnectOptions.setAutomaticReconnect(true);

            /* Use below code of socket factory when ssl required in MQTT*/
//            SocketFactory.SocketFactoryOptions socketFactoryOptions =
//                    new SocketFactory.SocketFactoryOptions();
//            try {
//                socketFactoryOptions.withCaInputStream(getResources().openRawResource(R.raw.dochat_mqtt));
//                //socketFactoryOptions.withClientP12Password("");
//                //socketFactoryOptions.withClientP12InputStream(getResources().openRawResource(R.raw.dochat_mqtt));
//                mqttConnectOptions.setSocketFactory(new SocketFactory(socketFactoryOptions));
//            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | UnrecoverableKeyException | CertificateException e) {
//                e.printStackTrace();
//            }
            //------------------------------end------------------------------//

            mqttConnectOptions.setUserName(BuildConfig.MQTT_USERNAME);
            mqttConnectOptions.setPassword(BuildConfig.MQTT_PASSWORD.toCharArray());
            JSONObject obj = new JSONObject();
            try {
                obj.put("lastSeenEnabled", sharedPref.getBoolean("enableLastSeen", true));
                obj.put("status", 2);
                obj.put("userId", userId);
            } catch (JSONException e) {

            }
            mqttConnectOptions.setWill(MqttEvents.OnlineStatus.value + "/" + userId, obj.toString().

                    getBytes(), 0, true);
            mqttConnectOptions.setKeepAliveInterval(60);


            /*
             * Has been removed from here to avoid the race condition for the mqtt connection with the mqtt broker
             */

        }

        if (!serviceAlreadyScheduled) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                ComponentName serviceName = new ComponentName(mInstance, OreoJobService.class.getName());
                PersistableBundle extras = new PersistableBundle();
                extras.putString("command", "start");

                JobInfo jobInfo =
                        (new JobInfo.Builder(MQTT_constants.MQTT_JOB_ID, serviceName)).setExtras(extras)
                                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                .setMinimumLatency(1L)
                                .setOverrideDeadline(1L)
                                .build();

                JobScheduler jobScheduler =
                        (JobScheduler) mInstance.getSystemService(Context.JOB_SCHEDULER_SERVICE);

                try {

                    jobScheduler.schedule(jobInfo);
                } catch (IllegalArgumentException errorMessage) {

                    errorMessage.printStackTrace();
                }
            } else {
                Intent changeStatus = new Intent(mInstance, AppKilled.class);
                startService(changeStatus);
            }

            serviceAlreadyScheduled = true;
        }

        if (notFromJobScheduler) {
            connectMqttClient();
        }

        return mqttAndroidClient;
    }

    private void rewardNotification(JSONObject obj) {
        try {
            String title = obj.getString("topic");
            String message = obj.getString("message");
            //
            Uri audioUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://"
                    + getPackageName()
                    + "/"
                    + R.raw.coin_spend);
            //            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this, "com.star.chat.reward").setSmallIcon(
                            R.drawable.notification)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(audioUri);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel notificationChannel;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioAttributes audioAttributes =
                        new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_ALARM)
                                .build();

                notificationChannel = new NotificationChannel("com.star.chat.reward", "reward",
                        notificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setShowBadge(true);
                notificationChannel.setSound(audioUri, audioAttributes);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            if (notificationManager != null) {
                notificationManager.notify(
                        Integer.parseInt(String.valueOf(System.currentTimeMillis() / 1000)), builder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public void publish(String topicName, JSONObject obj, int qos, boolean retained) {

        try {

            mqttAndroidClient.publish(topicName, obj.toString().getBytes(), qos, retained);
        } catch (MqttException e) {

        } catch (NullPointerException e) {

        }
    }

    private JSONObject convertMessageToJsonObject(MqttMessage message) {
        JSONObject obj = new JSONObject();
        try {
            obj = new JSONObject(new String(message.getPayload()));
        } catch (JSONException ignored) {
        }
        return obj;
    }

    public void connectMqttClient() {

        try {
            mqttAndroidClient.connect(mqttConnectOptions, mInstance, listener);
        } catch (MqttException e) {

        }
    }

    public void subscribeToTopic(String topic, int qos) {

        try {

            if (mqttAndroidClient != null) {

                mqttAndroidClient.subscribe(topic, qos);
            }
        } catch (MqttException e) {

        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public void unsubscribeToTopic(String topic) {

        try {

            if (mqttAndroidClient != null) {
                mqttAndroidClient.unsubscribe(topic);
            }
        } catch (MqttException e) {

        } catch (NullPointerException e) {

        }
    }

    public void updatePresence(int status, boolean applicationKilled) {

        if (signedIn) {

            if (status == 0) {

                /*
                 * Background
                 */

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("status", 0);

                    if (applicationKilled) {
                        if (sharedPref.getString("lastSeenTime", null) != null) {
                            obj.put("timestamp", sharedPref.getString("lastSeenTime", null));
                        } else {

                            obj.put("timestamp", Utilities.tsInGmt());
                        }
                    } else {

                        obj.put("timestamp", Utilities.tsInGmt());
                    }
                    obj.put("userId", userId);

                    obj.put("lastSeenEnabled", sharedPref.getBoolean("enableLastSeen", true));

                    publish(MqttEvents.OnlineStatus.value + "/" + userId, obj, 0, true);
                } catch (JSONException w) {

                }
            } else {

                /*
                 *Foreground
                 */

                //                if (!applicationKilled) {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("status", 1);
                    obj.put("userId", userId);
                    obj.put("lastSeenEnabled", sharedPref.getBoolean("enableLastSeen", true));

                    publish(MqttEvents.OnlineStatus.value + "/" + userId, obj, 0, true);
                } catch (JSONException w) {

                }
                //    }
            }
        }
    }

    //    /**
    //     *
    //     * For sending of the pending acknowledgements
    //     *
    //     */
    //    private void sendAcknowledgements() {
    //
    //
    //        ArrayList<JSONObject> arr = db.getUnsentAcks(unackMessageDocId);
    //
    //
    //
    //        ArrayList<String> arr = db.getUnsentAcks(unackMessageDocId);
    //
    //        if (arr.size() > 0) {
    //
    //            count=arr.size();
    //
    //            JSONObject obj;
    //
    //            for (int i = 0; i < arr.size(); i++)
    //            {
    //                obj=arr.get(i);
    //
    //
    //                try {
    //                obj=new JSONObject(arr.get(i));
    //
    //
    //
    //
    //
    //                    if(mqttAndroidClient.isConnected()) {
    //
    //
    //                        publish(MqttEvents.Acknowledgement + "/" + obj.getString("to"), obj, 2, false);
    //
    //               count--;
    //                    }
    //                }catch(JSONException e){
    //
    //                }
    //
    //            }
    //
    //
    //        }
    //
    //
    //
    //        if(count==0)   {
    //
    //            db.removeAllUnsentAcks(unackMessageDocId);
    //
    //        }
    //        count=-1;
    //
    //    }

    public void publishChatMessage(String topicName, JSONObject obj, int qos, boolean retained,
                                   HashMap<String, Object> map) {

        IMqttDeliveryToken token = null;

        try {

            obj.put("userImage", userImageUrl);
            obj.put("isStar", sessionManager.isStar());
        } catch (JSONException e) {

        }

        if (mqttAndroidClient != null && mqttAndroidClient.isConnected()) {
            try {

                token = mqttAndroidClient.publish(topicName, obj.toString().getBytes(), qos, retained);
            } catch (MqttException e) {

            }

            map.put("MQttToken", token);

            tokenMapping.add(map);
            set.add(token);
        }
    }

    public boolean canPublish() {

        return mqttAndroidClient != null && mqttAndroidClient.isConnected();
    }

    public void updateTokenMapping() {

        if (signedIn) {
            db.addMqttTokenMapping(mqttTokenDocId, tokenMapping);
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")

    private void resendUnsentMessages() {

        String documentId = AppController.getInstance().unsentMessageDocId;

        if (documentId != null) {

            ArrayList<Map<String, Object>> arr = db.getUnsentMessages(documentId);

            if (arr.size() > 0) {

                String to;
                for (int i = 0; i < arr.size(); i++) {

                    Map<String, Object> map = arr.get(i);

                    JSONObject obj = new JSONObject();
                    try {

                        to = (String) map.get("to");
                        obj.put("from", userId);
                        obj.put("to", map.get("to"));
                        obj.put("receiverIdentifier", userIdentifier);

                        String type = (String) map.get("type");

                        String message = (String) map.get("message");

                        String id = (String) map.get("id");

                        String secretId = "";

                        if (map.containsKey("secretId")) {
                            secretId = (String) map.get("secretId");
                        }

                        if (map.containsKey("isGroupMessage")) {

                            Map<String, Object> groupDetails =
                                    db.getGroupSubjectAndImageUrl((String) map.get("toDocId"));

                            if (groupDetails != null) {

                                obj.put("name", groupDetails.get("receiverName"));

                                obj.put("userImage", groupDetails.get("receiverImage"));
                            }
                        } else {
                            obj.put("name", map.get("name"));

                            obj.put("userImage", map.get("userImage"));
                        }
                        obj.put("toDocId", map.get("toDocId"));
                        obj.put("id", id);
                        obj.put("type", type);

                        if (!secretId.isEmpty()) {
                            obj.put("secretId", secretId);
                            obj.put("dTime", map.get("dTime"));
                        }

                        obj.put("timestamp", map.get("timestamp"));
                        HashMap<String, Object> mapTemp = new HashMap<>();
                        mapTemp.put("messageId", id);
                        mapTemp.put("docId", map.get("toDocId"));

                        if (type.equals("0")) {


                            /*
                             * Text message
                             */
                            try {

                                obj.put("payload",
                                        Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT).trim());
                            } catch (UnsupportedEncodingException e) {

                            }

                            if (map.containsKey("isGroupMessage")) {

                                AppController.getInstance()
                                        .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj, mapTemp);
                            } else {
                                AppController.getInstance()
                                        .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                mapTemp);
                            }
                            String tsInGmt = Utilities.tsInGmt();


                            /*
                             * Have been intentionally made to query again to avoid the case of chat being deleted b4 message is sent
                             */

                            String docId = AppController.getInstance()
                                    .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                            db.updateMessageTs(docId, id, tsInGmt);
                        } else if (type.equals("1")) {
                            /*
                             * Image message
                             */

                            Uri uri = null;
                            Bitmap bm = null;

                            try {

                                final BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;
                                BitmapFactory.decodeFile(message, options);

                                int height = options.outHeight;
                                int width = options.outWidth;

                                float density =
                                        AppController.getInstance().getResources().getDisplayMetrics().density;
                                int reqHeight;

                                reqHeight = (int) ((150 * density) * (height / width));

                                bm = AppController.getInstance()
                                        .decodeSampledBitmapFromResource(message, (int) (150 * density), reqHeight);

                                if (bm != null) {

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                    bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                                    byte[] b = baos.toByteArray();

                                    try {
                                        baos.close();
                                    } catch (IOException e) {

                                    }
                                    baos = null;

                                    File f =
                                            AppController.getInstance().convertByteArrayToFileToUpload(b, id, ".jpg");
                                    b = null;

                                    uri = Uri.fromFile(f);
                                    f = null;
                                }
                            } catch (OutOfMemoryError e) {

                            } catch (Exception e) {

                                /*
                                 *
                                 * to handle the file not found exception
                                 *
                                 *
                                 * */

                            }

                            if (uri != null) {


                                /*
                                 *
                                 *
                                 * make thumbnail
                                 *
                                 * */

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);

                                bm = null;
                                byte[] b = baos.toByteArray();

                                try {
                                    baos.close();
                                } catch (IOException e) {

                                }
                                baos = null;

                                obj.put("thumbnail", Base64.encodeToString(b, Base64.DEFAULT));

                                AppController.getInstance()
                                        .uploadFile(uri, AppController.getInstance().userId + id, 1, obj,
                                                (String) map.get("to"), id, mapTemp, secretId, null, true,
                                                map.containsKey("isGroupMessage"), map.get("groupMembersDocId"));

                                uri = null;
                                b = null;
                                bm = null;
                            }
                        } else if (type.equals("2")) {
                            /*
                             * Video message
                             */

                            Uri uri = null;

                            try {
                                File video = new File(message);

                                if (video.exists()) {

                                    byte[] b = convertFileToByteArray(video);
                                    video = null;

                                    File f =
                                            AppController.getInstance().convertByteArrayToFileToUpload(b, id, ".mp4");
                                    b = null;

                                    uri = Uri.fromFile(f);
                                    f = null;

                                    b = null;

                                    if (uri != null) {

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        Bitmap bm = ThumbnailUtils.createVideoThumbnail(message,
                                                MediaStore.Images.Thumbnails.MINI_KIND);

                                        if (bm != null) {

                                            bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                                            bm = null;
                                            b = baos.toByteArray();
                                            try {
                                                baos.close();
                                            } catch (IOException e) {

                                            }
                                            baos = null;

                                            obj.put("thumbnail", Base64.encodeToString(b, Base64.DEFAULT));

                                            AppController.getInstance()
                                                    .uploadFile(uri, AppController.getInstance().userId + id, 2, obj,
                                                            (String) map.get("to"), id, mapTemp, secretId, null, true,
                                                            map.containsKey("isGroupMessage"), map.get("groupMembersDocId"));
                                            uri = null;
                                            b = null;
                                        }
                                    }
                                }
                            } catch (OutOfMemoryError e) {

                            } catch (Exception e) {


                                /*
                                 *
                                 * to handle the file not found exception incase file has not been found
                                 *
                                 * */

                            }
                        } else if (type.equals("3")) {


                            /*
                             * Location message
                             */

                            try {
                                obj.put("payload",
                                        (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                            } catch (UnsupportedEncodingException e) {

                            }
                            if (map.containsKey("isGroupMessage")) {

                                AppController.getInstance()
                                        .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj, mapTemp);
                            } else {
                                AppController.getInstance()
                                        .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                mapTemp);
                            }
                            String tsInGmt = Utilities.tsInGmt();
                            String docId = AppController.getInstance()
                                    .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                            db.updateMessageTs(docId, id, tsInGmt);
                        } else if (type.equals("4")) {

                            /*
                             * Follow message
                             */

                            try {

                                obj.put("payload",
                                        (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                            } catch (UnsupportedEncodingException e) {

                            }
                            if (map.containsKey("isGroupMessage")) {

                                AppController.getInstance()
                                        .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj, mapTemp);
                            } else {

                                AppController.getInstance()
                                        .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                mapTemp);
                            }
                            String tsInGmt = Utilities.tsInGmt();
                            String docId = AppController.getInstance()
                                    .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                            db.updateMessageTs(docId, id, tsInGmt);
                        } else if (type.equals("5")) {

                            /*
                             * Audio message
                             */

                            Uri uri;
                            try {

                                File audio = new File(message);

                                if (audio.exists()) {

                                    byte[] b = convertFileToByteArray(audio);
                                    audio = null;

                                    File f =
                                            AppController.getInstance().convertByteArrayToFileToUpload(b, id, ".3gp");
                                    b = null;

                                    uri = Uri.fromFile(f);
                                    f = null;

                                    b = null;

                                    if (uri != null) {

                                        AppController.getInstance()
                                                .uploadFile(uri, AppController.getInstance().userId + id, 5, obj,
                                                        (String) map.get("to"), id, mapTemp, secretId, null,
                                                        map.containsKey("toDelete"), map.containsKey("isGroupMessage"),
                                                        map.get("groupMembersDocId"));
                                    }
                                }
                            } catch (Exception e) {


                                /*
                                 *
                                 * to handle the file not found exception incase file has not been found
                                 *
                                 * */

                            }
                        } else if (type.equals("6")) {
                            /*
                             * Sticker
                             */
                            try {
                                obj.put("payload",
                                        (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                            } catch (UnsupportedEncodingException e) {

                            }

                            if (map.containsKey("isGroupMessage")) {

                                AppController.getInstance()
                                        .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj, mapTemp);
                            } else {
                                AppController.getInstance()
                                        .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                mapTemp);
                            }
                            String tsInGmt = Utilities.tsInGmt();
                            String docId = AppController.getInstance()
                                    .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                            db.updateMessageTs(docId, id, tsInGmt);
                        } else if (type.equals("7")) {
                            /*
                             *Doodle
                             */

                            Uri uri = null;
                            Bitmap bm = null;

                            try {

                                final BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;
                                BitmapFactory.decodeFile(message, options);

                                int height = options.outHeight;
                                int width = options.outWidth;

                                float density =
                                        AppController.getInstance().getResources().getDisplayMetrics().density;
                                int reqHeight;

                                reqHeight = (int) ((150 * density) * (height / width));

                                bm = AppController.getInstance()
                                        .decodeSampledBitmapFromResource(message, (int) (150 * density), reqHeight);

                                if (bm != null) {

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                    bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                                    byte[] b = baos.toByteArray();

                                    try {
                                        baos.close();
                                    } catch (IOException e) {

                                    }
                                    baos = null;

                                    File f =
                                            AppController.getInstance().convertByteArrayToFileToUpload(b, id, ".jpg");
                                    b = null;

                                    uri = Uri.fromFile(f);
                                    f = null;
                                }
                            } catch (OutOfMemoryError e) {

                            } catch (Exception e) {

                                /*
                                 *
                                 * to handle the file not found exception
                                 *
                                 *
                                 * */

                            }

                            if (uri != null) {


                                /*
                                 *
                                 *
                                 * make thumbnail
                                 *
                                 * */

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);

                                bm = null;
                                byte[] b = baos.toByteArray();

                                try {
                                    baos.close();
                                } catch (IOException e) {

                                }
                                baos = null;

                                obj.put("thumbnail", Base64.encodeToString(b, Base64.DEFAULT));

                                AppController.getInstance()
                                        .uploadFile(uri, AppController.getInstance().userId + id, 7, obj,
                                                (String) map.get("to"), id, mapTemp, secretId, null, true,
                                                map.containsKey("isGroupMessage"), map.get("groupMembersDocId"));

                                uri = null;
                                b = null;
                                bm = null;
                            }
                        } else if (type.equals("8")) {
                            /*
                             *Gif
                             */

                            try {
                                obj.put("payload",
                                        (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                            } catch (UnsupportedEncodingException e) {

                            }

                            if (map.containsKey("isGroupMessage")) {

                                AppController.getInstance()
                                        .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj, mapTemp);
                            } else {
                                AppController.getInstance()
                                        .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                mapTemp);
                            }
                            String tsInGmt = Utilities.tsInGmt();
                            String docId = AppController.getInstance()
                                    .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                            db.updateMessageTs(docId, id, tsInGmt);
                        } else if (type.equals("9")) {

                            /*
                             * Document
                             */

                            Uri uri;
                            try {

                                File document = new File(message);

                                if (document.exists()) {

                                    uri = Uri.fromFile(document);

                                    document = null;

                                    if (uri != null) {

                                        AppController.getInstance()
                                                .uploadFile(uri, AppController.getInstance().userId + id, 9, obj,
                                                        (String) map.get("to"), id, mapTemp, secretId,

                                                        (String) map.get("extension"), false, map.containsKey("isGroupMessage"),
                                                        map.get("groupMembersDocId"));
                                    }
                                }
                            } catch (Exception e) {


                                /*
                                 *
                                 * to handle the file not found exception incase file has not been found
                                 *
                                 * */

                            }
                        } else if (type.equals("10")) {
                            /*
                             * Reply message
                             */
                            String replyType = (String) map.get("replyType");

                            String previousType = (String) map.get("previousType");

                            obj.put("replyType", replyType);

                            obj.put("previousFrom", map.get("previousFrom"));

                            obj.put("previousType", previousType);

                            obj.put("previousId", map.get("previousId"));

                            obj.put("previousReceiverIdentifier", map.get("previousReceiverIdentifier"));
                            if (previousType.equals("9")) {

                                obj.put("previousFileType", map.get("previousFileType"));
                            }

                            if (previousType.equals("1") || previousType.equals("2") || previousType.equals(
                                    "7")) {
                                switch (Integer.parseInt(previousType)) {

                                    case 1: {


                                        /*
                                         *Image
                                         */
                                        Bitmap bm =
                                                decodeSampledBitmapFromResource((String) map.get("previousPayload"), 180,
                                                        180);

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                                        bm = null;
                                        byte[] b = baos.toByteArray();

                                        try {
                                            baos.close();
                                        } catch (IOException e) {

                                        }
                                        baos = null;

                                        obj.put("previousPayload", Base64.encodeToString(b, Base64.DEFAULT).trim());

                                        break;
                                    }
                                    case 2: {

                                        /*
                                         * Video
                                         */
                                        Bitmap bm =
                                                ThumbnailUtils.createVideoThumbnail((String) map.get("previousPayload"),
                                                        MediaStore.Images.Thumbnails.MINI_KIND);

                                        bm = Bitmap.createScaledBitmap(bm, 180, 180, false);

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                                        bm = null;
                                        byte[] b = baos.toByteArray();

                                        try {
                                            baos.close();
                                        } catch (IOException e) {

                                        }
                                        baos = null;

                                        obj.put("previousPayload", Base64.encodeToString(b, Base64.DEFAULT).trim());
                                        break;
                                    }
                                    case 7: {
                                        /*
                                         * Doodle
                                         */

                                        Bitmap bm =
                                                decodeSampledBitmapFromResource((String) map.get("previousPayload"), 180,
                                                        180);

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                                        bm = null;
                                        byte[] b = baos.toByteArray();

                                        try {
                                            baos.close();
                                        } catch (IOException e) {

                                        }
                                        baos = null;

                                        obj.put("previousPayload", Base64.encodeToString(b, Base64.DEFAULT).trim());
                                        break;
                                    }
                                }
                            } else {

                                obj.put("previousPayload", map.get("previousPayload"));
                            }

                            switch (Integer.parseInt(replyType)) {

                                case 0: {
                                    /*
                                     * Text
                                     */

                                    try {

                                        obj.put("payload",
                                                Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT).trim());
                                    } catch (UnsupportedEncodingException e) {

                                    }

                                    if (map.containsKey("isGroupMessage")) {

                                        AppController.getInstance()
                                                .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj,
                                                        mapTemp);
                                    } else {
                                        AppController.getInstance()
                                                .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                        mapTemp);
                                    }
                                    String tsInGmt = Utilities.tsInGmt();
                                    String docId = AppController.getInstance()
                                            .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                                    db.updateMessageTs(docId, id, tsInGmt);
                                    break;
                                }
                                case 1: {
                                    /*
                                     * Image
                                     */

                                    Uri uri = null;
                                    Bitmap bm = null;

                                    try {

                                        final BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;
                                        BitmapFactory.decodeFile(message, options);

                                        int height = options.outHeight;
                                        int width = options.outWidth;

                                        float density =
                                                AppController.getInstance().getResources().getDisplayMetrics().density;
                                        int reqHeight;

                                        reqHeight = (int) ((150 * density) * (height / width));

                                        bm = AppController.getInstance()
                                                .decodeSampledBitmapFromResource(message, (int) (150 * density), reqHeight);

                                        if (bm != null) {

                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                                            byte[] b = baos.toByteArray();

                                            try {
                                                baos.close();
                                            } catch (IOException e) {

                                            }
                                            baos = null;

                                            File f =
                                                    AppController.getInstance().convertByteArrayToFileToUpload(b, id, ".jpg");
                                            b = null;

                                            uri = Uri.fromFile(f);
                                            f = null;
                                        }
                                    } catch (OutOfMemoryError e) {

                                    } catch (Exception e) {

                                        /*
                                         *
                                         * to handle the file not found exception
                                         *
                                         *
                                         * */

                                    }

                                    if (uri != null) {


                                        /*
                                         *
                                         *
                                         * make thumbnail
                                         *
                                         * */

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                        bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);

                                        bm = null;
                                        byte[] b = baos.toByteArray();

                                        try {
                                            baos.close();
                                        } catch (IOException e) {

                                        }
                                        baos = null;

                                        obj.put("thumbnail", Base64.encodeToString(b, Base64.DEFAULT));

                                        AppController.getInstance()
                                                .uploadFile(uri, AppController.getInstance().userId + id, 1, obj,
                                                        (String) map.get("to"), id, mapTemp, secretId, null, true,
                                                        map.containsKey("isGroupMessage"), map.get("groupMembersDocId"));

                                        uri = null;
                                        b = null;
                                        bm = null;
                                    }

                                    break;
                                }
                                case 2: {
                                    /*
                                     * Video
                                     */
                                    Uri uri = null;

                                    try {
                                        File video = new File(message);

                                        if (video.exists()) {

                                            byte[] b = convertFileToByteArray(video);
                                            video = null;

                                            File f =
                                                    AppController.getInstance().convertByteArrayToFileToUpload(b, id, ".mp4");
                                            b = null;

                                            uri = Uri.fromFile(f);
                                            f = null;

                                            b = null;

                                            if (uri != null) {

                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                Bitmap bm = ThumbnailUtils.createVideoThumbnail(message,
                                                        MediaStore.Images.Thumbnails.MINI_KIND);

                                                if (bm != null) {

                                                    bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                                                    bm = null;
                                                    b = baos.toByteArray();
                                                    try {
                                                        baos.close();
                                                    } catch (IOException e) {

                                                    }
                                                    baos = null;

                                                    obj.put("thumbnail", Base64.encodeToString(b, Base64.DEFAULT));

                                                    AppController.getInstance()
                                                            .uploadFile(uri, AppController.getInstance().userId + id, 2, obj,
                                                                    (String) map.get("to"), id, mapTemp, secretId, null, true,
                                                                    map.containsKey("isGroupMessage"), map.get("groupMembersDocId"));
                                                    uri = null;
                                                    b = null;
                                                }
                                            }
                                        }
                                    } catch (OutOfMemoryError e) {

                                    } catch (Exception e) {


                                        /*
                                         *
                                         * to handle the file not found exception incase file has not been found
                                         *
                                         * */

                                    }

                                    break;
                                }
                                case 3: {
                                    /*
                                     * Location
                                     */
                                    try {
                                        obj.put("payload",
                                                (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                                    } catch (UnsupportedEncodingException e) {

                                    }
                                    if (map.containsKey("isGroupMessage")) {

                                        AppController.getInstance()
                                                .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj,
                                                        mapTemp);
                                    } else {
                                        AppController.getInstance()
                                                .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                        mapTemp);
                                    }
                                    String tsInGmt = Utilities.tsInGmt();
                                    String docId = AppController.getInstance()
                                            .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                                    db.updateMessageTs(docId, id, tsInGmt);

                                    break;
                                }
                                case 4: {
                                    /*
                                     * Follow
                                     */

                                    try {

                                        obj.put("payload",
                                                (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                                    } catch (UnsupportedEncodingException e) {

                                    }
                                    if (map.containsKey("isGroupMessage")) {

                                        AppController.getInstance()
                                                .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj,
                                                        mapTemp);
                                    } else {

                                        AppController.getInstance()
                                                .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                        mapTemp);
                                    }

                                    String tsInGmt = Utilities.tsInGmt();
                                    String docId = AppController.getInstance()
                                            .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                                    db.updateMessageTs(docId, id, tsInGmt);
                                    break;
                                }
                                case 5: {
                                    /*
                                     * Audio
                                     */

                                    Uri uri;
                                    try {

                                        File audio = new File(message);

                                        if (audio.exists()) {

                                            byte[] b = convertFileToByteArray(audio);
                                            audio = null;

                                            File f =
                                                    AppController.getInstance().convertByteArrayToFileToUpload(b, id, ".3gp");
                                            b = null;

                                            uri = Uri.fromFile(f);
                                            f = null;

                                            b = null;

                                            if (uri != null) {

                                                AppController.getInstance()
                                                        .uploadFile(uri, AppController.getInstance().userId + id, 5, obj,
                                                                (String) map.get("to"), id, mapTemp, secretId, null,
                                                                map.containsKey("toDelete"), map.containsKey("isGroupMessage"),
                                                                map.get("groupMembersDocId"));
                                            }
                                        }
                                    } catch (Exception e) {


                                        /*
                                         *
                                         * to handle the file not found exception incase file has not been found
                                         *
                                         * */

                                    }
                                    break;
                                }
                                case 6: {
                                    /*
                                     * Sticker
                                     */
                                    try {
                                        obj.put("payload",
                                                (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                                    } catch (UnsupportedEncodingException e) {

                                    }

                                    if (map.containsKey("isGroupMessage")) {

                                        AppController.getInstance()
                                                .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj,
                                                        mapTemp);
                                    } else {
                                        AppController.getInstance()
                                                .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                        mapTemp);
                                    }
                                    String tsInGmt = Utilities.tsInGmt();
                                    String docId = AppController.getInstance()
                                            .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                                    db.updateMessageTs(docId, id, tsInGmt);

                                    break;
                                }
                                case 7: {
                                    /*
                                     * Doodle
                                     */
                                    Uri uri = null;
                                    Bitmap bm = null;

                                    try {

                                        final BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;
                                        BitmapFactory.decodeFile(message, options);

                                        int height = options.outHeight;
                                        int width = options.outWidth;

                                        float density =
                                                AppController.getInstance().getResources().getDisplayMetrics().density;
                                        int reqHeight;

                                        reqHeight = (int) ((150 * density) * (height / width));

                                        bm = AppController.getInstance()
                                                .decodeSampledBitmapFromResource(message, (int) (150 * density), reqHeight);

                                        if (bm != null) {

                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                                            byte[] b = baos.toByteArray();

                                            try {
                                                baos.close();
                                            } catch (IOException e) {

                                            }
                                            baos = null;

                                            File f =
                                                    AppController.getInstance().convertByteArrayToFileToUpload(b, id, ".jpg");
                                            b = null;

                                            uri = Uri.fromFile(f);
                                            f = null;
                                        }
                                    } catch (OutOfMemoryError e) {

                                    } catch (Exception e) {

                                        /*
                                         *
                                         * to handle the file not found exception
                                         *
                                         *
                                         * */

                                    }

                                    if (uri != null) {


                                        /*
                                         *
                                         *
                                         * Make thumbnail
                                         *
                                         * */

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                                        bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);

                                        bm = null;
                                        byte[] b = baos.toByteArray();

                                        try {
                                            baos.close();
                                        } catch (IOException e) {

                                        }
                                        baos = null;

                                        obj.put("thumbnail", Base64.encodeToString(b, Base64.DEFAULT));

                                        AppController.getInstance()
                                                .uploadFile(uri, AppController.getInstance().userId + id, 7, obj,
                                                        (String) map.get("to"), id, mapTemp, secretId, null, true,
                                                        map.containsKey("isGroupMessage"), map.get("groupMembersDocId"));

                                        uri = null;
                                        b = null;
                                        bm = null;
                                    }

                                    break;
                                }
                                case 8: {
                                    /*
                                     * Gif
                                     */

                                    try {
                                        obj.put("payload",
                                                (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                                    } catch (UnsupportedEncodingException e) {

                                    }

                                    if (map.containsKey("isGroupMessage")) {

                                        AppController.getInstance()
                                                .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj,
                                                        mapTemp);
                                    } else {
                                        AppController.getInstance()
                                                .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                        mapTemp);
                                    }
                                    String tsInGmt = Utilities.tsInGmt();
                                    String docId = AppController.getInstance()
                                            .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                                    db.updateMessageTs(docId, id, tsInGmt);
                                    break;
                                }
                                case 9: {

                                    /*
                                     * Document
                                     */
                                    Uri uri;
                                    try {

                                        File document = new File(message);

                                        if (document.exists()) {

                                            uri = Uri.fromFile(document);

                                            document = null;

                                            if (uri != null) {

                                                AppController.getInstance()
                                                        .uploadFile(uri, AppController.getInstance().userId + id, 9, obj,
                                                                (String) map.get("to"), id, mapTemp, secretId,
                                                                (String) map.get("extension"), false,
                                                                map.containsKey("isGroupMessage"), map.get("groupMembersDocId"));
                                            }
                                        }
                                    } catch (Exception e) {


                                        /*
                                         *
                                         * to handle the file not found exception incase file has not been found
                                         *
                                         * */

                                    }

                                    break;
                                }
                                case 13: {

                                    /*
                                     * Post
                                     */

                                    try {
                                        obj.put("payload",
                                                (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                                        obj.put("postId", (String) map.get("postId"));
                                        obj.put("postTitle", (String) map.get("postTitle"));
                                        obj.put("postType", (int) map.get("postType"));
                                    } catch (UnsupportedEncodingException e) {

                                    }

                                    if (map.containsKey("isGroupMessage")) {

                                        AppController.getInstance()
                                                .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj,
                                                        mapTemp);
                                    } else {
                                        AppController.getInstance()
                                                .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                        mapTemp);
                                    }
                                    String tsInGmt = Utilities.tsInGmt();
                                    String docId = AppController.getInstance()
                                            .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                                    db.updateMessageTs(docId, id, tsInGmt);

                                    break;
                                }
                            }
                        } else if (type.equals("11")) {


                            /*
                             * Remove message
                             */
                            try {

                                obj.put("payload",
                                        Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT).trim());

                                obj.put("removedAt", map.get("timestamp"));
                            } catch (UnsupportedEncodingException e) {

                            }

                            if (map.containsKey("isGroupMessage")) {

                                AppController.getInstance()
                                        .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj, mapTemp);
                            } else {
                                AppController.getInstance()
                                        .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                mapTemp);
                            }
                        } else if (type.equals("12")) {




                            /*
                             * Edit message
                             */
                            try {

                                obj.put("editedAt", map.get("timestamp"));
                                obj.put("payload",
                                        Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT).trim());
                            } catch (UnsupportedEncodingException e) {

                            }

                            if (map.containsKey("isGroupMessage")) {

                                AppController.getInstance()
                                        .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj, mapTemp);
                            } else {
                                AppController.getInstance()
                                        .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                mapTemp);
                            }
                            String tsInGmt = Utilities.tsInGmt();


                            /*
                             * Have been intentionally made to query again to avoid the case of chat being deleted b4 message is sent
                             */

                            String docId = AppController.getInstance()
                                    .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                            db.updateMessageTs(docId, id, tsInGmt);
                        } else if (type.equals("13")) {

                            /*
                             * Post message
                             */

                            try {

                                obj.put("payload",
                                        (Base64.encodeToString(message.getBytes("UTF-8"), Base64.DEFAULT)).trim());
                                obj.put("postId", (String) map.get("postId"));
                                obj.put("postTitle", (String) map.get("postTitle"));
                                obj.put("postType", (int) map.get("postType"));
                            } catch (UnsupportedEncodingException e) {

                            }
                            if (map.containsKey("isGroupMessage")) {

                                AppController.getInstance()
                                        .publishGroupChatMessage((String) map.get("groupMembersDocId"), obj, mapTemp);
                            } else {

                                AppController.getInstance()
                                        .publishChatMessage(MqttEvents.Message.value + "/" + to, obj, 1, false,
                                                mapTemp);
                            }
                            String tsInGmt = Utilities.tsInGmt();
                            String docId = AppController.getInstance()
                                    .findDocumentIdOfReceiver((String) map.get("to"), secretId);
                            db.updateMessageTs(docId, id, tsInGmt);
                        }
                    } catch (JSONException e) {

                    } catch (OutOfMemoryError e) {

                    }
                }
            }
        }
    }

    public void disconnect() {

        try {
            if (mqttAndroidClient != null) mqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

        activeActivitiesCount++;


        /*
         *
         * As app can also be started when clicked on the chat notification
         *
         *
         * */

        if(activity.getClass().getSimpleName().equals(CallingActivity.class.getSimpleName())){
            AppController.getInstance().setActiveOnACall(true,true);
        }

        //Log.d("log1", activity.getClass().getSimpleName());
        if (AppController.getInstance().getSignedIn() && activity.getClass()
                .getSimpleName()
                .equals("MainActivity")) {
            //            new PublishPost().retryPublishingPosts();
            //            sendAcknowledgements();
            foreground = true;

            setApplicationKilled(false);
            updatePresence(1, false);


            /*
             *
             * When the app started,update the status(JUST have put it for checking)
             *
             */

            //  if (signedIn) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("status", 1);
                publish(MqttEvents.CallsAvailability.value + "/" + userId, obj, 0, true);
                AppController.getInstance().setActiveOnACall(false, true);
            } catch (JSONException e) {

            }
            //      }
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {

        if (activity.getClass().getSimpleName().equals("ContactSyncLandingPage")) {

            checkActiveCalls();
        }

        //Log.d("log2", activity.getClass().getSimpleName());
        activeActivitiesCount--;

        if(activity.getClass().getSimpleName().equals(CallingActivity.class.getSimpleName())){
            AppController.getInstance().setActiveOnACall(false,true);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

        //        Log.d("log3", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityResumed(Activity activity) {

        //Log.d("log4", activity.getClass().getSimpleName());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

        //        Log.d("log5", activity.getClass().getSimpleName());
    }

    //    /**
    //     * To remove the message to be deleted from list of the active timers
    //     */
    //
    //
    //    public void removeMessageToBeDeleted(String docId, String messageId) {
    //
    //
    //    }

    public void updateReconnected() {
        //  Log.d("log55", "reconnection");

        if (!applicationKilled) {
            updatePresence(1, false);
        } else {

            updatePresence(0, true);
        }

        try {

            JSONObject obj = new JSONObject();
            obj.put("eventName", MqttEvents.Connect.value);
            networkConnector.setConnected(true);
            connectionObserver.isConnected(networkConnector);
            bus.post(obj);
        } catch (Exception e) {

        }

        if (flag) {

            flag = false;
            subscribeToTopic(MqttEvents.Message.value + "/" + userId, 1);
            subscribeToTopic(MqttEvents.Acknowledgement.value + "/" + userId, 2);
            subscribeToTopic(MqttEvents.Calls.value + "/" + userId, 0);

            subscribeToTopic(MqttEvents.UserUpdates.value + "/" + userId, 1);
            subscribeToTopic(MqttEvents.GroupChats.value + "/" + userId, 1);

            /*
             *
             * Will have to control their subscription to be requirement based
             */

            subscribeToTopic(MqttEvents.FetchMessages.value + "/" + userId, 1);
        }

        if (signedIn) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("status", 1);
                publish(MqttEvents.CallsAvailability.value + "/" + userId, obj, 0, true);
                AppController.getInstance().setActiveOnACall(false, true);
            } catch (JSONException e) {

            }
        }
        resendUnsentMessages();
    }

    public void updateLastSeenSettings(boolean lastSeenSetting) {

        sharedPref.edit().putBoolean("enableLastSeen", lastSeenSetting).apply();
    }

    @SuppressWarnings("unchecked")
    public void getCurrentTime() {

        new FetchTime().execute();
    }

    public void cutCallOnKillingApp(boolean appCrashed) {

        /*
         * Have to make myself available and inform other user of the call getting cut
         */

        try {

            if (activeCallId != null && activeCallerId != null) {
                JSONObject obj = new JSONObject();

                obj.put("callId", activeCallId);
                obj.put("userId", activeCallerId);
                obj.put("type", 2);

                AppController.getInstance()
                        .publish(MqttEvents.Calls.value + "/" + activeCallerId, obj, 0, false);

                removeCurrentCallDetails();

                if (appCrashed) {

                    JSONObject obj2 = new JSONObject();

                    obj2.put("eventName", "appCrashed");

                    bus.post(obj2);
                }
            }
            JSONObject obj = new JSONObject();
            obj.put("status", 1);
            AppController.getInstance()
                    .publish(
                            MqttEvents.CallsAvailability.value + "/" + AppController.getInstance().getUserId(),
                            obj, 0, true);
            AppController.getInstance().setActiveOnACall(false, true);
        } catch (JSONException e) {

        }
    }

    public String randomString() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        sb.append("PnPLabs3Embed");
        return sb.toString();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void registerContactsObserver() {

        if (contactSynced && !registeredObserver) {
            registeredObserver = true;

            getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, false,
                    contactUpdateObserver);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                getContentResolver().registerContentObserver(ContactsContract.DeletedContacts.CONTENT_URI,
                        false, contactDeletedObserver);
            }
        }
    }

    public void unregisterContactsObserver() {

        if (contactSynced) {

            getContentResolver().unregisterContentObserver(contactUpdateObserver);

            getContentResolver().unregisterContentObserver(contactDeletedObserver);
            registeredObserver = false;
        }
    }

    /**
     * @param arrayList containing the list of the contact ids which have been deleted
     */

    public void updateDeletedContact(ArrayList<String> arrayList) {

        ArrayList<Map<String, Object>> allContactDetails = db.fetchAllContacts(allContactsDocId);
        Map<String, Object> localContact;
        JSONObject obj;
        JSONArray arr = new JSONArray();

        for (int i = 0; i < allContactDetails.size(); i++) {
            localContact = allContactDetails.get(i);

            if (arrayList.contains(localContact.get("contactId"))) {

                obj = new JSONObject();
                try {

                    obj.put("number", localContact.get("phoneNumber"));

                    obj.put("type", localContact.get("type"));
                } catch (JSONException e) {

                }

                arr.put(obj);
            }
        }
        if (arr.length() > 0) {

            try {
                obj = new JSONObject();

                obj.put("contacts", arr);

                hitDeleteContactApi(obj);
            } catch (JSONException e) {

            }
        }
    }

    /**
     * If a new contact has been added
     */

    public void putUpdatedContact(ArrayList<Map<String, Object>> arrayList) {

        ArrayList<Map<String, Object>> allContactDetails = db.fetchAllContacts(allContactsDocId);

        Map<String, Object> localContact;
        JSONObject obj;
        JSONArray arr = new JSONArray();

        boolean contactAlreadyExists;

        Map<String, Object> contactChanged;

        for (int j = 0; j < arrayList.size(); j++) {

            contactAlreadyExists = false;

            contactChanged = arrayList.get(j);

            for (int i = 0; i < allContactDetails.size(); i++) {
                localContact = allContactDetails.get(i);

                /*
                 * Considering only change of the phone number and the name in the contacts list
                 */

                if ((contactChanged.get("contactId")).equals(localContact.get("contactId"))) {
                    contactAlreadyExists = true;

                    if (!((contactChanged.get("phoneNumber")).equals(localContact.get("phoneNumber")))) {

                        /*
                         * This means phone number has been changed,so will need to send to the server
                         */

                        obj = new JSONObject();
                        try {

                            obj.put("number", contactChanged.get("phoneNumber"));

                            obj.put("type", contactChanged.get("type"));
                        } catch (JSONException e) {

                        }
                        arr.put(obj);

                        Map<String, Object> wasActiveContact =
                                db.wasActiveContact(contactsDocId, (String) contactChanged.get("contactId"));

                        if ((boolean) wasActiveContact.get("wasActive")) {
                            /*
                             * Follow whose number has been changed was on active contacts list
                             */

                            Map<String, Object> dirtyContact = new HashMap<>();
                            dirtyContact.put("contactUid", wasActiveContact.get("contactUid"));
                            dirtyContact.put("contactNumber", contactChanged.get("phoneNumber"));
                            dirtyContact.put("contactIdentifier", wasActiveContact.get("contactIdentifier"));

                            dirtyContact.put("contactId", contactChanged.get("contactId"));

                            dirtyContact.put("contactName", contactChanged.get("userName"));

                            dirtyContacts.add(dirtyContact);
                        } else {
                            /*
                             * Follow was in list of the inactive contacts
                             */

                            Map<String, Object> inactiveContact = new HashMap<>();
                            inactiveContact.put("contactUid", wasActiveContact.get("contactUid"));
                            inactiveContact.put("phoneNumber", contactChanged.get("phoneNumber"));
                            inactiveContact.put("contactIdentifier", wasActiveContact.get("contactIdentifier"));

                            inactiveContact.put("contactId", contactChanged.get("contactId"));

                            inactiveContact.put("userName", contactChanged.get("userName"));

                            inactiveContacts.add(inactiveContact);
                        }
                        db.updateAllContactNunber(allContactsDocId, (String) contactChanged.get("phoneNumber"),
                                (String) contactChanged.get("contactId"));
                    }

                    if (!((contactChanged.get("userName")).equals(localContact.get("userName")))) {



                        /*
                         * If just the name changed,then update the value in the local contacts,calls and chat list but no need to send to the server
                         */

                        try {

                            Map<String, Object> result =
                                    db.updateActiveContactName(contactsDocId, (String) contactChanged.get("userName"),
                                            (String) contactChanged.get("contactId"));

                            if ((boolean) result.get("updated")) {

                                /*
                                 * If the contact exists in list of active users
                                 */

                                JSONObject jsonObject = new JSONObject();

                                jsonObject.put("eventName", "ContactNameUpdated");
                                jsonObject.put("contactName", contactChanged.get("userName"));
                                jsonObject.put("contactUid", result.get("contactUid"));
                                bus.post(jsonObject);
                            }


                            /*
                             * To update the list of all contacts for the new name
                             */
                            db.updateAllContactName(allContactsDocId, (String) contactChanged.get("userName"),
                                    (String) contactChanged.get("contactId"));
                        } catch (JSONException e) {

                        }
                    }
                }
            }

            if (!contactAlreadyExists) {

                obj = new JSONObject();
                try {

                    obj.put("number", contactChanged.get("phoneNumber"));

                    obj.put("type", contactChanged.get("type"));

                    newContacts.add(contactChanged);
                } catch (JSONException e) {

                }
                arr.put(obj);

                db.addToAllContacts(allContactsDocId, contactChanged);
            }
        }

        if (arr.length() > 0) {

            try {
                obj = new JSONObject();

                obj.put("contacts", arr);

                hitContactAddedApi(obj);
            } catch (JSONException e) {

            }
        }
    }

    private void hitDeleteContactApi(final JSONObject obj) {

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.DELETE, ApiOnServer.SYNC_CONTACTS, null,
                        new com.android.volley.Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                            SessionObserver sessionObserver = new SessionObserver();
                            sessionObserver.getObservable()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DisposableObserver<Boolean>() {
                                        @Override
                                        public void onNext(Boolean flag) {
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    hitDeleteContactApi(obj);
                                                }
                                            }, 1000);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                        }
                                    });
                            sessionApiCall.getNewSession(sessionObserver);
                        }
                    }
                }

                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("authorization", apiToken);

                        headers.put("contacts", obj.toString());

                        headers.put("lang", Constants.LANGUAGE);

                        return headers;
                    }
                };

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "deleteContactsApiRequest");
    }

    private void hitContactAddedApi(JSONObject obj) {

        pendingContactUpdateRequests++;

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.PUT, ApiOnServer.SYNC_CONTACTS, obj,
                        new com.android.volley.Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                            }
                        }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                            SessionObserver sessionObserver = new SessionObserver();
                            sessionObserver.getObservable()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DisposableObserver<Boolean>() {
                                        @Override
                                        public void onNext(Boolean flag) {
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    hitContactAddedApi(obj);
                                                }
                                            }, 1000);
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                        }
                                    });
                            sessionApiCall.getNewSession(sessionObserver);
                        }
                    }
                }

                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", sessionManager.getGuestToken());

                        headers.put("token", apiToken);

                        return headers;
                    }
                };

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "putContactsApiRequest");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void checkContactAddedOrUpdated() {

        ArrayList<Map<String, Object>> localContactsList = new ArrayList<>();

        ArrayList<String> alreadyAddedContacts = new ArrayList<>();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        /*
         * For added or updated contact
         */
        String time = String.valueOf(Utilities.getGmtEpoch());
        String lastChecked =
                AppController.getInstance().getSharedPreferences().getString("lastUpdated", time);
        try {

            Cursor cur = AppController.getInstance()
                    .getContentResolver()
                    .query(uri, null, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + ">=?",
                            new String[]{lastChecked}, null);

            Map<String, Object> contactsInfo;
            if (cur != null && cur.getCount() > 0) {

                int type;

                while (cur.moveToNext()) {

                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (Integer.parseInt(
                            cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor pCur =
                                getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                                        null);

                        if (pCur != null) {

                            while (pCur.moveToNext()) {

                                String phoneNo = pCur.getString(
                                        pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                /*
                                 * To replace brackets or the dashes or the spaces
                                 *
                                 * */

                                phoneNo = phoneNo.replaceAll("[\\D\\s\\-()]", "");

                                /*
                                 * To get all the phone numbers for the given contacts
                                 */
                                if (phoneNo != null && !phoneNo.trim().equals("null") && !phoneNo.trim()
                                        .isEmpty()) {


                                    /*
                                     * By default assuming the number is of unknown type
                                     */

                                    type = -1;
                                    switch (pCur.getInt(
                                            pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))) {
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                            type = 1;
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                            type = 0;
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                                            type = 8;
                                    }

                                    if (!alreadyAddedContacts.contains(phoneNo)) {

                                        alreadyAddedContacts.add(phoneNo);
                                        contactsInfo = new HashMap<>();

                                        contactsInfo.put("phoneNumber", "+" + phoneNo);
                                        contactsInfo.put("userName", name);
                                        contactsInfo.put("type", type);
                                        contactsInfo.put("contactId", id);

                                        localContactsList.add(contactsInfo);
                                    }
                                }
                            }

                            pCur.close();
                        }
                    }
                }

                if (localContactsList.size() > 0) putUpdatedContact(localContactsList);
            }

            if (cur != null) {
                cur.close();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();

            generatePushForContactUpdate();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void checkContactDeleted() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Uri uri = ContactsContract.DeletedContacts.CONTENT_URI;

            /*
             * For deleted contact
             */
            String time = String.valueOf(Utilities.getGmtEpoch());
            String lastChecked =
                    AppController.getInstance().getSharedPreferences().getString("lastDeleted", time);

            Cursor cur = getContentResolver().query(uri, null,
                    ContactsContract.DeletedContacts.CONTACT_DELETED_TIMESTAMP + ">=?",
                    new String[]{lastChecked}, null);

            if (cur != null && cur.getCount() > 0) {

                ArrayList<String> deletedContactIds = new ArrayList<>();

                while (cur.moveToNext()) {

                    String id =
                            cur.getString(cur.getColumnIndex(ContactsContract.DeletedContacts.CONTACT_ID));

                    deletedContactIds.add(id);
                }

                /*
                 * Have to tell the server of the contact being deleted and also to remove from the local list
                 */

                updateDeletedContact(deletedContactIds);
            }

            if (cur != null) {
                cur.close();
            }
        }
    }

    /*
     * To remove a particular notification
     */

    public void removeNotification(String notificationId) {

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

            int systemNotificationId = db.removeNotification(notificationDocId, notificationId);
            if (systemNotificationId != -1) {

                NotificationManager nMgr =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                nMgr.cancel(notificationId, systemNotificationId);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void publishGroupChatMessage(String groupMembersDocId, JSONObject messageObject,
                                        HashMap<String, Object> map) {
        /*
         * IMQtt delivery token of only the last message is stored to decide if message has succesfully delivered to all members in group
         */

        IMqttDeliveryToken token = null;

        try {

            Map<String, Object> groupInfo = db.fetchGroupInfo(groupMembersDocId);





            /*
             * Have to only emit,if current user is the active member of the group
             */

            if ((boolean) groupInfo.get("isActive")) {

                ArrayList<Map<String, Object>> groupMembers =
                        (ArrayList<Map<String, Object>>) groupInfo.get("groupMembersArray");




                /*
                 * Have to add the functionality for the active flag ,so can check b4 emitting a message if current user is member of group or not
                 *
                 */

                for (int i = 0; i < groupMembers.size(); i++) {

                    if (!groupMembers.get(i).get("memberId").equals(userId)) {


                        /*
                         * Since no need to emit to the group messages topic for the sender itself
                         */

                        if (mqttAndroidClient != null && mqttAndroidClient.isConnected()) {
                            try {

                                token = mqttAndroidClient.publish(
                                        MqttEvents.GroupChats.value + "/" + groupMembers.get(i).get("memberId"),
                                        messageObject.toString().getBytes(), 1, false);
                            } catch (MqttException e) {

                            }
                        }
                    }
                }
                /*
                 * To allow logging onto the server
                 */
                if (mqttAndroidClient != null && mqttAndroidClient.isConnected()) {

                    messageObject.put("totalMembers", String.valueOf(groupMembers.size()));

                    mqttAndroidClient.publish(
                            MqttEvents.GroupChatMessages.value + "/" + messageObject.getString("to"),
                            messageObject.toString().getBytes(), 1, false);
                }

                if (token != null) {


                    /*
                     * Have been successfully emitted
                     */

                    map.put("MQttToken", token);

                    tokenMapping.add(map);

                    set.add(token);
                }
            }
        } catch (Exception e) {
            /*
             * To avoid the case of group being deleted while still having message to be sent
             */

        }
    }

    public boolean checkWifiConnected() {


        /*
         *Since connectionManager.isConnected() is deprecated in api23
         */

        if (wifiMgr == null) {
            wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        }
        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            if (wifiInfo == null) {

                wifiInfo = wifiMgr.getConnectionInfo();
            }
            if (wifiInfo == null) {

                return false;
            } else {
                if (wifiInfo.getNetworkId() == -1) {

                    return false; // Not connected to an access point
                }
            }

            return true; // Connected to an access point
        } else {

            return false; // Wi-Fi adapter is OFF
        }
    }


    /*
     *
     *To allow the option of auto downloading of the media,when on wifi
     *
     */

    public boolean checkMobileDataOn() {

        try {

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();

            return (info != null
                    && info.isConnected()
                    && info.getType() == ConnectivityManager.TYPE_MOBILE);
        } catch (Exception e) {

            return false;
        }
    }

    @SuppressWarnings("all")
    public boolean writeResponseBodyToDisk(ResponseBody body, String filePath, String messageType,
                                           final String messageId, int replyType) {

        try {
            // todo change the file location/name according to your needs

            File folder;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                folder = new File(getInstance().getExternalFilesDir(null) + "/" + ApiOnServer.APP_NAME);
            } else {
                folder = new File(getInstance().getFilesDir().getPath() + "/" + ApiOnServer.APP_NAME);
            }

            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }

            File file = new File(filePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                byte[] fileReader = new byte[4096];

                final long fileSize = body.contentLength();

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {

                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                MediaStore.Images.Media.insertImage(getInstance().getContentResolver(), file.getAbsolutePath(),
                        file.getName(), file.getName());

                return true;
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    /*
     * Will try to download the media message asynchronously in the background
     *
     */

    /**
     * For generating the push notification incase contact updated on below api level 18 devices.
     */
    private void generatePushForContactUpdate() {

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this).setSmallIcon(R.drawable.notification)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(getString(R.string.app_name))

                        .setContentText(getString(R.string.NotificationContent))
                        .setTicker(getString(R.string.NotificationTitle))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(
                                PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0))

                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setPriority(Notification.PRIORITY_HIGH);

        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /*
         *
         * Notification id is used to notify the same notification
         *
         * */

        notificationManager.notify(-1, notificationBuilder.build());
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public void createNewPostListener(HowdooService howdooService,
                                      SocialObserver socialShareObserver) {

        if (publishPost == null) {
            publishPost = new PublishPost();
        }
        publishPost.retryPublishingPosts(howdooService, socialShareObserver);
    }
boolean newPostCreated;
    public void addNewPost(PostData postData, SocialObserver socialObserver) {
        newPostCreated=true;
        if (publishPost != null) {

            publishPost.addNewPost(postData, socialObserver);
        }
    }

    public boolean isNewPostCreated() {
        return newPostCreated;
    }

    public void setNewPostCreated(boolean newPostCreated) {
        this.newPostCreated = newPostCreated;
    }

    public void addNewPost(PostData postData) {

        if (publishPost != null) {

            publishPost.addNewPost(postData);
        }
    }

    public ArrayList<Integer> getExcludedFilterIds() {
        return excludedIds;
    }

    public void setExcludedFilterIds(ArrayList<Integer> excludedIds) {
        this.excludedIds = excludedIds;
        db.updateExcludedFilters(userDocId, excludedIds);
    }

    public boolean isFiltersUpdated() {
        return filtersUpdated;
    }

    public void setFiltersUpdated(boolean filtersUpdated) {
        this.filtersUpdated = filtersUpdated;
    }

    public void refreshMediaGallery(String path) {

        for (int i = 0; i < 17; i++) {

            try {

                MediaScannerConnection.scanFile(this, new String[]{path + (i) + ".jpg"}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            /**
                             *(non-Javadoc)
                             *@see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                             */
                            public void onScanCompleted(String path, Uri uri) {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyOnImageDeleteRequired(String tempPath) {
        try {

            MediaScannerConnection.scanFile(this, new String[]{tempPath}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        /*
                         *   (non-Javadoc)
                         * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                         */
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private class FetchTime extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            sharedPref.edit().putBoolean("deltaRequired", true).apply();

            String url_ping = "https://google.com/";
            URL url = null;
            try {
                url = new URL(url_ping);
            } catch (MalformedURLException e) {

            }

            try {
                /*
                 * Maybe inaccurate due to network inaccuracy
                 */

                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();

                if (urlc.getResponseCode() == 200 || urlc.getResponseCode() == 503) {
                    long dateStr = urlc.getDate();
                    //Here I do something with the Date String

                    timeDelta = System.currentTimeMillis() - dateStr;

                    sharedPref.edit().putBoolean("deltaRequired", false).apply();
                    sharedPref.edit().putLong("timeDelta", timeDelta).apply();
                    urlc.disconnect();
                }
            } catch (IOException e) {


                /*
                 * Should disable user from using the app
                 */

            } catch (NullPointerException e) {

            }
            return null;
        }
    }

    public void logOutAndDisconnectMqtt() {
        if (mqttConnectOptions != null) mqttConnectOptions.setAutomaticReconnect(false);
        try {
            if (mqttAndroidClient != null) mqttAndroidClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        mqttAndroidClient = null;
        setSignedIn(false, "", "", "", "", -1, null, null, null, null, null, null, null);
        setSignStatusChanged(false);
    }

    private void initializeFirebase() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token

                        pushToken = task.getResult().getToken();
                        sharedPref.edit().putString("pushToken", pushToken).apply();
                    }
                });
    }

    private void checkActiveCalls() {

        String callId = getCallId();
        String activeCallerId = getCallerId();

        if (callId != null && activeCallerId != null) {

            JSONObject obj = new JSONObject();
            try {
                obj.put("callId", callId);
                obj.put("userId", getUserId());
                obj.put("type", 2);

                publish(MqttEvents.Calls.value + "/" + activeCallerId, obj, 0, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        removeCurrentCallDetails();
    }

    public void saveCurrentCallDetails(String callId, String callerId) {

        sharedPref.edit().putString("callId", callId).apply();
        sharedPref.edit().putString("callerId", callerId).apply();
    }

    public void removeCurrentCallDetails() {

        sharedPref.edit().putString("callId", null).apply();
        sharedPref.edit().putString("callerId", null).apply();
    }

    public String getCallId() {
        return sharedPref.getString("callId", null);
    }

    public String getCallerId() {
        return sharedPref.getString("callerId", null);
    }

    HurlStack hurlStack = new HurlStack() {
        @Override
        protected HttpURLConnection createConnection(java.net.URL url) throws IOException {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
            try {
                httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory(getApplicationContext()));
                // httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpsURLConnection;
        }
    };

    private SSLSocketFactory getSSLSocketFactory(Context context)
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException,
            KeyManagementException {

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = context.getResources().openRawResource(R.raw.dochat);

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");

        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0) {
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkClientTrusted", e.toString());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0) {
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }

    public SSLSocketFactory getSocketFactory(int certificateId, String certificatePassword) {
        SSLSocketFactory result = mSocketFactoryMap.get(certificateId);
        if ((null == result) && (null != getApplicationContext())) {

            try {
                KeyStore keystoreTrust = KeyStore.getInstance("BKS");
                keystoreTrust.load(getApplicationContext().getResources().
                        openRawResource(certificateId), certificatePassword.toCharArray());
                TrustManagerFactory trustManagerFactory =
                        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keystoreTrust);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
                result = sslContext.getSocketFactory();
                mSocketFactoryMap.put(certificateId, result);
            } catch (Exception ex) {
            }
        }
        return result;
    }

    private HashMap<Integer, SSLSocketFactory> mSocketFactoryMap =
            new HashMap<Integer, SSLSocketFactory>();

    private static InputStream trustedCertificatesInputStream() {
        return AppController.getInstance().getResources().openRawResource(R.raw.dochat);
    }

    @Override
    public void onActivityPostResumed(@NonNull Activity atv) {
        activity = atv;
    }

    /**
     * used to get the live data object for dicoonect call
     */

    MutableLiveData<Pair<Boolean, String>> disconnectCallLiveData =
            new MutableLiveData<Pair<Boolean, String>>();

    public MutableLiveData<Pair<Boolean, String>> getDisconnectCallLiveData() {
        return disconnectCallLiveData;
    }

    /*This method is used to get list of participant in call*/
    private void getParticipant(String callId, boolean checkCallExist,
                                LoginDataDetails loginDataDetails, CallerDetailsResponse callerDetailsResponse) {

        String url = ApiOnServer.GET_PARTICIPANT + "?callId=" + callId;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Gson gson = new Gson();
                        GetParticipantResponse participantResponse =
                                gson.fromJson(response.toString(), GetParticipantResponse.class);
                        Data data = participantResponse.getData();
                        List<User> users = data.getUsers();

                        if (checkCallExist) {
                            if (!users.isEmpty()) {
                                if (!AppController.getInstance().isActiveOnACall()) {
                                    /*
                                     * Bug Title: we cannot cut the call once we minimize the application and maximize the application on call,
                                     * caller press home button calling screen gone not able to retrieve again
                                     * Fix Description: launch flags are set in activity
                                     * Developer Name: Hardik
                                     * Fix Date: 5/4/2021
                                     * */
                                    Intent intent = new Intent(AppController.this, CallingActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    intent.putExtra(com.appscrip.myapplication.utility.Constants.USER_NAME,
                                            data.getUserName());
                                    intent.putExtra(com.appscrip.myapplication.utility.Constants.USER_IMAGE,
                                            data.getUserImage());
                                    intent.putExtra(com.appscrip.myapplication.utility.Constants.USER_ID,
                                            data.getUserId());
                                    intent.putExtra(com.appscrip.myapplication.utility.Constants.CALL_STATUS,
                                            CallStatus.NEW_CALL);
                                    intent.putExtra(CALL_TYPE, data.getType());
                                    intent.putExtra(CALL_ID, callId);
                                    intent.putExtra(ROOM_ID, data.getRoom());
                                    startActivity(intent);
                                } else {
                                    disconnectCall(callId);
                                }
                            }
                        } else {

                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                    SessionObserver sessionObserver = new SessionObserver();
                    sessionObserver.getObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableObserver<Boolean>() {
                                @Override
                                public void onNext(Boolean flag) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            getParticipant(callId, checkCallExist, loginDataDetails,
                                                    callerDetailsResponse);
                                        }
                                    }, 1000);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                    sessionApiCall.getNewSession(sessionObserver);
                }
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lan", Constants.LANGUAGE);
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "get participant");
    }

    /*This method is used to disconnect call*/
    private void disconnectCall(String callId) {

        String url = ApiOnServer.DISCONNECT_CALL;

        JSONObject obj = new JSONObject();

        try {
            obj.put("callId", callId);
            obj.put("callFrom", CallDisconnectType.BUSY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE, url, obj,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        AppController.getInstance().setActiveOnACall(false, false);
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppController.getInstance().setActiveOnACall(false, false);
                if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                    SessionObserver sessionObserver = new SessionObserver();
                    sessionObserver.getObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableObserver<Boolean>() {
                                @Override
                                public void onNext(Boolean flag) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            disconnectCall(callId);
                                        }
                                    }, 1000);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                    sessionApiCall.getNewSession(sessionObserver);
                }
            }
        }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lan", Constants.LANGUAGE);
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "disconnect call");
    }

    public IceServerResponse iceServerResponse;

    public IceServerResponse getIceServerResponse() {
        return iceServerResponse;
    }

    public void setIceServerResponse(IceServerResponse iceServerResponse) {
        this.iceServerResponse = iceServerResponse;
    }

    /*Used to get ice servers list from server for calling*/
    private void getIceServersApi(final JSONObject obj) {

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.GET, ApiOnServer.GET_ICE_SERVERS, obj,
                        new com.android.volley.Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                Gson gson = new Gson();
                                IceServerResponse res = gson.fromJson(response.toString(), IceServerResponse.class);
                                setIceServerResponse(res);
                                Log.d("ice_server", "onResponse: ice server working");
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("authorization", apiToken);
                        headers.put("lang", Constants.LANGUAGE);
                        return headers;
                    }
                };

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "getIceServersApiRequest");
    }

    /*Used to set post ios push params*/
    private void postIosPush() {

        JSONObject obj = new JSONObject();

        try {
            obj.put("iosAudioCallPush", "123456"); //Not used for android
            obj.put("iosVideoCallPush", "123456"); //Not used for android
            obj.put("iosMassageCallPush", "123456"); //Not used for android
            obj.put("isdevelopment", false); //Not used for android
            obj.put("appName", "dubly"); //doChat || dubly || picoadda
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.POST_IOS, obj,
                        new com.android.volley.Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("POST IOS:" + response);
                            }
                        }, new com.android.volley.Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("POST IOS:" + error);
                    }
                }

                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("authorization", apiToken);
                        headers.put("lang", Constants.LANGUAGE);
                        return headers;
                    }
                };

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "POST IOS");
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public boolean isGuest() {
        return sharedPref.getBoolean(Constants.GUEST_USER, false);
    }

    public void setGuest(boolean guest) {
        sharedPref.edit().putBoolean(Constants.GUEST_USER, guest).apply();
    }

    public void openSignInDialog(Context mContext) {
        Intent intent2 = new Intent(this, PreLoginActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mContext.startActivity(intent2);
      /*  ConfirmDialog dialog = new ConfirmDialog(mContext, R.drawable.welcome_screen_logo,
                mContext.getString(R.string.sign_in_message), mContext.getString(R.string.login),
                mContext.getString(R.string.cancel));
        dialog.show();

        Button btnYes = dialog.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(v1 -> {
            Intent intent2 = new Intent(this, LoginActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            mContext.startActivity(intent2);
            dialog.dismiss();
        });

        dialog.show();*/
    }

    public void createIsometrikConfiguration(boolean firstTime, String accountId, String projectId,
                                             String keysetId, String licenseKey, String rtcAppId, String arFiltersAppId, String userId,
                                             String userName, String userIdentifier, String userProfilePic) {
//        if (firstTime) {
//
//            Log.d(TAG, "Iso1 " + userId);
//
//            IsometrikUiSdk.getInstance()
//                    .createConfiguration(accountId, projectId, keysetId, licenseKey, rtcAppId, arFiltersAppId,
//                            BuildConfig.APPLICATION_ID);
//
//            IsometrikUiSdk.getInstance()
//                    .getUserSession()
//                    .switchUser(userId, userName, userIdentifier, userProfilePic, false);
//        } else {
//
//            IsometrikUiSdk.getInstance()
//                    .createConfiguration(sessionManager.getAccountId(), sessionManager.getProjectId(),
//                            sessionManager.getKeysetId(), sessionManager.getLicenseKey(),
//                            sessionManager.getRtcAppId(), sessionManager.getArFiltersAppId(),
//                            BuildConfig.APPLICATION_ID);
//
//            String name = sessionManager.getFirstName();
//            if (sessionManager.getLastName() != null && !sessionManager.getLastName().isEmpty()) {
//                name = name + " " + sessionManager.getLastName();
//            }
//
//            Log.d(TAG, "Iso1 " + sessionManager.getIsometrikUserId());
//
//            IsometrikUiSdk.getInstance()
//                    .getUserSession()
//                    .switchUser(sessionManager.getIsometrikUserId(), name, sessionManager.getUserName(),
//                            sessionManager.getUserProfilePic(), false);
//        }
    }

    /*
     * Bug Title: we cannot cut the call once we minimize the application and maximize the application on call,
     * when the receiver minimizes the callers call, the video call is struck in the callers end to receive and cut call not working instantaneously,
     * pending call api,
     * user a calls user b , user b opens the app via the launch icon , the call should ideally show but it does not ,Hardik needs to call the pending call api whenever app is launched from background to foreground
     * Fix Description: have implemented new pending call api when this activity launch
     * Developer Name: Hardik
     * Fix Date: 5/4/2021
     * */
    private void getPendingCalls() {
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
                AppController.getInstance().getApplicationContext());
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiOnServer.CALLING_BASE)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(HowdooService.class)
                .getPendingCalls(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<PendingCallsResponse>>() {
                    @Override
                    public void onNext(@NotNull Response<PendingCallsResponse> response) {
                        if (response.code() == 200) {
                            if (response.body() != null && response.body().getData() != null) {
                                Data data = response.body().getData();
                                Intent notifyIntent = new Intent(getApplicationContext(), CallingActivity.class);
                                notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                notifyIntent.putExtra(USER_NAME, data.getUserName());
                                notifyIntent.putExtra(USER_IMAGE, data.getUserImage());
                                notifyIntent.putExtra(USER_ID, data.getUserId());
                                notifyIntent.putExtra(CALL_STATUS, CallStatus.NEW_CALL);
                                notifyIntent.putExtra(CALL_TYPE, data.getType());
                                notifyIntent.putExtra(CALL_ID, data.getId());
                                notifyIntent.putExtra(ROOM_ID, data.getRoom());
                                startActivity(notifyIntent);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}