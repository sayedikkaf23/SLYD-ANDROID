package chat.hola.com.app.home.social.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.ExpandableTextView;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ViewHolder</h1>
 *
 * @author 3Embed
 * @since 24/2/2018.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rlAction)
    RelativeLayout rlAction;
    @BindView(R.id.tvActionText)
    TextView tvActionButton;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.profileNameTv)
    TextView profileNameTv;
    @BindView(R.id.vBgLike)
    View vBgLike;
    @BindView(R.id.ivLikeIt)
    ImageView ivLikeIt;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.postTimeTv)
    TextView postTimeTv;
    @BindView(R.id.ivMedia)
    ImageView ivMedia;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @BindView(R.id.ivVideoCam)
    ImageView ivVideoCam;
    @BindView(R.id.parentContsraint)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.llComments)
    LinearLayout llComments;
    @BindView(R.id.ibComment)
    ImageView ibComment;
    @BindView(R.id.ibLike)
    ImageView ibLike;
    @BindView(R.id.tvView)
    TextView tvView;
    @BindView(R.id.tvViewCount)
    TextView tvViewCount;
    @BindView(R.id.tvLike)
    TextView tvLike;
    @BindView(R.id.tvLikeCount)
    TextView tvLikeCount;
    @BindView(R.id.ivStarBadge)
    ImageView ivStarBadge;
    @BindView(R.id.tvOptions)
    TextView tvOptions;
    @BindView(R.id.rL_save)
    RelativeLayout rL_save;
    @BindView(R.id.tV_saveTo)
    TextView tV_saveTo;
    @BindView(R.id.tV_savedView)
    TextView tV_savedView;
    @BindView(R.id.ibSaved)
    ImageView ibSaved;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.rlTitle)
    RelativeLayout rlTitle;
    @BindView(R.id.tvViewMore)
    TextView tvViewMore;
    private ClickListner clickListner;

    public ViewHolder(View itemView, TypefaceManager typefaceManager, ClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.clickListner = clickListner;
        itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        postTimeTv.setTypeface(typefaceManager.getRegularFont());
        profileNameTv.setTypeface(typefaceManager.getSemiboldFont());
        tvTitle.setTypeface(typefaceManager.getRegularFont());
        tvLike.setTypeface(typefaceManager.getRegularFont());
        tvView.setTypeface(typefaceManager.getRegularFont());
        tvLikeCount.setTypeface(typefaceManager.getSemiboldFont());
        tvViewCount.setTypeface(typefaceManager.getSemiboldFont());
        tvActionButton.setTypeface(typefaceManager.getSemiboldFont());
        tvPrice.setTypeface(typefaceManager.getSemiboldFont());
        tV_saveTo.setTypeface(typefaceManager.getRegularFont());
        tV_savedView.setTypeface(typefaceManager.getRegularFont());
    }

    @OnClick(R.id.ivMedia)
    public void item() {
        clickListner.onItemClick(getAdapterPosition(), ivMedia);
    }

    @OnClick({R.id.ivProfilePic, R.id.profileNameTv})
    public void user() {
        clickListner.onUserClick(getAdapterPosition());
    }

    @OnClick(R.id.ibShare)
    public void comment() {
        clickListner.share(getAdapterPosition());
    }

    @OnClick(R.id.ibSend)
    public void send() {
        clickListner.send(getAdapterPosition());
    }
}