package chat.hola.com.app.profileScreen.followers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.discover.facebook.FacebookFrag;
import chat.hola.com.app.profileScreen.followers.Model.Data;

/**
 * <h>youAdapter.class</h>
 * <p>
 * This adapter class is used by {@link FacebookFrag}.
 *
 * @author 3Embed
 * @since 02/03/18.
 */

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    private static final String TAG = FollowerAdapter.class.getSimpleName();

    //private final int DATA_SIZE = 10;
    private List<Data> followers = new ArrayList<>();
    private Context context;
    private TypefaceManager typefaceManager;
    boolean isFollowers = false;
    // Random random =  new Random();
    OnFollowUnfollowClickCallback clickCallback = null;

    public void setFollowers(boolean isFollowers) {
        this.isFollowers = isFollowers;
    }

    public interface OnFollowUnfollowClickCallback {
        void onFollow(String userId);

        void onUnfollow(String userId);
    }

    public void setOnFollowUnfollowClickCallback(OnFollowUnfollowClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    @Inject
    public FollowerAdapter(Context context, TypefaceManager typefaceManager) {
        this.context = context;
        this.typefaceManager = typefaceManager;
    }

    public void setData(List<Data> followers) {
        this.followers = followers;
        notifyDataSetChanged();
    }

    @Override
    public FollowerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.you_row_new, parent, false);
        return new FollowerAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(FollowerAdapter.ViewHolder holder, int position) {

        Data follower = followers.get(position);

        String profilePic = follower.getProfilePic();
//        if (profilePic == null || profilePic.isEmpty()) {
//            profilePic = Constants.DEFAULT_PROFILE_PIC_LINK;
//        }

        holder.ivStarBadge.setVisibility(follower.getStar() ? View.VISIBLE : View.GONE);
        if (follower.getProfilePic()!=null && follower.getProfilePic().length()>0) {
            Glide.with(context)
                    .load(follower.getProfilePic())
                    .asBitmap()
                    .signature(new StringSignature(AppController.getInstance().getSessionManager()
                            .getUserProfilePicUpdateTime()))
                    .centerCrop()
                    .placeholder(R.drawable.chat_attachment_profile_default_image_frame)
                    .into(new BitmapImageViewTarget(holder.ivRow) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.ivRow.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }else {
            Utilities.setTextRoundDrawable(context, follower.getFirstName(),follower.getLastName(), holder.ivRow);
        }
        
        holder.tvRowTitle.setText(follower.getUsername());
        String fname;
        try {
            fname = follower.getFirstName().substring(0, 1).toUpperCase() + follower.getFirstName().substring(1);
        } catch (Exception e) {
            fname = "";
        }

        String lname;
        try {
            lname = follower.getLastName().substring(0, 1).toUpperCase() + follower.getLastName().substring(1);
        } catch (Exception e) {
            lname = "";
        }
        if(follower.getStar()){
            holder.tvRowTime.setText(follower.getStarUserKnownBy());
        }else {
            holder.tvRowTime.setText(fname + " " + lname);
        }
        follower.setFollowing(follower.getType().getStatus().equals(1));
        String userId = follower.getUserId();//isFollowers ? follower.getFollower() : follower.getFollowee();
        holder.tbFollow.setVisibility(userId.equals(AppController.getInstance().getUserId()) ? View.GONE : View.VISIBLE);
        holder.tbFollow.setChecked(follower.isFollowing());
    }

    @Override
    public int getItemCount() {
        return followers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivRow)
        ImageView ivRow;
        @BindView(R.id.tvRowTitle)
        TextView tvRowTitle;
        @BindView(R.id.tvRowTime)
        TextView tvRowTime;
        @BindView(R.id.tbFollow)
        ToggleButton tbFollow;
        @BindView(R.id.tvAdd)
        TextView tvAdd;
        @BindView(R.id.ivStarBadge)
        ImageView ivStarBadge;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvRowTime.setVisibility(View.VISIBLE);
            tvAdd.setVisibility(View.GONE);
            tbFollow.setVisibility(View.VISIBLE);

            tvRowTitle.setTypeface(typefaceManager.getSemiboldFont());
            tvRowTime.setTypeface(typefaceManager.getMediumFont());
            tbFollow.setTypeface(typefaceManager.getMediumFont());
            tbFollow.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Data follower = followers.get(getAdapterPosition());
                    Intent intent = new Intent(context, ProfileActivity.class);
//                    if (isFollowers)
                    intent.putExtra("userId", follower.getUserId());
//                    else
//                        intent.putExtra("userId", follower.getFollowee());
                    context.startActivity(intent);
                    //  ((FollowersActivity) context).finish();
                }
            });
        }

        @Override
        public void onClick(View v) {
            Data follower = followers.get(getAdapterPosition());
            switch (v.getId()) {
                case R.id.tbFollow:
                    if (tbFollow.isChecked())
//                          if (isFollowers)
                        clickCallback.onFollow(follower.getUserId());
//                          else
//                              clickCallback.onFollow(follower.getFollowee());
                    else if (isFollowers)
                        clickCallback.onUnfollow(follower.getUserId());//follower.getFollower());
                    else
                        clickCallback.onUnfollow(follower.getUserId());//follower.getFollowee());
                    break;
                default:
            }
        }
    }
}
