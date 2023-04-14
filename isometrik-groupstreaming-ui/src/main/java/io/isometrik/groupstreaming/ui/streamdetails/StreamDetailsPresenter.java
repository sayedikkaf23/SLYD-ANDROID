package io.isometrik.groupstreaming.ui.streamdetails;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.utils.StreamDialogEnum;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.callbacks.ConnectionEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.StreamEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.events.connection.ConnectEvent;
import io.isometrik.gs.events.connection.ConnectionFailedEvent;
import io.isometrik.gs.events.connection.DisconnectEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.response.error.IsometrikError;
import org.jetbrains.annotations.NotNull;

/**
 * The stream details presenter to listen for the connections, viewers, members, streams event.
 * It implements StreamDetailsContract.Presenter{@link StreamDetailsContract.Presenter}
 *
 * @see StreamDetailsContract.Presenter
 */
public class StreamDetailsPresenter implements StreamDetailsContract.Presenter {

  /**
   * Instantiates a new stream details presenter.
   */
  StreamDetailsPresenter(StreamDetailsContract.View streamDetailsView) {
    this.streamDetailsView = streamDetailsView;
  }

  private StreamDetailsContract.View streamDetailsView;
  private String streamId;

  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  @Override
  public void initialize(String streamId) {
    this.streamId = streamId;
  }

  /**
   * @see io.isometrik.gs.callbacks.ConnectionEventCallback
   */
  private ConnectionEventCallback connectionEventCallback = new ConnectionEventCallback() {
    @Override
    public void disconnected(@NotNull Isometrik isometrik,
        @NotNull DisconnectEvent disconnectEvent) {

      streamDetailsView.connectionStateChanged(false);
    }

    @Override
    public void connected(@NotNull Isometrik isometrik, @NotNull ConnectEvent connectEvent) {

      streamDetailsView.connectionStateChanged(true);
    }

    @Override
    public void connectionFailed(@NotNull Isometrik isometrik,
        @NotNull IsometrikError isometrikError) {

      streamDetailsView.connectionStateChanged(false);
    }
    @Override
    public void failedToConnect(@NotNull Isometrik isometrik,
        @NotNull ConnectionFailedEvent connectionFailedEvent) {
      streamDetailsView.connectionStateChanged(false);
    }
  };

  /**
   * @see io.isometrik.gs.callbacks.ViewerEventCallback
   */
  private ViewerEventCallback viewerEventCallback = new ViewerEventCallback() {
    @Override
    public void viewerJoined(@NotNull Isometrik isometrik,
        @NotNull ViewerJoinEvent viewerJoinEvent) {
      //TODO Nothing
    }

    @Override
    public void viewerLeft(@NotNull Isometrik isometrik,
        @NotNull ViewerLeaveEvent viewerLeaveEvent) {
      //TODO Nothing
    }

    @Override
    public void viewerRemoved(@NotNull Isometrik isometrik,
        @NotNull ViewerRemoveEvent viewerRemoveEvent) {
      if (viewerRemoveEvent.getStreamId().equals(streamId)) {

        if (viewerRemoveEvent.getViewerId()
            .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

          streamDetailsView.onStreamOffline(IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_viewer_kicked_out, viewerRemoveEvent.getInitiatorName()),
              StreamDialogEnum.KickedOut.getValue());
        }
      }
    }

