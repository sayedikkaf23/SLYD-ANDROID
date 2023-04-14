package chat.hola.com.app.ViewHolders;
/*
 * Created by moda on 02/04/16.
 */

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import chat.hola.com.app.AppController;
import com.ezcall.android.R;
import chat.hola.com.app.Utilities.AdjustableImageView;
import chat.hola.com.app.Utilities.RingProgressBar;


/**
 * View holder for video received recycler view item
 */
public class ViewHolderVideoReceived extends RecyclerView.ViewHolder {

    public TextView senderName, time, date, fnf, previousMessage_head, previousMessage_content;


    public ImageView download, cancel,forward,previousMessage_iv,pic;


    public ProgressBar progressBar2;

    public RingProgressBar progressBar;

    public AdjustableImageView thumbnail;

    public RelativeLayout messageRoot, previousMessage_rl;
    public ViewHolderVideoReceived(View view) {
        super(view);


        senderName = (TextView) view.findViewById(R.id.lblMsgFrom);

        forward= (ImageView) view.findViewById(R.id.forward_iv);
        messageRoot=(RelativeLayout) view.findViewById(R.id.message_root);

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
        time = (TextView) view.findViewById(R.id.ts);
        thumbnail = (AdjustableImageView) view.findViewById(R.id.vidshow);


        cancel = (ImageView) view.findViewById(R.id.cancel);

        progressBar2 = (ProgressBar) view.findViewById(R.id.progress2);
        progressBar = (RingProgressBar) view.findViewById(R.id.progress);


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
