package io.isometrik.groupstreaming.ui.streams.preview;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.copublish.CopublishContract;
import io.isometrik.groupstreaming.ui.streams.grid.StreamsModel;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.stream.FetchStreamsQuery;
import io.isometrik.gs.callbacks.ConnectionEventCallback;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.PresenceEventCallback;
import io.isometrik.gs.callbacks.StreamEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.enums.IMStreamType;
import io.isometrik.gs.events.connection.ConnectEvent;
import io.isometrik.gs.events.connection.ConnectionFailedEvent;
import io.isometrik.gs.events.connection.DisconnectEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.events.presence.PresenceStreamStartEvent;
import io.isometrik.gs.events.presence.PresenceStreamStopEvent;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.stream.FetchStreamsResult;
import io.isometrik.gs.rtcengine.utils.PreviewVideoGridContainer;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * The streams presenter to listen for the streams, connections, members, viewers and stream
 * presence
 * events.Also fetches the list of live stream groups along with paging.Adds/removes subscriptions
 * for stream presence events.
 *
 * It implements PreviewStreamsContract.Presenter{@link PreviewStreamsContract.Presenter}
 *
 * @see PreviewStreamsContract.Presenter
 */
public class PreviewStreamsPresenter implements PreviewStreamsContract.Presenter {

  /**
   * Instantiates a new streams presenter.
   */
  PreviewStreamsPresenter(PreviewStreamsContract.View streamsView) {
    this.streamsView = streamsView;
  }

  private PreviewStreamsContract.View streamsView;
  private String pageToken;
  private boolean isLastPage;
  private boolean isLoading;

  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  /**
   * @see ConnectionEventCallback
   */
  private ConnectionEventCallback connectionEventCallback = new ConnectionEventCallback() {
    @Override
    public void disconnected(@NotNull Isometrik isometrik,
        @NotNull DisconnectEvent disconnectEvent) {

      streamsView.connectionStateChanged(false);
    }

    @Override
    public void connected(@NotNull Isometrik isometrik, @NotNull ConnectEvent connectEvent) {

      streamsView.connectionStateChanged(true);
    }

    @Override
    public void connectionFailed(@NotNull Isometrik isometrik,
        @NotNull IsometrikError isometrikError) {

      streamsView.connectionStateChanged(false);
    }
    @Override
    public void failedToConnect(@NotNull Isometrik isometrik,
        @NotNull ConnectionFailedEvent connectionFailedEvent) {
      streamsView.connectionStateChanged(false);
    }
  };

  /**
   * @see CopublishEventCallback
   */
  private CopublishEventCallback copublishEventCallback = new CopublishEventCallback() {
    @Override
    public void copublishRequestAccepted(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAcceptEvent copublishRequestAcceptEvent) {
      if (copublishRequestAcceptEvent.getUserId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        streamsView.onCopublishRequestAccepted(copublishRequestAcceptEvent);
      }
    }

    @Override
    public void copublishRequestDenied(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestDenyEvent copublishRequestDenyEvent) {
      //TODO Nothing
    }

    @Override
    public void copublishRequestAdded(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAddEvent copublishRequestAddEvent) {
      //TODO Nothing
    }

    @Override
    public void copublishRequestRemoved(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
      //TODO Nothing
    }

    @Override
    public void switchProfile(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
      //TODO Nothing
    }
  };

  /**
   * @see StreamEventCallback
   */
  private StreamEventCallback streamEventCallback = new StreamEventCallback() {
    @Override
    public void streamStarted(@NotNull Isometrik isometrik,
        @NotNull StreamStartEvent streamStartEvent) {

      String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

      if (!userId.equals(streamStartEvent.getInitiatorId())) {

        boolean givenUserIsMember = false;
        List<StreamStartEvent.StreamMember> members = streamStartEvent.getMembers();

        ArrayList<String> memberIds = new ArrayList<>();
        String memberId;
        int size = members.size();
        for (int i = 0; i < size; i++) {
          memberId = members.get(i).getMemberId();
          memberIds.add(memberId);
          if (memberId.equals(userId)) {
            givenUserIsMember = true;
          }
        }

        streamsView.onStreamStarted(
            new StreamsModel(streamStartEvent.getStreamId(), streamStartEvent.getStreamImage(),
                streamStartEvent.getStreamDescription(), streamStartEvent.getMembersCount(), 0, 1,
                streamStartEvent.getInitiatorId(), streamStartEvent.getInitiatorName(),
                streamStartEvent.getInitiatorIdentifier(), streamStartEvent.getInitiatorImage(),
                streamStartEvent.getTimestamp(), memberIds, givenUserIsMember,
                streamStartEvent.isPublic()));
      }
    }

    @Override
    public void streamStopped(@NotNull Isometrik isometrik,
        @NotNull StreamStopEvent streamStopEvent) {

      if (!streamStopEvent.getInitiatorId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        streamsView.onStreamEnded(streamStopEvent.getStreamId());
      }
    }
  };

