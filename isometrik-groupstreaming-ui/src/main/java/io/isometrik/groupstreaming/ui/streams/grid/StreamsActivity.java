package io.isometrik.groupstreaming.ui.streams.grid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.snackbar.Snackbar;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.live.GoLiveActivity;
import io.isometrik.groupstreaming.ui.profile.UserDetailsActivity;
import io.isometrik.groupstreaming.ui.streams.preview.PreviewStreamsActivity;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import java.io.Serializable;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The type Streams activity.
 * It implements StreamsContract.View{@link StreamsContract.View}
 *
 * @see StreamsContract.View
 */
public class StreamsActivity extends AppCompatActivity implements StreamsContract.View {

  private StreamsContract.Presenter streamsPresenter;

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

  @BindView(R2.id.ivProfile)
  AppCompatImageView ivProfile;

  private AlertProgress alertProgress;

  private AlertDialog alertDialog;

  private ArrayList<StreamsModel> streams = new ArrayList<>();
  private StreamsAdapter streamsAdapter;

  private GridLayoutManager layoutManager;
  private boolean unregisteredListeners;

  private Intent broadcastIntent;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ism_activity_livestreams);
    ButterKnife.bind(this);

    streamsPresenter = new StreamsPresenter(this);
    alertProgress = new AlertProgress();

    int mColumnCount = 2;
    layoutManager = new GridLayoutManager(this, mColumnCount);
    rvLiveStreams.setLayoutManager(layoutManager);
    streamsAdapter = new StreamsAdapter(this, streams);
    rvLiveStreams.addOnScrollListener(recyclerViewOnScrollListener);
    rvLiveStreams.setAdapter(streamsAdapter);

    fetchLatestStreams();

    streamsPresenter.addSubscription(true);
    streamsPresenter.addSubscription(false);

    streamsPresenter.registerStreamsEventListener();
    streamsPresenter.registerPresenceEventListener();
    streamsPresenter.registerStreamMembersEventListener();
    streamsPresenter.registerStreamViewersEventListener();
    streamsPresenter.registerConnectionEventListener();
    streamsPresenter.registerCopublishRequestsEventListener();
    try {
      Glide.with(this)
          .load(IsometrikUiSdk.getInstance().getUserSession().getUserProfilePic())
          .asBitmap()
          .placeholder(R.drawable.ism_default_profile_image)
          .into(new BitmapImageViewTarget(ivProfile) {
            @Override
            protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable circularBitmapDrawable =
                  RoundedBitmapDrawableFactory.create(getResources(), resource);
              circularBitmapDrawable.setCircular(true);
              ivProfile.setImageDrawable(circularBitmapDrawable);
            }
          });
    } catch (IllegalArgumentException | NullPointerException e) {
      e.printStackTrace();
    }
    refresh.setOnRefreshListener(this::fetchLatestStreams);

    try {
      new Handler().postDelayed(() -> IsometrikUiSdk.getInstance()
          .getIsometrik()
          .createConnection(IsometrikUiSdk.getInstance().getUserSession().getUserId()), 500);
    } catch (Exception e) {
      e.printStackTrace();
    }

    streamsPresenter.updateUserPublishStatus();
  }

  /**
   * Go live.
   */
  @OnClick(R2.id.btGoLive)
  public void goLive() {

    startActivity(new Intent(this, GoLiveActivity.class));
  }

  /**
   * View user profile.
   */
  @OnClick(R2.id.ivProfile)
  public void viewUserProfile() {

    startActivity(new Intent(this, UserDetailsActivity.class));
  }

  /**
   * View user profile.
   */
  @OnClick(R2.id.ivPreview)
  public void previewStreams() {

    startActivity(
        new Intent(this, PreviewStreamsActivity.class).putExtra("streams", (Serializable) streams));
  }

  /**
   * The Recycler view on scroll listener.
   */
  public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
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
   * {@link StreamsContract.View#onLiveStreamsDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onLiveStreamsDataReceived(ArrayList<StreamsModel> streams, boolean latestStreams) {

    if (latestStreams) {
      this.streams.clear();
    }
    this.streams.addAll(streams);

    runOnUiThread(() -> {
      if (StreamsActivity.this.streams.size() > 0) {
        tvNoBroadCaster.setVisibility(View.GONE);
        rvLiveStreams.setVisibility(View.VISIBLE);
        streamsAdapter.notifyDataSetChanged();
      } else {
        tvNoBroadCaster.setVisibility(View.VISIBLE);
        rvLiveStreams.setVisibility(View.GONE);
      }
    });
    hideProgressDialog();
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
  }

  /**
   * {@link StreamsContract.View#onMemberAdded(MemberAddEvent, boolean)}
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
   * {@link StreamsContract.View#onCopublishRequestAccepted(CopublishRequestAcceptEvent)}
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
   * {@link StreamsContract.View#onMemberRemoved(MemberRemoveEvent, boolean)}
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
   * {@link StreamsContract.View#onMemberLeft(MemberLeaveEvent)}
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
   * {@link StreamsContract.View#onStreamEnded(String)}
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
   * {@link StreamsContract.View#onStreamStarted(StreamsModel)}
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

      //streamsAdapter.notifyDataSetChanged();
    });
  }

  /**
   * {@link StreamsContract.View#updateMembersAndViewersCount(String, int, int)}
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
   * {@link StreamsContract.View#onPublishStarted(PublishStartEvent, String)}
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
   * {@link StreamsContract.View#onPublishStopped(PublishStopEvent, String)}
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
   * {@link StreamsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
    hideProgressDialog();

    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(StreamsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(StreamsActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  /**
   * {@link StreamsContract.View#failedToConnect(String)}
   */
  @Override
  public void failedToConnect(String errorMessage) {

    runOnUiThread(() -> {
      tvConnectionState.setVisibility(View.VISIBLE);

      if (errorMessage != null) {
        Toast.makeText(StreamsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(StreamsActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
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

  private void unregisterListeners() {
    if (!unregisteredListeners) {
      unregisteredListeners = true;
      hideProgressDialog();

      streamsPresenter.removeSubscription(true);
      streamsPresenter.removeSubscription(false);
      streamsPresenter.unregisterStreamsEventListener();
      streamsPresenter.unregisterPresenceEventListener();
      streamsPresenter.unregisterStreamMembersEventListener();
      streamsPresenter.unregisterStreamViewersEventListener();
      streamsPresenter.unregisterConnectionEventListener();
      streamsPresenter.unregisterCopublishRequestsEventListener();
    }
  }

  /**
   * Check streaming permissions.
   */
  private void checkStreamingPermissions() {

    if ((ContextCompat.checkSelfPermission(StreamsActivity.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
        StreamsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
        StreamsActivity.this, Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED)) {

      if ((ActivityCompat.shouldShowRequestPermissionRationale(StreamsActivity.this,
          Manifest.permission.CAMERA))
          || (ActivityCompat.shouldShowRequestPermissionRationale(StreamsActivity.this,
          Manifest.permission.WRITE_EXTERNAL_STORAGE))
          || (ActivityCompat.shouldShowRequestPermissionRationale(StreamsActivity.this,
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

      startActivity(broadcastIntent);
    }
  }

  private void requestPermissions() {

    ActivityCompat.requestPermissions(StreamsActivity.this, new String[] {
        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
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
        startActivity(broadcastIntent);
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

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void fetchLatestStreams() {
    showProgressDialog(getString(R.string.ism_fetching_streams));
    try {
      streamsPresenter.requestLiveStreamsData(Constants.STREAMS_PAGE_SIZE, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}