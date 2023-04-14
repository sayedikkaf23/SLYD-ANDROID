package chat.hola.com.app.ui.withdraw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.models.StripeResponse;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.ViewHolder> {
    private int lastSelectedPosition = -1;
    private List<StripeResponse.Data.ExternalAccounts.Account> data;
    private TypefaceManager typefaceManager;
    private Context context;
    private ClickListner clickListner;

    @Inject
    public BankAccountAdapter(Context context, List<StripeResponse.Data.ExternalAccounts.Account> data, TypefaceManager typefaceManager) {
        this.data = data;
        this.typefaceManager = typefaceManager;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(typefaceManager,
                LayoutInflater.from(context).inflate(R.layout.bank_account_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StripeResponse.Data.ExternalAccounts.Account account = data.get(position);
        holder.tVAccountNum.setText(R.string.account_no_demo + account.getLast4());
        holder.tVBankCode.setText(account.getBankName());
        holder.rbSelect.setChecked(lastSelectedPosition == position);
//        holder.cb.setChecked(data.get(position).isSelected());

//        holder.cb.setOnCheckedChangeListener((buttonView, isChecked) -> data.get(position).setSelected(isChecked));
//        holder.itemView.setOnClickListener(view -> {
//            if (clickListner != null)
//                clickListner.onItemSelect(data.get(position));
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rbSelect)
        RadioButton rbSelect;
        @BindView(R.id.tVAccountNum)
        TextView tVAccountNum;
        @BindView(R.id.tVBankCode)
        TextView tVBankCode;
        @BindView(R.id.ivNext)
        ImageView ivNext;

        public ViewHolder(TypefaceManager typefaceManager, @NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tVAccountNum.setTypeface(typefaceManager.getRegularFont());
            tVBankCode.setTypeface(typefaceManager.getRegularFont());

            rbSelect.setOnClickListener(v -> {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
                if (clickListner != null)
                    clickListner.onItemSelect(data.get(lastSelectedPosition));
            });

            ivNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListner != null)
                        clickListner.onDetail(data.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface ClickListner {
        void onItemSelect(StripeResponse.Data.ExternalAccounts.Account data);

        void onDetail(StripeResponse.Data.ExternalAccounts.Account data);
    }
}
