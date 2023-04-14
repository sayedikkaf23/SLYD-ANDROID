package chat.hola.com.app.ecom.pdp.adapters;

import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemHtmlAttributesBinding;
import com.kotlintestgradle.model.ecom.pdp.InnerAttributesData;
import java.util.ArrayList;

/*
 * adapter for HTML attributes
 */

public class HtmlAttributesAdapter extends RecyclerView.Adapter<HtmlAttributesAdapter.ViewHolder> {

  private ArrayList<InnerAttributesData> mData;
  private OnAttributesClickListener mListener;
  private Context mContext;

  public HtmlAttributesAdapter(ArrayList<InnerAttributesData> data,
                               OnAttributesClickListener listener) {
    this.mData = data;
    this.mListener = listener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemHtmlAttributesBinding mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.getContext()),
            R.layout.item_html_attributes, parent, false);
    return new ViewHolder(mBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.mBinding.tvAttributeName.setText(mData.get(position).getName());
    holder.mBinding.tvAttributeName.setOnClickListener(
            v -> mListener.onAttributesClicked(mData.get(position)));
  }

  @Override
  public int getItemCount() {
    return Utilities.isEmptyArray(mData) ? ZERO : mData.size();
  }

  /**
   * ViewHolder class for HtmlAttributes items
   */
  static class ViewHolder extends RecyclerView.ViewHolder {

    private ItemHtmlAttributesBinding mBinding;

    public ViewHolder(@NonNull ItemHtmlAttributesBinding binding) {
      super(binding.getRoot());
      this.mBinding = binding;
    }
  }

  /**
   * listener for attributes
   */
  public interface OnAttributesClickListener {

    /**
     * called when attributes is clicked
     * @param position
     */
    void onAttributesClicked(InnerAttributesData position);
  }
}
