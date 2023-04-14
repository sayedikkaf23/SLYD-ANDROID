package chat.hola.com.app.orderdetails.orderboxes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemHistoryInsideBoxBinding;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;

import java.util.ArrayList;

import chat.hola.com.app.orderdetails.SelectItem;

import static chat.hola.com.app.Utilities.Constants.ZERO;
/**
 * adapter class for notifications
 */
public class PendingShipmentAdapter extends
    RecyclerView.Adapter<PendingShipmentAdapter.ViewHolder> {
  private Context mContext;
  private ArrayList<OrderHistProductData> mOrderHistProductData;
  private SelectItem mSelectItem;
  private String mStoreName;

  /**
   * constructor for this class
   *
   * @param notificationList arrayList notification data
   */
  public PendingShipmentAdapter(String storeName,
      ArrayList<OrderHistProductData> notificationList) {
    this.mOrderHistProductData = notificationList;
    this.mStoreName = storeName;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
      int viewType) {
    mContext = parent.getContext();
    ItemHistoryInsideBoxBinding itemHistoryInsideBoxBinding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_history_inside_box, parent, false);
    return new ViewHolder(itemHistoryInsideBoxBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
  }

  @Override
  public int getItemCount() {
    return mOrderHistProductData != null ? mOrderHistProductData.size() : ZERO;
  }

  /**
   * view holder class
   */
  class ViewHolder extends RecyclerView.ViewHolder {
    ItemHistoryInsideBoxBinding mItemHistoryInsideBoxBinding;

    /**
     * view holder class constructor
     *
     * @param itemHistoryInsideBoxBinding binding reference.
     */
    ViewHolder(@NonNull ItemHistoryInsideBoxBinding itemHistoryInsideBoxBinding) {
      super(itemHistoryInsideBoxBinding.getRoot());
      mItemHistoryInsideBoxBinding = itemHistoryInsideBoxBinding;
    }
  }
}
