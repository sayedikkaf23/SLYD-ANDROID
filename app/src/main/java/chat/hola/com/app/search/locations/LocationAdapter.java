package chat.hola.com.app.search.locations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {


    private Context context;
    private List<Location> list;
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;

    public LocationAdapter(Context context, List<Location> list, TypefaceManager typefaceManager) {
        this.context = context;
        this.list = list;
        this.typefaceManager = typefaceManager;
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(v -> clickListner.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Location> data) {
        list = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvName.setTypeface(typefaceManager.getSemiboldFont());
        }

    }

    public interface ClickListner {
        void onItemClick(int position);
    }
}
