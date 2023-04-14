package chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.stories.model.Viewer;
import chat.hola.com.app.live_stream.CallbacksFromChatPredefined;
import chat.hola.com.app.live_stream.Observable.AllStreamsObservable;
import chat.hola.com.app.live_stream.ResponcePojo.AllGiftsData;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.ResponcePojo.GiftEvent;
import chat.hola.com.app.live_stream.ResponcePojo.Gifts;
import chat.hola.com.app.live_stream.ResponcePojo.LikeEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;
import chat.hola.com.app.live_stream.ResponcePojo.StreamPresenceEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamRestartEvent;
import chat.hola.com.app.live_stream.adapter.ChatPredefinedMessagesAdapter;
import chat.hola.com.app.live_stream.adapter.GiftCategoriesAdapter;
import chat.hola.com.app.live_stream.adapter.GiftsAdapter;
import chat.hola.com.app.live_stream.adapter.MessageAdapter;
import chat.hola.com.app.live_stream.heart_animation.HeartsRenderer;
import chat.hola.com.app.live_stream.heart_animation.HeartsView;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.pubsub.MqttEvents;
import chat.hola.com.app.live_stream.utility.AlertProgress;
import chat.hola.com.app.live_stream.utility.BitmapHelper;
import chat.hola.com.app.live_stream.utility.CustomLinearLayoutManager;
import chat.hola.com.app.live_stream.utility.RecyclerItemClickListener;
import chat.hola.com.app.live_stream.utility.TextDrawable;
import chat.hola.com.app.live_stream.utility.TimerHelper;
import chat.hola.com.app.live_stream.utility.Utility;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.GiftCategories;
import chat.hola.com.app.models.WalletResponse;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by moda on 11/28/2018.
 */
