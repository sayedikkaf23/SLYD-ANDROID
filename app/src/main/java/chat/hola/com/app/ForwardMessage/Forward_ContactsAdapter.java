package chat.hola.com.app.ForwardMessage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;

import chat.hola.com.app.Activities.ContactsList;
import chat.hola.com.app.Activities.ContactsSecretChat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TextDrawable;
import chat.hola.com.app.home.connect.ContactActivity;

/**
 * Created by moda on 30/08/17.
 */

public class Forward_ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<Forward_ContactItem> mOriginalListData = new ArrayList<>();
    private ArrayList<Forward_ContactItem> mFilteredListData;

    private Context mContext;
    private int registeredContactsSize = 0;


    private int density;

//    private Typeface tf;


    /**
     * @param mContext  Context
     * @param mListData ArrayList<Forward_ContactItem>
     */
    public Forward_ContactsAdapter(Context mContext, ArrayList<Forward_ContactItem> mListData) {


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


    public void registeredContacts(int registeredContactsSize){
        this.registeredContactsSize = registeredContactsSize;
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
        try{

            if (position==registeredContactsSize){
                //show textview
                vh.tv_InviteContact.setVisibility(View.VISIBLE);

            }else {
                vh.tv_InviteContact.setVisibility(View.GONE);
//                vh.frame.setGravity(RelativeLayout.CENTER_VERTICAL);
            }
            if (forwardContactItem != null) {

                if (!forwardContactItem.getInvite()) {
                    // hide invite
                    vh.btn_invite.setVisibility(View.GONE);
                } else {
                    // show invite
                    vh.btn_invite.setVisibility(View.VISIBLE);
                }


//            vh.contactName.setTypeface(tf, Typeface.NORMAL);
//            vh.contactStatus.setTypeface(tf, Typeface.NORMAL);


                vh.contactName.setText(forwardContactItem.getContactName());


                vh.contactStatus.setText(forwardContactItem.getContactStatus());


                if (forwardContactItem.isSelected()) {
                    vh.contactSelected.setVisibility(View.VISIBLE);
                } else {
                    vh.contactSelected.setVisibility(View.GONE);
                }

                if(forwardContactItem.isStar()){
                    vh.iV_star.setVisibility(View.VISIBLE);
                }else {
                    vh.iV_star.setVisibility(View.GONE);
                }
                if (forwardContactItem.getContactImage() != null && !forwardContactItem.getContactImage().isEmpty())


                {

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
        } catch (Exception e){
            e.printStackTrace();
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
                Forward_ContactsAdapter.this.notifyDataSetChanged();
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

                    if(mContext instanceof ContactsSecretChat){
                        ((ContactsSecretChat) mContext).showNoSearchResults(constraint, mFilteredListData.size() == mOriginalListData.size());
                    } else if(mContext instanceof ActivityForwardMessage){
                        ((ActivityForwardMessage) mContext).showNoSearchResults(constraint, mFilteredListData.size() == mOriginalListData.size(), 1);
                    }


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