package chat.hola.com.app.comment.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.CommentTextView;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.UserSpannable;
import chat.hola.com.app.Utilities.Utilities;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

/**
 * <h1>BlockUserAdapter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public class CommentAdapter extends RecyclerView.Adapter<ViewHolder> {
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;
    private List<Comment> comments;
    private Context context;
    private final HashMap<String, Boolean> mCommentsToggledPositions = new HashMap();

    @Inject
    public CommentAdapter(List<Comment> comments, Context mContext, TypefaceManager typefaceManager) {
        this.typefaceManager = typefaceManager;
        this.comments = comments;
        this.context = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(itemView, typefaceManager, clickListner);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        if (getItemCount() > 0) {
            final Comment comment = comments.get(position);

            holder.iVLike.setSelected(comment.isLiked());

            SpannableString spanString = new SpannableString(comment.getComment());
            //holder.tvMessage.setText(spanString);
            Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
            findMatch(spanString, matcher);
            Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
            findMatch(spanString, userMatcher);
            //holder.tvMessage.setText(spanString);
            //holder.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());

            holder.commentTextView.setOnExpandStateChangeListener((commentId, isCollapsed) -> {
                mCommentsToggledPositions.put(commentId,isCollapsed);
                if(isCollapsed){
                    notifyItemChanged(holder.getAdapterPosition());
                }
            });
            holder.commentTextView.setText(spanString, mCommentsToggledPositions, comment.getId(),
                typefaceManager);

            holder.tvUserName.setText(comment.getCommentedBy());

            if(!TextUtils.isEmpty(comment.getProfilePic())) {
                Glide.with(context)
                        .load(comment.getProfilePic())
                        .asBitmap()
                        .centerCrop()
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .placeholder(R.drawable.chat_attachment_profile_default_image_frame)
                        .into(new BitmapImageViewTarget(holder.ivCommentProfilePic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.ivCommentProfilePic.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }else{
                Utilities.setTextRoundDrawable(context,comment.getCommentedByFirstName(),
                        comment.getCommentedByLastName(),holder.ivCommentProfilePic);
            }

            holder.tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(comment.getTimeStamp())));
            holder.item.setSelected(comment.isSelected());
            holder.ivCommentProfilePic.setOnClickListener(v -> clickListner.onUserClick(position, comment.getCommentedByUserId()));
            holder.tvUserName.setOnClickListener(v -> clickListner.onUserClick(position, comment.getCommentedByUserId()));

//            holder.iVDelete.setVisibility(comment.canDeleteComment() ? View.VISIBLE : View.GONE);

            holder.iVDelete.setOnClickListener(v -> clickListner.onDeleteComment(position,comment));

            holder.iVLike.setOnClickListener(v -> {
                holder.iVLike.setSelected(!holder.iVLike.isSelected());
                clickListner.onLikeComment(position,comment,holder.iVLike.isSelected());
            });
        }
    }

    private void findMatch(SpannableString spanString, Matcher matcher) {
        while (matcher.find()) {
            final String tag = matcher.group(0);
            spanString.setSpan(new TagSpannable(context, tag), matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void findUserMatch(SpannableString spanString, Matcher matcher) {
        while (matcher.find()) {
            final String tag = matcher.group(0);
            spanString.setSpan(new UserSpannable(context, tag), matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setListener(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public void setData(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }
}