package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for fetching the viewers count in a stream group.
 */
public class FetchViewersCountQuery {

  private String streamId;

  private FetchViewersCountQuery(Builder builder) {
    this.streamId = builder.streamId;
  }

  /**
   * The Builder class for the FetchViewersCountQuery.
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
     * @param streamId the id of the stream group, whose viewers count is to be fetched
     * @return the FetchViewersCountQuery.Builder{@link io.isometrik.gs.builder.viewer.FetchViewersCountQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.viewer.FetchViewersCountQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Build fetch viewers count query.
     *
     * @return the FetchViewersCountQuery{@link io.isometrik.gs.builder.viewer.FetchViewersCountQuery}
     * instance
     * @see io.isometrik.gs.builder.viewer.FetchViewersCountQuery
     */
    public FetchViewersCountQuery build() {
      return new FetchViewersCountQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group, whose viewers count is to be fetched
   */
  public String getStreamId() {
    return streamId;
  }
}
