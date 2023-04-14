package chat.hola.com.app.home.connect;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Adapters.ChatlistAdapter;
import chat.hola.com.app.Adapters.GroupChatlistAdapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.GroupChat.Activities.CreateGroup;
import chat.hola.com.app.GroupChat.Activities.GroupChatMessageScreen;
import chat.hola.com.app.ModelClasses.ChatlistItem;
import chat.hola.com.app.SecretChat.SecretChatMessageScreen;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.SlackLoadingView;
import chat.hola.com.app.Utilities.TimestampSorter;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by moda on 05/08/17.
 */


/**
 * BUG TO BE SOLVED LATER FOR SHOWING REGISTERED NAME AT TIME OF SECRET CHAT INITIATED
 */
@SuppressLint("ValidFragment")

public class GroupChatsFragment extends Fragment implements View.OnClickListener {

    //    public ConnectFragment connectFragment;
    private SessionApiCall sessionApiCall = new SessionApiCall();


    public GroupChatsFragment() {
    }
//    public ChatsFragment(ConnectFragment connectFragment) {
//        this.connectFragment = connectFragment;
//    }

    private static Bus bus = AppController.getBus();
    final String[] dTimeForDB = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "30", "60", "3600", "86400", "604800"};//getResources().getStringArray(R.array.dTimeForDB);
    final String[] dTimeOptions = {"off", "1 second", "2 seconds", "3 seconds", "4 seconds", "5 seconds", "6 seconds", "7 seconds", "8 seconds", "9 seconds", "10 seconds", "15 seconds", "30 seconds", "1 minute", "1 hour", "1 day", "1 week"};//getResources().getStringArray(R.array.dTimeOptions);
    private GroupChatlistAdapter mAdapter;

    private RecyclerView recyclerView_chat;
    private ArrayList<ChatlistItem> mChatData;
    private ArrayList<ChatlistItem> mSearchableData;
    //    private SearchView searchView;
    private LinearLayout llEmpty;
    private Typeface tf;
    private View view;
    private boolean firstTime = true;
    private CoordinatorLayout searchRoot;
    private CoordinatorLayout root;

    private String contactNumberToSave;
    public int unreadChatsCount = 0;
    private SlackLoadingView slack;
    private LandingActivity mActivity;
    private FloatingActionButton faBtn;
    public SearchView searchView;
    RelativeLayout rel_new_add;


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    @Override
    @SuppressWarnings("unchecked")

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (view == null) {

            view = inflater.inflate(R.layout.activity_retrieve_chatlist, container, false);
        } else {

            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
        }

//        connectFragment = new ConnectFragment();
//        connectFragment.setSearchListner(this);
        root = (CoordinatorLayout) view.findViewById(R.id.root);

        slack = (SlackLoadingView) view.findViewById(R.id.slack);
        RelativeLayout refresh_rl = (RelativeLayout) view.findViewById(R.id.refresh_rl);
        rel_new_add = (RelativeLayout) view.findViewById(R.id.rel_new_add);
        RelativeLayout rel_add_new_contact = (RelativeLayout) view.findViewById(R.id.rel_add_new_contact);
        RelativeLayout rel_create_new_group = (RelativeLayout) view.findViewById(R.id.rel_create_new_group);
        RelativeLayout rel_create_new_section = (RelativeLayout) view.findViewById(R.id.rel_create_new_section);

        searchRoot = (CoordinatorLayout) view.findViewById(R.id.root2);
        llEmpty = (LinearLayout) view.findViewById(R.id.llEmpty);
        mActivity = (LandingActivity) getActivity();
        faBtn = (FloatingActionButton) view.findViewById(R.id.faBtn);
        faBtn.setOnClickListener(this);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        close.setVisibility(View.GONE);
        //mActivity.visibleActionBar();
        ImageView imgBack = (ImageView) view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rel_new_add.setVisibility(View.GONE);
            }
        });
        ImageView img_add_chat = (ImageView) view.findViewById(R.id.img_add_chat);

        img_add_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rel_new_add.setVisibility(View.VISIBLE);
            }
        });

        rel_create_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateGroup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        rel_add_new_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
                addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivity(addContactIntent);

            }
        });

        rel_create_new_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        searchView = (SearchView) view.findViewById(R.id.search_view);

        searchView.setIconified(true);
        searchView.setIconifiedByDefault(true);


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
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
                onQueryTextChanges(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onClosed();
                return false;
            }
        });

        mChatData = new ArrayList<>();
        mSearchableData = new ArrayList<>();
        recyclerView_chat = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView_chat.setHasFixedSize(true);
        mAdapter = new GroupChatlistAdapter(getActivity(), mChatData, this);
        recyclerView_chat.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView_chat.setItemAnimator(new DefaultItemAnimator());
        recyclerView_chat.setAdapter(mAdapter);


        ItemTouchHelper.Callback callback = new ChatMessageTouchHelper(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView_chat);

        //   ContactsFragment parentFrag = ((ConnectFragment)getConn;
        /// parentFrag.someMethod();

        recyclerView_chat.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView_chat, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (position >= 0) {
                    final ChatlistItem item = mAdapter.getList().get(position);


                    Intent intent;


                    if (item.isGroupChat()) {
                        intent = new Intent(view.getContext(), GroupChatMessageScreen.class);
                    } else {
                        if (item.isSecretChat()) {
                            intent = new Intent(view.getContext(), SecretChatMessageScreen.class);
                        } else {
                            intent = new Intent(view.getContext(), ChatMessageScreen.class);
                        }
                    }

                    intent.putExtra("receiverUid", item.getReceiverUid());
                    intent.putExtra("receiverName", item.getReceiverName());
                    intent.putExtra("isStar", item.isStar());
                    intent.putExtra("documentId", item.getDocumentId());
                    intent.putExtra("receiverIdentifier", item.getReceiverIdentifier());

                    intent.putExtra("receiverImage", item.getReceiverImage());
                    intent.putExtra("colorCode", AppController.getInstance().getColorCode(position % 19));

                    if (item.isGroupChat()) {

                        /*
                         * GroupChat
                         */

                        if (item.isFromSearchMessage()) {
                            intent.putExtra("fromSearchMessage", true);

                            intent.putExtra("messagePosition", item.getMessagePosition());

                        } else {
                            intent.putExtra("fromSearchMessage", false);
                        }
                    } else {
                        /*
                         * Normal and the secret chat
                         */


                        if (!item.isSecretChat()) {
                            /*
                             *Non-secret chat
                             */
                            if (item.isFromSearchMessage()) {
                                intent.putExtra("fromSearchMessage", true);

                                intent.putExtra("messagePosition", item.getMessagePosition());

                            } else {
                                intent.putExtra("fromSearchMessage", false);
                            }


                        }


                    }


                    /*
                     * To clear the unread messages count, when clicked on that chat
                     */

                    if (item.hasNewMessage()) {

                        item.sethasNewMessage(false);


                    }

//                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());

                    startActivity(intent);
//                    searchView.setIconified(true);
//                    searchView.setIconifiedByDefault(true);
//                    searchView.setQuery("", true);
//                    searchView.clearFocus();

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });


        //searchView = (SearchView) view.findViewById(R.id.search);
//        searchView = connectFragment.search;


//        searchView.setIconified(true);
//        searchView.setIconifiedByDefault(true);
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//
//                searchView.setIconifiedByDefault(true);
//                searchView.setIconified(true);
//                searchView.setQuery("", false);
//                searchView.clearFocus();
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
////                mAdapter.getFilter().filter(newText);
//
//
//                if (newText.isEmpty()) {
//                    addChat();t
//                } else {
//                    showLocalSearchResults(AppController.getInstance().getDbController().searchTextMessages(newText), newText);
//                }
//
////                if (!newText.isEmpty()) {
////
////
////                }
//                return false;
//            }
//        });
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//
//
//                addChat();
//                return false;
//            }
//        });
//
//        searchView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorHintOfRed));


        TextView title = (TextView) view.findViewById(R.id.title);


        tf = AppController.getInstance().getRegularFont();
        title.setTypeface(new TypefaceManager(getActivity()).getMediumFont());


        refresh_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*
                 *To avoid the bug of progress bar being shown continously incase of poor or no internet,
                 * when refresh chats has been clicked
                 */

                refreshClicked();

            }
        });


        if (!AppController.getInstance().getChatSynced()) {

            if (AppController.getInstance().isFriendsFetched()) {

                refreshClicked();
            }


        }

        return view;

    }

    public void refreshClicked() {


        if (AppController.getInstance().canPublish()) {
            try {


                AppController.getInstance().setChatSynced(false);

                if (!AppController.getInstance().getChatSynced()) {


                    JSONObject obj = new JSONObject();

                    obj.put("eventName", "SyncChats");
                    bus.post(obj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {


            if (root != null) {


                Snackbar snackbar = Snackbar.make(root, getString(R.string.No_Internet_Connection_Available), Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view2 = snackbar.getView();
                TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            }


        }
    }

    public void showNoSearchResults(CharSequence constraint) {

        Snackbar snackbar = Snackbar
                .make(searchRoot, getString(R.string.string_948) + " " + constraint, Snackbar.LENGTH_SHORT);


        //snackbar.setMaxWidth(3000); //for fullsize on tablets
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.WHITE);


        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_text_black));
        snackbar.show();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResume() {
        super.onResume();
        rel_new_add.setVisibility(View.GONE);

        if (firstTime) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null)
                        addChat();
                }
            }, 500);

            firstTime = false;
        } else {
            if (getActivity() != null)
                addChat();
        }

        if (mChatData != null && mChatData.size() > 0) {

            if (llEmpty != null) {


                llEmpty.setVisibility(View.GONE);


            } else {


                TextView tv = (TextView) view.findViewById(R.id.notLoggedIn);
                tv.setVisibility(View.GONE);


            }
        }


//        AppController.getInstance().setUnreadChatCount(0);
//        AppController.getInstance().getDbController().clearUnreadChatsReceiverUids(AppController.getInstance().getChatDocId());


