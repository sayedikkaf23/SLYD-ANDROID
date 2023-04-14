package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for fetching status of a copublish request in a stream group.
 */
public class FetchCopublishRequestStatusQuery {
  private String streamId;
  private String userId;

  private FetchCopublishRequestStatusQuery(
      io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery.Builder builder) {
    this.streamId = builder.streamId;
    this.userId = builder.userId;
  }

  /**
   * The Builder class for the FetchCopublishRequestStatusQuery.
   */
  public static class Builder {
    private String streamId;
    private String userId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream for which to fetch status of a copublish request
     * @return the FetchCopublishRequestStatusQuery.Builder{@link io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery.Builder
     */
    public io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery.Builder setStreamId(
        String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user whose copublish request's status is to be fetched for the
     * stream group
     * group
     * @return the FetchCopublishRequestStatusQuery.Builder{@link io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery.Builder
     */
    public io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery.Builder setUserId(
        String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build fetch status of the copublish request query.
     *
     * @return the FetchCopublishRequestStatusQuery{@link io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery}
     * instance
     * @see io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery
     */
    public io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery build() {
      return new io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to fetch status of a copublish request
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user whose copublish request's status is to be fetched for the stream group
   */
  public String getUserId() {
    return userId;
  }
}
