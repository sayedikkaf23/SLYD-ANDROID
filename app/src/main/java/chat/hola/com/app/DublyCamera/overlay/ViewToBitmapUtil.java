package chat.hola.com.app.DublyCamera.overlay;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.NonNull;

public class ViewToBitmapUtil {

  /**
   * Creates a bitmap from the supplied view.
   *
   * @param view The view to get the bitmap.
   * @param width The width for the bitmap.
   * @param height The height for the bitmap.
   * @return The bitmap from the supplied view.
   */
  public @NonNull
  static Bitmap createBitmapFromView(@NonNull View view, int width, int height) {
    if (width > 0 && height > 0) {
      //view.measure(View.MeasureSpec.makeMeasureSpec(convertDpToPixels(width),
      //    View.MeasureSpec.EXACTLY),
      //    View.MeasureSpec.makeMeasureSpec(convertDpToPixels(height),
      //        View.MeasureSpec.EXACTLY));

      view.measure(View.MeasureSpec.makeMeasureSpec(width,
          View.MeasureSpec.EXACTLY),
          View.MeasureSpec.makeMeasureSpec(height,
              View.MeasureSpec.EXACTLY));
    }
    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

    Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
        Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Drawable background = view.getBackground();

    if (background != null) {
      background.draw(canvas);
    }
    view.draw(canvas);

    return bitmap;
  }

  /**
   * Converts DP into pixels.
   *
   * @param dp The value in DP to be converted into pixels.
   *
   * @return The converted value in pixels.
   */
  private static int convertDpToPixels(float dp) {
    return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        dp, Resources.getSystem().getDisplayMetrics()));
  }

}