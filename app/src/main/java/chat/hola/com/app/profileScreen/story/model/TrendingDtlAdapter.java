package chat.hola.com.app.profileScreen.story.model;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.FlowLayout;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

/**
 * <h>TrendingDtlAdapter.class</h>
 * <p>
 * This Adapter is used by {@link TrendingDetail} activity recyclerView.
 *
 * @author 3Embed
 * @since 14/2/18.
 */

public class TrendingDtlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context context;
  private List<Data> data = new ArrayList<>();
  private String fragmentType;
  private ClickListner clickListner;

  public void setListener(ClickListner clickListner) {
    this.clickListner = clickListner;
  }

  public interface ClickListner {
    void onItemClick(int position, int type, View view);

    void onLikeClick(String postId, boolean like);

    void onCommentClick(String postId);
  }

  @Inject
  public TrendingDtlAdapter(Context context, List<Data> data) {
    this.context = context;
    this.data = data;
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
        .inflate(R.layout.trending_detail_item, parent,
            false));//viewType == 0 ? new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_detail_item, parent, false)) : new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_detail_item1, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

    try {
      if (position != -1) {
        final Data data = this.data.get(position);


        switch (holder.getItemViewType()) {
          case 0:
            ViewHolder1 holder1 = (ViewHolder1) holder;
            String thumbnailUrl = data.getThumbnailUrl1();
            if(data.getPurchased()) {
            if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

              thumbnailUrl = thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
            }}

            DrawableRequestBuilder<String> thumbnail = Glide.with(context)
                .load(Utilities.getModifiedThumbnailLink(thumbnailUrl))
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);

            if (data.getMediaType1() == 0) {
              holder1.ibPlay.setVisibility(View.GONE);
              //image
              if (data.getPurchased()) {
                holder1.rlLockedPost.setVisibility(View.GONE);
              } else {
                holder1.rlLockedPost.setVisibility(View.VISIBLE);
              }
              Glide.with(context)
                  .load(Utilities.getModifiedImageLink(data.getImageUrl1()))
                  .thumbnail(thumbnail)
                  .dontAnimate()
                  .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_default))
                  .into(holder1.ivImage);
            } else {
              //video
              holder1.ibPlay.setVisibility(View.VISIBLE);

              String videoCoverImageUrl;
              if (data.getPurchased()) {
                videoCoverImageUrl = Utilities.getModifiedImageLink(data.getImageUrl1());
                holder1.rlLockedPost.setVisibility(View.GONE);
              } else {
                videoCoverImageUrl = thumbnailUrl;
                holder1.rlLockedPost.setVisibility(View.VISIBLE);
              }

              Glide.with(context)
                  .load(videoCoverImageUrl)
                  .dontAnimate()
                  .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_default))
                  .thumbnail(thumbnail)
                  .into(holder1.ivImage);
            }

            holder1.ivImage.setOnClickListener(
                view -> clickListner.onItemClick(position, data.getMediaType1(), view));

            holder1.ibPlay.setOnClickListener(
                v -> clickListner.onItemClick(position, data.getMediaType1(), v));

            break;

          case 1:
            ViewHolder2 holder2 = (ViewHolder2) holder;
            if (data.getPurchased()) {
              holder2.rlLockedPost.setVisibility(View.GONE);
            } else {
              holder2.rlLockedPost.setVisibility(View.VISIBLE);
            }

            if (data.getMediaType1() == 0) {
              holder2.ibPlay.setVisibility(View.GONE);
              Glide.with(context)
                  .load(Utilities.getModifiedImageLink(data.getImageUrl1()))
                  .asBitmap()
                  .centerCrop()
                  .into(new BitmapImageViewTarget(holder2.ivImage));
            } else {
              holder2.ibPlay.setVisibility(View.VISIBLE);
              Glide.with(context)
                  .load(Utilities.getModifiedImageLink(
                      Utilities.getModifiedImageLink(data.getImageUrl1())))
                  .asBitmap()
                  .centerCrop()
                  .into(new BitmapImageViewTarget(holder2.ivImage));
            }

            holder2.ivImage.setOnClickListener(
                view -> clickListner.onItemClick(position, data.getMediaType1(), view));

            holder2.ibPlay.setOnClickListener(
                v -> clickListner.onItemClick(position, data.getMediaType1(), v));

            holder2.cbLike.setChecked(data.isLiked());
            holder2.cbLike.setOnClickListener(
                v -> clickListner.onLikeClick(data.getPostId(), holder2.cbLike.isChecked()));

            holder2.ibComment.setOnClickListener(
                v -> clickListner.onCommentClick(data.getPostId()));

            Glide.with(context)
                .load(data.getProfilepic())
                .asBitmap()
                .signature(new StringSignature(
                    AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .centerCrop()
                .placeholder(R.drawable.profile_one)
                .into(new BitmapImageViewTarget(holder2.ivUserPhoto));

            holder2.tvUserName.setText(data.getUsername());
            //holder2.tvUserName1.setText(data.getUserName());

            if (!TextUtils.isEmpty(data.getTitle())) {
              holder2.tvTitle.setVisibility(View.VISIBLE);
              SpannableString spanString = new SpannableString(data.getTitle());
              Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
              findMatch(spanString, matcher);
              Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
              findMatch(spanString, userMatcher);
              holder2.tvTitle.setText(spanString);
              holder2.tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
              holder2.tvTitle.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.getPlace())) {
              holder2.tvLocation.setVisibility(View.VISIBLE);
              holder2.tvLocation.setText(data.getPlace());
            } else {
              holder2.tvLocation.setVisibility(View.GONE);
            }

            List<String> s = data.getHashTags();
            if (s != null && s.size() > 0) s.remove(0);
            //                if (s != null && !s.isEmpty()) {
            //                    holder2.flHashTags.removeAllViews();
            //                    holder2.flHashTags.setVisibility(View.VISIBLE);
            //                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ////                    StringBuilder hash = new StringBuilder();
            //                    for (String hash : s) {
            //                        //hash.append(h).append(" ");
            //                        TextView textView = new TextView(context);
            //                        textView.setLayoutParams(params);
            //                        textView.setText(hash + " ");
            //                        textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            //                        textView.setOnClickListener(new View.OnClickListener() {
            //                            @Override
            //                            public void onClick(View view) {
            //                                context.startActivity(new Intent(context, TrendingDetail.class).putExtra("call", "hashtag").putExtra("hashtag", hash));
            //                            }
            //                        });
            //                        holder2.flHashTags.addView(textView);
            //                    }
            //                } else {
            //                    holder2.flHashTags.setVisibility(View.GONE);
            //                }

            holder2.tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(data.getTimeStamp())));

            break;
        }
      }
    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
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
    RelativeLayout rlLockedPost;
    public ViewHolder1(View itemView) {
      super(itemView);
      ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
      ibPlay = (ImageButton) itemView.findViewById(R.id.ibVideo);
      rlLockedPost=itemView.findViewById(R.id.rlLockedPost);
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
    RelativeLayout rlLockedPost;
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
      rlLockedPost=itemView.findViewById(R.id.rlLockedPost);
    }
  }
}