package chat.hola.com.app.collections.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.collections.add_to_collection.AddToCollectionActivity;

/**
 * <h1>{@link AllPostViewHolder}</h1>
 * <p1>ViewHolder for AllPostItem in Collection</p1>
 * @author : 3Embed
 * @since : 20/8/19
 */

public class AllPostViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image1)
    public ImageView image1;
    @BindView(R.id.image2)
    public ImageView image2;
    @BindView(R.id.image3)
    public ImageView image3;
    @BindView(R.id.image4)
    public ImageView image4;
    @BindView(R.id.tV_title)
    public TextView tV_title;
    @BindView(R.id.cardView)
    public CardView cardView;
    public AllPostViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this ,itemView);
    }
}