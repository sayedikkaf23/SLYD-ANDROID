package chat.hola.com.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Activities.ContactsList;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ModelClasses.ContactsItem;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TextDrawable;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ViewHolders.ViewHolderContact;
import chat.hola.com.app.home.connect.ContactActivity;
import chat.hola.com.app.home.connect.ContactsFragment;

public class ContactsInviteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private ArrayList<ContactsItem> mOriginalListData = new ArrayList<>();
    private ArrayList<ContactsItem> mFilteredListData;

    private ClickListner clickListner;
    private Context mContext;
    public char settLetter = 'A';
    public Boolean setFlag = true;
    private int density;

    private ContactsFragment fragment;

    /**
     * @param mContext  Context
     * @param mListData ArrayList<ContactsItem>
     */
    public ContactsInviteAdapter(Context mContext, ArrayList<ContactsItem> mListData, ContactsFragment fragment) {


        this.fragment = fragment;
        this.mOriginalListData = mListData;
        this.mFilteredListData = mListData;
        this.mContext = mContext;


        density = (int) mContext.getResources().getDisplayMetrics().density;
        //   tf = AppController.getInstance().getRobotoCondensedFont();

    }

    public void setListener(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    /**
     * @return number of items
     */
    @Override
    public int getItemCount() {
        return mFilteredListData.size();
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


        View v1 = inflater.inflate(R.layout.contact_item, viewGroup, false);
        viewHolder = new ViewHolderContact(v1);

        return viewHolder;
    }


    /**
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   item position
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        ViewHolderContact vh2 = (ViewHolderContact) viewHolder;

        configureViewHolderContact(vh2, position);


    }


    /**
     * @param vh       ViewHolderContact
     * @param position item position
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderContact(final ViewHolderContact vh, int position) {

        final ContactsItem contactsItem = mFilteredListData.get(position);
        try {
            Log.v("contactsItem", String.valueOf(position) + ":" + contactsItem.getContactName() + ":" + contactsItem.getType() + ":");
            if (contactsItem != null) {

                if (!contactsItem.getInvite()) {
                } else {

                    if (!contactsItem.getInvite()) {
                        // hide invite
                        vh.btn_invite.setVisibility(View.GONE);
                    } else {
                        // show invite
                        vh.btn_invite.setVisibility(View.VISIBLE);
                    }

//            vh.contactName.setTypeface(tf, Typeface.NORMAL);
//            vh.contactStatus.setTypeface(tf, Typeface.NORMAL);


                    if (contactsItem.isSelected()) {
                        vh.contactStatus.setText(mContext.getString(R.string.dashboardAddedCall));//todo not able to use getString()
                        vh.frame.setAlpha(0.5f);
                    }

                    vh.contactName.setText(contactsItem.getContactName());
                    String contactName = contactsItem.getContactName().toUpperCase();

                    if (contactName.length() > 0) {
                        char firstLetter = contactName.charAt(0);
                        //   settLetter=firstLetter;
                        if (settLetter != firstLetter) {
                            setFlag = true;
                        }
                        if (setFlag) {
                            vh.contactnameIndicatorTv.setText(mContext.getString(R.string.double_inverted_comma) + firstLetter);
                            setFlag = false;
                            settLetter = firstLetter;
                        } else {
                            //settLetter=firstLetter;
                            vh.contactnameIndicatorTv.setText(mContext.getString(R.string.space));//todo not able to use getString()

                        }
                    }
                    vh.contactStatus.setText(contactsItem.getContactIdentifier());
                    //vh.contactStatus.setText(contactsItem.getContactStatus());
                    if (contactsItem.getContactImage() != null && !contactsItem.getContactImage().isEmpty()) {
                        try {

                            Glide.with(mContext).load(contactsItem.getContactImage()).asBitmap()
                                    .signature(new StringSignature(
                                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                                    .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                                    into(new BitmapImageViewTarget(vh.contactImage) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
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
                            if (contactsItem.getContactName() != null && contactsItem.getContactName().trim().length() > 0) {
                                vh.contactImage.setImageDrawable(TextDrawable.builder()
                                        .beginConfig()
                                        .textColor(Color.WHITE)
                                        .useFont(Typeface.DEFAULT)
                                        .fontSize(24 * density) /* size in px */
                                        .bold()
                                        .toUpperCase()
                                        .endConfig()
                                        .buildRound((contactsItem.getContactName().trim()).charAt(0) + "", Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
                            } else {
                                vh.contactImage.setImageDrawable(TextDrawable.builder()
                                        .beginConfig()
                                        .textColor(Color.WHITE)
                                        .useFont(Typeface.DEFAULT)
                                        .fontSize(24 * density) /* size in px */
                                        .bold()
                                        .toUpperCase()
                                        .endConfig()
                                        .buildRound("T" + "", Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
                            }

                        } catch (NullPointerException e) {
                            vh.contactImage.setImageDrawable(TextDrawable.builder()


                                    .beginConfig()
                                    .textColor(Color.WHITE)
                                    .useFont(Typeface.DEFAULT)
                                    .fontSize(24 * density) /* size in px */
                                    .bold()
                                    .toUpperCase()
                                    .endConfig()


                                    .buildRound("C", Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
                        } catch (IndexOutOfBoundsException e) {
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

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ContactsItem item = contactsItem;

                if (!contactsItem.getInvite()) {

                    if (item.isChatEnable()) {

                        Intent intent = new Intent(mContext.getApplicationContext(), ChatMessageScreen.class);
                        intent.putExtra("receiverUid", item.getContactUid());
                        intent.putExtra("receiverName", item.getContactName());

                        String docId =
                                AppController.getInstance().findDocumentIdOfReceiver(item.getContactUid(), "");

                        if (docId.isEmpty()) {
                            docId = AppController.findDocumentIdOfReceiver(item.getContactUid(),
                                    Utilities.tsInGmt(), item.getContactName(), item.getContactImage(), "", false,
                                    item.getContactIdentifier(), "", false, item.getStar());
                        }

                        intent.putExtra("documentId", docId);
                        intent.putExtra("receiverIdentifier", item.getContactIdentifier());
                        intent.putExtra("receiverImage", item.getContactImage());
                        intent.putExtra("isStar", item.getStar());
                        intent.putExtra("colorCode", AppController.getInstance().getColorCode(position % 19));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "User has disable their chat", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //nothing doing

                    String url = Constants.DYNAMIC_LINK + "/" + Constants.DYNAMIC_LINK_PROFILE;
                    String shareMessage = "Check out " + "Syld" + " for your smartphone. Download it today from\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + "com.ezcall.app" + "\n" + url;
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(shareIntent);
                }
            }
        });

        vh.btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                clickListner.onInviteButton(contactsItem);
                String url = Constants.DYNAMIC_LINK + "/" + Constants.DYNAMIC_LINK_PROFILE;
                String shareMessage = "Check out " + "Syld" + " for your smartphone. Download it today from\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + "com.ezcall.app" + "\n" + url;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(shareIntent);
            }
        });
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
                mFilteredListData = (ArrayList<ContactsItem>) results.values;
                ContactsInviteAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<ContactsItem> filteredResults;
                if (constraint.length() == 0) {
                    filteredResults = mOriginalListData;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());


                }
                mFilteredListData = filteredResults;
                if (fragment != null) {
                    fragment.showNoSearchResults(constraint, mFilteredListData.size() == mOriginalListData.size());
                } else if (mContext instanceof ContactActivity) {
                    ((ContactActivity) mContext).showNoSearchResults(constraint, mFilteredListData.size() == mOriginalListData.size());
                } else if (mContext instanceof ContactsList) {
                    ((ContactsList) mContext).showNoSearchResults(constraint, mFilteredListData.size() == mOriginalListData.size());
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }


    /**
     * @param constraint query to search for
     * @return ArrayList<ContactsItem>
     */

    private ArrayList<ContactsItem> getFilteredResults(String constraint) {
        ArrayList<ContactsItem> results = new ArrayList<>();

        for (ContactsItem item : mOriginalListData) {
            if ((item).getContactName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    /**
     * @return ArrayList<ContactsItem>
     */

    public ArrayList<ContactsItem> getList() {


        return mFilteredListData;
    }

    public interface ClickListner {

        void onInviteButton(ContactsItem contact);

    }
}

