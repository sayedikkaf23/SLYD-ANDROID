package chat.hola.com.app.profileScreen.discover.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;

public class InviteContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    private static final String TAG = InviteContactAdapter.class.getSimpleName();

    private ArrayList<Contact> contacts = new ArrayList<>();
    public ArrayList<Contact> contactsFiltered = new ArrayList<>();
    private Context context;
    //    private TypefaceManager typefaceManager;
    private FilterListener filterListener;
    private ClickListner clickListner;

    public InviteContactAdapter(Context context) {
        this.context = context;
//        this.typefaceManager = typefaceManager;
    }


    public void setData(ArrayList<Contact> contacts) {
        this.contacts = contacts;
        this.contactsFiltered = contacts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invite_row, parent, false);
        return new InviteContactAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        try {
            Contact contact = contactsFiltered.get(position);
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvRowTitle.setText(contact.getFirstName());
            holder.tvRowTime.setText(contact.getNumber());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListner.onUserSelected(contactsFiltered.get(position));
                }
            });

//            holder.tvInvite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                    sendIntent.setType("vnd.android-dir/mms-sms");
//                    String shareMessage = "\nCheck out " + context.getString(R.string.app_name) +
//                            " for your smartphone. Download it today from\n\n + " +
//                            "\"https://play.google.com/store/apps/details?id=\" + " +
//                            "BuildConfig.APPLICATION_ID +\"\\n\\n\"";
//                    // You can add extras to populate your own message and such as
//                    sendIntent.putExtra("sms_body", shareMessage);
//
//                    // then just startActivity with the intent.
//                    context.startActivity(sendIntent);
////                        clickListner.onUserSelected(position);
//                }
//            });
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return contactsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactsFiltered = contacts;
                } else {
                    charString = charString.replace(" ", "");
                    ArrayList<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contacts) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String fullName = row.getFullName().toLowerCase().replace(" ", "");
                        if ((row.getNumber() != null && row.getNumber()
                                .toLowerCase()
                                .contains(charString.toLowerCase().replace(" ", ""))) || row.getFirstName()
                                .toLowerCase()
                                .contains(charString.toLowerCase().replace(" ", "")) || fullName.contains(
                                charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactsFiltered = (ArrayList<Contact>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
                if (filterListener != null) {
                    filterListener.onFilter(contactsFiltered.size());
                }
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivRow)
        ImageView ivRow;
        @BindView(R.id.tvRowTitle)
        TextView tvRowTitle;
        @BindView(R.id.tvRowTime)
        TextView tvRowTime;
        @BindView(R.id.tvInvite)
        TextView tvInvite;
        @BindView(R.id.ivStarBadge)
        ImageView ivStarBadge;
        @BindView(R.id.tbFollow)
        ToggleButton tbFollow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void setListener(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    public interface ClickListner {
        void add(int position);

        void view(int position);

        void onUserSelected(Contact contact);

        void onFollow(String id, boolean checked, int position);
    }

    public interface FilterListener {
        void onFilter(int count);
    }
}
