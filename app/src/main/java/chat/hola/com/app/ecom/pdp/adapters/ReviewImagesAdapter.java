package chat.hola.com.app.ecom.pdp.adapters;

import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.ONE_FIFTY_FIVE;
import static chat.hola.com.app.Utilities.Constants.REVIEW_IMAGE_WIDTH;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.ecom.allreviews.ReviewImgClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemReviewImagesBinding;
import java.util.ArrayList;
import java.util.Objects;

/**
 * adapter class for the review images.
 */
public class ReviewImagesAdapter extends
    RecyclerView.Adapter<ReviewImagesAdapter.ReviewImagesViewHolder> {
  private ArrayList<String> mReviewImages;
  private ReviewImgClick mReviewImgClick;
  private Context mContext;
  private int mReviewPos;

  /**
   * constructor for the review images.
   *
   * @param reviewImages review images arrayList.
   */
  ReviewImagesAdapter(ArrayList<String> reviewImages, ReviewImgClick reviewImgClick,
      int reviewPos) {
    this.mReviewImages = reviewImages;
    this.mReviewImgClick = reviewImgClick;
    this.mReviewPos = reviewPos;
  }

  @NonNull
  @Override
  public ReviewImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    this.mContext = parent.getContext();
    ItemReviewImagesBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_review_images, parent, false);
    return new ReviewImagesViewHolder(binding);
  }

  @SuppressLint("CheckResult")
  @Override
  public void onBindViewHolder(@NonNull ReviewImagesViewHolder holder,
      int position) {
    String imageUrl = mReviewImages.get(position);
    if (imageUrl != null && !"".equals(imageUrl)) {
      Glide.with(mContext).load(imageUrl.trim())
          .asBitmap()
          .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
          into(new BitmapImageViewTarget(holder.mBinding.ivReview) {
            @Override
            protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable circularBitmapDrawable =
                  RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
              Bitmap.createScaledBitmap(circularBitmapDrawable.getBitmap(), REVIEW_IMAGE_WIDTH, REVIEW_IMAGE_WIDTH, false);
              circularBitmapDrawable.setCircular(true);
              holder.mBinding.ivReview.setImageDrawable(circularBitmapDrawable);
            }
          });
    }
    holder.mBinding.ivReview.setOnClickListener(
        view -> mReviewImgClick.onImgClick(position, mReviewPos));
  }

  @Override
  public int getItemCount() {
    return (mReviewImages != null && mReviewImages.size() > FOUR) ? FIVE : Objects.requireNonNull(
        mReviewImages).size();
  }

  /**
   * view holder class for the review images.
   */
  class ReviewImagesViewHolder extends RecyclerView.ViewHolder {
    ItemReviewImagesBinding mBinding;

    ReviewImagesViewHolder(@NonNull ItemReviewImagesBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}