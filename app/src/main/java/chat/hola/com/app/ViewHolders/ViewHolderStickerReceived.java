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


/**
 * Created by moda on 21/10/16.
 */
public class ViewHolderStickerReceived extends RecyclerView.ViewHolder {


    public TextView senderName, time, date, previousMessage_head, previousMessage_content;

    public ImageView imageView, download, forward,previousMessage_iv;


    public RelativeLayout messageRoot, previousMessage_rl, relative_layout_message;

    public ViewHolderStickerReceived(View view) {
        super(view);


        senderName = (TextView) view.findViewById(R.id.lblMsgFrom);

        forward = (ImageView) view.findViewById(R.id.forward_iv);
        messageRoot = (RelativeLayout) view.findViewById(R.id.message_root);
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

        relative_layout_message = (RelativeLayout) view.findViewById(R.id.relative_layout_message);
        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);
        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }

    }
}
