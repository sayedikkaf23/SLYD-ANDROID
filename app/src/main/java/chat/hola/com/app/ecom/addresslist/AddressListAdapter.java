package chat.hola.com.app.ecom.addresslist;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.NONE;
import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemAddressBinding;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import java.util.ArrayList;

/*
 * Purpose – This class Holds list of Address added by user.
 * @author 3Embed
 * Created on Dec 10, 2019
 * Modified on
 */
public class AddressListAdapter extends
    RecyclerView.Adapter<AddressListAdapter.AddressListViewHolder> {
  private ArrayList<AddressListItemData> mListItemData;
  private OnAddressItemClickListener mClickListener;
  private int mLastSelectedPosition = NONE;
  private boolean mIsFreshlyLaunched = true;
  private String mDefaultAddressId;
  private Context mContext;
  private boolean mIsFromCart;
  private Handler mHandler = new Handler();
  private boolean mIsFromRide;

  AddressListAdapter(
      ArrayList<AddressListItemData> listItemData, OnAddressItemClickListener clickListener,
      boolean isFromCart, int lastSelectedPosition, boolean isFromRide) {
    this.mListItemData = listItemData;
    this.mClickListener = clickListener;
    this.mIsFromCart = isFromCart;
    this.mIsFromRide = isFromRide;
    this.mLastSelectedPosition = lastSelectedPosition;
  }

  @NonNull
  @Override
  public AddressListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemAddressBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_address, parent, false);
    return new AddressListViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull AddressListViewHolder holder, int position) {
    if (mListItemData.get(position).isDefault()) {
      mDefaultAddressId = mListItemData.get(position).getId();
      if (mIsFreshlyLaunched) {
        mIsFreshlyLaunched = FALSE;
        if (mIsFromCart) {
          int copyOfLastCheckedPosition = mLastSelectedPosition;
          mLastSelectedPosition = position;
          mClickListener.selectAddress(mListItemData.get(position));
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              notifyItemChanged(copyOfLastCheckedPosition);
              notifyItemChanged(mLastSelectedPosition);
            }
          });
        } else {
          mClickListener.selectAddress(mListItemData.get(position));
        }
      }
    }
    if(!mIsFromRide)
    setSelectableBackground(holder, position == mLastSelectedPosition);
    else
      setSelectableBackgroundRide(holder, position == mLastSelectedPosition);
    setDrawables(holder, position);
    holder.mBinding.ivDeleteAddress.setEnabled(!mListItemData.get(position).isDefault());
    holder.mBinding.tvAddressDefault.setVisibility(
        mListItemData.get(position).isDefault() ? View.VISIBLE : View.INVISIBLE);
    holder.mBinding.tvMakeDefault.setVisibility(!mIsFromCart ? (!mListItemData.get(
        position).isDefault()
        ? View.VISIBLE : View.INVISIBLE) : View.GONE);
    holder.mBinding.tvName.setText(mListItemData.get(position).getName());
    holder.mBinding.tvAddressType.setText(mListItemData.get(position).getTaggedAs());
    holder.mBinding.tvPhNum.setText(
        String.format("+%s %s", mListItemData.get(position).getMobileNumberCode(),
            mListItemData.get(position).getMobileNumber()));
    holder.mBinding.tvProfileAddress.setText(
        String.format("%s,%s,%s,%s", mListItemData.get(position).getAddLine1(),
            mListItemData.get(position).getLocality(), mListItemData.get(position).getCity(),
            mListItemData.get(position).getCountry()));
    holder.mBinding.ivEditAddress.setOnClickListener(
        view -> mClickListener.onEditClickListener(mListItemData.get(position)));
    holder.mBinding.ivDeleteAddress.setOnClickListener(
        view -> mClickListener.onDeleteClickListener(mListItemData.get(position).getId()));
    if (mIsFromCart) {
      holder.mBinding.cvAddress.setOnClickListener(view -> {
        int copyOfLastCheckedPosition = mLastSelectedPosition;
        mLastSelectedPosition = position;
        if (mListItemData.get(position).isDefault()) {
          mClickListener.selectAddress(mListItemData.get(position));
          notifyItemChanged(copyOfLastCheckedPosition);
          notifyItemChanged(mLastSelectedPosition);
        } else {
          mClickListener.makeAddressDefault(mListItemData.get(position).getId(),
              mDefaultAddressId, mLastSelectedPosition);
        }
      });
    } else {
      holder.mBinding.tvMakeDefault.setOnClickListener(view -> {
        if (mListItemData.get(position).isDefault()) {
          mClickListener.selectAddress(mListItemData.get(position));
        } else {
          mClickListener.makeAddressDefault(mListItemData.get(position).getId(),
              mDefaultAddressId, mLastSelectedPosition);
        }
      });
    if(mIsFromRide) {
      holder.mBinding.cvAddress.setOnClickListener(v -> {
        int copyOfLastCheckedPosition = mLastSelectedPosition;
        mLastSelectedPosition = position;
        if (mListItemData.get(position).isDefault()) {
          mClickListener.selectRideAddress(mListItemData.get(position));
          notifyItemChanged(copyOfLastCheckedPosition);
          notifyItemChanged(mLastSelectedPosition);
        } else {
          mClickListener.makeAddressDefault(mListItemData.get(position).getId(),
              mDefaultAddressId, mLastSelectedPosition);
        }
      });
    }
    }
  }

  /**
   * set background of card selectable for ride
   *
   * @param isSelected boolean
   */
  private void setSelectableBackgroundRide(AddressListViewHolder holder, boolean isSelected) {
    holder.mBinding.cvAddress.setBackground(ContextCompat.getDrawable(mContext, isSelected
        ? R.drawable.add_select_ride_address : R.drawable.white_selected__rect_background));
    holder.mBinding.tvAddressType.setBackgroundColor(ContextCompat.getColor(mContext, isSelected
        ? R.color.purple : R.color.wildSand));
    holder.mBinding.tvAddressType.setTextColor(ContextCompat.getColor(mContext, isSelected
        ? R.color.white : R.color.color_black));
  }

  /**
   * set background of ard selectable
   *
   * @param isSelected boolean
   */
  private void setSelectableBackground(AddressListViewHolder holder, boolean isSelected) {
    holder.mBinding.cvAddress.setCardBackgroundColor(ContextCompat.getColor(mContext, isSelected
        ? R.color.addressGreen : R.color.white));
    holder.mBinding.tvAddressType.setBackgroundColor(ContextCompat.getColor(mContext, isSelected
        ? R.color.colorAddressGreen : R.color.wildSand));
    holder.mBinding.tvAddressType.setTextColor(ContextCompat.getColor(mContext, isSelected
        ? R.color.white : R.color.color_black));
  }

  @Override
  public int getItemCount() {
    return Utilities.isEmptyArray(mListItemData) ? ZERO : mListItemData.size();
  }

  /**
   * This method is using to set Image drawables
   */
  private void setDrawables(AddressListViewHolder holder, int position) {
    Drawable deleteIcon = mListItemData.get(position).isDefault()
        ? mContext.getResources().getDrawable(
        R.drawable.address_non_active_delete) : mContext.getResources().getDrawable(
        R.drawable.ic_delete);
    holder.mBinding.ivDeleteAddress.setImageDrawable(deleteIcon);
  }

  public interface OnAddressItemClickListener {
    /**
     * This method is using to listen for delete action click
     *
     * @param addressId addressId of the Address to be delete
     */
    void onDeleteClickListener(String addressId);

    /**
     * This method is using to listen for Edit click action of address elements
     *
     * @param addressListItemData address to be Edit
     */
    void onEditClickListener(AddressListItemData addressListItemData);

    void makeAddressDefault(String addressId, String prevDefAddressId, int lastSelectedPosition);

    void selectAddress(AddressListItemData addressListItemData);

    void selectRideAddress(AddressListItemData addressListItemData);
  }

  static class AddressListViewHolder extends RecyclerView.ViewHolder {
    ItemAddressBinding mBinding;

    AddressListViewHolder(ItemAddressBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }
}