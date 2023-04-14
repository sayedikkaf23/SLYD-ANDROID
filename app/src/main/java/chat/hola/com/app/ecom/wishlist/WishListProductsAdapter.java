package chat.hola.com.app.ecom.wishlist;

import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemWishlistBinding;
import com.kotlintestgradle.model.ecom.common.ProductsData;
import java.util.ArrayList;

/*
 * Purpose – This class Holds list of WishList added by user.
 * @author 3Embed
 * Created on Dec 11, 2019
 * Modified on
 */
public class WishListProductsAdapter extends
    RecyclerView.Adapter<WishListProductsAdapter.WishListViewHolder> {

  private ArrayList<ProductsData> mProductsData;
  private Context mContext;
  private OnClickListener mClickListener;

  WishListProductsAdapter(
      ArrayList<ProductsData> productsData, OnClickListener clickListener) {
    mProductsData = productsData;
    this.mClickListener = clickListener;
  }

  @NonNull
  @Override
  public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemWishlistBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
        R.layout.item_wishlist, parent, false);
    return new WishListViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull WishListViewHolder holder, int position) {
    holder.mBinding.tvWishListProductName.setText(mProductsData.get(position).getProductName());
    String imgUrl = mProductsData.get(position).getImages().get(ZERO).getMedium();
    String currencySymbol = mProductsData.get(position).getCurrencySymbol();
    if (!TextUtils.isEmpty(imgUrl)) {
      Glide.with(mContext)
          .load(imgUrl.replace(" ", "%20"))
          .into(holder.mBinding.ivWishListImage);
    }

    holder.mBinding.ivWishListDelete.setOnClickListener(
        view -> {
          mClickListener.deleteItem(mProductsData.get(position).getChildProductId());
        });

    holder.mBinding.rbWishListRatings.setRating(mProductsData.get(position).getAvgRating());
    if (mProductsData.get(position).getFinalPriceList() != null) {
      String basePrice = mProductsData.get(position).getFinalPriceList().getBasePrice();
      String finalPrice = mProductsData.get(position).getFinalPriceList().getFinalPrice();
      double numBasePrice = !TextUtils.isEmpty(basePrice) ? Double.parseDouble(basePrice) : ZERO;
      double numFinalPrice = !TextUtils.isEmpty(finalPrice) ? Double.parseDouble(finalPrice) : ZERO;
      holder.mBinding.tvWishListBasePrice.setVisibility(
          numBasePrice > numFinalPrice ? View.VISIBLE : View.GONE);
      holder.mBinding.tvWishListBasePrice.setText(
          String.format("%s%s", currencySymbol, mProductsData.get(
              position).getFinalPriceList().getBasePrice()));
      holder.mBinding.tvWishListFinalPrice.setText(
          numBasePrice > numFinalPrice ? currencySymbol + finalPrice : currencySymbol + basePrice);
    }
    holder.mBinding.clWishListMain.setOnClickListener(
        view -> mClickListener.redirectToPdp(mProductsData.get(position).getParentProductId(),
            mProductsData.get(position).getChildProductId()));
  }

  @Override
  public int getItemCount() {
    return Utilities.isEmptyArray(mProductsData) ? ZERO : mProductsData.size();
  }

  /**
   * will update the list content
   * @param list ArrayList
   */
  void updateList(ArrayList<ProductsData> list) {
    this.mProductsData.clear();
    this.mProductsData.addAll(list);
    notifyDataSetChanged();
  }

  public interface OnClickListener {
    /**
     * This Method is using to delete wishList product
     *
     * @param productId productId to delete
     */
    void deleteItem(String productId);

    void redirectToPdp(String parentProductId, String childProductId);
  }

  static class WishListViewHolder extends RecyclerView.ViewHolder {

    ItemWishlistBinding mBinding;

    WishListViewHolder(ItemWishlistBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
      binding.tvWishListBasePrice.setPaintFlags(
          binding.tvWishListBasePrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
  }
}