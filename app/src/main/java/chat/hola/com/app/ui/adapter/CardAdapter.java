package chat.hola.com.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.Card;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private int lastSelectedPosition = -1;

    private Context context;
    private List<Card> cards;
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;

    @Inject
    public CardAdapter(Context context, List<Card> cards, TypefaceManager typefaceManager) {
        this.context = context;
        this.cards = cards;
        this.typefaceManager = typefaceManager;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false), typefaceManager);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.ivImage.setImageBitmap(Utilities.setCreditCardLogo(card.getBrand(), context));
        holder.tvBankAccount.setText("XXX XXX " + card.getLast4());
        String year = card.getExpYear().substring(Math.max(card.getExpYear().length() - 2, 0));
        holder.tvExpDate.setText(card.getExpMonth() + "/" + year);
        holder.rbSelect.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void setData(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void onCardSelect(Card card);

        void onCardDelete(Card card);
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rbSelect)
        RadioButton rbSelect;
        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.tvBankAccount)
        TextView tvBankAccount;
        @BindView(R.id.tvExpDate)
        TextView tvExpDate;
        @BindView(R.id.ibDelete)
        ImageButton ibDelete;

        CardViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvBankAccount.setTypeface(typefaceManager.getRegularFont());
            tvExpDate.setTypeface(typefaceManager.getRegularFont());

            rbSelect.setOnClickListener(v -> {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
                if (clickListner != null)
                    clickListner.onCardSelect(cards.get(lastSelectedPosition));
            });

            ibDelete.setOnClickListener(v -> {
                if (clickListner != null)
                    clickListner.onCardDelete(cards.get(getAdapterPosition()));
            });
        }
    }
}
