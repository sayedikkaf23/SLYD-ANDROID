package io.isometrik.gs.builder.viewer;

/**
 * Query builder class for creating the request for leaving as viewer by a user for a stream group.
 */
public class LeaveViewerQuery {

  private String streamId;
  private String viewerId;

  private LeaveViewerQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.viewerId = builder.viewerId;
  }

  /**
   * The Builder class for the LeaveViewerQuery.
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
     * @param streamId the id of stream group to be left by a viewer
     * @return the LeaveViewerQuery.Builder{@link io.isometrik.gs.builder.viewer.LeaveViewerQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.viewer.LeaveViewerQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets viewer id.
     *
     * @param viewerId the id of the user who is leaving a stream group as viewer
     * @return the LeaveViewerQuery.Builder{@link io.isometrik.gs.builder.viewer.LeaveViewerQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.viewer.LeaveViewerQuery.Builder
     */
    public Builder setViewerId(String viewerId) {
      this.viewerId = viewerId;
      return this;
    }

    /**
     * Build leave viewer query.
     *
     * @return the LeaveViewerQuery{@link io.isometrik.gs.builder.viewer.LeaveViewerQuery} instance
     * @see io.isometrik.gs.builder.viewer.LeaveViewerQuery
     */
    public LeaveViewerQuery build() {
      return new LeaveViewerQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of stream group to be left by a viewer
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets viewer id.
   *
   * @return the id of the user who is leaving a stream group as viewer
   */
  public String getViewerId() {
    return viewerId;
  }
}
