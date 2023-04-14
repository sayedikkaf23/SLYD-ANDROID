package chat.hola.com.app.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WithdrawLog;
import chat.hola.com.app.ui.viewholder.WalletTransactionViewHolder;

/**
 * <h1>WalletTransactionAdapter</h1>
 *
 * <p>Displays all the transaction detail list</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public class WithdrawLogsAdapter extends RecyclerView.Adapter<WalletTransactionViewHolder> implements Constants.Transaction {

    private List<WithdrawLog.DataResponse.Data> transactionDataList;
    private TypefaceManager typefaceManager;
    private Context context;
    private ClickListner clickListner;
    private SessionManager sessionManager;

    @Inject
    public WithdrawLogsAdapter(Context context, TypefaceManager typefaceManager, List<WithdrawLog.DataResponse.Data> transactionDataList, SessionManager sessionManager) {
        this.transactionDataList = transactionDataList;
        this.typefaceManager = typefaceManager;
        this.context = context;
        this.sessionManager = sessionManager;
    }

    public void setData(List<WithdrawLog.DataResponse.Data> transactionDataList) {
        this.transactionDataList.addAll(transactionDataList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WalletTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_withdraw_log, parent, false);
        return new WalletTransactionViewHolder(typefaceManager, view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WalletTransactionViewHolder holder, int position) {
        WithdrawLog.DataResponse.Data data = transactionDataList.get(position);
        int color;
        switch (data.getStatus()) {
            case WithdrawStatus.NEW:
                color = R.drawable.circle_small_blue;
                holder.tvAmount.setTextColor(context.getResources().getColor(R.color.light_blue_A400));
                break;
            case WithdrawStatus.APPROVED:
            case WithdrawStatus.TRANSFERRED:
                color = R.drawable.circle_small_green;
                holder.tvAmount.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case WithdrawStatus.FAILED:
            case WithdrawStatus.CANCELED:
            case WithdrawStatus.REJECTED:
                color = R.drawable.circle_small_red;
                holder.tvAmount.setTextColor(context.getResources().getColor(R.color.red));
                break;
            default:
                color = R.drawable.circle_small_yellow;
                holder.tvAmount.setTextColor(context.getResources().getColor(R.color.yellow));
                break;
        }
        holder.ivMark.setImageDrawable(context.getDrawable(color));
        holder.tvAmount.setText(data.getStatus());
        holder.tvDescription.setText(sessionManager.getCurrencySymbol() + "" + data.getAmount());
        holder.tvTransactionId.setText(context.getString(R.string.withdraw_id) + data.getWithdrawid());
        holder.tvTransactionDate.setText(Utilities.getDate(data.getUpdatedatTimestamp()));
        ImageViewCompat.setImageTintList(holder.ivNextArrow, ColorStateList.valueOf(color));
        holder.itemView.setOnClickListener(view -> {
            if (clickListner != null)
                clickListner.onItemClick(data);
        });
    }

    @Override
    public int getItemCount() {
        return transactionDataList.size();
    }

    // sets listner
    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    // to handle click
    public interface ClickListner {
        /**
         * pass data to detail activity
         *
         * @param data : Transaction data
         */
        void onItemClick(WithdrawLog.DataResponse.Data data);
    }
}
