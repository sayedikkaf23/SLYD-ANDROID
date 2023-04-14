package chat.hola.com.app.search_user;

import android.content.Context;
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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.contact.Friend;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private Context context;
    private List<Friend> searchData;
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;
    private boolean isStar;

    public SearchUserAdapter(Context context, List<Friend> data, TypefaceManager typefaceManager) {
        this.context = context;
        this.searchData = data;
        this.typefaceManager = typefaceManager;
    }

    public void setData(List<Friend> data) {
        this.searchData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.you_row_new, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend data = searchData.get(position);
        //holder.tvAdd.setVisibility(data.getId().equals(AppController.getInstance().getUserId()) || data.isStar() ? View.GONE : View.VISIBLE);
        holder.tvAdd.setVisibility(View.GONE);
        //holder.tbFollow.setVisibility(!data.getId().equals(AppController.getInstance().getUserId()) && data.isStar() ? View.VISIBLE : View.GONE);
        holder.tbFollow.setVisibility(!data.getId().equals(AppController.getInstance().getUserId()) ? View.VISIBLE : View.GONE);
        boolean isPrivate = data.getPrivate() == 1;
        Glide.with(context).load(data.getProfilePic()).asBitmap() .signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop().placeholder(R.drawable.profile_one).into(new BitmapImageViewTarget(holder.ivRow));

        String name, userName;
//        if (data.isStar()) {
//            StarData starData = data.getStarData();
//            userName = starData.getCategoryName() != null ? starData.getCategoryName() : data.getUserName();
//            name = starData.getStarUserKnownBy() != null && !starData.getStarUserKnownBy().isEmpty() ? (starData.getStarUserKnownBy().substring(0, 1).toUpperCase() + starData.getStarUserKnownBy().substring(1)) : (data.getFirstName().substring(0, 1).toUpperCase() + data.getFirstName().substring(1)) + " " + (data.getLastName().substring(0, 1).toUpperCase() + data.getLastName().substring(1));
//        } else {
        userName = data.getUserName();
        String fname;
        try {
            fname = data.getFirstName().substring(0, 1).toUpperCase() + data.getFirstName().substring(1);
        } catch (Exception e) {
            fname = "";
        }

        String lname;
        try {
            lname = data.getLastName().substring(0, 1).toUpperCase() + data.getLastName().substring(1);
        } catch (Exception e) {
            lname = "";
        }
        name = fname + " " + lname;
//        }

        holder.tvRowTitle.setText(userName);
        holder.tvRowTime.setText(name);
        holder.ivStarBadge.setVisibility(data.isStar() ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(v -> clickListner.onUserSelected(position));
        try {

            int friendStatus = data.getFriendStatusCode();
            if (true) {
                boolean isChecked;

                switch (data.getFollowStatus()) {
                    case 0:
                        //public - unfollow
                        isPrivate = data.getPrivate().equals("1");
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
                holder.tbFollow.setTextOn(context.getResources().getString(isPrivate ? R.string.requested : R.string.following));
                holder.tbFollow.setTextOff(context.getResources().getString(R.string.follow));
                holder.tbFollow.setChecked(isChecked);
            } else {
                switch (friendStatus) {
                    case 1:
                        // default
                        holder.tvAdd.setText(context.getResources().getString(R.string.add));
                        break;
                    case 2:
                        // friend
                        holder.tvAdd.setText(context.getResources().getString(R.string.view));
                        break;
                    case 3:
                        // requested
                        holder.tvAdd.setText(context.getResources().getString(R.string.requested));
                        holder.tvAdd.setBackgroundColor(context.getResources().getColor(R.color.color_white));
                        break;
                }
            }
            boolean finalIsPrivate = isPrivate;
            holder.tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (friendStatus) {
                        case 1:
                            // default
                            clickListner.add(position);
                            holder.tvAdd.setText(context.getResources().getString(finalIsPrivate ? R.string.requested : R.string.view));
                            if (finalIsPrivate)
                                holder.tvAdd.setBackgroundColor(context.getResources().getColor(R.color.color_white));
                            break;
                        case 2:
                            // friend
                            clickListner.view(position);
                            break;
                    }
                }
            });

            holder.tbFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListner != null)
                        clickListner.follow(position, !holder.tbFollow.isChecked(), data.getId());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }

    public void setStar(boolean isStar) {
        this.isStar = isStar;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivRow)
        ImageView ivRow;
        @BindView(R.id.tvRowTitle)
        TextView tvRowTitle;
        @BindView(R.id.tvRowTime)
        TextView tvRowTime;
        @BindView(R.id.tvAdd)
        TextView tvAdd;
        @BindView(R.id.ivStarBadge)
        ImageView ivStarBadge;

        @BindView(R.id.tbFollow)
        ToggleButton tbFollow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tvRowTitle.setTypeface(typefaceManager.getSemiboldFont());
            tvRowTime.setTypeface(typefaceManager.getRegularFont());
            tvAdd.setTypeface(typefaceManager.getSemiboldFont());
        }
    }

    public void setListener(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void add(int position);

        void view(int position);

        void onUserSelected(int position);

        void follow(int position, boolean b, String id);
    }
}
