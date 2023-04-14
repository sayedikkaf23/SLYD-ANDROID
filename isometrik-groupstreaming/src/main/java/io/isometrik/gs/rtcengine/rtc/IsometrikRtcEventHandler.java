package io.isometrik.gs.rtcengine.rtc;

/**
 * The interface to handle isometrik ETC events.
 */
public interface IsometrikRtcEventHandler {

  /**
   * On first remote video decoded.
   *
   * @param uid the uid
   * @param width the width
   * @param height the height
   * @param elapsed the elapsed
   */
  void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed);

  /**
   * On user offline.
   *
   * @param uid the uid
   * @param reason the reason
   */
  void onUserOffline(int uid, int reason);

  //void onJoinChannelSuccess(String channel, int uid, int elapsed);
  //void onUserJoined(int uid, int elapsed);

  /**
   * On connection state changed.
   *
   * @param state the current state of connection with backend
   * CONNECTION_STATE_DISCONNECTED(1): The SDK is disconnected from streaming server.
   * CONNECTION_STATE_CONNECTING(2): The SDK is connecting to streaming server.
   * CONNECTION_STATE_CONNECTED(3): The SDK joined a channel and is connected to streaming server.
   * You can now publish or subscribe to a media stream in the channel.
   * CONNECTION_STATE_RECONNECTING(4): The SDK keeps rejoining the channel after being disconnected
   * from a joined channel because of network issues.
   * CONNECTION_STATE_FAILED(5): The SDK fails to join the channel.
   * @param reason the reason of the connection loss
   * CONNECTION_CHANGED_CONNECTING(0): Connecting.
   * CONNECTION_CHANGED_JOIN_SUCCESS(1): The SDK has joined the channel successfully.
   * CONNECTION_CHANGED_INTERRUPTED(2): The connection is interrupted.
   * CONNECTION_CHANGED_BANNED_BY_SERVER(3): The connection is banned by streaming server.
   * CONNECTION_CHANGED_JOIN_FAILED(4): The SDK fails to join the channel for more than 20 minutes
   * and stops reconnecting to the channel.
   * CONNECTION_CHANGED_LEAVE_CHANNEL(5): The SDK has left the channel.
   * CONNECTION_CHANGED_INVALID_APP_ID(6): The specified App ID is invalid. Try to rejoin the
   * channel with a valid App ID.
   * CONNECTION_CHANGED_INVALID_CHANNEL_NAME(7): The specified channel name is invalid. Try to
   * rejoin the channel with a valid channel name.
   * CONNECTION_CHANGED_INVALID_TOKEN(8): The generated token is invalid probably due to the
   * following reasons:
   * The App Certificate for the project is enabled in Console, but you do not use the token.
   * The uid that you specify in the joinChannel method is different from the uid that you pass for
   * generating the token.
   * CONNECTION_CHANGED_TOKEN_EXPIRED(9): The token has expired. Generate a new token from your
   * server.
   * CONNECTION_CHANGED_REJECTED_BY_SERVER(10): The user is banned by the server.
   * CONNECTION_CHANGED_SETTING_PROXY_SERVER(11): The SDK tries to reconnect after setting a proxy
   * server.
   * CONNECTION_CHANGED_RENEW_TOKEN(12): The token renews.
   * CONNECTION_CHANGED_CLIENT_IP_ADDRESS_CHANGED(13): The client IP address has changed, probably
   * due to a change of the network type, IP address, or network port.
   * CONNECTION_CHANGED_KEEP_ALIVE_TIMEOUT(14): Timeout for the keep-alive of the connection between
   * the SDK and streaming server. The connection state changes to CONNECTION_STATE_RECONNECTING(4).
   * @param connected whether client is currently connected to streaming server or not
   */
  void onConnectionStateChanged(int state, int reason, Boolean connected);

  /**
   * On connection lost.
   */
  void onConnectionLost();

  /**
   * Callback when a broadcast channel has been successfully joined.
   *
   * @param channel channel name being joined
   * @param uid id of the user
   * @param elapsed time elapsed in joining
   */
  void onJoinChannelSuccess(String channel, int uid, int elapsed);

  /**
   * @param width width of the local video frame
   * @param height height of the local video frame
   * @param elapsed time elapsed
   */
  void onFirstLocalVideoFrame(int width, int height, int elapsed);

  /**
   * ERR_OK(0) : No error occurs
   * ERR_FAILED(1) : A general error occurs (no specified reason).
   * ERR_INVALID_ARGUMENT(2) : An invalid parameter is used. For example, the specific channel name
   * includes illegal characters.
   * ERR_NOT_READY(3) : The SDK module is not ready. We recommend the following methods to solve
   * this error:
   *
   * Check the audio device.
   * Check the completeness of the app.
   * Re-initialize the SDK.
   *
   * ERR_NOT_SUPPORTED(4) : The SDK does not support this function.
   * ERR_REFUSED(5) : The request is rejected.
   * ERR_BUFFER_TOO_SMALL(6) : The buffer size is not big enough to store the returned data.
   * ERR_NOT_INITIALIZED(7) : The SDK is not initialized before calling this method.
   * ERR_NO_PERMISSION(9) : No permission exists. Check if the user has granted access to the audio
   * or video device.
   * ERR_TIMEDOUT(10) : An API timeout occurs. Some APIs require the SDK to return the execution
   * result, and this error occurs if the request takes too long for the SDK to process.
   * ERR_CANCELED(11) : The request is cancelled. This is for internal SDK internal use only, and
   * is
   * not returned to the app through any method or callback.
   * ERR_TOO_OFTEN(12) : The method is called too often. This is for internal SDK internal use
   * only,
   * and is not returned to the app through any method or callback.
   * ERR_BIND_SOCKET(13) : The SDK fails to bind to the network socket. This is for internal SDK
   * internal use only, and is not returned to the app through any method or callback.
   * ERR_NET_DOWN(14) : The network is unavailable. This is for internal SDK internal use only, and
   * is not returned to the app through any method or callback.
   * ERR_NET_NOBUFS(15) : No network buffers are available. This is for internal SDK internal use
   * only, and is not returned to the app through any method or callback.
   * ERR_JOIN_CHANNEL_REJECTED(17) : The request to join the channel is rejected. This error
   * usually
   * occurs when:
   *
   * The user is already in the channel, and still calls the API to join the channel, for example,
   * joinChannel.
   * The user tries to join the channel during echo test. Wait until the echo test is finished.
   * ERR_LEAVE_CHANNEL_REJECTED(18) : The request to leave the channel is rejected. This error
   * usually occurs:
   *
   * When the user left the channel and still calls the API to leave the channel, for example,
   * leaveChannel. This error stops once the user stops calling the method.
   * When the user calls leaveChannel before joining the channel. No extra operation is needed.
   * ERR_ALREADY_IN_USE(19) : The resources are occupied and cannot be used.
   * ERR_INVALID_APP_ID(101) : The specified App ID is invalid. Please try to rejoin the channel
   * with a valid App ID.
   * ERR_INVALID_CHANNEL_NAME(102) : The specified channel name is invalid. Please try to rejoin
   * the
   * channel with a valid channel name.
   * ERR_NO_SERVER_RESOURCES() : There is no server resources. Please try to set the others area
   * code.
   * ERR_TOKEN_EXPIRED(109) : The token expired due to one of the following reasons:
   *
   * Authorized Timestamp expired: The timestamp is represented by the number of seconds elapsed
   * since 1/1/1970. The user can use the Token to access the Agora service within 24 hours after
   * the Token is generated. If the user does not access the Agora service after five minutes, this
   * Token will no longer be valid.
   * Call Expiration Timestamp expired: The timestamp is the exact time when a user can no longer
   * use the Agora service (for example, when a user is forced to leave an ongoing call). When a
   * value is set for the Call Expiration Timestamp, it does not mean that the Dynamic Key will
   * expire, but that the user will be banned from the channel.
   * ERR_INVALID_TOKEN(110) : The token is invalid due to one of the following reasons:
   *
   * The App Certificate for the project is enabled in Console, but the user is still using the App
   * ID.
   * Once the App Certificate is enabled, the user must use a token. The uid is mandatory, and
   * users
   * must set the same uid as the one set in the joinChannel method.
   * ERR_CONNECTION_INTERRUPTED(111) : The Internet connection is interrupted.
   * ERR_CONNECTION_LOST(112) : The Internet connection is lost.
   * ERR_NOT_IN_CHANNEL(113) : The user is not in the channel.
   * ERR_SIZE_TOO_LARGE(114) : The size of the sent data is over 1024 bytes when the user calls the
   * sendStreamMessage method.
   * ERR_BITRATE_LIMIT(115) : The bitrate of the sent data exceeds the limit of 6 Kbps when the
   * user
   * calls the sendStreamMessage method.
   * ERR_TOO_MANY_DATA_STREAMS(116) : Too many data streams (over five streams) are created when
   * the
   * user calls the createDataStream} method.
   * ERR_DECRYPTION_FAILED(120) : Decryption fails. The user may have used a different encryption
   * password to join the channel. Please check your settings or try rejoining the channel.
   * ERR_CLIENT_IS_BANNED_BY_SERVER(123) : The client is banned by the server.
   * ERR_WATERMARK_PATH(125) : Incorrect watermark file path.
   * ERR_INVALID_USER_ACCOUNT(134) : The user account is invalid.
   * ERR_LOAD_MEDIA_ENGINE(1001) : Fails to load the media engine.
   * ERR_START_CALL(1002) : Fails to start the call after enabling the media engine.
   * ERR_START_CAMERA(1003) : Fails to start the camera.
   * ERR_START_VIDEO_RENDER(1004) : Fails to start the video rendering module.
   * ERR_ADM_GENERAL_ERROR(1005) : Audio Device Module: A general error occurs in the Audio Device
   * Module (no classified reason). Please check if the audio device is used by other apps, or try
   * joining the channel
   * ERR_ADM_JAVA_RESOURCE(1006) : Audio Device Module: An error occurs in using the Java
   * resources.
   * ERR_ADM_SAMPLE_RATE(1007) : Audio Device Module: An error occurs in setting the sampling
   * frequency.
   * ERR_ADM_INIT_PLAYOUT(1008) : Audio Device Module: An error occurs in initializing the playback
   * device.
   * ERR_ADM_START_PLAYOUT(1009) : Audio Device Module: An error occurs in starting the playback
   * device.
   * ERR_ADM_STOP_PLAYOUT(1010) : Audio Device Module: An error occurs in stopping the playback
   * device.
   * ERR_ADM_INIT_RECORDING(1011) : Audio Device Module: An error occurs in initializing the
   * recording device.
   * ERR_ADM_START_RECORDING(1012) : Audio Device Module: An error occurs in starting the recording
   * device.
   * ERR_ADM_STOP_RECORDING(1013) : Audio Device Module: An error occurs in stopping the recording
   * device.
   * ERR_ADM_RUNTIME_PLAYOUT_ERROR(1015) : Audio Device Module: A playback error occurs.
   * ERR_ADM_RUNTIME_RECORDING_ERROR(1017) : Audio Device Module: A recording error occurs.
   * ERR_ADM_RECORD_AUDIO_FAILED(1018) : Audio Device Module: Fails to record.
   * ERR_ADM_INIT_LOOPBACK(1022) : Audio Device Module: An error occurs in initializing the
   * loopback
   * device.
   * ERR_ADM_START_LOOPBACK(1023) : Audio Device Module: An error occurs in starting the loopback
   * device.
   * ERR_AUDIO_BT_SCO_FAILED(1030) : Audio Routing: Fails to route the audio to the connected
   * Bluetooth device. The default route is used.
   * ERR_ADM_NO_RECORDING_DEVICE(1359) : Audio Device Module: No recording device exists.
   * ERR_VDM_CAMERA_NOT_AUTHORIZED(1501) : Video Device Module: The camera is not authorized.
   * ERR_VCM_UNKNOWN_ERROR(1600) : Video Device Module: Unknown error.
   * ERR_VCM_ENCODER_INIT_ERROR(1601) : Video Device Module: Error in initializing the video
   * encoder. This is a serious error and you can try to rejoin the channel.
   * ERR_VCM_ENCODER_ENCODE_ERROR(1602) : Video Device Module: Error in the video encoder. This is a
   * serious error and you can try to rejoin the channel.
   * ERR_VCM_ENCODER_SET_ERROR(1603) : Video Device Module: Error in setting the video encoder.
   *
   * @param err the error code of the streaming error
   */
  void onStreamingError(int err);
}

