package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for deleting a copublish request in a stream group.
 */
public class DeleteCopublishRequestQuery {
  private String streamId;
  private String userId;

  private DeleteCopublishRequestQuery(DeleteCopublishRequestQuery.Builder builder) {
    this.streamId = builder.streamId;
    this.userId = builder.userId;
  }

  /**
   * The Builder class for the DeleteCopublishRequestQuery.
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
     * @param streamId the id of the stream for which to delete copublish request
     * @return the DeleteCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery.Builder
     */
    public DeleteCopublishRequestQuery.Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user whose copublish request is to be deleted for the stream
     * group
     * @return the DeleteCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery.Builder
     */
    public DeleteCopublishRequestQuery.Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build delete copublish request query.
     *
     * @return the DeleteCopublishRequestQuery{@link io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery}
     * instance
     * @see io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery
     */
    public DeleteCopublishRequestQuery build() {
      return new DeleteCopublishRequestQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to delete copublish request
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user whose copublish request is to be deleted for the stream group
   */
  public String getUserId() {
    return userId;
  }
}