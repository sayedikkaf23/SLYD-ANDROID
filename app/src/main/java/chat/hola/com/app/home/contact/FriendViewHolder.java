package chat.hola.com.app.home.contact;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ezcall.android.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @BindView(R.id.ivStarBadge)
    ImageView ivStarBadge;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tbFollow)
    ToggleButton tbFollow;

    private ContactAdapter.ClickListner clickListner;

    public FriendViewHolder(@NonNull View itemView, TypefaceManager typefaceManager, ContactAdapter.ClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvName.setTypeface(typefaceManager.getSemiboldFont());
        tvUserName.setTypeface(typefaceManager.getRegularFont());
        tbFollow.setTypeface(typefaceManager.getRegularFont());
        this.clickListner = clickListner;
    }

    @OnClick(R.id.ivProfilePic)
    public void iemClick() {
        clickListner.onUserSelected(getAdapterPosition());
    }
}
