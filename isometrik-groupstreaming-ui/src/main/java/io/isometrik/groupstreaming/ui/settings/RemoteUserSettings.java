package io.isometrik.groupstreaming.ui.settings;

import android.content.Context;
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
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to remote user's audio/video mute state and to toggle it in a
 * broadcast.Host can kick out a member.
 */
public class RemoteUserSettings extends BottomSheetDialogFragment {

  public static final String TAG = "RemoteUserSettings";

  private View view;

  @BindView(R2.id.tvRemoteAudio)
  TextView tvRemoteAudio;
  @BindView(R2.id.tvRemoteVideo)
  TextView tvRemoteVideo;

  @BindView(R2.id.ivRemoteAudio)
  AppCompatImageView ivRemoteAudio;
  @BindView(R2.id.ivRemoteVideo)
  AppCompatImageView ivRemoteVideo;

  @BindView(R2.id.rlKickOut)
  RelativeLayout rlKickOut;

  private boolean remoteVideoMuted, remoteAudioMuted, isAdmin;

  private String userName, streamId, userId;
  private int uid;
  private ActionCallback actionCallback;

  public RemoteUserSettings() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_remoteuser_settings, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);

    if (isAdmin) {
      rlKickOut.setVisibility(View.VISIBLE);
    } else {
      rlKickOut.setVisibility(View.GONE);
    }

    //For allowing switch from one stream to another, which by default will have remote audio/video unmuted
    ivRemoteAudio.setSelected(remoteAudioMuted);
    if (remoteAudioMuted) {
      tvRemoteAudio.setText(
          getString(R.string.ism_mute_unmute_audio, getString(R.string.ism_unmute), userName));
    } else {
      tvRemoteAudio.setText(
          getString(R.string.ism_mute_unmute_audio, getString(R.string.ism_mute), userName));
    }

    ivRemoteVideo.setSelected(remoteVideoMuted);

    if (remoteVideoMuted) {
      tvRemoteVideo.setText(
          getString(R.string.ism_mute_unmute_video, getString(R.string.ism_unmute), userName));
    } else {
      tvRemoteVideo.setText(
          getString(R.string.ism_mute_unmute_video, getString(R.string.ism_mute), userName));
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

  @OnClick(R2.id.rlRemoteVideo)
  public void toggleRemoteUsersVideoState() {

    remoteVideoMuted = !remoteVideoMuted;
    ivRemoteVideo.setSelected(remoteVideoMuted);

    if (remoteVideoMuted) {
      tvRemoteVideo.setText(
          getString(R.string.ism_mute_unmute_video, getString(R.string.ism_unmute), userName));
    } else {
      tvRemoteVideo.setText(
          getString(R.string.ism_mute_unmute_video, getString(R.string.ism_mute), userName));
    }

    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .muteRemoteVideo(uid, remoteVideoMuted);

    if (actionCallback != null) {
      actionCallback.onRemoteUserMediaSettingsUpdated(streamId, userId, uid, userName, false,
          remoteVideoMuted);
    }
  }

  @OnClick(R2.id.rlKickOut)
  public void kickOutRemoteUser() {
    if (actionCallback != null) actionCallback.removeMemberRequested(streamId, userId);
  }

  @OnClick(R2.id.rlRemoteAudio)
  public void toggleRemoteUsersAudioState() {

    remoteAudioMuted = !remoteAudioMuted;
    ivRemoteAudio.setSelected(remoteAudioMuted);

    if (remoteAudioMuted) {
      tvRemoteAudio.setText(
          getString(R.string.ism_mute_unmute_audio, getString(R.string.ism_unmute), userName));
    } else {
      tvRemoteAudio.setText(
          getString(R.string.ism_mute_unmute_audio, getString(R.string.ism_mute), userName));
    }

    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .muteRemoteAudio(uid, remoteAudioMuted);
    if (actionCallback != null) {
      actionCallback.onRemoteUserMediaSettingsUpdated(streamId, userId, uid, userName, true,
          remoteAudioMuted);
    }
  }

  public void updateParameters(String streamId, boolean isAdmin, String userId, int uid,
      String userName, boolean remoteAudioMuted, boolean remoteVideoMuted) {
    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.remoteAudioMuted = remoteAudioMuted;
    this.remoteVideoMuted = remoteVideoMuted;
    this.uid = uid;
    this.userName = userName;
    this.userId = userId;
  }
}