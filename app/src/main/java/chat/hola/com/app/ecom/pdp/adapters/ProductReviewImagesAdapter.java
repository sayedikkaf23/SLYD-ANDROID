package chat.hola.com.app.ecom.pdp.adapters;

import static chat.hola.com.app.Utilities.Constants.ONE_FIFTY_FIVE;
import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemReviewImagesBinding;
import java.util.ArrayList;

/**
 * adapter class for the review images
 */
public class ProductReviewImagesAdapter extends
    RecyclerView.Adapter<ProductReviewImagesAdapter.ReviewImagesViewHolder> {
  private ArrayList<String> mReviewImages;
  private Context mContext;

  /**
   * constructor for review images.
   *
   * @param reviewImages review images arrayList.
   */
  public ProductReviewImagesAdapter(ArrayList<String> reviewImages) {
    this.mReviewImages = reviewImages;
  }

  @NonNull
  @Override
  public ProductReviewImagesAdapter.ReviewImagesViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    this.mContext = parent.getContext();
    ItemReviewImagesBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_review_images, parent, false);
    return new ReviewImagesViewHolder(binding);
  }

  @SuppressLint("CheckResult")
  @Override
  public void onBindViewHolder(@NonNull ProductReviewImagesAdapter.ReviewImagesViewHolder holder,
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
              Bitmap.createScaledBitmap(circularBitmapDrawable.getBitmap(), ONE_FIFTY_FIVE, ONE_FIFTY_FIVE, false);
              circularBitmapDrawable.setCircular(true);
              holder.mBinding.ivReview.setImageDrawable(circularBitmapDrawable);
            }
          });
    }
  }

  @Override
  public int getItemCount() {
    return mReviewImages != null ? mReviewImages.size() : ZERO;
  }

  /**
   * view holder class for the review images.
   */
  class ReviewImagesViewHolder extends RecyclerView.ViewHolder {
    private ItemReviewImagesBinding mBinding;

    ReviewImagesViewHolder(@NonNull ItemReviewImagesBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}