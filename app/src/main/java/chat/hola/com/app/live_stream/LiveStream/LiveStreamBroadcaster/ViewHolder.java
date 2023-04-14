package chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ViewHolder</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 07 August 2019
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivProfilePic)
    ImageView profilePic;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tbFollow)
    ToggleButton tbFollow;

    public ViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvName.setTypeface(typefaceManager.getSemiboldFont());
        tvUserName.setTypeface(typefaceManager.getRegularFont());
        tbFollow.setTypeface(typefaceManager.getRegularFont());
    }
}
