package chat.hola.com.app.Activities;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Adapters.ContactsAdapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ModelClasses.ContactsItem;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.SortContacts;

/**
 * Created by moda on 19/08/17.
 */

public class ContactsList extends AppCompatActivity {

    private ContactsAdapter mAdapter;


    private ArrayList<ContactsItem> mContactData = new ArrayList<>();
    private SearchView searchView;


    private Bus bus = AppController.getBus();
    private TextView messageCall, noMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        final ImageView backButton = (ImageView) findViewById(R.id.close);


        RecyclerView rv = (RecyclerView) findViewById(R.id.list_of_users);
        mAdapter = new ContactsAdapter(ContactsList.this, mContactData, null);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ContactsList.this);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        messageCall = (TextView) findViewById(R.id.userMessagechat);
        noMatch = (TextView) findViewById(R.id.noMatch);
        rv.setAdapter(mAdapter);
        addContacts();

        searchView = (SearchView) findViewById(R.id.search);

//        searchView.setVisibility(View.VISIBLE);
        searchView.setIconified(true);
        searchView.setBackgroundColor(ContextCompat.getColor(ContactsList.this, R.color.color_white));
        searchView.setIconifiedByDefault(true);
        searchView.clearFocus();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    backButton.setVisibility(View.INVISIBLE);
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.setIconifiedByDefault(true);
                searchView.setIconified(true);
                searchView.setQuery("", false);
                searchView.clearFocus();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.getFilter().filter(newText);

                return false;
            }
        });


        /*
         * Dont need it anymore in the present logic
         */

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noMatch.setVisibility(View.GONE);


                        if (mContactData.size() == 0) {


                            messageCall.setVisibility(View.VISIBLE);
                        }

                        backButton.setVisibility(View.VISIBLE);
                    }
                });


                return false;
            }
        });

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(com.google.android.material.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView title = (TextView) findViewById(R.id.title);


        Typeface tf = AppController.getInstance().getRegularFont();


        title.setText(getString(R.string.Contacts));
        title.setTypeface(tf, Typeface.BOLD);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        bus.register(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mContactData.clear();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mAdapter.notifyDataSetChanged();

            }
        });
        addContacts();
    }


    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(ContactsList.this, ChatMessageScreen.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("receiverUid", obj.getString("receiverUid"));
            intent.putExtra("receiverName", obj.getString("receiverName"));
            intent.putExtra("documentId", obj.getString("documentId"));
            intent.putExtra("isStar", obj.getBoolean("isStar"));
            intent.putExtra("receiverImage", obj.getString("receiverImage"));
            intent.putExtra("colorCode", obj.getString("colorCode"));

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            } else if (object.getString("eventName").equals(MqttEvents.UserUpdates.value + "/" + AppController.getInstance().getUserId())) {


                switch (object.getInt("type")) {


                    case 1:


                        /*
                         * Status update by any of the contact
                         */



                        /*
                         * To update in the contacts list
                         */

                        final int pos = findContactPositionInList(object.getString("userId"));
                        if (pos != -1) {


                            ContactsItem item = mContactData.get(pos);


                            item.setContactStatus(object.getString("socialStatus"));

                            mContactData.set(pos, item);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyItemChanged(pos);
                                }
                            });
                        }


                        break;

                    case 2:
                        /*
                         * Profile pic update
                         */


                        /*
                         * To update in the contacts list
                         */


                        final int pos1 = findContactPositionInList(object.getString("userId"));
                        if (pos1 != -1) {

                            /*
                             * Have to clear the glide cache
                             */
//                            Glide.get(getActivity()).clearDiskCache();
//
//                            Glide.get(getActivity()).clearMemory();
//                            Glide.get(getActivity()).getBitmapPool().clearMemory();
                            ContactsItem item = mContactData.get(pos1);


                            item.setContactImage(object.getString("profilePic"));
                            mContactData.set(pos1, item);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyItemChanged(pos1);
                                }
                            });
                        }


                        break;
                    case 3:
                        /*
                         * Any of mine previous phone contact join
                         */

                        ContactsItem item2 = new ContactsItem();
                        item2.setContactImage("");
                        item2.setContactUid(object.getString("userId"));


                        item2.setContactStatus(getString(R.string.default_status));

                        item2.setContactIdentifier(object.getString("number"));


                        item2.setContactName(object.getString("name"));
                        final int position = findContactPositionInList(object.getString("userId"));
                        if (position == -1) {


                            mContactData.add(item2);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyItemInserted(mContactData.size() - 1);
                                }
                            });


                        } else {
                            mContactData.set(position, item2);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyItemChanged(position);
                                }
                            });

                        }
                        break;

                    case 4:
                        /*
                         * New contact added request sent,for the response of the PUT contact api
                         */


                        switch (object.getInt("subtype")) {

                            case 0:

                                /*
                                 * Follow name or number changed but number still valid
                                 */


                                final int pos3 = findContactPositionInList(object.getString("contactUid"));


                                if (pos3 != -1) {


                                    ContactsItem item = mContactData.get(pos3);


                                    item.setContactName(object.getString("contactName"));
                                    mContactData.set(pos3, item);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyItemChanged(pos3);
                                        }
                                    });


                                }

                                break;
                            case 1:
                                /*
                                 * Number of active contact changed and new number not in contact
                                 */

                                final int pos4 = findContactPositionInList(object.getString("contactUid"));


                                if (pos4 != -1) {


                                    mContactData.remove(pos4);


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyItemChanged(pos4);
                                        }
                                    });
                                }

                                break;

                            case 2:

                                /*
                                 * New contact added
                                 */


                                try {
                                    ContactsItem item = new ContactsItem();

                                    item.setContactImage(object.getString("contactPicUrl"));
                                    item.setContactName(object.getString("contactName"));
                                    item.setContactStatus(object.getString("contactStatus"));
                                    item.setContactIdentifier(object.getString("contactIdentifier"));
                                    item.setContactUid(object.getString("contactUid"));
                                    mContactData.add(item);


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyItemInserted(mContactData.size() - 1);
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                break;
                        }


                        break;

                    case 5:
                        /*
                         * Follow deleted request sent,for the response of the DELETE contact api
                         */


                        /*
                         * Number was in active contact
                         */
                        if (object.has("status") && object.getInt("status") == 0) {

                            final int pos2 = findContactPositionInList(object.getString("userId"));

                            if (pos2 != -1) {


                                mContactData.remove(pos2);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        mAdapter.notifyItemChanged(pos2);
                                        //  mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                        break;


                }


            } else if (object.getString("eventName").equals("ContactNameUpdated")) {

                final int pos = findContactPositionInList(object.getString("contactUid"));


                ContactsItem item = mContactData.get(pos);


                item.setContactName(object.getString("contactName"));
                mContactData.set(pos, item);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }

        } catch (JSONException e)

        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

    @Override
    public void onBackPressed() {


        if (AppController.getInstance().isActiveOnACall()) {
            if (AppController.getInstance().isCallMinimized()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        } else {
            super.onBackPressed();
            supportFinishAfterTransition();
        }

    }


    public void showNoSearchResults(final CharSequence constraint, boolean flag) {

        try {
            if (flag) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noMatch.setVisibility(View.GONE);


                        if (mContactData.size() == 0) {


                            messageCall.setVisibility(View.VISIBLE);
                        } else {

                            messageCall.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (mAdapter.getList().size() == 0) {
                            if (noMatch != null) {


                                noMatch.setVisibility(View.VISIBLE);

                                noMatch.setText(getString(R.string.noMatch) + getString(R.string.space) + constraint);
                                messageCall.setVisibility(View.GONE);

                            }
                        } else {
                            noMatch.setVisibility(View.GONE);


                        }
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private void addContacts() {


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mContactData.clear();
                mAdapter.notifyDataSetChanged();
            }
        });


        ArrayList<Map<String, Object>> contacts = AppController.getInstance().getDbController().loadContacts(AppController.getInstance().getContactsDocId());

        ContactsItem contact;
        Map<String, Object> contactIthPosition;


        if (mContactData != null) {

            for (int i = 0; i < contacts.size(); i++) {


                contact = new ContactsItem();


                contactIthPosition = contacts.get(i);

                if (!contactIthPosition.containsKey("blocked")) {
                    contact.setContactIdentifier((String) contactIthPosition.get("contactIdentifier"));

                    contact.setContactImage((String) contactIthPosition.get("contactPicUrl"));
                    contact.setContactName((String) contactIthPosition.get("contactName"));
                    contact.setContactStatus((String) contactIthPosition.get("contactStatus"));
                    contact.setContactUid((String) contactIthPosition.get("contactUid"));
                    mContactData.add(contact);

                }
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });


            if (mContactData.size() == 0) {
                messageCall.setVisibility(View.VISIBLE);

            } else if (mContactData.size() > 0) {
                messageCall.setVisibility(View.GONE);
                Collections.sort(mContactData, new SortContacts());
            }
        }

    }

    private int findContactPositionInList(String contactUid) {
        int pos = -1;

        for (int i = 0; i < mContactData.size(); i++) {


            if (mContactData.get(i).getContactUid().equals(contactUid)) {

                return i;
            }
        }

        return pos;
    }

}
