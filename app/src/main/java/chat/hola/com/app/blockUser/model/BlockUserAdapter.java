package chat.hola.com.app.blockUser.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;

/**
 * <h1>BlockUserAdapter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public class BlockUserAdapter extends RecyclerView.Adapter<ViewHolder> {
  private TypefaceManager typefaceManager;
  private ClickListner clickListner;
  private List<User> comments;
  private Context context;

  @Inject
  public BlockUserAdapter(List<User> comments, Activity mContext, TypefaceManager typefaceManager) {
    this.typefaceManager = typefaceManager;
    this.comments = comments;
    this.context = mContext;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.block_item, parent, false);
    return new ViewHolder(itemView, typefaceManager, clickListner);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
    if (getItemCount() > 0) {
      final User comment = comments.get(position);
      holder.tvUserName.setText(comment.getUserName());
      holder.tvName.setText(comment.getFirstName() + " " + comment.getLastName());
      if (comment.getProfilePic() != null && comment.getProfilePic().length() > 0) {
        Glide.with(context)
                .load(comment.getProfilePic())
                .asBitmap().centerCrop()
                .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .into(new BitmapImageViewTarget(holder.ivProfilePic) {
                  @Override
                  protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                  }
                });
      } else {
        Utilities.setTextRoundDrawable(context, comment.getFirstName(), comment.getLastName(), holder.ivProfilePic);
      }
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