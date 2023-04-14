package chat.hola.com.app.ecom.home;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.ONE_MINUTE_MILLI_SECOND;
import static chat.hola.com.app.Utilities.Constants.TEN;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.Utilities.Utilities.isEmptyArray;
import static com.kotlintestgradle.remote.RemoteConstants.PARENT_PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ID;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.DateAndTimeUtil;
import chat.hola.com.app.ecom.pdp.ProductDetailsActivity;
import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemHomeProductsListBinding;
import com.kotlintestgradle.remote.model.response.ecom.homeSubCategory.CategoryDetails;
import java.util.ArrayList;

/*
 * Purpose – This class Holds Products List display in the HomePage.
 * @author 3Embed
 * Created on Oct 21, 2019
 * Modified on
 */
public class ProductsListAdapter extends RecyclerView.Adapter<ProductsListAdapter.ViewHolder> {
  private ArrayList<CategoryDetails> mProductList;
  private Context mContext;
  private int mMasterPosition = ZERO;
  private int mCartAction;
  private String mShoppingListId;
  private OnMyListListenerClickListener mOnMyListListenerClickListener;
  private boolean mIsUserLoggedIn = FALSE;
  private OnCartUpdateListener mUpdateListener;
  private ProductListBottomSheet.ClickItem clickItem;

  public ProductsListAdapter(ArrayList<CategoryDetails> productList,
      String shoppingListId, boolean isUserLoggedIn, ProductListBottomSheet.ClickItem clickItem) {
    this.mProductList = productList;
    this.mShoppingListId = shoppingListId;
    this.mIsUserLoggedIn = isUserLoggedIn;
    this.clickItem = clickItem;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemHomeProductsListBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_home_products_list, parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    String currency = mProductList.get(position).getCurrencySymbol();
    holder.mBinding.tvHomeItemProductName.setText(mProductList.get(position).getProductName());
    holder.mBinding.tvHomeItemProductCategories.setText(mProductList.get(position).getBrandName());
    holder.mBinding.tvHomeItemProductPrice.setText(
        String.format("%s %s", currency, mProductList.get(
            position).getFinalPriceList().getFinalPrice()));
    holder.mBinding.tvHomeItemProductOfferPrice.setText(
        String.format("%s%s", currency, mProductList.get(
            position).getFinalPriceList().getBasePrice()));
    holder.mBinding.tvOutOfStock.setVisibility(
        mProductList.get(position).isOutOfStock() ? VISIBLE : View.GONE);
    holder.mBinding.clShadow.setVisibility(
        mProductList.get(position).isOutOfStock() ? VISIBLE : View.GONE);
    holder.mBinding.tvHomeOffer.setVisibility(mProductList.get(position).getOffers() != null
        && mProductList.get(
        position).getOffers().getDiscountValue() != null
        ? VISIBLE : INVISIBLE);
    holder.mBinding.tvHomeItemProductOfferPrice.setVisibility(mProductList.get(position).getOffers()
        != null
        ? VISIBLE : INVISIBLE);
    if (mProductList.get(position).getOffers() != null && mProductList.get(
        position).getOffers().getDiscountValue() != null) {
      String disType = mProductList.get(position).getOffers().getDiscountType() == ZERO
          ? mContext.getString(R.string.flat) : mContext.getString(R.string.upto);
      holder.mBinding.tvHomeOffer.setText(String.format("%s %s %% %s", disType, mProductList.get(
          position).getOffers().getDiscountValue(), mContext.getString(R.string.off)));
      if (mProductList.get(
          position).getOffers().getDiscountValue().equals("" + ZERO)) {
        holder.mBinding.tvHomeOffer.setVisibility(INVISIBLE);
        holder.mBinding.tvHomeItemProductOfferPrice.setVisibility(INVISIBLE);
      }
    }

    holder.mBinding.rbHomeItemProductRatings.setVisibility(mProductList.get(
        position).getTotalStarRating() > ZERO ? VISIBLE : GONE);

    holder.mBinding.rbHomeItemProductRatings.setRating(mProductList.get(
        position).getTotalStarRating());
    if (!isEmptyArray(mProductList) && !isEmptyArray(mProductList.get(position).getImages())) {
      String imgUrl = mProductList.get(position).getImages().get(ZERO).getSmall();
      if (!TextUtils.isEmpty(imgUrl)) {
        imgUrl = imgUrl.trim();
        Glide.with(mContext).load(imgUrl).into(holder.mBinding.ivHomeItemProducts);
      }
    }
    if (holder.timer != null) {
      holder.timer.cancel();
    }
    if (mProductList.get(position).getOffers() != null && mProductList.get(
        position).getOffers().getEndDateTimeISO() > ZERO) {
      holder.timer = new CountDownTimer(mProductList.get(
          position).getOffers().getEndDateTimeISO(), ONE_MINUTE_MILLI_SECOND) {
        @Override
        public void onTick(long l) {
          holder.mBinding.tvDealTimer.setVisibility(VISIBLE);
          holder.mBinding.tvDealTimer.setText(
              DateAndTimeUtil.findNumberOfDays(mContext, mProductList.get(
                  position).getOffers().getEndDateTimeISO()));
        }

        @Override
        public void onFinish() {
          holder.mBinding.tvDealTimer.setVisibility(INVISIBLE);
        }
      }.start();
    }
    holder.mBinding.clProductMain.setOnClickListener(
        v -> redirectToProductDetailsPage(holder, position));


    holder.mBinding.tvAdd.setVisibility(GONE);
    holder.mBinding.tvHomeOffer.setBackgroundColor(
        mContext.getResources().getColor(R.color.colorAdd));
    holder.mBinding.tvPrescriptionRequired.setVisibility(GONE);
    holder.mBinding.tvDealTimer.setTextColor(mContext.getResources().getColor(R.color.hippieGreen));
    holder.mBinding.rbHomeItemProductRatings.setVisibility(View.VISIBLE);
  }

