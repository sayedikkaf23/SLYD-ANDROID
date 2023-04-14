package chat.hola.com.app.ViewHolders;

import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import chat.hola.com.app.AppController;
import com.ezcall.android.R;


/**
 * Created by moda on 27/07/17.
 */

public class ViewHolderCall extends RecyclerView.ViewHolder {
    public TextView name, callTime, callTypeTv, callTypeText;
    public ImageView receiverImage, callDialledIv, callTypeIv,img_video_call,img_audio_call;


    public ViewHolderCall(View view) {
        super(view);
        receiverImage = (ImageView) view.findViewById(R.id.userImage);
        name = (TextView) view.findViewById(R.id.usernamecall);
        callDialledIv = (ImageView) view.findViewById(R.id.callStatus);
        img_video_call = (ImageView) view.findViewById(R.id.img_video_call);
        img_audio_call = (ImageView) view.findViewById(R.id.img_audio_call);
        callTypeIv = (AppCompatImageView) view.findViewById(R.id.Statuscall);
        callTime = (TextView) view.findViewById(R.id.timeingcall);
        callTypeTv = (TextView) view.findViewById(R.id.call_type);
        callTypeText= (TextView) view.findViewById(R.id.call_type_text);
        Typeface tf = AppController.getInstance().getRegularFont();
        callTypeTv.setTypeface(tf, Typeface.NORMAL);
//        name.setTypeface(tf, Typeface.NORMAL);
//        callTime.setTypeface(tf, Typeface.NORMAL);
    }
}