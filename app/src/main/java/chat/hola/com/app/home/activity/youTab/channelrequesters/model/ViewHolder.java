package chat.hola.com.app.home.activity.youTab.channelrequesters.model;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * Created by DELL on 4/17/2018.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.btnAccept)
    Button btnAccept;
    @BindView(R.id.btnReject)
    Button btnReject;

    private ClickListner clickListner;

    public ViewHolder(View itemView, TypefaceManager typefaceManager, ClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvUserName.setTypeface(typefaceManager.getSemiboldFont());
        tvTime.setTypeface(typefaceManager.getRegularFont());
        btnAccept.setTypeface(typefaceManager.getRegularFont());
        btnReject.setTypeface(typefaceManager.getRegularFont());
        this.clickListner = clickListner;
    }


}