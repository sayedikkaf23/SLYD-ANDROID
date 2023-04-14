package io.isometrik.gs.events.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing member timeout event details.
 */
public class MemberTimeoutEvent implements Serializable {

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

  @SerializedName("memberName")
  @Expose
  private String memberName;

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

  @SerializedName("viewersCount")
  @Expose
  private int viewersCount;

  /**
   * Gets action.
   *
   * @return the action specifying the member timed out event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group in which user was publishing at time of being timed out
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which user timed out
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets member id.
   *
   * @return the id of the stream group member who timed out
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets member name.
   *
   * @return the name of the stream group member who timed out
   */
  public String getMemberName() {
    return memberName;
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
