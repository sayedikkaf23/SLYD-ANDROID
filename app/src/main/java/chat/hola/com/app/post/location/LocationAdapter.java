package chat.hola.com.app.post.location;


import android.app.Activity;
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
import chat.hola.com.app.models.Place;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 23 September 2019
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<Place> places;
    private Activity mContext;
    private TypefaceManager typefaceManage;
    private ClickListner clickListner;

    @Inject
    public LocationAdapter(List<Place> places, Activity mContext, TypefaceManager typefaceManager) {
        this.places = places;
        this.mContext = mContext;
        this.typefaceManage = typefaceManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_place, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.tvPlace.setText(place.getTitle());
        holder.tvAddress.setText(place.getAddress());
        holder.itemView.setOnClickListener(v -> clickListner.onAddressSelect(place));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPlace)
        public TextView tvPlace;
        @BindView(R.id.tvAddress)
        public TextView tvAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvPlace.setTypeface(typefaceManage.getMediumFont());
            tvAddress.setTypeface(typefaceManage.getRegularFont());
        }
    }

    public interface ClickListner {
        void onAddressSelect(Place place);
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }
}
