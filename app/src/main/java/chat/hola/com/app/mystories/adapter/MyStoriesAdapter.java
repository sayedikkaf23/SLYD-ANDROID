package chat.hola.com.app.mystories.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.home.stories.model.StoryPost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.List;

/**
 * Created by embed on 13/12/18.
 */

public class MyStoriesAdapter extends RecyclerView.Adapter<MyStoriesAdapter.MyViewHolder> {

  Context mContext;
  List<StoryPost> data;
  DeleteStoryListerner deleteStoryListerner;

  public MyStoriesAdapter(Context mContext, List<StoryPost> data) {
    this.mContext = mContext;
    this.data = data;
    this.deleteStoryListerner = (DeleteStoryListerner) mContext;
  }

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.single_row_my_stories, null, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    Glide.with(mContext)
        .load(data.get(position)
            .getUrlPath()
            .replace("upload/", "upload/q_10/")
            .replace("mp4", "jpg"))
        .asBitmap()
        .centerCrop()
        .signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
        .into(new BitmapImageViewTarget(holder.profilePic) {
          @Override
          protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            holder.profilePic.setImageDrawable(circularBitmapDrawable);
          }
        });
    holder.tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(data.get(position).getTimestamp())));
    holder.tV_viewCount.setText(data.get(position).getUniqueViewCount());
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView profilePic;
    TextView tvUserName, tvTime, tV_viewCount;
    ImageView iV_option;

    public MyViewHolder(View itemView) {
      super(itemView);

      profilePic = (ImageView) itemView.findViewById(R.id.ivProfilePic);
      tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
      tvTime = (TextView) itemView.findViewById(R.id.tvTime);
      iV_option = (ImageView) itemView.findViewById(R.id.iV_option);
      tV_viewCount = (TextView) itemView.findViewById(R.id.tV_viewCount);

      iV_option.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          PopupMenu popup = new PopupMenu(mContext, iV_option);
          //inflating menu from xml resource
          popup.inflate(R.menu.mystories_option);
          //adding click listener
          popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
              switch (item.getItemId()) {
                case R.id.delete:

                  deleteStoryListerner.onDelete(getAdapterPosition());

                  break;
              }
              return false;
            }
          });
          //displaying the popup
          popup.show();
          //deleteStoryListerner.onDelete(getAdapterPosition());
        }
      });
    }
  }

  public interface DeleteStoryListerner {
    void onDelete(int position);
  }
}
