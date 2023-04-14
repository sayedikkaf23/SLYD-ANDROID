package chat.hola.com.app.GroupChat.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONArray;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Activities.ContactsSecretChat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ForwardMessage.Forward_ContactItem;
import chat.hola.com.app.ForwardMessage.SortContactsToForward;
import chat.hola.com.app.GroupChat.Adapters.MembersAdapter;
import chat.hola.com.app.GroupChat.Adapters.SelectedMemberAdapter;
import chat.hola.com.app.ModelClasses.ContactsItem;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ContactSync;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.home.connect.ContactActivity;
import chat.hola.com.app.manager.session.SessionManager;

/**
 * Created by moda on 19/09/17.
 */

public class CreateGroup extends AppCompatActivity {

    private LinearLayout llEmpty;
    private TextView numberOfParticipants;

    private SearchView searchView;

    private FloatingActionButton createGroup;
    private Button btn_create;

    private ImageView backButton;
    private Bus bus = AppController.getBus();
    private MembersAdapter mAdapter;
    private SessionManager sessionManager;

    private SelectedMemberAdapter mAdapter2;
    private ArrayList<Forward_ContactItem> mContactData = new ArrayList<>();
    private ArrayList<Forward_ContactItem> contactsInviteList = new ArrayList<>();


    private ArrayList<Forward_ContactItem> mSelectedMembersData = new ArrayList<>();
    private int count = 0;

    private GridLayoutManager linearLayoutManager;


