package chat.hola.com.app.ecom.cart;

import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.NONE;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;
import static com.kotlintestgradle.data.utils.DataConstants.PHARMACY_FLOW;

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
import com.ezcall.android.databinding.ItemCartBinding;
import com.kotlintestgradle.model.ecom.getcart.CartAccoutingData;
import com.kotlintestgradle.model.ecom.getcart.CartOfferData;
import com.kotlintestgradle.model.ecom.getcart.CartProductItemData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

/*inflates the cart items list*/
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private SelectItem mSelectItem;
  private ArrayList<CartProductItemData> mCartProductItemData;
  private Context mContext;
  private QuantityClick mQuantityClick;
  private boolean mConfirmOrder;
  private int mWithPrescriptionSize, mWithoutPrescriptionSize;

  public CartAdapter(ArrayList<CartProductItemData> cartProductItemData,
      QuantityClick quantityClick, SelectItem selectItem,
      CartType cartType, boolean confirmOrder, int withPrescriptionSize,
      int withoutPrescriptionSize) {
    mCartProductItemData = cartProductItemData;
    this.mQuantityClick = quantityClick;
    this.mConfirmOrder = confirmOrder;
    this.mSelectItem = selectItem;
    this.mWithPrescriptionSize = withPrescriptionSize;
    this.mWithoutPrescriptionSize = withoutPrescriptionSize;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemCartBinding cartBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
        R.layout.item_cart, parent, false);
    return new CartViewHolder(cartBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if (viewHolder instanceof CartViewHolder) {
      CartViewHolder holder = (CartViewHolder) viewHolder;
      CartProductItemData data = mCartProductItemData.get(position);
      if (data == null) return;
      holder.mBinding.clProductItem.setEnabled(data.isEnable());
      holder.mBinding.tvCartProductName.setText(data.getName());
      if (data.getAttributes() != null && data.getAttributes().size() > ZERO) {
        StringBuilder attributeName = new StringBuilder();
        for (int i = ZERO; i < data.getAttributes().size(); i++) {
          if (data.getAttributes().get(i).getValue() != null && !data.getAttributes().get(
              i).getValue().isEmpty()) {
            attributeName.append(data.getAttributes().get(i).getAttrname())
                .append(": ")
                .append(data.getAttributes().get(i).getValue())
                .append(String.format(" %s", (data.getAttributes().get(i).getMeasurementUnitName()
                    == null
                    ? EMPTY_STRING : data.getAttributes().get(i).getMeasurementUnitName())))
                .append(" . ");
          }
        }
        holder.mBinding.tvCartSizeAndColor.setVisibility((attributeName.toString().isEmpty())
            ? View.GONE : View.VISIBLE);
        holder.mBinding.tvCartSizeAndColor.setText(attributeName.toString());
        holder.mBinding.tvCartMore.setVisibility(
            isMoreThanTwoAttributes(data) ? View.VISIBLE : View.GONE);
      }
      holder.mBinding.tvSellerName.setText(data.getStoreName());
      CartAccoutingData accoutingData = data.getAccounting();
      Utilities.printLog(
          "exe" + "accoutingData.getFinalUnitPrice()" + accoutingData.getFinalUnitPrice());
      if (accoutingData.getFinalUnitPrice() != null) {
        holder.mBinding.tvCartFinalPrice.setText(
            mConfirmOrder ? String.format("%s %s*%s%s", data.getQuantity().getValue(),
                data.getQuantity().getUnit(), data.getCurrencySymbol(),
                String.format("%.2f", Float.parseFloat(accoutingData.getFinalTotal()))) :
                String.format("%s%s", data.getCurrencySymbol(),
                    String.format("%.2f", Float.parseFloat(accoutingData.getFinalTotal()))));
      }
      CartOfferData cartOfferData = data.getOfferDetails();
      if (cartOfferData.getOfferId() != null && !cartOfferData.getOfferId().isEmpty()) {
        holder.mBinding.tvCartBasePrice.setText(
            String.format("%s%s", data.getCurrencySymbol(), accoutingData.getUnitPrice()));
        if (cartOfferData.getOfferType() != null && !cartOfferData.getOfferType().isEmpty()) {
          if (Integer.parseInt(cartOfferData.getOfferType()) == ZERO) {
            holder.mBinding.tvCartProductDiscount.setText(
                String.format("%s %s", cartOfferData.getOfferValue(),
                    mContext.getResources().getString(R.string.pdpFlat)));
          } else if (Integer.parseInt(cartOfferData.getOfferType()) == ONE) {
            holder.mBinding.tvCartProductDiscount.setText(
                String.format("%s%s %s", cartOfferData.getOfferValue(), "%",
                    mContext.getResources().getString(R.string.pdpOff)));
          }
        }
      }
      holder.mBinding.tvCartProductQuantity.setText(
          String.format("%s %s", data.getQuantity().getValue(), data.getQuantity().getUnit()));
      holder.mBinding.tvCartRemove.setVisibility(mConfirmOrder ? View.GONE : View.VISIBLE);
      holder.mBinding.tvCartProductQuantity.setVisibility(mConfirmOrder ? View.GONE : View.VISIBLE);
      String imageUrl = data.getImages().getMedium();
      if (!TextUtils.isEmpty(imageUrl)) {
        Glide.with(mContext)
            .load(imageUrl.replace(" ", "%20"))
            .into(holder.mBinding.ivCartProductImg);
      }
      holder.mBinding.tvCartProductQuantity.setOnClickListener(view -> {
        if (mQuantityClick != null && !mConfirmOrder) {
          mQuantityClick.onItemClick(holder.mBinding.tvCartProductQuantity,
              data.getQuantity().getUnit(), position, ZERO);
        }
      });
      holder.mBinding.tvCartMore.setOnClickListener(view -> {
        if (mQuantityClick != null) {
          mQuantityClick.onItemClick(null,
              null, holder.getAdapterPosition(), FOUR);
        }
      });
      holder.mBinding.tvCartRemove.setOnClickListener(view -> {
        if (mQuantityClick != null) {
          mQuantityClick.onItemClick(null,
              null, holder.getAdapterPosition(), THREE);
        }
      });

      holder.mBinding.clProductItem.setOnClickListener(view -> mSelectItem.onSelectItem(position));
    }
  }

  /**
   * check if more than two attributes have values
   *
   * @param data CartProductItemData
   * @return boolean true if more than two attributes have a valid value data
   */
  private boolean isMoreThanTwoAttributes(CartProductItemData data) {
    int count = ZERO;
    for (int i = 0; i < data.getAttributes().size(); i++) {
      if (data.getAttributes().get(i) != null && data.getAttributes().get(i).getValue() != null
          && !data.getAttributes().get(i).getValue().isEmpty()) {
        count++;
      }
    }
    return count > TWO;
  }

  @Override
  public int getItemCount() {
    return Utilities.isEmptyArray(mCartProductItemData) ? ZERO : mCartProductItemData.size();
  }

  @Override
  public int getItemViewType(int position) {
    if (mCartProductItemData != null && mCartProductItemData.get(position) != null) {
        return mCartProductItemData.get(position).getType();
    }
    return NONE;
  }

  static class CartViewHolder extends RecyclerView.ViewHolder {
    ItemCartBinding mBinding;

    CartViewHolder(ItemCartBinding binding) {
      super(binding.getRoot());
      mBinding = binding;
      Utilities.strikeThroughText(mBinding.tvCartBasePrice);
    }
  }
}