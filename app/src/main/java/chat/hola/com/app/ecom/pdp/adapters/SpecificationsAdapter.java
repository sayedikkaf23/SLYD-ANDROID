package chat.hola.com.app.ecom.pdp.adapters;

import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.SIX;
import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemPdpSpecificationsListBinding;
import com.kotlintestgradle.model.ecom.getcart.CartAttributesData;
import com.kotlintestgradle.model.ecom.pdp.InnerAttributesData;
import java.util.ArrayList;

/**
 * adapter class for the specifications
 */
public class SpecificationsAdapter extends
    RecyclerView.Adapter<SpecificationsAdapter.SpecificationsViewHolder> {
  private ArrayList<InnerAttributesData> mSpecialitiesList;
  private ArrayList<CartAttributesData> mCartAttributesData;
  private boolean mIsFromPdp;

  /**
   * constructor for this specifications adaper class
   *
   * @param specialitiesList specifications list
   * @param isFromPdp       this is coming from pdp or all details activity
   */
  public SpecificationsAdapter(ArrayList<InnerAttributesData> specialitiesList,
      boolean isFromPdp) {
    this.mSpecialitiesList = specialitiesList;
    this.mIsFromPdp = isFromPdp;
  }

  /**
   * constructor for this specifications adaper class
   *
   * @param cartAttributesData specifications list
   */
  public SpecificationsAdapter(ArrayList<CartAttributesData> cartAttributesData) {
    this.mCartAttributesData = cartAttributesData;
  }

  @NonNull
  @Override
  public SpecificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemPdpSpecificationsListBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_pdp_specifications_list, parent, false);
    return new SpecificationsViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull SpecificationsViewHolder holder, int position) {
    if (mSpecialitiesList != null) {
      InnerAttributesData innerAttributesData = mSpecialitiesList.get(position);
      if (innerAttributesData != null) {
        holder.mBinding.tvSpecKey.setText(innerAttributesData.getName());
        holder.mBinding.tvSpecValue.setText(innerAttributesData.getValue());
      }
    }
    if (mCartAttributesData != null) {
      CartAttributesData cartAttributesData = mCartAttributesData.get(position);
      if (cartAttributesData != null) {
        if (cartAttributesData.getValue() != null && !cartAttributesData.getValue().isEmpty()) {
          holder.mBinding.tvSpecKey.setText(cartAttributesData.getAttrname());
          holder.mBinding.tvSpecValue.setText(cartAttributesData.getValue());
          holder.mBinding.tvSpecValue.setGravity(Gravity.END);
        }
      }
    }
  }

  @Override
  public int getItemCount() {
    return mCartAttributesData != null ? mCartAttributesData.size()
        : (mSpecialitiesList != null && mSpecialitiesList.size() > ZERO) ? (mIsFromPdp ? (
            (mSpecialitiesList.size() > FIVE) ? SIX : mSpecialitiesList.size())
            : mSpecialitiesList.size()) : ZERO;
  }

  /**
   * view holder class for the specifications
   */
  class SpecificationsViewHolder extends RecyclerView.ViewHolder {
    ItemPdpSpecificationsListBinding mBinding;

    SpecificationsViewHolder(@NonNull ItemPdpSpecificationsListBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}