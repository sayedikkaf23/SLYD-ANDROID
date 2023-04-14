package chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.SurfaceViewRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.LowBalanceDialog;
import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.coin.base.CoinActivity;
import chat.hola.com.app.home.stories.model.Viewer;
import chat.hola.com.app.live_stream.CallbacksFromChatPredefined;
import chat.hola.com.app.live_stream.Home.follow_user.FollowUserActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.ViewersAdapter;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.ResponcePojo.GiftEvent;
import chat.hola.com.app.live_stream.ResponcePojo.LikeEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;
import chat.hola.com.app.live_stream.ResponcePojo.StreamPresenceEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamRestartEvent;
import chat.hola.com.app.live_stream.adapter.ChatPredefinedMessagesAdapter;
import chat.hola.com.app.live_stream.adapter.GiftAdapter;
import chat.hola.com.app.live_stream.adapter.GiftViewPagerAdapter;
import chat.hola.com.app.live_stream.adapter.MessageAdapter;
import chat.hola.com.app.live_stream.gift.GiftDataResponse;
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
import chat.hola.com.app.models.GiftCategories;
import chat.hola.com.app.models.WalletResponse;
import dagger.android.support.DaggerAppCompatActivity;
import de.tavendo.autobahn.WebSocket;
import io.isometrik.android.webrtc.IWebRTCClient;
import io.isometrik.android.webrtc.IWebRTCListener;
import io.isometrik.android.webrtc.WebRTCClient;
import io.isometrik.android.webrtc.apprtc.Constants;
import io.isometrik.android.webrtc.apprtc.UnhandledExceptionHandler;
import io.reactivex.disposables.CompositeDisposable;

