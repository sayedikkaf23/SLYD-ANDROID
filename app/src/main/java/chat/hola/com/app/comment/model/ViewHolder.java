package chat.hola.com.app.comment.model;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.CommentTextView;
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
    public TextView tvUserName;
    @BindView(R.id.tvMessage)
    public TextView tvMessage;
    @BindView(R.id.commentTextView)
    public CommentTextView commentTextView; // view more support
    @BindView(R.id.ivCommentProfilePic)
    public ImageView ivCommentProfilePic;
    @BindView(R.id.tvTime)
    public TextView tvTime;
    @BindView(R.id.item)
    public FrameLayout item;
    @BindView(R.id.iVDelete)
    public ImageView iVDelete;
    @BindView(R.id.iVLike)
    public ImageView iVLike;
    private ClickListner clickListner;

    /*
    * Bug Title: font while typing comment is different from font in which comments appear
    * Bug Id: DUBAND133
    * Fix Desc: change typeface
    * Fix Dev: hardik
    * Fix Date: 6/5/21
    * */
    public ViewHolder(View itemView, TypefaceManager typefaceManager, ClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvUserName.setTypeface(typefaceManager.getSemiboldFont());
        tvMessage.setTypeface(typefaceManager.getSemiboldFont());
        tvTime.setTypeface(typefaceManager.getRegularFont());
        this.clickListner = clickListner;
    }

    @OnClick(R.id.item)
    public void itemSelect() {
        clickListner.itemSelect(getAdapterPosition(), !item.isSelected());
    }


}