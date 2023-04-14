package io.isometrik.gs.builder.member;

/**
 * Query builder class for creating the request for fetching the members in a stream group
 */
public class FetchMembersQuery {

  private String streamId;

  private FetchMembersQuery(Builder builder) {
    this.streamId = builder.streamId;
  }

  /**
   * The Builder class for the FetchMembersQuery.
   */
  public static class Builder {
    private String streamId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream id.
     *
     * @param streamId the id of the stream group, whose members are to be fetched
     * @return the FetchMembersQuery.Builder{@link io.isometrik.gs.builder.member.FetchMembersQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.member.FetchMembersQuery.Builder
     */
    public Builder setStreamId(String streamId) {
      this.streamId = streamId;
      return this;
    }

    /**
     * Build fetch members query.
     *
     * @return the FetchMembersQuery{@link io.isometrik.gs.builder.member.FetchMembersQuery} instance
     * @see io.isometrik.gs.builder.member.FetchMembersQuery
     */
    public FetchMembersQuery build() {
      return new FetchMembersQuery(this);
    }
  }

  /**
   * Gets stream id.
   *
   * @return the id of the stream group, whose members are to be fetched
   */
  public String getStreamId() {
    return streamId;
  }
}
