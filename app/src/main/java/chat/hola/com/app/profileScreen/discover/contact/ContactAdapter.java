package chat.hola.com.app.profileScreen.discover.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;
import chat.hola.com.app.socialDetail.ItemAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * <h>youAdapter.class</h>
 * <p> This adapter class is used by {@link chat.hola.com.app.profileScreen.discover.DiscoverActivity}.</p>
 *
 * @author 3Embed
 * @since 02/03/18.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements Filterable {

  private static final String TAG = ContactAdapter.class.getSimpleName();

  private ArrayList<Contact> contacts = new ArrayList<>();
  public ArrayList<Contact> contactsFiltered = new ArrayList<>();
  private Context context;
  private TypefaceManager typefaceManager;
  private FilterListener filterListener;
  private ClickListner clickListner;

  @Inject
  public ContactAdapter(Context context , TypefaceManager typefaceManager) {
    this.context = context;
    this.typefaceManager = typefaceManager;
  }


  public void setData(ArrayList<Contact> contacts) {
    this.contacts = contacts;
    this.contactsFiltered = contacts;
    notifyDataSetChanged();
  }

  @Override
  public int getItemViewType(int position) {
    return contactsFiltered.get(position).getIsTitle() ? 0 : 1;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View itemView = viewType == 0 ? LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_title, parent, false)
        : LayoutInflater.from(parent.getContext()).inflate(R.layout.you_row_new, parent, false);
    return viewType == 0 ? new TitleViewHolder(itemView, typefaceManager)
        : new ContactAdapter.ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
    try {
      Contact contact = contactsFiltered.get(position);
      if (getItemViewType(position) == 0) {
        TitleViewHolder holder = (TitleViewHolder) viewHolder;
        holder.title.setText(contact.getTitle().toUpperCase());
      } else {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.ivStarBadge.setVisibility(contact.isStar() ? View.VISIBLE : View.GONE);
        if (contact.getProfilePic()!=null && contact.getProfilePic().length()>0) {
          Glide.with(context)
                  .load(contact.getProfilePic())
                  .asBitmap()
                  .signature(new StringSignature(
                          AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                  .centerCrop()
                  .placeholder(R.drawable.profile_one)
                  //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                  .into(new BitmapImageViewTarget(holder.ivRow) {
                    @Override
                    protected void setResource(Bitmap resource) {
                      super.setResource(resource);
                      RoundedBitmapDrawable circularBitmapDrawable =
                              RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                      circularBitmapDrawable.setCircular(true);
                      holder.ivRow.setImageDrawable(circularBitmapDrawable);
                    }
                  });
        }else {
        Utilities.setTextRoundDrawable(context, contact.getFirstName(),contact.getLastName(), holder.ivRow);
      }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            clickListner.onUserSelected(position);
          }
        });

        /* Message form Discovery Activity */
        holder.messageButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            String fullName =
                    CommonClass.createFullName(contact.getFirstName(), contact.getLastName());

            Intent intent = new Intent(context, ChatMessageScreen.class);
            intent.putExtra("receiverUid", contact.getId());
            intent.putExtra("receiverName", fullName);
            intent.putExtra("isStar", contact.isStar());

            String docId = AppController.getInstance().findDocumentIdOfReceiver(contact.getId(), "");

            if (docId.isEmpty()) {
              docId =
                      AppController.findDocumentIdOfReceiver(contact.getId(), Utilities.tsInGmt(), fullName,
                              contact.getProfilePic(), "", false, contact.getNumber(), "", false, true);
            }

            intent.putExtra("documentId", docId);
            intent.putExtra("receiverIdentifier", contact.getNumber());
            intent.putExtra("receiverImage", contact.getProfilePic());
            intent.putExtra("colorCode", AppController.getInstance().getColorCode(position % 19));
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            context.startActivity(intent);
          }
        });

        holder.tvAdd.setVisibility(View.GONE);

        try {

          holder.tbFollow.setVisibility(
              contact.getId().equals(AppController.getInstance().getUserId()) ? View.GONE
                  : View.GONE);

          boolean isPrivate = contact.get_private() == 1;
          boolean isChecked;

          switch (contact.getFollowStatus()) {
            case 0:
              //public - unfollow
              isPrivate = contact.get_private().equals("1");
              isChecked = false;
              break;
            case 1:
              //public - follow
              isPrivate = false;
              isChecked = true;
              break;
            case 2:
              //private - requested
              isPrivate = true;
              isChecked = true;
              break;
            case 3:
              //private - request
              isPrivate = true;
              isChecked = false;
              break;
            default:
              isChecked = false;
              break;
          }
          holder.tbFollow.setTextOn(context.getResources()
              .getString(isPrivate ? R.string.requested : R.string.following));
          holder.tbFollow.setTextOff(context.getResources().getString(R.string.follow));
          holder.tbFollow.setChecked(isChecked);
          holder.tbFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              clickListner.onFollow(contact.getId(), holder.tbFollow.isChecked(), position);
            }
          });

          //                boolean isPrivate = contact.get_private() == 1;
          //                switch (contact.getFriendStatusCode()) {
          //                    case 1:
          //                        // default
          //                        holder.tvAdd.setText(context.getResources().getString(R.string.add));
          //                        holder.tvAdd.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
          //                        break;
          //                    case 2:
          //                        // friend
          //                        holder.tvAdd.setText(context.getResources().getString(R.string.view));
          //                        holder.tvAdd.setBackground(context.getResources().getDrawable(R.drawable.border_gray));
          //                        break;
          //                    case 3:
          //                        // requested
          //                        holder.tvAdd.setText(context.getResources().getString(R.string.requested));
          //                        holder.tvAdd.setBackgroundColor(context.getResources().getColor(R.color.color_white));
          //                        break;
          //                }
          //
          //                holder.tvAdd.setOnClickListener(new View.OnClickListener() {
          //                    @Override
          //                    public void onClick(View v) {
          //                        switch (contact.getFriendStatusCode()) {
          //                            case 1:
          //                                // default
          //                                clickListner.add(position);
          //                                holder.tvAdd.setText(context.getResources().getString(isPrivate ? R.string.requested : R.string.view));
          //                                break;
          //                            case 2:
          //                                // friend
          //                                clickListner.view(position);
          //                                break;
          //                        }
          //                    }
          //                });
          holder.tvRowTime.setText(contact.getUserName());

          String fullName = "";
          String fname = contact.getFirstName();
          String lname = contact.getLastName();

          if (fname != null && !fname.isEmpty()) {
            fullName = (fname.substring(0, 1).toUpperCase() + fname.substring(1));
          }
          if (lname != null && !lname.isEmpty()) {
            fullName = fullName + " " + (lname.substring(0, 1).toUpperCase() + lname.substring(1));
          }
          holder.tvRowTitle.setText(fullName);
        } catch (Exception ignored) {
          ignored.printStackTrace();
        }
        //            holder.ivRow.setOnClickListener(v -> clickListner.onUserSelected(position));
      }
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
            if ((row.getUserName() != null && row.getUserName()
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
        filterListener.onFilter(contactsFiltered.size());
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
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    @BindView(R.id.ivStarBadge)
    ImageView ivStarBadge;
    @BindView(R.id.tbFollow)
    ToggleButton tbFollow;
    @BindView(R.id.messageButton)
    Button messageButton;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      tvRowTitle.setTypeface(typefaceManager.getSemiboldFont());
      tvRowTime.setTypeface(typefaceManager.getRegularFont());
      tvAdd.setTypeface(typefaceManager.getSemiboldFont());
      tbFollow.setTypeface(typefaceManager.getRegularFont());
      messageButton.setVisibility(View.VISIBLE);
    }
  }


  public class TitleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView title;

    public TitleViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      title.setTypeface(typefaceManager.getBoldFont());
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

    void onUserSelected(int position);

    void onFollow(String id, boolean checked, int position);
  }

  public interface FilterListener {
    void onFilter(int count);
  }
}
