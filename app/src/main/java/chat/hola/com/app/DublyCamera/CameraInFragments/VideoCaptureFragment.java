package chat.hola.com.app.DublyCamera.CameraInFragments;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arthenica.mobileffmpeg.FFmpeg;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.DublyFilterAdapter;
import chat.hola.com.app.DublyCamera.DublyFilterModel;
import chat.hola.com.app.DublyCamera.FilterType;
import chat.hola.com.app.DublyCamera.QtFastStart;
import chat.hola.com.app.DublyCamera.ResultHolder;
import chat.hola.com.app.DublyCamera.RippleBackground;
import chat.hola.com.app.DublyCamera.SampleCameraGLView;
import chat.hola.com.app.DublyCamera.dubbing.CountDownDurationAdjustView;
import chat.hola.com.app.DublyCamera.dubbing.TimeDownView;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.CameraRecordListener;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.GPUCameraRecorder;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.GPUCameraRecorderBuilder;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.LensFacing;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.dublycategory.DubCategoryActivity;

public class VideoCaptureFragment extends Fragment {

  private static final int CAMERA_PERMISSIONS_REQ_CODE = 0;
  private static final int MAXIMUM_RECORDING_LENGTH_ALLOWED = 60;
  private final int SELECT_DUB_SOUND_REQUEST = 0;
  private int speed = 3;
  private GPUCameraRecorder GPUCameraRecorder;
  private LensFacing lensFacing = LensFacing.FRONT;
  //Binding views

  @BindView(R.id.camera)
  FrameLayout camera;
  @BindView(R.id.contentFrame)
  RelativeLayout parent;
  @BindView(R.id.selctsound)
  LinearLayout selectSound;
  @BindView(R.id.tvselectsound)
  TextView tvSelectSound;
  @BindView(R.id.speedLayout)
  LinearLayout speedLayout;
  //For the speed controls
  @BindView(R.id.tvRatio_01x)
  TextView tvRatio0_1X;
  @BindView(R.id.tvRatio_05X)
  TextView tvRatio0_5X;
  @BindView(R.id.tvRatio_1X)
  TextView tvRatio_1X;
  @BindView(R.id.tvRatio_2X)
  TextView tvRatio_2X;
  @BindView(R.id.tvRatio_3X)
  TextView tvRatio_3X;
  @BindView(R.id.blackCover)
  View coverView;
  //Bindviews for camera controls
  @BindView(R.id.facingButton)
  AppCompatImageView facingButton;
  @BindView(R.id.flashButton)
  AppCompatImageView flashButton;
  @BindView(R.id.rippleCaptureButton)
  RippleBackground rippleCaptureButton;
  @BindView(R.id.cross)
  AppCompatImageView cross;
  @BindView(R.id.resetZoom)
  AppCompatImageView resetZoom;
  @BindView(R.id.tick)
  AppCompatImageView tick;
  @BindView(R.id.captureButton)
  AppCompatImageView captureButton;

  private ArrayList<String> recordedVideosList = new ArrayList<String>();
  private File speedChangedVideo;
  private ViewGroup flowLayout;
  private int length = 0;
  private ArrayList<Integer> flowLayoutChilds = new ArrayList<>();
  private ArrayList<Integer> dubbingSoundRecordedLengths = new ArrayList<>();
  private ArrayList<Integer> recordedVideoDurations = new ArrayList<>();
  private MediaPlayer player;
  private String appName;
  //For the data received in bundle/extras
  private String requestType;
  private String audioFile = "";
  private String musicId = "";

  private int selectedAudioDurationInSecs = 0;
  private ProgressDialog dialog;
  private int progressBarHeight;
  private String folderPath;
  private int widthScreen;
  private boolean noPendingPermissionsRequest = true;
  private boolean audioSelected = false;
  private boolean captureImageDisabled = false;

  private boolean captureVideoRequested = false;
  private Handler videoCaptureHandler = new Handler();
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
  private DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
  private int screenWidth = displayMetrics.widthPixels;
  private int cameraWidth = screenWidth;
  private int videoWidth = screenWidth;
  private int screenHeight = displayMetrics.heightPixels;
  //    private int cameraHeight = screenHeight;
  //    private int videoHeight = screenHeight;
  private int cameraHeight = screenWidth;
  private int videoHeight = screenWidth;
  private boolean toggleCamera = false;

  private boolean fragmentVisible;
  private boolean flashRunning;

  private Bus bus = AppController.getBus();

  private Context context;

  private View view;
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
  @BindView(R.id.llMute)
  LinearLayout llMute;
  @BindView(R.id.ivMute)
  AppCompatImageView ivMute;
  @BindView(R.id.tvMute)
  TextView tvMute;
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
  @BindView(R.id.ivZoomIn)
  AppCompatImageView ivZoomIn;
  @BindView(R.id.ivZoomOut)
  AppCompatImageView ivZoomOut;

