package io.isometrik.groupstreaming.ui.members.add;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.groupstreaming.ui.utils.StreamDialogEnum;
import io.isometrik.groupstreaming.ui.viewers.ViewersContract;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.member.AddMemberQuery;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.builder.viewer.FetchViewersQuery;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.response.user.FetchUsersResult;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The members presenter to listen for the members and to fetch
 * list of members in a stream group and to remove a member from the stream group.
 * It implements MembersContract.Presenter{@link AddMembersContract.Presenter}
 *
 * @see AddMembersContract.Presenter
 */
public class AddMembersPresenter implements AddMembersContract.Presenter {

  /**
   * Instantiates a new add member presenter.
   */
  AddMembersPresenter() {

  }

  private AddMembersContract.View addMemberView;
  private String pageToken;
  private boolean isLastPage;
  private boolean isLoading;
  private String streamId;
  private ArrayList<String> memberIds;

  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  private int count;

  @Override
  public void initialize(String streamId, ArrayList<String> memberIds) {
    this.streamId = streamId;
    this.memberIds = memberIds;
  }

  /**
   * @see ViewerEventCallback
   */
  private ViewerEventCallback viewerEventCallback = new ViewerEventCallback() {
    @Override
    public void viewerJoined(@NotNull Isometrik isometrik,
        @NotNull ViewerJoinEvent viewerJoinEvent) {

      if (viewerJoinEvent.getStreamId().equals(streamId)) {

        boolean joinedViewerIsMember = false;

        if (memberIds.contains(viewerJoinEvent.getViewerId())) {
          joinedViewerIsMember = true;
        }
        if (addMemberView != null) {
          addMemberView.addViewerEvent(new AddMembersModel(viewerJoinEvent, joinedViewerIsMember));
        }
      }
    }

    @Override
    public void viewerLeft(@NotNull Isometrik isometrik,
        @NotNull ViewerLeaveEvent viewerLeaveEvent) {

      if (viewerLeaveEvent.getStreamId().equals(streamId)) {
        if (addMemberView != null) {
          addMemberView.removeViewerEvent(viewerLeaveEvent.getViewerId());
        }
      }
    }

    @Override
    public void viewerRemoved(@NotNull Isometrik isometrik,
        @NotNull ViewerRemoveEvent viewerRemoveEvent) {
      if (viewerRemoveEvent.getStreamId().equals(streamId)) {

        if (addMemberView != null) {
          addMemberView.removeViewerEvent(viewerRemoveEvent.getViewerId());
        }
      }
    }

    @Override
    public void viewerTimedOut(@NotNull Isometrik isometrik,
        @NotNull ViewerTimeoutEvent viewerTimeoutEvent) {
      if (viewerTimeoutEvent.getStreamId().equals(streamId)) {
        if (addMemberView != null) {
          addMemberView.removeViewerEvent(viewerTimeoutEvent.getViewerId());
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

        if (addMemberView != null) {
          addMemberView.onProfileSwitched(copublishRequestSwitchProfileEvent.getUserId());
        }
      }
    }
  };

  /**
   * {@link AddMembersContract.Presenter#requestUsersData(int, boolean)}
   */
  @Override
  public void requestUsersData(int pageSize, boolean refreshRequest) {
    isLoading = true;

    if (refreshRequest) {
      pageToken = null;
      isLastPage = false;
    }

    isometrik.fetchUsers(
        new FetchUsersQuery.Builder().setCount(pageSize).setPageToken(pageToken).build(),
        (var1, var2) -> {

          if (var1 != null) {

            ArrayList<AddMembersModel> usersModels = new ArrayList<>();

            pageToken = var1.getPageToken();
            if (pageToken == null) {

              isLastPage = true;
            }

            String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

            ArrayList<FetchUsersResult.User> users = var1.getUsers();
            int size = users.size();

            for (int i = 0; i < size; i++) {

              String currentUserId = users.get(i).getUserId();

              if (currentUserId.equals(userId) || memberIds.contains(currentUserId)) {
                count++;
              } else {

                usersModels.add(new AddMembersModel(users.get(i)));
              }
            }
            if (addMemberView != null) {
              addMemberView.onUsersDataReceived(usersModels, refreshRequest);
            }
          } else {

            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                if (addMemberView != null) {
                  //No users found
                  addMemberView.onUsersDataReceived(new ArrayList<>(), true);
                }
              } else {
                isLastPage = true;
              }
            } else {
              if (addMemberView != null) {
                addMemberView.onError(var2.getErrorMessage());
              }
            }
          }
          isLoading = false;
        });
  }

  /**
   * {@link AddMembersContract.Presenter#requestUsersDataOnScroll(int, int, int)}
   */
  @Override
  public void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount + count >= Constants.USERS_PAGE_SIZE) {
        requestUsersData(Constants.USERS_PAGE_SIZE, false);
      }
    }
  }

  /**
   * {@link AddMembersContract.Presenter#addMember(String, String)}
   */
  @Override
  public void addMember(String memberId, String streamId) {

    isometrik.addMember(new AddMemberQuery.Builder().setMemberId(memberId)
        .setInitiatorId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .setStreamId(streamId)
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (addMemberView != null) {
          addMemberView.onMemberAdded(memberId);
        }
      } else {
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 1) {
          if (addMemberView != null) {
            //Stream not live anymore
            addMemberView.onStreamOffline(
                IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                StreamDialogEnum.StreamOffline.getValue());
          }
        } else {
          if (addMemberView != null) {
            addMemberView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ViewersContract.Presenter#requestStreamViewersData(String, ArrayList)}
   */
  @Override
  public void requestStreamViewersData(String streamId, ArrayList<String> memberIds) {

    isometrik.fetchViewers(new FetchViewersQuery.Builder().setStreamId(streamId).build(),
        (var1, var2) -> {

          isLoading = false;

          if (var1 != null) {

            ArrayList<AddMembersModel> viewersModels = new ArrayList<>();

            ArrayList<FetchViewersResult.StreamViewer> viewers = var1.getStreamViewers();
            int size = viewers.size();

            FetchViewersResult.StreamViewer viewer;

            for (int i = 0; i < size; i++) {

              viewer = viewers.get(i);

              boolean givenViewerIsMember = false;

              if (this.memberIds.contains(viewer.getViewerId())) {
                givenViewerIsMember = true;
              }

              viewersModels.add(new AddMembersModel(viewer, givenViewerIsMember));
            }
            if (addMemberView != null) {
              addMemberView.onStreamViewersDataReceived(viewersModels);
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {
              if (addMemberView != null) {   //No viewers found
                addMemberView.onStreamViewersDataReceived(new ArrayList<>());
              }
            } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
              if (addMemberView != null) {  //Stream not live anymore
                addMemberView.onStreamOffline(IsometrikUiSdk.getInstance()
                        .getContext()
                        .getString(R.string.ism_stream_offline),
                    StreamDialogEnum.StreamOffline.getValue());
              }
            } else {
              if (addMemberView != null) {
                addMemberView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  @Override
  public void attachView(AddMembersContract.View addMemberView) {
    this.addMemberView = addMemberView;
  }

  @Override
  public void detachView() {
    this.addMemberView = null;
  }

  /**
   * {@link AddMembersContract.Presenter#registerStreamViewersEventListener()}
   */
  @Override
  public void registerStreamViewersEventListener() {
    isometrik.addViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link AddMembersContract.Presenter#unregisterStreamViewersEventListener()}
   */
  @Override
  public void unregisterStreamViewersEventListener() {
    isometrik.removeViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link AddMembersContract.Presenter#registerCopublishRequestsEventListener()}
   */
  @Override
  public void registerCopublishRequestsEventListener() {
    isometrik.addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link AddMembersContract.Presenter#unregisterCopublishRequestsEventListener()}
   */
  @Override
  public void unregisterCopublishRequestsEventListener() {
    isometrik.removeCopublishEventListener(copublishEventCallback);
  }
}
