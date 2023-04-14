package io.isometrik.gs.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * The class to parse send message result of sending a message in a stream group query
 * SendMessageQuery{@link io.isometrik.gs.builder.message.SendMessageQuery}.
 *
 * @see io.isometrik.gs.builder.message.SendMessageQuery
 */
public class SendMessageResult implements Serializable {

  @SerializedName("messageId")
  @Expose
  private String messageId;
  @SerializedName("sentAt")
  @Expose
  private long sentAt;
  @SerializedName("messagesCount")
  @Expose
  private int messagesCount;

  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets message id.
   *
   * @return the id of the message sent
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets sent at.
   *
   * @return the time at which message was sent
   */
  public long getSentAt() {
    return sentAt;
  }

  /**
   * Gets messages count.
   *
   * @return the number of messages actually sent,corresponding to given send message request
   */
  public int getMessagesCount() {
    return messagesCount;
  }
}
