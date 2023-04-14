package chat.hola.com.app.live_stream.adapter;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.live_stream.gift.GiftDataResponse;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.GiftViewHolder> {

    private List<GiftDataResponse.Data.Gift> gifts;
    private Context context;
    private GiftListener listener;

    public GiftAdapter(Context context, GiftListener listener) {
        this.context = context;
        this.listener = listener;
        gifts = new ArrayList<>();
    }

    public void setGifts(List<GiftDataResponse.Data.Gift> gifts) {
        this.gifts = gifts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_livestream_gift, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {
        GiftDataResponse.Data.Gift gift = gifts.get(position);
        holder.tvCoins.setText(gift.getGiftCost());
        Glide.with(context).load(gift.getMobileThumbnail().replace("upload/", "upload/h_40,w_40/")).asBitmap().centerCrop().into(holder.ivGift);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onGiftSelect(gift);
        });
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public class GiftViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGift)
        ImageView ivGift;
        @BindView(R.id.tvCoins)
        TextView tvCoins;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface GiftListener {
        void onGiftSelect(GiftDataResponse.Data.Gift gift);
    }
}
