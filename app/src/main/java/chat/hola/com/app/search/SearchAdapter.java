package chat.hola.com.app.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.List;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.contact.Friend;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private AppController appController;
    private Typeface fontMedium;
    private List<Friend> searchData;
    private Context context;
    private SearchAdapter.ClickListner clickListner;
    private boolean isPrivate = false;

    public void setData(Context context, List<Friend> data) {
        this.context = context;
        this.searchData = data;
        notifyDataSetChanged();
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row, parent, false);
        appController = AppController.getInstance();
        fontMedium = appController.getMediumFont();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, final int position) {
        if (getItemCount() != -1) {
            Friend data = searchData.get(position);
            holder.ivStarBadge.setVisibility(data.isStar() ? View.VISIBLE : View.GONE);
            if (data.getProfilePic() != null && data.getProfilePic().length() > 0) {
                Glide.with(context).load(data.getProfilePic()).asBitmap()
                        .signature(new StringSignature(
                        AppController.getInstance().getSessionManager()
                                .getUserProfilePicUpdateTime())).centerCrop()
                        .placeholder(R.drawable.profile_one)
                        .into(new BitmapImageViewTarget(holder.profileIv) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.profileIv.setImageDrawable(circularBitmapDrawable);
                    }
                });
            } else {
                Utilities.setTextRoundDrawable(context, searchData.get(position).getFirstName(),
                        searchData.get(position).getLastName(), holder.profileIv);
            }

            String name = "";
            if (data.getFirstName() != null)
                name += data.getFirstName();
            if (data.getLastName() != null)
                name += " " + data.getLastName();
            holder.tvName.setText(data.getUserName());
            holder.profileNameTv.setText(name);
            holder.relativeLayout.setOnClickListener(view -> clickListner.onItemClick(position));
            isPrivate = data.getPrivate().equals("1");

            holder.follow.setVisibility(data.getId().equals(AppController.getInstance().getUserId()) ? View.GONE : View.VISIBLE);

            boolean isChecked;

            /*
             * Bug Title:Follow Request to private user flickering
             * Bug Desc: Explore Module: the request button is flickering, following is displayed and then requested is displayed in the search page
             * Developer name:Ankit K Tiwary
             * Fixed Date:9-April-2021*/

            switch (data.getFollowStatus()) {
                case 0:
                    //public - unfollow
                    isPrivate = data.getPrivate() == 1;
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
            holder.follow.setTextOn(
                    context.getResources().getString(isPrivate ? R.string.requested : R.string.following));
            holder.follow.setTextOff(context.getResources().getString(R.string.follow));
            holder.follow.setChecked(isChecked);

            holder.follow.setOnClickListener(view -> clickListner.onFollow(data.getId(), holder.follow.isChecked(), position));
        }
    }

    @Override
    public int getItemCount() {
        return searchData != null ? searchData.size() : -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView profileIv;
        private TextView profileNameTv, tvName;
        private RelativeLayout relativeLayout;
        private ToggleButton follow;
        private ImageView ivStarBadge;

        public ViewHolder(View itemView) {
            super(itemView);
            profileNameTv = itemView.findViewById(R.id.profileNameTv);
            tvName = itemView.findViewById(R.id.tvName);
            profileIv = itemView.findViewById(R.id.profileIv);
            ivStarBadge = itemView.findViewById(R.id.ivStarBadge);
            relativeLayout = itemView.findViewById(R.id.rlItem);
            follow = itemView.findViewById(R.id.tbFollow);
            profileNameTv.setTypeface(appController.getSemiboldFont());
            tvName.setTypeface(fontMedium);
        }
    }

    public void setListener(SearchAdapter.ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void onItemClick(int position);

        void onFollow(String userId, boolean follow, int position);
    }
}
