package chat.hola.com.app.orderdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemPackageDetailsBinding;
import com.kotlintestgradle.model.orderdetails.pharmacy.PackingDetailItem;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.historyproductdetail.OrderDetailClick;
import chat.hola.com.app.orderdetails.orderboxes.BoxItemsAdapter;

import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;


public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {
    private Context mContext;
    private  ArrayList<PackingDetailItem> mHashMap;
    private OrderDetailClick mOrderDetailClick;
    private OnPackageTrackingClickListener listener;
    private SelectItem mSelectItem;
    private String mStoreName;

    public PackageAdapter(
            ArrayList<PackingDetailItem> hashMap,
                             OrderDetailClick orderDetailClick,OnPackageTrackingClickListener listener) {
        mHashMap = hashMap;
        mOrderDetailClick = orderDetailClick;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                        int viewType) {
        mContext = parent.getContext();
        ItemPackageDetailsBinding itemHistoryBoxBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_package_details, parent, false);
        return new ViewHolder(itemHistoryBoxBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItemHistoryBoxBinding.tvProductName.setText(
                String.format("%s %d", mContext.getResources().getString(R.string.box), (position + ONE)));
        holder.mItemHistoryBoxBinding.tvOrderNo.setText(mHashMap.get(position).getStoreOrderId());
            holder.mItemHistoryBoxBinding.tvPid.setText(
                    String.format("%s: %s", mContext.getResources().getString(R.string.packageId),
                            mHashMap.get(position).getPackageId()));
            holder.mItemHistoryBoxBinding.tvOrderStatus.setText(
                            mHashMap.get(position).getStatus().getStatusText());
        holder.mItemHistoryBoxBinding.tvOrderTimeTrack.setText(
                Utilities.getDateForHistoryDet(
                        mHashMap.get(position).getStatus().getUpdatedOnTimeStamp()));
        holder.mItemHistoryBoxBinding.clTracking.setOnClickListener(
                view -> listener.onTrackClicked(mHashMap.get(position),"Box "+String.valueOf(position+1)));
        BoxItemsAdapter splitBoxItemsAdapter = new BoxItemsAdapter(mStoreName,
                (ArrayList<OrderHistProductData>) mHashMap.get(position).getProducts(), mOrderDetailClick);
        holder.mItemHistoryBoxBinding.rvOrderStores.setAdapter(splitBoxItemsAdapter);
    }

    @Override
    public int getItemCount() {
        return mHashMap != null ? mHashMap.size() : ZERO;
    }

    /**
     * view holder class
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ItemPackageDetailsBinding mItemHistoryBoxBinding;

        /**
         * view holder class constructor
         *
         * @param itemHistoryBoxBinding binding reference.
         */
        ViewHolder(@NonNull ItemPackageDetailsBinding itemHistoryBoxBinding) {
            super(itemHistoryBoxBinding.getRoot());
            mItemHistoryBoxBinding = itemHistoryBoxBinding;
        }
    }

    interface OnPackageTrackingClickListener{
        void onTrackClicked(PackingDetailItem packingDetailItem,String postion);
    }
}
