package chat.hola.com.app.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WalletTransactionData;
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
public class WalletTransactionAdapter extends RecyclerView.Adapter<WalletTransactionViewHolder> implements Constants.Transaction {

    private List<WalletTransactionData> transactionDataList;
    private TypefaceManager typefaceManager;
    private Context context;
    private ClickListner clickListner;
    private SessionManager sessionManager;

    @Inject
    public WalletTransactionAdapter(Context context, TypefaceManager typefaceManager, List<WalletTransactionData> transactionDataList, SessionManager sessionManager) {
        this.transactionDataList = transactionDataList;
        this.typefaceManager = typefaceManager;
        this.context = context;
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public WalletTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_transaction, parent, false);
        return new WalletTransactionViewHolder(typefaceManager, view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WalletTransactionViewHolder holder, int position) {
        WalletTransactionData data = transactionDataList.get(position);
        int color;
        switch (data.getTxnType()) {
            case Type.CREDIT:
                //credit
                color = R.drawable.circle_small_green;
                break;
            case Type.DEBIT:
                //debit
                color = R.drawable.circle_small_red;
                break;
            default:
                //other transaction
                color = R.drawable.circle_small_yellow;
                break;
        }
        holder.ivMark.setImageDrawable(context.getDrawable(color));
        String currencySymbol = data.getCurrencySymbol() == null || data.getCurrencySymbol().isEmpty() ? sessionManager.getCurrencySymbol() : data.getCurrencySymbol();
        holder.tvAmount.setText(currencySymbol + "" + data.getAmount());
        holder.tvDescription.setText(data.getDescription());
        holder.tvTransactionId.setText(context.getString(R.string.txn_no) + data.getTxnId());
        holder.tvTransactionDate.setText(Utilities.getDate(data.getTimestamp()));

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
        void onItemClick(WalletTransactionData data);
    }
}