//        AppController.initMQttMessageHandlerInstance(handler);


    }

    /**
     * To fetch the chats from the couchdb, stored locally
     */

    @SuppressWarnings("unchecked")

    public void addChat() {


        unreadChatsCount = 0;

        if (getActivity() != null) {
            mChatData.clear();


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

//                        mAdapter.notifyItemInserted(mProductData.size() - 1);


                    if (mAdapter != null)
                        mAdapter.notifyDataSetChanged();


                }
            });


            CouchDbController db = AppController.getInstance().getDbController();


            Map<String, Object> map = db.getAllChatDetails(AppController.getInstance().getChatDocId());


            if (map != null) {


                ArrayList<String> receiverUidArray = (ArrayList<String>) map.get("receiverUidArray");

                ArrayList<String> receiverDocIdArray = (ArrayList<String>) map.get("receiverDocIdArray");


                Map<String, Object> chat_item;


                ChatlistItem chat;


                ArrayList<Map<String, Object>> chats = new ArrayList<>();
                for (int i = 0; i < receiverUidArray.size(); i++) {

                    Map<String, Object> groupChat = db.getParticularChatInfo(receiverDocIdArray.get(i), true);
                    if (groupChat != null) {
                        chats.add(groupChat);

                    }
                    Collections.sort(chats, new TimestampSorter());

                }

                String contactDocId = AppController.getInstance().getFriendsDocId();


                Map<String, Object> contactInfo;
                for (int i = 0; i < chats.size(); i++) {
                    chat_item = chats.get(i);


                    chat = new ChatlistItem();

                    boolean hasNewMessage = (boolean) chat_item.get("hasNewMessage");
                    chat.sethasNewMessage(hasNewMessage);


                    try {

                        if (chat_item.get("groupMemberInfo") != null) {

                            String groupMembersString = (String) chat_item.get("groupMemberInfo");

                            chat.setMembersInfo(groupMembersString);
                            Log.d("GroupMemberInfo", "===>>" + chat_item.get("groupMemberInfo"));

                        }

                        if (((String) chat_item.get("secretId")).isEmpty()) {
                            chat.setSecretChat(false);

                            chat.setSecretId("");

                        } else {
                            chat.setSecretChat(true);

                            chat.setSecretId((String) chat_item.get("secretId"));
                        }
                    } catch (NullPointerException e) {

                        chat.setSecretChat(false);


                        chat.setSecretId("");
                    }
                    if (hasNewMessage) {

                        unreadChatsCount++;
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
                        } else {
                            boolean isGroup = chat_item.containsKey("groupChat") && (boolean) chat_item.get("groupChat");
                            if (message.equals("No Messages To Show!!") && !isGroup) {
                                continue;
                            }
                        }
                    }

                    chat.setReceiverIdentifier((String) chat_item.get("receiverIdentifier"));
                    chat.setDocumentId((String) chat_item.get("selfDocId"));

                    chat.setReceiverUid((String) chat_item.get("selfUid"));

                    if (chat_item.containsKey("isStar"))
                        chat.setStar((boolean) chat_item.get("isStar"));
                    else
                        chat.setStar(false);


                    if (chat_item.containsKey("groupChat") && (boolean) chat_item.get("groupChat")) {


                        /*
                         *Given chat is a group chat
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
                            //chat.setReceiverName((String) chat_item.get("receiverIdentifier"));
                            chat.setReceiverName((String) chat_item.get("receiverName"));
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
                    mSearchableData.add(chat);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


//                            mAdapter.notifyDataSetChanged();

                            mAdapter.notifyItemInserted(mChatData.size() - 1);
                        }
                    });


                }


                recyclerView_chat.scrollToPosition(0);

            }


            if (mChatData.size() == 0) {

                if (llEmpty != null) {


                    llEmpty.setVisibility(View.VISIBLE);

//                    try {
//                        tv.setTypeface(tf, Typeface.NORMAL);
//
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    }
//                    tv.setText(R.string.string_245);
                } else {


                    TextView tv = (TextView) view.findViewById(R.id.notLoggedIn);


                    tv.setVisibility(View.VISIBLE);
                    try {
                        tv.setTypeface(tf, Typeface.NORMAL);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    tv.setText(getString(R.string.string_245));
                }

            } else {
                if (llEmpty != null)
                    llEmpty.setVisibility(View.GONE);
            }


            if (unreadChatsCount == 0) {

                ((LandingActivity) getActivity()).updateChatBadge(false, -1);

            } else {

                if (unreadChatsCount > 0) {
                    ((LandingActivity) getActivity()).updateChatBadge(true, unreadChatsCount);
                }

            }
        }
    }

    public int alreadyInContact(String sender, String secretId) {
        int j = -1;


        for (int i = 0; i < mChatData.size(); i++) {


            if (mChatData.get(i).getReceiverUid().equals(sender)) {

                /*
                 * This code is commented as it fails incase of normal chat message received and we have a secret chat above it in list
                 */
//                if (secretId.isEmpty()) {
//                    j = i;
//                    break;
//
//                } else {
//                    if (mChatData.get(i).getSecretId().equals(secretId)) {
//
//                        j = i;
//                        break;
//
//                    }
//
//                }

                if (mChatData.get(i).getSecretId().equals(secretId)) {

                    j = i;
                    break;

                }
            }
        }


        return j;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {


            if (getActivity() != null) {

                hideKeyboard(getActivity());

            }
        }
