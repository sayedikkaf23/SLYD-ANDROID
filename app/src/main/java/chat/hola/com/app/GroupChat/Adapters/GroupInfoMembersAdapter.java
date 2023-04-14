package chat.hola.com.app.GroupChat.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.GroupChat.ModelClasses.GroupInfoMemberItem;
import chat.hola.com.app.GroupChat.ViewHolders.ViewHolderGroupMember;
import chat.hola.com.app.Utilities.TextDrawable;

/**
 * Created by moda on 22/09/17.
 */

public class GroupInfoMembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<GroupInfoMemberItem> mListData = new ArrayList<>();


    private Context mContext;


    private int density;


    /**
     * @param mContext  Context
     * @param mListData ArrayList<GroupInfoMemberItem>
     */
    public GroupInfoMembersAdapter(Context mContext, ArrayList<GroupInfoMemberItem> mListData) {


        this.mListData = mListData;


        this.mContext = mContext;


        density = (int) mContext.getResources().getDisplayMetrics().density;

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


        View v1 = inflater.inflate(R.layout.gc_member_item, viewGroup, false);
        viewHolder = new ViewHolderGroupMember(v1);

        return viewHolder;
    }


    /**
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   item position
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        ViewHolderGroupMember vh2 = (ViewHolderGroupMember) viewHolder;

        configureViewHolderGroupMember(vh2, position);


    }


    /**
     * @param vh       ViewHolderGroupMember
     * @param position item position
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderGroupMember(final ViewHolderGroupMember vh, int position) {
        final GroupInfoMemberItem groupInfoMemberItem = mListData.get(position);
        if (groupInfoMemberItem != null) {


            vh.contactName.setText(groupInfoMemberItem.getContactName());


            vh.contactStatus.setText(groupInfoMemberItem.getContactStatus());


            if (groupInfoMemberItem.isAdmin()) {
                vh.admin_rl.setVisibility(View.VISIBLE);
            } else {
                vh.admin_rl.setVisibility(View.GONE);
            }


            if (groupInfoMemberItem.isStar()) {
                vh.iV_star.setVisibility(View.VISIBLE);
            } else {
                vh.iV_star.setVisibility(View.GONE);
            }


            if (groupInfoMemberItem.getContactImage() != null && !groupInfoMemberItem.getContactImage().isEmpty()) {

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