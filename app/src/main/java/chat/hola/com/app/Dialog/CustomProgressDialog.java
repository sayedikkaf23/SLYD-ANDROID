package chat.hola.com.app.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * Created by ankit on 1/3/18.
 */

@SuppressLint("ValidFragment")
public class CustomProgressDialog {


    TypefaceManager typefaceManager;

    @BindView(R.id.pbProgress)
    ProgressBar pbProgress;

    @BindView(R.id.tvProgress)
    TextView tvProgress;

    private Activity activity;

    private Unbinder unbinder;
    private String msg ="Loading...";
    private AlertDialog dialog;

    public void setMessage(String msg){
        tvProgress.setText(R.string.double_inverted_comma+msg);
    }//todo getString() not apllied

    @Inject
    @SuppressLint("ValidFragment")
    public CustomProgressDialog(String msg, Activity activity,TypefaceManager typefaceManager) {
        this.activity = activity;
        this.msg = msg;
        this.typefaceManager = typefaceManager;
        init(activity);
    }

    public void init(Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null);
        unbinder = ButterKnife.bind(this,dialogView);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        LinearLayout mainLayout = (LinearLayout) dialogView.findViewById(R.id.mainLayout);
        mainLayout.setBackgroundColor(Color.TRANSPARENT);
        //AnimationDrawable frameAnimation = (AnimationDrawable)mainLayout.getBackground();
        //frameAnimation.start();
        tvProgress.setText(msg);
        dialog = dialogBuilder.create();
    }

    public AlertDialog getDialog(){
        return dialog;
    }


}
