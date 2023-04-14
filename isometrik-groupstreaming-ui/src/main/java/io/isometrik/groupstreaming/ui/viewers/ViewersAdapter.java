package io.isometrik.groupstreaming.ui.viewers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.isometrik.groupstreaming.ui.R;
import java.util.ArrayList;

/**
 * The type Viewers adapter.
 */
public class ViewersAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<ViewersModel> viewers;
  private ViewersFragment viewersBottomSheetFragment;

  /**
   * Instantiates a new Viewers adapter.
   *
   * @param mContext the m context
   * @param viewers the viewers
   * @param viewersBottomSheetFragment the viewersBottomSheetFragment fragment
   */
  ViewersAdapter(Context mContext, ArrayList<ViewersModel> viewers,
      ViewersFragment viewersBottomSheetFragment) {
    this.mContext = mContext;
    this.viewers = viewers;
    this.viewersBottomSheetFragment = viewersBottomSheetFragment;
  }

  @Override
  public int getItemCount() {
    return viewers.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new ViewersAdapter.ViewersViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.ism_viewers_item, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    ViewersAdapter.ViewersViewHolder holder = (ViewersAdapter.ViewersViewHolder) viewHolder;

    try {
      ViewersModel viewer = viewers.get(position);
      if (viewer != null) {
        holder.tvViewerName.setText(viewer.getViewerName());
        holder.tvViewerIdentifier.setText(viewer.getViewerIdentifier());
        holder.tvJoinTime.setText(viewer.getJoinTime());

        if (viewer.isMember()) {

          holder.ivMember.setVisibility(View.VISIBLE);
        } else {

          holder.ivMember.setVisibility(View.GONE);
        }

        if (viewer.isCanRemoveViewer()) {

          holder.rlRemoveViewer.setVisibility(View.VISIBLE);

          holder.rlRemoveViewer.setOnClickListener(
              v -> (viewersBottomSheetFragment).removeViewer(viewer.getViewerId()));
        } else {

          holder.rlRemoveViewer.setVisibility(View.GONE);
        }

        try {
          Glide.with(mContext)
              .load(viewer.getViewerProfilePic())
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(holder.ivViewerImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  holder.ivViewerImage.setImageDrawable(circularBitmapDrawable);
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
   * The type Viewers view holder.
   */
  static class ViewersViewHolder extends RecyclerView.ViewHolder {
    /**
     * The Tv join time.
     */
    TextView tvJoinTime, /**
     * The Tv viewer name.
     */
    tvViewerName, /**
     * The Tv viewer identifier.
     */
    tvViewerIdentifier;
    /**
     * The Iv viewer image.
     */
    ImageView ivViewerImage, /**
     * The Iv member.
     */
    ivMember;
    /**
     * The Rl remove viewer.
     */
    RelativeLayout rlRemoveViewer;

    /**
     * Instantiates a new Viewers view holder.
     *
     * @param itemView the item view
     */
    ViewersViewHolder(@NonNull View itemView) {
      super(itemView);
      tvJoinTime = itemView.findViewById(R.id.tvJoinTime);
      tvViewerName = itemView.findViewById(R.id.tvViewerName);
      tvViewerIdentifier = itemView.findViewById(R.id.tvViewerIdentifier);

      ivViewerImage = itemView.findViewById(R.id.ivViewerImage);
      ivMember = itemView.findViewById(R.id.ivMember);

      rlRemoveViewer = itemView.findViewById(R.id.rlRemove);
    }
  }
}
