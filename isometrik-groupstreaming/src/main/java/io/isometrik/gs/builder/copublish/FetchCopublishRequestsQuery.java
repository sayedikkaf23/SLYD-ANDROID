package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for fetching copublish requests in a stream group.
 */
public class FetchCopublishRequestsQuery {
  private String streamId;

  private FetchCopublishRequestsQuery(
      io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery.Builder builder) {
    this.streamId = builder.streamId;
  }

  /**
   * The Builder class for the FetchCopublishRequestsQuery.
   */
  public static class Builder {
    private String streamId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream for which to fetch copublish requests
     * @return the FetchCopublishRequestsQuery.Builder{@link io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery.Builder
     */
    public io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery.Builder setStreamId(
        String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Build fetch copublish requests query.
     *
     * @return the FetchCopublishRequestsQuery{@link io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery}
     * instance
     * @see io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery
     */
    public io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery build() {
      return new io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to fetch copublish requests
   */
  public String getStreamId() {
    return streamId;
  }
}
