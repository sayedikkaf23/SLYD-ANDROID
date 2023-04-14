package io.isometrik.gs.events.viewer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing viewer remove event details.
 */
public class ViewerRemoveEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("streamId")
  @Expose
  private String streamId;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("viewerId")
  @Expose
  private String viewerId;

  @SerializedName("viewerName")
  @Expose
  private String viewerName;

  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;

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
   * @return the action specifying the viewer removed event from the stream group
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which viewer has been removed
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which viewer has been removed from the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets viewer id.
   *
   * @return the id of the viewer who has been removed from the stream group
   */
  public String getViewerId() {
    return viewerId;
  }

  /**
   * Gets viewer name.
   *
   * @return the name of the viewer who has been removed from the stream group
   */
  public String getViewerName() {
    return viewerName;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the user who has removed the viewer from the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets initiator name.
   *
   * @return the name of the user who has removed the viewer from the stream group
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
