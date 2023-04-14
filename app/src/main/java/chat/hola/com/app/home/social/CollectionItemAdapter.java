package chat.hola.com.app.home.social;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.home.model.Data;

public class CollectionItemAdapter extends RecyclerView.Adapter<CollectionItemAdapter.ItemViewHolder> {

    private Context mContext;
    private List<CollectionData> data;
    private ClickListener clickListener;
    private Data selectedPost;

    public CollectionItemAdapter(Context mContext, List<CollectionData> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public void setListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.social_collection_item,null,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if(data.get(position).getCoverImage()!=null) {
            Glide.with(mContext)
                    .load(data.get(position).getCoverImage())
                    .centerCrop()
                    .placeholder(R.color.colorBonJour)
                    .into(holder.image);
        }

        holder.tV_title.setText(data.get(position).getCollectionName());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    clickListener.onCollectionSelect(position,data.get(position),selectedPost);
                }catch (NullPointerException e){
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setSelectedPost(Data data) {
        selectedPost = data;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        public ImageView image;
        @BindView(R.id.tV_title)
        public TextView tV_title;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ClickListener{
        public void onCollectionSelect(int pos, CollectionData data, Data selectedPost);
    }
}
