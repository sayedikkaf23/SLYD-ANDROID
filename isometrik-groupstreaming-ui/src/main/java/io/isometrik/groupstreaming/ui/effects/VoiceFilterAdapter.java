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
import io.isometrik.gs.rtcengine.voice.VoiceEffect;
import java.util.ArrayList;

/**
 * Recycler view adapter for the Voice filter items{@link VoiceEffect}.
 *
 * @see VoiceEffect
 */
public class VoiceFilterAdapter
    extends RecyclerView.Adapter<VoiceFilterAdapter.ViewHolderVoiceFilters> {

  private ArrayList<VoiceEffect> mListData;
  private int textSize;
  private Context mContext;

  public VoiceFilterAdapter(Context activity, ArrayList<VoiceEffect> mListData) {

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
  public ViewHolderVoiceFilters onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.ism_ar_filters_item, parent, false);

    return new ViewHolderVoiceFilters(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolderVoiceFilters vh, int position) {

    final VoiceEffect voiceEffect = mListData.get(position);

    if (voiceEffect != null) {

      vh.tvFilterName.setText(voiceEffect.getEffectName());
      if (voiceEffect.isSelected()) {
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

            .buildRound((voiceEffect.getEffectName().trim()).charAt(0) + "",
                Color.parseColor(ColorsUtil.getColorCode(vh.getAdapterPosition() % 19))));
      } catch (Exception ignore) {
      }
    }
  }

  public static class ViewHolderVoiceFilters extends RecyclerView.ViewHolder {

    public TextView tvFilterName;
    public AppCompatImageView ivFilterImage, ivFilterSelected;

    public ViewHolderVoiceFilters(View view) {
      super(view);

      tvFilterName = view.findViewById(R.id.tvFilterName);
      ivFilterImage = view.findViewById(R.id.ivFilterImage);
      ivFilterSelected = view.findViewById(R.id.ivFilterSelected);
    }
  }
}