package chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.SurfaceViewRenderer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.CameraActivity;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.stories.model.Viewer;
import chat.hola.com.app.live_stream.CallbacksFromChatPredefined;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.ResponcePojo.GiftEvent;
import chat.hola.com.app.live_stream.ResponcePojo.LikeEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;
import chat.hola.com.app.live_stream.ResponcePojo.StreamPresenceEvent;
import chat.hola.com.app.live_stream.adapter.ChatPredefinedMessagesAdapter;
import chat.hola.com.app.live_stream.adapter.MessageAdapter;
import chat.hola.com.app.live_stream.heart_animation.HeartsRenderer;
import chat.hola.com.app.live_stream.heart_animation.HeartsView;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.pubsub.MqttEvents;
import chat.hola.com.app.live_stream.utility.AlertProgress;
import chat.hola.com.app.live_stream.utility.BitmapHelper;
import chat.hola.com.app.live_stream.utility.CustomLinearLayoutManager;
import chat.hola.com.app.live_stream.utility.TextDrawable;
import chat.hola.com.app.live_stream.utility.TimerHelper;
import chat.hola.com.app.live_stream.utility.Utility;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.IPServicePojo;
import chat.hola.com.app.models.StreamStats;
import dagger.android.support.DaggerAppCompatActivity;
import de.tavendo.autobahn.WebSocket;
import io.isometrik.android.webrtc.IWebRTCClient;
import io.isometrik.android.webrtc.IWebRTCListener;
import io.isometrik.android.webrtc.WebRTCClient;
import io.isometrik.android.webrtc.apprtc.Constants;
import io.isometrik.android.webrtc.apprtc.UnhandledExceptionHandler;

import static chat.hola.com.app.live_stream.pubsub.MqttEvents.WillTopic;

