package chat.hola.com.app.home.connect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Activities.ContactsSecretChat;
import chat.hola.com.app.Adapters.ContactsAdapter;
import chat.hola.com.app.Adapters.ContactsCallAdapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.GroupChat.Activities.CreateGroup;
import chat.hola.com.app.ModelClasses.ContactsItem;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ContactSync;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.SortContacts;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ContactCallActivity extends AppCompatActivity implements View.OnClickListener {
  private ContactsCallAdapter mAdapter;
  private ArrayList<ContactsItem> contactsList = new ArrayList<>();
  private SearchView searchView;
  private TextView messageCall, noMatch;
  private boolean firstTime = true;
  private CoordinatorLayout root;
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
  private SwipeRefreshLayout swipeRefresh;


//  AdView adView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.contact_list);
    LandingActivity.isConnect = true;
    bus.register(this);
    root = (CoordinatorLayout) findViewById(R.id.root);
    fontMedium = appController.getMediumFont();
    fontBold = appController.getSemiboldFont();

    newSecretChat = (RelativeLayout) findViewById(R.id.requestSecretChat);

    newGroupChat = (RelativeLayout) findViewById(R.id.requestGroupChat);

    swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

    pDialog = new ProgressDialog(this);
    pDialog.setMessage(getString(R.string.fetching));
    pDialog.setCancelable(false);
    if (!AppController.getInstance().isFriendsFetched()) {

      if (AppController.getInstance().canPublish()) {
        //getFriends(false);
        swipeRefresh.setRefreshing(true);
        new ContactSync(ContactCallActivity.this);

      } else {
        try {
          if (root != null) {
            Snackbar snackbar =
                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                            Snackbar.LENGTH_SHORT);
            snackbar.show();
            View view = snackbar.getView();
            TextView txtv = (TextView) findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    final RelativeLayout backButton = (RelativeLayout) findViewById(R.id.close_rl);

    //backButton.setVisibility(View.GONE);
    RelativeLayout addContacts = (RelativeLayout) findViewById(R.id.delete_rl);

    RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
    mAdapter = new ContactsCallAdapter(getApplicationContext(), contactsList, null);
    RecyclerView.LayoutManager mLayoutManager =
            new LinearLayoutManager(this.getApplicationContext());

    //        StickyLayoutManager layoutManager = new TopSnappedStickyLayoutManager(this, mAdapter);
    //        layoutManager.elevateHeaders(true); // Default elevation of 5dp
    //        // You can also specify a specific dp for elevation
    ////        layoutManager.elevateHeaders(10);
    //        rv.setLayoutManager(layoutManager);
    rv.setLayoutManager(mLayoutManager);
    rv.setItemAnimator(new DefaultItemAnimator());
    rv.setAdapter(mAdapter);
    messageCall = (TextView) findViewById(R.id.userMessagechat);
    llEmpty = (LinearLayout) findViewById(R.id.llEmpty);
    noMatch = (TextView) findViewById(R.id.noMatch);
    backIv = (ImageView) findViewById(R.id.backIv);
//    adView = (AdView) findViewById(R.id.adView);

    backIv.setOnClickListener(this);

//    adView.setVisibility(View.VISIBLE);

    // ads
//    AdRequest adRequest = new AdRequest.Builder().build();
//    adView.loadAd(adRequest);

    /*
     * Api calls to fetch the list of the calls from the server
     */

    //makeStringReq();
    final TextView title = (TextView) findViewById(R.id.title);
    title.setText("New Call");

    searchView = (SearchView) findViewById(R.id.search);
    searchView.setIconified(true);
    //searchView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_white));
    searchView.setIconifiedByDefault(true);
    searchView.clearFocus();
    searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean b) {
        if (b) {
          //backButton.setVisibility(View.INVISIBLE);
          title.setVisibility(View.GONE);
          backButton.setVisibility(View.GONE);
          searchView.setBackgroundColor(getResources().getColor(R.color.color_white));
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
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            noMatch.setVisibility(View.GONE);
            if (contactsList.size() == 0) {
              llEmpty.setVisibility(View.VISIBLE);
            }

            backButton.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            searchView.setBackgroundColor(getResources().getColor(R.color.colorHintOfRed));
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
          onBackPressed();
        }
        // this.onBackPressed();
      }
    });

    newSecretChat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent intent = new Intent(ContactCallActivity.this, ContactsSecretChat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
      }
    });

    newGroupChat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(ContactCallActivity.this, CreateGroup.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
      }
    });

    swipeRefresh.setOnRefreshListener(() -> {
      if (AppController.getInstance().canPublish()) {
        swipeRefresh.setRefreshing(true);
        new ContactSync(ContactCallActivity.this);
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
    });

//    rv.addOnItemTouchListener(new RecyclerItemClickListener(this, rv,
//            new RecyclerItemClickListener.OnItemClickListener() {
//
//              @Override
//              public void onItemClick(View view, int position) {
//
//                if (position >= 0) {
//                  final ContactsItem item = (ContactsItem) mAdapter.getList().get(position);
//
//                  if(item.isChatEnable()) {
//
//                    //                    Intent intent = new Intent(view.getContext(), ChatMessagesScreen.class);
//
//                    Intent intent = new Intent(view.getContext(), ChatMessageScreen.class);
//                    intent.putExtra("receiverUid", item.getContactUid());
//                    intent.putExtra("receiverName", item.getContactName());
//
//                    String docId =
//                            AppController.getInstance().findDocumentIdOfReceiver(item.getContactUid(), "");
//
//                    if (docId.isEmpty()) {
//                      docId = AppController.findDocumentIdOfReceiver(item.getContactUid(),
//                              Utilities.tsInGmt(), item.getContactName(), item.getContactImage(), "", false,
//                              item.getContactIdentifier(), "", false, item.getStar());
//                    }
//
//                    intent.putExtra("documentId", docId);
//                    intent.putExtra("receiverIdentifier", item.getContactIdentifier());
//                    intent.putExtra("receiverImage", item.getContactImage());
//                    intent.putExtra("isStar", item.getStar());
//                    intent.putExtra("colorCode", AppController.getInstance().getColorCode(position % 19));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//
//                    startActivity(intent);
//
//                    supportFinishAfterTransition();
//                  }else{
//                    Toast.makeText(ContactCallActivity.this,getString(R.string.chat_disable),Toast.LENGTH_LONG).show();
//                  }
//                }
//              }
//
//              @Override
//              public void onItemLongClick(View view, int position) {
//
//              }
//            }));

    Typeface tf = AppController.getInstance().getRegularFont();
    title.setTypeface(tf, Typeface.BOLD);

    //        MQttResponseHandler handler = new MQttResponseHandler(this) {
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

    TextView tvSecret = (TextView) findViewById(R.id.tvSecret);

    tvSecret.setTypeface(tf, Typeface.NORMAL);

    TextView tvGroup = (TextView) findViewById(R.id.tvGroup);
    TextView tvContact = (TextView) findViewById(R.id.tvContact);

    tvGroup.setTypeface(fontMedium);
    tvContact.setTypeface(fontBold);
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
        runOnUiThread(new Runnable() {
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
        runOnUiThread(new Runnable() {
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

    contactsList.clear();
    runOnUiThread(new Runnable() {
      @Override
      public void run() {

        mAdapter.notifyDataSetChanged();
      }
    });

    ArrayList<Map<String, Object>> contacts = AppController.getInstance()
            .getDbController()
            .loadFriends(AppController.getInstance().getFriendsDocId());

    ContactsItem contact;
    Map<String, Object> contactIthPosition;

    if (contactsList != null) {

      for (int i = 0; i < contacts.size(); i++) {

        contact = new ContactsItem();

        contactIthPosition = contacts.get(i);
        //                    if (!contactIthPosition.containsKey("blocked")) {
        //contact.setItemType(0);
        contact.setContactIdentifier((String) contactIthPosition.get("userName"));
        contact.setStar((boolean) contactIthPosition.get("isStar"));
        contact.setContactImage((String) contactIthPosition.get("profilePic"));
        contact.setContactName(
                contactIthPosition.get("firstName") + " " + contactIthPosition.get("lastName"));
        contact.setContactStatus((String) contactIthPosition.get("userName"));
        contact.setContactUid((String) contactIthPosition.get("userId"));
        contact.setChatEnable((boolean) contactIthPosition.get("isChatEnable"));
        contactsList.add(contact);

        //                    }
      }

      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          mAdapter.notifyDataSetChanged();
        }
      });

      if (contactsList.size() == 0) {

        llEmpty.setVisibility(View.VISIBLE);
      } else if (contactsList.size() > 0) {

        llEmpty.setVisibility(View.GONE);
        Collections.sort(contactsList, new SortContacts());
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
            addContacts();
          }
        }, 500);

        firstTime = false;
      } else {
        addContacts();
      }

      if (contactsList != null && contactsList.size() > 0) {

        if (llEmpty != null) {

          llEmpty.setVisibility(View.GONE);
        } else {

          TextView messageCall = (TextView) findViewById(R.id.userMessagechat);
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

    //        runOnUiThread(new Runnable() {
    //            @Override
    //            public void run() {
    //                contactsList.clear();
    //
    //
    //                mAdapter.notifyDataSetChanged();
    //            }
    //        });

    try {
      JSONArray array = jsonObject.getJSONArray("contacts");

      Gson gson = new Gson();
      List<Friend> data = gson.fromJson(array.toString(), new TypeToken<List<Friend>>() {
      }.getType());

      saveFriendsToDb(data);

      addContacts();

            /*JSONObject obj;
            ContactsItem item;


            ArrayList<Map<String, Object>> contacts = new ArrayList<>();


            Map<String, Object> contact;
            String contactUid, contactName, contactStatus, contactProfilePic, contactIdentifier, localContactNumber;
            int followStatus, _private;
            String fname, laname;

            for (int i = 0; i < array.length(); i++) {

                obj = array.getJSONObject(i);

                localContactNumber = obj.getString("localNumber");
                followStatus = obj.getInt("followStatus");
                _private = obj.getInt("private");

                Map<String, Object> localContactInfo = getLocalContactIdAndName(localContactNumber);


                // contactName = (String) localContactInfo.get("userName");
                //contactName = obj.getString("userName");
                fname = obj.getString("firstName");
                laname = obj.getString("lastName");

                if (!fname.trim().isEmpty() && !laname.trim().isEmpty())
                    contactName = fname + " " + laname;
                else if (laname.trim().isEmpty() && !fname.trim().isEmpty())
                    contactName = fname;
                else if (fname.trim().isEmpty() && !laname.trim().isEmpty())
                    contactName = laname;
                else
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


                runOnUiThread(new Runnable() {
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


                    TextView messageCall = (TextView) findViewById(R.id.userMessagechat);
                    llEmpty.setVisibility(View.GONE);


                }
            }
            Collections.sort(contactsList, new SortContacts());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mAdapter.notifyDataSetChanged();
                }
            });
            AppController.getInstance().getDbController().insertContactsInfo(contacts, AppController.getInstance().getContactsDocId());
            AppController.getInstance().getDbController().updateAllContacts(localContactsList, AppController.getInstance().getAllContactsDocId());*/

    } catch (JSONException e) {
      e.printStackTrace();
    } catch (Exception e) {
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
      finish();
    }
  }

  public void getFriends(boolean isRefresh) {

    if (pDialog != null && (!AppController.getInstance().isFriendsFetched() || isRefresh)) {
      pDialog.show();
      ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);

      bar.getIndeterminateDrawable()
              .setColorFilter(getResources().getColor(R.color.color_black),
                      android.graphics.PorterDuff.Mode.SRC_IN);
    }
    hitContactSyncApi();
  }

  @SuppressWarnings("TryWithIdenticalCatches")
  private void hitContactSyncApi() {

    AppController.getInstance()
            .subscribeToTopic(
                    MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId(), 0);

    JsonObjectRequest jsonObjReq =
            new JsonObjectRequest(Request.Method.GET, ApiOnServer.GET_FOLLWERS_FOLLOWEE, null,
                    new Response.Listener<JSONObject>() {

                      @Override
                      public void onResponse(JSONObject response) {

                      }
                    }, new Response.ErrorListener() {

              @Override
              public void onErrorResponse(VolleyError error) {

                if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                  SessionObserver sessionObserver = new SessionObserver();
                  sessionObserver.getObservable()
                          .subscribeOn(Schedulers.io())
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribe(new DisposableObserver<Boolean>() {
                            @Override
                            public void onNext(Boolean flag) {
                              Handler handler = new Handler();
                              handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                  hitContactSyncApi();
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
                } else {
                  if (pDialog != null && pDialog.isShowing()) {

                    Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();

                    if (context instanceof Activity) {

                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing()
                                && !((Activity) context).isDestroyed()) {
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

    jsonObjReq.setRetryPolicy(
            new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add the request to the RequestQueue.*/
    AppController.getInstance().addToRequestQueue(jsonObjReq, "syncContactsApiRequest");
  }

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

              ContactsItem item = contactsList.get(pos1);

              item.setContactImage(object.getString("profilePic"));
              contactsList.set(pos1, item);

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

              //  item2.setItemType(0);
              contactsList.add(item2);

              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  mAdapter.notifyItemInserted(contactsList.size() - 1);
                }
              });
            } else {
              contactsList.set(position, item2);

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

                  ContactsItem item = contactsList.get(pos3);

                  item.setContactName(object.getString("contactName"));
                  contactsList.set(pos3, item);

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

                  contactsList.remove(pos4);

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

                    runOnUiThread(new Runnable() {
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

        ContactsItem item = contactsList.get(pos);

        item.setContactName(object.getString("contactName"));
        contactsList.set(pos, item);

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            mAdapter.notifyDataSetChanged();
          }
        });
      } else if (object.getString("eventName")
              .equals(MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId())) {
        responseCame = true;
        swipeRefresh.setRefreshing(false);
        //saveContactsFromApiResponse(object);

        addContacts();

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

  public void saveFriendsToDb(List<Friend> data) {

    try {

      ArrayList<Map<String, Object>> friends = new ArrayList<>();

      for (Friend f : data) {

        Map<String, Object> friend = new HashMap<>();

        friend.put("userId", f.getId());
        friend.put("userName", f.getUserName());
        friend.put("countryCode", f.getCountryCode());
        friend.put("number", f.getNumber());
        friend.put("profilePic", f.getProfilePic());
        friend.put("firstName", f.getFirstName());
        friend.put("lastName", f.getLastName());
        friend.put("socialStatus", f.getStatus());
        friend.put("isStar", f.isStar());
        friend.put("starRequest", f.getStarData());
        friend.put("private", f.getPrivate());
        friend.put("friendStatusCode", f.getFriendStatusCode());
        friend.put("followStatus", f.getFollowStatus());
        friend.put("timestamp", f.getTimestamp());
        friend.put("message", f.getMessage());
        friend.put("isChatEnable",f.isChatEnable());

        //            Log.d("log1","friends data-->"+f.isStar());

        friends.add(friend);

        friend = null;
      }

      AppController.getInstance()
              .getDbController()
              .insertFriendsInfo(friends, AppController.getInstance().getFriendsDocId());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
