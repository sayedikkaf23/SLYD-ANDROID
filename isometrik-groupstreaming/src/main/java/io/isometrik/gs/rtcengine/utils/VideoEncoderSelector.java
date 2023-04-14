package io.isometrik.gs.rtcengine.utils;

import io.agora.rtc.video.VideoEncoderConfiguration;

public class VideoEncoderSelector {

  private static int lastKnownNetworkQuality;

  private static VideoEncoderConfiguration.VideoDimensions videoDimensions =
      VideoEncoderConfiguration.VD_640x480;

  public static VideoEncoderConfiguration getPortraitVideoEncoderBitrateBasedOnTransmissionQuality(
      int txQuality) {

    if (txQuality != lastKnownNetworkQuality) {

      if (txQuality != -1) lastKnownNetworkQuality = txQuality;
      int bitrate;
      switch (txQuality) {
        case 1:
          //1000kbps
          bitrate = 1000;
          //VideoEncoderConfiguration.VD_640x480
          break;
        case 2:
          //800kbps
          bitrate = 800;
          //VideoEncoderConfiguration.VD_640x360
          break;
        case 3:
          //640kbps
          bitrate = 640;
          //VideoEncoderConfiguration.VD_480x360
          break;
        case 4:
          //400kbps
          bitrate = 400;
          //VideoEncoderConfiguration.VD_320x240
          break;
        case 5:
          //240kbps
          bitrate = 240;
          //VideoEncoderConfiguration.VD_240x180
          break;
        case 6:
          //130kbps
          bitrate = 130;
          //VideoEncoderConfiguration.VD_160x120
          break;
        default:
          //1000kbps
          bitrate = 1000;
          //VideoEncoderConfiguration.VD_640x480
      }
      videoDimensions = VideoEncoderConfiguration.VD_640x480;
      return new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x480,
          VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15, bitrate,
          VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
    }

    return null;
  }

  public static VideoEncoderConfiguration getSquareVideoEncoderBitrateBasedOnTransmissionQuality(
      int txQuality) {

    if (txQuality != lastKnownNetworkQuality) {
      if (txQuality != -1) lastKnownNetworkQuality = txQuality;
      int bitrate;
      switch (txQuality) {
        case 1:
          //800kbps
          bitrate = 800;
          //VideoEncoderConfiguration.VD_480x480
          break;
        case 2:

          //520kbps
          bitrate = 520;
          //VideoEncoderConfiguration.VD_360x360
          break;
        case 3:

          //280kbps
          bitrate = 280;
          //VideoEncoderConfiguration.VD_240x240
          break;
        case 4:

          //200kbps
          bitrate = 200;
          //VideoEncoderConfiguration.VD_180x180
          break;
        case 5:

        case 6:

          //100kbps
          bitrate = 100;
          //VideoEncoderConfiguration.VD_120x120
          break;
        default:
          //800kbps
          bitrate = 800;
          //VideoEncoderConfiguration.VD_480x480
      }
      videoDimensions = VideoEncoderConfiguration.VD_480x480;
      return new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_480x480,
          VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15, bitrate,
          VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
    }

    return null;
  }

