package io.isometrik.groupstreaming.ui.scrollable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.action.CopublishActionCallbacks;
import io.isometrik.groupstreaming.ui.action.accept.AcceptedCopublishRequestFragment;
import io.isometrik.groupstreaming.ui.action.reject.RejectedCopublishRequestFragment;
import io.isometrik.groupstreaming.ui.action.request.CopublishRequestStatusFragment;
import io.isometrik.groupstreaming.ui.action.request.RequestCopublishFragment;
import io.isometrik.groupstreaming.ui.effects.EffectsFragment;
import io.isometrik.groupstreaming.ui.endleave.EndLeaveFragment;
import io.isometrik.groupstreaming.ui.gifts.GiftsActionCallback;
import io.isometrik.groupstreaming.ui.gifts.GiftsFragment;
import io.isometrik.groupstreaming.ui.gifts.GiftsModel;
import io.isometrik.groupstreaming.ui.hearts.HeartsRenderer;
import io.isometrik.groupstreaming.ui.hearts.HeartsView;
import io.isometrik.groupstreaming.ui.members.add.AddMembersFragment;
import io.isometrik.groupstreaming.ui.members.list.MembersFragment;
import io.isometrik.groupstreaming.ui.messages.MessagesAdapter;
import io.isometrik.groupstreaming.ui.messages.MessagesModel;
import io.isometrik.groupstreaming.ui.messages.PredefinedMessagesAdapter;
import io.isometrik.groupstreaming.ui.profile.UserInfoFragment;
import io.isometrik.groupstreaming.ui.requests.RequestListActionCallback;
import io.isometrik.groupstreaming.ui.requests.RequestsFragment;
import io.isometrik.groupstreaming.ui.settings.RemoteUserSettings;
import io.isometrik.groupstreaming.ui.settings.SettingsFragment;
import io.isometrik.groupstreaming.ui.settings.callbacks.ActionCallback;
import io.isometrik.groupstreaming.ui.settings.callbacks.ActionEnum;
import io.isometrik.groupstreaming.ui.streamdetails.StreamDetailsActivity;
import io.isometrik.groupstreaming.ui.streams.grid.StreamsModel;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.BitmapHelper;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.groupstreaming.ui.utils.KeyboardUtil;
import io.isometrik.groupstreaming.ui.utils.MessageTypeEnum;
import io.isometrik.groupstreaming.ui.utils.PredefinedMessagesUtil;
import io.isometrik.groupstreaming.ui.utils.StreamDialog;
import io.isometrik.groupstreaming.ui.utils.StreamDialogEnum;
import io.isometrik.groupstreaming.ui.utils.TimeDownView;
import io.isometrik.groupstreaming.ui.utils.TimeUtil;
import io.isometrik.groupstreaming.ui.utils.shimmer.ShimmerFrameLayout;
import io.isometrik.groupstreaming.ui.viewers.ViewersFragment;
import io.isometrik.groupstreaming.ui.viewers.ViewersListModel;
import io.isometrik.groupstreaming.ui.viewers.ViewersUtil;
import io.isometrik.gs.rtcengine.rtc.IsometrikRtcEventHandler;
import io.isometrik.gs.rtcengine.utils.UserFeedClickedCallback;
import io.isometrik.gs.rtcengine.utils.VideoGridContainer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.jetbrains.annotations.NotNull;

/**
 * The Scrollable streams activity containing code for joining a broadcast as a publisher or
 * audience,
 * switch
 * profile from a viewer to publisher, send/receive gifts or likes, exchange/remove messages.
 * List of members, viewers, or copublish requests can also be seen in bottomsheet fragments, as
 * well a new copulish request can be created/deleted by viewer and that can be accepted/rejected
 * by the host.Publishers can also apply AR effects/switch camera and control various other
 * settings
 * like mute/unmute local or remote media.
 *
 * It implements ScrollableStreamsContract.View{@link ScrollableStreamsContract.View}
 *
 * @see ScrollableStreamsContract.View
 * @see MembersFragment
 * @see RequestsFragment
 * @see ViewersFragment
 * @see SettingsFragment
 * @see UserInfoFragment
 * @see EffectsFragment
 * @see RequestCopublishFragment
 * @see CopublishRequestStatusFragment
 * @see AcceptedCopublishRequestFragment
 * @see RejectedCopublishRequestFragment
 * @see EndLeaveFragment
 * @see GiftsFragment
 * @see AddMembersFragment
 */