//        else if (searchView != null) {
//
//            if (!searchView.isIconified()) {
//
//
//                searchView.setIconified(true);
//
//                if (getActivity() != null)
//                    addChat();
//            }
//
//        }


    }

    /*
     * Have to register the bus for updating the Profile pic in the calls list
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bus.register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {

            if (object.getString("eventName").equals("postSent")) {
                /*Get when user send post in chat*/
                addChat();
            } else if (object.getString("eventName").equals(MqttEvents.UserUpdates.value + "/" + AppController.getInstance().getUserId())) {


                switch (object.getInt("type")) {
                    case 2:


                        /*
                         * Profile pic update
                         */


                        String profilePic = object.getString("profilePic");
                        ArrayList<Integer> arr = findContactPositionInList(object.getString("userId"));

                        if (arr.size() > 0) {
                            for (int i = 0; i < arr.size(); i++) {

                                ChatlistItem item = mChatData.get(arr.get(i));


                                item.setReceiverImage(profilePic);
                                mChatData.set(arr.get(i), item);


                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        break;
                    case 4:

                        switch (object.getInt("subtype")) {

                            case 0:

                                /*
                                 * Follow name or number changed but number still valid
                                 */


                                ArrayList<Integer> arr2 = findContactPositionInList(object.getString("contactUid"));

                                if (arr2.size() > 0) {
                                    for (int i = 0; i < arr2.size(); i++) {

                                        ChatlistItem item = mChatData.get(arr2.get(i));


                                        item.setReceiverName(object.getString("contactName"));
                                        mChatData.set(arr2.get(i), item);


                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                                break;
                            case 1:
                                /*
                                 * Number of active contact changed and new number not in contact
                                 */


                                ArrayList<Integer> arr3 = findContactPositionInList(object.getString("contactUid"));

                                if (arr3.size() > 0) {
                                    for (int i = 0; i < arr3.size(); i++) {

                                        ChatlistItem item = mChatData.get(arr3.get(i));


                                        item.setReceiverName(item.getReceiverIdentifier());
                                        mChatData.set(arr3.get(i), item);


                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }


                                break;

                            case 2:

                                /*
                                 * New contact added
                                 */


                                ArrayList<Integer> arr4 = findContactPositionInList(object.getString("contactUid"));

                                if (arr4.size() > 0) {
                                    for (int i = 0; i < arr4.size(); i++) {

                                        ChatlistItem item = mChatData.get(arr4.get(i));

                                        String profilePic2 = "";


                                        if (object.has("contactPicUrl")) {
                                            profilePic2 = object.getString("contactPicUrl");
                                        }
                                        item.setReceiverImage(profilePic2);

                                        item.setReceiverName(object.getString("contactName"));
                                        mChatData.set(arr4.get(i), item);


                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }


                                break;
                        }
                        break;
                    case 5:

                        /*
                         * Number was in active contact
                         */


                        object = object.getJSONArray("contacts").getJSONObject(0);
                        if (object.has("status") && object.getInt("status") == 0) {

                            ArrayList<Integer> arr2 = findContactPositionInList(object.getString("userId"));

                            if (arr2.size() > 0) {
                                for (int i = 0; i < arr2.size(); i++) {

                                    ChatlistItem item = mChatData.get(arr2.get(i));


                                    item.setReceiverName(item.getReceiverIdentifier());
                                    mChatData.set(arr2.get(i), item);


                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                        break;


                    case 3:


                        ArrayList<Integer> arr2 = findInactiveContactPositionInList(object.getString("number"));

                        if (arr2.size() > 0) {
                            for (int i = 0; i < arr2.size(); i++) {

                                ChatlistItem item = mChatData.get(arr2.get(i));


                                item.setReceiverName(object.getString("name"));
                                mChatData.set(arr2.get(i), item);


                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        break;

                }


            } else if (object.getString("eventName").equals("ContactNameUpdated")) {

                ArrayList<Integer> arr = findContactPositionInList(object.getString("contactUid"));

                if (arr.size() > 0) {
                    for (int i = 0; i < arr.size(); i++) {

                        ChatlistItem item = mChatData.get(arr.get(i));


                        item.setReceiverName(object.getString("contactName"));
                        mChatData.set(arr.get(i), item);


                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            } else if (object.getString("eventName").equals("contactRefreshed")) {

                addChat();
            } else if (object.getString("eventName").equals(MqttEvents.FetchChats.value + "/" + AppController.getInstance().getUserId())) {


                try {
                    if (mChatData.size() > 0) {
                        mChatData.clear();


                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (slack != null)
                    slack.setVisibility(View.GONE);
                addChat();
                //addChatsFetchedFromServer(object.getJSONArray("chats"));


                AppController.getInstance().setChatSynced(true);


            } else if (object.getString("eventName").equals("SyncChats")) {

                Log.d("SyncChat Event", "Call");

                /*
                 * If logging in for the first time,then have to sync the chats
                 */

                if (AppController.getInstance().isFriendsFetched()) {
                    if (AppController.getInstance().canPublish()) {
                        AppController.getInstance().subscribeToTopic(MqttEvents.FetchChats.value + "/" + AppController.getInstance().getUserId(), 1);


                        try {
                            if (mChatData.size() > 0) {
                                mChatData.clear();


                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        fetchChatHistory();
                    } else {


                        if (root != null) {
                            Snackbar snackbar = Snackbar.make(root, getString(R.string.No_Internet_Connection_Available), Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }
                }
            } else if (object.getString("eventName").equals(MqttEvents.Message.value + "/" + AppController.getInstance().getUserId())) {
                String sender = object.getString("from");
                String timestamp = object.getString("timestamp");
                String messageType = object.getString("type");

                if (messageType.equals("15")) {
                    if (sender.equals(AppController.getInstance().getUserId())) {
                        sender = object.getString("to");
                    }
                }

                // String name = object.getString("name");


                String message;


                switch (Integer.parseInt(messageType)) {

                    case 0:

                        message = object.getString("payload").trim();


                        if (message.isEmpty()) {


                            String message_dTime = String.valueOf(object.getLong("dTime"));


                            if (!message_dTime.equals("-1")) {

                                for (int i = 0; i < dTimeForDB.length; i++) {
                                    if (message_dTime.equals(dTimeForDB[i])) {

                                        if (i == 0) {


                                            message = getString(R.string.Timer_set_off);
                                        } else {


                                            message = getString(R.string.Timer_set_to) + " " + dTimeOptions[i];

                                        }
                                        break;
                                    }
                                }

                            } else {


                                message = getResources().getString(R.string.youAreInvited) + " " + object.getString("contactName") + " " +
                                        getResources().getString(R.string.JoinSecretChat);

                            }
                        } else {


                            try {
                                message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case 1:

                        message = getString(R.string.NewImage);
                        break;
                    case 2:
                        message = getString(R.string.NewVideo);
                        break;
                    case 3:
                        message = getString(R.string.NewLocation);
                        break;
                    case 4:
                        message = getString(R.string.NewContact);
                        break;
                    case 5:
                        message = getString(R.string.NewAudio);
                        break;
                    case 6:
                        message = getString(R.string.NewSticker);
                        break;
                    case 7:
                        message = getString(R.string.NewDoodle);
                        break;

                    case 8:
                        message = getString(R.string.NewGiphy);
                        break;
                    case 9:
                        message = getString(R.string.NewDocument);
                        break;

                    case 13:
                        message = getString(R.string.NewPost);
                        break;
                    case 15:
                        message = getString(R.string.new_transfer);
                        break;
                    case 16:
                        message = getString(R.string.missed_call);
                        break;
                    case 17:
                        message = getString(R.string.call);
                        break;

                    case 11: {

                        /*
                         * Message removed
                         */


                        message = object.getString("payload").trim();
                        try {
                            message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        handleMessageRemovedSignal(message, sender, object.getString("removedAt"), object);
                        return;
                    }


                    case 12: {

                        /*
                         * Message edited
                         */

                        message = object.getString("payload").trim();
                        try {
                            message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        handleMessageEditedSignal(message, sender, object.getString("editedAt"), object);
                        return;
                    }

                    default: {

                        switch (Integer.parseInt(object.getString("replyType"))) {

                            case 0:
                                message = object.getString("payload").trim();


                                try {
                                    message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:

                                message = getString(R.string.NewImage);
                                break;
                            case 2:
                                message = getString(R.string.NewVideo);
                                break;
                            case 3:
                                message = getString(R.string.NewLocation);
                                break;
                            case 4:
                                message = getString(R.string.NewContact);
                                break;
                            case 5:
                                message = getString(R.string.NewAudio);
                                break;
                            case 6:
                                message = getString(R.string.NewSticker);
                                break;
                            case 7:
                                message = getString(R.string.NewDoodle);
                                break;

                            case 8:
                                message = getString(R.string.NewGiphy);
                                break;

                            case 13:
                                message = getString(R.string.NewPost);
                                break;
                            default:
                                message = getString(R.string.NewDocument);
                                break;


                        }


                    }


                }


                String secretId = "";

                if (object.has("secretId")) {

                    secretId = object.getString("secretId");

                }


                boolean isStar = false;
                if (object.has("isStar")) {

                    isStar = object.getBoolean("isStar");

                }

                ChatlistItem chat = new ChatlistItem();
                chat.setReceiverUid(sender);
                chat.setNewMessage(message);
                chat.setSecretId(secretId);
                chat.setStar(isStar);
                chat.sethasNewMessage(true);
                chat.setDocumentId(AppController.getInstance().findDocumentIdOfReceiver(sender, secretId));
                chat.setSecretChat(!secretId.isEmpty());
                chat.setReceiverIdentifier(object.getString("receiverIdentifier"));



                /*
                 * If the uid exists in contacts
                 */


                Map<String, Object> contactInfo = AppController.getInstance().getDbController().
                        getFriendInfoFromUid(AppController.getInstance().getFriendsDocId(), sender);

                if (contactInfo != null) {
                    chat.setReceiverName(contactInfo.get("firstName") + " " + contactInfo.get("lastName"));


                    String contactPicUrl = (String) contactInfo.get("profilePic");

                    if (contactPicUrl != null && !contactPicUrl.isEmpty()) {
                        chat.setReceiverImage(contactPicUrl);
                    } else {
                        chat.setReceiverImage("");

                    }


                    chat.setReceiverInContacts(true);
                } else {
                    /*
                     * If userId doesn't exists in contact
                     */
                    chat.setReceiverName(object.getString("receiverIdentifier"));
                    if (object.has("userImage")) {
                        chat.setReceiverImage(object.getString("userImage"));
                    } else {
                        chat.setReceiverImage("");
                    }
                    chat.setReceiverInContacts(true);
                }


                chat.setNewMessageTime(Utilities.epochtoGmt(timestamp));
                chat.setNewMessageCount(AppController.getInstance().getDbController().getNewMessageCount(
                        AppController.getInstance().findDocumentIdOfReceiver(sender, secretId)));


                int alreadyInContact = alreadyInContact(sender, secretId);

                if (alreadyInContact == -1) {

                    unreadChatsCount++;


                    ((LandingActivity) getActivity()).updateChatBadge(true, unreadChatsCount);


                    mChatData.add(0, chat);
                    mSearchableData.add(0, chat);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } else {


                    if (!mChatData.get(alreadyInContact).hasNewMessage()) {

                        unreadChatsCount++;


                        ((LandingActivity) getActivity()).updateChatBadge(true, unreadChatsCount);


                    }


                    mChatData.remove(alreadyInContact);
                    mSearchableData.remove(alreadyInContact);
                    mChatData.add(0, chat);
                    mSearchableData.add(0, chat);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mAdapter.notifyDataSetChanged();
                        }
                    });


                }


                if (llEmpty != null && llEmpty.getVisibility() == View.VISIBLE) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            llEmpty.setVisibility(View.GONE);
                        }
                    });
                }


            } else if (object.getString("eventName").equals(MqttEvents.Acknowledgement.value + "/" + AppController.getInstance().getUserId())) {

                /*
                 * Will update status only if not active on any of the chats
                 */
                if (AppController.getInstance().getActiveReceiverId().isEmpty()) {


                    String receiverId = object.getString("from");

                    String secretId = "";


                    if (object.has("secretId")) {


                        secretId = object.getString("secretId");
                    }

                    /*
                     * Although NOT THE BEST WAY,CAUSE NEED LAST MESSAGE ID FROM THE SERVER,but have done it for the time being
                     *
                     */

                    updateLastMessageDeliveryStatus(receiverId, secretId, object.getString("msgId"), Integer.parseInt(object.getString("status")));
                }

            } else if (object.getString("eventName").equals(MqttEvents.MessageResponse.value)) {
                /*
                 * Will update status only if not active on any of the chats
                 */
                if (AppController.getInstance().getActiveReceiverId().isEmpty()) {


                    updateLastMessageDeliveryStatus(object.getString("docId"), object.getString("messageId"));

                }

            } else if (object.getString("eventName").equals(MqttEvents.GroupChats.value + "/" + AppController.getInstance().getUserId())) {


                if (object.has("payload")) {


                    String sender = object.getString("groupId");
                    String timestamp = object.getString("timestamp");
                    String messageType = object.getString("type");


                    String message;


                    switch (Integer.parseInt(messageType)) {

                        case 0:

                            message = object.getString("payload").trim();


                            try {
                                message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                            break;
                        case 1:

                            message = getString(R.string.NewImage);
                            break;
                        case 2:
                            message = getString(R.string.NewVideo);
                            break;
                        case 3:
                            message = getString(R.string.NewLocation);
                            break;
                        case 4:
                            message = getString(R.string.NewContact);
                            break;
                        case 5:
                            message = getString(R.string.NewAudio);
                            break;
                        case 6:
                            message = getString(R.string.NewSticker);
                            break;
                        case 7:
                            message = getString(R.string.NewDoodle);
                            break;

                        case 8:
                            message = getString(R.string.NewGiphy);
                            break;
                        case 9:
                            message = getString(R.string.NewDocument);
                            break;

                        case 13:
                            message = getString(R.string.NewPost);
                            break;
                        case 11: {

                            /*
                             *
                             *Message removed
                             * */
                            message = object.getString("payload").trim();


                            try {
                                message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            handleGroupMessageRemovedSignal(message, sender, object.getString("removedAt"), object);
                            return;


                        }


                        case 12: {
                            /*
                             *
                             *Message edited
                             * */

                            message = object.getString("payload").trim();


                            try {
                                message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            handleGroupMessageEditedSignal(message, sender, object.getString("editedAt"), object);
                            return;
                        }


                        default: {

                            switch (Integer.parseInt(object.getString("replyType"))) {

                                case 0:
                                    message = object.getString("payload").trim();


                                    try {
                                        message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 1:

                                    message = getString(R.string.NewImage);
                                    break;
                                case 2:
                                    message = getString(R.string.NewVideo);
                                    break;
                                case 3:
                                    message = getString(R.string.NewLocation);
                                    break;
                                case 4:
                                    message = getString(R.string.NewContact);
                                    break;
                                case 5:
                                    message = getString(R.string.NewAudio);
                                    break;
                                case 6:
                                    message = getString(R.string.NewSticker);
                                    break;
                                case 7:
                                    message = getString(R.string.NewDoodle);
                                    break;

                                case 8:
                                    message = getString(R.string.NewGiphy);
                                    break;

                                case 13:
                                    message = getString(R.string.NewPost);
                                    break;
                                default:
                                    message = getString(R.string.NewDocument);
                                    break;


                            }


                        }


                    }


                    ChatlistItem chat = new ChatlistItem();
                    chat.setReceiverUid(sender);
                    chat.setNewMessage(message);
                    chat.setSecretId("");
                    chat.sethasNewMessage(true);


                    chat.setDocumentId(AppController.getInstance().findDocumentIdOfReceiver(sender, ""));
                    chat.setSecretChat(false);
                    chat.setReceiverIdentifier(sender);
                    chat.setGroupChat(true);


                    /*
                     * If the uid exists in contacts
                     */



                    /*
                     * If userId doesn't exists in contact
                     */
                    chat.setReceiverName(object.getString("name"));
                    if (object.has("userImage")) {
                        chat.setReceiverImage(object.getString("userImage"));
                    } else {
                        chat.setReceiverImage("");
                    }
                    chat.setReceiverInContacts(true);


                    chat.setNewMessageTime(Utilities.epochtoGmt(timestamp));
                    chat.setNewMessageCount(AppController.getInstance().getDbController().getNewMessageCount(
                            AppController.getInstance().findDocumentIdOfReceiver(sender, "")));


                    int alreadyInContact = alreadyInContact(sender, "");

                    if (alreadyInContact == -1) {

                        unreadChatsCount++;


                        ((LandingActivity) getActivity()).updateChatBadge(true, unreadChatsCount);


                        mChatData.add(0, chat);
                        mSearchableData.add(0, chat);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {


                        if (!mChatData.get(alreadyInContact).hasNewMessage()) {

                            unreadChatsCount++;


                            ((LandingActivity) getActivity()).updateChatBadge(true, unreadChatsCount);


                        }


                        mChatData.remove(alreadyInContact);
                        mSearchableData.remove(alreadyInContact);
                        mChatData.add(0, chat);
                        mSearchableData.add(0, chat);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mAdapter.notifyDataSetChanged();
                            }
                        });


                    }


                    if (llEmpty != null && llEmpty.getVisibility() == View.VISIBLE) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                llEmpty.setVisibility(View.GONE);
                            }
                        });
                    }


                } else {


                    if (!object.has("self")) {

                        String sender = object.getString("groupId");


                        ChatlistItem chat = new ChatlistItem();
                        chat.setReceiverUid(sender);
                        chat.setGroupChat(true);
                        chat.setNewMessage(object.getString("message"));

                        chat.sethasNewMessage(true);
                        chat.setDocumentId(object.getString("docId"));
                        chat.setSecretChat(false);
                        chat.setSecretId("");

                        chat.setReceiverIdentifier(sender);
                        chat.setReceiverInContacts(true);
                        chat.setNewMessageTime(object.getString("timestamp"));
                        chat.setNewMessageCount(AppController.getInstance().getDbController().getNewMessageCount(
                                object.getString("docId")));




                        /*
                         * If userId doesn't exists in contact
                         */


                        int alreadyInContact = alreadyInContact(sender, "");


                        switch (object.getInt("type")) {

                            case 0: {


                                /*
                                 * Group created
                                 */


                                chat.setReceiverName(object.getString("groupSubject"));
                                if (object.has("groupImageUrl")) {
                                    chat.setReceiverImage(object.getString("groupImageUrl"));
                                } else {
                                    chat.setReceiverImage("");
                                }
                                break;
                            }


                            case 1: {
                                /*
                                 * Member added
                                 */


                                if (object.getString("memberId").equals(AppController.getInstance().getUserId())) {

                                    if (object.has("groupImageUrl")) {
                                        chat.setReceiverImage(object.getString("groupImageUrl"));
                                    } else {
                                        chat.setReceiverImage("");
                                    }

                                    chat.setReceiverName(object.getString("groupSubject"));
                                } else {

                                    if (alreadyInContact == -1) {
                                        /*
                                         * Group icon url  and subject field added manually
                                         */

                                        Map<String, Object> groupInfo = AppController.getInstance().getDbController().getGroupSubjectAndImageUrl(object.getString("docId"));

                                        try {
                                            chat.setReceiverImage((String) groupInfo.get("receiverImage"));

                                            chat.setReceiverName((String) groupInfo.get("receiverName"));
                                        } catch (Exception e) {


                                            chat.setReceiverImage("");

                                            chat.setReceiverName(getString(R.string.string_247));


                                        }
                                    } else {

                                        chat.setReceiverName(mChatData.get(alreadyInContact).getReceiverName());
                                        chat.setReceiverImage(mChatData.get(alreadyInContact).getReceiverImage());
                                    }
                                }


                            }


                            case 2: {

                                if (alreadyInContact == -1) {
                                    /*
                                     * Group icon url  and subject field added manually
                                     */

                                    Map<String, Object> groupInfo = AppController.getInstance().getDbController().getGroupSubjectAndImageUrl(object.getString("docId"));

                                    try {
                                        chat.setReceiverImage((String) groupInfo.get("receiverImage"));

                                        chat.setReceiverName((String) groupInfo.get("receiverName"));

                                    } catch (Exception e) {


                                        chat.setReceiverImage("");

                                        chat.setReceiverName(getString(R.string.string_247));


                                    }
                                } else {

                                    chat.setReceiverName(mChatData.get(alreadyInContact).getReceiverName());
                                    chat.setReceiverImage(mChatData.get(alreadyInContact).getReceiverImage());
                                }


                                break;
                            }
                            case 3: {


                                if (alreadyInContact == -1) {
                                    /*
                                     * Group icon url  and subject field added manually
                                     */

                                    Map<String, Object> groupInfo = AppController.getInstance().getDbController().getGroupSubjectAndImageUrl(object.getString("docId"));

                                    try {
                                        chat.setReceiverImage((String) groupInfo.get("receiverImage"));

                                        chat.setReceiverName((String) groupInfo.get("receiverName"));
                                    } catch (Exception e) {


                                        chat.setReceiverImage("");

                                        chat.setReceiverName(getString(R.string.string_247));


                                    }
                                } else {

                                    chat.setReceiverName(mChatData.get(alreadyInContact).getReceiverName());
                                    chat.setReceiverImage(mChatData.get(alreadyInContact).getReceiverImage());
                                }

                                break;
                            }
                            case 4: {

                                /*
                                 * Group subject updated
                                 */

                                chat.setReceiverName(object.getString("groupSubject"));


                                if (alreadyInContact == -1) {
                                    /*
                                     * Group icon url field added manually
                                     */

                                    chat.setReceiverImage(AppController.getInstance().getDbController().getGroupImageUrl(object.getString("docId")));


                                } else {


                                    chat.setReceiverImage(mChatData.get(alreadyInContact).getReceiverImage());
                                }


                                break;
                            }
                            case 5: {


                                /*
                                 * Group icon updated
                                 */
                                chat.setReceiverImage(object.getString("groupImageUrl"));


                                if (alreadyInContact == -1) {

                                    /*
                                     * Group subject field added manually
                                     */

                                    chat.setReceiverName(AppController.getInstance().getDbController().getGroupSubject(object.getString("docId")));


                                } else {


                                    chat.setReceiverName(mChatData.get(alreadyInContact).getReceiverName());
                                }


                                break;
                            }


                            case 6: {
                                /*
                                 * Member left the conversation
                                 */

                                if (alreadyInContact == -1) {
                                    /*
                                     * Group icon url  and subject field added manually
                                     */

                                    Map<String, Object> groupInfo = AppController.getInstance().getDbController().getGroupSubjectAndImageUrl(object.getString("docId"));

                                    try {
                                        chat.setReceiverImage((String) groupInfo.get("receiverImage"));

                                        chat.setReceiverName((String) groupInfo.get("receiverName"));
                                    } catch (Exception e) {


                                        chat.setReceiverImage("");

                                        chat.setReceiverName(getString(R.string.string_247));


                                    }
                                } else {

                                    chat.setReceiverName(mChatData.get(alreadyInContact).getReceiverName());
                                    chat.setReceiverImage(mChatData.get(alreadyInContact).getReceiverImage());
                                }
                            }

                        }


                        if (alreadyInContact == -1) {

                            unreadChatsCount++;


                            ((LandingActivity) getActivity()).updateChatBadge(true, unreadChatsCount);


                            mChatData.add(0, chat);
                            mSearchableData.add(0, chat);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {


                            if (!mChatData.get(alreadyInContact).hasNewMessage()) {

                                unreadChatsCount++;


                                ((LandingActivity) getActivity()).updateChatBadge(true, unreadChatsCount);


                            }


                            mChatData.remove(alreadyInContact);
                            mSearchableData.remove(alreadyInContact);
                            mChatData.add(0, chat);
                            mSearchableData.add(0, chat);


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mAdapter.notifyDataSetChanged();
                                }
                            });


                        }


                        if (llEmpty != null && llEmpty.getVisibility() == View.VISIBLE) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    llEmpty.setVisibility(View.GONE);
                                }
                            });
                        }

                    }
                }
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }


    private ArrayList<Integer> findContactPositionInList(String contactUid) {

        ArrayList<Integer> positions = new ArrayList<>();


        for (int i = 0; i < mChatData.size(); i++) {


            if (mChatData.get(i).getReceiverUid().equals(contactUid)) {

                positions.add(i);
            }
        }

        return positions;
    }


    private ArrayList<Integer> findInactiveContactPositionInList(String contactNumber) {

        ArrayList<Integer> positions = new ArrayList<>();


        for (int i = 0; i < mChatData.size(); i++) {


            if (mChatData.get(i).getReceiverIdentifier().equals(contactNumber)) {

                positions.add(i);
            }
        }

        return positions;
    }


    /*
     * To fetch the list of the chats in the background
     */


    private void fetchChatHistory() {

        if (slack != null) {
            /*
             * To avoid first time showing strange text after login
             */

            if (llEmpty != null) {


                llEmpty.setVisibility(View.GONE);


            } else {


                TextView tv = (TextView) view.findViewById(R.id.notLoggedIn);


                tv.setVisibility(View.GONE);

            }

            slack.setVisibility(View.VISIBLE);


            slack.start();
        }

        JSONObject object = new JSONObject();
        try {
            object.put("pageNo", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ApiOnServer.FETCH_CHATS, object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {


                    if (slack != null)
                        slack.setVisibility(View.GONE);
                    if (response.getInt("code") != 200) {

                        if (root != null) {
                            Snackbar snackbar = Snackbar.make(root, getString(R.string.Chat_Sync), Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }

                    } else {

                    }

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (slack != null)
                        slack.setVisibility(View.GONE);

                    if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                        SessionObserver sessionObserver = new SessionObserver();
                        sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<Boolean>() {
                                    @Override
                                    public void onNext(Boolean flag) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                fetchChatHistory();
                                            }
                                        }, 1000);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });
                        sessionApiCall.getNewSession(sessionObserver);
                    } else if (root != null) {


                        Snackbar snackbar = Snackbar.make(root, getString(R.string.No_Internet_Connection_Available), Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                    }
                } catch (Exception ignore) {
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lang", Constants.LANGUAGE);

                return headers;
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "getChatsApi");


    }

    /*
     * To add the chats fetched from the server
     */

    private void addChatsFetchedFromServer(JSONArray chats) {


        unreadChatsCount = 0;


        JSONObject chat;
        ChatlistItem item;
        String message, receiverName, profilePic;

        Map<String, Object> contactInfo;


        String contactDocId = AppController.getInstance().getFriendsDocId();

        int unreadCount;
        for (int j = chats.length() - 1; j >= 0; j--) {


            try {


                chat = chats.getJSONObject(j);


                if (chat.getBoolean("groupChat")) {


                    /*
                     * If the corresponding chat is a group chat
                     */


                    unreadCount = chat.getInt("totalUnread");


                    if (unreadCount > 0) {
                        unreadChatsCount++;
                    }


                    item = new ChatlistItem();
                    item.setReceiverUid(chat.getString("chatId"));

                    item.setReceiverIdentifier(chat.getString("chatId"));


                    /*
                     * userName field contains the groupName
                     */
                    item.setReceiverName(chat.getString("userName"));


                    if (chat.has("profilePic") && chat.getString("profilePic") != null) {
                        item.setReceiverImage(chat.getString("profilePic"));

                    } else {

                        item.setReceiverImage("");

                    }

                    item.setNewMessageTime(Utilities.epochtoGmt(String.valueOf(chat.getLong("timestamp"))));


                    item.setDocumentId(AppController.getInstance().findDocumentIdOfReceiver(chat.getString("chatId"), ""));

                    if (chat.has("payload")) {

                        /*
                         * Last message has been normal message in the chat
                         */


                        if (chat.getString("senderId").equals(AppController.getInstance().getUserId())) {


                            item.setShowTick(true);

                            item.setTickStatus(chat.getInt("status"));
                        } else {


                            item.setShowTick(false);
                        }


                        switch (Integer.parseInt(chat.getString("messageType"))) {

                            case 0: {

                                message = chat.getString("payload").trim();


                                try {
                                    message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }


                                break;
                            }

                            case 1: {
                                if (unreadCount > 0) {
                                    message = getString(R.string.NewImage);
                                } else {

                                    message = getString(R.string.Image);
                                }
                                break;
                            }
                            case 2: {

                                if (unreadCount > 0) {
                                    message = getString(R.string.NewVideo);
                                } else {
                                    message = getString(R.string.Video);

                                }
                                break;
                            }
                            case 3: {
                                if (unreadCount > 0) {
                                    message = getString(R.string.NewLocation);
                                } else {

                                    message = getString(R.string.Location);
                                }
                                break;
                            }
                            case 4: {
                                if (unreadCount > 0) {
                                    message = getString(R.string.NewContact);
                                } else {

                                    message = getString(R.string.Contact);
                                }
                                break;
                            }
                            case 5: {
                                if (unreadCount > 0) {
                                    message = getString(R.string.NewAudio);
                                } else {

                                    message = getString(R.string.Audio);
                                }
                                break;
                            }
                            case 6: {
                                if (unreadCount > 0) {
                                    message = getString(R.string.NewSticker);
                                } else {

                                    message = getString(R.string.Stickers);
                                }
                                break;
                            }
                            case 7: {
                                if (unreadCount > 0) {
                                    message = getString(R.string.NewDoodle);
                                } else {


                                    message = getString(R.string.Doodle);
                                }
                                break;
                            }
                            case 8: {
                                if (unreadCount > 0) {
                                    message = getString(R.string.NewGiphy);
                                } else {

                                    message = getString(R.string.Giphy);
                                }
                                break;

                            }
                            case 9: {
                                if (unreadCount > 0) {
                                    message = getString(R.string.NewDocument);
                                } else {

                                    message = getString(R.string.Document);
                                }
                                break;
                            }
                            case 11: {

                                message = chat.getString("payload").trim();

                                item.setNewMessageTime(Utilities.epochtoGmt(chat.getString("removedAt")));
                                try {
                                    message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }


                                break;
                            }

                            case 13: {
                                if (unreadCount > 0) {
                                    message = getString(R.string.NewPost);
                                } else {

                                    message = getString(R.string.Post);
                                }
                                break;
                            }
                            default: {


                                switch (Integer.parseInt(chat.getString("replyType"))) {


                                    case 0: {
                                        message = chat.getString("payload").trim();


                                        try {
                                            message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }


                                        break;
                                    }
                                    case 1: {
                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewImage);
                                        } else {

                                            message = getString(R.string.Image);
                                        }
                                        break;
                                    }
                                    case 2: {

                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewVideo);
                                        } else {
                                            message = getString(R.string.Video);

                                        }
                                        break;
                                    }
                                    case 3: {
                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewLocation);
                                        } else {

                                            message = getString(R.string.Location);
                                        }
                                        break;
                                    }
                                    case 4: {
                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewContact);
                                        } else {

                                            message = getString(R.string.Contact);
                                        }
                                        break;
                                    }
                                    case 5: {
                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewAudio);
                                        } else {

                                            message = getString(R.string.Audio);
                                        }
                                        break;
                                    }
                                    case 6: {
                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewSticker);
                                        } else {

                                            message = getString(R.string.Stickers);
                                        }
                                        break;
                                    }
                                    case 7: {
                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewDoodle);
                                        } else {


                                            message = getString(R.string.Doodle);
                                        }
                                        break;
                                    }
                                    case 8: {
                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewGiphy);
                                        } else {

                                            message = getString(R.string.Giphy);
                                        }
                                        break;
                                    }
                                    case 13: {
                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewPost);
                                        } else {

                                            message = getString(R.string.Post);
                                        }
                                        break;
                                    }

                                    default: {
                                        if (unreadCount > 0) {
                                            message = getString(R.string.NewDocument);
                                        } else {

                                            message = getString(R.string.Document);
                                        }
                                        break;

                                    }
                                }


                            }


                        }


                    } else {

                        /*
                         * Last message has been a group chat tag message
                         */


                        item.setShowTick(false);


                        switch (Integer.parseInt(chat.getString("messageType"))) {
                            case 0: {
                                /*
                                 * Group created
                                 *
                                 */


                                String initiatorName;

                                if (chat.getString("initiatorId").equals(AppController.getInstance().getUserId())) {

                                    initiatorName = getString(R.string.You);
                                } else {
                                    initiatorName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = chat.getString("initiatorIdentifier");
                                    }
                                }
                                message = getString(R.string.CreatedGroup, initiatorName) + " " +
                                        chat.getString("groupSubject");

                                break;

                            }


                            case 1: {

                                /*
                                 * Member added
                                 */


                                String initiatorName, memberName;

                                if (chat.getString("initiatorId").equals(AppController.getInstance().getUserId())) {

                                    initiatorName = getString(R.string.You);
                                } else {
                                    initiatorName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = chat.getString("initiatorIdentifier");
                                    }
                                }


                                if (chat.getString("memberId").equals(AppController.getInstance().getUserId())) {

                                    memberName = getString(R.string.YouSmall);
                                } else {
                                    memberName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("memberId"));
                                    if (memberName == null) {

                                        memberName = chat.getString("memberIdentifier");
                                    }
                                }

                                message = initiatorName + " " + getString(R.string.AddedMember, memberName)
                                        + " " + getString(R.string.ToGroup);

                                break;
                            }
                            case 2: {
                                /*
                                 * Member removed
                                 *
                                 */


                                String initiatorName, memberName;

                                if (chat.getString("initiatorId").equals(AppController.getInstance().getUserId())) {

                                    initiatorName = getString(R.string.You);
                                } else {
                                    initiatorName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = chat.getString("initiatorIdentifier");
                                    }
                                }


                                if (chat.getString("memberId").equals(AppController.getInstance().getUserId())) {

                                    memberName = getString(R.string.YouSmall);
                                } else {
                                    memberName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("memberId"));
                                    if (memberName == null) {

                                        memberName = chat.getString("memberIdentifier");
                                    }
                                }
                                message = initiatorName + " " + getString(R.string.Removed) + " " + memberName;


                                break;

                            }
                            case 3: {
                                /*
                                 * Made admin
                                 *
                                 */


                                String initiatorName, memberName;

                                if (chat.getString("initiatorId").equals(AppController.getInstance().getUserId())) {

                                    initiatorName = getString(R.string.You);
                                } else {
                                    initiatorName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = chat.getString("initiatorIdentifier");
                                    }
                                }


                                if (chat.getString("memberId").equals(AppController.getInstance().getUserId())) {

                                    memberName = getString(R.string.YouSmall);
                                } else {
                                    memberName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("memberId"));
                                    if (memberName == null) {

                                        memberName = chat.getString("memberIdentifier");
                                    }
                                }

                                message = initiatorName + " " + getString(R.string.Made) + " " +
                                        memberName + " " + getString(R.string.MakeAdmin);

                                break;
                            }
                            case 4: {
                                /*
                                 * Group name updated
                                 *
                                 */


                                String initiatorName;

                                if (chat.getString("initiatorId").equals(AppController.getInstance().getUserId())) {

                                    initiatorName = getString(R.string.You);
                                } else {
                                    initiatorName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = chat.getString("initiatorIdentifier");
                                    }
                                }
                                message = initiatorName + " " + getString(R.string.UpdatedGroupSubject, chat.getString("groupSubject"));

                                break;
                            }
                            case 5: {
                                /*
                                 * Group icon updated
                                 *
                                 */


                                String initiatorName;

                                if (chat.getString("initiatorId").equals(AppController.getInstance().getUserId())) {

                                    initiatorName = getString(R.string.You);
                                } else {
                                    initiatorName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = chat.getString("initiatorIdentifier");
                                    }
                                }
                                message = initiatorName + " " + getString(R.string.UpdatedGroupIcon);


                                break;

                            }


                            default: {
                                String memberName;


                                if (chat.getString("initiatorId").equals(AppController.getInstance().getUserId())) {

                                    memberName = getString(R.string.You);
                                } else {
                                    memberName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), chat.getString("initiatorId"));
                                    if (memberName == null) {

                                        memberName = chat.getString("initiatorIdentifier");
                                    }
                                }
                                message = getString(R.string.LeftGroup, memberName);


                                break;
                            }


                        }
                    }


                    item.setNewMessage(message);
                    item.setNewMessageCount(String.valueOf(chat.getInt("totalUnread")));


                    item.sethasNewMessage(chat.getInt("totalUnread") > 0);

                    item.setSecretId("");

                    item.setGroupChat(true);
                    item.setSecretChat(false);


                    mChatData.add(0, item);
                    mSearchableData.add(0, item);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            mAdapter.notifyItemInserted(0);
                        }
                    });


                } else {


                    /*
                     * If the corresponding chat is a normal or a secret chat
                     */


                    unreadCount = chat.getInt("totalUnread");


                    if (unreadCount > 0) {
                        unreadChatsCount++;
                    }


                    item = new ChatlistItem();
                    item.setReceiverUid(chat.getString("recipientId"));

                    item.setReceiverIdentifier(chat.getString("number"));
//                item.setReceiverImage(chat.getString("profilePic"));



                    /*
                     * If the uid exists in contacts
                     */


                    contactInfo = AppController.
                            getInstance().getDbController().
                            getFriendInfoFromUid(contactDocId, chat.getString("recipientId"));

                    if (contactInfo != null) {


                        receiverName = contactInfo.get("firstName") + " " + contactInfo.get("lastName");


                        profilePic = (String) contactInfo.get("profilePic");

                        if (profilePic == null || profilePic.isEmpty()) {
                            if (chat.has("profilePic")) {

                                profilePic = chat.getString("profilePic");
                            } else {


                                profilePic = "";
                            }
                        }


                        item.setReceiverInContacts(true);
                        item.setReceiverImage(profilePic);
                    } else {
                        /*
                         * If userId doesn't exists in contact
                         */

//                        receiverName = chat.getString("number");

                        receiverName = chat.getString("userName");
                        if (chat.has("profilePic")) {

                            profilePic = chat.getString("profilePic");
                        } else {


                            profilePic = "";
                        }
                        item.setReceiverImage(profilePic);
                        item.setReceiverInContacts(false);
                    }


                    item.setReceiverName(receiverName);
                    item.setNewMessageTime(Utilities.epochtoGmt(String.valueOf(chat.getLong("timestamp"))));


                    item.setDocumentId(AppController.getInstance().findDocumentIdOfReceiver(chat.getString("recipientId"), chat.getString("secretId")));


                    if (chat.getString("senderId").equals(AppController.getInstance().getUserId())) {


                        item.setShowTick(true);

                        item.setTickStatus(chat.getInt("status"));
                    } else {


                        item.setShowTick(false);
                    }
                    switch (Integer.parseInt(chat.getString("messageType"))) {

                        case 0:

                            message = chat.getString("payload").trim();


                            if (message.isEmpty()) {


                                String message_dTime = String.valueOf(chat.getLong("dTime"));


                                if (!message_dTime.equals("-1")) {

                                    for (int i = 0; i < dTimeForDB.length; i++) {
                                        if (message_dTime.equals(dTimeForDB[i])) {

                                            if (i == 0) {


                                                message = getString(R.string.Timer_set_off);
                                            } else {


                                                message = getString(R.string.Timer_set_to) + " " + dTimeOptions[i];

                                            }
                                            break;
                                        }
                                    }

                                } else {

                                    if (chat.getString("senderId").equals(AppController.getInstance().getUserId())) {

                                        message = getResources().getString(R.string.YouInvited) + " " + receiverName + " " +
                                                getResources().getString(R.string.JoinSecretChat);

                                    } else {
                                        message = getResources().getString(R.string.youAreInvited) + " " + receiverName + " " +
                                                getResources().getString(R.string.JoinSecretChat);
                                    }

                                    item.setShowTick(false);
                                }
                            } else {


                                try {
                                    message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            break;

                        case 1:
                            if (unreadCount > 0) {
                                message = getString(R.string.NewImage);
                            } else {

                                message = getString(R.string.Image);
                            }
                            break;
                        case 2:

                            if (unreadCount > 0) {
                                message = getString(R.string.NewVideo);
                            } else {
                                message = getString(R.string.Video);

                            }
                            break;
                        case 3:
                            if (unreadCount > 0) {
                                message = getString(R.string.NewLocation);
                            } else {

                                message = getString(R.string.Location);
                            }
                            break;
                        case 4:
                            if (unreadCount > 0) {
                                message = getString(R.string.NewContact);
                            } else {

                                message = getString(R.string.Contact);
                            }
                            break;
                        case 5:
                            if (unreadCount > 0) {
                                message = getString(R.string.NewAudio);
                            } else {

                                message = getString(R.string.Audio);
                            }
                            break;
                        case 6:
                            if (unreadCount > 0) {
                                message = getString(R.string.NewSticker);
                            } else {

                                message = getString(R.string.Stickers);
                            }
                            break;
                        case 7:
                            if (unreadCount > 0) {
                                message = getString(R.string.NewDoodle);
                            } else {


                                message = getString(R.string.Doodle);
                            }
                            break;
                        case 8:
                            if (unreadCount > 0) {
                                message = getString(R.string.NewGiphy);
                            } else {

                                message = getString(R.string.Giphy);
                            }
                            break;


                        case 9:
                            if (unreadCount > 0) {
                                message = getString(R.string.NewDocument);
                            } else {

                                message = getString(R.string.Document);
                            }
                            break;


                        case 13:
                            if (unreadCount > 0) {
                                message = getString(R.string.NewPost);
                            } else {

                                message = getString(R.string.Post);
                            }
                            break;

                        case 11: {

                            message = chat.getString("payload").trim();

                            item.setNewMessageTime(Utilities.epochtoGmt(chat.getString("removedAt")));

                            try {
                                message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                            break;
                        }
                        case 15: {
                            if (unreadCount > 0) {
                                message = getString(R.string.new_transfer);
                            } else {

                                message = getString(R.string.transfer);
                            }
                            break;
                        }
                        case 16: {
                            if (unreadCount > 0) {
                                message = getString(R.string.missed_call);
                            } else {

                                message = getString(R.string.missed_call);
                            }
                            break;
                        }
                        case 17: {
                            if (unreadCount > 0) {
                                message = getString(R.string.call);
                            } else {

                                message = getString(R.string.call);
                            }
                            break;
                        }

                        default: {


                            switch (Integer.parseInt(chat.getString("replyType"))) {


                                case 0:
                                    message = chat.getString("payload").trim();


                                    if (message.isEmpty()) {


                                        String message_dTime = String.valueOf(chat.getLong("dTime"));


                                        if (!message_dTime.equals("-1")) {

                                            for (int i = 0; i < dTimeForDB.length; i++) {
                                                if (message_dTime.equals(dTimeForDB[i])) {

                                                    if (i == 0) {


                                                        message = getString(R.string.Timer_set_off);
                                                    } else {


                                                        message = getString(R.string.Timer_set_to) + " " + dTimeOptions[i];

                                                    }
                                                    break;
                                                }
                                            }

                                        } else {

                                            if (chat.getString("senderId").equals(AppController.getInstance().getUserId())) {

                                                message = getResources().getString(R.string.YouInvited) + " " + receiverName + " " +
                                                        getResources().getString(R.string.JoinSecretChat);

                                            } else {
                                                message = getResources().getString(R.string.youAreInvited) + " " + receiverName + " " +
                                                        getResources().getString(R.string.JoinSecretChat);
                                            }

                                            item.setShowTick(false);
                                        }
                                    } else {


                                        try {
                                            message = new String(Base64.decode(message, Base64.DEFAULT), "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    break;
                                case 1:
                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewImage);
                                    } else {

                                        message = getString(R.string.Image);
                                    }
                                    break;
                                case 2:

                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewVideo);
                                    } else {
                                        message = getString(R.string.Video);

                                    }
                                    break;
                                case 3:
                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewLocation);
                                    } else {

                                        message = getString(R.string.Location);
                                    }
                                    break;
                                case 4:
                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewContact);
                                    } else {

                                        message = getString(R.string.Contact);
                                    }
                                    break;
                                case 5:
                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewAudio);
                                    } else {

                                        message = getString(R.string.Audio);
                                    }
                                    break;
                                case 6:
                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewSticker);
                                    } else {

                                        message = getString(R.string.Stickers);
                                    }
                                    break;
                                case 7:
                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewDoodle);
                                    } else {


                                        message = getString(R.string.Doodle);
                                    }
                                    break;
                                case 8:
                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewGiphy);
                                    } else {

                                        message = getString(R.string.Giphy);
                                    }
                                    break;

                                case 13:
                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewPost);
                                    } else {

                                        message = getString(R.string.Post);
                                    }
                                    break;
                                default:
                                    if (unreadCount > 0) {
                                        message = getString(R.string.NewDocument);
                                    } else {

                                        message = getString(R.string.Document);
                                    }
                                    break;


                            }


                        }


                    }

                    item.setNewMessage(message);
                    item.setNewMessageCount(String.valueOf(chat.getInt("totalUnread")));


                    item.sethasNewMessage(chat.getInt("totalUnread") > 0);

                    item.setSecretId(chat.getString("secretId"));


                    item.setSecretChat(!chat.getString("secretId").isEmpty());


                    mChatData.add(0, item);
                    mSearchableData.add(0, item);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            mAdapter.notifyItemInserted(0);
                        }
                    });


                }
            } catch (
                    JSONException e) {
                e.printStackTrace();
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                mAdapter.notifyDataSetChanged();
                recyclerView_chat.scrollToPosition(0);
            }
        });


        if (mChatData.size() == 0) {

            if (llEmpty != null) {


                llEmpty.setVisibility(View.VISIBLE);

//                try {
//                    tv.setTypeface(tf, Typeface.NORMAL);
//
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//                tv.setText(R.string.string_245);
            } else {


                TextView tv = (TextView) view.findViewById(R.id.notLoggedIn);


                tv.setVisibility(View.VISIBLE);
                try {
                    tv.setTypeface(tf, Typeface.NORMAL);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                tv.setText(getString(R.string.string_245));
            }


        } else {

            if (llEmpty != null) {


                llEmpty.setVisibility(View.GONE);


            } else {


                TextView tv = (TextView) view.findViewById(R.id.notLoggedIn);


                tv.setVisibility(View.GONE);

            }


        }


        if (unreadChatsCount == 0) {

            ((LandingActivity) getActivity()).updateChatBadge(false, -1);

        } else {

            if (unreadChatsCount > 0) {
                ((LandingActivity) getActivity()).updateChatBadge(true, unreadChatsCount);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void saveContact(String contactNumber) {
        Intent intentInsertEdit = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intentInsertEdit.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        intentInsertEdit.putExtra(ContactsContract.Intents.Insert.PHONE, contactNumber);
        intentInsertEdit.putExtra(ContactsContract.Intents.Insert.NAME, "");
        intentInsertEdit.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        intentInsertEdit.putExtra("finishActivityOnSaveCompleted", true);
        startActivity(intentInsertEdit, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.faBtn) {
            Bundle bundle = new Bundle();
            bundle.putString("name", "ChatFragment");
            startActivity(new Intent(getContext(), ContactActivity.class));
//            ContactsFragment contactsFragment = new ContactsFragment(faBtn, tabLayout);
//            contactsFragment.setArguments(bundle);
//
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            if (fragmentManager != null) {
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                if (ft != null) {
//                    ft.add(R.id.fragment, contactsFragment);
//                    ft.addToBackStack("contactsFragment");
//                    ft.commit();
//                }
//            }
//            faBtn.setVisibility(View.GONE);
//            tabLayout.setVisibility(View.GONE);
        }
    }

    private class ChatMessageTouchHelper extends ItemTouchHelper.Callback {

        private final GroupChatlistAdapter mAdapter2;

        private ChatMessageTouchHelper(GroupChatlistAdapter adapter) {
            mAdapter2 = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            if (!mChatData.get(position).isGroupChat()) {


                deleteChat(0, position, null);
            } else {


                /*
                 *If group chat then only allow to delete if current user is no longer a member
                 */


                try {

                    String groupMembersDocId = AppController.getInstance().getDbController().fetchGroupChatDocumentId(
                            AppController.getInstance().getGroupChatsDocId(), mChatData.get(position).getReceiverUid());
                    if (!AppController.getInstance().getDbController().checkIfActive(
                            groupMembersDocId)) {

                        deleteChat(1, position, groupMembersDocId);
                    } else {


                        if (root != null) {


                            Snackbar snackbar = Snackbar.make(root, R.string.LeaveGroup, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                        }


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemChanged(position);
                            }
                        });
                    }
                } catch (Exception e) {

                    /*
                     * Incase groupchat document doesnt exists
                     */
                    if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, R.string.delete_failed, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                    }


                }
            }
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder
                viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder
                viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }
    }


    private void deleteChatFromServer(final ChatlistItem chat) {

        String secretId = chat.getSecretId();

        if (secretId == null || secretId.isEmpty()) {
            secretId = null;
        }

        JSONObject object = new JSONObject();
        try {
            object.put("recipientId", chat.getReceiverUid());
            object.put("secretId", secretId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                ApiOnServer.FETCH_CHATS + "?recipientId=" + chat.getReceiverUid() + "&secretId=" + secretId, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getInt("code") == 200) {
                        AppController.getInstance().getDbController().deleteParticularChatDetail(AppController.getInstance().getChatDocId(),
                                AppController.getInstance().findDocumentIdOfReceiver(chat.getReceiverUid(),
                                        chat.getSecretId()), false, null);

//                        AppController.getInstance().getDbController().deleteParticularChatDetail(chat.getDocumentId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                        SessionObserver sessionObserver = new SessionObserver();
                        sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<Boolean>() {
                                    @Override
                                    public void onNext(Boolean flag) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                deleteChatFromServer(chat);
                                            }
                                        }, 1000);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });
                        sessionApiCall.getNewSession(sessionObserver);
                    } else if (root != null) {


                        Snackbar snackbar = Snackbar.make(root, R.string.delete_failed, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                    }
                } catch (Exception e) {
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lang", Constants.LANGUAGE);
                return headers;
            }
        };


        jsonObjReq.setRetryPolicy(new

                DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().

                addToRequestQueue(jsonObjReq, "deleteChatApiRequest");

    }


    private void updateLastMessageDeliveryStatus(String receiverId, String secretId, String messageId, int status) {
        try {

            ChatlistItem item;


            for (int i = 0; i < mChatData.size(); i++) {

                item = mChatData.get(i);

                if (item.getReceiverUid().equals(receiverId) && item.getSecretId().equals(secretId)) {


                    if (item.isShowTick()) {


                        if (AppController.getInstance().getDbController().checkIfLastMessage(item.getDocumentId(), messageId)) {

                            item.setTickStatus(status);
                            final int k = i;
                            try {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        if (mAdapter != null)
                                            mAdapter.notifyItemChanged(k);


                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                    break;
                }

            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }


    private void updateLastMessageDeliveryStatus(String docId, String messageId) {


        try {
            ChatlistItem item;
            for (int i = 0; i < mChatData.size(); i++) {

                item = mChatData.get(i);

                if (item.getDocumentId().equals(docId)) {


                    if (item.isShowTick()) {


                        if (AppController.getInstance().getDbController().checkIfLastMessage(docId, messageId)) {


                            item.setTickStatus(1);
                            final int k = i;
                            try {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        if (mAdapter != null)
                                            mAdapter.notifyItemChanged(k);


                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    break;
                }

            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }


    private void deleteChat(final int type, final int position, final String groupMembersDocId) {




        /*
         * Allowing only to delete a non-group chat or secret chat by swiping
         */


        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(getActivity(), 0);
        builder.setTitle(getResources().getString(R.string.DeleteConfirmation));
        builder.setMessage(getResources().getString(R.string.DeleteChat));
        builder.setPositiveButton(getResources().getString(R.string.Continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {


                try {
                    ChatlistItem item = mChatData.get(position);


                    if (type == 0) {


                        /*
                         * To delete the normal or the secret chat from the server
                         */


                        deleteChatFromServer(item);
                    } else {
                        /*
                         * To delete the group chat from the server
                         */
                        deleteGroup(item, groupMembersDocId);
                    }

                    mChatData.remove(position);
                    mSearchableData.remove(position);

                    if (position == 0) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemRemoved(position);
                            }
                        });
                    }


                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // mAdapter.notifyItemChanged(position);


                        mAdapter.notifyDataSetChanged();
                    }
                });
                dialog.cancel();

            }
        });
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();


        alertDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemChanged(position);
                            }
                        });


                    }
                });
        alertDialog.show();

        Button b_pos;
        b_pos = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (b_pos != null) {
            b_pos.setTextColor(


                    ContextCompat.getColor(getActivity(), R.color.color_black)

            );
        }
        Button n_pos;
        n_pos = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        if (n_pos != null) {
            n_pos.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_black));
        }


    }


    private void deleteGroup(final ChatlistItem chatlistItem, final String groupMembersDocId) {
        JSONObject object = new JSONObject();
        try {
            object.put("chatId", chatlistItem.getReceiverUid());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                ApiOnServer.DELETE_GROUP + "?chatId=" + chatlistItem.getReceiverUid(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getInt("code") == 200) {


//                        AppController.getInstance().getDbController().deleteParticularChatDetail(chatlistItem.getDocumentId());

                        AppController.getInstance().getDbController().deleteParticularChatDetail(
                                AppController.getInstance().getChatDocId(),
                                AppController.getInstance().findDocumentIdOfReceiver(chatlistItem.getReceiverUid(), ""), true, groupMembersDocId);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                        SessionObserver sessionObserver = new SessionObserver();
                        sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<Boolean>() {
                                    @Override
                                    public void onNext(Boolean flag) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                deleteGroup(chatlistItem, groupMembersDocId);
                                            }
                                        }, 1000);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });
                        sessionApiCall.getNewSession(sessionObserver);
                    } else if (root != null) {


                        Snackbar snackbar = Snackbar.make(root, R.string.delete_group, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                    }
                } catch (Exception ignore) {
                }
            }
        }


        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lang", Constants.LANGUAGE);


                return headers;
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "deleteGroupApiRequest");
    }


    /**
     * Have to add the functionality for allowing the direct scroll to the selected message once it has been searched
     */
    private void showLocalSearchResults(ArrayList<Map<String, Object>> arr, String searchText) {

        if (getActivity() != null) {

            //For adding the search results-

            mChatData.clear();


            for (int i = 0; i < mSearchableData.size(); i++) {

                try {


                    if ((mSearchableData.get(i).getReceiverName().toLowerCase(Locale.US)).contains(searchText)) {

                        mChatData.add(mSearchableData.get(i));


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (arr.size() > 0) {
                ChatlistItem chatlist;

                Map<String, Object> message;
                for (int i = 0; i < arr.size(); i++) {
                    chatlist = new ChatlistItem();

                    message = arr.get(i);


                    chatlist.setFromSearchMessage(true);


                    chatlist.setMessagePosition((int) message.get("position"));

                    chatlist.setDocumentId((String) message.get("docId"));
                    chatlist.setReceiverImage((String) message.get("receiverImage"));

                    chatlist.setReceiverName((String) message.get("receiverName"));
                    chatlist.setGroupChat((boolean) message.get("groupChat"));
                    chatlist.setNewMessageTime((String) message.get("timestamp"));
                    chatlist.setReceiverInContacts(true);

                    chatlist.sethasNewMessage(false);

                    chatlist.setShowTick(false);

                    chatlist.setNewMessage((String) message.get("message"));
                    if (!(boolean) message.get("groupChat")) {


                        chatlist.setSecretId((String) message.get("secretId"));


                        //chatlist.setSecretChat(((String) message.get("secretId")).isEmpty());


                    } else {

                        chatlist.setReceiverIdentifier((String) message.get("receiverIdentifier"));

                    }

                    mChatData.add(chatlist);
                    mSearchableData.add(chatlist);


                    mAdapter.notifyDataSetChanged();
                }
            } else {
                mAdapter.notifyDataSetChanged();
            }

        }
    }


    private void handleMessageRemovedSignal(String message, String sender, String timestamp, JSONObject object) {
        try {


            String secretId = "";

            if (object.has("secretId")) {

                secretId = object.getString("secretId");

            }


            ChatlistItem chat = new ChatlistItem();
            chat.setReceiverUid(sender);
            chat.setNewMessage(message);
            chat.setSecretId(secretId);

            chat.setDocumentId(AppController.getInstance().findDocumentIdOfReceiver(sender, secretId));
            chat.setSecretChat(!secretId.isEmpty());
            chat.setReceiverIdentifier(object.getString("receiverIdentifier"));



            /*
             * If the uid exists in contacts
             */


            Map<String, Object> contactInfo = AppController.getInstance().getDbController().
                    getFriendInfoFromUid(AppController.getInstance().getFriendsDocId(), sender);

            if (contactInfo != null) {
                chat.setReceiverName(contactInfo.get("firstName") + " " + contactInfo.get("lastName"));


                String contactPicUrl = (String) contactInfo.get("profilePic");


                if (contactPicUrl != null && !contactPicUrl.isEmpty()) {
                    chat.setReceiverImage(contactPicUrl);
                } else {
                    chat.setReceiverImage("");

                }


                chat.setReceiverInContacts(true);
            } else {
                /*
                 * If userId doesn't exists in contact
                 */
                chat.setReceiverName(object.getString("receiverIdentifier"));
                if (object.has("userImage")) {
                    chat.setReceiverImage(object.getString("userImage"));
                } else {
                    chat.setReceiverImage("");
                }
                chat.setReceiverInContacts(true);
            }


            chat.setNewMessageTime(Utilities.epochtoGmt(timestamp));


            int alreadyInContact = alreadyInContact(sender, secretId);

            if (alreadyInContact == -1) {

                chat.sethasNewMessage(false);
                mChatData.add(0, chat);
                mSearchableData.add(0, chat);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            } else {

                chat.sethasNewMessage(mChatData.get(alreadyInContact).hasNewMessage());

                chat.setNewMessageCount(mChatData.get(alreadyInContact).getNewMessageCount());

                mChatData.remove(alreadyInContact);
                mSearchableData.remove(alreadyInContact);
                mChatData.add(0, chat);
                mSearchableData.add(0, chat);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.notifyDataSetChanged();
                    }
                });


            }


            if (llEmpty != null && llEmpty.getVisibility() == View.VISIBLE) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llEmpty.setVisibility(View.GONE);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handleMessageEditedSignal(String message, String sender, String timestamp, JSONObject object) {
        try {


            String secretId = "";

            if (object.has("secretId")) {

                secretId = object.getString("secretId");

            }


            ChatlistItem chat = new ChatlistItem();
            chat.setReceiverUid(sender);
            chat.setNewMessage(message);
            chat.setSecretId(secretId);

            chat.setDocumentId(AppController.getInstance().findDocumentIdOfReceiver(sender, secretId));
            chat.setSecretChat(!secretId.isEmpty());
            chat.setReceiverIdentifier(object.getString("receiverIdentifier"));



            /*
             * If the uid exists in contacts
             */


            Map<String, Object> contactInfo = AppController.getInstance().getDbController().
                    getFriendInfoFromUid(AppController.getInstance().getFriendsDocId(), sender);

            if (contactInfo != null) {


                chat.setReceiverName(contactInfo.get("firstName") + " " + contactInfo.get("lastName"));


                String contactPicUrl = (String) contactInfo.get("profilePic");


                if (contactPicUrl != null && !contactPicUrl.isEmpty()) {
                    chat.setReceiverImage(contactPicUrl);
                } else {
                    chat.setReceiverImage("");

                }


                chat.setReceiverInContacts(true);
            } else {
                /*
                 * If userId doesn't exists in contact
                 */
                chat.setReceiverName(object.getString("receiverIdentifier"));
                if (object.has("userImage")) {
                    chat.setReceiverImage(object.getString("userImage"));
                } else {
                    chat.setReceiverImage("");
                }
                chat.setReceiverInContacts(true);
            }


            chat.setNewMessageTime(Utilities.epochtoGmt(timestamp));


            int alreadyInContact = alreadyInContact(sender, secretId);

            if (alreadyInContact == -1) {

                chat.sethasNewMessage(false);
                mChatData.add(0, chat);
                mSearchableData.add(0, chat);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            } else {
/**
 * Not to increment the unread messages count
 */
                chat.sethasNewMessage(mChatData.get(alreadyInContact).hasNewMessage());

                chat.setNewMessageCount(mChatData.get(alreadyInContact).getNewMessageCount());

                mChatData.remove(alreadyInContact);
                mSearchableData.remove(alreadyInContact);
                mChatData.add(0, chat);
                mSearchableData.add(0, chat);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.notifyDataSetChanged();
                    }
                });


            }


            if (llEmpty != null && llEmpty.getVisibility() == View.VISIBLE) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llEmpty.setVisibility(View.GONE);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handleGroupMessageRemovedSignal(String message, String sender, String timestamp, JSONObject object) {
        try {
            ChatlistItem chat = new ChatlistItem();
            chat.setReceiverUid(sender);
            chat.setNewMessage(message);
            chat.setSecretId("");


            chat.setDocumentId(AppController.getInstance().findDocumentIdOfReceiver(sender, ""));
            chat.setSecretChat(false);
            chat.setReceiverIdentifier(sender);
            chat.setGroupChat(true);


            chat.setReceiverName(object.getString("name"));
            if (object.has("userImage")) {
                chat.setReceiverImage(object.getString("userImage"));
            } else {
                chat.setReceiverImage("");
            }
            chat.setReceiverInContacts(true);


            chat.setNewMessageTime(Utilities.epochtoGmt(timestamp));
            chat.setNewMessageCount(AppController.getInstance().getDbController().getNewMessageCount(
                    AppController.getInstance().findDocumentIdOfReceiver(sender, "")));


            int alreadyInContact = alreadyInContact(sender, "");

            if (alreadyInContact == -1) {
                chat.sethasNewMessage(false);


                mChatData.add(0, chat);
                mSearchableData.add(0, chat);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            } else {

                chat.sethasNewMessage(mChatData.get(alreadyInContact).hasNewMessage());

                chat.setNewMessageCount(mChatData.get(alreadyInContact).getNewMessageCount());

                mChatData.remove(alreadyInContact);
                mSearchableData.remove(alreadyInContact);
                mChatData.add(0, chat);
                mSearchableData.add(0, chat);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.notifyDataSetChanged();
                    }
                });


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void handleGroupMessageEditedSignal(String message, String sender, String timestamp, JSONObject object) {
        try {
            ChatlistItem chat = new ChatlistItem();
            chat.setReceiverUid(sender);
            chat.setNewMessage(message);
            chat.setSecretId("");


            chat.setDocumentId(AppController.getInstance().findDocumentIdOfReceiver(sender, ""));
            chat.setSecretChat(false);
            chat.setReceiverIdentifier(sender);
            chat.setGroupChat(true);


            chat.setReceiverName(object.getString("name"));
            if (object.has("userImage")) {
                chat.setReceiverImage(object.getString("userImage"));
            } else {
                chat.setReceiverImage("");
            }
            chat.setReceiverInContacts(true);


            chat.setNewMessageTime(Utilities.epochtoGmt(timestamp));
            chat.setNewMessageCount(AppController.getInstance().getDbController().getNewMessageCount(
                    AppController.getInstance().findDocumentIdOfReceiver(sender, "")));


            int alreadyInContact = alreadyInContact(sender, "");

            if (alreadyInContact == -1) {
                chat.sethasNewMessage(false);


                mChatData.add(0, chat);
                mSearchableData.add(0, chat);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            } else {

                /*
                 * Unread message count is not incremented.
                 */
                chat.sethasNewMessage(mChatData.get(alreadyInContact).hasNewMessage());

                chat.setNewMessageCount(mChatData.get(alreadyInContact).getNewMessageCount());

                mChatData.remove(alreadyInContact);
                mSearchableData.remove(alreadyInContact);
                mChatData.add(0, chat);
                mSearchableData.add(0, chat);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mAdapter.notifyDataSetChanged();
                    }
                });


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDetach() {
        if (mActivity != null)
            //mActivity.visibleActionBar();
            super.onDetach();

    }


    public void onQueryTextChanges(String newText) {
        if (mAdapter != null)
            mAdapter.getFilter().filter(newText);


        if (newText.isEmpty()) {
            addChat();
        } else {
            showLocalSearchResults(AppController.getInstance().getDbController().searchTextMessages(newText), newText);
        }

    }

    public void onClosed() {


        addChat();
    }

}

