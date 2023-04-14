package chat.hola.com.app.orders.historyscreen;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemHistoryOrdersBinding;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;
import com.kotlintestgradle.model.orderhistory.OrderHistStoreOrderData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.DecompositionType.SUPER;
import static chat.hola.com.app.Utilities.Constants.IS_SPIT_PRODUCT;
import static chat.hola.com.app.Utilities.Constants.ORDER_CANCELLED;
import static chat.hola.com.app.Utilities.Constants.ORDER_PENDING_CONFIRMATION;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.Utilities.Utilities.isEmptyArray;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_TIME;


/**
 * adapter class for history stores
 */
public class HistoryStoreAdapter extends
    RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private Context mContext;
  private ArrayList<OrderHistStoreOrderData> mOrderHistStoreOrderData;
  private HistoryItemClick mHistoryItemClick;
  private HistoryStoreClickListener mHistoryStoreClickListener;
  private int mMasterPos;
  private int mStoreType;
  private boolean isSuper= false;
  private String mOrderId = EMPTY_STRING;
  private String mOrderTime = EMPTY_STRING;

  /**
   * history store adapter
   *
   * @param orderHistStoreOrderData arrayList orderHistStoreOrderData
   * @param historyItemClick        interface on click listener
   */
  HistoryStoreAdapter(boolean isSuper,
      ArrayList<OrderHistStoreOrderData> orderHistStoreOrderData, int masterPos,
      HistoryItemClick historyItemClick, String orderId, String orderTime,
      HistoryStoreClickListener historyStoreClickListener, int storeType) {
    mOrderHistStoreOrderData = orderHistStoreOrderData;
    mMasterPos = masterPos;
    mOrderId = orderId;
    this.isSuper = isSuper;
    mOrderTime = orderTime;
    this.mStoreType = storeType;
    mHistoryItemClick = historyItemClick;
    mHistoryStoreClickListener = historyStoreClickListener;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                    int viewType) {
    mContext = parent.getContext();

        ItemHistoryOrdersBinding historyOrderStoresBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_history_orders, parent, false);
        return new HistoryStoreViewHolder(historyOrderStoresBinding);

  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,
      int position) {

    OrderHistStoreOrderData orderHistStoreOrderData = mOrderHistStoreOrderData.get(position);
    if (viewHolder instanceof HistoryStoreViewHolder) {
      HistoryStoreViewHolder holder = (HistoryStoreViewHolder) viewHolder;
      if (orderHistStoreOrderData != null) {
        holder.mStoresBinding.tvStoreName.setText(orderHistStoreOrderData.getStoreName());
       // Glide.with(mContext).load(orderHistStoreOrderData.getStoreLogo().getLogoImageMobile()).into(holder.mStoresBinding.ivStoreIcon);
        HistoryStoreProductsAdapter historyStoreAdapter;
        holder.mStoresBinding.tvStoreId.setText(
                String.format("%s%s", mContext.getResources().getString(R.string.historyStoreOrderId),
                        orderHistStoreOrderData.getStoreOrderId()));
        historyStoreAdapter = new HistoryStoreProductsAdapter(
                orderHistStoreOrderData,
                orderHistStoreOrderData.getProducts(),mMasterPos,position, mHistoryItemClick, mStoreType);
        holder.mStoresBinding.rvOrderStoreItems.setAdapter(historyStoreAdapter);

      }
    }

  }

  /**
   * checks if any product has been rejected
   * @param products ArrayList<OrderHistProductData>
   * @return boolean
   */
  private int isAnyProductRejected(ArrayList<OrderHistProductData> products) {
    int rejectedItemCount = ZERO;
    for (OrderHistProductData data : products) {
      if (data.getStatus() != null && data.getStatus().getStatus() != null
        && (data.getStatus().getStatus().equals(String.valueOf(ORDER_CANCELLED))
              || data.getStatus().getStatus().equals(String.valueOf(ORDER_PENDING_CONFIRMATION)))) {
        rejectedItemCount++;
      }
    }
    return rejectedItemCount;
  }

  /**
   * set up store type for hyper local
   * @param holder HistoryStoreViewHolder
   * @param position int
   * @param orderHistStoreOrderData OrderHistStoreOrderData
   * @return HistoryStoreProductAdapter
   */
  @NotNull
  private HistoryStoreProductsAdapter getHistoryStoreAdapter(@NonNull HistoryStoreViewHolder holder, int position, OrderHistStoreOrderData orderHistStoreOrderData) {
    HistoryStoreProductsAdapter historyStoreAdapter;
    if (orderHistStoreOrderData.getProducts().size() > TWO) {
      historyStoreAdapter = new HistoryStoreProductsAdapter(orderHistStoreOrderData,
              getLimitedOrderHistProductData(orderHistStoreOrderData), mMasterPos,
              position, mHistoryItemClick, mStoreType);
    } else {
      historyStoreAdapter = new HistoryStoreProductsAdapter(
              orderHistStoreOrderData,
              orderHistStoreOrderData.getProducts(), mMasterPos, position, mHistoryItemClick, mStoreType);
    }
    holder.mStoresBinding.tvTrackOrder.setVisibility(View.VISIBLE);
    holder.mStoresBinding.tvOrderDetails.setVisibility(View.VISIBLE);

    return historyStoreAdapter;
  }

  /**
   * it will return limited order history data
   * @param orderHistStoreOrderData OrderHistStoreOrderData
   * @return ArrayList<OrderHistProductData>
   */
  @NotNull
  private ArrayList<OrderHistProductData> getLimitedOrderHistProductData(OrderHistStoreOrderData orderHistStoreOrderData) {
    ArrayList<OrderHistProductData> data = new ArrayList<>();
    data.add(orderHistStoreOrderData.getProducts().get(ZERO));
    data.add(orderHistStoreOrderData.getProducts().get(ONE));
    return data;
  }



  @Override
  public int getItemCount() {
    return isEmptyArray(mOrderHistStoreOrderData) ? ZERO : mOrderHistStoreOrderData.size();
  }

  /**
   * view holder class
   */
  static class HistoryStoreViewHolder extends RecyclerView.ViewHolder {
    ItemHistoryOrdersBinding mStoresBinding;

    /**
     * view holder class constructor
     *
     * @param historyOrderStoresBinding binding reference.
     */
    HistoryStoreViewHolder(
        @NonNull ItemHistoryOrdersBinding historyOrderStoresBinding) {
      super(historyOrderStoresBinding.getRoot());
      mStoresBinding = historyOrderStoresBinding;
    }
  }

  /**
   * view holder class
   */

  /**
   * click listener for history store items
   */
  public interface HistoryStoreClickListener {

    /**
     * called when track  order button will be clicked
     * @param position int
     */
    void onTrackOrderClicked(boolean isFood,int masterPosition, int position);

    /**
     * called when review order button will be clicked
     * @param position int
     */
    void onReviewOrderClicked(boolean isFood,int masterPosition, int position);

    /**
     * called when review order button will be clicked
     * @param position int
     */
    void onReOrderClicked(boolean isFood,int masterPosition, int position);

    void onRateOrderClicked(String driverId,boolean isFood,int masterPosition, int position,String orderId);

    /**
     * called when order details button will be clicked
     * @param position int
     */
    void onOrderDetailsClicked(boolean isFood,int masterPosition, int position, boolean isReviewOrder);

  }

  @Override
  public int getItemViewType(int position) {
      return SUPER;

  }
}
