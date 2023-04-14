package io.isometrik.gs.events.copublish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The type Copublish request add event.
 */
public class CopublishRequestAddEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("streamId")
  @Expose
  private String streamId;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

  @SerializedName("viewersCount")
  @Expose
  private int viewersCount;

  @SerializedName("userId")
  @Expose
  private String userId;

  @SerializedName("userIdentifier")
  @Expose
  private String userIdentifier;

  @SerializedName("userProfilePic")
  @Expose
  private String userProfilePic;

  @SerializedName("userName")
  @Expose
  private String userName;

  /**
   * Gets action.
   *
   * @return the action specifying the addition of a copublish request by a user
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group for which to add copublish request
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which copublish request has been added
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets members count.
   *
   * @return the members count of the stream group at time of addition of copublish request
   */
  public int getMembersCount() {
    return membersCount;
  }

  /**
   * Gets viewers count.
   *
   * @return the viewers count of the stream group at time of addition of copublish request
   */
  public int getViewersCount() {
    return viewersCount;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user whose copublish request has been added
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets user identifier.
   *
   * @return the identifier of the user whose copublish request has been added
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets user profile pic.
   *
   * @return the profile pic of the user whose copublish request has been added
   */
  public String getUserProfilePic() {
    return userProfilePic;
  }

  /**
   * Gets user name.
   *
   * @return the name of the user whose copublish request has been added
   */
  public String getUserName() {
    return userName;
  }
}
