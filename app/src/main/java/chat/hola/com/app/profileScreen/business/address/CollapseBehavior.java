package chat.hola.com.app.profileScreen.business.address;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * @author Pramod
 * @since 28-02-2018
 */

public class CollapseBehavior<V extends ViewGroup> extends CoordinatorLayout.Behavior<V>{


    public CollapseBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        if (isBottomSheet(dependency)) {
            BottomSheetBehavior behavior = ((BottomSheetBehavior) ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior());

            int peekHeight = behavior.getPeekHeight();
            // The default peek height is -1, which
            // gets resolved to a 16:9 ratio with the parent
            final int actualPeek = peekHeight >= 0 ? peekHeight : (int) (((parent.getHeight() * 1.0) / (16.0)) * 9.0);
            if (dependency.getTop() >= actualPeek) {
                // Only perform translations when the
                // view is between "hidden" and "collapsed" states
                final int dy = dependency.getTop() - parent.getHeight();
                ViewCompat.setTranslationY(child, dy/2);
                return true;
            }
        }

        return false;
    }

    private static boolean isBottomSheet(@NonNull View view) {
        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof CoordinatorLayout.LayoutParams) {
            return ((CoordinatorLayout.LayoutParams) lp)
                    .getBehavior() instanceof BottomSheetBehavior;
        }
        return false;
    }


}
