package chat.hola.com.app.cameraActivities.ui.view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Doodle.DrawViewOnImage;
import chat.hola.com.app.Utilities.OnSwipeTouchListener;
import chat.hola.com.app.Utilities.RingProgressBar;
import chat.hola.com.app.cameraActivities.TextEditingListener;
import chat.hola.com.app.cameraActivities.configuration.CameraConfiguration;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.cameraActivities.utils.DateTimeUtils;
import chat.hola.com.app.cameraActivities.utils.SandriosBus;
import chat.hola.com.app.motionView.filters.helperClasses.GenerateThumbnails;
import chat.hola.com.app.motionView.filters.modelClasses.Filter_item;
import chat.hola.com.app.motionView.motionviews.ui.StickerSelectActivity;
import chat.hola.com.app.motionView.motionviews.ui.adapter.FontsAdapter;
import chat.hola.com.app.motionView.motionviews.utils.FontProvider;
import chat.hola.com.app.motionView.motionviews.viewmodel.Font;
import chat.hola.com.app.motionView.motionviews.viewmodel.Layer;
import chat.hola.com.app.motionView.motionviews.viewmodel.TextLayer;
import chat.hola.com.app.motionView.motionviews.widget.MotionView;
import chat.hola.com.app.motionView.motionviews.widget.entity.ImageEntity;
import chat.hola.com.app.motionView.motionviews.widget.entity.MotionEntity;
import chat.hola.com.app.motionView.motionviews.widget.entity.TextEntity;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

import static chat.hola.com.app.cameraActivities.ui.BaseSandriosActivity.getMimeType;

/**
 * Created by Arpit Gandhi on 7/6/16.
 */
