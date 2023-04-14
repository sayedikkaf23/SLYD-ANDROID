package chat.hola.com.app.ecom.review;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.RATING_TYPE;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemReviewProductBinding;
import com.kotlintestgradle.model.ecom.getratable.RatableAttributesData;
import java.util.ArrayList;

/**
 * adapter class for the rating types
 */
public class RatingTypesAdapter extends
    RecyclerView.Adapter<RatingTypesAdapter.ReviewProductViewHolder> {
  private ArrayList<RatableAttributesData> mRatingAttributesList;
  private RatingOrReviewCallBack mRatingOrReviewCallBack;
  private Context mContext;
  private int mReviewType;

  /**
   * constructor class for this adapter
   *
   * @param ratingAttributesList   arraylist of rating attributes
   * @param ratingOrReviewCallBack rating review call back
   */
  RatingTypesAdapter(
      ArrayList<RatableAttributesData> ratingAttributesList,
      RatingOrReviewCallBack ratingOrReviewCallBack, int reviewType) {
    mRatingAttributesList = ratingAttributesList;
    mRatingOrReviewCallBack = ratingOrReviewCallBack;
    mReviewType = reviewType;
  }

  @NonNull
  @Override
  public RatingTypesAdapter.ReviewProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    mContext = parent.getContext();
    ItemReviewProductBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_review_product, parent, false);
    return new ReviewProductViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull RatingTypesAdapter.ReviewProductViewHolder holder,
      int position) {
    holder.mBinding.tvRatingType.setText(mRatingAttributesList.get(position).getAttributeName());
    int attributeRating = (int) mRatingAttributesList.get(position).getAttributeRating();
    if (attributeRating != ZERO) {
      for (int i = ONE; i <= attributeRating; i++) {
        switch (i) {
          case ONE:
            activateButton(holder.mBinding.tvRatingOne);
            break;
          case TWO:
            activateButton(holder.mBinding.tvRatingTwo);
            break;
          case THREE:
            activateButton(holder.mBinding.tvRatingThree);
            break;
          case FOUR:
            activateButton(holder.mBinding.tvRatingFour);
            break;
          default:
            activateButton(holder.mBinding.tvRatingFive);
            break;
        }
      }
    }
    holder.mBinding.tvRatingOne.setOnClickListener(view -> {
      if (!holder.mBinding.tvRatingOne.isSelected() || holder.mBinding.tvRatingTwo.isSelected()) {
        mRatingOrReviewCallBack.ratingOrReview(RATING_TYPE, mReviewType,
            mRatingAttributesList.get(position).getAttributeId(), ONE);
      }
      holder.mBinding.tvRatingOne.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingTwo.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingThree.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingFour.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingFive.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingTwo.setSelected(FALSE);
      holder.mBinding.tvRatingThree.setSelected(FALSE);
      holder.mBinding.tvRatingFour.setSelected(FALSE);
      holder.mBinding.tvRatingFive.setSelected(FALSE);
      holder.mBinding.tvRatingOne.setSelected(TRUE);
    });
    holder.mBinding.tvRatingTwo.setOnClickListener(view -> {
      if (!holder.mBinding.tvRatingTwo.isSelected() || holder.mBinding.tvRatingThree.isSelected()) {
        mRatingOrReviewCallBack.ratingOrReview(RATING_TYPE, mReviewType,
            mRatingAttributesList.get(position).getAttributeId(), TWO);
      }
      holder.mBinding.tvRatingTwo.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingOne.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingOne.setSelected(TRUE);
      holder.mBinding.tvRatingThree.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingFour.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingFive.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingThree.setSelected(FALSE);
      holder.mBinding.tvRatingFour.setSelected(FALSE);
      holder.mBinding.tvRatingFive.setSelected(FALSE);
      holder.mBinding.tvRatingTwo.setSelected(TRUE);
    });
    holder.mBinding.tvRatingThree.setOnClickListener(view -> {
      if (!holder.mBinding.tvRatingThree.isSelected()
          || holder.mBinding.tvRatingFour.isSelected()) {
        mRatingOrReviewCallBack.ratingOrReview(RATING_TYPE, mReviewType,
            mRatingAttributesList.get(position).getAttributeId(), THREE);
      }
      holder.mBinding.tvRatingThree.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingOne.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingOne.setSelected(TRUE);
      holder.mBinding.tvRatingTwo.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingTwo.setSelected(TRUE);
      holder.mBinding.tvRatingFour.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingFive.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingFour.setSelected(FALSE);
      holder.mBinding.tvRatingFive.setSelected(FALSE);
      holder.mBinding.tvRatingThree.setSelected(TRUE);
    });
    holder.mBinding.tvRatingFour.setOnClickListener(view -> {
      if (!holder.mBinding.tvRatingFour.isSelected() || holder.mBinding.tvRatingFive.isSelected()) {
        mRatingOrReviewCallBack.ratingOrReview(RATING_TYPE, mReviewType,
            mRatingAttributesList.get(position).getAttributeId(), FOUR);
      }
      holder.mBinding.tvRatingFour.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingOne.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingOne.setSelected(TRUE);
      holder.mBinding.tvRatingTwo.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingTwo.setSelected(TRUE);
      holder.mBinding.tvRatingThree.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingThree.setSelected(TRUE);
      holder.mBinding.tvRatingFive.setTextColor(
          mContext.getResources().getColor(R.color.color_black));
      holder.mBinding.tvRatingFive.setSelected(FALSE);
      holder.mBinding.tvRatingFour.setSelected(TRUE);
    });
    holder.mBinding.tvRatingFive.setOnClickListener(view -> {
      if (!holder.mBinding.tvRatingFive.isSelected()) {
        mRatingOrReviewCallBack.ratingOrReview(RATING_TYPE, mReviewType,
            mRatingAttributesList.get(position).getAttributeId(), FIVE);
      }
      holder.mBinding.tvRatingFive.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingOne.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingOne.setSelected(TRUE);
      holder.mBinding.tvRatingTwo.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingTwo.setSelected(TRUE);
      holder.mBinding.tvRatingThree.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingThree.setSelected(TRUE);
      holder.mBinding.tvRatingFour.setTextColor(
          mContext.getResources().getColor(R.color.allWhiteColor));
      holder.mBinding.tvRatingFour.setSelected(TRUE);
      holder.mBinding.tvRatingFive.setSelected(TRUE);
    });
  }

  /*activates the button clicked*/
  private void activateButton(AppCompatTextView textView) {
    textView.setSelected(TRUE);
    textView.setTextColor(
        mContext.getResources().getColor(R.color.allWhiteColor));
  }

  @Override
  public int getItemCount() {
    return mRatingAttributesList != null ? mRatingAttributesList.size() : ZERO;
  }

  /**
   * review product view holder class
   */
  class ReviewProductViewHolder extends RecyclerView.ViewHolder {
    private ItemReviewProductBinding mBinding;

    ReviewProductViewHolder(@NonNull ItemReviewProductBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}