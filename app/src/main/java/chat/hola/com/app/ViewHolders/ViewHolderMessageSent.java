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
 * View holder for text message sent recycler view item
 */
public class ViewHolderMessageSent extends RecyclerView.ViewHolder {


    //    public TextView senderName;
    private AppController appController;
    private Typeface fontMedium, fontRegular;

    public TextView message, time, date, previousMessage_head, previousMessage_content;

    public ImageView singleTick, doubleTickGreen, doubleTickBlue, clock, previousMessage_iv, forward,pic;//, blocked;
    public RelativeLayout messageRoot, previousMessage_rl;

    public ViewHolderMessageSent(View view) {
        super(view);
//        blocked = (ImageView) view.findViewById(R.id.blocked);
        appController=AppController.getInstance();
        fontMedium=appController.getMediumFont();
        fontRegular=appController.getRegularFont();
        date = (TextView) view.findViewById(R.id.date);
        forward = (ImageView) view.findViewById(R.id.forward_iv);
        // senderName = (TextView) view.findViewById(R.id.lblMsgFrom);
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

        singleTick = (ImageView) view.findViewById(R.id.single_tick_green);

        doubleTickGreen = (ImageView) view.findViewById(R.id.double_tick_green);

        doubleTickBlue = (ImageView) view.findViewById(R.id.double_tick_blue);
        clock = (ImageView) view.findViewById(R.id.clock);
        // message.setMovementMethod(LinkMovementMethod.getInstance());
        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(fontRegular);

        date.setTypeface(tf, Typeface.ITALIC);
        message.setTypeface(fontRegular);


        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }
    }
}
