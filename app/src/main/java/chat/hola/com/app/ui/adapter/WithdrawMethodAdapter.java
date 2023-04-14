package chat.hola.com.app.ui.adapter;

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

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.models.WithdrawMethod;

public class WithdrawMethodAdapter extends RecyclerView.Adapter<WithdrawMethodAdapter.WithdrawMethodViewHolder> {

    private Context context;
    private List<WithdrawMethod> withdrawMethods;
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;

    @Inject
    public WithdrawMethodAdapter(Context context, List<WithdrawMethod> withdrawMethods, TypefaceManager typefaceManager) {
        this.context = context;
        this.withdrawMethods = withdrawMethods;
        this.typefaceManager = typefaceManager;
    }

    @NonNull
    @Override
    public WithdrawMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WithdrawMethodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_withdraw_method, parent, false), typefaceManager);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawMethodViewHolder holder, int position) {
        WithdrawMethod method = withdrawMethods.get(position);
        holder.tvName.setText(method.getName());
        Glide.with(context).load(method.getImgUrl()).placeholder(R.drawable.ic_bank).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return withdrawMethods.size();
    }

    public void setData(List<WithdrawMethod> withdrawMethods) {
        this.withdrawMethods = withdrawMethods;
        notifyDataSetChanged();
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void onItemSelect(WithdrawMethod method);
    }

    class WithdrawMethodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvName)
        TextView tvName;


        WithdrawMethodViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvName.setTypeface(typefaceManager.getRegularFont());
            itemView.setOnClickListener(v -> {
                if (clickListner != null)
                    clickListner.onItemSelect(withdrawMethods.get(getAdapterPosition()));
            });
        }
    }
}
