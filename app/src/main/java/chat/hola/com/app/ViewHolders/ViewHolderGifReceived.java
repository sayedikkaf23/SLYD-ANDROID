package chat.hola.com.app.ViewHolders;

import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import chat.hola.com.app.AppController;
import com.ezcall.android.R;


/**
 * Created by embed on 29/12/16.
 */
public class ViewHolderGifReceived extends RecyclerView.ViewHolder {

        public TextView senderName;
    public TextView time, date, previousMessage_head, previousMessage_content;


    public ImageView gifImage, gifStillImage,forward,previousMessage_iv,pic;

    public RelativeLayout messageRoot, previousMessage_rl;
    public ViewHolderGifReceived(View view) {
        super(view);
        senderName = (TextView) view.findViewById(R.id.lblMsgFrom);

        forward= (ImageView) view.findViewById(R.id.forward_iv);
        messageRoot=(RelativeLayout) view.findViewById(R.id.message_root);
        pic = (ImageView) view.findViewById(R.id.pic);

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

        gifImage = (ImageView) view.findViewById(R.id.vidshow);


        date = (TextView) view.findViewById(R.id.date);
        time = (TextView) view.findViewById(R.id.ts);

        gifStillImage = (ImageView) view.findViewById(R.id.gifStillImage);
        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);
        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }

    }
}
