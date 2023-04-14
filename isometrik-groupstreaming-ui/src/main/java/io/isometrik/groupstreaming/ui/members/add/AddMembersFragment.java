package io.isometrik.groupstreaming.ui.members.add;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.groupstreaming.ui.utils.shimmer.ShimmerFrameLayout;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show list of viewers and users which can be added to a
 * broadcast.Implements interface
 * MembersContract view{@link AddMembersContract.View}
 *
 * @see AddMembersContract.View
 */
public class AddMembersFragment extends BottomSheetDialogFragment
    implements AddMembersContract.View {

  public static final String TAG = "AddMembersBottomSheetFragment";

  private AddMembersContract.Presenter addMembersPresenter;
  private View view;
  @BindView(R2.id.tvNoUsers)
  TextView tvNoUsers;
  @BindView(R2.id.rvUsers)
  RecyclerView rvUsers;

  @BindView(R2.id.refresh)
  SwipeRefreshLayout refresh;
  @BindView(R2.id.shimmerFrameLayout)
  ShimmerFrameLayout shimmerFrameLayout;

  @BindView(R2.id.tabLayoutUsers)
  TabLayout tabLayoutUsers;

  private AlertProgress alertProgress;

  private AlertDialog alertDialog;

  private ArrayList<AddMembersModel> users;
  private ArrayList<AddMembersModel> viewers;
  private AddMembersAdapter addMembersAdapter;
  private ArrayList<String> memberIds;

  private String streamId;
  private Activity activity;

  private int selectedTabPosition;
  private LinearLayoutManager layoutManager;
  private boolean alreadyAddedTabs;

  private Drawable noViewers;
  private Drawable noUsers;

  public AddMembersFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_add_members, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);
    noViewers = ContextCompat.getDrawable(activity, R.drawable.ism_ic_no_viewers);
    noUsers = ContextCompat.getDrawable(activity, R.drawable.ism_ic_no_users);
    if (!alreadyAddedTabs) {
      alreadyAddedTabs = true;
      tabLayoutUsers.addTab(tabLayoutUsers.newTab()
          .setText(getString(R.string.ism_users))
          .setIcon(R.drawable.ism_ic_members));
      tabLayoutUsers.addTab(tabLayoutUsers.newTab()
          .setText(getString(R.string.ism_stream_viewers))
          .setIcon(R.drawable.ism_ic_viewers));
    }

    tabLayoutUsers.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        selectedTabPosition = tabLayoutUsers.getSelectedTabPosition();
        if (tab.getIcon() != null) {
          tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
        switch (selectedTabPosition) {

          case 0: {
            //Users selected
            addMembersAdapter = new AddMembersAdapter(activity, users, AddMembersFragment.this);
            rvUsers.setAdapter(addMembersAdapter);
            addMembersAdapter.notifyDataSetChanged();
            if (users.size() > 0) {
              tvNoUsers.setVisibility(View.GONE);
              rvUsers.setVisibility(View.VISIBLE);
            } else {
              tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null, noUsers, null, null);
              tvNoUsers.setText(getString(R.string.ism_no_users_add_member));
              tvNoUsers.setVisibility(View.VISIBLE);
              rvUsers.setVisibility(View.GONE);
            }
            break;
          }
          case 1: {
            //Viewers selected
            addMembersAdapter = new AddMembersAdapter(activity, viewers, AddMembersFragment.this);
            rvUsers.setAdapter(addMembersAdapter);
            addMembersAdapter.notifyDataSetChanged();
            if (viewers.size() > 0) {
              tvNoUsers.setVisibility(View.GONE);
              rvUsers.setVisibility(View.VISIBLE);
            } else {
              tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null, noViewers, null, null);
              tvNoUsers.setText(getString(R.string.ism_no_viewers_add_member));
              tvNoUsers.setVisibility(View.VISIBLE);
              rvUsers.setVisibility(View.GONE);
            }
            break;
          }
        }
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
        if (tab.getIcon() != null) {
          tab.getIcon()
              .setColorFilter(ContextCompat.getColor(activity, R.color.ism_grey),
                  PorterDuff.Mode.SRC_IN);
        }
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });

    try {
      tabLayoutUsers.getTabAt(0)
          .getIcon()
          .setColorFilter(ContextCompat.getColor(activity, R.color.ism_white),
              PorterDuff.Mode.SRC_IN);
    } catch (NullPointerException ignore) {
    }

    try {
      tabLayoutUsers.getTabAt(0).select();
    } catch (NullPointerException ignore) {
    }
    tvNoUsers.setVisibility(View.GONE);
    alertProgress = new AlertProgress();

    layoutManager = new LinearLayoutManager(activity);
    rvUsers.setLayoutManager(layoutManager);

    users = new ArrayList<>();
    viewers = new ArrayList<>();

    addMembersAdapter = new AddMembersAdapter(activity, users, this);
    rvUsers.setAdapter(addMembersAdapter);
    rvUsers.addOnScrollListener(recyclerViewOnScrollListener);
    addMembersPresenter.initialize(streamId, memberIds);

    fetchLatestUsers();
    fetchLatestViewers();
    addMembersPresenter.registerStreamViewersEventListener();
    addMembersPresenter.registerCopublishRequestsEventListener();
    refresh.setOnRefreshListener(() -> {
      if (selectedTabPosition == 0) {
        fetchLatestUsers();
      } else {
        fetchLatestViewers();
      }
    });
    //To allow scroll on user's recyclerview
    rvUsers.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });
    return view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    addMembersPresenter = new AddMembersPresenter();
    addMembersPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    addMembersPresenter.detachView();
    activity = null;
  }

  /**
   * {@link AddMembersContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
    updateShimmerVisibility(false);
    hideProgressDialog();
    if (activity != null) {
      activity.runOnUiThread(() -> {
        if (errorMessage != null) {
          Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(activity, getString(R.string.ism_error), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  /**
   * {@link AddMembersContract.View#onStreamOffline(String,
   * int)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType) {
    updateShimmerVisibility(false);
    hideProgressDialog();
  }

  /**
   * @param streamId id of the stream group
   * @param memberIds list containing ids of the stream group members
   */
  public void updateParameters(String streamId, ArrayList<String> memberIds) {
    this.streamId = streamId;
    this.memberIds = memberIds;
  }

  /**
   * Add member.
   *
   * @param memberId the member id
   */
  public void addMember(String memberId) {
    showProgressDialog(getString(R.string.ism_adding_member));
    addMembersPresenter.addMember(memberId, streamId);
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

          if (selectedTabPosition == 0) {

            addMembersPresenter.requestUsersDataOnScroll(
                layoutManager.findFirstVisibleItemPosition(), layoutManager.getChildCount(),
                layoutManager.getItemCount());
          }
        }
      };

  /**
   * {@link AddMembersContract.View#onUsersDataReceived(ArrayList, boolean)}
   */
  @Override
  public void onUsersDataReceived(ArrayList<AddMembersModel> users, boolean latestUsers) {

    activity.runOnUiThread(() -> {
      if (latestUsers) {
        this.users.clear();
      }
      this.users.addAll(users);
      if (selectedTabPosition == 0) {
        if (users.size() > 0) {
          tvNoUsers.setVisibility(View.GONE);
          rvUsers.setVisibility(View.VISIBLE);
          addMembersAdapter.notifyDataSetChanged();
        } else {
          tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null, noUsers, null, null);
          tvNoUsers.setText(getString(R.string.ism_no_users_add_member));
          tvNoUsers.setVisibility(View.VISIBLE);
          rvUsers.setVisibility(View.GONE);
        }
      }
    });

    updateShimmerVisibility(false);

    if (refresh.isRefreshing()) refresh.setRefreshing(false);
  }

  /**
   * {@link AddMembersContract.View#onMemberAdded(String)}
   */
  @Override
  public void onMemberAdded(String memberId) {

    activity.runOnUiThread(() -> {

      if (selectedTabPosition == 0) {
        int size = users.size();
        for (int i = 0; i < size; i++) {

          if (users.get(i).getUserId().equals(memberId)) {

            users.remove(i);
            addMembersAdapter.notifyItemRemoved(i);
            break;
          }
        }
      } else {
        int size = viewers.size();
        for (int i = 0; i < size; i++) {

          if (viewers.get(i).getUserId().equals(memberId)) {
            AddMembersModel addMembersModel = viewers.get(i);
            addMembersModel.setMember(true);
            viewers.set(i, addMembersModel);
            addMembersAdapter.notifyItemChanged(i);
            break;
          }
        }
      }
    });

    hideProgressDialog();
  }

  /**
   * {@link AddMembersContract.View#onStreamViewersDataReceived(ArrayList<AddMembersModel>)}
   */
  @Override
  public void onStreamViewersDataReceived(ArrayList<AddMembersModel> viewers) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        this.viewers.clear();
        this.viewers.addAll(viewers);
        if (selectedTabPosition == 1) {
          if (viewers.size() > 0) {
            tvNoUsers.setVisibility(View.GONE);
            rvUsers.setVisibility(View.VISIBLE);
            addMembersAdapter.notifyDataSetChanged();
          } else {
            tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null, noViewers, null, null);
            tvNoUsers.setText(getString(R.string.ism_no_viewers_add_member));
            tvNoUsers.setVisibility(View.VISIBLE);
            rvUsers.setVisibility(View.GONE);
          }
        }
      });
    }

    updateShimmerVisibility(false);

    if (refresh.isRefreshing()) refresh.setRefreshing(false);
  }

  /**
   * {@link AddMembersContract.View#removeViewerEvent(String)}
   */
  @Override
  public void removeViewerEvent(String viewerId) {

    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = viewers.size();

        for (int i = 0; i < size; i++) {

          if (viewers.get(i).getUserId().equals(viewerId)) {

            viewers.remove(i);
            if (selectedTabPosition == 1) {
              addMembersAdapter.notifyItemRemoved(i);
              //viewersAdapter.notifyDataSetChanged();}
            }
            break;
          }
        }
        if (selectedTabPosition == 1) {
          size = viewers.size();
          if (size == 0) {
            tvNoUsers.setCompoundDrawablesWithIntrinsicBounds(null, noViewers, null, null);
            tvNoUsers.setText(getString(R.string.ism_no_viewers_add_member));
            tvNoUsers.setVisibility(View.VISIBLE);
            rvUsers.setVisibility(View.GONE);
          }
        }
      });
    }
  }

  /**
   * {@link AddMembersContract.View#addViewerEvent(AddMembersModel)}
   */
  @Override
  public void addViewerEvent(AddMembersModel addMembersModel) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        viewers.add(0, addMembersModel);

        if (selectedTabPosition == 1) {
          addMembersAdapter.notifyItemInserted(0);
          //viewersAdapter.notifyDataSetChanged();

          if (tvNoUsers.getVisibility() == View.VISIBLE) {

            tvNoUsers.setVisibility(View.GONE);
            rvUsers.setVisibility(View.VISIBLE);
          }
        }
      });
    }
  }

  /**
   * {@link AddMembersContract.View#onProfileSwitched(String)}
   */
  @Override
  public void onProfileSwitched(String viewerId) {
    removeViewerEvent(viewerId);
  }

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    addMembersPresenter.unregisterStreamViewersEventListener();
    addMembersPresenter.unregisterCopublishRequestsEventListener();
  }

  private void showProgressDialog(String message) {

    if (activity != null) {
      alertDialog = alertProgress.getProgressDialog(activity, message);
      if (!activity.isFinishing()) alertDialog.show();
    }
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void fetchLatestUsers() {
    updateShimmerVisibility(true);
    try {
      addMembersPresenter.requestUsersData(Constants.USERS_PAGE_SIZE, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void fetchLatestViewers() {
    updateShimmerVisibility(true);
    try {
      addMembersPresenter.requestStreamViewersData(streamId, memberIds);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateShimmerVisibility(boolean visible) {
    if (visible) {
      shimmerFrameLayout.startShimmer();
      shimmerFrameLayout.setVisibility(View.VISIBLE);
    } else {
      if (shimmerFrameLayout.getVisibility() == View.VISIBLE) {
        shimmerFrameLayout.setVisibility(View.GONE);
        shimmerFrameLayout.stopShimmer();
      }
    }
  }
}