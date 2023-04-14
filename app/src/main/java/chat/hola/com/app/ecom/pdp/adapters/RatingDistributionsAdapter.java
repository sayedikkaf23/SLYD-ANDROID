package chat.hola.com.app.ecom.pdp.adapters;

import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemProductDitributionRatingBinding;
import com.kotlintestgradle.model.ecom.pdp.Rating;
import java.util.ArrayList;

/**
 * adapter class for rating distribution
 */
public class RatingDistributionsAdapter extends
    RecyclerView.Adapter<RatingDistributionsAdapter.RatingDistributionsViewHolder> {
  private ArrayList<Rating> mRatingArrayList;
  private Context mContext;

  /**
   * rating distribution adapter
   *
   * @param ratingArrayList rating arrayList
   */
  public RatingDistributionsAdapter(ArrayList<Rating> ratingArrayList) {
    this.mRatingArrayList = ratingArrayList;
  }

  @NonNull
  @Override
  public RatingDistributionsAdapter.RatingDistributionsViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemProductDitributionRatingBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_product_ditribution_rating, parent, false);
    return new RatingDistributionsViewHolder(binding);
  }

  @SuppressLint("DefaultLocale")
  @Override
  public void onBindViewHolder(@NonNull RatingDistributionsViewHolder holder, int position) {
    Rating rating = mRatingArrayList.get(position);
    if (rating != null) {
      holder.mBinding.tvProductRating.setText(String.format("%d", rating.getRatingRange()));
      holder.mBinding.tvProductTotalRating.setText(String.format("%d", rating.getRatingCount()));
      if (rating.getRatingRange() == ONE) {
        holder.mBinding.pbDistribution.setProgressDrawable(
            mContext.getResources().getDrawable(R.drawable.custom_progressbar_coral_red));
      } else if (rating.getRatingRange() == TWO) {
        holder.mBinding.pbDistribution.setProgressDrawable(
            mContext.getResources().getDrawable(R.drawable.custom_progressbar_orange));
      } else {
        holder.mBinding.pbDistribution.setProgressDrawable(
            mContext.getResources().getDrawable(R.drawable.custom_progressbar));
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        holder.mBinding.pbDistribution.setProgress(rating.getPercentage(), TRUE);
      }
    }
  }

  @Override
  public int getItemCount() {
    return mRatingArrayList != null ? mRatingArrayList.size() : ZERO;
  }

  class RatingDistributionsViewHolder extends RecyclerView.ViewHolder {
    ItemProductDitributionRatingBinding mBinding;

    RatingDistributionsViewHolder(@NonNull ItemProductDitributionRatingBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}