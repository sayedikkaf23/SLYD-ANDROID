package chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @since  09/05/17.
 */
public class CustomGalleryMediaAdapter extends RecyclerView.Adapter<CustomGalleryMediaViewholder> implements Filterable
{
    private ArrayList<CustomGalleryMediaItemPojo> item_list_data;
    private ArrayList<CustomGalleryMediaItemPojo> temp_details = new ArrayList<>();
    private ItemImageFilter mFilter = new ItemImageFilter();
    private Activity mactivity;
    public CustomGalleryMediaAdapter(Activity activity, ArrayList<CustomGalleryMediaItemPojo> item_list)
    {
        this.item_list_data = item_list;
        mactivity = activity;
        Glide.get(mactivity).clearMemory();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Glide.get(mactivity).clearDiskCache();
            }
        }).start();
    }

    @Override
    public CustomGalleryMediaViewholder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_gallery_item_layout, parent, false);
        return new CustomGalleryMediaViewholder(v);
    }

    @Override
    public void onBindViewHolder(CustomGalleryMediaViewholder holder, int position)
    {

        if(item_list_data.get(position).isVideo())
        {
            holder.video_icon.setVisibility(View.VISIBLE);
        }else
        {
            holder.video_icon.setVisibility(View.INVISIBLE);
        }
       /*
        *Loading profile picture*/
        load_Image(holder.thumb_nail, item_list_data.get(position).getPath());
    }
    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount()
    {
        if (this.temp_details.size() <= 0)
        {
            this.temp_details.addAll(item_list_data);
        }
        return item_list_data.size();
    }
    /**
     * <h2>load_Image</h2>
     * <p>
     * Loading the Image from the Given string image path .
     * Here checking the Image is from the url or from the file path.
     * if the iamge is from the file path then converting path using file Uri conversion.
     * Or else if url then converting the Url.
     * </P>
     *
     * @param image_url contains the image url.
     */
    private void load_Image(ImageView image_view, String image_url)
    {
             Glide
                .with(mactivity)
                .load(image_url)
                .into(image_view);
    }

    @Override
    public Filter getFilter()
    {
        return mFilter;
    }
    private class ItemImageFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            int count = temp_details.size();
            final ArrayList<CustomGalleryMediaItemPojo> nlist = new ArrayList<>();
            /*
             * Verifying details.*/
            for (int i = 0; i < count; i++)
            {
                if (filterString.equalsIgnoreCase(temp_details.get(i).getBukket_name()))
                {
                    nlist.add(temp_details.get(i));
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            if (results.values != null && results.count > 0)
            {
                item_list_data.clear();
                item_list_data.addAll((ArrayList<CustomGalleryMediaItemPojo>) results.values);
            } else {
                item_list_data.clear();
                item_list_data.addAll(temp_details);
            }
            notifyDataSetChanged();
        }
    }
}
