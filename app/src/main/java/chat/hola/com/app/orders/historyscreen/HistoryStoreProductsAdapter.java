package chat.hola.com.app.orders.historyscreen;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemHistoryOrdersBinding;
import com.ezcall.android.databinding.ItemProductHistoryBinding;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;
import com.kotlintestgradle.model.orderhistory.OrderHistStoreOrderData;

import java.util.ArrayList;
import java.util.Locale;

import static chat.hola.com.app.Utilities.Constants.HISTROY_ORDER_DETAIL;
import static chat.hola.com.app.Utilities.Constants.HISTROY_PRODUCT_DETAIL;
import static chat.hola.com.app.Utilities.Constants.IS_SPIT_PRODUCT;
import static chat.hola.com.app.Utilities.Constants.PRICE;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_COLOUR;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_IMAGE;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_NAME;
import static chat.hola.com.app.Utilities.Constants.REVIEW_TYPE;
import static chat.hola.com.app.Utilities.Constants.STORE_NAME;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.Utilities.Utilities.isEmptyArray;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;
import static com.kotlintestgradle.data.utils.DataConstants.HYPER_LOCAL_TYPE;
import static com.kotlintestgradle.data.utils.DataConstants.PHARMACY_FLOW;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PACK_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PARENT_PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ORDER_ID;


/**
 * adapter class for history store products
 */
public class HistoryStoreProductsAdapter extends
    RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private Context mContext;
  private ArrayList<OrderHistProductData> mOrderHistProductData;
  private OrderHistStoreOrderData mOrderHistStoreOrderData;
  private HistoryItemClick mHistoryItemClick;
  private HistoryStoreProductClickListener mListener;
  private int mMasterPos, mStorePos, mStoreType;
  private boolean isShowAllItem = false;

  /**
   * constructor for history store products adapter
   *
   * @param orderHistProductData arrayList orderHistProductData
   * @param historyItemClick     interface on click listener
   */
  HistoryStoreProductsAdapter(
      OrderHistStoreOrderData orderHistStoreOrderData,
      ArrayList<OrderHistProductData> orderHistProductData, int masterPos, int storePos,
      HistoryItemClick historyItemClick, int storeType) {
    mOrderHistProductData = orderHistProductData;
    mOrderHistStoreOrderData = orderHistStoreOrderData;
    mMasterPos = masterPos;
    mStorePos = storePos;
    mStoreType = storeType;
    mHistoryItemClick = historyItemClick;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();

        ItemProductHistoryBinding historyOrderItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_product_history, parent, false);
        return new HistoryStoreProductsViewHolder(historyOrderItemBinding);


  }

  @Override
  public void onBindViewHolder(
          @NonNull RecyclerView.ViewHolder holders, int position) {

        HistoryStoreProductsViewHolder holder = (HistoryStoreProductsViewHolder) holders;
        bind(position, holder);
  }

  /**
   * bind views with HistoryStoreProductsViewHolder
   * @param position int
   * @param holder HistoryStoreProductsViewHolder
   */
  private void bind(int position, HistoryStoreProductsViewHolder holder) {
    OrderHistProductData data = mOrderHistProductData.get(position);
    String imageUrl = data.getImages().getMedium();
    if (!TextUtils.isEmpty(imageUrl)) {
      Glide.with(mContext)
          .load(imageUrl.replace(" ", "%20"))
          .into(holder.mItemHistoryOrderItemBinding.ivProductImg);
    }
    holder.mItemHistoryOrderItemBinding.tvProductName.setText(data.getName());
    holder.mItemHistoryOrderItemBinding.tvProductStatus.setText(data.getStatus().getStatusText());
    if (data.getStatus().getStatus() != null && Integer.parseInt(data.getStatus().getStatus())
        == TWO) {
      holder.mItemHistoryOrderItemBinding.tvProductStatus.setTextColor(
          mContext.getResources().getColor(R.color.hippieGreen));
    } else if (Integer.parseInt(data.getStatus().getStatus()) == THREE) {
      holder.mItemHistoryOrderItemBinding.tvProductStatus.setTextColor(
          mContext.getResources().getColor(R.color.colorRed));
    } else {
      holder.mItemHistoryOrderItemBinding.tvProductStatus.setTextColor(
          mContext.getResources().getColor(R.color.colorOrangePeel));
    }
    if (mStoreType != PHARMACY_FLOW) {
      holder.mItemHistoryOrderItemBinding.clProductItem.setOnClickListener(
        view -> {
          Bundle bundle = new Bundle();
          if (data.isSplitProduct()) {
            bundle.putString(ORDER_ID, data.getProductOrderId());
            bundle.putBoolean(IS_SPIT_PRODUCT, TRUE);
            bundle.putString(STORE_NAME, mOrderHistStoreOrderData.getStoreName());
            mHistoryItemClick.onClick(HISTROY_ORDER_DETAIL, mMasterPos, mStorePos, position,
                bundle);
          } else {
            bundle.putString(PRODUCT_ID, data.getProductId());
            bundle.putString(PRODUCT_ORDER_ID, data.getProductOrderId());
            bundle.putString(PACK_ID, data.getPackageId());
            mHistoryItemClick.onClick(HISTROY_PRODUCT_DETAIL, mMasterPos, mStorePos, position,
                bundle);
          }
        });
    }
    holder.mItemHistoryOrderItemBinding.tvProductReview.setOnClickListener(
        view -> {
          Bundle bundle = new Bundle();
          bundle.putString(PRODUCT_IMAGE, imageUrl);
          bundle.putString(PRODUCT_COLOUR, data.getColor());
          bundle.putString(PRODUCT_NAME, data.getName());
          bundle.putString(PRICE, String.format("%s%s", data.getCurrencySymbol(),
              data.getSingleUnitPrice().getFinalUnitPrice()));
          bundle.putString(PARENT_PRODUCT_ID, data.getCentralProductId());
          bundle.putString(PRODUCT_ID, data.getProductId());
          mHistoryItemClick.onClick(REVIEW_TYPE, mMasterPos, mStorePos, position, bundle);
        });
    if (mStoreType == HYPER_LOCAL_TYPE) {

    }
  }

  /**
   * this will update the list contents
   * @param orderHistProductData ArrayList<OrderHistProductData>
   */
  void updateList(ArrayList<OrderHistProductData> orderHistProductData) {
    this.mOrderHistProductData = orderHistProductData;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    if (!isEmptyArray(mOrderHistProductData)) {
      if (mOrderHistProductData.size() <= TWO) {
        return mOrderHistProductData.size();
      } else {
        return isShowAllItem ? mOrderHistProductData.size() : TWO;
      }
    }
    return ZERO;
  }

  /**
   * view holder class
   */
  static class HistoryStoreProductsViewHolder extends RecyclerView.ViewHolder {
    ItemProductHistoryBinding mItemHistoryOrderItemBinding;

    /**
     * view holder class constructor
     *
     * @param historyOrderItemBinding binding reference.
     */
    HistoryStoreProductsViewHolder(
        @NonNull ItemProductHistoryBinding historyOrderItemBinding) {
      super(historyOrderItemBinding.getRoot());
      mItemHistoryOrderItemBinding = historyOrderItemBinding;
    }
  }


  /**
   * click listener for history store product items
   */
  public interface HistoryStoreProductClickListener {

    /**
     * called when view more button will be clicked
     * @param position int
     */
    void onViewMoreClicked(int position);

    /**
     * called when view less will be clicked
     * @param position int
     */
    void onViewLessClicked(int position);
  }
}
