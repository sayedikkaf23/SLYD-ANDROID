package io.isometrik.groupstreaming.ui.profile;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;

public class UserInfoFragment extends BottomSheetDialogFragment {

  public static final String TAG = "UserInfoFragment";

  private View view;

  @BindView(R2.id.tvUserName)
  TextView tvUserName;
  @BindView(R2.id.tvUserIdetifier)
  TextView tvUserIdetifier;

  @BindView(R2.id.ivUserImage)
  AppCompatImageView ivUserImage;
  private String userName, userIdentifier, userImageUrl;

  public UserInfoFragment() {
    // Required empty public constructor
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.ism_bottomsheet_userdetails, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }
    ButterKnife.bind(this, view);
    tvUserName.setText(userName);
    tvUserIdetifier.setText(userIdentifier);

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

  public void updateParameters(String userName, String userIdentifier, String userImageUrl) {
    this.userName = userName;
    this.userIdentifier = userIdentifier;
    this.userImageUrl = userImageUrl;
  }
}
