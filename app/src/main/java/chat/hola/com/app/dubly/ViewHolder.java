package chat.hola.com.app.dubly;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;
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
    @BindView(R.id.ivThumbnail)
    ImageView ivThumbnail;
    @BindView(R.id.ibPlay)
    CheckBox ibPlay;
    @BindView(R.id.llDubWithIt)
    LinearLayout llDubWithIt;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvDuration)
    TextView tvDuration;

    @BindView(R.id.tvPlaybackTime)
    TextView tvPlaybackTime;

    @BindView(R.id.ivLike)
    CheckBox ivLike;
    private ClickListner clickListner;

    public ViewHolder(View itemView, TypefaceManager typefaceManager, ClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvDescription.setTypeface(typefaceManager.getMediumFont());
        tvDuration.setTypeface(typefaceManager.getRegularFont());

        tvPlaybackTime.setTypeface(typefaceManager.getRegularFont());
        this.clickListner = clickListner;
    }

    @OnClick({R.id.item, R.id.ibPlay})
    public void item() {
        clickListner.play(getAdapterPosition(), !ibPlay.isChecked());
    }

    @OnClick(R.id.ivLike)
    public void like() {
        clickListner.like(getAdapterPosition(), !ibPlay.isChecked());
    }
}