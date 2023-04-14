package chat.hola.com.app.DublyCamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ezcall.android.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlBilateralFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlBoxBlurFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlBrightnessFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlBulgeDistortionFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlCGAColorspaceFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlContrastFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlCrosshatchFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlExposureFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlGammaFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlGaussianBlurFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlGrayScaleFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlHalftoneFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlHazeFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlHighlightShadowFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlHueFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlInvertFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlLookUpTableFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlLuminanceFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlLuminanceThresholdFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlMonochromeFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlOpacityFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlPixelationFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlPosterizeFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlRGBFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlSaturationFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlSepiaFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlSharpenFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlSolarizeFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlSphereRefractionFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlSwirlFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlToneCurveFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlToneFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlVibranceFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlVignetteFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlWeakPixelInclusionFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlWhiteBalanceFilter;
import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlZoomBlurFilter;

//import com.daasuu.gpuvideoandroid.filter.GlBitmapOverlayFilter;

public enum FilterType {
    DEFAULT,
    BILATERAL_BLUR,
    BOX_BLUR,
    BRIGHTNESS,
    BULGE_DISTORTION,
    CGA_COLORSPACE,
    CONTRAST,
    CROSSHATCH,
    EXPOSURE,
//    FILTER_GROUP_SAMPLE,
    GAMMA,
    GAUSSIAN_FILTER,
    GRAY_SCALE,
    HALFTONE,
    HAZE,
    HIGHLIGHT_SHADOW,
    HUE,
    INVERT,
    LOOK_UP_TABLE,
    LUMINANCE,
    LUMINANCE_THRESHOLD,
    MONOCHROME,
    OPACITY,
//    OVERLAY,
    PIXELATION,
    POSTERIZE,
    RGB,
    SATURATION,
    SEPIA,
    SHARP,
    SOLARIZE,
    SPHERE_REFRACTION,
    SWIRL,
    TONE_CURVE,
    TONE,
    VIBRANCE,
    VIGNETTE,
//    WATERMARK,
    WEAK_PIXEL,
    WHITE_BALANCE,
    ZOOM_BLUR//,
//    BITMAP_OVERLAY_SAMPLE
    ;


    public static List<FilterType> createFilterList() {
        return Arrays.asList(FilterType.values());
    }

    public static GlFilter createGlFilter(FilterType filterType, Context context) {
        switch (filterType) {
            case DEFAULT:
                return new GlFilter();
            case BILATERAL_BLUR:
                return new GlBilateralFilter();
            case BOX_BLUR:
                return new GlBoxBlurFilter();
            case BRIGHTNESS:
                GlBrightnessFilter glBrightnessFilter = new GlBrightnessFilter();
                glBrightnessFilter.setBrightness(0.2f);
                return glBrightnessFilter;
            case BULGE_DISTORTION:
                return new GlBulgeDistortionFilter();
            case CGA_COLORSPACE:
                return new GlCGAColorspaceFilter();
            case CONTRAST:
                GlContrastFilter glContrastFilter = new GlContrastFilter();
                glContrastFilter.setContrast(2.5f);
                return glContrastFilter;
            case CROSSHATCH:
                return new GlCrosshatchFilter();
            case EXPOSURE:
                return new GlExposureFilter();
//            case FILTER_GROUP_SAMPLE:
//                return new GlFilterGroup(new GlSepiaFilter(), new GlVignetteFilter());
            case GAMMA:
                GlGammaFilter glGammaFilter = new GlGammaFilter();
                glGammaFilter.setGamma(2f);
                return glGammaFilter;
            case GAUSSIAN_FILTER:
                return new GlGaussianBlurFilter();
            case GRAY_SCALE:
                return new GlGrayScaleFilter();
            case HALFTONE:
                return new GlHalftoneFilter();
            case HAZE:
                GlHazeFilter glHazeFilter = new GlHazeFilter();
                glHazeFilter.setSlope(-0.5f);
                return glHazeFilter;
            case HIGHLIGHT_SHADOW:
                return new GlHighlightShadowFilter();
            case HUE:
                return new GlHueFilter();
            case INVERT:
                return new GlInvertFilter();
            case LOOK_UP_TABLE:
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_sample);
                return new GlLookUpTableFilter(bitmap);
            case LUMINANCE:
                return new GlLuminanceFilter();
            case LUMINANCE_THRESHOLD:
                return new GlLuminanceThresholdFilter();
            case MONOCHROME:
                return new GlMonochromeFilter();
            case OPACITY:
                GlOpacityFilter glOpacityFilter = new GlOpacityFilter();
                glOpacityFilter.setOpacity(0f);
                return glOpacityFilter;


            case PIXELATION:
                return new GlPixelationFilter();
            case POSTERIZE:
                return new GlPosterizeFilter();
            case RGB:
                GlRGBFilter glRGBFilter = new GlRGBFilter();
                glRGBFilter.setRed(0.5f);
                return glRGBFilter;
            case SATURATION:
                return new GlSaturationFilter();
            case SEPIA:
                return new GlSepiaFilter();
            case SHARP:
                GlSharpenFilter glSharpenFilter = new GlSharpenFilter();
                glSharpenFilter.setSharpness(4f);
                return glSharpenFilter;
            case SOLARIZE:
                return new GlSolarizeFilter();
            case SPHERE_REFRACTION:
                return new GlSphereRefractionFilter();
            case SWIRL:
                return new GlSwirlFilter();
            case TONE_CURVE:
                try {
                    InputStream is = context.getAssets().open("acv/tone_cuver_sample.acv");
                    return new GlToneCurveFilter(is);
                } catch (IOException e) {
                    Log.e("FilterType", "Error");
                }
                return new GlFilter();
            case TONE:
                return new GlToneFilter();
            case VIBRANCE:
                GlVibranceFilter glVibranceFilter = new GlVibranceFilter();
                glVibranceFilter.setVibrance(3f);
                return glVibranceFilter;
            case VIGNETTE:
                return new GlVignetteFilter();


//            case WATERMARK:
//                return new GlWatermarkFilter(BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_bitmap), GlWatermarkFilter.Position.RIGHT_BOTTOM);


                case WEAK_PIXEL:
                return new GlWeakPixelInclusionFilter();
            case WHITE_BALANCE:
                GlWhiteBalanceFilter glWhiteBalanceFilter = new GlWhiteBalanceFilter();
                glWhiteBalanceFilter.setTemperature(2400f);
                glWhiteBalanceFilter.setTint(2f);
                return glWhiteBalanceFilter;
            case ZOOM_BLUR:
                return new GlZoomBlurFilter();


//            case BITMAP_OVERLAY_SAMPLE:
//                return new GlBitmapOverlayFilter(BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_bitmap));

                default:
                return new GlFilter();
        }
    }


}
