package chat.hola.com.app.DublyCamera.CameraInFragments.Filters.HelperClasses;

/**
 * Created by moda on 18/05/17.
 */

import android.content.Context;
import android.graphics.PointF;

import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;

public class FilterTools {


    public static GPUImageFilter createFilterForType(final Context context, final FilterType type) {
        switch (type) {

            case BRIGHTNESS:


                return new GPUImageBrightnessFilter(0f);

            case CONTRAST:

                return new GPUImageContrastFilter(1.0f);
            case SATURATION:
                return new GPUImageSaturationFilter(1.0f);


//            case HIGHLIGHTS:
//
//                return new GPUImageHighlightFilter(0.0f, 1.0f);


            case SHADOW:

                return new GPUImageHighlightShadowFilter(0.0f, 1.0f);

            case VIGNETTE:

                PointF centerPoint = new PointF();
                centerPoint.x = 0.5f;
                centerPoint.y = 0.5f;
                return new GPUImageVignetteFilter(centerPoint, new float[]{0.0f, 0.0f, 0.0f}, 0.75f, 1.0f);
            case SHARPEN:

                GPUImageSharpenFilter sharpness = new GPUImageSharpenFilter();
                sharpness.setSharpness(0.0f);
                return sharpness;
            default:
                throw new IllegalStateException("No filter of that type!");
        }

    }


    public enum FilterType {
        BRIGHTNESS, CONTRAST,
        SATURATION, /**
         * HIGHLIGHTS,
         */SHADOW, VIGNETTE, SHARPEN


    }


    public static class FilterAdjuster {
        private final Adjuster<? extends GPUImageFilter> adjuster;

        public FilterAdjuster(final GPUImageFilter filter) {
            if (filter instanceof GPUImageSharpenFilter) {
                adjuster = new SharpnessAdjuster().filter(filter);
            } else if (filter instanceof GPUImageContrastFilter) {
                adjuster = new ContrastAdjuster().filter(filter);
            } else if (filter instanceof GPUImageBrightnessFilter) {
                adjuster = new BrightnessAdjuster().filter(filter);
            } else if (filter instanceof GPUImageSaturationFilter) {
                adjuster = new SaturationAdjuster().filter(filter);
            }
// else if (filter instanceof GPUImageShadowFilter) {
//
//
//                adjuster = new ShadowAdjuster().filter(filter);
//            } else if (filter instanceof GPUImageHighlightFilter) {
//
//
//                adjuster = new HighlightAdjuster().filter(filter);
//            }

            else if (filter instanceof GPUImageHighlightShadowFilter) {
                adjuster = new HighlightShadowAdjuster().filter(filter);
            } else if (filter instanceof GPUImageVignetteFilter) {
                adjuster = new VignetteAdjuster().filter(filter);
            } else {
                adjuster = null;
            }
        }

        public void adjust(final int percentage) {
            if (adjuster != null) {
                adjuster.adjust(percentage);
            }
        }

        private abstract class Adjuster<T extends GPUImageFilter> {
            private T filter;

            @SuppressWarnings("unchecked")
            public Adjuster<T> filter(final GPUImageFilter filter) {
                this.filter = (T) filter;
                return this;
            }

            public T getFilter() {
                return filter;
            }

            public abstract void adjust(int percentage);

            protected float range(final int percentage, final float start, final float end) {
                return (end - start) * percentage / 100.0f + start;
            }

            protected int range(final int percentage, final int start, final int end) {
                return (end - start) * percentage / 100 + start;
            }
        }

        private class SharpnessAdjuster extends Adjuster<GPUImageSharpenFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setSharpness(range(percentage, 0f, 0.5f));
            }
        }


        private class ContrastAdjuster extends Adjuster<GPUImageContrastFilter> {
            @Override
            public void adjust(final int percentage) {


                getFilter().setContrast(range(percentage, 1.1f, 1.35f));

            }
        }


        private class BrightnessAdjuster extends Adjuster<GPUImageBrightnessFilter> {
            @Override
            public void adjust(final int percentage) {


                getFilter().setBrightness(range(percentage, 0.05f, 0.25f));


            }
        }


        private class SaturationAdjuster extends Adjuster<GPUImageSaturationFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setSaturation(range(percentage, 1.0f, 2.0f));
            }
        }


        private class HighlightShadowAdjuster extends Adjuster<GPUImageHighlightShadowFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setShadows(range(percentage, 0f, 0.3f));
                getFilter().setHighlights(range(percentage, 0f, 1f));

            }
        }

        private class VignetteAdjuster extends Adjuster<GPUImageVignetteFilter> {
            @Override
            public void adjust(final int percentage) {
                getFilter().setVignetteStart(range(percentage, 1.0f, 0.2f));
            }
        }


    }
}