package io.isometrik.groupstreaming.ui.requests;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.isometrik.groupstreaming.ui.R;
import java.util.ArrayList;

/**
 * The type Requests adapter.
 */
public class RequestsAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<RequestsModel> requests;
  private RequestsFragment requestsBottomSheetFragment;

  /**
   * Instantiates a new Requests adapter.
   *
   * @param mContext the m context
   * @param requests the requests
   * @param requestsBottomSheetFragment the requestsBottomSheetFragment fragment
   */
  RequestsAdapter(Context mContext, ArrayList<RequestsModel> requests,
      RequestsFragment requestsBottomSheetFragment) {
    this.mContext = mContext;
    this.requests = requests;
    this.requestsBottomSheetFragment = requestsBottomSheetFragment;
  }

  @Override
  public int getItemCount() {
    return requests.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new RequestsViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.ism_requests_item, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    RequestsViewHolder holder = (RequestsViewHolder) viewHolder;

    try {
      RequestsModel request = requests.get(position);
      if (request != null) {
        holder.tvUserName.setText(request.getUserName());
        holder.tvUserIdentifier.setText(request.getUserIdentifier());

        if (request.isPending()) {
          holder.tvRequestTime.setText(request.getRequestTime());
          holder.tvRequestTime.setTextColor(
              ContextCompat.getColor(mContext, R.color.ism_jointime_text_white));

          if (request.isInitiator()) {

            holder.rlAccept.setVisibility(View.VISIBLE);
            holder.rlDecline.setVisibility(View.VISIBLE);

            holder.rlAccept.setOnClickListener(
                v -> (requestsBottomSheetFragment).acceptCopublishRequest(request.getUserId()));

            holder.rlDecline.setOnClickListener(
                v -> (requestsBottomSheetFragment).declineCopublishRequest(request.getUserId()));
          } else {

            holder.rlAccept.setVisibility(View.GONE);
            holder.rlDecline.setVisibility(View.GONE);
          }
        } else {

          if (request.isAccepted()) {
            holder.tvRequestTime.setText(mContext.getString(R.string.ism_accepted));
            holder.tvRequestTime.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_accept_green));
          } else {
            holder.tvRequestTime.setText(mContext.getString(R.string.ism_declined));
            holder.tvRequestTime.setTextColor(
                ContextCompat.getColor(mContext, R.color.ism_decline_red));
          }
          holder.rlAccept.setVisibility(View.GONE);
          holder.rlDecline.setVisibility(View.GONE);
        }

        try {
          Glide.with(mContext)
              .load(request.getUserProfilePic())
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(holder.ivUserImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  holder.ivUserImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Requests view holder.
   */
  static class RequestsViewHolder extends RecyclerView.ViewHolder {

    TextView tvRequestTime, tvUserName, tvUserIdentifier;
    ImageView ivUserImage;
    RelativeLayout rlAccept, rlDecline;

    /**
     * Instantiates a new Requests view holder.
     *
     * @param itemView the item view
     */
    RequestsViewHolder(@NonNull View itemView) {
      super(itemView);
      tvRequestTime = itemView.findViewById(R.id.tvRequestTime);
      tvUserName = itemView.findViewById(R.id.tvUserName);
      tvUserIdentifier = itemView.findViewById(R.id.tvUserIdentifier);

      ivUserImage = itemView.findViewById(R.id.ivUserImage);

      rlAccept = itemView.findViewById(R.id.rlAccept);
      rlDecline = itemView.findViewById(R.id.rlDecline);
    }
  }
}
