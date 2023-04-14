package chat.hola.com.app.search.channel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import chat.hola.com.app.AppController;
import chat.hola.com.app.search.channel.module.Channels;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.List;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {
    private AppController appController;
    private Typeface fontMedium;
    private List<Channels> searchData;
    private Context context;
    private ChannelAdapter.ClickListner clickListner;

    //    @Inject
    //    public ChannelAdapter(List<Data> arrayList, LandingActivity mContext, TypefaceManager typefaceManager) {
    //    }

    public void setData(Context context, List<Channels> data) {
        this.context = context;
        this.searchData = data;
        notifyDataSetChanged();
    }

    @Override
    public ChannelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_channel_row, parent, false);
        appController = AppController.getInstance();
        fontMedium = appController.getMediumFont();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChannelAdapter.ViewHolder holder, final int position) {
        if (getItemCount() != -1) {
            Channels data = searchData.get(position);

            Glide.with(context).load(data.getChannelImageUrl()).asBitmap().centerCrop()
                    .placeholder(R.drawable.ic_channel_default)
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .into(new BitmapImageViewTarget(holder.profileIv));
            holder.tvSubscriber.setText(data.getSubscriber() + " subscribers");
            holder.profileNameTv.setText(data.getChannelName());
            boolean isMine = data.getUserId().equals(AppController.getInstance().getUserId());
            holder.subscribe.setVisibility(isMine ? View.GONE : View.VISIBLE);
            if (!isMine) {
                int offText = -1, onText = -1;
                boolean isChecked = false;
                switch (data.getSubscribeStatus()) {
                    case 0:
                        //not subscribed
                        offText = R.string.subscribe;
                        onText = data.getPrivate() ? R.string.requested : R.string.subscribed;
                        break;
                    case 1:
                        // subscribed
                        onText = R.string.subscribed;
                        offText = R.string.subscribed;
                        isChecked = true;
                        break;
                    case 2:
                        // requested
                        onText = R.string.requested;
                        offText = R.string.request;
                        isChecked = true;
                        break;
                }
                try {

                    if (offText != -1)
                        holder.subscribe.setTextOff(context.getResources().getString(offText));
                    if (onText != -1)
                        holder.subscribe.setTextOn(context.getResources().getString(onText));
                } catch (Exception ignored) {

                }

                holder.subscribe.setChecked(isChecked);

                boolean finalIsChecked = isChecked;
                holder.subscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListner.onSubscribe(position, finalIsChecked);
                    }
                });
            }
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListner.onItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return searchData != null ? searchData.size() : -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileIv;
        private TextView profileNameTv, tvSubscriber;
        private RelativeLayout relativeLayout;
        private ToggleButton subscribe;

        public ViewHolder(View itemView) {
            super(itemView);
            profileNameTv = (TextView) itemView.findViewById(R.id.profileNameTv);
            profileIv = (ImageView) itemView.findViewById(R.id.profileIv);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rlItem);
            tvSubscriber = (TextView) itemView.findViewById(R.id.tvName);
            subscribe = (ToggleButton) itemView.findViewById(R.id.tbSubscribe);
            profileNameTv.setTypeface(fontMedium);
            tvSubscriber.setTypeface(fontMedium);
        }
    }

    public void setListener(ChannelAdapter.ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void onItemClick(int position);

        void onSubscribe(int position, boolean flag);
    }
}
