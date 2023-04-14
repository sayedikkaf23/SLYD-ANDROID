package chat.hola.com.app.DublyCamera;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.Utilities;
import com.ezcall.android.R;
import java.util.ArrayList;

public class DublyFilterAdapter extends RecyclerView.Adapter<ViewHolderDublyFilters> {

  private ArrayList<DublyFilterModel> mListData;
  private Context mContext;
  private boolean previewVideo;

  public DublyFilterAdapter(Context activity, ArrayList<DublyFilterModel> mListData,
      boolean previewVideo) {

    this.mContext = activity;
    this.mListData = mListData;
    this.previewVideo = previewVideo;
  }

  @Override
  public int getItemCount() {
    return mListData.size();
  }

  @NonNull
  @Override
  public ViewHolderDublyFilters onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.video_filter_item, parent, false);

    return new ViewHolderDublyFilters(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolderDublyFilters vh, int position) {

    final DublyFilterModel callItem = mListData.get(position);

    if (callItem != null) {

      vh.filterName.setText(callItem.getFilterName());
      if (callItem.isSelected()) {
        vh.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

        vh.selectedSign.setVisibility(View.VISIBLE);
      } else {
        if (previewVideo) {
          vh.filterName.setTextColor(Color.BLACK);
        } else {
          vh.filterName.setTextColor(Color.WHITE);
        }
        vh.selectedSign.setVisibility(View.GONE);
      }

      vh.filterImage.setImageResource(Utilities.getDrawableByString(mContext,
          "effect_" + callItem.getFilterName().toLowerCase()));
    }
  }
}