package io.isometrik.gs.events.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing publish start event details.
 */
public class PublishStartEvent implements Serializable {

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
   * @return the action specifying the publish start event by a member in the stream group
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group in which publish has been started by a member
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which publish has been started by a member in the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets member id.
   *
   * @return the id of the member who has started publishing in the stream group
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets member name.
   *
   * @return the name of the member who has started publishing in the stream group
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
