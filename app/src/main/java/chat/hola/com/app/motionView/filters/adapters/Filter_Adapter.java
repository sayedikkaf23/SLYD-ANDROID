package chat.hola.com.app.motionView.filters.adapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ezcall.android.R;


import java.util.ArrayList;

import chat.hola.com.app.AppController;
import chat.hola.com.app.motionView.filters.ViewHolderFilter;
import chat.hola.com.app.motionView.filters.modelClasses.Filter_item;

/**
 * Created by moda on 18/05/17.
 */

public class Filter_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Filter_item> mListData = new ArrayList<>();


    /*
     * To check if the given item is the filter or the manage filter
     */
    private final int FILTER = 0;
    private final int MANAGE = 1;


    private Context mContext;


    private Typeface tf;
    private int density;

    public Filter_Adapter(final Context mContext, ArrayList<Filter_item> mListData) {
        this.mListData = mListData;
        this.mContext = mContext;
        density = (int) ((mContext.getResources().getDisplayMetrics().density) * (90));
        tf = AppController.getInstance().getMediumFont();

        /*
         * Main thread
         */
        Glide.get(mContext).clearMemory();


        /*
         * Background thread
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(mContext).clearDiskCache();
            }
        }).start();

    }


    @Override
    public int getItemCount() {
        return this.mListData.size();
    }


    @Override
    public int getItemViewType(int position) {

        String type = mListData.get(position).getItemType();


        if (type.equals("0")) {
            return FILTER;
        } else {

            return MANAGE;
        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View v1 = inflater.inflate(R.layout.filter_item, viewGroup, false);
   /*     switch (viewType) {
            case FILTER:
                v1 = inflater.inflate(R.layout.filter_item, viewGroup, false);
                viewHolder = new ViewHolderFilter(v1);
                break;


            default:
                v1 = inflater.inflate(R.layout.manage_item, viewGroup, false);
                viewHolder = new ViewHolderManage(v1);
                break;


        }*/
        return new ViewHolderFilter(v1);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolderFilter vh2 = (ViewHolderFilter) viewHolder;

        configureViewHolderFilter(vh2, position);


    /*    switch (viewHolder.getItemViewType()) {


            case FILTER:
                ViewHolderFilter vh2 = (ViewHolderFilter) viewHolder;

                configureViewHolderFilter(vh2, position);

                break;


            default:

                ViewHolderManage vh16 = (ViewHolderManage) viewHolder;
                configureViewHolderManage(vh16, position);
                break;


        }*/
    }

    private void configureViewHolderFilter(ViewHolderFilter vh14, int position) {
        final Filter_item filter = mListData.get(position);
        if (filter != null) {


            vh14.filterName.setText(filter.getFilterName());

            vh14.filterName.setTypeface(tf, Typeface.NORMAL);

            if (filter.isSelected()) {
//                vh14.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.color_text_black));
                vh14.filterSelected.setVisibility(View.VISIBLE);

            } else {
//                vh14.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.color_white));
//                vh14.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.gray_text_darker));
                vh14.filterSelected.setVisibility(View.GONE);

            }

            try {



                Glide
                        .with(mContext)
                        .load(filter.getImageUrl())
                        .override(density, density)

                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                        .placeholder(R.drawable.image_background)

                        .into(vh14.filterImage);


            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


        }

    }


/*
    private void configureViewHolderManage(ViewHolderManage vh14, int position) {
        final Filter_item filter = mListData.get(position);
        if (filter != null) {
            vh14.filterName.setTypeface(tf, Typeface.BOLD);
        }
    }

*/

}