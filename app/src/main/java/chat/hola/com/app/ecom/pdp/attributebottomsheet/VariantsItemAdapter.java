package chat.hola.com.app.ecom.pdp.attributebottomsheet;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.MINUS_ONE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemColorBinding;
import com.ezcall.android.databinding.ItemVariantBinding;
import com.kotlintestgradle.model.ecom.pdp.SizeData;
import java.util.ArrayList;

/**
 * adapter class for the variants items
 */
public class VariantsItemAdapter
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private static final int TYPE_COLOUR = ONE;
  private static final int TYPE_ALL = TWO;
  private int colorCheckedPosition = MINUS_ONE;
  private ArrayList<SizeData> mSizeDataArrayList;
  private ProductAttributesClick mProductAttributesClick;
  private int mColorUnCheckedPosition;
  private Context mContext;
  private int mCheckedPosition = MINUS_ONE, mUnCheckedPosition;

  /**
   * constructor for this adapter class.
   */
  VariantsItemAdapter(ArrayList<SizeData> sizeDataArrayList,
      ProductAttributesClick productAttributesClick) {
    this.mSizeDataArrayList = sizeDataArrayList;
    this.mProductAttributesClick = productAttributesClick;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    mContext = parent.getContext();
    switch (viewType) {
      case TYPE_COLOUR:
        ItemColorBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.item_color, parent, false);
        return new ColorsViewHolder(binding);
      case TYPE_ALL:
        ItemVariantBinding itemVariantBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.item_variant, parent, false);
        return new ViewHolder(itemVariantBinding);
      default:
        return null;
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ColorsViewHolder) {
      ColorsViewHolder colorsViewHolder = (ColorsViewHolder) holder;
      SizeData sizeData = mSizeDataArrayList.get(position);
      if (sizeData.getImage() != null && !sizeData.getImage().isEmpty()) {
        colorsViewHolder.mBinding.ivColorProduct.setVisibility(View.VISIBLE);
        colorsViewHolder.mBinding.ivColor.setVisibility(View.GONE);
        colorsViewHolder.mBinding.tvColorBackGround.setVisibility(View.GONE);
        colorsViewHolder.mBinding.ivColorProduct.setSelected(sizeData.getIsPrimary());
        if (sizeData.getIsPrimary()) {
          colorCheckedPosition = colorsViewHolder.getAdapterPosition();
          mColorUnCheckedPosition = colorCheckedPosition;
        }
        Glide.with(mContext).load(sizeData.getImage().trim())
            .asBitmap()
            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
            into(new BitmapImageViewTarget(colorsViewHolder.mBinding.ivColorProduct) {
              @Override
              protected void setResource(Bitmap resource) {
                colorsViewHolder.mBinding.ivColorProduct.setImageBitmap(resource);
              }
            });
      } else {
        colorsViewHolder.mBinding.ivColorProduct.setVisibility(View.INVISIBLE);
        colorsViewHolder.mBinding.ivColor.setVisibility(View.VISIBLE);
        int color = Color.parseColor("#00FFFF");
        colorsViewHolder.mBinding.ivColor.setBackgroundColor(color);
        colorsViewHolder.mBinding.tvColorBackGround.setVisibility(
            sizeData.getIsPrimary() ? View.VISIBLE : View.INVISIBLE);
        if (sizeData.getIsPrimary()) {
          colorCheckedPosition = colorsViewHolder.getAdapterPosition();
          mColorUnCheckedPosition = colorCheckedPosition;
        }
      }
      colorsViewHolder.mBinding.clColorItem.setOnClickListener(view -> {
        if (!colorsViewHolder.mBinding.ivColorProduct.isSelected()) {
          colorCheckedPosition = colorsViewHolder.getAdapterPosition();
          mSizeDataArrayList.get(mColorUnCheckedPosition).setIsPrimary(FALSE);
          notifyItemChanged(mColorUnCheckedPosition);
          mProductAttributesClick.onClick(
              mSizeDataArrayList.get(colorCheckedPosition).getChildProductId());
        }

      });
    } else {
      ViewHolder viewHolder = (ViewHolder) holder;
      SizeData sizeData = mSizeDataArrayList.get(position);

      viewHolder.mBinding.tvVariantName.setTextColor(sizeData.getIsPrimary()
          ? mContext.getResources().getColor(R.color.allWhiteColor)
          : mContext.getResources().getColor(R.color.colorProductNeroTitle));
      viewHolder.mBinding.tvVariantName.setSelected(sizeData.getIsPrimary());

      if (sizeData.getIsPrimary()) {
        mCheckedPosition = viewHolder.getAdapterPosition();
        mUnCheckedPosition = mCheckedPosition;
      }
      viewHolder.mBinding.tvVariantName.setText(mSizeDataArrayList.get(position).getSizeString());
      viewHolder.mBinding.tvVariantName.setOnClickListener(view -> {
        if (!viewHolder.mBinding.tvVariantName.isSelected()) {
          mCheckedPosition = viewHolder.getAdapterPosition();
          mSizeDataArrayList.get(mUnCheckedPosition).setIsPrimary(FALSE);
          notifyItemChanged(mUnCheckedPosition);
          mProductAttributesClick.onClick(
              mSizeDataArrayList.get(mCheckedPosition).getChildProductId());
        }
      });
    }
  }

  @Override
  public int getItemViewType(int position) {
    if (mSizeDataArrayList.get(position).getRgb() != null && !mSizeDataArrayList.get(
        position).getRgb().isEmpty()) {
      return TYPE_COLOUR;
    }
    return TYPE_ALL;
  }

  @Override
  public int getItemCount() {
    return mSizeDataArrayList != null ? mSizeDataArrayList.size() : ZERO;
  }

  /**
   * colors view holder
   */
  static class ColorsViewHolder extends RecyclerView.ViewHolder {
    ItemColorBinding mBinding;

    ColorsViewHolder(@NonNull ItemColorBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }

  /**
   * item view holder class
   */
  static class ViewHolder extends RecyclerView.ViewHolder {
    ItemVariantBinding mBinding;

    ViewHolder(@NonNull ItemVariantBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}