package chat.hola.com.app.ViewHolders;

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
import chat.hola.com.app.Utilities.RingProgressBar;


/**
 * Created by moda on 23/08/17.
 */

public class ViewHolderDocumentSent extends RecyclerView.ViewHolder {

    public TextView time, date, fileName, fileType, fnf, previousMessage_head, previousMessage_content;


    public ImageView pic,singleTick, doubleTickGreen, doubleTickBlue, clock, fileImage, download, cancel,forward,previousMessage_iv;//, blocked;

    public RingProgressBar progressBar;


    public ProgressBar progressBar2;

    public RelativeLayout messageRoot, previousMessage_rl,documentLayout;
    public ViewHolderDocumentSent(View view) {
        super(view);
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

        documentLayout = (RelativeLayout) view.findViewById(R.id.rl);
        fileName = (TextView) view.findViewById(R.id.fileName);
        fileType = (TextView) view.findViewById(R.id.fileType);

        fileImage = (ImageView) view.findViewById(R.id.fileImage);
        date = (TextView) view.findViewById(R.id.date);

        time = (TextView) view.findViewById(R.id.ts);

        singleTick = (ImageView) view.findViewById(R.id.single_tick_green);

        doubleTickGreen = (ImageView) view.findViewById(R.id.double_tick_green);

        doubleTickBlue = (ImageView) view.findViewById(R.id.double_tick_blue);

        clock = (ImageView) view.findViewById(R.id.clock);


        fnf = (TextView) view.findViewById(R.id.fnf);


        progressBar2 = (ProgressBar) view.findViewById(R.id.progress2);
        progressBar = (RingProgressBar) view.findViewById(R.id.progress);
        download = (ImageView) view.findViewById(R.id.download);


        cancel = (ImageView) view.findViewById(R.id.cancel);


        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);
        fileName.setTypeface(tf, Typeface.NORMAL);
        fileType.setTypeface(tf, Typeface.NORMAL);

        date.setTypeface(tf, Typeface.ITALIC);
        fnf.setTypeface(tf, Typeface.NORMAL);
        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }
    }
}