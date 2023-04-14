package chat.hola.com.app.home.stories.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.connect.ConnectFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.List;
import javax.inject.Inject;

/**
 * <h1>StoriesAdapter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/26/2018
 */

public class ChatStoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private TypefaceManager typefaceManager;
  private List<StoryData> arrayList;
  private ClickListner clickListner;
  private Context mContext;

  @Inject
  public ChatStoriesAdapter(List<StoryData> arrayList, TypefaceManager typefaceManager,
      ConnectFragment connectFragment) {
    this.arrayList = arrayList;
    this.typefaceManager = typefaceManager;
    this.clickListner = connectFragment;
    this.mContext = connectFragment.getActivity();
  }

  @Override
  public int getItemViewType(int position) {
    return arrayList.get(position).isMine() ? 1 : 0;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_stories_row, parent, false);
    return new ViewHolder(itemView, typefaceManager, clickListner);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder mainHolder,
      @SuppressLint("RecyclerView") int position) {

    int padding_in_dp = 3;  // 6 dps
    final float scale = mContext.getResources().getDisplayMetrics().density;
    int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

    if (getItemViewType(position) == 1) {
      try {
        ViewHolder holder = (ViewHolder) mainHolder;
        StoryData storiesList = arrayList.get(position);
        holder.profileNameTv.setText(mContext.getString(R.string.myStory));

        Glide.with(mContext)
            .load(storiesList.getPosts().get(0).getThumbnail().replace(".mp4", ".jpg"))
            .asBitmap()
            .centerCrop()
            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
            .signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)
            .into(new BitmapImageViewTarget(holder.profilePicIv) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.profilePicIv.setImageDrawable(circularBitmapDrawable);
              }
            });

        StoryPost storyPost = storiesList.getPosts().get(storiesList.getPosts().size() - 1);
        if (storyPost.isViewed()) {
          holder.ringFrame.setBackground(
              mContext.getDrawable(R.drawable.circular_story_item_bg_black));
          holder.profilePicIv.setPadding(0, 0, 0, 0);
        } else {
          holder.ringFrame.setBackground(
              mContext.getDrawable(R.drawable.circular_story_item_bg_black));
          holder.profilePicIv.setPadding(padding_in_px, padding_in_px, padding_in_px,
              padding_in_px);
        }
      } catch (Exception ignored) {

      }
    } else {
      try {
        ViewHolder holder = (ViewHolder) mainHolder;
        StoryData storiesList = arrayList.get(position);
        holder.profileNameTv.setText(storiesList.getUserName());
        Glide.with(mContext)
            .load(storiesList.getPosts().get(0).getThumbnail().replace(".mp4", ".jpg"))
            .asBitmap()
            .centerCrop()
            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
            .signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)
            .into(new BitmapImageViewTarget(holder.profilePicIv) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.profilePicIv.setImageDrawable(circularBitmapDrawable);
              }
            });

        StoryPost storyPost = storiesList.getPosts().get(storiesList.getPosts().size() - 1);
        if (storyPost.isViewed()) {
          holder.ringFrame.setBackground(mContext.getDrawable(R.drawable.circular_image_bg));
          holder.profilePicIv.setPadding(0, 0, 0, 0);
        } else {
          holder.ringFrame.setBackground(mContext.getDrawable(R.drawable.circular_story_item_bg));
          holder.profilePicIv.setPadding(padding_in_px, padding_in_px, padding_in_px,
              padding_in_px);
        }
      } catch (Exception ignored) {

      }
    }
  }

  @Override
  public int getItemCount() {
    return arrayList.size();
  }

  public void setListener(ClickListner clickListner) {
    this.clickListner = clickListner;
  }
}
