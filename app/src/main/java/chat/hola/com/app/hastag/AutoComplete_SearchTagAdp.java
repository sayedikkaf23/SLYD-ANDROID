package chat.hola.com.app.hastag;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import chat.hola.com.app.AppController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>AutoComplete_SearchTagAdp</h2>
 * <p>
 * Loading the text data .
 * </P>
 *
 * @author 3Embed.
 * @since 14/2/17.
 */
public class AutoComplete_SearchTagAdp extends ArrayAdapter<Tag_pojo_item> {
  private List<Tag_pojo_item> tag_list;

  public AutoComplete_SearchTagAdp(Context context, List<Tag_pojo_item> temp_list) {
    super(context, 0, temp_list);
    this.tag_list = temp_list;
  }

  @Override
  public View getView(int position, View convertView, @NonNull ViewGroup parent) {
    Holder_class holder;
    if (convertView == null) {
      holder = new Holder_class();
      convertView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.tag_people_list_item, parent, false);
      holder.image = (ImageView) convertView.findViewById(R.id.profile_image_view);
      holder.full_name = (TextView) convertView.findViewById(R.id.full_name);
      holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
      holder.for_user_search = (RelativeLayout) convertView.findViewById(R.id.for_user_search);
      holder.hash_tag_Search = (RelativeLayout) convertView.findViewById(R.id.hash_tag_Search);
      holder.hash_tag = (TextView) convertView.findViewById(R.id.hash_tag);
      holder.count_data = (TextView) convertView.findViewById(R.id.count_data);
      convertView.setTag(holder);
    } else {
      holder = (Holder_class) convertView.getTag();
    }

    Tag_pojo_item data = tag_list.get(position);
    if (!data.isHash_tag()) {
      holder.hash_tag_Search.setVisibility(View.GONE);
      holder.for_user_search.setVisibility(View.VISIBLE);

      Glide.with(getContext()).load(data.getProfilePicUrl()).asBitmap().centerCrop()
          //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
          .signature(new StringSignature(
              AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
          //.diskCacheStrategy(DiskCacheStrategy.NONE)
          //.skipMemoryCache(true)
          .into(new BitmapImageViewTarget(holder.image) {
            @Override
            protected void setResource(Bitmap resource) {
              RoundedBitmapDrawable circularBitmapDrawable =
                  RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
              circularBitmapDrawable.setCircular(true);
              holder.image.setImageDrawable(circularBitmapDrawable);
            }
          });

      holder.full_name.setText(data.getFullName());
      holder.user_name.setText(data.getUsername());
    } else {
      holder.for_user_search.setVisibility(View.GONE);
      holder.hash_tag_Search.setVisibility(View.VISIBLE);
      holder.count_data.setText(data.getCount());
      holder.hash_tag.setText(data.getHashtag());
    }
    return convertView;
  }

  @NonNull
  @Override
  public Filter getFilter() {
    return new SearchPeopleTagFilter(this, tag_list);
  }

  /**
   * <h2>SearchPeopleTagFilter</h2>
   * <p>
   * <p>
   * </P>
   */
  private class SearchPeopleTagFilter extends Filter {
    AutoComplete_SearchTagAdp adapter;
    List<Tag_pojo_item> originalList;
    List<Tag_pojo_item> filteredList;

    SearchPeopleTagFilter(AutoComplete_SearchTagAdp adapter, List<Tag_pojo_item> originalList) {
      super();
      this.adapter = adapter;
      this.originalList = originalList;
      this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      filteredList.clear();
      final FilterResults results = new FilterResults();
      if (constraint == null || constraint.length() == 0) {
        filteredList.addAll(originalList);
      } else {
        String text = constraint.toString();
        for (Tag_pojo_item data : originalList) {
          if (data.isHash_tag()) {
            if (data.getHashtag().toLowerCase().contains(text)) {
              filteredList.add(data);
            }
          } else if (data.getUsername().toLowerCase().contains(text)) {
            filteredList.add(data);
          }
        }
      }
      results.values = filteredList;
      results.count = filteredList.size();
      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      try {
        adapter.tag_list.clear();
        adapter.tag_list.addAll((List) results.values);
        if (results.count > 0) {
          adapter.notifyDataSetChanged();
        } else {
          adapter.notifyDataSetInvalidated();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public int getCount() {
    return tag_list.size();
  }

  private class Holder_class {
    ImageView image;
    TextView full_name, hash_tag, count_data, user_name;
    RelativeLayout for_user_search, hash_tag_Search;
  }
}
