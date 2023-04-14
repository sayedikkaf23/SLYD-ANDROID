package io.isometrik.groupstreaming.ui.action.request;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * The Request copublish bottomsheet dialog fragment to add a copublish request for a stream group.
 */
public class RequestCopublishFragment extends BottomSheetDialogFragment {

  public static final String TAG = "RequestCopublishFragment";

  private View view;

  @BindView(R2.id.ivInitiatorImage)
  AppCompatImageView ivInitiatorImage;

  @BindView(R2.id.ivUserImage)
  AppCompatImageView ivUserImage;

  private String initiatorImageUrl, userImageUrl;
  private CopublishActionCallbacks copublishActionCallbacks;

  /**
   * Instantiates a new Request copublish fragment.
   */
  public RequestCopublishFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_request_copublish, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);

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
   * Request copublish.
   */
  @OnClick(R2.id.btAction)
  public void requestCopublish() {
    copublishActionCallbacks.requestCopublish();
  }

  /**
   * Update parameters.
   *
   * @param initiatorImageUrl the initiator image url
   * @param userImageUrl the user image url
   * @param copublishActionCallbacks the copublish action callbacks
   */
  public void updateParameters(String initiatorImageUrl, String userImageUrl,
      CopublishActionCallbacks copublishActionCallbacks) {
    this.initiatorImageUrl = initiatorImageUrl;
    this.userImageUrl = userImageUrl;
    this.copublishActionCallbacks = copublishActionCallbacks;
  }
}
