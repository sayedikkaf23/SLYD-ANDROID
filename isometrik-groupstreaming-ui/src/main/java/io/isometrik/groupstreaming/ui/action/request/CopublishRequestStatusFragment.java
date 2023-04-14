package io.isometrik.groupstreaming.ui.action.request;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.action.CopublishActionCallbacks;

/**
 * The Copublish request status bottomsheet dialog fragment to fetch the status of the copublish
 * request by a user for
 * a stream group.
 */
public class CopublishRequestStatusFragment extends BottomSheetDialogFragment {

  public static final String TAG = "CopublishRequestStatusFragment";

  private View view;

  private CopublishActionCallbacks copublishActionCallbacks;

  @BindView(R2.id.ivAcceptedUserImage)
  AppCompatImageView ivAcceptedUserImage;

  @BindView(R2.id.ivRejectedUserImage)
  AppCompatImageView ivRejectedUserImage;

  @BindView(R2.id.ivPendingUserImage)
  AppCompatImageView ivPendingUserImage;

  @BindView(R2.id.ivInitiatorImage)
  AppCompatImageView ivInitiatorImage;

  @BindView(R2.id.tvAcceptedHeading)
  TextView tvAcceptedHeading;

  @BindView(R2.id.tvRejectedHeading)
  TextView tvRejectedHeading;

  private String userImageUrl, initiatorName, initiatorImageUrl;
  private boolean pending, accepted;

  @BindView(R2.id.rlAccepted)
  RelativeLayout rlAccepted;

  @BindView(R2.id.rlRejected)
  RelativeLayout rlRejected;

  @BindView(R2.id.rlPending)
  RelativeLayout rlPending;

  @BindView(R2.id.tvActionDescription)
  TextView tvActionDescription;

  /**
   * Instantiates a new Copublish request status fragment.
   */
  public CopublishRequestStatusFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_copublish_request_status, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);

    if (pending) {
      rlPending.setVisibility(View.VISIBLE);
      rlAccepted.setVisibility(View.GONE);
      rlRejected.setVisibility(View.GONE);
      tvActionDescription.setText(
          getString(R.string.ism_copublish_request_pending_description, initiatorName));
      try {

        Glide.with(IsometrikUiSdk.getInstance().getContext())
            .load(initiatorImageUrl)
            .asBitmap()
            .placeholder(R.drawable.ism_default_profile_image)
            .into(new BitmapImageViewTarget(ivInitiatorImage) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                    IsometrikUiSdk.getInstance().getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivInitiatorImage.setImageDrawable(circularBitmapDrawable);
              }
            });
      } catch (IllegalArgumentException | NullPointerException e) {
        e.printStackTrace();
      }

      try {

        Glide.with(IsometrikUiSdk.getInstance().getContext())
            .load(userImageUrl)
            .asBitmap()
            .placeholder(R.drawable.ism_default_profile_image)
            .into(new BitmapImageViewTarget(ivPendingUserImage) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                    IsometrikUiSdk.getInstance().getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivPendingUserImage.setImageDrawable(circularBitmapDrawable);
              }
            });
      } catch (IllegalArgumentException | NullPointerException e) {
        e.printStackTrace();
      }
    } else {
      rlPending.setVisibility(View.GONE);
      if (accepted) {
        rlAccepted.setVisibility(View.VISIBLE);
        rlRejected.setVisibility(View.GONE);
        tvAcceptedHeading.setText(
            getString(R.string.ism_copublish_previously_accepted_heading, initiatorName));

        try {

          Glide.with(IsometrikUiSdk.getInstance().getContext())
              .load(userImageUrl)
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(ivAcceptedUserImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(
                          IsometrikUiSdk.getInstance().getContext().getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  ivAcceptedUserImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      } else {
        rlAccepted.setVisibility(View.GONE);
        rlRejected.setVisibility(View.VISIBLE);
        tvRejectedHeading.setText(
            getString(R.string.ism_copublish_previously_rejected_heading, initiatorName));

        try {

          Glide.with(IsometrikUiSdk.getInstance().getContext())
              .load(userImageUrl)
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(ivRejectedUserImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(
                          IsometrikUiSdk.getInstance().getContext().getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  ivRejectedUserImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    }

    return view;
  }

  /**
   * Exit on previously accepted request but no longer member
   */
  @OnClick(R2.id.btExit)
  public void exitOnNoLongerBeingAMember() {
    copublishActionCallbacks.exitOnNoLongerBeingAMember();
  }

  /**
   * Delete copublish request.
   */
  @OnClick(R2.id.tvDelete)
  public void deleteCopublishRequest() {
    copublishActionCallbacks.deleteCopublishRequest();
  }

  /**
   * Exit on copublish request rejected.
   */
  @OnClick(R2.id.tvExit)
  public void exitOnCopublishRequestRejected() {
    copublishActionCallbacks.exitOnCopublishRequestRejected();
  }

  /**
   * Continue watching on copublish request rejected.
   */
  @OnClick({ R2.id.tvContinueAccept, R2.id.tvContinueReject, R2.id.tvContinue })
  public void continueWatchingOnCopublishRequestRejected() {
    copublishActionCallbacks.continueWatching();
  }

  /**
   * Update parameters.
   *
   * @param initiatorImageUrl the initiator image url
   * @param userImageUrl the user image url
   * @param initiatorName the initiator name
   * @param pending the pending state of copublish request
   * @param accepted the accepted state of copublish request
   * @param copublishActionCallbacks the copublish action callbacks
   */
  public void updateParameters(String initiatorImageUrl, String userImageUrl, String initiatorName,
      boolean pending, boolean accepted, CopublishActionCallbacks copublishActionCallbacks) {
    this.initiatorImageUrl = initiatorImageUrl;
    this.userImageUrl = userImageUrl;
    this.initiatorName = initiatorName;
    this.copublishActionCallbacks = copublishActionCallbacks;
    this.pending = pending;
    this.accepted = accepted;
  }
}
