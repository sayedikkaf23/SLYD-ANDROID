package io.isometrik.groupstreaming.ui.members.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.shimmer.ShimmerFrameLayout;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to show list of members in a
 * broadcast along with their publishing status.Host can kick out a member.Implements interface
 * MembersContract view{@link MembersContract.View}
 *
 * @see MembersContract.View
 */

public class MembersFragment extends BottomSheetDialogFragment implements MembersContract.View {

  public static final String TAG = "MembersBottomSheetFragment";

  private MembersContract.Presenter membersPresenter;
  private View view;
  @BindView(R2.id.tvNoMember)
  TextView tvNoMember;
  @BindView(R2.id.rvMembers)
  RecyclerView rvMembers;
  @BindView(R2.id.tvMembersCount)
  TextView tvMembersCount;
  @BindView(R2.id.refresh)
  SwipeRefreshLayout refresh;
  @BindView(R2.id.shimmerFrameLayout)
  ShimmerFrameLayout shimmerFrameLayout;

  private AlertProgress alertProgress;

  private AlertDialog alertDialog;

  private ArrayList<MembersModel> members;
  private MembersAdapter membersAdapter;

  private String streamId, membersCount;
  private boolean isAdmin;

  private Activity activity;

  public MembersFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_members, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);
    tvNoMember.setVisibility(View.GONE);
    alertProgress = new AlertProgress();

    tvMembersCount.setText(membersCount);

    rvMembers.setLayoutManager(new LinearLayoutManager(activity));
    members = new ArrayList<>();
    membersAdapter = new MembersAdapter(activity, members, this);
    rvMembers.setAdapter(membersAdapter);

    membersPresenter.initialize(streamId, isAdmin);

    fetchStreamMembers();

    membersPresenter.registerStreamMembersEventListener();
    membersPresenter.registerCopublishRequestsEventListener();
    refresh.setOnRefreshListener(this::fetchStreamMembers);

    //To allow scroll on member's recyclerview
    rvMembers.setOnTouchListener((v, event) -> {
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
    membersPresenter = new MembersPresenter();
    membersPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    membersPresenter.detachView();
    activity = null;
  }

  /**
   * {@link MembersContract.View#onError(String)}
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
   * Request remove member.
   *
   * @param memberId the id of the member to be removed
   */
  public void requestRemoveMember(String memberId) {
    showProgressDialog(getString(R.string.ism_removing_member));
    membersPresenter.requestRemoveMember(memberId);
  }

  /**
   * {@link MembersContract.View#onStreamMembersDataReceived(ArrayList)}
   */
  @Override
  public void onStreamMembersDataReceived(ArrayList<MembersModel> members) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        this.members.clear();
        this.members.addAll(members);
        if (members.size() > 0) {
          tvNoMember.setVisibility(View.GONE);
          rvMembers.setVisibility(View.VISIBLE);
          membersAdapter.notifyDataSetChanged();
        } else {
          tvNoMember.setVisibility(View.VISIBLE);
          rvMembers.setVisibility(View.GONE);
        }
      });
    }
    updateShimmerVisibility(false);
    if (refresh.isRefreshing()) refresh.setRefreshing(false);
  }

  /**
   * {@link MembersContract.View#onMemberRemovedResult(String)}
   */
  @Override
  public void onMemberRemovedResult(String memberId) {

    hideProgressDialog();
    removeMemberEvent(memberId, -1);
  }

  /**
   * {@link MembersContract.View#addMemberEvent(MembersModel,
   * int)}
   */
  @Override
  public void addMemberEvent(MembersModel membersModel, int membersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        members.add(0, membersModel);
        tvMembersCount.setText(String.valueOf(membersCount));
        membersAdapter.notifyItemInserted(0);
        //membersAdapter.notifyDataSetChanged();

        if (tvNoMember.getVisibility() == View.VISIBLE) {

          tvNoMember.setVisibility(View.GONE);
          rvMembers.setVisibility(View.VISIBLE);
        }
      });
    }
  }

  /**
   * {@link MembersContract.View#removeMemberEvent(String,
   * int)}
   */
  @Override
  public void removeMemberEvent(String memberId, int membersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = members.size();
        for (int i = 0; i < size; i++) {

          if (members.get(i).getMemberId().equals(memberId)) {

            members.remove(i);
            membersAdapter.notifyItemRemoved(i);
            // membersAdapter.notifyDataSetChanged();}

            break;
          }
        }

        size = members.size();
        if (size == 0) {
          tvNoMember.setVisibility(View.VISIBLE);
          rvMembers.setVisibility(View.GONE);
        }
        if (membersCount == -1) {
          tvMembersCount.setText(String.valueOf(size));
        } else {
          tvMembersCount.setText(String.valueOf(membersCount));
        }
      });
    }
  }

  /**
   * {@link MembersContract.View#publishStatusChanged(String,
   * boolean, int, String)}
   */
  @Override
  public void publishStatusChanged(String memberId, boolean publishing, int membersCount,
      String joinTime) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = members.size();
        for (int i = 0; i < size; i++) {

          if (members.get(i).getMemberId().equals(memberId)) {

            MembersModel membersModel = members.get(i);
            membersModel.setPublishing(publishing);
            if (publishing) {

              membersModel.setJoinTime(joinTime);
            } else {

              membersModel.setJoinTime(getString(R.string.ism_not_publishing));
            }

            members.set(i, membersModel);
            membersAdapter.notifyItemChanged(i);
            // membersAdapter.notifyDataSetChanged();}

            break;
          }
        }

        tvMembersCount.setText(String.valueOf(membersCount));
      });
    }
  }

  /**
   * {@link MembersContract.View#onStreamOffline(String,
   * int)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType) {
    updateShimmerVisibility(false);
    hideProgressDialog();
  }

  /**
   * @param streamId id of the stream group
   * @param isAdmin whether given user is admin or not
   * @param membersCount number of members
   */
  public void updateParameters(String streamId, boolean isAdmin, String membersCount) {
    this.streamId = streamId;
    this.isAdmin = isAdmin;
    this.membersCount = membersCount;
  }

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    membersPresenter.unregisterStreamMembersEventListener();
    membersPresenter.unregisterCopublishRequestsEventListener();
  }

  /**
   * {@link MembersContract.View#onProfileSwitched(String, int, String)}
   */
  @Override
  public void onProfileSwitched(String memberId, int membersCount, String joinTime) {

    publishStatusChanged(memberId, true, membersCount, joinTime);
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

  private void fetchStreamMembers() {
    updateShimmerVisibility(true);
    try {
      membersPresenter.requestStreamMembersData(streamId, isAdmin);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}