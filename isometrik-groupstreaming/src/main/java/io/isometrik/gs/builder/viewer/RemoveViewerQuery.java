package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for removing a viewer from a stream group.
 */
public class RemoveViewerQuery {

  private String streamId;
  private String initiatorId;
  private String viewerId;

  private RemoveViewerQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.initiatorId = builder.initiatorId;
    this.viewerId = builder.viewerId;
  }

  /**
   * The Builder class for the RemoveViewerQuery.
   */
  public static class Builder {
    private String streamId;
    private String initiatorId;
    private String viewerId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group,from which to remove the viewer
     * @return the RemoveViewerQuery.Builder{@link io.isometrik.gs.builder.viewer.RemoveViewerQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.viewer.RemoveViewerQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets initiator id.
     *
     * @param initiatorId the id of the user removing a viewer from the stream group
     * @return the RemoveViewerQuery.Builder{@link io.isometrik.gs.builder.viewer.RemoveViewerQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.viewer.RemoveViewerQuery.Builder
     */
    public Builder setInitiatorId(String initiatorId) {
      this.initiatorId = initiatorId;
      return this;
    }

    /**
     * Sets viewer id.
     *
     * @param viewerId the id of the viewer who is being removed from a stream group
     * @return the RemoveViewerQuery.Builder{@link io.isometrik.gs.builder.viewer.RemoveViewerQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.viewer.RemoveViewerQuery.Builder
     */
    public Builder setViewerId(String viewerId) {
      this.viewerId = viewerId;
      return this;
    }

    /**
     * Build remove viewer query.
     *
     * @return the RemoveViewerQuery{@link io.isometrik.gs.builder.viewer.RemoveViewerQuery} instance
     * @see io.isometrik.gs.builder.viewer.RemoveViewerQuery
     */
    public RemoveViewerQuery build() {
      return new RemoveViewerQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group,from which to remove the viewer
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the user removing a viewer from the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets viewer id.
   *
   * @return the id of the viewer who is being removed from a stream group
   */
  public String getViewerId() {
    return viewerId;
  }
}
