package chat.hola.com.app.ecom.review;

import static chat.hola.com.app.Utilities.Constants.CAMERA_ITEM;
import static chat.hola.com.app.Utilities.Constants.REVIEW_IMAGE_WIDTH;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemCameraBinding;
import com.ezcall.android.databinding.ItemReviewImagesBinding;
import java.io.File;
import java.util.ArrayList;

/**
 * adapter class for the review products
 */
public class ReviewProductsAdapter extends
    RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int TYPE_CAMERA = TWO;
  private static final int TYPE_IMAGE = ONE;
  private ArrayList<String> mReviewImagesList;
  private Context mContext;
  private ReviewProductCameraClick mReviewProductCameraClick;

  /**
   * constructor for review products
   *
   * @param reviewImagesList         arrayList for review images
   * @param reviewProductCameraClick interface listener for the review product camera click
   */
  ReviewProductsAdapter(ArrayList<String> reviewImagesList,
      ReviewProductCameraClick reviewProductCameraClick) {
    this.mReviewImagesList = reviewImagesList;
    this.mReviewProductCameraClick = reviewProductCameraClick;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    switch (viewType) {
      case TYPE_IMAGE:
        ItemReviewImagesBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.item_review_images, parent, false);
        return new ReviewProductViewHolder(binding);
      case TYPE_CAMERA:
        ItemCameraBinding itemCameraBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.item_camera, parent, false);
        return new CameraViewHolder(itemCameraBinding);
      default:
        return null;
    }
  }

  /**
   * update the list
   * @param models ArrayList<PrescriptionModel>
   */
  void updateList(ArrayList<String> models) {
    mReviewImagesList= models;
    notifyDataSetChanged();
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ReviewProductViewHolder) {
      ReviewProductViewHolder reviewProductViewHolder = (ReviewProductViewHolder) holder;
      String imageUrl = mReviewImagesList.get(position);
      Utilities.printLog("imageUrl" + imageUrl);
      if (imageUrl.contains("https:") || imageUrl.contains("http:")) {
        Glide.with(mContext).load(imageUrl.trim())
            .asBitmap()
            .into(new BitmapImageViewTarget(reviewProductViewHolder.mBinding.ivReview) {
              @Override
              protected void setResource(Bitmap resource) {
                Bitmap.createScaledBitmap(resource, REVIEW_IMAGE_WIDTH, REVIEW_IMAGE_WIDTH, false);
                reviewProductViewHolder.mBinding.ivReview.setImageBitmap(resource);
              }
            });
      } else {
        if (!"".equals(imageUrl)) {
          File file = new File(imageUrl);
          Glide.with(mContext).load(file)
              .asBitmap()
              .into(new BitmapImageViewTarget(reviewProductViewHolder.mBinding.ivReview) {
                @Override
                protected void setResource(Bitmap resource) {
                  Bitmap.createScaledBitmap(resource, REVIEW_IMAGE_WIDTH, REVIEW_IMAGE_WIDTH, false);
                  reviewProductViewHolder.mBinding.ivReview.setImageBitmap(resource);
                }
              });
        }
      }
    } else {
      CameraViewHolder cameraViewHolder = (CameraViewHolder) holder;
      cameraViewHolder.itemView.setOnClickListener(view -> {
        mReviewProductCameraClick.onCameraClick();
      });
    }
  }

  @Override
  public int getItemCount() {
    return mReviewImagesList != null ? mReviewImagesList.size() : ZERO;
  }

  @Override
  public int getItemViewType(int position) {
    if (mReviewImagesList.get(position) != null && mReviewImagesList.get(position).equals(
        CAMERA_ITEM)) {
      return TYPE_CAMERA;
    }
    return TYPE_IMAGE;
  }

  /**
   * view holder class for the review product
   */
  class ReviewProductViewHolder extends RecyclerView.ViewHolder {
    ItemReviewImagesBinding mBinding;

    ReviewProductViewHolder(@NonNull ItemReviewImagesBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }

  /**
   * view holder class for the camera
   */
  class CameraViewHolder extends RecyclerView.ViewHolder {
    ItemCameraBinding mBinding;

    CameraViewHolder(@NonNull ItemCameraBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}