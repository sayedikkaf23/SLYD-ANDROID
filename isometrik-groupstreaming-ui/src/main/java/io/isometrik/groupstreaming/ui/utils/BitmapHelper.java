package io.isometrik.groupstreaming.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * The type Bitmap helper.
 */
public class BitmapHelper {
  /**
   * Gets bitmap from vector drawable.
   *
   * @param context the context
   * @param drawableId the drawable id
   * @return the bitmap from vector drawable
   */
  public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
    Drawable drawable = ContextCompat.getDrawable(context, drawableId);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      drawable = (DrawableCompat.wrap(drawable)).mutate();
    }

    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
        Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }
}