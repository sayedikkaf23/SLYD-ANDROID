package chat.hola.com.app.hastag;

import android.app.Activity;
import android.content.Context;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * <h2>Auto_complete_text_view</h2>
 * <p>
 * <p>
 * </P>
 *
 * @author 3Embed.
 * @since 3/10/2017.
 */
public class AutoCompleteTextView extends AppCompatMultiAutoCompleteTextView {
    private boolean isPeople_search = false, isNotifyied_required = true;
    private Context mcontext;
    private Activity mactivity;
    private ArrayList<Tag_pojo_item> response_list;
    private AutoComplete_SearchTagAdp data_adapter;
    private AutoTxtCallback callback;

    public AutoCompleteTextView(Context context) {
        super(context);
        mcontext = context;
        handel_Text_Listener();
        initialization_data_holder();
    }

    public void setListener(AutoTxtCallback callback) {
        this.callback = callback;
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        handel_Text_Listener();
        initialization_data_holder();
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mcontext = context;
        handel_Text_Listener();
        initialization_data_holder();
    }

    private void initialization_data_holder() {
        mactivity = (Activity) mcontext;
        setThreshold(1);
        response_list = new ArrayList<>();
        data_adapter = new AutoComplete_SearchTagAdp(mcontext, response_list);
        setAdapter(data_adapter);
    }


    private void handel_Text_Listener() {
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String text = charSequence.toString();

                if (text.lastIndexOf("@") > text.lastIndexOf(" ") && text.lastIndexOf("@") > text.lastIndexOf("#")) {
                    String tag = text.substring(text.lastIndexOf("@"), text.length());
                    tag = tag.replace("@", "");
                    if (tag.length() > 0) {
                        isNotifyied_required = true;
                        getSearch_List(tag);
                    }

                } else if (text.lastIndexOf("#") > text.lastIndexOf(" ")) {
                    String hash_tag = text.substring(text.lastIndexOf("#"), text.length());
                    hash_tag = hash_tag.replace("#", "");
                    if (hash_tag.length() > 0) {
                        isNotifyied_required = true;
                        getHashTag_list(hash_tag);
                    }
                } else {
                    isNotifyied_required = false;
                    response_list.clear();
                    data_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * <h2>getHashTag_list</h2>
     * <p>
     * Searching for the hashTag list search.
     * </P>
     *
     * @param search_data contains the hash tag list to be searched.
     */
    private void getHashTag_list(String search_data) {
        if (callback != null)
            callback.onHashTag(search_data);
    }

    public void updateHashTagDetails(Hash_tag_people_pojo response_handler) {
        if (isNotifyied_required) {
            ArrayList<Hash_data> data = response_handler.getData();
            if (data.size() > 0) {
                ArrayList<Tag_pojo_item> temp_list = new ArrayList<>();
                Tag_pojo_item temp_data;
                for (Hash_data item : data) {
                    temp_data = new Tag_pojo_item();
                    temp_data.setHash_tag(true);
                    temp_data.setHashtag(item.getId());
                    if (item.getCount() == 1) {
                        temp_data.setCount(item.getCount() + " " + "public post");
                    } else {
                        temp_data.setCount(item.getCount() + " " + "public posts");
                    }
                    temp_list.add(temp_data);
                }

                response_list.clear();
                response_list.addAll(temp_list);
                mactivity.runOnUiThread(new Runnable() {

                    public void run() {
                        AutoCompleteTextView.this.setTokenizer(new User_has_tag_tokanizer());
                        AutoCompleteTextView.this.setAdapter(new AutoComplete_SearchTagAdp(mcontext, response_list));
                        data_adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    /**
     * <h2>getSearch_List</h2>
     * <p>
     * Searching for the user list search.
     * </P>
     *
     * @param user_name contains the user_name  to be searched.
     */
    private void getSearch_List(String user_name) {
        if (callback != null)
            callback.onUserSearch(user_name);
    }


    public void updateUserSearch(Hash_tag_people_pojo response_handler) {
        if (isNotifyied_required) {
            ArrayList<Hash_data> data = response_handler.getData();
            if (data.size() > 0) {
                ArrayList<Tag_pojo_item> temp_list = new ArrayList<>();
                Tag_pojo_item temp_data;
                for (Hash_data item : data) {
                    temp_data = new Tag_pojo_item();
                    temp_data.setHash_tag(false);
                    temp_data.setUsername(item.getUserName());
                    temp_data.setFullName(item.getFirstName() + " " + item.getLastName());
                    temp_data.setProfilePicUrl(item.getProfilePic());
                    temp_list.add(temp_data);
                }

                response_list.clear();
                response_list.addAll(temp_list);
                mactivity.runOnUiThread(new Runnable() {
                    public void run() {
                        AutoCompleteTextView.this.setTokenizer(new UserNameTokenizer());
                        AutoCompleteTextView.this.setAdapter(new AutoComplete_SearchTagAdp(mcontext, response_list));
                        data_adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public interface AutoTxtCallback {
        void onHashTag(String tag);

        void onUserSearch(String tag);

        void onClear();
    }
}
