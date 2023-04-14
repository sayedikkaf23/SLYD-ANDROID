package chat.hola.com.app.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.ezcall.android.R;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.model.User;

public class SuggestUserAdapter extends RecyclerView.Adapter<SuggestUserAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> users;
    private TypefaceManager typefaceManager;
    private ClickListener clickListener;

    @Inject
    public SuggestUserAdapter(Context context, ArrayList<User> users, TypefaceManager typefaceManager) {
        this.context = context;
        this.users = users;
        this.typefaceManager = typefaceManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(io.isometrik.android.R.layout.item_suggest_user, parent, false);
        return new ViewHolder(view, typefaceManager);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);

        holder.btnFollow.setChecked(user.getFollowStatus() != 0);

        String name = user.getFirstName();
        if (user.getLastName() != null && !user.getLastName().isEmpty())
            name = name + " " + user.getLastName();

        holder.tvName.setText(name);
        holder.tvUserName.setText(user.getUserName());


        Glide.with(context).load(user.getProfilePic()).asBitmap().centerCrop().placeholder(R.mipmap.ic_launcher)
                .into(new BitmapImageViewTarget(holder.ivPhoto) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.ivPhoto.setImageDrawable(circularBitmapDrawable);
                    }
                });

        holder.btnFollow.setOnClickListener(v -> {
            clickListener.onFollow(user.getId(), holder.btnFollow.isChecked(), position);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setData(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onFollow(String userId, boolean follow, int position);

        void onProfileClick(String userId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.btnFollow)
        ToggleButton btnFollow;

        public ViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvUserName.setTypeface(typefaceManager.getBoldFont());
            tvName.setTypeface(typefaceManager.getMediumFont());
            btnFollow.setTypeface(typefaceManager.getSemiboldFont());
        }
    }
}
