package chat.hola.com.app.music;

import android.annotation.SuppressLint;
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
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.model.Data;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.ezcall.android.R;
import java.util.List;
import javax.inject.Inject;

/**
 * <h1>HashtagAdapter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private List<Data> dataList;
    private Context mContext;
    private ClickListner clickListner;

    @Inject
    public MusicAdapter(List<Data> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (getItemCount() > 0) {
            try {
                final Data data = dataList.get(position);
                if (data.getTrending_score() != null)
                    data.setTrending_score("No Score...");
                holder.tvScore.setText(data.getTrending_score());
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(holder.ivMedia);

                String thumbnailUrl = data.getThumbnailUrl1();
                if(data.getPurchased()) {
                    if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                        thumbnailUrl = thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
                    }
                }
                DrawableRequestBuilder<String> thumbnailRequest = Glide
                    .with(mContext)
                    .load(Utilities.getModifiedThumbnailLink(thumbnailUrl)).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .override(Integer.parseInt(data.getImageUrl1Width()), Integer.parseInt(data.getImageUrl1Height()));

                if (data.getMediaType1() == 1) {
                    //video
                    holder.imageButton.setVisibility( View.VISIBLE);

                    String videoCoverImageUrl;
                    if (data.getPurchased()) {
                        videoCoverImageUrl = Utilities.getModifiedImageLink(data.getImageUrl1());
                        holder.rlLockedPost.setVisibility(View.GONE);
                    } else {
                        videoCoverImageUrl = thumbnailUrl;
                        holder.rlLockedPost.setVisibility(View.VISIBLE);
                    }

                    Glide.with(mContext)
                            .load(videoCoverImageUrl)
                            .thumbnail(thumbnailRequest)
                            .dontAnimate()
                            .fitCenter()
                            .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_default))
                            .into(imageViewTarget);
                } else {
                    //image
                    holder.imageButton.setVisibility(View.GONE);
                    if ( data.getPurchased()) {
                        holder.rlLockedPost.setVisibility(View.GONE);
                    } else {
                        holder.rlLockedPost.setVisibility(View.VISIBLE);
                    }
                    Glide.with(mContext)
                        .load(Utilities.getModifiedImageLink(data.getImageUrl1()))
                        .dontAnimate()
                        .fitCenter()
                        .thumbnail(thumbnailRequest)
                        .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_default))
                        .into(holder.ivMedia);
                }

            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void onItemSelected(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivMedia)
        ImageView ivMedia;
        @BindView(R.id.tvScore)
        TextView tvScore;
        @BindView(R.id.ibCam)
        ImageView imageButton;
        @BindView(R.id.rlLockedPost)
        RelativeLayout rlLockedPost;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ivMedia)
        public void onItemClick() {
            clickListner.onItemSelected(getAdapterPosition());
        }
    }
}