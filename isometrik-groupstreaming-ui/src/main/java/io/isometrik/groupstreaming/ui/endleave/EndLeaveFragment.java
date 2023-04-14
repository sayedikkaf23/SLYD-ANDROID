package io.isometrik.groupstreaming.ui.endleave;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.settings.callbacks.ActionCallback;
import org.jetbrains.annotations.NotNull;

/**
 * Bottomsheet dialog fragment to allow publisher to either stop publishing or leave a
 * broadcast.Host can end a live video as well.Action callbacks are triggered using interface
 * ActionCallback{@link ActionCallback}
 *
 * @see ActionCallback
 */
public class EndLeaveFragment extends BottomSheetDialogFragment {

  public static final String TAG = "EndLeaveFragment";

  private View view;

  @BindView(R2.id.tvAction)
  TextView tvAction;
  @BindView(R2.id.tvWarning)
  TextView tvWarning;
  @BindView(R2.id.tvCancel)
  TextView tvCancel;

  private ActionCallback actionCallback;
  private boolean isAdmin;
  private Activity activity;

  public EndLeaveFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_end_leave, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);

    if (isAdmin) {
      tvWarning.setText(getString(R.string.ism_warning_end));
      tvAction.setText(getString(R.string.ism_end_video));
      tvCancel.setText(getString(R.string.ism_cancel));
    } else {
      tvWarning.setText(getString(R.string.ism_warning_leave));
      tvAction.setText(getString(R.string.ism_leave_video));
      tvCancel.setText(getString(R.string.ism_stop_publishing));
    }
    return view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activity = getActivity();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    activity = null;
  }

  @Override
  public void onAttach(@NotNull Context context) {
    super.onAttach(context);

    if (context instanceof ActionCallback) {
      actionCallback = (ActionCallback) context;
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    actionCallback = null;
  }

  @OnClick(R2.id.tvCancel)
  public void dismissDialog() {
    if (isAdmin) {
      if (activity != null && !activity.isFinishing()) dismiss();
    } else {
      actionCallback.stopPublishingRequested();
    }
  }

  @OnClick(R2.id.tvAction)
  public void endLeaveBroadcast() {
    if (actionCallback != null) {
      if (isAdmin) {
        actionCallback.endBroadcastRequested();
      } else {
        actionCallback.leaveBroadcastRequested();
      }
    }
  }

  public void updateParameters(boolean isAdmin) {

    this.isAdmin = isAdmin;
  }
}
