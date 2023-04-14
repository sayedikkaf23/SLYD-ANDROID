package io.isometrik.gs.builder.stream;

/**
 * Query builder class for creating the request for updating publishing status of a user for all of
 * the stream groups.
 */
public class UpdateUserPublishingStatusQuery {
  private String userId;

  private UpdateUserPublishingStatusQuery(Builder builder) {
    this.userId = builder.userId;
  }

  /**
   * The Builder class for the UpdateUserPublishingStatusQuery.
   */
  public static class Builder {
    private String userId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user whose publishing status is to be updated on app restart
     * @return the UpdateUserPublishingStatusQuery.Builder{@link io.isometrik.gs.builder.stream.UpdateUserPublishingStatusQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.UpdateUserPublishingStatusQuery.Builder
     */
    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build update user publishing status query.
     *
     * @return the UpdateUserPublishingStatusQuery{@link io.isometrik.gs.builder.stream.UpdateUserPublishingStatusQuery}
     * instance
     * @see io.isometrik.gs.builder.stream.UpdateUserPublishingStatusQuery
     */
    public UpdateUserPublishingStatusQuery build() {
      return new UpdateUserPublishingStatusQuery(this);
    }
  }

  /**
   * Gets user id.
   *
   * @return the id of the user whose publishing status is to be updated on app restart
   */
  public String getUserId() {
    return userId;
  }
}
