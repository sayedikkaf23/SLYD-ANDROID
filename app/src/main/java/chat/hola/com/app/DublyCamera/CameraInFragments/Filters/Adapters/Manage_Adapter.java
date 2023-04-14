package chat.hola.com.app.DublyCamera.CameraInFragments.Filters.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.ezcall.android.R;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ModelClasses.Manage_item;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ViewHolders.ViewHolderManageFilter;

/**
 * Created by moda on 22/05/17.
 */

public class Manage_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Manage_item> mListData = new ArrayList<>();


    private Context mContext;


    private int density_44;

    public Manage_Adapter(Context mContext, ArrayList<Manage_item> mListData) {
        this.mListData = mListData;

        this.mContext = mContext;
        density_44 = (int) ((44) * (mContext.getResources().getDisplayMetrics().density));

    }


    @Override
    public int getItemCount() {
        return this.mListData.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


        View v = inflater.inflate(R.layout.manage_filter_item, viewGroup, false);
        viewHolder = new ViewHolderManageFilter(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        ViewHolderManageFilter vh2 = (ViewHolderManageFilter) viewHolder;
        configureViewHolderManageFilter(vh2, position);

    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderManageFilter(final ViewHolderManageFilter vh, int position) {


        final Manage_item manageItem = mListData.get(position);


        if (manageItem != null) {


            if (manageItem.isSelected()) {

                vh.overlay.setVisibility(View.GONE);

                vh.check.setImageResource(R.drawable.check_circle);
            } else {
                vh.overlay.setVisibility(View.VISIBLE);

                vh.check.setImageResource(R.drawable.share_checkbox);
            }

            vh.filterName.setText(manageItem.getFilterName());


//            vh.filterName.setTypeface(tf, Typeface.NORMAL);


            try {
                Glide
                        .with(mContext)
                        .load(manageItem.getFilterImageUrl())

                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .override(density_44, density_44)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)


                        .into(vh.filterImage);
            } catch (IllegalArgumentException e) {
                e.getStackTrace();
            } catch (NullPointerException e) {
                e.getStackTrace();
            }

        }


    }


}