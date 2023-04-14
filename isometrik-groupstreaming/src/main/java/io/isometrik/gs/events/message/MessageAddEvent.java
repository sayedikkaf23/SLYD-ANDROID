package io.isometrik.gs.events.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class containing message add event details.
 */
public class MessageAddEvent implements Serializable {

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

  @SerializedName("senderId")
  @Expose
  private String senderId;

  @SerializedName("senderIdentifier")
  @Expose
  private String senderIdentifier;

  @SerializedName("senderImage")
  @Expose
  private String senderImage;

  @SerializedName("senderName")
  @Expose
  private String senderName;

  @SerializedName("message")
  @Expose
  private String message;

  @SerializedName("messageType")
  @Expose
  private int messageType;

  /**
   * Gets action.
   *
   * @return the action specifying the message received event  in the stream group
   */
  public String getAction() {
    return action;
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group in which message has been received
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets message id.
   *
   * @return the id of the message received in the stream group
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp at which message was sent in the stream group
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets sender id.
   *
   * @return the id of the user who sent the message in the stream group
   */
  public String getSenderId() {
    return senderId;
  }

  /**
   * Gets sender identifier.
   *
   * @return the identifier of the user who sent the message in the stream group
   */
  public String getSenderIdentifier() {
    return senderIdentifier;
  }

  /**
   * Gets sender image.
   *
   * @return the image of the user who sent the message in the stream group
   */
  public String getSenderImage() {
    return senderImage;
  }

  /**
   * Gets sender name.
   *
   * @return the name of the user who sent the message in the stream group
   */
  public String getSenderName() {
    return senderName;
  }

  /**
   * Gets message.
   *
   * @return the message that was sent in the stream group
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets message type.
   *
   * @return the type of message sent in the stream group
   */
  public int getMessageType() {
    return messageType;
  }
}
