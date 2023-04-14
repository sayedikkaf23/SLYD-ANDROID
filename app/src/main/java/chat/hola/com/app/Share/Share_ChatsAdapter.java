package chat.hola.com.app.Share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ModelClasses.ChatlistItem;
import chat.hola.com.app.Utilities.TextDrawable;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ViewHolders.ViewHolderChatlist;

/**
 * Created by moda on 13/10/17.
 */

public class Share_ChatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private ArrayList<ChatlistItem> mOriginalListData = new ArrayList<>();

    private ArrayList<ChatlistItem> mFilteredListData;

    private Context mContext;


    private int density;

//    private Typeface tf;


    /**
     * @param mContext  Context
     * @param mListData ArrayList<ChatlistItem>
     */
    public Share_ChatsAdapter(Context mContext, ArrayList<ChatlistItem> mListData) {


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


        View v = inflater.inflate(R.layout.chatlist_item, viewGroup, false);
        viewHolder = new ViewHolderChatlist(v);

        return viewHolder;
    }


    /**
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   item position
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        ViewHolderChatlist vh2 = (ViewHolderChatlist) viewHolder;
        configureViewHolderChatlist(vh2, position);

    }


    /**
     * @param vh       ViewHolderChatlist
     * @param position item position
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderChatlist(final ViewHolderChatlist vh, int position) {
        final ChatlistItem chat = mFilteredListData.get(position);
        if (chat != null) {


//            vh.newMessageCount.setTypeface(tf, Typeface.BOLD);
//            vh.newMessageDate.setTypeface(tf, Typeface.NORMAL);
//            vh.newMessageTime.setTypeface(tf, Typeface.NORMAL);
//            vh.newMessage.setTypeface(tf, Typeface.NORMAL);


            if (chat.isSelected()) {
                vh.chatSelected.setVisibility(View.VISIBLE);


            } else {


                vh.chatSelected.setVisibility(View.GONE);
            }

            if (chat.isSecretChat()) {

                vh.lock.setVisibility(View.VISIBLE);

            } else {

                vh.lock.setVisibility(View.GONE);


            }
            vh.storeName.setText(chat.getReceiverName());


            vh.newMessage.setText(chat.getNewMessage());

            if(chat.isStar()){
                vh.iV_star.setVisibility(View.VISIBLE);
            }else {
                vh.iV_star.setVisibility(View.GONE);
            }
            try {

                String formatedDate = Utilities.formatDate(Utilities.tsFromGmt(chat.getNewMessageTime()));

                if ((chat.getNewMessageTime().substring(0, 8)).equals((Utilities.tsInGmt().substring(0, 8)))) {


                    vh.newMessageDate.setText(R.string.string_207);
                } else if ((Integer.parseInt((Utilities.tsInGmt().substring(0, 8))) - Integer.parseInt((chat.getNewMessageTime().substring(0, 8)))) == 1) {


                    vh.newMessageDate.setText(R.string.string_208);

                } else {
                    vh.newMessageDate.setText(formatedDate.substring(9, 24));

                }
                String last = convert24to12hourformat(formatedDate.substring(0, 9));

                vh.newMessageTime.setText(last);


                if (chat.hasNewMessage()) {


                    vh.newMessageCount.setText(chat.getNewMessageCount());

                    vh.rl.setVisibility(View.VISIBLE);
                } else {
                    vh.newMessageCount.setText("");
                    vh.rl.setVisibility(View.GONE);

                }


            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            if (chat.getReceiverImage() != null && !chat.getReceiverImage().isEmpty())


            {

                try{
                    Glide.with(mContext).load(chat.getReceiverImage()).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh.storeImage) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh.storeImage.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } else {

                try {
                    vh.storeImage.setImageDrawable(TextDrawable.builder()


                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(24 * density) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()


                            .buildRound((chat.getReceiverName().trim()).charAt(0) + "", Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
                } catch (IndexOutOfBoundsException e) {
                    vh.storeImage.setImageDrawable(TextDrawable.builder()


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


            if (chat.isShowTick()) {
                vh.tick.setVisibility(View.VISIBLE);

                if (chat.getTickStatus() == 0) {

                    vh.tick.clearColorFilter();
                    vh.tick.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.clock));


                } else if (chat.getTickStatus() == 1) {

                    vh.tick.clearColorFilter();
                    vh.tick.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_single_tick));


                } else if (chat.getTickStatus() == 2) {

                    vh.tick.clearColorFilter();
                    vh.tick.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_double_tick));


                } else if (chat.getTickStatus() == 3) {
                    vh.tick.setColorFilter(ContextCompat.getColor(mContext, R.color.chat_blue_tick), PorterDuff.Mode.SRC_IN);

                    vh.tick.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_double_tick));

                }


            } else {
                vh.tick.setVisibility(View.GONE);
            }

        }
    }


    /*
     *
     * to convert date from  24 hour format to the 12 hour format
     * */

    /**
     * @param d date in 24 hour format
     * @return date in 12 hour format
     */

    private String convert24to12hourformat(String d) {

        String datein12hour = null;

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.US);
            final Date dateObj = sdf.parse(d);


            datein12hour = new SimpleDateFormat("h:mm a", Locale.US).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }


        return datein12hour;

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
                mFilteredListData = (ArrayList<ChatlistItem>) results.values;
                Share_ChatsAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<ChatlistItem> filteredResults;
                if (constraint.length() == 0) {
                    filteredResults = mOriginalListData;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());


                }
                mFilteredListData = filteredResults;
                if (mContext != null) {
                    ((CustomShare) mContext).showNoSearchResults(constraint, mFilteredListData.size() == mOriginalListData.size(), 2);
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }


    /**
     * @param constraint query to search for
     * @return ArrayList<ChatlistItem>
     */

    private ArrayList<ChatlistItem> getFilteredResults(String constraint) {
        ArrayList<ChatlistItem> results = new ArrayList<>();

        for (ChatlistItem item : mOriginalListData) {
            if ((item).getReceiverName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }


    /**
     * @return ArrayList<ChatlistItem>
     */

    public ArrayList<ChatlistItem> getList() {


        return mFilteredListData;
    }


}
