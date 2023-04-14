package chat.hola.com.app.live_stream.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.CountFormat;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.RTMPStreamPlayerActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.WebRTCStreamPlayerActivity;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;

/**
 * Created by moda on 12/18/2018.
 */
public class LiveBroadcastersAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<AllStreamsData> dataStreams;
    private ClickListner listner;

    public LiveBroadcastersAdapter(Context mContext, ArrayList<AllStreamsData> dataStreams) {
        this.mContext = mContext;
        this.dataStreams = dataStreams;
    }

    public void setListner(ClickListner listner) {
        this.listner = listner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.broadcasters_list_item, viewGroup, false);
        return new LiveBroadcastersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        LiveBroadcastersViewHolder holder = (LiveBroadcastersViewHolder) viewHolder;

        try {
            holder.tvBroadCasterName.setText(dataStreams.get(i).getUserName());
            //http://s3-ap-southeast-1.amazonaws.com/appscrip/LiveStream/thumbNail/1545401039953.jpg

            holder.tvBroadCasterliveViewer.setText(CountFormat.format(Long.parseLong(String.valueOf(dataStreams.get(i).getViewers()))));
            String thumbNail;
            if (!dataStreams.get(i).getThumbnail().contains("http")) {
                thumbNail = "http:" + dataStreams.get(i).getThumbnail();
            } else {
                thumbNail = dataStreams.get(i).getThumbnail();
            }
            thumbNail = thumbNail.replace(".jpg", ".webp").replace(".jpeg", ".webp").replace(".png", ".webp");
            try {
                Glide.with(mContext).load(thumbNail).asBitmap().centerCrop().signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(new BitmapImageViewTarget(holder.ivBroadcasterThumbNail));
                Glide.with(mContext)
                        .load(dataStreams.get(i).getUserImage()).asBitmap().centerCrop()
                        .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(holder.ivProfilePic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                            }
                        });

            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }
            holder.cbFollow.setOnCheckedChangeListener(null);
            holder.cbFollow.setChecked(dataStreams.get(i).isFollowing());
            holder.cbFollow.setOnCheckedChangeListener((compoundButton, isChecked) -> new Handler(Looper.getMainLooper()).post(() -> listner.follow(isChecked, dataStreams.get(i).getUserId())));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataStreams.size();
    }

    class LiveBroadcastersViewHolder extends RecyclerView.ViewHolder {
        TextView tvBroadCasterName, tvBroadCasterliveViewer;
        ImageView ivBroadcasterThumbNail, ivProfilePic;
        CheckBox cbFollow;

        LiveBroadcastersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBroadCasterName = itemView.findViewById(R.id.tvBroadcasterName);
            ivBroadcasterThumbNail = itemView.findViewById(R.id.ivBroadcasterThumbnail);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvBroadCasterliveViewer = itemView.findViewById(R.id.tvBroadcastViewers);
            cbFollow = itemView.findViewById(R.id.cbFollow);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    if (ApiOnServer.STREAMING_TYPE == 0) {
                        //RTMP Streaming
                        intent = new Intent(mContext, RTMPStreamPlayerActivity.class);
                    } else {
                        //WebRTC Streaming
                        intent = new Intent(mContext, WebRTCStreamPlayerActivity.class);
                        //  intent = new Intent(mContext, GroupStreamsPlayerActivity.class);
                    }
                    //                    Intent intent = new Intent(mContext, LiveStreamPlayerIJK.class);
                    intent.putExtra("streamName", dataStreams.get(getAdapterPosition()).getName());
                    intent.putExtra("streamId", dataStreams.get(getAdapterPosition()).getId());
                    intent.putExtra("viewers", dataStreams.get(getAdapterPosition()).getViewers());
                    intent.putExtra("startTime", dataStreams.get(getAdapterPosition()).getStarted());
                    intent.putExtra("data", dataStreams.get(getAdapterPosition()));

                    mContext.startActivity(intent);
                }
            });
        }
    }

    public void setDataStream(ArrayList<AllStreamsData> data) {
        dataStreams.clear();
        dataStreams = data;
        notifyDataSetChanged();
    }

    public interface ClickListner {
        void follow(boolean following, String userId);
    }
}
