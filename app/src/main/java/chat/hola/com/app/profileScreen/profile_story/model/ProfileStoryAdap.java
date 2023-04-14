package chat.hola.com.app.profileScreen.profile_story.model;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.FlowLayout;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryFragment;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ezcall.android.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import javax.inject.Inject;

/**
 * <h>TrendingDtlAdapter.class</h>
 * <p>
 * This Adapter is used by {@link TrendingDetail} activity recyclerView.
 *
 * @author 3Embed
 * @since 14/2/18.
 */

public class ProfileStoryAdap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context context;
  private List<StoryPost> data;
  private String fragmentType;
  private ClickListner clickListner;

  public void setListener(ClickListner clickListner) {
    this.clickListner = clickListner;
  }

  public interface ClickListner {
    void onItemClick(int position);

    void onLikeClick(String postId, boolean like);

    void onCommentClick(String postId);

    void onOptionItemClick(StoryPost data, int position, ImageView iVOptions);
  }

  @Inject
  public ProfileStoryAdap(Context context, List<StoryPost> data, ClickListner frag) {
    this.context = context;
    this.data = data;
    this.clickListner = frag;
  }

  //    @Override
  //    public int getItemViewType(int position) {
  //        return fragmentType.equals("grid") ? 0 : 1;
  //    }

  public void setData(String fragmentType) {
    this.fragmentType = fragmentType;
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder1(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.profile_story_item, parent,
                    false));//viewType == 0 ? new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_detail_item, parent, false)) : new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_detail_item1, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    final StoryPost data = this.data.get(position);

    ViewHolder1 holder1 = (ViewHolder1) holder;
    if (data.getType().equals("1")) {
      holder1.ibPlay.setVisibility(View.GONE);
      //image

      DrawableRequestBuilder<String> thumbnail = Glide.with(context)
              .load(Utilities.getModifiedThumbnailLink(data.getUrlPath()))
              .diskCacheStrategy(DiskCacheStrategy.SOURCE);

      Glide.with(context)
              .load(Utilities.getModifiedImageLink(data.getUrlPath()))
              .thumbnail(thumbnail)
              .dontAnimate()
              .placeholder(context.getResources().getDrawable(R.drawable.ic_default))
              .into(holder1.ivImage);
    } else {
      //video
      holder1.ibPlay.setVisibility(View.VISIBLE);
      DrawableRequestBuilder<String> thumbnail = Glide.with(context)
              .load(Utilities.getModifiedThumbnailLink(data.getThumbnail()))
              .diskCacheStrategy(DiskCacheStrategy.SOURCE);

      Glide.with(context)
              .load(Utilities.getModifiedImageLink(data.getThumbnail()))
              .dontAnimate()
              .placeholder(context.getResources().getDrawable(R.drawable.ic_default))
              .thumbnail(thumbnail)
              .into(holder1.ivImage);

      if(clickListner instanceof LiveStreamHistoryFragment) {
        holder1.iVOptions.setVisibility(View.VISIBLE);
        holder1.iVOptions.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            clickListner.onOptionItemClick(data,position,holder1.iVOptions);
          }
        });
      }
    }

    if (data.getTimestamp() != null) {
      SimpleDateFormat dateformatter = new SimpleDateFormat("d");
      SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
      String dateString = dateformatter.format(new Date(Long.parseLong(data.getTimestamp())));
      String month = monthFormat.format(new Date(Long.parseLong(data.getTimestamp())));
      holder1.tV_date.setText(dateString);
      holder1.tV_month.setText(month);
      holder1.rL_date.setVisibility(View.VISIBLE);
    } else {
      holder1.rL_date.setVisibility(View.GONE);
    }
  }

  private void findMatch(SpannableString spanString, Matcher matcher) {
    while (matcher.find()) {
      final String tag = matcher.group(0);
      spanString.setSpan(new TagSpannable(context, tag), matcher.start(), matcher.end(),
              Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  class ViewHolder1 extends RecyclerView.ViewHolder {
    ImageView ivImage;
    ImageButton ibPlay;
    TextView tV_date, tV_month;
    RelativeLayout rL_date;
    ImageView iVOptions;

    public ViewHolder1(View itemView) {
      super(itemView);
      ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
      iVOptions = (ImageView) itemView.findViewById(R.id.iVOptions);
      ibPlay = (ImageButton) itemView.findViewById(R.id.ibVideo);
      tV_date = (TextView) itemView.findViewById(R.id.tV_date);
      rL_date = (RelativeLayout) itemView.findViewById(R.id.rL_date);
      tV_month = (TextView) itemView.findViewById(R.id.tV_month);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          clickListner.onItemClick(getAdapterPosition());
        }
      });
    }
  }

  class ViewHolder2 extends RecyclerView.ViewHolder {
    ImageView ivImage;
    ImageButton ibPlay;
    ImageView ivUserPhoto;
    TextView tvUserName, tvUserName1, tvLocation, tvTitle, tvTime;
    FlowLayout flHashTags;
    ImageButton ibComment, ibCoin;
    CheckBox cbLike;

    public ViewHolder2(View itemView) {
      super(itemView);
      ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
      ibPlay = (ImageButton) itemView.findViewById(R.id.ibVideo);
      ivUserPhoto = (ImageView) itemView.findViewById(R.id.ivUserPhoto);
      cbLike = (CheckBox) itemView.findViewById(R.id.cbLike);
      ibComment = (ImageButton) itemView.findViewById(R.id.ibComment);
      tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
      tvUserName1 = (TextView) itemView.findViewById(R.id.tvUserName1);
      tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
      tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
      flHashTags = (FlowLayout) itemView.findViewById(R.id.flHashTags);
      tvTime = (TextView) itemView.findViewById(R.id.tvTime);
    }
  }
}