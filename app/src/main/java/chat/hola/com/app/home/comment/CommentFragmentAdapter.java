package chat.hola.com.app.home.comment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.UserSpannable;
import chat.hola.com.app.comment.model.ClickListner;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.comment.model.ViewHolder;

/**
 * <h1>BlockUserAdapter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public class CommentFragmentAdapter extends RecyclerView.Adapter<ViewHolder> {
  private TypefaceManager typefaceManager;
  private ClickListner clickListner;
  private List<Comment> comments;
  private Context context;

  @Inject
  public CommentFragmentAdapter(List<Comment> comments, Context mContext,
                                TypefaceManager typefaceManager) {
    this.typefaceManager = typefaceManager;
    this.comments = comments;
    this.context = mContext;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
    return new ViewHolder(itemView, typefaceManager, clickListner);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder,
      @SuppressLint("RecyclerView") final int position) {
    if (getItemCount() > 0) {
      final Comment comment = comments.get(position);

      SpannableString spanString = new SpannableString(comment.getComment());
      holder.tvMessage.setText(spanString);
      Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
      findMatch(spanString, matcher);
      Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
      findMatch(spanString, userMatcher);
      holder.tvMessage.setText(spanString);
      holder.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());

      holder.tvUserName.setText(comment.getCommentedBy());
      Glide.with(context)
          .load(comment.getProfilePic())
          .asBitmap()
          .centerCrop()
          //.signature(new StringSignature(String.valueOf("comments")))
          .signature(new StringSignature(
              AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
          .into(new BitmapImageViewTarget(holder.ivCommentProfilePic) {
            @Override
            protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable circularBitmapDrawable =
                  RoundedBitmapDrawableFactory.create(context.getResources(), resource);
              circularBitmapDrawable.setCircular(true);
              holder.ivCommentProfilePic.setImageDrawable(circularBitmapDrawable);
            }
          });

      holder.tvTime.setText(TimeAgo.getTimeAgo(Long.parseLong(comment.getTimeStamp())));

      holder.item.setSelected(comment.isSelected());
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
}