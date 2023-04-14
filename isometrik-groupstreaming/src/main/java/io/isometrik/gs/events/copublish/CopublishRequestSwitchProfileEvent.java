package io.isometrik.gs.events.copublish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The type Copublish request switch profile event.
 */
public class CopublishRequestSwitchProfileEvent implements Serializable {

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
   * @return the action specifying the switch of profile from viewer to broadcaster
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group for which user has switched profile
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which profile has been switched by the user
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets members count.
   *
   * @return the members count of the stream group at time of profile switch
   */
  public int getMembersCount() {
    return membersCount;
  }

  /**
   * Gets viewers count.
   *
   * @return the viewers count of the stream group at time of profile switch
   */
  public int getViewersCount() {
    return viewersCount;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user who switched profile
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets user identifier.
   *
   * @return the identifier of the user who switched profile
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets user profile pic.
   *
   * @return the profile pic of the user who switched profile
   */
  public String getUserProfilePic() {
    return userProfilePic;
  }

  /**
   * Gets user name.
   *
   * @return the name of the user who switched profile
   */
  public String getUserName() {
    return userName;
  }
}
