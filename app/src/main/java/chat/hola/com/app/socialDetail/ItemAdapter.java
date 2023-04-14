package chat.hola.com.app.socialDetail;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import chat.hola.com.app.AppController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.RoundedImageView;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.home.HomeFragment;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 9/3/2018.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements Filterable {
    //    implements Filterable
    private List<Friend> mItems = new ArrayList<>();
    private List<Friend> filterList = new ArrayList<>();
    private ItemListener mListener;
    private Context context;
    private TypefaceManager typefaceManager;
    private int postPosition;

    public ItemAdapter(Context context, List<Friend> items, ItemListener listener, TypefaceManager typefaceManager) {
        this.context = context;
        if (items != null) {
            mItems = items;
            filterList = items;
        }
        mListener = listener;
        this.typefaceManager = typefaceManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_bottom_sheet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.tvUserName.setTypeface(typefaceManager.getSemiboldFont());
            holder.tvName.setTypeface(typefaceManager.getRegularFont());
            holder.tvUserName.setText(filterList.get(position).getUserName());
            String fullName = filterList.get(position).getFirstName();
            if (filterList.get(position).getLastName() != null && !filterList.get(position).getLastName().isEmpty())
                fullName = fullName + " " + filterList.get(position).getLastName();

            holder.tvName.setText(fullName);
            String profilepic = Constants.DEFAULT_PROFILE_PIC_LINK;
            if (filterList.get(position).getProfilePic() != null | filterList.get(position).getProfilePic().isEmpty())
                profilepic = filterList.get(position).getProfilePic();
            if(!TextUtils.isEmpty(profilepic)) {
                Glide.with(context)
                        .load(profilepic).asBitmap()
                        .signature(new StringSignature
                        (AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .centerCrop()
                        .into(new BitmapImageViewTarget(holder.ivProfilePic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                            }
                        });

            }else {
                Utilities.setTextRoundDrawable(context, filterList.get(position).getFirstName(), filterList.get(position).getLastName(), holder.ivProfilePic);
            }
            holder.tbSend.setChecked(filterList.get(position).isSent());
            holder.tbSend.setEnabled(true);
            holder.tbSend.setEnabled(!filterList.get(position).isSent());
            holder.tbSend.setText(filterList.get(position).isSent() ? "Sent" : "Send");

            holder.tbSend.setOnCheckedChangeListener((compoundButton, b) -> {
                filterList.get(position).setSent(b);
                if (b) {
                    if (mListener != null && filterList.get(position).isChatEnable()) {
                        holder.tbSend.setEnabled(false);
                        mListener.onItemClick(filterList.get(position), postPosition);
                    }else{
                        holder.tbSend.setChecked(false);
                        Toast.makeText(context,context.getString(R.string.chat_disable),Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception ignored) {

        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public void setPostItemClickPostion(int position) {
        this.postPosition = position;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        TextView tvUserName;
        ImageView ivProfilePic;
        ToggleButton tbSend;
//        Button tbSend;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            ivProfilePic =  itemView.findViewById(R.id.ivProfilePic);
//            tbSend = (Button) itemView.findViewById(R.id.tbSend);
            tbSend = (ToggleButton) itemView.findViewById(R.id.tbSend);
           // tbSend.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterList = mItems;
                } else {
                    List<Friend> filteredList = new ArrayList<>();
                    for (Friend row : filterList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFirstName().toLowerCase().contains(charString.toLowerCase()) || row.getLastName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    filterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (ArrayList<Friend>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
                mListener.onFilter(filterList.size());
            }
        };
    }

    public interface ItemListener {
        void onItemClick(Friend data,int position);
        void onFilter(int count);
    }
}
