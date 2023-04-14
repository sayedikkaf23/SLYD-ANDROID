package chat.hola.com.app.Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.post.model.ChannelData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.List;

/**
 * Created by ankit on 23/2/18.
 */

public class ChannelPicker extends RecyclerView.Adapter<ChannelPicker.ViewHolder> {

  private List<ChannelData> data;
  private int selectedPos = -1;
  private TypefaceManager typefaceManager;
  private Context context;
  private String channelId;
  private ChannelSelectCallback callback;

  public void refreshData(Context context, List<ChannelData> data, String channelId) {
    this.data = data;
    this.context = context;
    if (channelId != null) this.channelId = channelId;
    notifyDataSetChanged();
  }

  public ChannelPicker(TypefaceManager typefaceManager) {
    this.typefaceManager = typefaceManager;
  }

  public void setChannelSelector(ChannelSelectCallback channelSelector, String channelId) {
    this.callback = channelSelector;
    this.channelId = channelId;
  }

  public interface ChannelSelectCallback {
    void onclick(String channelId, String categoryId, String categoryName);

    void onUnSelectChannel();
  }

  @Override
  public ChannelPicker.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
    return new ChannelPicker.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ChannelPicker.ViewHolder holder, int position) {
    if (getItemCount() != -1) {
      ChannelData channel = data.get(position);
      Glide.with(context)
          .load(channel.getChannelImageUrl().replace("upload/", Constants.PROFILE_PIC_SHAPE))
          .asBitmap()
          .centerCrop()
              .placeholder(R.drawable.ic_default)
          //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
          .signature(new StringSignature(
              AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
          .into(new BitmapImageViewTarget(holder.ivRow));
      holder.tvRow.setText(channel.getChannelName());

      if (channel.getId().equals(channelId)) selectedPos = position;
      holder.cbRow.setChecked(selectedPos == position);
    }
  }

  @Override
  public int getItemCount() {
    return data != null ? data.size() : -1;
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.ivRow)
    ImageView ivRow;
    @BindView(R.id.tvRow)
    TextView tvRow;
    @BindView(R.id.cbRow)
    CheckBox cbRow;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      cbRow.setOnClickListener(this);
      tvRow.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    public void onClick(View v) {

      if (v.getId() == R.id.cbRow) {
        if (((CheckBox) v).isChecked()) {
          selectedPos = getAdapterPosition();
          callback.onclick(data.get(selectedPos).getId(), data.get(selectedPos).getCategoryId(),
              data.get(selectedPos).getCategoryName());
          for (int i = 0; i <= getItemCount(); i++)
            notifyItemChanged(i);
        } else {
          callback.onUnSelectChannel();
        }
      }
    }
  }
}
