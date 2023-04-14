package chat.hola.com.app.ecom.wishlist.sort;

import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.kotlintestgradle.remote.RemoteConstants.ONE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemEcomSortBinding;
import java.util.ArrayList;
import java.util.List;

/*
 * Purpose – This class is using to show List of sort types
 * @author 3Embed
 * Created on Nov 25, 2019
 * Modified on
 */
public class SortTypeListAdapter extends RecyclerView.Adapter<SortTypeListAdapter.SortViewHolder> {
  private static int LAST_SELECTED = -1;
  private List<String> mSortTypeList = new ArrayList<>();
  private OnSortItemClicked mClickListener;
  private int mLayoutVisible = 1;
  private int mLayoutGone = 2;

  SortTypeListAdapter(List<String> sortTypeList, OnSortItemClicked clickListener) {
    this.mSortTypeList.addAll(sortTypeList);
    this.mClickListener = clickListener;
  }

  @NonNull
  @Override
  public SortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemEcomSortBinding homeBannerBinding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_ecom_sort, parent, false);
    homeBannerBinding.getRoot().setVisibility(viewType == mLayoutGone ? View.GONE : View.VISIBLE);
    if (viewType == mLayoutGone) {
      homeBannerBinding.getRoot().setLayoutParams(new RecyclerView.LayoutParams(ZERO, ZERO));
    } else if (viewType == mLayoutVisible) {
      homeBannerBinding.getRoot().setLayoutParams(
          new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    return new SortViewHolder(homeBannerBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull SortViewHolder holder, int position) {
    holder.homeBannerBinding.rbSortName.setText(mSortTypeList.get(position));
    holder.homeBannerBinding.rbSortName.setChecked(LAST_SELECTED == position);

    holder.homeBannerBinding.rbSortName.setOnClickListener(v -> {
      LAST_SELECTED = position;
      mClickListener.onSortItemLClickedListener(position);
      notifyDataSetChanged();
    });
  }

  @Override
  public int getItemCount() {
    return Utilities.isEmptyArray(mSortTypeList) ? ZERO : mSortTypeList.size();
  }

  @Override
  public int getItemViewType(int position) {
    return position == ZERO || position == ONE ? mLayoutGone : mLayoutVisible;
  }

  public interface OnSortItemClicked {
    /**
     * This method is using as a callback method for item click listener
     */
    void onSortItemLClickedListener(int type);
  }

  static class SortViewHolder extends RecyclerView.ViewHolder {
    ItemEcomSortBinding homeBannerBinding;

    SortViewHolder(ItemEcomSortBinding homeBannerBinding) {
      super(homeBannerBinding.getRoot());
      this.homeBannerBinding = homeBannerBinding;
    }
  }
}