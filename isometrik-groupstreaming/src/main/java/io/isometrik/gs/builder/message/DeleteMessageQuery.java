package io.isometrik.gs.builder.message;

/**
 * Query builder class for creating the request for deleting messages sent, from a stream group.
 */
public class DeleteMessageQuery {

  private String streamId;
  private String memberId;
  private String memberName;
  private String messageId;
  private long sentAt;

  private DeleteMessageQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.memberId = builder.memberId;
    this.memberName = builder.memberName;
    this.messageId = builder.messageId;
    this.sentAt = builder.sentAt;
  }

  /**
   * The Builder class for the DeleteMessageQuery.
   */
  public static class Builder {
    private String streamId;
    private String memberId;
    private String memberName;
    private String messageId;
    private long sentAt;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group from which to delete a message
     * @return the DeleteMessageQuery.Builder{@link io.isometrik.gs.builder.message.DeleteMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.DeleteMessageQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets member id.
     *
     * @param memberId the id of the member who is deleting the message
     * @return the DeleteMessageQuery.Builder{@link io.isometrik.gs.builder.message.DeleteMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.DeleteMessageQuery.Builder
     */
    public Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    /**
     * Sets member name.
     *
     * @param memberName the name of the member who is deleting the message
     * @return the DeleteMessageQuery.Builder{@link io.isometrik.gs.builder.message.DeleteMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.DeleteMessageQuery.Builder
     */
    public Builder setMemberName(String memberName) {
      this.memberName = memberName;
      return this;
    }

    /**
     * Sets message id.
     *
     * @param messageId the id of the message to be removed
     * @return the DeleteMessageQuery.Builder{@link io.isometrik.gs.builder.message.DeleteMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.DeleteMessageQuery.Builder
     */
    public Builder setMessageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    /**
     * Sets sent at.
     *
     * @param sentAt the time at which the message which is to be removed was sent
     * @return the DeleteMessageQuery.Builder{@link io.isometrik.gs.builder.message.DeleteMessageQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.DeleteMessageQuery.Builder
     */
    public Builder setSentAt(long sentAt) {
      this.sentAt = sentAt;
      return this;
    }

    /**
     * Build delete message query.
     *
     * @return the DeleteMessageQuery{@link io.isometrik.gs.builder.message.DeleteMessageQuery} instance
     * @see io.isometrik.gs.builder.message.DeleteMessageQuery
     */
    public DeleteMessageQuery build() {
      return new DeleteMessageQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which to delete a message
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets member id.
   *
   * @return the id of the member who is deleting the message
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Gets member name.
   *
   * @return the name of the member who is deleting the message
   */
  public String getMemberName() {
    return memberName;
  }

  /**
   * Gets message id.
   *
   * @return the id of the message to be removed
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets sent at.
   *
   * @return the time at which the message which is to be removed was sent
   */
  public long getSentAt() {
    return sentAt;
  }
}