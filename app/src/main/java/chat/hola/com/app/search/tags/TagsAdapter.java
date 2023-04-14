package chat.hola.com.app.search.tags;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.search.tags.module.Tags;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.List;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
  private AppController appController;
  private Typeface fontMedium;
  private List<Tags> searchData;
  private Context context;
  private TagsAdapter.ClickListner clickListner;

  //    @Inject
  //    public ChannelAdapter(List<Data> arrayList, LandingActivity mContext, TypefaceManager typefaceManager) {
  //    }

  public void setData(Context context, List<Tags> data) {
    this.context = context;
    this.searchData = data;
    notifyDataSetChanged();
  }

  @Override
  public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.hashtag_row, parent, false);
    appController = AppController.getInstance();
    fontMedium = appController.getMediumFont();
    return new ViewHolder(itemView);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(TagsAdapter.ViewHolder holder, final int position) {
    if (getItemCount() != -1) {
      Tags data = searchData.get(position);
      if (data.getHashTags() != null) {
        Glide.with(context).load(data.getImage()).asBitmap().centerCrop()
                .placeholder(R.drawable.ic_def_hashtag)
            .signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).
            //diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
            .into(new BitmapImageViewTarget(holder.profileIv) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.profileIv.setImageDrawable(circularBitmapDrawable);
              }
            });
        holder.profileNameTv.setText(data.getHashTags().replace("#", ""));
        holder.tvCounts.setText(
            data.getTotalPublicPosts() + " " + (data.getTotalPublicPosts() > 1 ? " Posts"
                : " Post"));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            clickListner.onItemClick(position);
          }
        });
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private static Bitmap getBitmap(Drawable vectorDrawable) {
    Bitmap bitmap =
        Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    vectorDrawable.draw(canvas);
    return bitmap;
  }

  @Override
  public int getItemCount() {
    return searchData != null ? searchData.size() : -1;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView profileIv;
    private TextView profileNameTv;
    private RelativeLayout relativeLayout;
    private TextView tvCounts;

    public ViewHolder(View itemView) {
      super(itemView);
      profileNameTv = (TextView) itemView.findViewById(R.id.profileNameTv);
      profileIv = (ImageView) itemView.findViewById(R.id.profileIv);
      relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rlItem);
      tvCounts = (TextView) itemView.findViewById(R.id.tvCounts);
      tvCounts.setTypeface(fontMedium);
      profileNameTv.setTypeface(appController.getSemiboldFont());
    }
  }

  public void setListener(TagsAdapter.ClickListner clickListner) {
    this.clickListner = clickListner;
  }

  public interface ClickListner {
    void onItemClick(int position);
  }
}
