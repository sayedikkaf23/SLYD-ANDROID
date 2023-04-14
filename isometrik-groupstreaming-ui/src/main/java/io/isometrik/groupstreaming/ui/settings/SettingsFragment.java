package io.isometrik.groupstreaming.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.settings.callbacks.ActionCallback;
import io.isometrik.groupstreaming.ui.settings.callbacks.ActionEnum;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to update settings in a broadcast to toggle local/remote audio/video
 * state,visibility of chat messages,network stats and control buttons.Members can also leave a
 * broadcast.
 */
public class SettingsFragment extends BottomSheetDialogFragment {

  public static final String TAG = "SettingsFragment";

  private View view;

  @BindView(R2.id.tvLocalAudio)
  TextView tvLocalAudio;
  @BindView(R2.id.tvLocalVideo)
  TextView tvLocalVideo;
  @BindView(R2.id.tvRemoteAudio)
  TextView tvRemoteAudio;
  @BindView(R2.id.tvRemoteVideo)
  TextView tvRemoteVideo;
  @BindView(R2.id.tvNetworkStats)
  TextView tvNetworkStats;
  @BindView(R2.id.tvChat)
  TextView tvChat;
  @BindView(R2.id.tvControls)
  TextView tvControls;

  @BindView(R2.id.ivLocalAudio)
  AppCompatImageView ivLocalAudio;
  @BindView(R2.id.ivLocalVideo)
  AppCompatImageView ivLocalVideo;
  @BindView(R2.id.ivRemoteAudio)
  AppCompatImageView ivRemoteAudio;
  @BindView(R2.id.ivRemoteVideo)
  AppCompatImageView ivRemoteVideo;
  @BindView(R2.id.ivNetworkStats)
  AppCompatImageView ivNetworkStats;
  @BindView(R2.id.ivChat)
  AppCompatImageView ivChat;
  @BindView(R2.id.ivControls)
  AppCompatImageView ivControls;
  @BindView(R2.id.rlLeave)
  RelativeLayout rlLeave;

  @BindView(R2.id.rlLocalAudio)
  RelativeLayout rlLocalAudio;
  @BindView(R2.id.rlLocalVideo)
  RelativeLayout rlLocalVideo;

  private boolean isBroadcaster, remoteVideoMuted, remoteAudioMuted, localVideoMuted,
      localAudioMuted, controlButtonsHidden, chatMessagesHidden, networkStatsShown, recreatingView,
      isAdmin;

  private String streamId;

  private ActionCallback actionCallback;

