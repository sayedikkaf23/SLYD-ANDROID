package io.isometrik.gs.events.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing member add event details.
 */
public class MemberAddEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("streamId")
  @Expose
  private String streamId;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("memberId")
  @Expose
  private String memberId;

  @SerializedName("memberIdentifier")
  @Expose
  private String memberIdentifier;

  @SerializedName("memberProfilePic")
  @Expose
  private String memberProfilePic;

  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;

  @SerializedName("memberName")
  @Expose
  private String memberName;

  @SerializedName("initiatorName")
  @Expose
  private String initiatorName;

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

  @SerializedName("viewersCount")
  @Expose
  private int viewersCount;

  /**
   * Gets action.
   *
   * @return the action specifying the member added event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group in which member has been added
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which member was added to the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets member id.
   *
   * @return the id of the member added to the stream group
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the user who added a member to the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets member name.
   *
   * @return the name of the member added to the stream group
   */
  public String getMemberName() {
    return memberName;
  }

  /**
   * Gets initiator name.
   *
   * @return the name of the user who added a member to the stream group
   */
  public String getInitiatorName() {
    return initiatorName;
  }

  /**
   * Gets members count.
   *
   * @return the members count of the stream group at the time of given event
   */
  public int getMembersCount() {
    return membersCount;
  }

  /**
   * Gets viewers count.
   *
   * @return the viewers count of the stream group at the time of given event
   */
  public int getViewersCount() {
    return viewersCount;
  }

  /**
   * Gets member identifier.
   *
   * @return the identifier of the member added
   */
  public String getMemberIdentifier() {
    return memberIdentifier;
  }

  /**
   * Gets member profile pic.
   *
   * @return the profile pic of the member added
   */
  public String getMemberProfilePic() {
    return memberProfilePic;
  }
}
