package chat.hola.com.app.Share;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.DownloadFile.FileUploadService;
import chat.hola.com.app.DownloadFile.FileUtils;
import chat.hola.com.app.DownloadFile.ServiceGenerator;
import chat.hola.com.app.ForwardMessage.Forward_ContactItem;
import chat.hola.com.app.ForwardMessage.SortContactsToForward;
import chat.hola.com.app.ModelClasses.ChatlistItem;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.TimestampSorter;
import chat.hola.com.app.Utilities.Utilities;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moda on 13/10/17.
 */

public class CustomShare extends AppCompatActivity {


    private int count = 0;
    private RelativeLayout send_rl, root;


    private Bus bus = AppController.getBus();

    private ArrayList<Forward_ContactItem> mContactData = new ArrayList<>();


    private ArrayList<Forward_ContactItem> mCollapsedContactData = new ArrayList<>();


    private ArrayList<ChatlistItem> mChatData = new ArrayList<>();


    private SearchView searchView;
    private TextView messageCall;


    private TextView noContact, noChat;
    private Share_ContactsAdapter mAdapter;

    private Share_ChatsAdapter mAdapter2;

    private TextView selectedContacts, viewAllHide_tv;
    private int height = 0;

    private static final double MAX_VIDEO_SIZE = 26 * 1024 * 1024;
    private Intent intent;

    private static final int IMAGE_QUALITY = 50;//change it to higher level if want,but then slower image sending


    private ProgressDialog pDialog;


    private RelativeLayout chatsHeader, contactsHeader, viewAll_rl;