  public SettingsFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_settings, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);

    if (!recreatingView) {
      ivNetworkStats.setSelected(true);
      recreatingView = true;
    }

    if (isBroadcaster) {
      if (isAdmin) {
        rlLeave.setVisibility(View.GONE);
      } else {
        rlLeave.setVisibility(View.VISIBLE);
      }
      rlLocalAudio.setVisibility(View.VISIBLE);
      rlLocalVideo.setVisibility(View.VISIBLE);
    } else {
      rlLeave.setVisibility(View.GONE);
      rlLocalAudio.setVisibility(View.GONE);
      rlLocalVideo.setVisibility(View.GONE);
    }

    //For allowing switch from one stream to another,which by default will have remote audio/video unmuted
    if (!controlButtonsHidden) {
      ivControls.setSelected(false);
      tvControls.setText(getString(R.string.ism_disable_control_buttons));
    }

    if (!remoteAudioMuted) {
      ivRemoteAudio.setSelected(false);
      tvRemoteAudio.setText(getString(R.string.ism_remote_audio, getString(R.string.ism_mute)));
    }
    if (!remoteVideoMuted) {
      ivRemoteVideo.setSelected(false);
      tvRemoteVideo.setText(getString(R.string.ism_remote_video, getString(R.string.ism_mute)));
    }
    return view;
  }

  @Override
  public void onAttach(@NotNull Context context) {
    super.onAttach(context);

    if (context instanceof ActionCallback) {
      actionCallback = (ActionCallback) context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    actionCallback = null;
  }

  @OnClick(R2.id.rlLocalVideo)
  public void toggleLocalVideoState() {

    localVideoMuted = !localVideoMuted;
    ivLocalVideo.setSelected(localVideoMuted);

    if (localVideoMuted) {
      tvLocalVideo.setText(getString(R.string.ism_unmute_local_video));
    } else {
      tvLocalVideo.setText(getString(R.string.ism_mute_local_video));
    }

    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .muteLocalVideo(localVideoMuted);
  }

  @OnClick(R2.id.rlLocalAudio)
  public void toggleLocalAudioState() {

    localAudioMuted = !localAudioMuted;
    ivLocalAudio.setSelected(localAudioMuted);

    if (localAudioMuted) {
      tvLocalAudio.setText(getString(R.string.ism_unmute_local_audio));
    } else {
      tvLocalAudio.setText(getString(R.string.ism_mute_local_audio));
    }

    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .muteLocalAudio(localAudioMuted);
  }

  @OnClick(R2.id.rlRemoteVideo)
  public void toggleRemoteVideoState() {

    remoteVideoMuted = !remoteVideoMuted;
    ivRemoteVideo.setSelected(remoteVideoMuted);

    if (remoteVideoMuted) {
      tvRemoteVideo.setText(getString(R.string.ism_remote_video, getString(R.string.ism_unmute)));
    } else {
      tvRemoteVideo.setText(getString(R.string.ism_remote_video, getString(R.string.ism_mute)));
    }

    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .muteAllRemoteVideo(remoteVideoMuted);
  }

  @OnClick(R2.id.rlLeave)
  public void leaveBroadcast() {
    if (actionCallback != null) actionCallback.leaveBroadcastRequested();
  }

  @OnClick(R2.id.rlRemoteAudio)
  public void toggleRemoteAudioState() {

    remoteAudioMuted = !remoteAudioMuted;
    ivRemoteAudio.setSelected(remoteAudioMuted);

    if (remoteAudioMuted) {
      tvRemoteAudio.setText(getString(R.string.ism_remote_audio, getString(R.string.ism_unmute)));
    } else {
      tvRemoteAudio.setText(getString(R.string.ism_remote_audio, getString(R.string.ism_mute)));
    }

    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .muteAllRemoteAudio(remoteAudioMuted);
  }

  @OnClick(R2.id.rlNetworkStats)
  public void updateNetworkStatsVisibility() {

    ivNetworkStats.setSelected(networkStatsShown);

    networkStatsShown = !networkStatsShown;

    if (networkStatsShown) {
      tvNetworkStats.setText(getString(R.string.ism_disable_network_stats));
    } else {
      tvNetworkStats.setText(getString(R.string.ism_enable_network_stats));
    }

    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .setNetworkQualityIndicatorEnabled(networkStatsShown);
  }

  @OnClick(R2.id.rlChat)
  public void updateChatMessagesVisibility() {

    chatMessagesHidden = !chatMessagesHidden;

    ivChat.setSelected(chatMessagesHidden);

    if (chatMessagesHidden) {
      tvChat.setText(getString(R.string.ism_enable_messages));
    } else {
      tvChat.setText(getString(R.string.ism_disable_messages));
    }
    if (actionCallback != null) {
      actionCallback.onSettingsChange(ActionEnum.ChatMessagesVisibilityToggleAction.getValue(),
          !chatMessagesHidden);
    }
  }

  @OnClick(R2.id.rlControls)
  public void updateControlButtonsVisibility() {
    controlButtonsHidden = !controlButtonsHidden;

    ivControls.setSelected(controlButtonsHidden);

    if (controlButtonsHidden) {
      tvControls.setText(getString(R.string.ism_enable_control_buttons));
    } else {
      tvControls.setText(getString(R.string.ism_disable_control_buttons));
    }
    if (actionCallback != null) {
      actionCallback.onSettingsChange(ActionEnum.ControlButtonsVisibilityToggleAction.getValue(),
          !controlButtonsHidden);
    }
  }

  public void updateParameters(boolean isAdmin, boolean isBroadcaster, String streamId) {

    this.isAdmin = isAdmin;
    this.isBroadcaster = isBroadcaster;

    try {
      if (!streamId.equals(this.streamId)) {

        remoteAudioMuted = false;
        remoteVideoMuted = false;
        controlButtonsHidden = false;
      }
      this.streamId = streamId;
    } catch (Exception ignore) {

    }
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    //to handle case when stream has been switched and than same stream selected back,leading to incorrect state of remote audio/video text and image as streamId was not changed
    this.streamId = "";
    super.onDismiss(dialog);
  }
}