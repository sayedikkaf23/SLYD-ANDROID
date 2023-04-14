package chat.hola.com.app.post.model;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.List;

import chat.hola.com.app.location.Address_list_item_pojo;

/**
 * Created by DELL on 2/28/2018.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private List<Address_list_item_pojo> list;
    private ClickListner clickListner;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }

    public void setListener(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public interface ClickListner {
        void onItemClick(int position);
    }

    public AddressAdapter(List<Address_list_item_pojo> moviesList) {
        this.list = moviesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Address_list_item_pojo item = list.get(position);
        holder.title.setText(item.getAddress_title());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
