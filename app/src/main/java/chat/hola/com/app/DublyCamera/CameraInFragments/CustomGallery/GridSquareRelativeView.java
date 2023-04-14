package chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

    /**
     * @since 1/13/2017.
     */
    public class GridSquareRelativeView  extends RelativeLayout
    {
        public GridSquareRelativeView(Context context)
        {
            super(context);
        }

        public GridSquareRelativeView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public GridSquareRelativeView(Context context, AttributeSet attrs, int defStyle)
        {
            super(context, attrs, defStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }
