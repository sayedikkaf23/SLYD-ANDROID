package chat.hola.com.app.home.stories.model;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
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

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvHeader)
    TextView tvHeader;

    public HeaderViewHolder(View itemView, TypefaceManager typefaceManager) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvHeader.setTypeface(typefaceManager.getSemiboldFont());
    }
}
