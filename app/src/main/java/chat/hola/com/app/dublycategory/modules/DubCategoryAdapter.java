package chat.hola.com.app.dublycategory.modules;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>BlockUserAdapter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public class DubCategoryAdapter extends RecyclerView.Adapter<CategoryGridViewHolder> {
    private List<CategoriesCombo> dubs;
    private Context context;
    private TypefaceManager typefaceManager;
    private CategoryClickListner categoryClickListner;

    @Inject
    public DubCategoryAdapter(List<CategoriesCombo> dubs, Activity mContext, TypefaceManager typefaceManager) {
        this.dubs = dubs;
        this.context = mContext;
        this.typefaceManager = typefaceManager;
    }


    @Override
    public CategoryGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dub_category_grid, parent, false);//.inflate(R.layout.dub_category_item, parent, false);
        return new CategoryGridViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(final CategoryGridViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        List<DubCategory> categories = dubs.get(position).getDubCategories();
        holder.recyclerView.setAdapter(new DubCategoryListAdapter(context, typefaceManager, categories, categoryClickListner));
    }

    @Override
    public int getItemCount() {
        return dubs.size();
    }


    public void setListener(CategoryClickListner categoryClickListner) {
        this.categoryClickListner = categoryClickListner;
    }
}