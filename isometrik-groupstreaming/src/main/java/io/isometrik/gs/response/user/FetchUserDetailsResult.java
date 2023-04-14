package io.isometrik.gs.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse fetch user details result of fetching the user details query
 * FetchUserDetailsQuery{@link io.isometrik.gs.builder.user.FetchUserDetailsQuery}.
 *
 * @see io.isometrik.gs.builder.user.FetchUserDetailsQuery
 */
public class FetchUserDetailsResult implements Serializable {

  @SerializedName("userName")
  @Expose
  private String userName;
  @SerializedName("userProfilePic")
  @Expose
  private String userProfilePic;
  @SerializedName("userIdentifier")
  @Expose
  private String userIdentifier;
  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
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
   * Gets user profile pic.
   *
   * @return the profile pic of the user
   */
  public String getUserProfilePic() {
    return userProfilePic;
  }

  /**
   * Gets user identifier.
   *
   * @return the identifier of the user
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }
}