    @Override
    public void viewerTimedOut(@NotNull Isometrik isometrik,
        @NotNull ViewerTimeoutEvent viewerTimeoutEvent) {
      //TODO Nothing
    }
  };

  /**
   * @see io.isometrik.gs.callbacks.StreamEventCallback
   */
  private StreamEventCallback streamEventCallback = new StreamEventCallback() {
    @Override
    public void streamStarted(@NotNull Isometrik isometrik,
        @NotNull StreamStartEvent streamStartEvent) {
      //TODO Nothing
    }

    @Override
    public void streamStopped(@NotNull Isometrik isometrik,
        @NotNull StreamStopEvent streamStopEvent) {
      if (streamStopEvent.getStreamId().equals(streamId)) {
        streamDetailsView.onStreamOffline(IsometrikUiSdk.getInstance()
                .getContext()
                .getString(R.string.ism_stream_offline_initiator_stop),
            StreamDialogEnum.StreamOffline.getValue());
      }
    }
  };

  /**
   * @see io.isometrik.gs.callbacks.MemberEventCallback
   */
  private MemberEventCallback memberEventCallback = new MemberEventCallback() {
    @Override
    public void memberAdded(@NotNull Isometrik isometrik, @NotNull MemberAddEvent memberAddEvent) {
      //TODO nothing
    }

    @Override
    public void memberLeft(@NotNull Isometrik isometrik,
        @NotNull MemberLeaveEvent memberLeaveEvent) {
      //TODO nothing
    }

    @Override
    public void memberRemoved(@NotNull Isometrik isometrik,
        @NotNull MemberRemoveEvent memberRemoveEvent) {
      //TODO nothing
    }

    @Override
    public void memberTimedOut(@NotNull Isometrik isometrik,
        @NotNull MemberTimeoutEvent memberTimeoutEvent) {
      //TODO nothing
    }

    @Override
    public void memberPublishStarted(@NotNull Isometrik isometrik,
        @NotNull PublishStartEvent publishStartEvent) {
      //TODO nothing
    }

    @Override
    public void memberPublishStopped(@NotNull Isometrik isometrik,
        @NotNull PublishStopEvent publishStopEvent) {
      //TODO nothing
    }

    @Override
    public void noMemberPublishing(@NotNull Isometrik isometrik,
        @NotNull NoPublisherLiveEvent noPublisherLiveEvent) {
      if (noPublisherLiveEvent.getStreamId().equals(streamId)) {
        streamDetailsView.onStreamOffline(IsometrikUiSdk.getInstance()
                .getContext()
                .getString(R.string.ism_stream_offline_no_publisher),
            StreamDialogEnum.StreamOffline.getValue());
      }
    }
  };

  /**
   * {@link StreamDetailsContract.Presenter#registerConnectionEventListener()}
   */
  @Override
  public void registerConnectionEventListener() {
    isometrik.addConnectionEventListener(connectionEventCallback);
  }

  /**
   * {@link StreamDetailsContract.Presenter#unregisterConnectionEventListener()}
   */
  @Override
  public void unregisterConnectionEventListener() {
    isometrik.removeConnectionEventListener(connectionEventCallback);
  }

  /**
   * {@link StreamDetailsContract.Presenter#registerStreamsEventListener()}
   */
  @Override
  public void registerStreamsEventListener() {
    isometrik.addStreamEventListener(streamEventCallback);
  }

  /**
   * {@link StreamDetailsContract.Presenter#unregisterStreamsEventListener()}
   */
  @Override
  public void unregisterStreamsEventListener() {
    isometrik.removeStreamEventListener(streamEventCallback);
  }

  /**
   * {@link StreamDetailsContract.Presenter#registerStreamViewersEventListener()}
   */
  @Override
  public void registerStreamViewersEventListener() {
    isometrik.addViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link StreamDetailsContract.Presenter#unregisterStreamViewersEventListener()}
   */
  @Override
  public void unregisterStreamViewersEventListener() {
    isometrik.removeViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link StreamDetailsContract.Presenter#registerStreamMembersEventListener()}
   */
  @Override
  public void registerStreamMembersEventListener() {
    isometrik.addMemberEventListener(memberEventCallback);
  }

  /**
   * {@link StreamDetailsContract.Presenter#unregisterStreamMembersEventListener()}
   */
  @Override
  public void unregisterStreamMembersEventListener() {
    isometrik.removeMemberEventListener(memberEventCallback);
  }
}
