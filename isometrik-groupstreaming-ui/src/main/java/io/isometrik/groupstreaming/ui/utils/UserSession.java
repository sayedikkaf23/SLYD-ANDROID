package io.isometrik.groupstreaming.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * The type User session.
 */
public class UserSession {

  private SharedPreferences sharedPreferences;

  /**
   * Instantiates a new User session.
   *
   * @param context the context
   */
  public UserSession(Context context) {

    sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
  }

  /**
   * Gets user id.
   *
   * @return the user id
   */
  public String getUserId() {
    return sharedPreferences.getString("userId", null);
  }

  /**
   * Sets user id.
   *
   * @param userId the user id
   */
  public void setUserId(String userId) {
    sharedPreferences.edit().putString("userId", userId).apply();
  }

  /**
   * Gets user name.
   *
   * @return the user name
   */
  public String getUserName() {
    return sharedPreferences.getString("userName", null);
  }

  /**
   * Gets user profile pic.
   *
   * @return the user profile pic
   */
  public String getUserProfilePic() {
    return sharedPreferences.getString("userProfilePic", null);
  }

  /**
   * Gets user identifier.
   *
   * @return the user identifier
   */
  public String getUserIdentifier() {
    return sharedPreferences.getString("userIdentifier", null);
  }

  /**
   * Gets user selected.
   *
   * @return the user selected
   */
  public boolean getUserSelected() {
    return sharedPreferences.getBoolean("userSelected", true);
  }

  /**
   * Clear.
   */
  public void clear() {
    sharedPreferences.edit().clear().apply();
  }

  /**
   * Switch user.
   *
   * @param userId the user id
   * @param userName the user name
   * @param userIdentifier the user identifier
   * @param userProfilePic the user profile pic
   * @param userSelected the user selected
   */
  public void switchUser(String userId, String userName, String userIdentifier,
      String userProfilePic, boolean userSelected) {

    sharedPreferences.edit().putString("userId", userId).apply();
    sharedPreferences.edit().putString("userName", userName).apply();
    sharedPreferences.edit().putString("userProfilePic", userProfilePic).apply();
    sharedPreferences.edit().putString("userIdentifier", userIdentifier).apply();
    sharedPreferences.edit().putBoolean("userSelected", userSelected).apply();
  }

  /**
   * Sets user name.
   *
   * @param userName the user name
   */
  public void setUserName(String userName) {
    sharedPreferences.edit().putString("userName", userName).apply();
  }

  /**
   * Sets user profile pic.
   *
   * @param userProfilePic the user profile pic
   */
  public void setUserProfilePic(String userProfilePic) {
    sharedPreferences.edit().putString("userProfilePic", userProfilePic).apply();
  }

  /**
   * Sets user identifier.
   *
   * @param userIdentifier the user identifier
   */
  public void setUserIdentifier(String userIdentifier) {
    sharedPreferences.edit().putString("userIdentifier", userIdentifier).apply();
  }
  //
  //public void setUserSelected(boolean userSelected) {
  //  sharedPreferences.edit().putBoolean("userSelected", userSelected).apply();
  //}
}

