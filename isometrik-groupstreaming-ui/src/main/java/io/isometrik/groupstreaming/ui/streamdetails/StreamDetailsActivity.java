package io.isometrik.groupstreaming.ui.streamdetails;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.utils.DateUtil;
import io.isometrik.groupstreaming.ui.utils.StreamDialog;

/**
 * The type Stream details activity.
 * It implements StreamDetailsContract.View{@link StreamDetailsContract.View}
 *
 * @see StreamDetailsContract.View
 */
public class StreamDetailsActivity extends AppCompatActivity implements StreamDetailsContract.View {

  private StreamDetailsContract.Presenter streamDetailsPresenter;

  @BindView(R2.id.tvStreamName)
  TextView tvStreamName;

  @BindView(R2.id.tvInitiatedBy)
  TextView tvInitiatedBy;

  @BindView(R2.id.tvConnectionState)
  TextView tvConnectionState;

  @BindView(R2.id.ivStreamImage)
  AppCompatImageView ivStreamImage;

  @BindView(R2.id.abLayout)
  AppBarLayout abLayout;

  @BindView(R2.id.clToolbar)
  CollapsingToolbarLayout clToolbar;

  private boolean unregisteredListeners;
  private boolean titleHidden = false;
  private boolean audienceRequest;

  private boolean showingStreamDialog;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ism_activity_stream_details);
    ButterKnife.bind(this);

    streamDetailsPresenter = new StreamDetailsPresenter(this);

    String streamDescription = getIntent().getStringExtra("streamDescription");
    audienceRequest = getIntent().getBooleanExtra("audienceRequest", false);

    tvStreamName.setText(streamDescription);

    clToolbar.setTitleEnabled(true);
    clToolbar.setTitle(streamDescription);
    clToolbar.setExpandedTitleColor(
        ContextCompat.getColor(StreamDetailsActivity.this, R.color.ism_transparent));
    clToolbar.setCollapsedTitleTextColor(
        ContextCompat.getColor(StreamDetailsActivity.this, R.color.ism_black));

    tvInitiatedBy.setText(
        getString(R.string.ism_initiated_by, getIntent().getStringExtra("initiatorName"),
            DateUtil.getDate(getIntent().getLongExtra("startTime", 0))));

    try {
      Glide.with(StreamDetailsActivity.this)
          .load(getIntent().getStringExtra("streamImage"))
          .centerCrop()
          .placeholder(R.drawable.ism_avatar_group_large)
          .into(ivStreamImage);
    } catch (NullPointerException | IllegalArgumentException e) {
      e.printStackTrace();
    }

    abLayout.addOnOffsetChangedListener((appBarLayout, offset) -> {
      int maxScroll = appBarLayout.getTotalScrollRange();
      double percentage = (float) Math.abs(offset) / (float) maxScroll;

      if (percentage < 0.05) {

        if (titleHidden) {

          tvStreamName.setVisibility(View.VISIBLE);
          tvInitiatedBy.setVisibility(View.VISIBLE);
          titleHidden = false;
        }
      } else {

        if (!titleHidden) {

          tvStreamName.setVisibility(View.GONE);
          tvInitiatedBy.setVisibility(View.GONE);
          titleHidden = true;
        }
      }
    });
    streamDetailsPresenter.initialize(getIntent().getStringExtra("streamId"));
    streamDetailsPresenter.registerStreamsEventListener();
    streamDetailsPresenter.registerStreamMembersEventListener();
    streamDetailsPresenter.registerConnectionEventListener();

    if (audienceRequest) {

      streamDetailsPresenter.registerStreamViewersEventListener();
    }
  }

  /**
   * Back.
   */
  @OnClick({ R2.id.ibBack })
  public void back() {
    onBackPressed();
  }

  @Override
  public void connectionStateChanged(boolean connected) {
    runOnUiThread(() -> tvConnectionState.setVisibility(connected ? View.GONE : View.VISIBLE));
  }

  /**
   * {@link StreamDetailsContract.View#onStreamOffline(String, int)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType) {
    unregisterListeners();

    runOnUiThread(() -> showAlertDialog(message, dialogType));
  }

  private void showAlertDialog(String message, int dialogType) {
    if (!showingStreamDialog) {
      showingStreamDialog = true;
      AlertDialog.Builder alertDialog =
          StreamDialog.getStreamDialog(StreamDetailsActivity.this, message, dialogType);
      alertDialog.setPositiveButton(getString(R.string.ism_ok), (dialog, which) -> onBackPressed());
      AlertDialog alert = alertDialog.create();
      alert.setCancelable(false);
      alert.setCanceledOnTouchOutside(false);
      if (!isFinishing()) alert.show();
    }
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

      streamDetailsPresenter.unregisterStreamsEventListener();
      streamDetailsPresenter.unregisterStreamMembersEventListener();
      streamDetailsPresenter.unregisterConnectionEventListener();
      if (audienceRequest) {

        streamDetailsPresenter.unregisterStreamViewersEventListener();
      }
    }
  }
}
