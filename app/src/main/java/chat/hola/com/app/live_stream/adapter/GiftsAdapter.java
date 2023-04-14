package chat.hola.com.app.live_stream.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.ArrayList;

import chat.hola.com.app.models.GiftCombo;

public class GiftsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<GiftCombo> gifts;
    private Context mContext;
    private GiftGridAdapter.ItemSelctListner itemSelctListner;

    //    public GiftsAdapter(ArrayList<Gifts> gifts, Context mContext) {
//        this.gifts = gifts;
//        this.mContext = mContext;
//    }
    public GiftsAdapter(ArrayList<GiftCombo> gifts, Context mContext, GiftGridAdapter.ItemSelctListner itemSelctListner) {
        this.gifts = gifts;
        this.mContext = mContext;
        this.itemSelctListner=itemSelctListner;
    }

    //    gifts_item
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.gift_grid, viewGroup, false);
        return new ViewHolderGifts(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolderGifts holder = (ViewHolderGifts) viewHolder;
        GiftCombo giftsArrayList = gifts.get(position);

        holder.recyclerView.setAdapter(new GiftGridAdapter(giftsArrayList.getGifts(), mContext,itemSelctListner));
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }

    public void setData(ArrayList<GiftCombo> arrayLists) {
        this.gifts = arrayLists;
        notifyDataSetChanged();
    }

    private class ViewHolderGifts extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        private ViewHolderGifts(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rvGifGrid);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        }
    }

}
