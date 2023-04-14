package io.isometrik.gs.events.stream;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing stream stop event details, which is triggered if user is the part of the
 * stream group.
 */
public class StreamStopEvent implements Serializable {

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
   * @return the action specifying the end of the stream group event
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group that was ended
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
