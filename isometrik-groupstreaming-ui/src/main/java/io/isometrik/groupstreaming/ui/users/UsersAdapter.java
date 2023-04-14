package io.isometrik.groupstreaming.ui.users;

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
 * The type Users adapter.
 */
public class UsersAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<UsersModel> users;

  /**
   * Instantiates a new Users adapter.
   *
   * @param mContext the m context
   * @param users the users
   */
  UsersAdapter(Context mContext, ArrayList<UsersModel> users) {
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
    return new UsersAdapter.UsersViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.ism_users_item, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    UsersAdapter.UsersViewHolder holder = (UsersAdapter.UsersViewHolder) viewHolder;

    try {
      UsersModel user = users.get(position);
      if (user != null) {
        holder.tvUserName.setText(user.getUserName());
        holder.tvUserIdentifier.setText(user.getUserIdentifier());

        holder.rlSelectUser.setOnClickListener(v -> ((UsersActivity) mContext).selectUser(user));

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
    /**
     * The Tv user name.
     */
    TextView tvUserName, /**
     * The Tv user identifier.
     */
    tvUserIdentifier;
    /**
     * The Iv user image.
     */
    ImageView ivUserImage;
    /**
     * The Rl select user.
     */
    RelativeLayout rlSelectUser;

    /**
     * Instantiates a new Users view holder.
     *
     * @param itemView the item view
     */
    UsersViewHolder(@NonNull View itemView) {
      super(itemView);

      tvUserName = itemView.findViewById(R.id.tvUserName);
      tvUserIdentifier = itemView.findViewById(R.id.tvUserIdentifier);

      ivUserImage = itemView.findViewById(R.id.ivUserImage);
      rlSelectUser = itemView.findViewById(R.id.rlSelect);
    }
  }
}