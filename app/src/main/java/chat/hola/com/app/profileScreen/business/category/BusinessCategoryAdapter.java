package chat.hola.com.app.profileScreen.business.category;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.profileScreen.model.BusinessCategory;

/**
 * <h1>BusinessCategoryAdapter</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 16 August 2019
 */
public class BusinessCategoryAdapter extends RecyclerView.Adapter<BusinessCategoryAdapter.ViewHolder> {

    private Context context;
    private List<BusinessCategory.Data> categories;
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;
    private int lastCheckedPosition = -1;

    @Inject
    public BusinessCategoryAdapter(List<BusinessCategory.Data> categories, Activity mContext, TypefaceManager typefaceManager) {
        this.context = mContext;
        this.categories = categories;
        this.typefaceManager = typefaceManager;
    }

    /**
     * It sets ClickListner
     *
     * @param clickListner : ClickListner
     */
    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false), typefaceManager);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusinessCategory.Data category = categories.get(position);
        holder.tvCategoryName.setText(category.getName());
        holder.tvCategoryName.setSelected(position == lastCheckedPosition);
        holder.itemView.setOnClickListener(v -> clickListner.onItemSelect(category));
    }

    public void setLastCheckedPosition(int pos) {
        lastCheckedPosition = pos;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * Handles the click listner
     */
    public interface ClickListner {
        /**
         * On select of any list item
         *
         * @param businessCategory : business category information
         */
        void onItemSelect(BusinessCategory.Data businessCategory);
    }


    /**
     * Class holds the adapter view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCategoryName)
        TextView tvCategoryName;

        public ViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvCategoryName.setTypeface(typefaceManager.getRegularFont());
        }
    }
}
