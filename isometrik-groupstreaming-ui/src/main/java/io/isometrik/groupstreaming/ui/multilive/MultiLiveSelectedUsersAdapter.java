package io.isometrik.groupstreaming.ui.multilive;

import android.content.Context;
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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.isometrik.groupstreaming.ui.R;
import java.util.ArrayList;

/**
 * The type selected users adapter for the multi live.
 */
public class MultiLiveSelectedUsersAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<MultiLiveSelectMembersModel> users;

  /**
   * Instantiates a new Selected users adapter for the multi live.
   *
   * @param mContext the m context
   * @param users the users
   */
  MultiLiveSelectedUsersAdapter(Context mContext, ArrayList<MultiLiveSelectMembersModel> users) {
    this.mContext = mContext;
    this.users = users;
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new UsersViewHolder(LayoutInflater.from(mContext)
        .inflate(R.layout.ism_selected_members_item, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    UsersViewHolder holder = (UsersViewHolder) viewHolder;

    try {
      MultiLiveSelectMembersModel user = users.get(position);
      if (user != null) {
        holder.tvUserName.setText(user.getUserName());

        holder.ivRemoveUser.setOnClickListener(
            v -> ((MultiLiveSelectMembersActivity) mContext).removeUser(user.getUserId()));

        try {
          Glide.with(mContext)
              .load(user.getUserProfilePic())
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
   * The type Users view holder.
   */
  static class UsersViewHolder extends RecyclerView.ViewHolder {

    TextView tvUserName;

    AppCompatImageView ivUserImage, ivRemoveUser;

    /**
     * Instantiates a new Users view holder.
     *
     * @param itemView the item view
     */
    UsersViewHolder(@NonNull View itemView) {
      super(itemView);

      tvUserName = itemView.findViewById(R.id.tvUserName);
      ivUserImage = itemView.findViewById(R.id.ivUserImage);
      ivRemoveUser = itemView.findViewById(R.id.ivRemoveUser);
    }
  }
}