package io.isometrik.gs.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class to parse fetch users result of fetching the list of users query FetchUsersQuery{@link
 * io.isometrik.gs.builder.user.FetchUsersQuery}.
 *
 * @see io.isometrik.gs.builder.user.FetchUsersQuery
 */
public class FetchUsersResult implements Serializable {

  @SerializedName("users")
  @Expose
  private ArrayList<User> users;

  @SerializedName("pageToken")
  @Expose
  private String pageToken;

  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * The class containing details of the users.
   */
  public static class User {

    @SerializedName("userid")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_identifier")
    @Expose
    private String userIdentifier;
    @SerializedName("user_profilepic")
    @Expose
    private String userProfilePic;

    /**
     * Gets user id.
     *
     * @return the id of the user
     */
    public String getUserId() {
      return userId;
    }

    /**
     * Gets user name.
     *
     * @return the name of the user
     */
    public String getUserName() {
      return userName;
    }

    /**
     * Gets user identifier.
     *
     * @return the identifier of the user
     */
    public String getUserIdentifier() {
      return userIdentifier;
    }

    /**
     * Gets user profile pic.
     *
     * @return the profile pic of the user
     */
    public String getUserProfilePic() {
      return userProfilePic;
    }
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets users.
   *
   * @return the list of users
   * @see io.isometrik.gs.response.user.FetchUsersResult.User
   */
  public ArrayList<User> getUsers() {
    return users;
  }

  /**
   * Gets page token.
   *
   * @return the page token to be used for the pagination of the users fetched
   */
  public String getPageToken() {
    return pageToken;
  }
}
