package io.isometrik.groupstreaming.ui.effects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.utils.ColorsUtil;
import io.isometrik.groupstreaming.ui.utils.TextDrawable;
import io.isometrik.gs.rtcengine.ar.AREffect;
import java.util.ArrayList;

/**
 * Recycler view adapter for the Ar filter items{@link AREffect}.
 *
 * @see AREffect
 */
public class ArFilterAdapter extends RecyclerView.Adapter<ArFilterAdapter.ViewHolderArFilters> {

  private ArrayList<AREffect> mListData;
  private int textSize;
  private Context mContext;

  public ArFilterAdapter(Context activity, ArrayList<AREffect> mListData) {

    this.mContext = activity;
    this.mListData = mListData;

    textSize = (int) (activity.getResources().getDisplayMetrics().density * 24);
  }

  @Override
  public int getItemCount() {
    return mListData.size();
  }

  @NonNull
  @Override
  public ViewHolderArFilters onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.ism_ar_filters_item, parent, false);

    return new ViewHolderArFilters(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolderArFilters vh, int position) {

    final AREffect arEffect = mListData.get(position);

    if (arEffect != null) {

      vh.tvFilterName.setText(arEffect.getEffectName());
      if (arEffect.isSelected()) {
        vh.tvFilterName.setTextColor(ContextCompat.getColor(mContext, R.color.ism_end_color));

        vh.ivFilterSelected.setVisibility(View.VISIBLE);
      } else {
        vh.tvFilterName.setTextColor(Color.WHITE);

        vh.ivFilterSelected.setVisibility(View.GONE);
      }

      try {
        vh.ivFilterImage.setImageDrawable(TextDrawable.builder()

            .beginConfig()
            .textColor(Color.WHITE)
            .useFont(Typeface.DEFAULT)
            .fontSize(textSize)
            .bold()
            .toUpperCase()
            .endConfig()

            .buildRound((arEffect.getEffectName().trim()).charAt(0) + "",
                Color.parseColor(ColorsUtil.getColorCode(vh.getAdapterPosition() % 19))));
      } catch (Exception ignore) {
      }
    }
  }

  public static class ViewHolderArFilters extends RecyclerView.ViewHolder {

    public TextView tvFilterName;
    public AppCompatImageView ivFilterImage, ivFilterSelected;

    public ViewHolderArFilters(View view) {
      super(view);

      tvFilterName = view.findViewById(R.id.tvFilterName);
      ivFilterImage = view.findViewById(R.id.ivFilterImage);
      ivFilterSelected = view.findViewById(R.id.ivFilterSelected);
    }
  }
}