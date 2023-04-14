package chat.hola.com.app.orderdetails.orderboxes;

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
import com.ezcall.android.databinding.ItemHistoryProductDetailBinding;
import com.kotlintestgradle.model.orderhistory.OrderHistAttributeData;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;

import java.util.ArrayList;
import java.util.function.Predicate;

import chat.hola.com.app.historyproductdetail.OrderDetailClick;

import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;


/**
 * Adapter class for the history product detail
 */
public class BoxItemsAdapter extends
    RecyclerView.Adapter<BoxItemsAdapter.HistoryProductDetailViewHolder> {
  private Context mContext;
  private ArrayList<OrderHistProductData> mOrderHistProductData;
  private OrderDetailClick mOrderDetailClick;
  private String mStoreName;

  /**
   * constructor for this adapter
   *
   * @param orderHistProductData order history product data
   */
  public BoxItemsAdapter(String storeName,
      ArrayList<OrderHistProductData> orderHistProductData, OrderDetailClick orderDetailClick) {
    mOrderHistProductData = orderHistProductData;
    mOrderDetailClick = orderDetailClick;
    mStoreName = storeName;
  }

  @NonNull
  @Override
  public HistoryProductDetailViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemHistoryProductDetailBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_history_product_detail, parent, false);
    return new HistoryProductDetailViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(
      @NonNull HistoryProductDetailViewHolder holder, int position) {
    OrderHistProductData data = mOrderHistProductData.get(position);
    String imageUrl = data.getImages().getMedium();
    if (!TextUtils.isEmpty(imageUrl)) {
      Glide.with(mContext)
          .load(imageUrl.replace(" ", "%20"))
          .into(holder.mBinding.ivProductImg);
    }
    holder.mBinding.tvProductName.setText(data.getName());
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      Predicate<OrderHistAttributeData> condition =
          orderHistAttributeData -> orderHistAttributeData.getValue().equals("");
      data.getAttributes().removeIf(condition);
      if (data.getAttributes() != null && data.getAttributes().size() > ZERO) {
        StringBuilder attributeName = new StringBuilder();
        for (int i = ZERO; i < data.getAttributes().size(); i++) {
          attributeName.append(data.getAttributes().get(i).getAttrname()).append(":").append(
              data.getAttributes().get(i).getValue()).append(" ");
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
    }
    holder.mBinding.tvProductOrderId.setVisibility(View.VISIBLE);
    holder.mBinding.tvProductOrderId.setText(
        String.format("%s%s", mContext.getResources().getString(R.string.historyProductOrderId),
            data.getProductId()));
    holder.mBinding.tvProductCasePrice.setText(
        String.format("%s %s *%s%s", data.getQuantity().getValue(),
            data.getQuantity().getUnit(), data.getCurrencySymbol(),
            data.getSingleUnitPrice().getFinalUnitPrice()));
    if (data.getOfferDetails() != null) {
      if (data.getOfferDetails().getOfferId() != null
          && !data.getOfferDetails().getOfferId().isEmpty()) {
        holder.mBinding.tvProductBasePrice.setText(
            String.format("%s %s", data.getCurrencySymbol(),
                data.getSingleUnitPrice().getUnitPrice()));
      } else {
        holder.mBinding.tvProductBasePrice.setVisibility(View.GONE);
      }
    }
    holder.mBinding.tvSellerName.setText(mStoreName);
    holder.mBinding.clProductItem.setOnClickListener(
        view -> mOrderDetailClick.orderItemClick(data.getProductId(),
            data.getCentralProductId()));
  }

  @Override
  public int getItemCount() {
    return mOrderHistProductData != null ? mOrderHistProductData.size() : ZERO;
  }

  /**
   * view holder class
   */
  public class HistoryProductDetailViewHolder extends RecyclerView.ViewHolder {
    private ItemHistoryProductDetailBinding mBinding;

    /**
     * constructor for this view holder
     *
     * @param binding binding object.
     */
    public HistoryProductDetailViewHolder(@NonNull ItemHistoryProductDetailBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}
