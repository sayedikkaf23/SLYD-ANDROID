package chat.hola.com.app.trendingDetail.model;

import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * <h1>ViewHolder</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 5/4/2018.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.ibVideo)
    ImageButton ibPlay;
    @BindView(R.id.tvScore)
    TextView tvScore;
    private ClickListner clickListner;
    @BindView(R.id.rlLockedPost)
    RelativeLayout rlLockedPost;

    public ViewHolder(View itemView, ClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.clickListner = clickListner;
    }

    @Optional
    @OnClick({R.id.ivImage, R.id.ibPlay})
    public void ivImage() {
        clickListner.onItemClick(getAdapterPosition(), ivImage);
    }
}
