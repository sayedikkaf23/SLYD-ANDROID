package chat.hola.com.app.DublyCamera;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ezcall.android.R;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private Context context;
    private List<Filter> filterImages;
    private SelectFilterListner listner;

    public FilterAdapter(Context context, List<Filter> filterImages, SelectFilterListner listner) {
        this.context = context;
        this.filterImages = filterImages;
        this.listner = listner;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Filter image = filterImages.get(position);
        holder.imageView.setImageDrawable(context.getResources().getDrawable(image.getFilterImage()));
        holder.ivSelected.setVisibility(image.isSelected() ? View.VISIBLE : View.GONE);
        holder.imageView.setOnClickListener(view -> listner.onFilterSelected(position));
    }

    @Override
    public int getItemCount() {
        return filterImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageView ivSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivFilter);
            ivSelected = itemView.findViewById(R.id.ivSelected);
        }
    }

    public interface SelectFilterListner {
        void onFilterSelected(int position);
    }
}
