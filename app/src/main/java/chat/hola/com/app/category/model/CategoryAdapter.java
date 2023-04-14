package chat.hola.com.app.category.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.post.model.CategoryData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.List;
import javax.inject.Inject;

/**
 * <h1>CategoryAdapter</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 28/3/18
 */

public class CategoryAdapter extends RecyclerView.Adapter<ViewHolder> {
  private List<CategoryData> categories;
  private TypefaceManager typefaceManager;
  private Context context;
  private ClickListner clickListner;

  @Inject

  public CategoryAdapter(Context context, List<CategoryData> categories,
      TypefaceManager typefaceManager) {
    this.typefaceManager = typefaceManager;
    this.categories = categories;
    this.context = context;
  }

  public void setListener(ClickListner clickListner) {
    this.clickListner = clickListner;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
    return new ViewHolder(view, typefaceManager, clickListner);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
    CategoryData category = categories.get(position);
    Glide.with(context)
        .load(category.getCategoryActiveIconUrl())
        .asBitmap()
        .centerCrop()
        .placeholder(R.mipmap.ic_launcher)
        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
        .signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
        .into(new BitmapImageViewTarget(holder.ivRow));
    holder.tvRow.setText(category.getCategoryName());
    holder.cbRow.setChecked(category.isSelected());
  }

  @Override
  public int getItemCount() {
    return categories.size();
  }
}
