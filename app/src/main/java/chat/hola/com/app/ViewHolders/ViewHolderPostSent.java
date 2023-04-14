package chat.hola.com.app.ViewHolders;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.AdjustableImageView;

/**
 * Created by moda on 16/08/18.
 */

public class ViewHolderPostSent extends RecyclerView.ViewHolder {


    public TextView time, date, previousMessage_head, previousMessage_content,title;

    public ImageView singleTick, doubleTickGreen, doubleTickBlue, clock, forward,pic;


    public AdjustableImageView thumbnail;
    public ImageView previousMessage_iv,playButton;


    public RelativeLayout messageRoot, previousMessage_rl;

    public ViewHolderPostSent(View view) {
        super(view);


        // senderName = (TextView) view.findViewById(R.id.lblMsgFrom);

//        blocked = (ImageView) view.findViewById(R.id.blocked);

        forward = (ImageView) view.findViewById(R.id.forward_iv);
        messageRoot = (RelativeLayout) view.findViewById(R.id.message_root);

        pic = (ImageView)view.findViewById(R.id.pic);


        /*
         * For message reply feature
         */
        previousMessage_rl = (RelativeLayout) view.findViewById(R.id.initialMessage_rl);
        previousMessage_head = (TextView) view.findViewById(R.id.senderName_tv);
        previousMessage_iv = (ImageView) view.findViewById(R.id.initialMessage_iv);
        previousMessage_content = (TextView) view.findViewById(R.id.message_tv);
        playButton = (ImageView) view.findViewById(R.id.playButton);

        ObjectAnimator animation = ObjectAnimator.ofFloat(forward, "rotationY", 0.0f, 180f);
        animation.setDuration(0);

        animation.start();
        time = (TextView) view.findViewById(R.id.ts);

        singleTick = (ImageView) view.findViewById(R.id.single_tick_green);

        doubleTickGreen = (ImageView) view.findViewById(R.id.double_tick_green);

        doubleTickBlue = (ImageView) view.findViewById(R.id.double_tick_blue);
        date = (TextView) view.findViewById(R.id.date);
        clock = (ImageView) view.findViewById(R.id.clock);
        thumbnail = (AdjustableImageView) view.findViewById(R.id.vidshow);
        title = (TextView) view.findViewById(R.id.title);

        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);

        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }
    }
}