package io.isometrik.groupstreaming.ui.streams.preview;

import io.isometrik.groupstreaming.ui.streams.grid.StreamsModel;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.rtcengine.utils.PreviewVideoGridContainer;
import java.util.ArrayList;

/**
 * The interface preview streams contract containing interfaces Presenter and View to be implemented
 * by the
 * PreviewStreamsPresenter{@link PreviewStreamsPresenter} and
 * PreviewStreamsActivity{@link PreviewStreamsActivity} respectively.
 *
 * @see PreviewStreamsPresenter
 * @see PreviewStreamsActivity
 */
public interface PreviewStreamsContract {

  /**
   * The interface StreamsContract.Presenter to be implemented by PreviewStreamsPresenter{@link
   * PreviewStreamsPresenter}
   *
   * @see PreviewStreamsPresenter
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
     * Join a broadcast for preview.
     *
     * @param streamId id of the broadcast for which to start preview
     * @param previewVideoGridContainer preview video grid container
     * @param joinedChannel whether already joined a channel
     */
    void joinBroadcastPreview(String streamId, PreviewVideoGridContainer previewVideoGridContainer,
        boolean joinedChannel);

    /**
     * Leave a broadcast after preview.
     *
     * @param previewVideoGridContainer preview video grid container
     * @param clearPreview whether to clear preview or not
     */
    void stopBroadcastPreview(PreviewVideoGridContainer previewVideoGridContainer,
        boolean clearPreview);

    /**
     * Ends the preview for any of the broadcast.
     */
    void endBroadcastPreview();
  }

  /**
   * The interface PreviewStreamsContract.View to be implemented by PreviewStreamsActivity{@link
   * PreviewStreamsActivity}
   *
   * @see PreviewStreamsActivity
   */
  interface View {

    /**
     * On live streams data received.
     *
     * @param streams the streams
     * @param latestStreams the latest streams
     * @param apiResponse whether response if from api or not
     */
    void onLiveStreamsDataReceived(ArrayList<StreamsModel> streams, boolean latestStreams,
        boolean apiResponse);

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
  }
}
