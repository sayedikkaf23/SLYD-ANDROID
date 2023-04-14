package io.isometrik.gs.events.viewer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing viewer join event details.
 */
public class ViewerJoinEvent implements Serializable {

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

  @SerializedName("viewerIdentifier")
  @Expose
  private String viewerIdentifier;

  @SerializedName("viewerProfilePic")
  @Expose
  private String viewerProfilePic;

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
   * @return the action specifying the joining of a viewer event to the stream group
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group joined by the viewer
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which viewer joined the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets viewer id.
   *
   * @return the id of the viewer who joined the stream group
   */
  public String getViewerId() {
    return viewerId;
  }

  /**
   * Gets viewer name.
   *
   * @return the name of the viewer who joined the stream group
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

  /**
   * Gets viewer identifier.
   *
   * @return the identifier of the viewer who joined the stream group
   */
  public String getViewerIdentifier() {
    return viewerIdentifier;
  }

  /**
   * Gets viewer profile pic.
   *
   * @return the profile pic of the viewer who joined the stream group
   */
  public String getViewerProfilePic() {
    return viewerProfilePic;
  }
}
