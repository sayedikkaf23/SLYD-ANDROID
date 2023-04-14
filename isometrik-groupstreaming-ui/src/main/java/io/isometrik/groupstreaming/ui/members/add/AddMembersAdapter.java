package io.isometrik.groupstreaming.ui.members.add;

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
 * The type Members adapter.
 */
public class AddMembersAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<AddMembersModel> addMembersModels;
  private AddMembersFragment addMembersFragment;
  private final int NORMAL_USER = 0;
  private final int VIEWER = 1;

  /**
   * Instantiates a new Add member adapter.
   *
   * @param mContext the m context
   * @param addMembersModels the users
   */
  AddMembersAdapter(Context mContext, ArrayList<AddMembersModel> addMembersModels,
      AddMembersFragment addMembersFragment) {
    this.mContext = mContext;
    this.addMembersModels = addMembersModels;
    this.addMembersFragment = addMembersFragment;
  }

  @Override
  public int getItemViewType(int position) {

    if (addMembersModels.get(position).isNormaUser()) {
      return NORMAL_USER;
    } else {
      return VIEWER;
    }
  }

  @Override
  public int getItemCount() {
    return addMembersModels.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    if (viewType == NORMAL_USER) {

      return new UsersViewHolder(LayoutInflater.from(viewGroup.getContext())
          .inflate(R.layout.ism_add_user_item, viewGroup, false));
    } else {

      return new UsersViewHolder(LayoutInflater.from(viewGroup.getContext())
          .inflate(R.layout.ism_add_viewer_item, viewGroup, false));
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    if (viewHolder.getItemViewType() == NORMAL_USER) {

      configureViewHolderUser((UsersViewHolder) viewHolder, position);
    } else {

      configureViewHolderViewer((UsersViewHolder) viewHolder, position);
    }
  }

  public void configureViewHolderUser(@NonNull UsersViewHolder holder, int position) {

    try {
      AddMembersModel addMembersModel = addMembersModels.get(position);
      if (addMembersModel != null) {
        holder.tvUserName.setText(addMembersModel.getUserName());
        holder.tvUserIdentifier.setText(addMembersModel.getUserIdentifier());

        holder.rlAddUser.setOnClickListener(
            v -> addMembersFragment.addMember(addMembersModel.getUserId()));
        try {
          Glide.with(mContext)
              .load(addMembersModel.getUserProfilePic())
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

  public void configureViewHolderViewer(@NonNull UsersViewHolder holder, int position) {

    try {
      AddMembersModel addMembersModel = addMembersModels.get(position);
      if (addMembersModel != null) {
        holder.tvUserName.setText(addMembersModel.getUserName());
        holder.tvUserIdentifier.setText(addMembersModel.getUserIdentifier());

        holder.tvJoinTime.setText(addMembersModel.getJoinTime());
        if (addMembersModel.isMember()) {
          holder.rlAddUser.setVisibility(View.INVISIBLE);
          holder.ivMember.setVisibility(View.VISIBLE);
        } else {
          holder.rlAddUser.setVisibility(View.VISIBLE);
          holder.ivMember.setVisibility(View.GONE);
          holder.rlAddUser.setOnClickListener(
              v -> addMembersFragment.addMember(addMembersModel.getUserId()));
        }

        try {
          Glide.with(mContext)
              .load(addMembersModel.getUserProfilePic())
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

    TextView tvUserName, tvUserIdentifier, tvJoinTime;
    ImageView ivUserImage, ivMember;

    RelativeLayout rlAddUser;

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
      tvJoinTime = itemView.findViewById(R.id.tvJoinTime);
      rlAddUser = itemView.findViewById(R.id.rlAdd);
      ivMember = itemView.findViewById(R.id.ivMember);
    }
  }
}
