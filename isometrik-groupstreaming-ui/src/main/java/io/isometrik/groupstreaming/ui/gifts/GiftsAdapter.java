package io.isometrik.groupstreaming.ui.gifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import io.isometrik.groupstreaming.ui.R;
import java.util.ArrayList;

/**
 * Recycler view adapter for the Gifts items{@link GiftsModel}.
 *
 * @see GiftsModel
 */
public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.ViewHolderGifts> {

  private ArrayList<GiftsModel> gifts;
  private Context mContext;

  /**
   * Instantiates a new Gifts adapter.
   *
   * @param gifts the gifts
   * @param mContext the m context
   */
  public GiftsAdapter(ArrayList<GiftsModel> gifts, Context mContext) {
    this.gifts = gifts;
    this.mContext = mContext;
  }

  @NonNull
  @Override
  public ViewHolderGifts onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v =
        LayoutInflater.from(mContext).inflate(R.layout.ism_gift_item, viewGroup, false);
    return new ViewHolderGifts(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolderGifts viewHolder, int position) {
    try {
      GiftsModel gift = gifts.get(position);

      viewHolder.tvGiftName.setText(gift.getGiftName());
      viewHolder.tvCoinValue.setText(
          mContext.getString(R.string.ism_coins, String.valueOf(gift.getCoinValue())));

      try {

        Glide.with(mContext).load(gift.getMessage()).fitCenter().into(viewHolder.ivGift);
      } catch (IllegalArgumentException | NullPointerException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return gifts.size();
  }

  /**
   * The type View holder gifts.
   */
  static class ViewHolderGifts extends RecyclerView.ViewHolder {
    private AppCompatImageView ivGift;
    private TextView tvGiftName, tvCoinValue;

    private ViewHolderGifts(@NonNull View itemView) {
      super(itemView);
      tvGiftName = itemView.findViewById(R.id.tvGiftName);
      tvCoinValue = itemView.findViewById(R.id.tvCoinValue);
      ivGift = itemView.findViewById(R.id.ivGift);
    }
  }
}