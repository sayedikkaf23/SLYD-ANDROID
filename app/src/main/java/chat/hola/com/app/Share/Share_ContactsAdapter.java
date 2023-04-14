package chat.hola.com.app.Share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ForwardMessage.Forward_ContactItem;
import chat.hola.com.app.ForwardMessage.ViewHolderForwardToContact;
import chat.hola.com.app.Utilities.TextDrawable;

/**
 * Created by moda on 13/10/17.
 */

public class Share_ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<Forward_ContactItem> mOriginalListData = new ArrayList<>();
    private ArrayList<Forward_ContactItem> mFilteredListData;

    private Context mContext;


    private int density;

//    private Typeface tf;


    /**
     * @param mContext  Context
     * @param mListData ArrayList<Forward_ContactItem>
     */
    public Share_ContactsAdapter(Context mContext, ArrayList<Forward_ContactItem> mListData) {


        this.mOriginalListData = mListData;


        this.mFilteredListData = mListData;
        this.mContext = mContext;


        density = (int) mContext.getResources().getDisplayMetrics().density;
        //   tf = AppController.getInstance().getRobotoCondensedFont();

    }

    /**
     * @return number of items
     */
    @Override
    public int getItemCount() {
        return this.mFilteredListData.size();
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


        View v1 = inflater.inflate(R.layout.forward_contact_item, viewGroup, false);
        viewHolder = new ViewHolderForwardToContact(v1);

        return viewHolder;
    }


    /**
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   item position
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        ViewHolderForwardToContact vh2 = (ViewHolderForwardToContact) viewHolder;

        configureViewHolderContact(vh2, position);


    }


    /**
     * @param vh       ViewHolderContact
     * @param position item position
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderContact(final ViewHolderForwardToContact vh, int position) {
        final Forward_ContactItem forwardContactItem = mFilteredListData.get(position);
        if (forwardContactItem != null) {


//            vh.contactName.setTypeface(tf, Typeface.NORMAL);
//            vh.contactStatus.setTypeface(tf, Typeface.NORMAL);


            vh.contactName.setText(forwardContactItem.getContactName());


            vh.contactStatus.setText(forwardContactItem.getContactStatus());
            if (forwardContactItem.isStar()) {
                vh.iV_star.setVisibility(View.VISIBLE);
            } else {
                vh.iV_star.setVisibility(View.GONE);
            }


            if (forwardContactItem.isSelected()) {
                vh.contactSelected.setVisibility(View.VISIBLE);
            } else {
                vh.contactSelected.setVisibility(View.GONE);
            }


            if (forwardContactItem.getContactImage() != null && !forwardContactItem.getContactImage().isEmpty()) {

                try {
                    Glide.with(mContext).load(forwardContactItem.getContactImage()).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh.contactImage) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh.contactImage.setImageDrawable(circularBitmapDrawable);
                                }
                            });


                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {

                try {

                    vh.contactImage.setImageDrawable(TextDrawable.builder()


                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(24 * density) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()


                            .buildRound((forwardContactItem.getContactName().trim()).charAt(0) + "", Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
                } catch (Exception e) {

                    vh.contactImage.setImageDrawable(TextDrawable.builder()


                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(24 * density) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()


                            .buildRound("C", Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
                }
            }


        }
    }


    /**
     * @return list of filtered items
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredListData = (ArrayList<Forward_ContactItem>) results.values;
                Share_ContactsAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Forward_ContactItem> filteredResults;
                if (constraint.length() == 0) {
                    filteredResults = mOriginalListData;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());


                }
                mFilteredListData = filteredResults;
                if (mContext != null) {
                    ((CustomShare) mContext).showNoSearchResults(constraint, mFilteredListData.size() == mOriginalListData.size(), 1);
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }


    /**
     * @param constraint query to search for
     * @return ArrayList<Forward_ContactItem>
     */

    private ArrayList<Forward_ContactItem> getFilteredResults(String constraint) {
        ArrayList<Forward_ContactItem> results = new ArrayList<>();

        for (Forward_ContactItem item : mOriginalListData) {
            if ((item).getContactName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    /**
     * @return ArrayList<ContactsItem>
     */

    public ArrayList<Forward_ContactItem> getList() {


        return mFilteredListData;
    }


}