package io.isometrik.groupstreaming.ui.profile;

/**
 * The interface user details contract containing interfaces Presenter and View to be implemented
 * by the
 * UserDetailsPresenter{@link UserDetailsPresenter} and
 * UserDetailsActivity{@link UserDetailsActivity} respectively.
 *
 * @see UserDetailsPresenter
 * @see UserDetailsActivity
 */
public interface UserDetailsContract {

  /**
   * The interface UserDetailsContract.Presenter to be implemented by UserDetailsPresenter{@link
   * UserDetailsPresenter}
   *
   * @see UserDetailsPresenter
   */
  interface Presenter {

    /**
     * Request user details.
     *
     * @param userId the id of the user whose details are to be fetched
     */
    void requestUserDetails(String userId);

    /**
     * Request user delete.
     *
     * @param userId the id of the user to be deleted
     */
    void requestUserDelete(String userId);

    /**
     * Clear user session.
     */
    void clearUserSession();
  }

  /**
   * The interface UserDetailsContract.View to be implemented by UserDetailsActivity{@link
   * UserDetailsActivity}
   *
   * @see UserDetailsActivity
   */
  interface View {

    /**
     * On user details received.
     *
     * @param userName the user name received
     * @param userIdentifier the user identifier received
     * @param userProfilePicUrl the user profile pic url received
     * @param updateUserName whether to update the username in the UI
     * @param updateUserIdentifier whether to update the user identifier in the UI
     * @param updateUserProfilePic whether to update the user profile pic in the UI
     */
    void onUserDetailsReceived(String userName, String userIdentifier, String userProfilePicUrl,
        boolean updateUserName, boolean updateUserIdentifier, boolean updateUserProfilePic);

    /**
     * On user deleted successfully.
     */
    void onUserDeleted();

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
