package chat.hola.com.app.ecom.home;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static chat.hola.com.app.Utilities.Constants.EMPTY;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.kotlintestgradle.remote.RemoteConstants.PARENT_PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ID;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ecom.pdp.ProductDetailsActivity;
import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemGridProductBinding;
import com.kotlintestgradle.model.ecom.common.ProductsData;
import java.util.ArrayList;

/*
 * Purpose – This class Holds All the ProductListing page Items.
 * @author 3Embed
 * Created on Nov 05, 2019
 * Modified on
 */
public class GridListProductsAdapter extends
    RecyclerView.Adapter<GridListProductsAdapter.GridViewHolder> {

  private ArrayList<ProductsData> mProductsData;
  private Context mContext;
  private boolean mIsUserLoggedIn = FALSE;
  private AddButtonClickListener mListener;

  public GridListProductsAdapter(ArrayList<ProductsData> mProductsData, int storeType) {
    this.mProductsData = mProductsData;
  }

  public GridListProductsAdapter(ArrayList<ProductsData> data, AddButtonClickListener listener,
                                 boolean isUserLoggedIn) {
    this.mProductsData = data;
    this.mListener = listener;
    this.mIsUserLoggedIn = isUserLoggedIn;
  }

  @NonNull
  @Override
  public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemGridProductBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_grid_product, parent, false);
    return new GridViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
    holder.mBinding.tvFinalPrice.setText(
        String.format("%s %s", mProductsData.get(position).getCurrencySymbol(), mProductsData.get(
            position).getFinalPriceList().getFinalPrice()));
    holder.mBinding.tvProductName.setText(mProductsData.get(position).getProductName());
    holder.mBinding.tvBrandName.setText(mProductsData.get(position).getBrandName());
    String imageUrl = mProductsData.get(position).getImages().get(ZERO).getSmall();

    holder.mBinding.rbHomeItemProductRatings.setVisibility(mProductsData.get(
        position).getTotalStarRating() > ZERO ? VISIBLE : (INVISIBLE));
    holder.mBinding.rbHomeItemProductRatings.setRating(mProductsData.get(
        position).getTotalStarRating());
    holder.mBinding.clShadow.setVisibility(
        mProductsData.get(position).getOutOfStock() ? VISIBLE : View.GONE);
    holder.mBinding.tvOutOfStock.setVisibility(
        mProductsData.get(position).getOutOfStock() ? VISIBLE : View.GONE);
    if (!TextUtils.isEmpty(imageUrl)) {
      Glide.with(mContext)
          .load(imageUrl.replace(EMPTY, "%20"))
          .into(holder.mBinding.ivProductImage);
    }

    if (Integer.parseInt(mProductsData.get(position).getFinalPriceList().getDiscountPercentage())
        != ZERO) {
      holder.mBinding.tvActualPrice.setText(
          String.format("%s %s", mProductsData.get(position).getCurrencySymbol(), mProductsData.get(
              position).getFinalPriceList().getBasePrice()));
      holder.mBinding.tvActualPrice.setVisibility(VISIBLE);
      holder.mBinding.tvOnOffer.setVisibility(VISIBLE);
    } else {
      if (mProductsData.get(position).getFinalPriceList().getDiscountPrice() != null
          && !mProductsData.get(
          position).getFinalPriceList().getDiscountPrice().isEmpty() && !mProductsData.get(
          position).getFinalPriceList().getDiscountPrice().equals("0")) {
        holder.mBinding.tvActualPrice.setText(
            String.format("%s %s", mProductsData.get(position).getCurrencySymbol(),
                mProductsData.get(
                    position).getFinalPriceList().getBasePrice()));
        holder.mBinding.tvActualPrice.setVisibility(VISIBLE);
        holder.mBinding.tvOnOffer.setVisibility(VISIBLE);
      } else {
        holder.mBinding.tvActualPrice.setVisibility(View.GONE);
        holder.mBinding.tvOnOffer.setVisibility(View.GONE);
      }
    }
        holder.mBinding.ivRemoveItem.setVisibility(View.GONE);
        holder.mBinding.ivAddItem.setVisibility(View.GONE);
        holder.mBinding.tvItemQuantity.setVisibility(View.GONE);
        holder.mBinding.tvAdd.setVisibility(View.GONE);
        holder.mBinding.tvPrescriptionRequired.setVisibility(View.GONE);

    holder.mBinding.clGridListItem.setOnClickListener(
        v -> {
          Intent intent =
              new Intent(holder.mBinding.clGridListItem.getContext(),
                  ProductDetailsActivity.class);
          intent.putExtra(PARENT_PRODUCT_ID, mProductsData.get(position).getParentProductId());
          intent.putExtra(PRODUCT_ID, mProductsData.get(position).getChildProductId());
          holder.mBinding.clGridListItem.getContext().startActivity(intent);
        });
  }

  /**
   * update login status
   * @param status boolean
   */
  void updateLoginStatus(boolean status) {
    this.mIsUserLoggedIn = status;
  }


  /**
   * set visibility of plus and minus buttons on item click
   * @param holder ViewHolder
   * @param quantity AtomicInteger
   */
  private void setVisibilityOnItemClick(@NonNull GridViewHolder holder, int quantity) {
    holder.mBinding.tvAdd.setVisibility((quantity > ZERO) ? INVISIBLE : VISIBLE);
    holder.mBinding.ivAddItem.setVisibility((quantity > ZERO) ? VISIBLE : INVISIBLE);
    holder.mBinding.ivRemoveItem.setVisibility((quantity > ZERO) ? VISIBLE : INVISIBLE);
    holder.mBinding.tvItemQuantity.setVisibility((quantity > ZERO) ? VISIBLE : INVISIBLE);
    holder.mBinding.tvItemQuantity.setText(String.valueOf(quantity));
  }

  /**
   * it will redirect to PDP(or ProductDetails) page
   * @param holder ViewHolder
   * @param position int
   */
  private void redirectToProductDetailsPage(@NonNull GridViewHolder holder, int position) {
    Intent intent =
            new Intent(holder.mBinding.clGridListItem.getContext(),
                    ProductDetailsActivity.class);
    intent.putExtra(PARENT_PRODUCT_ID, mProductsData.get(position).getParentProductId());
    intent.putExtra(PRODUCT_ID, mProductsData.get(position).getChildProductId());
    holder.mBinding.clGridListItem.getContext().startActivity(intent);
  }


  @Override
  public int getItemCount() {
    return Utilities.isEmptyArray(mProductsData) ? ZERO : mProductsData.size();
  }

  static class GridViewHolder extends RecyclerView.ViewHolder {
    ItemGridProductBinding mBinding;

    GridViewHolder(ItemGridProductBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
      binding.tvActualPrice.setPaintFlags(
          binding.tvActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
  }


  /**
   * used to pass listener when add button will be clicked
   */
  public interface AddButtonClickListener {

    /**
     * called when add button will be clicked
     * @param parentProductId String
     * @param productId String
     * @param unitId String
     */
    void onItemAdded(String parentProductId, String productId, String unitId, String storeId,
        int action, int newQuantity, int position);

    /**
     * called when notify button will be clicked
     * @param parentProductId String
     * @param productId String
     * @param unitId String
     */
    void onItemNotified(String parentProductId,
        String productId, String unitId);
  }
}