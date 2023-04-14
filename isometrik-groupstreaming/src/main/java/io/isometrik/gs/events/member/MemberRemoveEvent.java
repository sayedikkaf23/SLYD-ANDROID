package io.isometrik.gs.events.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing member remove event details.
 */
public class MemberRemoveEvent implements Serializable {

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
   * @return the action specifying the member removed event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which a member has been removed
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which the member was removed from the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets member id.
   *
   * @return the id of the member who was removed from the stream group
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the user who removed a member from the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets member name.
   *
   * @return the name of the member removed from the stream group
   */
  public String getMemberName() {
    return memberName;
  }

  /**
   * Gets initiator name.
   *
   * @return the name of the user who removed a member from the stream group
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
}
