package chat.hola.com.app.home.social;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chat.hola.com.app.AppController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.List;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.bumptech.glide.Glide;

import java.util.List;

import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.RoundedImageView;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.contact.Friend;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 9/3/2018.
 */
public class FriendItemAdapter extends RecyclerView.Adapter<FriendItemAdapter.ViewHolder> {
    //    implements Filterable
    private List<Friend> mItems;
    private ItemListener mListener;
    private Context context;
    private TypefaceManager typefaceManager;

    FriendItemAdapter(Context context, List<Friend> items, ItemListener listener, TypefaceManager typefaceManager) {
        this.context = context;
        mItems = items;
        mListener = listener;
        this.typefaceManager = typefaceManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_bottom_sheet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.tvUserName.setTypeface(typefaceManager.getSemiboldFont());
            holder.tvName.setTypeface(typefaceManager.getRegularFont());
            holder.tvUserName.setText(mItems.get(position).getUserName());
            String fullName = mItems.get(position).getFirstName();
            if (mItems.get(position).getLastName() != null && !mItems.get(position).getLastName().isEmpty())
                fullName = fullName + " " + mItems.get(position).getLastName();

            holder.tvName.setText(fullName);
            String profilepic = Constants.DEFAULT_PROFILE_PIC_LINK;
            if (mItems.get(position).getProfilePic() != null | mItems.get(position).getProfilePic().isEmpty())
                profilepic = mItems.get(position).getProfilePic();
            Glide.with(context).load(profilepic).asBitmap().signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop().into(holder.ivProfilePic);

            holder.tbSend.setEnabled(true);
            holder.tbSend.setOnCheckedChangeListener(null);
            holder.tbSend.setEnabled(!mItems.get(position).isSent());
            holder.tbSend.setText(mItems.get(position).isSent() ? "Sent" : "Send");

            holder.tbSend.setOnCheckedChangeListener((compoundButton, b) -> {
                mItems.get(position).setSent(b);
                if (b)
                    holder.tbSend.setEnabled(false);
            });
        } catch (Exception ignored) {

        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setData(List<Friend> tempFriends) {
        this.mItems = tempFriends;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        TextView tvUserName;
        RoundedImageView ivProfilePic;
        ToggleButton tbSend;
//        Button tbSend;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            ivProfilePic = (RoundedImageView) itemView.findViewById(R.id.ivProfilePic);
            tbSend = (ToggleButton) itemView.findViewById(R.id.tbSend);
//            tbSend = (Button) itemView.findViewById(R.id.tbSend);
            tbSend.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
//                tbSend.setText("Sent");
//                mItems.get(getAdapterPosition()).setSent(true);
                mListener.onItemClick(mItems.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

    interface ItemListener {
        void onItemClick(Friend data, int position);
    }
}