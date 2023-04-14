package io.isometrik.gs.builder.member;

/**
 * Query builder class for creating the request for adding a member to a stream group.
 */
public class AddMemberQuery {

  private String streamId;
  private String initiatorId;
  private String memberId;

  private AddMemberQuery(Builder builder) {
    this.streamId = builder.streamId;
    this.initiatorId = builder.initiatorId;
    this.memberId = builder.memberId;
  }

  /**
   * The Builder class for the AddMemberQuery.
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
     * @param streamId the id of the stream group, in which to add member
     * @return the AddMemberQuery.Builder{@link io.isometrik.gs.builder.member.AddMemberQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.member.AddMemberQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Sets initiator id.
     *
     * @param initiatorId the id of the member adding other member to the stream group
     * @return the AddMemberQuery.Builder{@link io.isometrik.gs.builder.member.AddMemberQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.member.AddMemberQuery.Builder
     */
    public Builder setInitiatorId(String initiatorId) {
      this.initiatorId = initiatorId;
      return this;
    }

    /**
     * Sets member id.
     *
     * @param memberId the id of the member to be added to the stream group
     * @return the AddMemberQuery.Builder{@link io.isometrik.gs.builder.member.AddMemberQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.member.AddMemberQuery.Builder
     */
    public Builder setMemberId(String memberId) {
      this.memberId = memberId;
      return this;
    }

    /**
     * Build add member query.
     *
     * @return the AddMemberQuery{@link io.isometrik.gs.builder.member.AddMemberQuery} instance
     * @see io.isometrik.gs.builder.member.AddMemberQuery
     */
    public AddMemberQuery build() {
      return new AddMemberQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group, in which to add member
   */
  public String getStreamId() {
    return streamId;
  }

  /**
   * Gets initiator id.
   *
   * @return the id of the member adding other member to the stream group
   */
  public String getInitiatorId() {
    return initiatorId;
  }

  /**
   * Gets member id.
   *
   * @return the id of the member to be added to the stream group
   */
  public String getMemberId() {
    return memberId;
  }
}
