package chat.hola.com.app.DublyCamera;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import chat.hola.com.app.DublyCamera.gpuv.egl.filter.GlOverlayFilter;

public class GlBitmapOverlayFilter extends GlOverlayFilter {

    private Bitmap bitmap;

    public GlBitmapOverlayFilter(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected void drawCanvas(Canvas canvas) {
        if (bitmap != null && !bitmap.isRecycled()) {

//            Paint transparentPaint = new Paint();
//            transparentPaint.setAlpha(255); // 0 - 255
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

}
