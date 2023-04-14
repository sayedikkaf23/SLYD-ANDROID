package chat.hola.com.app.home.contact;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import chat.hola.com.app.AppController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ContactAdapter</h1>
 *
 * @author 3Embed
 * @since 13/2/2019.
 */

public class FriendRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TypefaceManager typefaceManager;
    private Context mContext;
    private List<Friend> friendList;
    private ClickListner clickListner;

    @Inject
    public FriendRequestAdapter(List<Friend> friends, Activity mContext, TypefaceManager typefaceManager) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_rquest, parent, false);
        return new FriendViewHolder(itemView, typefaceManager);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Friend friend = friendList.get(position);

        FriendViewHolder viewHolder = (FriendViewHolder) holder;
        Glide.with(mContext).load(friend.getProfilePic()).asBitmap().signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop().into(new BitmapImageViewTarget(viewHolder.ivProfilePic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                viewHolder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
            }
        });
        viewHolder.tvUserName.setText((friend.getFirstName().substring(0, 1).toUpperCase() + friend.getFirstName().substring(1)) + " " + (friend.getLastName().substring(0, 1).toUpperCase() + friend.getLastName().substring(1)));

        viewHolder.ivProfilePic.setOnClickListener(v -> clickListner.onFriendRequestSelected(position));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public interface ClickListner {
        void onFriendRequestSelected(int position);
    }


    public class FriendViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProfilePic)
        ImageView ivProfilePic;
        @BindView(R.id.tvUserName)
        TextView tvUserName;


        public FriendViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvUserName.setTypeface(typefaceManager.getSemiboldFont());
        }

//        @OnClick(R.id.ivProfilePic)
//        public void iemClick() {
//            clickListner.onUserSelected(getAdapterPosition());
//        }
    }
}