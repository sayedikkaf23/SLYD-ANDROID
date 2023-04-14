package chat.hola.com.app.profileScreen.discover.follow;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import chat.hola.com.app.AppController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.discover.facebook.FacebookFrag;
import chat.hola.com.app.profileScreen.discover.facebook.apiModel.Data;

/**
 * <h>youAdapter.class</h>
 * <p>
 * This adapter class is used by {@link FacebookFrag}.
 *
 * @author 3Embed
 * @since 02/03/18.
 */

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    private Context context;
    private TypefaceManager typefaceManager;
    private ArrayList<Data> fbContacts = new ArrayList<>();

    FollowAdapter.OnFollowUnfollowClickCallback clickCallback = null;

    public interface OnFollowUnfollowClickCallback{
        void onFollow(String followerId);

        void onUnfollow(String followerId);
    }

    public void setOnFollowUnfollowClickCallback(FollowAdapter.OnFollowUnfollowClickCallback clickCallback){
        this.clickCallback = clickCallback;
    }

    @Inject
    public FollowAdapter(Context context, TypefaceManager typefaceManager){
        this.context = context;
        this.typefaceManager = typefaceManager;
    }

    public void setData(ArrayList<Data> fbContacts){
        this.fbContacts = fbContacts;
        notifyDataSetChanged();
    }

    @Override
    public FollowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.you_row_new,parent,false);
        return new FollowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FollowAdapter.ViewHolder holder, int position) {

        Data fbContact = fbContacts.get(position);
        if(fbContact.getProfilePic() != null){
            Glide.with(context)
                    .load(fbContact.getProfilePic())
                    .asBitmap()
                .signature(new StringSignature(
                    AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivRow);
        }
        holder.tvRowTitle.setText((fbContact.getUserName() == null)?"":fbContact.getUserName());
        holder.tvRowTime.setText("");
        if(fbContact.getFollow().getStatus() == 1)
            fbContact.setFollowing(true);
        else
            fbContact.setFollowing(false);

        holder.tbFollow.setChecked(fbContact.isFollowing());
    }

    @Override
    public int getItemCount() {
        return fbContacts.size();
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tvRowTitle.setTypeface(typefaceManager.getMediumFont());
            tvRowTitle.setTypeface(typefaceManager.getMediumFont());
            tbFollow.setTypeface(typefaceManager.getMediumFont());
            tbFollow.setOnClickListener(this);
            itemView.setOnClickListener(this);
            tvRowTime.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tbFollow:
                    Data fbContact = fbContacts.get(getAdapterPosition());
                    if(tbFollow.isChecked()){
                        if(clickCallback != null)
                            clickCallback.onFollow(fbContact.getId());
                    }
                    else{
                        if(clickCallback != null)
                            clickCallback.onUnfollow(fbContact.getId());
                    }
                    break;
                default:
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("userId", fbContacts.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                    ((DiscoverActivity) context).finish();
            }

        }
    }
}
