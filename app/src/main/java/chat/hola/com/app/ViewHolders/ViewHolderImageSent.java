package chat.hola.com.app.ViewHolders;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.AdjustableImageView;
import chat.hola.com.app.Utilities.RingProgressBar;


/**
 * Created by moda on 08/08/17.
 */

public class ViewHolderImageSent extends RecyclerView.ViewHolder {


//    public TextView senderName;


    public TextView time, date, fnf, previousMessage_head, previousMessage_content;
    public ImageView singleTick, doubleTickGreen, doubleTickBlue, clock, forward, download, cancel,previousMessage_iv,pic;//,blocked;


    public AdjustableImageView imageView;


    public RingProgressBar progressBar;

    public ProgressBar progressBar2;


    public RelativeLayout messageRoot, previousMessage_rl;

    public ViewHolderImageSent(View view) {
        super(view);

        //  senderName = (TextView) view.findViewById(R.id.lblMsgFrom);
        forward = (ImageView) view.findViewById(R.id.forward_iv);
        messageRoot = (RelativeLayout) view.findViewById(R.id.message_root);
        pic = (ImageView)view.findViewById(R.id.pic);

          /*
         * For message reply feature
         */
        previousMessage_rl = (RelativeLayout) view.findViewById(R.id.initialMessage_rl);
        previousMessage_head = (TextView) view.findViewById(R.id.senderName_tv);
        previousMessage_iv= (ImageView) view.findViewById(R.id.initialMessage_iv);
        previousMessage_content = (TextView) view.findViewById(R.id.message_tv);
        ObjectAnimator animation = ObjectAnimator.ofFloat(forward, "rotationY", 0.0f, 180f);
        animation.setDuration(0);

        animation.start();
        date = (TextView) view.findViewById(R.id.date);

        imageView = (AdjustableImageView) view.findViewById(R.id.imgshow);
//        blocked = (ImageView) view.findViewById(R.id.blocked);
        time = (TextView) view.findViewById(R.id.ts);

        singleTick = (ImageView) view.findViewById(R.id.single_tick_green);

        doubleTickGreen = (ImageView) view.findViewById(R.id.double_tick_green);

        doubleTickBlue = (ImageView) view.findViewById(R.id.double_tick_blue);

        clock = (ImageView) view.findViewById(R.id.clock);

        progressBar = (RingProgressBar) view.findViewById(R.id.progress);


        cancel = (ImageView) view.findViewById(R.id.cancel);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progress2);
        download = (ImageView) view.findViewById(R.id.download);

        fnf = (TextView) view.findViewById(R.id.fnf);
        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);
        fnf.setTypeface(tf, Typeface.NORMAL);
        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }
    }
}