    private TextView noMatch, messageContact;
    private SwipeRefreshLayout swipeRefresh;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gc_create_group);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        backButton = (ImageView) findViewById(R.id.close);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        sessionManager = new SessionManager(this);

        final TextView title = (TextView) findViewById(R.id.newGroup);
        numberOfParticipants = (TextView) findViewById(R.id.membersCount);
        noMatch = (TextView) findViewById(R.id.noMatch);
        llEmpty = (LinearLayout) findViewById(R.id.llEmpty);

        messageContact = (TextView) findViewById(R.id.messageContact);

        createGroup = (FloatingActionButton) findViewById(R.id.fab);
        btn_create = (Button) findViewById(R.id.btn_create);
        final RelativeLayout root = (RelativeLayout) findViewById(R.id.root);

        /*
         * For displaying the list of all the contacts
         */

        RecyclerView recyclerViewContacts = (RecyclerView) findViewById(R.id.rvContacts);
        recyclerViewContacts.setHasFixedSize(true);
        mAdapter = new MembersAdapter(CreateGroup.this, mContactData);
        recyclerViewContacts.setItemAnimator(new DefaultItemAnimator());


        recyclerViewContacts.setLayoutManager(new CustomLinearLayoutManager(CreateGroup.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewContacts.setAdapter(mAdapter);


        RecyclerView recyclerViewContactsSelected = (RecyclerView) findViewById(R.id.rvContactsSelected);
        recyclerViewContactsSelected.setHasFixedSize(true);
        mAdapter2 = new SelectedMemberAdapter(CreateGroup.this, mSelectedMembersData);
        recyclerViewContactsSelected.setItemAnimator(new DefaultItemAnimator());

//        linearLayoutManager = new CustomGridLayoutManager(CreateGroup.this, 2,GridLayoutManager.HORIZONTAL,2 );

        linearLayoutManager =new GridLayoutManager(CreateGroup.this, 2, GridLayoutManager.HORIZONTAL, false);
        recyclerViewContactsSelected.setLayoutManager(linearLayoutManager);
        recyclerViewContactsSelected.setAdapter(mAdapter2);

         if (AppController.getInstance().isFriendsFetched()) {
//             Toast.makeText(this, "CreateGroup: F Addcontact ", Toast.LENGTH_SHORT).show();
            addContacts();
        } else {
//             Toast.makeText(this, "CreateGroup: F ContactSync ", Toast.LENGTH_SHORT).show();
            swipeRefresh.setRefreshing(true);
            new ContactSync(CreateGroup.this);
        }

        swipeRefresh.setOnRefreshListener(() -> {
//            Toast.makeText(this, "CreateGroup: R ContactSync ", Toast.LENGTH_SHORT).show();
            swipeRefresh.setRefreshing(true);
            new ContactSync(CreateGroup.this);
        });

        searchView = (SearchView) findViewById(R.id.search);


        recyclerViewContacts.addOnItemTouchListener(new RecyclerItemClickListener(CreateGroup.this, recyclerViewContacts, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {
                    Forward_ContactItem item = mAdapter.getList().get(position);
                    if(!item.getInvite()){
                        if(item.isChatEnable()) {
                            if (item.isSelected()) {


                                item.setSelected(false);


                                count--;
                                removeSelectedMember(item.getContactUid());

                            } else {


                                item.setSelected(true);


                                count++;

                                addSelectedMember(item);
                            }

                            try {
                                int i = findPosition(item.getContactUid());
                                if (i != -1) {
                                    mContactData.set(i, item);
                                    mAdapter.notifyItemChanged(i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            updateSelectedMembersText();
                        }else{
                            Toast.makeText(CreateGroup.this,getString(R.string.chat_disable),Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String url = Constants.DYNAMIC_LINK + "/" + Constants.DYNAMIC_LINK_PROFILE + sessionManager.getUserId();
                        String shareMessage = "Check out " + getString(R.string.app_name) + " for your smartphone. Download it today from\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + "com.ezcall.app" + "\n" + url;
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        sendIntent.setType("text/plain");

                        Intent shareIntent = Intent.createChooser(sendIntent, null);
                        startActivity(shareIntent);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));


//        searchView.setVisibility(View.VISIBLE);
        searchView.setIconified(true);
        searchView.setIconifiedByDefault(true);
        searchView.clearFocus();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    backButton.setVisibility(View.GONE);
                    title.setVisibility(View.GONE);
                    numberOfParticipants.setVisibility(View.GONE);
                    searchView.setBackgroundColor(getResources().getColor(R.color.color_white));
                } else {

                    backButton.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                    numberOfParticipants.setVisibility(View.VISIBLE);
                    searchView.setBackgroundColor(getResources().getColor(R.color.colorHintOfRed));
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


                            messageContact.setVisibility(View.VISIBLE);
                        }

                        backButton.setVisibility(View.VISIBLE);
                        title.setVisibility(View.VISIBLE);
                        numberOfParticipants.setVisibility(View.VISIBLE);
                        searchView.setBackgroundColor(getResources().getColor(R.color.colorHintOfRed));
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count > 0) {


                    continueToAddSubjectScreen();


                } else {

                    if (root != null) {
                        Snackbar snackbar = Snackbar.make(root, R.string.AtleastOne, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view2 = snackbar.getView();
                        TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                    }


                }


            }
        });

        Typeface tf = AppController.getInstance().getRegularFont();


        numberOfParticipants.setTypeface(tf, Typeface.NORMAL);
        title.setTypeface(tf, Typeface.BOLD);


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
            Intent intent = new Intent(CreateGroup.this, ChatMessageScreen.class);

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
            } else if (object.getString("eventName").equals("groupCreated")) {

                supportFinishAfterTransition();

            }
            else if (object.getString("eventName").equals(MqttEvents.UserUpdates.value + "/" + AppController.getInstance().getUserId())) {


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
                            removeFromContacts(object.getString("initiatorId"));

                        }
                        break;
                    }
                }


            }
            else if (object.getString("eventName").equals("ContactNameUpdated")) {

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

            }
            else if (object.getString("eventName")
                    .equals(MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId())){
                swipeRefresh.setRefreshing(false);
                addContacts();
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(CreateGroup.this, "CreateGroup: ContactSync Event", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }

        } catch (JSONException e) {
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


    /**
     * To add the list of contacts, from which contact to be added at time of creating the group to be selected.
     */


    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    private void addContacts() {


        final ProgressDialog pDialog = new ProgressDialog(CreateGroup.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.Load_Contacts));
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(CreateGroup.this, R.color.color_black),
                android.graphics.PorterDuff.Mode.SRC_IN);


        ArrayList<Map<String, Object>> contacts = AppController.getInstance().getDbController().loadFriends(AppController.getInstance().getFriendsDocId());

        Forward_ContactItem contact;
        Map<String, Object> contactIthPosition;

        mContactData.clear();

        if (mContactData != null) {


            for (int i = 0; i < contacts.size(); i++) {

                contactIthPosition = contacts.get(i);

                if (!contactIthPosition.containsKey("blocked")) {
                    contact = new Forward_ContactItem();
                    String contactType = (String) contactIthPosition.get("type");
                    contact.setType(contactType);
                    if (contactType != null) {
                        contact.setContactName((String) contactIthPosition.get("name"));
                        contact.setContactIdentifier((String) contactIthPosition.get("name"));
                    } else {
                        contact.setContactName(
                                contactIthPosition.get("firstName") + " " + contactIthPosition.get("lastName"));
                        contact.setContactIdentifier((String) contactIthPosition.get("userName"));
                    }
                    contact.setInvite((Boolean) contactIthPosition.get("isInvite"));
                    contact.setSelected(false);
//                    contact.setContactIdentifier((String) contactIthPosition.get("userName"));
                    contact.setStar((boolean) contactIthPosition.get("isStar"));
                    contact.setContactImage((String) contactIthPosition.get("profilePic"));
//                    contact.setContactName(contactIthPosition.get("firstName") + " " + contactIthPosition.get("lastName"));
                    contact.setContactStatus((String) contactIthPosition.get("userName"));
                    contact.setContactUid((String) contactIthPosition.get("userId"));
                    contact.setChatEnable((boolean) contactIthPosition.get("isChatEnable"));
//                    mContactData.add(contact);

                    if(!(Boolean) contactIthPosition.get("isInvite")){
                        mContactData.add(contact);
                    }else{
                        contactsInviteList.add(contact);
                    }
                }
            }
            mAdapter.registeredContacts(mContactData.size());
            mContactData.addAll(contactsInviteList);

//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mAdapter.notifyDataSetChanged();
//                }
//            });
            if (mContactData.size() == 0) {

                messageContact.setVisibility(View.VISIBLE);

            } else if (mContactData.size() > 0) {
//                Collections.sort(mContactData, new SortContactsToForward());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(CreateGroup.this, "CreateGroup: NotifyDatasetChanged", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                    }
                });
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

        } else {
            llEmpty.setVisibility(View.VISIBLE);
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


    private int findPosition(String contactUid) {


        for (int i = 0; i < mContactData.size(); i++) {


            if (mContactData.get(i).getContactUid().equals(contactUid)) {

                return i;
            }


        }

        return -1;
    }

    private int findPositionInSelectedMembers(String contactUid) {


        for (int i = 0; i < mSelectedMembersData.size(); i++) {


            if (mSelectedMembersData.get(i).getContactUid().equals(contactUid)) {

                return i;
            }


        }

        return -1;
    }

    private void updateSelectedMembersText() {
        if (count > 0) {


            numberOfParticipants.setText(count + getString(R.string.space) + getString(R.string.MembersSelected));


            btn_create.setVisibility(View.VISIBLE);


        } else {

            numberOfParticipants.setText(getString(R.string.AddParticipants));
            btn_create.setVisibility(View.GONE);
        }
    }

    private void addSelectedMember(Forward_ContactItem contact) {

        mSelectedMembersData.add(contact);


        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // mAdapter2.notifyItemInserted(mSelectedMembersData.size() - 1);


                    mAdapter2.notifyDataSetChanged();

                    linearLayoutManager.scrollToPosition(mSelectedMembersData.size() - 1);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void removeSelectedMember(String contactUid) {

        for (int i = 0; i < mSelectedMembersData.size(); i++) {


            if (mSelectedMembersData.get(i).getContactUid().equals(contactUid)) {

                mSelectedMembersData.remove(i);


                if (i == 0) {


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter2.notifyDataSetChanged();
                        }
                    });

                } else {


                    final int k = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter2.notifyItemRemoved(k);
                        }
                    });


                }

                break;

            }

        }
    }


    private void continueToAddSubjectScreen() {

        JSONArray members = new JSONArray();
        JSONObject member;

        try {
            for (int j = 0; j < mSelectedMembersData.size(); j++) {

                member = new JSONObject();
                member.put("memberId", mSelectedMembersData.get(j).getContactUid());
                member.put("memberIdentifier", mSelectedMembersData.get(j).getContactIdentifier());
                member.put("memberLocalName", mSelectedMembersData.get(j).getContactName());

                member.put("memberImage", mSelectedMembersData.get(j).getContactImage());
                member.put("memberStatus", mSelectedMembersData.get(j).getContactStatus());


                member.put("memberIsStar", mSelectedMembersData.get(j).isStar());


                member.put("memberIsAdmin", false);
                members.put(member);
            }
            Intent i = new Intent(CreateGroup.this, AddSubject.class);

            i.putExtra("members", members.toString());
            startActivity(i);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void removeMember(String contactUid) {


        int i = findPositionInSelectedMembers(contactUid);


        if (i != -1) {


            mSelectedMembersData.remove(i);

            if (i == 0) {
                mAdapter2.notifyDataSetChanged();
            } else {
                mAdapter2.notifyItemChanged(i);
            }
        }


        i = findPosition(contactUid);


        if (i != -1) {

            Forward_ContactItem contactItem = mContactData.get(i);
            contactItem.setSelected(false);

            mContactData.set(i, contactItem);
            mAdapter.notifyItemChanged(i);
        }


        count--;
        updateSelectedMembersText();

    }


    public void showNoSearchResults(final CharSequence constraint, boolean flag) {

        try {
            if (flag) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        noMatch.setVisibility(View.GONE);


                        if (mContactData.size() == 0) {


                            messageContact.setVisibility(View.VISIBLE);
                        } else {

                            messageContact.setVisibility(View.GONE);
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
                                messageContact.setVisibility(View.GONE);

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

    /*
     * To remove the blocked user from the list to whom the message can be forwarded
     *
     *
     * As of now,only feature to remove a contact from the list is there,not the option to update the list incase you are unblocked
     *
     */


    private void removeFromContacts(String opponentId) {


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


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
