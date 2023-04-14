package io.isometrik.groupstreaming.ui.scrollable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.streams.grid.StreamsModel;
import io.isometrik.groupstreaming.ui.utils.blur.BlurTransformation;
import java.util.ArrayList;

/**
 * The type scrollable streams adapter.
 */
public class ScrollableStreamsAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<StreamsModel> streams;

  /**
   * Instantiates a new Streams adapter.
   *
   * @param mContext the m context
   * @param streams the streams
   */
  ScrollableStreamsAdapter(Context mContext, ArrayList<StreamsModel> streams) {
    this.mContext = mContext;
    this.streams = streams;
  }

  @Override
  public int getItemCount() {
    return streams.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

    return new StreamsViewHolder(LayoutInflater.from(mContext)
        .inflate(R.layout.ism_scrollable_streams_item, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int postion) {

    StreamsViewHolder holder = (StreamsViewHolder) viewHolder;

    try {
      StreamsModel stream = streams.get(postion);
      holder.ivStreamImage.setVisibility(View.VISIBLE);
      try {

        //Stream image url
        Glide.with(mContext)
            .load(stream.getStreamImage())
            .transform(new CenterCrop(mContext))
            .bitmapTransform(new BlurTransformation(mContext))
            .into(holder.ivStreamImage);
      } catch (IllegalArgumentException | NullPointerException e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Streams view holder.
   */
  static class StreamsViewHolder extends RecyclerView.ViewHolder {

    AppCompatImageView ivStreamImage;

    /**
     * Instantiates a new Streams view holder.
     *
     * @param itemView the item view
     */
    StreamsViewHolder(@NonNull View itemView) {
      super(itemView);

      ivStreamImage = itemView.findViewById(R.id.ivStreamImage);
    }
  }
}