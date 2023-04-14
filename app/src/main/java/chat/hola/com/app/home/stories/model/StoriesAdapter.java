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
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.TypefaceManager;
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

public class StoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private TypefaceManager typefaceManager;
  private List<StoryData> arrayList;
  private ClickListner clickListner;
  private Context context;

  @Inject
  public StoriesAdapter(Context context, List<StoryData> arrayList,
      TypefaceManager typefaceManager) {
    this.context = context;
    this.arrayList = arrayList;
    this.typefaceManager = typefaceManager;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == 0) {
      View itemView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.stories_header_row, parent, false);
      return new HeaderViewHolder(itemView, typefaceManager);
    } else {
      View itemView =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.stories_row, parent, false);
      return new ViewHolder(itemView, typefaceManager, clickListner);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder mainHolder,
      @SuppressLint("RecyclerView") int position) {
    if (getItemViewType(position) == 0) {
      HeaderViewHolder holder = (HeaderViewHolder) mainHolder;
      holder.tvHeader.setText(arrayList.get(position).getHeaderTitle());
    } else {
      try {
        ViewHolder holder = (ViewHolder) mainHolder;
        final StoryData storiesList = arrayList.get(position);
        holder.profileNameTv.setText(storiesList.getUserName());
        holder.timeTv.setText(storiesList.getPosts().get(position - 1).getTimestamp());

        Glide.with(context)
            .load(storiesList.getPosts()
                .get(storiesList.getPosts().size() - 1)
                .getThumbnail()
                .replace(".mp4", ".jpg"))
            .asBitmap()
            .signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
            .centerCrop()
            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
            //.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            .into(new BitmapImageViewTarget(holder.profilePicIv) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.profilePicIv.setImageDrawable(circularBitmapDrawable);
              }
            });

        holder.timeTv.setText(TimeAgo.getTimeAgo(Long.parseLong(
            storiesList.getPosts().get(storiesList.getPosts().size() - 1).getTimestamp())));
      } catch (Exception ignored) {

      }
    }
  }

  @Override
  public int getItemCount() {
    return arrayList.size();
  }

  @Override
  public int getItemViewType(int position) {
    return arrayList.get(position).isHeader() ? 0 : 1;
  }

  public void setListener(ClickListner clickListner) {
    this.clickListner = clickListner;
  }
}
