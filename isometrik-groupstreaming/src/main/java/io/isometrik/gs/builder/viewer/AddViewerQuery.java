package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for adding a viewer to a stream group.
 */
public class AddViewerQuery {

  private String streamId;
  private String viewerId;

  private AddViewerQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.viewerId = builder.viewerId;
  }

  /**
   * The Builder class for the AddViewerQuery.
   */
  public static class Builder {
    private String streamId;
    private String viewerId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream to be joined as viewer
     * @return the AddViewerQuery.Builder{@link io.isometrik.gs.builder.viewer.AddViewerQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.viewer.AddViewerQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets viewer id.
     *
     * @param viewerId the id of the user joining the stream group as viewer
     * @return the AddViewerQuery.Builder{@link io.isometrik.gs.builder.viewer.AddViewerQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.viewer.AddViewerQuery.Builder
     */
    public Builder setViewerId(String viewerId) {
      this.viewerId = viewerId;
      return this;
    }

    /**
     * Build add viewer query.
     *
     * @return the AddViewerQuery{@link io.isometrik.gs.builder.viewer.AddViewerQuery} instance
     * @see io.isometrik.gs.builder.viewer.AddViewerQuery
     */
    public AddViewerQuery build() {
      return new AddViewerQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream to be joined as viewer
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets viewer id.
   *
   * @return the id of the user joining the stream group as viewer
   */
  public String getViewerId() {
    return viewerId;
  }
}
