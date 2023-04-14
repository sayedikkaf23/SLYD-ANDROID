package io.isometrik.groupstreaming.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

/**
 * The type Keyboard util.
 */
public class KeyboardUtil {

  /**
   * Hide keyboard.
   *
   * @param activity the activity
   */
  public static void hideKeyboard(Activity activity) {
    try {
      InputMethodManager inputManager =
          (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

      if (inputManager != null && activity.getCurrentFocus() != null) {
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
            InputMethodManager.RESULT_UNCHANGED_SHOWN);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Dp to px float.
   *
   * @param context the context
   * @param valueInDp the value in dp
   * @return the float
   */
  public static float dpToPx(Context context, float valueInDp) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
  }
}