public class ScrollableStreamsActivity extends AppCompatActivity
    implements ActionCallback, ScrollableStreamsContract.View, IsometrikRtcEventHandler,
    CopublishActionCallbacks, GiftsActionCallback, UserFeedClickedCallback,
    RequestListActionCallback {

  ScrollableStreamsContract.Presenter scrollableStreamsPresenter;

  @BindView(R2.id.rlRootLayout)
  RelativeLayout rlRootLayout;

  @BindView(R2.id.tvNoOfMembers)
  TextView tvNoOfMembers;
  @BindView(R2.id.tvNoOfViewers)
  TextView tvNoOfViewers;
  //@BindView(R2.id.ibInfo)
  //AppCompatImageButton ibInfo;
  //
  @BindView(R2.id.tvConnectionState)
  TextView tvConnectionState;
  @BindView(R2.id.tvLiveStreamStatus)
  TextView tvLiveStreamStatus;

  @BindView(R2.id.rvMessages)
  RecyclerView rvMessages;
  @BindView(R2.id.rvPredefinedMessages)
  RecyclerView rvPredefinedMessages;

  @BindView(R2.id.etSendMessage)
  AppCompatEditText etSendMessage;

  @BindView(R2.id.tvDuration)
  TextView tvDuration;

  @BindView(R2.id.heartsView)
  HeartsView heartsView;

  @BindView(R2.id.videoGridContainer)
  VideoGridContainer videoGridContainer;

  @BindView(R2.id.tvInitiatorIdentifier)
  TextView tvInitiatorIdentifier;
  @BindView(R2.id.tvInitiatorName)
  TextView tvInitiatorName;
  @BindView(R2.id.ivInitiatorImage)
  AppCompatImageView ivInitiatorImage;
  @BindView(R2.id.tvCountDown)
  TimeDownView tvCountDown;

  private ArrayList<MessagesModel> messages = new ArrayList<>();
  private MessagesAdapter messagesAdapter;
  private LinearLayoutManager messagesLayoutManager;
  private boolean unregisteredListeners, alreadyStoppedPublishing;//microPhoneMuted, videoMuted, ;
  private String streamId;
  private HeartsView.Model model;

  private boolean showingStreamDialog;

  private TimerHandler timerHandler = new TimerHandler();
  private Timer mTimer;
  private boolean timerPaused = false;
  private boolean reconnectRequest;
  private long duration, streamStartTime;
  /**
   * For gifts
   */
  private boolean showingGifts;
  private ArrayList<GiftsModel> giftsModels = new ArrayList<>();

  @BindView(R2.id.tvGiftCoinValue)
  TextView tvGiftCoinValue;
  @BindView(R2.id.tvGiftSenderName)
  TextView tvGiftSenderName;
  @BindView(R2.id.tvGiftName)
  TextView tvGiftName;

  @BindView(R2.id.ivGiftImage)
  AppCompatImageView ivGiftImage;
  @BindView(R2.id.ivGiftGif)
  AppCompatImageView ivGiftGif;
  @BindView(R2.id.ivGiftSenderImage)
  AppCompatImageView ivGiftSenderImage;
  @BindView(R2.id.rlGifts)
  RelativeLayout rlGifts;

  @BindView(R2.id.ivLike)
  AppCompatImageView ivLike;
  @BindView(R2.id.ivJoin)
  AppCompatImageView ivJoin;
  @BindView(R2.id.ivRequest)
  AppCompatImageView ivRequest;
  @BindView(R2.id.ivAddMember)
  AppCompatImageView ivAddMember;
  @BindView(R2.id.ivGifts)
  AppCompatImageView ivGifts;
  @BindView(R2.id.ivEffects)
  AppCompatImageView ivEffects;
  @BindView(R2.id.ivBeautify)
  AppCompatImageView ivBeautify;
  @BindView(R2.id.ivSwitchCamera)
  AppCompatImageView ivSwitchCamera;
  @BindView(R2.id.ivSettings)
  AppCompatImageView ivSettings;

  //For the viewer images view on top-right
  @BindView(R2.id.ivOne)
  AppCompatImageView viewerImageOne;
  @BindView(R2.id.ivTwo)
  AppCompatImageView viewerImageTwo;
  @BindView(R2.id.ivThree)
  AppCompatImageView viewerImageThree;
  @BindView(R2.id.ivFour)
  AppCompatImageView viewerImageFour;
  @BindView(R2.id.ivFive)
  AppCompatImageView viewerImageFive;
  @BindView(R2.id.ivMore)
  AppCompatImageView ivMore;

  @BindView(R2.id.rlDisclaimer)
  RelativeLayout rlDisclaimer;

  @BindView(R2.id.shimmerFrameLayout)
  ShimmerFrameLayout shimmerFrameLayout;

  private MembersFragment membersFragment;
  private RequestsFragment requestsFragment;
  private ViewersFragment viewersFragment;
  private SettingsFragment settingsFragment;
  private UserInfoFragment userInfoFragment;
  private EffectsFragment effectsFragment;
  private RequestCopublishFragment requestCopublishFragment;
  private CopublishRequestStatusFragment copublishRequestStatusFragment;
  private AcceptedCopublishRequestFragment acceptedCopublishRequestFragment;
  private RejectedCopublishRequestFragment rejectedCopublishRequestFragment;
  private EndLeaveFragment endLeaveFragment;
  private GiftsFragment giftsFragment;
  private AddMembersFragment addMembersFragment;
  private RemoteUserSettings remoteUserSettings;

  private boolean isPublic, isBroadcaster, beautificationApplied, isAdmin;
  private String initiatorName, initiatorIdentifier, initiatorImageUrl;
  private ViewersUtil viewersUtil;
  private AlertProgress alertProgress;
  private AlertDialog alertDialog;

  private ArrayList<ViewersListModel> viewersListModels = new ArrayList<>();

  /**
   * For copublish requests who joined as viewer
   */
  private boolean alreadyRequestedCopublish;
  private boolean pending, accepted, countDownCompleted, channelJoined;
  private int viewersCount;

  private android.app.AlertDialog goLiveDialog;
  private AlertDialog alert;
  /**
   * For live streams as scrollable list
   */
  @BindView(R2.id.rvStreams)
  RecyclerView rvStreams;
  private ArrayList<StreamsModel> streams;

  private ScrollableStreamsAdapter streamsAdapter;

  private LinearLayoutManager streamsLayoutManager;
  @BindView(R2.id.refresh)
  SwipeRefreshLayout refresh;
  @BindView(R2.id.tvNoBroadcaster)
  TextView tvNoBroadCaster;
  @BindView(R2.id.rlStreams)
  RelativeLayout rlStreams;
  private int membersCount;
  private boolean switchChannelEnabled;

  @SuppressWarnings("MoveFieldAssignmentToInitializer")
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ism_activity_scrollable_streams);
    ButterKnife.bind(this);

    scrollableStreamsPresenter = new ScrollableStreamsPresenter(this);

    viewersUtil = new ViewersUtil(viewerImageOne, viewerImageTwo, viewerImageThree, viewerImageFour,
        viewerImageFive, ivMore, tvNoOfViewers, this);

    membersFragment = new MembersFragment();
    requestsFragment = new RequestsFragment();
    viewersFragment = new ViewersFragment();
    settingsFragment = new SettingsFragment();
    userInfoFragment = new UserInfoFragment();
    effectsFragment = new EffectsFragment();
    requestCopublishFragment = new RequestCopublishFragment();
    copublishRequestStatusFragment = new CopublishRequestStatusFragment();
    acceptedCopublishRequestFragment = new AcceptedCopublishRequestFragment();
    rejectedCopublishRequestFragment = new RejectedCopublishRequestFragment();
    endLeaveFragment = new EndLeaveFragment();
    giftsFragment = new GiftsFragment();
    addMembersFragment = new AddMembersFragment();
    remoteUserSettings = new RemoteUserSettings();

    goLiveDialog = new android.app.AlertDialog.Builder(this).create();

    isPublic = getIntent().getBooleanExtra("isPublic", false);
    isBroadcaster = getIntent().getBooleanExtra("isBroadcaster", true);
    isAdmin = getIntent().getBooleanExtra("isAdmin", false);
    streamId = getIntent().getStringExtra("streamId");
    streamStartTime = getIntent().getLongExtra("startTime", 0);
    scrollableStreamsPresenter.initialize(streamId,
        getIntent().getStringArrayListExtra("memberIds"), isAdmin, isBroadcaster);
    toggleRoleBasedActionButtonsVisibility(true);

    messagesLayoutManager = new LinearLayoutManager(this);
    rvMessages.setLayoutManager(messagesLayoutManager);
    messagesAdapter = new MessagesAdapter(this, messages);
    rvMessages.addOnScrollListener(messagesRecyclerViewOnScrollListener);
    rvMessages.setAdapter(messagesAdapter);

    scrollableStreamsPresenter.registerStreamViewersEventListener();
    scrollableStreamsPresenter.registerStreamMembersEventListener();
    scrollableStreamsPresenter.registerStreamsEventListener();
    scrollableStreamsPresenter.registerStreamMessagesEventListener();
    scrollableStreamsPresenter.registerConnectionEventListener();
    scrollableStreamsPresenter.registerPresenceEventListener();
    //Due to switch profile event have to register,even incase of private streams
    scrollableStreamsPresenter.registerCopublishRequestsEventListener();

    if (isBroadcaster) {
      if (getIntent().getBooleanExtra("publishRequired", true)) {

        scrollableStreamsPresenter.updateBroadcastingStatus(true, streamId, false);
      } else {
        if (isAdmin) {
          rlDisclaimer.setVisibility(View.VISIBLE);
          tvCountDown.setVisibility(View.VISIBLE);
          tvCountDown.downSecond(2);
          tvCountDown.setOnTimeDownListener(new TimeDownView.DownTimeWatcher() {
            @Override
            public void onTime(int num) {

            }

            @Override
            public void onLastTime(int num) {

            }

            @Override
            public void onLastTimeFinish(int num) {
              tvCountDown.setVisibility(View.GONE);
              countDownCompleted = true;
              if (channelJoined) {
                if (!getIntent().getBooleanExtra("publishRequired", true)) {
                  if (!isFinishing()) {

                    dismissAllDialogs();
                    showGoLiveView(goLiveDialog);
                  }
                }
              }
            }
          });
        }
        onBroadcastStatusUpdated(true, false, getIntent().getStringExtra("rtcToken"));
      }
    } else {
      scrollableStreamsPresenter.joinAsViewer(streamId, false);
    }
    alertProgress = new AlertProgress();
    IsometrikUiSdk.getInstance().getIsometrik().registerBroadcastRtcEventHandler(this);

    rvPredefinedMessages.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    rvPredefinedMessages.setAdapter(
        new PredefinedMessagesAdapter(PredefinedMessagesUtil.getPredefinedMessages(), this));

    initiatorName = getIntent().getStringExtra("initiatorName");

    if (isAdmin) {

      initiatorName = getString(R.string.ism_you, initiatorName);
    }
    initiatorIdentifier = getIntent().getStringExtra("initiatorIdentifier");
    initiatorImageUrl = getIntent().getStringExtra("initiatorImage");
    membersCount = getIntent().getIntExtra("membersCount", 0);
    viewersCount = getIntent().getIntExtra("viewersCount", 0);

    updateStreamInfo(null);

    streamsLayoutManager = new LinearLayoutManager(this);
    rvStreams.setLayoutManager(streamsLayoutManager);

    SnapHelper snapHelper = new PagerSnapHelper();
    snapHelper.attachToRecyclerView(rvStreams);

    //noinspection unchecked
    streams = (ArrayList<StreamsModel>) getIntent().getSerializableExtra("streams");

    if (streams == null) {
      //For broadcaster streams are not passed
      streams = new ArrayList<>();
    }
    streamsAdapter = new ScrollableStreamsAdapter(this, streams);
    rvStreams.addOnScrollListener(streamsRecyclerViewOnScrollListener);
    rvStreams.setAdapter(streamsAdapter);

    //Disable scrolls for publisher
    rvStreams.suppressLayout(isBroadcaster);

    if (!isBroadcaster) {
      rvStreams.scrollToPosition(getIntent().getIntExtra("streamPosition", 0));
    }
    refresh.setOnRefreshListener(this::fetchLatestStreams);
  }

  /**
   * To pass the touch to view underneath
   *
   * @param ev motion event
   */
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    videoGridContainer.dispatchTouchEvent(ev);
    return super.dispatchTouchEvent(ev);
  }

  /**
   * Fetch latest messages sent in the stream group
   */
  private void fetchLatestMessages() {
    try {
      scrollableStreamsPresenter.requestStreamMessagesData(streamId, Constants.MESSAGES_PAGE_SIZE,
          true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Open StreamDetailsActivity{@link StreamDetailsActivity} to show
   * stream details.
   *
   * @see StreamDetailsActivity
   */
  //@OnClick(R2.id.ibInfo)
  //public void showStreamInfo() {
  //
  //  startActivity(new Intent(this, StreamDetailsActivity.class).putExtra("streamDescription",
  //      getIntent().getStringExtra("streamDescription"))
  //      .putExtra("streamId", streamId)
  //      .putExtra("streamImage", getIntent().getStringExtra("streamImage"))
  //      .putExtra("initiatorName", getIntent().getStringExtra("initiatorName"))
  //      .putExtra("startTime", getIntent().getLongExtra("startTime", 0))
  //      .putExtra("audienceRequest", false));
  //}

  /**
   * Apply beautify options.
   */
  @OnClick(R2.id.ivBeautify)
  public void beautify() {

    beautificationApplied = !beautificationApplied;
    ivBeautify.setSelected(beautificationApplied);
    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .applyBeautifyOptions(beautificationApplied);
  }

  /**
   * Exit in case there are no live streams.
   */

  @OnClick(R2.id.ibExit)
  public void exitOnNoLiveStreams() {
    onBackPressed();
  }

  /**
   * Toggle camera between from and back camera.
   */
  @OnClick(R2.id.ivSwitchCamera)
  public void switchCamera() {

    IsometrikUiSdk.getInstance().getIsometrik().getBroadcastOperations().switchCamera();
  }

  /**
   * Send text message in a stream group.
   */
  @OnClick(R2.id.ivSendMessage)
  public void sendTextMessage() {
    if (etSendMessage.getText() != null && !etSendMessage.getText().toString().trim().isEmpty()) {
      String textMessage = etSendMessage.getText().toString().trim();
      scrollableStreamsPresenter.sendMessage(streamId, textMessage,
          MessageTypeEnum.NormalMessage.getValue(), 0, "");
      etSendMessage.setText("");
    }
  }

  /**
   * Remove message from a stream group.
   *
   * @param messageId the id of the message to be removed from the stream group
   * @param timestamp the timestamp at which message to be removed was sent
   */
  public void removeMessage(String messageId, long timestamp) {

    scrollableStreamsPresenter.removeMessage(streamId, messageId, timestamp);
  }

  /**
   * The Recyclerview's on scroll listener to fetch next set of messages on scroll.
   */
  private RecyclerView.OnScrollListener messagesRecyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          if (dy != 0 && messagesLayoutManager.findFirstVisibleItemPosition() == 0) {
            scrollableStreamsPresenter.requestStreamMessagesDataOnScroll(streamId);
          }
        }
      };

  /**
   * The Recyclerview's on scroll listener to fetch next set of streams on scroll.
   */
  private RecyclerView.OnScrollListener streamsRecyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);

          if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (streamsLayoutManager.findFirstVisibleItemPosition() > -1) {

              if (!isBroadcaster) {
                updateShimmerVisibility(false);

                updateStreamInfo(streams.get(streamsLayoutManager.findFirstVisibleItemPosition()));
              }
            }
          }
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);
          scrollableStreamsPresenter.requestLiveStreamsDataOnScroll(
              streamsLayoutManager.findFirstVisibleItemPosition(),
              streamsLayoutManager.getChildCount(), streamsLayoutManager.getItemCount());
        }
      };

  /**
   * @param messages the messages fetched in the stream group
   * @param refreshRequest whether the request for fetching messages is for latest messages or
   * paging
   *
   * {@link ScrollableStreamsContract.View#onStreamMessagesDataReceived(ArrayList,
   * boolean)}
   */
  @Override
  public void onStreamMessagesDataReceived(ArrayList<MessagesModel> messages,
      boolean refreshRequest) {

    //To avoid missing the message of self publish status change
    //if (refreshRequest) {
    //  this.messages.clear();
    //}

    runOnUiThread(() -> {
      this.messages.addAll(0, messages);
      messagesAdapter.notifyDataSetChanged();
      if (refreshRequest) scrollToLastMessage();
    });
  }

  /**
   * @param message the message to be shown in the popup on stream group being offline
   * @param dialogType the dialog type to identify type of layout to inflate to be shown in the
   * popup
   * @param streamId the id of the stream which is no longer live
   *
   * {@link ScrollableStreamsContract.View#onStreamOffline(String,
   * int, String, boolean)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType, String streamId, boolean kickout) {
    //kickout is not used as of now
    dismissAllDialogs();

    if (this.streamId.equals(streamId)) {

      IsometrikUiSdk.getInstance()
          .getIsometrik()
          .getBroadcastOperations()
          .updatePreviewRemoteMediaSettings(false);

      if (isBroadcaster) {

        unregisterListeners();
      } else {

        onStreamEnded(streamId, true);
      }
      runOnUiThread(() -> showAlertDialog(message, dialogType, isBroadcaster));
    } else {

      onStreamEnded(streamId, false);
    }
  }

  /**
   * @param message message to be shown of either stream no longer live,or being kicked out of
   * publisher's group
   * @param dialogType type of dialog,whether stream no longer live or being kicked out of
   * publisher's group
   */
  private void showAlertDialog(String message, int dialogType, boolean isBroadcaster) {

    if (!showingStreamDialog) {

      showingStreamDialog = true;
      AlertDialog.Builder alertDialog =
          StreamDialog.getStreamDialog(ScrollableStreamsActivity.this, message, dialogType);
      alertDialog.setPositiveButton(getString(R.string.ism_ok), (dialog, which) -> {
        if (isBroadcaster) {
          onBackPressed();
        } else {
          if (alert != null) {
            alert.cancel();
            showingStreamDialog = false;

            if (streams.size() == 0) {
              tvNoBroadCaster.setVisibility(View.VISIBLE);
              rlStreams.setVisibility(View.GONE);
              updateShimmerVisibility(false);
            }
          }
        }
      });
      alert = alertDialog.create();
      alert.setCancelable(false);
      alert.setCanceledOnTouchOutside(false);
      if (!isFinishing()) alert.show();
    }
  }

  /**
   * @param messagesModel the messages model MessagesModel{@link MessagesModel
   * } containing details of the message received
   * @see MessagesModel
   *
   * {@link ScrollableStreamsContract.View#onTextMessageReceived(MessagesModel)}
   */
  @Override
  public void onTextMessageReceived(MessagesModel messagesModel) {

    runOnUiThread(() -> {
      messages.add(messagesModel);
      messagesAdapter.notifyItemInserted(messages.size() - 1);
    });

    scrollToLastMessage();
  }

  /**
   * @param messagesModel the messages model MessagesModel{@link MessagesModel
   * } containing details of the message sent
   * @see MessagesModel
   *
   * {@link ScrollableStreamsContract.View#onTextMessageSent(MessagesModel)}
   */
  @Override
  public void onTextMessageSent(MessagesModel messagesModel) {

    runOnUiThread(() -> {
      messages.add(messagesModel);
      messagesAdapter.notifyItemInserted(messages.size() - 1);
    });
    scrollToLastMessage();
  }

  /**
   * @param membersCount the members count in the stream group
   * @param viewersCount the viewers count in the stream group
   *
   * {@link ScrollableStreamsContract.View#updateMembersAndViewersCount(int,
   * int)}
   */
  @Override
  public void updateMembersAndViewersCount(int membersCount, int viewersCount) {
    runOnUiThread(() -> {
      if (membersCount != -1) {
        tvNoOfMembers.setText(String.valueOf(membersCount));
      }
      if (viewersCount != -1) {
        tvNoOfViewers.setText(getString(R.string.ism_viewers_count, String.valueOf(viewersCount)));
      }
    });
  }

  /**
   * @param messagesModel the messages model MessagesModel{@link MessagesModel
   * } containing details of the presence message received,ex. viewer joined/left/removed/timeout
   * or
   * member  start/stop publish/left/removed/timeout
   * @see MessagesModel
   *
   * {@link ScrollableStreamsContract.View#onPresenceMessageEvent(MessagesModel)}
   */
  @Override
  public void onPresenceMessageEvent(MessagesModel messagesModel) {

    runOnUiThread(() -> {
      messages.add(messagesModel);
      messagesAdapter.notifyItemInserted(messages.size() - 1);
    });
    scrollToLastMessage();
  }

  /**
   * @param messageId the id of the message which has been deleted by a user
   * @param message the message to act as place holder for the deleted message
   *
   * {@link ScrollableStreamsContract.View#onMessageRemovedEvent(String,
   * String)}
   */
  @Override
  public void onMessageRemovedEvent(String messageId, String message) {

    runOnUiThread(() -> {
      int size = messages.size();
      for (int i = size - 1; i >= 0; i--) {
        if (messages.get(i).getMessageItemType() == MessageTypeEnum.NormalMessage.getValue()
            && messages.get(i).getMessageId().equals(messageId)) {

          MessagesModel messagesModel = messages.get(i);
          messagesModel.setMessageItemType(MessageTypeEnum.RemovedMessage.getValue());
          messagesModel.setMessage(message);
          messages.set(i, messagesModel);
          messagesAdapter.notifyItemChanged(i);
          break;
        }
      }
    });
  }

  /**
   * @param messageId the id of the message which was successfully sent
   * @param timestamp the timestamp at which message was sent
   * @param temporaryMessageId the temporary message id(local unique identifier for the message)
   *
   * {@link ScrollableStreamsContract.View#onMessageDelivered(String, long,
   * String)}
   */
  @Override
  public void onMessageDelivered(String messageId, long timestamp, String temporaryMessageId) {

    runOnUiThread(() -> {
      int size = messages.size();
      for (int i = size - 1; i >= 0; i--) {
        if (messages.get(i).getMessageItemType() == MessageTypeEnum.NormalMessage.getValue()
            && messages.get(i).getMessageId().equals(temporaryMessageId)) {

          MessagesModel messagesModel = messages.get(i);
          messagesModel.setDelivered(true);
          messagesModel.setMessageId(messageId);
          messagesModel.setTimestamp(timestamp);
          messages.set(i, messagesModel);
          messagesAdapter.notifyItemChanged(i);
          break;
        }
      }
    });
  }

  /**
   * @param errorMessage the error message to be shown in the toast for details of the failed
   * operation
   *
   * {@link ScrollableStreamsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
    hideProgressDialog();
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(ScrollableStreamsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(ScrollableStreamsActivity.this, getString(R.string.ism_error),
            Toast.LENGTH_SHORT).show();
      }
    });
  }

  /**
   * Initializes the view to allow hearts to be rendered on sending or receiving of the likes event.
   *
   * {@link ScrollableStreamsContract.View#initializeHeartsViews()}
   */
  @Override
  public void initializeHeartsViews() {
    HeartsRenderer.Config config = new HeartsRenderer.Config(3f, 0.35f, 2f);

    Bitmap bitmap = BitmapHelper.getBitmapFromVectorDrawable(ScrollableStreamsActivity.this,
        R.drawable.ism_ic_heart);

    model = new HeartsView.Model(0, bitmap);
    heartsView.applyConfig(config);
  }

  /**
   * @see ScrollableStreamsContract.View#connectionStateChanged(boolean)
   */
  @Override
  public void connectionStateChanged(boolean connected) {
    runOnUiThread(() -> {
      tvConnectionState.setVisibility(connected ? View.GONE : View.VISIBLE);

      timerPaused = !connected;
      if (connected) {
        tvLiveStreamStatus.setText(getString(R.string.ism_live_indicator));
      } else {
        tvLiveStreamStatus.setText(getString(R.string.ism_offline_indicator));
      }
    });
  }

  /**
   * Shows flying hearts on receipt of the like event.
   *
   * {@link ScrollableStreamsContract.View#onLikeEvent()}
   */
  @Override
  public void onLikeEvent() {
    try {
      if (model != null) {

        runOnUiThread(() -> heartsView.emitHeart(model));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Shows gifts view as overlay on gifts received.
   *
   * @param giftsModel gifts model containing details of the gift received
   * @see GiftsModel
   *
   * {@link ScrollableStreamsContract.View#onGiftEvent(GiftsModel)}
   */
  @Override
  public void onGiftEvent(GiftsModel giftsModel) {
    runOnUiThread(() -> {
      giftsModels.add(giftsModel);
      if (!showingGifts) {
        showingGifts = true;
        showGift(giftsModel);
      }
    });
  }

  /**
   * Show the gift sent or received as an overlay over the video feed
   *
   * @param giftsModel gifts model containing details of the gift received
   */
  private void showGift(GiftsModel giftsModel) {

    tvGiftSenderName.setText(giftsModel.getSenderName());
    tvGiftName.setText(giftsModel.getGiftName());
    tvGiftCoinValue.setText(
        getString(R.string.ism_coins, String.valueOf(giftsModel.getCoinValue())));

    try {
      Glide.with(ScrollableStreamsActivity.this)
          .load(giftsModel.getSenderImage())
          .asBitmap()
          .placeholder(R.drawable.ism_default_profile_image)
          .into(new BitmapImageViewTarget(ivGiftSenderImage) {
            @Override
            protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable circularBitmapDrawable =
                  RoundedBitmapDrawableFactory.create(getResources(), resource);
              circularBitmapDrawable.setCircular(true);
              ivGiftSenderImage.setImageDrawable(circularBitmapDrawable);
            }
          });
    } catch (NullPointerException | IllegalArgumentException e) {
      e.printStackTrace();
    }

    try {

      Glide.with(this).load(giftsModel.getMessage()).into(ivGiftImage);
    } catch (NullPointerException | IllegalArgumentException e) {
      e.printStackTrace();
    }

    try {
      Glide.with(this)
          .load(giftsModel.getMessage())
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
              if (resource.isAnimated() && resource instanceof GifDrawable) {
                ((GifDrawable) resource).setLoopCount(1);

                new Thread(() -> {
                  while (true) {
                    try {
                      Thread.sleep(3000);
                    } catch (InterruptedException e) {
                      onGifFinished();
                    }

                    if (!(((GifDrawable) resource).isRunning())) {
                      onGifFinished();
                      break;
                    }
                  }
                }).start();
              } else {
                try {
                  new Handler().postDelayed(() -> onGifFinished(), 3000);
                } catch (Exception e) {
                  onGifFinished();
                }
              }
              return false;
            }
          })
          .into(new GlideDrawableImageViewTarget(ivGiftGif, 1));
    } catch (Exception e) {

      onGifFinished();
    }

    rlGifts.setVisibility(View.VISIBLE);
  }

  /**
   * To display the next gift in queue,if any,after completion of current gifts display
   */
  private void onGifFinished() {

    runOnUiThread(() -> {
      try {
        giftsModels.remove(0);
      } catch (IndexOutOfBoundsException ignore) {
        //For the case when stream is switched while a gif gift was shown
      }

      if (giftsModels.size() > 0) {

        showGift(giftsModels.get(0));
      } else {
        showingGifts = false;
        rlGifts.setVisibility(View.GONE);
      }
    });
  }

  /**
   * Join broadcast after successfully updating status as publishing.
   *
   * {@link ScrollableStreamsContract.View#onBroadcastStatusUpdated(boolean, boolean, String)}
   */
  @Override
  public void onBroadcastStatusUpdated(boolean startBroadcast, boolean rejoinRequest,
      String rtcToken) {
    if (startBroadcast) {

      runOnUiThread(() -> {
        if (!rejoinRequest) {
          if (!switchChannelEnabled) {
            IsometrikUiSdk.getInstance()
                .getIsometrik()
                .getBroadcastOperations()
                .joinBroadcast(streamId, IsometrikUiSdk.getInstance().getUserSession().getUserId(),
                    rtcToken, io.isometrik.gs.rtcengine.utils.Constants.CLIENT_ROLE_BROADCASTER,
                    videoGridContainer, true, getString(R.string.ism_you,
                        IsometrikUiSdk.getInstance().getUserSession().getUserName()), this);
          }
          startTimer();
        }
        fetchLatestMessages();
      });
    }
  }

  /**
   * Exit on failure of updating broadcast status
   *
   * {@link ScrollableStreamsContract.View#onBroadcastingStatusUpdateFailed(boolean, String)}
   */
  @Override
  public void onBroadcastingStatusUpdateFailed(boolean startBroadcast, String errorMessage) {

    if (startBroadcast) {

      onError(errorMessage);
      try {
        new Handler().postDelayed(this::onBackPressed, 1000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Exit after user has successfully left stream group
   *
   * {@link ScrollableStreamsContract.View#onStreamGroupLeft()}
   */
  @Override
  public void onStreamGroupLeft() {
    alreadyStoppedPublishing = true;
    onBackPressed();
  }

  /**
   * Exit after initiator has successfully ended the live video
   *
   * {@link ScrollableStreamsContract.View#onBroadcastEnded()}
   */
  @Override
  public void onBroadcastEnded() {
    alreadyStoppedPublishing = true;
    onBackPressed();
  }

  /**
   * Send preset messages in a stream group,which included commonly used emojis and messages.
   *
   * @param message the message to be send in the stream group
   */
  public void sendPresetMessage(String message) {

    scrollableStreamsPresenter.sendMessage(streamId, message,
        MessageTypeEnum.NormalMessage.getValue(), 0, "");
  }

  /**
   * Scrolls message list to last message on receipt of new text or presence message to show latest
   * message received.
   */
  @SuppressWarnings("TryWithIdenticalCatches")
  private void scrollToLastMessage() {
    runOnUiThread(() -> {
      try {

        new Handler().postDelayed(() -> messagesLayoutManager.scrollToPositionWithOffset(
            messagesAdapter.getItemCount() - 1, 0), 500);
      } catch (IndexOutOfBoundsException e) {
        e.printStackTrace();
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
    });
  }

  @Override
  public void onBackPressed() {

    unregisterListeners();
    try {
      super.onBackPressed();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onDestroy() {
    unregisterListeners();
    super.onDestroy();
  }

  /**
   * Cleanup all realtime isometrik event listeners that were added at time of exit
   */
  private void unregisterListeners() {
    if (!unregisteredListeners) {
      dismissAllDialogs();
      unregisteredListeners = true;
      stopTimer();
      scrollableStreamsPresenter.unregisterStreamViewersEventListener();
      scrollableStreamsPresenter.unregisterStreamMembersEventListener();
      scrollableStreamsPresenter.unregisterStreamsEventListener();
      scrollableStreamsPresenter.unregisterStreamMessagesEventListener();
      scrollableStreamsPresenter.unregisterConnectionEventListener();
      scrollableStreamsPresenter.unregisterPresenceEventListener();
      scrollableStreamsPresenter.unregisterCopublishRequestsEventListener();

      if (isBroadcaster) {
        if (!alreadyStoppedPublishing) {
          scrollableStreamsPresenter.updateBroadcastingStatus(false, streamId, false);
        }
        try {
          runOnUiThread(() -> IsometrikUiSdk.getInstance()
              .getIsometrik()
              .getBroadcastOperations()
              .leaveBroadcast(IsometrikUiSdk.getInstance().getUserSession().getUserId(),
                  videoGridContainer, true));
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        scrollableStreamsPresenter.leaveAsViewer(streamId);
        try {
          runOnUiThread(() -> IsometrikUiSdk.getInstance()
              .getIsometrik()
              .getBroadcastOperations()
              .leaveBroadcast(IsometrikUiSdk.getInstance().getUserSession().getUserId(),
                  videoGridContainer, false));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      IsometrikUiSdk.getInstance().getIsometrik().removeBroadcastRtcEventHandler(this);
    }
  }

  /**
   * Requests addition of remote user feed to UI container, also closes the keyboard if open,before
   * adding the remote video feed to avoid UI messup,due to activity resize at time of keyboard
   * open/close
   *
   * @param uid the uid of the remote user who has started streaming
   * @param width the width of remote video feed
   * @param height the height of remote video feed
   * @param elapsed the elapsed
   *
   * {@link IsometrikRtcEventHandler#onFirstRemoteVideoDecoded(int,
   * int, int, int)}
   */
  @Override
  public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

    if ((rlRootLayout.getRootView().getHeight() - rlRootLayout.getHeight()) > KeyboardUtil.dpToPx(
        this, 200)) {
      //Keyboard is shown
      KeyboardUtil.hideKeyboard(this);

      runOnUiThread(() -> {
        try {
          new Handler().postDelayed(() -> IsometrikUiSdk.getInstance()
              .getIsometrik()
              .getBroadcastOperations()
              .renderRemoteUser(uid, videoGridContainer, scrollableStreamsPresenter.getMembers(),
                  this), 1000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    } else {
      runOnUiThread(() -> IsometrikUiSdk.getInstance()
          .getIsometrik()
          .getBroadcastOperations()
          .renderRemoteUser(uid, videoGridContainer, scrollableStreamsPresenter.getMembers(),
              this));
    }
  }

  /**
   * Removes video from UI container of remote user,when he is no longer publishing
   *
   * @param uid the uid of the remote user,whose remote video feed is no longer received
   * @param reason the reason of remote user's feed not received
   *
   * {@link IsometrikRtcEventHandler#onUserOffline(int, int)}
   */
  @Override
  public void onUserOffline(int uid, int reason) {

    runOnUiThread(() -> IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .removeRemoteUser(uid, videoGridContainer));
  }

  /**
   * Trigger on change of connection state to the streaming RTC engine
   *
   * @param state the state of connection to streaming RTC engine
   * @param reason the reason of change in state
   * @param connected whether connected ot streaming server or not
   *
   * {@link IsometrikRtcEventHandler#onConnectionStateChanged(int,
   * int, Boolean)}
   */
  @Override
  public void onConnectionStateChanged(int state, int reason, Boolean connected) {
    if (connected != null) {

      if (connected) {
        //Condition added to allow connection lost message being shown on channel switch
        connectionStateChanged(true);
      }
      if (isBroadcaster) {
        if (connected) {
          if (reconnectRequest) {
            IsometrikUiSdk.getInstance()
                .getIsometrik()
                .getBroadcastOperations()
                .togglePreview(true);
          } else {

            reconnectRequest = true;
          }
        }
      }
    }
  }

  /**
   * Triggered on loss of connection to the streaming RTC engine
   *
   * {@link IsometrikRtcEventHandler#onConnectionLost()}
   */
  @Override
  public void onConnectionLost() {
    connectionStateChanged(false);
    if (isBroadcaster) {
      IsometrikUiSdk.getInstance().getIsometrik().getBroadcastOperations().togglePreview(false);
    }
  }

  /**
   * Start timer and updates the textview to update live duration for stream group's.
   */
  public void startTimer() {
    duration = TimeUtil.getDuration(streamStartTime);
    mTimer = new Timer();
    mTimer.scheduleAtFixedRate(new TimerTask() {

      public void run() {
        duration += 1; //increase every sec
        timerHandler.obtainMessage(TimerHandler.INCREASE_TIMER).sendToTarget();
      }
    }, 0, 1000);
  }

  /**
   * Stop timer.
   */
  public void stopTimer() {

    if (mTimer != null) {
      mTimer.cancel();
    }
  }

  /**
   * The class to update stream live duration on UI thread.
   */
  @SuppressWarnings("all")
  public class TimerHandler extends Handler {

    /**
     * The Increase timer.
     */
    static final int INCREASE_TIMER = 1;

    public void handleMessage(Message msg) {

      if (msg.what == INCREASE_TIMER) {
        if (!timerPaused) {
          tvDuration.setText(TimeUtil.getDurationString(duration));
        }
      }
    }
  }

  /**
   * Toggles visibility of the action buttons based on the role of the user,whether publisher or
   * audience.
   *
   * @param firstTime whether toggle of action buttons has been requested for firsttime
   */
  private void toggleRoleBasedActionButtonsVisibility(boolean firstTime) {

    if (isBroadcaster) {
      //Broadcaster
      ivLike.setVisibility(View.INVISIBLE);
      ivGifts.setVisibility(View.GONE);
      ivEffects.setVisibility(View.VISIBLE);
      ivBeautify.setVisibility(View.VISIBLE);
      ivSwitchCamera.setVisibility(View.VISIBLE);

      if (isAdmin) {
        ivAddMember.setVisibility(View.VISIBLE);
      } else {
        ivAddMember.setVisibility(View.GONE);
      }
      ivJoin.setVisibility(View.GONE);
    } else {
      //Audience
      ivLike.setVisibility(View.VISIBLE);
      ivGifts.setVisibility(View.VISIBLE);
      ivEffects.setVisibility(View.GONE);
      ivBeautify.setVisibility(View.GONE);
      ivSwitchCamera.setVisibility(View.GONE);
      ivAddMember.setVisibility(View.GONE);

      //If given user can publish already
      if (scrollableStreamsPresenter.getMemberIds()
          .contains(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
        ivJoin.setVisibility(View.VISIBLE);
      } else {
        ivJoin.setVisibility(View.GONE);
      }
    }

    if (isPublic) {
      if (firstTime) {

        if (isBroadcaster) {

          ivRequest.setVisibility(View.VISIBLE);
        } else {
          if (scrollableStreamsPresenter.getMemberIds()
              .contains(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
            ivRequest.setVisibility(View.VISIBLE);
          } else {
            //Check if given user has already requested copublish
            scrollableStreamsPresenter.checkCopublishRequestStatus(streamId);
          }
        }
      } else {

        ivRequest.setVisibility(View.VISIBLE);
      }
    } else {
      ivRequest.setVisibility(View.GONE);
    }
  }

  /**
   * Opens the settings popup based on the role, whether publisher or audience{@link
   * SettingsFragment}.
   *
   * @see SettingsFragment
   */
  @OnClick(R2.id.ivSettings)
  public void showSettings() {

    if (!isFinishing() && !settingsFragment.isAdded()) {
      dismissAllDialogs();
      settingsFragment.updateParameters(isAdmin, isBroadcaster, streamId);
      settingsFragment.show(getSupportFragmentManager(), SettingsFragment.TAG);
    }
  }

  /**
   * {@link ActionCallback#onRemoteUserMediaSettingsUpdated(String, String, int, String,
   * boolean, boolean)}
   */
  @Override
  public void onRemoteUserMediaSettingsUpdated(String streamId, String userId, int uid,
      String userName, boolean audio, boolean muted) {

    scrollableStreamsPresenter.onRemoteUserMediaSettingsUpdated(streamId, userId, uid, userName,
        audio, muted);
  }

  /**
   * {@link ActionCallback#removeMemberRequested(String, String)}
   */
  @Override
  public void removeMemberRequested(String streamId, String userId) {
    if (remoteUserSettings.getDialog() != null
        && remoteUserSettings.getDialog().isShowing()
        && !remoteUserSettings.isRemoving()) {
      remoteUserSettings.dismiss();
    }
    showProgressDialog(getString(R.string.ism_removing_member));
    scrollableStreamsPresenter.requestRemoveMember(streamId, userId);
  }

  /**
   * {@link ScrollableStreamsContract.View#onMemberRemovedResult(String)}
   */
  @Override
  public void onMemberRemovedResult(String memberId) {

    hideProgressDialog();
  }

  /**
   * Callback from settings fragment based on role to toggle visibility of chat messages or control
   * buttons{@link SettingsFragment}
   *
   * @param action whether visibility for chat messages or control buttons to be toggled
   * @param enabled whether visibility has been enabled or disabled
   * @see SettingsFragment
   *
   * {@link ActionCallback#onSettingsChange(int,
   * boolean)}
   */
  @Override
  public void onSettingsChange(int action, boolean enabled) {

    if (action == ActionEnum.ChatMessagesVisibilityToggleAction.getValue()) {
      //Toggle messages visibility.

      if (enabled) {
        rvMessages.setVisibility(View.VISIBLE);
        rvPredefinedMessages.setVisibility(View.VISIBLE);
      } else {
        rvMessages.setVisibility(View.INVISIBLE);
        rvPredefinedMessages.setVisibility(View.INVISIBLE);
      }
    } else if (action == ActionEnum.ControlButtonsVisibilityToggleAction.getValue()) {
      //Toggle control buttons visibility.

      if (enabled) {
        toggleRoleBasedActionButtonsVisibility(false);
      } else {
        ivLike.setVisibility(View.INVISIBLE);
        ivGifts.setVisibility(View.GONE);
        ivEffects.setVisibility(View.GONE);
        ivBeautify.setVisibility(View.GONE);
        ivSwitchCamera.setVisibility(View.GONE);
        ivAddMember.setVisibility(View.GONE);
        ivJoin.setVisibility(View.GONE);
        ivRequest.setVisibility(View.GONE);
      }
    }
  }

  /**
   * Leave the stream group as member.
   */
  @Override
  public void leaveBroadcastRequested() {
    scrollableStreamsPresenter.leaveStreamGroup(streamId);
  }

  /**
   * Stop publishing requested by a member.
   */
  @Override
  public void stopPublishingRequested() {
    onBackPressed();
  }

  /**
   * End broadcast requested.
   */
  @Override
  public void endBroadcastRequested() {
    scrollableStreamsPresenter.stopBroadcast(streamId);
  }

  /**
   * Stop publishing in the stream group{@link EndLeaveFragment} or leave a broadcast or end a
   * broadcast popup.
   *
   * @see EndLeaveFragment
   */
  @OnClick(R2.id.ivEndStream)
  public void stopPublishing() {

    if (isBroadcaster) {

      if (!isFinishing() && !endLeaveFragment.isAdded()) {
        dismissAllDialogs();
        endLeaveFragment.updateParameters(isAdmin);
        endLeaveFragment.show(getSupportFragmentManager(), EndLeaveFragment.TAG);
      }
    } else {
      onBackPressed();
    }
  }

  /**
   * Send a like in the stream group.
   */
  @OnClick(R2.id.ivLike)
  public void sendLikeMessage() {

    scrollableStreamsPresenter.sendMessage(streamId, Constants.LIKE_URL,
        MessageTypeEnum.HeartMessage.getValue(), 0, "");
  }

  /**
   * Show the EffectsFragment{@link EffectsFragment} to allow application of ar filters or audio
   * effects.
   *
   * @see EffectsFragment
   */
  @OnClick(R2.id.ivEffects)
  public void showEffects() {

    if (!isFinishing() && !effectsFragment.isAdded()) {
      dismissAllDialogs();
      effectsFragment.updateParameters(true);
      effectsFragment.show(getSupportFragmentManager(), EffectsFragment.TAG);
    }
  }

  /**
   * Show the GiftsFragment{@link GiftsFragment} to send gifts.
   *
   * @see GiftsFragment
   */
  @OnClick(R2.id.ivGifts)
  public void showGifts() {

    if (!isFinishing() && !giftsFragment.isAdded()) {
      dismissAllDialogs();
      giftsFragment.updateParameters(this);
      giftsFragment.show(getSupportFragmentManager(), GiftsFragment.TAG);
    }
  }

  /**
   * {@link ScrollableStreamsContract.View#onStreamJoined(int, boolean, String)}
   */
  @Override
  public void onStreamJoined(int numOfViewers, boolean rejoinRequest, String rtcToken) {
    runOnUiThread(() -> {

      if (!rejoinRequest) {

        if (!switchChannelEnabled) {
          IsometrikUiSdk.getInstance()
              .getIsometrik()
              .getBroadcastOperations()
              .joinBroadcast(streamId, IsometrikUiSdk.getInstance().getUserSession().getUserId(),
                  rtcToken, io.isometrik.gs.rtcengine.utils.Constants.CLIENT_ROLE_AUDIENCE,
                  videoGridContainer, false, getString(R.string.ism_you,
                      IsometrikUiSdk.getInstance().getUserSession().getUserName()), this);
        }
        startTimer();
      }
      fetchLatestMessages();
      if (numOfViewers != -1) {
        tvNoOfViewers.setText(getString(R.string.ism_viewers_count, String.valueOf(numOfViewers)));
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.View#onStreamJoinError(String)}
   */
  @Override
  public void onStreamJoinError(String errorMessage) {

    onError(errorMessage);
    try {

      new Handler().postDelayed(this::onBackPressed, 1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Open MembersFragment{@link MembersFragment} to show list of
   * members of the stream group.
   *
   * @see MembersFragment
   */
  @OnClick(R2.id.tvNoOfMembers)
  public void showMembers() {

    if (!isFinishing() && !membersFragment.isAdded()) {
      dismissAllDialogs();
      membersFragment.updateParameters(streamId, getIntent().getBooleanExtra("isAdmin", false),
          tvNoOfMembers.getText().toString());
      membersFragment.show(getSupportFragmentManager(), MembersFragment.TAG);
    }
  }

  /**
   * Open ViewersFragment{@link ViewersFragment} to show list
   * of viewers of the stream group.
   *
   * @see ViewersFragment
   */
  @OnClick({ R2.id.rlViewers, R2.id.llViewers })
  public void showViewers() {

    if (!isFinishing() && !viewersFragment.isAdded()) {
      dismissAllDialogs();
      viewersFragment.updateParameters(streamId, String.valueOf(viewersCount),
          scrollableStreamsPresenter.getMemberIds());
      viewersFragment.show(getSupportFragmentManager(), ViewersFragment.TAG);
    }
  }

  /**
   * Open RequestsFragment{@link RequestsFragment} to show list
   * of copublish request of the stream group, if user is part of the publisher group else open
   * fragment to make a copublish request{@link RequestCopublishFragment} or show status of
   * copublish request{@link CopublishRequestStatusFragment}.
   *
   * @see RequestsFragment
   * @see RequestCopublishFragment
   * @see CopublishRequestStatusFragment
   */
  @OnClick(R2.id.ivRequest)
  public void showRequests() {

    if (scrollableStreamsPresenter.getMemberIds()
        .contains(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

      //A member can see all the copublish requests

      if (!isFinishing() && !requestsFragment.isAdded()) {
        dismissAllDialogs();
        requestsFragment.updateParameters(streamId, getIntent().getBooleanExtra("isAdmin", false),
            this);
        requestsFragment.show(getSupportFragmentManager(), RequestsFragment.TAG);
      }
    } else {

      if (alreadyRequestedCopublish) {

        if (!isFinishing() && !copublishRequestStatusFragment.isAdded()) {
          dismissAllDialogs();
          copublishRequestStatusFragment.updateParameters(initiatorImageUrl,
              IsometrikUiSdk.getInstance().getUserSession().getUserProfilePic(), initiatorName,
              pending, accepted, this);
          copublishRequestStatusFragment.show(getSupportFragmentManager(),
              CopublishRequestStatusFragment.TAG);
        }
      } else {
        //A viewer can make a copublish request
        if (!isFinishing() && !requestCopublishFragment.isAdded()) {
          dismissAllDialogs();
          requestCopublishFragment.updateParameters(initiatorImageUrl,
              IsometrikUiSdk.getInstance().getUserSession().getUserProfilePic(), this);

          requestCopublishFragment.show(getSupportFragmentManager(), RequestCopublishFragment.TAG);
        }
      }
    }
  }

  /**
   * Open UserInfoFragment{@link UserInfoFragment} to show information of the user who created
   * stream group stream group.
   *
   * @see UserInfoFragment
   */
  @OnClick(R2.id.rlInitiator)
  public void showInitiatorInfo() {
    if (!isFinishing() && !userInfoFragment.isAdded()) {
      dismissAllDialogs();
      userInfoFragment.updateParameters(initiatorName, initiatorIdentifier, initiatorImageUrl);

      userInfoFragment.show(getSupportFragmentManager(), UserInfoFragment.TAG);
    }
  }

  /**
   * {@link ScrollableStreamsContract.View#onCopublishRequestStatusFetched(boolean, boolean,
   * boolean)}
   */
  @Override
  public void onCopublishRequestStatusFetched(boolean alreadyRequestedCopublish, boolean pending,
      boolean accepted) {
    this.alreadyRequestedCopublish = alreadyRequestedCopublish;
    this.pending = pending;
    this.accepted = accepted;
    runOnUiThread(() -> ivRequest.setVisibility(View.VISIBLE));
  }

  /**
   * Switch profile from a viewer to a broadcaster
   */
  @OnClick(R2.id.ivJoin)
  public void switchProfile() {
    checkStreamingPermissions();
  }

  /**
   * Hide live stream disclaimer/warning text
   */
  @OnClick(R2.id.tvGotIt)
  public void hideDisclaimer() {
    rlDisclaimer.setVisibility(View.GONE);
  }

  /**
   * Open AddMembersFragment{@link AddMembersFragment} to add a user/viewer as member to a
   * broadcast.
   *
   * @see AddMembersFragment
   */
  @OnClick(R2.id.ivAddMember)
  public void addMember() {
    if (isAdmin) {
      if (!isFinishing() && !addMembersFragment.isAdded()) {
        dismissAllDialogs();
        addMembersFragment.updateParameters(streamId, scrollableStreamsPresenter.getMemberIds());

        addMembersFragment.show(getSupportFragmentManager(), AddMembersFragment.TAG);
      }
    }
  }

  /**
   * {@link CopublishActionCallbacks#startPublishingOnCopublishRequestAccepted()}
   */
  @Override
  public void startPublishingOnCopublishRequestAccepted() {

    if ((acceptedCopublishRequestFragment.getDialog() != null
        && acceptedCopublishRequestFragment.getDialog().isShowing())
        && !acceptedCopublishRequestFragment.isRemoving()) {

      acceptedCopublishRequestFragment.dismiss();
    }
    if ((copublishRequestStatusFragment.getDialog() != null
        && copublishRequestStatusFragment.getDialog().isShowing())
        && !copublishRequestStatusFragment.isRemoving()) {
      copublishRequestStatusFragment.dismiss();
    }
    switchProfile();
  }

  /**
   * {@link CopublishActionCallbacks#exitOnNoLongerBeingAMember()}
   */
  @Override
  public void exitOnNoLongerBeingAMember() {
    onBackPressed();
  }

  /**
   * {@link CopublishActionCallbacks#exitOnCopublishRequestRejected()}
   */
  @Override
  public void exitOnCopublishRequestRejected() {
    onBackPressed();
  }

  /**
   * {@link CopublishActionCallbacks#continueWatching()}
   */
  @Override
  public void continueWatching() {
    if ((rejectedCopublishRequestFragment.getDialog() != null
        && rejectedCopublishRequestFragment.getDialog().isShowing())
        && !rejectedCopublishRequestFragment.isRemoving()) {
      rejectedCopublishRequestFragment.dismiss();
    }
    if ((copublishRequestStatusFragment.getDialog() != null
        && copublishRequestStatusFragment.getDialog().isShowing())
        && !copublishRequestStatusFragment.isRemoving()) {
      copublishRequestStatusFragment.dismiss();
    }
  }

  /**
   * {@link CopublishActionCallbacks#requestCopublish()}
   */
  @Override
  public void requestCopublish() {

    if ((requestCopublishFragment.getDialog() != null && requestCopublishFragment.getDialog()
        .isShowing()) && !requestCopublishFragment.isRemoving()) {
      requestCopublishFragment.dismiss();
    }
    showProgressDialog(getString(R.string.ism_adding_request));
    scrollableStreamsPresenter.addCopublishRequest(streamId);
  }

  /**
   * {@link RequestListActionCallback#copublishRequestAction(boolean, String)}
   */
  @Override
  public void copublishRequestAction(boolean accepted, String userId) {
    updateCopublishChatMessage(userId);
  }

  /**
   * {@link CopublishActionCallbacks#deleteCopublishRequest()}
   */
  @Override
  public void deleteCopublishRequest() {
    if ((copublishRequestStatusFragment.getDialog() != null
        && copublishRequestStatusFragment.getDialog().isShowing())
        && !copublishRequestStatusFragment.isRemoving()) {
      copublishRequestStatusFragment.dismiss();
    }
    showProgressDialog(getString(R.string.ism_removing_request));
    scrollableStreamsPresenter.removeCopublishRequest(streamId);
  }

  /**
   * {@link UserFeedClickedCallback#userFeedClicked(int)}
   */
  @Override
  public void userFeedClicked(int uid) {

    HashMap<String, Object> members = scrollableStreamsPresenter.getMemberDetails(streamId, uid);
    if (members != null) {

      if (members.isEmpty()) {
        Toast.makeText(this, getString(R.string.ism_settings_unavailable), Toast.LENGTH_LONG)
            .show();
      } else {

        if (!isFinishing() && !remoteUserSettings.isAdded()) {
          dismissAllDialogs();

          remoteUserSettings.updateParameters(streamId, isAdmin, (String) members.get("userId"),
              uid, (String) members.get("userName"), (boolean) members.get("audioMuted"),
              (boolean) members.get("videoMuted"));

          remoteUserSettings.show(getSupportFragmentManager(), RemoteUserSettings.TAG);
        }
      }
    }
  }

  /**
   * {@link ScrollableStreamsContract.View#onCopublishRequestAdded()}
   */
  @Override
  public void onCopublishRequestAdded() {
    hideProgressDialog();
    alreadyRequestedCopublish = true;
    pending = true;
  }

  /**
   * {@link ScrollableStreamsContract.View#onCopublishRequestRemoved()}
   */
  @Override
  public void onCopublishRequestRemoved() {
    hideProgressDialog();
    alreadyRequestedCopublish = false;
    pending = false;
  }

  /**
   * {@link ScrollableStreamsContract.View#onProfileSwitched(String)}
   */
  @Override
  public void onProfileSwitched(String rtcToken) {
    hideProgressDialog();
    isBroadcaster = true;
    runOnUiThread(() -> {
      ivJoin.setVisibility(View.GONE);

      //Disable scrolls for publisher
      rvStreams.suppressLayout(true);

      IsometrikUiSdk.getInstance()
          .getIsometrik()
          .getBroadcastOperations()
          .switchRole(IsometrikUiSdk.getInstance().getUserSession().getUserId(), videoGridContainer,
              getString(R.string.ism_you,
                  IsometrikUiSdk.getInstance().getUserSession().getUserName()), rtcToken, this);

      removeViewerEvent(IsometrikUiSdk.getInstance().getUserSession().getUserId(),
          viewersListModels.size() - 1);

      toggleRoleBasedActionButtonsVisibility(false);
      scrollableStreamsPresenter.updateBroadcasterStatus(true);
      updateJoinChatMessage();
    });
  }

  /**
   * Accept copublish request.
   *
   * @param userId the user id
   */
  public void acceptCopublishRequest(String userId) {
    showProgressDialog(getString(R.string.ism_accepting_request));
    scrollableStreamsPresenter.acceptCopublishRequest(userId, streamId);
  }

  /**
   * Decline copublish request.
   *
   * @param userId the user id
   */
  public void declineCopublishRequest(String userId) {
    showProgressDialog(getString(R.string.ism_declining_request));
    scrollableStreamsPresenter.declineCopublishRequest(userId, streamId);
  }

  /**
   * {@link ScrollableStreamsContract.View#onCopublishRequestAcceptedApiResult(String)}
   */
  @Override
  public void onCopublishRequestAcceptedApiResult(String userId) {
    hideProgressDialog();
    updateCopublishChatMessage(userId);
  }

  /**
   * {@link ScrollableStreamsContract.View#onCopublishRequestDeclinedApiResult(String)}
   */
  @Override
  public void onCopublishRequestDeclinedApiResult(String userId) {
    hideProgressDialog();
    updateCopublishChatMessage(userId);
  }

  /**
   * {@link ScrollableStreamsContract.View#onCopublishRequestAcceptedEvent()}
   */
  @Override
  public void onCopublishRequestAcceptedEvent() {
    //No need to dismiss other dialog manually,as they will automatically be dismissed if new dialog is show
    if (!isFinishing() && !acceptedCopublishRequestFragment.isAdded()) {
      dismissAllDialogs();
      acceptedCopublishRequestFragment.updateParameters(initiatorImageUrl, initiatorName, this);

      acceptedCopublishRequestFragment.show(getSupportFragmentManager(),
          AcceptedCopublishRequestFragment.TAG);
    }
    runOnUiThread(() -> ivJoin.setVisibility(View.VISIBLE));

    pending = false;
    accepted = true;
  }

  /**
   * {@link ScrollableStreamsContract.View#onCopublishRequestDeclinedEvent()}
   */
  @Override
  public void onCopublishRequestDeclinedEvent() {
    //No need to dismiss other dialog manually,as they will automatically be dismissed if new dialog is show
    if (!isFinishing() && !rejectedCopublishRequestFragment.isAdded()) {
      dismissAllDialogs();
      rejectedCopublishRequestFragment.updateParameters(initiatorImageUrl, initiatorName, this);

      rejectedCopublishRequestFragment.show(getSupportFragmentManager(),
          RejectedCopublishRequestFragment.TAG);
    }
    pending = false;
    accepted = false;
  }

  /**
   * {@link ScrollableStreamsContract.View#onMemberAdded(boolean, String)}
   */
  @Override
  public void onMemberAdded(boolean added, String initiatorName) {

    runOnUiThread(() -> {
      if (added) {
        ivJoin.setVisibility(View.VISIBLE);
      } else {
        ivJoin.setVisibility(View.GONE);
        if (isBroadcaster) {
          onStreamOffline(getString(R.string.ism_member_kicked_out, initiatorName),
              StreamDialogEnum.KickedOut.getValue(), streamId, true);
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.View#updateRemoteUserNames(Map)}
   */
  @Override
  public void updateRemoteUserNames(Map<String, String> members) {
    runOnUiThread(() -> IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .updateRemoteUserNames(members, IsometrikUiSdk.getInstance().getUserSession().getUserId(),
            videoGridContainer));
  }

  /**
   * @see IsometrikRtcEventHandler#onJoinChannelSuccess(String, int, int)
   */
  @Override
  public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
    switchChannelEnabled = true;
    if (isAdmin) {
      channelJoined = true;
      if (countDownCompleted) {
        if (!getIntent().getBooleanExtra("publishRequired", true)) {
          if (!isFinishing()) {

            dismissAllDialogs();

            showGoLiveView(goLiveDialog);
          }
        }
      }
    } else {

      //To switch channel
      if (!streamId.equals(channel)) {
        IsometrikUiSdk.getInstance()
            .getIsometrik()
            .getBroadcastOperations()
            .switchPreviewBroadcast(streamId, null);
      }

      updateStreamPreviewImageVisibility(streamsLayoutManager.findFirstVisibleItemPosition(), false,
          streamId);
    }
    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .updatePreviewRemoteMediaSettings(true);
  }

  /**
   * @see IsometrikRtcEventHandler#onFirstLocalVideoFrame(int, int, int)
   */
  @Override
  public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
    //Not used with AR filters
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  /**
   * Dismisses all of the bottom sheet dialogs before showing a new dialog.
   */
  private void dismissAllDialogs() {

    if (!isFinishing()) {
      try {
        if (membersFragment.getDialog() != null
            && membersFragment.getDialog().isShowing()
            && !membersFragment.isRemoving()) {
          membersFragment.dismiss();
        } else if (requestsFragment.getDialog() != null
            && requestsFragment.getDialog().isShowing()
            && !requestsFragment.isRemoving()) {
          requestsFragment.dismiss();
        } else if (viewersFragment.getDialog() != null
            && viewersFragment.getDialog().isShowing()
            && !viewersFragment.isRemoving()) {
          viewersFragment.dismiss();
        } else if (settingsFragment.getDialog() != null
            && settingsFragment.getDialog().isShowing()
            && !settingsFragment.isRemoving()) {
          settingsFragment.dismiss();
        } else if (userInfoFragment.getDialog() != null
            && userInfoFragment.getDialog().isShowing()
            && !userInfoFragment.isRemoving()) {
          userInfoFragment.dismiss();
        } else if (effectsFragment.getDialog() != null
            && effectsFragment.getDialog().isShowing()
            && !effectsFragment.isRemoving()) {
          effectsFragment.dismiss();
        } else if (requestCopublishFragment.getDialog() != null
            && requestCopublishFragment.getDialog().isShowing()
            && !requestCopublishFragment.isRemoving()) {
          requestCopublishFragment.dismiss();
        } else if (copublishRequestStatusFragment.getDialog() != null
            && copublishRequestStatusFragment.getDialog().isShowing()
            && !copublishRequestStatusFragment.isRemoving()) {
          copublishRequestStatusFragment.dismiss();
        } else if (acceptedCopublishRequestFragment.getDialog() != null
            && acceptedCopublishRequestFragment.getDialog().isShowing()
            && !acceptedCopublishRequestFragment.isRemoving()) {
          acceptedCopublishRequestFragment.dismiss();
        } else if (rejectedCopublishRequestFragment.getDialog() != null
            && rejectedCopublishRequestFragment.getDialog().isShowing()
            && !rejectedCopublishRequestFragment.isRemoving()) {
          rejectedCopublishRequestFragment.dismiss();
        } else if (endLeaveFragment.getDialog() != null
            && endLeaveFragment.getDialog().isShowing()
            && !endLeaveFragment.isRemoving()) {
          endLeaveFragment.dismiss();
        } else if (giftsFragment.getDialog() != null
            && giftsFragment.getDialog().isShowing()
            && !giftsFragment.isRemoving()) {
          giftsFragment.dismiss();
        } else if (remoteUserSettings.getDialog() != null && remoteUserSettings.getDialog()
            .isShowing() && !remoteUserSettings.isRemoving()) {
          remoteUserSettings.dismiss();
        }
      } catch (Exception ignore) {
      }
    }
  }

  /**
   * Check permissions to switch start streaming.
   */
  public void checkStreamingPermissions() {
    //WRITE_EXTERNAL_STORAGE for agora logs
    if ((ContextCompat.checkSelfPermission(ScrollableStreamsActivity.this,
        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        || (ContextCompat.checkSelfPermission(ScrollableStreamsActivity.this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        || (ContextCompat.checkSelfPermission(ScrollableStreamsActivity.this,
        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {

      if ((ActivityCompat.shouldShowRequestPermissionRationale(ScrollableStreamsActivity.this,
          Manifest.permission.CAMERA))
          || (ActivityCompat.shouldShowRequestPermissionRationale(ScrollableStreamsActivity.this,
          Manifest.permission.WRITE_EXTERNAL_STORAGE))
          || (ActivityCompat.shouldShowRequestPermissionRationale(ScrollableStreamsActivity.this,
          Manifest.permission.RECORD_AUDIO))) {
        Snackbar snackbar = Snackbar.make(rlRootLayout, R.string.ism_permission_start_streaming,
            Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.ism_ok), view -> this.requestPermissions());

        snackbar.show();

        ((TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text))
            .setGravity(Gravity.CENTER_HORIZONTAL);
      } else {

        requestPermissions();
      }
    } else {

      requestSwitchProfile();
    }
  }

  /**
   * Request permissions to start broadcasting after switching profile from
   * a viewer to a publisher.
   */
  private void requestPermissions() {

    ActivityCompat.requestPermissions(ScrollableStreamsActivity.this, new String[] {
        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
    }, 0);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    boolean permissionDenied = false;
    if (requestCode == 0) {

      for (int grantResult : grantResults) {
        if (grantResult != PackageManager.PERMISSION_GRANTED) {
          permissionDenied = true;
          break;
        }
      }
      if (permissionDenied) {
        Toast.makeText(this, getString(R.string.ism_permission_start_streaming_denied),
            Toast.LENGTH_LONG).show();
      } else {
        requestSwitchProfile();
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  /**
   * Switch profile from a viewer to a publisher.
   */
  private void requestSwitchProfile() {

    showProgressDialog(getString(R.string.ism_switching_profile));
    scrollableStreamsPresenter.switchProfile(streamId, isPublic);
  }

  /**
   * Send a gift in a broadcast.
   *
   * @see GiftsActionCallback#sendGift(String, int, int, String)
   */
  @Override
  public void sendGift(String message, int messageType, int coinsValue, String giftName) {

    if ((giftsFragment.getDialog() != null && giftsFragment.getDialog().isShowing())
        && !giftsFragment.isRemoving()) {
      giftsFragment.dismiss();
    }
    scrollableStreamsPresenter.sendMessage(streamId, message, messageType, coinsValue, giftName);
  }

  /**
   * {@link ScrollableStreamsContract.View#onStreamViewersDataReceived(ArrayList)}
   */
  @Override
  public void onStreamViewersDataReceived(ArrayList<ViewersListModel> viewersListModels) {
    runOnUiThread(() -> {
      this.viewersListModels.clear();
      this.viewersListModels.addAll(viewersListModels);
      viewersCount = viewersListModels.size();
      viewersUtil.updateViewers(viewersListModels);
    });
  }

  /**
   * {@link ScrollableStreamsContract.View#removeViewerEvent(String, int)}
   */
  @Override
  public void removeViewerEvent(String viewerId, int viewersCount) {
    runOnUiThread(() -> {
      int size = viewersListModels.size();
      for (int i = 0; i < size; i++) {

        if (viewersListModels.get(i).getViewerId().equals(viewerId)) {

          viewersListModels.remove(i);

          break;
        }
      }
      viewersUtil.updateViewers(viewersListModels);
      this.viewersCount = viewersCount;
      if (viewersCount != viewersListModels.size()) {
        //To re-sync viewers
        scrollableStreamsPresenter.requestStreamViewersData(streamId);
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.View#addViewerEvent(ViewersListModel, int)}
   */
  @Override
  public void addViewerEvent(ViewersListModel viewersListModel, int viewersCount) {
    runOnUiThread(() -> {
      viewersListModels.add(0, viewersListModel);
      viewersUtil.updateViewers(viewersListModels);
      this.viewersCount = viewersCount;
      if (viewersCount != viewersListModels.size()) {
        //To re-sync viewers
        scrollableStreamsPresenter.requestStreamViewersData(streamId);
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.View#onLiveStreamsDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onLiveStreamsDataReceived(ArrayList<StreamsModel> streams, boolean latestStreams) {
    if (!isBroadcaster) {
      if (latestStreams) {
        this.streams.clear();
      }
      this.streams.addAll(streams);

      runOnUiThread(() -> {
        updateShimmerVisibility(false);
        if (ScrollableStreamsActivity.this.streams.size() > 0) {
          tvNoBroadCaster.setVisibility(View.GONE);
          rlStreams.setVisibility(View.VISIBLE);
          streamsAdapter.notifyDataSetChanged();

          StreamsModel streamsModel = streams.get(0);

          updateStreamInfo(streamsModel);

          if (streamsModel.getStreamId().equals(streamId)) {
            try {
              //As view holder at that position was not inflated(due to notify dataset changed) returning null for findViewHolderForAdapterPosition method
              new Handler().postDelayed(
                  () -> updateStreamPreviewImageVisibility(0, false, streamId), 100);
            } catch (Exception ignore) {
            }
          }
        } else {
          tvNoBroadCaster.setVisibility(View.VISIBLE);
          rlStreams.setVisibility(View.GONE);
        }
      });
      hideProgressDialog();
      if (refresh.isRefreshing()) refresh.setRefreshing(false);
    }
  }

  /**
   * {@link ScrollableStreamsContract.View#onStreamEnded(String, boolean)}
   */
  @Override
  public void onStreamEnded(String streamId, boolean activeStream) {

    runOnUiThread(() -> {
      int size = streams.size();

      if (size > 0) {

        for (int i = 0; i < size; i++) {

          if (streamId.equals(streams.get(i).getStreamId())) {

            streams.remove(streams.get(i));

            streamsAdapter.notifyItemRemoved(i);
            //streamsAdapter.notifyDataSetChanged();
            if (!isBroadcaster) {
              if (size == 1) {

                tvNoBroadCaster.setVisibility(View.VISIBLE);
                rlStreams.setVisibility(View.GONE);
              } else {
                if (activeStream) {
                  updateShimmerVisibility(true);
                  stopTimer();
                }
              }
            }
            break;
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.View#onStreamStarted(StreamsModel)}
   */
  @Override
  public void onStreamStarted(StreamsModel streamsModel) {
    if (!isBroadcaster) {
      runOnUiThread(() -> {
        updateShimmerVisibility(false);
        if (streams.size() == 0) {
          tvNoBroadCaster.setVisibility(View.GONE);
          rlStreams.setVisibility(View.VISIBLE);

          updateStreamInfo(streamsModel);
        }

        int position = -1;
        for (int i = 0; i < streams.size(); i++) {
          if (streams.get(i).getStreamId().equals(streamsModel.getStreamId())) {
            position = i;
            break;
          }
        }
        if (position != -1) {
          streams.set(position, streamsModel);
          streamsAdapter.notifyItemChanged(position);
        } else {
          streams.add(0, streamsModel);
          streamsAdapter.notifyItemInserted(0);
        }

        //streamsAdapter.notifyDataSetChanged();
      });
    }
  }

  /**
   * {@link ScrollableStreamsContract.View#updateVisibilityForJoinButton()}
   */
  @Override
  public void updateVisibilityForJoinButton() {
    if (!isAdmin && !isBroadcaster) {
      runOnUiThread(() -> ivJoin.setVisibility(View.VISIBLE));
    }
  }

  /**
   * {@link ScrollableStreamsContract.View#updateMembersAndViewersCountInList(String, int, int)}
   */
  @Override
  public void updateMembersAndViewersCountInList(String streamId, int membersCount,
      int viewersCount) {
    runOnUiThread(() -> {

      for (int i = 0; i < streams.size(); i++) {
        if (streams.get(i).getStreamId().equals(streamId)) {
          StreamsModel streamsModel = streams.get(i);
          if (membersCount != -1) streamsModel.setMembersCount(membersCount);
          if (viewersCount != -1) streamsModel.setViewersCount(viewersCount);
          streams.set(i, streamsModel);
          streamsAdapter.notifyItemChanged(i);
          //streamsAdapter.notifyDataSetChanged();
          break;
        }
      }
    });
  }

  /**
   * To fetch list of streams on pull to refresh
   */
  private void fetchLatestStreams() {
    if (isBroadcaster) {
      if (refresh.isRefreshing()) refresh.setRefreshing(false);
    } else {
      showProgressDialog(getString(R.string.ism_fetching_streams));
      try {
        scrollableStreamsPresenter.requestLiveStreamsData(Constants.STREAMS_PAGE_SIZE, true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Shows go live dialog,once the host is live.
   *
   * @param dialog the golive dialog
   */
  private void showGoLiveView(android.app.AlertDialog dialog) {

    @SuppressLint("InflateParams")
    View view = LayoutInflater.from(this).inflate(R.layout.ism_bottomsheet_live_notification, null);

    TextView tvLive = view.findViewById(R.id.tvLive);
    tvLive.setOnClickListener(v -> {
      if (goLiveDialog != null && goLiveDialog.isShowing()) {
        goLiveDialog.dismiss();
      }
    });

    TranslateAnimation translate =
        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

    translate.setDuration(200);
    translate.setFillAfter(false);

    dialog.show();
    dialog.setContentView(view);
    dialog.setCanceledOnTouchOutside(true);
    if (dialog.getWindow() != null) {
      WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
      params.width = WindowManager.LayoutParams.MATCH_PARENT;
      params.height = WindowManager.LayoutParams.WRAP_CONTENT;
      params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
      dialog.getWindow().setGravity(Gravity.BOTTOM);
      params.dimAmount = 0.7f;
      dialog.getWindow().setAttributes(params);
      dialog.getWindow()
          .setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.ism_transparent));
      dialog.getWindow().setWindowAnimations(R.style.golive_dialog_style);
    }
  }

  /**
   * Callback incase joining the broadcast fails,with corresponding errorcode
   *
   * @param err the error code of the streaming error
   * @see IsometrikRtcEventHandler#onStreamingError(int)
   */
  @Override
  public void onStreamingError(int err) {
    //err-17 -> Stream re-join error
    if (err != 17) {

      String errorMessage;

      if (err == 110) {

        if (isBroadcaster) {
          errorMessage = getString(R.string.ism_not_authorized_publisher);
        } else {
          errorMessage = getString(R.string.ism_not_authorized_audience);
        }
      } else {
        errorMessage = getString(R.string.ism_streaming_fail, err);
      }
      onError(errorMessage);
      try {
        runOnUiThread(
            () -> new Handler().postDelayed(ScrollableStreamsActivity.this::onBackPressed, 1000));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void updateStreamInfo(StreamsModel streamsModel) {

    if (streamsModel != null) {

      if (!streamsModel.getStreamId().equals(streamId)) {
        updateStreamPreviewImageVisibility(streamsLayoutManager.findFirstVisibleItemPosition(),
            true, streamsModel.getStreamId());

        //Mute remote media,when channel switched
        IsometrikUiSdk.getInstance()
            .getIsometrik()
            .getBroadcastOperations()
            .updatePreviewRemoteMediaSettings(false);

        videoGridContainer.release();

        if (switchChannelEnabled) {

          IsometrikUiSdk.getInstance()
              .getIsometrik()
              .getBroadcastOperations()
              .switchPreviewBroadcast(streamsModel.getStreamId(), null);
        }

        //Leave old stream as viewer
        scrollableStreamsPresenter.leaveAsViewer(streamId);
        streamId = streamsModel.getStreamId();
        //Clear messages and join new stream as viewer
        messages.clear();
        messagesAdapter.notifyDataSetChanged();

        scrollableStreamsPresenter.joinAsViewer(streamId, false);

        streamStartTime = streamsModel.getStartTime();
        isPublic = streamsModel.isPublic();
        isBroadcaster = false;
        isAdmin = streamsModel.getInitiatorId()
            .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId());

        scrollableStreamsPresenter.initialize(streamId, streamsModel.getMemberIds(), isAdmin,
            isBroadcaster);
        toggleRoleBasedActionButtonsVisibility(true);

        membersCount = streamsModel.getMembersCount();
        viewersCount = streamsModel.getViewersCount();
        initiatorName = streamsModel.getInitiatorName();
        initiatorIdentifier = streamsModel.getInitiatorIdentifier();
        initiatorImageUrl = streamsModel.getInitiatorImage();

        viewersListModels.clear();
        viewersUtil.updateViewers(viewersListModels);
        stopTimer();
        tvDuration.setText(getString(R.string.ism_duration_placeholder));
        alreadyRequestedCopublish = false;
        pending = false;
        accepted = false;

        if (showingGifts) {
          //To hide the gifts view on change of stream
          showingGifts = false;
          rlGifts.setVisibility(View.GONE);
          giftsModels = new ArrayList<>();
        }
      }
    }
    tvNoOfMembers.setText(String.valueOf(membersCount));
    tvNoOfViewers.setText(getString(R.string.ism_viewers_count, String.valueOf(viewersCount)));

    tvInitiatorName.setText(initiatorName);
    tvInitiatorIdentifier.setText(initiatorIdentifier);

    try {
      Glide.with(this)
          .load(initiatorImageUrl)
          .asBitmap()
          .placeholder(R.drawable.ism_default_profile_image)
          .into(new BitmapImageViewTarget(ivInitiatorImage) {
            @Override
            protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable circularBitmapDrawable =
                  RoundedBitmapDrawableFactory.create(getResources(), resource);
              circularBitmapDrawable.setCircular(true);
              ivInitiatorImage.setImageDrawable(circularBitmapDrawable);
            }
          });
    } catch (IllegalArgumentException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  private void updateStreamPreviewImageVisibility(int position, boolean show, String streamId) {

    if (position > -1) {
      try {

        if (streams.get(position).getStreamId().equals(streamId)) {

          ScrollableStreamsAdapter.StreamsViewHolder streamsViewHolder =
              (ScrollableStreamsAdapter.StreamsViewHolder) rvStreams.findViewHolderForAdapterPosition(
                  position);

          if (streamsViewHolder != null) {

            runOnUiThread(() -> {
              if (show) {

                Animation animationFadeIn =
                    AnimationUtils.loadAnimation(ScrollableStreamsActivity.this,
                        R.anim.ism_fade_in);
                animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
                  @Override
                  public void onAnimationStart(Animation animation) {
                    //Todo nothing
                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {
                    streamsViewHolder.ivStreamImage.setVisibility(View.VISIBLE);
                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {
                    //Todo nothing
                  }
                });

                streamsViewHolder.ivStreamImage.startAnimation(animationFadeIn);
              } else {

                Animation animationFadeOut =
                    AnimationUtils.loadAnimation(ScrollableStreamsActivity.this,
                        R.anim.ism_fade_out);
                animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                  @Override
                  public void onAnimationStart(Animation animation) {
                    //Todo nothing
                  }

                  @Override
                  public void onAnimationEnd(Animation animation) {
                    streamsViewHolder.ivStreamImage.setVisibility(View.GONE);
                  }

                  @Override
                  public void onAnimationRepeat(Animation animation) {
                    //Todo nothing
                  }
                });
                streamsViewHolder.ivStreamImage.startAnimation(animationFadeOut);
              }
            });
          }
        }
      } catch (Exception ignore) {

      }
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      //Keyborad is hidden to avoid click on send message button or preset messages,when shimmer is showing
      etSendMessage.setText("");
      KeyboardUtil.hideKeyboard(this);

      shimmerFrameLayout.startShimmer();
      shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
      }
    }
  }

  /**
   * Update status of copublish request in chat message,when copublish request has been
   * accepted/rejected
   */
  private void updateCopublishChatMessage(String userId) {
    runOnUiThread(() -> {
      int size = messages.size();
      for (int i = size - 1; i >= 0; i--) {
        if (messages.get(i).getMessageItemType()
            == MessageTypeEnum.CopublishRequestMessage.getValue()) {
          if (messages.get(i).getSenderId().equals(userId)) {
            MessagesModel messagesModel = messages.get(i);
            if (messagesModel.isInitiator()) {
              messagesModel.setInitiator(false);
              messagesAdapter.notifyItemChanged(i);
            }
            break;
          }
        }
      }
    });
  }

  /**
   * Update status of join button in chat message,when user switches role
   */
  private void updateJoinChatMessage() {

    String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();
    int size = messages.size();
    for (int i = size - 1; i >= 0; i--) {
      if (messages.get(i).getMessageItemType()
          == MessageTypeEnum.CopublishRequestAcceptedMessage.getValue()) {
        if (messages.get(i).getSenderId().equals(userId)) {
          MessagesModel messagesModel = messages.get(i);
          if (messagesModel.isCanJoin()) {
            messagesModel.setCanJoin(false);
            messagesAdapter.notifyItemChanged(i);
          }
          break;
        }
      }
    }
  }
}
