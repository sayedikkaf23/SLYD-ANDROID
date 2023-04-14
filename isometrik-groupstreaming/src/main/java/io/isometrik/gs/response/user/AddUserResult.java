package io.isometrik.gs.response.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse add user result of creating a new user query AddUserQuery{@link
 * io.isometrik.gs.builder.user.AddUserQuery}.
 *
 * @see io.isometrik.gs.builder.user.AddUserQuery
 */
public class AddUserResult implements Serializable {

  @SerializedName("userId")
  @Expose
  private String userId;

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
   * Gets user id.
   *
   * @return the id of the user created
   */
  public String getUserId() {
    return userId;
  }
}
