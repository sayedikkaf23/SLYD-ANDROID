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
import chat.hola.com.app.Utilities.RingProgressBar;


/**
 * Created by moda on 23/08/17.
 */

public class ViewHolderDocumentReceived extends RecyclerView.ViewHolder {


    public TextView senderName;

    public TextView time, date, fileName, fileType, fnf, previousMessage_head, previousMessage_content;
    public RingProgressBar progressBar;


    public ImageView fileImage, download, forward, cancel,previousMessage_iv,pic;
    public ProgressBar progressBar2;


    public RelativeLayout messageRoot, previousMessage_rl, documentLayout;

    public ViewHolderDocumentReceived(View view) {
        super(view);
        senderName = (TextView) view.findViewById(R.id.lblMsgFrom);
        forward = (ImageView) view.findViewById(R.id.forward_iv);
        messageRoot = (RelativeLayout) view.findViewById(R.id.message_root);
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

        documentLayout = (RelativeLayout) view.findViewById(R.id.rl);

        fileImage = (ImageView) view.findViewById(R.id.fileImage);


        fileName = (TextView) view.findViewById(R.id.fileName);

        fileType = (TextView) view.findViewById(R.id.fileType);


        time = (TextView) view.findViewById(R.id.ts);

        date = (TextView) view.findViewById(R.id.date);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progress2);
        progressBar = (RingProgressBar) view.findViewById(R.id.progress);
        download = (ImageView) view.findViewById(R.id.download);


        cancel = (ImageView) view.findViewById(R.id.cancel);

        fnf = (TextView) view.findViewById(R.id.fnf);

        Typeface tf = AppController.getInstance().getRegularFont();
        time.setTypeface(tf, Typeface.ITALIC);

        date.setTypeface(tf, Typeface.ITALIC);
        fileName.setTypeface(tf, Typeface.NORMAL);
        fileType.setTypeface(tf, Typeface.NORMAL);

        fnf.setTypeface(tf, Typeface.NORMAL);
        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }
    }

}
