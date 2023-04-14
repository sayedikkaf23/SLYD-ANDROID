package io.isometrik.groupstreaming.ui.viewers;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.utils.StreamDialogEnum;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.viewer.FetchViewersQuery;
import io.isometrik.gs.builder.viewer.RemoveViewerQuery;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
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
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The viewers presenter listens for the viewers, members events.Fetches list
 * of viewers in a stream group.Removes a viewer from a stream group.
 * It implements ViewersContract.Presenter{@link ViewersContract.Presenter}
 *
 * @see ViewersContract.Presenter
 */
public class ViewersPresenter implements ViewersContract.Presenter {

  /**
   * Instantiates a new viewers presenter.
   */
  ViewersPresenter() {
  }

  private ViewersContract.View viewersView;

  private String streamId = "";
  private ArrayList<String> memberIds = new ArrayList<>();
  private boolean isLoading;
  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  @Override
  public void attachView(ViewersContract.View viewersView) {
    this.viewersView = viewersView;
  }

  @Override
  public void detachView() {
    this.viewersView = null;
  }

  /**
   * {@link ViewersContract.Presenter#initialize(String, ArrayList)}
   */
  @Override
  public void initialize(String streamId, ArrayList<String> memberIds) {

    this.memberIds = memberIds;
    this.streamId = streamId;
  }

  /**
   * @see ViewerEventCallback
   */
  private ViewerEventCallback viewerEventCallback = new ViewerEventCallback() {
    @Override
    public void viewerJoined(@NotNull Isometrik isometrik,
        @NotNull ViewerJoinEvent viewerJoinEvent) {

      if (viewerJoinEvent.getStreamId().equals(streamId)) {

        boolean givenUserIsMember = false;
        boolean joinedViewerIsMember = false;

        if (memberIds.contains(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
          givenUserIsMember = true;
        }
        if (memberIds.contains(viewerJoinEvent.getViewerId())) {
          joinedViewerIsMember = true;
        }
        if (viewersView != null) {
          viewersView.addViewerEvent(
              new ViewersModel(viewerJoinEvent, joinedViewerIsMember, givenUserIsMember),
              viewerJoinEvent.getViewersCount());
        }
      }
    }

    @Override
    public void viewerLeft(@NotNull Isometrik isometrik,
        @NotNull ViewerLeaveEvent viewerLeaveEvent) {

      if (viewerLeaveEvent.getStreamId().equals(streamId)) {
        if (viewersView != null) {
          viewersView.removeViewerEvent(viewerLeaveEvent.getViewerId(),
              viewerLeaveEvent.getViewersCount());
        }
      }
    }

    @Override
    public void viewerRemoved(@NotNull Isometrik isometrik,
        @NotNull ViewerRemoveEvent viewerRemoveEvent) {
      if (viewerRemoveEvent.getStreamId().equals(streamId)) {

        if (viewerRemoveEvent.getViewerId()
            .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
          if (viewersView != null) {
            viewersView.onStreamOffline(IsometrikUiSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_viewer_kicked_out, viewerRemoveEvent.getInitiatorName()),
                StreamDialogEnum.KickedOut.getValue());
          }
        } else {
          if (viewersView != null) {
            viewersView.removeViewerEvent(viewerRemoveEvent.getViewerId(),
                viewerRemoveEvent.getViewersCount());
          }
        }
      }
    }

    @Override
    public void viewerTimedOut(@NotNull Isometrik isometrik,
        @NotNull ViewerTimeoutEvent viewerTimeoutEvent) {
      if (viewerTimeoutEvent.getStreamId().equals(streamId)) {
        if (viewersView != null) {
          viewersView.removeViewerEvent(viewerTimeoutEvent.getViewerId(),
              viewerTimeoutEvent.getViewersCount());
        }
      }
    }
  };

  /**
   * @see CopublishEventCallback
   */
  private CopublishEventCallback copublishEventCallback = new CopublishEventCallback() {
    @Override
    public void copublishRequestAccepted(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAcceptEvent copublishRequestAcceptEvent) {
      //Todo Nothing
    }

    @Override
    public void copublishRequestDenied(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestDenyEvent copublishRequestDenyEvent) {
      //Todo Nothing
    }

    @Override
    public void copublishRequestAdded(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAddEvent copublishRequestAddEvent) {
      //Todo Nothing
    }

    @Override
    public void copublishRequestRemoved(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
      //Todo Nothing
    }

    @Override
    public void switchProfile(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
      if (copublishRequestSwitchProfileEvent.getStreamId().equals(streamId)) {

        if (viewersView != null) {
          viewersView.onProfileSwitched(copublishRequestSwitchProfileEvent.getUserId());
        }
      }
    }
  };

