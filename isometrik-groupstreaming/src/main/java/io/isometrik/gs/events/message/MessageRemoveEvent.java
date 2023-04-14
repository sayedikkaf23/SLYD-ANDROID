package io.isometrik.gs.events.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing message remove event details.
 */
public class MessageRemoveEvent implements Serializable {

  @SerializedName("action")
  @Expose
  private String action;

  @SerializedName("streamId")
  @Expose
  private String streamId;

  @SerializedName("messageId")
  @Expose
  private String messageId;

  @SerializedName("timestamp")
  @Expose
  private long timestamp;

  @SerializedName("initiatorId")
  @Expose
  private String initiatorId;

  @SerializedName("initiatorName")
  @Expose
  private String initiatorName;

  /**
   * Gets action.
   *
   * @return the action specifying the message removed event in the stream group
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which message has been removed
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets message id.
   *
   * @return the id of the message which has been removed from the stream group
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which message was removed from the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the user who removed the message from the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets initiator name.
   *
   * @return the name of the user who removed the message from the stream group
   */
  public String getInitiatorName() {
    return initiatorName;
  }
}
