package io.isometrik.gs.rtcengine.rtc;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.rtcengine.stats.LocalStatsData;
import io.isometrik.gs.rtcengine.stats.RemoteStatsData;
import io.isometrik.gs.rtcengine.stats.StatsData;
import io.isometrik.gs.rtcengine.utils.VideoEncoderSelector;
import java.util.ArrayList;

/**
 * The class for handling isometrik RTC engine events.
 */
public class IsometrikRtcEngineEventHandler extends IRtcEngineEventHandler {
  private ArrayList<IsometrikRtcEventHandler> mHandler = new ArrayList<>();
  private ArrayList<RemoteMediaStateEventsHandler> remoteMediaStateEventsHandlers =
      new ArrayList<>();
  private Isometrik isometrik;
  private int selfUid;

  public IsometrikRtcEngineEventHandler(Isometrik isometrik) {
    this.isometrik = isometrik;
  }

  void setSelfUid(int uid) {
    this.selfUid = uid;
  }

  /**
   * Add handler.
   *
   * @param handler the handler to be added for listening to RTC events
   * @see io.agora.rtc.IRtcEngineEventHandler
   */
  public void addHandler(IsometrikRtcEventHandler handler) {
    mHandler.add(handler);
  }

  /**
   * Remove handler.
   *
   * @param handler the handler to be removed from listening to RTC events
   * @see io.agora.rtc.IRtcEngineEventHandler
   */
  public void removeHandler(IsometrikRtcEventHandler handler) {
    mHandler.remove(handler);
  }

  @Override
  public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

