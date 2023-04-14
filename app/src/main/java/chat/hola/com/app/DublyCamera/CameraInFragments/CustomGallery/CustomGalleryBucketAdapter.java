package chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ezcall.android.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by moda on 09/05/17.
 */

public class CustomGalleryBucketAdapter extends RecyclerView.Adapter<CustomGalleryBucketAdapter.Item_view_holder> {
    private ArrayList<String> item_list_data;

    public CustomGalleryBucketAdapter(ArrayList<String> item_list) {
        this.item_list_data = item_list;
    }

    @Override
    public Item_view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_popup_each_item_view, parent, false);
        return new Item_view_holder(v);
    }

    @Override
    public void onBindViewHolder(Item_view_holder holder, int position) {
        holder.bukket_name.setText(item_list_data.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return item_list_data.size();
    }

    class Item_view_holder extends RecyclerView.ViewHolder {
        TextView bukket_name;

        Item_view_holder(View itemView) {
            super(itemView);
            bukket_name = (TextView) itemView.findViewById(R.id.bukket_name);
        }
    }
}

