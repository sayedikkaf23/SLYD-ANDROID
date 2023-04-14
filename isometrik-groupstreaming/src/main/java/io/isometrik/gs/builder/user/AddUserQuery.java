package io.isometrik.gs.builder.user;

/**
 * Query builder class for creating the request for creating a new user.
 */
public class AddUserQuery {

  private String userIdentifier;
  private String userName;
  private String userProfilePic;

  private AddUserQuery(Builder builder) {
    this.userIdentifier = builder.userIdentifier;
    this.userName = builder.userName;
    this.userProfilePic = builder.userProfilePic;
  }

  /**
   * The Builder class for the AddUserQuery.
   */
  public static class Builder {
    private String userIdentifier;
    private String userName;
    private String userProfilePic;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets user identifier.
     *
     * @param userIdentifier the identifier of the new user to be created
     * @return the AddUserQuery.Builder{@link io.isometrik.gs.builder.user.AddUserQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.AddUserQuery.Builder
     */
    public Builder setUserIdentifier(String userIdentifier) {
      this.userIdentifier = userIdentifier;
      return this;
    }

    /**
     * Sets user name.
     *
     * @param userName the name of the new user to be created
     * @return the AddUserQuery.Builder{@link io.isometrik.gs.builder.user.AddUserQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.AddUserQuery.Builder
     */
    public Builder setUserName(String userName) {
      this.userName = userName;
      return this;
    }

    /**
     * Sets user profile pic.
     *
     * @param userProfilePic the profile pic of the new user to be created
     * @return the AddUserQuery.Builder{@link io.isometrik.gs.builder.user.AddUserQuery.Builder} instance
     * @see io.isometrik.gs.builder.user.AddUserQuery.Builder
     */
    public Builder setUserProfilePic(String userProfilePic) {
      this.userProfilePic = userProfilePic;
      return this;
    }

    /**
     * Build add user query.
     *
     * @return the AddUserQuery{@link io.isometrik.gs.builder.user.AddUserQuery} instance
     * @see io.isometrik.gs.builder.user.AddUserQuery
     */
    public AddUserQuery build() {
      return new AddUserQuery(this);
    }
  }

  /**
   * Gets user identifier.
   *
   * @return the identifier of the new user to be created
   */
  public String getUserIdentifier() {
    return userIdentifier;
  }

  /**
   * Gets user name.
   *
   * @return the name of the new user to be created
   */
  public String getUserName() {
    return userName;
  }

  /**
   * Gets user profile pic.
   *
   * @return the profile pic of the new user to be created
   */
  public String getUserProfilePic() {
    return userProfilePic;
  }
}
