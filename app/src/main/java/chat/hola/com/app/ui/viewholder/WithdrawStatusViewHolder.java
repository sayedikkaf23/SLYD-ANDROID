package chat.hola.com.app.ui.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
public class WithdrawStatusViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivMark)
    public ImageView ivMark;
    @BindView(R.id.tvStatus)
    public TextView tvStatus;
    @BindView(R.id.tvDate)
    public TextView tvDate;

    public WithdrawStatusViewHolder(TypefaceManager typefaceManager, @NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvStatus.setTypeface(typefaceManager.getSemiboldFont());
        tvDate.setTypeface(typefaceManager.getMediumFont());
    }
}
