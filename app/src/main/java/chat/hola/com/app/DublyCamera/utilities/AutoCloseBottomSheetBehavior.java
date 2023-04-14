package chat.hola.com.app.DublyCamera.utilities;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import org.jetbrains.annotations.NotNull;

public class AutoCloseBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

  public AutoCloseBottomSheetBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onInterceptTouchEvent(@NotNull CoordinatorLayout parent, @NotNull V child, MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN
        && getState() == BottomSheetBehavior.STATE_EXPANDED) {

      Rect outRect = new Rect();
      child.getGlobalVisibleRect(outRect);

      if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
        setState(BottomSheetBehavior.STATE_HIDDEN);
      }
    }
    return super.onInterceptTouchEvent(parent, child, event);
  }
}