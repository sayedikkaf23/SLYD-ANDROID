package chat.hola.com.app.GroupChat.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ForwardMessage.ViewHolderForwardToContact;
import chat.hola.com.app.GroupChat.Activities.AddMember;
import chat.hola.com.app.GroupChat.ModelClasses.AddMemberItem;
import chat.hola.com.app.GroupChat.ViewHolders.ViewHolderAddMember;
import chat.hola.com.app.Utilities.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.ArrayList;

/**
 * Created by moda on 26/09/17.
 */

public class AddMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements Filterable {

  private ArrayList<AddMemberItem> mOriginalListData = new ArrayList<>();
  private ArrayList<AddMemberItem> mFilteredListData;

  private Context mContext;

  private int density;

  private final int ALREADY_MEMBER = 0;

  private final int ADD_MEMBER = 1;

  private int colorGray, colorBlack, colorLightGray;

  /**
   * @param mContext Context
   * @param mListData ArrayList<AddMemberItem>
   */
  public AddMemberAdapter(Context mContext, ArrayList<AddMemberItem> mListData) {

    this.mOriginalListData = mListData;

    this.mFilteredListData = mListData;
    this.mContext = mContext;

    density = (int) mContext.getResources().getDisplayMetrics().density;
    colorGray = ContextCompat.getColor(mContext, R.color.color_text_search);

    colorLightGray = ContextCompat.getColor(mContext, R.color.direct_message);
    colorBlack = ContextCompat.getColor(mContext, R.color.color_text_black);
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

    if (mFilteredListData.get(position).isSelected()) {
      /*
       * Already member
       */

      return ALREADY_MEMBER;
    } else {

      return ADD_MEMBER;
    }
  }

  /**
   * @param viewGroup ViewGroup
   * @param viewType item viewType
   * @return RecyclerView.ViewHolder
   */

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

    RecyclerView.ViewHolder viewHolder;
    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

    View v1;
    switch (viewType) {
      case ALREADY_MEMBER:
        v1 = inflater.inflate(R.layout.gc_add_member_item, viewGroup, false);
        viewHolder = new ViewHolderAddMember(v1);

        break;

      default:
        v1 = inflater.inflate(R.layout.forward_contact_item, viewGroup, false);
        viewHolder = new ViewHolderForwardToContact(v1);
    }

    return viewHolder;
  }

  /**
   * @param viewHolder RecyclerView.ViewHolder
   * @param position item position
   */

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    switch (viewHolder.getItemViewType()) {

      case ALREADY_MEMBER:
        ViewHolderAddMember vh2 = (ViewHolderAddMember) viewHolder;
        configureViewHolderAddMember(vh2, position);

        break;

      case ADD_MEMBER:
        ViewHolderForwardToContact vh3 = (ViewHolderForwardToContact) viewHolder;
        configureViewHolderForwardToContact(vh3, position);
        break;
    }
  }

  /**
   * @param vh ViewHolderAddMember
   * @param position item position
   */

  @SuppressWarnings("TryWithIdenticalCatches")
  private void configureViewHolderAddMember(final ViewHolderAddMember vh, int position) {
    final AddMemberItem addMemberItem = mFilteredListData.get(position);
    if (addMemberItem != null) {

      vh.contactName.setText(addMemberItem.getContactName());

      vh.contactName.setTextColor(colorGray);

      if (addMemberItem.isStar()) {
        vh.iV_star.setVisibility(View.VISIBLE);
      } else {
        vh.iV_star.setVisibility(View.GONE);
      }
      vh.contactStatus.setTextColor(colorLightGray);
      if (addMemberItem.getContactImage() != null && !addMemberItem.getContactImage().isEmpty()) {

        try {

          Glide.with(mContext)
              .load(addMemberItem.getContactImage())
              .asBitmap()

              .centerCrop()
              .placeholder(R.drawable.chat_attachment_profile_default_image_frame)
              //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
              .signature(new StringSignature(
                  AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
              .into(new BitmapImageViewTarget(vh.contactImage) {
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

              .buildRound((addMemberItem.getContactName().trim()).charAt(0) + "", Color.parseColor(
                  AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
        } catch (Exception e) {

          vh.contactImage.setImageDrawable(TextDrawable.builder()

              .beginConfig()
              .textColor(Color.WHITE)
              .useFont(Typeface.DEFAULT)
              .fontSize(24 * density) /* size in px */
              .bold()
              .toUpperCase()
              .endConfig()

              .buildRound("C", Color.parseColor(
                  AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
        }
      }
    }
  }

  /**
   * @param vh ViewHolderForwardToContact
   * @param position item position
   */

  @SuppressWarnings("TryWithIdenticalCatches")
  private void configureViewHolderForwardToContact(final ViewHolderForwardToContact vh,
      int position) {
    final AddMemberItem addMemberItem = mFilteredListData.get(position);
    if (addMemberItem != null) {

      vh.contactName.setText(addMemberItem.getContactName());

      vh.contactStatus.setText(addMemberItem.getContactStatus());

      vh.contactName.setTextColor(colorBlack);

      vh.contactStatus.setTextColor(colorGray);

      vh.contactSelected.setVisibility(View.GONE);

      if (addMemberItem.isStar()) {
        vh.iV_star.setVisibility(View.VISIBLE);
      } else {
        vh.iV_star.setVisibility(View.GONE);
      }
      if (addMemberItem.getContactImage() != null && !addMemberItem.getContactImage().isEmpty()) {

        try {
          Glide.with(mContext).load(addMemberItem.getContactImage()).asBitmap()

              .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame)
              //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
              .signature(new StringSignature(
                  AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
              .into(new BitmapImageViewTarget(vh.contactImage) {
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

              .buildRound((addMemberItem.getContactName().trim()).charAt(0) + "", Color.parseColor(
                  AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
        } catch (Exception e) {

          vh.contactImage.setImageDrawable(TextDrawable.builder()

              .beginConfig()
              .textColor(Color.WHITE)
              .useFont(Typeface.DEFAULT)
              .fontSize(24 * density) /* size in px */
              .bold()
              .toUpperCase()
              .endConfig()

              .buildRound("C", Color.parseColor(
                  AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
        }
      }
    }
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
        mFilteredListData = (ArrayList<AddMemberItem>) results.values;
        AddMemberAdapter.this.notifyDataSetChanged();
      }

      @Override
      protected FilterResults performFiltering(CharSequence constraint) {
        ArrayList<AddMemberItem> filteredResults;
        if (constraint.length() == 0) {
          filteredResults = mOriginalListData;
        } else {
          filteredResults = getFilteredResults(constraint.toString().toLowerCase());
        }
        mFilteredListData = filteredResults;
        if (mContext != null) {
          ((AddMember) mContext).showNoSearchResults(constraint,
              mFilteredListData.size() == mOriginalListData.size());
        }
        FilterResults results = new FilterResults();
        results.values = filteredResults;

        return results;
      }
    };
  }

  /**
   * @param constraint query to search for
   * @return ArrayList<Forward_ContactItem>
   */

  private ArrayList<AddMemberItem> getFilteredResults(String constraint) {
    ArrayList<AddMemberItem> results = new ArrayList<>();

    for (AddMemberItem item : mOriginalListData) {
      if ((item).getContactName().toLowerCase().contains(constraint)) {
        results.add(item);
      }
    }
    return results;
  }

  /**
   * @return ArrayList<ContactsItem>
   */

  public ArrayList<AddMemberItem> getList() {

    return mFilteredListData;
  }
}