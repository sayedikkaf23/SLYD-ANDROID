package chat.hola.com.app.home.callhistory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;
import chat.hola.com.app.Adapters.CalllistAdapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.GroupChat.Activities.CreateGroup;
import chat.hola.com.app.ModelClasses.CallItem;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.connect.ContactActivity;
import chat.hola.com.app.home.connect.ContactCallActivity;
import chat.hola.com.app.home.stories.StoriesPresenter;
import chat.hola.com.app.home.stories.model.CallHistoryResponse;
import chat.hola.com.app.home.stories.model.StoryResponse;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.story.StoryPresenter;
import chat.hola.com.app.profileScreen.story.model.TrendingDtlAdapter;
import dagger.android.support.DaggerFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by moda on 24/08/17.
 */

@SuppressLint("ValidFragment")
public class CallsFragment extends DaggerFragment implements CallHistoryContract.View, View.OnClickListener, CalllistAdapter.CallClickListener {

    @Inject
    CallHistoryPresenter mPresenter;

    private RelativeLayout fragment;


    public CallsFragment() {
    }

    private SessionApiCall sessionApiCall = new SessionApiCall();
    private ArrayList<CallItem> callList = new ArrayList<>();
    private CalllistAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
//    private SearchView searchView;

    private TextView messageCall;
    private LinearLayout llEmpty;
    private boolean firstTime = true;
    private View view;
    private static Bus bus = AppController.getBus();
    private CoordinatorLayout root;
    private String contactNumberToSave;

    private boolean callsSynced = false;
    private RecyclerView rv;
    private FloatingActionButton faBtn;
    private ImageView callBtn;
    private LandingActivity mActivity;
    private Button btnAll;
    private Button btnMissed;
    private RelativeLayout rl_calls, rl_callslayout;
    public androidx.appcompat.widget.SearchView searchView;
    RelativeLayout rel_new_add;

    SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_calllist, container, false);
        } else {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
        }
        mPresenter.attachView(this);

        root = (CoordinatorLayout) view.findViewById(R.id.root);
        final RelativeLayout backButton = (RelativeLayout) view.findViewById(R.id.close_rl);
        mActivity = (LandingActivity) getActivity();
        faBtn = (FloatingActionButton) view.findViewById(R.id.faBtn);
        faBtn.setOnClickListener(this);
        callBtn = (ImageView) view.findViewById(R.id.callBtn);
        callBtn.setOnClickListener(this);
        backButton.setVisibility(View.GONE);
        btnAll = (Button) view.findViewById(R.id.btnAll);
        btnAll.setOnClickListener(this);
        btnMissed = (Button) view.findViewById(R.id.btnMissedCall);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        btnMissed.setOnClickListener(this);
        rl_calls = view.findViewById(R.id.rl_calls);
        rl_callslayout = view.findViewById(R.id.rl_callslayout);
        rel_new_add = (RelativeLayout) view.findViewById(R.id.rel_new_add);
        RelativeLayout rel_add_new_contact = (RelativeLayout) view.findViewById(R.id.rel_add_new_contact);
        RelativeLayout rel_create_new_group = (RelativeLayout) view.findViewById(R.id.rel_create_new_group);
        RelativeLayout rel_create_new_section = (RelativeLayout) view.findViewById(R.id.rel_create_new_section);


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
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rel_search_view);

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
//                onQueryTextChanges(newText);
                mAdapter.getFilter().filter(newText);

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                onClosed();
                return false;
            }
        });

        btnAll.setSelected(true);
        btnMissed.setSelected(false);


        changeVisibilityOfViews();


        RelativeLayout clearAllCalls = (RelativeLayout) view.findViewById(R.id.delete_rl);

        rv = (RecyclerView) view.findViewById(R.id.rv);


        messageCall = (TextView) view.findViewById(R.id.userMessagechat);
        llEmpty = (LinearLayout) view.findViewById(R.id.llEmpty);


        ItemTouchHelper.Callback callback = new ChatMessageTouchHelper(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);

        mPresenter.init();


        swipeRefresh.setOnRefreshListener(() -> {
            CallHistoryPresenter.page = 0;
            loadData();
        });


        /*
         * Api calls to fetch the list of the calls from the server
         */

        loadData();


        //makeStringReq();

        final TextView title = (TextView) view.findViewById(R.id.title);

        /*searchView = (SearchView) view.findViewById(R.id.search);
        searchView.setIconified(true);
        searchView.setIconifiedByDefault(true);
        searchView.clearFocus();
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    //backButton.setVisibility(View.INVISIBLE);

                    // title.setVisibility(View.GONE);
                } else {

                    //title.setVisibility(View.VISIBLE);
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
*/

        /*
         * Dont need it anymore in the present logic
         */

       /* searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llEmpty.setVisibility(View.GONE);


                        if (callList.size() == 0) {


                            llEmpty.setVisibility(View.VISIBLE);
                        }

                        //      backButton.setVisibility(View.VISIBLE);
                    }
                });


                return false;
            }
        });*/

       /* AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        clearAllCalls.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builder =
                        new androidx.appcompat.app.AlertDialog.Builder(getActivity(), 0);
                builder.setTitle(getResources().getString(R.string.DeleteConfirmation));
                builder.setMessage(getResources().getString(R.string.ClearCalls));
                builder.setPositiveButton(getResources().getString(R.string.Continue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        //   ArrayList<String> arr = getIds();
                        //  deleteCallFromServer(arr);

//

                        deleteCallLogsFromServer(null, true);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                llEmpty.setVisibility(View.VISIBLE);
                                callList.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
                androidx.appcompat.app.AlertDialog alertDialog = builder.create();


                alertDialog.show();

                Button b_pos;
                b_pos = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                if (b_pos != null) {
                    b_pos.setTextColor(getResources().getColor(R.color.color_black));
                }
                Button n_pos;
                n_pos = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                if (n_pos != null) {
                    n_pos.setTextColor(getResources().getColor(R.color.color_black));
                }
            }
        });


//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                getActivity().onBackPressed();
//
//            }
//        });


        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Condensed.ttf");
        //  title.setTypeface(tf, Typeface.BOLD);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            try {
//                mAdapter.setListener(mPresenter);

//                mPresenter.callHistory();
//                mPresenter.storyObserver();
            } catch (Exception ignored) {
            }

            if (getActivity() != null) {

                hideKeyboard(getActivity());

            }
        } else if (searchView != null) {


            /*
             * For the bug where the search was on and the fragment was swiped out and was no longer visible
             */
            if (!searchView.isIconified()) {


                searchView.setIconified(true);

                if (getActivity() != null)
                    addCalls();
            }

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 71) {


            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                /*
                 * Not required essentially
                 */
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {


                    mAdapter.requestAudioCall(null);
                }
            }

        } else if (requestCode == 72) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {


                    mAdapter.requestVideoCall(null);
                }
            } else if (grantResults.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {


                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {


                    mAdapter.requestVideoCall(null);

                }


            }

        }
    }


    private void addCalls() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    callList.clear();
                    mAdapter.notifyDataSetChanged();
                }
            });


            ArrayList<Map<String, Object>> callsList = AppController.getInstance().getDbController().getAllTheCalls(AppController.getInstance().getCallsDocId());

            CallItem call;
            Map<String, Object> callIthPosition;


            String contactDocId = AppController.getInstance().getFriendsDocId();


            Map<String, Object> contactInfo;

            if (callsList != null) {

                for (int i = callsList.size() - 1; i >= 0; i--) {


                    call = new CallItem();


                    callIthPosition = callsList.get(i);

                    call.setReceiverUid((String) callIthPosition.get("receiverUid"));

                    /*  *//*
                     * If the uid exists in contacts
                     *//*
                    contactInfo = AppController.getInstance().getDbController().getFriendInfoFromUid(contactDocId, (String) callIthPosition.get("receiverUid"));

                    if (contactInfo != null) {
                        call.setReceiverName(contactInfo.get("firstName") + " " + contactInfo.get("lastName"));

                        call.setStar((boolean) contactInfo.get("isStar"));
                        String image = (String) contactInfo.get("profilePic");


                        if (image != null && !image.isEmpty()) {
                            call.setReceiverImage(image);
                        } else {

                            call.setReceiverImage("");
                        }

                        call.setReceiverInContacts(true);


                        if (contactInfo.containsKey("blocked")) {


                            call.setCallNotAllowed(true);

                        }


                    } else {*/
                    call.setStar(false);
                    call.setReceiverInContacts(false);
                    /*
                     * If userId doesn't exists in contact
                     */
                    call.setReceiverName((String) callIthPosition.get("receiverIdentifier"));


                    String image = (String) callIthPosition.get("receiverImage");
                    if (image != null && !image.isEmpty()) {
                        call.setReceiverImage(image);
                    } else {

                        call.setReceiverImage("");
                    }

//                }
                    call.setReceiverIdentifier((String) callIthPosition.get("receiverIdentifier"));

                    call.setCallInitiateTime((String) callIthPosition.get("callTime"));
                    call.setCallId((String) callIthPosition.get("callId"));
                    call.setCallDuration((String) callIthPosition.get("callDuration"));
                    call.setCallType((String) callIthPosition.get("callType"));
                    call.setIsMissedCall((String) callIthPosition.get("isMissedCall"));
                    call.setCallInitiated((boolean) callIthPosition.get("callInitiated"));
                    callList.add(call);
                }
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }

            if (callList.size() == 0) {
                llEmpty.setVisibility(View.VISIBLE);

            } else if (callList.size() > 0) {
                llEmpty.setVisibility(View.GONE);

            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.callBtn) {
            //  mActivity.hideActionBar();
            Bundle bundle = new Bundle();
            bundle.putString("name", "CallFragment");
            startActivity(new Intent(getContext(), ContactCallActivity.class));

//            ContactsFragment contactsFragment = new ContactsFragment(faBtn, connectTl);
//            contactsFragment.setArguments(bundle);
//            getFragmentManager().beginTransaction().replace(R.id.fragment, contactsFragment)
//                    .addToBackStack("contactsFragment").commit();
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            if (fragmentManager != null) {
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                if (ft != null) {
//                    ft.add(R.id.fragment, contactsFragment);
//                    ft.addToBackStack("contactsFragment");
//                    //contactsFragment
//                    ft.commit();
//                }
//            }
//            faBtn.setVisibility(View.GONE);
//            connectTl.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btnAll) {
            btnAll.setSelected(true);
            btnMissed.setSelected(false);
            mAdapter.updateData(callList);
            rv.scrollToPosition(0);


//            mAdapter.sortBy("All");

        } else if (v.getId() == R.id.btnMissedCall) {
            btnAll.setSelected(false);
            btnMissed.setSelected(true);

            ArrayList<CallItem> tempList = new ArrayList<>();

            for (CallItem callItem : callList) {
                if (callItem.getIsMissedCall() != null) {
                    if (callItem.getIsMissedCall().equalsIgnoreCase("1")) {
                        tempList.add(callItem);
                    }
                }
            }
//            mAdapter.sortBy("Missed");
            mAdapter.updateData(tempList);
            rv.scrollToPosition(0);


            Log.e("Size", "Filter " + tempList.size());
        }
    }

    /*Here we change the common views visibility on selection of fragment*/
    public void changeVisibilityOfViews() {
        LandingActivity mActivity = (LandingActivity) getActivity();
        mActivity.hideActionBar();
        mActivity.ivProfilePic.setVisibility(View.GONE);
        mActivity.iV_plus.setVisibility(View.VISIBLE);
        mActivity.tvSearch.setVisibility(View.GONE);
        mActivity.setTitle(getString(R.string.string_639), new TypefaceManager(mActivity).getMediumFont());
        mActivity.removeFullScreenFrame();
        mActivity.linearPostTabs.setVisibility(View.GONE);
        mActivity.tvCoins.setVisibility(View.GONE);
    }

    public void hideHeader() {
        if (rl_calls != null)
            rl_calls.setVisibility(View.GONE);
        if (rl_callslayout != null)
            rl_callslayout.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void userBlocked() {

    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);


                    mPresenter.callApiOnScroll(getFirstVisiblePosition(rv),
                            rv.getChildCount(), mLayoutManager.getItemCount());
                }
            };

    @Override
    public void setupRecyclerView() {
        CallHistoryPresenter.page = 0;


        mAdapter = new CalllistAdapter(getActivity(), callList, this, this);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rv.setLayoutManager(mLayoutManager);
//        rv.setItemAnimator(new DefaultItemAnimator());

//        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rv, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                final CallItem item = mAdapter.getList().get(position);
//                Intent intent = new Intent(getActivity(), DetailCallInfo.class);
//
//                String receiverName = item.getReceiverName();
//                String callType = item.getCallType();
//                String receiverImage = item.getReceiverImage();
//                String calledDate = item.getCallInitiateTime();
//                Boolean isCallNotAllowed = item.isCallNotAllowed();
//                Boolean isCallInitiated = item.isCallInitiated();
//
//                intent.putExtra("receiverName", receiverName);
//                intent.putExtra("callType", callType);
//                intent.putExtra("receiverImage", receiverImage);
//                intent.putExtra("calledDate", calledDate);
//                intent.putExtra("isCallInitiated", isCallInitiated);
//                intent.putExtra("isCallNotAllowed", isCallNotAllowed);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//                if (position >= 0) {
//                    final CallItem item = mAdapter.getList().get(position);
//                    if (!item.isReceiverInContacts()) {
//
//                    }
//
//                }
//
//
//            }
//        }));

        rv.addOnScrollListener(recyclerViewOnScrollListener);
        rv.setAdapter(mAdapter);

    }


    public static int getFirstVisiblePosition(RecyclerView rv) {
        if (rv != null) {
            final RecyclerView.LayoutManager layoutManager = rv
                    .getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                return ((LinearLayoutManager) layoutManager)
                        .findFirstVisibleItemPosition();
            }
        }
        return 0;
    }

    @Override
    public void isLoading(boolean flag) {
        swipeRefresh.setRefreshing(flag);
    }

    @Override
    public void showCallHistory(ArrayList<CallItem> callListTemp, boolean isFirstPage) {
        if (isFirstPage) {
            callList.clear();
            mAdapter.addData(callListTemp);

//            addCalls();
        } else {
            mAdapter.addData(callListTemp);
        }

    }

    @Override
    public void isDataAvailable(boolean empty) {
        llEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);

    }

    @Override
    public void noData(boolean isFirstPage) {
        if (isFirstPage) {
            llEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadData() {
        mPresenter.loadData(CallHistoryPresenter.page * CallHistoryPresenter.PAGE_SIZE, CallHistoryPresenter.PAGE_SIZE);
    }

    @Override
    public void reload() {

    }

    @Override
    public void onItemClick(CallItem i, String time, int position) {

        CallItem item = mAdapter.getList().get(position);
        Intent intent = new Intent(getActivity(), DetailCallInfo.class);

        String receiverName = item.getReceiverName();
        String callType = item.getCallType();
        String receiverImage = item.getReceiverImage();
        String calledDate = item.getCallInitiateTime();
        Boolean isCallNotAllowed = item.isCallNotAllowed();
        Boolean isCallInitiated = item.isCallInitiated();

        intent.putExtra("receiverName", receiverName);
        intent.putExtra("callType", callType);
        intent.putExtra("receiverImage", receiverImage);
        intent.putExtra("calledDate", calledDate);
        intent.putExtra("isCallInitiated", isCallInitiated);
        intent.putExtra("isCallNotAllowed", isCallNotAllowed);
        intent.putExtra("callTime", time);
        intent.putExtra("position", position);
        intent.putExtra("callDuration", item.getCallDuration());
        startActivity(intent);
    }

    private class ChatMessageTouchHelper extends ItemTouchHelper.Callback {

        private final CalllistAdapter mAdapter2;

        private ChatMessageTouchHelper(CalllistAdapter adapter) {
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
            androidx.appcompat.app.AlertDialog.Builder builder =
                    new androidx.appcompat.app.AlertDialog.Builder(getActivity(), 0);
            builder.setTitle(getResources().getString(R.string.DeleteConfirmation));
            builder.setMessage(getResources().getString(R.string.DeleteCall));
            builder.setPositiveButton(getResources().getString(R.string.Continue), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        deleteCallLogsFromServer(callList.get(position), false);
                        callList.remove(position);
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

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (firstTime) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        firstTime = false;

                        if (getActivity() != null) {
                            addCalls();
                        }
                    }
                }, 500);
            } else {
                if (getActivity() != null)
                    addCalls();
            }
            loadData();
        }

    }


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public void showNoSearchResults(final CharSequence constraint, boolean flag) {

        try {
            if (flag) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llEmpty.setVisibility(View.GONE);


                        if (callList.size() == 0) {

                            llEmpty.setVisibility(View.VISIBLE);
                        } else {

                            llEmpty.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (mAdapter.getList().size() == 0) {
                            if (llEmpty != null) {


                                llEmpty.setVisibility(View.VISIBLE);

//                                llEmpty.setText(getString(R.string.noMatch) + " " + constraint);
                                //  llEmpty.setVisibility(View.GONE);

                            }
                        } else {
                            llEmpty.setVisibility(View.GONE);


                        }
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

            if (object.getString("eventName").equals(MqttEvents.UserUpdates.value + "/" + AppController.getInstance().getUserId())) {


                switch (object.getInt("type")) {


                    case 2:


                        /*
                         * Profile pic update
                         */


                        /*
                         * To update in the contacts list
                         */


                        String profilePic = object.getString("profilePic");
                        ArrayList<Integer> arr = findContactPositionInList(object.getString("userId"));

                        if (arr.size() > 0) {
                            for (int i = 0; i < arr.size(); i++) {

                                CallItem item = callList.get(arr.get(i));


                                item.setReceiverImage(profilePic);
                                callList.set(arr.get(i), item);


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

                                        CallItem item = callList.get(arr2.get(i));


                                        item.setReceiverName(object.getString("contactName"));
                                        callList.set(arr2.get(i), item);


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

                                        CallItem item = callList.get(arr3.get(i));


                                        item.setReceiverName(item.getReceiverIdentifier());
                                        callList.set(arr3.get(i), item);


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

                                        CallItem item = callList.get(arr4.get(i));


                                        item.setReceiverName(object.getString("contactName"));
                                        callList.set(arr4.get(i), item);


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
                        if (object.has("status") && object.getInt("status") == 0) {

                            ArrayList<Integer> arr2 = findContactPositionInList(object.getString("userId"));

                            if (arr2.size() > 0) {
                                for (int i = 0; i < arr2.size(); i++) {

                                    CallItem item = callList.get(arr2.get(i));


                                    item.setReceiverName(item.getReceiverIdentifier());
                                    callList.set(arr2.get(i), item);


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

                                CallItem item = callList.get(arr2.get(i));


                                item.setReceiverName(object.getString("name"));
                                callList.set(arr2.get(i), item);


                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        break;


                    case 6:

                        /*
                         *Block or unblock by the opponent
                         */

                        checkBlockedVisibility(object.getString("initiatorId"), object.getBoolean("blocked"));
                        break;
                }


            } else if (object.getString("eventName").equals("ContactNameUpdated")) {

                ArrayList<Integer> arr = findContactPositionInList(object.getString("contactUid"));

                if (arr.size() > 0) {
                    for (int i = 0; i < arr.size(); i++) {

                        CallItem item = callList.get(arr.get(i));


                        item.setReceiverName(object.getString("contactName"));
                        callList.set(arr.get(i), item);


                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            } else if (object.getString("eventName").equals("contactRefreshed")) {

                addCalls();
            } else if (object.getString("eventName").equals("SyncCallLogs")) {
//                if (!callsSynced) {
//
//                    if (callList.size() > 0) {
//                        fetchCallsLogs(new Utilities().gmtToEpoch(callList.get(0).getCallInitiateTime()));
//                    } else {
//                        fetchCallsLogs("0");
//                    }
//                }

            } else if (object.getString("eventName").equals("UserBlocked")) {


                checkBlockedVisibility(object.getString("opponentId"), true);


            } else if (object.getString("eventName").equals("UserUnblocked")) {


                checkBlockedVisibility(object.getString("opponentId"), false);


            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }


    private ArrayList<Integer> findContactPositionInList(String contactUid) {

        ArrayList<Integer> positions = new ArrayList<>();

        for (int i = 0; i < callList.size(); i++) {


            if (callList.get(i).getReceiverUid().equals(contactUid)) {

                positions.add(i);
            }
        }

        return positions;
    }

    private ArrayList<Integer> findInactiveContactPositionInList(String contactNumber) {

        ArrayList<Integer> positions = new ArrayList<>();


        for (int i = 0; i < callList.size(); i++) {


            if (callList.get(i).getReceiverIdentifier().equals(contactNumber)) {

                positions.add(i);
            }
        }

        return positions;
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

    private void fetchCallsLogs(String timestamp) {


        JSONObject object = new JSONObject();
        try {
            object.put("timeStamp", timestamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ApiOnServer.CALLS_LOGS, object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    callsSynced = true;
                    if (response.getInt("code") == 200) {

                        addCallsFetchedFromServer(response.getJSONArray("response"));

                    } else {

                        if (root != null) {
                            Snackbar snackbar = Snackbar.make(root, getString(R.string.Call_Sync), Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }

                    }

                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
                                            fetchCallsLogs(timestamp);
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "getCallsApi");


    }

    private void addCallsFetchedFromServer(JSONArray callLogs) {


        JSONObject callLog;
        String image;
        CallItem callItem;
        Map<String, Object> contactInfo;

        Map<String, Object> callsMap;
        ArrayList<Map<String, Object>> callArray = new ArrayList<>();


        String contactDocId = AppController.getInstance().getFriendsDocId();
        try {
            for (int i = callLogs.length() - 1; i >= 0; i--) {


                callLog = callLogs.getJSONObject(i);

                callItem = new CallItem();
                callsMap = new HashMap<>();

                callItem.setCallId(callLog.getString("callId"));
                callItem.setCallDuration(callLog.getString("callDuration"));
                callItem.setCallInitiated(callLog.getBoolean("callInitiated"));
                callItem.setCallInitiateTime(Utilities.epochtoGmt(callLog.getString("calltime")));


                if (callLog.getString("callType").equals("0")) {
                    callItem.setCallType("Audio Call");
                    callsMap.put("callType", "Audio Call");
                } else if (callLog.getString("callType").equals("1")) {
                    callItem.setCallType("Video Call");
                    callsMap.put("callType", "Video Call");
                }

                callItem.setReceiverUid(callLog.getString("opponentUid"));
                callItem.setReceiverIdentifier(callLog.getString("opponentNumber"));

                /*
                 * If the uid exists in contacts
                 */
                contactInfo = AppController.getInstance().getDbController().getFriendInfoFromUid(contactDocId, callLog.getString("opponentUid"));

                if (contactInfo != null) {
                    callItem.setReceiverName(contactInfo.get("firstName") + " " + contactInfo.get("lastName"));
                    callsMap.put("receiverName", contactInfo.get("firstName") + " " + contactInfo.get("lastName"));

                    image = (String) contactInfo.get("profilePic");


                    if (image != null && !image.isEmpty()) {
                        callItem.setReceiverImage(image);

                        callsMap.put("receiverImage", image);
                    } else {

                        callItem.setReceiverImage("");
                        callsMap.put("receiverImage", "");
                    }

                    callItem.setReceiverInContacts(true);
                } else {

                    callItem.setReceiverInContacts(false);
                    /*
                     * If userId doesn't exists in contact
                     */
//                    callItem.setReceiverName(callLog.getString("opponentNumber"));
//
//
//                    callsMap.put("receiverName", callLog.getString("opponentNumber"));


                    callItem.setReceiverName(callLog.getString("userName"));


                    callsMap.put("receiverName", callLog.getString("userName"));


                    image = "";
                    if (callLog.has("opponentProfilePic")) {
                        image = (String) callLog.get("opponentProfilePic");
                    }
                    if (image != null && !image.isEmpty()) {
                        callItem.setReceiverImage(image);
                        callsMap.put("receiverImage", image);
                    } else {

                        callItem.setReceiverImage("");
                        callsMap.put("receiverImage", "");
                    }

                }
                String isMissedCall = callLog.getString("isMissedCall");

                callItem.setIsMissedCall(isMissedCall);
                callList.add(callItem);

                callsMap.put("receiverUid", callLog.getString("opponentUid"));
                callsMap.put("callTime", Utilities.epochtoGmt(callLog.getString("calltime")));
                callsMap.put("callInitiated", callLog.getBoolean("callInitiated"));
                callsMap.put("callId", callLog.getString("callId"));
                callsMap.put("isMissedCall", isMissedCall);


                callsMap.put("receiverIdentifier", callLog.getString("opponentNumber"));
                callArray.add(callsMap);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*
         * Crash on Vivo phone
         */
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mAdapter.notifyDataSetChanged();
                    rv.scrollToPosition(0);
                }
            });

        } else {


            mAdapter.notifyDataSetChanged();
            rv.scrollToPosition(0);

        }
        if (callList.size() == 0) {


            llEmpty.setVisibility(View.VISIBLE);
//            try {
//                llEmpty.setTypeface(AppController.getInstance().getRegularFont(), Typeface.NORMAL);
//
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//
//            tv.setText(R.string.No_Calls)


        } else {


            // TextView tv = (TextView) view.findViewById(R.id.userMessagechat);


            llEmpty.setVisibility(View.GONE);


        }


        if (callArray.size() > 0) {
            AppController.getInstance().getDbController().appendCallLogs(AppController.getInstance().getCallsDocId(), callArray);

        }
    }

    private void deleteCallLogsFromServer(final CallItem call, final boolean deleteAll) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                ApiOnServer.CALLS_LOGS, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getInt("code") == 200) {


                        if (deleteAll) {
                            AppController.getInstance().getDbController().deleteAllCalls(AppController.getInstance().getCallsDocId());
                        } else {
                            AppController.getInstance().getDbController().deleteParticularCallDetail(AppController.getInstance().getCallsDocId(),
                                    call.getCallId());
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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
                                            deleteCallLogsFromServer(call, deleteAll);
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


                    Snackbar snackbar = Snackbar.make(root, R.string.delete_failed_calls, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                }
            }
        }


        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lang", Constants.LANGUAGE);


                if (deleteAll) {


                    headers.put("deleteAll", "1");
                } else {
                    headers.put("deleteAll", "0");
                    headers.put("callIds", (new JSONArray(Arrays.asList(new String[]{call.getCallId()}))).toString());
                }


                return headers;
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "deleteCallApiRequest");

    }

    private void checkBlockedVisibility(String opponentId, boolean blocked) {

        CallItem call;

        for (int i = 0; i < callList.size(); i++) {


            if (callList.get(i).getReceiverUid().equals(opponentId)) {

                call = callList.get(i);

                call.setCallNotAllowed(blocked);
                callList.set(i, call);
                final int k = i;
                if (getActivity() != null) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyItemChanged(k);
                        }
                    });

                }


            }


        }

    }

    @Override
    public void onDetach() {
        if (mActivity != null)
            mActivity.visibleActionBar();
        super.onDetach();
    }


}
