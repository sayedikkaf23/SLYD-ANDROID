package io.isometrik.gs.builder.user;

/**
 * Query builder class for creating the request for deleting an existing user
 */
public class DeleteUserQuery {
  private String userId;

  private DeleteUserQuery(Builder builder) {
    this.userId = builder.userId;
  }

  /**
   * The Builder class for the DeleteUserQuery.
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
     * @param userId the id of the user to be deleted
     * @return the DeleteUserQuery.Builder{@link io.isometrik.gs.builder.user.DeleteUserQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.DeleteUserQuery.Builder
     */
    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build delete user query.
     *
     * @return the DeleteUserQuery{@link io.isometrik.gs.builder.user.DeleteUserQuery} instance
     * @see io.isometrik.gs.builder.user.DeleteUserQuery
     */
    public DeleteUserQuery build() {
      return new DeleteUserQuery(this);
    }
  }

  /**
   * Gets user id.
   *
   * @return the id of the user to be deleted
   */
  public String getUserId() {
    return userId;
  }
}