  /**
   * @see PresenceEventCallback
   */
  private PresenceEventCallback presenceEventCallback = new PresenceEventCallback() {
    @Override
    public void streamStarted(@NotNull Isometrik isometrik,
        @NotNull PresenceStreamStartEvent presenceStreamStartEvent) {

      String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

      if (!userId.equals(presenceStreamStartEvent.getInitiatorId())) {

        boolean givenUserIsMember = false;
        List<PresenceStreamStartEvent.StreamMember> members = presenceStreamStartEvent.getMembers();

        ArrayList<String> memberIds = new ArrayList<>();
        String memberId;
        int size = members.size();
        for (int i = 0; i < size; i++) {
          memberId = members.get(i).getMemberId();
          memberIds.add(memberId);
          if (memberId.equals(userId)) {
            givenUserIsMember = true;
          }
        }

        streamsView.onStreamStarted(new StreamsModel(presenceStreamStartEvent.getStreamId(),
            presenceStreamStartEvent.getStreamImage(),
            presenceStreamStartEvent.getStreamDescription(),
            presenceStreamStartEvent.getMembersCount(), 0, 1,
            presenceStreamStartEvent.getInitiatorId(), presenceStreamStartEvent.getInitiatorName(),
            presenceStreamStartEvent.getInitiatorIdentifier(),
            presenceStreamStartEvent.getInitiatorImage(), presenceStreamStartEvent.getTimestamp(),
            memberIds, givenUserIsMember, presenceStreamStartEvent.isPublic()));
      }
    }

    @Override
    public void streamStopped(@NotNull Isometrik isometrik,
        @NotNull PresenceStreamStopEvent presenceStreamStopEvent) {

      if (!presenceStreamStopEvent.getInitiatorId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        streamsView.onStreamEnded(presenceStreamStopEvent.getStreamId());
      }
    }
  };

  /**
   * @see MemberEventCallback
   */
  private MemberEventCallback memberEventCallback = new MemberEventCallback() {
    @Override
    public void memberAdded(@NotNull Isometrik isometrik, @NotNull MemberAddEvent memberAddEvent) {

      if (memberAddEvent.getMemberId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        streamsView.onMemberAdded(memberAddEvent, true);
      } else {

        streamsView.onMemberAdded(memberAddEvent, false);
      }
    }

    @Override
    public void memberLeft(@NotNull Isometrik isometrik,
        @NotNull MemberLeaveEvent memberLeaveEvent) {

      streamsView.onMemberLeft(memberLeaveEvent);
    }

    @Override
    public void memberRemoved(@NotNull Isometrik isometrik,
        @NotNull MemberRemoveEvent memberRemoveEvent) {

      if (memberRemoveEvent.getMemberId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        streamsView.onMemberRemoved(memberRemoveEvent, true);
      } else {

        streamsView.onMemberRemoved(memberRemoveEvent, false);
      }
    }

    @Override
    public void memberTimedOut(@NotNull Isometrik isometrik,
        @NotNull MemberTimeoutEvent memberTimeoutEvent) {
      streamsView.updateMembersAndViewersCount(memberTimeoutEvent.getStreamId(),
          memberTimeoutEvent.getMembersCount(), memberTimeoutEvent.getViewersCount());
    }

    @Override
    public void memberPublishStarted(@NotNull Isometrik isometrik,
        @NotNull PublishStartEvent publishStartEvent) {

      if (publishStartEvent.getMemberId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        streamsView.onPublishStarted(publishStartEvent, publishStartEvent.getMemberId());
      } else {

        streamsView.updateMembersAndViewersCount(publishStartEvent.getStreamId(),
            publishStartEvent.getMembersCount(), publishStartEvent.getViewersCount());
      }
    }

    @Override
    public void memberPublishStopped(@NotNull Isometrik isometrik,
        @NotNull PublishStopEvent publishStopEvent) {
      if (publishStopEvent.getMemberId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        streamsView.onPublishStopped(publishStopEvent, publishStopEvent.getMemberId());
      } else {

        streamsView.updateMembersAndViewersCount(publishStopEvent.getStreamId(),
            publishStopEvent.getMembersCount(), publishStopEvent.getViewersCount());
      }
    }

    @Override
    public void noMemberPublishing(@NotNull Isometrik isometrik,
        @NotNull NoPublisherLiveEvent noPublisherLiveEvent) {
      streamsView.onStreamEnded(noPublisherLiveEvent.getStreamId());
    }
  };

