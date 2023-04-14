package chat.hola.com.app.Wallpapers.Library;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import java.util.ArrayList;

import chat.hola.com.app.AppController;

/**
 * Created by moda on 11/10/17.
 */

public class SelectWallpaper_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Wallpaper_Library_item> mListData = new ArrayList<>();

    private Context mContext;


    /**
     * @param mContext  Context
     * @param mListData ArrayList<Wallpaper_Library_item>
     */
    public SelectWallpaper_Adapter(Context mContext, ArrayList<Wallpaper_Library_item> mListData) {


        this.mListData = mListData;


        this.mContext = mContext;


    }

    /**
     * @return number of items
     */
    @Override
    public int getItemCount() {
        return this.mListData.size();
    }


    /**
     * @param position item position
     * @return item viewType
     */
    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    /**
     * @param viewGroup ViewGroup
     * @param viewType  item viewType
     * @return RecyclerView.ViewHolder
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


        View v = inflater.inflate(R.layout.wallpaper_lib_item, viewGroup, false);
        viewHolder = new ViewHolderLibraryWallpaper(v);

        return viewHolder;
    }


    /**
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   item position
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        ViewHolderLibraryWallpaper vh2 = (ViewHolderLibraryWallpaper) viewHolder;
        configureViewHolderLibraryWallpaper(vh2, position);

    }


    /**
     * @param vh       ViewHolderLibraryWallpaper
     * @param position item position
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderLibraryWallpaper(final ViewHolderLibraryWallpaper vh, int position) {
        final Wallpaper_Library_item wallpaperLibraryItem = mListData.get(position);
        if (wallpaperLibraryItem != null) {


            try {

                vh.wallpaper.setBackgroundColor(Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19)));
                Glide.with(mContext)
                        .load(wallpaperLibraryItem.getWallpaperUrl())
                        .crossFade()

                        .centerCrop()

                        .into(vh.wallpaper);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


        }
    }


}
