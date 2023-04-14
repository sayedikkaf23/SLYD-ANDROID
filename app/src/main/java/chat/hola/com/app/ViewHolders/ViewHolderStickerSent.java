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
 * Created by moda on 21/10/16.
 */
public class ViewHolderStickerSent extends RecyclerView.ViewHolder {


//    public TextView  time, date;

    public TextView time, date, previousMessage_head, previousMessage_content;

    public ImageView singleTick, doubleTickGreen, doubleTickBlue, clock, imageView,forward,previousMessage_iv;
    public RelativeLayout messageRoot, previousMessage_rl;
    public ViewHolderStickerSent(View view) {
        super(view);

//        senderName = (TextView) view.findViewById(R.id.StickerslblMsgFrom);
        forward= (ImageView) view.findViewById(R.id.forward_iv);
        messageRoot=(RelativeLayout) view.findViewById(R.id.message_root);
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
        imageView = (ImageView) view.findViewById(R.id.imgshow);

        date = (TextView) view.findViewById(R.id.date);

        time = (TextView) view.findViewById(R.id.ts);

        singleTick = (ImageView) view.findViewById(R.id.single_tick_green);

        doubleTickGreen = (ImageView) view.findViewById(R.id.double_tick_green);

        doubleTickBlue = (ImageView) view.findViewById(R.id.double_tick_blue);

        clock = (ImageView) view.findViewById(R.id.clock);
        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);
        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }

    }
}
