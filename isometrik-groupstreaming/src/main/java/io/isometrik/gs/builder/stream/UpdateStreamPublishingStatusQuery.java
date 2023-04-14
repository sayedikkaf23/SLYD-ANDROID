package io.isometrik.gs.builder.stream;

/**
 * Query builder class for creating the request for updating publishing status of a member in a
 * stream group.
 */
public class UpdateStreamPublishingStatusQuery {

  private String streamId;
  private String memberId;
  private boolean startPublish;

  private UpdateStreamPublishingStatusQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.memberId = builder.memberId;
    this.startPublish = builder.startPublish;
  }

  /**
   * The Builder class for the UpdateStreamPublishingStatusQuery.
   */
  public static class Builder {
    private String streamId;
    private boolean startPublish;
    private String memberId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group in which to update the publishing status of a user
     * @return the UpdateStreamPublishingStatusQuery.Builder{@link io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets start publish.
     *
     * @param startPublish the status, publishing started or stopped in a publishing group by a user
     * @return the UpdateStreamPublishingStatusQuery.Builder{@link io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery.Builder
     */
    public Builder setStartPublish(boolean startPublish) {
      this.startPublish = startPublish;
      return this;
    }

    /**
     * Sets member id.
     *
     * @param memberId the id of the user whose publishing status is updated in a stream group
     * @return the UpdateStreamPublishingStatusQuery.Builder{@link io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery.Builder
     */
    public Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    /**
     * Build update stream publishing status query.
     *
     * @return the UpdateStreamPublishingStatusQuery{@link io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery}
     * instance
     * @see io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery
     */
    public UpdateStreamPublishingStatusQuery build() {
      return new UpdateStreamPublishingStatusQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group in which to update the publishing status of a user
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets member id.
   *
   * @return the id of the user whose publishing status is updated in a stream group
   */
  public String getMemberId() {
    return memberId;
  }

  /**
   * Is start publish boolean.
   *
   * @return the status, publishing started or stopped in a publishing group by a user
   */
  public boolean isStartPublish() {
    return startPublish;
  }
}
