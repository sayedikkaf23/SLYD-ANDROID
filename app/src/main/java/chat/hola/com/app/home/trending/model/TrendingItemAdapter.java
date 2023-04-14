package chat.hola.com.app.home.trending.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.model.Data;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ezcall.android.R;
import java.util.List;

public class TrendingItemAdapter extends RecyclerView.Adapter<TrendingItemAdapter.ViewHolder> {

    private Context context;
    private List<Data> dataList;
    private long totalPosts;
    private String hashtag;
    private ClickListner listner;

    public TrendingItemAdapter(Context context, List<Data> posts, long totalPosts, String hashtag, ClickListner listner) {
        this.context = context;
        this.dataList = posts;
        this.totalPosts = totalPosts;
        this.listner = listner;
        this.hashtag = hashtag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position != -1) {
            Data data = dataList.get(position);

            String thumbnailUrl = data.getThumbnailUrl1();
            if(data.getPurchased()) {
                if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                    thumbnailUrl = thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
                }
            }
            DrawableRequestBuilder<String> thumbnail = Glide.with(context)
                .load(Utilities.getModifiedThumbnailLink(thumbnailUrl))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);

            if (data.getMediaType1() == 1) {
                //video
                String videoCoverImageUrl;
                if (data.getPurchased()) {
                    videoCoverImageUrl = Utilities.getModifiedImageLink(data.getImageUrl1());
                    holder.rlLockedPost.setVisibility(View.GONE);
                } else {
                    videoCoverImageUrl = thumbnailUrl;
                    holder.rlLockedPost.setVisibility(View.VISIBLE);
                }

                Glide.with(context)
                    .load(videoCoverImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .fitCenter()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_default))
                    .thumbnail(thumbnail)
                    .into(holder.imageView);
            } else {
                //image
                if ( data.getPurchased()) {
                    holder.rlLockedPost.setVisibility(View.GONE);

                } else {
                    holder.rlLockedPost.setVisibility(View.VISIBLE);
                }
                Glide.with(context)
                    .load(Utilities.getModifiedImageLink(data.getImageUrl1()))
                    .thumbnail(thumbnail)
                    .dontAnimate()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_default))
                    .into(holder.imageView);
            }

            holder.imageButton.setVisibility(data.getMediaType1() == 0 ? View.GONE : View.VISIBLE);
            holder.overlay.setVisibility(position == dataList.size() - 1 && totalPosts > 8 ? View.VISIBLE : View.GONE);
            holder.tvViewAll.setVisibility(position == dataList.size() - 1 && totalPosts > 8 ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.postClick(dataList, position, hashtag);
                }
            });

            if (position == dataList.size() - 1 && totalPosts > 8) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listner.viewAll(hashtag, String.valueOf(totalPosts));
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPostImage)
        ImageView imageView;
        @BindView(R.id.ibCam)
        ImageView imageButton;
        @BindView(R.id.overlay)
        View overlay;
        @BindView(R.id.tvViewAll)
        TextView tvViewAll;
        @BindView(R.id.rlLockedPost)
        RelativeLayout rlLockedPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.getLayoutParams().width = CommonClass.getDeviceWidth(context) / 3;
        }
    }

    public interface ClickListner {
        void viewAll(String hashtag, String totalPosts);

        void postClick(List<Data> dataList, int position, String hashtag);
    }
}
