
package chat.hola.com.app.live_stream.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.live_stream.ResponcePojo.AllGiftsData;

public class GiftCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AllGiftsData> allGiftsData;
    private Context mContext;
    private SelectListner selectListner;
    private TypefaceManager typefaceManager;

    public GiftCategoriesAdapter(ArrayList<AllGiftsData> allGiftsData, Context mContext, TypefaceManager typefaceManager) {
        this.allGiftsData = allGiftsData;
        this.mContext = mContext;
        this.typefaceManager = typefaceManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.gifts_categories_item, viewGroup, false);
        return new ViewHolderGiftCategories(v, typefaceManager);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolderGiftCategories holder = (ViewHolderGiftCategories) viewHolder;
        holder.tvName.setSelected(allGiftsData.get(position).isSelected());
        holder.tvName.setText(allGiftsData.get(position).getName());
        holder.itemView.setOnClickListener(v -> selectListner.selectItem(position));
    }

    @Override
    public int getItemCount() {
        return allGiftsData.size();
    }

    public void setListner(SelectListner selectListner) {
        this.selectListner = selectListner;
    }

    private class ViewHolderGiftCategories extends RecyclerView.ViewHolder {

        private TextView tvName;

        private ViewHolderGiftCategories(@NonNull View itemView, TypefaceManager typefaceManager) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvName.setTypeface(typefaceManager.getBoldFont());

        }
    }

    public interface SelectListner {
        void selectItem(int position);
    }
}
