package chat.hola.com.app.collections.saved;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import java.util.List;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.collections.view_holder.AllPostViewHolder;
import chat.hola.com.app.collections.view_holder.CollectionViewHolder;

public class SavedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ALL_POST = 0 ;
    private static final int COLLECTION = 1;
    private Context mContext;
    private List<CollectionData> data;
    private ClickListener clickListener;
    int spacing = 20; // 200px

    public SavedAdapter(Context mContext, List<CollectionData> data) {
        this.mContext = mContext;
        this.data = data;
    }

    /**
     * <p>For set listener.</p>
     * @param clickListener
     */
    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(viewType==ALL_POST){
            view = LayoutInflater.from(mContext).inflate(R.layout.collection_all_post_item,null,false);
            return new AllPostViewHolder(view);
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.collection_item,null,false);
            return new CollectionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)==ALL_POST){
            AllPostViewHolder allPostHolder = (AllPostViewHolder) holder;

            int setWidth = (CommonClass.getDeviceWidth((Activity) mContext)/2);
            //int setWidth = allPostHolder.itemView.getMeasuredWidth();

            //allPostHolder.cardView.getLayoutParams().width=setWidth;
            //allPostHolder.cardView.getLayoutParams().height=setWidth;

            int imageSpacing = 3;


            allPostHolder.image1.getLayoutParams().width=setWidth/2;
            allPostHolder.image1.getLayoutParams().height=setWidth/2;
            allPostHolder.image1.setPadding(0,0,imageSpacing,imageSpacing);

            allPostHolder.image2.getLayoutParams().width=setWidth/2;
            allPostHolder.image2.getLayoutParams().height=setWidth/2;
            allPostHolder.image2.setPadding(imageSpacing,0,0,imageSpacing);

            allPostHolder.image3.getLayoutParams().width=setWidth/2;
            allPostHolder.image3.getLayoutParams().height=setWidth/2;
            allPostHolder.image3.setPadding(0,imageSpacing,imageSpacing,0);

            allPostHolder.image4.getLayoutParams().width=setWidth/2;
            allPostHolder.image4.getLayoutParams().height=setWidth/2;
            allPostHolder.image4.setPadding(imageSpacing,imageSpacing,0,0);

//            allPostHolder.image1.getLayoutParams().width=imageSize;
//            allPostHolder.image1.getLayoutParams().height=imageSize;
//            allPostHolder.image2.getLayoutParams().width=imageSize;
//            allPostHolder.image2.getLayoutParams().height=imageSize;
//            allPostHolder.image3.getLayoutParams().width=imageSize;
//            allPostHolder.image3.getLayoutParams().height=imageSize;
//            allPostHolder.image4.getLayoutParams().width=imageSize;
//            allPostHolder.image4.getLayoutParams().height=imageSize;

            try {
                if (data.get(position).getImages().size() > 0 && data.get(position).getImages().get(0) != null) {
                    Glide.with(mContext)
                            .load(data.get(position).getImages().get(0))
                            .centerCrop()
                            .placeholder(R.color.colorBonJour)
                            .into(allPostHolder.image1);
                }else {
                    allPostHolder.image1.setImageDrawable(mContext.getDrawable(R.drawable.rounded_rectangle_light_gray));
                }


                if (data.get(position).getImages().size() > 1 && data.get(position).getImages().get(1) != null) {
                    Glide.with(mContext)
                            .load(data.get(position).getImages().get(1))
                            .centerCrop()
                            .placeholder(R.color.colorBonJour)
                            .into(allPostHolder.image2);
                }else {
                    allPostHolder.image2.setImageDrawable(mContext.getDrawable(R.drawable.rounded_rectangle_light_gray));
                }

                if (data.get(position).getImages().size() > 2 && data.get(position).getImages().get(2) != null) {
                    Glide.with(mContext)
                            .load(data.get(position).getImages().get(2))
                            .centerCrop()
                            .placeholder(R.color.colorBonJour)
                            .into(allPostHolder.image3);
                }else {
                    allPostHolder.image3.setImageDrawable(mContext.getDrawable(R.drawable.rounded_rectangle_light_gray));
                }

                if (data.get(position).getImages().size() > 3 && data.get(position).getImages().get(3) != null) {
                    Glide.with(mContext)
                            .load(data.get(position).getImages().get(3))
                            .centerCrop()
                            .placeholder(R.color.colorBonJour)
                            .into(allPostHolder.image4);
                }else {
                    allPostHolder.image4.setImageDrawable(mContext.getDrawable(R.drawable.rounded_rectangle_light_gray));
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            allPostHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        clickListener.OnItemClick(position,data.get(position));
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

        }else {
            CollectionViewHolder collectionHolder = (CollectionViewHolder) holder;

            int setWidth = (CommonClass.getDeviceWidth((Activity) mContext)/2);

            collectionHolder.image.getLayoutParams().width=setWidth;
            collectionHolder.image.getLayoutParams().height=setWidth;

            if(data.get(position).getCoverImage()!=null) {
                Glide.with(mContext)
                        .load(data.get(position).getCoverImage())
                        .centerCrop()
                        .placeholder(R.color.colorBonJour)
                        .into(collectionHolder.image);
            }else {
                collectionHolder.image.setImageDrawable(mContext.getDrawable(R.drawable.ic_default));
            }

            collectionHolder.tV_title.setText(data.get(position).getCollectionName());

            collectionHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        clickListener.OnItemClick(position,data.get(position));
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return ALL_POST;
        else
            return COLLECTION;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * Item Click listener of this adapter
     */
    public interface ClickListener{
        public void OnItemClick(int pos,CollectionData data);
    }
}
