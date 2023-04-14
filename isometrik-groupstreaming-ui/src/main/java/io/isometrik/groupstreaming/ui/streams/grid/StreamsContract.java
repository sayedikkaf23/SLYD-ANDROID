package io.isometrik.groupstreaming.ui.streams.grid;

import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import java.util.ArrayList;

/**
 * The interface streams contract containing interfaces Presenter and View to be implemented
 * by the
 * StreamsPresenter{@link StreamsPresenter} and
 * StreamsActivity{@link StreamsActivity} respectively.
 *
 * @see StreamsPresenter
 * @see StreamsActivity
 */
public interface StreamsContract {

  /**
   * The interface StreamsContract.Presenter to be implemented by StreamsPresenter{@link
   * StreamsPresenter}
   *
   * @see StreamsPresenter
   */
  interface Presenter {

    /**
     * Request live streams data.
     *
     * @param pageSize the page size
     * @param refreshRequest the refresh request
     */
    void requestLiveStreamsData(int pageSize, boolean refreshRequest);

    /**
     * Request live streams data on scroll.
     *
     * @param firstVisibleItemPosition the first visible item position
     * @param visibleItemCount the visible item count
     * @param totalItemCount the total item count
     */
    void requestLiveStreamsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    /**
     * Register connection event listener.
     */
    void registerConnectionEventListener();

    /**
     * Unregister connection event listener.
     */
    void unregisterConnectionEventListener();

    /**
     * Register streams event listener.
     */
    void registerStreamsEventListener();

    /**
     * Unregister streams event listener.
     */
    void unregisterStreamsEventListener();

    /**
     * Register copublish requests event listener.
     */
    void registerCopublishRequestsEventListener();

    /**
     * Unregister copublish requests event listener.
     */
    void unregisterCopublishRequestsEventListener();

    /**
     * Register presence event listener.
     */
    void registerPresenceEventListener();

    /**
     * Unregister presence event listener.
     */
    void unregisterPresenceEventListener();

    /**
     * Register stream members event listener.
     */
    void registerStreamMembersEventListener();

    /**
     * Unregister stream members event listener.
     */
    void unregisterStreamMembersEventListener();

    /**
     * Register stream viewers event listener.
     */
    void registerStreamViewersEventListener();

    /**
     * Unregister stream viewers event listener.
     */
    void unregisterStreamViewersEventListener();

    /**
     * Add subscription.
     *
     * @param streamStartEvents the stream start events
     */
    void addSubscription(boolean streamStartEvents);

    /**
     * Remove subscription.
     *
     * @param streamStartEvents the stream start events
     */
    void removeSubscription(boolean streamStartEvents);

    /**
     * Update user publish status.
     */
    void updateUserPublishStatus();
  }

  /**
   * The interface StreamsContract.View to be implemented by StreamsActivity{@link
   * StreamsActivity}
   *
   * @see StreamsActivity
   */
  interface View {

    /**
     * On live streams data received.
     *
     * @param streams the streams
     * @param latestStreams the latest streams
     */
    void onLiveStreamsDataReceived(ArrayList<StreamsModel> streams, boolean latestStreams);

    /**
     * Update members and viewers count.
     *
     * @param streamId the stream id
     * @param membersCount the members count
     * @param viewersCount the viewers count
     */
    void updateMembersAndViewersCount(String streamId, int membersCount, int viewersCount);

    /**
     * On member added.
     *
     * @param memberAddEvent the member add event
     * @param givenMemberAdded the given member added
     */
    void onMemberAdded(MemberAddEvent memberAddEvent, boolean givenMemberAdded);

    /**
     * On member removed.
     *
     * @param memberRemoveEvent the member remove event
     * @param givenMemberRemoved the given member removed
     */
    void onMemberRemoved(MemberRemoveEvent memberRemoveEvent, boolean givenMemberRemoved);

    /**
     * On member left.
     *
     * @param memberLeftEvent the member left event
     */
    void onMemberLeft(MemberLeaveEvent memberLeftEvent);

    /**
     * On stream ended.
     *
     * @param streamId the stream id
     */
    void onStreamEnded(String streamId);

    /**
     * On stream started.
     *
     * @param streamsModel the streams model
     */
    void onStreamStarted(StreamsModel streamsModel);

    /**
     * On publish started.
     *
     * @param publishStartEvent the publish start event
     * @param userId the user id
     */
    void onPublishStarted(PublishStartEvent publishStartEvent, String userId);

    /**
     * On publish stopped.
     *
     * @param publishStopEvent the publish stop event
     * @param userId the user id
     */
    void onPublishStopped(PublishStopEvent publishStopEvent, String userId);

    /**
     * On error.
     *
     * @param errorMessage the error message to be shown in the toast for details of the failed
     * operation
     */
    void onError(String errorMessage);

    /**
     * Connection state changed.
     *
     * @param connected whether connection to receive realtime events has been made or broken
     */
    void connectionStateChanged(boolean connected);

    /**
     * On copublish request accepted.
     *
     * @param copublishRequestAcceptEvent the copublish request accepted event
     */
    void onCopublishRequestAccepted(CopublishRequestAcceptEvent copublishRequestAcceptEvent);

    /**
     * Failed to connect.
     *
     * @param errorMessage the error message
     */
    void failedToConnect(String errorMessage);
  }
}
