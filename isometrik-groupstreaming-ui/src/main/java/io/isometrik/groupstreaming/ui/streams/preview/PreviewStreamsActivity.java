package io.isometrik.groupstreaming.ui.streams.preview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.snackbar.Snackbar;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.streams.grid.StreamsModel;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.groupstreaming.ui.utils.KeyboardUtil;
import io.isometrik.groupstreaming.ui.utils.TimeDownView;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.rtcengine.rtc.IsometrikRtcEventHandler;
import io.isometrik.gs.rtcengine.utils.PreviewVideoGridContainer;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

import static androidx.lifecycle.Lifecycle.State.RESUMED;

/**
 * The type preview streams activity.
 * It implements PreviewStreamsContract.View{@link PreviewStreamsContract.View}
 *
 * @see PreviewStreamsContract.View
 */
public class PreviewStreamsActivity extends AppCompatActivity
    implements PreviewStreamsContract.View, IsometrikRtcEventHandler {

  private PreviewStreamsContract.Presenter streamsPresenter;

  @BindView(R2.id.rlParent)
  RelativeLayout rlParent;

  @BindView(R2.id.tvNoBroadcaster)
  TextView tvNoBroadCaster;

  @BindView(R2.id.tvConnectionState)
  TextView tvConnectionState;

  @BindView(R2.id.rvLiveStreams)
  RecyclerView rvLiveStreams;

  @BindView(R2.id.refresh)
  SwipeRefreshLayout refresh;

  @BindView(R2.id.tvCountDown)
  TimeDownView tvCountDown;

  @BindView(R2.id.videoGridContainer)
  PreviewVideoGridContainer previewVideoGridContainer;

  @BindView(R2.id.tvPreviewDisclaimer)
  TextView tvPreviewDisclaimer;

  private AlertProgress alertProgress;

  private AlertDialog alertDialog;

  private ArrayList<StreamsModel> streams;

  private PreviewStreamsAdapter streamsAdapter;

  private LinearLayoutManager layoutManager;
  private boolean unregisteredListeners;

  private Intent broadcastIntent;

  private String currentlyPreviewingStream;
  private boolean channelSwitchEnabled;
  private boolean notAlreadyRecordedPreview = true;
  private String lastBroadcastJoined;
  private boolean alreadyResumed;

  @SuppressWarnings("unchecked")
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ism_activity_preview_livestreams);
    ButterKnife.bind(this);

    streamsPresenter = new PreviewStreamsPresenter(this);
    alertProgress = new AlertProgress();

    layoutManager = new LinearLayoutManager(this);
    rvLiveStreams.setLayoutManager(layoutManager);

    SnapHelper snapHelper = new PagerSnapHelper();
    snapHelper.attachToRecyclerView(rvLiveStreams);

    streams = (ArrayList<StreamsModel>) getIntent().getSerializableExtra("streams");
    IsometrikUiSdk.getInstance().getIsometrik().registerBroadcastRtcEventHandler(this);

    if (streams == null) {

      streams = new ArrayList<>();
    }
    streamsAdapter = new PreviewStreamsAdapter(this, streams);
    rvLiveStreams.addOnScrollListener(recyclerViewOnScrollListener);
    rvLiveStreams.setAdapter(streamsAdapter);
    onLiveStreamsDataReceived(streams, true, false);

    streamsPresenter.registerStreamsEventListener();
    streamsPresenter.registerPresenceEventListener();
    streamsPresenter.registerStreamMembersEventListener();
    streamsPresenter.registerStreamViewersEventListener();
    streamsPresenter.registerConnectionEventListener();
    streamsPresenter.registerCopublishRequestsEventListener();

    refresh.setOnRefreshListener(this::fetchLatestStreams);
  }

  private void fetchLatestStreams() {
    showProgressDialog(getString(R.string.ism_fetching_streams));
    try {
      streamsPresenter.requestLiveStreamsData(Constants.STREAMS_PAGE_SIZE, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The Recycler view on scroll listener.
   */
  public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);

          if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            tvPreviewDisclaimer.setVisibility(View.GONE);
            if (layoutManager.findFirstVisibleItemPosition() > -1) {
              int position = layoutManager.findFirstVisibleItemPosition();
              currentlyPreviewingStream = streams.get(position).getStreamId();
              if (streams.get(position).getNumberOfTimesAlreadySeen()
                  < PreviewStreamsConstants.MAXIMUM_FREE_PREVIEWS_ALLOWED) {

                joinBroadcastPreview();
              } else {
                tvPreviewDisclaimer.setVisibility(View.VISIBLE);

                updateStreamPreviewImageVisibility(position, true);
              }
            }
          } else {
            tvCountDown.setVisibility(View.GONE);
            if (currentlyPreviewingStream != null) {
              currentlyPreviewingStream = null;
              streamsPresenter.stopBroadcastPreview(previewVideoGridContainer, false);
            }
          }
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          streamsPresenter.requestLiveStreamsDataOnScroll(
              layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
              layoutManager.getItemCount());
        }
      };

  @Override
  public void connectionStateChanged(boolean connected) {
    runOnUiThread(() -> tvConnectionState.setVisibility(connected ? View.GONE : View.VISIBLE));
  }

  /**
   * {@link PreviewStreamsContract.View#onLiveStreamsDataReceived(ArrayList, boolean, boolean)}
   */
  @Override
  public void onLiveStreamsDataReceived(ArrayList<StreamsModel> streams, boolean latestStreams,
      boolean apiResponse) {
    tvPreviewDisclaimer.setVisibility(View.GONE);
    if (latestStreams) {
      if (apiResponse) this.streams.clear();
    }
    if (apiResponse) this.streams.addAll(streams);
    hideProgressDialog();
    runOnUiThread(() -> {
      if (PreviewStreamsActivity.this.streams.size() > 0) {
        tvNoBroadCaster.setVisibility(View.GONE);
        rvLiveStreams.setVisibility(View.VISIBLE);
        streamsAdapter.notifyDataSetChanged();

        currentlyPreviewingStream = streams.get(0).getStreamId();
        joinBroadcastPreview();
      } else {
        tvNoBroadCaster.setVisibility(View.VISIBLE);
        rvLiveStreams.setVisibility(View.GONE);
      }
    });

    if (refresh.isRefreshing()) refresh.setRefreshing(false);
  }

  /**
   * {@link PreviewStreamsContract.View#onMemberAdded(MemberAddEvent, boolean)}
   */
  @Override
  public void onMemberAdded(MemberAddEvent memberAddEvent, boolean givenMemberAdded) {

    runOnUiThread(() -> {
      int size = streams.size();

      if (size > 0) {
        String streamId = memberAddEvent.getStreamId();

        for (int i = 0; i < size; i++) {
          if (streamId.equals(streams.get(i).getStreamId())) {

            StreamsModel streamsModel = streams.get(i);
            streamsModel.setMembersCount(memberAddEvent.getMembersCount());
            streamsModel.setViewersCount(memberAddEvent.getViewersCount());

            ArrayList<String> memberIds = streamsModel.getMemberIds();
            if (!memberIds.contains(memberAddEvent.getMemberId())) {
              memberIds.add(memberAddEvent.getMemberId());
              streamsModel.setMemberIds(memberIds);
            }

            if (givenMemberAdded) streamsModel.setGivenUserIsMember(true);
            streams.set(i, streamsModel);
            streamsAdapter.notifyItemChanged(i);
            //streamsAdapter.notifyDataSetChanged();

            break;
          }
        }
      }
    });
  }

  /**
   * {@link PreviewStreamsContract.View#onCopublishRequestAccepted(CopublishRequestAcceptEvent)}
   */
  @Override
  public void onCopublishRequestAccepted(CopublishRequestAcceptEvent copublishRequestAcceptEvent) {

    runOnUiThread(() -> {
      int size = streams.size();

      if (size > 0) {
        String streamId = copublishRequestAcceptEvent.getStreamId();

        for (int i = 0; i < size; i++) {
          if (streamId.equals(streams.get(i).getStreamId())) {

            StreamsModel streamsModel = streams.get(i);
            streamsModel.setMembersCount(copublishRequestAcceptEvent.getMembersCount());
            streamsModel.setViewersCount(copublishRequestAcceptEvent.getViewersCount());

            ArrayList<String> memberIds = streamsModel.getMemberIds();
            if (!memberIds.contains(copublishRequestAcceptEvent.getUserId())) {
              memberIds.add(copublishRequestAcceptEvent.getUserId());
              streamsModel.setMemberIds(memberIds);
            }

            streamsModel.setGivenUserIsMember(true);
            streams.set(i, streamsModel);
            streamsAdapter.notifyItemChanged(i);
            //streamsAdapter.notifyDataSetChanged();

            break;
          }
        }
      }
    });
  }

  /**
   * {@link PreviewStreamsContract.View#onMemberRemoved(MemberRemoveEvent, boolean)}
   */
  @Override
  public void onMemberRemoved(MemberRemoveEvent memberRemoveEvent, boolean givenMemberRemoved) {

    runOnUiThread(() -> {
      int size = streams.size();
      if (size > 0) {
        String streamId = memberRemoveEvent.getStreamId();

        for (int i = 0; i < size; i++) {
          if (streamId.equals(streams.get(i).getStreamId())) {

            StreamsModel streamsModel = streams.get(i);
            streamsModel.setMembersCount(memberRemoveEvent.getMembersCount());
            streamsModel.setViewersCount(memberRemoveEvent.getViewersCount());
            if (givenMemberRemoved) streamsModel.setGivenUserIsMember(false);

            ArrayList<String> memberIds = streamsModel.getMemberIds();
            if (memberIds.contains(memberRemoveEvent.getMemberId())) {
              memberIds.remove(memberRemoveEvent.getMemberId());
              streamsModel.setMemberIds(memberIds);
            }
            streams.set(i, streamsModel);
            streamsAdapter.notifyItemChanged(i);
            //streamsAdapter.notifyDataSetChanged();

            break;
          }
        }
      }
    });
  }

  /**
   * {@link PreviewStreamsContract.View#onMemberLeft(MemberLeaveEvent)}
   */
  @Override
  public void onMemberLeft(MemberLeaveEvent memberLeaveEvent) {

    runOnUiThread(() -> {
      int size = streams.size();
      if (size > 0) {

        String streamId = memberLeaveEvent.getStreamId();

        for (int i = 0; i < size; i++) {
          if (streamId.equals(streams.get(i).getStreamId())) {

            StreamsModel streamsModel = streams.get(i);
            streamsModel.setMembersCount(memberLeaveEvent.getMembersCount());
            streamsModel.setViewersCount(memberLeaveEvent.getViewersCount());
            streamsModel.setGivenUserIsMember(false);

            ArrayList<String> memberIds = streamsModel.getMemberIds();
            if (memberIds.contains(memberLeaveEvent.getMemberId())) {
              memberIds.remove(memberLeaveEvent.getMemberId());
              streamsModel.setMemberIds(memberIds);
            }

            if (memberLeaveEvent.getMemberId()
                .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
              streamsModel.setGivenUserIsMember(false);
            }
            streams.set(i, streamsModel);
            streamsAdapter.notifyItemChanged(i);
            //streamsAdapter.notifyDataSetChanged();

            break;
          }
        }
      }
    });
  }

  /**
   * {@link PreviewStreamsContract.View#onStreamEnded(String)}
   */
  @Override
  public void onStreamEnded(String streamId) {

    runOnUiThread(() -> {
      int size = streams.size();
      if (size > 0) {
        for (int i = 0; i < size; i++) {
          if (streamId.equals(streams.get(i).getStreamId())) {
            streams.remove(streams.get(i));
            streamsAdapter.notifyItemRemoved(i);

            if (layoutManager.findFirstVisibleItemPosition() == i) {
              //Of the stream being viewed has been ended
              tvPreviewDisclaimer.setVisibility(View.GONE);
              tvCountDown.setVisibility(View.GONE);
              streamsPresenter.stopBroadcastPreview(previewVideoGridContainer, true);
              currentlyPreviewingStream = null;
            }

            //streamsAdapter.notifyDataSetChanged();
            if (size == 1) {
              tvNoBroadCaster.setVisibility(View.VISIBLE);
              rvLiveStreams.setVisibility(View.GONE);
            }
            break;
          }
        }
      }
    });
  }

  /**
   * {@link PreviewStreamsContract.View#onStreamStarted(StreamsModel)}
   */
  @Override
  public void onStreamStarted(StreamsModel streamsModel) {

    runOnUiThread(() -> {
      if (streams.size() == 0) {
        tvNoBroadCaster.setVisibility(View.GONE);
        rvLiveStreams.setVisibility(View.VISIBLE);
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
      if (streams.size() == 1) {
        //If the newly started stream is the only stream in the list
        currentlyPreviewingStream = streams.get(0).getStreamId();
        joinBroadcastPreview();
      }
      //streamsAdapter.notifyDataSetChanged();
    });
  }

  /**
   * {@link PreviewStreamsContract.View#updateMembersAndViewersCount(String, int, int)}
   */
  @Override
  public void updateMembersAndViewersCount(String streamId, int membersCount, int viewersCount) {

    runOnUiThread(() -> {
      int size = streams.size();
      if (size > 0) {

        for (int i = 0; i < size; i++) {
          if (streamId.equals(streams.get(i).getStreamId())) {

            StreamsModel streamsModel = streams.get(i);
            streamsModel.setMembersCount(membersCount);
            streamsModel.setViewersCount(viewersCount);
            streams.set(i, streamsModel);
            streamsAdapter.notifyItemChanged(i);
            //streamsAdapter.notifyDataSetChanged();

            break;
          }
        }
      }
    });
  }

  /**
   * {@link PreviewStreamsContract.View#onPublishStarted(PublishStartEvent, String)}
   */
  @Override
  public void onPublishStarted(PublishStartEvent publishStartEvent, String userId) {

    runOnUiThread(() -> {
      int size = streams.size();
      if (size > 0) {
        String streamId = publishStartEvent.getStreamId();
        for (int i = 0; i < size; i++) {
          if (streamId.equals(streams.get(i).getStreamId())) {

            StreamsModel streamsModel = streams.get(i);
            streamsModel.setMembersCount(publishStartEvent.getMembersCount());
            streamsModel.setViewersCount(publishStartEvent.getViewersCount());

            if (streamsModel.getMemberIds().contains(userId)) {
              streamsModel.setGivenUserIsMember(false);
            }

            streams.set(i, streamsModel);
            streamsAdapter.notifyItemChanged(i);
            //streamsAdapter.notifyDataSetChanged();

            break;
          }
        }
      }
    });
  }

  /**
   * {@link PreviewStreamsContract.View#onPublishStopped(PublishStopEvent, String)}
   */
  @Override
  public void onPublishStopped(PublishStopEvent publishStopEvent, String userId) {

    runOnUiThread(() -> {
      int size = streams.size();
      if (size > 0) {
        String streamId = publishStopEvent.getStreamId();
        for (int i = 0; i < size; i++) {
          if (streamId.equals(streams.get(i).getStreamId())) {

            StreamsModel streamsModel = streams.get(i);
            streamsModel.setMembersCount(publishStopEvent.getMembersCount());
            streamsModel.setViewersCount(publishStopEvent.getViewersCount());

            if (streamsModel.getMemberIds().contains(userId)) {
              streamsModel.setGivenUserIsMember(true);
            }

            streams.set(i, streamsModel);
            streamsAdapter.notifyItemChanged(i);
            //streamsAdapter.notifyDataSetChanged();

            break;
          }
        }
      }
    });
  }

  /**
   * {@link PreviewStreamsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
    hideProgressDialog();

    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(PreviewStreamsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(PreviewStreamsActivity.this, getString(R.string.ism_error),
            Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  @OnClick(R2.id.ibExit)
  public void exit() {

    onBackPressed();
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

  private void unregisterListeners() {
    if (!unregisteredListeners) {
      unregisteredListeners = true;
      hideProgressDialog();

      streamsPresenter.stopBroadcastPreview(previewVideoGridContainer, true);

      streamsPresenter.endBroadcastPreview();
      streamsPresenter.unregisterStreamsEventListener();
      streamsPresenter.unregisterPresenceEventListener();
      streamsPresenter.unregisterStreamMembersEventListener();
      streamsPresenter.unregisterStreamViewersEventListener();
      streamsPresenter.unregisterConnectionEventListener();
      streamsPresenter.unregisterCopublishRequestsEventListener();

      IsometrikUiSdk.getInstance().getIsometrik().removeBroadcastRtcEventHandler(this);
    }
  }

  private void requestPermissions() {

    ActivityCompat.requestPermissions(PreviewStreamsActivity.this, new String[] {
        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    }, 0);
  }

  /**
   * Check streaming permissions.
   */
  public void checkStreamingPermissions() {

    if ((ContextCompat.checkSelfPermission(PreviewStreamsActivity.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
        PreviewStreamsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
        PreviewStreamsActivity.this, Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED)) {

      if ((ActivityCompat.shouldShowRequestPermissionRationale(PreviewStreamsActivity.this,
          Manifest.permission.CAMERA))
          || (ActivityCompat.shouldShowRequestPermissionRationale(PreviewStreamsActivity.this,
          Manifest.permission.WRITE_EXTERNAL_STORAGE))
          || (ActivityCompat.shouldShowRequestPermissionRationale(PreviewStreamsActivity.this,
          Manifest.permission.RECORD_AUDIO))) {
        Snackbar snackbar = Snackbar.make(rlParent, R.string.ism_permission_start_streaming,
            Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.ism_ok), view -> this.requestPermissions());

        snackbar.show();

        ((TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text))
            .setGravity(Gravity.CENTER_HORIZONTAL);
      } else {

        requestPermissions();
      }
    } else {

      joinBroadcastAsPublisher(broadcastIntent);
    }
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
        joinBroadcastAsPublisher(broadcastIntent);
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  /**
   * Start broadcast.
   *
   * @param intent the intent
   */
  public void startBroadcast(Intent intent) {
    this.broadcastIntent = intent;
    checkStreamingPermissions();
  }

  private void joinBroadcastPreview() {
    if (tvCountDown.getVisibility() == View.VISIBLE) {
      //To cancel already running timer task
      tvCountDown.setVisibility(View.GONE);
    }

    notAlreadyRecordedPreview = true;

    if (currentlyPreviewingStream.equals(lastBroadcastJoined)) {
      //As switch channel to same channel is not triggering the callback
      streamsPresenter.joinBroadcastPreview(currentlyPreviewingStream, previewVideoGridContainer,
          false);
    } else {
      previewVideoGridContainer.release();
      streamsPresenter.joinBroadcastPreview(currentlyPreviewingStream, previewVideoGridContainer,
          channelSwitchEnabled);
    }

    lastBroadcastJoined = currentlyPreviewingStream;

    if (channelSwitchEnabled) {
      prepareCountDownTimer();
    } else {
      showProgressDialog(getString(R.string.ism_preparing_preview));
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
    if ((rlParent.getRootView().getHeight() - rlParent.getHeight()) > KeyboardUtil.dpToPx(this,
        200)) {
      //Keyboard is shown
      KeyboardUtil.hideKeyboard(this);

      runOnUiThread(() -> {
        try {
          new Handler().postDelayed(() -> IsometrikUiSdk.getInstance()
              .getIsometrik()
              .getBroadcastOperations()
              .renderPreviewRemoteUser(uid, previewVideoGridContainer), 1000);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    } else {
      runOnUiThread(() -> IsometrikUiSdk.getInstance()
          .getIsometrik()
          .getBroadcastOperations()
          .renderPreviewRemoteUser(uid, previewVideoGridContainer));
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
        .removePreviewRemoteUser(uid, previewVideoGridContainer));
  }

  /**
   * Trigger on change of connection state to the streaming RTC engine
   *
   * @param state the state of connection to streaming RTC engine
   * @param reason the reason of change in state
   * @param connected whether connected to streaming server or not
   *
   * {@link IsometrikRtcEventHandler#onConnectionStateChanged(int,
   * int, Boolean)}
   */
  @Override
  public void onConnectionStateChanged(int state, int reason, Boolean connected) {

    if (connected != null && connected) {
      connectionStateChanged(true);
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
  }

  @Override
  public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    if (getLifecycle().getCurrentState().isAtLeast(RESUMED)) {
      if (!channelSwitchEnabled) {

        channelSwitchEnabled = true;
        runOnUiThread(this::prepareCountDownTimer);

        hideProgressDialog();
      }

      if (channel.equals(currentlyPreviewingStream)) {
        onChannelJoined();
      }
    }
  }

  @Override
  public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
    //Not used with AR filters
  }

  @Override
  public void onStreamingError(int err) {

    if (err == 17) {
      //When user tries to rejoin the channel
      onChannelJoined();
    }
  }

  private void onChannelJoined() {

    updateStreamPreviewImageVisibility(layoutManager.findFirstVisibleItemPosition(), false);
  }

  private void prepareCountDownTimer() {
    tvCountDown.downSecond(PreviewStreamsConstants.PREVIEW_DURATION_ALLOWED);
    tvCountDown.setVisibility(View.VISIBLE);
    tvCountDown.setOnTimeDownListener(new TimeDownView.DownTimeWatcher() {
      @Override
      public void onTime(int num) {
        if (notAlreadyRecordedPreview) {

          if (num < PreviewStreamsConstants.DURATION_TO_COUNT_PREVIEW) {
            notAlreadyRecordedPreview = false;
            int position = layoutManager.findFirstVisibleItemPosition();

            if (position > -1) {
              try {

                if (currentlyPreviewingStream.equals(streams.get(position).getStreamId())) {

                  StreamsModel streamsModel = streams.get(position);

                  streamsModel.setNumberOfTimesAlreadySeen(
                      streamsModel.getNumberOfTimesAlreadySeen() + 1);
                  streams.set(position, streamsModel);
                }
              } catch (Exception ignore) {
              }
            }
          }
        }
      }

      @Override
      public void onLastTime(int num) {

      }

      @Override
      public void onLastTimeFinish(int num) {

        tvCountDown.setVisibility(View.GONE);
        tvPreviewDisclaimer.setVisibility(View.VISIBLE);

        updateStreamPreviewImageVisibility(layoutManager.findFirstVisibleItemPosition(), true);

        if (currentlyPreviewingStream != null) {
          currentlyPreviewingStream = null;
          streamsPresenter.stopBroadcastPreview(previewVideoGridContainer, false);
        }
      }
    });
  }

  private void updateStreamPreviewImageVisibility(int position, boolean show) {
    if (position > -1) {
      try {

        if (streams.get(position).getStreamId().equals(currentlyPreviewingStream)) {

          PreviewStreamsAdapter.StreamsViewHolder streamsViewHolder =
              (PreviewStreamsAdapter.StreamsViewHolder) rvLiveStreams.findViewHolderForAdapterPosition(
                  position);

          if (streamsViewHolder != null) {
            if (show) {
              streamsViewHolder.ivStreamImage.setVisibility(View.VISIBLE);
            } else {
              streamsViewHolder.ivStreamImage.setVisibility(View.GONE);
            }
          }
        }
      } catch (Exception ignore) {
      }
    }
  }

  @Override
  protected void onResume() {

    if (alreadyResumed) {
      if (!channelSwitchEnabled) {
        tvPreviewDisclaimer.setVisibility(View.GONE);
        if (layoutManager.findFirstVisibleItemPosition() > -1) {
          int position = layoutManager.findFirstVisibleItemPosition();
          currentlyPreviewingStream = streams.get(position).getStreamId();
          if (streams.get(position).getNumberOfTimesAlreadySeen()
              < PreviewStreamsConstants.MAXIMUM_FREE_PREVIEWS_ALLOWED) {

            joinBroadcastPreview();
          } else {
            tvPreviewDisclaimer.setVisibility(View.VISIBLE);

            updateStreamPreviewImageVisibility(position, true);
          }
        }
      }
    } else {

      alreadyResumed = true;
    }
    super.onResume();
  }

  public void joinBroadcastAsAudience(Intent intent) {
    streamsPresenter.endBroadcastPreview();
    channelSwitchEnabled = false;

    startActivity(intent);
  }

  public void joinBroadcastAsPublisher(Intent intent) {
    streamsPresenter.endBroadcastPreview();
    channelSwitchEnabled = false;

    startActivity(intent);
  }
}