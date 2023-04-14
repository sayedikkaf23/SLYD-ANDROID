package chat.hola.com.app.DublyCamera.CameraInFragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.DublyFilterAdapter;
import chat.hola.com.app.DublyCamera.DublyFilterModel;
import chat.hola.com.app.DublyCamera.FilterType;
import chat.hola.com.app.DublyCamera.ResultHolder;
import chat.hola.com.app.DublyCamera.RippleBackground;
import chat.hola.com.app.DublyCamera.SampleCameraGLView;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.CameraRecordListener;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.GPUCameraRecorder;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.GPUCameraRecorderBuilder;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.LensFacing;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.GONE;

public class ImageCaptureFragment extends Fragment {

  private static final int CAMERA_PERMISSIONS_REQ_CODE = 0;

  private GPUCameraRecorder GPUCameraRecorder;
  private LensFacing lensFacing = LensFacing.FRONT;
  //Binding views

  @BindView(R.id.camera)
  FrameLayout camera;
  @BindView(R.id.contentFrame)
  RelativeLayout parent;
  @BindView(R.id.selctsound)
  LinearLayout selectSound;
  @BindView(R.id.speedLayout)
  LinearLayout speedLayout;
  @BindView(R.id.blackCover)
  View coverView;
  //Bindviews for camera controls
  @BindView(R.id.facingButton)
  AppCompatImageView facingButton;
  @BindView(R.id.flashButton)
  AppCompatImageView flashButton;
  @BindView(R.id.rippleCaptureButton)
  RippleBackground rippleCaptureButton;
  @BindView(R.id.captureButton)
  AppCompatImageView captureButton;
  @BindView(R.id.resetZoom)
  AppCompatImageView resetZoom;
  private String appName;
  //For the data received in bundle/extras
  private String requestType;

  private boolean isFrontFace = false;

  private ProgressDialog dialog;

  private String folderPath;

  private boolean noPendingPermissionsRequest = true;

  private boolean captureImageDisabled = false;

  private SampleCameraGLView sampleGLView;

  private DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
  private int screenWidth = displayMetrics.widthPixels;
  private int cameraWidth = screenWidth;
  private int videoWidth = screenWidth;

  private int screenHeight = displayMetrics.heightPixels;
  private int cameraHeight = screenWidth;
  private int videoHeight = screenWidth;

  //    private int screenHeight = displayMetrics.heightPixels;
  //    private int cameraHeight = screenHeight;
  //    private int videoHeight = screenHeight;
  private boolean toggleCamera = false;

  private boolean flashRunning;
  private TimerTask imageCaptureTimerTask;
  private int alreadyTriedTimes;
  private static final int maxRetries = 4;
  private boolean bitmapCaptureAlreadyRunning = false;

  private static final int MAXIMUM_PIXELS_TO_COMPARE = 100;
  private boolean updatePixelComparisonLimitRequired = false;
  private Bus bus = AppController.getBus();

  private Context context;
  private boolean fragmentVisible;
  private View view;

  //For camera zoom functionality
  private float dX, dY;
  private float scale;
  private float initialCaptureButtonX, initialCaptureButtonY;
  private float nextYCoordinateForZoomOut;
  private float nextYCoordinateForZoomIn;
  private boolean canZoomOut;
  private ArrayList<Float> lengthsForZoom = new ArrayList<>();
  private int currentIndexForZoomInLevel, currentIndexForZoomOutLevel;
  private float maxLengthAvailableForZoom;
  private boolean notFirstTime;
  @BindView(R.id.rlCameraControls)
  RelativeLayout rlCameraControls;
  @BindView(R.id.llTimer)
  LinearLayout llTimer;
  /**
   * Filters
   */

  private ArrayList<DublyFilterModel> filterItems;
  private DublyFilterAdapter filterAdapter;
  RecyclerView filtersList;
  private View mFiltersView;
  private android.app.AlertDialog mFilterDialog;

