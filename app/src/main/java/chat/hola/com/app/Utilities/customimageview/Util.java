package chat.hola.com.app.Utilities.customimageview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class Util {
  public static final double EARTH_RADIUS = 6366198;

  static void checkZoomLevels(float minZoom, float midZoom,
                              float maxZoom) {
    if (minZoom >= midZoom) {
      throw new IllegalArgumentException(
          "Minimum zoom has to be less than Medium zoom. Call setMinimumZoom() with a more appropriate value");
    } else if (midZoom >= maxZoom) {
      throw new IllegalArgumentException(
          "Medium zoom has to be less than Maximum zoom. Call setMaximumZoom() with a more appropriate value");
    }
  }

  static boolean hasDrawable(ImageView imageView) {
    return imageView.getDrawable() != null;
  }

  static boolean isSupportedScaleType(final ImageView.ScaleType scaleType) {
    if (scaleType == null) {
      return false;
    }
    switch (scaleType) {
      case MATRIX:
        throw new IllegalStateException("Matrix scale type is not supported");
    }
    return true;
  }

  static int getPointerIndex(int action) {
    return (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
  }

  /**
   * This method is using for create the bitmap from the resource
   * @param context  Application Context
   * @param drawableId Resource id
   * @return bitmap
   */
  public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
    Drawable drawable = ContextCompat.getDrawable(context, drawableId);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
      assert drawable != null;
      drawable = (DrawableCompat.wrap(drawable)).mutate();
    }

    assert drawable != null;
    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
        drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);

    return bitmap;
  }

  /**
   * Add 2 points 1000m northEast and southWest of the center.
   * They increase the bounds only, if they are not already larger
   * than this.
   * 1000m on the diagonal translates into about 709m to each direction.
   */
  public static LatLngBounds createBoundsWithMinDiagonal(Activity context, Marker firstMarker, Marker secondMarker) {
    final LatLngBounds[] latLngBounds = new LatLngBounds[1];
    context.runOnUiThread(() ->
    {
      LatLngBounds.Builder builder = new LatLngBounds.Builder();
      builder.include(firstMarker.getPosition());
      builder.include(secondMarker.getPosition());

      LatLngBounds tmpBounds = builder.build();
      LatLng center = tmpBounds.getCenter();
      LatLng northEast = move(center, 709, 709);
      LatLng southWest = move(center, -709, -709);
      builder.include(southWest);
      builder.include(northEast);
      latLngBounds[0] = builder.build();
    });
    return latLngBounds[0];
  }

  /**
   * <h2>move</h2>
   * Create a new LatLng which lies toNorth meters north and toEast meters
   * east of startLL
   */
  private static LatLng move(LatLng startLL, double toNorth, double toEast) {
    double lonDiff = meterToLongitude(toEast, startLL.latitude);
    double latDiff = meterToLatitude(toNorth);
    return new LatLng(startLL.latitude + latDiff, startLL.longitude
        + lonDiff);
  }

  /**
   * meterToLongitude
   * this use for give the padding for east side
   * @param meterToEast
   * @param latitude
   * @return
   */
  private static double meterToLongitude(double meterToEast, double latitude) {
    double latArc = Math.toRadians(latitude);
    double radius = Math.cos(latArc) * EARTH_RADIUS;
    double rad = meterToEast / radius;
    return Math.toDegrees(rad);
  }

  /**
   * meterToLatitude
   *    * this use for give the padding for north side
   * @param meterToNorth
   * @return
   */
  private static double meterToLatitude(double meterToNorth) {
    double rad = meterToNorth / EARTH_RADIUS;
    return Math.toDegrees(rad);
  }

}
