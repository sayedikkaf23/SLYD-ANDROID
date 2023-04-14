package io.isometrik.groupstreaming.ui.members.add;

import io.isometrik.groupstreaming.ui.utils.DateUtil;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.response.user.FetchUsersResult;
import io.isometrik.gs.response.viewer.FetchViewersResult;

/**
 * The type Members model.
 */
class AddMembersModel {

  private String userId;
  private String userName;
  private String userIdentifier;
  private String userProfilePic;
  private String joinTime;
  private boolean isMember;
  private boolean normaUser;

  /**
   * Instantiates a new Add member model.
   *
   * @param user the user
   */
  AddMembersModel(FetchUsersResult.User user) {

    userId = user.getUserId();
    userName = user.getUserName();
    userIdentifier = user.getUserIdentifier();
    userProfilePic = user.getUserProfilePic();
    isMember = false;
    joinTime = null;
    normaUser = true;
  }

  /**
   * Instantiates a new Viewers model.
   *
   * @param viewer the viewer
   * @param isMember the is member
   */
  AddMembersModel(FetchViewersResult.StreamViewer viewer, boolean isMember) {

    userId = viewer.getViewerId();
    userName = viewer.getViewerName();
    userIdentifier = viewer.getViewerIdentifier();
    userProfilePic = viewer.getViewerProfilePic();

    joinTime = DateUtil.getDate(viewer.getJoinTime());
    this.isMember = isMember;
    normaUser = false;
  }

  /**
   * Instantiates a new Viewers model.
   *
   * @param viewerJoinEvent the viewer join event
   * @param isMember the is member
   */
  AddMembersModel(ViewerJoinEvent viewerJoinEvent, boolean isMember) {

    userId = viewerJoinEvent.getViewerId();
    userName = viewerJoinEvent.getViewerName();
    userIdentifier = viewerJoinEvent.getViewerIdentifier();
    userProfilePic = viewerJoinEvent.getViewerProfilePic();

    joinTime = DateUtil.getDate(viewerJoinEvent.getTimestamp());
    this.isMember = isMember;
    normaUser = false;
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  String getUserName() {
    return userName;
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets user profile pic.
   *
   * @return the user profile pic
   */
  String getUserProfilePic() {
    return userProfilePic;
  }

  public String getJoinTime() {
    return joinTime;
  }

  public boolean isMember() {
    return isMember;
  }

  public boolean isNormaUser() {
    return normaUser;
  }

  public void setMember(boolean member) {
    isMember = member;
  }
}
