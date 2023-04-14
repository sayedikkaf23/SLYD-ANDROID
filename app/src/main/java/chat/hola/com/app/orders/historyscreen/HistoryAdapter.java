package chat.hola.com.app.orders.historyscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemLayoutOrderBinding;
import com.kotlintestgradle.model.orderhistory.OrderHistoryItemData;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.Utilities;

import static android.icu.lang.UCharacter.DecompositionType.SUPER;
import static chat.hola.com.app.Utilities.Constants.HISTROY_ORDER_DETAIL;
import static chat.hola.com.app.Utilities.Constants.MINUS_ONE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.kotlintestgradle.data.utils.DataConstants.MASTER_ORDER_TYPE;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_TIME;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_TYPE;

/**
 * adapter class for history
 */
public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private Context mContext;
  private ArrayList<OrderHistoryItemData> mOrderHistoryItemData;
  private HistoryItemClick mHistoryItemClick;
  private boolean isSuper= false;
  private int mStoreType;
  private HistoryStoreAdapter.HistoryStoreClickListener mHistoryStoreClickListener;

  /**
   * constructor for this class
   *
   * @param orderHistoryItemData arrayList orderHistoryItemData
   * @param historyItemClick     interface on click listener
   */
  public HistoryAdapter(boolean isSuper,ArrayList<OrderHistoryItemData> orderHistoryItemData,
                 HistoryItemClick historyItemClick,
                        HistoryStoreAdapter.HistoryStoreClickListener historyStoreClickListener, int storeType) {
    this.mOrderHistoryItemData = orderHistoryItemData;
    this.mHistoryItemClick = historyItemClick;
    this.isSuper=isSuper;
    this.mStoreType = storeType;
    this.mHistoryStoreClickListener = historyStoreClickListener;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                    int viewType) {
    mContext = parent.getContext();
    switch (viewType){
      default:
        ItemLayoutOrderBinding historyOrderBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_layout_order, parent, false);
        return new HistoryViewHolder(historyOrderBinding);

    }


  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder instanceof HistoryViewHolder) {
      HistoryViewHolder holder = (HistoryViewHolder) viewHolder;
      OrderHistoryItemData orderHistoryItemData = mOrderHistoryItemData.get(position);
      holder.mHistoryOrderBinding.tvOrderNo.setText(orderHistoryItemData.getOrderId());
      holder.mHistoryOrderBinding.tvOrderTime.setText(
              Utilities.getDate(
                      orderHistoryItemData.getCreatedTimeStamp()));
      HistoryStoreAdapter historyStoreAdapter = new HistoryStoreAdapter(
              isSuper,
              orderHistoryItemData.getStoreOrders(), position, mHistoryItemClick,
              orderHistoryItemData.getOrderId(),
              holder.mHistoryOrderBinding.tvOrderTime.getText().toString(),
              mHistoryStoreClickListener, mStoreType);
      holder.mHistoryOrderBinding.rvOrderStores.setAdapter(historyStoreAdapter);

        holder.mHistoryOrderBinding.clOrderItem.setOnClickListener(view -> {
          Bundle bundle = new Bundle();
          bundle.putString(ORDER_ID, orderHistoryItemData.getOrderId());
          bundle.putString(ORDER_TYPE, MASTER_ORDER_TYPE);
          bundle.putString(ORDER_TIME, holder.mHistoryOrderBinding.tvOrderTime.getText().toString());
          mHistoryItemClick.onClick(HISTROY_ORDER_DETAIL, viewHolder.getAdapterPosition(), MINUS_ONE, MINUS_ONE, bundle);
        });
      holder.mHistoryOrderBinding.tvTotalAmount.setText(String.format("%s %s",
              orderHistoryItemData.getAccounting().getCurrencySymbol(),
              orderHistoryItemData.getAccounting().getFinalTotal()));

    }
  }

  /**
   * set up view for pharmacy flow
   * @param holder HistoryViewHolder
   * @param data OrderHistoryItemData
   */
  private void setUpPharmacyStore(HistoryViewHolder holder, OrderHistoryItemData data) {
    holder.mHistoryOrderBinding.tvHistoryMyOrders.setTextSize(15f);
  }


  @Override
  public int getItemCount() {
    return mOrderHistoryItemData != null ? mOrderHistoryItemData.size() : ZERO;
  }

  /**
   * view holder class
   */
  static class HistoryViewHolder extends RecyclerView.ViewHolder {
    ItemLayoutOrderBinding mHistoryOrderBinding;

    /**
     * view holder class constructor
     *
     * @param historyOrderBinding binding reference.
     */
    HistoryViewHolder(@NonNull ItemLayoutOrderBinding historyOrderBinding) {
      super(historyOrderBinding.getRoot());
      mHistoryOrderBinding = historyOrderBinding;
    }
  }



  @Override
  public int getItemViewType(int position) {
      return SUPER;
  }

}
