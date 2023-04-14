package io.isometrik.gs.rtcengine.rtc;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.view.SurfaceView;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.user.FetchUserDetailsQuery;
import io.isometrik.gs.rtcengine.ar.CameraGrabber;
import io.isometrik.gs.rtcengine.ar.CameraGrabberListener;
import io.isometrik.gs.rtcengine.ar.DeepARRenderer;
import io.isometrik.gs.rtcengine.utils.NetworkQualityTextEnum;
import io.isometrik.gs.rtcengine.utils.PreviewVideoGridContainer;
import io.isometrik.gs.rtcengine.utils.UserFeedClickedCallback;
import io.isometrik.gs.rtcengine.utils.UserIdGenerator;
import io.isometrik.gs.rtcengine.utils.VideoEncoderSelector;
import io.isometrik.gs.rtcengine.utils.VideoGridContainer;
import io.isometrik.gs.rtcengine.utils.VideoHelper;
import io.isometrik.gs.rtcengine.voice.VoiceChangerEnum;
import io.isometrik.gs.rtcengine.voice.VoiceReverberationEnum;
import java.util.Map;

import static io.isometrik.gs.rtcengine.utils.Constants.GROUP_CALL_MODE;

/**
 * The class to start or stop broadcast operations in a stream group.
 *
 * @see VoiceChangerEnum
 * @see VoiceReverberationEnum
 */
public class BroadcastOperations implements RemoteMediaStateEventsHandler {

  private RtcEngine mRtcEngine;
  private Context context;
  private IMConfiguration imConfiguration;
  private Isometrik isometrik;
  private CameraGrabber cameraGrabber;
  private VideoGridContainer videoGridContainer;
  private GLSurfaceView surface;
  private boolean networkQualityIndicatorEnabled;
  private PreviewVideoGridContainer previewVideoGridContainer;

  /**
   * Instantiates a new BroadcastOperations instance.
   *
   * @param mRtcEngine the RTC engine instance
   * @param context the android context
   * @param imConfiguration the isometrik configuration instance
   * @param isometrik the isometrik instance
   * @see io.agora.rtc.RtcEngine
   * @see android.content.Context
   * @see io.isometrik.gs.IMConfiguration
   * @see io.isometrik.gs.Isometrik
   */
  public BroadcastOperations(RtcEngine mRtcEngine, Context context, IMConfiguration imConfiguration,
      Isometrik isometrik) {
    this.mRtcEngine = mRtcEngine;
    this.context = context;
    this.imConfiguration = imConfiguration;
    this.isometrik = isometrik;

    VideoEncoderConfiguration configuration =
        new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x480,
            VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
            VideoEncoderConfiguration.STANDARD_BITRATE,
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
    configuration.mirrorMode =
        io.isometrik.gs.rtcengine.utils.Constants.VIDEO_MIRROR_MODES[imConfiguration.getMirrorEncodeIndex()];
    this.mRtcEngine.setVideoEncoderConfiguration(configuration);

    this.mRtcEngine.setExternalVideoSource(true, true, true);

    isometrik.registerRemoteMediaEventHandler(this);
  }

  /**
   * Set the role of the member in a stream group for a given user as either Broadcaster or Audience
   */
  private void setClientRole(int role) {

    mRtcEngine.setClientRole(role);
  }

