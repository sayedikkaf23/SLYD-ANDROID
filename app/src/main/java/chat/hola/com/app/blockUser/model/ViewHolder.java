package chat.hola.com.app.blockUser.model;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ViewHolder</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @BindView(R.id.item)
    FrameLayout item;
    @BindView(R.id.tvName)
    TextView tvName;

    private ClickListner clickListner;

    public ViewHolder(View itemView, TypefaceManager typefaceManager, ClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvUserName.setTypeface(typefaceManager.getSemiboldFont());
        this.clickListner = clickListner;
    }
    @OnClick(R.id.btnUnBlock)
    public void btnUnblock() {
        clickListner.btnUnblock(getAdapterPosition());
    }

    @OnClick(R.id.item)
    public void itemSelect() {
        clickListner.itemSelect(getAdapterPosition(), !item.isSelected());
    }

    @OnClick(R.id.ivProfilePic)
    public void profile() {
        clickListner.onUserClick(getAdapterPosition());
    }


}