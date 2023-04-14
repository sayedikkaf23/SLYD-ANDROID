package chat.hola.com.app.DublyCamera.dubbing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ezcall.android.R;

public class CountDownDurationAdjustView extends RelativeLayout {
  private View mCountDownBackground;
  /*
   * Length of video that has been taken
   * */
  private View mCaptureFinishedDuration;
  private TextView mDurationStartValue;
  private TextView mDurationEndValue;
  private TextView mShowCurrentTime;
  private ImageView mOperateHandle;

  private int mTotalWidth = 0;
  private int mHandleWidth = 0;
  private int mLeftMargin = 0;
  private float mPrevRawX;
  private int mHandleLeft = 0;
  private int mRightTextViewShowPos;
  private int mLeftTextViewShowPos;

  private StringBuilder mStrShowDuration = new StringBuilder("0s");
  /*
   * Maximum shooting duration value
   * */
  public long mMaxCaptureDuration = 0;
  /*
   * mHasFinishedDurationViewWidth
   * The mHasFinishedDurationViewWidth value is 0, which means that no countdown shooting has been performed; if it is not 0, one or more countdown shooting has been performed
   * */
  private int mHasFinishedDurationViewWidth = 0;
  //
  private long mNewCaptureDuration = 0;
  private OnCaptureDurationChangeListener mNewDurationChangeListener;

  public interface OnCaptureDurationChangeListener {
    void onNewDurationChange(long newDuration, boolean isDragEnd);
  }

  public void setNewDurationChangeListener(
      OnCaptureDurationChangeListener newDurationChangeListener) {
    this.mNewDurationChangeListener = newDurationChangeListener;
  }

  public CountDownDurationAdjustView(Context context) {
    this(context, null);
  }

  public CountDownDurationAdjustView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  /*
   * Set the current countdown shooting duration
   * */
  public void setNewCaptureDuration(long newCaptureDuration) {
    this.mNewCaptureDuration = newCaptureDuration;
  }

  /*
   * Need to set the maximum shooting time after the object is created
   **/
  public void setMaxCaptureDuration(int maxCaptureDuration) {
    mMaxCaptureDuration = maxCaptureDuration;
  }

  /*
   * After the object is created, you need to set the current shooting time
   * */
  public void setCurrentCaptureDuration(int finishedDuration) {
    float percent = finishedDuration / (float) mMaxCaptureDuration;
    mHasFinishedDurationViewWidth = (int) Math.floor(percent * mTotalWidth);
    ViewGroup.LayoutParams params = mCaptureFinishedDuration.getLayoutParams();
    params.width = mHasFinishedDurationViewWidth;
    mCaptureFinishedDuration.setLayoutParams(params);
  }

  public void resetHandleViewPosition(String maxDuration) {
    mHandleLeft = mRightTextViewShowPos;
    updateHandleViewPosition();
    mShowCurrentTime.setVisibility(View.GONE);
    mDurationEndValue.setVisibility(View.VISIBLE);
    mDurationEndValue.setText(maxDuration);
  }

  @SuppressLint("ClickableViewAccessibility")
  private void init(Context context) {
    View rootView = LayoutInflater.from(context).inflate(R.layout.countdown_capture, this);
    mCountDownBackground = rootView.findViewById(R.id.vCountDownBackground);
    mCaptureFinishedDuration = rootView.findViewById(R.id.vCaptureFinishedDuration);
    mDurationStartValue = rootView.findViewById(R.id.tvDurationStartValue);
    mDurationEndValue = rootView.findViewById(R.id.tvDurationEndValue);
    mShowCurrentTime = rootView.findViewById(R.id.tvShowCurrentTime);
    mOperateHandle = rootView.findViewById(R.id.ivOperateHandle);
    mOperateHandle.setOnTouchListener((v, event) -> {
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          getParent().requestDisallowInterceptTouchEvent(true);
          mHandleLeft = mOperateHandle.getLeft();
          mPrevRawX = (int) event.getRawX();
          updateShowCurrentTimeTextView();
          break;
        case MotionEvent.ACTION_MOVE:
          getParent().requestDisallowInterceptTouchEvent(true);
          float curRawX = event.getRawX();
          int dx = (int) Math.floor(curRawX - mPrevRawX + 0.5D);
          mPrevRawX = curRawX;
          moveHandleOperaView(dx);
          break;
        case MotionEvent.ACTION_UP:
          getParent().requestDisallowInterceptTouchEvent(false);
          updateShowCurrentTimeTextView();
          setNewDurationChangeCallback(true);
          break;
      }
      return true;
    });

