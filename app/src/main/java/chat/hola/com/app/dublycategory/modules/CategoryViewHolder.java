package chat.hola.com.app.dublycategory.modules;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ViewHolder</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ivCategoryImage)
    ImageView ivThumbnail;
    @BindView(R.id.tvCategoryName)
    TextView tvTitle;
    @BindView(R.id.item)
    LinearLayout item;

    private CategoryClickListner clickListner;

    public CategoryViewHolder(View itemView, TypefaceManager typefaceManager, CategoryClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        this.clickListner = clickListner;
    }


//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.item)
//            clickListner.onItemClick(getAdapterPosition());
//    }
}