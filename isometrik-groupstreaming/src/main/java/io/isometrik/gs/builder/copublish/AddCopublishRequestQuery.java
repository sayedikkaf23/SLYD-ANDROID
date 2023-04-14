package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for adding a copublish request in a stream group.
 */
public class AddCopublishRequestQuery {

  private String streamId;
  private String userId;

  private AddCopublishRequestQuery(AddCopublishRequestQuery.Builder builder) {
    this.streamId = builder.streamId;
    this.userId = builder.userId;
  }

  /**
   * The Builder class for the AddCopublishRequestQuery.
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
     * @param streamId the id of the stream for which to request copublish
     * @return the AddCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.AddCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.AddCopublishRequestQuery.Builder
     */
    public AddCopublishRequestQuery.Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user requesting the copublish in the stream group
     * @return the AddCopublishRequestQuery.Builder{@link io.isometrik.gs.builder.copublish.AddCopublishRequestQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.AddCopublishRequestQuery.Builder
     */
    public AddCopublishRequestQuery.Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build add copublish request query.
     *
     * @return the AddCopublishRequestQuery{@link io.isometrik.gs.builder.copublish.AddCopublishRequestQuery}
     * instance
     * @see io.isometrik.gs.builder.copublish.AddCopublishRequestQuery
     */
    public AddCopublishRequestQuery build() {
      return new AddCopublishRequestQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to request copublish
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user requesting the copublish in the stream group
   */
  public String getUserId() {
    return userId;
  }
}