package chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery;
import android.content.Context;
import android.graphics.Rect;

import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @since 18/1/17.
 */
public class ItemDecorationGridview extends RecyclerView.ItemDecoration
{
    private int mItemOffset;

    public ItemDecorationGridview(int item, int itemOffset) {
        mItemOffset = itemOffset;
    }

    public ItemDecorationGridview(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(0,context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }

}