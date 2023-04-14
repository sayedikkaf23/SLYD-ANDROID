package io.isometrik.groupstreaming.ui.members.list;

import io.isometrik.groupstreaming.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface members contract containing interfaces Presenter and View to be implemented
 * by the
 * MembersPresenter{@link MembersPresenter} and
 * MembersBottomSheetFragment{@link MembersFragment} respectively.
 *
 * @see MembersPresenter
 * @see MembersFragment
 */
public interface MembersContract {

  /**
   * The interface MembersContract.Presenter to be implemented by MembersPresenter{@link
   * MembersPresenter}
   *
   * @see MembersPresenter
   */
  interface Presenter extends BasePresenter<View> {

    /**
     * Request stream members data.
     *
     * @param streamId the stream id
     * @param isAdmin the is admin
     */
    void requestStreamMembersData(String streamId, boolean isAdmin);

    /**
     * Register copublish requests event listener.
     */
    void registerCopublishRequestsEventListener();

    /**
     * Unregister copublish requests event listener.
     */
    void unregisterCopublishRequestsEventListener();

    /**
     * Register stream members event listener.
     */
    void registerStreamMembersEventListener();

    /**
     * Unregister stream members event listener.
     */
    void unregisterStreamMembersEventListener();

    /**
     * Request remove member.
     *
     * @param memberId the member id
     */
    void requestRemoveMember(String memberId);

    /**
     * Initialize.
     *
     * @param streamId the stream id
     * @param isAdmin the is admin
     */
    void initialize(String streamId, boolean isAdmin);

    /**
     * Gets member ids.
     *
     * @param members the members
     * @return the member ids
     */
    ArrayList<String> getMemberIds(ArrayList<MembersModel> members);
  }

  /**
   * The interface MembersContract.View to be implemented by MembersBottomSheetFragment{@link
   * MembersFragment}
   *
   * @see MembersFragment
   */
  interface View {

    /**
     * On stream members data received.
     *
     * @param members the members
     */
    void onStreamMembersDataReceived(ArrayList<MembersModel> members);

    /**
     * On member removed result.
     *
     * @param memberId the member id
     */
    void onMemberRemovedResult(String memberId);

    /**
     * Add member event.
     *
     * @param membersModel the members model
     * @param membersCount the members count
     */
    void addMemberEvent(MembersModel membersModel, int membersCount);

    /**
     * Remove member event.
     *
     * @param memberId the member id
     * @param membersCount the members count
     */
    void removeMemberEvent(String memberId, int membersCount);

    /**
     * Publish status changed.
     *
     * @param memberId the member id
     * @param publishing the publishing
     * @param membersCount the members count
     */
    void publishStatusChanged(String memberId, boolean publishing, int membersCount,
        String timestamp);

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
     * @param memberId the member id
     * @param membersCount the members count
     */
    void onProfileSwitched(String memberId, int membersCount, String timestamp);
  }
}
