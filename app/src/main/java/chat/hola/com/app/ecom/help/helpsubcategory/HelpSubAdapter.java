package chat.hola.com.app.ecom.help.helpsubcategory;

import static chat.hola.com.app.Utilities.Constants.TITLE;
import static chat.hola.com.app.Utilities.Constants.URL;
import static chat.hola.com.app.Utilities.Constants.ZERO;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.webScreen.WebActivity;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemHelpBinding;
import com.kotlintestgradle.model.ecom.help.HelpSubCatListData;
import java.util.ArrayList;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */
public class HelpSubAdapter extends RecyclerView.Adapter<HelpSubAdapter.ViewHolder> {
  private ArrayList<HelpSubCatListData> helpDataArrayList;
  private Context mContext;

  HelpSubAdapter(ArrayList<HelpSubCatListData> helpDataArrayList) {
    this.helpDataArrayList = helpDataArrayList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    mContext = parent.getContext();
    ItemHelpBinding binding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.getContext()),
        R.layout.item_help, parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final HelpSubCatListData helpData = helpDataArrayList.get(position);
    holder.mHelpContentsRowBinding.helpWithThisOrderContentTv.setText(helpData.getName());
    holder.mHelpContentsRowBinding.helpLl.setOnClickListener(view -> {
      Intent intent = new Intent(mContext, WebActivity.class);
      intent.putExtra(URL, helpData.getLink());
      intent.putExtra(TITLE, helpData.getName());
      mContext.startActivity(intent);
    });
  }

  @Override
  public int getItemCount() {
    return helpDataArrayList != null ? helpDataArrayList.size() : ZERO;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ItemHelpBinding mHelpContentsRowBinding;

    public ViewHolder(ItemHelpBinding binding) {
      super(binding.getRoot());
      this.mHelpContentsRowBinding = binding;
    }
  }
}
