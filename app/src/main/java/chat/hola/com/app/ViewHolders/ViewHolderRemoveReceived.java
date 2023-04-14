package chat.hola.com.app.ViewHolders;

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;

/**
 * Created by moda on 12/01/18.
 */

public class ViewHolderRemoveReceived extends RecyclerView.ViewHolder {


    public TextView senderName;

    public TextView message, time, date;
    public RelativeLayout messageRoot;

    public ViewHolderRemoveReceived(View view) {
        super(view);
        messageRoot = (RelativeLayout) view.findViewById(R.id.message_root);
        date = (TextView) view.findViewById(R.id.date);
        senderName = (TextView) view.findViewById(R.id.lblMsgFrom);
        message = (TextView) view.findViewById(R.id.txtMsg);
        time = (TextView) view.findViewById(R.id.ts);

        Typeface tf = AppController.getInstance().getRegularFont();

        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);

        message.setTypeface(tf, Typeface.ITALIC);

    }
}