  /**
   * set margin and background resource for products available
   * in both pharmacy as well as in normal store
   *
   * @param holder ViewHolder
   */
  private void setNormalProductMargin(@NonNull ViewHolder holder) {
    holder.mBinding.tvHomeItemProductName.layout(TEN, ZERO, TEN, ZERO);
    holder.mBinding.tvHomeItemProductPrice.layout(TEN, ZERO, TEN, ZERO);
    holder.mBinding.tvDealTimer.layout(TEN, ZERO, TEN, ZERO);
    holder.mBinding.tvPrescriptionRequired.layout(TEN, FIVE, TEN, FIVE);
    holder.mBinding.tvHomeOffer.setBackgroundColor(
        mContext.getResources().getColor(R.color.colorAdd));
    holder.mBinding.tvDealTimer.setTextColor(mContext.getResources().getColor(R.color.colorAdd));
//        holder.mBinding.rbHomeItemProductRatings.setVisibility(INVISIBLE);
  }

  /**
   * set visibility of plus and minus buttons on item click
   *
   * @param holder   ViewHolder
   * @param quantity AtomicInteger
   */
  private void setVisibilityOnItemClick(@NonNull ViewHolder holder, int quantity) {
    holder.mBinding.tvAdd.setVisibility((quantity > ZERO) ? INVISIBLE : VISIBLE);
    holder.mBinding.ivAddItem.setVisibility((quantity > ZERO) ? VISIBLE : INVISIBLE);
    holder.mBinding.ivRemoveItem.setVisibility((quantity > ZERO) ? VISIBLE : INVISIBLE);
    holder.mBinding.tvItemQuantity.setVisibility((quantity > ZERO) ? VISIBLE : INVISIBLE);
    holder.mBinding.tvItemQuantity.setText(String.valueOf(quantity));
  }

  /**
   * it will redirect to PDP(or ProductDetails) page
   *
   * @param holder   ViewHolder
   * @param position int
   */
  private void redirectToProductDetailsPage(@NonNull ViewHolder holder, int position) {
    clickItem.clickItem();
    Intent intent =
        new Intent(holder.mBinding.clProductMain.getContext(),
            ProductDetailsActivity.class);
    intent.putExtra(PARENT_PRODUCT_ID, mProductList.get(position).getParentProductId());
    intent.putExtra(PRODUCT_ID, mProductList.get(position).getChildProductId());
    holder.mBinding.clProductMain.getContext().startActivity(intent);
  }

  @Override
  public int getItemCount() {
    return isEmptyArray(mProductList) ? ZERO : mProductList.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    ItemHomeProductsListBinding mBinding;
    CountDownTimer timer;

    ViewHolder(@NonNull ItemHomeProductsListBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
      binding.tvHomeItemProductOfferPrice.setPaintFlags(
          binding.tvHomeItemProductOfferPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }
  }

  public interface OnCartUpdateListener {
    /**
     * This method is using to add new item to cart
     */
    void onAddClickListener(int action, int newQuantity, String parentProductId,
        String productId, String unitId, String storeId, String productName);

    /**
     * This method is using to update cart product values
     */
    void onUpdateClickListener(int action, int newQuantity, String parentProductId,
        String productId, String unitId, String storeId, String productName);
  }

  public interface OnMyListListenerClickListener {
    /**
     * This method is using to delete product from my list
     */
    void onDeleteClickListener(String productId, String parentProductId, String shoppingListId);
  }
}