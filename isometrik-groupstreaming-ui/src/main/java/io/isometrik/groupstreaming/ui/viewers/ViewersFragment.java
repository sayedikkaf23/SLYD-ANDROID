package io.isometrik.groupstreaming.ui.viewers;

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

public class ViewersFragment extends BottomSheetDialogFragment implements ViewersContract.View {

  public static final String TAG = "ViewersBottomSheetFragment";

  private Activity activity;
  private ViewersContract.Presenter viewersPresenter;
  private View view;

  @BindView(R2.id.tvNoViewer)
  TextView tvNoViewer;
  @BindView(R2.id.rvViewers)
  RecyclerView rvViewers;
  @BindView(R2.id.tvViewersCount)
  TextView tvViewersCount;
  @BindView(R2.id.shimmerFrameLayout)
  ShimmerFrameLayout shimmerFrameLayout;
  @BindView(R2.id.refresh)
  SwipeRefreshLayout refresh;

  private AlertProgress alertProgress;

  private AlertDialog alertDialog;

  private ArrayList<ViewersModel> viewers;
  private ViewersAdapter viewersAdapter;

  private String streamId, viewersCount;
  private ArrayList<String> memberIds;

  public ViewersFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_viewers, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);
    tvNoViewer.setVisibility(View.GONE);
    alertProgress = new AlertProgress();

    tvViewersCount.setText(viewersCount);

    rvViewers.setLayoutManager(new LinearLayoutManager(activity));
    viewers = new ArrayList<>();
    viewersAdapter = new ViewersAdapter(activity, viewers, this);
    rvViewers.setAdapter(viewersAdapter);

    viewersPresenter.initialize(streamId, memberIds);
    fetchStreamViewers();

    viewersPresenter.registerStreamViewersEventListener();
    viewersPresenter.registerStreamMembersEventListener();
    viewersPresenter.registerCopublishRequestsEventListener();

    refresh.setOnRefreshListener(this::fetchStreamViewers);

    //To allow scroll on viewer's recyclerview
    rvViewers.setOnTouchListener((v, event) -> {
      v.getParent().requestDisallowInterceptTouchEvent(true);
      v.onTouchEvent(event);
      return true;
    });
    return view;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    viewersPresenter.detachView();
    activity = null;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
    viewersPresenter = new ViewersPresenter();
    viewersPresenter.attachView(this);
  }

  /**
   * {@link ViewersContract.View#onError(String)}
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
   * {@link ViewersContract.View#onStreamViewersDataReceived(ArrayList)}
   */
  @Override
  public void onStreamViewersDataReceived(ArrayList<ViewersModel> viewers) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        this.viewers.clear();
        this.viewers.addAll(viewers);

        if (viewers.size() > 0) {
          tvNoViewer.setVisibility(View.GONE);
          rvViewers.setVisibility(View.VISIBLE);
          viewersAdapter.notifyDataSetChanged();
        } else {
          tvNoViewer.setVisibility(View.VISIBLE);
          rvViewers.setVisibility(View.GONE);
        }
      });
    }

    updateShimmerVisibility(false);

    if (refresh.isRefreshing()) refresh.setRefreshing(false);
  }

  /**
   * Remove viewer.
   *
   * @param viewerId the viewer id
   */
  public void removeViewer(String viewerId) {
    showProgressDialog(getString(R.string.ism_removing_viewer));
    viewersPresenter.requestRemoveViewer(viewerId);
  }

  /**
   * {@link ViewersContract.View#onViewerRemovedResult(String)}
   */
  @Override
  public void onViewerRemovedResult(String viewerId) {
    hideProgressDialog();
    removeViewerEvent(viewerId, -1);
  }

  /**
   * {@link ViewersContract.View#removeViewerEvent(String,
   * int)}
   */
  @Override
  public void removeViewerEvent(String viewerId, int viewersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = viewers.size();

        for (int i = 0; i < size; i++) {

          if (viewers.get(i).getViewerId().equals(viewerId)) {

            viewers.remove(i);
            viewersAdapter.notifyItemRemoved(i);
            //viewersAdapter.notifyDataSetChanged();}

            break;
          }
        }

        size = viewers.size();
        if (size == 0) {
          tvNoViewer.setVisibility(View.VISIBLE);
          rvViewers.setVisibility(View.GONE);
        }
        if (viewersCount == -1) {
          tvViewersCount.setText(String.valueOf(size));
        } else {
          tvViewersCount.setText(String.valueOf(viewersCount));
        }
      });
    }
  }

  /**
   * {@link ViewersContract.View#addViewerEvent(ViewersModel,
   * int)}
   */
  @Override
  public void addViewerEvent(ViewersModel viewersModel, int viewersCount) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        tvViewersCount.setText(String.valueOf(viewersCount));

        viewers.add(0, viewersModel);

        viewersAdapter.notifyItemInserted(0);
        //viewersAdapter.notifyDataSetChanged();

        if (tvNoViewer.getVisibility() == View.VISIBLE) {

          tvNoViewer.setVisibility(View.GONE);
          rvViewers.setVisibility(View.VISIBLE);
        }
      });
    }
  }

  /**
   * {@link ViewersContract.View#onStreamOffline(String,
   * int)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType) {
    updateShimmerVisibility(false);
    hideProgressDialog();
  }

  /**
   * @param streamId id of the stream group
   * @param viewersCount number of viewers
   * @param memberIds list containing ids of the stream group members
   */
  public void updateParameters(String streamId, String viewersCount, ArrayList<String> memberIds) {
    this.streamId = streamId;
    this.viewersCount = viewersCount;
    this.memberIds = memberIds;
  }

  /**
   * {@link ViewersContract.View#onProfileSwitched(String)}
   */
  @Override
  public void onProfileSwitched(String viewerId) {
    removeViewerEvent(viewerId, -1);
  }

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    viewersPresenter.unregisterCopublishRequestsEventListener();
    viewersPresenter.unregisterStreamMembersEventListener();
    viewersPresenter.unregisterStreamViewersEventListener();
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

  private void fetchStreamViewers() {
    updateShimmerVisibility(true);
    try {
      viewersPresenter.requestStreamViewersData(streamId, memberIds);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
