package chat.hola.com.app.ViewHolders;

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;

/**
 * Created by moda on 12/01/18.
 */

public class ViewHolderRemoveSent extends RecyclerView.ViewHolder {


    public TextView message, time, date;

    public ImageView singleTick, doubleTickGreen, doubleTickBlue, clock;

    public RelativeLayout messageRoot;

    public ViewHolderRemoveSent(View view) {
        super(view);

        date = (TextView) view.findViewById(R.id.date);

        messageRoot = (RelativeLayout) view.findViewById(R.id.message_root);
        message = (TextView) view.findViewById(R.id.txtMsg);

        time = (TextView) view.findViewById(R.id.ts);

        singleTick = (ImageView) view.findViewById(R.id.single_tick_green);

        doubleTickGreen = (ImageView) view.findViewById(R.id.double_tick_green);

        doubleTickBlue = (ImageView) view.findViewById(R.id.double_tick_blue);
        clock = (ImageView) view.findViewById(R.id.clock);

        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);
        message.setTypeface(tf, Typeface.ITALIC);

    }
}
