package io.isometrik.groupstreaming.ui.requests;

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
 * Bottomsheet dialog fragment to show list of copublish requests in a
 * broadcast.Host can accept/decline a copublish request.Implements interface
 * RequestsContract view{@link RequestsContract.View}
 *
 * @see RequestsContract.View
 */
public class RequestsFragment extends BottomSheetDialogFragment implements RequestsContract.View {

  public static final String TAG = "RequestsBottomSheetFragment";

  private Activity activity;
  private RequestsContract.Presenter requestsPresenter;
  private View view;
  private RequestListActionCallback requestListActionCallback;
  @BindView(R2.id.tvNoRequest)
  TextView tvNoRequest;
  @BindView(R2.id.rvRequests)
  RecyclerView rvRequests;
  @BindView(R2.id.tvRequestsCount)
  TextView tvRequestsCount;
  @BindView(R2.id.refresh)
  SwipeRefreshLayout refresh;
  @BindView(R2.id.shimmerFrameLayout)
  ShimmerFrameLayout shimmerFrameLayout;

  private AlertProgress alertProgress;

  private AlertDialog alertDialog;

  private ArrayList<RequestsModel> requests;
  private RequestsAdapter requestsAdapter;

  private String streamId;
  private boolean isInitiator;

  public RequestsFragment() {
    // Required empty public constructor
  }

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_requests, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);
    tvNoRequest.setVisibility(View.GONE);
    alertProgress = new AlertProgress();

    rvRequests.setLayoutManager(new LinearLayoutManager(activity));
    requests = new ArrayList<>();
    requestsAdapter = new RequestsAdapter(activity, requests, this);
    rvRequests.setAdapter(requestsAdapter);

    requestsPresenter.initialize(streamId, isInitiator);
    fetchCopublishRequests();

    requestsPresenter.registerStreamRequestsEventListener();
    refresh.setOnRefreshListener(this::fetchCopublishRequests);

    //To allow scroll on request's recyclerview
    rvRequests.setOnTouchListener((v, event) -> {
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
    requestsPresenter = new RequestsPresenter();
    requestsPresenter.attachView(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    requestsPresenter.detachView();
    activity = null;
  }

  private void fetchCopublishRequests() {
    updateShimmerVisibility(true);
    try {
      requestsPresenter.requestCopublishRequestsData(streamId);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * {@link RequestsContract.View#onError(String)}
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
   * {@link RequestsContract.View#onCopublishRequestsDataReceived(ArrayList)}
   */
  @Override
  public void onCopublishRequestsDataReceived(ArrayList<RequestsModel> requests) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        this.requests.clear();
        this.requests.addAll(requests);
        int size = requests.size();
        if (size > 0) {
          tvRequestsCount.setText(String.valueOf(size));
          tvNoRequest.setVisibility(View.GONE);
          rvRequests.setVisibility(View.VISIBLE);
          requestsAdapter.notifyDataSetChanged();
        } else {
          tvNoRequest.setVisibility(View.VISIBLE);
          rvRequests.setVisibility(View.GONE);
        }
      });
    }

    updateShimmerVisibility(false);

    if (refresh.isRefreshing()) refresh.setRefreshing(false);
  }

  /**
   * Accept request.
   *
   * @param userId the user id
   */
  public void acceptCopublishRequest(String userId) {
    showProgressDialog(getString(R.string.ism_accepting_request));
    requestsPresenter.acceptCopublishRequest(userId);
  }

  /**
   * Decline request.
   *
   * @param userId the user id
   */
  public void declineCopublishRequest(String userId) {
    showProgressDialog(getString(R.string.ism_declining_request));
    requestsPresenter.declineCopublishRequest(userId);
  }

  /**
   * {@link RequestsContract.View#removeRequestEvent(String)}
   */
  @Override
  public void removeRequestEvent(String userId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {
        int size = requests.size();

        for (int i = 0; i < size; i++) {

          if (requests.get(i).getUserId().equals(userId)) {

            requests.remove(i);
            requestsAdapter.notifyItemRemoved(i);
            //requestsAdapter.notifyDataSetChanged();}

            break;
          }
        }

        size = requests.size();
        if (size == 0) {
          tvNoRequest.setVisibility(View.VISIBLE);
          rvRequests.setVisibility(View.GONE);
        }

        tvRequestsCount.setText(String.valueOf(size));
      });
    }
  }

  /**
   * {@link RequestsContract.View#addRequestEvent(RequestsModel)}
   */
  @Override
  public void addRequestEvent(RequestsModel requestsModel) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        requests.add(0, requestsModel);

        requestsAdapter.notifyItemInserted(0);
        //requestsAdapter.notifyDataSetChanged();

        tvRequestsCount.setText(String.valueOf(requests.size()));

        if (tvNoRequest.getVisibility() == View.VISIBLE) {

          tvNoRequest.setVisibility(View.GONE);
          rvRequests.setVisibility(View.VISIBLE);
        }
      });
    }
  }

  /**
   * {@link RequestsContract.View#onStreamOffline(String,
   * int)}
   */
  @Override
  public void onStreamOffline(String message, int dialogType) {
    updateShimmerVisibility(false);
    hideProgressDialog();
  }

  /**
   * {@link RequestsContract.View#onCopublishRequestAccepted(String)}
   */
  @Override
  public void onCopublishRequestAccepted(String userId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        for (int i = 0; i < requests.size(); i++) {

          if (requests.get(i).getUserId().equals(userId)) {

            RequestsModel requestsModel = requests.get(i);
            requestsModel.setAccepted(true);
            requestsModel.setPending(false);
            requests.set(i, requestsModel);

            requestsAdapter.notifyItemChanged(i);
            //requestsAdapter.notifyDataSetChanged();
            break;
          }
        }
      });
    }
    requestListActionCallback.copublishRequestAction(true, userId);
    hideProgressDialog();
  }

  /**
   * {@link RequestsContract.View#onCopublishRequestDeclined(String)}
   */
  @Override
  public void onCopublishRequestDeclined(String userId) {
    if (activity != null) {
      activity.runOnUiThread(() -> {

        for (int i = 0; i < requests.size(); i++) {

          if (requests.get(i).getUserId().equals(userId)) {

            RequestsModel requestsModel = requests.get(i);
            requestsModel.setAccepted(false);
            requestsModel.setPending(false);
            requests.set(i, requestsModel);

            requestsAdapter.notifyItemChanged(i);
            //requestsAdapter.notifyDataSetChanged();
            break;
          }
        }
      });
    }
    requestListActionCallback.copublishRequestAction(false, userId);
    hideProgressDialog();
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

  public void updateParameters(String streamId, boolean isInitiator,
      RequestListActionCallback requestListActionCallback) {
    this.streamId = streamId;
    this.isInitiator = isInitiator;
    this.requestListActionCallback = requestListActionCallback;
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

  @Override
  public void onCancel(@NotNull DialogInterface dialog) {
    super.onCancel(dialog);
    requestsPresenter.unregisterStreamRequestsEventListener();
  }
}
