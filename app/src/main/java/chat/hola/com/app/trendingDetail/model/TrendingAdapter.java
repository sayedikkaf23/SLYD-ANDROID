package chat.hola.com.app.trendingDetail.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ezcall.android.R;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * <h>TrendingDtlAdapter</h>
 * <p> This Adapter is used by {@link TrendingDetail} activity recyclerView.</p>
 *
 * @author 3Embed
 * @version 1.0
 * @since 14/2/18.
 */

public class TrendingAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<Data> data = new ArrayList<>();
    private ClickListner clickListner;

    @Inject
    public TrendingAdapter(Context context, List<Data> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_detail_item, parent, false), clickListner);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        initHolder(viewHolder);
    }

    private void initHolder(ViewHolder holder) {
        int position = holder.getAdapterPosition();
        try {
            if (position != -1) {
                Data data = this.data.get(position);
                if (data != null) {
                    if (data.getTrending_score() == null) {
                        data.setTrending_score("Score is not coming");
                    }
                    holder.tvScore.setText(data.getTrending_score());
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
                    if (data.getMediaType1() != null && data.getMediaType1() == 0) {
                        holder.ibPlay.setVisibility(View.GONE);
                        //image
                        if (data.getPurchased()) {
                            holder.rlLockedPost.setVisibility(View.GONE);
                        } else {
                            holder.rlLockedPost.setVisibility(View.VISIBLE);
                        }
                        Glide.with(context)
                            .load(Utilities.getModifiedImageLink(data.getImageUrl1()))
                            .thumbnail(thumbnail)
                            .dontAnimate()
                            .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_default))
                            .into(holder.ivImage);
                    } else {
                        //video
                        holder.ibPlay.setVisibility(View.VISIBLE);

                        String videoCoverImageUrl;
                        if (data.getPurchased()) {
                            videoCoverImageUrl =
                                Utilities.getModifiedImageLink(data.getImageUrl1());
                            holder.rlLockedPost.setVisibility(View.GONE);
                        } else {
                            videoCoverImageUrl = thumbnailUrl;
                            holder.rlLockedPost.setVisibility(View.VISIBLE);
                        }

                        Glide.with(context)
                            .load(videoCoverImageUrl)
                            .dontAnimate()
                            .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_default))
                            .thumbnail(thumbnail)
                            .into(holder.ivImage);
                    }
                }
            }
        } catch (Exception ignore) {
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void setListener(ClickListner clickListner) {
        this.clickListner = clickListner;
    }
}