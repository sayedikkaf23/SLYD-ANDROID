package chat.hola.com.app.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>WalletTransactionViewHolder</h1>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public class WalletTransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivMark)
    public ImageView ivMark;
    @BindView(R.id.tvTransactionId)
    public TextView tvTransactionId;
    @BindView(R.id.tvDescription)
    public TextView tvDescription;
    @BindView(R.id.tvAmount)
    public TextView tvAmount;
    @BindView(R.id.tvTransactionDate)
    public TextView tvTransactionDate;
    @BindView(R.id.ivNextArrow)
    @Nullable
    public ImageView ivNextArrow;

    public WalletTransactionViewHolder(TypefaceManager typefaceManager, @NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvAmount.setTypeface(typefaceManager.getSemiboldFont());
        tvTransactionId.setTypeface(typefaceManager.getMediumFont());
        tvDescription.setTypeface(typefaceManager.getRegularFont());
        tvTransactionDate.setTypeface(typefaceManager.getRegularFont());
    }
}
