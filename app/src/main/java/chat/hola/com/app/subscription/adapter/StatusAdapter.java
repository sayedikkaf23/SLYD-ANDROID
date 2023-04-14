package chat.hola.com.app.subscription.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemSubscriptionsBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.subscription.model.SubData;

public class StatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SubData> data;
    boolean isActive = false;
    ClickListener clickListener;

    public StatusAdapter(Context context, List<SubData> data, boolean isActive) {
        this.context = context;
        this.data = data;
        this.isActive = isActive;
    }

    public void setData(List<SubData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscriptionsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_subscriptions,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSubscriptionsBinding itemSubscriptionsBinding;
        public MyViewHolder(ItemSubscriptionsBinding itemSubscriptionsBinding) {
            super(itemSubscriptionsBinding.getRoot());
            this.itemSubscriptionsBinding = itemSubscriptionsBinding;
        }

        public void bind(int position) {
            SubData d = data.get(position);
            itemSubscriptionsBinding.tvTitle.setText(d.getFirstName() + " " + d.getLastName());
            String desc;
            if (isActive) {
                desc = d.getAmount() + " " + context.getString(R.string.coins)
                        + " " + context.getString(R.string.charged_on)
                        + " " + Utilities.getDateDDMMYYYY(d.getEndDate());
            }else {
                desc = d.getAmount() + " " + context.getString(R.string.coins)
                        + " " + context.getString(R.string.charged_on)
                        + " " + context.getString(R.string.every_month);
            }

            itemSubscriptionsBinding.tvDesc.setText(desc);
            itemSubscriptionsBinding.tvStatus.setText(isActive ?
                    d.isSubscriptionCancelled() ? context.getString(R.string.cancelled) : context.getString(R.string.cancel_text)
                    : context.getString(R.string.renew));
            itemSubscriptionsBinding.tvStatus.setTextColor(isActive
                    ? ContextCompat.getColor(context,R.color.red)
                    : ContextCompat.getColor(context,R.color.colorPrimary));
            itemSubscriptionsBinding.tvStatus.setOnClickListener(v -> {
                clickListener.onItemClick(position,d);
            });
            Glide.with(context)
                    .load(d.getProfilePic())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.profile_one)
                    .into(new BitmapImageViewTarget(itemSubscriptionsBinding.ivProfilePic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            itemSubscriptionsBinding.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }
    }

    public interface ClickListener{
        void onItemClick(int position, SubData d);
    }
}
