package chat.hola.com.app.collections.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1>{@link CollectionViewHolder}</h1>
 * <p1>ViewHolder for Collection Item</p1>
 * @author : 3Embed
 * @since : 20/8/19
 */
public class CollectionViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    public ImageView image;
    @BindView(R.id.tV_title)
    public TextView tV_title;

    public CollectionViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}