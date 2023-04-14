package io.isometrik.groupstreaming.ui.settings.callbacks;

public interface ActionCallback {

  /**
   * User has requested to toggle visibility settings for either chat messages or action buttons.
   *
   * @param action whether visibility for chat messages or control buttons to be toggled
   * @param enabled whether visibility has been enabled or disabled
   */
  void onSettingsChange(int action, boolean enabled);

  /**
   * Leave broadcast by a member has been requested
   */
  void leaveBroadcastRequested();

  /**
   * End broadcast by the host has been requested
   */
  void endBroadcastRequested();

  /**
   * Stop publishing video by a member has been requested
   */
  void stopPublishingRequested();

  /**
   * On remote user's media setting being updated
   *
   * @param streamId id of the stream for which to update remote user's setting
   * @param userId userId of the user whose  settings were updated
   * @param uid uid of the user whose  settings were updated
   * @param userName name of the user whose  settings were updated
   * @param audio whether audio or video setting has been updated
   * @param muted whether muted or unmuted
   */
  void onRemoteUserMediaSettingsUpdated(String streamId, String userId, int uid, String userName, boolean audio,
      boolean muted);

  /**
   * To remove a publisher
   *
   * @param streamId id of the stream from which to remove a publisher
   * @param userId id of the user to be removed
   */
  void removeMemberRequested(String streamId, String userId);
}
