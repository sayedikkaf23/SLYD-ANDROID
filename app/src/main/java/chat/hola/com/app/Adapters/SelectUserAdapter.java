package chat.hola.com.app.Adapters;

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
import java.util.ArrayList;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ModelClasses.SelectUserItem;
import com.ezcall.android.R;
import chat.hola.com.app.Utilities.TextDrawable;
import chat.hola.com.app.ViewHolders.ViewHolderSelectUser;

/**
 * Created by moda on 19/06/17.
 */

public class SelectUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SelectUserItem> mFilteredListData = new ArrayList<>();


    private Context mContext;


    public SelectUserAdapter(Context mContext, ArrayList<SelectUserItem> mListData) {
        this.mFilteredListData = mListData;
        this.mContext = mContext;
    }


    @Override
    public int getItemCount() {
        return this.mFilteredListData.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


        View v = inflater.inflate(R.layout.select_user_item, viewGroup, false);
        viewHolder = new ViewHolderSelectUser(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        ViewHolderSelectUser vh2 = (ViewHolderSelectUser) viewHolder;
        configureViewHolderSelectUser(vh2, position);

    }
    @SuppressWarnings("TryWithIdenticalCatches")

    private void configureViewHolderSelectUser(final ViewHolderSelectUser vh, int position) {


        final SelectUserItem chat = mFilteredListData.get(position);


        vh.userName.setText(chat.getUserName());


        vh.email.setText(chat.getUserIdentifier());

        try {
            if (chat.getUserImage() != null && !chat.getUserImage().isEmpty()) {
                try{
                    Glide.with(mContext).load(chat.getUserImage()).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh.userImage) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh.userImage.setImageDrawable(circularBitmapDrawable);
                                }
                            });


                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }



            } else {
                vh.userImage.setImageDrawable(TextDrawable.builder()


                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(24 * (int) mContext.getResources().getDisplayMetrics().density) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()


                        .buildRound((chat.getUserName().trim()).charAt(0) + "", Color.parseColor(AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}