  private android.app.AlertDialog mCountDownDialog;
  private View mCountDownView;
  private CountDownDurationAdjustView mCountDownCaptureView;
  /**
   * Countdown text control
   */
  private TimeDownView mCountDownTextView;
  /*
   * Countdown duration of video shooting in segments
   * */
  private long mCurrentCountDownDuration = 0;
  @BindView(R.id.rippleTimerButton)
  RippleBackground rippleTimerButton;

  /**
   * Filters
   */

  private ArrayList<DublyFilterModel> filterItems;
  private DublyFilterAdapter filterAdapter;
  private View mFiltersView;
  private android.app.AlertDialog mFilterDialog;

  @SuppressLint("ClickableViewAccessibility")
  @SuppressWarnings("ResultOfMethodCallIgnored")
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
    if (videoWidth % 16 != 0) {
      videoWidth = videoWidth - (videoWidth % 16);
    }
    if (videoHeight % 16 != 0) {
      videoHeight = videoHeight - (videoHeight % 16);
    }
    setupTimerView();
    final File imageFolder;
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      imageFolder = new File(context.getExternalFilesDir(null)
          + "/"
          + getResources().getString(R.string.app_name)
          + "/Media/");
    } else {

      imageFolder = new File(
          context.getFilesDir() + "/" + getResources().getString(R.string.app_name) + "/Media/");
    }
    if (!imageFolder.exists() && !imageFolder.isDirectory()) imageFolder.mkdirs();

    folderPath = imageFolder.getAbsolutePath();

    appName = "Demo";
    //getResources().getString(R.string.app_name);

    flowLayout = view.findViewById(R.id.flowlayout);
    dialog = new ProgressDialog(context);
    dialog.setCancelable(false);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((CameraInFragmentsActivity) context).getWindowManager()
        .getDefaultDisplay()
        .getMetrics(displayMetrics);

    widthScreen = displayMetrics.widthPixels;
    scale = displayMetrics.density;
    progressBarHeight = (int) (10 * scale);

    tvSelectSound.setTypeface(AppController.getInstance().getSemiboldFont());

    requestType = ((CameraInFragmentsActivity) context).getIntent().getStringExtra("call");

    if (requestType == null) {
      requestType = "post";
    } else if (requestType.equals("SaveProfile")) {
      speedLayout.setVisibility(View.GONE);
      selectSound.setVisibility(View.GONE);
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      parent.setFitsSystemWindows(true);
    }
    setupFilterView();

    setupZoomAllowedMetrics();
    rippleCaptureButton.setOnTouchListener(handleTouch);

    tvRatio0_1X.setOnClickListener(view -> {
      speed = 1;
      tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.message_select));
      tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.gray));
    });

    tvRatio0_5X.setOnClickListener(view -> {
      speed = 2;
      tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.message_select));
      tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.gray));
    });

    tvRatio_1X.setOnClickListener(view -> {
      speed = 3;

      tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.message_select));
      tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.gray));
    });

    tvRatio_2X.setOnClickListener(view -> {
      speed = 4;

      tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.message_select));
      tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.gray));
    });

    tvRatio_3X.setOnClickListener(view -> {
      speed = 5;

      tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.gray));
      tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.message_select));
    });

    tick.setOnClickListener(view -> {

      if (recordedVideosList.size() > 0) {

        sendVideo();
      }
    });

    cross.setOnClickListener(view -> {

      if (flowLayout.getChildCount() > 0) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(getString(R.string.delete_segment_title));
        alertDialog.setMessage(getString(R.string.delete_segment));
        alertDialog.setPositiveButton(getString(R.string.confirm),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {

                if (recordedVideosList.size() > 0) {
                  //Delete the last recorded video incase more than one video has been recorded

                  deleteFileFromDisk(recordedVideosList.get(recordedVideosList.size() - 1));
                  recordedVideosList.remove(recordedVideosList.size() - 1);
                }
                if (flowLayout.getChildCount() > 0) {
                  //Remove the views from the progress bar

                  try {
                    flowLayout.removeViewsInLayout(
                        flowLayout.getChildCount() - (flowLayoutChilds.get(
                            flowLayoutChilds.size() - 1)),
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
              }
            }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {

          }
        }).show();
      }
    });

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

    selectSound.setOnClickListener(view -> {
      if (timerStarted) {
        return;
      }
      ((CameraInFragmentsActivity) context).startActivityForResult(
          new Intent(context, DubCategoryActivity.class).putExtra("requestType", requestType),
          SELECT_DUB_SOUND_REQUEST);
    });

    resetZoom.setOnClickListener(view -> {
      if (GPUCameraRecorder != null) {

        resetCameraZoom();
        GPUCameraRecorder.zoomOut(true, true);
      }
    });

    //        try {
    //
    //            ((CameraInFragmentsActivity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    //
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }

    setMicMuted(false);
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

      totalCapturedLength = calculateTotalRecordedVideoLength();
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
      totalCapturedLength = calculateTotalRecordedVideoLength();
    } else {

      releaseResources();
    }
  }

  private void releaseResources() {

    try {
      if (player != null) {

        if (player.isPlaying()) {
          player.stop();
        }
        player.release();
      }

      if (timerTask != null) {
        timerTask.cancel();
      }

      if (dialog != null && dialog.isShowing()) {
        dialog.cancel();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    releaseCamera();
  }

  @Override
  public void onStop() {

    releaseResources();

    super.onStop();
  }

  public void onBackPressed() {
    if (timerStarted) {
      return;
    }
    if (flowLayout != null && flowLayout.getChildCount() > 0) {

      //No video has been recorded yet/All recorded videos has been deleted
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setTitle(getString(R.string.tips));
      builder.setMessage(getString(R.string.discard));

      builder.setPositiveButton(getString(R.string.camera_confirm), (dialogInterface, i) -> {
        deleteFilesOnExit();
        //                    super.onBackPressed();
        ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
      });

      builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
      });
      builder.show();
    } else {

      ((CameraInFragmentsActivity) context).closeVideoFragment();
    }
  }

  //Execute the FFmpeg command
  private void execFFmpegBinary(final String[] command, int timer,
      final String speedChangedVideoFilePath, final String capturedVideoFilePath) {
    dialog.setMessage(getString(R.string.processing_video));
    if (!getActivity().isFinishing()) dialog.show();
    FFmpeg.executeAsync(command, (executionId, returnCode) -> {


      if (returnCode == RETURN_CODE_SUCCESS) {
        //Async command execution completed successfully.
        deleteFileFromDisk(capturedVideoFilePath);

        recordedVideosList.add(speedChangedVideoFilePath);

        dialog.dismiss();

        if (audioSelected) {

          //Audio has been selected for dubbing
          if (timer >= selectedAudioDurationInSecs) {
            sendVideo();
          }
        } else {

          //Videos has been dubbed without an audio
          if (timer >= MAXIMUM_RECORDING_LENGTH_ALLOWED) {
            sendVideo();
          }
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

      if (speed == 1) {

        //0.1X speed selected

        if (audioSelected) {

          String[] complexCommand = {
              "-y", "-i", filepath, "-crf", "29", "-filter_complex",
              "[0:v]setpts=10.0*PTS[v]", "-map", "[v]", "-r", "3", "-vcodec", "libx264","-preset", "ultrafast",
              speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        } else {

          String[] complexCommand = {
              "-y", "-i", filepath,  "-crf", "29", "-filter_complex",
              "[0:v]setpts=10.0*PTS[v];[0:a]atempo=0.5,atempo=0.5,atempo=0.5 [a]", "-map", "[v]",
              "-map", "[a]", "-r", "3", "-vcodec", "libx264","-preset", "ultrafast", speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        }
      } else if (speed == 2) {

        //0.5X speed selected

        if (audioSelected) {

          String[] complexCommand = {
              "-y", "-i", filepath,  "-crf", "29", "-filter_complex",
              "[0:v]setpts=2.0*PTS[v]", "-map", "[v]", "-r", "15", "-vcodec", "libx264","-preset", "ultrafast",
              speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        } else {

          String[] complexCommand = {
              "-y", "-i", filepath,"-crf", "29", "-filter_complex",
              "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5 [a]", "-map", "[v]", "-map", "[a]", "-r",
              "15", "-vcodec", "libx264",  "-preset", "ultrafast", speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        }
      } else if (speed == 3) {

        //1X speed selected

        if (audioSelected) {

          String[] complexCommand = {
              "-y", "-i", filepath,  "-crf", "29", "-c:v", "libx264","-preset", "ultrafast", "-an",
              speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        } else {
          String[] complexCommand = {
              "-y", "-i", filepath, "-crf", "29", "-c:v", "libx264", "-preset", "ultrafast", "-c:a",
              "copy", speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        }
      } else if (speed == 4) {

        //2X speed selected

        if (audioSelected) {

          String[] complexCommand = {
              "-y", "-i", filepath,  "-crf", "29", "-filter_complex",
              "[0:v]setpts=0.5*PTS[v]", "-map", "[v]", "-r", "60", "-vcodec", "libx264","-preset", "ultrafast",
              speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        } else {

          String[] complexCommand = {
              "-y", "-i", filepath,  "-crf", "29", "-filter_complex",
              "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0 [a]", "-map", "[v]", "-map", "[a]", "-r",
              "60", "-vcodec", "libx264", "-preset", "ultrafast",speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        }
      } else if (speed == 5) {

        //3X speed selected

        if (audioSelected) {

          String[] complexCommand = {
              "-y", "-i", filepath,  "-crf", "29", "-filter_complex",
              "[0:v]setpts=0.33*PTS[v]", "-map", "[v]", "-r", "90", "-vcodec", "libx264","-preset", "ultrafast",
              speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        } else {

          String[] complexCommand = {
              "-y", "-i", filepath,  "-crf", "29", "-filter_complex",
              "[0:v]setpts=0.33*PTS[v];[0:a]atempo=sqrt(3),atempo=sqrt(3) [a]", "-map", "[v]",
              "-map", "[a]", "-r", "90", "-vcodec", "libx264","-preset", "ultrafast", speedChangedVideo.getAbsolutePath()
          };

          execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
              speedChangedVideo.getAbsolutePath(), filepath);
        }
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

      Toast.makeText(getActivity(), R.string.video_record_failed, Toast.LENGTH_SHORT).show();
    }
  }

  private void sendVideo() {

    //If we have normal speed video then capturedVideoFile != null, if we have video recorded at different speed than 1X then speedChangedVideo != null
    //        File videoMerge;
    if (speedChangedVideo != null) {

      if (audioSelected) {
        //Audio has been selected for merging

        ResultHolder.setCall(requestType);

        ResultHolder.setType("video");

        Intent intent = new Intent(context, PreviewFragmentVideoActivity.class);
        intent.putExtra("videoArray", recordedVideosList);
        intent.putExtra("videoWidth", videoWidth);
        intent.putExtra("videoHeight", videoHeight);
        intent.putExtra("durationArray", recordedVideoDurations);
        intent.putExtra("captured", true);
        intent.putExtra("maximumDuration", (selectedAudioDurationInSecs));
        intent.putExtra("audio", audioFile);
        if (!musicId.isEmpty()) {
          //intent.putExtra("audio", audioFile);
          intent.putExtra("musicId", musicId);
          intent.putExtra("time", selectedAudioDurationInSecs);
        }

        startActivity(intent);

        if (requestType.equals("story")) {
          ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
        } else {
          //TODO
          //Temporary solution for surfaceview not released by exoplayer when coming back
          //                    ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
        }
      } else {

        ResultHolder.setCall(requestType);

        ResultHolder.setType("video");

        Intent intent = new Intent(context, PreviewFragmentVideoActivity.class);
        intent.putExtra("videoArray", recordedVideosList);
        intent.putExtra("captured", true);
        intent.putExtra("videoWidth", videoWidth);
        intent.putExtra("videoHeight", videoHeight);
        intent.putExtra("durationArray", recordedVideoDurations);
        intent.putExtra("maximumDuration", MAXIMUM_RECORDING_LENGTH_ALLOWED);
        //                intent.putExtra("video", videoMerge.getAbsolutePath());

        startActivity(intent);
        if (requestType.equals("story")) {
          ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
        } else {
          //TODO
          //Temporary solution for surfaceview not released by exoplayer when coming back
          //                    ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
        }
      }
    }
  }

  private void setMicMuted(boolean state) {
    AudioManager myAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

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
  }

  private void checkCameraPermissions() {

    if (ActivityCompat.shouldShowRequestPermissionRationale((CameraInFragmentsActivity) context,
        Manifest.permission.READ_EXTERNAL_STORAGE)
        || ActivityCompat.shouldShowRequestPermissionRationale((CameraInFragmentsActivity) context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        || ActivityCompat.shouldShowRequestPermissionRationale((CameraInFragmentsActivity) context,
        Manifest.permission.RECORD_AUDIO)
        || ActivityCompat.shouldShowRequestPermissionRationale((CameraInFragmentsActivity) context,
        Manifest.permission.CAMERA)) {

      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setTitle("Permissions");
      builder.setMessage(R.string.camera_permission);
      builder.setPositiveButton("OK", (dialogInterface, i) -> requestCameraPermission());
      builder.setNegativeButton("DENY", (dialogInterface, i) -> cameraPermissionsDenied());
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
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED) {

      permissionsRequired.add(Manifest.permission.RECORD_AUDIO);
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

  private void videoCaptureAction(boolean dubbingStarted, float durationOfVideoCaptured) {

    try {
      if (dubbingStarted) {
        if (audioSelected) {
          if (speed == 1) {

            //0.1X speed selected
            if (player != null && player.isPlaying()) {
              player.stop();
            }
            player = MediaPlayer.create(context, Uri.parse(audioFile));
            player.setLooping(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

              PlaybackParams playbackParams = new PlaybackParams();
              playbackParams.setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
              playbackParams.setSpeed(10.0f);
              player.setPlaybackParams(playbackParams);
            }
            if (dubbingSoundRecordedLengths.size() != 0) {

              //To play audio from the location,after the last location upto which audio has been dubbed
              player.seekTo(
                  dubbingSoundRecordedLengths.get(dubbingSoundRecordedLengths.size() - 1));
            } else {
              player.seekTo(0);
            }
            player.start();
          } else if (speed == 2) {

            //0.5X speed selected
            if (player != null && player.isPlaying()) {
              player.stop();
            }
            player = MediaPlayer.create(context, Uri.parse(audioFile));
            player.setLooping(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

              PlaybackParams playbackParams = new PlaybackParams();
              playbackParams.setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
              playbackParams.setSpeed(2.0f);
              player.setPlaybackParams(playbackParams);
            }
            if (dubbingSoundRecordedLengths.size() != 0) {

              //To play audio from the location,after the last location upto which audio has been dubbed
              player.seekTo(
                  dubbingSoundRecordedLengths.get(dubbingSoundRecordedLengths.size() - 1));
            } else {
              player.seekTo(0);
            }
            player.start();
          } else if (speed == 3) {

            //1X speed selected
            if (player != null && player.isPlaying()) {
              player.stop();
            }
            player = MediaPlayer.create(context, Uri.parse(audioFile));
            player.setLooping(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

              PlaybackParams playbackParams = new PlaybackParams();
              playbackParams.setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
              playbackParams.setSpeed(1.0f);
              player.setPlaybackParams(playbackParams);
            }
            if (dubbingSoundRecordedLengths.size() != 0) {

              //To play audio from the location,after the last location upto which audio has been dubbed
              player.seekTo(
                  dubbingSoundRecordedLengths.get(dubbingSoundRecordedLengths.size() - 1));
            } else {
              player.seekTo(0);
            }
            player.start();
          } else if (speed == 4) {

            //2X speed selected
            if (player != null && player.isPlaying()) {
              player.stop();
            }
            player = MediaPlayer.create(context, Uri.parse(audioFile));
            player.setLooping(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

              PlaybackParams playbackParams = new PlaybackParams();
              playbackParams.setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
              playbackParams.setSpeed(0.5f);
              player.setPlaybackParams(playbackParams);
            }
            if (dubbingSoundRecordedLengths.size() != 0) {

              //To play audio from the location,after the last location upto which audio has been dubbed
              player.seekTo(
                  dubbingSoundRecordedLengths.get(dubbingSoundRecordedLengths.size() - 1));
            } else {
              player.seekTo(0);
            }
            player.start();
          } else if (speed == 5) {

            //3X speed selected
            if (player != null && player.isPlaying()) {
              player.stop();
            }
            player = MediaPlayer.create(context, Uri.parse(audioFile));
            player.setLooping(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

              PlaybackParams playbackParams = new PlaybackParams();
              playbackParams.setAudioFallbackMode(PlaybackParams.AUDIO_FALLBACK_MODE_DEFAULT);
              playbackParams.setSpeed(0.33f);
              player.setPlaybackParams(playbackParams);
            }

            if (dubbingSoundRecordedLengths.size() != 0) {

              //To play audio from the location,after the last location upto which audio has been dubbed
              player.seekTo(
                  dubbingSoundRecordedLengths.get(dubbingSoundRecordedLengths.size() - 1));
            } else {
              player.seekTo(0);
            }
            player.start();
          }
        }
      } else {

        if (audioSelected) {
          player.pause();
          length = player.getCurrentPosition();
        }
        dubbingSoundRecordedLengths.add(length);
        if (speed == 1) {
          //0.1X speed selected
          recordedVideoDurations.add((int) (durationOfVideoCaptured * 10));
        } else if (speed == 2) {
          //0.5X speed selected
          recordedVideoDurations.add((int) (durationOfVideoCaptured * 2));
        } else if (speed == 3) {
          //1X speed selected
          recordedVideoDurations.add((int) (durationOfVideoCaptured));
        } else if (speed == 4) {
          //2X speed selected
          recordedVideoDurations.add((int) (durationOfVideoCaptured / 2));
        } else if (speed == 5) {
          //3X speed selected
          recordedVideoDurations.add((int) (durationOfVideoCaptured / 3));
        }
      }

      updateViewOnVideoRecordedSuccessfully(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateRecordingUI() {

    if (speed == 1) {

      //0.1X speed selected

      width = 1;

      try {

        if (audioSelected) {

          width = widthScreen / (selectedAudioDurationInSecs / 10);
        } else {
          width = widthScreen / (MAXIMUM_RECORDING_LENGTH_ALLOWED / 10);
        }
      } catch (ArithmeticException e) {
        e.getStackTrace();
      }

      ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
        View view = new View(context);
        view.setBackgroundColor(getResources().getColor(R.color.blue));

        flowLayout.addView(view, lp);
      });
    } else if (speed == 2) {

      //0.5X speed selected

      width = 1;

      try {

        if (audioSelected) {

          width = widthScreen / (selectedAudioDurationInSecs / 2);
        } else {
          width = widthScreen / (MAXIMUM_RECORDING_LENGTH_ALLOWED / 2);
        }
      } catch (ArithmeticException e) {
        e.getStackTrace();
      }

      ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
        View view = new View(context);
        view.setBackgroundColor(getResources().getColor(R.color.blue));

        flowLayout.addView(view, lp);
      });
    } else if (speed == 3) {

      //1X speed selected

      width = 1;

      try {

        if (audioSelected) {

          width = widthScreen / (selectedAudioDurationInSecs);
        } else {
          width = widthScreen / (MAXIMUM_RECORDING_LENGTH_ALLOWED);
        }
      } catch (ArithmeticException e) {
        e.getStackTrace();
      }

      ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
        View view = new View(context);
        view.setBackgroundColor(getResources().getColor(R.color.blue));

        flowLayout.addView(view, lp);
      });
    } else if (speed == 4) {

      //2X speed selected

      width = 1;

      try {

        if (audioSelected) {

          width = widthScreen / (selectedAudioDurationInSecs * 2);
        } else {
          width = widthScreen / (MAXIMUM_RECORDING_LENGTH_ALLOWED * 2);
        }
      } catch (ArithmeticException e) {
        e.getStackTrace();
      }

      ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
        View view = new View(context);
        view.setBackgroundColor(getResources().getColor(R.color.blue));

        flowLayout.addView(view, lp);
      });
    } else if (speed == 5) {

      //3X speed selected

      width = 1;

      try {

        if (audioSelected) {

          width = widthScreen / (selectedAudioDurationInSecs * 3);
        } else {
          width = widthScreen / (MAXIMUM_RECORDING_LENGTH_ALLOWED * 3);
        }
      } catch (ArithmeticException e) {
        e.getStackTrace();
      }

      ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
        View view = new View(context);
        view.setBackgroundColor(getResources().getColor(R.color.blue));

        flowLayout.addView(view, lp);
      });
    }
  }

  private void validateCameraPermissions() {

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
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

      if (audioSelected) {
        if (audioFile != null) {
          player = MediaPlayer.create(context, Uri.parse(audioFile));
          player.setLooping(false);
        }
      }
    } else {
      checkCameraPermissions();
    }
  }

  private void updateSelectDubbingSoundOption(boolean enable) {

    selectSound.setClickable(enable);
    selectSound.setEnabled(enable);

    if (enable) {
      tvSelectSound.setText(getString(R.string.select_sound));

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

  private void updateProgressBar(boolean start) {

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
            if (audioSelected) {

              if ((totalCapturedLength + videoCapturedLength) >= selectedAudioDurationInSecs) {
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
        }
      };

      new Timer().schedule(timerTask, 1000, 1000);
      //            new Timer().schedule(timerTask, 0, 1000);
      videoCaptureAction(true, -1);
    } else {

      //Stop video capture

      if (captureEndTime - captureStartTime > 1000) {

        //Video should have been captured for atleast one sec
        videoCaptureAction(false, durationOfVideoCaptured);
        addRecordingStoppedViewInProgressbar();
      } else {

        //                removeLastAddedDuration();
        if (player != null && player.isPlaying()) {
          player.stop();
        }
        Toast.makeText(context, getString(R.string.record_small_duration), Toast.LENGTH_SHORT)
            .show();
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

      ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
        if (GPUCameraRecorder != null) {
          GPUCameraRecorder.stop();
        }
      });
    } else {
      if (GPUCameraRecorder != null) {
        GPUCameraRecorder.stop();
      }
    }
    updateProgressBar(false);

    durationOfVideoCaptured = 0;
    if (fromNonUIThread) {
      ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
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

    switch (speed) {

      case 1: {
        //0.1X speed selected
        actualDurationOfVideoCaptured = 10;

        break;
      }
      case 2: {
        //0.5X speed selected
        actualDurationOfVideoCaptured = 2;

        break;
      }
      case 3: {
        //1X speed selected
        actualDurationOfVideoCaptured = 1;

        break;
      }
      case 4: {
        //2X speed selected
        actualDurationOfVideoCaptured = 0.5f;

        break;
      }
      case 5: {
        //3X speed selected
        actualDurationOfVideoCaptured = 0.33f;

        break;
      }
    }

    durationOfVideoCaptured = 0;
    videoCapturedLength = 0;

    initialChildCount = flowLayout.getChildCount();
    captureStartTime = System.currentTimeMillis();

    filepath = folderPath + "/" + System.currentTimeMillis() + appName + ".mp4";

    //        filepath = getVideoFilePath();
    GPUCameraRecorder.start(filepath);

    //        updateProgressBar(true);
    captureButton.setColorFilter(ContextCompat.getColor(context, R.color.doodle_color_red),
        android.graphics.PorterDuff.Mode.SRC_IN);

    rippleCaptureButton.startRippleAnimation();
    if (!captureImageDisabled) {
      captureImageDisabled = true;
    }
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
    captureImageDisabled = false;
    flowLayoutChilds = new ArrayList<>();
    dubbingSoundRecordedLengths = new ArrayList<>();
    recordedVideoDurations = new ArrayList<>();
    audioSelected = false;

    flowLayout.removeViewsInLayout(0, flowLayout.getChildCount());

    updateSelectDubbingSoundOption(true);
    updateViewOnVideoRecordedSuccessfully(true);
  }

  private void addRecordingStoppedViewInProgressbar() {
    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(1, progressBarHeight);
    View view = new View(context);
    view.setBackgroundColor(getResources().getColor(R.color.whiteOverlay));

    ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
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
      if (filterItems != null) {
        deselectAllFilters();
      }
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

                    flashButton.setVisibility(View.GONE);
                  }
                });
              }

              @Override
              public void onRecordComplete() {

                if (!lastRecordingCanceled) {

                  dialog.setMessage(getString(R.string.saving_video));
                  dialog.show();

                  //For the issue fo file getting saved asynchronously on external storage and taking around 200ms for saving
                  delayProcessingForFileSaveHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      if (dialog != null && dialog.isShowing()) {
                        dialog.cancel();
                      }
                      handleVideoCaptured(filepath);
                    }
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
                  updateProgressBar(true);
                }
              }

              @Override
              public void onError(Exception e) {
                e.printStackTrace();
              }

              @Override
              public void onCameraThreadFinish() {

                if (toggleCamera) {

                  ((CameraInFragmentsActivity) context).runOnUiThread(() -> {
                    setUpCamera();
                  });
                }
                toggleCamera = false;
              }
            })
            .videoSize(videoWidth, videoHeight)
            .cameraSize(cameraWidth, cameraHeight)
            .lensFacing(lensFacing)
            //.mute(true)
            .build();
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
          vibrate();

          if (audioSelected) {

            if (totalCapturedLength < selectedAudioDurationInSecs) {

              captureVideoRequested = true;

              videoCaptureHandler.postDelayed(() -> {
                if (captureVideoRequested) {
                  initialCaptureButtonX = view.getX();
                  initialCaptureButtonY = view.getY();
                  startVideoCapture();
                }
              }, 250);
            } else {

              Toast.makeText(context, R.string.dubbing_max_size, Toast.LENGTH_SHORT).show();
            }
          } else {

            if (totalCapturedLength < MAXIMUM_RECORDING_LENGTH_ALLOWED) {

              captureVideoRequested = true;

              videoCaptureHandler.postDelayed(() -> {
                if (captureVideoRequested) {
                  initialCaptureButtonX = view.getX();
                  initialCaptureButtonY = view.getY();
                  startVideoCapture();
                }
              }, 250);
            } else {

              Toast.makeText(context, R.string.recording_max_size, Toast.LENGTH_SHORT).show();
            }
          }

          break;
        }
        case MotionEvent.ACTION_UP: {
          captureVideoRequested = false;

          if (capturingVideo) {

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

            view.animate().x(event.getRawX() + dX).y(event.getRawY() + dY).setDuration(0).start();

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
    } catch (QtFastStart.MalformedFileException | IOException | QtFastStart.UnsupportedFileException e1) {
      e1.printStackTrace();
    }

    return path;
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
        killCameraActivityRequested = true;
        ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
      } else if (object.getString("eventName").equals("selectDubSoundResult")) {

        if (object.getInt("requestCode") == SELECT_DUB_SOUND_REQUEST) {

          if (object.getInt("resultCode") == Activity.RESULT_OK) {

            audioFile = object.getString("audio");
            String selectedAudioName = object.getString("name");
            musicId = object.getString("musicId");
            audioSelected = true;

            if (audioFile != null) {
              Uri uri = Uri.parse(audioFile);
              MediaMetadataRetriever mmr = new MediaMetadataRetriever();
              mmr.setDataSource(context.getApplicationContext(), uri);
              String durationStr =
                  mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

              selectedAudioDurationInSecs = Integer.parseInt(durationStr) / 1000;
            }

            if (selectedAudioName != null) {
              tvSelectSound.setText(selectedAudioName.replace(".aac", ""));
              tvSelectSound.setSelected(true);
              setMicMuted(true);
            }
          } else if (object.getInt("resultCode") == Activity.RESULT_CANCELED) {
            //Write your code if there's no result

            Toast.makeText(context, getString(R.string.dubsound_cancel), Toast.LENGTH_SHORT).show();
          }
        }
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

  private boolean killCameraActivityRequested = false;

  void deleteFilesOnExit() {

    if (!killCameraActivityRequested) {
      if (recordedVideosList != null && recordedVideosList.size() > 0) {

        for (int j = 0; j < recordedVideosList.size(); j++) {

          deleteFileFromDisk(recordedVideosList.get(j));
        }
      }
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

  private void resetCaptureButton(boolean fromNonUiThread) {
    if (fromNonUiThread) {

      ((CameraInFragmentsActivity) context).runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (captureButtonView != null) {

            captureButtonView.animate()
                .x(initialCaptureButtonX)
                .y(initialCaptureButtonY)
                .setDuration(300)
                .start();
          }
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

    showCaptureDialogView(mCountDownDialog, mCountDownView);
    initCountDownDurationAdjustView();
  }

  private void setupTimerView() {

    llTimer.setVisibility(View.VISIBLE);
    llMute.setVisibility(View.VISIBLE);
    mCountDownView = LayoutInflater.from(context).inflate(R.layout.countdown_view, null);
    mCountDownCaptureView = mCountDownView.findViewById(R.id.countDownCaptureView);
    Button mStartCountDownCapture = mCountDownView.findViewById(R.id.btStartCountDownCapture);
    mCountDownTextView = view.findViewById(R.id.tvCountDown);
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

    mCountDownCaptureView.setNewDurationChangeListener((newDuration, isDragEnd) -> {
      mCurrentCountDownDuration = newDuration;
    });

    mStartCountDownCapture.setOnClickListener(v -> {
      closeCaptureDialogView(mCountDownDialog, false);

      if (audioSelected) {

        if (totalCapturedLength < selectedAudioDurationInSecs) {

          mCountDownTextView.setVisibility(View.VISIBLE);
          timerStarted = true;
          mCountDownTextView.downSecond(2);
          captureVideoRequested = true;
        } else {

          Toast.makeText(context, R.string.dubbing_max_size, Toast.LENGTH_SHORT).show();
        }
      } else {

        if (totalCapturedLength < MAXIMUM_RECORDING_LENGTH_ALLOWED) {

          mCountDownTextView.setVisibility(View.VISIBLE);
          timerStarted = true;
          mCountDownTextView.downSecond(2);
          captureVideoRequested = true;
        } else {

          Toast.makeText(context, R.string.recording_max_size, Toast.LENGTH_SHORT).show();
        }
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
    if (getActivity() != null) {
      mCountDownDialog = new android.app.AlertDialog.Builder(getActivity()).create();
      mCountDownDialog.setOnCancelListener(
          dialog -> closeCaptureDialogView(mCountDownDialog, true));
    }
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

  private void initCountDownDurationAdjustView() {

    int MAX_RECORD_DURATION;
    if (audioSelected) {
      MAX_RECORD_DURATION = selectedAudioDurationInSecs;
    } else {
      MAX_RECORD_DURATION = MAXIMUM_RECORDING_LENGTH_ALLOWED;
    }

    if (totalCapturedLength >= MAX_RECORD_DURATION) totalCapturedLength = MAX_RECORD_DURATION;
    mCountDownCaptureView.setMaxCaptureDuration(MAX_RECORD_DURATION);
    mCountDownCaptureView.setCurrentCaptureDuration((int) totalCapturedLength);

    mCountDownCaptureView.resetHandleViewPosition(
        getString(R.string.max_duration, String.valueOf(MAX_RECORD_DURATION)));
    int newMaxCaptureDuration = MAX_RECORD_DURATION - (int) totalCapturedLength;
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
        if (getActivity() != null) {
          getActivity().runOnUiThread(() -> {
            rlTimer.setVisibility(View.GONE);
            rippleTimerButton.stopRippleAnimation();
            rlCameraControls.setVisibility(View.VISIBLE);
          });
        }
      } else {
        rlTimer.setVisibility(View.GONE);
        rippleTimerButton.stopRippleAnimation();
        rlCameraControls.setVisibility(View.VISIBLE);
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
    RecyclerView filtersList = mFiltersView.findViewById(R.id.rvFilters);

    mFilterDialog = new android.app.AlertDialog.Builder(context).create();
    mFilterDialog.setOnCancelListener(dialog -> closeCaptureDialogView(mFilterDialog, true));

    filtersList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));

    filterItems = new ArrayList<>();

    prepareFilterItems();

    filterAdapter = new DublyFilterAdapter(context, filterItems, false);

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

  private boolean muted;

  /**
   * /*Bug Title- Please add a mute feature in the video recording
   * /*Bug Id-DUBAND016
   * /*Fix Description-Add option to toggle video mute state
   * /*Developer Name-Ashutosh
   * /*Fix Date-6/4/21
   **/
  @OnClick(R.id.llMute)
  public void mute() {
    ivMute.setSelected(muted);
    muted = !muted;
    setMicMuted(muted);
    if (muted) {
      if (getActivity() != null) {
        ivMute.setImageDrawable(getActivity().getDrawable(R.drawable.voice_call_record_icon_off));
        tvMute.setText("unMute");
      }
    } else {
      if (getActivity() != null) {
        ivMute.setImageDrawable(getActivity().getDrawable(R.drawable.voice_call_record_icon_on));
        tvMute.setText("Mute");
      }
    }
  }
}