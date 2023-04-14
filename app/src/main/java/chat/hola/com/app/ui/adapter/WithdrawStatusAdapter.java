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
import chat.hola.com.app.models.WithdawStatus;
import chat.hola.com.app.ui.viewholder.WithdrawStatusViewHolder;

/**
 * <h1>WithdrawStatusAdapter</h1>
 *
 * <p>Displays all the transaction detail list</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public class WithdrawStatusAdapter extends RecyclerView.Adapter<WithdrawStatusViewHolder> implements Constants.Transaction {

    private List<WithdawStatus> withdrawStatuses;
    private TypefaceManager typefaceManager;
    private Context context;
    private ClickListner clickListner;

    @Inject
    public WithdrawStatusAdapter(Context context, TypefaceManager typefaceManager, List<WithdawStatus> withdrawStatus) {
        this.withdrawStatuses = withdrawStatus;
        this.typefaceManager = typefaceManager;
        this.context = context;
    }

    public void setData(List<WithdawStatus> withdrawStatuses) {
        this.withdrawStatuses = withdrawStatuses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WithdrawStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_withdraw_status, parent, false);
        return new WithdrawStatusViewHolder(typefaceManager, view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WithdrawStatusViewHolder holder, int position) {
        WithdawStatus data = withdrawStatuses.get(position);
        int color;
        switch (data.getStatus()) {
            case WithdrawStatus.NEW:
                color = R.drawable.circle_small_blue;
//                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.light_blue_A400));
                break;
            case WithdrawStatus.APPROVED:
            case WithdrawStatus.TRANSFERRED:
                color = R.drawable.circle_small_green;
//                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case WithdrawStatus.FAILED:
            case WithdrawStatus.CANCELED:
            case WithdrawStatus.REJECTED:
                color = R.drawable.circle_small_red;
//                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                break;
            default:
                color = R.drawable.circle_small_yellow;
//                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.yellow));
                break;
        }
        holder.ivMark.setImageDrawable(context.getDrawable(color));

        String n = data.getStatus().toLowerCase();
        String s1 = n.toLowerCase().substring(0, 1).toUpperCase();
        String name = s1 + n.substring(1);
        holder.tvStatus.setText(name);

        holder.tvDate.setText(Utilities.getDate(data.getTimestamp()));

        holder.itemView.setOnClickListener(view -> {
            if (clickListner != null)
                clickListner.onItemClick(data);
        });
    }

    @Override
    public int getItemCount() {
        return withdrawStatuses.size();
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
        void onItemClick(WithdawStatus data);
    }
}
