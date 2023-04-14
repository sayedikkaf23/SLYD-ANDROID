package io.isometrik.groupstreaming.ui.requests;

public interface RequestListActionCallback {

  /**
   * @param accepted whether copublish request has been accepted or rejected
   * @param userId user whose request has been accepted or rejected
   */
  void copublishRequestAction(boolean accepted, String userId);
}
