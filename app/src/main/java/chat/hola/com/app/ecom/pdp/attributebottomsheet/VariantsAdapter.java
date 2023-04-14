package chat.hola.com.app.ecom.pdp.attributebottomsheet;

import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemVariantsBottomSheetBinding;
import com.kotlintestgradle.model.ecom.pdp.SizeData;
import com.kotlintestgradle.model.ecom.pdp.VariantsData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * adapter class for this variants in bottom sheet.
 */
public class VariantsAdapter extends RecyclerView.Adapter<VariantsAdapter.VariantsViewHolder> {
  private ArrayList<VariantsData> mVariantsDataArrayList;
  private ProductAttributesClick mProductAttributesClick;
  private Context mContext;

  /**
   * constructor for variants adapter
   *
   * @param variantsData           variants mData
   * @param productAttributesClick ProductAttributesClick interface
   */
  VariantsAdapter(ArrayList<VariantsData> variantsData,
      ProductAttributesClick productAttributesClick) {
    mVariantsDataArrayList = variantsData;
    this.mProductAttributesClick = productAttributesClick;
  }

  @NonNull
  @Override
  public VariantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    mContext = parent.getContext();
    ItemVariantsBottomSheetBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_variants_bottom_sheet, parent, false);
    return new VariantsViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull VariantsViewHolder holder, int position) {
    VariantsData variantsData = mVariantsDataArrayList.get(position);
    if (variantsData != null) {
      if (variantsData.getName() != null && !variantsData.getName().isEmpty()) {
        holder.mBinding.tvAttributeName.setText(variantsData.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          ArrayList<SizeData> sizeData = new ArrayList<>();
          sizeData.clear();
          sizeData.addAll(variantsData.getSizeData());
          Predicate<SizeData> condition =
              cartAttributesData -> !cartAttributesData.getIsPrimary();
          sizeData.removeIf(condition);
          if (sizeData.size() > ZERO) {
            holder.mBinding.tvAttributeSelName.setText(
                String.format(": %s", sizeData.get(ZERO).getName()));
          } else {
            holder.mBinding.tvAttributeSelName.setVisibility(View.GONE);
          }
        }
      } else {
        holder.mBinding.tvAttributeName.setVisibility(View.GONE);
      }
      if (variantsData.getSizeData() != null && variantsData.getSizeData().size() > ZERO) {
        Collections.sort(variantsData.getSizeData(),
            (sizeData, t1) -> t1.getSizeString().compareTo(sizeData.getSizeString()));
        holder.mBinding.rvPdpVariants.setVisibility(View.VISIBLE);
        VariantsItemAdapter variantsItemAdapter = new VariantsItemAdapter(
            variantsData.getSizeData(), mProductAttributesClick);
        if (mVariantsDataArrayList.get(position).getName().equals(
            mContext.getResources().getString(R.string.pdpColors))) {
          RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, FIVE);
          holder.mBinding.rvPdpVariants.setLayoutManager(layoutManager);
        } else {
          RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, THREE);
          holder.mBinding.rvPdpVariants.setLayoutManager(layoutManager);
        }
        holder.mBinding.rvPdpVariants.setAdapter(variantsItemAdapter);
      } else {
        holder.mBinding.rvPdpVariants.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public int getItemCount() {
    return mVariantsDataArrayList != null ? mVariantsDataArrayList.size() : ZERO;
  }

  /**
   * view holder class for the variants item
   */
  static class VariantsViewHolder extends RecyclerView.ViewHolder {
    private ItemVariantsBottomSheetBinding mBinding;

    VariantsViewHolder(@NonNull ItemVariantsBottomSheetBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}