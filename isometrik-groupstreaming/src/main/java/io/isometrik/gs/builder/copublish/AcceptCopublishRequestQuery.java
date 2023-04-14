package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for accepting a copublish request in a stream group.
 */
public class AcceptCopublishRequestQuery {

  private String streamId;
  private String userId;
  private String initiatorId;

  private AcceptCopublishRequestQuery(AcceptCopublishRequestQuery.Builder builder) {
    this.streamId = builder.streamId;
    this.userId = builder.userId;
    this.initiatorId = builder.initiatorId;
  }

  /**
   * The Builder class for the AcceptCopublishRequestQuery.
   */
  public static class Builder {
    private String streamId;
    private String userId;
    private String initiatorId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream for which to accept a copublish request
     * @return the AcceptCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery.Builder
     */
    public AcceptCopublishRequestQuery.Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user whose copublish request is to be accepted
     * @return the AcceptCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery.Builder
     */
    public AcceptCopublishRequestQuery.Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Sets initiator id.
     *
     * @param initiatorId the id of the initiator of the stream group who is accepting the copublish
     * request
     * @return the AcceptCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery.Builder
     */
    public AcceptCopublishRequestQuery.Builder setInitiatorId(String initiatorId) {
      this.initiatorId = initiatorId;
      return this;
    }

    /**
     * Build accept copublish request query.
     *
     * @return the AcceptCopublishRequestQuery{@link io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery}
     * instance
     * @see io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery
     */
    public AcceptCopublishRequestQuery build() {
      return new AcceptCopublishRequestQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to accept a copublish request
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user whose copublish request is to be accepted
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the initiator of the stream group who is accepting the copublish request
   */
  public String getInitiatorId() {
    return initiatorId;
  }
}