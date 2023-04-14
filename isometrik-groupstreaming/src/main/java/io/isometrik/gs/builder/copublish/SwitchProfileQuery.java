package io.isometrik.gs.builder.copublish;

/**
 * Query builder class for switching profile from a viewer to a broadcaster in a stream group.
 */
public class SwitchProfileQuery {

  private String streamId;
  private String userId;
  private Boolean isPublic;

  private SwitchProfileQuery(io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder builder) {
    this.streamId = builder.streamId;
    this.userId = builder.userId;
    this.isPublic = builder.isPublic;
  }

  /**
   * The Builder class for the SwitchProfileQuery.
   */
  public static class Builder {
    private String streamId;
    private String userId;
    private Boolean isPublic;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream for which to switch profile from a viewer to a
     * broadcaster
     * @return the SwitchProfileQuery.Builder{@link io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder
     */
    public io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder setStreamId(
        String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user switching profile from a viewer to a broadcaster in a stream
     * group
     * @return the SwitchProfileQuery.Builder{@link io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder
     */
    public io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Sets is public.
     *
     * @param isPublic whether given stream is public or not
     * group
     * @return the SwitchProfileQuery.Builder{@link io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder
     */
    public io.isometrik.gs.builder.copublish.SwitchProfileQuery.Builder setPublic(
        boolean isPublic) {
      this.isPublic = isPublic;
      return this;
    }

    /**
     * Build add switch profile query.
     *
     * @return the SwitchProfileQuery{@link io.isometrik.gs.builder.copublish.SwitchProfileQuery}
     * instance
     * @see io.isometrik.gs.builder.copublish.SwitchProfileQuery
     */
    public io.isometrik.gs.builder.copublish.SwitchProfileQuery build() {
      return new io.isometrik.gs.builder.copublish.SwitchProfileQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream for which to switch profile from a viewer to a broadcaster
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets user id.
   *
   * @return the id of the user switching profile from a viewer to a broadcaster in a stream group
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets is public.
   *
   * @return whether given stream group is public or not
   */
  public Boolean isPublic() {
    return isPublic;
  }
}