package io.isometrik.gs.builder.member;

/**
 * Query builder class for creating the request for removing a member from a stream group.
 */
public class RemoveMemberQuery {

  private String streamId;
  private String initiatorId;
  private String memberId;

  private RemoveMemberQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.initiatorId = builder.initiatorId;
    this.memberId = builder.memberId;
  }

  /**
   * The Builder class for the RemoveMemberQuery.
   */
  public static class Builder {
    private String streamId;
    private String initiatorId;
    private String memberId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group from which to remove a member
     * @return the RemoveMemberQuery.Builder{@link io.isometrik.gs.builder.member.RemoveMemberQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.member.RemoveMemberQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets initiator id.
     *
     * @param initiatorId the id of the member who is removing another member from a stream group
     * @return the RemoveMemberQuery.Builder{@link io.isometrik.gs.builder.member.RemoveMemberQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.member.RemoveMemberQuery.Builder
     */
    public Builder setInitiatorId(String initiatorId) {
      this.initiatorId = initiatorId;
      return this;
    }

    /**
     * Sets member id.
     *
     * @param memberId the id of the member being removed from a stream group
     * @return the RemoveMemberQuery.Builder{@link io.isometrik.gs.builder.member.RemoveMemberQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.member.RemoveMemberQuery.Builder
     */
    public Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    /**
     * Build remove member query.
     *
     * @return the RemoveMemberQuery{@link io.isometrik.gs.builder.member.RemoveMemberQuery} instance
     * @see io.isometrik.gs.builder.member.RemoveMemberQuery
     */
    public RemoveMemberQuery build() {
      return new RemoveMemberQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group from which to remove a member
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the member who is removing another member from a stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets member id.
   *
   * @return the id of the member being removed from a stream group
   */
  public String getMemberId() {
    return memberId;
  }
}