public class CameraControlPanel extends RelativeLayout implements RecordButton.RecordButtonListener,
    MediaActionSwitchView.OnMediaActionStateChangeListener {

  private Context context;

  private CameraSwitchView cameraSwitchView;
  private RecordButton recordButton;
  private MediaActionSwitchView mediaActionSwitchView;
  private FlashSwitchView flashSwitchView;
  private TextView recordDurationText;
  private TextView recordSizeText;
  private ImageButton settingsButton;
  private RecyclerView recyclerView;
  private RingProgressBar videoProgress;

  private ImageGalleryAdapter imageGalleryAdapter;
  private RecordButton.RecordButtonListener recordButtonListener;
  private TextEditingListener textEditingListener;
  private MediaActionSwitchView.OnMediaActionStateChangeListener onMediaActionStateChangeListener;
  private CameraSwitchView.OnCameraTypeChangeListener onCameraTypeChangeListener;
  private FlashSwitchView.FlashModeSwitchListener flashModeSwitchListener;
  private SettingsClickListener settingsClickListener;
  private PickerItemClickListener pickerItemClickListener;

  private TimerTaskBase countDownTimer;
  private long maxVideoFileSize = 0;
  private String mediaFilePath;
  private boolean hasFlash = false;
  private @MediaActionSwitchView.MediaActionState
  int mediaActionState;
  private int mediaAction;
  private boolean showImageCrop = false;
  private FileObserver fileObserver;

  /*  for image editing  */
  private View includeImageEditLayout, swipeForFilters;
  public static final int SELECT_STICKER_REQUEST_CODE = 123;
  private static final String TAG = CameraControlPanel.class.getSimpleName();
  private Activity mActivity;
  private ImageView originalImage;
  private ArrayList<Filter_item> mFilterData = new ArrayList<>();
  private String folderPathh, capturedImagePath;
  private GPUImageView mGPUImageView;
  private int count = 0;
  private File temp;
  private int[] filterString = {
      R.string.text_filter_in1977, R.string.text_filter_amaro, R.string.text_filter_brannan,
      R.string.text_filter_early_bird, R.string.text_filter_hefe, R.string.text_filter_hudson,
      R.string.text_filter_inkwell, R.string.text_filter_lomofi, R.string.text_filter_lord_kelvin,
      R.string.text_filter_early_bird, R.string.text_filter_rise, R.string.text_filter_sierra,
      R.string.text_filter_sutro, R.string.text_filter_toaster, R.string.text_filter_valencia,
      R.string.text_filter_walden, R.string.text_filter_xproii
  };
  private Animation anim1, anim2, anim3, anim4;
  private LinearLayout selectColour;
  private DrawViewOnImage drawView;
  private RelativeLayout doodleView;
  private ImageView redButton, blackButton, greenButton, blueButton, addStickers, drawDoodle,
      ivUndo;
  private TextView addText;
  private boolean isUndoActive, isStickerActive, isTextActive, isDoodleActive;

  protected MotionView motionView;
  protected View textEntityEditPanel;
  private FontProvider fontProvider;

  private final MotionView.MotionViewCallback motionViewCallback =
      new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
          if (entity instanceof TextEntity) {
            textEntityEditPanel.setVisibility(View.VISIBLE);
          } else {
            textEntityEditPanel.setVisibility(View.GONE);
          }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
          startTextEntityEditing(currentTextEntity());
        }
      };


  /*   -----------------   */

  public CameraControlPanel(Context context) {
    this(context, null);
  }

  public CameraControlPanel(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    mActivity = (Activity) context;

    init();
  }

  private void init() {
    hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

    LayoutInflater.from(context).inflate(R.layout.camera_control_panel_layout, this);
    setBackgroundColor(Color.TRANSPARENT);
    includeImageEditLayout = (View) findViewById(R.id.image_edit_layout);
    settingsButton = (ImageButton) findViewById(R.id.settings_view);
    cameraSwitchView = (CameraSwitchView) findViewById(R.id.front_back_camera_switcher);
    mediaActionSwitchView = (MediaActionSwitchView) findViewById(R.id.photo_video_camera_switcher);
    recordButton = (RecordButton) findViewById(R.id.record_button);
    flashSwitchView = (FlashSwitchView) findViewById(R.id.flash_switch_view);
    recordDurationText = (TextView) findViewById(R.id.record_duration_text);
    recordSizeText = (TextView) findViewById(R.id.record_size_mb_text);
    videoProgress = (RingProgressBar) findViewById(R.id.video_recording_progress);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    cameraSwitchView.setOnCameraTypeChangeListener(onCameraTypeChangeListener);
    mediaActionSwitchView.setOnMediaActionStateChangeListener(this);

    setOnCameraTypeChangeListener(onCameraTypeChangeListener);
    setOnMediaActionStateChangeListener(onMediaActionStateChangeListener);
    setFlashModeSwitchListener(flashModeSwitchListener);
    setRecordButtonListener(recordButtonListener);
    setTextEditingListener(textEditingListener);

    settingsButton.setImageDrawable(
        ContextCompat.getDrawable(context, R.drawable.ic_settings_white_24dp));
    settingsButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (settingsClickListener != null) settingsClickListener.onSettingsClick();
      }
    });

      if (hasFlash) {
          flashSwitchView.setVisibility(VISIBLE);
      } else {
          flashSwitchView.setVisibility(GONE);
      }

    countDownTimer = new TimerTask(recordDurationText);
  }

  public void postInit(int mediatype) {
      if (mediatype != 0 && mediatype == CameraConfiguration.VIDEO) {
          imageGalleryAdapter = new ImageGalleryAdapter(context, CameraConfiguration.VIDEO);
      } else {
          imageGalleryAdapter = new ImageGalleryAdapter(context);
      }
    recyclerView.setAdapter(imageGalleryAdapter);
    imageGalleryAdapter.setOnItemClickListener(new ImageGalleryAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(View view, int position) {
        pickerItemClickListener.onItemClick(imageGalleryAdapter.getItem(position).getImageUri());
      }
    });
  }

  public void lockControls() {
    cameraSwitchView.setEnabled(false);
    recordButton.setEnabled(false);
    settingsButton.setEnabled(false);
    flashSwitchView.setEnabled(false);
  }

  public void unLockControls() {
    cameraSwitchView.setEnabled(true);
    recordButton.setEnabled(true);
    settingsButton.setEnabled(true);
    flashSwitchView.setEnabled(true);
  }

  public void setup(int mediaAction) {
    this.mediaAction = mediaAction;
    if (CameraConfiguration.MEDIA_ACTION_VIDEO == mediaAction) {
      recordButton.setup(mediaAction, this);
      flashSwitchView.setVisibility(GONE);
    } else {
      recordButton.setup(CameraConfiguration.MEDIA_ACTION_PHOTO, this);
    }

        /*
        if (CameraConfiguration.MEDIA_ACTION_BOTH != mediaAction) {
            mediaActionSwitchView.setVisibility(GONE);
        } else mediaActionSwitchView.setVisibility(VISIBLE);
        */
  }

  public void setMediaFilePath(final File mediaFile) {
    this.mediaFilePath = mediaFile.toString();
  }

  public void setMaxVideoFileSize(long maxVideoFileSize) {
    this.maxVideoFileSize = maxVideoFileSize;
  }

  public void setMaxVideoDuration(int maxVideoDurationInMillis) {
      if (maxVideoDurationInMillis > 0) {
          countDownTimer = new CountdownTask(recordDurationText, maxVideoDurationInMillis);
      } else {
          countDownTimer = new TimerTask(recordDurationText);
      }
  }

  public void setFlasMode(@FlashSwitchView.FlashMode int flashMode) {
    flashSwitchView.setFlashMode(flashMode);
  }

  public void setMediaActionState(@MediaActionSwitchView.MediaActionState int actionState) {
    if (mediaActionState == actionState) return;
    if (MediaActionSwitchView.ACTION_PHOTO == actionState) {
      recordButton.setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO);
      if (hasFlash) flashSwitchView.setVisibility(VISIBLE);
    } else {
      recordButton.setMediaAction(CameraConfiguration.MEDIA_ACTION_VIDEO);
      flashSwitchView.setVisibility(GONE);
    }
    mediaActionState = actionState;
    mediaActionSwitchView.setMediaActionState(actionState);
  }

  public void setRecordButtonListener(RecordButton.RecordButtonListener recordButtonListener) {
    this.recordButtonListener = recordButtonListener;
  }

  public void setTextEditingListener(TextEditingListener textEditingListener) {
    this.textEditingListener = textEditingListener;
  }

  public void rotateControls(int rotation) {
    cameraSwitchView.setRotation(rotation);
    mediaActionSwitchView.setRotation(rotation);
    flashSwitchView.setRotation(rotation);
    recordDurationText.setRotation(rotation);
    recordSizeText.setRotation(rotation);
  }

  public void setOnMediaActionStateChangeListener(
      MediaActionSwitchView.OnMediaActionStateChangeListener onMediaActionStateChangeListener) {
    this.onMediaActionStateChangeListener = onMediaActionStateChangeListener;
  }

  public void setOnCameraTypeChangeListener(
      CameraSwitchView.OnCameraTypeChangeListener onCameraTypeChangeListener) {
    this.onCameraTypeChangeListener = onCameraTypeChangeListener;
      if (cameraSwitchView != null) {
          cameraSwitchView.setOnCameraTypeChangeListener(this.onCameraTypeChangeListener);
      }
  }

  public void setFlashModeSwitchListener(
      FlashSwitchView.FlashModeSwitchListener flashModeSwitchListener) {
    this.flashModeSwitchListener = flashModeSwitchListener;
      if (flashSwitchView != null) {
          flashSwitchView.setFlashSwitchListener(this.flashModeSwitchListener);
      }
  }

  public void setSettingsClickListener(SettingsClickListener settingsClickListener) {
    this.settingsClickListener = settingsClickListener;
  }

  public void setPickerItemClickListener(PickerItemClickListener pickerItemClickListener) {
    this.pickerItemClickListener = pickerItemClickListener;
  }

  @Override
  public void onTakePhotoButtonPressed() {
    if (recordButtonListener != null) recordButtonListener.onTakePhotoButtonPressed();
  }

  public void onStartVideoRecord(final File mediaFile) {
    setMediaFilePath(mediaFile);
    if (maxVideoFileSize > 0) {
      //            recordSizeText.setText("1Mb" + " / " + maxVideoFileSize / (1024 * 1024) + "Mb");
      //            recordSizeText.setVisibility(VISIBLE);
      videoProgress.setVisibility(VISIBLE);
      flashSwitchView.setVisibility(GONE);
      try {
        fileObserver = new FileObserver(this.mediaFilePath) {
          private long lastUpdateSize = 0;

          @Override
          public void onEvent(int event, String path) {
            final long fileSize = mediaFile.length() / (1024 * 1024);
            if ((fileSize - lastUpdateSize) >= 1) {
              lastUpdateSize = fileSize;
              recordSizeText.post(new Runnable() {
                @Override
                public void run() {

                  videoProgress.setProgress(
                      (int) ((fileSize * 100) / (maxVideoFileSize / (1024 * 1024))));
                }
              });
            }
          }
        };
        fileObserver.startWatching();
      } catch (Exception e) {
        Log.e("FileObserver", "setMediaFilePath: ", e);
      }
    }
    countDownTimer.start();
  }

  public void allowRecord(boolean isAllowed) {
    recordButton.setEnabled(isAllowed);
  }

  public void showPicker(boolean isShown) {
    recyclerView.setVisibility(isShown ? VISIBLE : GONE);
  }

  public boolean showCrop() {
    return showImageCrop;
  }

  public void shouldShowCrop(boolean showImageCrop) {
    this.showImageCrop = showImageCrop;
  }

  public void allowCameraSwitching(boolean isAllowed) {
    cameraSwitchView.setVisibility(isAllowed ? VISIBLE : GONE);
  }

  public void onStopVideoRecord() {
    if (fileObserver != null) fileObserver.stopWatching();
    countDownTimer.stop();
      /*  recyclerView.setVisibility(VISIBLE);
        recordSizeText.setVisibility(GONE);
        flashSwitchView.setVisibility(VISIBLE);
        cameraSwitchView.setVisibility(View.VISIBLE);
        settingsButton.setVisibility(VISIBLE);

        if (CameraConfiguration.MEDIA_ACTION_BOTH != mediaAction) {
            mediaActionSwitchView.setVisibility(GONE);
        } else mediaActionSwitchView.setVisibility(VISIBLE);*/
    recordButton.setRecordState(RecordButton.READY_FOR_RECORD_STATE);
  }

  @Override
  public void onStartRecordingButtonPressed() {
    cameraSwitchView.setVisibility(View.GONE);
    mediaActionSwitchView.setVisibility(GONE);
    settingsButton.setVisibility(GONE);
    recyclerView.setVisibility(GONE);

    if (recordButtonListener != null) recordButtonListener.onStartRecordingButtonPressed();
  }

  @Override
  public void onStopRecordingButtonPressed() {
    onStopVideoRecord();
    if (recordButtonListener != null) recordButtonListener.onStopRecordingButtonPressed();
  }

  @Override
  public void onMediaActionChanged(int mediaActionState) {
    setMediaActionState(mediaActionState);
      if (onMediaActionStateChangeListener != null) {
          onMediaActionStateChangeListener.onMediaActionChanged(this.mediaActionState);
      }
  }

  public void setMediaType(int type) {
  }

  public void startRecording() {
    recordButton.performClick();
  }

  public interface SettingsClickListener {
    void onSettingsClick();
  }

  public interface PickerItemClickListener {
    void onItemClick(Uri filePath);
  }

  abstract class TimerTaskBase {
    Handler handler = new Handler(Looper.getMainLooper());
    TextView timerView;
    boolean alive = false;
    long recordingTimeSeconds = 0;
    long recordingTimeMinutes = 0;

    TimerTaskBase(TextView timerView) {
      this.timerView = timerView;
    }

    abstract void stop();

    abstract void start();
  }

  private class CountdownTask extends TimerTaskBase implements Runnable {

    private int maxDurationMilliseconds = 0;

    CountdownTask(TextView timerView, int maxDurationMilliseconds) {
      super(timerView);
      this.maxDurationMilliseconds = maxDurationMilliseconds;
    }

    @Override
    public void run() {

      recordingTimeSeconds--;

      int millis = (int) recordingTimeSeconds * 1000;

      timerView.setText(
          String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
              TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                  TimeUnit.MILLISECONDS.toMinutes(millis))));

      if (recordingTimeSeconds < 10) {
        timerView.setTextColor(Color.RED);
      }

      if (alive && recordingTimeSeconds > 0) handler.postDelayed(this, DateTimeUtils.SECOND);
    }

    @Override
    void stop() {
      timerView.setVisibility(View.INVISIBLE);
      alive = false;
    }

    @Override
    void start() {
      alive = true;
      recordingTimeSeconds = maxDurationMilliseconds / 1000;
      timerView.setTextColor(Color.WHITE);
      timerView.setText(String.format(Locale.ENGLISH, "%02d:%02d",
          TimeUnit.MILLISECONDS.toMinutes(maxDurationMilliseconds),
          TimeUnit.MILLISECONDS.toSeconds(maxDurationMilliseconds) - TimeUnit.MINUTES.toSeconds(
              TimeUnit.MILLISECONDS.toMinutes(maxDurationMilliseconds))));
      timerView.setVisibility(View.VISIBLE);
      handler.postDelayed(this, DateTimeUtils.SECOND);
    }
  }

  private class TimerTask extends TimerTaskBase implements Runnable {

    TimerTask(TextView timerView) {
      super(timerView);
    }

    @Override
    public void run() {
      recordingTimeSeconds++;

      if (recordingTimeSeconds == 60) {
        recordingTimeSeconds = 0;
        recordingTimeMinutes++;
      }
      timerView.setText(
          String.format(Locale.ENGLISH, "%02d:%02d", recordingTimeMinutes, recordingTimeSeconds));
      if (alive) handler.postDelayed(this, DateTimeUtils.SECOND);
    }

    public void start() {
      alive = true;
      recordingTimeMinutes = 0;
      recordingTimeSeconds = 0;
      timerView.setText(
          String.format(Locale.ENGLISH, "%02d:%02d", recordingTimeMinutes, recordingTimeSeconds));
      timerView.setVisibility(View.VISIBLE);
      handler.postDelayed(this, DateTimeUtils.SECOND);
    }

    public void stop() {
      timerView.setVisibility(View.INVISIBLE);
      alive = false;
    }
  }

  public void openImageEditting(Context context, int mediaAction, String path) {
    mActivity = (Activity) context;
    includeImageEditLayout.setVisibility(VISIBLE);

    createEditingLayout(mediaAction, path);
  }

  private void createEditingLayout(int mediaAction, String path) {

    this.fontProvider = new FontProvider(getResources());

    capturedImagePath = path;

    originalImage = (ImageView) includeImageEditLayout.findViewById(R.id.iv_picture);
      /*  File file = new File(capturedImagePath);
        Bitmap imageBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        originalImage.setImageBitmap(imageBitmap);
        */
    //        originalImage.setImageURI(Uri.fromFile(new File(capturedImagePath)));
    //        originalImage.setImageURI(Uri.parse(new File(capturedImagePath).toString()));

    mGPUImageView = (GPUImageView) includeImageEditLayout.findViewById(R.id.iv_picture_filter);
    if (capturedImagePath != null) temp = new File(capturedImagePath);
    mGPUImageView.setImage(temp);

    motionView = (MotionView) includeImageEditLayout.findViewById(R.id.main_motion_view);

    textEntityEditPanel =
        includeImageEditLayout.findViewById(R.id.main_motion_text_entity_edit_panel);
    motionView.setMotionViewCallback(motionViewCallback);

    initTextEntitiesListenersAndOtherViews();

    initializeDoodleLayout();

    initializeColourSelectionView();

    initializeViewForFilters();
  }

  private void initializeViewForFilters() {
    swipeForFilters = (View) includeImageEditLayout.findViewById(R.id.swipe_for_filter);
    swipeForFilters.setVisibility(VISIBLE);
    swipeForFilters.setOnTouchListener(new OnSwipeTouchListener(mActivity) {
      public void onSwipeTop() {

        if (count != 0) {
          count = 0;
          switchFilterTo();
        }
      }

      public void onSwipeRight() {

        if (++count > 15) {
          count = 0;
        }
        switchFilterTo();
      }

      public void onSwipeLeft() {

        if (--count < 0) {
          count = 15;
        }

        switchFilterTo();
      }

      public void onSwipeBottom() {
        if (count != 0) {
          count = 0;
          switchFilterTo();
        }
      }
    });
  }

  private void initializeColourSelectionView() {

    //    initialize select colour view at last..

    selectColour = (LinearLayout) includeImageEditLayout.findViewById(R.id.select_colour);

    redButton = (ImageView) includeImageEditLayout.findViewById(R.id.redColour);
    blackButton = (ImageView) includeImageEditLayout.findViewById(R.id.blackColour);
    greenButton = (ImageView) includeImageEditLayout.findViewById(R.id.greenColour);
    blueButton = (ImageView) includeImageEditLayout.findViewById(R.id.blueColour);

    blackButton.setSelected(true);
    redButton.setSelected(false);
    greenButton.setSelected(false);
    blueButton.setSelected(false);
    redButton.setClickable(true);
    blackButton.setClickable(true);
    greenButton.setClickable(true);
    blueButton.setClickable(true);
    blackButton.setSelected(true);

    redButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {



        blackButton.setSelected(false);
        redButton.setSelected(true);
        greenButton.setSelected(false);
        blueButton.setSelected(false);
        drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_red));

        setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_red));
      }
    });

    blackButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {

        blackButton.setSelected(true);
        redButton.setSelected(false);
        greenButton.setSelected(false);
        blueButton.setSelected(false);
        drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
        setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
      }
    });

    blueButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        //                System.out.println("keyBoardHeight009: blueButton");
        blackButton.setSelected(false);
        redButton.setSelected(false);
        greenButton.setSelected(false);
        blueButton.setSelected(true);
        drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_blue));
        setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_blue));
      }
    });

    greenButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        //                System.out.println("keyBoardHeight009: greenButton");
        blackButton.setSelected(false);
        redButton.setSelected(false);
        greenButton.setSelected(true);
        blueButton.setSelected(false);
        drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_green));
        setTextColour(ContextCompat.getColor(mActivity, R.color.doodle_color_green));
      }
    });
  }

  private void setTextColour(int selectedColor) {
    TextEntity textEntity = currentTextEntity();
    if (textEntity != null) {
      textEntity.getLayer().getFont().setColor(selectedColor);
      textEntity.updateEntity();
      motionView.invalidate();
    }
  }

  private void initializeDoodleLayout() {

    doodleView = (RelativeLayout) includeImageEditLayout.findViewById(R.id.draw_doodle);
    drawView = new DrawViewOnImage(mActivity);
    doodleView.addView(drawView);
  }

  private void hideDoodleView(boolean addDoodleToImage) {

    if (doodleView.getVisibility() == View.VISIBLE) {

      if (addDoodleToImage) {
        addDoodle(drawView.getmBitmap());
        //                doodleView.setVisibility(View.GONE);
      }

      doodleView.removeAllViews();
      doodleView.invalidate();
      drawView = new DrawViewOnImage(mActivity);
      doodleView.addView(drawView);
      redButton.setClickable(true);
      blackButton.setClickable(true);
      greenButton.setClickable(true);
      blueButton.setClickable(true);
      blackButton.setSelected(true);
      redButton.setSelected(false);
      greenButton.setSelected(false);
      blueButton.setSelected(false);
      drawView.mPaint.setColor(ContextCompat.getColor(mActivity, R.color.doodle_color_black));
      doodleView.setVisibility(View.GONE);
    }
  }

  private void addDoodle(final Bitmap bitmap) {

    motionView.post(new Runnable() {
      @Override
      public void run() {
        Layer layer = new Layer();

        ImageEntity entity =
            new ImageEntity(layer, bitmap, motionView.getWidth(), motionView.getHeight());

        motionView.addEntity(entity);
        //                motionView.addEntityAndPosition(entity);
      }
    });
  }

  private void addSticker(final int stickerResId) {
    motionView.post(new Runnable() {
      @Override
      public void run() {
        Layer layer = new Layer();
        Bitmap pica = BitmapFactory.decodeResource(getResources(), stickerResId);

        ImageEntity entity =
            new ImageEntity(layer, pica, motionView.getWidth(), motionView.getHeight());

        motionView.addEntityAndPosition(entity);
      }
    });
  }

  private void initTextEntitiesListenersAndOtherViews() {
    includeImageEditLayout.findViewById(R.id.text_entity_font_size_increase)
        .setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            increaseTextEntitySize();
          }
        });
    includeImageEditLayout.findViewById(R.id.text_entity_font_size_decrease)
        .setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            decreaseTextEntitySize();
          }
        });

    includeImageEditLayout.findViewById(R.id.text_entity_font_change)
        .setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            changeTextEntityFont();
          }
        });
    includeImageEditLayout.findViewById(R.id.text_entity_edit)
        .setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
            startTextEntityEditing(currentTextEntity());
          }
        });

    ImageView close = (ImageView) includeImageEditLayout.findViewById(R.id.close);
    close.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackButtonPressed();
      }
    });

    addStickers = (ImageView) includeImageEditLayout.findViewById(R.id.iv_add_sticker);
    addStickers.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        if (isStickerActive) {
          isStickerActive = false;
          //                    addStickers.setColorFilter(ContextCompat.getColor(context, R.color.color_white), android.graphics.PorterDuff.Mode..MULTIPLY);
          addStickers.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          swipeForFilters.setVisibility(VISIBLE);
        } else {
          isStickerActive = true;
          isTextActive = false;
          isDoodleActive = false;
          isUndoActive = false;
          swipeForFilters.setVisibility(GONE);

          hideDoodleView(true);

          addStickers.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary),
              android.graphics.PorterDuff.Mode.SRC_IN);
          drawDoodle.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          addText.setTextColor(ContextCompat.getColor(context, R.color.color_white));
          ivUndo.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);

          selectColour.setVisibility(GONE);
          Intent intent = new Intent(mActivity, StickerSelectActivity.class);
          intent.putExtra("capturedImagePath", capturedImagePath);
          mActivity.startActivityForResult(intent, SELECT_STICKER_REQUEST_CODE);
        }
      }
    });

    drawDoodle = (ImageView) includeImageEditLayout.findViewById(R.id.iv_draw_doodle);
    drawDoodle.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        if (isDoodleActive) {
          isDoodleActive = false;
          drawDoodle.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          selectColour.setVisibility(GONE);
          swipeForFilters.setVisibility(VISIBLE);
        } else {
          isDoodleActive = true;
          isUndoActive = false;
          isTextActive = false;
          isStickerActive = false;
          swipeForFilters.setVisibility(GONE);

          doodleView.setVisibility(View.VISIBLE);
          drawDoodle.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary),
              android.graphics.PorterDuff.Mode.SRC_IN);
          selectColour.setVisibility(VISIBLE);
          addStickers.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          addText.setTextColor(ContextCompat.getColor(context, R.color.color_white));
          ivUndo.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
        }
      }
    });

    ivUndo = (ImageView) includeImageEditLayout.findViewById(R.id.iv_undo);
    ivUndo.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        if (isUndoActive) {
          isUndoActive = false;
          ivUndo.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          swipeForFilters.setVisibility(VISIBLE);
        } else {
          isUndoActive = true;
          isDoodleActive = false;
          isTextActive = false;
          isStickerActive = false;
          swipeForFilters.setVisibility(GONE);

            if (doodleView.getVisibility() == View.VISIBLE) {
                hideDoodleView(false);
            } else {
                undo();
            }
          // set the tint for Vector Drawable
          ivUndo.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary),
              android.graphics.PorterDuff.Mode.SRC_IN);
          selectColour.setVisibility(GONE);
          drawDoodle.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          addText.setTextColor(ContextCompat.getColor(context, R.color.color_white));
          addStickers.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
        }
      }
    });

    final ImageView saveAndSendImage =
        (ImageView) includeImageEditLayout.findViewById(R.id.save_and_send_image);
    saveAndSendImage.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        saveAndSendImage.setVisibility(View.GONE);

        ProgressBar sending =
            (ProgressBar) includeImageEditLayout.findViewById(R.id.pb_sending_image);
        sending.setVisibility(View.VISIBLE);

        hideDoodleView(true);

        saveFilteredImage();
      }
    });

    addText = (TextView) includeImageEditLayout.findViewById(R.id.tv_add_text);
    addText.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {

        if (isTextActive) {
          isTextActive = false;
          addText.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
          selectColour.setVisibility(GONE);
          swipeForFilters.setVisibility(VISIBLE);
        } else {
          isTextActive = true;
          isUndoActive = false;
          isDoodleActive = false;
          isStickerActive = false;
          swipeForFilters.setVisibility(GONE);
          hideDoodleView(true);
          selectColour.setVisibility(VISIBLE);
          addTextSticker();
          addText.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
          drawDoodle.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          addStickers.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
          ivUndo.setColorFilter(ContextCompat.getColor(context, R.color.color_white),
              android.graphics.PorterDuff.Mode.SRC_IN);
        }
      }
    });


    /* filter stuffs  (not much of this is used in this class) */

    count = 0;

    anim1 = new ScaleAnimation(1f, 0.85f, 1f, 0.85f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim1.setFillAfter(true);
    anim1.setDuration(100);

    anim2 = new ScaleAnimation(0.85f, 1f, 0.85f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim2.setFillAfter(true);
    anim2.setDuration(100);

    anim3 = new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim3.setFillAfter(true);
    anim3.setDuration(350);

    anim4 = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f);
    anim4.setFillAfter(true);
    anim4.setDuration(350);
  }

  private void saveFilteredImage() {

    final String tempPath = System.currentTimeMillis() + "filteredImage.jpg";

    mGPUImageView.saveToPictures(folderPathh, tempPath, new GPUImageView.OnPictureSavedListener() {
      @Override
      public void onPictureSaved(final Uri uri) {
        originalImage.setImageURI(uri);
        //                System.out.println("picturepath:saveImage925: " + uri.toString());

        saveImage(motionView);
      }
    });
  }

  private void increaseTextEntitySize() {
    TextEntity textEntity = currentTextEntity();
    if (textEntity != null) {
      textEntity.getLayer().getFont().increaseSize(TextLayer.Limits.FONT_SIZE_STEP);
      textEntity.updateEntity();
      motionView.invalidate();
    }
  }

  private void decreaseTextEntitySize() {
    TextEntity textEntity = currentTextEntity();
    if (textEntity != null) {
      textEntity.getLayer().getFont().decreaseSize(TextLayer.Limits.FONT_SIZE_STEP);
      textEntity.updateEntity();
      motionView.invalidate();
    }
  }

  private void changeTextEntityFont() {
    final List<String> fonts = fontProvider.getFontNames();
    FontsAdapter fontsAdapter = new FontsAdapter(mActivity, fonts, fontProvider);
    new AlertDialog.Builder(mActivity).setTitle(R.string.select_font)
        .setAdapter(fontsAdapter, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int which) {
            TextEntity textEntity = currentTextEntity();
            if (textEntity != null) {
              textEntity.getLayer().getFont().setTypeface(fonts.get(which));
              textEntity.updateEntity();
              motionView.invalidate();
            }
          }
        })
        .show();
  }

  private void startTextEntityEditing(TextEntity textEntity) {

    if (textEditingListener != null) {
      textEditingListener.startEditingTextEntity(textEntity);
    }
  }

  @Nullable
  private TextEntity currentTextEntity() {
    if (motionView != null && motionView.getSelectedEntity() instanceof TextEntity) {
      return ((TextEntity) motionView.getSelectedEntity());
    } else {
      return null;
    }
  }

  protected void addTextSticker() {
    TextLayer textLayer = createTextLayer();
    TextEntity textEntity =
        new TextEntity(textLayer, motionView.getWidth(), motionView.getHeight(), fontProvider);

    motionView.addEntityAndPosition(textEntity);

    // move text sticker up so that its not hidden under keyboard
    PointF center = textEntity.absoluteCenter();
    center.y = center.y * 0.5F;
    textEntity.moveCenterTo(center);

    // redraw
    motionView.invalidate();

    startTextEntityEditing(currentTextEntity());
  }

  private TextLayer createTextLayer() {
    TextLayer textLayer = new TextLayer();
    Font font = new Font();

    font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
    font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
    font.setTypeface(fontProvider.getDefaultFontName());

    textLayer.setFont(font);

    if (BuildConfig.DEBUG) {
      textLayer.setText(R.string.hello +R.string.comma+ getResources().getString(R.string.app_name) + R.string.cont_dot);
    }

    return textLayer;
  }

  public void onStickerSelectActivityResult(Intent data) {
    if (data != null) {
      int stickerId = data.getIntExtra(StickerSelectActivity.EXTRA_STICKER_ID, 0);
      if (stickerId != 0) {
        addSticker(stickerId);
      }
    }
  }

  public void onTextChanged(String text) {
    TextEntity textEntity = currentTextEntity();
    if (textEntity != null) {
      TextLayer textLayer = textEntity.getLayer();
      if (!text.equals(textLayer.getText())) {
        textLayer.setText(text);
        textEntity.updateEntity();
        motionView.invalidate();
      }
    }
  }

  public void onBackButtonPressed() {
    //        mActivity.finish();
    mActivity.onBackPressed();
  }

  public Bitmap getBitmapFromMV(MotionView motionView) {

    int size_x = motionView.getWidth();
    int size_y = motionView.getHeight();
    Bitmap.Config conf = Bitmap.Config.ARGB_8888;

    Bitmap bmp = Bitmap.createBitmap(size_x, size_y, conf);

    Canvas final_cnv = new Canvas(bmp);
    originalImage.draw(final_cnv);
    List<MotionEntity> mentities = motionView.getEntities();
    for (int i = 0; i < mentities.size(); i++) {
      mentities.get(i).draw(final_cnv, new Paint());
    }

    return bmp;
  }

  private void saveImage(MotionView motionView) {

    File imageFile;
    final String SavedImagePath;

    try {

      Bitmap imageBitmap = getBitmapFromMV(motionView);

      //            System.out.println("cameraBitmap:picturePath: " + imageBitmap.toString());

      final File imageFolder;
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        imageFolder = new File(context.getExternalFilesDir(null)
            + "/"
            + getResources().getString(R.string.app_name)
            + "/Media/");
      } else {

        imageFolder = new File(mActivity.getFilesDir()
            + "/"
            + getResources().getString(R.string.app_name)
            + "/Media/");
      }

      boolean success = true;
      if (!imageFolder.exists() && !imageFolder.isDirectory()) success = imageFolder.mkdirs();

      if (success) {
        SavedImagePath = imageFolder.getAbsolutePath()
            + File.separator
            + String.valueOf(System.nanoTime())
            + "Image.jpg";

        imageFile = new File(SavedImagePath);
      } else {
        Toast.makeText(mActivity, "Image Not saved", Toast.LENGTH_SHORT).show();
        return;
      }

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      // Save image into gallery
      if (imageBitmap != null) {
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
      }

      FileOutputStream file_out = new FileOutputStream(imageFile);
      file_out.write(outputStream.toByteArray());
      file_out.close();
      ContentValues values = new ContentValues();

      values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
      values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
      values.put(MediaStore.MediaColumns.DATA, imageFile.getAbsolutePath());

      mActivity.getApplicationContext()
          .getContentResolver()
          .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

      //            System.out.println(TAG + " " + "picturePath= " + SavedImagePath);

/*
            Intent intent = new Intent();
            intent.putExtra("imagePath", SavedImagePath);
            setResult(RESULT_OK, intent);
            finish();
*/

      int mimeType = getMimeType(mActivity, SavedImagePath);
      SandriosBus.getBus().send(new CameraOutputModel(mimeType, SavedImagePath));
      mActivity.finish();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void switchFilterTo() {

    GPUImageFilter mFilter = GenerateThumbnails.createFilterForType(mActivity,
        GenerateThumbnails.FilterType.values()[count]);
    mGPUImageView.setFilter(mFilter);
    mGPUImageView.requestRender();
  }

  private void undo() {
    List<MotionEntity> mentities = motionView.getEntities();

    int entitySize = mentities.size();
    if (entitySize > 0) {

      try {

        MotionEntity entity = motionView.getSelectedEntity();

        if (entity != null) {

          motionView.unselectEntity();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      motionView.removeEntity(mentities.get(entitySize - 1));
    }
  }
}
