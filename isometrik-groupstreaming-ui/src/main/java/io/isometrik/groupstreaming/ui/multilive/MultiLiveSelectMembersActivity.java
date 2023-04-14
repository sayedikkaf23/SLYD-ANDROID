package io.isometrik.groupstreaming.ui.multilive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.copublish.CopublishActivity;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.groupstreaming.ui.utils.RecyclerItemClickListener;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The type Select members activity for the multi live.
 * It implements MultiLiveSelectMembersContract.View{@link MultiLiveSelectMembersContract.View}
 *
 * @see MultiLiveSelectMembersContract.View
 */
public class MultiLiveSelectMembersActivity extends AppCompatActivity
    implements MultiLiveSelectMembersContract.View {

  private MultiLiveSelectMembersContract.Presenter multiLiveSelectMembersPresenter;

  @BindView(R2.id.tvMembersCount)
  TextView tvMembersCount;
  @BindView(R2.id.tvNoUsers)
  TextView tvNoUsers;

  @BindView(R2.id.refresh)
  SwipeRefreshLayout refresh;

  @BindView(R2.id.rvUsersSelected)
  RecyclerView rvUsersSelected;
  @BindView(R2.id.rvUsers)
  RecyclerView rvUsers;

  private AlertProgress alertProgress;

  private AlertDialog alertDialog;

  private ArrayList<MultiLiveSelectMembersModel> users = new ArrayList<>();
  private ArrayList<MultiLiveSelectMembersModel> multiLiveSelectMembersModels = new ArrayList<>();

  private MultiLiveUsersAdapter usersAdapter;
  private MultiLiveSelectedUsersAdapter multiLiveSelectedUsersAdapter;

  private LinearLayoutManager layoutManager;
  private LinearLayoutManager selectedUsersLayoutManager;
  private boolean cleanUpRequested = false;
  private int count;
  private String streamImage, streamDescription;
  private boolean isPublic;

  private static final int MAXIMUM_MEMBERS = 5;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ism_activity_multilive_select_member);
    ButterKnife.bind(this);

    multiLiveSelectMembersPresenter = new MultiLiveSelectMembersPresenter(this);
    alertProgress = new AlertProgress();

    streamImage = getIntent().getStringExtra("streamImage");
    streamDescription = getIntent().getStringExtra("streamDescription");
    isPublic = getIntent().getBooleanExtra("isPublic", true);

    layoutManager = new LinearLayoutManager(this);
    rvUsers.setLayoutManager(layoutManager);
    usersAdapter = new MultiLiveUsersAdapter(this, users);
    rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
    rvUsers.setAdapter(usersAdapter);

    selectedUsersLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    rvUsersSelected.setLayoutManager(selectedUsersLayoutManager);
    multiLiveSelectedUsersAdapter =
        new MultiLiveSelectedUsersAdapter(this, multiLiveSelectMembersModels);
    rvUsersSelected.setAdapter(multiLiveSelectedUsersAdapter);

    fetchLatestUsers();

    rvUsers.addOnItemTouchListener(new RecyclerItemClickListener(this, rvUsers,
        new RecyclerItemClickListener.OnItemClickListener() {
          @Override
          public void onItemClick(View view, int position) {
            if (position >= 0) {

              MultiLiveSelectMembersModel user = users.get(position);

              if (user.isSelected()) {
                user.setSelected(false);
                count--;
                removeSelectedUser(user.getUserId());
              } else {

                if (count < MAXIMUM_MEMBERS) {
                  //Maximum 5 members can be added
                  user.setSelected(true);
                  count++;
                  addSelectedUser(user);
                } else {
                  Toast.makeText(MultiLiveSelectMembersActivity.this,
                      getString(R.string.ism_max_members_selected), Toast.LENGTH_SHORT).show();
                }
              }
              updateSelectedMembersText();
              users.set(position, user);
              usersAdapter.notifyItemChanged(position);
            }
          }

          @Override
          public void onItemLongClick(View view, int position) {
          }
        }));

    refresh.setOnRefreshListener(this::fetchLatestUsers);
  }

  /**
   * Back.
   */
  @OnClick({ R2.id.ibBack })
  public void back() {
    onBackPressed();
  }

  /**
   * Move to next screen.
   */
  @OnClick({ R2.id.ivNext })
  public void moveToNextScreen() {
    showProgressDialog(getString(R.string.ism_starting_broadcast));

    multiLiveSelectMembersPresenter.startBroadcast(streamDescription, streamImage,
        multiLiveSelectMembersModels, isPublic);
  }

  /**
   * The Recycler view on scroll listener.
   */
  public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
          super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
          super.onScrolled(recyclerView, dx, dy);

          multiLiveSelectMembersPresenter.requestUsersDataOnScroll(
              layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
              layoutManager.getItemCount());
        }
      };

  /**
   * {@link MultiLiveSelectMembersContract.View#onUsersDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onUsersDataReceived(ArrayList<MultiLiveSelectMembersModel> users,
      boolean latestUsers) {

    if (latestUsers) {
      this.users.clear();
      this.multiLiveSelectMembersModels.clear();
      count = 0;
      runOnUiThread(this::updateSelectedMembersText);
    }
    this.users.addAll(users);
    runOnUiThread(() -> {
      if (MultiLiveSelectMembersActivity.this.users.size() > 0) {
        tvNoUsers.setVisibility(View.GONE);
        rvUsers.setVisibility(View.VISIBLE);
        usersAdapter.notifyDataSetChanged();
      } else {
        tvNoUsers.setVisibility(View.VISIBLE);
        rvUsers.setVisibility(View.GONE);
      }
      multiLiveSelectedUsersAdapter.notifyDataSetChanged();
    });
    hideProgressDialog();
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
  }

  /**
   * {@link MultiLiveSelectMembersContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (refresh.isRefreshing()) refresh.setRefreshing(false);

    hideProgressDialog();

    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(MultiLiveSelectMembersActivity.this, errorMessage, Toast.LENGTH_SHORT)
            .show();
      } else {
        Toast.makeText(MultiLiveSelectMembersActivity.this, getString(R.string.ism_error),
            Toast.LENGTH_SHORT).show();
      }
    });
  }

  /**
   * Remove user.
   *
   * @param userId the user id
   */
  public void removeUser(String userId) {
    int size = users.size();
    for (int i = 0; i < size; i++) {

      if (users.get(i).getUserId().equals(userId)) {

        MultiLiveSelectMembersModel selectMembersModel = users.get(i);
        selectMembersModel.setSelected(false);
        users.set(i, selectMembersModel);
        if (i == 0) {
          usersAdapter.notifyDataSetChanged();
        } else {
          usersAdapter.notifyItemChanged(i);
        }
        count--;
        updateSelectedMembersText();
        break;
      }
    }

    for (int i = 0; i < multiLiveSelectMembersModels.size(); i++) {

      if (multiLiveSelectMembersModels.get(i).getUserId().equals(userId)) {
        multiLiveSelectMembersModels.remove(i);
        if (i == 0) {
          multiLiveSelectedUsersAdapter.notifyDataSetChanged();
        } else {
          multiLiveSelectedUsersAdapter.notifyItemRemoved(i);
        }

        break;
      }
    }
  }

  @Override
  public void onBackPressed() {
    cleanupOnActivityDestroy();
    try {
      super.onBackPressed();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onDestroy() {
    cleanupOnActivityDestroy();
    super.onDestroy();
  }

  /**
   * {@link MultiLiveSelectMembersContract.View#onBroadcastStarted(String, String, String,
   * ArrayList, long,
   * String)}
   */
  @Override
  public void onBroadcastStarted(String streamId, String streamDescription, String streamImageUrl,
      ArrayList<String> memberIds, long startTime, String userId) {
    hideProgressDialog();
    memberIds.add(userId);
    Intent intent = new Intent(MultiLiveSelectMembersActivity.this, CopublishActivity.class);
    intent.putExtra("streamId", streamId);
    intent.putExtra("streamDescription", streamDescription);
    intent.putExtra("streamImage", streamImageUrl);
    intent.putExtra("startTime", startTime);
    intent.putExtra("membersCount", memberIds.size());
    intent.putExtra("viewersCount", 0);
    intent.putExtra("publishersCount", 1);
    intent.putExtra("joinRequest", true);
    intent.putExtra("isAdmin", true);
    intent.putStringArrayListExtra("memberIds", memberIds);
    intent.putExtra("initiatorName", IsometrikUiSdk.getInstance().getUserSession().getUserName());
    intent.putExtra("publishRequired", false);
    intent.putExtra("initiatorId", IsometrikUiSdk.getInstance().getUserSession().getUserId());

    intent.putExtra("initiatorIdentifier",
        IsometrikUiSdk.getInstance().getUserSession().getUserIdentifier());
    intent.putExtra("initiatorImage",
        IsometrikUiSdk.getInstance().getUserSession().getUserProfilePic());
    intent.putExtra("isPublic", isPublic);
    intent.putExtra("isBroadcaster", true);
    startActivity(intent);
    finish();
  }

  private void fetchLatestUsers() {

    showProgressDialog(getString(R.string.ism_fetching_users));
    try {
      multiLiveSelectMembersPresenter.requestUsersData(Constants.USERS_PAGE_SIZE, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void removeSelectedUser(String userId) {

    for (int i = 0; i < multiLiveSelectMembersModels.size(); i++) {
      if (multiLiveSelectMembersModels.get(i).getUserId().equals(userId)) {
        multiLiveSelectMembersModels.remove(i);
        if (i == 0) {
          multiLiveSelectedUsersAdapter.notifyDataSetChanged();
        } else {
          multiLiveSelectedUsersAdapter.notifyItemRemoved(i);
        }
        break;
      }
    }
  }

  private void addSelectedUser(MultiLiveSelectMembersModel selectMembersModel) {
    multiLiveSelectMembersModels.add(selectMembersModel);
    try {
      multiLiveSelectedUsersAdapter.notifyDataSetChanged();
      selectedUsersLayoutManager.scrollToPosition(multiLiveSelectMembersModels.size() - 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateSelectedMembersText() {

    if (count > 0) {
      tvMembersCount.setText(
          getString(R.string.ism_multilive_members_selected, String.valueOf(count),
              String.valueOf(MAXIMUM_MEMBERS)));
    } else {
      tvMembersCount.setText(getString(R.string.ism_add_members));
    }
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void cleanupOnActivityDestroy() {
    if (!cleanUpRequested) {
      cleanUpRequested = true;
      hideProgressDialog();
    }
  }
}
