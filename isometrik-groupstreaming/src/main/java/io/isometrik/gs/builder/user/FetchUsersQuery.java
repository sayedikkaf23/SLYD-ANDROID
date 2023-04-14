package io.isometrik.gs.builder.user;

/**
 * Query builder class for creating the request for fetching the list of users.
 */
public class FetchUsersQuery {
  private String pageToken;
  private int count;

  private FetchUsersQuery(Builder builder) {

    this.pageToken = builder.pageToken;
    this.count = builder.count;
  }

  /**
   * The Builder class for the FetchUsersQuery.
   */
  public static class Builder {

    private String pageToken;
    private int count;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets page token.
     *
     * @param pageToken the token to be used in pagination of fetching user's list
     * @return the FetchUsersQuery.Builder{@link io.isometrik.gs.builder.user.FetchUsersQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.FetchUsersQuery.Builder
     */
    public Builder setPageToken(String pageToken) {
      this.pageToken = pageToken;
      return this;
    }

    /**
     * Sets count.
     *
     * @param count the number of users to fetched
     * @return the FetchUsersQuery.Builder{@link io.isometrik.gs.builder.user.FetchUsersQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.FetchUsersQuery.Builder
     */
    public Builder setCount(int count) {
      this.count = count;
      return this;
    }

    /**
     * Build fetch users query.
     *
     * @return the FetchUsersQuery{@link io.isometrik.gs.builder.user.FetchUsersQuery} instance
     * @see io.isometrik.gs.builder.user.FetchUsersQuery
     */
    public FetchUsersQuery build() {
      return new FetchUsersQuery(this);
    }
  }

  /**
   * Gets page token.
   *
   * @return the token to be used in pagination of fetching user's list
   */
  public String getPageToken() {
    return pageToken;
  }

  /**
   * Gets count.
   *
   * @return the number of users to fetched
   */
  public int getCount() {
    return count;
  }
}
