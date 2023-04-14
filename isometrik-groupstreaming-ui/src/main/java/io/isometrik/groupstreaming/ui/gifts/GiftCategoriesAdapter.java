package io.isometrik.groupstreaming.ui.gifts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.groupstreaming.ui.R;
import java.util.ArrayList;

/**
 * Recycler view adapter for the Gift categories items{@link GiftCategoryModel}.
 *
 * @see GiftCategoryModel
 */
public class GiftCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private ArrayList<GiftCategoryModel> allGiftsData;
  private Context mContext;

  /**
   * Instantiates a new Gift categories adapter.
   *
   * @param allGiftsData the all gifts data
   * @param mContext the m context
   */
  public GiftCategoriesAdapter(ArrayList<GiftCategoryModel> allGiftsData, Context mContext) {

    this.allGiftsData = allGiftsData;
    this.mContext = mContext;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    View v =
        LayoutInflater.from(mContext).inflate(R.layout.ism_gift_category_item, viewGroup, false);
    return new GiftCategoriesAdapter.ViewHolderGiftCategories(v);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    try {
      GiftCategoriesAdapter.ViewHolderGiftCategories holder =
          (GiftCategoriesAdapter.ViewHolderGiftCategories) viewHolder;

      holder.tvGiftCategoryName.setText(allGiftsData.get(position).getGiftCategoryName());
      holder.tvGiftCategoryImage.setImageResource(
          allGiftsData.get(position).getGiftCategoryImage());

      if (allGiftsData.get(position).isSelected()) {

        holder.tvGiftCategoryImage.setColorFilter(
            ContextCompat.getColor(mContext, R.color.ism_white),
            android.graphics.PorterDuff.Mode.SRC_IN);

        holder.tvGiftCategoryName.setSelected(true);
      } else {

        holder.tvGiftCategoryImage.setColorFilter(
            ContextCompat.getColor(mContext, R.color.ism_grey),
            android.graphics.PorterDuff.Mode.SRC_IN);
        holder.tvGiftCategoryName.setSelected(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return allGiftsData.size();
  }

  private static class ViewHolderGiftCategories extends RecyclerView.ViewHolder {

    private TextView tvGiftCategoryName;
    private AppCompatImageView tvGiftCategoryImage;

    private ViewHolderGiftCategories(@NonNull View itemView) {
      super(itemView);
      tvGiftCategoryName = itemView.findViewById(R.id.tvGiftCategoryName);
      tvGiftCategoryImage = itemView.findViewById(R.id.tvGiftCategoryImage);
    }
  }
}
