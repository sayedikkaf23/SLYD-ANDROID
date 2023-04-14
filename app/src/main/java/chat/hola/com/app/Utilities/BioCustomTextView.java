package chat.hola.com.app.Utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ezcall.android.R;

import java.util.HashMap;

public class BioCustomTextView extends RelativeLayout implements View.OnClickListener {

  protected TextView mTv;

  protected TextView tvToggle; // Button to expand/collapse

  private boolean mRelayout;

  private boolean mCollapsed = true; // Show short version as default.

  private int mCollapsedHeight;

  private int mTextHeightWithMaxLines;

  private int mMaxCollapsedLines;

  private int mMarginBetweenTxtAndBottom;

  private String mExpandString, mCollapseString;
  private int mAnimationDuration;

  private float mAnimAlphaStart;

  private boolean mAnimating;

  /* Listener for callback */
  private BioCustomTextView.OnExpandStateChangeListener mListener;

  /* For saving collapsed status when used in ListView */
  private HashMap<String, Boolean> mCollapsedStatus;
  private String postId;

  public BioCustomTextView(Context context) {
    this(context, null);
  }

  public BioCustomTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public BioCustomTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs);
  }

//  @Override
//  public void setOrientation(int orientation) {
//    if (LinearLayout.HORIZONTAL == orientation) {
//      throw new IllegalArgumentException("PostTitleCustomTextView only supports Vertical Orientation.");
//    }
//    super.setOrientation(orientation);
//  }

  @Override
  public void onClick(View view) {

    if (view.getId() == R.id.tvToggle) {

      if (tvToggle.getVisibility() != View.VISIBLE) {
        return;
      }

      mCollapsed = !mCollapsed;
      tvToggle.setText(mCollapsed ? mExpandString : mCollapseString);

      if (mCollapsedStatus != null && postId != null) {
        mCollapsedStatus.put(postId, mCollapsed);
      }

      // mark that the animation is in progress
      mAnimating = true;

      Animation animation;
      if (mCollapsed) {
        animation = new BioCustomTextView.ExpandCollapseAnimation(this, getHeight(),
            mCollapsedHeight);
      } else {
        animation = new BioCustomTextView.ExpandCollapseAnimation(this, getHeight(),
            getHeight() + mTextHeightWithMaxLines - mTv.getHeight());
      }

      animation.setFillAfter(true);
      animation.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
          applyAlphaAnimation(mTv, mAnimAlphaStart);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
          // clear animation here to avoid repeated applyTransformation() calls
          clearAnimation();
          // clear the animation flag
          mAnimating = false;

          // notify the listener
          if (mListener != null && postId != null) {

            mListener.onExpandStateChanged(postId, mCollapsed);
          }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
      });

      clearAnimation();
      startAnimation(animation);
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    // while an animation is in progress, intercept all the touch events to children to
    // prevent extra clicks during the animation
    return mAnimating;
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    findViews();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // If no change, measure and return
    if (!mRelayout || getVisibility() == View.GONE) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      return;
    }
    mRelayout = false;

    // Setup with optimistic case
    // i.e. Everything fits. No button needed
    tvToggle.setVisibility(GONE);
    mTv.setMaxLines(Integer.MAX_VALUE);

    // Measure
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    // If the text fits in collapsed mode, we are done.
    if (mTv.getLineCount() <= mMaxCollapsedLines) {
      return;
    }

    // Saves the text height w/ max lines
    mTextHeightWithMaxLines = getRealTextViewHeight(mTv);

    // Doesn't fit in collapsed mode. Collapse text view as needed. Show
    // button.
    if (mCollapsed) {
      mTv.setMaxLines(mMaxCollapsedLines);
    }
    tvToggle.setVisibility(VISIBLE);

    // Re-measure with new setup
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    if (mCollapsed) {
      // Gets the margin between the TextView's bottom and the ViewGroup's bottom
      mTv.post(new Runnable() {
        @Override
        public void run() {
          mMarginBetweenTxtAndBottom = getHeight() - mTv.getHeight();
        }
      });
      // Saves the collapsed height of this ViewGroup
      mCollapsedHeight = getMeasuredHeight();
    }
  }

  public void setOnExpandStateChangeListener(
      @Nullable BioCustomTextView.OnExpandStateChangeListener listener) {
    mListener = listener;
  }

  public void setText(@Nullable CharSequence text, TypefaceManager typefaceManager) {
    mRelayout = true;
    mTv.setText(text);
    mTv.setTypeface(typefaceManager.getRobotoCondensedRegular());
    tvToggle.setTypeface(typefaceManager.getRobotoCondensedBold());

    mTv.setMovementMethod(LinkMovementMethod.getInstance());
    setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
  }

  public void setText(@Nullable CharSequence text,
      @NonNull HashMap<String, Boolean> collapsedStatus, String postId,
      TypefaceManager typefaceManager) {
    mCollapsedStatus = collapsedStatus;
    this.postId = postId;
    boolean isCollapsed;
    if (collapsedStatus.containsKey(postId)) {
      isCollapsed = collapsedStatus.get(postId);
    } else {
      isCollapsed = true;
    }
    clearAnimation();
    mCollapsed = isCollapsed;
    tvToggle.setText(mCollapsed ? mExpandString : mCollapseString);
    setText(text, typefaceManager);
    getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    requestLayout();
  }

  @Nullable
  public CharSequence getText() {
    if (mTv == null) {
      return "";
    }
    return mTv.getText();
  }

  private void init(AttributeSet attrs) {

    mMaxCollapsedLines = 1;
    mAnimationDuration = 300;
    mAnimAlphaStart = 0.7f;
    mExpandString = "View More";
    mCollapseString = "View Less";

    // enforces vertical orientation
    //setOrientation(LinearLayout.VERTICAL);

    // default visibility is gone
    setVisibility(GONE);
  }

  private void findViews() {
    mTv = (TextView) findViewById(R.id.expandable_text);
    //mTv.setOnClickListener(this);

    tvToggle = (TextView) findViewById(R.id.tvToggle);
    tvToggle.setText(mCollapsed ? mExpandString : mCollapseString);
    tvToggle.setOnClickListener(this);
  }

  private static boolean isPostHoneycomb() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  private static void applyAlphaAnimation(View view, float alpha) {
    if (isPostHoneycomb()) {
      view.setAlpha(alpha);
    } else {
      AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
      // make it instant
      alphaAnimation.setDuration(0);
      alphaAnimation.setFillAfter(true);
      view.startAnimation(alphaAnimation);
    }
  }

  private static int getRealTextViewHeight(@NonNull TextView textView) {
    int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
    int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
    return textHeight + padding;
  }

  class ExpandCollapseAnimation extends Animation {
    private final View mTargetView;
    private final int mStartHeight;
    private final int mEndHeight;

    public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
      mTargetView = view;
      mStartHeight = startHeight;
      mEndHeight = endHeight;
      setDuration(mAnimationDuration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
      final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
      mTv.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
      if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
        applyAlphaAnimation(mTv, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
      }
      mTargetView.getLayoutParams().height = newHeight;
      mTargetView.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
      super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
      return true;
    }
  }

  ;

  public interface OnExpandStateChangeListener {
    /**
     * Called when the expand/collapse animation has been finished
     *
     * @param postId - id of the post being expanded/collapsed
     * @param isCollapsed - true if the post tile has been collapsed
     */
    void onExpandStateChanged(String postId, boolean isCollapsed);
  }
}