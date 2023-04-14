package chat.hola.com.app.ecom.pdp.sellerbottomsheet;

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
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemSellerRatingBinding;
import com.kotlintestgradle.model.ecom.pdp.ReviewParameterData;
import java.util.ArrayList;

/**
 * inflates the sellers details in recycler view
 */
public class SellerBottomSheetAdapter extends
    RecyclerView.Adapter<SellerBottomSheetAdapter.ProductRatingOnViewHolder> {
  private Context mContext;
  private ArrayList<ReviewParameterData> mReviewParameterData;

  /**
   * constructor for this rating on adapter.
   *
   * @param reviewParameterData attribute rating mData
   */
  SellerBottomSheetAdapter(
      ArrayList<ReviewParameterData> reviewParameterData) {
    mReviewParameterData = reviewParameterData;
  }

  @NonNull
  @Override
  public SellerBottomSheetAdapter.ProductRatingOnViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemSellerRatingBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_seller_rating, parent, false);
    return new ProductRatingOnViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ProductRatingOnViewHolder holder, int position) {
    ReviewParameterData reviewParameterData = mReviewParameterData.get(position);
    if (reviewParameterData != null) {
      holder.mBinding.tvRatingAttributeName.setText(reviewParameterData.getName());
      if (reviewParameterData.getTotalStarRating() != null) {
        holder.mBinding.tvRating.setText(
            String.format("%.1f", reviewParameterData.getTotalStarRating()));
        int rating = (int) ((reviewParameterData.getTotalStarRating()) * HUNDRED);
        // logic is done to show floating progress
        if (rating >= HUNDRED && rating < TWO_HUNDRED) {
          holder.mBinding.pbTotalRating.setProgressDrawable(
              (mContext.getResources().getDrawable(R.drawable.circle_shape_red)));
        } else if (rating >= TWO_HUNDRED && rating < FOUR_HUNDRED) {
          holder.mBinding.pbTotalRating.setProgressDrawable(
              (mContext.getResources().getDrawable(R.drawable.circle_shape_orange)));
        } else {
          holder.mBinding.pbTotalRating.setProgressDrawable(
              (mContext.getResources().getDrawable(R.drawable.circle_shape_hippie_green)));
        }
        holder.mBinding.pbTotalRating.setProgress(
            rating);
      }
    }
  }

  @Override
  public int getItemCount() {
    return mReviewParameterData != null ? mReviewParameterData.size() : ZERO;
  }

  class ProductRatingOnViewHolder extends RecyclerView.ViewHolder {
    ItemSellerRatingBinding mBinding;

    ProductRatingOnViewHolder(@NonNull ItemSellerRatingBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}