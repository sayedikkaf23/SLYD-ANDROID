package chat.hola.com.app.GroupChat.Adapters;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;

import chat.hola.com.app.AppController;
import chat.hola.com.app.GroupChat.ModelClasses.MemberMessageAckItem;
import chat.hola.com.app.GroupChat.ViewHolders.ViewHolderGroupMember;
import chat.hola.com.app.GroupChat.ViewHolders.ViewHolderMessageReadStatus;
import chat.hola.com.app.Utilities.TextDrawable;

/**
 * Created by moda on 05/10/17.
 */

public class MessageAcknowledgementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int DELIVERED = 1;

    private final int READ = 0;


    private ArrayList<MemberMessageAckItem> mListData = new ArrayList<>();


    private Context mContext;


    private int density;


    private String read, delivered;


    /**
     * @param mContext  Context
     * @param mListData ArrayList<MemberMessageAckItem>
     */
    public MessageAcknowledgementAdapter(Context mContext, ArrayList<MemberMessageAckItem> mListData) {


        this.mListData = mListData;


        this.mContext = mContext;


        density = (int) mContext.getResources().getDisplayMetrics().density;
        read = mContext.getString(R.string.Read)+"- ";
        delivered = mContext.getString(R.string.Delivered)+"- ";

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
        if (mListData.get(position).isReadMember()) {

            return READ;
        } else {


            return DELIVERED;
        }

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

        View v1;
        switch (viewType) {
            case DELIVERED:
                v1 = inflater.inflate(R.layout.gc_member_item, viewGroup, false);
                viewHolder = new ViewHolderGroupMember(v1);

                break;

            default:
                v1 = inflater.inflate(R.layout.gc_message_status_item, viewGroup, false);
                viewHolder = new ViewHolderMessageReadStatus(v1);


        }


        return viewHolder;
    }


    /**
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   item position
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        switch (viewHolder.getItemViewType()) {


            case DELIVERED:
                ViewHolderGroupMember vh2 = (ViewHolderGroupMember) viewHolder;

                configureViewHolderGroupMember(vh2, position);

                break;

            case READ:
                ViewHolderMessageReadStatus vh3 = (ViewHolderMessageReadStatus) viewHolder;

                configureViewHolderMessageReadStatus(vh3, position);
                break;

        }


    }


    /**
     * @param vh       ViewHolderGroupMember
     * @param position item position
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderGroupMember(final ViewHolderGroupMember vh, int position) {
        final MemberMessageAckItem groupInfoMemberItem = mListData.get(position);
        if (groupInfoMemberItem != null) {


            vh.contactName.setText(groupInfoMemberItem.getContactName());


            vh.contactStatus.setText(groupInfoMemberItem.getDeliveryTime());


            if (groupInfoMemberItem.getContactImage() != null && !groupInfoMemberItem.getContactImage().isEmpty())


            {

                try {
                    Glide.with(mContext).load(groupInfoMemberItem.getContactImage()).asBitmap()
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


                            .buildRound((groupInfoMemberItem.getContactName().trim()).charAt(0) + "", Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
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
     * @param vh       ViewHolderGroupMember
     * @param position item position
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderMessageReadStatus(final ViewHolderMessageReadStatus vh, int position) {
        final MemberMessageAckItem groupInfoMemberItem = mListData.get(position);
        if (groupInfoMemberItem != null) {


            vh.contactName.setText(groupInfoMemberItem.getContactName());


            vh.deliveredAt.setText(delivered + R.string.space + groupInfoMemberItem.getDeliveryTime());

            vh.readAt.setText(read + R.string.space + groupInfoMemberItem.getReadTime());


            if (groupInfoMemberItem.getContactImage() != null && !groupInfoMemberItem.getContactImage().isEmpty())


            {

                try {
                    Glide.with(mContext).load(groupInfoMemberItem.getContactImage()).asBitmap()
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


                            .buildRound((groupInfoMemberItem.getContactName().trim()).charAt(0) + "", Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
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


}