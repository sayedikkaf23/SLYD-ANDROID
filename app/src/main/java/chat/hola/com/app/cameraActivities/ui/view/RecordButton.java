package chat.hola.com.app.cameraActivities.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import chat.hola.com.app.cameraActivities.configuration.CameraConfiguration;
import chat.hola.com.app.cameraActivities.utils.Utils;
import com.ezcall.android.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Arpit Gandhi on 7/6/16.
 */
public class RecordButton extends ImageButton {

  public static final int TAKE_PHOTO_STATE = 0;
  public static final int READY_FOR_RECORD_STATE = 1;
  public static final int RECORD_IN_PROGRESS_STATE = 2;
  private Context context;
  private int mediaAction = CameraConfiguration.MEDIA_ACTION_PHOTO;
  private @RecordState
  int currentState = TAKE_PHOTO_STATE;
  private Drawable takePhotoDrawable;
  private Drawable startRecordDrawable;
  private Drawable stopRecordDrawable;
  private int iconPadding = 8;
  private int iconPaddingStop = 18;
  private RecordButtonListener listener;

  public RecordButton(@NonNull Context context) {
    this(context, null, 0);
  }

  public RecordButton(@NonNull Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RecordButton(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
    takePhotoDrawable = ContextCompat.getDrawable(context, R.drawable.take_photo_button);
    startRecordDrawable = ContextCompat.getDrawable(context, R.drawable.start_video_record_button);
    stopRecordDrawable = ContextCompat.getDrawable(context, R.drawable.stop_button_background);
  }

  public void setup(@CameraConfiguration.MediaAction int mediaAction,
      @NonNull RecordButtonListener listener) {
    setMediaAction(mediaAction);
    this.listener = listener;

    //        setBackground(ContextCompat.getDrawable(context, R.drawable.circle_frame_background_dark));
    if (Build.VERSION.SDK_INT > 15) {
      setBackground(ContextCompat.getDrawable(context, R.drawable.circle_frame_background));
    } else {
      setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.circle_frame_background));
    }

    setIcon();
    //        setOnClickListener(new RecordClickListener());

    setOnTouchListener(new RecordButtonTouchListener());

    setSoundEffectsEnabled(false);
    setIconPadding(iconPadding);
  }

  private void setIconPadding(int paddingDP) {
    int padding = Utils.convertDipToPixels(context, paddingDP);
    setPadding(padding, padding, padding, padding);
  }

  public void setMediaAction(@CameraConfiguration.MediaAction int mediaAction) {
    this.mediaAction = mediaAction;
    if (CameraConfiguration.MEDIA_ACTION_PHOTO == mediaAction) {
      currentState = TAKE_PHOTO_STATE;
    } else {
      currentState = READY_FOR_RECORD_STATE;
    }
    setRecordState(currentState);
    setIcon();
  }

  public @RecordState
  int getRecordState() {
    return currentState;
  }

  public void setRecordState(@RecordState int state) {
    currentState = state;
    setIcon();
  }

  public void setRecordButtonListener(@NonNull RecordButtonListener listener) {
    this.listener = listener;
  }

  private void setIcon() {
    if (CameraConfiguration.MEDIA_ACTION_VIDEO == mediaAction) {

      setImageDrawable(startRecordDrawable);
      setIconPadding(iconPadding);

            /* if (READY_FOR_RECORD_STATE == currentState) {
                setImageDrawable(startRecordDrawable);
                setIconPadding(iconPadding);
            } else if (RECORD_IN_PROGRESS_STATE == currentState) {
                setImageDrawable(stopRecordDrawable);
                setIconPadding(iconPaddingStop);
            }*/
    } else {
      setImageDrawable(takePhotoDrawable);
      setIconPadding(iconPadding);
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void takePhoto(MediaActionSound sound) {
    sound.play(MediaActionSound.SHUTTER_CLICK);
    takePhoto();
  }

  private void takePhoto() {
    if (listener != null) listener.onTakePhotoButtonPressed();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void startRecording(MediaActionSound sound) {
    sound.play(MediaActionSound.START_VIDEO_RECORDING);
    startRecording();
  }

  private void startRecording() {
    currentState = RECORD_IN_PROGRESS_STATE;
    if (listener != null) {
      listener.onStartRecordingButtonPressed();
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void stopRecording(MediaActionSound sound) {
    sound.play(MediaActionSound.STOP_VIDEO_RECORDING);
    stopRecording();
  }

  private void stopRecording() {
    currentState = READY_FOR_RECORD_STATE;
    if (listener != null) {
      listener.onStopRecordingButtonPressed();
    }
  }

  @IntDef({ TAKE_PHOTO_STATE, READY_FOR_RECORD_STATE, RECORD_IN_PROGRESS_STATE })
  @Retention(RetentionPolicy.SOURCE)
  public @interface RecordState {
  }

  public interface RecordButtonListener {

    void onTakePhotoButtonPressed();

    void onStartRecordingButtonPressed();

    void onStopRecordingButtonPressed();
  }

  private class RecordClickListener implements OnClickListener {

    private final static int CLICK_DELAY = 1000;

    private long lastClickTime = 0;

    @Override
    public void onClick(View view) {

      if (System.currentTimeMillis() - lastClickTime < CLICK_DELAY) {
        return;
      } else {
        lastClickTime = System.currentTimeMillis();
      }

      if (Build.VERSION.SDK_INT > 15) {
        MediaActionSound sound = new MediaActionSound();
        if (TAKE_PHOTO_STATE == currentState) {
          takePhoto(sound);
        } else if (READY_FOR_RECORD_STATE == currentState) {
          startRecording(sound);
        } else if (RECORD_IN_PROGRESS_STATE == currentState) {
          stopRecording(sound);
        }
      } else {
        if (TAKE_PHOTO_STATE == currentState) {
          takePhoto();
        } else if (READY_FOR_RECORD_STATE == currentState) {
          startRecording();
        } else if (RECORD_IN_PROGRESS_STATE == currentState) {
          stopRecording();
        }
      }
      setIcon();
    }
  }

  private class RecordButtonTouchListener implements OnTouchListener {

    private final static int CLICK_DELAY = 1000;

    private long lastClickTime = 0;
    private boolean startVideoRecording = false;
    private Handler handler = new Handler();

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
        //                when button pressed is released

        startVideoRecording = false;
        setPressed(false);

        if (System.currentTimeMillis() - lastClickTime < CLICK_DELAY) {

          if (Build.VERSION.SDK_INT > 15) {
            MediaActionSound sound = new MediaActionSound();
            takePhoto(sound);
          } else {
            takePhoto();
          }
        } else {

          if (Build.VERSION.SDK_INT > 15) {
            MediaActionSound sound = new MediaActionSound();
            stopRecording(sound);
          } else {
            stopRecording();
          }
        }
        setIcon();

        return true;
      } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
        //                when the button is pressed..

        lastClickTime = System.currentTimeMillis();

        setPressed(true);
        startVideoRecording = true;
        waitForRecordVideo();

        return true;
      }
      return false;
    }

    private void waitForRecordVideo() {

      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          if (startVideoRecording) {

            currentState = READY_FOR_RECORD_STATE;
            mediaAction = CameraConfiguration.MEDIA_ACTION_VIDEO;

            if (Build.VERSION.SDK_INT > 15) {
              MediaActionSound sound = new MediaActionSound();
              startRecording(sound);
            } else {
              startRecording();
            }
            setIcon();
          }
        }
      }, CLICK_DELAY);
    }
  }
}