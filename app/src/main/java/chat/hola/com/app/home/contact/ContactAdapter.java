package chat.hola.com.app.home.contact;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ContactAdapter</h1>
 *
 * @author 3Embed
 * @since 13/2/2019.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TypefaceManager typefaceManager;
    private Context mContext;
    private List<Friend> friendList;
    private ClickListner clickListner;
    private boolean isStar;

    @Inject
    public ContactAdapter(List<Friend> friends, Activity mContext, TypefaceManager typefaceManager) {
        this.friendList = friends;
        this.mContext = mContext;
        this.typefaceManager = typefaceManager;
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public void refreshData(List<Friend> friends) {
        this.friendList = friends;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return friendList.get(position).isTitle() ? 0 : 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = viewType == 0 ? LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false) :
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return viewType == 0 ? new TitleViewHolder(itemView, typefaceManager) : new FriendViewHolder(itemView, typefaceManager, clickListner);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        try {
            Friend friend = friendList.get(position);

            if (getItemViewType(position) == 0) {
                TitleViewHolder viewHolder = (TitleViewHolder) holder;
                viewHolder.title.setText(friend.getTitle().toUpperCase());
            } else {
                FriendViewHolder viewHolder = (FriendViewHolder) holder;
                viewHolder.ivStarBadge.setVisibility(friend.isStar() ? View.VISIBLE : View.GONE);
                Glide.with(mContext).load(friend.getProfilePic()).asBitmap()
                    .signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .centerCrop().placeholder(R.drawable.profile_one).into(new BitmapImageViewTarget(viewHolder.ivProfilePic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        viewHolder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                    }
                });
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isStar)
                            clickListner.onUserSelected(position);
                        else
                            clickListner.onItemSelect(position);
                    }
                });
                viewHolder.ivProfilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isStar)
                            clickListner.onUserSelected(position);
                        else
                            clickListner.onItemSelect(position);
                    }
                });

                String name, userName;
                userName = friend.getUserName();
                String fname = friend.getFirstName().substring(0, 1).toUpperCase() + friend.getFirstName().substring(1);
                String lname;
                try {
                    lname = friend.getLastName().substring(0, 1).toUpperCase() + friend.getLastName().substring(1);
                } catch (Exception e) {
                    lname = "";
                }
                name = fname + " " + lname;

                viewHolder.tvUserName.setVisibility(View.VISIBLE);
                viewHolder.tvUserName.setText(userName);
                viewHolder.tvName.setText(name);


                /*viewHolder.tbFollow.setVisibility(friend.getId().equals(AppController.getInstance().getUserId()) ? View.GONE : View.VISIBLE);

                boolean isPrivate = friend.getPrivate() == 1;
                boolean isChecked;

                switch (friend.getFollowStatus()) {
                    case 0:
                        //public - unfollow
                        isPrivate = friend.getPrivate().equals("1");
                        isChecked = false;
                        break;
                    case 1:
                        //public - follow
                        isPrivate = false;
                        isChecked = true;
                        break;
                    case 2:
                        //private - requested
                        isPrivate = true;
                        isChecked = true;
                        break;
                    case 3:
                        //private - request
                        isPrivate = true;
                        isChecked = false;
                        break;
                    default:
                        isChecked = false;
                        break;

                }
                viewHolder.tbFollow.setTextOn(mContext.getResources().getString(isPrivate ? R.string.requested : R.string.following));
                viewHolder.tbFollow.setTextOff(mContext.getResources().getString(R.string.follow));
                viewHolder.tbFollow.setChecked(isChecked);
                viewHolder.tbFollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListner.onFollow(friend.getId(), viewHolder.tbFollow.isChecked(), position);
                    }
                });*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void setStar(boolean isStar) {
        this.isStar = isStar;
    }

    public interface ClickListner {
        void onItemSelect(int position);

        void onUserSelected(int position);

        void onFollow(String id, boolean follow, int position);
    }
}