    private boolean hasAtleastOneChat = false, showingAllContacts = false;//, hasAtleastOneContact = false;
    private RecyclerView recyclerViewContacts;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_message);

        this.intent = getIntent();
        mContactData = new ArrayList<>();

        height = (int) (51 * getResources().getDisplayMetrics().density);

        /*
         * Initialization of the xml content.*/
        initializeXmlContent();

        bus.register(this);
        requestStoragePermission();
    }

    /*
     * Initilaization of all the xml content.*/
    private void initializeXmlContent() {

        root = (RelativeLayout) findViewById(R.id.root);


        chatsHeader = (RelativeLayout) findViewById(R.id.rl3);

        contactsHeader = (RelativeLayout) findViewById(R.id.rl2);

        viewAll_rl = (RelativeLayout) findViewById(R.id.rl6);
        viewAllHide_tv = (TextView) findViewById(R.id.viewAll_tv);


        recyclerViewContacts = (RecyclerView) findViewById(R.id.contacts_rv);
        recyclerViewContacts.setHasFixedSize(true);
        mAdapter = new Share_ContactsAdapter(CustomShare.this, mContactData);
        recyclerViewContacts.setItemAnimator(new DefaultItemAnimator());

        recyclerViewContacts.setLayoutManager(new CustomLinearLayoutManager(CustomShare.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewContacts.setAdapter(mAdapter);


        final RecyclerView recyclerViewChats = (RecyclerView) findViewById(R.id.chats_rv);
        recyclerViewChats.setHasFixedSize(true);
        mAdapter2 = new Share_ChatsAdapter(CustomShare.this, mChatData);
        recyclerViewChats.setItemAnimator(new DefaultItemAnimator());

        recyclerViewChats.setLayoutManager(new CustomLinearLayoutManager(CustomShare.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewChats.setAdapter(mAdapter2);


        ImageView sendButton = (ImageView) findViewById(R.id.send_iv);

        messageCall = (TextView) findViewById(R.id.userMessagechat);

        messageCall.setText(getString(R.string.share_contacts));

        // noMatch = (TextView) findViewById(R.id.noMatch);

        noChat = (TextView) findViewById(R.id.noChat);
        noContact = (TextView) findViewById(R.id.noContact);
        selectedContacts = (TextView) findViewById(R.id.selectedContacts);

        final ImageView close = (ImageView) findViewById(R.id.close);


        send_rl = (RelativeLayout) findViewById(R.id.rl);


        addChats();
        addContacts();

        recyclerViewContacts.addOnItemTouchListener(new RecyclerItemClickListener(CustomShare.this, recyclerViewContacts, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {


                    Forward_ContactItem item = mAdapter.getList().get(position);
                    if (item.isSelected()) {


                        item.setSelected(false);


                        count--;
                    } else {


                        item.setSelected(true);


                        count++;


                    }


                    int i = findPosition(item.getContactUid());
                    if (i != -1) {
                        mContactData.set(i, item);
                        mAdapter.notifyItemChanged(i);
                    }

                    if (count < 1) {
                        send_rl.setVisibility(View.GONE);

                        if (hasAtleastOneChat) {
                            recyclerViewChats.setPadding(0, 0, 0, 0);
                        } else {
                            recyclerViewContacts.setPadding(0, 0, 0, 0);
                        }


                    } else {
                        if (hasAtleastOneChat) {
                            recyclerViewChats.setPadding(0, 0, 0, height);
                        } else {
                            recyclerViewContacts.setPadding(0, 0, 0, height);
                        }

                        send_rl.setVisibility(View.VISIBLE);


                    }


                    updateSelectedContactsText();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));


        recyclerViewChats.addOnItemTouchListener(new RecyclerItemClickListener(CustomShare.this, recyclerViewChats,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (position >= 0) {


                            ChatlistItem item = mAdapter2.getList().get(position);
                            if (item.isSelected()) {


                                item.setSelected(false);


                                count--;
                            } else {


                                item.setSelected(true);


                                count++;


                            }


                            int i = findPositionChat(item.getReceiverUid(), item.getSecretId());
                            if (i != -1) {
                                mChatData.set(i, item);
                                mAdapter2.notifyItemChanged(i);
                            }

                            if (count < 1) {
                                send_rl.setVisibility(View.GONE);
                                if (hasAtleastOneChat) {
                                    recyclerViewChats.setPadding(0, 0, 0, 0);
                                } else {
                                    recyclerViewContacts.setPadding(0, 0, 0, 0);
                                }


                            } else {

                                if (hasAtleastOneChat) {
                                    recyclerViewChats.setPadding(0, 0, 0, height);
                                } else {
                                    recyclerViewContacts.setPadding(0, 0, 0, height);
                                }

                                send_rl.setVisibility(View.VISIBLE);


                            }


                            updateSelectedContactsText();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (intent != null) {


                    if (pDialog == null) {
                        pDialog = new ProgressDialog(CustomShare.this);
                        pDialog.setMessage(getString(R.string.Sharing));
                        pDialog.setCancelable(false);
                    }
                    pDialog.show();


                    handleIncomingIntent();


                }


            }
        });


        searchView = (SearchView) findViewById(R.id.search);

        searchView.setVisibility(View.VISIBLE);
        searchView.setIconified(true);
        searchView.setBackgroundColor(ContextCompat.getColor(CustomShare.this, R.color.color_white));
        searchView.setIconifiedByDefault(true);
        searchView.clearFocus();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    close.setVisibility(View.INVISIBLE);
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


                mAdapter2.getFilter().filter(newText);

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
                        //  noMatch.setVisibility(View.GONE);


                        noChat.setVisibility(View.GONE);
                        noContact.setVisibility(View.GONE);


                        if (mContactData.size() == 0 && mChatData.size() == 0) {


                            messageCall.setVisibility(View.VISIBLE);
                        }

                        close.setVisibility(View.VISIBLE);
                    }
                });


                return false;
            }
        });

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView title = (TextView) findViewById(R.id.title);


        Typeface tf = AppController.getInstance().getRegularFont();


        title.setText(getString(R.string.Share));
        title.setTypeface(tf, Typeface.BOLD);


        viewAll_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manipulateAdapterContents();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(CustomShare.this, ChatMessageScreen.class);

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


                            Forward_ContactItem item = mContactData.get(pos);


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
                            Forward_ContactItem item = mContactData.get(pos1);


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

                        Forward_ContactItem item2 = new Forward_ContactItem();
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


                                    Forward_ContactItem item = mContactData.get(pos3);


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
                                    Forward_ContactItem item = new Forward_ContactItem();

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


                    case 6: {

                        if (object.getBoolean("blocked")) {
                            removeFromContactsAndChats(object.getString("initiatorId"));

                        }
                        break;
                    }


                }


            } else if (object.getString("eventName").equals("ContactNameUpdated")) {

                final int pos = findContactPositionInList(object.getString("contactUid"));


                Forward_ContactItem item = mContactData.get(pos);


                item.setContactName(object.getString("contactName"));
                mContactData.set(pos, item);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });

            } else if (object.getString("eventName").equals(MqttEvents.GroupChats.value + "/" + AppController.getInstance().getUserId())) {

                /*
                 * To remove the current groupchat from list of possible chats into which message can be forwarded
                 */

                if (!object.has("payload")) {
                    /*
                     * To only check if the current member has been removed from some group
                     */


                    if (object.getInt("type") == 2) {
                        /*
                         * Member removed
                         */


                        String chatId = object.getString("groupId");


                        for (int i = 0; i < mChatData.size(); i++) {


                            if (mChatData.get(i).getReceiverUid().equals(chatId)) {


                                if (mChatData.get(i).isSelected()) {

                                    mChatData.remove(i);
                                    final int k = i;


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (k == 0) {


                                                mAdapter2.notifyDataSetChanged();
                                            } else {


                                                mAdapter2.notifyItemRemoved(k);

                                            }

                                        }
                                    });

                                    updateSelectedContactsText();


                                } else {


                                    mChatData.remove(i);
                                    final int k = i;


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (k == 0) {


                                                mAdapter2.notifyDataSetChanged();
                                            } else {


                                                mAdapter2.notifyItemRemoved(k);

                                            }

                                        }
                                    });

                                }
                            }


                        }


                    }


                }
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

    public void showNoSearchResults(final CharSequence constraint, boolean flag, final int type) {

        try {
            if (flag) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noChat.setVisibility(View.GONE);
                        noContact.setVisibility(View.GONE);

                        if (mContactData.size() == 0 && mChatData.size() == 0) {
                            messageCall.setText(getString(R.string.share_chats_contacts));

                            messageCall.setVisibility(View.VISIBLE);


                        } else {

                            messageCall.setVisibility(View.GONE);
                        }


//                        else if (type == 1) {
//                            if (mContactData.size() == 0) {
//
//                                messageCall.setText(getString(R.string.forward_contacts));
//                                messageCall.setVisibility(View.VISIBLE);
//
//
//                            } else {
//
//                                messageCall.setVisibility(View.GONE);
//                            }
//                        } else if (type == 2) {
//                            if (mChatData.size() == 0) {
//
//                                messageCall.setText(getString(R.string.forward_chats));
//                                messageCall.setVisibility(View.VISIBLE);
//                            } else {
//
//                                messageCall.setVisibility(View.GONE);
//                            }
//                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (type == 1) {
                            if (mAdapter.getList().size() == 0) {
                                if (noContact != null) {

                                    noContact.setText(getString(R.string.noMatch) + " " + constraint);
                                    noContact.setVisibility(View.VISIBLE);


                                    messageCall.setVisibility(View.GONE);

                                }
                            } else {
                                noContact.setVisibility(View.GONE);


                            }
                        } else {


                            if (mAdapter2.getList().size() == 0) {
                                if (noChat != null) {

                                    noChat.setText(getString(R.string.noMatch) + " " + constraint);
                                    noChat.setVisibility(View.VISIBLE);


                                    messageCall.setVisibility(View.GONE);

                                }
                            } else {
                                noChat.setVisibility(View.GONE);


                            }


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


        ArrayList<Map<String, Object>> contacts = AppController.getInstance().getDbController().loadFriends(AppController.getInstance().getFriendsDocId());

        Forward_ContactItem contact;
        Map<String, Object> contactIthPosition;


        if (mContactData != null) {
            String contactUid;


            boolean hasChatWithContact;
            for (int i = 0; i < contacts.size(); i++) {

                contactIthPosition = contacts.get(i);
                /*
                 * Not showing current user in list of contacts to whom message can be forwarded
                 */
                hasChatWithContact = false;
                contactUid = (String) contactIthPosition.get("userId");
                for (int j = 0; j < mChatData.size(); j++) {

                    if (mChatData.get(j).getReceiverUid().equals(contactUid)) {


                        hasChatWithContact = true;
                        break;

                    }
                }

                if (!hasChatWithContact) {

                    if (contactUid != null && !contactUid.equals(AppController.getInstance().getActiveReceiverId())) {
                        if (!contactIthPosition.containsKey("blocked")) {
                            contact = new Forward_ContactItem();
                            contact.setSelected(false);
                            contact.setContactIdentifier((String) contactIthPosition.get("userName"));
                            contact.setStar((boolean) contactIthPosition.get("isStar"));
                            contact.setContactImage((String) contactIthPosition.get("profilePic"));
                            contact.setContactName(contactIthPosition.get("firstName") + " " + contactIthPosition.get("lastName"));
                            contact.setContactStatus((String) contactIthPosition.get("userName"));
                            contact.setContactUid(contactUid);
                            mContactData.add(contact);


                        }
                    }
                }

            }


//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mAdapter.notifyDataSetChanged();
//                }
//            });


            if (mContactData.size() == 0) {


                if (!hasAtleastOneChat) {

                    messageCall.setVisibility(View.VISIBLE);

                }

                contactsHeader.setVisibility(View.GONE);


            } else if (mContactData.size() > 0) {
                messageCall.setVisibility(View.GONE);
                Collections.sort(mContactData, new SortContactsToForward());

                int size;


                if (mContactData.size() > 2) {

                    viewAllHide_tv.setText(getString(R.string.ViewAll));
                    size = 2;
                } else {


                    size = mContactData.size();
                    viewAll_rl.setVisibility(View.GONE);
                }


                for (int i = 0; i < size; i++) {
                    try {
                        mCollapsedContactData.add(mContactData.get(i));
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }


                mAdapter = new Share_ContactsAdapter(CustomShare.this, mCollapsedContactData);
                recyclerViewContacts.setAdapter(mAdapter);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        mAdapter.notifyDataSetChanged();

                    }
                });

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        mContactData.clear();

        this.intent = intent;
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                mAdapter.notifyDataSetChanged();
//
//            }
//        });

        addChats();
        addContacts();
    }

    private int findPosition(String contactUid) {


        for (int i = 0; i < mContactData.size(); i++) {


            if (mContactData.get(i).getContactUid().equals(contactUid)) {

                return i;
            }


        }

        return -1;
    }


    private int findPositionChat(String contactUid, String secretId) {


        for (int i = 0; i < mChatData.size(); i++) {


            if (mChatData.get(i).getReceiverUid().equals(contactUid) && mChatData.get(i).getSecretId().equals(secretId)) {

                return i;
            }


        }

        return -1;
    }

    private void updateSelectedContactsText() {
        if (count > 0) {
            String currentlySelectedContacts = "";
            for (int i = 0; i < mContactData.size(); i++) {

                if (mContactData.get(i).isSelected()) {

                    currentlySelectedContacts = currentlySelectedContacts + "," + mContactData.get(i).getContactName();
                }
            }


            for (int i = 0; i < mChatData.size(); i++) {

                if (mChatData.get(i).isSelected()) {

                    currentlySelectedContacts = currentlySelectedContacts + "," + mChatData.get(i).getReceiverName();
                }
            }


            selectedContacts.setText(currentlySelectedContacts.substring(1));
        } else {

            selectedContacts.setText("");
        }
    }




    /*
     * Utility methods
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private static byte[] convertFileToByteArray(File f) {


        byte[] byteArray = null;
        byte[] b;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {

            InputStream inputStream = new FileInputStream(f);
            b = new byte[2663];

            int bytesRead;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }


            inputStream = null;

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            b = null;

            try {
                bos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            bos = null;
        }


        return byteArray;
    }


    /*
     * To save the byte array received in to file
     */
    @SuppressWarnings("all")
    public File convertByteArrayToFile(byte[] data, String name, String extension) {


        File file = null;

        try {


            File folder = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER);

            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }


            file = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER, name + extension);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return file;

    }

    /*
     * To calculate the required dimensions of image withoutb actually loading the bitmap in to the memory
     */
    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {


        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;


            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromResource(String pathName,
                                                   int reqWidth, int reqHeight) {


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);


        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);

    }


    /*
     * Uploading images and video and audio to  the server
     */
    @SuppressWarnings("TryWithIdenticalCatches,all")

    private void uploadFile(final Uri fileUri, final String name, final int messageType,
                            final JSONObject obj, final boolean toDeleteFile, final String extension) {


        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);


        final File file = FileUtils.getFile(this, fileUri);

        String url = null;
        if (messageType == 1) {

            url = name + ".jpg";


        } else if (messageType == 2) {

            url = name + ".mp4";


        } else if (messageType == 5) {

            url = name + ".3gp";


        }


        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", url, requestFile);


        String descriptionString = getString(R.string.string_803);
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);


        Call<ResponseBody> call = service.upload(description, body);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {

                /*
                 *
                 *
                 * has to get url from the server in response
                 *
                 *
                 * */


                try {


                    if (response.code() == 200) {


                        String url = null;
                        if (messageType == 1) {

                            url = name + ".jpg";


                        } else if (messageType == 2) {

                            url = name + ".mp4";


                        } else if (messageType == 5) {

                            url = name + ".3gp";


                        }
                        obj.put("payload", Base64.encodeToString((ApiOnServer.CHAT_UPLOAD_PATH + url).getBytes("UTF-8"), Base64.DEFAULT));
                        obj.put("dataSize", file.length());
                        obj.put("timestamp", new Utilities().gmtToEpoch(Utilities.tsInGmt()));
                        if (toDeleteFile) {
                            File fdelete = new File(fileUri.getPath());
                            if (fdelete.exists()) fdelete.delete();

                        }
                        /*
                         *
                         *
                         * emitting to the server the values after the file has been uploaded
                         *
                         * */


                        String receiverUid, documentId;
                        Forward_ContactItem contactItem;


                        /*
                         *For figuring out the contacts whom to forward the messages
                         */

                        for (int i = 0; i < mContactData.size(); i++) {
                            contactItem = mContactData.get(i);


                            if (contactItem.isSelected()) {
                                receiverUid = contactItem.getContactUid();


                                documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, "");


                                if (documentId.isEmpty()) {


                                    documentId = AppController.findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                                            contactItem.getContactName(), contactItem.getContactImage(), "",
                                            false, contactItem.getContactIdentifier(), "", false,contactItem.isStar());
                                }


                                try {
                                    obj.put("to", receiverUid);
                                    obj.put("toDocId", documentId);
                                    obj.put("name", AppController.getInstance().getUserName());

                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("messageId", obj.getString("id"));
                                    map.put("docId", documentId);


                                    AppController.getInstance().publishChatMessage(MqttEvents.Message.value + "/" + receiverUid, obj, 1, false, map);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }


                        ChatlistItem chatlistItem;


                        boolean isSecretChat;
                        String secretId;
                        long dTime = -1;
                        CouchDbController db = AppController.getInstance().getDbController();
                        for (int i = 0; i < mChatData.size(); i++) {
                            chatlistItem = mChatData.get(i);


                            if (chatlistItem.isSelected()) {


                                if (chatlistItem.isGroupChat()) {
                                    /*
                                     * Chat in which to forward the message is a groupchat
                                     */


                                    receiverUid = chatlistItem.getReceiverUid();


                                    documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, "");


                                    if (documentId.isEmpty()) {


                                        documentId = AppController.findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                                                chatlistItem.getReceiverName(), chatlistItem.getReceiverImage(), "", false,
                                                chatlistItem.getReceiverIdentifier(), "", false,chatlistItem.isStar());
                                    }


                                    try {
                                        obj.put("to", receiverUid);
                                        obj.put("toDocId", documentId);
                                        obj.put("name", AppController.getInstance().getUserName());


                                        obj.put("name", chatlistItem.getReceiverName());


                                        String receiverImage = chatlistItem.getReceiverImage();
                                        if (receiverImage == null || receiverImage.isEmpty()) {
                                            obj.put("userImage", "");
                                        } else {
                                            obj.put("userImage", receiverImage);
                                        }


                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("messageId", obj.getString("id"));
                                        map.put("docId", documentId);


                                        AppController.getInstance().publishGroupChatMessage(chatlistItem.getGroupMembersDocId(), obj, map);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                } else {


                                    /*
                                     * Chat to forward the message into is either the normal or the secret chat
                                     */

                                    isSecretChat = !chatlistItem.getSecretId().isEmpty();


                                    secretId = chatlistItem.getSecretId();
                                    receiverUid = chatlistItem.getReceiverUid();


                                    documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId);


                                    if (documentId.isEmpty()) {


                                        documentId = AppController.findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                                                chatlistItem.getReceiverName(), chatlistItem.getReceiverImage(), secretId, false,
                                                chatlistItem.getReceiverIdentifier(), "", false,chatlistItem.isStar());
                                    }
                                    if (isSecretChat) {

                                        Map<String, Object> chat_item = db.getParticularChatInfo(documentId,false);


                                        try {

                                            dTime = (long) (chat_item.get("dTime"));

                                        } catch (ClassCastException e) {

                                            dTime = (int) (chat_item.get("dTime"));

                                        }

                                    }

                                    try {
                                        obj.put("to", receiverUid);
                                        obj.put("toDocId", documentId);
                                        obj.put("name", AppController.getInstance().getUserName());


                                        if (isSecretChat) {








                                            /*
                                             * For secret chat exclusively
                                             */

                                            obj.put("secretId", secretId);
                                            obj.put("dTime", dTime);


                                        }


                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("messageId", obj.getString("id"));
                                        map.put("docId", documentId);


                                        AppController.getInstance().publishChatMessage(MqttEvents.Message.value + "/" + receiverUid, obj, 1, false, map);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                        dismissDialog();


                    } else {

                        dismissDialog();
                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_63, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                onBackPressed();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                dismissDialog();
                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.ShareFailed, ""), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        onBackPressed();


                    }
                }, 500);

            }
        });
    }


    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    private void addChats() {

        /*
         *To display the list of the chats to which message can be forwarded
         */
        final ProgressDialog pDialog = new ProgressDialog(CustomShare.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.Load_Chats));
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(CustomShare.this, R.color.color_black),
                android.graphics.PorterDuff.Mode.SRC_IN);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mChatData.clear();
                mAdapter2.notifyDataSetChanged();
            }
        });


        boolean toSkip = false;

        if (intent.getExtras().getInt("messageType") == 10) {

            toSkip = true;

        }


        CouchDbController db = AppController.getInstance().getDbController();


        Map<String, Object> map = db.getAllChatDetails(AppController.getInstance().getChatDocId());


        if (map != null) {


            ArrayList<String> receiverUidArray = (ArrayList<String>) map.get("receiverUidArray");

            ArrayList<String> receiverDocIdArray = (ArrayList<String>) map.get("receiverDocIdArray");


            Map<String, Object> chat_item;


            ChatlistItem chat;


            ArrayList<Map<String, Object>> chats = new ArrayList<>();
            for (int i = 0; i < receiverUidArray.size(); i++) {
                chats.add(db.getParticularChatInfo(receiverDocIdArray.get(i),false));
                Collections.sort(chats, new TimestampSorter());

            }

            String contactDocId = AppController.getInstance().getFriendsDocId();


            Map<String, Object> contactInfo;
            for (int i = 0; i < chats.size(); i++) {
                chat_item = chats.get(i);

//                if (((chat_item.get("selfUid")).equals(AppController.getInstance().getActiveReceiverId()) && (chat_item.get("secretId")).
//                        equals(AppController.getInstance().getActiveSecretId()))
//                        || ((chat_item.get("selfUid")).equals(AppController.getInstance().getActiveReceiverId())&&
//                        ((String)chat_item.get("secretId")).isEmpty()))


                /*
                 * To avoid forwarding into same chat to which the message being forwarded belongs
                 */

                if (((chat_item.get("selfUid")).equals(AppController.getInstance().getActiveReceiverId()) && (chat_item.get("secretId")).
                        equals(AppController.getInstance().getActiveSecretId()))
                ) {
                    continue;
                }

                chat = new ChatlistItem();


                boolean hasNewMessage = (boolean) chat_item.get("hasNewMessage");
                chat.sethasNewMessage(hasNewMessage);


                try {
                    if (((String) chat_item.get("secretId")).isEmpty()) {
                        chat.setSecretChat(false);


                        chat.setSecretId("");

                    } else {

                        if (toSkip) {

                            continue;
                        }

                        chat.setSecretChat(true);

                        chat.setSecretId((String) chat_item.get("secretId"));
                    }
                } catch (NullPointerException e) {

                    chat.setSecretChat(false);
                    chat.setSecretId("");
                }
                if (hasNewMessage) {


                    chat.setNewMessageTime((String) chat_item.get("newMessageTime"));
                    chat.setNewMessage((String) chat_item.get("newMessage"));

                    chat.setNewMessageCount((String) chat_item.get("newMessageCount"));


                    chat.setShowTick(false);
                } else {


                    Map<String, Object> map2 = db.getLastMessageDetails((String) chat_item.get("selfDocId"));

                    String time = (String) map2.get("lastMessageTime");

                    String message = (String) map2.get("lastMessage");

                    chat.setShowTick((boolean) map2.get("showTick"));

                    if ((boolean) map2.get("showTick")) {
                        chat.setTickStatus((int) map2.get("tickStatus"));

                    }
                    chat.setNewMessageTime(time);
                    chat.setNewMessage(message);

                    chat.setNewMessageCount("0");

                    if (message == null) {
                        chat.setNewMessage("");
                    }
                }

                //   chat.setReceiverIdentifier((String) chat_item.get("receiverIdentifier"));
                chat.setReceiverIdentifier((String) chat_item.get("userName"));
                chat.setDocumentId((String) chat_item.get("selfDocId"));

                chat.setReceiverUid((String) chat_item.get("selfUid"));

                if (chat_item.containsKey("isStar"))
                    chat.setStar((boolean) chat_item.get("isStar"));
                else
                    chat.setStar(false);
                if (chat_item.containsKey("groupChat") && (boolean) chat_item.get("groupChat")) {


                    /*
                     * Given chat is a group chat
                     */


                    chat.setGroupChat(true);

                    chat.setReceiverName((String) chat_item.get("receiverName"));
                    String image = (String) chat_item.get("receiverImage");
                    if (image != null && !image.isEmpty()) {
                        chat.setReceiverImage(image);
                    } else {

                        chat.setReceiverImage("");
                    }


                    /*
                     *To avoid the option of adding the member to the contact list on the long press of the group item
                     */


                    try {


                        String groupMembersDocId = AppController.getInstance().getDbController().fetchGroupChatDocumentId(
                                AppController.getInstance().getGroupChatsDocId(), (String) chat_item.get("selfUid"));

                        if (groupMembersDocId == null || (!AppController.getInstance().getDbController().checkIfActive(groupMembersDocId))) {


                            continue;
                        } else {

                            chat.setGroupMembersDocId(groupMembersDocId);

                        }
                    } catch (Exception e) {


                        continue;
                    }
                    chat.setReceiverInContacts(true);

                } else {


                    /*
                     *Given chat is the normal or the secret chat
                     */



                    /*
                     * If the uid exists in contacts
                     */
                    chat.setGroupChat(false);


                    contactInfo = AppController.
                            getInstance().getDbController().
                            getFriendInfoFromUid(contactDocId, (String) chat_item.get("selfUid"));

                    if (contactInfo != null) {


                        if (contactInfo.containsKey("blocked")) {
                            continue;


                        }

                        chat.setReceiverName(contactInfo.get("firstName") + " " + contactInfo.get("lastName"));


                        String image = (String) contactInfo.get("profilePic");


                        if (image != null && !image.isEmpty()) {
                            chat.setReceiverImage(image);
                        } else {

                            chat.setReceiverImage("");
                        }


                        chat.setReceiverInContacts(true);
                    } else {
                        chat.setReceiverInContacts(false);
                        /*
                         * If userId doesn't exists in contact
                         */
                        chat.setReceiverName((String) chat_item.get("receiverIdentifier"));
                        //  chat.setReceiverImage((String) chat_item.get("receiverImage"));
                        String image = (String) chat_item.get("receiverImage");
                        if (image != null && !image.isEmpty()) {
                            chat.setReceiverImage(image);
                        } else {

                            chat.setReceiverImage("");
                        }
                    }

                }

                mChatData.add(chat);


                mAdapter.notifyDataSetChanged();


            }


            //   recyclerViewChats.scrollToPosition(0);

        }


        if (mChatData.size() == 0) {

            chatsHeader.setVisibility(View.GONE);

        } else {
            hasAtleastOneChat = true;
        }
        if (pDialog.isShowing()) {


            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


            if (context instanceof Activity) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        pDialog.dismiss();
                    }
                } else {


                    if (!((Activity) context).isFinishing()) {
                        pDialog.dismiss();
                    }
                }
            } else {


                try {
                    pDialog.dismiss();
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();

                } catch (final Exception e) {
                    e.printStackTrace();

                }
            }
        }
    }


    private void manipulateAdapterContents() {


        if (showingAllContacts) {

            /*
             * To hide all groups
             */


            mCollapsedContactData.clear();

            /*
             * Have intentionally commented out onc e time initialization as contens of mContactData might change later as well.
             */
            int size;

            if (mContactData.size() > 2) {

                viewAllHide_tv.setText(getString(R.string.ViewAll));
                size = 2;
            } else {


                size = 1;

                viewAll_rl.setVisibility(View.GONE);
            }


            for (int i = 0; i < size; i++) {


                try {

                    mCollapsedContactData.add(mContactData.get(i));


                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }


            mAdapter = new Share_ContactsAdapter(CustomShare.this, mCollapsedContactData);
            recyclerViewContacts.setAdapter(mAdapter);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mAdapter.notifyDataSetChanged();

                }
            });

            viewAllHide_tv.setText(getString(R.string.ViewAll));


        } else {
            /*
             * To show all groups
             */
            mAdapter = new Share_ContactsAdapter(CustomShare.this, mContactData);
            recyclerViewContacts.setAdapter(mAdapter);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mAdapter.notifyDataSetChanged();

                }
            });

            viewAllHide_tv.setText(getString(R.string.Hide));

        }


        showingAllContacts = !showingAllContacts;
    }


    /*
     * To remove the blocked user from the list to whom the message can be forwarded
     *
     *
     * As of now,only feature to remove a contact from the list is there,not the option to update the list incase you are unblocked
     *
     */


    private void removeFromContactsAndChats(final String opponentId) {


        /*
         * For removing from the contacts list
         */

        for (int i = 0; i < mContactData.size(); i++) {

            if (mContactData.get(i).getContactUid().equals(opponentId)) {


                mContactData.remove(i);

                final int k = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (k == 0) {

                            mAdapter.notifyDataSetChanged();
                        } else {

                            mAdapter.notifyItemRemoved(k);
                        }
                    }
                });

                break;
            }
        }

        /*
         *From removing from the list of the contacts
         */

        /*
         * CAN'T SIMPLY REMOVE ITEMS ONE AT A TIME AS IT FAILS IF TWO SUCCESIVE CHATS FROM SAME USER
         */
        boolean exists = false;


        ArrayList<ChatlistItem> chatList = new ArrayList<>();

        for (int i = 0; i < mChatData.size(); i++) {

            if (!mChatData.get(i).getReceiverUid().equals(opponentId)) {


                chatList.add(mChatData.get(i));

                exists = true;
            }

        }

        if (exists) {
            mChatData.clear();
            mChatData.addAll(chatList);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter2.notifyDataSetChanged();

                }
            });
        }

    }


    private void handleIncomingIntent() {


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {


            if ("text/plain".equals(type)) {
                handleText(intent, 0); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleMedia(intent, 1); // Handle single image being sent
            } else if (type.startsWith("video/")) {
                handleMedia(intent, 2); // Handle single video being sent
            } else if (type.startsWith("audio/")) {
                handleMedia(intent, 5); // Handle single audio being sent
            }

        }


    }

    /*
     * To share a media message from outside the app
     */

    private void handleMedia(Intent intent, int messageType) {


        Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri != null) {
            try {


                String filePath = FileUtils.getPath(CustomShare.this, uri);


                switch (messageType) {


                    case 1: {

                        Uri uriImage = null;
                        String idImage = null;
                        Bitmap bm = null;

                        try {


                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(filePath, options);


                            int height = options.outHeight;
                            int width = options.outWidth;

                            float density = getResources().getDisplayMetrics().density;
                            int reqHeight;


                            if (width != 0) {


                                reqHeight = (int) ((150 * density) * (height / width));


                                bm = decodeSampledBitmapFromResource(filePath, (int) (150 * density), reqHeight);


                                ByteArrayOutputStream baos = new ByteArrayOutputStream();


                                if (bm != null) {

                                    bm.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, baos);
                                    //bm = null;
                                    byte[] b = baos.toByteArray();
                                    try {
                                        baos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    baos = null;


                                    idImage = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                                    File f = convertByteArrayToFile(b, idImage, ".jpg");
                                    b = null;

                                    uriImage = Uri.fromFile(f);
                                    f = null;


                                } else {
                                    dismissDialog();

                                    if (root != null) {

                                        Snackbar snackbar = Snackbar.make(root, R.string.string_482, Snackbar.LENGTH_SHORT);


                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                    }


                                }


                            } else {

                                dismissDialog();
                                if (root != null) {

                                    Snackbar snackbar = Snackbar.make(root, R.string.string_482, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                }

                            }


                        } catch (OutOfMemoryError e) {

                            dismissDialog();

                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, R.string.string_491, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }


                        }


                        if (uriImage != null) {

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                            bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);


                            bm = null;
                            byte[] b = baos.toByteArray();

                            try {
                                baos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            baos = null;

                            sendMessageToMultipleContacts(1, idImage, Base64.encodeToString(b, Base64.DEFAULT).trim(), uriImage, filePath);


                            uriImage = null;
                            b = null;
                            bm = null;

                        }
                        break;
                    }


                    case 2: {


                        Uri uriVideo = null;
                        String id = null;
                        try {


                            File video = new File(filePath);

                            if (video.length() <= (MAX_VIDEO_SIZE)) {

                                try {


                                    byte[] b = convertFileToByteArray(video);
                                    video = null;


                                    id = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                                    File f = convertByteArrayToFile(b, id, ".mp4");
                                    b = null;

                                    uriVideo = Uri.fromFile(f);
                                    f = null;

                                    b = null;


                                } catch (OutOfMemoryError e) {
                                    dismissDialog();
                                    if (root != null) {

                                        Snackbar snackbar = Snackbar.make(root, R.string.string_511, Snackbar.LENGTH_SHORT);


                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                    }


                                }

                                if (uriVideo != null) {


                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    Bitmap bm2 = ThumbnailUtils.createVideoThumbnail(filePath,
                                            MediaStore.Images.Thumbnails.MINI_KIND);

                                    if (bm2 != null) {

                                        bm2.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                                        bm2 = null;
                                        byte[] b = baos.toByteArray();
                                        try {
                                            baos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        baos = null;


                                        try {
                                            sendMessageToMultipleContacts(2, id, Base64.encodeToString(b, Base64.DEFAULT).trim(), uriVideo, filePath);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        uriVideo = null;
                                        b = null;

                                    } else {

                                        dismissDialog();


                                        if (root != null) {

                                            Snackbar snackbar = Snackbar.make(root, getString(R.string.string_674)

                                                    , Snackbar.LENGTH_SHORT);


                                            snackbar.show();
                                            View view = snackbar.getView();
                                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                        }


                                    }


                                } else {

                                    if (root != null) {

                                        Snackbar snackbar = Snackbar.make(root, getString(R.string.string_674)

                                                , Snackbar.LENGTH_SHORT);


                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                    }
                                }


                            } else {
                                dismissDialog();

                                if (root != null) {

                                    Snackbar snackbar = Snackbar.make(root, getString(R.string.string_52) + " " + MAX_VIDEO_SIZE / (1024 * 1024) + " " + getString(R.string.string_56), Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                }


                            }


                        } catch (NullPointerException e) {

                            dismissDialog();
                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, R.string.string_674, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }

                        }


                        break;

                    }

                    case 5: {


                        String idAudio = null;
                        Uri uriAudio = null;
                        try {


                            File audio = new File(filePath);


                            if (audio.length() <= (MAX_VIDEO_SIZE)) {
                                try {


                                    byte[] b = convertFileToByteArray(audio);
                                    audio = null;
                                    // b = compress(b);


                                    idAudio = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                                    File f = convertByteArrayToFile(b, idAudio, ".3gp");
                                    b = null;

                                    uriAudio = Uri.fromFile(f);
                                    f = null;

                                    b = null;


                                } catch (OutOfMemoryError e) {
                                    dismissDialog();
                                    if (root != null) {

                                        Snackbar snackbar = Snackbar.make(root, R.string.string_531, Snackbar.LENGTH_SHORT);


                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                    }


                                }

                                if (uriAudio != null) {


                                    sendMessageToMultipleContacts(5, idAudio, null, uriAudio, filePath);

                                    uriAudio = null;

                                }
                            } else {

                                dismissDialog();
                                if (root != null) {

                                    Snackbar snackbar = Snackbar.make(root, getString(R.string.string_54) + " " + MAX_VIDEO_SIZE / (1024 * 1024) + " " + getString(R.string.string_56), Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                }

                            }


                        } catch (NullPointerException e) {

                            dismissDialog();
                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, R.string.string_667, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }

                        }


                        break;
                    }


                }


            } catch (Exception e) {

                dismissDialog();
                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.ShareFailed, ""), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }


            }


        } else {

            String str = "";
            switch (messageType) {


                case 1:

                    str = getString(R.string.Image);
                    break;


                case 2:

                    str = getString(R.string.Video);
                    break;

                case 5:

                    str = getString(R.string.Audio);
                    break;


            }

            dismissDialog();
            if (root != null) {

                Snackbar snackbar = Snackbar.make(root, getString(R.string.ShareFailed, str), Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }

    }


    private void sendMessageToMultipleContacts(int messageType, String id, String thumbnail, Uri uri, String payload) {


        String tsForServer, tsForServerEpoch;

        if (id == null) {
            tsForServer = Utilities.tsInGmt();
            tsForServerEpoch = new Utilities().gmtToEpoch(tsForServer);
        } else {
            tsForServerEpoch = id;
            tsForServer = Utilities.epochtoGmt(id);

        }


        JSONObject obj;


        obj = new JSONObject();



        /*
         *
         *
         * image message so payload field is not set
         *
         * */


        switch (messageType) {

            case 1: {
                try {

                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());

                    obj.put("timestamp", tsForServerEpoch);

                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "1");

                    obj.put("thumbnail", thumbnail);
                    uploadFile(uri, AppController.getInstance().getUserId() + tsForServerEpoch, 1, obj, true, null);


                } catch (JSONException e) {
                    e.printStackTrace();
                }





                /*
                 *
                 *
                 * video message so payload field is not set
                 *
                 * */


                break;
            }
            case 2: {
                try {

                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());

                    obj.put("timestamp", tsForServerEpoch);

                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "2");

                    obj.put("thumbnail", thumbnail);

                    uploadFile(uri, AppController.getInstance().getUserId() + tsForServerEpoch, 2, obj, true, null);


                } catch (JSONException e) {
                    e.printStackTrace();


                }




                /*
                 *
                 *
                 * audio message so payload field is not set
                 *
                 * */


                break;
            }
            case 5: {
                try {

                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());

                    obj.put("timestamp", tsForServerEpoch);

                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "5");


                    uploadFile(uri, AppController.getInstance().getUserId() + tsForServerEpoch, 5, obj, true, null);


                } catch (JSONException e) {
                    e.printStackTrace();


                }

                break;
            }


        }


        String receiverUid, documentId;

        Forward_ContactItem contactItem;
        for (int i = 0; i < mContactData.size(); i++) {
            contactItem = mContactData.get(i);


            if (contactItem.isSelected()) {
                receiverUid = contactItem.getContactUid();


                documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, "");


                if (documentId.isEmpty()) {


                    documentId = AppController.findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                            contactItem.getContactName(), contactItem.getContactImage(), "", false, contactItem.getContactIdentifier(), "", false,contactItem.isStar());
                }


                try {
                    obj.put("to", receiverUid);
                    obj.put("toDocId", documentId);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /*
                 * For saving the message into db
                 */


                if (messageType == 1) {


                    /*
                     * receiverImage
                     */


                    Map<String, Object> map = new HashMap<>();
                    map.put("message", payload);
                    map.put("messageType", "1");
                    map.put("isSelf", true);


                    map.put("downloadStatus", 1);
                    map.put("from", AppController.getInstance().getUserId());
                    map.put("Ts", tsForServer);
                    map.put("deliveryStatus", "0");
                    map.put("id", tsForServerEpoch);


                    AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, "");
                    map = null;


                } else if (messageType == 2) {

                    /*
                     * Video
                     */


                    Map<String, Object> map = new HashMap<>();
                    map.put("message", payload);
                    map.put("messageType", "2");
                    map.put("isSelf", true);
                    map.put("downloadStatus", 1);
                    map.put("from", AppController.getInstance().getUserId());
                    map.put("Ts", tsForServer);
                    map.put("deliveryStatus", "0");
                    map.put("id", tsForServerEpoch);


                    AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, "");
                    map = null;


                } else if (messageType == 5) {

                    /*
                     * Audio
                     */


                    Map<String, Object> map = new HashMap<>();
                    map.put("message", payload);
                    map.put("messageType", "5");
                    map.put("isSelf", true);
                    map.put("from", AppController.getInstance().getUserId());
                    map.put("Ts", tsForServer);
                    map.put("downloadStatus", 1);
                    map.put("deliveryStatus", "0");
                    map.put("id", tsForServerEpoch);


                    AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, "");


                    map = null;


                }
                /*
                 *
                 *
                 *
                 * Need to store all the messages in db so that incase internet
                 * not present then has to resend all messages whenever internet comes back
                 *
                 *
                 * */


                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.put("from", AppController.getInstance().getUserId());
                mapTemp.put("to", receiverUid);

                mapTemp.put("toDocId", documentId);


                mapTemp.put("id", tsForServerEpoch);

                mapTemp.put("timestamp", tsForServerEpoch);


                String type = Integer.toString(messageType);


                mapTemp.put("type", type);


                mapTemp.put("name", AppController.getInstance().getUserName());


                mapTemp.put("message", payload);

                AppController.getInstance().getDbController().addUnsentMessage(AppController.getInstance().getunsentMessageDocId(), mapTemp);


            }


        }


        /*
         * For sharing the file in the chats
         */


        boolean isSecretChat;


        String secretId = "";


        CouchDbController db = AppController.getInstance().getDbController();
        long dTime = -1;


        ChatlistItem chatlistItem;
        for (int i = 0; i < mChatData.size(); i++) {
            chatlistItem = mChatData.get(i);


            if (chatlistItem.isSelected()) {

                if (chatlistItem.isGroupChat()) {

                    /*
                     * If the selected chat is a groupchat
                     */


                    receiverUid = chatlistItem.getReceiverUid();


                    documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, "");


                    if (documentId.isEmpty()) {


                        documentId = AppController.findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                                chatlistItem.getReceiverName(), chatlistItem.getReceiverImage(), "", false,
                                chatlistItem.getReceiverIdentifier(), "", false,chatlistItem.isStar());
                    }


                    try {
                        obj.put("to", receiverUid);
                        obj.put("toDocId", documentId);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    /*
                     * For saving the message into db
                     */


                    if (messageType == 1) {


                        /*
                         * receiverImage
                         */


                        Map<String, Object> map = new HashMap<>();
                        map.put("message", payload);
                        map.put("messageType", "1");
                        map.put("isSelf", true);


                        map.put("downloadStatus", 1);
                        map.put("from", AppController.getInstance().getUserId());
                        map.put("Ts", tsForServer);
                        map.put("deliveryStatus", "0");
                        map.put("id", tsForServerEpoch);


                        AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);
                        map = null;


                    } else if (messageType == 2) {

                        /*
                         * Video
                         */


                        Map<String, Object> map = new HashMap<>();
                        map.put("message", payload);
                        map.put("messageType", "2");
                        map.put("isSelf", true);
                        map.put("downloadStatus", 1);
                        map.put("from", AppController.getInstance().getUserId());
                        map.put("Ts", tsForServer);
                        map.put("deliveryStatus", "0");
                        map.put("id", tsForServerEpoch);


                        AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);
                        map = null;


                    } else if (messageType == 5) {

                        /*
                         * Audio
                         */


                        Map<String, Object> map = new HashMap<>();
                        map.put("message", payload);
                        map.put("messageType", "5");
                        map.put("isSelf", true);
                        map.put("from", AppController.getInstance().getUserId());
                        map.put("Ts", tsForServer);
                        map.put("downloadStatus", 1);
                        map.put("deliveryStatus", "0");
                        map.put("id", tsForServerEpoch);

                        AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);


                        map = null;


                    }




                    /*
                     *
                     *
                     *
                     * Need to store all the messages in db so that incase internet
                     * not present then has to resend all messages whenever internet comes back
                     *
                     *
                     * */


                    Map<String, Object> mapTemp = new HashMap<>();
                    mapTemp.put("from", AppController.getInstance().getUserId());
                    mapTemp.put("to", receiverUid);

                    mapTemp.put("toDocId", documentId);


                    mapTemp.put("id", tsForServerEpoch);

                    mapTemp.put("timestamp", tsForServerEpoch);


                    String type = Integer.toString(messageType);


                    mapTemp.put("type", type);

                    mapTemp.put("isGroupMessage", true);


                    mapTemp.put("groupMembersDocId", chatlistItem.getGroupMembersDocId());


                    mapTemp.put("name", chatlistItem.getReceiverName());


                    String receiverImage = chatlistItem.getReceiverImage();
                    if (receiverImage == null || receiverImage.isEmpty()) {
                        mapTemp.put("userImage", "");
                    } else {
                        mapTemp.put("userImage", receiverImage);
                    }


                    mapTemp.put("message", payload);

                    AppController.getInstance().getDbController().addUnsentMessage(AppController.getInstance().getunsentMessageDocId(), mapTemp);


                } else {


                    /*
                     *If the selected chat is a secret or the group chat
                     *
                     */


                    isSecretChat = !chatlistItem.getSecretId().isEmpty();
                    secretId = chatlistItem.getSecretId();

                    receiverUid = chatlistItem.getReceiverUid();


                    documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId);


                    if (documentId.isEmpty()) {


                        documentId = AppController.findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                                chatlistItem.getReceiverName(), chatlistItem.getReceiverImage(), secretId, false,
                                chatlistItem.getReceiverIdentifier(), "", false,chatlistItem.isStar());
                    }
                    if (isSecretChat) {

                        Map<String, Object> chat_item = db.getParticularChatInfo(documentId,false);


                        try {

                            dTime = (long) (chat_item.get("dTime"));

                        } catch (ClassCastException e) {

                            dTime = (int) (chat_item.get("dTime"));

                        }

                    }

                    try {
                        obj.put("to", receiverUid);
                        obj.put("toDocId", documentId);

                        if (isSecretChat) {





                            /*
                             * For secret chat exclusively
                             */

                            obj.put("secretId", secretId);
                            obj.put("dTime", dTime);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    /*
                     * For saving the message into db
                     */


                    if (messageType == 1) {


                        /*
                         * receiverImage
                         */


                        Map<String, Object> map = new HashMap<>();
                        map.put("message", payload);
                        map.put("messageType", "1");
                        map.put("isSelf", true);


                        map.put("downloadStatus", 1);
                        map.put("from", AppController.getInstance().getUserId());
                        map.put("Ts", tsForServer);
                        map.put("deliveryStatus", "0");
                        map.put("id", tsForServerEpoch);

                        if (isSecretChat) {
                            /*
                             * For secret chat exclusively
                             */

                            map.put("dTime", dTime);
                            map.put("timerStarted", false);

                        }
                        AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);
                        map = null;


                    } else if (messageType == 2) {

                        /*
                         * Video
                         */


                        Map<String, Object> map = new HashMap<>();
                        map.put("message", payload);
                        map.put("messageType", "2");
                        map.put("isSelf", true);
                        map.put("downloadStatus", 1);
                        map.put("from", AppController.getInstance().getUserId());
                        map.put("Ts", tsForServer);
                        map.put("deliveryStatus", "0");
                        map.put("id", tsForServerEpoch);

                        if (isSecretChat) {
                            /*
                             * For secret chat exclusively
                             */

                            map.put("dTime", dTime);
                            map.put("timerStarted", false);

                        }
                        AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);
                        map = null;


                    } else if (messageType == 5) {

                        /*
                         * Audio
                         */


                        Map<String, Object> map = new HashMap<>();
                        map.put("message", payload);
                        map.put("messageType", "5");
                        map.put("isSelf", true);
                        map.put("from", AppController.getInstance().getUserId());
                        map.put("Ts", tsForServer);
                        map.put("downloadStatus", 1);
                        map.put("deliveryStatus", "0");
                        map.put("id", tsForServerEpoch);

                        if (isSecretChat) {
                            /*
                             * For secret chat exclusively
                             */

                            map.put("dTime", dTime);
                            map.put("timerStarted", false);

                        }
                        AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);


                        map = null;


                    }




                    /*
                     *
                     *
                     *
                     * Need to store all the messages in db so that incase internet
                     * not present then has to resend all messages whenever internet comes back
                     *
                     *
                     * */


                    Map<String, Object> mapTemp = new HashMap<>();
                    mapTemp.put("from", AppController.getInstance().getUserId());
                    mapTemp.put("to", receiverUid);

                    mapTemp.put("toDocId", documentId);


                    mapTemp.put("id", tsForServerEpoch);

                    mapTemp.put("timestamp", tsForServerEpoch);


                    String type = Integer.toString(messageType);


                    mapTemp.put("type", type);


                    mapTemp.put("name", AppController.getInstance().getUserName());

                    if (isSecretChat) {



                        /*
                         * Exclusively for secret chat
                         */
                        mapTemp.put("dTime", dTime);
                        mapTemp.put("secretId", secretId);
                    }
                    mapTemp.put("message", payload);

                    AppController.getInstance().getDbController().addUnsentMessage(AppController.getInstance().getunsentMessageDocId(), mapTemp);


                }

            }


        }


    }

    @Override
    public void onBackPressed() {
        try {

            if (AppController.getInstance().isActiveOnACall()) {
                if (AppController.getInstance().isCallMinimized()) {
                    super.onBackPressed();
                    supportFinishAfterTransition();
                }
            } else {
                super.onBackPressed();
                supportFinishAfterTransition();
            }

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private void handleText(Intent intent, int messageType) {


        String payload = intent.getStringExtra(Intent.EXTRA_TEXT);

        String tsForServer, tsForServerEpoch;


        tsForServer = Utilities.tsInGmt();
        tsForServerEpoch = new Utilities().gmtToEpoch(tsForServer);


        JSONObject


                obj = new JSONObject();


        /*
         *
         * normal text message so payload field is set as well
         *
         * */

        switch (messageType) {


            case 0: {
                /*
                 * Text message
                 */


                try {


                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());

                    obj.put("payload", Base64.encodeToString(payload.getBytes("UTF-8"), Base64.DEFAULT).trim());


                    obj.put("timestamp", tsForServerEpoch);

                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "0");

                    obj.put("name", AppController.getInstance().getUserName());


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }




                /*
                 *
                 * location message so payload field is set as well
                 *
                 * */


                break;
            }


        }


        String receiverUid, documentId;

        Forward_ContactItem contactItem;
        for (
                int i = 0; i < mContactData.size(); i++) {
            contactItem = mContactData.get(i);


            if (contactItem.isSelected()) {
                receiverUid = contactItem.getContactUid();


                documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, "");


                if (documentId.isEmpty()) {


                    documentId = AppController.findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                            contactItem.getContactName(), contactItem.getContactImage(), "", false, contactItem.getContactIdentifier(), "", false,contactItem.isStar());
                }


                try {
                    obj.put("to", receiverUid);
                    obj.put("toDocId", documentId);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                /*
                 * For saving the message into db
                 */
                if (messageType == 0) {


                    /*
                     * Text message
                     */


                    Map<String, Object> map = new HashMap<>();
                    map.put("message", payload);
                    map.put("messageType", "0");
                    map.put("isSelf", true);
                    map.put("from", AppController.getInstance().getUserId());


                    map.put("Ts", tsForServer);
                    map.put("deliveryStatus", "0");
                    map.put("id", tsForServerEpoch);


                    AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, "");

                    map = null;


                }






                /*
                 *
                 *
                 *
                 * Need to store all the messages in db so that incase internet
                 * not present then has to resend all messages whenever internet comes back
                 *
                 *
                 * */


                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.put("from", AppController.getInstance().getUserId());
                mapTemp.put("to", receiverUid);

                mapTemp.put("toDocId", documentId);


                mapTemp.put("id", tsForServerEpoch);

                mapTemp.put("timestamp", tsForServerEpoch);


                String type = Integer.toString(messageType);


                mapTemp.put("type", type);


                mapTemp.put("name", AppController.getInstance().getUserName());


                mapTemp.put("message", payload);

                AppController.getInstance().getDbController().addUnsentMessage(AppController.getInstance().getunsentMessageDocId(), mapTemp);


                /*s
                 *
                 *
                 * emit directly if not image or video or audio
                 *
                 *
                 * */


                try {


                    obj.put("name", AppController.getInstance().getUserName());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                HashMap<String, Object> map = new HashMap<>();
                map.put("messageId", tsForServerEpoch);
                map.put("docId", documentId);


                AppController.getInstance().publishChatMessage(MqttEvents.Message.value + "/" + receiverUid, obj, 1, false, map);


            }


        }

        /*
         * For selecting chats in which to forward the given message
         */


        ChatlistItem chatItem;


        boolean isSecretChat;


        String secretId = "";


        CouchDbController db = AppController.getInstance().getDbController();
        long dTime = -1;
        for (
                int i = 0; i < mChatData.size(); i++) {

            chatItem = mChatData.get(i);


            if (chatItem.isSelected()) {


                if (chatItem.isGroupChat()) {

                    /*
                     * If the selected chat is the group chat
                     */


                    receiverUid = chatItem.getReceiverUid();


                    documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, "");


                    if (documentId.isEmpty()) {


                        documentId = AppController.findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                                chatItem.getReceiverName(), chatItem.getReceiverImage(), "", false, chatItem.getReceiverIdentifier(), "", false,chatItem.isStar());
                    }


                    try {
                        obj.put("to", receiverUid);
                        obj.put("toDocId", documentId);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    /*
                     * For saving the message into db
                     */
                    if (messageType == 0) {


                        /*
                         * Text message
                         */


                        Map<String, Object> map = new HashMap<>();
                        map.put("message", payload);
                        map.put("messageType", "0");
                        map.put("isSelf", true);
                        map.put("from", AppController.getInstance().getUserId());


                        map.put("Ts", tsForServer);
                        map.put("deliveryStatus", "0");
                        map.put("id", tsForServerEpoch);


                        AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);

                        map = null;


                    }



                    /*
                     *
                     *
                     *
                     * Need to store all the messages in db so that incase internet
                     * not present then has to resend all messages whenever internet comes back
                     *
                     *
                     * */


                    Map<String, Object> mapTemp = new HashMap<>();
                    mapTemp.put("from", AppController.getInstance().getUserId());
                    mapTemp.put("to", receiverUid);

                    mapTemp.put("toDocId", documentId);


                    mapTemp.put("id", tsForServerEpoch);

                    mapTemp.put("timestamp", tsForServerEpoch);


                    String type = Integer.toString(messageType);


                    mapTemp.put("type", type);


                    mapTemp.put("isGroupMessage", true);


                    mapTemp.put("groupMembersDocId", chatItem.getGroupMembersDocId());


                    mapTemp.put("name", chatItem.getReceiverName());


                    String receiverImage = chatItem.getReceiverImage();
                    if (receiverImage == null || receiverImage.isEmpty()) {
                        mapTemp.put("userImage", "");
                    } else {
                        mapTemp.put("userImage", receiverImage);
                    }

                    mapTemp.put("message", payload);

                    AppController.getInstance().getDbController().addUnsentMessage(AppController.getInstance().getunsentMessageDocId(), mapTemp);


                    /*s
                     *
                     *
                     * Emit directly if not image or video or audio to all the members in the corresponding group
                     *
                     *
                     * */


                    try {


                        obj.put("name", chatItem.getReceiverName());


                        obj.put("userImage", receiverImage);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    HashMap<String, Object> map = new HashMap<>();
                    map.put("messageId", tsForServerEpoch);
                    map.put("docId", documentId);


                    AppController.getInstance().publishGroupChatMessage(chatItem.getGroupMembersDocId(), obj, map);


                    AppController.getInstance().getDbController().updateChatListOnViewingMessage(documentId);

                } else {

                    /*
                     * If the selected chat is the group or the normal chat
                     */


                    isSecretChat = !chatItem.getSecretId().isEmpty();
                    secretId = chatItem.getSecretId();

                    receiverUid = chatItem.getReceiverUid();


                    documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId);


                    if (documentId.isEmpty()) {


                        documentId = AppController.findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                                chatItem.getReceiverName(), chatItem.getReceiverImage(), secretId, false, chatItem.getReceiverIdentifier(), "", false,chatItem.isStar());
                    }

                    if (isSecretChat) {

                        Map<String, Object> chat_item = db.getParticularChatInfo(documentId,false);


                        try {

                            dTime = (long) (chat_item.get("dTime"));

                        } catch (ClassCastException e) {

                            dTime = (int) (chat_item.get("dTime"));

                        }

                    }
                    try {
                        obj.put("to", receiverUid);
                        obj.put("toDocId", documentId);
                        if (isSecretChat) {





                            /*
                             * For secret chat exclusively
                             */

                            obj.put("secretId", secretId);
                            obj.put("dTime", dTime);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    /*
                     * For saving the message into db
                     */
                    if (messageType == 0) {


                        /*
                         * Text message
                         */


                        Map<String, Object> map = new HashMap<>();
                        map.put("message", payload);
                        map.put("messageType", "0");
                        map.put("isSelf", true);
                        map.put("from", AppController.getInstance().getUserId());


                        map.put("Ts", tsForServer);
                        map.put("deliveryStatus", "0");
                        map.put("id", tsForServerEpoch);


                        if (isSecretChat) {
                            /*
                             * For secret chat exclusively
                             */


                            map.put("isDTag", false);

                            map.put("dTime", dTime);
                            map.put("timerStarted", false);

                        }

                        AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);

                        map = null;


                    }



                    /*
                     *
                     *
                     *
                     * Need to store all the messages in db so that incase internet
                     * not present then has to resend all messages whenever internet comes back
                     *
                     *
                     * */


                    Map<String, Object> mapTemp = new HashMap<>();
                    mapTemp.put("from", AppController.getInstance().getUserId());
                    mapTemp.put("to", receiverUid);

                    mapTemp.put("toDocId", documentId);


                    mapTemp.put("id", tsForServerEpoch);

                    mapTemp.put("timestamp", tsForServerEpoch);


                    String type = Integer.toString(messageType);


                    mapTemp.put("type", type);


                    mapTemp.put("name", AppController.getInstance().getUserName());

                    if (isSecretChat) {



                        /*
                         * Exclusively for secret chat
                         */
                        mapTemp.put("dTime", dTime);
                        mapTemp.put("secretId", secretId);
                    }
                    mapTemp.put("message", payload);

                    AppController.getInstance().getDbController().addUnsentMessage(AppController.getInstance().getunsentMessageDocId(), mapTemp);


                    /*
                     *
                     *
                     * emit directly if not image or video or audio
                     *
                     *
                     * */


                    try {


                        obj.put("name", AppController.getInstance().getUserName());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    HashMap<String, Object> map = new HashMap<>();
                    map.put("messageId", tsForServerEpoch);
                    map.put("docId", documentId);


                    AppController.getInstance().publishChatMessage(MqttEvents.Message.value + "/" + receiverUid, obj, 1, false, map);

                    AppController.getInstance().getDbController().updateChatListOnViewingMessage(documentId);
                }
            }

        }


        if (pDialog != null && pDialog.isShowing()) {

            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


            if (context instanceof Activity) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        pDialog.dismiss();
                    }
                } else {


                    if (!((Activity) context).isFinishing()) {
                        pDialog.dismiss();
                    }
                }
            } else {


                try {
                    pDialog.dismiss();
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();

                } catch (final Exception e) {
                    e.printStackTrace();

                }
            }


        }


        onBackPressed();


    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void dismissDialog() {

        if (pDialog != null && pDialog.isShowing()) {
            // pDialog.dismiss();
            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


            if (context instanceof Activity) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        pDialog.dismiss();
                    }
                } else {


                    if (!((Activity) context).isFinishing()) {
                        pDialog.dismiss();
                    }
                }
            } else {


                try {
                    pDialog.dismiss();
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();

                } catch (final Exception e) {
                    e.printStackTrace();

                }
            }


        }

    }


    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {


        if (requestCode == 0) {


            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            onBackPressed();


                        }
                    }, 500);
                }
            } else {


                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        onBackPressed();


                    }
                }, 500);
            }


        }

    }


    private void requestStoragePermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar snackbar = Snackbar.make(root, R.string.StorageAccess,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions(CustomShare.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            0);
                }
            });


            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }


    }


}