  @SuppressLint("ClickableViewAccessibility")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    if (view == null) {

      view = inflater.inflate(R.layout.camera_main_fragment, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }

    context = getActivity();

    ButterKnife.bind(this, view);

    ViewGroup.LayoutParams params = camera.getLayoutParams();
    params.height = screenWidth;
    camera.setLayoutParams(params);

    params = coverView.getLayoutParams();
    params.height = screenWidth;
    coverView.setLayoutParams(params);

    int density = (int) context.getResources().getDisplayMetrics().density;

    RelativeLayout.LayoutParams layoutParams =
        (RelativeLayout.LayoutParams) facingButton.getLayoutParams();
    layoutParams.setMargins(13 * density, cameraHeight - 43 * density, 0, 0);

    facingButton.setLayoutParams(layoutParams);

    layoutParams = (RelativeLayout.LayoutParams) flashButton.getLayoutParams();
    layoutParams.setMargins(0, cameraHeight - 43 * density, 10 * density, 0);

    flashButton.setLayoutParams(layoutParams);
    layoutParams = (RelativeLayout.LayoutParams) resetZoom.getLayoutParams();
    layoutParams.setMargins(0, cameraHeight - 43 * density, 0, 0);

    resetZoom.setLayoutParams(layoutParams);

    final File imageFolder;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

      imageFolder = new File(
          getActivity().getExternalFilesDir(null) + "/" + getResources().getString(
              R.string.app_name) + "/Media/");

      //      imageFolder = new File(
      //          Environment.getExternalStorageDirectory() + "/" + getResources().getString(
      //              R.string.app_name) + "/Media/");
    } else {

      imageFolder = new File(
          context.getFilesDir() + "/" + getResources().getString(R.string.app_name) + "/Media/");
    }
    if (!imageFolder.exists() && !imageFolder.isDirectory()) imageFolder.mkdirs();

    folderPath = imageFolder.getAbsolutePath();

    appName = "Demo";
    //getResources().getString(R.string.app_name);

    dialog = new ProgressDialog(context);
    dialog.setCancelable(false);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((CameraInFragmentsActivity) context).getWindowManager()
        .getDefaultDisplay()
        .getMetrics(displayMetrics);
    scale = displayMetrics.density;
    isFrontFace = true;

    requestType = ((CameraInFragmentsActivity) context).getIntent().getStringExtra("call");

