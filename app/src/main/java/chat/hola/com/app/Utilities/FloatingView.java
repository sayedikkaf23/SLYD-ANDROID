package chat.hola.com.app.Utilities;

/*
 * Created by moda on 04/04/16.
 */

import android.app.Activity;
import android.graphics.Point;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ezcall.android.R;





 /* Floating view is used to display a custom view for attachments in the chat screen */

public class FloatingView {


    private static PopupWindow popWindow;

    private FloatingView() {
    }


    public static void onShowPopup(Activity activity, View inflatedView) {


        Display display = activity.getWindowManager().getDefaultDisplay();


        float density = activity.getResources().getDisplayMetrics().density;


        final Point size = new Point();
        display.getSize(size);

        popWindow = new PopupWindow(inflatedView, size.x, ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        popWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity,
                R.drawable.comment_popup_bg));

        popWindow.setFocusable(true);

        popWindow.setOutsideTouchable(true);

        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        if (Build.VERSION.SDK_INT > 27) {
            //only api 28 above

            popWindow.showAtLocation(activity.getWindow().getDecorView().getRootView(), Gravity.TOP, 0,
                    Math.round(86 * density));

        } else {
            //only api 28 down


            popWindow.showAtLocation(activity.getCurrentFocus(), Gravity.TOP, 0,
                    Math.round(86 * density));
        }
    }

    public static void dismissWindow() {

        if (popWindow != null) {

            popWindow.dismiss();
        }
    }
}