  /**
   * @see ViewerEventCallback
   */
  private ViewerEventCallback viewerEventCallback = new ViewerEventCallback() {
    @Override
    public void viewerJoined(@NotNull Isometrik isometrik,
        @NotNull ViewerJoinEvent viewerJoinEvent) {
      streamsView.updateMembersAndViewersCount(viewerJoinEvent.getStreamId(),
          viewerJoinEvent.getMembersCount(), viewerJoinEvent.getViewersCount());
    }

    @Override
    public void viewerLeft(@NotNull Isometrik isometrik,
        @NotNull ViewerLeaveEvent viewerLeaveEvent) {
      streamsView.updateMembersAndViewersCount(viewerLeaveEvent.getStreamId(),
          viewerLeaveEvent.getMembersCount(), viewerLeaveEvent.getViewersCount());
    }

    @Override
    public void viewerRemoved(@NotNull Isometrik isometrik,
        @NotNull ViewerRemoveEvent viewerRemoveEvent) {
      streamsView.updateMembersAndViewersCount(viewerRemoveEvent.getStreamId(),
          viewerRemoveEvent.getMembersCount(), viewerRemoveEvent.getViewersCount());
    }

    @Override
    public void viewerTimedOut(@NotNull Isometrik isometrik,
        @NotNull ViewerTimeoutEvent viewerTimeoutEvent) {
      streamsView.updateMembersAndViewersCount(viewerTimeoutEvent.getStreamId(),
          viewerTimeoutEvent.getMembersCount(), viewerTimeoutEvent.getViewersCount());
    }
  };

