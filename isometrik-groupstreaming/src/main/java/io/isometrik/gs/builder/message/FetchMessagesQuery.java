package io.isometrik.gs.builder.message;

/**
 * Query builder class for creating the request for fetching messages sent in a stream group.
 */
public class FetchMessagesQuery {

  private String streamId;
  private String userId;
  private String pageToken;
  private int count;

  private FetchMessagesQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.userId = builder.userId;
    this.pageToken = builder.pageToken;
    this.count = builder.count;
  }

  /**
   * The Builder class for the FetchMessagesQuery.
   */
  public static class Builder {
    private String streamId;
    private String userId;
    private String pageToken;
    private int count;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group whose messages are to be fetched
     * @return the FetchMessagesQuery.Builder{@link io.isometrik.gs.builder.message.FetchMessagesQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.FetchMessagesQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user fetching messages in a stream group
     * @return the FetchMessagesQuery.Builder{@link io.isometrik.gs.builder.message.FetchMessagesQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.FetchMessagesQuery.Builder
     */
    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Sets page token.
     *
     * @param pageToken the token to be used for pagination fo messages fetched in a stream group
     * @return the FetchMessagesQuery.Builder{@link io.isometrik.gs.builder.message.FetchMessagesQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.FetchMessagesQuery.Builder
     */
    public Builder setPageToken(String pageToken) {
      this.pageToken = pageToken;
      return this;
    }

    /**
     * Sets count.
     *
     * @param count the number of messages to be fetched in a stream group
     * @return the FetchMessagesQuery.Builder{@link io.isometrik.gs.builder.message.FetchMessagesQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.message.FetchMessagesQuery.Builder
     */
    public Builder setCount(int count) {
      this.count = count;
      return this;
    }

    /**
     * Build fetch messages query.
     *
     * @return the FetchMessagesQuery{@link io.isometrik.gs.builder.message.FetchMessagesQuery} instance
     * @see io.isometrik.gs.builder.message.FetchMessagesQuery
     */
    public FetchMessagesQuery build() {
      return new FetchMessagesQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group whose messages are to be fetched
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user fetching messages in a stream group
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets page token.
   *
   * @return the token to be used for pagination fo messages fetched in a stream group
   */
  public String getPageToken() {
    return pageToken;
  }

  /**
   * Gets count.
   *
   * @return the number of messages to be fetched in a stream group
   */
  public int getCount() {
    return count;
  }
}
