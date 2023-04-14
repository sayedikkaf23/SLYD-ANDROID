package io.isometrik.groupstreaming.ui.users;

import io.isometrik.gs.response.user.FetchUsersResult;

/**
 * The type Users model.
 */
public class UsersModel {

  private String userId;
  private String userName;
  private String userIdentifier;
  private String userProfilePic;

  /**
   * Instantiates a new Users model.
   *
   * @param user the user
   */
  UsersModel(FetchUsersResult.User user) {

    userId = user.getUserId();
    userName = user.getUserName();
    userIdentifier = user.getUserIdentifier();
    userProfilePic = user.getUserProfilePic();
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
}