    for (IsometrikRtcEventHandler handler : mHandler) {
      handler.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
    }
  }
  //@Override
  //public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
  ////for (IsometrikRtcEventHandler handler : mHandler) {
  ////  handler.onJoinChannelSuccess(channel, uid, elapsed);
  ////}
  //
  //  //renderer.setCallInProgress(true);
  //}

  //@Override
  //public void onUserJoined(int uid, int elapsed) {
  //
  //  for (IsometrikRtcEventHandler handler : mHandler) {
  //    handler.onUserJoined(uid, elapsed);
  //  }
  //}

  @Override
  public void onUserOffline(int uid, int reason) {

    for (IsometrikRtcEventHandler handler : mHandler) {
      handler.onUserOffline(uid, reason);
    }
  }

  @Override
  public void onConnectionStateChanged(int state, int reason) {

    for (IsometrikRtcEventHandler handler : mHandler) {

      if (state == 3) {//Connected callback
        handler.onConnectionStateChanged(state, reason, true);
      } else if (state == 1) {
        //Disconnected callback
        handler.onConnectionStateChanged(state, reason, false);
      } else {
        handler.onConnectionStateChanged(state, reason, null);
      }
    }
  }

  @Override
  public void onConnectionLost() {

    for (IsometrikRtcEventHandler handler : mHandler) {
      handler.onConnectionLost();
    }
  }

  /**
   * Add handler for handling remote media state changes.
   *
   * @param handler the handler to be added for listening to remote media state change events
   * @see RemoteMediaStateEventsHandler
   */
  public void addRemoteMediaStateHandler(RemoteMediaStateEventsHandler handler) {
    remoteMediaStateEventsHandlers.add(handler);
  }

  /**
   * Remove handler for handling remote media state changes
   *
   * @param handler the handler to be removed from listening to remote media state change events
   * @see RemoteMediaStateEventsHandler
   */
  public void removeRemoteMediaStateHandler(RemoteMediaStateEventsHandler handler) {
    remoteMediaStateEventsHandlers.remove(handler);
  }

  @Override
  public void onRemoteAudioStateChanged(int uid, int state, int reason, int elapsed) {
    for (RemoteMediaStateEventsHandler handler : remoteMediaStateEventsHandlers) {
      handler.onRemoteAudioStateChanged(uid, state, reason, elapsed);
    }
  }

  @Override
  public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {

    for (RemoteMediaStateEventsHandler handler : remoteMediaStateEventsHandlers) {
      handler.onRemoteVideoStateChanged(uid, state, reason, elapsed);
    }
  }

  @Override
  public void onNetworkQuality(int uid, int txQuality, int rxQuality) {

    for (RemoteMediaStateEventsHandler handler : remoteMediaStateEventsHandlers) {
      handler.onNetworkQuality(uid, txQuality, rxQuality);
    }

    if (!isometrik.getConfiguration().isEnableDetailedRtcStats()) return;
    if (!isometrik.getIsometrikRtcStatsManager().isEnabled()) return;

    if (uid == 0) {
      uid = selfUid;
    }

    StatsData data = isometrik.getIsometrikRtcStatsManager().getStatsData(uid);
    if (data == null) return;

    data.setSendQuality(isometrik.getIsometrikRtcStatsManager().qualityToString(txQuality));
    data.setRecvQuality(isometrik.getIsometrikRtcStatsManager().qualityToString(rxQuality));
  }

  @Override
  public void onLocalVideoStats(IRtcEngineEventHandler.LocalVideoStats stats) {
    if (!isometrik.getConfiguration().isEnableDetailedRtcStats()) return;

    if (!isometrik.getIsometrikRtcStatsManager().isEnabled()) return;

    LocalStatsData data =
        (LocalStatsData) isometrik.getIsometrikRtcStatsManager().getStatsData(selfUid);
    if (data == null) return;

    VideoEncoderConfiguration.VideoDimensions videoDimensions =
        VideoEncoderSelector.getVideoDimensions();
    data.setWidth(videoDimensions.width);
    data.setHeight(videoDimensions.height);
    data.setFramerate(stats.sentFrameRate);
  }

  @Override
  public void onRtcStats(IRtcEngineEventHandler.RtcStats stats) {
    if (!isometrik.getConfiguration().isEnableDetailedRtcStats()) return;
    if (!isometrik.getIsometrikRtcStatsManager().isEnabled()) return;

    LocalStatsData data =
        (LocalStatsData) isometrik.getIsometrikRtcStatsManager().getStatsData(selfUid);
    if (data == null) return;

    data.setLastMileDelay(stats.lastmileDelay);
    data.setVideoSendBitrate(stats.txVideoKBitRate);
    data.setVideoRecvBitrate(stats.rxVideoKBitRate);
    data.setAudioSendBitrate(stats.txAudioKBitRate);
    data.setAudioRecvBitrate(stats.rxAudioKBitRate);
    data.setCpuApp(stats.cpuAppUsage);
    data.setCpuTotal(stats.cpuAppUsage);
    data.setSendLoss(stats.txPacketLossRate);
    data.setRecvLoss(stats.rxPacketLossRate);
  }

  @Override
  public void onRemoteVideoStats(IRtcEngineEventHandler.RemoteVideoStats stats) {
    if (!isometrik.getConfiguration().isEnableDetailedRtcStats()) return;
    if (!isometrik.getIsometrikRtcStatsManager().isEnabled()) return;

    RemoteStatsData data =
        (RemoteStatsData) isometrik.getIsometrikRtcStatsManager().getStatsData(stats.uid);
    if (data == null) return;

    data.setWidth(stats.width);
    data.setHeight(stats.height);
    data.setFramerate(stats.rendererOutputFrameRate);
    data.setVideoDelay(stats.delay);
  }

  @Override
  public void onRemoteAudioStats(IRtcEngineEventHandler.RemoteAudioStats stats) {
    if (!isometrik.getConfiguration().isEnableDetailedRtcStats()) return;
    if (!isometrik.getIsometrikRtcStatsManager().isEnabled()) return;

    RemoteStatsData data =
        (RemoteStatsData) isometrik.getIsometrikRtcStatsManager().getStatsData(stats.uid);
    if (data == null) return;

    data.setAudioNetDelay(stats.networkTransportDelay);
    data.setAudioNetJitter(stats.jitterBufferDelay);
    data.setAudioLoss(stats.audioLossRate);
    data.setAudioQuality(isometrik.getIsometrikRtcStatsManager().qualityToString(stats.quality));
  }

  @Override
  public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
    for (IsometrikRtcEventHandler handler : mHandler) {
      handler.onJoinChannelSuccess(channel, uid, elapsed);
    }
  }

  @Override
  public void onFirstLocalVideoFrame(int width, int height, int elapsed) {

    for (IsometrikRtcEventHandler handler : mHandler) {
      handler.onFirstLocalVideoFrame(width, height, elapsed);
    }
  }

  @Override
  public void onError(int err) {
    for (IsometrikRtcEventHandler handler : mHandler) {
      handler.onStreamingError(err);
    }
  }
}
