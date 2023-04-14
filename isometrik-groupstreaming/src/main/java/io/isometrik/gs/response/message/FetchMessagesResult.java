package io.isometrik.gs.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class to parse fetch messages result of fetching the list of messages in a stream group query
 * FetchMessagesQuery{@link io.isometrik.gs.builder.message.FetchMessagesQuery}.
 *
 * @see io.isometrik.gs.builder.message.FetchMessagesQuery
 */
public class FetchMessagesResult implements Serializable {

  @SerializedName("messages")
  @Expose
  private ArrayList<StreamMessage> streamMessages;

  @SerializedName("pageToken")
  @Expose
  private String pageToken;

  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * The class containing details of the message in the stream group.
   */
  public static class StreamMessage {

    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("sender_image")
    @Expose
    private String senderImage;
    @SerializedName("senderid")
    @Expose
    private String senderId;
    @SerializedName("sender_identifier")
    @Expose
    private String senderIdentifier;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("messageid")
    @Expose
    private String messageId;
    @SerializedName("message_type")
    @Expose
    private int messageType;
    @SerializedName("sentat")
    @Expose
    private long sentAt;

    /**
     * Gets sender name.
     *
     * @return the name of the user who sent message
     */
    public String getSenderName() {
      return senderName;
    }

    /**
     * Gets sender image.
     *
     * @return the image of the user who sent message
     */
    public String getSenderImage() {
      return senderImage;
    }

    /**
     * Gets sender id.
     *
     * @return the id of the user who sent message
     */
    public String getSenderId() {
      return senderId;
    }

    /**
     * Gets sender identifier.
     *
     * @return the identifier of the user who sent message
     */
    public String getSenderIdentifier() {
      return senderIdentifier;
    }

    /**
     * Gets message.
     *
     * @return the actual message
     */
    public String getMessage() {
      return message;
    }

    /**
     * Gets message id.
     *
     * @return the id of message
     */
    public String getMessageId() {
      return messageId;
    }

    /**
     * Gets message type.
     *
     * @return the type of message sent
     */
    public int getMessageType() {
      return messageType;
    }

    /**
     * Gets sent at.
     *
     * @return the time at which message was sent
     */
    public long getSentAt() {
      return sentAt;
    }
  }

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets stream messages.
   *
   * @return the list of messages in the stream group
   * @see io.isometrik.gs.response.message.FetchMessagesResult.StreamMessage
   */
  public ArrayList<StreamMessage> getStreamMessages() {
    return streamMessages;
  }

  /**
   * Gets page token.
   *
   * @return the page token to be used for pagination of the messages fetched
   */
  public String getPageToken() {
    return pageToken;
  }
}