  /**
   * @see MemberEventCallback
   */
  private MemberEventCallback memberEventCallback = new MemberEventCallback() {
    @Override
    public void memberAdded(@NotNull Isometrik isometrik, @NotNull MemberAddEvent memberAddEvent) {

      if (memberAddEvent.getStreamId().equals(streamId)) {

        if (!memberIds.contains(memberAddEvent.getMemberId())) {
          memberIds.add(memberAddEvent.getMemberId());
        }
      }
    }

    @Override
    public void memberLeft(@NotNull Isometrik isometrik,
        @NotNull MemberLeaveEvent memberLeaveEvent) {
      if (memberLeaveEvent.getStreamId().equals(streamId)) {

        memberIds.remove(memberLeaveEvent.getMemberId());
      }
    }

    @Override
    public void memberRemoved(@NotNull Isometrik isometrik,
        @NotNull MemberRemoveEvent memberRemoveEvent) {
      if (memberRemoveEvent.getStreamId().equals(streamId)) {

        memberIds.remove(memberRemoveEvent.getMemberId());

        if (memberRemoveEvent.getMemberId()
            .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
          if (viewersView != null) {
            viewersView.onStreamOffline(IsometrikUiSdk.getInstance()
                    .getContext()
                    .getString(R.string.ism_member_kicked_out, memberRemoveEvent.getInitiatorName()),
                StreamDialogEnum.KickedOut.getValue());
          }
        }
      }
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
        if (viewersView != null) {
          viewersView.onStreamOffline(IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_stream_offline_no_publisher),
              StreamDialogEnum.StreamOffline.getValue());
        }
      }
    }
  };

  /**
   * {@link ViewersContract.Presenter#registerStreamViewersEventListener()}
   */
  @Override
  public void registerStreamViewersEventListener() {
    isometrik.addViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#unregisterStreamViewersEventListener()}
   */
  @Override
  public void unregisterStreamViewersEventListener() {
    isometrik.removeViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#registerStreamMembersEventListener()}
   */
  @Override
  public void registerStreamMembersEventListener() {
    isometrik.addMemberEventListener(memberEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#unregisterStreamMembersEventListener()}
   */
  @Override
  public void unregisterStreamMembersEventListener() {
    isometrik.removeMemberEventListener(memberEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#registerCopublishRequestsEventListener()}
   */
  @Override
  public void registerCopublishRequestsEventListener() {
    isometrik.addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#unregisterCopublishRequestsEventListener()}
   */
  @Override
  public void unregisterCopublishRequestsEventListener() {
    isometrik.removeCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link ViewersContract.Presenter#requestStreamViewersData(String, ArrayList)}
   */
  @Override
  public void requestStreamViewersData(String streamId, ArrayList<String> memberIds) {

    if (!isLoading) {
      isLoading = true;

      isometrik.fetchViewers(new FetchViewersQuery.Builder().setStreamId(streamId).build(),
          (var1, var2) -> {

            isLoading = false;

            if (var1 != null) {

              ArrayList<ViewersModel> viewersModels = new ArrayList<>();

              ArrayList<FetchViewersResult.StreamViewer> viewers = var1.getStreamViewers();
              int size = viewers.size();

              FetchViewersResult.StreamViewer viewer;

              for (int i = 0; i < size; i++) {

                viewer = viewers.get(i);

                boolean givenUserIsMember = false;

                if (this.memberIds.contains(
                    IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
                  givenUserIsMember = true;
                }

                boolean givenViewerIsMember = false;

                if (this.memberIds.contains(viewer.getViewerId())) {
                  givenViewerIsMember = true;
                }

                viewersModels.add(new ViewersModel(viewer, givenViewerIsMember, givenUserIsMember));
              }
              if (viewersView != null) {
                viewersView.onStreamViewersDataReceived(viewersModels);
              }
            } else {
              if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {
                if (viewersView != null) {   //No viewers found
                  viewersView.onStreamViewersDataReceived(new ArrayList<>());
                }
              } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
                if (viewersView != null) {  //Stream not live anymore
                  viewersView.onStreamOffline(IsometrikUiSdk.getInstance()
                          .getContext()
                          .getString(R.string.ism_stream_offline),
                      StreamDialogEnum.StreamOffline.getValue());
                }
              } else {
                if (viewersView != null) {
                  viewersView.onError(var2.getErrorMessage());
                }
              }
            }
          });
    }
  }

  /**
   * {@link ViewersContract.Presenter#requestRemoveViewer(String)}
   */
  @Override
  public void requestRemoveViewer(String viewerId) {

    isometrik.removeViewer(new RemoveViewerQuery.Builder().setStreamId(streamId)
        .setViewerId(viewerId)
        .setInitiatorId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (viewersView != null) {
          viewersView.onViewerRemovedResult(viewerId);
        }
      } else {
        //Stream not live anymore
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
          if (viewersView != null) {
            viewersView.onStreamOffline(
                IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                StreamDialogEnum.StreamOffline.getValue());
          }
        } else {
          if (viewersView != null) {
            viewersView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }
}
