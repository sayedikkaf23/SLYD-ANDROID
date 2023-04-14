package io.isometrik.gs.rtcengine.utils;

import android.content.Context;
import android.view.SurfaceView;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.isometrik.gs.IMConfiguration;

/**
 * The class to setup local or remote video in isometrik RTC engine.
 */
public class VideoHelper {

  /**
   * Prepare rtc video surface view.
   *
   * @param uid the uid
   * @param local the local
   * @param context the context
   * @param rtcEngine the rtc engine
   * @param imConfiguration the im configuration
   * @return the surface view
   */
  public static SurfaceView prepareRtcVideo(int uid, boolean local, Context context,
      RtcEngine rtcEngine, IMConfiguration imConfiguration) {
    SurfaceView surface = RtcEngine.CreateRendererView(context);
    if (local) {

      rtcEngine.setupLocalVideo(new VideoCanvas(surface, (VideoCanvas.RENDER_MODE_HIDDEN), uid,
          Constants.VIDEO_MIRROR_MODES[imConfiguration.getMirrorLocalIndex()]));
    } else {

      rtcEngine.setupRemoteVideo(new VideoCanvas(surface, (VideoCanvas.RENDER_MODE_HIDDEN), uid,
          Constants.VIDEO_MIRROR_MODES[imConfiguration.getMirrorRemoteIndex()]));
    }

    return surface;
  }

  /**
   * Remove rtc video.
   *
   * @param uid the uid
   * @param local the local
   * @param rtcEngine the rtc engine
   */
  public static void removeRtcVideo(int uid, boolean local, RtcEngine rtcEngine) {
    if (local) {
      rtcEngine.setupLocalVideo(null);
    } else {

      rtcEngine.setupRemoteVideo(new VideoCanvas(null, (VideoCanvas.RENDER_MODE_HIDDEN), uid));
    }
  }
}
