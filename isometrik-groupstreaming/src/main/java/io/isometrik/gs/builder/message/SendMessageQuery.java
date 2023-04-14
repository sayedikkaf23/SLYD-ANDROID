package io.isometrik.gs.builder.message;

/**
 * Query builder class for creating the request for sending message in a stream group
 */
public class SendMessageQuery {

  private String streamId;
  private String senderId;
  private String senderName;
  private String senderIdentifier;
  private String senderImage;
  private String message;
  private int messageType;

  private SendMessageQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.senderId = builder.senderId;
    this.senderName = builder.senderName;
    this.senderIdentifier = builder.senderIdentifier;
    this.senderImage = builder.senderImage;
    this.message = builder.message;
    this.messageType = builder.messageType;
  }

  /**
   * The Builder class for the SendMessageQuery.
   */
  public static class Builder {
    private String streamId;
    private String senderId;
    private String senderName;
    private String senderIdentifier;
    private String senderImage;
    private String message;
    private int messageType;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group into which message is to be sent
     * @return the SendMessageQuery.Builder{@link io.isometrik.gs.builder.message.SendMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.SendMessageQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets sender id.
     *
     * @param senderId the id of the user sending the message in the stream group
     * @return the SendMessageQuery.Builder{@link io.isometrik.gs.builder.message.SendMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.SendMessageQuery.Builder
     */
    public Builder setSenderId(String senderId) {
      this.senderId = senderId;
      return this;
    }

    /**
     * Sets sender name.
     *
     * @param senderName the name of the user sending the message in the stream group
     * @return the SendMessageQuery.Builder{@link io.isometrik.gs.builder.message.SendMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.SendMessageQuery.Builder
     */
    public Builder setSenderName(String senderName) {
      this.senderName = senderName;
      return this;
    }

    /**
     * Sets sender identifier.
     *
     * @param senderIdentifier the identifier of the user sending the message in the stream group
     * @return the SendMessageQuery.Builder{@link io.isometrik.gs.builder.message.SendMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.SendMessageQuery.Builder
     */
    public Builder setSenderIdentifier(String senderIdentifier) {
      this.senderIdentifier = senderIdentifier;
      return this;
    }

    /**
     * Sets sender image.
     *
     * @param senderImage the image of the user sending the message in the stream group
     * @return the SendMessageQuery.Builder{@link io.isometrik.gs.builder.message.SendMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.SendMessageQuery.Builder
     */
    public Builder setSenderImage(String senderImage) {
      this.senderImage = senderImage;
      return this;
    }

    /**
     * Sets message.
     *
     * @param message the message that is being sent in the stream group
     * @return the SendMessageQuery.Builder{@link io.isometrik.gs.builder.message.SendMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.SendMessageQuery.Builder
     */
    public Builder setMessage(String message) {
      this.message = message;
      return this;
    }

    /**
     * Sets message type.
     *
     * @param messageType the type of the message being sent in the stream group
     * @return the SendMessageQuery.Builder{@link io.isometrik.gs.builder.message.SendMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.SendMessageQuery.Builder
     */
    public Builder setMessageType(int messageType) {
      this.messageType = messageType;
      return this;
    }

    /**
     * Build send message query.
     *
     * @return the SendMessageQuery{@link io.isometrik.gs.builder.message.SendMessageQuery} instance
     * @see io.isometrik.gs.builder.message.SendMessageQuery
     */
    public SendMessageQuery build() {
      return new SendMessageQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group into which message is to be sent
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets sender id.
   *
   * @return the id of the user sending the message in the stream group
   */
  public String getSenderId() {
    return senderId;
  }

  /**
   * Gets sender name.
   *
   * @return the name of the user sending the message in the stream group
   */
  public String getSenderName() {
    return senderName;
  }

  /**
   * Gets sender identifier.
   *
   * @return the identifier of the user sending the message in the stream group
   */
  public String getSenderIdentifier() {
    return senderIdentifier;
  }

  /**
   * Gets sender image.
   *
   * @return the image of the user sending the message in the stream group
   */
  public String getSenderImage() {
    return senderImage;
  }

  /**
   * Gets message.
   *
   * @return the message that is being sent in the stream group
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets message type.
   *
   * @return the type of the message being sent in the stream group
   */
  public int getMessageType() {
    return messageType;
  }
}
