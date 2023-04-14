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

import com.ezcall.android.R;
import com.masoudss.lib.WaveformSeekBar;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.RingProgressBar;


/**
 * View holder for audio received recycler view item
 */
public class ViewHolderAudioReceived extends RecyclerView.ViewHolder {


    public TextView senderName;

    public TextView time, date, tv, fnf,previousMessage_head, previousMessage_content;
    public RingProgressBar progressBar;


    public ImageView playButton, download, forward, cancel,previousMessage_iv,pic,pauseButton;
    public ProgressBar progressBar2;
    public WaveformSeekBar waveformSeekBar;


    public RelativeLayout messageRoot, previousMessage_rl;

    public ViewHolderAudioReceived(View view) {
        super(view);


         senderName = (TextView) view.findViewById(R.id.lblMsgFrom);
        forward = (ImageView) view.findViewById(R.id.forward_iv);

        messageRoot = (RelativeLayout) view.findViewById(R.id.message_root);
        pic = (ImageView)view.findViewById(R.id.pic);
          /*
         * For message reply feature
         */

        waveformSeekBar =  view.findViewById(R.id.waveformSeekBar);

        previousMessage_rl = (RelativeLayout) view.findViewById(R.id.initialMessage_rl);
        previousMessage_head = (TextView) view.findViewById(R.id.senderName_tv);

        previousMessage_content = (TextView) view.findViewById(R.id.message_tv);
        previousMessage_iv= (ImageView) view.findViewById(R.id.initialMessage_iv);
        ObjectAnimator animation = ObjectAnimator.ofFloat(forward, "rotationY", 0.0f, 180f);
        animation.setDuration(0);

        animation.start();
        playButton = (ImageView) view.findViewById(R.id.imageView26);
        pauseButton = (ImageView) view.findViewById(R.id.imageView30);

        tv = (TextView) view.findViewById(R.id.tv);

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
        tv.setTypeface(tf, Typeface.NORMAL);

        fnf.setTypeface(tf, Typeface.NORMAL);


        if (previousMessage_head != null) {
            previousMessage_head.setTypeface(tf, Typeface.BOLD);

            previousMessage_content.setTypeface(tf, Typeface.NORMAL);
        }
    }

}
