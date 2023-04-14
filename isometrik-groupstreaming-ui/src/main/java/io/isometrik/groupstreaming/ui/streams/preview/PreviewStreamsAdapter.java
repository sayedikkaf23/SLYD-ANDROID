package io.isometrik.groupstreaming.ui.streams.preview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.copublish.CopublishActivity;
import io.isometrik.groupstreaming.ui.streams.grid.StreamsModel;
import io.isometrik.groupstreaming.ui.utils.blur.BlurTransformation;
import java.util.ArrayList;

/**
 * The type preview streams adapter.
 */
public class PreviewStreamsAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<StreamsModel> streams;

  private String userId;

  /**
   * Instantiates a new Streams adapter.
   *
   * @param mContext the m context
   * @param streams the streams
   */
  PreviewStreamsAdapter(Context mContext, ArrayList<StreamsModel> streams) {
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
        LayoutInflater.from(mContext).inflate(R.layout.ism_preview_streams_item, viewGroup, false));
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

      if (stream.getNumberOfTimesAlreadySeen()
          < PreviewStreamsConstants.MAXIMUM_FREE_PREVIEWS_ALLOWED) {
        holder.tvPreviewDisclaimer.setVisibility(View.GONE);
      } else {
        holder.tvPreviewDisclaimer.setVisibility(View.VISIBLE);
      }
      if (stream.isPublic()) {
        holder.ivPrivate.setVisibility(View.GONE);
      } else {
        holder.ivPrivate.setVisibility(View.VISIBLE);
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

      if (stream.isGivenUserIsMember()) {
        holder.ivJoinOnlyAsAudience.setVisibility(View.GONE);

        holder.ivJoinAsPublisher.setOnClickListener(v -> {

          Intent intent = new Intent(mContext, CopublishActivity.class);
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

          ((PreviewStreamsActivity) mContext).startBroadcast(intent);
        });

        holder.ivJoinAsAudience.setOnClickListener(v -> {

          Intent intent = new Intent(mContext, CopublishActivity.class);
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
          ((PreviewStreamsActivity) mContext).joinBroadcastAsAudience(intent);
        });

        holder.ivJoinAsPublisher.setVisibility(View.VISIBLE);
        holder.ivJoinAsAudience.setVisibility(View.VISIBLE);
      } else {
        holder.ivJoinAsPublisher.setVisibility(View.GONE);
        holder.ivJoinAsAudience.setVisibility(View.GONE);

        holder.ivJoinOnlyAsAudience.setOnClickListener(v -> {
          Intent intent = new Intent(mContext, CopublishActivity.class);
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

          ((PreviewStreamsActivity) mContext).joinBroadcastAsAudience(intent);
        });

        holder.ivJoinOnlyAsAudience.setVisibility(View.VISIBLE);
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
        tvInitiatorName, tvPreviewDisclaimer;

    AppCompatImageView ivStreamImage, ivInitiatorImage, ivMultiGuests, ivPrivate, ivJoinAsPublisher,
        ivJoinAsAudience, ivJoinOnlyAsAudience;

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
      tvPreviewDisclaimer = itemView.findViewById(R.id.tvPreviewDisclaimer);

      ivStreamImage = itemView.findViewById(R.id.ivStreamImage);

      tvInitiatorName = itemView.findViewById(R.id.tvInitiatorName);
      ivInitiatorImage = itemView.findViewById(R.id.ivInitiatorImage);
      ivMultiGuests = itemView.findViewById(R.id.ivMultiGuests);
      ivPrivate = itemView.findViewById(R.id.ivPrivate);

      ivJoinAsPublisher = itemView.findViewById(R.id.ivJoinAsPublisher);
      ivJoinAsAudience = itemView.findViewById(R.id.ivJoinAsAudience);
      ivJoinOnlyAsAudience = itemView.findViewById(R.id.ivJoinOnlyAsAudience);
    }
  }
}