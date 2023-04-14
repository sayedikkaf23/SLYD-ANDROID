package io.isometrik.groupstreaming.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.snackbar.Snackbar;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.streams.grid.StreamsActivity;
import io.isometrik.groupstreaming.ui.users.UsersActivity;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.ImageUtil;
import java.io.File;
import java.io.IOException;

/**
 * The type Create user activity.
 * It implements CreateUserContract.View{@link CreateUserContract.View}
 *
 * @see CreateUserContract.View
 */
public class CreateUserActivity extends AppCompatActivity implements CreateUserContract.View {

  private CreateUserContract.Presenter createUserPresenter;

  @BindView(R2.id.tvSelectUser)
  TextView tvSelectUser;

  @BindView(R2.id.rlParent)
  RelativeLayout rlParent;

  @BindView(R2.id.ibAddImage)
  AppCompatImageButton ibAddImage;

  @BindView(R2.id.etName)
  AppCompatEditText etName;

  @BindView(R2.id.etUserIdentifier)
  AppCompatEditText etUserIdentifier;

  @BindView(R2.id.btCreate)
  AppCompatButton btCreate;

  @BindView(R2.id.ivProfilePic)
  AppCompatImageView ivProfilePic;

  private AlertProgress alertProgress;

  private File imageFile;
  private AlertDialog alertDialog;
  private boolean cleanUpRequested = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ism_activity_create_user);
    ButterKnife.bind(this);

    createUserPresenter = new CreateUserPresenter(this);
    alertProgress = new AlertProgress();
  }

  /**
   * {@link CreateUserContract.View#onUserCreated()}
   */
  @Override
  public void onUserCreated() {
    startActivity(new Intent(CreateUserActivity.this, StreamsActivity.class));
    finish();
  }

  /**
   * Back.
   */
  @OnClick({ R2.id.ibBack, R2.id.tvSelectUser })
  public void back() {
    onBackPressed();
  }

  /**
   * Validate user details.
   */
  @OnClick({ R2.id.btCreate })
  public void validateUserDetails() {

    createUserPresenter.validateUserDetails(etName.getText().toString(),
        etUserIdentifier.getText().toString(), imageFile);
  }

  /**
   * Check camera permissions.
   */
  @OnClick({ R2.id.ibAddImage })
  public void checkCameraPermissions() {

    if ((ContextCompat.checkSelfPermission(CreateUserActivity.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
        CreateUserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED)) {

      if ((ActivityCompat.shouldShowRequestPermissionRationale(CreateUserActivity.this,
          Manifest.permission.CAMERA)) || (ActivityCompat.shouldShowRequestPermissionRationale(
          CreateUserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
        Snackbar snackbar = Snackbar.make(rlParent, R.string.ism_permission_image_capture,
            Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.ism_ok), view -> requestPermissions());

        snackbar.show();

        ((TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text))
            .setGravity(Gravity.CENTER_HORIZONTAL);
      } else {

        requestPermissions();
      }
    } else {

      requestImageCapture();
    }
  }

  private void requestPermissions() {

    ActivityCompat.requestPermissions(CreateUserActivity.this,
        new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
  }

  @Override
  public void onBackPressed() {
    cleanupOnActivityDestroy();
    startActivity(new Intent(CreateUserActivity.this, UsersActivity.class));
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
   * {@link CreateUserContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {
    hideProgressDialog();
    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(CreateUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(CreateUserActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  /**
   * {@link CreateUserContract.View#onImageUploadResult(String)}
   */
  @Override
  public void onImageUploadResult(String url) {
    hideProgressDialog();
    showProgressDialog(getString(R.string.ism_creating_user));
    createUserPresenter.requestCreateUser(etName.getText().toString(),
        etUserIdentifier.getText().toString(), url);
  }

  /**
   * {@link CreateUserContract.View#onUserDetailsValidationResult(String)}
   */
  @Override
  public void onUserDetailsValidationResult(String errorMessage) {

    if (errorMessage != null) {

      Toast.makeText(CreateUserActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    } else {

      showProgressDialog(getString(R.string.ism_uploading_image));
      createUserPresenter.requestImageUpload(imageFile.getAbsolutePath());
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    boolean permissionDenied = false;
    if (requestCode == 0) {

      for (int grantResult : grantResults) {
        if (grantResult != PackageManager.PERMISSION_GRANTED) {
          permissionDenied = true;
          break;
        }
      }
      if (permissionDenied) {
        Toast.makeText(this, getString(R.string.ism_permission_image_capture_denied),
            Toast.LENGTH_LONG).show();
      } else {
        requestImageCapture();
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);

    if (requestCode == 0) {

      if (resultCode == RESULT_OK) {

        if (imageFile != null) {
          try {
            Glide.with(this)
                .load(imageFile.getAbsolutePath())
                .asBitmap()
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
        } else {
          deleteImage();
          Toast.makeText(this, getString(R.string.ism_image_capture_failure), Toast.LENGTH_LONG)
              .show();
        }
      } else {
        deleteImage();
        Toast.makeText(this, getString(R.string.ism_image_capture_canceled), Toast.LENGTH_LONG)
            .show();
      }
    }
  }

  private void requestImageCapture() {

    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

      imageFile = null;
      try {
        imageFile =
            ImageUtil.createImageFile(String.valueOf(System.currentTimeMillis()), false, this);
      } catch (IOException ex) {
        ex.printStackTrace();
      }

      if (imageFile != null) {
        Uri photoURI = FileProvider.getUriForFile(this,
            IsometrikUiSdk.getInstance().getApplicationId() + ".provider", imageFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        startActivityForResult(takePictureIntent, 0);
      } else {
        Toast.makeText(this, getString(R.string.ism_image_capture_failure), Toast.LENGTH_LONG)
            .show();
      }
    } else {
      Toast.makeText(this, getString(R.string.ism_camera_not_found), Toast.LENGTH_LONG).show();
    }
  }

  private void deleteImage() {

    createUserPresenter.deleteImage(imageFile);
  }

  /**
   * {@link CreateUserContract.View#onImageUploadError(String)}
   */
  @Override
  public void onImageUploadError(String errorMessage) {
    hideProgressDialog();
    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
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
      deleteImage();
    }
  }
}
