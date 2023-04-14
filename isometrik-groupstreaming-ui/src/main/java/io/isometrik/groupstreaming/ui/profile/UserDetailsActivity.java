package io.isometrik.groupstreaming.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.users.UsersActivity;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.UserSession;

/**
 * The type User details activity.
 * It implements UserDetailsContract.View{@link UserDetailsContract.View}
 *
 * @see UserDetailsContract.View
 */
public class UserDetailsActivity extends AppCompatActivity implements UserDetailsContract.View {

  private UserDetailsContract.Presenter userDetailsPresenter;

  @BindView(R2.id.ibEdit)
  AppCompatImageButton ibEdit;

  @BindView(R2.id.ibDelete)
  AppCompatImageButton ibDelete;

  @BindView(R2.id.tvNameValue)
  TextView tvName;

  @BindView(R2.id.tvUserIdentifierValue)
  TextView tvUserIdentifier;

  @BindView(R2.id.btSwitchUser)
  AppCompatButton btSwitchUser;

  @BindView(R2.id.ivProfilePic)
  AppCompatImageView ivProfilePic;

  private AlertProgress alertProgress;

  private UserSession userSession;
  private AlertDialog alertDialog;
  private boolean cleanUpRequested;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ism_activity_user_details);
    ButterKnife.bind(this);

    userDetailsPresenter = new UserDetailsPresenter(this);
    alertProgress = new AlertProgress();

    userSession = IsometrikUiSdk.getInstance().getUserSession();

    updateUserDetails(false, userSession.getUserName(), userSession.getUserIdentifier(),
        userSession.getUserProfilePic(), false, false, false);
    userDetailsPresenter.requestUserDetails(userSession.getUserId());
  }

  /**
   * Switch user.
   */
  @OnClick({ R2.id.btSwitchUser })
  public void switchUser() {
    cleanupOnActivityDestroy();

    userDetailsPresenter.clearUserSession();
    startActivity(new Intent(UserDetailsActivity.this, UsersActivity.class).addFlags(
        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    finish();
  }

  /**
   * Edit user.
   */
  @OnClick({ R2.id.ibEdit })
  public void editUser() {
    startActivityForResult(new Intent(UserDetailsActivity.this, EditUserActivity.class), 0);
  }

  /**
   * Delete user.
   */
  @OnClick({ R2.id.ibDelete })
  public void deleteUser() {
    showProgressDialog(getString(R.string.ism_deleting_user));
    userDetailsPresenter.requestUserDelete(userSession.getUserId());
  }

  /**
   * Back.
   */
  @OnClick({ R2.id.ibBack })
  public void back() {
    onBackPressed();
  }

  @Override
  public void onBackPressed() {
    cleanupOnActivityDestroy();
    try {
      super.onBackPressed();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onDestroy() {
    cleanupOnActivityDestroy();
    super.onDestroy();
  }

  /**
   * {@link UserDetailsContract.View#onUserDetailsReceived(String, String, String, boolean, boolean,
   * boolean)}
   */
  @Override
  public void onUserDetailsReceived(String userName, String userIdentifier,
      String userProfilePicUrl, boolean updateUserName, boolean updateUserIdentifier,
      boolean updateUserProfilePic) {

    runOnUiThread(
        () -> updateUserDetails(true, userName, userIdentifier, userProfilePicUrl, updateUserName,
            updateUserIdentifier, updateUserProfilePic));
  }

  /**
   * {@link UserDetailsContract.View#onUserDeleted()}
   */
  @Override
  public void onUserDeleted() {
    switchUser();
  }

  /**
   * {@link UserDetailsContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();

    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(UserDetailsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(UserDetailsActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

    if (requestCode == 0) {

      if (resultCode == RESULT_OK) {

        updateUserDetails(true, intent.getStringExtra("userName"),
            intent.getStringExtra("userIdentifier"), intent.getStringExtra("userProfilePic"),
            intent.getBooleanExtra("userNameUpdated", false),
            intent.getBooleanExtra("userIdentifierUpdated", false),
            intent.getBooleanExtra("userProfilePicUpdated", false));
      } else {

        Toast.makeText(this, getString(R.string.ism_edit_user_details_canceled), Toast.LENGTH_LONG)
            .show();
      }
    }
  }

  private void updateUserDetails(boolean remote, String userName, String userIdentifier,
      String userProfilePicUrl, boolean updateUserName, boolean updateUserIdentifier,
      boolean updateUserProfilePic) {

    if (remote) {

      if (updateUserName) {
        tvName.setText(userName);
      }

      if (updateUserIdentifier) {
        tvUserIdentifier.setText(userIdentifier);
      }

      if (updateUserProfilePic) {
        try {
          Glide.with(this)
              .load(userProfilePicUrl)
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(ivProfilePic) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  ivProfilePic.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    } else {

      tvName.setText(userName);
      tvUserIdentifier.setText(userIdentifier);
      try {
        Glide.with(this)
            .load(userProfilePicUrl)
            .asBitmap()
            .placeholder(R.drawable.ism_default_profile_image)
            .into(new BitmapImageViewTarget(ivProfilePic) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivProfilePic.setImageDrawable(circularBitmapDrawable);
              }
            });
      } catch (IllegalArgumentException | NullPointerException e) {
        e.printStackTrace();
      }

      if (userSession.getUserSelected()) {

        ibEdit.setVisibility(View.GONE);
        ibDelete.setVisibility(View.GONE);
      } else {

        ibEdit.setVisibility(View.VISIBLE);
        ibDelete.setVisibility(View.VISIBLE);
      }
    }
  }

  private void showProgressDialog(String message) {

    alertDialog = alertProgress.getProgressDialog(this, message);
    if (!isFinishing()) alertDialog.show();
  }

  private void hideProgressDialog() {

    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
  }

  private void cleanupOnActivityDestroy() {
    if (!cleanUpRequested) {
      cleanUpRequested = true;
      hideProgressDialog();
    }
  }
}
