package chat.hola.com.app.DublyCamera.CameraInFragments.Filters.HelperClasses;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by moda on 22/05/17.
 */

class PixelUtil {

    private PixelUtil() {
    }

    static int dpToPx(Context context, int dp) {

        return Math.round(dp * getPixelScaleFactor(context));
    }

    public static int pxToDp(Context context, int px) {
        return Math.round(px / getPixelScaleFactor(context));
    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}