public class RTMPStreamPlayerActivity extends DaggerAppCompatActivity
    implements LiveStreamPresenterContract.LiveStreamView {

  private static final String TAG = RTMPStreamPlayerActivity.class.getSimpleName();

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

  @BindView(R.id.tvNoOfViewers)
  TextView tvNoOfViewers;
  @BindView(R.id.ivSendMessage)
  ImageView ivSendMessage;
  @BindView(R.id.etSendMessage)
  EditText etSendMessage;
  @BindView(R.id.rvMessages)
  RecyclerView rvMessages;
  @BindView(R.id.ivCloseStreaming)
  ImageView ivCloseStreaming;
  @BindView(R.id.ivHeart)
  ImageView ivHeart;

  @BindView(R.id.rvPresetMessages)
  RecyclerView rvPresetMessages;

  //    @BindView(R.id.rvGiftCategories)
  //    RecyclerView rvGiftCategories;

  @BindView(R.id.rvGifts)
  RecyclerView rvGifts;

  @BindView(R.id.rlGiftsParent)
  CoordinatorLayout rlGiftsParent;

  @BindView(R.id.ivGifts)
  AppCompatImageView ivGifts;

  @BindView(R.id.player_view)
  PlayerView player_view;

  @BindView(R.id.tvNoGifts)
  TextView tvNoGifts;

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

  //For the timer
  @BindView(R.id.tvTimer)
  TextView tvTimer;
  @BindView(R.id.llTimer)
  LinearLayout llTimer;

  private AlertDialog dialog;

  private ArrayList<GiftEvent> giftEvents = new ArrayList<>();
  private boolean giftShowing;
  private ArrayList<AllGiftsData> giftsCategories = new ArrayList<>();

  private ArrayList<Gifts> gifts = new ArrayList<>();
  private GiftCategoriesAdapter giftCategoriesAdapter;
  private GiftsAdapter giftsAdapter;

  private Observer<AllStreamsData> observer;
  private CompositeDisposable disposable;

  private MessageAdapter messageAdapter;
  private ArrayList<StreamChatMessage> messages = new ArrayList<>();

  private Bus bus = MQTTManager.getBus();
  private CustomLinearLayoutManager llm;

  private boolean alreadyEndedStream;

  /*For Heart animation*/

  private HeartsView.Model model;

  private SimpleExoPlayer player;

  private String streamId;

  private BottomSheetBehavior sheetBehavior;
  private boolean giftsVisible, giftsAvailable;
  private int density;
  private ExtractorMediaSource mediaSource;
  private boolean firstTime = true;
  private TrackSelection.Factory videoTrackSelectionFactory;

  //For updating the duration of stream
  private Timer mTimer;
  public TimerHandler mTimerHandler;
  private long mElapsedTime;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rtmp_player);
    ButterKnife.bind(this);
    mTimerHandler = new TimerHandler();
    disposable = new CompositeDisposable();
    String streamName = getIntent().getStringExtra("streamName");
    streamId = getIntent().getStringExtra("streamId");

    tvNoOfViewers.setText(getIntent().getStringExtra("viewers"));

    density = (int) getResources().getDisplayMetrics().density;

    //Preparing for stream playback

    RTMPDataSource.RtmpDataSourceFactory rtmpDataSourceFactory =
        new RTMPDataSource.RtmpDataSourceFactory();

    videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());

    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

    mediaSource = new ExtractorMediaSource.Factory(rtmpDataSourceFactory).setExtractorsFactory(
        extractorsFactory)
        //                .createMediaSource(Uri.parse(RTMP_BASE_URL + streamName + " live=1"));
        .createMediaSource(
            Uri.parse(ApiOnServer.RTMP_BASE_URL + streamName + " live=1 buffer=1000"));
    playStream();

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
          Toast.makeText(RTMPStreamPlayerActivity.this, R.string.no_gifts, Toast.LENGTH_LONG)
              .show();
        }
      }
    });

    initializeBottomSheet();

    initializeHeartsViews();

    checkMQttConnection();

    presenter.streamChatMessageRxJava();
    presenter.allStreamEventRxJAva();
    presenter.streamPresenceEventRxJAva();
    presenter.likeEventRxJAva();
    presenter.giftEventRxJAva();

    subscribeToApi();
    getStreamChatHistoryAndViewers();
    initializeMessagesAdapter();
    initializeGiftAdapters();
    fetchGifts();

    bus.register(this);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      if (!firstTime) {

        playStream();
      } else {

        firstTime = false;
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    if ((Util.SDK_INT <= 23)) {
      if (!firstTime) {

        playStream();
      } else {

        firstTime = false;
      }
    }
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(networkChangeReceiver, intentFilter);
  }

  private void playStream() {
    showProgress();

    try {

      //            player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector(videoTrackSelectionFactory), new DefaultLoadControl(
      //                    new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE),
      //                    500,  // min buffer 0.5s
      //                    3000, //max buffer 3s
      ////                    1000, // playback 1s
      //                    500, // playback 1s
      //                    500,   //playback after rebuffer 1s
      //                     C.LENGTH_UNSET ,
      //                    true

      player = ExoPlayerFactory.newSimpleInstance(this,
          new DefaultTrackSelector(videoTrackSelectionFactory));

      player.addVideoListener(new VideoListener() {
        @Override
        public void onRenderedFirstFrame() {

          //When the first frame is ready to be rendered

          hideProgress();
        }
      });

      player_view.setPlayer(player);
      player_view.requestFocus();
      player_view.setUseController(false);

      player.prepare(mediaSource);
      player.setPlayWhenReady(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void fetchGifts() {
    presenter.fetchGifts();
  }

  private void initializeGiftAdapters() {

    giftCategoriesAdapter =
        new GiftCategoriesAdapter(giftsCategories, RTMPStreamPlayerActivity.this, typefaceManager);

    //        rvGiftCategories.setLayoutManager(
    //                new LinearLayoutManager(RTMPStreamPlayerActivity.this, LinearLayoutManager.HORIZONTAL,
    //                        false));
    //        rvGiftCategories.setAdapter(giftCategoriesAdapter);
    //        rvGiftCategories.addOnItemTouchListener(
    //                new RecyclerItemClickListener(RTMPStreamPlayerActivity.this, rvGiftCategories,
    //                        new RecyclerItemClickListener.OnItemClickListener() {
    //                            @Override
    //                            public void onItemClick(View view, final int position) {
    //                                if (position >= 0) {
    //                                    ArrayList<Gifts> giftsData = giftsCategories.get(position).getGifts();
    //                                    if (giftsData.size() > 0) {
    //                                        tvNoGifts.setVisibility(View.GONE);
    //                                        rvGifts.setVisibility(View.VISIBLE);
    //                                        gifts.clear();
    //                                        gifts.addAll(giftsData);
    //                                        giftsAdapter.notifyDataSetChanged();
    //                                    } else {
    //
    //                                        rvGifts.setVisibility(View.GONE);
    //                                        tvNoGifts.setVisibility(View.VISIBLE);
    //                                    }
    //                                }
    //                            }
    //                        }
    //
    //                ));

    //        giftsAdapter = new
    //
    //                GiftsAdapter(gifts, RTMPStreamPlayerActivity.this);

    rvGifts.setLayoutManager(new

            GridLayoutManager(this, 3));
    rvGifts.setAdapter(giftsAdapter);

    rvGifts.addOnItemTouchListener(
        new RecyclerItemClickListener(RTMPStreamPlayerActivity.this, rvGifts,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {
                if (position >= 0) {

                  sendGift(gifts.get(position));

                  updateGiftsVisibility(giftsVisible);
                }
              }
            }

        ));
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

    Bitmap bitmap = BitmapHelper.getBitmapFromVectorDrawable(RTMPStreamPlayerActivity.this,
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
    llm = new CustomLinearLayoutManager(RTMPStreamPlayerActivity.this, LinearLayoutManager.VERTICAL,
        false);

    messageAdapter = new MessageAdapter(messages, this);
    rvMessages.setLayoutManager(llm);
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
        Utility.hideKeyboard(RTMPStreamPlayerActivity.this);
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

  private void getStreamChatHistoryAndViewers() {

    presenter.getLiveStreamsChatHistory(streamId, 0, 50);

    presenter.getActiveViewers(streamId);
  }

  @OnClick({ R.id.ivCloseStreaming, R.id.ivSendMessage })
  public void onViewClicked(View v) {
    switch (v.getId()) {
      case R.id.ivCloseStreaming:

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

    if (observer != null) {
      AllStreamsObservable.getInstance().removeObserver(observer);
    }
    disposable.clear();
    disposable.dispose();

    if (mqttManager.isMQttConnected()) {
      unsubscribeFromTopic();
    }

    bus.unregister(this);

    if (!alreadyEndedStream) {
      onBackPressed();
    }
  }

  @Override
  public void onBackPressed() {

    stopTimer();
    alreadyEndedStream = true;
    presenter.subscribeToStream(streamId, "stop");
    super.onBackPressed();
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
  public void onStreamUnsubscribed() {
    if (observer != null) AllStreamsObservable.getInstance().removeObserver(observer);
    observer = null;
    disposable.clear();
    disposable.dispose();
    supportFinishAfterTransition();
  }

  @Override
  public void showProgress() {
    //Shown at he time of fetching the first frame fo the stream

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
  protected void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      try {
        player.stop();
        player.release();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    unregisterReceiver(networkChangeReceiver);
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      try {
        player.stop();
        player.release();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
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

        getStreamChatHistoryAndViewers();
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
        } catch (IndexOutOfBoundsException e) {
          e.printStackTrace();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  public void onAllStreamEventReceived(AllStreamsData streamData) {

    if (streamData.getAction().equals("stop")) {
      if (streamData.getStreamId() != null && streamData.getStreamId().equals(streamId)) {

        if (!alreadyEndedStream) {
          onBackPressed();
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
          onBackPressed();
        }
      }
    } else if (streamPresenceEvent.getAction().equals("subscribe")
        || streamPresenceEvent.getAction().equals("unsubscribe")) {

      //For the subscribe or unsubscribe event

      tvNoOfViewers.setText(String.valueOf(streamPresenceEvent.getActiveViewwers()));
    }
  }

  @Override
  public void onStreamRestartEvent(StreamRestartEvent streamRestartEvent) {

  }

  @Override
  public void onActiveViewersReceived(int viewers) {

    tvNoOfViewers.setText(String.valueOf(viewers));
  }

  @Override
  public void onFailedToFetchActiveViewers(String message) {
    //Fetch gifts failed

    Toast.makeText(RTMPStreamPlayerActivity.this, getString(R.string.retrieve_viewers_failed),
        Toast.LENGTH_LONG).show();
  }

  @Override
  public void insufficientBalance() {

  }

  @Override
  public void showBalance(WalletResponse data) {

  }


  @Override
  public void startCoinAnimation(String coin) {

  }

  @Override
  public void hideLoader() {

  }

  @Override
  public void showFailedToPublishMessageAlert(String message) {
    //Publish message failed

    Toast.makeText(RTMPStreamPlayerActivity.this, getString(R.string.sending_message_failed),
        Toast.LENGTH_LONG).show();
  }

  @Override
  public void onFailedToFetchChatHistory(String message) {
    //Fetch gifts failed

    Toast.makeText(RTMPStreamPlayerActivity.this, getString(R.string.retrieve_chathistory_failed),
        Toast.LENGTH_LONG).show();
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
      heartsView.emitHeart(model);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onGiftEvent(GiftEvent giftEvent) {
    //On gift event received

    giftEvents.add(giftEvent);
    if (!giftShowing) {

      showGift(giftEvent);
    }
  }

  private void sendLike() {

    //Calling the api for the analytics

    if (mqttManager.isMQttConnected()) {
      presenter.likeStream(streamId);
    } else {

      Toast.makeText(RTMPStreamPlayerActivity.this, R.string.like_failed, Toast.LENGTH_LONG).show();
    }
  }

  private void sendGift(Gifts giftData) {

    //Calling the api for the analytics

    if (mqttManager.isMQttConnected()) {
      presenter.sendGift(streamId, null,  null);
    } else {
      Toast.makeText(RTMPStreamPlayerActivity.this, R.string.sending_gift_failed, Toast.LENGTH_LONG)
          .show();
    }
  }

  private void showGift(GiftEvent giftEvent) {

    giftShowing = true;
    tvGiftCoin.setText(giftEvent.getCoin() + getString(R.string.space) + getString(R.string.coins));
    tvGiftName.setText(giftEvent.getName());

    tvGiftSender.setText(giftEvent.getUserName());

    try {
      Glide.with(this).load(giftEvent.getImage()).asBitmap().centerCrop()
          //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
          //.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
          .into(new BitmapImageViewTarget(ivGiftImage));
      //            GlideApp.with(this).load(giftEvent.getImage()).into(ivGiftImage);
    } catch (Exception e) {
      e.printStackTrace();
    }

    //If in future we have image for each user being available as well
    if (giftEvent.getUserImage() != null && !giftEvent.getUserImage().isEmpty()) {
      try {
        Glide.with(this).load(giftEvent.getUserImage()).asBitmap().centerCrop()
            .signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)
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
      GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(ivGiftGif);
      DrawableRequestBuilder<String> thumbnailRequest = Glide.with(this)
          .load(giftEvent.getImage())
          .diskCacheStrategy(DiskCacheStrategy.SOURCE)
          .fitCenter();

      Glide.with(this)
          .load(giftEvent.getImage())
          .thumbnail(thumbnailRequest)
          .dontAnimate()
          .fitCenter()
          .listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                boolean isFirstResource) {
              onGifFinished();
              return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model,
                Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
              resource.setLoopCount(1);
              new Thread(() -> {
                while (true) {
                  if (!resource.isRunning()) {
                    onGifFinished();
                    break;
                  }
                }
              }).start();
              return false;
            }
          })
          .placeholder(getResources().getDrawable(R.drawable.ic_default))
          //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
          .into(imageViewTarget);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      onGifFinished();
    } catch (NullPointerException e) {
      e.printStackTrace();
      onGifFinished();
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

  public void updateViewersCount(int count) {

    //To update number of viewers of the stream

    tvNoOfViewers.setText(String.valueOf(count));
  }

  @Override
  public void onFailedToSubscribeStream(String message) {
    Toast.makeText(RTMPStreamPlayerActivity.this, getString(R.string.stream_subscribe_failed),
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
        mTimerHandler.obtainMessage(TimerHandler.INCREASE_TIMER).sendToTarget();
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

  private class TimerHandler extends Handler {

    static final int INCREASE_TIMER = 1;

    public void handleMessage(Message msg) {
      tvTimer.setText(TimerHelper.getDurationString(mElapsedTime));
    }
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
  public void broadcastViewer(List<Viewer> viewers) {

  }
  @Override
  public void streamOffline() {

    giftsVisible=false;
    onBackPressed();
  }

  @Override
  public void giftCategories(List<GiftCategories.Data.Category> categories) {

  }

  @Override
  public void setCoinBalance(String balance) {

  }
}