public class WebRTCStreamBroadcasterActivity extends DaggerAppCompatActivity
        implements ViewersAdapter.ClickListner, LiveBroadCastPresenterContract.LiveBroadCastView,
        IWebRTCListener {
    static final int PAGE_SIZE = chat.hola.com.app.Utilities.Constants.PAGE_SIZE;
    private static final String TAG = WebRTCStreamBroadcasterActivity.class.getSimpleName();

    private String place = "";
    private String country = "";
    private String city = "";

    @Inject
    LiveBroadCastPresenterContract.LiveBroadCastPresenter presenter;
    @Inject
    MQTTManager mqttManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    AlertProgress alertProgress;

    @BindView(R.id.etSendMessage)
    EditText etSendMessage;
    @BindView(R.id.tvTimer)
    TextView tvTimer;
    @BindView(R.id.tvLiveStreamStatus)
    TextView tvLiveStreamStatus;
    @BindView(R.id.tvNoOfViewers)
    TextView tvNoOfViewers;
    @BindView(R.id.llTimer)
    LinearLayout llTimer;
    @BindView(R.id.llStartStreaming)
    LinearLayout llStartStreaming;
    @BindView(R.id.rvMessages)
    RecyclerView rvMessages;
    @BindView(R.id.rvPresetMessages)
    RecyclerView rvPresetChatMessages;
    @BindView(R.id.ivSendMessage)
    AppCompatImageView ivSendMessage;
    @BindView(R.id.btnCloseLiveStream)
    Button btnCloseLiveStream;
    //For the gifts
    @BindView(R.id.rlGiftViewer)
    RelativeLayout rlGiftViewer;
    @BindView(R.id.tvGiftName)
    TextView tvGiftName;
    @BindView(R.id.tvGiftCoin)
    TextView tvGiftCoin;
    @BindView(R.id.tvGiftSender)
    TextView tvGiftSender;
    @BindView(R.id.ivGiftImage)
    AppCompatImageView ivGiftImage;
    @BindView(R.id.ivUserImage)
    AppCompatImageView ivUserImage;
    @BindView(R.id.ivGiftGif)
    AppCompatImageView ivGiftGif;
    @BindView(R.id.heartsView)
    HeartsView heartsView;
    @BindView(R.id.rlHeader)
    RelativeLayout rlHeader;
    @BindView(R.id.ivMute)
    AppCompatImageView ivMute;
    @BindView(R.id.camera_view_renderer)
    SurfaceViewRenderer cameraViewRenderer;
    @BindView(R.id.tvCoins)
    TextView tvCoins;
    @BindView(R.id.tvEarned)
    TextView tvEarned;
    @BindView(R.id.tvViews)
    TextView tvViews;
    @BindView(R.id.tvGifts)
    TextView tvGifts;
    @BindView(R.id.tvLiveTime)
    TextView tvLiveTime;
    private ArrayList<GiftEvent> giftEvents = new ArrayList<>();
    private boolean giftShowing;

    /*For Heart animation*/

    private HeartsView.Model model;

    private Timer mTimer;
    public TimerHandler mTimerHandler;

    private MessageAdapter messageAdapter;
    private ArrayList<StreamChatMessage> messages = new ArrayList<>();
    private String streamName = "rtmpstream-";
    private String streamId = "", streamType, thumbnail = "";
    private boolean stopStreamingRequested = false;
    private CustomLinearLayoutManager llm;
    private boolean isPublishing = false;

    private static long mElapsedTime;
    private static long duration;
    private AlertDialog dialog;
    private int density;
    private boolean connectionToStreamingServerLost;
    private AlertDialog connectionLostDialog;
    private boolean microPhoneMuted, restartRequest;
    private boolean rearCameraActive;
    private WebRTCClient webRTCClient;

    @BindView(R.id.bottom_sheet)
    View bottomSheet;
    @BindView(R.id.viewerList)
    RecyclerView viewerList;
    @BindView(R.id.tvViewer)
    TextView tvViewer;
    @BindView(R.id.done)
    TextView done;

    @BindView(R.id.tvShareMessage)
    TextView tvShareMessage;
    @BindView(R.id.btnShare)
    Button btnShare;
    @BindView(R.id.switchShare)
    Switch switchShare;
    @BindView(R.id.rlStreamEnd)
    RelativeLayout rlStreamEnd;
    @BindView(R.id.clContent)
    CoordinatorLayout clContent;
    @BindView(R.id.ivStreamPhoto)
    ImageView ivStreamPhoto;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;

    private LinearLayoutManager viewersLayoutManager;
    private BottomSheetBehavior behavior;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    List<Viewer> viewerDataList;
    @Inject
    ViewersAdapter viewersAdapter;
    private String lat, lng;
    //private Loader loader;
    private boolean alreadyRequestedStats = false;

    private boolean timerPaused = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //below exception handler show the exception in a popup window
        //it is better to use in development, do not use in production
        Thread.setDefaultUncaughtExceptionHandler(new UnhandledExceptionHandler(this));

        // Set window styles for fullscreen-window size. Needs to be done before
        // adding content.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_webrtc_broadcaster);
        ButterKnife.bind(this);
        tvViewer.setTypeface(typefaceManager.getSemiboldFont());
        done.setTypeface(typefaceManager.getRegularFont());
        //loader = new Loader(this);
        tvShareMessage.setTypeface(typefaceManager.getRegularFont());
        btnShare.setTypeface(typefaceManager.getSemiboldFont());

        webRTCClient = new WebRTCClient(this, this);
        webRTCClient.setVideoRenderers(null, cameraViewRenderer);
        webRTCClient.setOpenFrontCamera(true);

        this.getIntent().putExtra(Constants.EXTRA_CAPTURETOTEXTURE_ENABLED, true);

        mTimerHandler = new TimerHandler();
        density = (int) getResources().getDisplayMetrics().density;

        ivSendMessage.setOnClickListener(onClickSendMessage());
        ivMute.setSelected(true);
        ivMute.setOnClickListener(onMuteClicked());

        getIntentValue();
        initializeMessagesAdapter();
        initializeHeartsViews();

        presenter.streamChatMessageRxJava();
        presenter.allStreamEventRxJAva();
        presenter.streamPresenceEventRxJava();
        presenter.likeEventRxJAva();
        presenter.giftEventRxJAva();

        setMicrophoneMuted(false);

        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        viewersLayoutManager = new LinearLayoutManager(this);
        viewerList.setLayoutManager(viewersLayoutManager);
        viewerList.setAdapter(viewersAdapter);
        viewerList.addOnScrollListener(recyclerViewOnScrollListener);
        viewersAdapter.setClickListner(this);

        new IPAddress().execute();

        Glide.with(this)
                .load(CameraActivity.imageUrl)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivStreamPhoto);

        String profilePic = sessionManager.getUserProfilePic();
        if (!TextUtils.isEmpty(profilePic)) {
            Glide.with(this).load(profilePic).asBitmap().centerCrop()
                    .placeholder(R.drawable.empty_user)
                    .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .into(new BitmapImageViewTarget(ivProfilePic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivProfilePic.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            Utilities.setTextRoundDrawable(this, sessionManager.getFirstName(), sessionManager.getLastName(), ivProfilePic);
        }

    }

    @OnClick(R.id.btnCloseLiveStream)
    public void endLiveStreaming() {

        Dialog streamEndDialog = new Dialog(this);
        streamEndDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        streamEndDialog.setContentView(R.layout.dialog_stream);

        Button end = (Button) streamEndDialog.findViewById(R.id.btnEndLiveVideo);
        Button cancel = (Button) streamEndDialog.findViewById(R.id.btnCancel);
        end.setTypeface(typefaceManager.getSemiboldFont());
        cancel.setTypeface(typefaceManager.getSemiboldFont());

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamEndDialog.dismiss();

                stopStreaming();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamEndDialog.dismiss();
            }
        });

        streamEndDialog.show();
    }

    private void showSteamStoreUI() {
        clContent.setVisibility(View.GONE);
        rlStreamEnd.setVisibility(View.VISIBLE);

        switchShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvShareMessage.setTextColor(
                        getResources().getColor(isChecked ? R.color.white : R.color.gray));
//                btnShare.setText(
//                        getResources().getString(isChecked ? R.string.share : R.string.discard_text));
            }
        });

        btnShare.setOnClickListener(v -> {
            //                if (loader!=null)
            //                loader.show();
            if (switchShare.isChecked()) {
                //TODO store stream
                exitStreamingScreen();
            } else {
                exitStreamingScreen();
            }
        });
    }

    @OnClick(R.id.done)
    public void done() {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    //private void switchView(boolean isStreamStarted) {
    //  heartsView.setVisibility(isStreamStarted ? View.VISIBLE : View.GONE);
    //  rlHeader.setVisibility(isStreamStarted ? View.VISIBLE : View.GONE);
    //  llStartStreaming.setVisibility(isStreamStarted ? View.VISIBLE : View.GONE);
    //}

    private void initializeHeartsViews() {
        HeartsRenderer.Config config = new HeartsRenderer.Config(3f, 0.35f, 2f);

        Bitmap bitmap = BitmapHelper.getBitmapFromVectorDrawable(WebRTCStreamBroadcasterActivity.this,
                R.drawable.ic_heart);

        model = new HeartsView.Model(0,// Unique ID of this image, used for Rajawali materials caching
                bitmap);
        heartsView.applyConfig(config);
    }

    private void initializeMessagesAdapter() {

        messageAdapter = new MessageAdapter(messages, this);

        llm = new CustomLinearLayoutManager(WebRTCStreamBroadcasterActivity.this,
                LinearLayoutManager.VERTICAL, false);

        rvMessages.setLayoutManager(llm);
        rvMessages.addOnScrollListener(recyclerViewMessageOnScrollListener);
        rvMessages.setAdapter(messageAdapter);
        String[] stringArray = new String[9];
        int blush = 0x1F60A;
        int smile = 0x1F601;
        int smile_tear = 0x1F602;
        int happy_surprised = 0x1F603;
        int happy_heart = 0x1F60D;
        int hi = 0x270B;
        int victory = 0x270C;
        int thumbsUp = 0x1F44D;
        String sBlush = getEmojiByUnicode(blush);
        String sSmile = getEmojiByUnicode(smile);
        String sSmiletear = getEmojiByUnicode(smile_tear);
        String sHappySurprised = getEmojiByUnicode(happy_surprised);
        String sHappyHeart = getEmojiByUnicode(happy_heart);
        String sHi = getEmojiByUnicode(hi);
        String sVictory = getEmojiByUnicode(victory);
        String sThumbsUp = getEmojiByUnicode(thumbsUp);
        stringArray[0] = "hello";
        stringArray[1] = sSmile;
        stringArray[2] = sBlush + sBlush + sBlush;
        stringArray[3] = sSmiletear + sSmiletear + sSmiletear;
        stringArray[4] = sHappySurprised + sHappySurprised + sHappySurprised;
        stringArray[5] = sHappyHeart + sHappyHeart + sHappyHeart;
        stringArray[6] = sHi + sHi + sHi;
        stringArray[7] = sVictory + sVictory + sVictory;
        stringArray[8] = sThumbsUp;

        CallbacksFromChatPredefined preset = new CallbacksFromChatPredefined() {
            @Override
            public void onPresetChatClicked(String chat) {
                presenter.publishChatMessage(streamId, chat);
                Utility.hideKeyboard(WebRTCStreamBroadcasterActivity.this);
            }
        };

        rvPresetChatMessages.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPresetChatMessages.setAdapter(new ChatPredefinedMessagesAdapter(stringArray, preset));
    }

    private String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    private View.OnClickListener onClickSendMessage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etSendMessage.getText().toString().trim().equals("")) {
                    String msg = etSendMessage.getText().toString().trim();
                    presenter.publishChatMessage(streamId, msg);
                    etSendMessage.setText(getString(R.string.double_inverted_comma));
                    Utility.hideKeyboard(WebRTCStreamBroadcasterActivity.this);
                }
            }
        };
    }

    private View.OnClickListener onCloseClicked() {

        return view -> onBackPressed();
    }

    private View.OnClickListener onMuteClicked() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ivMute.setSelected(microPhoneMuted);
                microPhoneMuted = !microPhoneMuted;
                setMicrophoneMuted(microPhoneMuted);
            }
        };
    }

    private void getIntentValue() {
        if (getIntent().getExtras() != null) {
            streamName = getIntent().getStringExtra("StreamName");
            streamType = getIntent().getStringExtra("StreamType");
            streamId = getIntent().getStringExtra("StreamId");
            thumbnail = getIntent().getStringExtra("ThumbNail");
            webRTCClient.init(ApiOnServer.WEBRTC_BASE_URL, streamId, IWebRTCClient.MODE_PUBLISH,
                    "tokenId", this.getIntent(), false);
            restartRequest = false;
            startBroadCasting();
        }

        checkMQttConnection();
    }

    private void checkMQttConnection() {

        if (mqttManager.isMQttConnected()) {

            subscribeToTopic();
        }
    }

    public void changeCamera(View v) {
        if (webRTCClient != null) {

            webRTCClient.switchCamera(rearCameraActive);

            rearCameraActive = !rearCameraActive;
        }
    }

    @Override
    protected void onPause() {
        new Handler().post(() -> {
            try {
                AppController.getLiveStreamBus().unregister(WebRTCStreamBroadcasterActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        unregisterReceiver(networkChangeReceiver);
        super.onPause();
    }

    private void startBroadCasting() {

        if (restartRequest) {

            webRTCClient.stopStream();
            webRTCClient.releaseResources();
            webRTCClient = new WebRTCClient(this, this);
            webRTCClient.setVideoRenderers(null, cameraViewRenderer);
            webRTCClient.setOpenFrontCamera(true);
            webRTCClient.init(ApiOnServer.WEBRTC_BASE_URL, streamId, IWebRTCClient.MODE_PUBLISH,
                    "tokenId", this.getIntent(), false);
        }
        webRTCClient.startStream();
        if (connectionLostDialog != null && connectionLostDialog.isShowing()) {
            connectionLostDialog.dismiss();
        }
        showProgress();
    }

    public void triggerStopStreaming(boolean connectionLost) {
        if (isPublishing) {

            if (!connectionLost) {
                isPublishing = false;
                stopTimer();
                webRTCClient.stopStream();
                webRTCClient.releaseResources();
            }

            tvLiveStreamStatus.setText(getString(R.string.offline_indicator));
        }
    }

    public void stopStreaming() {

        if (!stopStreamingRequested) {

            stopVideoStreaming();
        }

        if (!alreadyRequestedStats) {
            alreadyRequestedStats = true;
            presenter.streamStats(streamId);
        }
        if (isPublishing) {
            isPublishing = false;
            stopTimer();
            webRTCClient.stopStream();
            webRTCClient.releaseResources();
            tvLiveStreamStatus.setText(getString(R.string.offline_indicator));
        }
    }

    //This method starts a mTimer and updates the textview to show elapsed time for streaming
    public void startTimer() {

        if (mTimer == null) {
            mTimer = new Timer();
        }

        mElapsedTime = 0;
        mTimer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                mElapsedTime += 1; //increase every sec
                mTimerHandler.obtainMessage(TimerHandler.INCREASE_TIMER)
                        .sendToTarget();
            }
        }, 0, 1000);
    }

    public void stopTimer() {
        duration = mElapsedTime;
        if (mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = null;
        mElapsedTime = 0;
    }

    @Override
    public void showProgress() {
        if (dialog == null) {

            dialog = alertProgress.getProgressDialog(this, getString(R.string.starting_streaming));
        }
        dialog.show();
    }

    @Override
    public void hideProgress() {
        try {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void apiError(String errorMsg) {

    }

    @Override
    public void onNetworkRetry(boolean isNetwork, String thisApi) {

    }

    @Override
    public void follow(boolean following, String userId) {
        if (following) {
            presenter.follow(userId);
        } else {
            presenter.unfollow(userId);
        }
    }

    private class TimerHandler extends Handler {

        static final int INCREASE_TIMER = 1;

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INCREASE_TIMER:

                    if (!timerPaused) {
                        tvTimer.setText(TimerHelper.getDurationString(mElapsedTime));
                    }
                    duration = mElapsedTime;
                    break;
            }
        }
    }

    @Override
    public void onStreamSubscribed(String streamId) {
        //supportFinishAfterTransition();
    }

    @Override
    public void onBackPressed() {

        if (!stopStreamingRequested) {
            stopVideoStreaming();
        }
        try {
            AppController.getLiveStreamBus().unregister(this);
        } catch (Exception e) {

        }

        try {
            if (isPublishing) {
                isPublishing = false;
                stopTimer();
                webRTCClient.stopStream();
                webRTCClient.releaseResources();
                tvLiveStreamStatus.setText(getString(R.string.offline_indicator));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        try {

            if (!stopStreamingRequested) {

                stopVideoStreaming();
            }
            //            AppController.getLiveStreamBus().unregister(this);
        } catch (Exception e) {

            e.printStackTrace();
        }

        presenter.clearObservables();
        super.onDestroy();
    }

    @Override
    public void onStreamChatMessageReceived(StreamChatMessage message) {

        if (message.getStreamId() != null && message.getStreamId().equals(streamId)) {

            messages.add(message);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        messageAdapter.notifyItemInserted(messageAdapter.getItemCount() - 1);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                llm.scrollToPositionWithOffset(messageAdapter.getItemCount() - 1, 0);
                            }
                        }, 500);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onStreamPresenceEvent(StreamPresenceEvent streamPresenceEvent) {
        if (streamPresenceEvent.getStreamId() != null && streamPresenceEvent.getStreamId()
                .equals(streamId)) {

            if (streamPresenceEvent.getAction().equals("stop")) {
                //For the streaming stopped

                stopStreaming();
            } else if (streamPresenceEvent.getAction().equals("subscribe")
                    || streamPresenceEvent.getAction().equals("unsubscribe")) {

                //For the subscribe or unsubscribe event
                if (streamPresenceEvent.getActiveViewwers() > 0) {
                    tvNoOfViewers.setVisibility(View.VISIBLE);
                    tvNoOfViewers.setText(String.valueOf(streamPresenceEvent.getActiveViewwers()));
                } else {
                    tvNoOfViewers.setVisibility(View.GONE);
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        }
    }

    @OnClick(R.id.tvNoOfViewers)
    public void viewers() {
        presenter.broadcastViewer(streamId, 0, PAGE_SIZE);
    }

    private void stopVideoStreaming() {
        stopStreamingRequested = true;
        triggerStopStreaming(false);
        streamType = "stop";

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        presenter.subscribeStream(streamId, streamType, CameraActivity.imageUrl, streamName,
                switchShare.isChecked(), duration * 1000, country, city, place, lat, lng, width, height);

        presenter.endStream();

        unsubscribeToTopic();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Subscribe
    public void getMessage(JSONObject object) {

        try {
            if (object.getString("eventName").equals("connect")) {

                subscribeToTopic();
                restartBroadcast();
            } else if (object.getString("eventName").equals("disconnect")) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void unsubscribeToTopic() {

        mqttManager.unSubscribeFromTopic(MqttEvents.AllStreams.value + "/");
        mqttManager.unSubscribeFromTopic(
                MqttEvents.ParticularStreamPresenceEvents.value + "/" + streamId);
        mqttManager.unSubscribeFromTopic(
                MqttEvents.ParticularStreamChatMessages.value + "/" + streamId);

        mqttManager.unSubscribeFromTopic(MqttEvents.ParticularStreamLikeEvent.value + "/" + streamId);
        mqttManager.unSubscribeFromTopic(MqttEvents.ParticularStreamGiftEvent.value + "/" + streamId);

        byte[] payload = sessionManager.getUserId().getBytes();
        try {

            mqttManager.publish(WillTopic.value, payload, 1, true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void subscribeToTopic() {
        mqttManager.subscribeToTopic(MqttEvents.AllStreams.value + "/", 1);
        mqttManager.subscribeToTopic(MqttEvents.ParticularStreamPresenceEvents.value + "/" + streamId,
                1);
        mqttManager.subscribeToTopic(MqttEvents.ParticularStreamChatMessages.value + "/" + streamId, 1);

        mqttManager.subscribeToTopic(MqttEvents.ParticularStreamLikeEvent.value + "/" + streamId, 1);
        mqttManager.subscribeToTopic(MqttEvents.ParticularStreamGiftEvent.value + "/" + streamId, 1);
    }

    private void exitStreamingScreen() {
        btnShare.setEnabled(false);

        new Handler().postDelayed(() -> WebRTCStreamBroadcasterActivity.this.finish(), 2000);
    }

    @Override
    public void showFailedToPublishMessageAlert(String message) {
        //Publish message failed

        Toast.makeText(WebRTCStreamBroadcasterActivity.this, getString(R.string.sending_message_failed),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailedToFetchChatHistory(String message) {
        //Fetch gifts failed

        Toast.makeText(WebRTCStreamBroadcasterActivity.this,
                getString(R.string.retrieve_chathistory_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailedToFetchActiveViewers(String message) {
        //Fetch gifts failed

        Toast.makeText(WebRTCStreamBroadcasterActivity.this,
                getString(R.string.retrieve_viewers_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLikeEvent(LikeEvent likeEvent) {
        //On like event received

        try {
            if (likeEvent.getStreamID().equals(streamId)) {

                heartsView.emitHeart(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGiftEvent(GiftEvent giftEvent) {
        //On gift event received
        if (giftEvent.getStreamID().equals(streamId)) {
            giftEvents.add(giftEvent);
            if (!giftShowing) {
                showGift(giftEvent);
            }
        }
    }

    @Override
    public void broadcastViewer(List<Viewer> viewers) {
        viewersAdapter.updateData(viewers);
        bottomSheet.setVisibility(View.VISIBLE);
        bottomSheet.post(() -> {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
    }

    @Override
    public void showStats(StreamStats.Stats stats) {
        tvCoins.setText(stats.getTotalEarning());
        tvViews.setText(stats.getViewers());
        tvGifts.setText(stats.getGifts());
        tvEarned.setText(sessionManager.getCurrencySymbol() + "" + String.valueOf(stats.getMoneyEarned()));
        tvLiveTime.setText(tvTimer.getText().toString());
        showSteamStoreUI();
    }

    private void showGift(GiftEvent giftEvent) {

        giftShowing = true;
        tvGiftCoin.setText(giftEvent.getCoin() + getString(R.string.space) + getString(R.string.coins));
        tvGiftName.setText(giftEvent.getName());

        tvGiftSender.setText(giftEvent.getUserName());

        try {
            Glide.with(this)
                    .load(giftEvent.getImage())
                    .asBitmap()
                    .centerCrop()
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(ivGiftImage));
            //            GlideApp.with(this).load(giftEvent.getImage()).into(ivGiftImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (giftEvent.getUserImage() != null && !giftEvent.getUserImage().isEmpty()) {
            try {
                Glide.with(this)
                        .load(giftEvent.getUserImage())
                        .asBitmap()
                        .centerCrop()
                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(ivUserImage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivUserImage.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                //                GlideApp.with(this)
                //                        .load(giftEvent.getUserImage())
                //                        .circleCrop()
                //                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                //                        .
                //                                into(ivUserImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            //If in future we have image for each user being available as well
            try {
                ivUserImage.setImageDrawable(TextDrawable.builder()

                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(24 * density) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()

                        .buildRound((giftEvent.getUserName().trim()).charAt(0) + "",
                                ContextCompat.getColor(this, R.color.color_black)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rlGiftViewer.setVisibility(View.VISIBLE);

        try {
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ivGiftGif, 1);
            Glide.with(this).load(giftEvent.getGifs()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    onGifFinished();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    resource.setLoopCount(1);
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        while (true) {
                            if (!resource.isRunning()) {
                                onGifFinished();//do your stuff
                                break;
                            }
                        }
                    }).start();
                    return false;
                }
            }).into(imageViewTarget);

        } catch (Exception e) {
            e.printStackTrace();
            onGifFinished();
        }

        //        try {
        //            GlideApp.with(this)
        //                    .asGif()
        //                    .load(giftEvent.getGifs())
        //                    .fitCenter()
        //                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
        //                    .listener(new RequestListener<GifDrawable>() {
        //                        @Override
        //                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
        //                                                    Target<GifDrawable> target, boolean isFirstResource) {
        //                            onGifFinished();//do your stuff
        //                            return false;
        //                        }
        //
        //                        @Override
        //                        public boolean onResourceReady(final GifDrawable resource, Object model,
        //                                                       Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
        //                            resource.setLoopCount(1);
        //                            new Thread(new Runnable() {
        //                                @Override
        //                                public void run() {
        //                                    while (true) {
        //                                        try {
        //                                            Thread.sleep(200);
        //                                        } catch (InterruptedException e) {
        //                                            e.printStackTrace();
        //                                        }
        //
        //                                        if (!resource.isRunning()) {
        //                                            onGifFinished();//do your stuff
        //                                            break;
        //                                        }
        //                                    }
        //                                }
        //                            }).start();
        //                            return false;
        //                        }
        //                    })
        //                    .into(ivGiftGif);
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //            onGifFinished();
        //        }
    }

    private void onGifFinished() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                giftEvents.remove(0);
                if (giftEvents.size() > 0) {

                    showGift(giftEvents.get(0));
                } else {
                    giftShowing = false;
                    rlGiftViewer.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onAllPastChatMessagesReceived(ArrayList<StreamChatMessage> data) {

        messages.clear();
        if (data.size() > 0) {
            messages.addAll(data);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        messageAdapter.notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                llm.scrollToPositionWithOffset(messageAdapter.getItemCount() - 1, 0);
                            }
                        }, 500);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void onActiveViewersReceived(int viewers) {

        tvNoOfViewers.setText(String.valueOf(viewers));
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getExtras() == null) return;

            ConnectivityManager manager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (manager != null) {
                NetworkInfo ni = manager.getActiveNetworkInfo();

                if (ni != null && (ni.getState() == NetworkInfo.State.CONNECTED
                        || ni.getState() == NetworkInfo.State.CONNECTING)) {
                    try {

                        if (!mqttManager.isMQttConnected()) {

                            mqttManager.connectMQttClient(sessionManager.getUserId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getLiveStreamBus().register(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

        //try {
        //
        //  if (webRTCClient != null) {
        //
        //    webRTCClient.startVideoSource();
        //  }
        //} catch (Exception e) {
        //  e.printStackTrace();
        //}
    }

    private void setMicrophoneMuted(boolean state) {
        AudioManager myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (myAudioManager != null) {
            // get the working mode and keep it
            int workingAudioMode = myAudioManager.getMode();
            myAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

            // change mic state only if needed
            if (myAudioManager.isMicrophoneMute() != state) {
                myAudioManager.setMicrophoneMute(state);
            }

            // set back the original working mode
            myAudioManager.setMode(workingAudioMode);
        }
    }

    @Override
    public void onFailedToSubscribeStream(String message) {
        Toast.makeText(WebRTCStreamBroadcasterActivity.this,
                getString(R.string.stream_subscribe_failed), Toast.LENGTH_LONG).show();
    }

    //WebRTC Streaming code

    @Override
    public void onPublishStarted() {
        isPublishing = true;
        timerPaused = false;
        hideProgress();

        //start the streaming duration

        if (restartRequest) {
            tvLiveStreamStatus.setText(getString(R.string.live_indicator));
            presenter.getLiveStreamsChatHistory(streamId, 0, 50);
            presenter.getActiveViewers(streamId);
            presenter.sendRestartMessage(streamId);
        } else {
            tvLiveStreamStatus.setVisibility(View.VISIBLE);
            llStartStreaming.setVisibility(View.VISIBLE);
            llTimer.setVisibility(View.VISIBLE);

            startTimer();
        }
    }

    @Override
    public void onPublishFinished() {

    }

    @Override
    public void onError(String description) {

        if (dialog != null && dialog.isShowing()) dialog.dismiss();

        //Toast.makeText(this, description, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, getString(R.string.broadcast_Start_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignalChannelClosed(
            WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code) {
        triggerStopStreaming(true);

        timerPaused = true;

        if (connectionLostDialog == null) {
            AlertDialog.Builder connectionLostDialogBuilder =
                    new AlertDialog.Builder(WebRTCStreamBroadcasterActivity.this).setMessage(
                            R.string.broadcast_connection_lost).setPositiveButton(android.R.string.yes, null);

            // Create the Alert dialog
            connectionLostDialog = connectionLostDialogBuilder.create();
        }

        try {
            connectionLostDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        connectionToStreamingServerLost = true;
    }

    @Override
    public void onDisconnected() {
        timerPaused = true;
        connectionToStreamingServerLost = true;
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onIceDisconnected() {
        timerPaused = true;
        connectionToStreamingServerLost = true;
    }

    @Override
    public void onIceConnected() {
        //it is called when connected to ice
        //Have to check if restart request is there

        //       restartBroadcast();
    }

    private void restartBroadcast() {

        if (connectionToStreamingServerLost) {

            connectionToStreamingServerLost = false;

            runOnUiThread(() -> {
                restartRequest = true;
                startBroadCasting();
            });
        }
    }

    //@Override
    //public void onSurfaceInitialized() {
    //}

    @Override
    public void onPlayStarted() {
    }

    @Override
    public void onPlayFinished() {
    }

    @Override
    public void noStreamExistsToPlay() {
    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    presenter.callApiOnScroll(streamId, viewersLayoutManager.findFirstVisibleItemPosition(),
                            viewersLayoutManager.getChildCount(), viewersLayoutManager.getItemCount());
                }
            };

    public RecyclerView.OnScrollListener recyclerViewMessageOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    presenter.callMessageApiOnScroll(streamId, llm.findFirstVisibleItemPosition(),
                            llm.getChildCount(), llm.getItemCount());
                }
            };

    private class IPAddress extends AsyncTask<Void, Void, IPServicePojo> {
        @Override
        protected IPServicePojo doInBackground(Void... voids) {
            String stringUrl = "https://ipinfo.io/json";
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                StringBuffer response = new StringBuffer();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Gson gson = new Gson();
                return gson.fromJson(response.toString(), IPServicePojo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(IPServicePojo s) {
            super.onPostExecute(s);
            if (s != null) {

                sessionManager.setIpAdress(s.getIp());
                sessionManager.setCity(s.getCity());
                sessionManager.setCountry(s.getCountry());

                String[] latlng = s.getLoc().split(",");
                place = "";
                lat = latlng[0];
                lng = latlng[1];
                city = s.getCity();
                country = s.getCountry();
            }
        }
    }

    @Override
    public void onAllStreamEventReceived(AllStreamsData streamData) {

        if (streamData.getAction().equals("stop")) {

            if (streamData.getStreamId() != null && streamData.getStreamId().equals(streamId)) {

                stopStreaming();
            }
        }
    }

    @Override
    public void streamOffline() {

        try {
            Toast.makeText(WebRTCStreamBroadcasterActivity.this, getString(R.string.stream_not_live),
                    Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    onBackPressed();
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}