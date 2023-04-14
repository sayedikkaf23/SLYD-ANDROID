package io.isometrik.groupstreaming.ui.live;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.R2;
import io.isometrik.groupstreaming.ui.effects.EffectsFragment;
import io.isometrik.groupstreaming.ui.multilive.MultiLiveSelectMembersActivity;
import io.isometrik.groupstreaming.ui.scrollable.ScrollableStreamsActivity;
import io.isometrik.groupstreaming.ui.utils.AlertProgress;
import io.isometrik.groupstreaming.ui.utils.ImageFilePathUtils;
import io.isometrik.groupstreaming.ui.utils.ImageUtil;
import io.isometrik.groupstreaming.ui.utils.blur.BlurTransformation;
import io.isometrik.gs.rtcengine.ar.capture.CaptureOperations;
import io.isometrik.gs.rtcengine.ar.capture.ImageCaptureCallbacks;
import java.io.File;
import java.util.ArrayList;

public class GoLiveActivity extends AppCompatActivity
    implements GoLiveContract.View, ImageCaptureCallbacks {
  GoLiveContract.Presenter goLivePresenter;

  @BindView(R2.id.rlPublic)
  RelativeLayout rlPublic;
  @BindView(R2.id.rlPrivate)
  RelativeLayout rlPrivate;

  @BindView(R2.id.tvMultiStream)
  TextView tvMultiStream;
  @BindView(R2.id.ivMultiStream)
  AppCompatImageView ivMultiStream;
  @BindView(R2.id.tvSingleStream)
  TextView tvSingleStream;
  @BindView(R2.id.ivSingleStream)
  AppCompatImageView ivSingleStream;
  @BindView(R2.id.etStreamDescription)
  AppCompatEditText etStreamDescription;
  @BindView(R2.id.rlParent)
  RelativeLayout rlParent;
  @BindView(R2.id.surface)
  SurfaceView arView;
  @BindView(R2.id.ivCoverImage)
  AppCompatImageView ivCoverImage;
  @BindView(R2.id.ivRemoveCoverImage)
  AppCompatImageView ivRemoveCoverImage;
  @BindView(R2.id.ivCoverImagePlaceHolder)
  AppCompatImageView ivCoverImagePlaceHolder;

  private boolean isPublic = true;
  private boolean multiGuestLive;
  private boolean cleanUpRequested;
  private EffectsFragment effectsFragment;
  private AlertProgress alertProgress;
  private String mediaPath;
  private AlertDialog alertDialog;
  private String capturedImagePath;
  private CaptureOperations captureOperations;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ism_activity_golive);
    ButterKnife.bind(this);

    goLivePresenter = new GoLivePresenter(this);
    alertProgress = new AlertProgress();
    effectsFragment = new EffectsFragment();

    etStreamDescription.setText(IsometrikUiSdk.getInstance().getUserSession().getUserName());
    rlPublic.setSelected(true);
    captureOperations = new CaptureOperations(IsometrikUiSdk.getInstance().getIsometrik(), arView);
    IsometrikUiSdk.getInstance().getIsometrik().setImageCaptureCallbacks(this);
    checkStreamingPermissions();
  }

  @OnClick(R2.id.rlPublic)
  public void togglePublicStreamType() {
    if (!isPublic) {
      rlPublic.setSelected(true);
      rlPrivate.setSelected(false);
      isPublic = !isPublic;
    }
  }

  @OnClick(R2.id.rlPrivate)
  public void togglePrivateStreamType() {
    if (isPublic) {
      rlPublic.setSelected(false);
      rlPrivate.setSelected(true);
      isPublic = !isPublic;
    }
  }

  @OnClick(R2.id.rlSingleStream)
  public void toggleSingleGuestLive() {
    if (multiGuestLive) {
      tvSingleStream.setTextColor(ContextCompat.getColor(this, R.color.ism_white));
      tvMultiStream.setTextColor(ContextCompat.getColor(this, R.color.ism_filters_gray));
      ivSingleStream.setVisibility(View.VISIBLE);
      ivMultiStream.setVisibility(View.GONE);
      multiGuestLive = !multiGuestLive;
    }
  }

  @OnClick(R2.id.rlMultiStream)
  public void toggleMultiGuestLive() {
    if (!multiGuestLive) {
      tvSingleStream.setTextColor(ContextCompat.getColor(this, R.color.ism_filters_gray));
      tvMultiStream.setTextColor(ContextCompat.getColor(this, R.color.ism_white));
      ivSingleStream.setVisibility(View.GONE);
      ivMultiStream.setVisibility(View.VISIBLE);
      multiGuestLive = !multiGuestLive;
    }
  }

  /**
   * Show the effects popup.
   */
  @OnClick(R2.id.ivEffects)
  public void showEffects() {
    effectsFragment.updateParameters(false);
    effectsFragment.show(getSupportFragmentManager(), EffectsFragment.TAG);
  }

  @OnClick(R2.id.tvGoLive)
  public void goLive() {

    if (etStreamDescription.getText() != null && !etStreamDescription.getText()
        .toString()
        .trim()
        .isEmpty()) {

      if (mediaPath != null && ivRemoveCoverImage.getVisibility() == View.VISIBLE) {
        showProgressDialog(getString(R.string.ism_uploading_image));
        try {
          Glide.with(this)
              .load(mediaPath)
              .transform(new CenterCrop(this))
              .bitmapTransform(new BlurTransformation(this))
              .diskCacheStrategy(DiskCacheStrategy.NONE)
              .into(ivCoverImagePlaceHolder);
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
        goLivePresenter.requestImageUpload(mediaPath);
      } else {
        requestImageCapture();
      }
    } else {
      Toast.makeText(GoLiveActivity.this, getString(R.string.ism_invalid_broadcast_description),
          Toast.LENGTH_SHORT).show();
    }
  }

  @OnClick(R2.id.ivClose)
  public void exit() {
    onBackPressed();
  }

  @OnClick(R2.id.rlCoverImage)
  public void addCoverImageFromGallery() {

    checkGalleryPermissions();
  }

  /**
   * {@link GoLiveContract.View#onImageUploadResult(String)}
   */
  @Override
  public void onImageUploadResult(String url) {
    hideProgressDialog();

    if (etStreamDescription.getText() != null) {
      if (multiGuestLive) {
        //To release camera surfaceview explicitly
        arView.setVisibility(View.GONE);
        //Open add members activity
        Intent intent = new Intent(GoLiveActivity.this, MultiLiveSelectMembersActivity.class);
        intent.putExtra("streamDescription", etStreamDescription.getText().toString().trim());
        intent.putExtra("streamImage", url);
        intent.putExtra("isPublic", isPublic);
        startActivity(intent);
        finish();
      } else {
        //Go live directly

        showProgressDialog(getString(R.string.ism_starting_broadcast));

        goLivePresenter.startBroadcast(etStreamDescription.getText().toString().trim(), url,
            isPublic);
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    boolean permissionDenied = false;

    for (int grantResult : grantResults) {
      if (grantResult != PackageManager.PERMISSION_GRANTED) {
        permissionDenied = true;
        break;
      }
    }
    if (permissionDenied) {
      if (requestCode == 0) {
        Toast.makeText(this, getString(R.string.ism_permission_start_streaming_denied),
            Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, getString(R.string.ism_permission_gallery_denied), Toast.LENGTH_LONG)
            .show();
      }
    } else {
      if (requestCode == 0) {
        openCamera();
      } else {
        selectImageFromGallery();
      }
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  /**
   * Check streaming permissions.
   */
  public void checkStreamingPermissions() {

    if ((ContextCompat.checkSelfPermission(GoLiveActivity.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
        GoLiveActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
        GoLiveActivity.this, Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED)) {

      if ((ActivityCompat.shouldShowRequestPermissionRationale(GoLiveActivity.this,
          Manifest.permission.CAMERA))
          || (ActivityCompat.shouldShowRequestPermissionRationale(GoLiveActivity.this,
          Manifest.permission.WRITE_EXTERNAL_STORAGE))
          || (ActivityCompat.shouldShowRequestPermissionRationale(GoLiveActivity.this,
          Manifest.permission.RECORD_AUDIO))) {
        Snackbar snackbar = Snackbar.make(rlParent, R.string.ism_permission_start_streaming,
            Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.ism_ok), view -> this.requestCameraPermissions());

        snackbar.show();

        ((TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text))
            .setGravity(Gravity.CENTER_HORIZONTAL);
      } else {

        requestCameraPermissions();
      }
    } else {

      openCamera();
    }
  }

  private void checkGalleryPermissions() {
    if ((ContextCompat.checkSelfPermission(GoLiveActivity.this,
        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

      if ((ActivityCompat.shouldShowRequestPermissionRationale(GoLiveActivity.this,
          Manifest.permission.READ_EXTERNAL_STORAGE))) {
        Snackbar snackbar =
            Snackbar.make(rlParent, R.string.ism_permission_gallery, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.ism_ok), view -> this.requestGalleryPermission());

        snackbar.show();

        ((TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text))
            .setGravity(Gravity.CENTER_HORIZONTAL);
      } else {

        requestGalleryPermission();
      }
    } else {

      selectImageFromGallery();
    }
  }

  private void requestCameraPermissions() {

    ActivityCompat.requestPermissions(GoLiveActivity.this, new String[] {
        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    }, 0);
  }

  private void requestGalleryPermission() {

    ActivityCompat.requestPermissions(GoLiveActivity.this, new String[] {

        Manifest.permission.READ_EXTERNAL_STORAGE
    }, 1);
  }

  private void selectImageFromGallery() {
    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(pickPhoto, 1);
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

  private void cleanupOnActivityDestroy() {
    if (!cleanUpRequested) {
      cleanUpRequested = true;
      captureOperations.cleanup();
      if (capturedImagePath != null) {
        goLivePresenter.deleteImage(new File(capturedImagePath));
      }
      IsometrikUiSdk.getInstance().getIsometrik().setImageCaptureCallbacks(this);
      hideProgressDialog();
    }
  }

  /**
   * {@link GoLiveContract.View#onImageUploadError(String)}
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

  /**
   * {@link GoLiveContract.View#onBroadcastStarted(String, String, String, ArrayList, long,
   * String)}
   */
  @Override
  public void onBroadcastStarted(String streamId, String streamDescription, String streamImageUrl,
      ArrayList<String> memberIds, long startTime, String userId) {
    hideProgressDialog();
    memberIds.add(userId);
    //To release camera surfaceview explicitly for bug of camera hanging if gone live without multilive, due to surfaceview being not released
    arView.setVisibility(View.GONE);
    cleanupOnActivityDestroy();

    Intent intent = new Intent(GoLiveActivity.this, ScrollableStreamsActivity.class);
    intent.putExtra("streamId", streamId);
    intent.putExtra("streamDescription", streamDescription);
    intent.putExtra("streamImage", streamImageUrl);
    intent.putExtra("startTime", startTime);
    intent.putExtra("membersCount", memberIds.size());
    intent.putExtra("viewersCount", 0);
    intent.putExtra("publishersCount", 1);
    intent.putExtra("joinRequest", true);
    intent.putExtra("isAdmin", true);
    intent.putStringArrayListExtra("memberIds", memberIds);
    intent.putExtra("initiatorName", IsometrikUiSdk.getInstance().getUserSession().getUserName());
    intent.putExtra("publishRequired", false);
    intent.putExtra("initiatorId", IsometrikUiSdk.getInstance().getUserSession().getUserId());

    intent.putExtra("initiatorIdentifier",
        IsometrikUiSdk.getInstance().getUserSession().getUserIdentifier());
    intent.putExtra("initiatorImage",
        IsometrikUiSdk.getInstance().getUserSession().getUserProfilePic());
    intent.putExtra("isPublic", isPublic);
    intent.putExtra("isBroadcaster", true);
    startActivity(intent);
    finish();
  }

  /**
   * {@link GoLiveContract.View#onError(String)}
   */
  @Override
  public void onError(String errorMessage) {

    hideProgressDialog();

    runOnUiThread(() -> {
      if (errorMessage != null) {
        Toast.makeText(GoLiveActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(GoLiveActivity.this, getString(R.string.ism_error), Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  private void openCamera() {
    captureOperations.openCamera(this, getWindowManager());
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK) {
      if (data != null) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          mediaPath = ImageFilePathUtils.getPathAboveN(this, data.getData());
        } else {

          mediaPath = ImageFilePathUtils.getPath(this, data.getData());
        }

        try {
          Glide.with(this).load(mediaPath).centerCrop()

              .listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                    boolean isFirstResource) {
                  return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model,
                    Target<GlideDrawable> target, boolean isFromMemoryCache,
                    boolean isFirstResource) {
                  ivRemoveCoverImage.setVisibility(View.VISIBLE);
                  return false;
                }
              }).into(ivCoverImage);
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    } else if (resultCode == RESULT_CANCELED) {
      Toast.makeText(GoLiveActivity.this, getString(R.string.ism_gallery_cancel),
          Toast.LENGTH_SHORT).show();
    }
  }

  private void requestImageCapture() {
    showProgressDialog(getString(R.string.ism_capturing_image));
    captureOperations.requestImageCapture();
  }

  @OnClick(R2.id.ivRemoveCoverImage)
  public void removeCoverImage() {
    ivRemoveCoverImage.setVisibility(View.GONE);
    mediaPath = null;
    try {
      Glide.clear(ivCoverImage);
    } catch (IllegalArgumentException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void imageCaptured(Bitmap bitmap) {
    if (bitmap != null) {

      new Handler().post(() -> {

        hideProgressDialog();
        capturedImagePath = ImageUtil.saveCapturedBitmap(bitmap, this);
        if (capturedImagePath != null) {
          showProgressDialog(getString(R.string.ism_uploading_image));
          try {
            Glide.with(this)
                .load(capturedImagePath)
                .transform(new CenterCrop(this))
                .bitmapTransform(new BlurTransformation(this))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivCoverImagePlaceHolder);
          } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
          }

          goLivePresenter.requestImageUpload(capturedImagePath);
        } else {
          Toast.makeText(GoLiveActivity.this, getString(R.string.ism_image_capture_failure),
              Toast.LENGTH_SHORT).show();
        }
      });
    } else {
      Toast.makeText(GoLiveActivity.this, getString(R.string.ism_image_capture_failure),
          Toast.LENGTH_SHORT).show();
    }
  }
}
