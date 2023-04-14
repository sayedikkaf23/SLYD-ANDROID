package io.isometrik.gs.rtcengine.rtc;

public interface RemoteMediaStateEventsHandler {

  /**
   * @param uid id of the user whose audio state has been changed
   * @param state State of the remote audio:
   * REMOTE_AUDIO_STATE_STOPPED(0): The remote audio is in the default state, probably due to
   * REMOTE_AUDIO_REASON_LOCAL_MUTED(3), REMOTE_AUDIO_REASON_REMOTE_MUTED(5), or
   * REMOTE_AUDIO_REASON_REMOTE_OFFLINE(7).
   * REMOTE_AUDIO_STATE_STARTING(1): The first remote audio packet is received.
   * REMOTE_AUDIO_STATE_DECODING(2): The remote audio stream is decoded and plays normally, probably
   * due to REMOTE_AUDIO_REASON_NETWORK_RECOVERY(2), REMOTE_AUDIO_REASON_LOCAL_UNMUTED(4) or
   * REMOTE_AUDIO_REASON_REMOTE_UNMUTED(6).
   * REMOTE_AUDIO_STATE_FROZEN(3): The remote audio is frozen, probably due to
   * REMOTE_AUDIO_REASON_NETWORK_CONGESTION(1).
   * REMOTE_AUDIO_STATE_FAILED(4): The remote audio fails to start, probably due to
   * REMOTE_AUDIO_REASON_INTERNAL(0).
   * @param reason The reason of the remote audio state change.
   * REMOTE_AUDIO_REASON_INTERNAL(0): Internal reasons.
   * REMOTE_AUDIO_REASON_NETWORK_CONGESTION(1): Network congestion.
   * REMOTE_AUDIO_REASON_NETWORK_RECOVERY(2): Network recovery.
   * REMOTE_AUDIO_REASON_LOCAL_MUTED(3): The local user stops receiving the remote audio stream or
   * disables the audio module.
   * REMOTE_AUDIO_REASON_LOCAL_UNMUTED(4): The local user resumes receiving the remote audio stream
   * or enables the audio module.
   * REMOTE_AUDIO_REASON_REMOTE_MUTED(5): The remote user stops sending the audio stream or disables
   * the audio module.
   * REMOTE_AUDIO_REASON_REMOTE_UNMUTED(6): The remote user resumes sending the audio stream or
   * enables the audio module.
   * REMOTE_AUDIO_REASON_REMOTE_OFFLINE(7): The remote user leaves the channel.
   * @param elapsed Time elapsed (ms) from the local user calling the joinChannel
   */
  void onRemoteAudioStateChanged(int uid, int state, int reason, int elapsed);

  /**
   * @param uid id of the user whose video state has been changed
   * @param state REMOTE_VIDEO_STATE_STOPPED(0): The remote video is in the default state, probably
   * due to REMOTE_VIDEO_STATE_REASON_LOCAL_MUTED(3), REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED(5), or
   * REMOTE_VIDEO_STATE_REASON_REMOTE_OFFLINE(7).
   * REMOTE_VIDEO_STATE_STARTING(1): The first remote video packet is received.
   * REMOTE_VIDEO_STATE_DECODING(2): The remote video stream is decoded and plays normally,
   * probably
   * due to REMOTE_VIDEO_STATE_REASON_NETWORK_RECOVERY (2), REMOTE_VIDEO_STATE_REASON_LOCAL_UNMUTED(4),
   * REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED(6), or REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK_RECOVERY(9).
   * REMOTE_VIDEO_STATE_FROZEN(3): The remote video is frozen, probably due to
   * REMOTE_VIDEO_STATE_REASON_NETWORK_CONGESTION(1) or REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK(8).
   * REMOTE_VIDEO_STATE_FAILED(4): The remote video fails to start, probably due to
   * REMOTE_VIDEO_STATE_REASON_INTERNAL(0).
   * @param reason REMOTE_VIDEO_STATE_REASON_INTERNAL(0): Internal reasons.
   * REMOTE_VIDEO_STATE_REASON_NETWORK_CONGESTION(1): Network congestion.
   * REMOTE_VIDEO_STATE_REASON_NETWORK_RECOVERY(2): Network recovery.
   * REMOTE_VIDEO_STATE_REASON_LOCAL_MUTED(3): The local user stops receiving the remote video
   * stream or disables the video module.
   * REMOTE_VIDEO_STATE_REASON_LOCAL_UNMUTED(4): The local user resumes receiving the remote video
   * stream or enables the video module.
   * REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED(5): The remote user stops sending the video stream or
   * disables the video module.
   * REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED(6): The remote user resumes sending the video stream
   * or enables the video module.
   * REMOTE_VIDEO_STATE_REASON_REMOTE_OFFLINE(7): The remote user leaves the channel.
   * REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK(8): The remote media stream falls back to the
   * audio-only stream due to poor network conditions.
   * REMOTE_VIDEO_STATE_REASON_AUDIO_FALLBACK_RECOVERY(9): The remote media stream switches back to
   * the video stream after the network conditions improve.
   * @param elapsed Time elapsed (ms) from the local user calling the joinChannel
   */
  void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed);

  /**
   * @param uid User ID. The network quality of the user with this uid is reported. If uid is 0, the
   * local network quality is reported.
   * @param txQuality Transmission quality of the user:
   * QUALITY_UNKNOWN(0): The quality is unknown.
   * QUALITY_EXCELLENT(1): The quality is excellent.
   * QUALITY_GOOD(2): The quality is quite good, but the bitrate may be slightly lower than
   * excellent.
   * QUALITY_POOR(3): Users can feel the communication slightly impaired.
   * QUALITY_BAD(4): Users can communicate not very smoothly.
   * QUALITY_VBAD(5): The quality is so bad that users can barely communicate.
   * QUALITY_DOWN(6): Users cannot communicate at all.
   * @param rxQuality Receiving quality of the user:
   * QUALITY_UNKNOWN(0): The quality is unknown.
   * QUALITY_EXCELLENT(1): The quality is excellent.
   * QUALITY_GOOD(2): The quality is quite good, but the bitrate may be slightly lower than
   * excellent.
   * QUALITY_POOR(3): Users can feel the communication slightly impaired.
   * QUALITY_BAD(4): Users can communicate not very smoothly.
   * QUALITY_VBAD(5): The quality is so bad that users can barely communicate.
   * QUALITY_DOWN(6): Users cannot communicate at all.
   */
  void onNetworkQuality(int uid, int txQuality, int rxQuality);
}
