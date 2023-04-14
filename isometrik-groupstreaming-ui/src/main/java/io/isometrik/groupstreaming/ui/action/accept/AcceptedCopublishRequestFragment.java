package io.isometrik.groupstreaming.ui.action.accept;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * The Accepted copublish request bottomsheet dialog fragment to show the bottomsheet when copublish
 * request has been
 * accepted.
 */
public class AcceptedCopublishRequestFragment extends BottomSheetDialogFragment {

  public static final String TAG = "AcceptedCopublishRequestFragment";

  private View view;

  private CopublishActionCallbacks copublishActionCallbacks;

  @BindView(R2.id.ivUserImage)
  AppCompatImageView ivUserImage;

  @BindView(R2.id.tvAcceptedHeading)
  TextView tvAcceptedHeading;

  private String userImageUrl, userName;

  /**
   * Instantiates a new Accepted copublish request fragment.
   */
  public AcceptedCopublishRequestFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_accepted_copublish, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);
    tvAcceptedHeading.setText(getString(R.string.ism_copublish_accepted_heading, userName));
    try {

      Glide.with(IsometrikUiSdk.getInstance().getContext())
          .load(userImageUrl)
          .asBitmap()
          .placeholder(R.drawable.ism_default_profile_image)
          .into(new BitmapImageViewTarget(ivUserImage) {
            @Override
            protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                  IsometrikUiSdk.getInstance().getContext().getResources(), resource);
              circularBitmapDrawable.setCircular(true);
              ivUserImage.setImageDrawable(circularBitmapDrawable);
            }
          });
    } catch (IllegalArgumentException | NullPointerException e) {
      e.printStackTrace();
    }

    return view;
  }

  /**
   * Start publishing.
   */
  @OnClick(R2.id.btStartPublish)
  public void startPublishing() {
    copublishActionCallbacks.startPublishingOnCopublishRequestAccepted();
  }

  /**
   * Update parameters.
   *
   * @param userImageUrl the user image url
   * @param userName the user name
   * @param copublishActionCallbacks the copublish action callbacks
   */
  public void updateParameters(String userImageUrl, String userName,
      CopublishActionCallbacks copublishActionCallbacks) {
    this.userImageUrl = userImageUrl;
    this.userName = userName;
    this.copublishActionCallbacks = copublishActionCallbacks;
  }
}
