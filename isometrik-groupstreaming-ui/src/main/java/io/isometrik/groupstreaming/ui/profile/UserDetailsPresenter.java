package io.isometrik.groupstreaming.ui.profile;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.utils.UserSession;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.user.DeleteUserQuery;
import io.isometrik.gs.builder.user.FetchUserDetailsQuery;

/**
 * The user details presenter to fetch the user details and to delete a user.
 * It implements UserDetailsContract.Presenter{@link UserDetailsContract.Presenter}
 *
 * @see UserDetailsContract.Presenter
 */
public class UserDetailsPresenter implements UserDetailsContract.Presenter {

  /**
   * Instantiates a new User details presenter.
   */
  UserDetailsPresenter(UserDetailsContract.View userDetailsView) {
    this.userDetailsView = userDetailsView;
  }

  private UserDetailsContract.View userDetailsView;
  private boolean deletingUser;
  private boolean fetchingUserDetails;

  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  /**
   * {@link UserDetailsContract.Presenter#requestUserDetails(String)}
   */
  @Override
  public void requestUserDetails(String userId) {
    if (!fetchingUserDetails) {
      fetchingUserDetails = true;
      isometrik.fetchUserDetails(new FetchUserDetailsQuery.Builder().setUserId(userId).build(),
          (var1, var2) -> {
            fetchingUserDetails = false;
            if (var1 != null) {

              try {

                UserSession userSession = IsometrikUiSdk.getInstance().getUserSession();

                boolean nameToBeUpdated = true, identifierToBeUpdated = true,
                    profilePicToBeUpdated = true;

                if (userSession.getUserName() != null) {

                  nameToBeUpdated = !userSession.getUserName().equals(var1.getUserName());
                }

                if (userSession.getUserIdentifier() != null) {

                  identifierToBeUpdated =
                      !userSession.getUserIdentifier().equals(var1.getUserIdentifier());
                }
                if (userSession.getUserProfilePic() != null) {

                  profilePicToBeUpdated =
                      !userSession.getUserProfilePic().equals(var1.getUserProfilePic());
                }

                userDetailsView.onUserDetailsReceived(var1.getUserName(), var1.getUserIdentifier(),
                    var1.getUserProfilePic(), nameToBeUpdated, identifierToBeUpdated,
                    profilePicToBeUpdated);
              } catch (Exception ignore) {
                //For the exception,in case profile has been switched before response for user details api came
              }
            } else {

              userDetailsView.onError(var2.getErrorMessage());
            }
          });
    }
  }

  /**
   * {@link UserDetailsContract.Presenter#clearUserSession()}
   */
  @Override
  public void clearUserSession() {
    IsometrikUiSdk.getInstance().getUserSession().clear();
  }

  /**
   * {@link UserDetailsContract.Presenter#requestUserDelete(String)}
   */
  @Override
  public void requestUserDelete(String userId) {
    if (!deletingUser) {
      deletingUser = true;
      isometrik.deleteUser(new DeleteUserQuery.Builder().setUserId(userId).build(),
          (var1, var2) -> {
            deletingUser = false;
            if (var1 != null) {
              clearUserSession();
              userDetailsView.onUserDeleted();
            } else {

              userDetailsView.onError(var2.getErrorMessage());
            }
          });
    }
  }
}
