package chat.hola.com.app.wallet.transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.WalletTransactionData;
import chat.hola.com.app.wallet.transaction.Model.TransactionData;

public class TransactionAdap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context mContext;
    ArrayList<WalletTransactionData> list;

    public TransactionAdap(Context mContext, ArrayList<WalletTransactionData> allList) {
        this.mContext = mContext;
        this.list = allList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_row_transaction_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        WalletTransactionData data = list.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        /* txnType | txnTypeCode |
         * -----------------------
         *    1    |  CREDIT     |
         *    2    |  DEBIT      |
         *    3    |  PURCHASE   |
         *    4    |  TRANSFER   |
         * -----------------------*/
        if (data.getTxnType() == 1) {
            myViewHolder.iV_type.setImageDrawable(mContext.getDrawable(R.drawable.ic__trans_credit));
        } else if (data.getTxnType() == 2) {
            myViewHolder.iV_type.setImageDrawable(mContext.getDrawable(R.drawable.ic_trans_debit));
        } else if (data.getTxnType() == 4) {
            myViewHolder.iV_type.setImageDrawable(mContext.getDrawable(R.drawable.ic_trans_debit));
        }

        String amt = data.getAmount();
        myViewHolder.tV_amount.setText(Utilities.formatMoney(Double.parseDouble(amt)));
        myViewHolder.tV_id.setText(data.getTxnId());
        myViewHolder.tV_reason.setText(data.getDescription());

        myViewHolder.tV_time.setText(Utilities.getDate(data.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<WalletTransactionData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iV_type;
        TextView tV_id, tV_reason, tV_time, tV_amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iV_type = (ImageView) itemView.findViewById(R.id.iV_type);
            tV_id = (TextView) itemView.findViewById(R.id.tV_id);
            tV_reason = (TextView) itemView.findViewById(R.id.tV_reason);
            tV_time = (TextView) itemView.findViewById(R.id.tV_time);
            tV_amount = (TextView) itemView.findViewById(R.id.tV_amount);
        }
    }
}
