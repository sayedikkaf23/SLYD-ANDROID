package io.isometrik.gs.events.viewer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing viewer timeout event details.
 */
public class ViewerTimeoutEvent implements Serializable {

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

  @SerializedName("membersCount")
  @Expose
  private int membersCount;

  @SerializedName("viewersCount")
  @Expose
  private int viewersCount;

  /**
   * Gets action.
   *
   * @return the action specifying the viewer timed out event in the stream group
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group having the viewer who has timed out
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which viewer has timed out
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets viewer id.
   *
   * @return the id of the viewer who has timed out
   */
  public String getViewerId() {
    return viewerId;
  }

  /**
   * Gets viewer name.
   *
   * @return the name of the viewer who has timed out
   */
  public String getViewerName() {
    return viewerName;
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
