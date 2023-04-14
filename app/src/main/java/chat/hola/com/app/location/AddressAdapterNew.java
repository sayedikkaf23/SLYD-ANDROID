package chat.hola.com.app.location;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.List;

/**
 * @since 2/2/17.
 */
public class AddressAdapterNew extends RecyclerView.Adapter<AddressAdapterNew.Address_list_item> {
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private final OnItemClickListener listener;
    private List<Address_list_item_pojo> adress_details;

    public AddressAdapterNew(List<Address_list_item_pojo> objects, OnItemClickListener clickListener) {
        this.listener = clickListener;
        this.adress_details = objects;
    }

    @Override
    public Address_list_item onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        return new Address_list_item(v);
    }

    @Override
    public void onBindViewHolder(Address_list_item holder, final int position) {
        holder.locationName.setText(adress_details.get(position).getAddress_title());
        holder.addressTextview.setText(adress_details.get(position).getSub_Address());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adress_details.size();
    }

    class Address_list_item extends RecyclerView.ViewHolder {
        TextView locationName, addressTextview;
        RelativeLayout item;

        Address_list_item(View itemView) {
            super(itemView);
            locationName = (TextView) itemView.findViewById(R.id.location_name);
            addressTextview = (TextView) itemView.findViewById(R.id.address_textview);
            item = (RelativeLayout) itemView.findViewById(R.id.item);
        }
    }
}