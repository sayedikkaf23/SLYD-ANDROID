package io.isometrik.gs.rtcengine.utils;

/**
 * The class to generate unique uid from user id.
 */
public class UserIdGenerator {

  /**
   * Gets uid.
   *
   * @param userId the user id from which to generate unique uid
   * @return the uid generated from the input user id
   */
  public static int getUid(String userId) {
    return Math.abs(Integer.parseInt(userId.substring(0, 8), 16));
  }

  public static int getViewId(String helperId) {

    return Math.abs(helperId.hashCode());
  }
}
