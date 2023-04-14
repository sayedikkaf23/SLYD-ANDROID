package io.isometrik.groupstreaming.ui.members.add;

import io.isometrik.groupstreaming.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface members contract containing interfaces Presenter and View to be implemented
 * by the
 * MembersPresenter{@link AddMembersPresenter} and
 * MembersBottomSheetFragment{@link AddMembersFragment} respectively.
 *
 * @see AddMembersPresenter
 * @see AddMembersFragment
 */
public interface AddMembersContract {

  /**
   * The interface MembersContract.Presenter to be implemented by MembersPresenter{@link
   * AddMembersPresenter}
   *
   * @see AddMembersPresenter
   */
  interface Presenter extends BasePresenter<View> {
    /**
     * Initialize.
     *
     * @param streamId the stream id
     * @param memberIds the member ids
     */
    void initialize(String streamId, ArrayList<String> memberIds);

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
     * Request stream viewers data.
     *
     * @param streamId the id of the stream whose list of viewers is to be fetched
     * @param memberIds the list containing ids of the members of the stream group
     */
    void requestStreamViewersData(String streamId, ArrayList<String> memberIds);

    /**
     * Register stream viewers event listener.
     */
    void registerStreamViewersEventListener();

    /**
     * Unregister stream viewers event listener.
     */
    void unregisterStreamViewersEventListener();

    /**
     * Register copublish requests event listener.
     */
    void registerCopublishRequestsEventListener();

    /**
     * Unregister copublish requests event listener.
     */
    void unregisterCopublishRequestsEventListener();

    /**
     * Add member.
     *
     * @param memberId the member id
     * @param streamId the stream id
     */
    void addMember(String memberId, String streamId);
  }

  /**
   * The interface MembersContract.View to be implemented by MembersBottomSheetFragment{@link
   * AddMembersFragment}
   *
   * @see AddMembersFragment
   */
  interface View {
    /**
     * On users data received.
     *
     * @param users the users
     * @param latestUsers the latest users
     */
    void onUsersDataReceived(ArrayList<AddMembersModel> users, boolean latestUsers);

    /**
     * On member added.
     *
     * @param memberId the member id
     */
    void onMemberAdded(String memberId);

    /**
     * On stream viewers data received.
     *
     * @param viewers the list of viewers AddMembersModel{@link AddMembersModel}
     * in a stream group
     * @see AddMembersModel
     */
    void onStreamViewersDataReceived(ArrayList<AddMembersModel> viewers);

    /**
     * Viewer removed from a stream group event.
     *
     * @param viewerId the id of the viewer to be added to a stream group
     */
    void removeViewerEvent(String viewerId);

    /**
     * Viewer added to a stream group event.
     *
     * @param addMembersModel the add members model
     * @see AddMembersModel
     */
    void addViewerEvent(AddMembersModel addMembersModel);

    /**
     * On stream offline.
     *
     * @param message the message
     * @param dialogType the dialog type
     */
    void onStreamOffline(String message, int dialogType);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On switch of profile from viewer to broadcaster.
     *
     * @param viewerId the id of the viewer who switched profile in the stream group
     */
    void onProfileSwitched(String viewerId);
  }
}
