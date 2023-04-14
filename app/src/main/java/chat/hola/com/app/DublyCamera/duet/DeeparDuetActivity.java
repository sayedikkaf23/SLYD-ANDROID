package chat.hola.com.app.DublyCamera.duet;

import ai.deepar.ar.CameraResolutionPreset;
import ai.deepar.ar.DeepAR;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.DublyFilterAdapter;
import chat.hola.com.app.DublyCamera.DublyFilterModel;
import chat.hola.com.app.DublyCamera.FilterType;
import chat.hola.com.app.DublyCamera.QtFastStart;
import chat.hola.com.app.DublyCamera.RippleBackground;
import chat.hola.com.app.DublyCamera.SampleCameraGLView;
import chat.hola.com.app.DublyCamera.deepar.DeeparFilterAdapter;
import chat.hola.com.app.DublyCamera.deepar.ar.AREffect;
import chat.hola.com.app.DublyCamera.deepar.ar.ARHelper;
import chat.hola.com.app.DublyCamera.deepar.ar.ARInitializeListener;
import chat.hola.com.app.DublyCamera.deepar.ar.AROperations;
import chat.hola.com.app.DublyCamera.deepar.ar.CameraGrabber;
import chat.hola.com.app.DublyCamera.deepar.ar.CameraGrabberListener;
import chat.hola.com.app.DublyCamera.deepar.ar.DeeparCallbacks;
import chat.hola.com.app.DublyCamera.dubbing.CountDownDurationAdjustView;
import chat.hola.com.app.DublyCamera.dubbing.TimeDownView;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.CameraRecordListener;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.GPUCameraRecorderBuilder;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.LensFacing;
import chat.hola.com.app.DublyCamera.utilities.DownloadZipResult;
import chat.hola.com.app.DublyCamera.utilities.FiltersConfig;
import chat.hola.com.app.DublyCamera.utilities.ZipUtils;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class DeeparDuetActivity extends AppCompatActivity
    implements DeeparCallbacks, DownloadZipResult {

  private static final int CAMERA_PERMISSIONS_REQ_CODE = 0;
  private int MAXIMUM_RECORDING_LENGTH_ALLOWED = 60;

  protected chat.hola.com.app.DublyCamera.gpuv.camerarecorder.GPUCameraRecorder GPUCameraRecorder;
  protected LensFacing lensFacing = LensFacing.FRONT;

  //Binding views
  @BindView(R.id.camera)
  FrameLayout camera;
  @BindView(R.id.contentFrame)
  FrameLayout parent;

  @BindView(R.id.blackCover)
  View coverView;
  @BindView(R.id.facingButton)
  AppCompatImageView facingButton;
  @BindView(R.id.resetZoom)
  AppCompatImageView resetZoom;
  @BindView(R.id.flashButton)
  AppCompatImageView flashButton;
  @BindView(R.id.captureButton)
  AppCompatImageView captureButton;
  @BindView(R.id.cross)
  AppCompatImageView cross;
  @BindView(R.id.tick)
  AppCompatImageView tick;

  @BindView(R.id.rippleCaptureButton)
  RippleBackground rippleCaptureButton;

  private ArrayList<String> recordedVideosList = new ArrayList<>();
  private File speedChangedVideo;
  private ViewGroup flowLayout;

  private ArrayList<Integer> flowLayoutChilds = new ArrayList<>();
  private ArrayList<Integer> dubbingSoundRecordedLengths = new ArrayList<>();
  private ArrayList<Integer> recordedVideoDurations = new ArrayList<>();
  private SimpleExoPlayer player;
  private String appName;

  private boolean isFrontFace = false;
  private ProgressDialog dialog;
  private int progressBarHeight;
  private String folderPath;
  private int widthScreen;
  private boolean noPendingPermissionsRequest = true;

  private Handler delayProcessingForFileSaveHandler = new Handler();
  private boolean capturingVideo;
  private TimerTask timerTask;
  private int initialChildCount;
  private int width;
  private float videoCapturedLength;
  private float totalCapturedLength;
  private float actualDurationOfVideoCaptured;
  private int durationOfVideoCaptured;
  private long captureStartTime, captureEndTime;
  private SampleCameraGLView sampleGLView;
  private String filepath;
  private boolean toggleCamera = false;
  private boolean flashRunning;

  private Bus bus = AppController.getBus();
  private boolean lastRecordingCanceled;

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
  private View captureButtonView;

  /*
   * Countdown shooting
   * */
  @BindView(R.id.llTimer)
  LinearLayout llTimer;
  @BindView(R.id.rlCameraControls)
  RelativeLayout rlCameraControls;
  @BindView(R.id.rlTimer)
  RelativeLayout rlTimer;

  @BindView(R.id.ivResetZoom)
  AppCompatImageView ivResetZoom;

  private android.app.AlertDialog mCountDownDialog;
  private View mCountDownView;
  private CountDownDurationAdjustView mCountDownCaptureView;

  private TimeDownView mCountDownTextView;
  private long mCurrentCountDownDuration = 0;
  @BindView(R.id.rippleTimerButton)
  RippleBackground rippleTimerButton;
  @BindView(R.id.tabLayout)
  TabLayout tabLayout;
  @BindView(R.id.vTabs)
  View vTabs;

  private ArrayList<DublyFilterModel> filterItems;
  private DublyFilterAdapter filterAdapter;
  private View mFiltersView;
  private android.app.AlertDialog mFilterDialog;
  @BindView(R.id.llARFilters)
  LinearLayout llArFilters;

  @BindView(R.id.ivMute)
  AppCompatImageView ivMute;

  private boolean muted;
  private int seekTime;

  /**
   * AR camera
   */

  @BindView(R.id.tvARCamera)
  TextView tvARCamera;

  @BindView(R.id.llBeautify)
  LinearLayout llBeautify;
  @BindView(R.id.llARCamera)
  LinearLayout llARCamera;

  @BindView(R.id.ivBeautify)
  AppCompatImageView ivBeautify;
  @BindView(R.id.surface)
  SurfaceView arView;
  private boolean beautificationApplied;
  private boolean arCameraActive;

  private CameraGrabber cameraGrabber;

  private DeepAR deepAR;
  private AROperations arOperations;
  private ArrayList<AREffect> arFilterItems;
  private DeeparFilterAdapter arFilterAdapter;
  private View mArFiltersView;
  private android.app.AlertDialog mArFilterDialog;

  private int cameraDevice = Camera.CameraInfo.CAMERA_FACING_FRONT;
  private int selectedArTabPosition;
  private String selectedSlot = AROperations.SLOT_MASKS;
  private RelativeLayout rlDownloadFilters;
  private boolean downloadRequired = FiltersConfig.DOWNLOAD_REQUIRED;
  private boolean filtersDownloadedAlready;
  private AlertDialog alertDialogDownloadFilters;
  /**
   * For isometrik groupstreaming
   */
  private boolean isometrikGSWithARFiltersAdded = true;
  private boolean deepARFiltersClearedAlready = false;
  private boolean requestedToUseTimer = false;
  private int timerDurationSelected = 0;

  @SuppressLint("ClickableViewAccessibility")
  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_deepar_duet);
    ButterKnife.bind(this);

    setupDimensions();

    final File imageFolder;
    if (Environment.getExternalStorageState().

        equals(Environment.MEDIA_MOUNTED)) {
      imageFolder = new File(getExternalFilesDir(null)
          + "/"
          + getResources().getString(R.string.app_name)
          + "/Media/");
    } else {

      imageFolder = new File(
          getFilesDir().getPath() + getString(R.string.slash) + getResources().getString(
              R.string.app_name) + "/Media/");
    }

    if (!imageFolder.exists() || !imageFolder.isDirectory()) {

      imageFolder.mkdirs();
    }

    folderPath = imageFolder.getAbsolutePath();
    appName = "Demo";

    flowLayout =

        findViewById(R.id.flowlayout);

    dialog = new

        ProgressDialog(this);
    dialog.setCancelable(false);

    DisplayMetrics displayMetrics = new DisplayMetrics();

    getWindowManager().

        getDefaultDisplay().

        getMetrics(displayMetrics);

    scale = displayMetrics.density;

    widthScreen = displayMetrics.widthPixels;

    progressBarHeight = (int) (10 * scale);

    isFrontFace = true;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      parent.setFitsSystemWindows(true);
    }
    setupCameraTabs();
    initializeAREngine();
    setupTimerView();
    setupFilterView();
    setupZoomAllowedMetrics();

    rippleCaptureButton.setOnTouchListener(handleTouch);

    tick.setOnClickListener(view -> {

      if (recordedVideosList.size() > 0) {

        sendVideo();
      }
    });

    cross.setOnClickListener(view -> {

      if (flowLayout.getChildCount() > 0) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeeparDuetActivity.this);
        alertDialog.setTitle(getString(R.string.delete_segment_title));
        alertDialog.setMessage(getString(R.string.delete_segment));
        alertDialog.setPositiveButton(getString(R.string.confirm), (dialogInterface, i) -> {

          if (recordedVideosList.size() > 0) {
            //Delete the last recorded video incase more than one video has been recorded

            deleteFileFromDisk(recordedVideosList.get(recordedVideosList.size() - 1));
            recordedVideosList.remove(recordedVideosList.size() - 1);
          }
          if (flowLayout.getChildCount() > 0) {
            //Remove the views from the progress bar

            try {
              flowLayout.removeViewsInLayout(
                  flowLayout.getChildCount() - (flowLayoutChilds.get(flowLayoutChilds.size() - 1)),
                  flowLayoutChilds.get(flowLayoutChilds.size() - 1));
              flowLayoutChilds.remove(flowLayoutChilds.size() - 1);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          if (recordedVideoDurations.size() > 0) {

            recordedVideoDurations.remove(recordedVideoDurations.size() - 1);
          }

          if (dubbingSoundRecordedLengths.size() != 0) {
            dubbingSoundRecordedLengths.remove(dubbingSoundRecordedLengths.size() - 1);
          }

          if (recordedVideosList.size() == 0) {

            //When no more recorded video segments are left
            resetVideoRecordingSettings();
          } else {

            totalCapturedLength = calculateTotalRecordedVideoLength();
          }
        }).setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {

        }).show();
      }
    });

    facingButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

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

                if (lensFacing == LensFacing.FRONT) {

                  lensFacing = LensFacing.BACK;

                  changeViewImageResource((ImageView) view, R.drawable.ic_facing_front);
                } else {
                  lensFacing = LensFacing.FRONT;

                  changeViewImageResource((ImageView) view, R.drawable.ic_facing_back);
                }

                if (arCameraActive) {
                  if (cameraGrabber != null) {
                    cameraDevice =
                        cameraGrabber.getCurrCameraDevice() == Camera.CameraInfo.CAMERA_FACING_FRONT
                            ? Camera.CameraInfo.CAMERA_FACING_BACK
                            : Camera.CameraInfo.CAMERA_FACING_FRONT;
                    cameraGrabber.changeCameraDevice(cameraDevice);
                  }
                } else {
                  toggleCamera = true;
                  releaseCamera();
                }
                coverView.animate()
                    .alpha(0)
                    .setStartDelay(200)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                      @Override
                      public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        coverView.setVisibility(View.GONE);
                      }
                    })
                    .start();
              }
            })
            .start();
      }
    });

    flashButton.setOnClickListener(view -> {
      if (arCameraActive) {
        Toast.makeText(DeeparDuetActivity.this, getString(R.string.flash_not_supported),
            Toast.LENGTH_SHORT).show();
      } else {
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
      }
    });

    resetZoom.setOnClickListener(view -> {
      if (GPUCameraRecorder != null) {
        resetCameraZoom();
        GPUCameraRecorder.zoomOut(true, true);
      }
    });

    try {

      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
          WindowManager.LayoutParams.FLAG_FULLSCREEN);
    } catch (Exception e) {
      e.printStackTrace();
    }

    bus.register(this);
  }

  @Override
  protected void onResume() {
    super.onResume();

    if (noPendingPermissionsRequest) {
      validateCameraPermissions();
    } else {

      noPendingPermissionsRequest = true;
    }

    totalCapturedLength = calculateTotalRecordedVideoLength();
  }

  //For the camera capture view to be shown as fullscreen
  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    if (hasFocus) {
      hideSystemUI();
    } else {
      showSystemUI();
    }
  }

  @Override
  protected void onStop() {

    try {
      if (player != null) {

        if (isPlaying()) {
          try {
            player.setPlayWhenReady(false);
          } catch (IllegalStateException e) {
            e.printStackTrace();
          }
        }
      }

      if (timerTask != null) {
        timerTask.cancel();
      }

      if (!isFinishing() && dialog != null && dialog.isShowing()) {
        dialog.cancel();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    releaseCamera();
    super.onStop();
  }

  @Override
  public void onBackPressed() {
    if (timerStarted) {
      return;
    }

    if (flowLayout.getChildCount() > 0) {

      //No video has been recorded yet/All recorded videos has been deleted
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle(getString(R.string.tips));
      builder.setMessage(getString(R.string.discard));

      builder.setPositiveButton(getString(R.string.camera_confirm), (dialogInterface, i) -> {
        if (recordedVideosList != null && recordedVideosList.size() > 0) {

          for (int j = 0; j < recordedVideosList.size(); j++) {

            deleteFileFromDisk(recordedVideosList.get(j));
          }
        }
        supportFinishAfterTransition();
      });

      builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
      });
      builder.show();
    } else {
      deleteFileFromDisk(duetFilePath);
      super.onBackPressed();
    }
  }

  @OnClick({ R.id.back_button })
  public void back() {
    onBackPressed();
  }

  //Execute the FFmpeg command
  private void execFFmpegBinary(final String[] command, int timer,
      final String speedChangedVideoFilePath, final String capturedVideoFilePath) {

    FFmpeg.executeAsync(command, (executionId, returnCode) -> {

      if (returnCode == RETURN_CODE_SUCCESS) {
        //Async command execution completed successfully.
        deleteFileFromDisk(capturedVideoFilePath);

        recordedVideosList.add(speedChangedVideoFilePath);

        dialog.dismiss();

        //Videos has been dubbed without an audio
        if (timer >= MAXIMUM_RECORDING_LENGTH_ALLOWED) {
          sendVideo();
        }
      } else if (returnCode == RETURN_CODE_CANCEL) {
        //Async command execution cancelled by user.

      } else {
        //Async command execution failed
        deleteFileFromDisk(speedChangedVideoFilePath);
        dialog.dismiss();
      }
    });
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void handleVideoCaptured(String recordedFilepath) {

    if (new File(recordedFilepath).exists()) {

      dialog.setMessage(getString(R.string.processing_video));
      if (!isFinishing()) dialog.show();
      try {
        int totalRecordedVideoDuration = (int) totalCapturedLength;
        updateSelectDubbingSoundOption(false);

        String filepath = makeMp4FilesStreamable(recordedFilepath);

        speedChangedVideo = new File(folderPath, System.currentTimeMillis() + appName + ".mp4");

        if (!speedChangedVideo.exists()) {
          try {
            speedChangedVideo.createNewFile();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filepath);

        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time);

        String[] complexCommand = {
            "-y", "-i", filepath, "-ss", String.valueOf(seekTime / 1000), "-t",
            String.valueOf(timeInMillisec / 1000), "-i", duetFilePath, "-vsync", "2", "-crf", "29",
            "-filter_complex", "[1:v]scale="
            + videoWidth
            + ":"
            + videoHeight
            + "[v1];[0:v][v1]hstack=inputs=2[v];[0:a][1:a]amerge[a]", "-map", "[v]", "-map", "[a]",
            "-ac", "2", "-vcodec", "libx264", "-preset", "ultrafast",
            speedChangedVideo.getAbsolutePath()
        };

        execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
            speedChangedVideo.getAbsolutePath(), filepath);
      } catch (Exception e) {
        dialog.dismiss();
      }
    } else {

      //Failed to capture video
      if (flowLayout.getChildCount() > 0) {
        //Remove the views from the progress bar

        try {
          flowLayout.removeViewsInLayout(
              flowLayout.getChildCount() - (flowLayoutChilds.get(flowLayoutChilds.size() - 1)),
              flowLayoutChilds.get(flowLayoutChilds.size() - 1));
          flowLayoutChilds.remove(flowLayoutChilds.size() - 1);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      if (recordedVideoDurations.size() > 0) {

        recordedVideoDurations.remove(recordedVideoDurations.size() - 1);
      }

      if (dubbingSoundRecordedLengths.size() != 0) {
        dubbingSoundRecordedLengths.remove(dubbingSoundRecordedLengths.size() - 1);
      }

      Toast.makeText(this, R.string.video_record_failed, Toast.LENGTH_SHORT).show();
    }
  }

  private void sendVideo() {

    //If we have normal speed video then capturedVideoFile != null, if we have video recorded at different speed than 1X then speedChangedVideo != null
    //  File videoMerge;
    if (speedChangedVideo != null) {

      chat.hola.com.app.DublyCamera.ResultHolder.setCall("post");

      chat.hola.com.app.DublyCamera.ResultHolder.setType("video");

      Intent intent = new Intent(DeeparDuetActivity.this, DuetVideoPreviewActivity.class);
      intent.putExtra("videoArray", recordedVideosList);
      intent.putExtra("videoWidth", screenWidth);
      intent.putExtra("videoHeight", videoHeight);
      intent.putExtra("durationArray", recordedVideoDurations);
      intent.putExtra("maximumDuration", MAXIMUM_RECORDING_LENGTH_ALLOWED);
      intent.putExtra("isFrontFace", isFrontFace);

      startActivity(intent);
    }
  }

  private void setMicMuted(boolean state) {
    try {
      AudioManager myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      if (myAudioManager != null) {
        // get the working mode and keep it
        int workingAudioMode = myAudioManager.getMode();
        myAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

        // change mic state only if needed
        if (myAudioManager.isMicrophoneMute() != state) {
          myAudioManager.setMicrophoneMute(state);
        }

        // set back the original working mode
        myAudioManager.setMode(workingAudioMode);
      }
    } catch (SecurityException ignored) {
    }
  }

  private void checkCameraPermissions() {

    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.READ_EXTERNAL_STORAGE)
        || ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        || ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.RECORD_AUDIO)
        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

      AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    new Handler().postDelayed(this::supportFinishAfterTransition, 500);
  }

  private void requestCameraPermission() {

    ArrayList<String> permissionsRequired = new ArrayList<>();

    if (ActivityCompat.checkSelfPermission(DeeparDuetActivity.this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {

      permissionsRequired.add(Manifest.permission.CAMERA);
    }
    if (ActivityCompat.checkSelfPermission(DeeparDuetActivity.this,
        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

      permissionsRequired.add(Manifest.permission.RECORD_AUDIO);
    }
    if (ActivityCompat.checkSelfPermission(DeeparDuetActivity.this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

      permissionsRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    if (ActivityCompat.checkSelfPermission(DeeparDuetActivity.this,
        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

      permissionsRequired.add(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    noPendingPermissionsRequest = false;
    ActivityCompat.requestPermissions(this,
        permissionsRequired.toArray(new String[permissionsRequired.size()]),
        CAMERA_PERMISSIONS_REQ_CODE);
  }

  public void videoCaptureAction(boolean dubbingStarted, float durationOfVideoCaptured,
      boolean fromNonUIThread) {

    try {
      if (dubbingStarted) {

        if (dubbingSoundRecordedLengths.size() != 0) {
          seekTime = dubbingSoundRecordedLengths.get(dubbingSoundRecordedLengths.size() - 1);
          //To play audio from the location,after the last location upto which audio has been dubbed
          player.seekTo(dubbingSoundRecordedLengths.get(dubbingSoundRecordedLengths.size() - 1));
        } else {
          seekTime = 0;
          player.seekTo(0);
        }
        player.setPlayWhenReady(true);
      } else {

        //1X speed selected

        if (fromNonUIThread) {

          runOnUiThread(() -> {
            if (player != null && isPlaying()) {
              player.setPlayWhenReady(false);
            }

            int length = (int) player.getCurrentPosition();

            dubbingSoundRecordedLengths.add(length);
            //1X speed selected
            recordedVideoDurations.add((int) (durationOfVideoCaptured));
          });
        } else {
          if (player != null && isPlaying()) {
            player.setPlayWhenReady(false);
            //player.stop();
          }

          int length = (int) player.getCurrentPosition();

          dubbingSoundRecordedLengths.add(length);
          //1X speed selected
          recordedVideoDurations.add((int) (durationOfVideoCaptured));
        }
      }

      updateViewOnVideoRecordedSuccessfully(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateRecordingUI() {

    try {

      width = widthScreen / (MAXIMUM_RECORDING_LENGTH_ALLOWED);
    } catch (ArithmeticException e) {
      e.getStackTrace();
    }

    runOnUiThread(() -> {
      ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
      View view = new View(DeeparDuetActivity.this);
      view.setBackgroundColor(getResources().getColor(R.color.blue));

      flowLayout.addView(view, lp);
    });
  }

  private void validateCameraPermissions() {

    if (ActivityCompat.checkSelfPermission(DeeparDuetActivity.this, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(DeeparDuetActivity.this,
        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(DeeparDuetActivity.this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(DeeparDuetActivity.this,
        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

      try {
        setUpCamera();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      checkCameraPermissions();
    }
  }

  private void hideSystemUI() {
    View decorView = getWindow().getDecorView();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          | View.SYSTEM_UI_FLAG_FULLSCREEN);
    } else {
      // Enables regular immersive mode.
      // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
      // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
      decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          // Set the content to appear under the system bars so that the
          // content doesn't resize when the system bars hide and show.
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
          // Hide the nav bar and status bar
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
  }

  // Shows the system bars by removing all the flags
  // except for the ones that make the content appear under the system bars.
  private void showSystemUI() {
    View decorView = getWindow().getDecorView();
    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
  }

  private void updateSelectDubbingSoundOption(boolean enable) {

    if (enable) {

      setMicMuted(false);
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

  private void updateViewOnVideoRecordedSuccessfully(boolean allSegmentsDeleted) {

    if (allSegmentsDeleted) {
      flowLayout.setVisibility(View.GONE);
      tick.setVisibility(View.GONE);
      cross.setVisibility(View.GONE);
    } else {
      flowLayout.setVisibility(View.VISIBLE);
      tick.setVisibility(View.VISIBLE);
      cross.setVisibility(View.VISIBLE);
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

  public void updateProgressBar(boolean start, boolean fromNonUIThread) {

    if (start) {

      //Start video capture

      timerTask = new TimerTask() {
        public void run() {

          durationOfVideoCaptured++;

          videoCapturedLength = videoCapturedLength + actualDurationOfVideoCaptured;

          if (timerStarted) {
            //Add the last bar for the progress
            if ((durationOfVideoCaptured * actualDurationOfVideoCaptured)
                >= mCurrentCountDownDuration) {
              updateRecordingUI();

              //Have to stop the capture of the video as timer has ended
              if (capturingVideo) {
                capturingVideo = false;
                try {
                  if (timerTask != null) {
                    timerTask.cancel();
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }

                stopVideoCapture(true);
                showCameraControls(false, true);
              }
            } else {
              //Maximum length allowed of dubbing has not yet been reached

              if (capturingVideo) {

                updateRecordingUI();
              }
            }
          } else {

            if ((totalCapturedLength + videoCapturedLength) >= MAXIMUM_RECORDING_LENGTH_ALLOWED) {

              //Add the last bar for the progress

              updateRecordingUI();

              //Have to stop the capture of the video
              if (capturingVideo) {
                capturingVideo = false;
                try {
                  if (timerTask != null) {
                    timerTask.cancel();
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
                stopVideoCapture(true);
              }
            } else {
              //Maximum length allowed of recording has not yet been reached

              if (capturingVideo) {
                updateRecordingUI();
              }
            }
          }
        }
      };

      new Timer().schedule(timerTask, 1000, 1000);
      //            new Timer().schedule(timerTask, 0, 1000);
      videoCaptureAction(true, -1, fromNonUIThread);
    } else {

      //Stop video capture

      if (captureEndTime - captureStartTime > 1000) {

        //Video should have been captured for atleast one sec
        videoCaptureAction(false, durationOfVideoCaptured, fromNonUIThread);
        addRecordingStoppedViewInProgressbar();
      } else {

        if (player != null && isPlaying()) {
          player.stop();
        }
        Toast.makeText(DeeparDuetActivity.this, getString(R.string.record_small_duration),
            Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void stopVideoCapture(boolean fromNonUIThread) {

    totalCapturedLength += videoCapturedLength;
    captureEndTime = System.currentTimeMillis();

    if (captureEndTime - captureStartTime < 1000) {

      lastRecordingCanceled = true;
    }

    if (fromNonUIThread) {

      if (arCameraActive) {
        //Stop video capture from AR camera
        runOnUiThread(() -> {
          if (deepAR != null) deepAR.stopVideoRecording();
        });
      } else {
        runOnUiThread(() -> {
          if (GPUCameraRecorder != null) {
            GPUCameraRecorder.stop();
          }
        });
      }
    } else {

      if (arCameraActive) {
        //Stop video capture from AR camera
        if (deepAR != null) deepAR.stopVideoRecording();
      } else {
        if (GPUCameraRecorder != null) {
          GPUCameraRecorder.stop();
        }
      }
    }
    updateProgressBar(false, fromNonUIThread);

    durationOfVideoCaptured = 0;
    if (fromNonUIThread) {
      runOnUiThread(() -> {
        captureButton.setColorFilter(null);

        rippleCaptureButton.stopRippleAnimation();
      });
    } else {
      captureButton.setColorFilter(null);

      rippleCaptureButton.stopRippleAnimation();
    }

    resetCaptureButton(fromNonUIThread);
    timerStarted = false;
  }

  private void startVideoCapture() {
    capturingVideo = true;
    lastRecordingCanceled = false;

    //1X speed selected
    actualDurationOfVideoCaptured = 1;

    durationOfVideoCaptured = 0;
    videoCapturedLength = 0;

    initialChildCount = flowLayout.getChildCount();
    captureStartTime = System.currentTimeMillis();

    filepath = folderPath + "/" + System.currentTimeMillis() + appName + ".mp4";

    if (arCameraActive) {
      //Start video capture from AR camera
      deepAR.startVideoRecording(filepath, videoWidth, videoHeight);
    } else {

      GPUCameraRecorder.start(filepath);
    }

    captureButton.setColorFilter(
        ContextCompat.getColor(DeeparDuetActivity.this, R.color.doodle_color_red),
        android.graphics.PorterDuff.Mode.SRC_IN);

    rippleCaptureButton.startRippleAnimation();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void deleteFileFromDisk(String filePath) {
    try {
      File file = new File(filePath);

      if (file.exists()) {

        file.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void resetVideoRecordingSettings() {
    totalCapturedLength = 0f;
    flowLayoutChilds = new ArrayList<>();
    dubbingSoundRecordedLengths = new ArrayList<>();
    recordedVideoDurations = new ArrayList<>();
    flowLayout.removeViewsInLayout(0, flowLayout.getChildCount());

    updateSelectDubbingSoundOption(true);
    updateViewOnVideoRecordedSuccessfully(true);
  }

  public void addRecordingStoppedViewInProgressbar() {
    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(1, progressBarHeight);
    View view = new View(this);
    view.setBackgroundColor(getResources().getColor(R.color.whiteOverlay));

    runOnUiThread(() -> {
      flowLayout.addView(view, lp);
      flowLayoutChilds.add(flowLayout.getChildCount() - initialChildCount);
    });
  }

  private int calculateTotalRecordedVideoLength() {
    int totalRecordedVideoDuration = 0;
    for (int k = 0; k < recordedVideoDurations.size(); k++) {
      totalRecordedVideoDuration = totalRecordedVideoDuration + recordedVideoDurations.get(k);
    }

    return totalRecordedVideoDuration;
  }

  //Code for the video filters
  private void releaseCamera() {

    if (arCameraActive) {

      if (cameraGrabber != null) {

        cameraGrabber.setFrameReceiver(null);

        cameraGrabber.stopPreview();

        cameraGrabber.releaseCamera();
        cameraGrabber = null;
        //deepAR.setRenderSurface(null,0,0);
      }
    } else {
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
    }
    try {
      deselectAllFilters();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setUpCameraView() {

    runOnUiThread(() -> {

      camera.removeAllViews();
      sampleGLView = null;
      sampleGLView = new SampleCameraGLView(getApplicationContext());

      sampleGLView.setTouchListener((event, width, height) -> {
        if (GPUCameraRecorder == null) return;
        GPUCameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
      });

      camera.addView(sampleGLView);
    });
  }

  private void setUpCamera() {

    if (!arCameraActive) setUpCameraView();
    if (notFirstTime) {

      resetCameraZoom();
    } else {

      notFirstTime = true;
    }
    if (arCameraActive) {

      //arView.setVisibility(View.GONE);
      //arView.setVisibility(View.VISIBLE);
      flashButton.setVisibility(View.GONE);
      cameraGrabber = new CameraGrabber(cameraDevice);

      switch (ARHelper.getScreenOrientation(getWindowManager())) {
        case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
          cameraGrabber.setScreenOrientation(90);
          break;
        case ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
          cameraGrabber.setScreenOrientation(270);
          break;
        default:
          cameraGrabber.setScreenOrientation(0);
          break;
      }

      cameraGrabber.setResolutionPreset(CameraResolutionPreset.P1280x720);

      final Activity context = this;
      cameraGrabber.initCamera(new CameraGrabberListener() {
        @Override
        public void onCameraInitialized() {
          cameraGrabber.setFrameReceiver(deepAR);
          cameraGrabber.startPreview();
        }

        @Override
        public void onCameraError(String errorMsg) {
          AlertDialog.Builder builder = new AlertDialog.Builder(context);
          builder.setTitle("Camera error");
          builder.setMessage(errorMsg);
          builder.setCancelable(true);
          builder.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.cancel());
          AlertDialog dialog = builder.create();
          dialog.show();
        }
      });
    } else {

      GPUCameraRecorder = new GPUCameraRecorderBuilder(this, sampleGLView)

          .cameraRecordListener(new CameraRecordListener() {
            @Override
            public void onGetFlashSupport(boolean flashSupport) {
              runOnUiThread(() -> {

                flashRunning = false;

                changeViewImageResource(flashButton, R.drawable.ic_flash_on);

                if (flashSupport) {
                  flashButton.setVisibility(View.VISIBLE);
                } else {

                  flashButton.setVisibility(View.GONE);
                }
              });
            }

            @Override
            public void onRecordComplete() {

              if (!lastRecordingCanceled) {

                dialog.setMessage(getString(R.string.saving_video));
                if (!isFinishing()) dialog.show();

                //For the issue fo file getting saved asynchronously on external storage and taking around 200ms for saving
                delayProcessingForFileSaveHandler.postDelayed(() -> {
                  if (!isFinishing() && dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                  }
                  handleVideoCaptured(filepath);
                }, 1000);
              } else {

                if (recordedVideosList.size() == 0) {
                  //For the case when even the first recorded video is failed
                  updateViewOnVideoRecordedSuccessfully(true);
                }

                lastRecordingCanceled = false;
              }
            }

            @Override
            public void onRecordStart() {

              if (capturingVideo) {
                updateProgressBar(true, true);
              }
            }

            @Override
            public void onError(Exception e) {
              e.printStackTrace();
            }

            @Override
            public void onCameraThreadFinish() {

              if (toggleCamera) {

                runOnUiThread(() -> {
                  setUpCamera();
                });
              }
              toggleCamera = false;
            }
          })
          .videoSize(videoWidth, videoHeight)
          .cameraSize(cameraWidth, cameraHeight)
          .lensFacing(lensFacing)
          .build();
    }
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
          captureButtonView = view;
          dX = view.getX() - event.getRawX();
          dY = view.getY() - event.getRawY();
          initialCaptureButtonX = view.getX();
          initialCaptureButtonY = view.getY();
          vibrate();

          if (totalCapturedLength < MAXIMUM_RECORDING_LENGTH_ALLOWED) {

            if ((!arCameraActive && GPUCameraRecorder != null) || (arCameraActive
                && deepAR != null)) {
              if (requestedToUseTimer) {
                startCountDownTimer();
              } else {
                startVideoCapture();
              }
            } else {

              Toast.makeText(DeeparDuetActivity.this, getString(R.string.preparing_camera),
                  Toast.LENGTH_SHORT).show();
            }
          } else {

            Toast.makeText(DeeparDuetActivity.this, R.string.recording_max_size, Toast.LENGTH_SHORT)
                .show();
          }

          break;
        }
        case MotionEvent.ACTION_UP: {

          if (!requestedToUseTimer && capturingVideo) {

            //Stop video capture
            capturingVideo = false;

            try {
              if (timerTask != null) {
                timerTask.cancel();
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
            stopVideoCapture(false);
          }

          break;
        }
        case MotionEvent.ACTION_MOVE: {

          if (capturingVideo) {

            view.animate()

                .x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();

            updateZoomLevel(view);
          }
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
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private String makeMp4FilesStreamable(String inputFilePath) {
    String path = inputFilePath;
    File input = new File(inputFilePath); // Your input file

    File output =
        new File(folderPath, System.currentTimeMillis() + appName + ".mp4"); // Your output file
    try {
      if (!output.exists()) {// if there is no output file we'll create one
        output.createNewFile();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      boolean ret = QtFastStart.fastStart(input, output);

      if (ret) {
        deleteFileFromDisk(inputFilePath);

        path = output.getAbsolutePath();
      }
    } catch (QtFastStart.MalformedFileException | QtFastStart.UnsupportedFileException | IOException e1) {
      e1.printStackTrace();
    }

    return path;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    try {
      if (player != null) {

        if (isPlaying()) {
          try {
            player.stop();
          } catch (IllegalStateException e) {
            e.printStackTrace();
          }
        }
        player.release();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    bus.unregister(this);
    if (isometrikGSWithARFiltersAdded) {
      if (deepAR == null) {
        return;
      }
      deepAR.setAREventListener(null);
      deepAR.release();
      deepAR = null;
    }
  }

  @Subscribe
  public void getMessage(JSONObject object) {
    try {
      if (object.getString("eventName").equals("killCameraActivity")) {

        supportFinishAfterTransition();
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void vibrate() {
    // TODO Auto-generated method stub
    try {
      Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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
      } catch (IndexOutOfBoundsException | NullPointerException e) {
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
        } catch (IndexOutOfBoundsException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void setupZoomAllowedMetrics() {

    //Since margin from bottom is 18dp,so any movement lower to the initial position,should result in zoomout,if possible
    // height=66dp,margin=33dp
    //33+66+17=116
    maxLengthAvailableForZoom = screenHeight - (116 * scale);

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

  private void resetCaptureButton(boolean fromNonUiThread) {
    if (fromNonUiThread) {

      runOnUiThread(() -> {
        if (captureButtonView != null) {

          captureButtonView.animate()
              .x(initialCaptureButtonX)
              .y(initialCaptureButtonY)
              .setDuration(300)
              .start();
        }
      });
    } else {
      if (captureButtonView != null) {

        captureButtonView.animate()
            .x(initialCaptureButtonX)
            .y(initialCaptureButtonY)
            .setDuration(300)
            .start();
      }
    }

    if (currentIndexForZoomInLevel > 1) {

      canZoomOut = false;
    }
  }

  //@Override
  //public void showAlert(String message) {
  //
  //}
  //
  //@Override
  //public void onError(String message) {
  //
  //}

  /**
   * Countdown timer code
   */

  private boolean timerStarted = false;

  private void setCountDownButtonEnable(boolean enable) {
    llTimer.setEnabled(enable);
    llTimer.setAlpha(enable ? 1.0f : 0.3f);
  }

  @OnClick(R.id.llTimer)
  public void timerClicked() {
    if (timerStarted) {
      return;
    }

    showCaptureDialogView(mCountDownDialog, mCountDownView, true);
    initCountDownDurationAdjustView(-1);
  }

  private void setupTimerView() {

    llTimer.setVisibility(View.VISIBLE);

    mCountDownView = LayoutInflater.from(this).inflate(R.layout.countdown_view, null);
    mCountDownCaptureView = mCountDownView.findViewById(R.id.countDownCaptureView);
    Button mStartCountDownCapture = mCountDownView.findViewById(R.id.btStartCountDownCapture);
    mCountDownTextView = findViewById(R.id.tvCountDown);
    initCountDownDialog();
    ivResetZoom.setOnClickListener(view -> {
      if (GPUCameraRecorder != null) {
        resetCameraZoom();
        GPUCameraRecorder.zoomOut(true, true);
      }
    });

    rippleTimerButton.setOnClickListener(v -> {
      //Have to stop the capture of the video
      if (timerStarted && capturingVideo) {
        capturingVideo = false;
        try {
          if (timerTask != null) {
            timerTask.cancel();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        stopVideoCapture(false);
        showCameraControls(false, false);
      }
    });

    mCountDownCaptureView.setNewDurationChangeListener(
        (newDuration, isDragEnd) -> mCurrentCountDownDuration = newDuration);

    mStartCountDownCapture.setOnClickListener(v -> {
      closeCaptureDialogView(mCountDownDialog, false);

      if (totalCapturedLength < MAXIMUM_RECORDING_LENGTH_ALLOWED) {
        vTabs.setVisibility(View.VISIBLE);
        mCountDownTextView.setVisibility(View.VISIBLE);
        timerStarted = true;
        mCountDownTextView.downSecond(2);
      } else {

        Toast.makeText(DeeparDuetActivity.this, R.string.recording_max_size, Toast.LENGTH_SHORT)
            .show();
      }
    });

    mCountDownTextView.setOnTimeDownListener(new TimeDownView.DownTimeWatcher() {
      @Override
      public void onTime(int num) {

      }

      @Override
      public void onLastTime(int num) {

      }

      @Override
      public void onLastTimeFinish(int num) {
        mCountDownTextView.setVisibility(View.GONE);
        mCountDownTextView.closeDefaultAnimate();

        startVideoCapture();
        showCameraControls(true, false);
      }
    });
  }

  private void initCountDownDialog() {
    mCountDownDialog = new android.app.AlertDialog.Builder(this).create();
    mCountDownDialog.setOnCancelListener(dialog -> closeCaptureDialogView(mCountDownDialog, true));
  }

  /*
   * Show window
   * */
  private void showCaptureDialogView(android.app.AlertDialog dialog, View view, boolean show) {
    TranslateAnimation translate =
        new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

    translate.setDuration(200);
    translate.setFillAfter(false);
    rlCameraControls.startAnimation(translate);
    if (!isFinishing() && show) {
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
          .setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.transparent));
      dialog.getWindow().setWindowAnimations(R.style.fx_dlg_style);
    }
    rlCameraControls.setVisibility(View.INVISIBLE);
  }

  /*
   * Close window
   * */
  private void closeCaptureDialogView(android.app.AlertDialog dialog, boolean visible) {
    dialog.dismiss();

    if (visible) {
      TranslateAnimation translate =
          new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
              Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
      translate.setDuration(300);
      translate.setFillAfter(false);
      rlCameraControls.setAnimation(translate);
      rlCameraControls.setVisibility(View.VISIBLE);
    }
  }

  private void initCountDownDurationAdjustView(int captureDurationRequested) {

    int MAX_RECORD_DURATION = MAXIMUM_RECORDING_LENGTH_ALLOWED;
    if (totalCapturedLength >= MAX_RECORD_DURATION) totalCapturedLength = MAX_RECORD_DURATION;
    mCountDownCaptureView.setMaxCaptureDuration(MAX_RECORD_DURATION);
    mCountDownCaptureView.setCurrentCaptureDuration((int) totalCapturedLength);

    mCountDownCaptureView.resetHandleViewPosition(
        getString(R.string.max_duration, String.valueOf(MAX_RECORD_DURATION)));
    int newMaxCaptureDuration = MAX_RECORD_DURATION - (int) totalCapturedLength;
    if (captureDurationRequested != -1) {
      if (newMaxCaptureDuration > captureDurationRequested) {
        newMaxCaptureDuration = captureDurationRequested;
      }
    }
    if (newMaxCaptureDuration <= 0) newMaxCaptureDuration = 0;
    mCurrentCountDownDuration = newMaxCaptureDuration;

    mCountDownCaptureView.setNewCaptureDuration(newMaxCaptureDuration);
  }

  private void showCameraControls(boolean visible, boolean fromNonUiThread) {
    if (visible) {
      rlTimer.setVisibility(View.VISIBLE);
      rippleTimerButton.startRippleAnimation();
    } else {
      if (fromNonUiThread) {
        runOnUiThread(() -> {
          rlTimer.setVisibility(View.GONE);
          rippleTimerButton.stopRippleAnimation();
          rlCameraControls.setVisibility(View.VISIBLE);
          vTabs.setVisibility(View.GONE);

          if (selectedTabPosition != 0) {
            tabLayout.setScrollPosition(0, 0f, true);
            llTimer.setVisibility(View.VISIBLE);
            llArFilters.setVisibility(View.VISIBLE);
            llARCamera.setVisibility(View.VISIBLE);
            if (arCameraActive) {

              llBeautify.setVisibility(View.VISIBLE);
              resetZoom.setVisibility(View.GONE);
            } else {
              resetZoom.setVisibility(View.VISIBLE);
            }
            requestedToUseTimer = false;
          }
        });
      } else {
        rlTimer.setVisibility(View.GONE);
        rippleTimerButton.stopRippleAnimation();
        rlCameraControls.setVisibility(View.VISIBLE);
        vTabs.setVisibility(View.GONE);

        if (selectedTabPosition != 0) {
          tabLayout.setScrollPosition(0, 0f, true);
          llTimer.setVisibility(View.VISIBLE);
          llArFilters.setVisibility(View.VISIBLE);
          llARCamera.setVisibility(View.VISIBLE);
          if (arCameraActive) {

            llBeautify.setVisibility(View.VISIBLE);
            resetZoom.setVisibility(View.GONE);
          } else {
            resetZoom.setVisibility(View.VISIBLE);
          }
          requestedToUseTimer = false;
        }
      }
    }
  }

  @OnClick({ R.id.ivZoomOut })
  public void zoomOut() {

    try {

      if (GPUCameraRecorder != null) {
        GPUCameraRecorder.zoomOut(true, false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnClick({ R.id.ivZoomIn })
  public void zoomIn() {

    try {

      if (GPUCameraRecorder != null) {
        GPUCameraRecorder.zoomOut(false, false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private int selectedTabPosition;

  private void setupCameraTabs() {

    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.video)));
    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fifteen_sec)));
    tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sixty_sec)));

    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        selectedTabPosition = tabLayout.getSelectedTabPosition();

        handleTabSelectEvent();
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
        handleTabSelectEvent();
      }
    });

    //try {
    //
    //  //Keep the video tab selected by default
    //
    //  tabLayout.getTabAt(0).select();
    //} catch (NullPointerException e) {
    //  e.printStackTrace();
    //}
  }

  private void handleTabSelectEvent() {
    switch (selectedTabPosition) {

      case 0: {
        //Video selected
        llTimer.setVisibility(View.VISIBLE);
        llArFilters.setVisibility(View.VISIBLE);
        llARCamera.setVisibility(View.VISIBLE);
        if (arCameraActive) {

          llBeautify.setVisibility(View.VISIBLE);
          resetZoom.setVisibility(View.GONE);
        } else {
          resetZoom.setVisibility(View.VISIBLE);
        }
        requestedToUseTimer = false;
        break;
      }
      case 1: {
        //15s
        llTimer.setVisibility(View.GONE);
        llArFilters.setVisibility(View.VISIBLE);
        llARCamera.setVisibility(View.VISIBLE);
        if (arCameraActive) {

          llBeautify.setVisibility(View.VISIBLE);
          resetZoom.setVisibility(View.GONE);
        } else {
          resetZoom.setVisibility(View.VISIBLE);
        }
        requestedToUseTimer = true;
        timerDurationSelected = 15;
        break;
      }
      case 2: {
        //60s
        llTimer.setVisibility(View.GONE);
        llArFilters.setVisibility(View.VISIBLE);
        llARCamera.setVisibility(View.VISIBLE);
        if (arCameraActive) {

          llBeautify.setVisibility(View.VISIBLE);
          resetZoom.setVisibility(View.GONE);
        } else {
          resetZoom.setVisibility(View.VISIBLE);
        }
        requestedToUseTimer = true;
        timerDurationSelected = 60;
        break;
      }
    }
  }

  private String checkVideoCanBeCaptured() {
    if (totalCapturedLength < MAXIMUM_RECORDING_LENGTH_ALLOWED) {

      return null;
    } else {

      return getString(R.string.recording_max_size);
    }
  }

  //////////////////////AR filters

  /**
   * Switch between normal and ar camera.
   */
  @OnClick(R.id.llARCamera)
  public void switchARCamera() {

    coverView.setAlpha(0);
    coverView.setVisibility(View.VISIBLE);
    releaseCamera();
    coverView.animate()
        .alpha(1)
        .setStartDelay(0)
        .setDuration(300)
        .setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            arCameraActive = !arCameraActive;

            if (arCameraActive) {
              resetZoom.setVisibility(View.GONE);
              arView.setVisibility(View.VISIBLE);
              llBeautify.setVisibility(View.VISIBLE);
              tvARCamera.setText(getString(R.string.normal_camera));
            } else {
              if (selectedTabPosition != 1) {
                resetZoom.setVisibility(View.VISIBLE);
              }
              arView.setVisibility(View.GONE);
              llBeautify.setVisibility(View.GONE);
              tvARCamera.setText(getString(R.string.ar_camera));
            }
            setUpCamera();

            coverView.animate()
                .alpha(0)
                .setStartDelay(200)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                  @Override
                  public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    coverView.setVisibility(View.GONE);
                  }
                })
                .start();
          }
        })
        .start();
  }

  /**
   * Apply beautify options.
   */
  @OnClick(R.id.llBeautify)
  public void beautify() {

    if (downloadRequired) {
      if (filtersDownloadedAlready) {

        beautificationApplied = !beautificationApplied;
        ivBeautify.setSelected(beautificationApplied);
        applyBeautifyOptions(beautificationApplied);
      } else {
        Toast.makeText(this, R.string.beauty_download, Toast.LENGTH_SHORT).show();
      }
    } else {

      beautificationApplied = !beautificationApplied;
      ivBeautify.setSelected(beautificationApplied);
      applyBeautifyOptions(beautificationApplied);
    }
  }

  /**
   * Update filters visibility.
   */
  @OnClick(R.id.llARFilters)
  public void updateFiltersVisibility() {

    if (arCameraActive) {

      showCaptureDialogView(mArFilterDialog, mArFiltersView, true);
    } else {

      showCaptureDialogView(mFilterDialog, mFiltersView, true);
    }
  }

  @Override
  public void arVideoRecordingStarted() {

    if (capturingVideo) {
      updateProgressBar(true, true);
    }
  }

  @Override
  public void arVideoRecordingFinished() {
    if (!lastRecordingCanceled) {

      dialog.setMessage(getString(R.string.saving_video));
      if (!isFinishing()) dialog.show();

      //For the issue fo file getting saved asynchronously on external storage and taking around 200ms for saving
      delayProcessingForFileSaveHandler.postDelayed(() -> {
        if (!isFinishing() && dialog != null && dialog.isShowing()) {
          dialog.cancel();
        }
        handleVideoCaptured(filepath);
      }, 1000);
      //handleVideoCaptured(filepath);
    } else {

      if (recordedVideosList.size() == 0) {
        //For the case when even the first recorded video is failed
        updateViewOnVideoRecordedSuccessfully(true);
      }

      lastRecordingCanceled = false;
    }
  }

  @Override
  public void arVideoRecordingFailed() {
    Toast.makeText(DeeparDuetActivity.this, getString(R.string.video_record_failed),
        Toast.LENGTH_SHORT).show();
  }

  @Override
  public void arVideoRecordingPrepared() {

  }

  @Override
  public void arImageCaptured(Bitmap bitmap) {

  }

  private void initializeAREngine() {

    try {
      if (isometrikGSWithARFiltersAdded) {
//        deepAR = IsometrikUiSdk.getInstance().getIsometrik().getAREngine();
      } else {
        deepAR = new DeepAR(this);
        deepAR.setLicenseKey(BuildConfig.DEEPAR_KEY);
      }
      deepAR.initialize(this, new ARInitializeListener(this));
      arOperations = new AROperations(deepAR);

      arView.setOnClickListener(view -> deepAR.onClick());

      arView.getHolder().addCallback(new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

          if (deepAR != null) {

            deepAR.setRenderSurface(holder.getSurface(), width, height);

            /**
             * /*Bug Title- The effects selected for previous recording are being displayed
             * /*Bug Id-DUBAND021
             * /*Fix Description-Clear filters and beauty effect on ar camera becoming active for first time
             * /*Developer Name-Ashutosh
             * /*Fix Date-6/4/21
             **/
            if (!deepARFiltersClearedAlready) {
              if (downloadRequired) {
                if (AppController.getInstance()
                    .getSharedPreferences()
                    .getBoolean("deeparFiltersDownloaded", false)) {
                  try {
                    clearFilters();
                    applyBeautifyOptions(false);
                    deepARFiltersClearedAlready = true;
                  } catch (Exception ignore) {
                  }
                } else {
                  deepARFiltersClearedAlready = true;
                }
              } else {
                try {
                  clearFilters();
                  applyBeautifyOptions(false);
                  deepARFiltersClearedAlready = true;
                } catch (Exception ignore) {
                }
              }
            }
          }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

          if (deepAR != null) {

            deepAR.setRenderSurface(null, 0, 0);
          }
        }
      });
      // Surface might already be initialized, so we force the call to onSurfaceChanged
      arView.setVisibility(View.VISIBLE);
      arView.setVisibility(View.GONE);
    } catch (Exception e) {

      e.printStackTrace();
    }
  }

  /**
   * Filters
   */

  private void setupFilterView() {

    mFiltersView = LayoutInflater.from(this).inflate(R.layout.dialog_filters, null);
    RecyclerView filtersList = mFiltersView.findViewById(R.id.rvFilters);

    mFilterDialog = new android.app.AlertDialog.Builder(this).create();
    mFilterDialog.setOnCancelListener(dialog -> closeCaptureDialogView(mFilterDialog, true));

    filtersList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

    filterItems = new ArrayList<>();

    prepareFilterItems();

    filterAdapter = new DublyFilterAdapter(this, filterItems, false);

    filtersList.setAdapter(filterAdapter);
    filtersList.addOnItemTouchListener(
        new RecyclerItemClickListener(DeeparDuetActivity.this, filtersList,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {
                if (GPUCameraRecorder != null) {
                  DublyFilterModel filterItem = filterItems.get(position);

                  if (filterItem != null) {

                    if (filterItem.isSelected()) {
                      GPUCameraRecorder.setFilter(
                          FilterType.createGlFilter(filterItems.get(0).getFilterType(),
                              getApplicationContext()));

                      deselectAllFilters();
                    } else {
                      GPUCameraRecorder.setFilter(
                          FilterType.createGlFilter(filterItem.getFilterType(),
                              getApplicationContext()));
                      selectCurrentFilter(position);
                    }
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, final int position) {

              }
            }));

    ////AR filters-

    mArFiltersView = LayoutInflater.from(this).inflate(R.layout.dialog_deepar_filters, null);
    RecyclerView arFiltersList = mArFiltersView.findViewById(R.id.rvFilters);

    TabLayout tabLayoutFilters = mArFiltersView.findViewById(R.id.tabLayoutFilters);
    tabLayoutFilters.addTab(tabLayoutFilters.newTab().setText(getString(R.string.ism_masks)));
    tabLayoutFilters.addTab(tabLayoutFilters.newTab().setText(getString(R.string.ism_effects)));
    tabLayoutFilters.addTab(tabLayoutFilters.newTab().setText(getString(R.string.ism_filters)));

    mArFilterDialog = new android.app.AlertDialog.Builder(this).create();
    mArFilterDialog.setOnCancelListener(dialog -> closeCaptureDialogView(mArFilterDialog, true));

    rlDownloadFilters = mArFiltersView.findViewById(R.id.rlDownload);

    arFiltersList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

    arFilterItems = new ArrayList<>();

    arFilterItems.addAll(arOperations.getMasks());

    arFilterAdapter = new DeeparFilterAdapter(this, arFilterItems);

    arFiltersList.setAdapter(arFilterAdapter);
    filtersDownloadedAlready = AppController.getInstance()
        .getSharedPreferences()
        .getBoolean("deeparFiltersDownloaded", false);

    if (downloadRequired) {
      if (filtersDownloadedAlready) {
        rlDownloadFilters.setVisibility(View.GONE);
      } else {
        rlDownloadFilters.setVisibility(View.VISIBLE);
      }

      rlDownloadFilters.setOnClickListener(v -> {

        closeCaptureDialogView(mArFilterDialog, true);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DeeparDuetActivity.this);
        @SuppressLint("InflateParams")
        final View vDownloadFilters = (DeeparDuetActivity.this).getLayoutInflater()
            .inflate(R.layout.dialog_download_filters, null);

        TextView tvDownloadFilters = vDownloadFilters.findViewById(R.id.tvDownload);
        TextView tvSize = vDownloadFilters.findViewById(R.id.tvSize);

        TextView tvCancel = vDownloadFilters.findViewById(R.id.tvCancel);
        tvSize.setText(FiltersConfig.DEEPAR_SIZE);

        tvDownloadFilters.setEnabled(true);
        tvCancel.setVisibility(View.VISIBLE);
        tvDownloadFilters.setOnClickListener(view -> {
          tvDownloadFilters.setEnabled(false);
          tvCancel.setVisibility(View.GONE);
          ZipUtils.downloadEffects(1, DeeparDuetActivity.this,
              vDownloadFilters.findViewById(R.id.pbDownload));
        });

        alertDialog.setView(vDownloadFilters);
        alertDialog.setCancelable(false);
        alertDialogDownloadFilters = alertDialog.create();
        if (!isFinishing()) alertDialogDownloadFilters.show();
        tvCancel.setOnClickListener(view -> alertDialogDownloadFilters.dismiss());
      });
    } else {
      rlDownloadFilters.setVisibility(View.GONE);
    }

    TextView tvClearArFilters = mArFiltersView.findViewById(R.id.tvClearArFilters);
    tvClearArFilters.setOnClickListener(v -> {
      if (downloadRequired) {
        if (filtersDownloadedAlready) {
          clearAllArFilters();
        } else {
          Toast.makeText(DeeparDuetActivity.this, getString(R.string.filters_download_required),
              Toast.LENGTH_SHORT).show();
        }
      } else {
        clearAllArFilters();
      }
    });
    arFiltersList.addOnItemTouchListener(
        new RecyclerItemClickListener(DeeparDuetActivity.this, arFiltersList,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, final int position) {
                if (deepAR != null) {
                  AREffect filterItem = arFilterItems.get(position);

                  if (filterItem != null) {
                    if (downloadRequired) {

                      if (filtersDownloadedAlready) {
                        if (filterItem.isSelected()) {

                          deselectCurrentArFilter(position);
                        } else {

                          selectCurrentArFilter(position);
                        }
                      } else {
                        Toast.makeText(DeeparDuetActivity.this,
                            getString(R.string.filters_download_required), Toast.LENGTH_SHORT)
                            .show();
                      }
                    } else {
                      if (filterItem.isSelected()) {

                        deselectCurrentArFilter(position);
                      } else {

                        selectCurrentArFilter(position);
                      }
                    }
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, final int position) {

              }
            }));
    tabLayoutFilters.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        selectedArTabPosition = tabLayoutFilters.getSelectedTabPosition();

        switch (selectedArTabPosition) {

          case 0: {
            //Masks selected
            selectedSlot = AROperations.SLOT_MASKS;
            arFilterItems.clear();
            arFilterItems.addAll(arOperations.getMasks());
            break;
          }
          case 1: {
            //Effects selected
            selectedSlot = AROperations.SLOT_EFFECTS;
            arFilterItems.clear();
            arFilterItems.addAll(arOperations.getEffects());
            break;
          }
          case 2: {
            //Filters selected
            selectedSlot = AROperations.SLOT_FILTERS;
            arFilterItems.clear();
            arFilterItems.addAll(arOperations.getFilters());
            break;
          }
        }
        arFilterAdapter.notifyDataSetChanged();
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {
      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {
      }
    });
  }

  /**
   * To allow filters download on the fly
   */
  @Override
  public void downloadResult(String result, String filePath) {
    if (!isFinishing()
        && alertDialogDownloadFilters != null
        && alertDialogDownloadFilters.isShowing()) {
      try {
        alertDialogDownloadFilters.dismiss();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (filePath != null) {

      dialog.setMessage(getString(R.string.preparing_filters));
      if (!isFinishing()) {
        runOnUiThread(() -> dialog.show());
      }
      ZipUtils.extractZip(filePath, 1, this);
    } else {
      runOnUiThread(
          () -> Toast.makeText(DeeparDuetActivity.this, result, Toast.LENGTH_SHORT).show());
    }
  }

  @Override
  public void zipExtractResult(String result) {
    if (!isFinishing() && dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }

    if (result == null) {
      filtersDownloadedAlready = true;

      AppController.getInstance()
          .getSharedPreferences()
          .edit()
          .putBoolean("deeparFiltersDownloaded", true)
          .apply();
      runOnUiThread(() -> {
        rlDownloadFilters.setVisibility(View.GONE);
        Toast.makeText(DeeparDuetActivity.this, R.string.filters_prepared, Toast.LENGTH_SHORT)
            .show();
      });
    } else {
      runOnUiThread(() -> Toast.makeText(DeeparDuetActivity.this, R.string.filters_prepare_failed,
          Toast.LENGTH_SHORT).show());
    }
  }

  private void deselectCurrentArFilter(int position) {

    applyFilter(selectedSlot, "none");

    AREffect arEffect = arFilterItems.get(position);
    arEffect.setSelected(false);

    arFilterItems.set(position, arEffect);
    arFilterAdapter.notifyItemChanged(position);

    updateArFiltersSelectedStatus();
  }

  private void selectCurrentArFilter(int position) {
    applyFilter(selectedSlot, arFilterItems.get(position).getPath());

    AREffect arEffect;
    for (int i = 0; i < arFilterItems.size(); i++) {

      arEffect = arFilterItems.get(i);
      if (position == i) {
        arEffect.setSelected(true);
      } else {
        arEffect.setSelected(false);
      }
      arFilterItems.set(i, arEffect);
    }
    arFilterAdapter.notifyDataSetChanged();
    updateArFiltersSelectedStatus();
  }

  private void updateArFiltersSelectedStatus() {
    switch (selectedArTabPosition) {
      case 0:
        //Masks selected
        arOperations.setMasks(arFilterItems);
        break;
      case 1:
        //Effects selected
        arOperations.setEffects(arFilterItems);
        break;
      case 2:
        //Filters selected
        arOperations.setFilters(arFilterItems);
        break;
    }
  }

  private void clearAllArFilters() {
    clearFilters();

    AREffect arEffect;
    for (int i = 0; i < arFilterItems.size(); i++) {

      arEffect = arFilterItems.get(i);
      arEffect.setSelected(false);

      arFilterItems.set(i, arEffect);
    }

    arFilterAdapter.notifyDataSetChanged();
    updateArFiltersSelectedStatus();
    ArrayList<AREffect> arEffects;
    ArrayList<AREffect> values;
    for (int i = 0; i < 3; i++) {
      if (i != selectedArTabPosition) {

        switch (i) {
          case 0:
            arEffects = arOperations.getMasks();
            values = new ArrayList<>();

            for (int j = 0; j < arEffects.size(); j++) {

              arEffect = arEffects.get(j);
              arEffect.setSelected(false);

              values.add(arEffect);
            }
            arOperations.setMasks(values);
            break;
          case 1:
            arEffects = arOperations.getEffects();
            values = new ArrayList<>();

            for (int j = 0; j < arEffects.size(); j++) {

              arEffect = arEffects.get(j);
              arEffect.setSelected(false);

              values.add(arEffect);
            }
            arOperations.setEffects(values);
            break;
          case 2:
            arEffects = arOperations.getFilters();
            values = new ArrayList<>();

            for (int j = 0; j < arEffects.size(); j++) {

              arEffect = arEffects.get(j);
              arEffect.setSelected(false);

              values.add(arEffect);
            }
            arOperations.setFilters(values);
            break;
        }
      }
    }
  }

  public void applyFilter(String slot, String path) {
    arOperations.applyFilter(slot, path);
  }

  public void clearFilters() {

    arOperations.clearAllFilters();
  }

  public void applyBeautifyOptions(boolean beautificationApplied) {
    arOperations.applyBeautifyOptions(beautificationApplied);
  }

  /**
   * For duet
   */

  private DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
  private int screenWidth = displayMetrics.widthPixels;
  private int screenHeight = displayMetrics.heightPixels;
  protected int cameraWidth = screenWidth / 2;
  protected int videoWidth = screenWidth / 2;
  protected int cameraHeight;
  protected int videoHeight;
  private File duetFile;
  private String duetFilePath;
  @BindView(R.id.exoPlayer)
  SimpleExoPlayerView exoPlayer;

  /**
   * Update filters visibility.
   */
  @OnClick(R.id.ivMute)
  public void mute() {
    ivMute.setSelected(muted);
    muted = !muted;
    setMicMuted(muted);
  }

  private void setupDimensions() {

    duetFilePath = getIntent().getStringExtra("duetFilePath");
    duetFile = new File(duetFilePath);

    if(duetFilePath!=null)tabLayout.setVisibility(View.GONE);

    getDuration(getIntent().getStringExtra("remoteUrl"));

    ViewGroup.LayoutParams params = camera.getLayoutParams();
    params.height = cameraHeight;
    camera.setLayoutParams(params);
    camera.requestLayout();

    params = coverView.getLayoutParams();
    params.height = cameraHeight;
    coverView.setLayoutParams(params);
    coverView.requestLayout();

    params = exoPlayer.getLayoutParams();
    params.height = cameraHeight;
    exoPlayer.setLayoutParams(params);
    exoPlayer.requestLayout();

    params = arView.getLayoutParams();
    params.height = cameraHeight;
    arView.setLayoutParams(params);
    arView.requestLayout();

    player = ExoPlayerFactory.newSimpleInstance(this);
    exoPlayer.setPlayer(player);
    exoPlayer.setUseController(false);
    buildMediaSource(Uri.parse(duetFilePath));
    ivMute.setSelected(true);
  }

  private void getDuration(String remoteUrl) {

    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    retriever.setDataSource(this, Uri.fromFile(duetFile));

    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    long timeInMillisec = Long.parseLong(time);
    if (timeInMillisec == 0) {
      /*
       * Cause for downloaded media, duration was not updated
       */
      retriever = new MediaMetadataRetriever();
      retriever.setDataSource(remoteUrl, new HashMap<>());

      time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

      timeInMillisec = Long.parseLong(time);
    }
    MAXIMUM_RECORDING_LENGTH_ALLOWED = (int) (timeInMillisec / 1000);

    String metaRotation = retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);
    int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
    int duetVideoWidth, duetVideoHeight;
    if (rotation == 90 || rotation == 270) {

      duetVideoWidth = Integer.parseInt(
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
      duetVideoHeight = Integer.parseInt(
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
    } else {

      duetVideoHeight = Integer.parseInt(
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
      duetVideoWidth = Integer.parseInt(
          retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
    }

    cameraHeight = ((duetVideoHeight * cameraWidth) / duetVideoWidth);
    videoHeight = cameraHeight;

    if (videoWidth % 16 != 0) {
      videoWidth = videoWidth - (videoWidth % 16);
    }
    if (videoHeight % 16 != 0) {
      videoHeight = videoHeight - (videoHeight % 16);
    }

    retriever.release();
  }

  private boolean isPlaying() {
    return player.getPlaybackState() == Player.STATE_READY && player.getPlayWhenReady();
  }

  private void buildMediaSource(Uri mUri) {
    // Measures bandwidth during playback. Can be null if not required.
    DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    // Produces DataSource instances through which media data is loaded.
    DataSource.Factory dataSourceFactory =
        new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)),
            bandwidthMeter);
    // This is the MediaSource representing the media to be played.
    MediaSource videoSource =
        new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
    // Prepare the player with the source.
    player.prepare(videoSource);
    player.setVolume(1.0f);
  }

  private void startCountDownTimer() {
    String message = checkVideoCanBeCaptured();
    if (message == null) {
      //Video can be captured
      if (timerStarted) {
        return;
      }

      llTimer.setVisibility(View.GONE);
      llArFilters.setVisibility(View.GONE);
      llARCamera.setVisibility(View.GONE);
      if (arCameraActive) {

        llBeautify.setVisibility(View.GONE);
      }
      resetZoom.setVisibility(View.GONE);
      vTabs.setVisibility(View.VISIBLE);

      showCaptureDialogView(mCountDownDialog, mCountDownView, false);
      initCountDownDurationAdjustView(timerDurationSelected);
      mCountDownTextView.setVisibility(View.VISIBLE);
      timerStarted = true;
      mCountDownTextView.downSecond(2);
    } else {
      Toast.makeText(DeeparDuetActivity.this, message, Toast.LENGTH_SHORT).show();
    }
  }
}