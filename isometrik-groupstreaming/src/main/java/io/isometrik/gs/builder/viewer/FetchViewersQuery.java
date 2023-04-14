package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for fetching the list of viewers in a stream group.
 */
public class FetchViewersQuery {

  private String streamId;

  private FetchViewersQuery(Builder builder) {
    this.streamId = builder.streamId;
  }

  /**
   * The Builder class for the FetchViewersQuery.
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
     * @param streamId the id of the stream group, whose viewers are to be fetched
     * @return the FetchViewersQuery.Builder{@link io.isometrik.gs.builder.viewer.FetchViewersQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.viewer.FetchViewersQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Build fetch viewers query.
     *
     * @return the FetchViewersQuery{@link io.isometrik.gs.builder.viewer.FetchViewersQuery} instance
     * @see io.isometrik.gs.builder.viewer.FetchViewersQuery
     */
    public FetchViewersQuery build() {
      return new FetchViewersQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group, whose viewers are to be fetched
   */
  public String getStreamId() {
    return streamId;
  }
}
