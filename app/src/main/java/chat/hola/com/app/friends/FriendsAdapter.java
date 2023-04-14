package chat.hola.com.app.friends;

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
import chat.hola.com.app.home.contact.Friend;

/**
 * <h1>BlockUserAdapter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private TypefaceManager typefaceManager;
    private List<Friend> friends;
    private Context context;
    private ClickListner clickListner;

    @Inject
    public FriendsAdapter(List<Friend> friends, Activity mContext, TypefaceManager typefaceManager) {
        this.typefaceManager = typefaceManager;
        this.friends = friends;
        this.context = mContext;
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    @Override
    public int getItemViewType(int position) {
        return friends.get(position).isTitle() ? 0 : 1;
    }

    public void setData(List<Friend> data) {
        friends = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = viewType == 0 ? LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false) :
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return viewType == 0 ? new TitleViewHolder(itemView, typefaceManager) : new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemCount() > 0) {
            try {

                Friend friend = friends.get(position);
                if (getItemViewType(position) == 0) {
                    TitleViewHolder viewHolder = (TitleViewHolder) holder;
                    viewHolder.title.setText(friend.getTitle().toUpperCase());
                } else {
                    ViewHolder viewHolder = (ViewHolder) holder;
                    viewHolder.ivStarBadge.setVisibility(friend.isStar() ? View.VISIBLE : View.GONE);
                    Glide.with(context).load(friend.getProfilePic()).asBitmap().signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop().placeholder(R.mipmap.ic_launcher).into(new BitmapImageViewTarget(viewHolder.ivProfilePic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            viewHolder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    viewHolder.tvUserName.setText(friend.getUserName());
                    String fname = friend.getFirstName().substring(0, 1).toUpperCase() + friend.getFirstName().substring(1);
                    String lname;
                    try {
                        lname = friend.getLastName().substring(0, 1).toUpperCase() + friend.getLastName().substring(1);
                    } catch (Exception e) {
                        lname = "";
                    }
                    viewHolder.tvName.setText(fname + (R.string.space) + lname);//todo getString() cannot be applied
                    viewHolder.itemView.setOnClickListener(v -> {
                        if (friend.isFriendRequest())
                            clickListner.openRequest(position);
                        else
                            clickListner.openProfile(position);
                    });
                }
            } catch (Exception ignored) {

            }
        }
    }

    public interface ClickListner {
        void openRequest(int position);

        void openProfile(int position);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfilePic)
        ImageView ivProfilePic;
        @BindView(R.id.ivStarBadge)
        ImageView ivStarBadge;
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvName)
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvName.setTypeface(typefaceManager.getSemiboldFont());
            tvUserName.setTypeface(typefaceManager.getRegularFont());
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