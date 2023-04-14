package io.isometrik.gs.builder.user;

/**
 * Query builder class for creating the request for updating the user details of a user.
 */
public class UpdateUserQuery {

  private String userId;
  private String userIdentifier;
  private String userName;
  private String userProfilePic;

  private UpdateUserQuery(Builder builder) {
    this.userId = builder.userId;
    this.userIdentifier = builder.userIdentifier;
    this.userName = builder.userName;
    this.userProfilePic = builder.userProfilePic;
  }

  /**
   * The Builder class for the UpdateUserQuery.
   */
  public static class Builder {
    private String userIdentifier;
    private String userName;
    private String userProfilePic;
    private String userId;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user identifier.
     *
     * @param userIdentifier the new identifier of the user to be updated
     * @return the UpdateUserQuery.Builder{@link io.isometrik.gs.builder.user.UpdateUserQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.UpdateUserQuery.Builder
     */
    public Builder setUserIdentifier(String userIdentifier) {
      this.userIdentifier = userIdentifier;
      return this;
    }

    /**
     * Sets user name.
     *
     * @param userName the new name of the user to be updated
     * @return the UpdateUserQuery.Builder{@link io.isometrik.gs.builder.user.UpdateUserQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.UpdateUserQuery.Builder
     */
    public Builder setUserName(String userName) {
      this.userName = userName;
      return this;
    }

    /**
     * Sets user profile pic.
     *
     * @param userProfilePic the profile pic of the user to be updated
     * @return the UpdateUserQuery.Builder{@link io.isometrik.gs.builder.user.UpdateUserQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.UpdateUserQuery.Builder
     */
    public Builder setUserProfilePic(String userProfilePic) {
      this.userProfilePic = userProfilePic;
      return this;
    }

    /**
     * Sets user id.
     *
     * @param userId the id of the user whose details are to be updated
     * @return the UpdateUserQuery.Builder{@link io.isometrik.gs.builder.user.UpdateUserQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.UpdateUserQuery.Builder
     */
    public Builder setUserId(String userId) {
      this.userId = userId;
      return this;
    }

    /**
     * Build update user query.
     *
     * @return the UpdateUserQuery{@link io.isometrik.gs.builder.user.UpdateUserQuery} instance
     * @see io.isometrik.gs.builder.user.UpdateUserQuery
     */
    public UpdateUserQuery build() {
      return new UpdateUserQuery(this);
    }
  }

  /**
   * Gets user id.
   *
   * @return the id of the user whose details are to be updated
   */
  public String getUserId() {
    return userId;
  }

  /**
   * Gets user identifier.
   *
   * @return the new identifier of the user to be updated
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets user name.
   *
   * @return the new name of the user to be updated
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Gets user profile pic.
   *
   * @return the profile pic of the user to be updated
   */
  public String getUserProfilePic() {
    return userProfilePic;
  }
}
