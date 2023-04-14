package chat.hola.com.app.dublycategory.modules;


import android.content.Context;
import android.view.View;

import com.ezcall.android.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 14 August 2019
 */
public class CategoryGridViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rvCategoriesList)
    RecyclerView recyclerView;

    public CategoryGridViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

    }
}
