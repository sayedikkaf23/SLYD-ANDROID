package chat.hola.com.app.DublyCamera;
//https://res.cloudinary.com/dxkoc9aao/image/upload/g_south_east,l_watermark:watermark.png/v1596463659/rn5nxuhap4uocwanqvwy.jpg

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.opengl.GLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Range;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.dubbing.CountDownDurationAdjustView;
import chat.hola.com.app.DublyCamera.dubbing.TimeDownView;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.CameraRecordListener;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.GPUCameraRecorder;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.GPUCameraRecorderBuilder;
import chat.hola.com.app.DublyCamera.gpuv.camerarecorder.LensFacing;
import chat.hola.com.app.DublyCamera.live_stream.CameraStreamContract;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.dublycategory.DubCategoryActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.WebRTCStreamBroadcasterActivity;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import dagger.android.support.DaggerAppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import org.json.JSONException;
import org.json.JSONObject;

import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class CameraActivity extends DaggerAppCompatActivity implements CameraStreamContract.View {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_PERMISSIONS_REQ_CODE = 0;
    private static final int MAXIMUM_RECORDING_LENGTH_ALLOWED = 60;
    private final int SELECT_DUB_SOUND_REQUEST = 0;
    public int speed = 3;
    protected GPUCameraRecorder GPUCameraRecorder;
    protected LensFacing lensFacing = LensFacing.FRONT;
    public static String imageUrl;
    @Inject
    CameraStreamContract.Presenter presenter;

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
    @BindView(R.id.resetZoom)
    AppCompatImageView resetZoom;
    @BindView(R.id.flashButton)
    AppCompatImageView flashButton;
    @BindView(R.id.captureButton)
    AppCompatImageView captureButton;
    @BindView(R.id.cross)
    AppCompatImageView cross;
    @BindView(R.id.iv_gallery)
    AppCompatImageView ivGallery;
    @BindView(R.id.tick)
    AppCompatImageView tick;
    @BindView(R.id.selectFilters)
    AppCompatImageView selectFilters;
    @BindView(R.id.filtersList)
    RecyclerView filtersList;
    @BindView(R.id.filtersBottomView)
    LinearLayout filtersBottomView;

    @BindView(R.id.rippleCaptureButton)
    RippleBackground rippleCaptureButton;

    @BindView(R.id.rlNormalCamera)
    RelativeLayout rlNormalCamera;
    @BindView(R.id.rlLiveStream)
    RelativeLayout rlLiveStream;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.btnSnap)
    ImageButton btnSnap;
    @BindView(R.id.ivLiveStreamCover)
    ImageView ivLiveStreamCover;
    @BindView(R.id.btnLiveStream)
    Button btnLiveStream;

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
    private boolean isFrontFace = false;
    private int selectedAudioDurationInSecs = 0;
    private ProgressDialog dialog;
    private int progressBarHeight;
    private String folderPath;
    private int widthScreen;
    private boolean noPendingPermissionsRequest = true;
    private boolean audioSelected = false;
    private boolean captureImageDisabled = false;
    private boolean onlyPhotoCanBeCaptured = false;
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
    protected int cameraWidth = screenWidth;
    protected int videoWidth = screenWidth;
    private int screenHeight = displayMetrics.heightPixels;
    protected int cameraHeight = screenHeight;
    protected int videoHeight = screenHeight;
    private boolean toggleCamera = false;

    private boolean filtersVisible = false;
    private ArrayList<DublyFilterModel> filterItems;
    private DublyFilterAdapter filterAdapter;

    private boolean flashRunning;
    private TimerTask imageCaptureTimerTask;
    private int alreadyTriedTimes;
    private static final int maxRetries = 4;
    private boolean bitmapCaptureAlreadyRunning = false;

    private static final int MAXIMUM_PIXELS_TO_COMPARE = 100;
    private boolean updatePixelComparisonLimitRequired = false;
    private Bus bus = AppController.getBus();
    private BottomSheetBehavior sheetBehavior;
    private boolean lastRecordingCanceled;

    private int galleryVideoWidth, galleryVideoHeight;

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

    private String streamName = "rtmpstream-";
    private String streamType;
    private String thumbnailUrl = "";
    private boolean fromLandingActivity;

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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_main);
        ButterKnife.bind(this);

        boolean isLiveStream = getIntent().getBooleanExtra("isLiveStream", false);
        fromLandingActivity = getIntent().getBooleanExtra("fromLandingActivity", false);

        rlNormalCamera.setVisibility(isLiveStream ? View.GONE : View.VISIBLE);
        rlLiveStream.setVisibility(!isLiveStream ? View.GONE : View.VISIBLE);
        llTimer.setVisibility(isLiveStream ? View.GONE : View.VISIBLE);

        streamName = AppController.getInstance().getUserId() + System.currentTimeMillis();
        streamType = "start";

        boolean isRecord = getIntent().getBooleanExtra("isRecord", false);
        if (isRecord) {
            try {
                audioFile = getIntent().getStringExtra("audio");
                String selectedAudioName = getIntent().getStringExtra("name");
                musicId = getIntent().getStringExtra("musicId");
                audioSelected = true;

                if (audioFile != null) {

                    Uri uri = Uri.parse(audioFile);
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(CameraActivity.this, uri);
                    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    selectedAudioDurationInSecs = Integer.parseInt(durationStr) / 1000;
                }

                if (selectedAudioName != null) {
                    tvSelectSound.setText(
                            selectedAudioName.replace(".aac", getString(R.string.double_inverted_comma)));
                    tvSelectSound.setSelected(true);
                    setMicMuted(true);
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.unable_to_select_audio), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        try {

            MediaCodec mediaCodec;
            MediaCodecInfo info;
            MediaCodecInfo.CodecCapabilities cap;
            try {
                mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);

                info = mediaCodec.getCodecInfo();

                cap = info.getCapabilitiesForType(MediaFormat.MIMETYPE_VIDEO_AVC);
            } catch (IllegalArgumentException e) {
                //For Xiaomi devices
                mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_H263);

                info = mediaCodec.getCodecInfo();

                cap = info.getCapabilitiesForType(MediaFormat.MIMETYPE_VIDEO_H263);
            }

            MediaCodecInfo.VideoCapabilities vcap = cap.getVideoCapabilities();

            try {
                Range<Integer> heights = vcap.getSupportedHeightsFor(screenWidth);

                int maxSupportedHeight = heights.getUpper();
                if (videoHeight > maxSupportedHeight) {
                    videoHeight = maxSupportedHeight;
                }
            } catch (IllegalArgumentException e) {
                //To handle
                //java.lang.IllegalArgumentException: unsupported width
                videoWidth = vcap.getSupportedWidths().getUpper();

                Range<Integer> heights = vcap.getSupportedHeightsFor(videoWidth);

                int maxSupportedHeight = heights.getUpper();
                if (videoHeight > maxSupportedHeight) {
                    videoHeight = maxSupportedHeight;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //}

        if (videoWidth % 16 != 0) {
            videoWidth = videoWidth - (videoWidth % 16);
        }
        if (videoHeight % 16 != 0) {
            videoHeight = videoHeight - (videoHeight % 16);
        }
        //Log.d("logcamera", videoWidth + " " + videoHeight);
        final File imageFolder;
        if (Environment.getExternalStorageState().

                equals(Environment.MEDIA_MOUNTED)) {
            imageFolder = new File(getExternalFilesDir(null)
                    + "/"
                    + getResources().getString(R.string.app_name)
                    + "/Media/");

            //            imageFolder = new File(
            //                    Environment.getExternalStorageDirectory() + "/" + getResources().getString(
            //                            R.string.app_name) + "/Media/");

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
        //getResources().getString(R.string.app_name);

        flowLayout = (ViewGroup)

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

        tvSelectSound.setTypeface(AppController.getInstance().

                getSemiboldFont());

        isFrontFace = true;

        requestType =

                getIntent().

                        getStringExtra("call");

        if (requestType == null) {
            requestType = "post";
        } else if (requestType.equals("SaveProfile")) {
            speedLayout.setVisibility(View.GONE);
            selectSound.setVisibility(View.GONE);
            llTimer.setVisibility(View.GONE);
            onlyPhotoCanBeCaptured = true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parent.setFitsSystemWindows(true);
        }
        setupTimerView();

        filtersList.setLayoutManager(new

                GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));

        filterItems = new ArrayList<>();

        prepareFilterItems();

        sheetBehavior = BottomSheetBehavior.from(filtersBottomView);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        filterAdapter = new

                DublyFilterAdapter(this, filterItems, false);
        filtersList.setAdapter(filterAdapter);
        filtersList.addOnItemTouchListener(
                new RecyclerItemClickListener(CameraActivity.this, filtersList,
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

                                            //                            filterItem.setSelected(false);
                                        } else {
                                            GPUCameraRecorder.setFilter(
                                                    FilterType.createGlFilter(filterItem.getFilterType(),
                                                            getApplicationContext()));
                                            selectCurrentFilter(position);
                                            //                            filterItem.setSelected(true);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onItemLongClick(View view, final int position) {

                            }
                        }));

        setupZoomAllowedMetrics();

        rippleCaptureButton.setOnTouchListener(handleTouch);

        tvRatio0_1X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed = 1;
                tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.message_select));
                tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        });

        tvRatio0_5X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed = 2;
                tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.message_select));
                tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        });

        tvRatio_1X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed = 3;

                tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.message_select));
                tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        });

        tvRatio_2X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed = 4;

                tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.message_select));
                tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.gray));
            }
        });

        tvRatio_3X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speed = 5;

                tvRatio0_1X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio0_5X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_1X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_2X.setBackgroundColor(getResources().getColor(R.color.gray));
                tvRatio_3X.setBackgroundColor(getResources().getColor(R.color.message_select));
            }
        });

        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (recordedVideosList.size() > 0) {

                    sendVideo();
                }
            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flowLayout.getChildCount() > 0) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(CameraActivity.this);
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

        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        selectSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timerStarted) {
                    return;
                }
                startActivityForResult(
                        new Intent(CameraActivity.this, DubCategoryActivity.class).putExtra("requestType",
                                requestType), SELECT_DUB_SOUND_REQUEST);
            }
        });

        selectFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFiltersVisibility(filtersVisible);
            }
        });
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        filtersVisible = false;
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        filtersVisible = true;
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        filtersVisible = false;
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        resetZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GPUCameraRecorder != null) {
                    resetCameraZoom();
                    GPUCameraRecorder.zoomOut(true, true);
                }
            }
        });


        try {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setMicMuted(false);
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

                if (player.isPlaying()) {
                    try {
                        player.stop();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
                player.release();
            }

            if (timerTask != null) {
                timerTask.cancel();
            }
            if (imageCaptureTimerTask != null) {
                imageCaptureTimerTask.cancel();
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
        if (fromLandingActivity) {
            startActivity(new Intent(this, LandingActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            finish();
        } else {
            if (flowLayout.getChildCount() > 0) {

                //No video has been recorded yet/All recorded videos has been deleted
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.tips));
                builder.setMessage(getString(R.string.discard));

                builder.setPositiveButton(getString(R.string.camera_confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (recordedVideosList != null && recordedVideosList.size() > 0) {

                                    for (int j = 0; j < recordedVideosList.size(); j++) {

                                        deleteFileFromDisk(recordedVideosList.get(j));
                                    }
                                }
                                //                    super.onBackPressed();
                                supportFinishAfterTransition();
                            }
                        });

                builder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                });
                builder.show();
            } else {

                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @OnClick(R.id.iv_gallery)
    public void openGallery() {

        launchImagePicker();
    }

    @OnClick({R.id.back_button, R.id.ibCLose})
    public void back() {
        onBackPressed();
    }


    //Execute the FFmpeg command
    private void execFFmpegBinary(final String[] command, int timer,
                                  final String speedChangedVideoFilePath, final String capturedVideoFilePath) {

        dialog.setMessage(getString(R.string.processing_video));
        if (!isFinishing()) dialog.show();
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

        //try {
        //  ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
        //    @Override
        //    public void onFailure(String s) {
        //
        //      deleteFileFromDisk(speedChangedVideoFilePath);
        //    }
        //
        //    @Override
        //    public void onSuccess(String s) {
        //
        //
        //    }
        //
        //    @Override
        //    public void onProgress(String s) {
        //
        //
        //    }
        //
        //    @Override
        //    public void onStart() {
        //
        //      //                Log.d("OnStart", "Started command : " + command);
        //
        //      dialog.setMessage(getString(R.string.processing_video));
        //      if (!isFinishing()) dialog.show();
        //    }
        //
        //    @Override
        //    public void onFinish() {
        //
        //      //                Log.d("OnFinish", "Finish " + speedChangedVideo.length());
        //
        //      deleteFileFromDisk(capturedVideoFilePath);
        //
        //      recordedVideosList.add(speedChangedVideoFilePath);
        //
        //      dialog.dismiss();
        //
        //      if (audioSelected) {
        //
        //        //Audio has been selected for dubbing
        //        if (timer >= selectedAudioDurationInSecs) {
        //          sendVideo();
        //        }
        //      } else {
        //
        //        //Videos has been dubbed without an audio
        //        if (timer >= MAXIMUM_RECORDING_LENGTH_ALLOWED) {
        //          sendVideo();
        //        }
        //      }
        //    }
        //  }, this);
        //} catch (FFmpegCommandAlreadyRunningException e) {
        //  e.printStackTrace();
        //}
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void handleVideoCaptured(String recordedFilepath) {

        int totalRecordedVideoDuration = (int) totalCapturedLength;
        updateSelectDubbingSoundOption(false);

        String filepath = makeMp4FilesStreamable(recordedFilepath);

        //
        //        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //        //use one of overloaded setDataSource() functions to set your data source
        //
        //        try {
        //            retriever.setDataSource(CameraActivity.this, Uri.fromFile(capturedVideoFile));
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        //        long timeInMillisec = Long.parseLong(time);
        //        retriever.release();

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

                //                String[] complexCommand = {"-y", "-i", capturedVideoFile.getAbsolutePath(), "-preset", "ultrafast", "-crf", "29", "-filter_complex", "[0:v]setpts=10.0*PTS[v]", "-map", "[v]", "-r", "3", "-vcodec", "libx264", speedChangedVideo.getAbsolutePath()};
                //
                //                execFFmpegBinary(complexCommand, totalRecordedVideoDuration, speedChangedVideo.getAbsolutePath(), capturedVideoFile.getAbsolutePath());
                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-filter_complex",
                        "[0:v]setpts=10.0*PTS[v]", "-map", "[v]", "-r", "3", "-vcodec", "libx264", "-preset", "ultrafast",
                        speedChangedVideo.getAbsolutePath()
                };

                //                String[] complexCommand = {
                //                        "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "29", "-filter_complex",
                //                        "[0:v]setpts=10.0*PTS[v]", "-map", "[v]", "-r", "3",
                //                        speedChangedVideo.getAbsolutePath()
                //                };

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            } else {

                //                String[] complexCommand = {"-y", "-i", capturedVideoFile.getAbsolutePath(), "-preset", "ultrafast", "-crf", "29", "-filter_complex", "[0:v]setpts=10.0*PTS[v];[0:a]atempo=0.5,atempo=0.5,atempo=0.5 [a]", "-map", "[v]", "-map", "[a]", "-r", "3", "-vcodec", "libx264", speedChangedVideo.getAbsolutePath()};
                //
                //                execFFmpegBinary(complexCommand, totalRecordedVideoDuration, speedChangedVideo.getAbsolutePath(), capturedVideoFile.getAbsolutePath());
                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-filter_complex",
                        "[0:v]setpts=10.0*PTS[v];[0:a]atempo=0.5,atempo=0.5,atempo=0.5 [a]", "-map", "[v]",
                        "-map", "[a]", "-r", "3", "-vcodec", "libx264", "-preset", "ultrafast", speedChangedVideo.getAbsolutePath()
                };

                //                String[] complexCommand = {
                //                        "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "29", "-filter_complex",
                //                        "[0:v]setpts=10.0*PTS[v];[0:a]atempo=0.5,atempo=0.5,atempo=0.5 [a]", "-map", "[v]",
                //                        "-map", "[a]", "-r", "3", speedChangedVideo.getAbsolutePath()
                //                };

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            }
        } else if (speed == 2) {

            //0.5X speed selected

            if (audioSelected) {

                //                String[] complexCommand = {"-y", "-i", capturedVideoFile.getAbsolutePath(), "-preset", "ultrafast", "-crf", "29", "-filter_complex", "[0:v]setpts=2.0*PTS[v]", "-map", "[v]", "-r", "15", "-vcodec", "libx264", speedChangedVideo.getAbsolutePath()};
                //
                //                execFFmpegBinary(complexCommand, totalRecordedVideoDuration, speedChangedVideo.getAbsolutePath(), capturedVideoFile.getAbsolutePath());
                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-filter_complex",
                        "[0:v]setpts=2.0*PTS[v]", "-map", "[v]", "-r", "15", "-vcodec", "libx264", "-preset", "ultrafast",
                        speedChangedVideo.getAbsolutePath()
                };

                //                String[] complexCommand = {
                //                        "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "29", "-filter_complex",
                //                        "[0:v]setpts=2.0*PTS[v]", "-map", "[v]", "-r", "15",
                //                        speedChangedVideo.getAbsolutePath()
                //                };

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            } else {

                //                String[] complexCommand = {"-y", "-i", capturedVideoFile.getAbsolutePath(), "-preset", "ultrafast", "-crf", "29", "-filter_complex", "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5 [a]", "-map", "[v]", "-map", "[a]", "-r", "15", "-vcodec", "libx264", speedChangedVideo.getAbsolutePath()};
                //
                //                execFFmpegBinary(complexCommand, totalRecordedVideoDuration, speedChangedVideo.getAbsolutePath(), capturedVideoFile.getAbsolutePath());
                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-filter_complex",
                        "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5 [a]", "-map", "[v]", "-map", "[a]", "-r", "15",
                        "-vcodec", "libx264", "-preset", "ultrafast", speedChangedVideo.getAbsolutePath()
                };

                //                String[] complexCommand = {
                //                        "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "29", "-filter_complex",
                //                        "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5 [a]", "-map", "[v]", "-map", "[a]", "-r", "15", speedChangedVideo.getAbsolutePath()
                //                };

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            }
        } else if (speed == 3) {

            //1X speed selected

            if (audioSelected) {

                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-c:v", "libx264", "-preset", "ultrafast", "-an",
                        speedChangedVideo.getAbsolutePath()
                };

                //String[] complexCommand = {
                // "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "32", "-vcodec", "libx264","-vcodec", "copy", "-an",
                // speedChangedVideo.getAbsolutePath()
                //};

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            } else {
                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-c:v", "libx264", "-preset", "ultrafast", "-c:a",
                        "copy", speedChangedVideo.getAbsolutePath()
                };

                //String[] complexCommand = {
                // "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "32", "-vcodec", "libx264","-vcodec", "copy", "-an",
                // speedChangedVideo.getAbsolutePath()
                //};

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            }
        } else if (speed == 4) {

            //2X speed selected

            if (audioSelected) {

                //                String[] complexCommand = {"-y", "-i", capturedVideoFile.getAbsolutePath(), "-preset", "ultrafast", "-crf", "29", "-filter_complex", "[0:v]setpts=0.5*PTS[v]", "-map", "[v]", "-r", "60", "-vcodec", "libx264", speedChangedVideo.getAbsolutePath()};
                //
                //                execFFmpegBinary(complexCommand, totalRecordedVideoDuration, speedChangedVideo.getAbsolutePath(), capturedVideoFile.getAbsolutePath());
                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-filter_complex",
                        "[0:v]setpts=0.5*PTS[v]", "-map", "[v]", "-r", "60", "-vcodec", "libx264", "-preset", "ultrafast",
                        speedChangedVideo.getAbsolutePath()
                };

                //                String[] complexCommand = {
                //                        "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "29", "-filter_complex",
                //                        "[0:v]setpts=0.5*PTS[v]", "-map", "[v]", "-r", "60",
                //                        speedChangedVideo.getAbsolutePath()
                //                };

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            } else {

                //                String[] complexCommand = {"-y", "-i", capturedVideoFile.getAbsolutePath(), "-preset", "ultrafast", "-crf", "29", "-filter_complex", "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0 [a]", "-map", "[v]", "-map", "[a]", "-r", "60", "-vcodec", "libx264", speedChangedVideo.getAbsolutePath()};
                //
                //                execFFmpegBinary(complexCommand, totalRecordedVideoDuration, speedChangedVideo.getAbsolutePath(), capturedVideoFile.getAbsolutePath());
                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-filter_complex",
                        "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0 [a]", "-map", "[v]", "-map", "[a]", "-r", "60",
                        "-vcodec", "libx264", "-preset", "ultrafast", speedChangedVideo.getAbsolutePath()
                };

                //                String[] complexCommand = {
                //                        "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "29", "-filter_complex",
                //                        "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0 [a]", "-map", "[v]", "-map", "[a]", "-r", "60", speedChangedVideo.getAbsolutePath()
                //                };

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            }
        } else if (speed == 5) {

            //3X speed selected

            if (audioSelected) {

                //                String[] complexCommand = {"-y", "-i", capturedVideoFile.getAbsolutePath(), "-preset", "ultrafast", "-crf", "29", "-filter_complex", "[0:v]setpts=0.33*PTS[v]", "-map", "[v]", "-r", "90", "-vcodec", "libx264", speedChangedVideo.getAbsolutePath()};
                //
                //                execFFmpegBinary(complexCommand, totalRecordedVideoDuration, speedChangedVideo.getAbsolutePath(), capturedVideoFile.getAbsolutePath());
                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-filter_complex",
                        "[0:v]setpts=0.33*PTS[v]", "-map", "[v]", "-r", "90", "-vcodec", "libx264", "-preset", "ultrafast",
                        speedChangedVideo.getAbsolutePath()
                };

                //                String[] complexCommand = {
                //                        "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "29", "-filter_complex",
                //                        "[0:v]setpts=0.33*PTS[v]", "-map", "[v]", "-r", "90",
                //                        speedChangedVideo.getAbsolutePath()
                //                };

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            } else {

                //                String[] complexCommand = {"-y", "-i", capturedVideoFile.getAbsolutePath(), "-preset", "ultrafast", "-crf", "29", "-filter_complex", "[0:v]setpts=0.33*PTS[v];[0:a]atempo=sqrt(3),atempo=sqrt(3) [a]", "-map", "[v]", "-map", "[a]", "-r", "90", "-vcodec", "libx264", speedChangedVideo.getAbsolutePath()};
                //
                //                execFFmpegBinary(complexCommand, totalRecordedVideoDuration, speedChangedVideo.getAbsolutePath(), capturedVideoFile.getAbsolutePath());
                String[] complexCommand = {
                        "-y", "-i", filepath, "-crf", "29", "-filter_complex",
                        "[0:v]setpts=0.33*PTS[v];[0:a]atempo=sqrt(3),atempo=sqrt(3) [a]", "-map", "[v]", "-map",
                        "[a]", "-r", "90", "-vcodec", "libx264", "-preset", "ultrafast", speedChangedVideo.getAbsolutePath()
                };

                //                String[] complexCommand = {
                //                        "-y", "-i", filepath, "-preset", "ultrafast", "-crf", "29", "-filter_complex",
                //                        "[0:v]setpts=0.33*PTS[v];[0:a]atempo=sqrt(3),atempo=sqrt(3) [a]", "-map", "[v]", "-map",
                //                        "[a]", "-r", "90", speedChangedVideo.getAbsolutePath()
                //                };

                execFFmpegBinary(complexCommand, totalRecordedVideoDuration,
                        speedChangedVideo.getAbsolutePath(), filepath);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void sendVideo() {

        //If we have normal speed video then capturedVideoFile != null, if we have video recorded at different speed than 1X then speedChangedVideo != null
        //  File videoMerge;
        if (speedChangedVideo != null) {

            if (audioSelected) {
                //Audio has been selected for merging

                //                videoMerge = new File(folderPath, System.currentTimeMillis() + appName + ".mp4");
                //                if (!videoMerge.exists()) {
                //                    try {
                //                        videoMerge.createNewFile();
                //                    } catch (IOException e) {
                //                        e.printStackTrace();
                //                    }
                //                }

                //                ResultHolder.setVideo(videoMerge);
                chat.hola.com.app.DublyCamera.ResultHolder.setCall(requestType);
                //ResultHolder.setPath(videoMerge.getAbsolutePath());
                chat.hola.com.app.DublyCamera.ResultHolder.setType("video");

                Intent intent = new Intent(CameraActivity.this, PreviewVideoActivity.class);
                intent.putExtra("videoArray", recordedVideosList);
                intent.putExtra("videoWidth", videoWidth);
                intent.putExtra("videoHeight", videoHeight);
                intent.putExtra("durationArray", recordedVideoDurations);

                intent.putExtra("maximumDuration", (selectedAudioDurationInSecs));
                intent.putExtra("audio", audioFile);
                if (!musicId.isEmpty()) {
                    //intent.putExtra("audio", audioFile);
                    intent.putExtra("musicId", musicId);
                    intent.putExtra("time", selectedAudioDurationInSecs);
                }
                intent.putExtra("isFrontFace", isFrontFace);
                startActivity(intent);

                if (requestType.equals("story")) finish();
            } else {

                //                videoMerge = new File(folderPath, System.currentTimeMillis() + appName + ".mp4");
                //                if (!videoMerge.exists()) {
                //                    try {
                //                        videoMerge.createNewFile();
                //                    } catch (IOException e) {
                //                        e.printStackTrace();
                //                    }
                //                }

                //                ResultHolder.setVideo(videoMerge);
                chat.hola.com.app.DublyCamera.ResultHolder.setCall(requestType);
                //ResultHolder.setPath(videoMerge.getAbsolutePath());
                chat.hola.com.app.DublyCamera.ResultHolder.setType("video");

                Intent intent = new Intent(CameraActivity.this, PreviewVideoActivity.class);
                intent.putExtra("videoArray", recordedVideosList);
                intent.putExtra("videoWidth", videoWidth);
                intent.putExtra("videoHeight", videoHeight);
                intent.putExtra("durationArray", recordedVideoDurations);
                intent.putExtra("maximumDuration", MAXIMUM_RECORDING_LENGTH_ALLOWED);
                //intent.putExtra("video", videoMerge.getAbsolutePath());
                intent.putExtra("isFrontFace", isFrontFace);

                startActivity(intent);
                if (requestType.equals("story")) finish();
            }
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
            TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                supportFinishAfterTransition();
            }
        }, 500);
    }

    private void requestCameraPermission() {

        ArrayList<String> permissionsRequired = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            permissionsRequired.add(Manifest.permission.CAMERA);
        }
        if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            permissionsRequired.add(Manifest.permission.RECORD_AUDIO);
        }
        if (ActivityCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            permissionsRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            permissionsRequired.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        noPendingPermissionsRequest = false;
        ActivityCompat.requestPermissions(this,
                permissionsRequired.toArray(new String[permissionsRequired.size()]),
                CAMERA_PERMISSIONS_REQ_CODE);
    }

    public void launchImagePicker() {

        //        Intent intent = new Intent(Intent.ACTION_PICK);
        //
        //        intent.setType(requestType.equals("SaveProfile") ? "image/*" : "video/*, image/*");

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType(requestType.equals("SaveProfile") ? "image/*" : "image/*,video/*");

        if (!requestType.equals("SaveProfile")) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
        }

        startActivityForResult(intent, RESULT_LOAD_IMAGE);

        //        Intent intent = new Intent();
        //
        //        intent.setType("image/*");
        //
        //        String chooseTitle = "select Image";
        //
        //        intent.setAction(Intent.ACTION_GET_CONTENT);
        //        startActivityForResult(Intent.createChooser(intent, chooseTitle), RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE) {
            sendMedia(data);
        } else if (requestCode == 111) {
            Intent intent = new Intent();
            intent.putExtra("profilePic", data.getStringExtra("profilePic"));
            setResult(RESULT_OK, intent);
        } else if (requestCode == SELECT_DUB_SOUND_REQUEST) {

            if (resultCode == Activity.RESULT_OK) {
                try {

                    audioFile = data.getStringExtra("audio");

                    String selectedAudioName = data.getStringExtra("name");
                    musicId = data.getStringExtra("musicId");
                    audioSelected = true;

                    if (audioFile != null) {

                        //                        File f=new File(audioFile);

                        Uri uri = Uri.parse(audioFile);

                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

                        mmr.setDataSource(CameraActivity.this, uri);
                        //                    mmr.setDataSource(getApplicationContext(), uri);
                        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                        selectedAudioDurationInSecs = Integer.parseInt(durationStr) / 1000;
                    }

                    if (selectedAudioName != null) {
                        tvSelectSound.setText(
                                selectedAudioName.replace(".aac", getString(R.string.double_inverted_comma)));
                        tvSelectSound.setSelected(true);
                        setMicMuted(true);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.unable_to_select_audio), Toast.LENGTH_SHORT)
                            .show();
                    e.printStackTrace();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

                Toast.makeText(CameraActivity.this, getString(R.string.dubsound_cancel), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void sendMedia(Intent data) {
        try {
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            String mime = cr.getType(uri);

            chat.hola.com.app.DublyCamera.ResultHolder.dispose();
            chat.hola.com.app.DublyCamera.ResultHolder.setCall(requestType);

            String mediaPath;//= ImageFilePath.getPath(this, uri);

            //            getDialog.setTitle("Please wait...");
            //            getDialog.show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaPath = ImageFilePath.getPathAboveN(this, uri);
            } else {

                mediaPath = ImageFilePath.getPath(this, uri);
            }
            //            getDialog.dismiss();

            if (mediaPath == null) {

                Toast.makeText(this, getString(R.string.media_selection_failed), Toast.LENGTH_SHORT).show();
            } else {
                chat.hola.com.app.DublyCamera.ResultHolder.setPath(mediaPath);

                if (mime != null && !mime.contains("image")) {
                    //video

                    long duration = getDuration(new File(mediaPath));

                    if (duration > 0 && duration <= (MAXIMUM_RECORDING_LENGTH_ALLOWED * 1000)) {
                        chat.hola.com.app.DublyCamera.ResultHolder.setDuration(duration);
                        chat.hola.com.app.DublyCamera.ResultHolder.setCall(requestType);
                        chat.hola.com.app.DublyCamera.ResultHolder.setType("video");
                        if (((int) (duration / 1000)) > 0) {
                            duration = (int) (duration / 1000);
                        } else {

                            duration = 1;
                        }

                        ArrayList<Integer> recordedVideoDurations = new ArrayList<>();
                        recordedVideoDurations.add((int) duration);

                        ArrayList<String> recordedVideosList = new ArrayList<>();
                        recordedVideosList.add(mediaPath);
                        Intent intent = new Intent(CameraActivity.this, PreviewGalleryVideoActivity.class);
                        intent.putExtra("videoArray", recordedVideosList);
                        intent.putExtra("captured", false);
                        intent.putExtra("durationArray", recordedVideoDurations);
                        intent.putExtra("maximumDuration", (int) duration);

                        intent.putExtra("videoWidth", galleryVideoWidth);
                        intent.putExtra("videoHeight", galleryVideoHeight);

                        startActivity(intent);
                    } else if (duration > (MAXIMUM_RECORDING_LENGTH_ALLOWED * 1000)) {
                        Toast.makeText(this, R.string.video_size_message, Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        Toast.makeText(this, getString(R.string.not_able_to_share_text), Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    //image
                    chat.hola.com.app.DublyCamera.ResultHolder.setType("image");

                    Intent intent = new Intent(this, PreviewGalleryImageActivity.class);
                    intent.putExtra("isFrontFace", isFrontFace);
                    if (requestType.equals("SaveProfile")) {

                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    }
                    startActivity(intent);
                }

                if (requestType.equals("story")) finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getDuration(File file) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, Uri.fromFile(file));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time);

        String metaRotation = retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);
        int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);

        if (rotation == 90 || rotation == 270) {
            galleryVideoWidth = Integer.valueOf(
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            galleryVideoHeight = Integer.valueOf(
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        } else {

            galleryVideoHeight = Integer.valueOf(
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            galleryVideoWidth = Integer.valueOf(
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        }
        //      Bitmap  bmp = retriever.getFrameAtTime();
        //        galleryVideoWidth=bmp.getHeight();
        //        galleryVideoHeight=bmp.getWidth();

        retriever.release();
        return timeInMillisec;
    }

    //    public void face(boolean isFace) {
    //        this.isFrontFace = isFace;
    //    }

    public void videoCaptureAction(boolean dubbingStarted, float durationOfVideoCaptured) {

        try {
            if (dubbingStarted) {
                if (audioSelected) {
                    if (speed == 1) {

                        //0.1X speed selected
                        if (player != null && player.isPlaying()) {
                            player.stop();
                        }
                        player = MediaPlayer.create(CameraActivity.this, Uri.parse(audioFile));
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
                        player = MediaPlayer.create(CameraActivity.this, Uri.parse(audioFile));
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
                        player = MediaPlayer.create(CameraActivity.this, Uri.parse(audioFile));
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
                        player = MediaPlayer.create(CameraActivity.this, Uri.parse(audioFile));
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
                        player = MediaPlayer.create(CameraActivity.this, Uri.parse(audioFile));
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

    public void handleImageCaptured(Bitmap bitmap) {

        final String path = saveCapturedBitmap(bitmap);

        chat.hola.com.app.DublyCamera.ResultHolder.dispose();

        chat.hola.com.app.DublyCamera.ResultHolder.setCall(requestType);
        chat.hola.com.app.DublyCamera.ResultHolder.setType("image");

        chat.hola.com.app.DublyCamera.ResultHolder.setPath(path);

        Intent intent = new Intent(this, PreviewImageActivity.class);
        intent.putExtra("isFrontFace", isFrontFace);
        if (requestType.equals("SaveProfile")) {

            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        }
        startActivity(intent);

        if (requestType.equals("story")) finish();
    }

    public void updateRecordingUI() {

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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
                    View view = new View(CameraActivity.this);
                    view.setBackgroundColor(getResources().getColor(R.color.blue));

                    flowLayout.addView(view, lp);
                }
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
                    View view = new View(CameraActivity.this);
                    view.setBackgroundColor(getResources().getColor(R.color.blue));

                    flowLayout.addView(view, lp);
                }
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
                    View view = new View(CameraActivity.this);
                    view.setBackgroundColor(getResources().getColor(R.color.blue));

                    flowLayout.addView(view, lp);
                }
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
                    View view = new View(CameraActivity.this);
                    view.setBackgroundColor(getResources().getColor(R.color.blue));

                    flowLayout.addView(view, lp);
                }
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(width, progressBarHeight);
                    View view = new View(CameraActivity.this);
                    view.setBackgroundColor(getResources().getColor(R.color.blue));

                    flowLayout.addView(view, lp);
                }
            });
        }
    }

    private void validateCameraPermissions() {

        if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(CameraActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            try {
                setUpCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (audioSelected) {
                if (audioFile != null) {
                    player = MediaPlayer.create(CameraActivity.this, Uri.parse(audioFile));
                    if (player != null) player.setLooping(false);
                }
            }
        } else {
            checkCameraPermissions();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
            int size = grantResults.length;

            boolean allPermissionsGranted = true;
            for (int i = 0; i < size; i++) {

                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

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
            ivGallery.setVisibility(View.VISIBLE);
            tick.setVisibility(View.GONE);
            cross.setVisibility(View.GONE);
        } else {
            flowLayout.setVisibility(View.VISIBLE);
            ivGallery.setVisibility(View.GONE);
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
        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(resId);
            }
        }, 120);
    }

    public void updateProgressBar(boolean start) {

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
                Toast.makeText(CameraActivity.this, getString(R.string.record_small_duration),
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (GPUCameraRecorder != null) {
                        GPUCameraRecorder.stop();
                    }
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    captureButton.setColorFilter(null);

                    rippleCaptureButton.stopRippleAnimation();
                }
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
        captureButton.setColorFilter(
                ContextCompat.getColor(CameraActivity.this, R.color.doodle_color_red),
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

    public void addRecordingStoppedViewInProgressbar() {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(1, progressBarHeight);
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(R.color.whiteOverlay));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flowLayout.addView(view, lp);
                flowLayoutChilds.add(flowLayout.getChildCount() - initialChildCount);
            }
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
        setUpCameraView();
        if (notFirstTime) {

            resetCameraZoom();
        } else {

            notFirstTime = true;
        }
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
                            delayProcessingForFileSaveHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isFinishing() && dialog != null && dialog.isShowing()) {
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
                        runOnUiThread(() -> {

                            if (filtersVisible) {
                                updateFiltersVisibility(true);
                            }
                        });

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

                            runOnUiThread(() -> {
                                setUpCamera();
                            });
                        }
                        toggleCamera = false;
                    }
                })
                //                .videoSize(videoWidth, videoWidth)
                .videoSize(videoWidth, videoHeight).cameraSize(cameraWidth, cameraHeight)
                //                .cameraSize(cameraWidth,cameraWidth)
                .lensFacing(lensFacing)
                //.mute(true)
                .build();
    }

    private void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        if (sampleGLView != null) {
            sampleGLView.queueEvent(() -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                    Bitmap snapshotBitmap =
                            Bitmap.createBitmap(sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(),
                                    Bitmap.Config.ARGB_8888);
                    try {
                        PixelCopy.request(sampleGLView, snapshotBitmap,
                                new PixelCopy.OnPixelCopyFinishedListener() {
                                    @Override
                                    public void onPixelCopyFinished(int i) {
                                        runOnUiThread(() -> {
                                            bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
                                        });
                                    }
                                }, new Handler(Looper.getMainLooper()));
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(CameraActivity.this, getString(R.string.string_17), Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    EGL10 egl = (EGL10) EGLContext.getEGL();

                    GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();

                    Bitmap snapshotBitmap = createBitmapFromGLSurface(sampleGLView.getMeasuredWidth(),
                            sampleGLView.getMeasuredHeight(), gl);

                    runOnUiThread(() -> {
                        bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
                    });
                }
            });
        } else {

            Toast.makeText(CameraActivity.this, getString(R.string.string_17), Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                "Title" + System.currentTimeMillis(), null);

        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private Bitmap createBitmapFromGLSurface(int w, int h, GL10 gl) {

        //        if (i < maxRetries-1) {
        //            i++;
        //
        //            return null;
        //        }

        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
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

    private void updateFiltersVisibility(boolean isFiltersVisible) {
        if (isFiltersVisible) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            this.filtersVisible = false;
        } else {
            if (filtersBottomView.getVisibility() == View.GONE) {
                filtersBottomView.setVisibility(View.VISIBLE);
            }
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            this.filtersVisible = true;
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

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            handleViewTouchFeedback(view, event);

            if (GPUCameraRecorder != null) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        captureButtonView = view;
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        vibrate();

                        if (!onlyPhotoCanBeCaptured) {

                            if (audioSelected) {

                                if (totalCapturedLength < selectedAudioDurationInSecs) {

                                    captureVideoRequested = true;

                                    videoCaptureHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (captureVideoRequested) {
                                                initialCaptureButtonX = view.getX();
                                                initialCaptureButtonY = view.getY();

                                                startVideoCapture();
                                            }
                                        }
                                    }, 250);
                                } else {

                                    Toast.makeText(CameraActivity.this, R.string.dubbing_max_size, Toast.LENGTH_SHORT)
                                            .show();
                                }
                            } else {

                                if (totalCapturedLength < MAXIMUM_RECORDING_LENGTH_ALLOWED) {

                                    captureVideoRequested = true;

                                    videoCaptureHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (captureVideoRequested) {
                                                initialCaptureButtonX = view.getX();
                                                initialCaptureButtonY = view.getY();

                                                startVideoCapture();
                                            }
                                        }
                                    }, 250);
                                } else {

                                    Toast.makeText(CameraActivity.this, R.string.recording_max_size,
                                            Toast.LENGTH_SHORT).show();
                                }
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
                        } else {

                            //Request image capture

                            if (!captureImageDisabled) {
                                captureImageDisabled = true;
                                if (filtersVisible) {
                                    updateFiltersVisibility(true);
                                }

                                dialog.setMessage(getString(R.string.capturing_image));
                                if (!isFinishing()) dialog.show();

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

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    imageCaptureTimerTask.cancel();

                                                    captureImageDisabled = false;
                                                    if (!isFinishing()
                                                        && dialog != null
                                                        && dialog.isShowing()) {
                                                        dialog.cancel();
                                                    }
                                                    Toast.makeText(CameraActivity.this,
                                                            getString(R.string.image_capture_failed), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                        alreadyTriedTimes++;
                                    }
                                };
                                new Timer().schedule(imageCaptureTimerTask, 0, 1000);
                            }
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
            } else {

                Toast.makeText(CameraActivity.this, getString(R.string.preparing_camera),
                        Toast.LENGTH_SHORT).show();
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

        if (position == 5 || position == 19 | position == 35) {
            updatePixelComparisonLimitRequired = true;
        } else {

            updatePixelComparisonLimitRequired = false;
        }
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
        } catch (QtFastStart.MalformedFileException e1) {
            e1.printStackTrace();
        } catch (QtFastStart.UnsupportedFileException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }

        return path;
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

            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(),
                    file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
                    if (!isFinishing() && dialog != null && dialog.isShowing()) {
                        dialog.cancel();
                    }
                    handleImageCaptured(bitmap);
                });
            }

            bitmapCaptureAlreadyRunning = false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("killCameraActivity")) {
                //     super.onBackPressed();

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
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
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
                } catch (NullPointerException e) {
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

            runOnUiThread(new Runnable() {
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

    //livestream
    @OnClick(R.id.btnLiveStream)
    public void liveStream() {
        tvTitle.setText(getString(R.string.checking_internet));
        tvMessage.setText(getString(R.string.check_internet_message));
        presenter.startLiveBroadcastApi("", streamType, thumbnailUrl, streamName);
    }

    @Override
    public void onSuccess(String streamId) {
        hideAlert();
        //Stream prepared successfully
        Intent intent = new Intent(this, WebRTCStreamBroadcasterActivity.class);
        intent.putExtra("StreamName", streamName);
        intent.putExtra("StreamType", streamType);
        intent.putExtra("StreamId", streamId);
        intent.putExtra("ThumbNail", thumbnailUrl);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        finish();
    }

    @Override
    public void showAlert(String message) {
        //        dialog = alertProgress.getProgressDialog(this, message);
        //        dialog.show();
    }

    @Override
    public void onError(String message) {
        tvTitle.setText(getString(R.string.oops_wrong));
        tvMessage.setText(getString(R.string.check_internet));
    }

    public void hideAlert() {
        tvTitle.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
    }

    @OnClick(R.id.btnSnap)
    public void snap() {
        captureBitmap(bitmap -> {

            if (bitmap != null) {
                new Handler().post(() -> {
                    btnSnap.setEnabled(false);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    final String path = saveCapturedBitmap(bitmap);
                    cloudinaryUpload(path);

                });
            }
        });
    }

    public void cloudinaryUpload(String path) {
        MediaManager.get()
                .upload(path)
                .option(Constants.Post.FOLDER, Constants.Cloudinary.livestream)
                .option(Constants.Post.RESOURCE_TYPE, Constants.Post.IMAGE)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        imageUrl = (String) resultData.get(Constants.Post.URL);
                        thumbnailUrl = imageUrl;
                        tvTitle.setText(getString(R.string.live_on_app));
                        tvMessage.setText(getString(R.string.we_will_notify));
                        btnSnap.setVisibility(View.GONE);


                        /*
                         * BugId: DUBAND076
                         * Bug Title: Live stream-> The cover photo for the live streaming captures but it is not displayed appropriately during capture and not showing as cover photo (please refer ios)
                         * Fix Description: Added Imageview and showing after uploading to the cloudinary
                         * Developer Name: Shaktisinh
                         * Fix Date: 15/4/2021
                         * */
                        Glide.with(CameraActivity.this).load(thumbnailUrl).asBitmap().into(ivLiveStreamCover);
                        ivLiveStreamCover.setVisibility(View.VISIBLE);

                        btnLiveStream.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.d("post", "cloudinary onError: ");
                        Log.i("snap", "8");
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .constrain(TimeWindow.immediate())
                .dispatch();
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
        if (filtersVisible) {

            filtersBottomView.setVisibility(View.GONE);
            updateFiltersVisibility(true);
        }

        showCaptureDialogView(mCountDownDialog, mCountDownView);
        initCountDownDurationAdjustView();
    }

    private void setupTimerView() {
        if (!onlyPhotoCanBeCaptured) {
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

                        Toast.makeText(CameraActivity.this, R.string.dubbing_max_size, Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {

                    if (totalCapturedLength < MAXIMUM_RECORDING_LENGTH_ALLOWED) {

                        mCountDownTextView.setVisibility(View.VISIBLE);
                        timerStarted = true;
                        mCountDownTextView.downSecond(2);
                        captureVideoRequested = true;
                    } else {

                        Toast.makeText(CameraActivity.this, R.string.recording_max_size, Toast.LENGTH_SHORT)
                                .show();
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
        } else {
            llTimer.setVisibility(View.GONE);
        }
    }

    private void initCountDownDialog() {
        mCountDownDialog = new android.app.AlertDialog.Builder(this).create();
        mCountDownDialog.setOnCancelListener(dialog -> closeCaptureDialogView(mCountDownDialog, true));
    }

    /*
     * Show window
     * */
    private void showCaptureDialogView(android.app.AlertDialog dialog, View view) {
        TranslateAnimation translate =
            new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);

        translate.setDuration(200);
        translate.setFillAfter(false);
        rlCameraControls.startAnimation(translate);
        if (!isFinishing()) {
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
            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.transparent));
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
                runOnUiThread(() -> {
                    rlTimer.setVisibility(View.GONE);
                    rippleTimerButton.stopRippleAnimation();
                    rlCameraControls.setVisibility(View.VISIBLE);
                });
            } else {
                rlTimer.setVisibility(View.GONE);
                rippleTimerButton.stopRippleAnimation();
                rlCameraControls.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.ivZoomOut})
    public void zoomOut() {

        try {

            if (GPUCameraRecorder != null) {
                GPUCameraRecorder.zoomOut(true, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ivZoomIn})
    public void zoomIn() {

        try {

            if (GPUCameraRecorder != null) {
                GPUCameraRecorder.zoomOut(false, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
