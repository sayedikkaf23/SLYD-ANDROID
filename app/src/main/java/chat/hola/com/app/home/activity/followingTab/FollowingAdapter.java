package chat.hola.com.app.home.activity.followingTab;

import android.content.Context;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import chat.hola.com.app.AppController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.activity.followingTab.model.ClickListner;
import chat.hola.com.app.home.activity.followingTab.model.Following;
import chat.hola.com.app.home.model.Data;

/**
 * <h>FollowAdapter.class</h>
 * <p>This adapter used by {@link FollowingFrag}</p>
 *
 * @author 3Embed
 * @since 14/2/18.
 */

public class FollowingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private TypefaceManager typefaceManager;
    private ArrayList<Following> followings = new ArrayList<>();
    private ClickListner clickListner;

    @Inject
    public FollowingAdapter(Context context, TypefaceManager typefaceManager) {
        this.context = context;
        this.typefaceManager = typefaceManager;
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public void setData(ArrayList<Following> followings) {
        this.followings.clear();
        this.followings.addAll(followings);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 3://following
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_item, parent, false);
                return new FollowingViewHolder(view);
            case 5://commented
            case 2://liked
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commented_item, parent, false);
                return new CommentViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_item, parent, false);
                return new FollowingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Following data = followings.get(position);
        try {
            switch (getItemViewType(position)) {
                case 3://following
                    FollowingViewHolder holder1 = (FollowingViewHolder) holder;

                    Glide.with(context).load(data.getProfilePic()).asBitmap().signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop().placeholder(R.drawable.profile_one)
                            .into(new BitmapImageViewTarget(holder1.ivProfilePic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    holder1.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                                }
                            });

                    String other = data.getTargetUserName().isEmpty() ? "" : "@" + data.getTargetUserName();
                    String message = "@" + data.getUserName() + context.getString(R.string.space) + data.getMessage() + context.getString(R.string.space) + other;
                    SpannableString spanString = new SpannableString(message);
                    Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
                    findMatch(spanString, userMatcher);
                    holder1.tvMessage.setText(spanString);
                    holder1.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());

                    holder1.tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(data.getTimeStamp())));
                    break;
                case 5://commented
                case 2://liked
                    CommentViewHolder holder2 = (CommentViewHolder) holder;

                    Glide.with(context).load(data.getProfilePic()).asBitmap().signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop().placeholder(R.drawable.profile_one)
                            .into(new BitmapImageViewTarget(holder2.ivProfilePic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    holder2.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                                }
                            });

                    String other1 = data.getTargetUserName().isEmpty() ? "" : "@" + data.getTargetUserName();
                    String message1 = "@" + data.getUserName() + context.getString(R.string.space) + data.getMessage() + context.getString(R.string.space) + other1;
                    SpannableString spanString1 = new SpannableString(message1);
                    Matcher userMatcher1 = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString1);
                    findMatch(spanString1, userMatcher1);
                    holder2.tvMessage.setText(spanString1);
                    holder2.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
                    holder2.tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(data.getTimeStamp())));

                    Data mediaData = data.getData();
                    holder2.ivPlay.setVisibility(mediaData.getMediaType1() == 0 ? View.GONE : View.VISIBLE);
                    Glide.with(context).load(Utilities.getModifiedImageLink(mediaData.getImageUrl1())).asBitmap().signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop()
                            .placeholder(R.drawable.ic_default).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder2.ivMedia);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findMatch(SpannableString spanString, Matcher matcher) {
        while (matcher.find()) {
            final String tag = matcher.group(0);
            spanString.setSpan(new TagSpannable(context, tag), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return followings.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return followings != null ? followings.size() : 0;
    }

    public class FollowingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfilePic)
        ImageView ivProfilePic;
        @BindView(R.id.tvMessage)
        TextView tvMessage;
        @BindView(R.id.tvTime)
        TextView tvTime;

        public FollowingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvMessage.setTypeface(typefaceManager.getMediumFont());
            tvTime.setTypeface(typefaceManager.getRegularFont());
        }

        @Optional
        @OnClick(R.id.ivProfilePic)
        public void user() {
            clickListner.onUserClicked(followings.get(getAdapterPosition()).getUserId());
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfilePic)
        ImageView ivProfilePic;
        @BindView(R.id.tvMessage)
        TextView tvMessage;
        @BindView(R.id.ivMedia)
        ImageView ivMedia;
        @BindView(R.id.ivPlay)
        ImageView ivPlay;
        @BindView(R.id.tvTime)
        TextView tvTime;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvMessage.setTypeface(typefaceManager.getMediumFont());
            tvTime.setTypeface(typefaceManager.getRegularFont());
        }

        @Optional
        @OnClick(R.id.ivProfilePic)
        public void user() {
            clickListner.onUserClicked(followings.get(getAdapterPosition()).getUserId());
        }


        @Optional
        @OnClick(R.id.ivMedia)
        public void media() {
            clickListner.onMediaClick(getAdapterPosition(), ivMedia);
        }
    }
}
