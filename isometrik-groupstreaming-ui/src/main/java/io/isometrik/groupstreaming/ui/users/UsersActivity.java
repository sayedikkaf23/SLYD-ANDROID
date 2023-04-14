package io.isometrik.groupstreaming.ui.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.profile.CreateUserActivity;
import io.isometrik.groupstreaming.ui.streams.grid.StreamsActivity;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.Constants;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The type Users activity.
 * It implements UsersContract.View{@link io.isometrik.groupstreaming.ui.users.UsersContract.View}
 *
 * @see io.isometrik.groupstreaming.ui.users.UsersContract.View
 */
public class UsersActivity extends AppCompatActivity implements UsersContract.View {

  private UsersContract.Presenter usersPresenter;

  @BindView(R2.id.tvNoUsers)
  TextView tvNoUsers;
  @BindView(R2.id.rvUsers)
  RecyclerView rvUsers;
  @BindView(R2.id.refresh)
  SwipeRefreshLayout refresh;
  @BindView(R2.id.ibBack)
  AppCompatImageButton ibBack;

  private AlertProgress alertProgress;

  private AlertDialog alertDialog;

  private ArrayList<UsersModel> users = new ArrayList<>();
  private UsersAdapter usersAdapter;

  private LinearLayoutManager layoutManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ism_activity_users);
    ButterKnife.bind(this);

    usersPresenter = new UsersPresenter(this);
    alertProgress = new AlertProgress();

    layoutManager = new LinearLayoutManager(this);
    rvUsers.setLayoutManager(layoutManager);
    usersAdapter = new UsersAdapter(this, users);
    rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
    rvUsers.setAdapter(usersAdapter);
    //alertProgress=new AlertProgress();
    fetchLatestUsers();

    refresh.setOnRefreshListener(this::fetchLatestUsers);
    ibBack.setVisibility(View.GONE);
  }

  /**
   * Create user.
   */
  @OnClick(R2.id.btCreateUser)
  public void createUser() {
    startActivity(new Intent(this, CreateUserActivity.class));
    finish();
  }

  /**
   * Select user.
   *
   * @param user the user
   */
  public void selectUser(UsersModel user) {
    IsometrikUiSdk.getInstance()
        .getUserSession()
        .switchUser(user.getUserId(), user.getUserName(), user.getUserIdentifier(),
            user.getUserProfilePic(), true);
    startActivity(new Intent(this, StreamsActivity.class));
    finish();
  }

  private void fetchLatestUsers() {
    showProgressDialog(getString(R.string.ism_fetching_users));
    try {
      usersPresenter.requestUsersData(Constants.USERS_PAGE_SIZE, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
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

          usersPresenter.requestUsersDataOnScroll(layoutManager.findFirstVisibleItemPosition(),
              layoutManager.getChildCount(), layoutManager.getItemCount());
        }
      };

  /**
   * {@link UsersContract.View#onUsersDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onUsersDataReceived(ArrayList<UsersModel> users, boolean latestUsers) {

    runOnUiThread(() -> {

      if (latestUsers) {
        this.users.clear();
      }
      this.users.addAll(users);

      if (UsersActivity.this.users.size() > 0) {
        tvNoUsers.setVisibility(View.GONE);
        rvUsers.setVisibility(View.VISIBLE);
        usersAdapter.notifyDataSetChanged();
      } else {
        tvNoUsers.setVisibility(View.VISIBLE);
        rvUsers.setVisibility(View.GONE);
      }
    });
    hideProgressDialog();
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  /**
   * {@link UsersContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
    hideProgressDialog();
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(UsersActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(UsersActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }
}
