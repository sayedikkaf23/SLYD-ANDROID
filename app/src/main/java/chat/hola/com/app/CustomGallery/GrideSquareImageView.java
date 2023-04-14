package chat.hola.com.app.CustomGallery;

/**
 * Created by moda on 30/08/17.
 */

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
/**
 * @since /8/2017.
 */
public class GrideSquareImageView extends AppCompatImageView
{
    public GrideSquareImageView(Context context)
    {
        super(context);
    }

    public GrideSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GrideSquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}