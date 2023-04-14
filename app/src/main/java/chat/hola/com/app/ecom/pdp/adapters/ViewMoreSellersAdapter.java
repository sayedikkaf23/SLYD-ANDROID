package chat.hola.com.app.ecom.pdp.adapters;

import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ecom.pdp.ViewMoreSellersOnClick;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemViewMoreSellersBinding;
import com.kotlintestgradle.model.ecom.common.FinalPriceListData;
import com.kotlintestgradle.model.ecom.pdp.SupplierData;
import java.util.ArrayList;

/**
 * inflates the sellers list
 */
public class ViewMoreSellersAdapter extends
    RecyclerView.Adapter<ViewMoreSellersAdapter.ViewMoreSellersViewHolder> {
  private ArrayList<SupplierData> mSupplierArrayList;
  private Context mContext;
  private ViewMoreSellersOnClick mMoreSellersOnClick;

  public ViewMoreSellersAdapter(
      ArrayList<SupplierData> supplierArrayList, ViewMoreSellersOnClick moreSellersOnClick) {
    mSupplierArrayList = supplierArrayList;
    this.mMoreSellersOnClick = moreSellersOnClick;
  }

  @NonNull
  @Override
  public ViewMoreSellersAdapter.ViewMoreSellersViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemViewMoreSellersBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_view_more_sellers, parent, false);
    return new ViewMoreSellersViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewMoreSellersViewHolder holder,
      int position) {
    SupplierData supplier = mSupplierArrayList.get(position);
    if (supplier != null) {
      holder.mBinding.tvViewMoreSellerName.setText(supplier.getSupplier().getSupplierName());
      if (supplier.getSupplier().getRating() != null) {
        holder.mBinding.tvSellerOverallRating.setText(
            (String.format("%.1f", Float.parseFloat(supplier.getSupplier().getRating()))));
      }
      FinalPriceListData finalPriceListData = supplier.getFinalPriceList();
      Utilities.printLog("basePrice" + finalPriceListData.getBasePrice() + "tPercentage"
          + finalPriceListData.getDiscountPercentage());
      if (finalPriceListData.getFinalPrice() != null) {
        holder.mBinding.tvViewSellersProductActualPrice.setText(
            String.format("%s%s", supplier.getCurrencySymbol(),
                String.format("%.2f", Float.parseFloat(finalPriceListData.getFinalPrice()))));
      }
      if (finalPriceListData.getDiscountPercentage() != null && TextUtils.isDigitsOnly(
          finalPriceListData.getDiscountPercentage()) && Integer.parseInt(
          finalPriceListData.getDiscountPercentage()) != ZERO) {
        holder.mBinding.tvViewSellersOfferPercentage.setText(
            String.format("(%s %s%% %s)",
                mContext.getString(R.string.pdpOnUpto),
                finalPriceListData.getDiscountPercentage(),
                mContext.getString(R.string.pdpOff)));
        holder.mBinding.tvViewSellersOfferPercentage.setVisibility(View.VISIBLE);
      } else {
        holder.mBinding.vgSellerOffer.setVisibility(
            ((finalPriceListData.getDiscountPrice() != null
                && !finalPriceListData.getDiscountPrice().isEmpty()
                && !finalPriceListData.getDiscountPrice().equals(String.format("%d", ZERO))))
                ? View.VISIBLE
                : View.GONE);
        if (finalPriceListData.getBasePrice() != null) {
          holder.mBinding.tvViewSellersOfferPrice.setText(
              finalPriceListData.getBasePrice());
        }
        if (finalPriceListData.getDiscountPrice() != null
            && !finalPriceListData.getDiscountPrice().isEmpty()
            && !finalPriceListData.getDiscountPrice().equals("" + ZERO)) {
          holder.mBinding.tvViewSellersOfferPercentage.setText(
              String.format("(%s %s %s)",
                  mContext.getString(R.string.pdpFlat),
                  finalPriceListData.getDiscountPrice(),
                  mContext.getString(R.string.pdpOff)));
        }
      }
      holder.mBinding.cvItem.setOnClickListener(
          view -> mMoreSellersOnClick.onClick(position));
    }
  }

  @Override
  public int getItemCount() {
    return mSupplierArrayList != null ? mSupplierArrayList.size() : ZERO;
  }

  static class ViewMoreSellersViewHolder extends RecyclerView.ViewHolder {
    private ItemViewMoreSellersBinding mBinding;

    ViewMoreSellersViewHolder(@NonNull ItemViewMoreSellersBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
      Utilities.strikeThroughText(mBinding.tvViewSellersOfferPrice);
    }
  }
}