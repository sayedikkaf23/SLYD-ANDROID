package chat.hola.com.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.models.PaymentGateway;

/**
 * <h1>PaymentGatewayAdapter</h1>
 *
 * <p>Displays all the payment gateways detail list</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public class PaymentGatewayAdapter extends RecyclerView.Adapter<PaymentGatewayAdapter.ViewHolder> {

    private List<PaymentGateway> paymentGateways;
    private TypefaceManager typefaceManager;
    private Context context;
    private ClickListner clickListner;

    @Inject
    public PaymentGatewayAdapter(Context context, TypefaceManager typefaceManager, List<PaymentGateway> paymentGateways) {
        this.paymentGateways = paymentGateways;
        this.typefaceManager = typefaceManager;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_gateway, parent, false);
        return new ViewHolder(typefaceManager, view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentGateway data = paymentGateways.get(position);
        Glide.with(context).load(data.getImage()).asBitmap().centerCrop()
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                .placeholder(R.mipmap.ic_launcher).into(new BitmapImageViewTarget(holder.ivImage) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.ivImage.setImageDrawable(circularBitmapDrawable);
            }
        });
        holder.tvName.setText(data.getName());
        holder.itemView.setOnClickListener(view -> {
            if (clickListner != null)
                clickListner.onItemSelect(data);
        });
    }

    @Override
    public int getItemCount() {
        return paymentGateways.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvName)
        TextView tvName;

        public ViewHolder(TypefaceManager typefaceManager, @NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvName.setTypeface(typefaceManager.getSemiboldFont());
        }

    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void onItemSelect(PaymentGateway data);
    }
}
