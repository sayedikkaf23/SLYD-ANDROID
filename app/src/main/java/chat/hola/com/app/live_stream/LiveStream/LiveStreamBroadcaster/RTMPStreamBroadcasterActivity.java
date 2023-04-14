package chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.material.snackbar.Snackbar;
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
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ApiOnServer;
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
import chat.hola.com.app.models.StreamStats;
import dagger.android.support.DaggerAppCompatActivity;
import io.isometrik.android.rtmp.ILiveVideoBroadcaster;
import io.isometrik.android.rtmp.LiveVideoBroadcaster;

public class RTMPStreamBroadcasterActivity extends DaggerAppCompatActivity
    implements LiveBroadCastPresenterContract.LiveBroadCastView {

  private static final String TAG = RTMPStreamBroadcasterActivity.class.getSimpleName();

  @Inject
  LiveBroadCastPresenterContract.LiveBroadCastPresenter presenter;
  @Inject
  MQTTManager mqttManager;
  @Inject
  SessionManager sessionManager;
  @Inject
  AlertProgress alertProgress;

  @BindView(R.id.clRootLayout)
  CoordinatorLayout clRootLayout;

  @BindView(R.id.etSendMessage)
  EditText etSendMessage;

  @BindView(R.id.tvTimer)
  TextView tvTimer;

  @BindView(R.id.tvLiveStreamStatus)
  TextView tvLiveStreamStatus;

  @BindView(R.id.tvNoOfViewers)
  TextView tvNoOfViewers;

  @BindView(R.id.glSurfaceView)
  GLSurfaceView glSurfaceView;

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

  @BindView(R.id.ivCloseLiveStream)
  AppCompatImageView ivCloseLiveStream;

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

  @BindView(R.id.ivMute)
  AppCompatImageView ivMute;

  private ArrayList<GiftEvent> giftEvents = new ArrayList<>();
  private boolean giftShowing;

  /*For Heart animation*/

  private HeartsView.Model model;

  private Timer mTimer;
  public TimerHandler mTimerHandler;

  private Bus bus = MQTTManager.getBus();

  private Intent mLiveVideoBroadcasterServiceIntent;

  private ILiveVideoBroadcaster mLiveVideoBroadcaster;

  private MessageAdapter messageAdapter;
  private ArrayList<StreamChatMessage> messages = new ArrayList<>();
  private String streamName = "rtmpstream-";
  private String streamId = "", streamType, thumbnail = "";
  private boolean stopStreamingRequested = false;
  private boolean streamingStarted = false;
  private CustomLinearLayoutManager llm;
  private boolean mIsRecording = false;
  private long mElapsedTime;
  private AlertDialog dialog;

  private int density;

  private DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

  private boolean connectionToStreamingServerLost;
  private AlertDialog connectionLostDialog;
  private boolean microPhoneMuted;

  /**
   * Defines callbacks for service binding, passed to bindService()
   */
  private ServiceConnection mConnection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName className, IBinder service) {
      // We've bound to LocalService, cast the IBinder and get LocalService instance
      LiveVideoBroadcaster.LocalBinder binder = (LiveVideoBroadcaster.LocalBinder) service;

      if (mLiveVideoBroadcaster == null) {

        mLiveVideoBroadcaster = binder.getService();
        mLiveVideoBroadcaster.init(RTMPStreamBroadcasterActivity.this, glSurfaceView);
        mLiveVideoBroadcaster.setAdaptiveStreaming(true);
        // DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        //  mLiveVideoBroadcaster.setResolution(new Resolution(displayMetrics.widthPixels, displayMetrics.heightPixels));
      }

      mLiveVideoBroadcaster.openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT,
          displayMetrics.widthPixels, displayMetrics.heightPixels);

      if (!streamingStarted && streamId != null) {

        startBroadCasting(false);
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName arg0) {

      mLiveVideoBroadcaster = null;
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Hide title
    //requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    //binding on resume not to having leaked service connection
    mLiveVideoBroadcasterServiceIntent = new Intent(this, LiveVideoBroadcaster.class);
    //this makes service do its job until done
    startService(mLiveVideoBroadcasterServiceIntent);
    setContentView(R.layout.activity_rtmp_broadcaster);
    ButterKnife.bind(this);
    mTimerHandler = new TimerHandler();
    density = (int) getResources().getDisplayMetrics().density;

    // Configure the GLSurfaceView.  This will start the Renderer thread, with an
    // appropriate EGL activity.

    if (glSurfaceView != null) {
      glSurfaceView.setEGLContextClientVersion(2);     // select GLES 2.0
    }

    ivSendMessage.setOnClickListener(onClickSendMessage());
    ivCloseLiveStream.setOnClickListener(onCloseClicked());
    ivMute.setSelected(true);
    ivMute.setOnClickListener(onMuteClicked());

    getIntentValue();
    initializeMessagesAdapter();
    initializeHeartsViews();

    presenter.streamChatMessageRxJava();
    presenter.streamPresenceEventRxJava();
    presenter.likeEventRxJAva();
    presenter.giftEventRxJAva();

    bus.register(this);
    setMicrophoneMuted(false);
  }

  private void initializeHeartsViews() {
    HeartsRenderer.Config config = new HeartsRenderer.Config(3f, 0.35f, 2f);

    Bitmap bitmap = BitmapHelper.getBitmapFromVectorDrawable(RTMPStreamBroadcasterActivity.this,
        R.drawable.ic_heart);

    model = new HeartsView.Model(0,// Unique ID of this image, used for Rajawali materials caching
        bitmap);
    heartsView.applyConfig(config);
  }

  private void initializeMessagesAdapter() {

    messageAdapter = new MessageAdapter(messages, this);

    llm = new CustomLinearLayoutManager(RTMPStreamBroadcasterActivity.this,
        LinearLayoutManager.VERTICAL, false);

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
        Utility.hideKeyboard(RTMPStreamBroadcasterActivity.this);
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
          Utility.hideKeyboard(RTMPStreamBroadcasterActivity.this);
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
    }

    checkMQttConnection();

    if (!streamingStarted && mLiveVideoBroadcaster != null) {
      startBroadCasting(false);
    }
  }

  private void checkMQttConnection() {

    if (mqttManager.isMQttConnected()) {

      subscribeToTopic();
    }
  }

  public void changeCamera(View v) {
    if (mLiveVideoBroadcaster != null) {

      mLiveVideoBroadcaster.changeCamera(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    //this lets activity bind
    bindService(mLiveVideoBroadcasterServiceIntent, mConnection, 0);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case LiveVideoBroadcaster.PERMISSIONS_REQUEST: {
        if (mLiveVideoBroadcaster.isPermissionGranted()) {
          mLiveVideoBroadcaster.openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT,
              displayMetrics.widthPixels, displayMetrics.heightPixels);
        } else {
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
              || ActivityCompat.shouldShowRequestPermissionRationale(this,
              Manifest.permission.RECORD_AUDIO)
              || ActivityCompat.shouldShowRequestPermissionRationale(this,
              Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mLiveVideoBroadcaster.requestPermission();
          } else {
            new AlertDialog.Builder(RTMPStreamBroadcasterActivity.this).setTitle(
                R.string.permission)
                .setMessage(getString(R.string.app_doesnot_work_without_permissions))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {

                    try {
                      //Open the specific App Info page:
                      Intent intent =
                          new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                      intent.setData(
                          Uri.parse("package:" + getApplicationContext().getPackageName()));
                      startActivity(intent);
                    } catch (ActivityNotFoundException e) {

                      //Open the generic Apps page:
                      Intent intent =
                          new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                      startActivity(intent);
                    }
                  }
                })
                .show();
          }
        }
        return;
      }
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    unregisterReceiver(networkChangeReceiver);

    mLiveVideoBroadcaster.pause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    unbindService(mConnection);
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
      mLiveVideoBroadcaster.setDisplayOrientation();
    }
  }

  private void startBroadCasting(boolean restartRequest) {

    if (connectionLostDialog != null && connectionLostDialog.isShowing()) {
      connectionLostDialog.dismiss();
    }

    streamingStarted = true;
    if (!mIsRecording) {

      if (mLiveVideoBroadcaster != null) {

        if (!mLiveVideoBroadcaster.isConnected()) {

          showProgress();

          new AsyncTask<String, String, Boolean>() {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Boolean doInBackground(String... url) {

              return mLiveVideoBroadcaster.startBroadcasting(url[0]);
            }

            @Override
            protected void onPostExecute(Boolean result) {

              mIsRecording = result;

              hideProgress();
              if (result) {

                tvLiveStreamStatus.setVisibility(View.VISIBLE);
                llStartStreaming.setVisibility(View.VISIBLE);
                llTimer.setVisibility(View.VISIBLE);
                //start the streaming duration

                if (restartRequest) {
                  presenter.getLiveStreamsChatHistory(streamId, 0, 50);

                  presenter.getActiveViewers(streamId);
                }
                startTimer();
              } else {
                Snackbar.make(clRootLayout, R.string.stream_not_started, Snackbar.LENGTH_LONG)
                    .show();

                triggerStopStreaming(false);

                exitStreamingScreen();
              }
            }
          }.execute(ApiOnServer.RTMP_BASE_URL + streamName);
        } else {
          Snackbar.make(clRootLayout, R.string.streaming_not_finished, Snackbar.LENGTH_LONG).show();

          exitStreamingScreen();
        }
      } else {
        Snackbar.make(clRootLayout, R.string.oopps_shouldnt_happen, Snackbar.LENGTH_LONG).show();

        exitStreamingScreen();
      }
    }
  }

  public void triggerStopStreaming(boolean connectionLost) {
    if (mIsRecording) {
      mIsRecording = false;
      stopTimer();
      if (connectionLost) {
        llStartStreaming.setVisibility(View.GONE);
      }
      tvLiveStreamStatus.setVisibility(View.GONE);
      mLiveVideoBroadcaster.stopBroadcasting();
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
        mTimerHandler.obtainMessage(TimerHandler.INCREASE_TIMER).sendToTarget();

        if (mLiveVideoBroadcaster == null || !mLiveVideoBroadcaster.isConnected()) {
          mTimerHandler.obtainMessage(TimerHandler.CONNECTION_LOST).sendToTarget();
        }
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
  public void showProgress() {
    if (dialog == null) {

      dialog = alertProgress.getProgressDialog(this, getString(R.string.starting_streaming));
    }
    dialog.show();
  }

  @Override
  public void hideProgress() {

    if (dialog != null && dialog.isShowing()) dialog.dismiss();
  }

  @Override
  public void apiError(String errorMsg) {

  }

  @Override
  public void onNetworkRetry(boolean isNetwork, String thisApi) {

  }

  private class TimerHandler extends Handler {
    static final int CONNECTION_LOST = 2;
    static final int INCREASE_TIMER = 1;

    public void handleMessage(Message msg) {
      switch (msg.what) {
        case INCREASE_TIMER:
          tvTimer.setText(TimerHelper.getDurationString(mElapsedTime));

          break;
        case CONNECTION_LOST:
          triggerStopStreaming(true);

          if (connectionLostDialog == null) {
            AlertDialog.Builder connectionLostDialogBuilder =
                new AlertDialog.Builder(RTMPStreamBroadcasterActivity.this).setMessage(
                    R.string.broadcast_connection_lost)
                    .setPositiveButton(android.R.string.yes, null);

            // Create the Alert dialog
            connectionLostDialog = connectionLostDialogBuilder.create();
          }

          connectionLostDialog.show();
          connectionToStreamingServerLost = true;
          break;
      }
    }
  }

  @Override
  public void onStreamSubscribed(String streamId) {
    supportFinishAfterTransition();
  }

  @Override
  public void onBackPressed() {

    stopVideoStreaming();
    super.onBackPressed();
  }

  @Override
  protected void onDestroy() {

    if (!stopStreamingRequested) {
      stopVideoStreaming();
    }
    presenter.clearObservables();
    try {
      bus.unregister(this);
    } catch (Exception e) {
    }
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

        onBackPressed();
      } else if (streamPresenceEvent.getAction().equals("subscribe")
          || streamPresenceEvent.getAction().equals("unsubscribe")) {

        //For the subscribe or unsubscribe event

        tvNoOfViewers.setText(String.valueOf(streamPresenceEvent.getActiveViewwers()));
      }
    }
  }

  private void stopVideoStreaming() {
    stopStreamingRequested = true;
    triggerStopStreaming(false);
    streamType = "stop";
    presenter.subscribeStream(streamId, streamType, thumbnail, streamName, false, mElapsedTime, "",
        "", "", "", "", 0, 0);
    unsubscribeToTopic();
  }

  @SuppressWarnings("TryWithIdenticalCatches")
  @Subscribe
  public void getMessage(JSONObject object) {

    try {
      if (object.getString("eventName").equals("connect")) {

        subscribeToTopic();

        if (connectionToStreamingServerLost) {

          connectionToStreamingServerLost = false;

          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              startBroadCasting(true);
            }
          });
        }
      } else if (object.getString("eventName").equals("disconnect")) {

      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void unsubscribeToTopic() {
    mqttManager.unSubscribeFromTopic(
        MqttEvents.ParticularStreamPresenceEvents.value + "/" + streamId);
    mqttManager.unSubscribeFromTopic(
        MqttEvents.ParticularStreamChatMessages.value + "/" + streamId);

    mqttManager.unSubscribeFromTopic(MqttEvents.ParticularStreamLikeEvent.value + "/" + streamId);
    mqttManager.unSubscribeFromTopic(MqttEvents.ParticularStreamGiftEvent.value + "/" + streamId);
  }

  private void subscribeToTopic() {
    mqttManager.subscribeToTopic(MqttEvents.ParticularStreamPresenceEvents.value + "/" + streamId,
        1);
    mqttManager.subscribeToTopic(MqttEvents.ParticularStreamChatMessages.value + "/" + streamId, 1);

    mqttManager.subscribeToTopic(MqttEvents.ParticularStreamLikeEvent.value + "/" + streamId, 1);
    mqttManager.subscribeToTopic(MqttEvents.ParticularStreamGiftEvent.value + "/" + streamId, 1);
  }

  private void exitStreamingScreen() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        onBackPressed();
      }
    }, 2000);
  }

  @Override
  public void showFailedToPublishMessageAlert(String message) {
    //Publish message failed

    Toast.makeText(RTMPStreamBroadcasterActivity.this, getString(R.string.sending_message_failed),
        Toast.LENGTH_LONG).show();
  }

  @Override
  public void onFailedToFetchChatHistory(String message) {
    //Fetch gifts failed

    Toast.makeText(RTMPStreamBroadcasterActivity.this,
        getString(R.string.retrieve_chathistory_failed), Toast.LENGTH_LONG).show();
  }

  @Override
  public void onFailedToFetchActiveViewers(String message) {
    //Fetch gifts failed

    Toast.makeText(RTMPStreamBroadcasterActivity.this, getString(R.string.retrieve_viewers_failed),
        Toast.LENGTH_LONG).show();
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

  @Override
  public void broadcastViewer(List<Viewer> viewers) {

  }

  @Override
  public void showStats(StreamStats.Stats stats) {

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

    if (giftEvent.getUserImage() != null && !giftEvent.getUserImage().isEmpty()) {
      try {
        Glide.with(this)
            .load(giftEvent.getUserImage())
            .asBitmap()
            .centerCrop()
            .signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
            //    .diskCacheStrategy(DiskCacheStrategy.NONE)
            //    .skipMemoryCache(true)
            .into(new BitmapImageViewTarget(ivUserImage) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivUserImage.setImageDrawable(circularBitmapDrawable);
              }
            });
        //                GlideApp.with(this).load(giftEvent.getUserImage()).circleCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).
        //                        into(ivUserImage);
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

    //            GlideApp.with(this).asGif().load(giftEvent.getGifs()).fitCenter()
    //                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC)).listener(new RequestListener<GifDrawable>() {
    //                @Override
    //                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
    //                    onGifFinished();//do your stuff
    //                    return false;
    //                }
    //
    //                @Override
    //                public boolean onResourceReady(final GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
    //                    resource.setLoopCount(1);
    //                    new Thread(new Runnable() {
    //                        @Override
    //                        public void run() {
    //                            while (true) {
    //                                try {
    //                                    Thread.sleep(200);
    //                                } catch (InterruptedException e) {
    //                                    e.printStackTrace();
    //                                }
    //
    //                                if (!resource.isRunning()) {
    //                                    onGifFinished();//do your stuff
    //                                    break;
    //                                }
    //                            }
    //
    //
    //                        }
    //                    }).start();
    //                    return false;
    //                }
    //            }).into(ivGiftGif);

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

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(networkChangeReceiver, intentFilter);
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
    Toast.makeText(RTMPStreamBroadcasterActivity.this, getString(R.string.stream_subscribe_failed),
        Toast.LENGTH_LONG).show();
  }

  @Override
  public void onAllStreamEventReceived(AllStreamsData streamData) {

  }

  @Override
  public void streamOffline() {
    onBackPressed();
  }
}
