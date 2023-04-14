package chat.hola.com.app.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <h> ProgressDialog class</h>
 * @author 3Embed.
 * @since 27/2/18.
 */

public class CustomAlertDialog implements CustomDialogInterface {

    private static final String TAG = CustomAlertDialog.class.getSimpleName();

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvProgress)
    TextView tvProgress;

    @BindView(R.id.ibTryAgain)
    ImageButton ibTryAgain;

    @BindView(R.id.pbProgress)
    ProgressBar pbProgress;

    AlertDialog alertDialog;

    private Unbinder unbinder;


    public CustomAlertDialog(@NonNull Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert layoutInflater != null;
        @SuppressLint("InflateParams")
        View rootView = layoutInflater.inflate(R.layout.dialog_alert,null,false);
        unbinder = ButterKnife.bind(this,rootView);
        tvTitle.setText(R.string.preparing_to_share);//todo getString() not apllied
        tvProgress.setText(R.string.zero_perc);//todo getString() not apllied
        builder.setView(rootView);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);

    }

    @Override
    public void show() {
        if(alertDialog != null){
            if(!alertDialog.isShowing())
                alertDialog.show();
        }
        else{
            Log.e(TAG,"alertDialog can not be null!!");
        }
    }

    @Override
    public void dismiss() {
        if(alertDialog != null){
         alertDialog.dismiss();
        }
        else{
            Log.e(TAG,"alertDialog can not be null!!");
        }
    }

    @Override
    public void setProgress(int progress) {
        if(tvProgress != null){
            tvProgress.setText(progress+R.string.perc);
        }
        if(pbProgress != null) {
            pbProgress.setProgress(progress);
        }
    }

    @Override
    public void setTitle(String title) {
        if(tvTitle != null){
            tvTitle.setText(title);
        }
    }

    @Override
    public void enableTryAgain() {
        if(tvProgress != null && ibTryAgain != null){
            tvProgress.setVisibility(View.GONE);
            ibTryAgain.setVisibility(View.VISIBLE);
        }
    }
}
