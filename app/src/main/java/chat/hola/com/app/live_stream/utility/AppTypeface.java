package chat.hola.com.app.live_stream.utility;

import android.content.Context;
import android.graphics.Typeface;

/**
 * <h2>AppTypeface</h2>
 * This class contains several methods that are used for setting and getting methods for typeFace.
 */

public class AppTypeface {
    private static AppTypeface setTypeface = null;
    private Typeface hind_semiBold;

    private AppTypeface(Context context) {
        initTypefaces(context);
    }

    /**
     * <h2>AppTypeface</h2>
     *
     * @param context: calling activity reference
     * @return : Single instance of this class
     */
    public static AppTypeface getInstance(Context context) {
        if (setTypeface == null) {
            setTypeface = new AppTypeface(context.getApplicationContext());
        }
        return setTypeface;
    }

    /**
     * <h2>initTypefaces</h2>
     * <p>
     * method to initializes the typefaces of the app
     * </p>
     *
     * @param context Context of the Activity
     */
    private void initTypefaces(Context context) {

        this.hind_semiBold = Typeface.createFromAsset(context.getAssets(), "fonts/Hind-Semibold_0.ttf");
    }


    //======== GETTER METHODS FOR ALL TYPEFACES


    public Typeface getHind_semiBold() {
        return hind_semiBold;
    }


}
