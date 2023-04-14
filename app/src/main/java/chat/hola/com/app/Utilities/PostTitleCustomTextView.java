package chat.hola.com.app.Utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import com.ezcall.android.R;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

public class PostTitleCustomTextView extends LinearLayout {

  protected TextView mTv;

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
  public static final char LF = '\n';
  public static final char CR = '\r';
  /* Listener for callback */
  private PostTitleCustomTextView.OnExpandStateChangeListener mListener;

  /* For saving collapsed status when used in ListView */
  private HashMap<String, Boolean> mCollapsedStatus;
  private String postId;

  //private String originalText, truncatedText;
  private SpannableString originalText;
  CharSequence truncatedText;
  private SpannableString collapsedSpan, expandedSpan;

  public PostTitleCustomTextView(Context context) {
    this(context, null);
  }

  public PostTitleCustomTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public PostTitleCustomTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs);
  }

  @Override
  public void setOrientation(int orientation) {
    if (LinearLayout.HORIZONTAL == orientation) {
      throw new IllegalArgumentException(
          "PostTitleCustomTextView only supports Vertical Orientation.");
    }
    super.setOrientation(orientation);
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

    // Re-measure with new setup
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int lineStart = mTv.getLayout().getLineStart(mMaxCollapsedLines - 1);
    int lineEnd = mTv.getLayout().getLineEnd(mMaxCollapsedLines - 1);
    //Although there are 12 characters,still took 16 due to scaling of view more/view less text required.
    if (lineEnd - lineStart > 16) {
      truncatedText =
          originalText.subSequence(0, lineEnd - 16);//originalText.substring(0, lineEnd - 12);
    } else {
      truncatedText = originalText.subSequence(0, lineEnd);
    }

    int lastIndex = truncatedText.length() - 1;
    if (truncatedText.charAt(lastIndex) == LF || truncatedText.charAt(lastIndex) == CR) {
      truncatedText = truncatedText.subSequence(0, lastIndex - 1);
    }

    if (mCollapsed) {
      // Gets the margin between the TextView's bottom and the ViewGroup's bottom
      mTv.post(() -> mMarginBetweenTxtAndBottom = getHeight() - mTv.getHeight());
      // Saves the collapsed height of this ViewGroup
      mCollapsedHeight = getMeasuredHeight();
      mTv.setText(TextUtils.concat(truncatedText, "...", expandedSpan));
    } else {
      mTv.setText(TextUtils.concat(originalText, "  ", collapsedSpan));
    }
  }

  public void setOnExpandStateChangeListener(
      @Nullable PostTitleCustomTextView.OnExpandStateChangeListener listener) {
    mListener = listener;
  }

  public void setText(@Nullable CharSequence text, TypefaceManager typefaceManager) {
    mRelayout = true;
    mTv.setText(text);
    mTv.setTypeface(typefaceManager.getRobotoCondensedRegular());

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      collapsedSpan.setSpan(new TypefaceSpan(typefaceManager.getRobotoCondensedBold()), 0,
          mCollapseString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      expandedSpan.setSpan(new TypefaceSpan(typefaceManager.getRobotoCondensedBold()), 0,
          mExpandString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    mTv.setMovementMethod(LinkMovementMethod.getInstance());
    setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
  }

  public void setText(@Nullable SpannableString text,
      @NonNull HashMap<String, Boolean> collapsedStatus, String postId,
      TypefaceManager typefaceManager) {
    mCollapsedStatus = collapsedStatus;
    this.postId = postId;
    originalText = text;//text.toString();
    boolean isCollapsed;
    if (collapsedStatus.containsKey(postId)) {
      isCollapsed = collapsedStatus.get(postId);
    } else {
      isCollapsed = true;
    }
    clearAnimation();
    mCollapsed = isCollapsed;
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

    mMaxCollapsedLines = 2;
    mAnimationDuration = 300;
    mAnimAlphaStart = 0.7f;
    mExpandString = "View More";
    mCollapseString = "View Less";
    mExpandString = "View More";
    mCollapseString = "View Less";
    collapsedSpan = new SpannableString(mCollapseString);
    expandedSpan = new SpannableString(mExpandString);
    collapsedSpan.setSpan(new ClickableSpan() {
      @Override
      public void onClick(@NotNull View v) {
        toggleVisibility();
      }

      @Override
      public void updateDrawState(@NonNull TextPaint ds) {
        ds.setUnderlineText(false); // set to false to remove underline

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
          Typeface myTypeface = Typeface.createFromAsset(AppController.getInstance().getAssets(),
              "fonts/RobotoCondensed-Bold.ttf");

          ds.setTypeface(myTypeface);
        }
      }
    }, 0, mCollapseString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

    collapsedSpan.setSpan(new RelativeSizeSpan(1.1f), 0, mCollapseString.length(),
        Spanned.SPAN_INCLUSIVE_INCLUSIVE);

    expandedSpan.setSpan(new ClickableSpan() {
      @Override
      public void onClick(@NotNull View v) {
        toggleVisibility();
      }

      @Override
      public void updateDrawState(@NonNull TextPaint ds) {
        ds.setUnderlineText(false); // set to false to remove underline

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
          Typeface myTypeface = Typeface.createFromAsset(AppController.getInstance().getAssets(),
              "fonts/RobotoCondensed-Bold.ttf");

          ds.setTypeface(myTypeface);
        }
      }
    }, 0, mExpandString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    expandedSpan.setSpan(new RelativeSizeSpan(1.1f), 0, mExpandString.length(),
        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    // enforces vertical orientation
    setOrientation(LinearLayout.VERTICAL);

    // default visibility is gone
    setVisibility(GONE);
  }

  private void findViews() {
    mTv = (TextView) findViewById(R.id.expandable_text);
    //mTv.setOnClickListener(this);
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

  private void toggleVisibility() {

    mCollapsed = !mCollapsed;

    if (mCollapsedStatus != null && postId != null) {
      mCollapsedStatus.put(postId, mCollapsed);
    }

    // mark that the animation is in progress
    mAnimating = true;

    Animation animation;
    if (mCollapsed) {
      animation =
          new PostTitleCustomTextView.ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
    } else {
      animation = new PostTitleCustomTextView.ExpandCollapseAnimation(this, getHeight(),
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
        if (truncatedText != null) {
          if (mCollapsed) {
            mTv.setText(TextUtils.concat(truncatedText, "...", expandedSpan));
          } else {
            mTv.setText(TextUtils.concat(originalText, "  ", collapsedSpan));
          }
          mTv.scrollTo(0, 0);
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