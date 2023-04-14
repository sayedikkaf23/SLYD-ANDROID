package io.isometrik.groupstreaming.ui.action;

/**
 * The interface Copublish action callbacks for communication back to copublish activity, on click of
 * various action buttons.
 */
public interface CopublishActionCallbacks {

  /**
   * Start publishing on copublish request accepted.
   */
  void startPublishingOnCopublishRequestAccepted();

  /**
   * Exit on no longer being a member.
   */
  void exitOnNoLongerBeingAMember();

  /**
   * Exit on copublish request rejected.
   */
  void exitOnCopublishRequestRejected();

  /**
   * Continue watching.
   */
  void continueWatching();

  /**
   * Request copublish.
   */
  void requestCopublish();

  /**
   * Delete copublish request.
   */
  void deleteCopublishRequest();
}
