package chat.hola.com.app.ecom.pdp.adapters;

import static chat.hola.com.app.Utilities.Constants.FOUR_HUNDRED;
import static chat.hola.com.app.Utilities.Constants.HUNDRED;
import static chat.hola.com.app.Utilities.Constants.TWO_HUNDRED;
import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemProductRatingOnBinding;
import com.kotlintestgradle.model.ecom.pdp.AttributeRatingData;
import java.util.ArrayList;

/**
 * adapter class for rating on particular attribute
 */
public class ProductRatingOnAdapter extends
    RecyclerView.Adapter<ProductRatingOnAdapter.ProductRatingOnViewHolder> {
  private ArrayList<AttributeRatingData> mAttributeRatingData;
  private Context mContext;

  /**
   * constructor for this rating on adapter.
   *
   * @param attributeRatingData attribute rating mData
   */
  public ProductRatingOnAdapter(
      ArrayList<AttributeRatingData> attributeRatingData) {
    mAttributeRatingData = attributeRatingData;
  }

  @NonNull
  @Override
  public ProductRatingOnAdapter.ProductRatingOnViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemProductRatingOnBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_product_rating_on, parent, false);
    return new ProductRatingOnViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ProductRatingOnViewHolder holder,
      int position) {
    AttributeRatingData attributeRatingData = mAttributeRatingData.get(position);
    if (attributeRatingData != null) {
      holder.mBinding.tvRatingAttributeName.setText(attributeRatingData.getAttributeName());
      if (attributeRatingData.getTotalStarRating() != null) {
        holder.mBinding.tvRating.setText(
            String.format("%.1f", attributeRatingData.getTotalStarRating()));
        int rating = (int) (
            attributeRatingData.getTotalStarRating() * HUNDRED);
        if (rating >= HUNDRED && rating <= TWO_HUNDRED) {
          holder.mBinding.pbTotalRating.setProgressDrawable(
              (mContext.getResources().getDrawable(R.drawable.circle_shape_red)));
        } else if (rating > TWO_HUNDRED && rating <= FOUR_HUNDRED) {
          holder.mBinding.pbTotalRating.setProgressDrawable(
              (mContext.getResources().getDrawable(R.drawable.circle_shape_orange)));
        } else {
          holder.mBinding.pbTotalRating.setProgressDrawable(
              (mContext.getResources().getDrawable(R.drawable.circle_shape_hippie_green)));
        }
        Utilities.printLog(
            "TotalStarRating" + attributeRatingData.getTotalStarRating() + "total" + (int) (
                attributeRatingData.getTotalStarRating() * 100.0f));
        holder.mBinding.pbTotalRating.setProgress(
            rating);
      }
    }
  }

  @Override
  public int getItemCount() {
    return mAttributeRatingData != null ? mAttributeRatingData.size() : ZERO;
  }

  class ProductRatingOnViewHolder extends RecyclerView.ViewHolder {
    ItemProductRatingOnBinding mBinding;

    ProductRatingOnViewHolder(@NonNull ItemProductRatingOnBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}