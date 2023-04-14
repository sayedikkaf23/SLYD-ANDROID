package io.isometrik.android.webrtc.apprtc;

import io.isometrik.android.BuildConfig;

public class Constants {
  private static String applicationId = "com.ezcall.android";
  public static final String EXTRA_ROOMID = applicationId+".ROOMID";
  public static final String EXTRA_URLPARAMETERS = applicationId+".URLPARAMETERS";
  public static final String EXTRA_LOOPBACK =applicationId+".LOOPBACK";
  public static final String EXTRA_VIDEO_CALL = applicationId+".VIDEO_CALL";
  public static final String EXTRA_SCREENCAPTURE = applicationId+".SCREENCAPTURE";
  public static final String EXTRA_CAMERA2 = applicationId+".CAMERA2";
  public static final String EXTRA_VIDEO_WIDTH = applicationId+".VIDEO_WIDTH";
  public static final String EXTRA_VIDEO_HEIGHT = applicationId+".VIDEO_HEIGHT";
  public static final String EXTRA_VIDEO_FPS =applicationId+".VIDEO_FPS";
  public static final String EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED =
      applicationId+".VIDEO_CAPTUREQUALITYSLIDER";
  public static final String EXTRA_VIDEO_BITRATE = applicationId+".VIDEO_BITRATE";
  public static final String EXTRA_VIDEOCODEC = applicationId+".VIDEOCODEC";
  public static final String EXTRA_HWCODEC_ENABLED = applicationId+".HWCODEC";
  public static final String EXTRA_CAPTURETOTEXTURE_ENABLED = applicationId+".CAPTURETOTEXTURE";
  public static final String EXTRA_FLEXFEC_ENABLED =applicationId+".FLEXFEC";
  public static final String EXTRA_AUDIO_BITRATE =applicationId+".AUDIO_BITRATE";
  public static final String EXTRA_AUDIOCODEC =applicationId+".AUDIOCODEC";
  public static final String EXTRA_NOAUDIOPROCESSING_ENABLED =
      applicationId+".NOAUDIOPROCESSING";
  public static final String EXTRA_AECDUMP_ENABLED = applicationId+".AECDUMP";
  public static final String EXTRA_SAVE_INPUT_AUDIO_TO_FILE_ENABLED =
      applicationId+".SAVE_INPUT_AUDIO_TO_FILE";
  public static final String EXTRA_OPENSLES_ENABLED = applicationId+".OPENSLES";
  public static final String EXTRA_DISABLE_BUILT_IN_AEC = applicationId+".DISABLE_BUILT_IN_AEC";
  public static final String EXTRA_DISABLE_BUILT_IN_AGC = applicationId+".DISABLE_BUILT_IN_AGC";
  public static final String EXTRA_DISABLE_BUILT_IN_NS =applicationId+".DISABLE_BUILT_IN_NS";
  public static final String EXTRA_DISABLE_WEBRTC_AGC_AND_HPF =
      applicationId+".DISABLE_WEBRTC_GAIN_CONTROL";
  public static final String EXTRA_DISPLAY_HUD =applicationId+".DISPLAY_HUD";
  public static final String EXTRA_TRACING =applicationId+".TRACING";
  public static final String EXTRA_CMDLINE = applicationId+".CMDLINE";
  public static final String EXTRA_RUNTIME =applicationId+".RUNTIME";
  public static final String EXTRA_VIDEO_FILE_AS_CAMERA = applicationId+".VIDEO_FILE_AS_CAMERA";
  public static final String EXTRA_SAVE_REMOTE_VIDEO_TO_FILE =
      applicationId+".SAVE_REMOTE_VIDEO_TO_FILE";
  public static final String EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH =
      applicationId+".SAVE_REMOTE_VIDEO_TO_FILE_WIDTH";
  public static final String EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT =
      applicationId+".SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT";
  public static final String EXTRA_USE_VALUES_FROM_INTENT =
      applicationId+".USE_VALUES_FROM_INTENT";
  public static final String EXTRA_DATA_CHANNEL_ENABLED = applicationId+".DATA_CHANNEL_ENABLED";
  public static final String EXTRA_ORDERED = applicationId+".ORDERED";
  public static final String EXTRA_MAX_RETRANSMITS_MS = applicationId+".MAX_RETRANSMITS_MS";
  public static final String EXTRA_MAX_RETRANSMITS = applicationId+".MAX_RETRANSMITS";
  public static final String EXTRA_PROTOCOL = applicationId+".PROTOCOL";
  public static final String EXTRA_NEGOTIATED =applicationId+".NEGOTIATED";
  public static final String EXTRA_ID =applicationId+".ID";
  public static final String EXTRA_ENABLE_RTCEVENTLOG = applicationId+".ENABLE_RTCEVENTLOG";
  public static final String EXTRA_USE_LEGACY_AUDIO_DEVICE =
      applicationId+".USE_LEGACY_AUDIO_DEVICE";
  public static final int CAPTURE_PERMISSION_REQUEST_CODE = 1;

  // List of mandatory application permissions.
  public static final String[] MANDATORY_PERMISSIONS = {"android.permission.MODIFY_AUDIO_SETTINGS",
      "android.permission.RECORD_AUDIO", "android.permission.INTERNET"};

  // Peer connection statistics callback period in ms.
  public static final int STAT_CALLBACK_PERIOD = 1000;
}
