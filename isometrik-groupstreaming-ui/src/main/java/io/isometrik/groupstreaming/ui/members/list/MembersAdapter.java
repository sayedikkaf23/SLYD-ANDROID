package io.isometrik.groupstreaming.ui.members.list;

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
 * The type Members adapter.
 */
public class MembersAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<MembersModel> members;
  private MembersFragment membersBottomSheetFragment;

  /**
   * Instantiates a new Members adapter.
   *
   * @param mContext the m context
   * @param members the members
   * @param membersBottomSheetFragment the membersBottomSheetFragment fragment
   */
  MembersAdapter(Context mContext, ArrayList<MembersModel> members,
      MembersFragment membersBottomSheetFragment) {
    this.mContext = mContext;
    this.members = members;
    this.membersBottomSheetFragment = membersBottomSheetFragment;
  }

  @Override
  public int getItemCount() {
    return members.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new MembersAdapter.MembersViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.ism_members_item, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    MembersAdapter.MembersViewHolder holder = (MembersAdapter.MembersViewHolder) viewHolder;

    try {
      MembersModel member = members.get(position);
      if (member != null) {
        holder.tvMemberName.setText(member.getMemberName());
        holder.tvMemberIdentifier.setText(member.getMemberIdentifier());
        holder.tvJoinTime.setText(member.getJoinTime());

        if (member.isAdmin()) {

          holder.tvAdmin.setText(mContext.getString(R.string.ism_host));
          holder.rlAdmin.setVisibility(View.VISIBLE);
          holder.rlAdmin.setBackground(
              ContextCompat.getDrawable(mContext, R.drawable.ism_copublish_button));
        } else {

          if (member.isCanRemoveMember()) {
            holder.tvAdmin.setText(mContext.getString(R.string.ism_kickout));
            holder.rlAdmin.setVisibility(View.VISIBLE);
            holder.rlAdmin.setBackground(
                ContextCompat.getDrawable(mContext, R.drawable.ism_request_button));

            holder.rlAdmin.setOnClickListener(
                v -> (membersBottomSheetFragment).requestRemoveMember(member.getMemberId()));
          } else {

            holder.rlAdmin.setVisibility(View.GONE);
          }
        }

        if (member.isPublishing()) {

          holder.ivLiveStatus.setColorFilter(
              ContextCompat.getColor(mContext, R.color.ism_green_dark));
        } else {

          holder.ivLiveStatus.setColorFilter(
              ContextCompat.getColor(mContext, R.color.ism_red_dark));
        }

        try {
          Glide.with(mContext)
              .load(member.getMemberProfilePic())
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(holder.ivMemberImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  holder.ivMemberImage.setImageDrawable(circularBitmapDrawable);
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
   * The type Members view holder.
   */
  static class MembersViewHolder extends RecyclerView.ViewHolder {
    /**
     * The Tv join time.
     */
    TextView tvJoinTime, /**
     * The Tv member name.
     */
    tvMemberName, /**
     * The Tv member identifier.
     */
    tvMemberIdentifier, /**
     * The Tv admin.
     */
    tvAdmin;
    /**
     * The Iv member image.
     */
    ImageView ivMemberImage, /**
     * The Iv live status.
     */
    ivLiveStatus;
    /**
     * The Rl admin.
     */
    RelativeLayout rlAdmin;

    /**
     * Instantiates a new Members view holder.
     *
     * @param itemView the item view
     */
    MembersViewHolder(@NonNull View itemView) {
      super(itemView);
      tvJoinTime = itemView.findViewById(R.id.tvJoinTime);
      tvMemberName = itemView.findViewById(R.id.tvMemberName);
      tvMemberIdentifier = itemView.findViewById(R.id.tvMemberIdentifier);
      tvAdmin = itemView.findViewById(R.id.tvAdmin);

      ivMemberImage = itemView.findViewById(R.id.ivMemberImage);
      ivLiveStatus = itemView.findViewById(R.id.ivLiveStatus);

      rlAdmin = itemView.findViewById(R.id.rlAdmin);
    }
  }
}
