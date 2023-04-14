package chat.hola.com.app.home.stories.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.Utilities;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.List;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/29/2018.
 */

public class ViewerAdapter extends RecyclerView.Adapter<ViewerAdapter.ViewHolder> {

  private List<Viewer> mItems;
  private Context context;

  public ViewerAdapter(Context context, List<Viewer> mItems) {
    this.context = context;
    this.mItems = mItems;
  }

  public void setData(List<Viewer> mItems) {
    this.mItems = mItems;
    notifyDataSetChanged();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.bottom_sheet_item, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.setData(mItems.get(position));
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    Viewer item;
    @BindView(R.id.ivProfilePic)
    ImageView profilePic;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvTime)
    TextView tvTime;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    void setData(Viewer viewer) {
      this.item = viewer;
      if (item.getProfilePic() != null && item.getProfilePic().length() > 0) {
        Glide.with(context)
                .load(item.getProfilePic().replace("upload/", "upload/q_10/").replace("mp4", "jpg"))
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.chat_attachment_profile_default_image_frame)
                .signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //    .skipMemoryCache(true)
                .into(new BitmapImageViewTarget(profilePic) {
                  @Override
                  protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    profilePic.setImageDrawable(circularBitmapDrawable);
                  }
                });
      } else {
        Utilities.setTextRoundDrawable(context, item.getFirstName(), item.getLastName(), profilePic);
      }
      tvUserName.setText(item.getUserName());
      tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(item.getTimestamp())));
    }
  }
}
