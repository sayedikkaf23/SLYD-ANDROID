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
 * View holder for text message received recycler view item
 */
public class ViewHolderMessageReceived extends RecyclerView.ViewHolder {


    public TextView senderName;

    public TextView message, time, date, previousMessage_head, previousMessage_content;
    public RelativeLayout messageRoot, previousMessage_rl;
    private AppController appController;
    private Typeface fontReg;

    public ImageView previousMessage_iv,forward,pic;


    public ViewHolderMessageReceived(View view) {
        super(view);
        appController=AppController.getInstance();
        fontReg=appController.getRegularFont();
        date = (TextView) view.findViewById(R.id.date);
        senderName = (TextView) view.findViewById(R.id.lblMsgFrom);
        forward = (ImageView) view.findViewById(R.id.forward_iv);
        messageRoot = (RelativeLayout) view.findViewById(R.id.message_root);
        pic = (ImageView)view.findViewById(R.id.pic);
        ObjectAnimator animation = ObjectAnimator.ofFloat(forward, "rotationY", 0.0f, 180f);
        animation.setDuration(0);

        animation.start();

         /*
         * For message reply feature
         */
        previousMessage_rl = (RelativeLayout) view.findViewById(R.id.initialMessage_rl);
        previousMessage_head = (TextView) view.findViewById(R.id.senderName_tv);
        previousMessage_iv = (ImageView) view.findViewById(R.id.initialMessage_iv);
        previousMessage_content = (TextView) view.findViewById(R.id.message_tv);
        message = (TextView) view.findViewById(R.id.txtMsg);

        time = (TextView) view.findViewById(R.id.ts);
        //    message.setMovementMethod(LinkMovementMethod.getInstance());
        Typeface tf = AppController.getInstance().getRegularFont();

        time.setTypeface(fontReg);

        date.setTypeface(fontReg);
        message.setTypeface(fontReg);
        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);
            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }
    }
}
