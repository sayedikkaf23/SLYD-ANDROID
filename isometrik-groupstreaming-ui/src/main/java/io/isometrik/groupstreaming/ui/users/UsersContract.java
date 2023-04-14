package io.isometrik.groupstreaming.ui.users;

import java.util.ArrayList;

/**
 * The interface users contract containing interfaces Presenter and View to be implemented
 * by the
 * UsersPresenter{@link io.isometrik.groupstreaming.ui.users.UsersPresenter} and
 * UsersActivity{@link io.isometrik.groupstreaming.ui.users.UsersActivity} respectively.
 *
 * @see io.isometrik.groupstreaming.ui.users.UsersPresenter
 * @see io.isometrik.groupstreaming.ui.users.UsersActivity
 */
public interface UsersContract {

  /**
   * The interface UsersContract.Presenter to be implemented by UsersPresenter{@link
   * io.isometrik.groupstreaming.ui.users.UsersPresenter}
   *
   * @see io.isometrik.groupstreaming.ui.users.UsersPresenter
   */
  interface Presenter {

    /**
     * Request users data.
     *
     * @param pageSize the number of users to be fetched
     * @param refreshRequest whether to fetch the first page of users or the specific page of
     * users,with paging
     */
    void requestUsersData(int pageSize, boolean refreshRequest);

    /**
     * Request users data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position in the recyclerview
     * containing list of users
     * @param visibleItemCount the visible item count in the recyclerview containing list of users
     * @param totalItemCount the total number of users in the recyclerview containing list of users
     */
    void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);
  }

  /**
   * The interface UsersContract.View to be implemented by UsersActivity{@link
   * io.isometrik.groupstreaming.ui.users.UsersActivity}
   *
   * @see io.isometrik.groupstreaming.ui.users.UsersActivity
   */
  interface View {

    /**
     * On users data received.
     *
     * @param users the list of users UsersModel{@link io.isometrik.groupstreaming.ui.users.UsersModel}
     * fetched
     * @param latestUsers whether the list of users fetched is for the first page or with paging
     * @see io.isometrik.groupstreaming.ui.users.UsersModel
     */
    void onUsersDataReceived(ArrayList<UsersModel> users, boolean latestUsers);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
