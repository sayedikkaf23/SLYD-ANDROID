package chat.hola.com.app.DublyCamera.deepar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.ViewHolderDublyFilters;
import chat.hola.com.app.DublyCamera.deepar.ar.AREffect;
import chat.hola.com.app.Utilities.TextDrawable;
import com.ezcall.android.R;
import java.util.ArrayList;

public class DeeparFilterAdapter extends RecyclerView.Adapter<ViewHolderDublyFilters> {

  private ArrayList<AREffect> mListData;
  private int density;
  private Context mContext;

  public DeeparFilterAdapter(Context activity, ArrayList<AREffect> mListData) {

    this.mContext = activity;
    this.mListData = mListData;

    density = (int) activity.getResources().getDisplayMetrics().density;
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

    final AREffect arEffect = mListData.get(position);

    if (arEffect != null) {

      vh.filterName.setText(arEffect.getEffectName());
      if (arEffect.isSelected()) {
        vh.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));

        vh.selectedSign.setVisibility(View.VISIBLE);
      } else {
        vh.filterName.setTextColor(Color.WHITE);

        vh.selectedSign.setVisibility(View.GONE);
      }

      try {
        vh.filterImage.setImageDrawable(TextDrawable.builder()

            .beginConfig()
            .textColor(Color.WHITE)
            .useFont(Typeface.DEFAULT)
            .fontSize(24 * density) /* size in px */
            .bold()
            .toUpperCase()
            .endConfig()

            .buildRound((arEffect.getEffectName().trim()).charAt(0) + "", Color.parseColor(
                AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
      } catch (Exception e) {
      }
    }
  }
}