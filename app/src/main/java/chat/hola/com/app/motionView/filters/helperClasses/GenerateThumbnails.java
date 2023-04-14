package chat.hola.com.app.motionView.filters.helperClasses;

import android.content.Context;


import chat.hola.com.app.motionView.filters.predefinedFilters.IF1977Filter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFAmaroFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFBrannanFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFEarlybirdFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFHefeFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFHudsonFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFInkwellFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFLomoFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFLordKelvinFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFNashvilleFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFRiseFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFSierraFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFSutroFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFToasterFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFValenciaFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFWaldenFilter;
import chat.hola.com.app.motionView.filters.predefinedFilters.IFXprollFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;


/**
 * Created by moda on 19/05/17.
 */

public class GenerateThumbnails

{


    public static GPUImageFilter createFilterForType(final Context context, final FilterType type) {
        switch (type) {

            case I_NORMAL:
                return new GPUImageFilter();
            case I_1977:
                return new IF1977Filter(context);
            case I_AMARO:
                return new IFAmaroFilter(context);
            case I_BRANNAN:
                return new IFBrannanFilter(context);
            case I_EARLYBIRD:
                return new IFEarlybirdFilter(context);
            case I_HEFE:
                return new IFHefeFilter(context);
            case I_HUDSON:
                return new IFHudsonFilter(context);
            case I_INKWELL:
                return new IFInkwellFilter(context);
            case I_LOMO:
                return new IFLomoFilter(context);
            case I_LORDKELVIN:
                return new IFLordKelvinFilter(context);
            case I_NASHVILLE:
                return new IFNashvilleFilter(context);
            case I_RISE:
                return new IFRiseFilter(context);
            case I_SIERRA:
                return new IFSierraFilter(context);
            case I_SUTRO:
                return new IFSutroFilter(context);
            case I_TOASTER:
                return new IFToasterFilter(context);
            case I_VALENCIA:
                return new IFValenciaFilter(context);
            case I_WALDEN:
                return new IFWaldenFilter(context);
            case I_XPROII:
                return new IFXprollFilter(context);

            default:
                throw new IllegalStateException("No filter of that type!");
        }

    }


    public enum FilterType {

        I_NORMAL,
        I_1977, I_AMARO, I_BRANNAN, I_EARLYBIRD, I_HEFE, I_HUDSON, I_INKWELL, I_LOMO, I_LORDKELVIN, I_NASHVILLE, I_RISE, I_SIERRA, I_SUTRO,
        I_TOASTER, I_VALENCIA, I_WALDEN, I_XPROII
    }


}