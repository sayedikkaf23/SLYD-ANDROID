package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for denying a copublish request in a stream group.
 */
public class DenyCopublishRequestQuery {

  private String streamId;
  private String userId;
  private String initiatorId;

  private DenyCopublishRequestQuery(DenyCopublishRequestQuery.Builder builder) {
    this.streamId = builder.streamId;
    this.userId = builder.userId;
    this.initiatorId = builder.initiatorId;
  }

  /**
   * The Builder class for the DenyCopublishRequestQuery.
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
     * @param streamId the id of the stream for which to deny a copublish request
     * @return the DenyCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery.Builder
     */
    public DenyCopublishRequestQuery.Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user whose copublish request is to be denied
     * @return the DenyCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery.Builder
     */
    public DenyCopublishRequestQuery.Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Sets initiator id.
     *
     * @param initiatorId the id of the initiator of the stream group who is denying the copublish
     * request
     * @return the DenyCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery.Builder
     */
    public DenyCopublishRequestQuery.Builder setInitiatorId(String initiatorId) {
      this.initiatorId = initiatorId;
      return this;
    }

    /**
     * Build deny copublish request query.
     *
     * @return the DenyCopublishRequestQuery{@link io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery}
     * instance
     * @see io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery
     */
    public DenyCopublishRequestQuery build() {
      return new DenyCopublishRequestQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to deny a copublish request
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user whose copublish request is to be denied
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the initiator of the stream group who is denying the copublish request
   */
  public String getInitiatorId() {
    return initiatorId;
  }
}