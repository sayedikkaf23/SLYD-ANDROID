package io.isometrik.gs.rtcengine.utils;

import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoEncoderConfiguration;

/**
 * The class containing constants.
 */
public class Constants {

  public static final boolean GROUP_CALL_MODE = false;

  private static final int BEAUTY_EFFECT_DEFAULT_CONTRAST =
      BeautyOptions.LIGHTENING_CONTRAST_NORMAL;
  private static final float BEAUTY_EFFECT_DEFAULT_LIGHTNESS = 0.7f;
  private static final float BEAUTY_EFFECT_DEFAULT_SMOOTHNESS = 0.5f;
  private static final float BEAUTY_EFFECT_DEFAULT_REDNESS = 0.1f;

  public static final int CLIENT_ROLE_BROADCASTER = 1;

  public static final int CLIENT_ROLE_AUDIENCE = 2;

  public static final BeautyOptions DEFAULT_BEAUTY_OPTIONS =
      new BeautyOptions(BEAUTY_EFFECT_DEFAULT_CONTRAST, BEAUTY_EFFECT_DEFAULT_LIGHTNESS,
          BEAUTY_EFFECT_DEFAULT_SMOOTHNESS, BEAUTY_EFFECT_DEFAULT_REDNESS);



  /**
   * The constant VIDEO_MIRROR_MODES.
   */
  public static int[] VIDEO_MIRROR_MODES = new int[] {
      io.agora.rtc.Constants.VIDEO_MIRROR_MODE_AUTO,
      io.agora.rtc.Constants.VIDEO_MIRROR_MODE_ENABLED,
      io.agora.rtc.Constants.VIDEO_MIRROR_MODE_DISABLED,
  };


}
