package io.isometrik.groupstreaming.ui.requests;

import io.isometrik.groupstreaming.ui.utils.BasePresenter;
import java.util.ArrayList;

/**
 * The interface requests contract containing interfaces Presenter and View to be implemented
 * by the
 * RequestsPresenter{@link RequestsPresenter} and
 * RequestsBottomSheetFragment{@link RequestsFragment}
 * respectively.
 *
 * @see RequestsPresenter
 * @see RequestsFragment
 */
public interface RequestsContract {

  /**
   * The interface RequestsContract.Presenter implemented by RequestsPresenter{@link
   * RequestsPresenter}.
   *
   * @see RequestsPresenter
   */
  interface Presenter extends BasePresenter<RequestsContract.View> {

    /**
     * Request stream copublish request data.
     *
     * @param streamId the id of the stream whose list of request is to be fetched
     */
    void requestCopublishRequestsData(String streamId);

    /**
     * Register stream requests event listener.
     */
    void registerStreamRequestsEventListener();

    /**
     * Unregister stream requests event listener.
     */
    void unregisterStreamRequestsEventListener();

    /**
     * Accept copublish request.
     *
     * @param userId the id of the user who copublish request is to be accepted
     */
    void acceptCopublishRequest(String userId);

    /**
     * Decline copublish request.
     *
     * @param userId the id of the user who copublish request is to be declined
     */
    void declineCopublishRequest(String userId);

    /**
     * Initialize.
     *
     * @param streamId the id of the stream group for which to fetch the copublish requests
     * @param initiator whether given user is the initiator of the stream
     */
    void initialize(String streamId, boolean initiator);
  }

  /**
   * The interface RequestsContract.View implemented by the RequestsBottomSheetFragment{@link
   * RequestsFragment}.
   *
   * @see RequestsFragment
   */
  interface View {

    /**
     * On stream requests data received.
     *
     * @param requests the list of requests RequestsModel{@link RequestsModel}
     * in a stream group
     * @see RequestsModel
     */
    void onCopublishRequestsDataReceived(ArrayList<RequestsModel> requests);

    /**
     * On copublish request being successfully accepted.
     *
     * @param userId the id of the user whose copublish has been accepted
     */
    void onCopublishRequestAccepted(String userId);

    /**
     * On copublish request being successfully denied.
     *
     * @param userId the id of the user whose copublish has been declined
     */
    void onCopublishRequestDeclined(String userId);

    /**
     * Request removed from a stream group event.
     *
     * @param userId the id of the user who has requested to copublish
     */
    void removeRequestEvent(String userId);

    /**
     * Request added to a stream group event.
     *
     * @param requestsModel the requests model
     * @see RequestsModel
     */
    void addRequestEvent(RequestsModel requestsModel);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * On stream offline.
     *
     * @param message the message to be shown in the popup
     * @param dialogType the type od dialog whether for stream offline or being kicked out of
     * audience
     */
    void onStreamOffline(String message, int dialogType);
  }
}