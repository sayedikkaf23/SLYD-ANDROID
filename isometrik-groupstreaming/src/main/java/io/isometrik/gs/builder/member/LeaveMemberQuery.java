package io.isometrik.gs.builder.member;

/**
 * Query builder class for creating the request for leaving a stream group be a member.
 */
public class LeaveMemberQuery {

  private String streamId;
  private String memberId;

  private LeaveMemberQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.memberId = builder.memberId;
  }

  /**
   * The Builder class for the LeaveMemberQuery.
   */
  public static class Builder {
    private String streamId;
    private String memberId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group to be left by a member
     * @return the LeaveMemberQuery.Builder{@link io.isometrik.gs.builder.member.LeaveMemberQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.member.LeaveMemberQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets member id.
     *
     * @param memberId the id of the member leaving the stream group
     * @return the LeaveMemberQuery.Builder{@link io.isometrik.gs.builder.member.LeaveMemberQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.member.LeaveMemberQuery.Builder
     */
    public Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    /**
     * Build leave member query.
     *
     * @return the LeaveMemberQuery{@link io.isometrik.gs.builder.member.LeaveMemberQuery} instance
     * @see io.isometrik.gs.builder.member.LeaveMemberQuery
     */
    public LeaveMemberQuery build() {
      return new LeaveMemberQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group to be left by a member
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets member id.
   *
   * @return the id of the member leaving the stream group
   */
  public String getMemberId() {
    return memberId;
  }
}
