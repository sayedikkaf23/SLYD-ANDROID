package chat.hola.com.app.Utilities;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;

/**
 * Created by DELL on 4/2/2018.
 */

public class InternetToast extends Toast {
    public InternetToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence text, int duration, boolean flag) {
        InternetToast result = new InternetToast(context);

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.internet_toast, null);
        TextView toastMessage = (TextView) v.findViewById(R.id.tvMessage);
        toastMessage.setText(text);
        toastMessage.setTypeface(AppController.getInstance().getRegularFont());
        if (flag)
            toastMessage.setBackgroundColor(context.getResources().getColor(R.color.green));
        else
            toastMessage.setBackgroundColor(context.getResources().getColor(R.color.doodle_color_red));

        result.setView(v);
        int offset = Math.round(40 * context.getResources().getDisplayMetrics().density);
        result.setDuration(duration);
        result.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, offset);

        return result;
    }
}
