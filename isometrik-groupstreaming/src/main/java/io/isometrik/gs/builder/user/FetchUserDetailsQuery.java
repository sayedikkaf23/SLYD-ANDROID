package io.isometrik.gs.builder.user;

/**
 * Query builder class for creating the request for fetching the details of a user.
 */
public class FetchUserDetailsQuery {
  private String userId;

  private FetchUserDetailsQuery(Builder builder) {
    this.userId = builder.userId;
  }

  /**
   * The Builder class for the FetchUserDetailsQuery.
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
     * @param userId the id of the user whose details are to be fetched
     * @return the FetchUserDetailsQuery.Builder{@link io.isometrik.gs.builder.user.FetchUserDetailsQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.user.FetchUserDetailsQuery.Builder
     */
    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build fetch user details query.
     *
     * @return the FetchUserDetailsQuery{@link io.isometrik.gs.builder.user.FetchUserDetailsQuery} instance
     * @see io.isometrik.gs.builder.user.FetchUserDetailsQuery
     */
    public FetchUserDetailsQuery build() {
      return new FetchUserDetailsQuery(this);
    }
  }

  /**
   * Gets user id.
   *
   * @return the id of the user whose details are to be fetched
   */
  public String getUserId() {
    return userId;
  }
}
