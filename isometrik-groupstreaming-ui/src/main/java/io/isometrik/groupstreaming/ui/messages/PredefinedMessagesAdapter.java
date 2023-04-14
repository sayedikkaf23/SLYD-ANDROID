package io.isometrik.groupstreaming.ui.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.copublish.CopublishActivity;
import io.isometrik.groupstreaming.ui.scrollable.ScrollableStreamsActivity;

/**
 * The type Predefined messages adapter.
 */
public class PredefinedMessagesAdapter extends RecyclerView.Adapter {

  private String[] presetMessages;
  private Context context;

  /**
   * Instantiates a new Predefined messages adapter.
   *
   * @param presetMessages the preset messages
   * @param context the context
   */
  public PredefinedMessagesAdapter(String[] presetMessages, Context context) {
    this.presetMessages = presetMessages;
    this.context = context;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.ism_preset_message_item, viewGroup, false);
    return new PresetMessagesViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    try {

      PresetMessagesViewHolder presetMessagesViewHolder = (PresetMessagesViewHolder) viewHolder;
      presetMessagesViewHolder.tvPresetMessage.setText(presetMessages[position]);
      presetMessagesViewHolder.tvPresetMessage.setOnClickListener(v -> {
        if (context instanceof CopublishActivity) {
          ((CopublishActivity) context).sendPresetMessage(presetMessages[position]);
        } else if (context instanceof ScrollableStreamsActivity) {
          ((ScrollableStreamsActivity) context).sendPresetMessage(presetMessages[position]);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return (presetMessages != null) ? presetMessages.length : 0;
  }

  /**
   * The type Preset messages view holder.
   */
  static class PresetMessagesViewHolder extends RecyclerView.ViewHolder {
    /**
     * The Tv preset message.
     */
    TextView tvPresetMessage;

    /**
     * Instantiates a new Preset messages view holder.
     *
     * @param itemView the item view
     */
    PresetMessagesViewHolder(@NonNull View itemView) {
      super(itemView);
      tvPresetMessage = itemView.findViewById(R.id.tvPresetMessage);
    }
  }
}
