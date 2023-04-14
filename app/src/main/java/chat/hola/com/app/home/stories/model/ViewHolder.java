package chat.hola.com.app.home.stories.model;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ViewHolder</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/26/2018
 */

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.profilePicIv)
    ImageView profilePicIv;
    @BindView(R.id.profileNameTv)
    TextView profileNameTv;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.ringFrame)
    FrameLayout ringFrame;
    private ClickListner clickListner;

    public ViewHolder(View itemView, TypefaceManager typefaceManager, ClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        profileNameTv.setTypeface(typefaceManager.getMediumFont());
        timeTv.setTypeface(typefaceManager.getRegularFont());
        this.clickListner = clickListner;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        clickListner.onItemClick(getAdapterPosition());
    }
}
