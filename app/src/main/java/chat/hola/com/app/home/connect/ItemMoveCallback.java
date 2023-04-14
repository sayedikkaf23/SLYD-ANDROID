package chat.hola.com.app.home.connect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;
    private Drawable mIcon;
    private final ColorDrawable mBackground;


    public ItemMoveCallback(Context context, int backgroundColor, int drawable, ItemTouchHelperContract adapter) {
        mAdapter = adapter;
        mIcon = ContextCompat.getDrawable(context, drawable);
        mBackground = new ColorDrawable(context.getResources().getColor(backgroundColor));
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        mAdapter.onRowClear(viewHolder,i);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 25; //so mBackground is behind the rounded corners of itemView

        int iconMargin = (itemView.getHeight() - mIcon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - mIcon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + mIcon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = iconLeft + mIcon.getIntrinsicWidth();
            mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            mBackground.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - mIcon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            mBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            mIcon.setBounds(0, 0, 0, 0);
            mBackground.setBounds(0, 0, 0, 0);
        }

        mBackground.draw(c);
        mIcon.draw(c);
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END ;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);

//        void onRowSelected(SelectStoriesAdapter.ViewHolder1 myViewHolder);

        void onRowClear(RecyclerView.ViewHolder myViewHolder,int toPosition);


    }

}

