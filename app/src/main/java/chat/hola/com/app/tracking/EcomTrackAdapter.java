package chat.hola.com.app.tracking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemTrackingBinding;
import com.kotlintestgradle.model.tracking.TrackingItemData;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.DateAndTimeUtil;

import static chat.hola.com.app.Utilities.Constants.INVALID_DATE;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;


/**
 * adapter class for the tracking activity.
 */
public class EcomTrackAdapter extends RecyclerView.Adapter<EcomTrackAdapter.TrackViewHolder> {
  private ArrayList<TrackingItemData> mTrackingItemData;
  private Context mContext;
  private String mReason;

  EcomTrackAdapter(
          ArrayList<TrackingItemData> trackingItemData) {
    mTrackingItemData = trackingItemData;
  }

  public void setReason(String reason) {
    mReason = reason;
  }

  @NonNull
  @Override
  public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
    mContext = parent.getContext();
    ItemTrackingBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.item_tracking, parent, false);
    return new TrackViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
    TrackingItemData trackingItemData = mTrackingItemData.get(position);
    holder.mBinding.tvTrackingStatus.setText(trackingItemData.getStatusText());
    if (trackingItemData.getTime() != null && !trackingItemData.getTime().isEmpty()) {
      if (!trackingItemData.getFormatedDate().equals(INVALID_DATE)){
        holder.mBinding.tvOrderStatusDate.setText(
                DateAndTimeUtil.getTrackingTransactionTime(trackingItemData.getTime()));
      }
      holder.mBinding.tvOrderStatusDate.setVisibility(trackingItemData.getTime().equals("0")?
              View.GONE :View.VISIBLE);
      if (trackingItemData.getStatus() != null) {
        if (Integer.parseInt(trackingItemData.getStatus()) == THREE) {
          holder.mBinding.tvOrderStatusReason.setVisibility(View.VISIBLE);
          holder.mBinding.tvOrderStatusReason.setText(mReason);
        }
      }
    }else {
      holder.mBinding.tvOrderStatusDate.setVisibility(View.GONE);
    }
    holder.mBinding.viewTrackIndicatorHalf1.setBackgroundColor(trackingItemData.isViewStatus1()
            ? mContext.getResources().getColor(R.color.colorightWhite)
            : mContext.getResources().getColor(R.color.hippieGreen));
    holder.mBinding.viewTrackIndicatorHalf2.setBackgroundColor(trackingItemData.isViewStatus2()
            ? mContext.getResources().getColor(R.color.colorightWhite)
            : mContext.getResources().getColor(R.color.hippieGreen));
    holder.mBinding.ivCircle.setImageDrawable(
            trackingItemData.getFormatedDate().isEmpty() || trackingItemData.getFormatedDate().equals(INVALID_DATE) ? mContext.getResources().getDrawable(
                    R.drawable.alto_circle) : mContext.getResources().getDrawable(R.drawable.green_circle));
    if (mTrackingItemData.size() - ONE == position) {
      holder.mBinding.viewTrackIndicatorHalf1.setVisibility(View.GONE);
      holder.mBinding.viewTrackIndicatorHalf2.setVisibility(View.GONE);
    }
  }

  @Override
  public int getItemCount() {
    return mTrackingItemData != null ? mTrackingItemData.size() : ZERO;
  }

  class TrackViewHolder extends RecyclerView.ViewHolder {
    ItemTrackingBinding mBinding;

    TrackViewHolder(@NonNull ItemTrackingBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}
