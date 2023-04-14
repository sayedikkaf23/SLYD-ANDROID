package io.isometrik.groupstreaming.ui.streams.grid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.scrollable.ScrollableStreamsActivity;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The type Streams adapter.
 */
public class StreamsAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<StreamsModel> streams;
  private String userId;

  /**
   * Instantiates a new Streams adapter.
   *
   * @param mContext the m context
   * @param streams the streams
   */
  StreamsAdapter(Context mContext, ArrayList<StreamsModel> streams) {
    this.mContext = mContext;
    this.streams = streams;
    userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();
  }

  @Override
  public int getItemCount() {
    return streams.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new StreamsViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.ism_livestreams_item, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    StreamsViewHolder holder = (StreamsViewHolder) viewHolder;

    try {
      StreamsModel stream = streams.get(position);
      holder.tvNoOfMembers.setText(String.valueOf(stream.getMembersCount()));
      holder.tvNoOfViewers.setText(String.valueOf(stream.getViewersCount()));
      holder.tvNoOfPublishers.setText(String.valueOf(stream.getPublishersCount()));
      holder.tvStreamDescription.setText(stream.getStreamDescription());
      holder.tvStreamDuration.setText(stream.getDuration());
      holder.tvInitiatorName.setText(stream.getInitiatorName());

      if (stream.getMembersCount() > 1) {
        holder.ivMultiGuests.setVisibility(View.VISIBLE);
      } else {
        holder.ivMultiGuests.setVisibility(View.GONE);
      }

      if (stream.isPublic()) {
        holder.ivPrivate.setVisibility(View.GONE);
      } else {
        holder.ivPrivate.setVisibility(View.VISIBLE);
      }

      if (stream.isGivenUserIsMember()) {

        holder.btJoin.setOnClickListener(v -> {

          Intent intent = new Intent(mContext, ScrollableStreamsActivity.class);
          intent.putExtra("streamId", stream.getStreamId());
          intent.putExtra("streamDescription", stream.getStreamDescription());
          intent.putExtra("streamImage", stream.getStreamImage());
          intent.putExtra("startTime", stream.getStartTime());
          intent.putExtra("membersCount", stream.getMembersCount());
          intent.putExtra("viewersCount", stream.getViewersCount());
          intent.putExtra("publishersCount", stream.getPublishersCount());
          intent.putExtra("joinRequest", true);
          intent.putExtra("isAdmin", stream.getInitiatorId().equals(userId));
          intent.putStringArrayListExtra("memberIds", stream.getMemberIds());
          intent.putExtra("initiatorName", stream.getInitiatorName());
          intent.putExtra("publishRequired", true);
          intent.putExtra("initiatorId", stream.getInitiatorId());
          intent.putExtra("initiatorIdentifier", stream.getInitiatorIdentifier());
          intent.putExtra("initiatorImage", stream.getInitiatorImage());
          intent.putExtra("isPublic", stream.isPublic());
          intent.putExtra("isBroadcaster", true);
          //mContext.startActivity(intent);

          ((StreamsActivity) mContext).startBroadcast(intent);
        });

        holder.ivStreamImage.setOnClickListener(v -> {

          Intent intent = new Intent(mContext, ScrollableStreamsActivity.class);
          intent.putExtra("streamId", stream.getStreamId());
          intent.putExtra("streamDescription", stream.getStreamDescription());
          intent.putExtra("streamImage", stream.getStreamImage());
          intent.putExtra("startTime", stream.getStartTime());
          intent.putExtra("membersCount", stream.getMembersCount());
          intent.putExtra("viewersCount", stream.getViewersCount());
          intent.putExtra("publishersCount", stream.getPublishersCount());
          intent.putStringArrayListExtra("memberIds", stream.getMemberIds());
          intent.putExtra("isAdmin", stream.getInitiatorId().equals(userId));
          intent.putExtra("initiatorName", stream.getInitiatorName());
          intent.putExtra("initiatorId", stream.getInitiatorId());
          intent.putExtra("initiatorIdentifier", stream.getInitiatorIdentifier());
          intent.putExtra("initiatorImage", stream.getInitiatorImage());
          intent.putExtra("isPublic", stream.isPublic());
          intent.putExtra("isBroadcaster", false);
          intent.putExtra("streamPosition", holder.getAdapterPosition());
          intent.putExtra("streams", (Serializable) streams);

          mContext.startActivity(intent);
        });

        holder.btJoin.setVisibility(View.VISIBLE);
      } else {
        holder.btJoin.setVisibility(View.GONE);

        holder.ivStreamImage.setOnClickListener(v -> {
          Intent intent = new Intent(mContext, ScrollableStreamsActivity.class);
          intent.putExtra("streamId", stream.getStreamId());
          intent.putExtra("streamDescription", stream.getStreamDescription());
          intent.putExtra("streamImage", stream.getStreamImage());
          intent.putExtra("startTime", stream.getStartTime());
          intent.putExtra("membersCount", stream.getMembersCount());
          intent.putExtra("viewersCount", stream.getViewersCount());
          intent.putExtra("publishersCount", stream.getPublishersCount());
          intent.putExtra("isAdmin", stream.getInitiatorId().equals(userId));
          intent.putExtra("initiatorName", stream.getInitiatorName());
          intent.putExtra("initiatorId", stream.getInitiatorId());
          intent.putExtra("initiatorIdentifier", stream.getInitiatorIdentifier());
          intent.putExtra("initiatorImage", stream.getInitiatorImage());
          intent.putStringArrayListExtra("memberIds", stream.getMemberIds());
          intent.putExtra("isPublic", stream.isPublic());
          intent.putExtra("isBroadcaster", false);
          intent.putExtra("streamPosition", holder.getAdapterPosition());
          intent.putExtra("streams", (Serializable) streams);

          mContext.startActivity(intent);
        });
      }

      try {
        Glide.with(mContext).load(stream.getStreamImage()).into(holder.ivStreamImage);
      } catch (IllegalArgumentException | NullPointerException e) {
        e.printStackTrace();
      }

      try {
        Glide.with(mContext)
            .load(stream.getInitiatorImage())
            .asBitmap()
            .placeholder(R.drawable.ism_default_profile_image)
            .into(new BitmapImageViewTarget(holder.ivInitiatorImage) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.ivInitiatorImage.setImageDrawable(circularBitmapDrawable);
              }
            });
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

    TextView tvNoOfPublishers, tvStreamDescription, tvNoOfMembers, tvNoOfViewers, tvStreamDuration,
        tvInitiatorName;

    AppCompatImageView ivStreamImage, ivInitiatorImage, ivMultiGuests, ivPrivate;

    AppCompatButton btJoin;

    /**
     * Instantiates a new Streams view holder.
     *
     * @param itemView the item view
     */
    StreamsViewHolder(@NonNull View itemView) {
      super(itemView);
      tvNoOfPublishers = itemView.findViewById(R.id.tvNoOfPublishers);
      tvStreamDescription = itemView.findViewById(R.id.tvStreamDescription);
      tvNoOfMembers = itemView.findViewById(R.id.tvNoOfMembers);
      tvNoOfViewers = itemView.findViewById(R.id.tvNoOfViewers);
      tvStreamDuration = itemView.findViewById(R.id.tvStreamDuration);
      ivStreamImage = itemView.findViewById(R.id.ivStreamImage);
      btJoin = itemView.findViewById(R.id.btJoin);
      tvInitiatorName = itemView.findViewById(R.id.tvInitiatorName);
      ivInitiatorImage = itemView.findViewById(R.id.ivInitiatorImage);
      ivMultiGuests = itemView.findViewById(R.id.ivMultiGuests);
      ivPrivate = itemView.findViewById(R.id.ivPrivate);
    }
  }
}