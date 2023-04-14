package chat.hola.com.app.dublycategory.modules;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 14 August 2019
 */
class DubCategoryListAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private List<DubCategory> dubs;
    private Context context;
    private CategoryClickListner clickListner;
    private TypefaceManager typefaceManager;

    public DubCategoryListAdapter(Context context, TypefaceManager typefaceManager, List<DubCategory> dubs, CategoryClickListner categoryClickListner) {
        this.dubs = dubs;
        this.context = context;
        this.typefaceManager = typefaceManager;
        this.clickListner = categoryClickListner;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dub_category_item, parent, false);
        return new CategoryViewHolder(itemView, typefaceManager, clickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final DubCategory dub = dubs.get(position);
        Glide.with(context)
                .load(dub.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_default)
                .into(holder.ivThumbnail);
        holder.tvTitle.setText(dub.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListner != null)
                    clickListner.onItemClick(dub.getId(), dub.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dubs.size();
    }

}
