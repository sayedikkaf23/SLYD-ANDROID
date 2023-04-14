package chat.hola.com.app.DublyCamera.CameraInFragments.Filters.Adapters;

import android.content.Context;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ezcall.android.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ModelClasses.Edit_item;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ViewHolders.ViewHolderEdit;

/**
 * Created by moda on 18/05/17.
 */

public class Edit_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Edit_item> mListData = new ArrayList<>();


    private Context mContext;


    private Typeface tf;

    private int[] toolIcons = {R.drawable.tool_brightness_whiteout, R.drawable.tool_contrast_whiteout,
            R.drawable.tool_saturation_whiteout, /**R.drawable.tool_highlights_whiteout*/R.drawable.tool_shadows_whiteout, R.drawable.tool_vignette_whiteout, R.drawable.tool_sharpen_whiteout};


    public Edit_Adapter(final Context mContext, ArrayList<Edit_item> mListData) {
        this.mListData = mListData;
        this.mContext = mContext;

        tf = AppController.getInstance().getAvenyT();

    }


    @Override
    public int getItemCount() {
        return this.mListData.size();
    }


    @Override
    public int getItemViewType(int position) {

        return 0;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


        View v1 = inflater.inflate(R.layout.filter_edit_item, viewGroup, false);
        viewHolder = new ViewHolderEdit(v1);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        ViewHolderEdit vh2 = (ViewHolderEdit) viewHolder;

        configureViewHolderEdit(vh2, position);


    }

    private void configureViewHolderEdit(ViewHolderEdit vh14, int position) {
        final Edit_item filter = mListData.get(position);
        if (filter != null) {


            vh14.filterName.setText(filter.getEditName());


            vh14.filterName.setTypeface(tf, Typeface.BOLD);

            vh14.filterImage.setImageResource(toolIcons[position]);
//            try {
//
//
//                Glide
//                        .with(mContext)
//                        .load(filter.getImageUrl())
//
//
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//
//                        .placeholder(R.drawable.login_splash_picogram_logo)
//
//
//                        .into(vh14.filterImage);
//
//
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//

        }

    }


}