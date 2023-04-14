package io.isometrik.gs.events.presence;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing presence stream stop event details, which is triggered if user is
 * subscribed to stream start events irrespective of whether he is member of the stream group or
 * not.
 */
public class PresenceStreamStopEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("streamId")
  @Expose
  private String streamId;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;

  /**
   * Gets action.
   *
   * @return the action specifying the stream stop event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group being ended
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which stream group was ended
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the user who ended the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }
}
