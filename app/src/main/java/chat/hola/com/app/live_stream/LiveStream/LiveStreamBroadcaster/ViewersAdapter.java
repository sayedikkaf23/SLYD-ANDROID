package chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.stories.model.Viewer;
import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1>ViewersAdapter</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 07 August 2019
 */
public class ViewersAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Viewer> viewers;
    private Context context;
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;
    private SessionManager sessionManager;
    private String userId;

    @Inject
    public ViewersAdapter(List<Viewer> viewers, Context mContext, TypefaceManager typefaceManager) {
        this.viewers = viewers;
        this.context = mContext;
        this.typefaceManager = typefaceManager;
        sessionManager = new SessionManager(context);
        userId = sessionManager.getUserId();
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewer, parent, false),
                typefaceManager);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Viewer viewer = viewers.get(position);


        holder.tvUserName.setText(viewer.getUserName());
        String fname = viewer.getFirstName();
        String lname = "";
        if (viewer.getLastName() != null) lname = viewer.getLastName();
        holder.tvName.setText(fname + context.getString(R.string.space) + lname);

        if (viewer.getProfilePic() != null && viewer.getProfilePic().isEmpty()) {
            Glide.with(context)
                    .load(viewer.getProfilePic())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.profile_one)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(holder.profilePic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.profilePic.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            Utilities.setTextRoundDrawable(context, fname, lname, holder.profilePic);
        }
/**
 * Bug Title :Viewer list -> getting follow button on loged in user name
 * Bug Id :DUBAND049
 * Fix Description :
 * Developer Name : Jadeja
 * Fix Date : 6/4/2021
 */
        holder.tbFollow.setVisibility(viewer.getUserId().equals(userId) ? View.GONE : View.VISIBLE);
        if (!viewer.getUserId().equals(userId)) {

            if (viewer.getFollowing()) {
                holder.tbFollow.setTextOn(viewer.getFollowStatus() == 2 ? "Requested" : "Following");
            } else if (viewer.isPrivate()) {
                holder.tbFollow.setTextOn("Requested");
            }
            if (viewer.getUserId().equals(AppController.getInstance().getUserId())) {
                holder.tbFollow.setVisibility(View.GONE);
            } else {
                holder.tbFollow.setVisibility(View.VISIBLE);
                holder.tbFollow.setOnClickListener(null);
                holder.tbFollow.setChecked(viewer.getFollowing());
                holder.tbFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        clickListner.follow(isChecked, viewer.getUserId());
                    }

                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return viewers.size();
    }

    public void updateData(List<Viewer> viewers) {
        this.viewers = viewers;
        notifyDataSetChanged();
    }

    public interface ClickListner {
        void follow(boolean following, String userId);
    }
}