    if (requestType == null) {
      requestType = "post";
    } else if (requestType.equals("SaveProfile")) {

      requestType = "SaveProfile";
    }
    speedLayout.setVisibility(GONE);
    selectSound.setVisibility(GONE);
    llTimer.setVisibility(GONE);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      parent.setFitsSystemWindows(true);
    }

    setupFilterView();

    setupZoomAllowedMetrics();
    rippleCaptureButton.setOnTouchListener(handleTouch);

    facingButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //                releaseCamera();
        coverView.setAlpha(0);
        coverView.setVisibility(View.VISIBLE);
        coverView.animate()
            .alpha(1)
            .setStartDelay(0)
            .setDuration(300)
            .setListener(new AnimatorListenerAdapter() {
              @Override
              public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                releaseCamera();
                if (lensFacing == LensFacing.FRONT) {

                  lensFacing = LensFacing.BACK;

                  changeViewImageResource((ImageView) view, R.drawable.ic_facing_front);
                } else {
                  lensFacing = LensFacing.FRONT;

                  changeViewImageResource((ImageView) view, R.drawable.ic_facing_back);
                }
                toggleCamera = true;

                coverView.animate()
                    .alpha(0)
                    .setStartDelay(200)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                      @Override
                      public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        coverView.setVisibility(GONE);
                      }
                    })
                    .start();
              }
            })
            .start();
      }
    });

    flashButton.setOnClickListener(view -> {

      if (GPUCameraRecorder != null && GPUCameraRecorder.isFlashSupport()) {
        GPUCameraRecorder.switchFlashMode();

        GPUCameraRecorder.changeAutoFocus();
      }

      if (flashRunning) {

        flashRunning = false;
        changeViewImageResource((AppCompatImageView) view, R.drawable.ic_flash_on);
      } else {

        flashRunning = true;
        changeViewImageResource((AppCompatImageView) view, R.drawable.ic_flash_off);
      }
    });

    resetZoom.setOnClickListener(view -> {
      if (GPUCameraRecorder != null) {

        resetCameraZoom();
        GPUCameraRecorder.zoomOut(true, true);
      }
    });

    bus.register(this);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();

    if (fragmentVisible) {
      if (noPendingPermissionsRequest) {
        validateCameraPermissions();
      } else {

        noPendingPermissionsRequest = true;
      }
    }
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    fragmentVisible = isVisibleToUser;
    if (isVisibleToUser) {

      if (noPendingPermissionsRequest) {
        validateCameraPermissions();
      } else {

        noPendingPermissionsRequest = true;
      }
    } else {

      releaseResources();
    }
  }

  @Override
  public void onStop() {

    releaseResources();
    super.onStop();
  }

  private void releaseResources() {
    try {

      if (imageCaptureTimerTask != null) {
        imageCaptureTimerTask.cancel();
      }

      if (dialog != null && dialog.isShowing()) {
        dialog.cancel();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    releaseCamera();
  }
  //    public void onBackPressed() {
  //        ((CameraInFragmentsActivity) context).onBackPressed();
  //    }
  //
  //
  //    @OnClick(R.id.back_button)
  //    public void back() {
  //        onBackPressed();
  //    }

  private void checkCameraPermissions() {

    if (ActivityCompat.shouldShowRequestPermissionRationale((CameraInFragmentsActivity) context,
        Manifest.permission.CAMERA)
        || ActivityCompat.shouldShowRequestPermissionRationale((CameraInFragmentsActivity) context,
        Manifest.permission.READ_EXTERNAL_STORAGE)
        || ActivityCompat.shouldShowRequestPermissionRationale((CameraInFragmentsActivity) context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setTitle("Permissions");
      builder.setMessage(R.string.camera_permission);
      builder.setPositiveButton("OK", (dialogInterface, i) -> requestCameraPermission());
      builder.setNegativeButton("DENY", (dialogInterface, i) -> {
        cameraPermissionsDenied();
      });
      builder.show();
    } else {

      requestCameraPermission();
    }
  }

  private void cameraPermissionsDenied() {

    if (parent != null) {
      Snackbar snackbar =
          Snackbar.make(parent, R.string.camera_permission_denied, Snackbar.LENGTH_SHORT);
      snackbar.show();

      View view = snackbar.getView();
      TextView txtv = view.findViewById(R.id.snackbar_text);
      txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
      }
    }, 500);
  }

  private void requestCameraPermission() {

    ArrayList<String> permissionsRequired = new ArrayList<>();

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {

      permissionsRequired.add(Manifest.permission.CAMERA);
    }

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {

      permissionsRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {

      permissionsRequired.add(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    noPendingPermissionsRequest = false;
    requestPermissions(permissionsRequired.toArray(new String[permissionsRequired.size()]),
        CAMERA_PERMISSIONS_REQ_CODE);
  }

  private void handleImageCaptured(Bitmap bitmap) {

    final String path = saveCapturedBitmap(bitmap);

    ResultHolder.dispose();

    ResultHolder.setCall(requestType);
    ResultHolder.setType("image");

    ResultHolder.setPath(path);

    Intent intent;

    if (AppController.getInstance().isPreviewImagesForFiltersToBeGenerated()) {
      intent = new Intent(context, PreviewFragmentFilterImageActivity.class);
    } else {
      intent = new Intent(context, PreviewFragmentImageActivity.class);
    }
    intent.putExtra("isFrontFace", isFrontFace);
    if (requestType.equals("SaveProfile")) {
      selectSound.setVisibility(GONE);
      Bundle bundle = ((CameraInFragmentsActivity) context).getIntent().getExtras();
      String userName = bundle != null ? bundle.getString("userName") : "";
      String firstName = bundle != null ? bundle.getString("firstName") : "";
      String lastName = bundle != null ? bundle.getString("lastName") : "";
      boolean isPrivate = bundle.getBoolean("isPrivate");
      if (!TextUtils.isEmpty(userName)) intent.putExtra("userName", userName);
      if (!TextUtils.isEmpty(firstName)) intent.putExtra("firstName", firstName);
      if (!TextUtils.isEmpty(lastName)) intent.putExtra("lastName", lastName);
      intent.putExtra("isPrivate", isPrivate);
    }

    startActivity(intent);
    if (requestType.equals("story")) {
      ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
    }
  }

  private void validateCameraPermissions() {

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED) {

      try {
        setUpCamera();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      checkCameraPermissions();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == CAMERA_PERMISSIONS_REQ_CODE) {

      boolean allPermissionsGranted = true;
      for (int grantResult : grantResults) {

        if (grantResult != PackageManager.PERMISSION_GRANTED) {

          allPermissionsGranted = false;
          break;
        }
      }
      if (allPermissionsGranted) {

        setUpCamera();
      } else {
        cameraPermissionsDenied();
      }
    }
  }

  private void handleViewTouchFeedback(View view, MotionEvent motionEvent) {
    switch (motionEvent.getAction()) {
      case MotionEvent.ACTION_DOWN: {
        touchDownAnimation(view);
      }

      case MotionEvent.ACTION_UP: {
        touchUpAnimation(view);
      }
    }
  }

  private void touchDownAnimation(View view) {
    view.animate()
        .scaleX(0.88f)
        .scaleY(0.88f)
        .setDuration(300)
        .setInterpolator(new OvershootInterpolator())
        .start();
  }

  private void touchUpAnimation(View view) {
    view.animate()
        .scaleX(1f)
        .scaleY(1f)
        .setDuration(300)
        .setInterpolator(new OvershootInterpolator())
        .start();
  }

  private void changeViewImageResource(final ImageView imageView, @DrawableRes final int resId) {
    imageView.setRotation(0);
    imageView.animate()
        .rotationBy(360)
        .setDuration(400)
        .setInterpolator(new OvershootInterpolator())
        .start();
    imageView.postDelayed(() -> imageView.setImageResource(resId), 120);
  }

  //Code for the video filters
  private void releaseCamera() {
    if (sampleGLView != null) {
      sampleGLView.onPause();
    }

    if (GPUCameraRecorder != null) {
      GPUCameraRecorder.stop();
      GPUCameraRecorder.release();
      GPUCameraRecorder = null;
    }

    if (sampleGLView != null) {
      (camera).removeView(sampleGLView);
      sampleGLView = null;
    }
    try {

      if (filterItems != null) deselectAllFilters();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setUpCameraView() {

    ((CameraInFragmentsActivity) context).runOnUiThread(() -> {

      camera.removeAllViews();
      sampleGLView = null;
      sampleGLView = new SampleCameraGLView(context.getApplicationContext());

      sampleGLView.setTouchListener((event, width, height) -> {
        if (GPUCameraRecorder == null) return;
        GPUCameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
      });
      camera.addView(sampleGLView);
    });
  }

  private void setUpCamera() {
    setUpCameraView();

    if (notFirstTime) {

      resetCameraZoom();
    } else {

      notFirstTime = true;
    }

    GPUCameraRecorder =
        new GPUCameraRecorderBuilder((CameraInFragmentsActivity) context, sampleGLView)

            .cameraRecordListener(new CameraRecordListener() {
              @Override
              public void onGetFlashSupport(boolean flashSupport) {
                ((CameraInFragmentsActivity) context).runOnUiThread(() -> {

                  flashRunning = false;

                  changeViewImageResource(flashButton, R.drawable.ic_flash_on);

                  if (flashSupport) {
                    flashButton.setVisibility(View.VISIBLE);
                  } else {

                    flashButton.setVisibility(GONE);
                  }
                });
              }

              @Override
              public void onRecordComplete() {

              }

              @Override
              public void onRecordStart() {

              }

              @Override
              public void onError(Exception e) {
                e.printStackTrace();
              }

              @Override
              public void onCameraThreadFinish() {

                if (toggleCamera) {

                  ((CameraInFragmentsActivity) context).runOnUiThread(() -> setUpCamera());
                }
                toggleCamera = false;
              }
            })
            .videoSize(videoWidth, videoHeight)
            .cameraSize(cameraWidth, cameraHeight)
            .lensFacing(lensFacing)
            .mute(true)
            .build();
  }

  private void captureBitmap(final ImageCaptureFragment.BitmapReadyCallbacks bitmapReadyCallbacks) {

    sampleGLView.queueEvent(() -> {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

        Bitmap snapshotBitmap =
            Bitmap.createBitmap(sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        PixelCopy.request(sampleGLView, snapshotBitmap,
            i -> ((CameraInFragmentsActivity) context).runOnUiThread(
                () -> bitmapReadyCallbacks.onBitmapReady(snapshotBitmap)),
            new Handler(Looper.getMainLooper()));
      } else {
        EGL10 egl = (EGL10) EGLContext.getEGL();

        GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();

        Bitmap snapshotBitmap = createBitmapFromGLSurface(sampleGLView.getMeasuredWidth(),
            sampleGLView.getMeasuredHeight(), gl);

        //            if (snapshotBitmap != null) {
        ((CameraInFragmentsActivity) context).runOnUiThread(
            () -> bitmapReadyCallbacks.onBitmapReady(snapshotBitmap));
        //            }

      }
    });
  }

  private Bitmap createBitmapFromGLSurface(int w, int h, GL10 gl) {

    //        if (i < maxRetries-1) {
    //            i++;
    //
    //            return null;
    //        }

    int[] bitmapBuffer = new int[w * h];
    int[] bitmapSource = new int[w * h];
    IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
    intBuffer.position(0);

    try {

      int count1 = 0, count2 = 0;

      int maxPixelComparisons = MAXIMUM_PIXELS_TO_COMPARE;
      //TODO
      // Have to add code for handling failure in weakpixel,luminance threshold and cga_colorspce filter

      if (updatePixelComparisonLimitRequired) {

        maxPixelComparisons = w * h;
      }

      boolean requiredNumberOfPixelsNotChecked = true;

      gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);

      int offset1, offset2, texturePixel, blue, red, pixel;

      long firstPixelValue = bitmapBuffer[0];

      for (int i = 0; i < h; i++) {
        offset1 = i * w;
        offset2 = (h - i - 1) * w;
        for (int j = 0; j < w; j++) {
          texturePixel = bitmapBuffer[offset1 + j];
          blue = (texturePixel >> 16) & 0xff;
          red = (texturePixel << 16) & 0x00ff0000;
          pixel = (texturePixel & 0xff00ff00) | red | blue;
          bitmapSource[offset2 + j] = pixel;
          if (requiredNumberOfPixelsNotChecked) {
            //Check for only first maxPixelComparisons pixel values
            if (count1 < maxPixelComparisons) {
              count1++;
              //                            Log.d("bitmapBuffer", bitmapBuffer[j] + " "+count1);
              if (firstPixelValue == bitmapBuffer[j]) {

                count2++;
              }
            } else {
              requiredNumberOfPixelsNotChecked = false;
              //All hundred pixels have same values
              if (count2 == maxPixelComparisons) {

                return null;
              }
            }
          }
        }
      }
    } catch (GLException e) {
      e.printStackTrace();
      return null;
    }

    return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
  }

  //For the image capture
  private interface BitmapReadyCallbacks {
    void onBitmapReady(Bitmap bitmap);
  }

  private void prepareFilterItems() {

    final List<FilterType> filtersList = FilterType.createFilterList();

    DublyFilterModel dublyFilterModel;
    for (int i = 0; i < filtersList.size(); i++) {

      dublyFilterModel = new DublyFilterModel();
      dublyFilterModel.setFilterId(filtersList.get(i));
      dublyFilterModel.setFilterName(filtersList.get(i).name());
      dublyFilterModel.setSelected(false);
      filterItems.add(dublyFilterModel);
    }
  }

  private View.OnTouchListener handleTouch = new View.OnTouchListener() {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
      handleViewTouchFeedback(view, event);

      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {

          dX = view.getX() - event.getRawX();
          dY = view.getY() - event.getRawY();
          vibrate();

          initialCaptureButtonX = view.getX();
          initialCaptureButtonY = view.getY();

          break;
        }
        case MotionEvent.ACTION_UP: {

          //Request image capture

          if (!captureImageDisabled) {
            captureImageDisabled = true;

            dialog.setMessage(getString(R.string.capturing_image));
            dialog.show();

            //                            i = 0;
            alreadyTriedTimes = 0;
            if (imageCaptureTimerTask != null) {

              imageCaptureTimerTask.cancel();
            }

            imageCaptureTimerTask = new TimerTask() {
              public void run() {

                if (alreadyTriedTimes < maxRetries) {

                  if (!bitmapCaptureAlreadyRunning) {
                    bitmapCaptureAlreadyRunning = true;
                    requestBitmapCapture();
                  }
                } else {

                  ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
                    imageCaptureTimerTask.cancel();

                    captureImageDisabled = false;
                    if (dialog != null && dialog.isShowing()) {
                      dialog.cancel();
                    }
                    Toast.makeText(context, getString(R.string.image_capture_failed),
                        Toast.LENGTH_SHORT).show();
                  });
                }

                alreadyTriedTimes++;
              }
            };
            new Timer().schedule(imageCaptureTimerTask, 0, 1000);

            view.animate()
                .x(initialCaptureButtonX)
                .y(initialCaptureButtonY)
                .setDuration(300)
                .start();

            if (currentIndexForZoomInLevel > 1) {

              canZoomOut = false;
            }
          }

          break;
        }
        case MotionEvent.ACTION_MOVE: {

          view.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();

          updateZoomLevel(view);

          break;
        }
      }

      return true;
    }
  };

  private void deselectAllFilters() {

    DublyFilterModel dublyFilterModel;
    for (int i = 0; i < filterItems.size(); i++) {

      dublyFilterModel = filterItems.get(i);
      dublyFilterModel.setSelected(false);

      filterItems.set(i, dublyFilterModel);
    }

    filterAdapter.notifyDataSetChanged();
    updatePixelComparisonLimitRequired = false;
  }

  private void selectCurrentFilter(int position) {
    DublyFilterModel dublyFilterModel;
    for (int i = 0; i < filterItems.size(); i++) {

      dublyFilterModel = filterItems.get(i);
      if (position == i) {
        dublyFilterModel.setSelected(true);
      } else {
        dublyFilterModel.setSelected(false);
      }
      filterItems.set(i, dublyFilterModel);
    }
    filterAdapter.notifyDataSetChanged();

    //        5,19,35 for the CGA_COLORSPACE, LUMINANCE_THRESHOLD and WEAK_PIXEL filters respectively

    updatePixelComparisonLimitRequired = position == 5 || position == 19 | position == 35;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private String saveCapturedBitmap(Bitmap capturedBitmap) {

    OutputStream fOutputStream;

    File file = new File(folderPath, System.currentTimeMillis() + appName + ".jpg");
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      fOutputStream = new FileOutputStream(file);

      capturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

      fOutputStream.flush();
      fOutputStream.close();

      MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(),
          file.getName(), file.getName());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return file.getAbsolutePath();
  }

  private void requestBitmapCapture() {

    captureBitmap(bitmap -> {

      if (bitmap != null) {

        new Handler().post(() -> {
          if (imageCaptureTimerTask != null) {
            imageCaptureTimerTask.cancel();
          }
          captureImageDisabled = false;
          if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
          }
          handleImageCaptured(bitmap);
        });
      }

      bitmapCaptureAlreadyRunning = false;
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    bus.unregister(this);
  }

  @Subscribe
  public void getMessage(JSONObject object) {
    try {

      if (object.getString("eventName").equals("killCameraActivity")) {
        //     super.onBackPressed();

        ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void vibrate() {
    // TODO Auto-generated method stub
    try {
      Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
      if (vibrator != null) {

        if (Build.VERSION.SDK_INT >= 26) {
          vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {

          vibrator.vibrate(200);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateZoomLevel(View view) {

    if (view.getY() < nextYCoordinateForZoomIn) {

      //Zooming in on moving up

      try {
        if (currentIndexForZoomInLevel < 10) {

          nextYCoordinateForZoomIn = lengthsForZoom.get(currentIndexForZoomInLevel + 1);

          nextYCoordinateForZoomOut = lengthsForZoom.get(currentIndexForZoomInLevel);
          currentIndexForZoomInLevel++;
          currentIndexForZoomOutLevel++;
          if (GPUCameraRecorder != null) {
            GPUCameraRecorder.zoomOut(false, false);
          }
        }
      } catch (IndexOutOfBoundsException e) {
        e.printStackTrace();
      }
      canZoomOut = true;
    } else if (view.getY() > nextYCoordinateForZoomOut) {

      //Zooming out on moving down

      if (canZoomOut) {
        try {

          if (currentIndexForZoomOutLevel > 0) {
            nextYCoordinateForZoomOut = lengthsForZoom.get(currentIndexForZoomOutLevel - 1);

            nextYCoordinateForZoomIn = lengthsForZoom.get(currentIndexForZoomOutLevel);
            currentIndexForZoomInLevel--;
            currentIndexForZoomOutLevel--;
            if (GPUCameraRecorder != null) {
              GPUCameraRecorder.zoomOut(true, false);
            }
          }
        } catch (IndexOutOfBoundsException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void setupZoomAllowedMetrics() {

    //Since margin from bottom is 7dp,so any movement lower to the initial position,should result in zoomout,if possible
    // height=66dp,margin=33dp
    //33+66=99
    //Also toolbar of height 48dp on top and tablayout of 48 dp at bottom
    // =>6+48+48+99=201
    maxLengthAvailableForZoom = screenHeight - ((201) * scale);

    //Assuming 10 levels of zoom allowed

    lengthsForZoom.add(maxLengthAvailableForZoom);
    lengthsForZoom.add(0.90f * maxLengthAvailableForZoom);
    lengthsForZoom.add(0.80f * maxLengthAvailableForZoom);
    lengthsForZoom.add(0.70f * maxLengthAvailableForZoom);
    lengthsForZoom.add(0.60f * maxLengthAvailableForZoom);
    lengthsForZoom.add(0.50f * maxLengthAvailableForZoom);
    lengthsForZoom.add(0.40f * maxLengthAvailableForZoom);
    lengthsForZoom.add(0.30f * maxLengthAvailableForZoom);
    lengthsForZoom.add(0.20f * maxLengthAvailableForZoom);
    lengthsForZoom.add(0.10f * maxLengthAvailableForZoom);

    //Dummy value
    lengthsForZoom.add(0.05f * maxLengthAvailableForZoom);

    resetCameraZoom();
  }

  private void resetCameraZoom() {

    nextYCoordinateForZoomOut = screenHeight;

    currentIndexForZoomInLevel = 1;

    currentIndexForZoomOutLevel = 0;
    //To control the sensitivity at initial press of  button,ideally should have been 0.90
    nextYCoordinateForZoomIn = 0.90f * maxLengthAvailableForZoom;
  }

  /*
   * Show window
   * */
  private void showCaptureDialogView(android.app.AlertDialog dialog, View view) {
    TranslateAnimation translate =
        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

    translate.setDuration(200);
    translate.setFillAfter(false);
    rlCameraControls.startAnimation(translate);

    dialog.show();
    dialog.setContentView(view);
    dialog.setCanceledOnTouchOutside(true);
    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
    params.width = WindowManager.LayoutParams.MATCH_PARENT;
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
    dialog.getWindow().setGravity(Gravity.BOTTOM);
    params.dimAmount = 0.0f;
    dialog.getWindow().setAttributes(params);
    dialog.getWindow()
        .setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent));
    dialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);

    rlCameraControls.setVisibility(View.INVISIBLE);
  }

  /*
   * Close window
   * */
  private void closeCaptureDialogView(android.app.AlertDialog dialog) {
    dialog.dismiss();

    TranslateAnimation translate =
        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
    translate.setDuration(300);
    translate.setFillAfter(false);
    rlCameraControls.setAnimation(translate);
    rlCameraControls.setVisibility(View.VISIBLE);
  }

  /**
   * Update filters visibility.
   */
  @OnClick(R.id.llARFilters)
  public void updateFiltersVisibility() {

    showCaptureDialogView(mFilterDialog, mFiltersView);
  }

  /**
   * Filters
   */

  private void setupFilterView() {

    mFiltersView = LayoutInflater.from(context).inflate(R.layout.dialog_filters, null);
    filtersList = mFiltersView.findViewById(R.id.rvFilters);

    mFilterDialog = new android.app.AlertDialog.Builder(context).create();
    mFilterDialog.setOnCancelListener(dialog -> closeCaptureDialogView(mFilterDialog));

    filtersList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

    filterItems = new ArrayList<>();

    prepareFilterItems();

    filterAdapter = new DublyFilterAdapter(context, filterItems,false);

    filtersList.setAdapter(filterAdapter);
    filtersList.addOnItemTouchListener(new RecyclerItemClickListener(context, filtersList,
        new RecyclerItemClickListener.OnItemClickListener() {
          @Override
          public void onItemClick(View view, final int position) {
            if (GPUCameraRecorder != null) {
              DublyFilterModel filterItem = filterItems.get(position);

              if (filterItem != null) {

                if (filterItem.isSelected()) {
                  GPUCameraRecorder.setFilter(
                      FilterType.createGlFilter(filterItems.get(0).getFilterType(),
                          context.getApplicationContext()));

                  deselectAllFilters();
                } else {
                  GPUCameraRecorder.setFilter(FilterType.createGlFilter(filterItem.getFilterType(),
                      context.getApplicationContext()));
                  selectCurrentFilter(position);
                }
              }
            }
          }

          @Override
          public void onItemLongClick(View view, final int position) {

          }
        }));
  }
}
