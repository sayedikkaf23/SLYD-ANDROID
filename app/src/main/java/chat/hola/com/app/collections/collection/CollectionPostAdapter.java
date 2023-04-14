package chat.hola.com.app.collections.collection;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import java.util.List;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.collections.model.PostData;
import chat.hola.com.app.collections.saved.SavedAdapter;
import chat.hola.com.app.collections.view_holder.CollectionPostViewHolder;

public class CollectionPostAdapter extends RecyclerView.Adapter<CollectionPostViewHolder> {

    private Context mContext;
    private List<PostData> data;
    private ClickListener clickListener;

    public CollectionPostAdapter(Context mContext, List<PostData> data) {
        this.mContext = mContext;
        this.data = data;
    }

    /**
     * Here listener has been set.
     * @param clickListener
     */
    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CollectionPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.collection_post_item,null,false);
        return new CollectionPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionPostViewHolder holder, int position) {

        PostData postData = data.get(position);
        holder.iV_selected.setVisibility(postData.isSelected() ? View.VISIBLE : View.GONE);

        int setWidth = CommonClass.getDeviceWidth((Activity) mContext)/3;
        holder.image.getLayoutParams().width=setWidth;
        holder.image.getLayoutParams().height=setWidth;

        if(postData.getThumbnailUrl()!=null) {
            Glide.with(mContext)
                    .load(postData.getThumbnailUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_default)
                    .into(holder.image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    clickListener.OnItemClick(position,postData);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Click listener of this adapter
     */
    public interface ClickListener{
        public void OnItemClick(int pos, PostData data);
    }
}
