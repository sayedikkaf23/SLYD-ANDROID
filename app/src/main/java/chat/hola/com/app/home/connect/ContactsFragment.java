package chat.hola.com.app.home.connect;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Activities.ContactsSecretChat;
import chat.hola.com.app.Adapters.ContactsAdapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.GroupChat.Activities.CreateGroup;
import chat.hola.com.app.ModelClasses.ContactsItem;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.SortContacts;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.models.SessionObserver;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.webScreen.WebActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moda on 26/07/17.
 */


/*
 *
 * To load the list of the contacts
 */

@SuppressLint("ValidFragment")
public class ContactsFragment extends Fragment implements View.OnClickListener {
  private FloatingActionButton fab;
  private TabLayout tabLayout;

  @SuppressLint("ValidFragment")
  public ContactsFragment(FloatingActionButton fab, TabLayout tabLayout) {
    this.fab = fab;
    this.tabLayout = tabLayout;
  }

  private View view;
  private ContactsAdapter mAdapter;
  private ArrayList<ContactsItem> contactsList = new ArrayList<>();
  private SearchView searchView;
  private TextView messageCall, noMatch;
  private boolean firstTime = true;
  private CoordinatorLayout root;
  private LandingActivity mActivity;
  private ProgressDialog pDialog;
  private ArrayList<Map<String, Object>> localContactsList = new ArrayList<>();
  private ArrayList<String> alreadyAddedContacts = new ArrayList<>();
  private boolean responseCame = false;
  private Typeface fontMedium, fontBold;
  private AppController appController = AppController.getInstance();
  private ImageView backIv;
  private RelativeLayout newSecretChat;
  private RelativeLayout newGroupChat;
  private LinearLayout llEmpty;
  private SessionApiCall sessionApiCall = new SessionApiCall();

  @Override
  @SuppressWarnings("unchecked")
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    LandingActivity.isConnect = true;
    if (view == null) {
      view = inflater.inflate(R.layout.contact_list, container, false);
    } else {

      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
    }

    mActivity = (LandingActivity) getActivity();
    mActivity.hideActionBar();
    root = (CoordinatorLayout) view.findViewById(R.id.root);
    fontMedium = appController.getMediumFont();
    fontBold = appController.getSemiboldFont();

    newSecretChat = (RelativeLayout) view.findViewById(R.id.requestSecretChat);

    newGroupChat = (RelativeLayout) view.findViewById(R.id.requestGroupChat);

    pDialog = new ProgressDialog(mActivity);
    pDialog.setMessage(getString(R.string.Sync_Contacts));
    pDialog.setCancelable(false);
    if (!AppController.getInstance().getContactSynced()) {

      if (AppController.getInstance().canPublish()) {
        // TODO
      } else {
        try {
          if (root != null) {
            Snackbar snackbar =
                Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                    Snackbar.LENGTH_SHORT);
            snackbar.show();
            View view = snackbar.getView();
            TextView txtv =
                (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    final RelativeLayout backButton = (RelativeLayout) view.findViewById(R.id.close_rl);

    //backButton.setVisibility(View.GONE);
    RelativeLayout addContacts = (RelativeLayout) view.findViewById(R.id.delete_rl);

    RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
    //uncomment below
    //        mAdapter = new ContactsAdapter(getActivity(), contactsList, this);
    RecyclerView.LayoutManager mLayoutManager =
        new LinearLayoutManager(getActivity().getApplicationContext());

    //        StickyLayoutManager layoutManager = new TopSnappedStickyLayoutManager(getActivity(), mAdapter);
    //        layoutManager.elevateHeaders(true); // Default elevation of 5dp
    //        // You can also specify a specific dp for elevation
    ////        layoutManager.elevateHeaders(10);
    //        rv.setLayoutManager(layoutManager);
    rv.setLayoutManager(mLayoutManager);
    rv.setItemAnimator(new DefaultItemAnimator());
    rv.setAdapter(mAdapter);
    messageCall = (TextView) view.findViewById(R.id.userMessagechat);
    llEmpty = (LinearLayout) view.findViewById(R.id.llEmpty);
    noMatch = (TextView) view.findViewById(R.id.noMatch);
    backIv = (ImageView) view.findViewById(R.id.backIv);

    backIv.setOnClickListener(this);


    /*
     * Api calls to fetch the list of the calls from the server
     */

    //makeStringReq();
    final TextView title = (TextView) view.findViewById(R.id.title);
    searchView = (SearchView) view.findViewById(R.id.search);
    searchView.setIconified(true);
    //searchView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_white));
    searchView.setIconifiedByDefault(true);
    searchView.clearFocus();
    searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean b) {
        if (b) {
          //backButton.setVisibility(View.INVISIBLE);
          title.setVisibility(View.GONE);
          backButton.setVisibility(View.GONE);
          searchView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_white));
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


    /*
     * Dont   need it anymore in the present logic
     */

    searchView.setOnCloseListener(new SearchView.OnCloseListener() {
      @Override
      public boolean onClose() {
        getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            noMatch.setVisibility(View.GONE);
            if (contactsList.size() == 0) {
              llEmpty.setVisibility(View.VISIBLE);
            }

            backButton.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            searchView.setBackgroundColor(
                ContextCompat.getColor(getActivity(), R.color.colorHintOfRed));
          }
        });

        return false;
      }
    });