    mHandleWidth = mOperateHandle.getLayoutParams().width;
    RelativeLayout.LayoutParams backgroundParams =
        (RelativeLayout.LayoutParams) mCountDownBackground.getLayoutParams();
    mLeftMargin = backgroundParams.leftMargin;
    int screenWidth = getScreenWidth(context);
    mTotalWidth = screenWidth - 2 * mLeftMargin;
    mLeftTextViewShowPos = mHandleWidth - mLeftMargin;
    mRightTextViewShowPos = screenWidth - mHandleWidth;
    mHandleLeft = mRightTextViewShowPos;
  }

  private void moveHandleOperaView(int dx) {
    mHandleLeft += dx;
    if (mHandleLeft <= mHasFinishedDurationViewWidth) {
      mHandleLeft = mHasFinishedDurationViewWidth;
    }

    if (mHandleLeft + mHandleWidth >= mTotalWidth + 2 * mLeftMargin) {
      mHandleLeft = mTotalWidth + 2 * mLeftMargin - mHandleWidth;
    }
    updateHandleViewPosition();

    int newPixelPos = mHandleLeft + mHandleWidth - 2 * mLeftMargin;
    float resPercent = (newPixelPos - mHasFinishedDurationViewWidth) / (float) mTotalWidth;
    mNewCaptureDuration = (long) Math.floor(resPercent * mMaxCaptureDuration);
    setNewDurationChangeCallback(false);
    updateShowCurrentTimeTextView();
  }

  private void updateShowCurrentTimeTextView() {
    mShowCurrentTime.setVisibility(View.VISIBLE);
    int newPixelPos = mHandleLeft + mHandleWidth - 2 * mLeftMargin;
    float percent = newPixelPos / (float) mTotalWidth;
    long curDuration = (long) Math.floor(percent * mMaxCaptureDuration);
    String strTimeDuration = TimeFormatUtil.formatUsToString(curDuration);
    int index = mStrShowDuration.indexOf("s");
    mStrShowDuration.replace(0, index, strTimeDuration);

    mShowCurrentTime.setText(mStrShowDuration.toString());

    RelativeLayout.LayoutParams lp =
        (RelativeLayout.LayoutParams) mShowCurrentTime.getLayoutParams();
    lp.leftMargin = mHandleLeft;
    mShowCurrentTime.setLayoutParams(lp);
    if (mHandleLeft + mHandleWidth <= mRightTextViewShowPos) {
      mDurationEndValue.setVisibility(View.VISIBLE);
    } else {
      mDurationEndValue.setVisibility(View.GONE);
    }
    if (mHandleLeft >= mLeftTextViewShowPos) {
      mDurationStartValue.setVisibility(View.VISIBLE);
    } else {
      mDurationStartValue.setVisibility(View.GONE);
    }
  }

  private void updateHandleViewPosition() {
    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mOperateHandle.getLayoutParams();
    lp.leftMargin = mHandleLeft;
    mOperateHandle.setLayoutParams(lp);
  }

  private void setNewDurationChangeCallback(boolean isDragEnd) {
    if (mNewDurationChangeListener != null) {
      mNewDurationChangeListener.onNewDurationChange(mNewCaptureDuration, isDragEnd);
    }
  }

  private int getScreenWidth(Context context) {
    DisplayMetrics dm = new DisplayMetrics();
    WindowManager mWm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    mWm.getDefaultDisplay().getMetrics(dm);
    int screenWidth = dm.widthPixels;
    return screenWidth;
  }
}
