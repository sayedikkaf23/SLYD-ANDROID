package chat.hola.com.app.collections.view_holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>{@link CollectionPostViewHolder}</h1>
 * <p1>ViewHolder for Collection post item.</p1>
 * @author : 3Embed
 * @since : 20/8/19
 */

public class CollectionPostViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image)
    public ImageView image;
    @BindView(R.id.iV_selected)
    public ImageView iV_selected;
    public CollectionPostViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