  /**
   * {@link PreviewStreamsContract.Presenter#registerConnectionEventListener()}
   */
  @Override
  public void registerConnectionEventListener() {
    isometrik.addConnectionEventListener(connectionEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#unregisterConnectionEventListener()}
   */
  @Override
  public void unregisterConnectionEventListener() {
    isometrik.removeConnectionEventListener(connectionEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#registerStreamsEventListener()}
   */
  @Override
  public void registerStreamsEventListener() {
    isometrik.addStreamEventListener(streamEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#unregisterStreamsEventListener()}
   */
  @Override
  public void unregisterStreamsEventListener() {
    isometrik.removeStreamEventListener(streamEventCallback);
  }

  /**
   * {@link CopublishContract.Presenter#registerCopublishRequestsEventListener()}
   */
  @Override
  public void registerCopublishRequestsEventListener() {
    isometrik.addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link CopublishContract.Presenter#unregisterCopublishRequestsEventListener()}
   */
  @Override
  public void unregisterCopublishRequestsEventListener() {
    isometrik.removeCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#registerPresenceEventListener()}
   */
  @Override
  public void registerPresenceEventListener() {
    isometrik.addPresenceEventListener(presenceEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#unregisterPresenceEventListener()}
   */
  @Override
  public void unregisterPresenceEventListener() {
    isometrik.removePresenceEventListener(presenceEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#registerStreamMembersEventListener()}
   */
  @Override
  public void registerStreamMembersEventListener() {
    isometrik.addMemberEventListener(memberEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#unregisterStreamMembersEventListener()}
   */
  @Override
  public void unregisterStreamMembersEventListener() {
    isometrik.removeMemberEventListener(memberEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#registerStreamViewersEventListener()}
   */
  @Override
  public void registerStreamViewersEventListener() {
    isometrik.addViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#unregisterStreamViewersEventListener()}
   */
  @Override
  public void unregisterStreamViewersEventListener() {
    isometrik.removeViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#requestLiveStreamsData(int, boolean)}
   */
  @Override
  public void requestLiveStreamsData(int pageSize, boolean refreshRequest) {
    isLoading = true;

    if (refreshRequest) {
      pageToken = null;
      isLastPage = false;
    }

    isometrik.fetchStreams(new FetchStreamsQuery.Builder().setCount(pageSize)
        .setPageToken(pageToken)
        .setStreamType(IMStreamType.IMAllStreams.getValue())
        .build(), (var1, var2) -> {

      if (var1 != null) {

        ArrayList<StreamsModel> streamsModels = new ArrayList<>();

        pageToken = var1.getPageToken();
        if (pageToken == null) {

          isLastPage = true;
        }

        String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

        ArrayList<FetchStreamsResult.Stream> streams = var1.getStreams();
        int size = streams.size();

        FetchStreamsResult.Stream stream;

        for (int i = 0; i < size; i++) {

          stream = streams.get(i);
          if (!userId.equals(stream.getInitiatorId())) {

            boolean givenUserIsMember = false;
            ArrayList<String> membersIds = stream.getMemberIds();

            for (int j = 0; j < membersIds.size(); j++) {

              if (membersIds.get(j).equals(userId)) {
                givenUserIsMember = true;
                break;
              }
            }

            streamsModels.add(new StreamsModel(stream, givenUserIsMember));
          }
        }

        streamsView.onLiveStreamsDataReceived(streamsModels, refreshRequest, true);
      } else {

        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

          if (refreshRequest) {
            //No streams found
            streamsView.onLiveStreamsDataReceived(new ArrayList<>(), true, true);
          } else {
            isLastPage = true;
          }
        } else {
          streamsView.onError(var2.getErrorMessage());
        }
      }
      isLoading = false;
    });
  }

  /**
   * {@link PreviewStreamsContract.Presenter#requestLiveStreamsDataOnScroll(int, int, int)}
   */
  @Override
  public void requestLiveStreamsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= Constants.STREAMS_PAGE_SIZE) {
        requestLiveStreamsData(Constants.STREAMS_PAGE_SIZE, false);
      }
    }
  }

  /**
   * {@link PreviewStreamsContract.Presenter#joinBroadcastPreview(String,
   * PreviewVideoGridContainer, boolean)}
   */
  @Override
  public void joinBroadcastPreview(String streamId,
      PreviewVideoGridContainer previewVideoGridContainer, boolean joinedChannel) {
    if (joinedChannel) {
      IsometrikUiSdk.getInstance()
          .getIsometrik()
          .getBroadcastOperations()
          .switchPreviewBroadcast(streamId, null);
    } else {
      IsometrikUiSdk.getInstance()
          .getIsometrik()
          .getBroadcastOperations()
          .joinPreviewBroadcast(streamId, IsometrikUiSdk.getInstance().getUserSession().getUserId(),
              null, previewVideoGridContainer);
    }

    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .updatePreviewRemoteMediaSettings(true);
  }

  /**
   * {@link PreviewStreamsContract.Presenter#stopBroadcastPreview(PreviewVideoGridContainer,
   * boolean)}
   */
  @Override
  public void stopBroadcastPreview(PreviewVideoGridContainer previewVideoGridContainer,
      boolean clearPreview) {
    IsometrikUiSdk.getInstance()
        .getIsometrik()
        .getBroadcastOperations()
        .updatePreviewRemoteMediaSettings(false);
    if (clearPreview) previewVideoGridContainer.release();
  }

  /**
   * {@link PreviewStreamsContract.Presenter#endBroadcastPreview()}
   */
  @Override
  public void endBroadcastPreview() {
    IsometrikUiSdk.getInstance().getIsometrik().getBroadcastOperations().leavePreviewBroadcast();
  }
}