    AutoCompleteTextView searchTextView =
        (AutoCompleteTextView) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
    try {
      Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
      mCursorDrawableRes.setAccessible(true);
      mCursorDrawableRes.set(searchTextView, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }


    /*
     * Open activity to add the contacts
     */
    addContacts.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {

      }
    });

    backButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if (getFragmentManager().getBackStackEntryCount() > 0) {
          getFragmentManager().popBackStack();
        } else {
          mActivity.onBackPressed();
        }
        // getActivity().onBackPressed();
      }
    });

    newSecretChat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(getActivity(), ContactsSecretChat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
      }
    });

    newGroupChat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(getActivity(), CreateGroup.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
      }
    });

    RelativeLayout refresh = (RelativeLayout) view.findViewById(R.id.refresh_rl);

    refresh.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        //                AppController.getInstance().setChatSynced(false);

        if (AppController.getInstance().canPublish()) {

        } else {

          try {
            if (root != null) {
              Snackbar snackbar =
                  Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                      Snackbar.LENGTH_SHORT);

              snackbar.show();
              View view = snackbar.getView();
              TextView txtv =
                  (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
              txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });

    rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rv,
        new RecyclerItemClickListener.OnItemClickListener() {

          @Override
          public void onItemClick(View view, int position) {

            if (position >= 0) {
              final ContactsItem item = (ContactsItem) mAdapter.getList().get(position);

              //                    Intent intent = new Intent(view.getContext(), ChatMessagesScreen.class);

              Intent intent = new Intent(view.getContext(), ChatMessageScreen.class);
              intent.putExtra("receiverUid", item.getContactUid());
              intent.putExtra("receiverName", item.getContactName());

              String docId =
                  AppController.getInstance().findDocumentIdOfReceiver(item.getContactUid(), "");

              if (docId.isEmpty()) {
                //                        docId = AppController.findDocumentIdOfReceiver(item.getContactUid(), Utilities.tsInGmt(), item.getContactName(),
                //                                item.getContactImage(), "", false, item.getContactIdentifier(), "", false,);
                //

                //#####################################
                //TODO replace isstart with actual value
                docId = AppController.findDocumentIdOfReceiver(item.getContactUid(),
                    Utilities.tsInGmt(), item.getContactName(), item.getContactImage(), "", false,
                    item.getContactIdentifier(), "", false, false);
                //#####################################
              }

              intent.putExtra("documentId", docId);
              intent.putExtra("receiverIdentifier", item.getContactIdentifier());
              intent.putExtra("receiverImage", item.getContactImage());
              intent.putExtra("colorCode", AppController.getInstance().getColorCode(position % 19));
              intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

              startActivity(intent,
                  ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
          }

          @Override
          public void onItemLongClick(View view, int position) {

          }
        }));

    Typeface tf = AppController.getInstance().getRegularFont();
    title.setTypeface(tf, Typeface.BOLD);

    //        MQttResponseHandler handler = new MQttResponseHandler(getActivity()) {
    //
    //            @Override
    //            public void execute(String event, JSONObject jsonObject) throws JSONException {
    //
    //
    //                if (event.equals(MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId())) {
    //                    responseCame = true;
    //                    saveContactsFromApiResponse(jsonObject);
    //
    //                    if (!AppController.getInstance().getChatSynced()) {
    //                        JSONObject obj = new JSONObject();
    //
    //                        obj.put("eventName", "SyncChats");
    //                        bus.post(obj);
    //                    }
    //                    AppController.getInstance().setContactSynced(true);
    //
    //                    AppController.getInstance().registerContactsObserver();
    //
    //                }
    //
    //
    //            }
    //        };

    //        AppController.initMQttContactSyncHandlerInstance(handler);

    TextView tvSecret = (TextView) view.findViewById(R.id.tvSecret);

    tvSecret.setTypeface(tf, Typeface.NORMAL);

    TextView tvGroup = (TextView) view.findViewById(R.id.tvGroup);
    TextView tvContact = (TextView) view.findViewById(R.id.tvContact);

    tvGroup.setTypeface(fontMedium);
    tvContact.setTypeface(fontBold);
    return view;
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);

    if (isVisibleToUser) {
      if (getActivity() != null) {
        hideKeyboard(getActivity());
      }
    } else if (searchView != null) {

      if (!searchView.isIconified()) {

        searchView.setIconified(true);
        if (getActivity() != null) {
          addContacts();
        }
      }
    }
  }

  public static void hideKeyboard(Context ctx) {
    InputMethodManager inputManager =
        (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
    View v = ((Activity) ctx).getCurrentFocus();
    if (v == null) return;
    inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
  }

  public void showNoSearchResults(final CharSequence constraint, boolean flag) {

    try {
      if (flag) {
        getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            noMatch.setVisibility(View.GONE);

            if (contactsList.size() == 0) {

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
              if (noMatch != null) {

                noMatch.setVisibility(View.VISIBLE);

                noMatch.setText(getString(R.string.noMatch) + getString(R.string.space) + constraint);
                llEmpty.setVisibility(View.GONE);
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

  private void addContacts() {

    if (mActivity != null) {
      contactsList.clear();
      mActivity.runOnUiThread(new Runnable() {
        @Override
        public void run() {

          mAdapter.notifyDataSetChanged();
        }
      });

      ArrayList<Map<String, Object>> contacts = AppController.getInstance()
          .getDbController()
          .loadContacts(AppController.getInstance().getContactsDocId());

      ContactsItem contact;
      Map<String, Object> contactIthPosition;

      if (contactsList != null) {

        for (int i = 0; i < contacts.size(); i++) {

          contact = new ContactsItem();

          contactIthPosition = contacts.get(i);
          //                    if (!contactIthPosition.containsKey("blocked")) {
          //contact.setItemType(0);
          contact.setContactIdentifier((String) contactIthPosition.get("contactIdentifier"));

          contact.setContactImage((String) contactIthPosition.get("contactPicUrl"));
          contact.setContactName((String) contactIthPosition.get("contactName"));
          contact.setContactStatus((String) contactIthPosition.get("contactStatus"));
          contact.setContactUid((String) contactIthPosition.get("contactUid"));
          contactsList.add(contact);

          //                    }
        }

        if (mActivity != null) {
          mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              mAdapter.notifyDataSetChanged();
            }
          });
        }

        if (contactsList.size() == 0) {

          llEmpty.setVisibility(View.VISIBLE);
        } else if (contactsList.size() > 0) {

          llEmpty.setVisibility(View.GONE);
          Collections.sort(contactsList, new SortContacts());
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onResume() {

    super.onResume();
    if (mAdapter != null) {

      if (firstTime) {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            if (getActivity() != null) addContacts();
          }
        }, 500);

        firstTime = false;
      } else {
        if (getActivity() != null) addContacts();
      }

      if (contactsList != null && contactsList.size() > 0) {

        if (llEmpty != null) {

          llEmpty.setVisibility(View.GONE);
        } else {

          TextView messageCall = (TextView) view.findViewById(R.id.userMessagechat);
          llEmpty.setVisibility(View.GONE);
        }
      }
    }
  }

  @SuppressWarnings("TryWithIdenticalCatches,unchecked")
  private void saveContactsFromApiResponse(JSONObject jsonObject) {

    AppController.getInstance()
        .unsubscribeToTopic(
            MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId());

    mActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        contactsList.clear();

        mAdapter.notifyDataSetChanged();
      }
    });

    try {
      JSONArray array = jsonObject.getJSONArray("contacts");

      JSONObject obj;
      ContactsItem item;

      ArrayList<Map<String, Object>> contacts = new ArrayList<>();

      Map<String, Object> contact;
      String contactUid, contactName, contactStatus, contactProfilePic, contactIdentifier,
          localContactNumber;
      int followStatus, _private;

      for (int i = 0; i < array.length(); i++) {

        obj = array.getJSONObject(i);

        localContactNumber = obj.getString("localNumber");
        followStatus = obj.getInt("followStatus");
        _private = obj.getInt("private");

        Map<String, Object> localContactInfo = getLocalContactIdAndName(localContactNumber);

        // contactName = (String) localContactInfo.get("userName");
        contactName = obj.getString("userName");
        contactUid = obj.getString("_id");
        contactProfilePic = "";

        if (obj.has("profilePic")) {
          contactProfilePic = obj.getString("profilePic");
        }
        contactIdentifier = obj.getString("number");

        if (obj.has("socialStatus")) {

          contactStatus = obj.getString("socialStatus");
        } else {
          contactStatus = getString(R.string.default_status);
        }

        item = new ContactsItem();

        item.setContactStatus(contactStatus);
        item.setContactUid(contactUid);

        item.setContactName(contactName);
        item.setContactImage(contactProfilePic);
        //                item.setItemType(0);
        item.setContactIdentifier(contactIdentifier);
        item.set_private(_private);
        item.setFollowStatus(followStatus);

        contactsList.add(item);

        mActivity.runOnUiThread(new Runnable() {
          @Override
          public void run() {

            mAdapter.notifyItemInserted(contactsList.size() - 1);
          }
        });

        contact = new HashMap<>();

        contact.put("contactLocalNumber", localContactNumber);
        contact.put("contactLocalId", localContactInfo.get("contactId"));
        contact.put("contactUid", contactUid);

        contact.put("contactType", localContactInfo.get("type"));

        contact.put("contactName", contactName);
        contact.put("contactStatus", contactStatus);
        contact.put("contactPicUrl", contactProfilePic);
        contact.put("contactIdentifier", contactIdentifier);
        contact.put("followStatus", followStatus);
        contact.put("private", _private);
        contacts.add(contact);
      }

      if (contactsList != null && contactsList.size() > 0) {

        if (llEmpty != null) {

          llEmpty.setVisibility(View.GONE);
        } else {

          TextView messageCall = (TextView) view.findViewById(R.id.userMessagechat);
          llEmpty.setVisibility(View.GONE);
        }
      }
      Collections.sort(contactsList, new SortContacts());
      mActivity.runOnUiThread(new Runnable() {
        @Override
        public void run() {

          mAdapter.notifyDataSetChanged();
        }
      });
      AppController.getInstance()
          .getDbController()
          .insertContactsInfo(contacts, AppController.getInstance().getContactsDocId());
      AppController.getInstance()
          .getDbController()
          .updateAllContacts(localContactsList, AppController.getInstance().getAllContactsDocId());
    } catch (JSONException e) {
      e.printStackTrace();
    }




    /*
     * To hit the API to send the contacts
     */
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

    JSONObject obj = new JSONObject();
    try {
      obj.put("eventName", "contactRefreshed");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    bus.post(obj);
  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.backIv) {
      //  fab.setVisibility(View.VISIBLE);
      FragmentManager fm = getFragmentManager();
      // FragmentManager fm = getChildFragmentManager();

      fm.popBackStack();
      //fm.
      //   getActivity().finish();
      // getFragmentManager().popBackStack();

      mActivity.visibleActionBar();
      tabLayout.setVisibility(View.VISIBLE);
      //            Log.d("exe", "back of contact fragment ");
    }
  }

  //    private void syncContacts() {
  //    }


  /*
   * Have to filter out the contacts,to remove the spaces
   */

  /**
   * FIXED_LINE = 0,
   * MOBILE = 1,
   * FIXED_LINE_OR_MOBILE = 2,
   * TOLL_FREE = 3,
   * PREMIUM_RATE = 4,
   * SHARED_COST = 5,
   * VOIP = 6,
   * PERSONAL_NUMBER = 7,
   * PAGER = 8,
   * UAN = 9,
   * VOICEMAIL = 10,
   * UNKNOWN = -1
   */

  private Map<String, Object> getLocalContactIdAndName(String localContactNumber) {

    Map<String, Object> map = new HashMap<>();
    for (int i = 0; i < localContactsList.size(); i++) {

      map = localContactsList.get(i);

      if (map.get("phoneNumber").equals(localContactNumber)) {

        return map;
      }
    }
    return map;
  }

  private static Bus bus = AppController.getBus();

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

      if (object.getString("eventName")
          .equals(MqttEvents.UserUpdates.value + "/" + AppController.getInstance().getUserId())) {

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

              ContactsItem item = contactsList.get(pos);

              item.setContactStatus(object.getString("socialStatus"));

              contactsList.set(pos, item);

              mActivity.runOnUiThread(new Runnable() {
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

              ContactsItem item = contactsList.get(pos1);

              item.setContactImage(object.getString("profilePic"));
              contactsList.set(pos1, item);

              mActivity.runOnUiThread(new Runnable() {
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

              //  item2.setItemType(0);
              contactsList.add(item2);

              mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  mAdapter.notifyItemInserted(contactsList.size() - 1);
                }
              });
            } else {
              contactsList.set(position, item2);

              mActivity.runOnUiThread(new Runnable() {
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

                  ContactsItem item = contactsList.get(pos3);

                  item.setContactName(object.getString("contactName"));
                  contactsList.set(pos3, item);

                  mActivity.runOnUiThread(new Runnable() {
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

                  contactsList.remove(pos4);

                  mActivity.runOnUiThread(new Runnable() {
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

                  String contactUid = object.getString("contactUid");

                  ContactsItem item = new ContactsItem();

                  item.setContactImage(object.getString("contactPicUrl"));
                  item.setContactName(object.getString("contactName"));
                  item.setContactStatus(object.getString("contactStatus"));
                  item.setContactIdentifier(object.getString("contactIdentifier"));
                  item.setContactUid(object.getString("contactUid"));
                  //  item.setItemType(0);
                  boolean contactAlreadyInList = false;

                  for (int i = 0; i < contactsList.size(); i++) {

                    if (contactsList.get(i).getContactUid().equals(contactUid)) {

                      contactAlreadyInList = true;

                      break;
                    }
                  }
                  if (!contactAlreadyInList) {
                    contactsList.add(item);

                    mActivity.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                        mAdapter.notifyItemInserted(contactsList.size() - 1);
                      }
                    });
                  }
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

                contactsList.remove(pos2);

                getActivity().runOnUiThread(new Runnable() {
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

        ContactsItem item = contactsList.get(pos);

        item.setContactName(object.getString("contactName"));
        contactsList.set(pos, item);

        getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            mAdapter.notifyDataSetChanged();
          }
        });
      } else if (object.getString("eventName")
          .equals(MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId())) {
        responseCame = true;
        saveContactsFromApiResponse(object);

        AppController.getInstance().setContactSynced(true);

        AppController.getInstance().registerContactsObserver();

        if (!AppController.getInstance().getChatSynced()) {

          JSONObject obj = new JSONObject();

          obj.put("eventName", "SyncChats");
          bus.post(obj);

          obj = new JSONObject();

          obj.put("eventName", "SyncCallLogs");
          bus.post(obj);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private int findContactPositionInList(String contactUid) {
    int pos = -1;
    for (int i = 0; i < contactsList.size(); i++) {
      if (contactsList.get(i).getContactUid().equals(contactUid)) {
        return i;
      }
    }
    return pos;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    //        Log.d("exe", "destroy the present fragment");
    tabLayout.setVisibility(View.VISIBLE);
    //tabLayout.setVisibility(View.GONE);

    newSecretChat.setVisibility(View.GONE);
    fab.setVisibility(View.VISIBLE);
  }
}