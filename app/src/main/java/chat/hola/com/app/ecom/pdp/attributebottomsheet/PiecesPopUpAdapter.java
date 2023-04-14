package chat.hola.com.app.ecom.pdp.attributebottomsheet;

import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import androidx.databinding.DataBindingUtil;
import com.ezcall.android.R;
import com.ezcall.android.databinding.QuantityBottomsheetItemBinding;
import java.util.ArrayList;

/**
 * adapter class for the pop up window for the quantity selecting.
 */
public class PiecesPopUpAdapter extends BaseAdapter {
  private Context mContext;
  private QuantityItemClick mQuantityItemClick;
  private ArrayList<String> mQuantityList;

  /**
   * constructor class for the pieces pop up
   *
   * @param laundryProductData arrayList of laundry product mData
   * @param quantityItemClick  interface listener for the quantity click.
   */
  public PiecesPopUpAdapter(ArrayList<String> laundryProductData,
      QuantityItemClick quantityItemClick) {
    this.mQuantityList = laundryProductData;
    this.mQuantityItemClick = quantityItemClick;
  }

  @Override
  public int getCount() {
    return mQuantityList != null ? mQuantityList.size() : ZERO;
  }

  @Override
  public Object getItem(int position) {
    return mQuantityList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return ZERO;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    PiecesPopUpAdapter.ViewHolder holder;
    mContext = parent.getContext();
    if (convertView == null) {
      QuantityBottomsheetItemBinding binding = DataBindingUtil.inflate(
          LayoutInflater.from(parent.getContext()),
          R.layout.quantity_bottomsheet_item, parent, false);
      convertView = binding.getRoot();
      holder = new ViewHolder(binding);
      convertView.setTag(holder);
    } else {
      holder = (PiecesPopUpAdapter.ViewHolder) convertView.getTag();
    }
    holder.mBinding.tvQuantity.setText(mQuantityList.get(position));
    holder.mBinding.tvQuantity.setOnClickListener(view -> {
      String itemName = mQuantityList.get(position);
      mQuantityItemClick.onItemClick(itemName);
    });
    return convertView;
  }

  /**
   * <P>Holder class for single  row </P>
   */
  public class ViewHolder {
    QuantityBottomsheetItemBinding mBinding;

    ViewHolder(QuantityBottomsheetItemBinding binding) {
      this.mBinding = binding;
    }
  }
}