public class WebRTCStreamPlayerActivity extends DaggerAppCompatActivity
        implements ViewersAdapter.ClickListner, LiveStreamPresenterContract.LiveStreamView, IWebRTCListener, GiftAdapter.GiftListener {

    private static final String TAG = WebRTCStreamPlayerActivity.class.getSimpleName();
    private AllStreamsData streamsData;
    static final int PAGE_SIZE = chat.hola.com.app.Utilities.Constants.PAGE_SIZE;
    int no_of_categories = -1;
    private GiftViewPagerAdapter giftViewPagerAdapter;
    List<GiftCategories.Data.Category> categories;
    @Inject
    LiveStreamService liveStreamService;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    MQTTManager mqttManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    LiveStreamPresenterContract.LiveStreamPresenter presenter;
    @Inject
    AlertProgress alertProgress;

    @BindView(R.id.tvCoinSpent)
    TextView tvCoinSpent;
    @BindView(R.id.tvNoOfViewers)
    TextView tvNoOfViewers;
    @BindView(R.id.ivSendMessage)
    ImageView ivSendMessage;
    @BindView(R.id.etSendMessage)
    EditText etSendMessage;
    @BindView(R.id.rvMessages)
    RecyclerView rvMessages;
    @BindView(R.id.ibCloseStreaming)
    ImageButton ibCloseStreaming;
    @BindView(R.id.ivHeart)
    ImageView ivHeart;
    @BindView(R.id.rvPresetMessages)
    RecyclerView rvPresetMessages;
    @BindView(R.id.tabLayout)
    TabLayout tlGiftCategories;
    @BindView(R.id.rvGifts)
    RecyclerView rvGifts;
    @BindView(R.id.rlGiftsParent)
    CoordinatorLayout rlGiftsParent;
    @BindView(R.id.ivGifts)
    AppCompatImageView ivGifts;
    @BindView(R.id.camera_view_renderer)
    SurfaceViewRenderer cameraViewRenderer;
    @BindView(R.id.tvNoGifts)
    TextView tvNoGifts;
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
    @BindView(R.id.tvTimer)
    TextView tvTimer;
    @BindView(R.id.llTimer)
    LinearLayout llTimer;
    @BindView(R.id.tv_coin)
    TextView tvCoin;
    @BindView(R.id.fl_coin_one)
    FrameLayout flCoinOne;
    @BindView(R.id.fl_coin_two)
    FrameLayout flCoinTwo;
    @BindView(R.id.fl_coin_three)
    FrameLayout flCoinThree;
    @BindView(R.id.clCoin)
    CoordinatorLayout clCoin;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.ibMute)
    ImageButton ibMute;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.tvLiveStreamStatus)
    TextView tvLiveStreamStatus;

    private AlertDialog dialog;
    private ArrayList<GiftEvent> giftEvents = new ArrayList<>();
    private boolean giftShowing;
    private CompositeDisposable disposable;
    private MessageAdapter messageAdapter;
    private ArrayList<StreamChatMessage> messages = new ArrayList<>();
    private CustomLinearLayoutManager llm;
    private boolean alreadyEndedStream;
    private HeartsView.Model model;
    private String streamId;
    private BottomSheetBehavior sheetBehavior;
    private boolean giftsVisible, giftsAvailable;
    private int density;
    private WebRTCClient webRTCClient;
    private Timer mTimer;
    public TimerHandler mTimerHandler;
    private long mElapsedTime;
    private boolean connectionToStreamingServerLost, restartRequest;
    private AlertDialog connectionLostDialog;
    private Dialog loader;
    private Animation coinAnimOne, coinAnimTwo, coinAnimThree;
    private MediaPlayer mediaPlayer;
    private boolean isMute = false;
    double balance = 0.0d;
    private int volume;
    private AudioManager myAudioManager;

    private LinearLayoutManager viewersLayoutManager;
    private BottomSheetBehavior behavior;
    @BindView(R.id.bottom_sheet)
    View bottomSheet;
    @BindView(R.id.viewerList)
    RecyclerView viewerList;
    @Inject
    List<Viewer> viewerDataList;
    @Inject
    ViewersAdapter viewersAdapter;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private boolean timerPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide title
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

        setContentView(R.layout.activity_webrtc_player);
        ButterKnife.bind(this);

        streamsData = (AllStreamsData) getIntent().getSerializableExtra("data");

        mediaPlayer = MediaPlayer.create(this, R.raw.coin_spend);
        coinAnimOne = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.coin_anim_one);
        coinAnimTwo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.coin_anim_two);
        coinAnimThree = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.coin_anim_three);

        webRTCClient = new WebRTCClient(this, this);
        webRTCClient.setVideoRenderers(null, cameraViewRenderer);
        //webRTCClient.setOpenFrontCamera(true);

        this.getIntent().putExtra(Constants.EXTRA_CAPTURETOTEXTURE_ENABLED, true);

        mTimerHandler = new TimerHandler();
        disposable = new CompositeDisposable();
        //String streamName = getIntent().getStringExtra("streamName");
        streamId = getIntent().getStringExtra("streamId");

        tvNoOfViewers.setText(getIntent().getStringExtra("viewers"));

        density = (int) getResources().getDisplayMetrics().density;

        //Preparing for stream playback
        webRTCClient = new WebRTCClient(this, this);
        webRTCClient.setVideoRenderers(null, cameraViewRenderer);
        webRTCClient.init(ApiOnServer.WEBRTC_BASE_URL, streamId, IWebRTCClient.MODE_PLAY, "tokenId",
                this.getIntent(), false);
        restartRequest = false;
        startPlaying();
        long currentTime = TimerHelper.getUnixTime();
        mElapsedTime = currentTime - getIntent().getLongExtra("startTime", currentTime) - 3;
        if (mElapsedTime < 0) {

            mElapsedTime = 0;
        }
        startTimer();

        ivGifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (giftsAvailable) {
                    updateGiftsVisibility(giftsVisible);
                } else {
                    Toast.makeText(WebRTCStreamPlayerActivity.this, R.string.no_gifts, Toast.LENGTH_LONG).show();
                }
            }
        });

        initializeBottomSheet();

        initializeHeartsViews();

        checkMQttConnection();

        presenter.streamChatMessageRxJava();
        presenter.allStreamEventRxJAva();
        presenter.streamPresenceEventRxJAva();
        presenter.streamRestartEventRxJAva();
        presenter.likeEventRxJAva();
        presenter.giftEventRxJAva();

        subscribeToApi();

        getStreamChatHistoryAndSubscribers();
        initializeMessagesAdapter();
        presenter.giftCategories();

        presenter.getWalletBalance();
        if (sessionManager.getCoinBalance() != null && !sessionManager.getCoinBalance().equals("null")) {
            balance = Double.parseDouble(sessionManager.getCoinBalance());
            tvBalance.setText(Utilities.formatMoney(balance));
        }

        loader = new Dialog(this);
        loader.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loader.setContentView(R.layout.dialog_login);

        if (loader.getWindow() != null) {
            loader.getWindow()
                    .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (myAudioManager != null) {
            volume = myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            enableSpeaker();
        }

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

        //gifts
//        tlGiftCategories.setupWithViewPager(viewPager, false);
        tlGiftCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @OnClick(R.id.done)
    public void done() {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @OnClick(R.id.ibMute)
    public void mute() {
        isMute = !isMute;
        ibMute.setSelected(isMute);
        setMicrophoneMuted(isMute ? 0 : volume);
    }

    @Override
    public void startCoinAnimation(String coin) {
        //        if(!isCoinAnimRunning) {
        clCoin.setVisibility(View.VISIBLE);
        mediaPlayer.start();
        tvCoinSpent.setText(coin);
        tvCoinSpent.startAnimation(coinAnimTwo);
        flCoinOne.startAnimation(coinAnimOne);
        flCoinTwo.startAnimation(coinAnimTwo);
        flCoinThree.startAnimation(coinAnimThree);
        coinAnimThree.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvCoinSpent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clCoin.setVisibility(View.GONE);
                tvCoinSpent.setVisibility(View.GONE);
                if (loader != null && loader.isShowing()) loader.dismiss();
                updateGiftsVisibility(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setOffscreenPageLimit(1);
        AppController.getLiveStreamBus().register(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        new Thread(() -> {
            try {
                AppController.getLiveStreamBus().unregister(WebRTCStreamPlayerActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        unregisterReceiver(networkChangeReceiver);
        super.onPause();
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

    private void startPlaying() {

        if (restartRequest) {

            webRTCClient.stopStream();

            webRTCClient.releaseResources();

            webRTCClient = new WebRTCClient(this, this);
            webRTCClient.setVideoRenderers(null, cameraViewRenderer);
            webRTCClient.init(ApiOnServer.WEBRTC_BASE_URL, streamId, IWebRTCClient.MODE_PLAY, "tokenId",
                    this.getIntent(), false);
        }
        webRTCClient.startStream();

        if (connectionLostDialog != null && connectionLostDialog.isShowing()) {
            connectionLostDialog.dismiss();
        }
        showProgress();
    }

    private void initializeBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(rlGiftsParent);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        giftsVisible = false;
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        giftsVisible = true;
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        giftsVisible = false;
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void initializeHeartsViews() {

        HeartsRenderer.Config config = new HeartsRenderer.Config(3f, 0.35f, 2f);

        Bitmap bitmap = BitmapHelper.getBitmapFromVectorDrawable(WebRTCStreamPlayerActivity.this,
                R.drawable.ic_heart);

        model = new HeartsView.Model(0,// Unique ID of this image, used for Rajawali materials caching
                bitmap);
        heartsView.applyConfig(config);
        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLike();

                try {
                    heartsView.emitHeart(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeMessagesAdapter() {
        llm =
                new CustomLinearLayoutManager(WebRTCStreamPlayerActivity.this, LinearLayoutManager.VERTICAL,
                        false);

        messageAdapter = new MessageAdapter(messages, this);
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
                Utility.hideKeyboard(WebRTCStreamPlayerActivity.this);
            }
        };

        ChatPredefinedMessagesAdapter chat_preset_adapter =
                new ChatPredefinedMessagesAdapter(stringArray, preset);
        rvPresetMessages.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPresetMessages.setAdapter(chat_preset_adapter);
    }

    String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    private void subscribeToApi() {

        presenter.subscribeToStream(streamId, "start");
    }

    private void getStreamChatHistoryAndSubscribers() {

        presenter.getLiveStreamsChatHistory(streamId, 0, 50);
        if (restartRequest) {
            presenter.getActiveViewers(streamId);
        }
    }

    @OnClick({R.id.ibCloseStreaming, R.id.ivSendMessage})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.ibCloseStreaming:

                onBackPressed();
                break;
            case R.id.ivSendMessage:
                if (!etSendMessage.getText().toString().trim().equals("")) {
                    String msg = etSendMessage.getText().toString().trim();
                    etSendMessage.setText(getString(R.string.double_inverted_comma));
                    Utility.hideKeyboard(this);
                    presenter.publishChatMessage(streamId, msg);
                }
                break;
        }
    }

    private void checkMQttConnection() {
        if (mqttManager.isMQttConnected()) {
            subscribeToTopic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
        presenter.clearObservables();

        disposable.clear();
        disposable.dispose();

        if (mqttManager.isMQttConnected()) {
            unsubscribeFromTopic();
        }

        //        AppController.getLiveStreamBus().unregister(this);

        setMicrophoneMuted(0);
        if (!alreadyEndedStream) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {

        if (giftsVisible) {

            updateGiftsVisibility(true);
            return;
        }

        stopTimer();
        alreadyEndedStream = true;
        presenter.subscribeToStream(streamId, "stop");
        try {
            webRTCClient.stopStream();
            webRTCClient.releaseResources();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //super.onBackPressed();
    }

    @Override
    public void onAllPastChatMessagesReceived(ArrayList<StreamChatMessage> data) {

        messages.clear();
        if (data.size() > 0) {
            messages.addAll(data);

            runOnUiThread(() -> {

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
            });
        }
    }

    @Override
    public void onStreamUnsubscribed() {
        disposable.clear();
        disposable.dispose();
        supportFinishAfterTransition();
    }

    @Override
    public void showProgress() {
        //Shown at he time of fetching the first frame fo the stream
        tvMessage.setVisibility(View.GONE);
        if (dialog == null) {
            dialog = alertProgress.getProgressDialog(this, getString(R.string.playing_streaming));
        }
        dialog.show();
    }

    @Override
    public void hideProgress() {
        //Hidden when first frame of the video has been rendered
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void apiError(String errorMsg) {

    }

    @Override
    public void onNetworkRetry(boolean isNetwork, String thisApi) {

    }

    @Override
    public void setCoinBalance(String coin) {
        tvBalance.setText(coin);
        balance = balance - Double.parseDouble(coin);
        balance = Double.parseDouble(coin);
        sessionManager.setCoinBalance("" + balance);
        tvBalance.setText(Utilities.formatMoney(Double.parseDouble(sessionManager.getCoinBalance())));
    }

    private void unsubscribeFromTopic() {
        mqttManager.unSubscribeFromTopic(
                MqttEvents.ParticularStreamPresenceEvents.value + "/" + streamId);
        mqttManager.unSubscribeFromTopic(
                MqttEvents.ParticularStreamChatMessages.value + "/" + streamId);

        mqttManager.unSubscribeFromTopic(MqttEvents.ParticularStreamLikeEvent.value + "/" + streamId);
        mqttManager.unSubscribeFromTopic(MqttEvents.ParticularStreamGiftEvent.value + "/" + streamId);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Subscribe
    public void getMessage(JSONObject object) {

        try {
            if (object.getString("eventName").equals("connect")) {

                subscribeToTopic();
                restartStream();
            } else if (object.getString("eventName").equals("disconnect")) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void subscribeToTopic() {

        mqttManager.subscribeToTopic(MqttEvents.AllStreams.value + "/", 1);
        mqttManager.subscribeToTopic(MqttEvents.ParticularStreamChatMessages.value + "/" + streamId, 1);
        mqttManager.subscribeToTopic(MqttEvents.ParticularStreamPresenceEvents.value + "/" + streamId,
                1);

        mqttManager.subscribeToTopic(MqttEvents.ParticularStreamLikeEvent.value + "/" + streamId, 1);
        mqttManager.subscribeToTopic(MqttEvents.ParticularStreamGiftEvent.value + "/" + streamId, 1);
    }

    @Override
    public void onStreamChatMessageReceived(StreamChatMessage message) {
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
                } catch (IndexOutOfBoundsException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onAllStreamEventReceived(AllStreamsData streamData) {

        if (streamData.getAction().equals("wait")) {

            if (streamData.getStreamId().equals(streamId)) {
                showMessage(streamData.getMessage());
            }
        } else if (streamData.getAction().equals("stop")) {

            if (streamData.getStreamId() != null && streamData.getStreamId().equals(streamId)) {

                if (!alreadyEndedStream) {
                    alreadyEndedStream = true;

                    try {
                        webRTCClient.stopStream();
                        webRTCClient.releaseResources();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stopTimer();

                    presenter.subscribeToStream(streamId, "stop");
                    startActivity(new Intent(this, FollowUserActivity.class).putExtra("data", streamsData));
                    finish();
                }
            }
        }
    }

    @Override
    public void onStreamPresenceEvent(StreamPresenceEvent streamPresenceEvent) {

        if (streamPresenceEvent.getAction().equals("stop")) {

            if (streamPresenceEvent.getStreamId() != null && streamPresenceEvent.getStreamId()
                    .equals(streamId)) {

                if (!alreadyEndedStream) {
                    alreadyEndedStream = true;

                    try {
                        webRTCClient.stopStream();
                        webRTCClient.releaseResources();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stopTimer();

                    presenter.subscribeToStream(streamId, "stop");
                    startActivity(new Intent(this, FollowUserActivity.class).putExtra("data", streamsData));
                    finish();
                }
            }
        } else if (streamPresenceEvent.getAction().equals("subscribe")
                || streamPresenceEvent.getAction().equals("unsubscribe")) {
            //For the subscribe or unsubscribe event

            tvNoOfViewers.setText(String.valueOf(streamPresenceEvent.getActiveViewwers()));
        }
    }

    @Override
    public void onActiveViewersReceived(int viewers) {

        tvNoOfViewers.setText(String.valueOf(viewers));
    }

    @Override
    public void onFailedToFetchActiveViewers(String message) {
        //Fetch gifts failed
        Toast.makeText(WebRTCStreamPlayerActivity.this, getString(R.string.retrieve_viewers_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void insufficientBalance() {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.insufficient_balance));
        builder.setMessage(getString(R.string.insufficient_balance_message));
        builder.setPositiveButton("Recharge", (dialog, which) -> dialog.cancel());
        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void showBalance(WalletResponse data) {
        balance = Double.parseDouble(data.getData().getCoinWallet().getBalance());
        sessionManager.setCoinBalance("" + balance);
        tvBalance.setText(Utilities.formatMoney(Double.parseDouble(sessionManager.getCoinBalance())));
    }

    @Override
    public void showFailedToPublishMessageAlert(String message) {
        //Publish message failed
        Toast.makeText(WebRTCStreamPlayerActivity.this, getString(R.string.sending_message_failed),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailedToFetchChatHistory(String message) {
        //Fetch gifts failed
        Toast.makeText(WebRTCStreamPlayerActivity.this, getString(R.string.retrieve_chathistory_failed),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailedToLike(String message) {
        //Failed to log the send like event
    }

    @Override
    public void onFailedToSendGift(String message) {
        //Failed to log the send gift event

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
    public void onStreamRestartEvent(StreamRestartEvent streamRestartEvent) {
        //On restart event received

        try {

            if (streamRestartEvent.getStreamID().equals(streamId)) {

                restartStream();
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

    private void sendLike() {
        //Calling the api for the analytics
        if (mqttManager.isMQttConnected()) {
            presenter.likeStream(streamId);
        } else {
            Toast.makeText(WebRTCStreamPlayerActivity.this, R.string.like_failed, Toast.LENGTH_LONG).show();
        }
    }

    private void sendGift(GiftDataResponse.Data.Gift gift) {
        //Calling the api for the analytics
        if (mqttManager.isMQttConnected()) {
            loader.show();
            presenter.sendGift(streamId, gift, streamsData);
            startCoinAnimation(gift.getGiftCost());
        } else {
            Toast.makeText(WebRTCStreamPlayerActivity.this, R.string.sending_gift_failed, Toast.LENGTH_LONG).show();
        }
    }

    private void showGift(GiftEvent giftEvent) {

        giftShowing = true;
        tvGiftCoin.setText(giftEvent.getCoin());
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        //If in future we have image for each user being available as well
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
                        .buildRound((giftEvent.getUserName().trim()).charAt(0) + "", ContextCompat.getColor(this, R.color.color_black)));
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

    public void updateViewersCount(int count) {

        //To update number of viewers of the stream

        tvNoOfViewers.setText(String.valueOf(count));
    }

    @Override
    public void onFailedToSubscribeStream(String message) {
        Toast.makeText(WebRTCStreamPlayerActivity.this, getString(R.string.stream_subscribe_failed),
                Toast.LENGTH_LONG).show();
    }

    //This method starts a mTimer and updates the textview to show elapsed time for streaming
    public void startTimer() {
        llTimer.setVisibility(View.VISIBLE);
        if (mTimer == null) {
            mTimer = new Timer();
        }

        mTimer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                mElapsedTime += 1; //increase every sec
                mTimerHandler.obtainMessage(TimerHandler.INCREASE_TIMER)
                        .sendToTarget();
            }
        }, 0, 1000);
    }

    public void stopTimer() {

        if (mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = null;
        this.mElapsedTime = 0;
    }

    @Override
    public void onGiftSelect(GiftDataResponse.Data.Gift gift) {
        balance = Double.parseDouble(sessionManager.getCoinBalance());
        double giftCost = Double.parseDouble(gift.getGiftCost());
        if (balance >= giftCost) {
            sendGift(gift);
        } else {
            //show insufficient balance
            LowBalanceDialog lowBalanceDialog = new LowBalanceDialog(this, R.drawable.ic_coin, getString(R.string.insufficient_balance_message));
            lowBalanceDialog.show();
            Button btnRechargeNow = lowBalanceDialog.findViewById(R.id.btnRecharge);
            btnRechargeNow.setOnClickListener(v -> startActivity(new Intent(this, CoinActivity.class)));
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

                    break;
            }
        }
    }

    //WebRTC Streaming code

    @Override
    public void onPublishStarted() {

    }

    @Override
    public void onPublishFinished() {
    }

    @Override
    public void onError(String description) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
        //Toast.makeText(this, getString(R.string.stream_play_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignalChannelClosed(
            WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code) {
        timerPaused = true;
        updateOpponentLiveStatus(false);
        if (connectionLostDialog == null) {
            AlertDialog.Builder connectionLostDialogBuilder =
                    new AlertDialog.Builder(WebRTCStreamPlayerActivity.this).setMessage(
                            R.string.broadcast_connection_lost)
                            .setCancelable(true)
                            .setPositiveButton(R.string.change_stream, (dialog, which) -> finish());

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
        updateOpponentLiveStatus(false);
        connectionToStreamingServerLost = true;
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onIceDisconnected() {

        timerPaused = true;
        updateOpponentLiveStatus(false);
        connectionToStreamingServerLost = true;
    }

    @Override
    public void onIceConnected() {

        //it is called when connected to ice
        //        restartStream();
    }

    //@Override
    //public void onSurfaceInitialized() {
    //}

    @Override
    public void onPlayStarted() {
        hideProgress();
        timerPaused = false;
        updateOpponentLiveStatus(true);
        if (restartRequest) {
            getStreamChatHistoryAndSubscribers();
        }
    }

    @Override
    public void onPlayFinished() {

    }

    private void updateOpponentLiveStatus(boolean live) {

        if (live) {
            tvLiveStreamStatus.setText(getString(R.string.live_indicator));
        } else {
            tvLiveStreamStatus.setText(getString(R.string.offline_indicator));
        }
    }

    @Override
    public void noStreamExistsToPlay() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
        giftsVisible = false;
        try {
            Toast.makeText(WebRTCStreamPlayerActivity.this, getString(R.string.stream_offline),
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

    private void restartStream() {

        if (connectionToStreamingServerLost) {

            connectionToStreamingServerLost = false;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    restartRequest = true;
                    startPlaying();
                }
            });
        }
    }

    private void setMicrophoneMuted(int volume) {

        if (myAudioManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                        volume == 0 ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE, 0);
            } else {
                myAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, volume == 0);
            }
        }
    }

    public RecyclerView.OnScrollListener recyclerViewMessageOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = llm.getChildCount();
                    int totalItemCount = llm.getItemCount();
                    int firstVisibleItemPosition =
                            ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    presenter.callMessageApiOnScroll(streamId, firstVisibleItemPosition, visibleItemCount,
                            totalItemCount);
                }
            };

    @Override
    public void hideLoader() {
        if (loader != null && loader.isShowing()) loader.dismiss();
    }

    private void showMessage(String message) {
        tvMessage.setText(message);
        tvMessage.setVisibility(View.VISIBLE);
    }

    ////////////
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

    @Override
    public void broadcastViewer(List<Viewer> viewers) {
        viewersAdapter.updateData(viewers);
        bottomSheet.setVisibility(View.VISIBLE);
        bottomSheet.post(() -> {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
    }

    @OnClick(R.id.tvNoOfViewers)
    public void viewers() {
        presenter.broadcastViewer(streamId, 0, PAGE_SIZE);
    }

    @Override
    public void follow(boolean following, String userId) {
        if (following) {
            presenter.follow(userId);
        } else {
            presenter.unfollow(userId);
        }
    }

    @Override
    public void streamOffline() {

        giftsVisible = false;
        try {
            Toast.makeText(WebRTCStreamPlayerActivity.this, getString(R.string.stream_not_live),
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

    @Override
    public void giftCategories(List<GiftCategories.Data.Category> categories) {
        this.categories = categories;
        no_of_categories = categories.size();
        giftsAvailable = !categories.isEmpty();
        for (GiftCategories.Data.Category category : categories) {
            View view = LayoutInflater.from(this).inflate(R.layout.tab_gift, null);
            ImageView icon = view.findViewById(R.id.icon);
            Glide.with(this).load(category.getThumbnail()).asBitmap().into(new BitmapImageViewTarget(icon));
            TextView title = view.findViewById(R.id.title);
            title.setText(category.getTitle());
            tlGiftCategories.addTab(tlGiftCategories.newTab().setCustomView(view));
        }
        giftViewPagerAdapter = new GiftViewPagerAdapter(getSupportFragmentManager(), tlGiftCategories.getTabCount(), categories, liveStreamService, this);
        viewPager.setAdapter(giftViewPagerAdapter);
    }

    private void updateGiftsVisibility(boolean isGiftsVisible) {

        if (isGiftsVisible) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            rlGiftsParent.setVisibility(View.GONE);
            this.giftsVisible = false;
        } else {
            rlGiftsParent.setVisibility(View.VISIBLE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            this.giftsVisible = true;
        }
    }

    /*
     * BugId:DUBAND056
     * BugTitle:Sometimes not able to hear voice from viewer side (sometone its in earphone)
     * Developer name:Ashutosh & Shaktisinh
     * Fixed date:7 April 2021
     */

    private void enableSpeaker() {
        myAudioManager.setMode(AudioManager.MODE_IN_CALL);
        if (!myAudioManager.isSpeakerphoneOn())
            myAudioManager.setSpeakerphoneOn(true);
    }

    @OnClick(R.id.btnBuyCoins)
    public void btnBuyCoins() {
        startActivity(new Intent(this, CoinActivity.class));
    }

    @OnClick(R.id.ibCloseGift)
    public void ibCloseGift() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        rlGiftsParent.setVisibility(View.GONE);
    }
}