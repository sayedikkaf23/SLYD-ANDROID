package chat.hola.com.app.ViewHolders;
/*
 * Created by moda on 02/04/16.
 */

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;


/**
 * View holder for contact received recycler view item
 */
public class ViewHolderContactReceived extends RecyclerView.ViewHolder {
    public  TextView senderName;


    public TextView time, contactName, contactNumber, date, previousMessage_head, previousMessage_content;

    public ImageView forward,previousMessage_iv,pic;

    public RelativeLayout contact_rl;
    public RelativeLayout messageRoot,previousMessage_rl;

    public ViewHolderContactReceived(View view) {
        super(view);

        senderName = (TextView) view.findViewById(R.id.lblMsgFrom);

        forward = (ImageView) view.findViewById(R.id.forward_iv);

        messageRoot=(RelativeLayout) view.findViewById(R.id.message_root);

        pic = (ImageView) view.findViewById(R.id.pic);
           /*
         * For message reply feature
         */
        previousMessage_rl = (RelativeLayout) view.findViewById(R.id.initialMessage_rl);
        previousMessage_head = (TextView) view.findViewById(R.id.senderName_tv);
        previousMessage_iv= (ImageView) view.findViewById(R.id.initialMessage_iv);
        previousMessage_content = (TextView) view.findViewById(R.id.message_tv);

        contact_rl=(RelativeLayout) view.findViewById(R.id.relative_layout_message);
        ObjectAnimator animation = ObjectAnimator.ofFloat(forward, "rotationY", 0.0f, 180f);
        animation.setDuration(0);

        animation.start();

        date = (TextView) view.findViewById(R.id.date);
        time = (TextView) view.findViewById(R.id.ts);

        contactName = (TextView) view.findViewById(R.id.contactName);

        contactNumber = (TextView) view.findViewById(R.id.contactNumber);
        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);
        contactName.setTypeface(tf, Typeface.NORMAL);
        contactNumber.setTypeface(tf, Typeface.NORMAL);
        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }
    }
}
