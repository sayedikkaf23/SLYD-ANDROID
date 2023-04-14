package chat.hola.com.app.stars;

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
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.contact.Friend;

public class StarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TypefaceManager typefaceManager;
    private Context mContext;
    private List<Friend> friendList;
    private ClickListner clickListner;
    private boolean isStar;

    @Inject
    public StarAdapter(List<Friend> friends, Activity mContext, TypefaceManager typefaceManager) {
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
                Glide.with(mContext).load(friend.getProfilePic()).asBitmap() .signature(new StringSignature(
                    AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop().into(new BitmapImageViewTarget(viewHolder.ivProfilePic) {
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

                viewHolder.tvUserName.setText(userName);
                viewHolder.tvName.setText(name);
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
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProfilePic)
        ImageView ivProfilePic;
        @BindView(R.id.ivStarBadge)
        ImageView ivStarBadge;
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvName)
        TextView tvName;

        private ClickListner clickListner;

        public FriendViewHolder(@NonNull View itemView, TypefaceManager typefaceManager, ClickListner clickListner) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvName.setTypeface(typefaceManager.getSemiboldFont());
            tvUserName.setTypeface(typefaceManager.getRegularFont());
            this.clickListner = clickListner;
        }

        @OnClick(R.id.ivProfilePic)
        public void iemClick() {
            clickListner.onUserSelected(getAdapterPosition());
        }
    }


    public class TitleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;


        public TitleViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            title.setTypeface(typefaceManager.getBoldFont());
        }

    }
}