  public static VideoEncoderConfiguration getPortraitVideoEncoderResolutionBasedOnTransmissionQuality(
      int txQuality) {

    if (txQuality != lastKnownNetworkQuality) {

      if (txQuality != -1) lastKnownNetworkQuality = txQuality;
      VideoEncoderConfiguration.VideoDimensions videoDimensions;
      switch (txQuality) {
        case 1:
          //1000kbps
          videoDimensions = VideoEncoderConfiguration.VD_640x480;
          break;
        case 2:
          //800kbps
          videoDimensions = VideoEncoderConfiguration.VD_640x360;
          break;
        case 3:
          //640kbps
          videoDimensions = VideoEncoderConfiguration.VD_480x360;
          break;
        case 4:
          //400kbps
          videoDimensions = VideoEncoderConfiguration.VD_320x240;
          break;
        case 5:
          //240kbps
          videoDimensions = VideoEncoderConfiguration.VD_240x180;
          break;
        case 6:
          //130kbps
          videoDimensions = VideoEncoderConfiguration.VD_160x120;
          break;
        default:
          //1000kbps
          videoDimensions = VideoEncoderConfiguration.VD_640x480;
      }
      VideoEncoderSelector.videoDimensions = videoDimensions;
      return new VideoEncoderConfiguration(videoDimensions,
          VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
          VideoEncoderConfiguration.STANDARD_BITRATE,
          VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
    }

    return null;
  }

  public static VideoEncoderConfiguration getSquareVideoEncoderResolutionBasedOnTransmissionQuality(
      int txQuality) {

    if (txQuality != lastKnownNetworkQuality) {
      if (txQuality != -1) lastKnownNetworkQuality = txQuality;
      VideoEncoderConfiguration.VideoDimensions videoDimensions;

      switch (txQuality) {
        case 1:
          //800kbps
          videoDimensions = VideoEncoderConfiguration.VD_480x480;
          break;
        case 2:

          //520kbps
          videoDimensions = VideoEncoderConfiguration.VD_360x360;
          break;
        case 3:

          //280kbps
          videoDimensions = VideoEncoderConfiguration.VD_240x240;
          break;
        case 4:

          //200kbps
          videoDimensions = VideoEncoderConfiguration.VD_180x180;
          break;
        case 5:

        case 6:

          //100kbps
          videoDimensions = VideoEncoderConfiguration.VD_120x120;
          break;
        default:
          //800kbps
          videoDimensions = VideoEncoderConfiguration.VD_480x480;
      }
      VideoEncoderSelector.videoDimensions = videoDimensions;
      return new VideoEncoderConfiguration(videoDimensions,
          VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
          VideoEncoderConfiguration.STANDARD_BITRATE,
          VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
    }

    return null;
  }

  public static VideoEncoderConfiguration getPortraitVideoEncoderBitrateResolutionBasedOnTransmissionQuality(
      int txQuality, int numberOfPublishers) {

    if (txQuality != lastKnownNetworkQuality) {

      if (txQuality != -1) lastKnownNetworkQuality = txQuality;
      int bitrate;
      switch (txQuality) {
        case 1:
          //1000kbps
          bitrate = 1000;
          //VideoEncoderConfiguration.VD_640x480
          break;
        case 2:
          //800kbps
          bitrate = 800;
          //VideoEncoderConfiguration.VD_640x360
          break;
        case 3:
          //640kbps
          bitrate = 640;
          //VideoEncoderConfiguration.VD_480x360
          break;
        case 4:
          //400kbps
          bitrate = 400;
          //VideoEncoderConfiguration.VD_320x240
          break;
        case 5:
          //240kbps
          bitrate = 240;
          //VideoEncoderConfiguration.VD_240x180
          break;
        case 6:
          //130kbps
          bitrate = 130;
          //VideoEncoderConfiguration.VD_160x120
          break;
        default:
          //1000kbps
          bitrate = 1000;
          //VideoEncoderConfiguration.VD_640x480
      }

      switch (numberOfPublishers) {

        case 1:
          videoDimensions = VideoEncoderConfiguration.VD_640x480;
          break;
        case 2:
          videoDimensions = VideoEncoderConfiguration.VD_480x360;
          break;

        case 3:

        case 4:

        case 5:

        case 6:
          videoDimensions = VideoEncoderConfiguration.VD_320x240;
          break;

        default:
          videoDimensions = VideoEncoderConfiguration.VD_640x480;
      }

      return new VideoEncoderConfiguration(videoDimensions,
          VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15, bitrate,
          VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
    }

    return null;
  }

  public static VideoEncoderConfiguration.VideoDimensions getVideoDimensions() {
    return videoDimensions;
  }
}
