package chat.hola.com.app.orderdetails;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemHistoryOrderDetailBinding;
import com.kotlintestgradle.model.orderhistory.OrderHistAttributeData;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;

import java.util.ArrayList;
import java.util.function.Predicate;

import chat.hola.com.app.historyproductdetail.OrderDetailClick;

import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;


/**
 * adapter class for history order details
 */
public class HistoryOrderDetailAdapter extends
    RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private Context mContext;
  private ArrayList<OrderHistProductData> mOrderHistProductData;
  private OrderDetailClick mOrderDetailClick;
  private boolean isFood=false;

  /**
   * constructor for this adapter
   *
   * @param orderHistProductData order history product data.
   */
  public HistoryOrderDetailAdapter(
      ArrayList<OrderHistProductData> orderHistProductData, OrderDetailClick orderDetailClick) {
    mOrderHistProductData = orderHistProductData;
    this.mOrderDetailClick = orderDetailClick;
    this.isFood=isFood;
  }

  public HistoryOrderDetailAdapter(boolean isFood,
      ArrayList<OrderHistProductData> orderHistProductData) {
    mOrderHistProductData = orderHistProductData;
    this.isFood=isFood;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();

        ItemHistoryOrderDetailBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.item_history_order_detail, parent, false);
        return new HistoryOrderDetailsViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(
          @NonNull RecyclerView.ViewHolder viewHolder, int position) {
    OrderHistProductData orderHistProductData = mOrderHistProductData.get(position);
    if (viewHolder instanceof HistoryOrderDetailsViewHolder){
      HistoryOrderDetailsViewHolder holder = (HistoryOrderDetailsViewHolder) viewHolder;
      holder.mBinding.tvProductName.setText(orderHistProductData.getName());
      holder.mBinding.tvProductStatus.setText(orderHistProductData.getStatus().getStatusText());
      if (orderHistProductData.getStatus().getStatus() != null && Integer.parseInt(
          orderHistProductData.getStatus().getStatus()) == TWO) {
        holder.mBinding.tvProductStatus.setTextColor(
            mContext.getResources().getColor(R.color.hippieGreen));
      } else {
        holder.mBinding.tvProductStatus.setTextColor(
            mContext.getResources().getColor(R.color.colorOrangePeel));
      }
      if (orderHistProductData.getAttributes() != null
          && orderHistProductData.getAttributes().size() > ZERO) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          Predicate<OrderHistAttributeData> condition =
              orderHistAttributeData -> orderHistAttributeData.getValue() != null
                  && orderHistAttributeData.getValue().equals("");
          orderHistProductData.getAttributes().removeIf(condition);
        }
        StringBuilder attributeName = new StringBuilder();
        for (int i = ZERO; i < orderHistProductData.getAttributes().size(); i++) {
          if (orderHistProductData.getAttributes().get(i).getValue() != null
              && !orderHistProductData.getAttributes().get(
              i).getValue().isEmpty()) {
            attributeName.append(orderHistProductData.getAttributes().get(i).getAttrname()).append(
                ":").append(
                orderHistProductData.getAttributes().get(i).getValue()).append(" ");
          }
          if (i == ONE) {
            break;
          }
        }
        if (attributeName.length() > ZERO) {
          holder.mBinding.tvHistorySizeAndColor.setText(attributeName.toString());
        } else {
          holder.mBinding.tvHistorySizeAndColor.setVisibility(View.GONE);
        }
      }
      String imageUrl = orderHistProductData.getImages().getMedium();
      if (!TextUtils.isEmpty(imageUrl)) {
        Glide.with(mContext)
            .load(imageUrl.replace(" ", "%20"))
            .into(holder.mBinding.ivProductImg);
      }
      holder.mBinding.tvProductCasePrice.setText(
          String.format("%s %s *%s%s", orderHistProductData.getQuantity().getValue(),
              orderHistProductData.getQuantity().getUnit(), orderHistProductData.getCurrencySymbol(),
              orderHistProductData.getSingleUnitPrice().getFinalUnitPrice()));
      holder.mBinding.tvProductOrderId.setText(
          String.format("%s%s", mContext.getResources().getString(R.string.historyProductOrderId),
              orderHistProductData.getProductOrderId()));
      if (orderHistProductData.getOfferDetails() != null) {
        if (orderHistProductData.getOfferDetails().getOfferId() != null
            && !orderHistProductData.getOfferDetails().getOfferId().isEmpty()) {
          holder.mBinding.tvProductBasePrice.setText(
              String.format("%s %s", orderHistProductData.getCurrencySymbol(),
                  orderHistProductData.getSingleUnitPrice().getUnitPrice()));
        } else {
          holder.mBinding.tvProductBasePrice.setVisibility(View.GONE);
        }
      }
      holder.mBinding.tvProductPrice.setText(
          String.format("%s %s", orderHistProductData.getCurrencySymbol(),
              orderHistProductData.getAccounting().getFinalUnitPrice()));
      holder.mBinding.tvSellerName.setText(orderHistProductData.getSellerName());
      holder.mBinding.clProductItem.setOnClickListener(
          view -> mOrderDetailClick.orderItemClick(orderHistProductData.getProductId(),
              orderHistProductData.getCentralProductId()));
    }

  }

  @Override
  public int getItemCount() {
    return mOrderHistProductData != null ? mOrderHistProductData.size() : ZERO;
  }

  /**
   * view holder class for history order detail
   */
  class HistoryOrderDetailsViewHolder extends RecyclerView.ViewHolder {
    ItemHistoryOrderDetailBinding mBinding;

    /**
     * constructor for HistoryOrderDetailsViewHolder
     *
     * @param binding binding object
     */
    HistoryOrderDetailsViewHolder(@NonNull ItemHistoryOrderDetailBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }


}