  /**
   * Start video publishing in a stream group
   *
   * @param userId id of the user who is starting the broadcast ina stream group
   * @param mVideoGridContainer UI container to add the local user video in split screen
   * @param userName the name of the local user to be added over the video feed
   */
  private void startBroadcast(String userId, VideoGridContainer mVideoGridContainer,
      String userName, UserFeedClickedCallback userFeedClickedCallback) {
    mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
    int uid = UserIdGenerator.getUid(userId);

    if (isometrik.isARFiltersEnabled()) {

      cameraGrabber = new CameraGrabber();
      cameraGrabber.initCamera(new CameraGrabberListener() {
        @Override
        public void onCameraInitialized() {

          cameraGrabber.setFrameReceiver(isometrik.getAREngine());
          cameraGrabber.startPreview();
        }

        @Override
        public void onCameraError(String errorMsg) {

        }
      });

      surface = new GLSurfaceView(context);
      surface.setEGLContextClientVersion(2);
      surface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
      DeepARRenderer renderer = new DeepARRenderer(isometrik.getAREngine(), mRtcEngine);
      renderer.setStreamingInProgress(true);
      surface.setEGLContextFactory(new DeepARRenderer.MyContextFactory(renderer));

      surface.setRenderer(renderer);
      surface.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
      mVideoGridContainer.addUserVideoSurface(uid, surface, true, userName,
          networkQualityIndicatorEnabled, userFeedClickedCallback);
    } else {
      SurfaceView surface =
          VideoHelper.prepareRtcVideo(uid, true, context, mRtcEngine, imConfiguration);
      mVideoGridContainer.addUserVideoSurface(uid, surface, true, userName,
          networkQualityIndicatorEnabled, userFeedClickedCallback);
    }
    VideoEncoderConfiguration videoEncoderConfiguration =
        new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x480,
            VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
            VideoEncoderConfiguration.STANDARD_BITRATE,
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);

    videoEncoderConfiguration.mirrorMode =
        io.isometrik.gs.rtcengine.utils.Constants.VIDEO_MIRROR_MODES[imConfiguration.getMirrorEncodeIndex()];

    mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);
  }

  /**
   * Stop video publishing in a stream group
   *
   * @param userId id of the user whose video is to be removed
   * @param mVideoGridContainer UI container from which to remove the local video from split screen
   */
  private void stopBroadcast(String userId, VideoGridContainer mVideoGridContainer) {
    mRtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);

    int uid = UserIdGenerator.getUid(userId);
    VideoHelper.removeRtcVideo(uid, true, mRtcEngine);
    mVideoGridContainer.removeUserVideo(uid, true);
  }

  /**
   * Switch camera to toggle camera between the front and rear camera.
   */
  public void switchCamera() {
    if (isometrik.isARFiltersEnabled()) {
      if (cameraGrabber != null) {
        int cameraDevice =
            cameraGrabber.getCurrCameraDevice() == Camera.CameraInfo.CAMERA_FACING_FRONT
                ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        cameraGrabber.changeCameraDevice(cameraDevice);
      }
    } else {
      mRtcEngine.switchCamera();
    }
  }

  /**
   * Render remote user video feed.
   *
   * @param uid the uid of the remote user who video stream is to be added to UI
   * @param mVideoGridContainer UI container in which to add the remote video in split screen
   * @param members the map of members of the stream group containing mapping of memberId to
   * memberName
   */
  public void renderRemoteUser(int uid, VideoGridContainer mVideoGridContainer,
      Map<String, String> members, UserFeedClickedCallback userFeedClickedCallback) {

    SurfaceView surface =
        VideoHelper.prepareRtcVideo(uid, false, context, mRtcEngine, imConfiguration);
    mVideoGridContainer.addUserVideoSurface(uid, surface, false, null,
        networkQualityIndicatorEnabled, userFeedClickedCallback);

    for (Map.Entry<String, String> entry : members.entrySet()) {

      if (UserIdGenerator.getUid(entry.getKey()) == uid) {

        if (!entry.getValue().isEmpty()) {

          mVideoGridContainer.addUserName(uid, entry.getValue());
        } else {

          isometrik.fetchUserDetails(
              new FetchUserDetailsQuery.Builder().setUserId(entry.getKey()).build(),
              (var1, var2) -> {

                if (var1 != null) {

                  mVideoGridContainer.addUserName(uid, var1.getUserName());
                }
              });
        }
        break;
      }
    }

    if (videoGridContainer != null) {

      updateVideoEncoder(-1, videoGridContainer.getNumberOfPublishers());
    }
    //To unmute remote media when user join
    muteRemoteAudio(uid, false);
    muteRemoteVideo(uid, false);
  }

  /**
   * Remove remote user video feed.
   *
   * @param uid the uid of the remote user whose video is to be removed from the UI
   * @param mVideoGridContainer UI container from which to remove the remote video from split screen
   */
  public void removeRemoteUser(int uid, VideoGridContainer mVideoGridContainer) {

    VideoHelper.removeRtcVideo(uid, false, mRtcEngine);
    mVideoGridContainer.removeUserVideo(uid, false);
    if (videoGridContainer != null) {

      updateVideoEncoder(-1, videoGridContainer.getNumberOfPublishers());
    }
    //To unmute remote media when user leaves
    muteRemoteAudio(uid, false);
    muteRemoteVideo(uid, false);
  }

  /**
   * Join broadcast.
   *
   * @param streamId the stream id
   * @param userId the user id
   * @param token the token
   * @param role the role whether to join broadcast as publisher or as audience
   * @param mVideoGridContainer the UI container to add the video feed
   * @param startBroadcast whether to start broadcasting or join just as audience
   * @param userName the user name
   */
  public void joinBroadcast(String streamId, String userId, String token, int role,
      VideoGridContainer mVideoGridContainer, boolean startBroadcast, String userName,
      UserFeedClickedCallback userFeedClickedCallback) {
    setClientRole(role);
    // Initialize token, extra info here before joining channel
    // 1. Users can only see each other after they join the
    // same channel successfully using the same app id.
    // 2. One token is only valid for the channel name and uid that
    // you use to generate this token.
    // default, no token
    this.videoGridContainer = mVideoGridContainer;
    videoGridContainer.setStatsManager(isometrik.getIsometrikRtcStatsManager(),
        isometrik.getConfiguration().isEnableDetailedRtcStats());
    isometrik.getIsometrikRtcEngineEventHandler().setSelfUid(UserIdGenerator.getUid(userId));
    if (isometrik.isARFiltersEnabled()) {

      if (startBroadcast) {
        startBroadcast(userId, mVideoGridContainer, userName, userFeedClickedCallback);
      }
      mRtcEngine.joinChannel(token, streamId, "", UserIdGenerator.getUid(userId));
    } else {

      mRtcEngine.joinChannel(token, streamId, "", UserIdGenerator.getUid(userId));
      if (startBroadcast) {
        startBroadcast(userId, mVideoGridContainer, userName, userFeedClickedCallback);
      }
    }



    /*
     * Unmute remote media stream on joining a channel
     */
    muteAllRemoteAudio(false);
    muteAllRemoteVideo(false);
  }

  /**
   * Update name over remote user's video feed.
   *
   * @param members the map of members of the stream group containing mapping of memberId to
   * memberName
   * @param userId the id of the self user
   * @param mVideoGridContainer UI container in which to add the remote video in split screen
   */
  public void updateRemoteUserNames(Map<String, String> members, String userId,
      VideoGridContainer mVideoGridContainer) {

    int selfUid = UserIdGenerator.getUid(userId);
    for (Map.Entry<String, String> entry : members.entrySet()) {

      int uid = UserIdGenerator.getUid(entry.getKey());

      if (selfUid != uid) {

        mVideoGridContainer.addUserName(uid, entry.getValue());
      }
    }
  }

  /**
   * Mute all remote audio.
   *
   * @param mute whether to mute or un-mute all remote audio streams
   */
  public void muteAllRemoteAudio(boolean mute) {

    mRtcEngine.muteAllRemoteAudioStreams(mute);
  }

  /**
   * Mute all remote video.
   *
   * @param mute whether to mute or un-mute all remote video streams
   */
  public void muteAllRemoteVideo(boolean mute) {

    mRtcEngine.muteAllRemoteVideoStreams(mute);
  }

  /**
   * Mute remote audio.
   *
   * @param uid the uid of the user whose audio is to be muted
   * @param mute whether to mute or un-mute the remote user's audio
   */
  public void muteRemoteAudio(int uid, boolean mute) {
    mRtcEngine.muteRemoteAudioStream(uid, mute);
  }

  /**
   * Mute remote video.
   *
   * @param uid the uid of the user whose video is to be muted
   * @param mute whether to mute or un-mute the remote user's video
   */
  public void muteRemoteVideo(int uid, boolean mute) {
    mRtcEngine.muteRemoteVideoStream(uid, mute);
  }

  /**
   * Mute local video.
   *
   * @param mute whether to mute or un-mute the local user's video
   */
  public void muteLocalVideo(boolean mute) {

    mRtcEngine.muteLocalVideoStream(mute);
  }

  /**
   * Mute local audio.
   *
   * @param mute whether to mute or un-mute the local user's audio
   */
  public void muteLocalAudio(boolean mute) {

    mRtcEngine.muteLocalAudioStream(mute);
  }

  /**
   * Leave broadcast and clear all AR filters if any applied to its back to neutral state in case
   * user creates new stream groups.
   *
   * @param userId the user id who has to stop broadcasting or exit from audience depending on his
   * role
   * @param videoGridContainer the UI container from which to remove the user's feed
   * @param stopBroadcast whether to stop publishing if user was publishing or simply exit from
   * audience in case user was just a viewer
   */
  public void leaveBroadcast(String userId, VideoGridContainer videoGridContainer,
      boolean stopBroadcast) {

    if (stopBroadcast) {
      stopBroadcast(userId, videoGridContainer);

      if (cameraGrabber != null) {

        cameraGrabber.setFrameReceiver(null);

        cameraGrabber.stopPreview();

        cameraGrabber.releaseCamera();
        cameraGrabber = null;
      }
    }
    mRtcEngine.leaveChannel();
    if (this.videoGridContainer != null) {
      this.videoGridContainer.setStatsManager(null, false);
    }
    this.videoGridContainer = null;
    surface = null;
    networkQualityIndicatorEnabled = false;
    isometrik.getIsometrikRtcStatsManager().enableStats(false);
    isometrik.getIsometrikRtcStatsManager().clearAllData();

    if (isometrik.isARFiltersEnabled()) {

      isometrik.getAROperations().applyFilter("masks", "none");
      isometrik.getAROperations().applyFilter("effects", "none");
      isometrik.getAROperations().applyFilter("filters", "none");

      isometrik.getAREngine().setRenderSurface(null, -1, -1);
    }
    applyBeautifyOptions(false);
  }

  @Override
  public void onRemoteAudioStateChanged(int uid, int state, int reason, int elapsed) {
    if (state == 0 && reason == 5) {
      if (videoGridContainer != null) {
        videoGridContainer.updateAudioState(uid, true);
      } else if (previewVideoGridContainer != null) {
        previewVideoGridContainer.updateAudioState(uid, true);
      }
    } else {
      if (state == 2 && reason == 6) {
        if (videoGridContainer != null) {
          videoGridContainer.updateAudioState(uid, false);
        } else if (previewVideoGridContainer != null) {
          previewVideoGridContainer.updateAudioState(uid, false);
        }
      }
    }
  }

  @Override
  public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {
    if (state == 0 && reason == 5) {
      if (videoGridContainer != null) {
        videoGridContainer.updateVideoState(uid, true);
      } else if (previewVideoGridContainer != null) {
        previewVideoGridContainer.updateVideoState(uid, true);
      }
    } else {
      if (state == 2 && reason == 6) {
        if (videoGridContainer != null) {
          videoGridContainer.updateVideoState(uid, false);
        } else if (previewVideoGridContainer != null) {
          previewVideoGridContainer.updateVideoState(uid, false);
        }
      }
    }
  }

  /**
   * To enable or disable beautify effects
   *
   * @param enable whether to enable or disable the beautify effect
   */
  public void applyBeautifyOptions(boolean enable) {
    if (isometrik.isARFiltersEnabled()) {

      if (enable) {
        //To allow beautify effect to be applied even without downloading filters

        //if (FiltersConfig.isDownloadRequired()) {
        //  isometrik.getAROperations()
        //      .applyFilter("beautify", FiltersConfig.DEEPAR_DIRECTORY + "deepar/beauty");
        //} else {
        isometrik.getAROperations().applyFilter("beautify", "file:///android_asset/deepar/beauty");
        //}
      } else {

        isometrik.getAROperations().applyFilter("beautify", "none");
      }
    } else {
      mRtcEngine.setBeautyEffectOptions(enable, new BeautyOptions());
    }
  }

  @Override
  public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
    if (videoGridContainer != null) {

      int userUid;
      if (uid == 0) {

        updateVideoEncoder(txQuality, videoGridContainer.getNumberOfPublishers());
        userUid = UserIdGenerator.getUid(isometrik.getConfiguration().getClientId());
      } else {

        userUid = uid;
      }

      if (networkQualityIndicatorEnabled) {
        String tx;
        String rx;

        int txNetworkQualityColor;
        int rxNetworkQualityColor;

        switch (txQuality) {

          case 0:
            tx = NetworkQualityTextEnum.UNKNOWN.getValue();
            txNetworkQualityColor = Color.GRAY;
            break;
          case 1:
            tx = NetworkQualityTextEnum.EXCELLENT.getValue();
            txNetworkQualityColor = Color.GREEN;
            break;
          case 2:
            tx = NetworkQualityTextEnum.GOOD.getValue();
            txNetworkQualityColor = Color.GREEN;
            break;
          case 3:
            tx = NetworkQualityTextEnum.POOR.getValue();
            txNetworkQualityColor = Color.RED;
            break;
          case 4:
            tx = NetworkQualityTextEnum.BAD.getValue();
            txNetworkQualityColor = Color.RED;
            break;
          case 5:
            tx = NetworkQualityTextEnum.VBAD.getValue();
            txNetworkQualityColor = Color.RED;
            break;
          case 6:
            tx = NetworkQualityTextEnum.DOWN.getValue();
            txNetworkQualityColor = Color.RED;
            break;
          default:
            tx = NetworkQualityTextEnum.DETECTING.getValue();
            txNetworkQualityColor = Color.GRAY;
            break;
        }

        switch (rxQuality) {
          case 0:
            rx = NetworkQualityTextEnum.UNKNOWN.getValue();
            rxNetworkQualityColor = Color.GRAY;
            break;
          case 1:
            rx = NetworkQualityTextEnum.EXCELLENT.getValue();
            rxNetworkQualityColor = Color.GREEN;
            break;
          case 2:
            rx = NetworkQualityTextEnum.GOOD.getValue();
            rxNetworkQualityColor = Color.GREEN;
            break;
          case 3:
            rx = NetworkQualityTextEnum.POOR.getValue();
            rxNetworkQualityColor = Color.RED;
            break;
          case 4:
            rx = NetworkQualityTextEnum.BAD.getValue();
            rxNetworkQualityColor = Color.RED;
            break;
          case 5:
            rx = NetworkQualityTextEnum.VBAD.getValue();
            rxNetworkQualityColor = Color.RED;
            break;
          case 6:
            rx = NetworkQualityTextEnum.DOWN.getValue();
            rxNetworkQualityColor = Color.RED;
            break;
          default:
            rx = NetworkQualityTextEnum.DETECTING.getValue();
            rxNetworkQualityColor = Color.GRAY;
            break;
        }
        videoGridContainer.updateNetworkQualityIndicator(userUid, tx, rx, txNetworkQualityColor,
            rxNetworkQualityColor);
      }
    }
  }

  private void updateVideoEncoder(int txQuality, int numberOfPublishers) {
    if (GROUP_CALL_MODE) {
      //For streaming UI like a group call
      if (isometrik.isARFiltersEnabled()) {

        if (numberOfPublishers == 2) {

          VideoEncoderConfiguration videoEncoderConfiguration =
              VideoEncoderSelector.getSquareVideoEncoderBitrateBasedOnTransmissionQuality(
                  txQuality);
          if (videoEncoderConfiguration != null) {
            videoEncoderConfiguration.mirrorMode =
                io.isometrik.gs.rtcengine.utils.Constants.VIDEO_MIRROR_MODES[imConfiguration.getMirrorEncodeIndex()];

            mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);
          }
        } else {
          VideoEncoderConfiguration videoEncoderConfiguration =
              VideoEncoderSelector.getPortraitVideoEncoderBitrateBasedOnTransmissionQuality(
                  txQuality);
          if (videoEncoderConfiguration != null) {
            videoEncoderConfiguration.mirrorMode =
                io.isometrik.gs.rtcengine.utils.Constants.VIDEO_MIRROR_MODES[imConfiguration.getMirrorEncodeIndex()];

            mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);
          }
        }
      } else {

        VideoEncoderConfiguration videoEncoderConfiguration =
            VideoEncoderSelector.getPortraitVideoEncoderBitrateBasedOnTransmissionQuality(
                txQuality);
        if (videoEncoderConfiguration != null) {
          videoEncoderConfiguration.mirrorMode =
              io.isometrik.gs.rtcengine.utils.Constants.VIDEO_MIRROR_MODES[imConfiguration.getMirrorEncodeIndex()];

          mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);
        }
      }
    } else {
      //For streaming UI like a multi-live

      VideoEncoderConfiguration videoEncoderConfiguration =
          VideoEncoderSelector.getPortraitVideoEncoderBitrateResolutionBasedOnTransmissionQuality(
              txQuality, numberOfPublishers);
      if (videoEncoderConfiguration != null) {
        videoEncoderConfiguration.mirrorMode =
            io.isometrik.gs.rtcengine.utils.Constants.VIDEO_MIRROR_MODES[imConfiguration.getMirrorEncodeIndex()];

        mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);
      }
    }
  }

  public void togglePreview(boolean connected) {
    if (isometrik.isARFiltersEnabled()) {

      if (surface != null) {
        if (connected) {
          surface.onResume();
        } else {
          surface.onPause();
        }
      }
    }
  }

  public void setNetworkQualityIndicatorEnabled(boolean networkQualityIndicatorEnabled) {
    this.networkQualityIndicatorEnabled = networkQualityIndicatorEnabled;
    if (videoGridContainer != null) {
      videoGridContainer.toggleNetworkQualityIndicator(networkQualityIndicatorEnabled);
    }
    isometrik.getIsometrikRtcStatsManager().enableStats(networkQualityIndicatorEnabled);
  }

  public void switchRole(String userId, VideoGridContainer mVideoGridContainer, String userName,
      String rtcToken, UserFeedClickedCallback userFeedClickedCallback) {
    startBroadcast(userId, mVideoGridContainer, userName, userFeedClickedCallback);
    //mRtcEngine.renewToken(rtcToken);
  }

  /**
   * To switch preview of remote broadcast on scroll of list
   *
   * @param streamId id of the broadcast for which to switch preview
   * @param rtcToken token to be use for authentication for joining a stream preview
   */

  public void switchPreviewBroadcast(String streamId, String rtcToken) {

    mRtcEngine.switchChannel(rtcToken, streamId);
  }

  /**
   * Updates remote media settings on join/leave of a remote broadcast preview
   *
   * @param previewJoined whether remote media settings are to be updated on join or leave of
   * broadcast
   */
  public void updatePreviewRemoteMediaSettings(boolean previewJoined) {

    muteAllRemoteVideo(!previewJoined);
    muteAllRemoteAudio(!previewJoined);
  }

  public void joinPreviewBroadcast(String streamId, String userId, String rtcToken,
      PreviewVideoGridContainer previewVideoGridContainer) {
    this.previewVideoGridContainer = previewVideoGridContainer;
    mRtcEngine.joinChannel(rtcToken, streamId, "", UserIdGenerator.getUid(userId));
  }

  public void leavePreviewBroadcast() {
    previewVideoGridContainer = null;
    mRtcEngine.leaveChannel();
  }

  /**
   * Render remote user video feed.
   *
   * @param uid the uid of the remote user who video stream is to be added to UI
   * @param mVideoGridContainer UI container in which to add the remote video in split screen
   */
  public void renderPreviewRemoteUser(int uid, PreviewVideoGridContainer mVideoGridContainer) {

    SurfaceView surface =
        VideoHelper.prepareRtcVideo(uid, false, context, mRtcEngine, imConfiguration);
    mVideoGridContainer.addUserVideoSurface(uid, surface, false);
  }

  /**
   * Remove remote user video feed.
   *
   * @param uid the uid of the remote user whose video is to be removed from the UI
   * @param mVideoGridContainer UI container from which to remove the remote video from split screen
   */
  public void removePreviewRemoteUser(int uid, PreviewVideoGridContainer mVideoGridContainer) {

    VideoHelper.removeRtcVideo(uid, false, mRtcEngine);
    mVideoGridContainer.removeUserVideo(uid, false);
  }
}