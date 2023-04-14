package chat.hola.com.app.live_stream.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;

import java.util.ArrayList;
import java.util.Random;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;
import chat.hola.com.app.live_stream.utility.TextDrawable;

/**
 * Created by moda on 12/24/2018.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<StreamChatMessage> areMessage;
    private Context mContext;
    private int density;
    int[] androidColors;

    public MessageAdapter(ArrayList<StreamChatMessage> areMessage, Context mContext) {
        this.areMessage = areMessage;
        this.mContext = mContext;
        androidColors= mContext.getResources().getIntArray(R.array.random);
        density = (int) mContext.getResources().getDisplayMetrics().density;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_item, viewGroup, false);
        return new ViewMessageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewMessageHolder holder = (ViewMessageHolder) viewHolder;

        StreamChatMessage message = areMessage.get(i);

//        holder.tvMessage.setText(message.getMessage());
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        String text = message.getUserName() + " " + message.getMessage();
        holder.tvMessage.setText(Utilities.colorized(text, message.getUserName(), randomAndroidColor), TextView.BufferType.SPANNABLE);

        if (message.getUserImage() != null && !message.getUserImage().isEmpty()) {

            try {
                Glide.with(mContext).load(message.getUserImage()).asBitmap().centerCrop()
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE)
                        //    .skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(holder.ivProfilePic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                //                GlideApp.with(mContext)
                //
                //                        .load(message.getUserImage())
                //                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                //                        .circleCrop()
                //                        .into(holder.ivProfilePic);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            try {
                holder.ivProfilePic.setImageDrawable(TextDrawable.builder()

                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(14 * density) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()

                        .buildRound((message.getUserName().trim()).charAt(0) + "",
                                ContextCompat.getColor(mContext, R.color.colorAccent)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return areMessage.size();
    }

    private class ViewMessageHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfilePic;
        private TextView tvMessage, tvName;

        private ViewMessageHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvName = itemView.findViewById(R.id.tvName);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }
    }
}
