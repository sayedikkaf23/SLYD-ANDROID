package io.isometrik.groupstreaming.ui.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Recyclerview helper class to implement touch listener on recyclerview items
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
  /**
   * The interface On item click listener.
   */
  public interface OnItemClickListener {
    /**
     * On item click.
     *
     * @param view the view
     * @param position the position
     */
    void onItemClick(View view, int position);

    /**
     * On item long click.
     *
     * @param view the view
     * @param position the position
     */
    void onItemLongClick(View view, int position);
  }

  private OnItemClickListener mListener;

  private GestureDetector mGestureDetector;

  /**
   * Instantiates a new Recycler item click listener.
   *
   * @param context the context
   * @param recyclerView the recycler view
   * @param listener the listener
   */
  public RecyclerItemClickListener(Context context, final RecyclerView recyclerView,
      OnItemClickListener listener) {
    mListener = listener;

    mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
      @Override
      public boolean onSingleTapUp(MotionEvent e) {
        return true;
      }

      @Override
      public void onLongPress(MotionEvent e) {
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());

        if (childView != null && mListener != null) {
          mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
        }
      }
    });
  }

  @Override
  public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
    View childView = view.findChildViewUnder(e.getX(), e.getY());

    if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
      mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
    }

    return false;
  }

  @Override
  public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
  }

  @Override
  public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
  }
}