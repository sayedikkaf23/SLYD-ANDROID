package io.isometrik.gs.builder.stream;

/**
 * Query builder class for creating the request for fetching the streams.
 * streamType-
 * 0-All
 * 1-Public
 * 2-Private
 */
public class FetchStreamsQuery {

  private String pageToken;
  private int count;
  private Integer streamType;

  private FetchStreamsQuery(Builder builder) {

    this.pageToken = builder.pageToken;
    this.count = builder.count;
    this.streamType = builder.streamType;
  }

  /**
   * The Builder class for the FetchStreamsQuery.
   */
  public static class Builder {

    private String pageToken;
    private int count;
    private Integer streamType;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets page token.
     *
     * @param pageToken the token to be used in pagination of the streams fetched
     * @return the FetchStreamsQuery.Builder{@link io.isometrik.gs.builder.stream.FetchStreamsQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.FetchStreamsQuery.Builder
     */
    public Builder setPageToken(String pageToken) {
      this.pageToken = pageToken;
      return this;
    }

    /**
     * Sets count.
     *
     * @param count the number of streams to be fetched
     * @return the FetchStreamsQuery.Builder{@link io.isometrik.gs.builder.stream.FetchStreamsQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.FetchStreamsQuery.Builder
     */
    public Builder setCount(int count) {
      this.count = count;
      return this;
    }

    /**
     * Sets stream type.
     *
     * @param streamType the type of streams to be fetched
     * @return the FetchStreamsQuery.Builder{@link io.isometrik.gs.builder.stream.FetchStreamsQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.FetchStreamsQuery.Builder
     */
    public Builder setStreamType(Integer streamType) {
      this.streamType = streamType;
      return this;
    }

    /**
     * Build fetch streams query.
     *
     * @return the FetchStreamsQuery{@link io.isometrik.gs.builder.stream.FetchStreamsQuery} instance
     * @see io.isometrik.gs.builder.stream.FetchStreamsQuery
     */
    public FetchStreamsQuery build() {
      return new FetchStreamsQuery(this);
    }
  }

  /**
   * Gets page token.
   *
   * @return the token to be used in pagination of the streams fetched
   */
  public String getPageToken() {
    return pageToken;
  }

  /**
   * Gets count.
   *
   * @return the number of streams to be fetched
   */
  public int getCount() {
    return count;
  }

  /**
   * Gets stream type.
   *
   * @return the type of streams to be fetched
   */
  public Integer getStreamType() {
    return streamType;
  }
}
