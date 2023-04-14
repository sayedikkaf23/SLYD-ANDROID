package io.isometrik.groupstreaming.ui.users;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.response.user.FetchUsersResult;
import java.util.ArrayList;

/**
 * The users presenter fetches the list of users along with paging.
 * It implements UsersContract.Presenter{@link io.isometrik.groupstreaming.ui.users.UsersContract.Presenter}
 *
 * @see io.isometrik.groupstreaming.ui.users.UsersContract.Presenter
 */
public class UsersPresenter implements UsersContract.Presenter {

  /**
   * Instantiates a new users presenter.
   */
  UsersPresenter(UsersContract.View usersView) {
    this.usersView = usersView;
  }

  private UsersContract.View usersView;
  private String pageToken;
  private boolean isLastPage;
  private boolean isLoading;

  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  /**
   * {@link UsersContract.Presenter#requestUsersData(int, boolean)}
   */
  @Override
  public void requestUsersData(int pageSize, boolean refreshRequest) {
    isLoading = true;

    if (refreshRequest) {
      pageToken = null;
    }

    isometrik.fetchUsers(
        new FetchUsersQuery.Builder().setCount(pageSize).setPageToken(pageToken).build(),
        (var1, var2) -> {

          isLoading = false;

          if (var1 != null) {

            ArrayList<UsersModel> usersModels = new ArrayList<>();

            pageToken = var1.getPageToken();
            if (pageToken == null) {

              isLastPage = true;
            }

            ArrayList<FetchUsersResult.User> users = var1.getUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {

              usersModels.add(new UsersModel(users.get(i)));
            }

            usersView.onUsersDataReceived(usersModels, refreshRequest);
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No users found
                usersView.onUsersDataReceived(new ArrayList<>(), true);
              } else {
                isLastPage = true;
              }
            } else {
              usersView.onError(var2.getErrorMessage());
            }
          }
        });
  }

  /**
   * {@link UsersContract.Presenter#requestUsersDataOnScroll(int, int, int)}
   */
  @Override
  public void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= Constants.USERS_PAGE_SIZE) {
        requestUsersData(Constants.USERS_PAGE_SIZE, false);
      }
    }
  }
}
