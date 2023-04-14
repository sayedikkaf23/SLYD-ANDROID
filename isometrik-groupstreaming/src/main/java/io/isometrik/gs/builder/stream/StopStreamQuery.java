package io.isometrik.gs.builder.stream;

/**
 * Query builder class for creating the request for stopping a stream.
 */
public class StopStreamQuery {

  private String streamId;
  private String createdBy;

  private StopStreamQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.createdBy = builder.createdBy;
  }

  /**
   * The Builder class for the StopStreamQuery.
   */
  public static class Builder {
    private String streamId;
    private String createdBy;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group to end
     * @return the StopStreamQuery.Builder{@link io.isometrik.gs.builder.stream.StopStreamQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.StopStreamQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets created by.
     *
     * @param createdBy the id of the stream group creator, who is ending the stream group
     * @return the StopStreamQuery.Builder{@link io.isometrik.gs.builder.stream.StopStreamQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.StopStreamQuery.Builder
     */
    public Builder setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    /**
     * Build stop stream query.
     *
     * @return the StopStreamQuery{@link io.isometrik.gs.builder.stream.StopStreamQuery} instance
     * @see io.isometrik.gs.builder.stream.StopStreamQuery
     */
    public StopStreamQuery build() {
      return new StopStreamQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group to end
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets created by.
   *
   * @return the id of the stream group creator, who is ending the stream group
   */
  public String getCreatedBy() {
    return createdBy;
  }
}
