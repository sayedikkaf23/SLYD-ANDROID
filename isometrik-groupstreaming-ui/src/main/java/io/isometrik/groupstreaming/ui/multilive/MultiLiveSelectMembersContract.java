package io.isometrik.groupstreaming.ui.multilive;

import java.util.ArrayList;

/**
 * The interface select members contract for the multi live containing interfaces Presenter and View
 * to be implemented
 * by the
 * MultiLiveSelectMembersPresenter{@link MultiLiveSelectMembersPresenter} and
 * MultiLiveSelectMembersActivity{@link MultiLiveSelectMembersActivity} respectively.
 *
 * @see MultiLiveSelectMembersPresenter
 * @see MultiLiveSelectMembersActivity
 */
public interface MultiLiveSelectMembersContract {

  /**
   * The interface SelectMembersContract.Presenter to be implemented by SelectMembersPresenter{@link
   * MultiLiveSelectMembersPresenter}
   *
   * @see MultiLiveSelectMembersPresenter
   */
  interface Presenter {

    /**
     * Request users data.
     *
     * @param pageSize the page size
     * @param refreshRequest the refresh request
     */
    void requestUsersData(int pageSize, boolean refreshRequest);

    /**
     * Request users data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Start broadcast.
     *
     * @param streamDescription the stream description
     * @param streamImageUrl the stream image url
     * @param members the members
     * @param isPublic whether stream type is public or not
     */
    void startBroadcast(String streamDescription, String streamImageUrl,
        ArrayList<MultiLiveSelectMembersModel> members, boolean isPublic);
  }

  /**
   * The interface MultiLiveSelectMembersContract.View to be implemented by
   * MultiLiveSelectMembersActivity{@link
   * MultiLiveSelectMembersActivity}
   *
   * @see MultiLiveSelectMembersActivity
   */
  interface View {

    /**
     * On users data received.
     *
     * @param users the users
     * @param latestUsers the latest users
     */
    void onUsersDataReceived(ArrayList<MultiLiveSelectMembersModel> users, boolean latestUsers);

    /**
     * On broadcast started.
     *
     * @param streamId the stream id
     * @param streamDescription the stream description
     * @param streamImageUrl the stream image url
     * @param memberIds the member ids
     * @param startTime the start time
     * @param userId the user id
     */
    void onBroadcastStarted(String streamId, String streamDescription, String streamImageUrl,
        ArrayList<String> memberIds, long startTime, String userId);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);
  }
}
