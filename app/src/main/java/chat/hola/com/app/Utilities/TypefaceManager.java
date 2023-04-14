package chat.hola.com.app.Utilities;

import android.content.Context;
import android.graphics.Typeface;
import javax.inject.Inject;

/**
 * Created by ankit on 20/2/18.
 */

public class TypefaceManager {
    private Typeface tfBoldFont;
    private Typeface tfSemiboldFont;
    private Typeface tfMediumFont;
    private Typeface tfRegularFont;
    private Typeface dublyLogo;

    private Typeface robotoCondensed, robotoCondensedBold, robotoCondensedRegular;
    private Context context;

    @Inject
    public TypefaceManager(Context context) {
        this.context = context;
        tfBoldFont = Typeface.createFromAsset(context.getAssets(), "fonts/sf_pro_bold.otf");
        tfRegularFont = Typeface.createFromAsset(context.getAssets(), "fonts/sf_pro_regular.otf");
        tfMediumFont = Typeface.createFromAsset(context.getAssets(), "fonts/sf_pro_medium.otf");
        tfSemiboldFont = Typeface.createFromAsset(context.getAssets(), "fonts/sf_pro_semibold.otf");
        dublyLogo = Typeface.createFromAsset(context.getAssets(), "fonts/lucy_the_cat.otf");
        robotoCondensed =
            Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Condensed.ttf");
        robotoCondensedBold =
            Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        robotoCondensedRegular =
            Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
    }

    public Typeface getSemiboldFont() {
        return tfSemiboldFont;
    }

    public Typeface getMediumFont() {
        return tfMediumFont;
    }

    public Typeface getRegularFont() {
        return tfRegularFont;
    }

    public Typeface getDublyLogo() {
        return dublyLogo;
    }

    public Typeface getBoldFont() {
        return tfBoldFont;
    }

    public Typeface getRobotoCondensed() {
        return robotoCondensed;
    }

    public Typeface getRobotoCondensedBold() {
        return robotoCondensedBold;
    }

    public Typeface getRobotoCondensedRegular() {
        return robotoCondensedRegular;
    }
}

