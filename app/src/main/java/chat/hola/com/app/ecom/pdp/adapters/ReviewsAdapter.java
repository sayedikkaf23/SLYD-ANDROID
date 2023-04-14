package chat.hola.com.app.ecom.pdp.adapters;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.ecom.allreviews.ReviewImgClick;
import chat.hola.com.app.ecom.pdp.ReviewsMenuClick;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemProductReviewsBinding;
import com.kotlintestgradle.model.ecom.pdp.UserReviewData;
import java.util.ArrayList;

/**
 * adapter class for the reviews
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
  private ArrayList<UserReviewData> mUserReviewDataArrayList;
  private ReviewsMenuClick mReviewsMenuClick;
  private ReviewImgClick mReviewImgClick;
  private boolean mIsFromPdp;

  /**
   * constructor for reviews
   *
   * @param userReviewData userdata arrayList
   */
  public ReviewsAdapter(ArrayList<UserReviewData> userReviewData,
      ReviewsMenuClick reviewsMenuClick, ReviewImgClick reviewImgClick, boolean isFromPdp) {
    this.mUserReviewDataArrayList = userReviewData;
    this.mReviewsMenuClick = reviewsMenuClick;
    this.mReviewImgClick = reviewImgClick;
    this.mIsFromPdp = isFromPdp;
  }

  @NonNull
  @Override
  public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    Context context = parent.getContext();
    ItemProductReviewsBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_product_reviews, parent, false);
    return new ReviewsViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
    UserReviewData userReviewData = mUserReviewDataArrayList.get(position);
    if (userReviewData != null) {
      if (userReviewData.getRating() != null) {
        holder.mBinding.tvProductRating.setText(userReviewData.getRating());
      }
      if (userReviewData.getReviewTitle() != null) {
        holder.mBinding.tvReviewTitle.setText(userReviewData.getReviewTitle());
      }
      if (userReviewData.getReviewDesc() != null) {
        holder.mBinding.tvReviewDesc.setText(userReviewData.getReviewDesc());
      }
      holder.mBinding.rvProductReviewsImages.setVisibility(
          (userReviewData.getImages() != null && userReviewData.getImages().size() > ZERO)
              ? View.VISIBLE : View.GONE);
      if (userReviewData.getImages() != null && userReviewData.getImages().size() > ZERO) {
        ReviewImagesAdapter reviewImagesAdapter = new ReviewImagesAdapter(
            userReviewData.getImages(), mReviewImgClick, position);
        holder.mBinding.rvProductReviewsImages.setAdapter(reviewImagesAdapter);
      }
      holder.mBinding.tvLikeCount.setCompoundDrawablesWithIntrinsicBounds(
          userReviewData.getLikeSel() ? R.drawable.pdp_like_sel : R.drawable.pdp_like, ZERO, ZERO,
          ZERO);
      holder.mBinding.tvDislikeCount.setCompoundDrawablesWithIntrinsicBounds(
          userReviewData.getDisLikeSel() ? R.drawable.pdp_dislike_sel : R.drawable.pdp_dislike,
          ZERO,
          ZERO,
          ZERO);
      holder.mBinding.tvLikeCount.setText(String.format("%d", userReviewData.getLikes()));
      holder.mBinding.tvDislikeCount.setText(String.format("%d", userReviewData.getDisLikes()));
      holder.mBinding.tvPersonDet.setText(userReviewData.getName());
      holder.mBinding.tvReviewDate.setText(userReviewData.getTimestamp());
      holder.mBinding.ivReviewOptions.setOnClickListener(
          view -> mReviewsMenuClick.onMenuClick(ONE, position, holder.mBinding.ivReviewOptions,
              FALSE));
      holder.mBinding.tvLikeCount.setOnClickListener(view -> {
        if (!userReviewData.getLikeSel()) {
          mReviewsMenuClick.onMenuClick(TWO, position, null, TRUE);
        }
      });
      holder.mBinding.tvDislikeCount.setOnClickListener(view -> {
        if (!userReviewData.getDisLikeSel()) {
          mReviewsMenuClick.onMenuClick(TWO, position, null, FALSE);
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return (mUserReviewDataArrayList != null && mUserReviewDataArrayList.size() > ZERO) ? (
        mIsFromPdp ? (
            (mUserReviewDataArrayList.size() > TWO) ? THREE : mUserReviewDataArrayList.size())
            : mUserReviewDataArrayList.size()) : ZERO;
  }

  /**
   * view holder class for the reviews
   */
  class ReviewsViewHolder extends RecyclerView.ViewHolder {
    ItemProductReviewsBinding mBinding;

    ReviewsViewHolder(@NonNull ItemProductReviewsBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}