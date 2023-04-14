package io.isometrik.groupstreaming.ui.multilive;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.groupstreaming.ui.utils.UserSession;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.stream.StartStreamQuery;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.response.user.FetchUsersResult;
import java.util.ArrayList;

/**
 * The select members presenter for the multi live to fetch list of users with paging and to start
 * a
 * broadcast.
 *
 * It implements MultiLiveSelectMembersContract.Presenter{@link MultiLiveSelectMembersContract.Presenter}
 *
 * @see MultiLiveSelectMembersContract.Presenter
 */
public class MultiLiveSelectMembersPresenter implements MultiLiveSelectMembersContract.Presenter {

  /**
   * Instantiates a new Select members presenter.
   */
  MultiLiveSelectMembersPresenter(MultiLiveSelectMembersContract.View selectMembersView) {
    this.selectMembersView = selectMembersView;
  }

  private MultiLiveSelectMembersContract.View selectMembersView;
  private String pageToken;
  private boolean isLastPage;
  private boolean isLoading;
  private int count;
  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  /**
   * {@link MultiLiveSelectMembersContract.Presenter#requestUsersData(int, boolean)}
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

            ArrayList<MultiLiveSelectMembersModel> usersModels = new ArrayList<>();

            pageToken = var1.getPageToken();
            if (pageToken == null) {

              isLastPage = true;
            }

            ArrayList<FetchUsersResult.User> users = var1.getUsers();
            int size = users.size();

            String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

            for (int i = 0; i < size; i++) {
              if (users.get(i).getUserId().equals(userId)) {

                count = 1;
              } else {

                usersModels.add(new MultiLiveSelectMembersModel(users.get(i)));
              }
            }

            selectMembersView.onUsersDataReceived(usersModels, refreshRequest);
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

              if (refreshRequest) {
                //No users found
                selectMembersView.onUsersDataReceived(new ArrayList<>(), true);
              } else {
                isLastPage = true;
              }
            } else {
              selectMembersView.onError(var2.getErrorMessage());
            }
          }
          isLoading = false;
        });
  }

  /**
   * {@link MultiLiveSelectMembersContract.Presenter#requestUsersDataOnScroll(int, int, int)}
   */
  @Override
  public void requestUsersDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {

    if (!isLoading && !isLastPage) {

      if ((visibleItemCount + firstVisibleItemPosition) >= (totalItemCount)
          && firstVisibleItemPosition >= 0
          && (totalItemCount + count) >= Constants.USERS_PAGE_SIZE) {

        requestUsersData(Constants.USERS_PAGE_SIZE, false);
      }
    }
  }

  /**
   * {@link MultiLiveSelectMembersContract.Presenter#startBroadcast(String, String, ArrayList,
   * boolean)}
   */
  @Override
  public void startBroadcast(String streamDescription, String streamImageUrl,
      ArrayList<MultiLiveSelectMembersModel> members, boolean isPublic) {

    UserSession userSession = IsometrikUiSdk.getInstance().getUserSession();

    ArrayList<String> memberIds = new ArrayList<>();
    int size = members.size();
    for (int i = 0; i < size; i++) {

      memberIds.add(members.get(i).getUserId());
    }

    isometrik.startStream(new StartStreamQuery.Builder().setStreamDescription(streamDescription)
        .setCreatedBy(userSession.getUserId())
        .setStreamImage(streamImageUrl)
        .setMembers(memberIds)
        .setPublic(isPublic)
        .build(), (var1, var2) -> {

      if (var1 != null) {

        selectMembersView.onBroadcastStarted(var1.getStreamId(), streamDescription, streamImageUrl,
            memberIds, var1.getStartTime(), userSession.getUserId());
      } else {

        selectMembersView.onError(var2.getErrorMessage());
      }
    });
  }
}
