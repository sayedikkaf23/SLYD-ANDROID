package chat.hola.com.app.GroupChat.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.GroupChat.Adapters.AddMemberAdapter;
import chat.hola.com.app.GroupChat.ModelClasses.AddMemberItem;
import chat.hola.com.app.GroupChat.Utilities.SortMembersToAdd;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.SessionObserver;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
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
 * Created by moda on 26/09/17.
 */

public class AddMember extends AppCompatActivity {
  private SessionApiCall sessionApiCall = new SessionApiCall();

  private TextView noMatch, messageContact, numberOfContacts;

  private ArrayList<AddMemberItem> mContactData = new ArrayList<>();

  private Bus bus = AppController.getBus();
  private AddMemberAdapter mAdapter;

  private Intent intent;

  private String groupName, groupId;

  private ArrayList<Map<String, Object>> groupMembers = new ArrayList<>();

  private ArrayList<String> memberIds = new ArrayList<>();

  private RelativeLayout root;

  private String groupImageUrl, groupMessagesDocId, groupMembersDocId;

  @Override

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.gc_add_member);
    //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
    final ImageView backButton = (ImageView) findViewById(R.id.close);

    root = (RelativeLayout) findViewById(R.id.root);

    final TextView title = (TextView) findViewById(R.id.newGroup);
    numberOfContacts = (TextView) findViewById(R.id.membersCount);
    noMatch = (TextView) findViewById(R.id.noMatch);

    messageContact = (TextView) findViewById(R.id.messageContact);
    final SearchView searchView = (SearchView) findViewById(R.id.search);


    /*
     * For displaying the list of all the contacts
     */

    RecyclerView recyclerViewContacts = (RecyclerView) findViewById(R.id.rvContacts);
    recyclerViewContacts.setHasFixedSize(true);
    mAdapter = new AddMemberAdapter(AddMember.this, mContactData);
    recyclerViewContacts.setItemAnimator(new DefaultItemAnimator());

    recyclerViewContacts.setLayoutManager(
        new CustomLinearLayoutManager(AddMember.this, LinearLayoutManager.VERTICAL, false));
    recyclerViewContacts.setAdapter(mAdapter);

    intent = getIntent();

    addContacts();

    recyclerViewContacts.addOnItemTouchListener(
        new RecyclerItemClickListener(AddMember.this, recyclerViewContacts,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                if (position >= 0) {

                  final AddMemberItem member = mAdapter.getList().get(position);

                  if (member.isChatEnable()) {
                    if (member.isAllowedToAdd()) {

                      if (member.isSelected()) {

                        if (root != null) {
                          Snackbar snackbar = Snackbar.make(root,
                                  getString(R.string.AddedAlready, member.getContactName()),
                                  Snackbar.LENGTH_SHORT);

                          snackbar.show();
                          View view2 = snackbar.getView();
                          TextView txtv = (TextView) view2.findViewById(
                                  com.google.android.material.R.id.snackbar_text);
                          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                      } else {
                        String str = getString(R.string.AddMemberTo, member.getContactName())
                                + " "
                                + getString(R.string.AddMember, groupName);

                        androidx.appcompat.app.AlertDialog.Builder builder =
                                new androidx.appcompat.app.AlertDialog.Builder(AddMember.this, 0);

                        builder.setMessage(str);
                        builder.setPositiveButton(R.string.string_580,
                                new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int id) {

                                    String text = getString(R.string.Adding, member.getContactName())
                                            + " "
                                            + getString(R.string.ToGroupName, groupName);
                                    addMemberToGroup(text, member.getContactUid(),
                                            member.getContactIdentifier(), member.getContactImage(),
                                            member.getContactStatus(), member.getContactName(),
                                            member.isStar());

                                    dialog.dismiss();
                                  }
                                });
                        builder.setNegativeButton(R.string.string_591,
                                new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                  }
                                });

                        builder.show();
                      }
                    } else {
                      if (root != null) {
                        Snackbar snackbar =
                                Snackbar.make(root, getString(R.string.NoLongerMember, groupName),
                                        Snackbar.LENGTH_SHORT);

                        snackbar.show();
                        View view2 = snackbar.getView();
                        TextView txtv = (TextView) view2.findViewById(
                                com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                      }
                    }
                  }else{
                    Toast.makeText(AddMember.this,getString(R.string.chat_disable),Toast.LENGTH_LONG).show();
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {
              }
            }));

    searchView.setVisibility(View.VISIBLE);
    searchView.setIconified(true);
    searchView.setBackgroundColor(ContextCompat.getColor(AddMember.this, R.color.color_white));
    searchView.setIconifiedByDefault(true);
    searchView.clearFocus();
    searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean b) {
        if (b) {
          backButton.setVisibility(View.INVISIBLE);
          title.setVisibility(View.GONE);
          numberOfContacts.setVisibility(View.GONE);
        } else {

          backButton.setVisibility(View.VISIBLE);
          title.setVisibility(View.VISIBLE);
          numberOfContacts.setVisibility(View.VISIBLE);
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

    backButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    Typeface tf = AppController.getInstance().getRegularFont();

    numberOfContacts.setTypeface(tf, Typeface.NORMAL);
    title.setTypeface(tf, Typeface.BOLD);

    bus.register(this);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    this.intent = intent;
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
      Intent intent = new Intent(AddMember.this, ChatMessageScreen.class);

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
      } else if (object.getString("eventName")
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

              AddMemberItem item = mContactData.get(pos);

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

              AddMemberItem item = mContactData.get(pos1);

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

            AddMemberItem item2 = new AddMemberItem();
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

                  AddMemberItem item = mContactData.get(pos3);

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
                  AddMemberItem item = new AddMemberItem();

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
      } else if (object.getString("eventName")
          .equals(MqttEvents.GroupChats.value + "/" + AppController.getInstance().getUserId())) {

        /*
         * Don't have to take any action for the locally received ones
         */

        if (object.getString("groupId").equals(groupId)) {
          if (!object.has("self")) {

            switch (object.getInt("type")) {

              case 1:
                /*
                 * Member added
                 */

                updateContactStateIfAlreadyAdded(object, false);

                break;

              case 2:
                /*
                 *
                 *Member removed
                 */
                updateRemovedMemberInGroupUI(object.getString("memberId"));

                break;

              case 4:

                /*
                 *
                 *Group subject updated
                 */
                groupName = object.getString("groupSubject");

                break;

              case 5:
                /*
                 *
                 *Group icon updated
                 */
                groupImageUrl = object.getString("groupImageUrl");

                break;

              case 6:

                /*
                 * Member left the conversation
                 */
                updateRemovedMemberInGroupUI(object.getString("initiatorId"));

                break;
            }
          } else {

            switch (object.getInt("type")) {

              case 4:

                /*
                 *
                 *Group subject updated
                 */

                groupName = object.getString("groupSubject");

                break;
              case 5:
                /*
                 *
                 *Group icon updated
                 */
                groupImageUrl = object.getString("groupImageUrl");

                break;
            }
          }
        }
      } else if (object.getString("eventName").equals("ContactNameUpdated")) {

        final int pos = findContactPositionInList(object.getString("contactUid"));

        AddMemberItem item = mContactData.get(pos);

        item.setContactName(object.getString("contactName"));
        mContactData.set(pos, item);

        runOnUiThread(new Runnable() {
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

  /**
   * To add the list of contacts, from which contact to be added at time of creating the group to be
   * selected.
   */

  @SuppressWarnings("unchecked,TryWithIdenticalCatches")
  private void addContacts() {
    final ProgressDialog pDialog = new ProgressDialog(AddMember.this, 0);

    pDialog.setCancelable(false);

    pDialog.setMessage(getString(R.string.Load_Contacts));
    pDialog.show();

    ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);

    bar.getIndeterminateDrawable()
        .setColorFilter(ContextCompat.getColor(AddMember.this, R.color.color_black),
            android.graphics.PorterDuff.Mode.SRC_IN);

    Bundle extras = intent.getExtras();
    if (extras != null) {

      groupName = extras.getString("groupName");

      groupId = extras.getString("groupId");

      groupMembersDocId = extras.getString("groupMembersDocId");
      groupMembers =
          AppController.getInstance().getDbController().fetchGroupMember(groupMembersDocId);
      groupImageUrl = extras.getString("groupImageUrl");
      groupMessagesDocId = extras.getString("groupMessagesDocId");
    }

    memberIds = new ArrayList<>();

    for (int i = 0; i < groupMembers.size(); i++) {
      memberIds.add((String) groupMembers.get(i).get("memberId"));
    }

    ArrayList<Map<String, Object>> contacts = AppController.getInstance()
        .getDbController()
        .loadFriends(AppController.getInstance().getFriendsDocId());

    AddMemberItem contact;
    Map<String, Object> contactIthPosition;

    if (mContactData != null) {

      for (int i = 0; i < contacts.size(); i++) {

        contactIthPosition = contacts.get(i);
        if (!contactIthPosition.containsKey("blocked")) {

          contact = new AddMemberItem();

          if (memberIds.contains((String) contactIthPosition.get("userId"))) {

            contact.setSelected(true);
          } else {
            contact.setSelected(false);
          }

          contact.setContactIdentifier((String) contactIthPosition.get("userName"));
          contact.setAllowedToAdd(true);
          contact.setStar((boolean) contactIthPosition.get("isStar"));
          contact.setContactImage((String) contactIthPosition.get("profilePic"));
          contact.setContactName(
              contactIthPosition.get("firstName") + " " + contactIthPosition.get("lastName"));
          contact.setContactStatus((String) contactIthPosition.get("userName"));
          contact.setContactUid((String) contactIthPosition.get("userId"));
          contact.setChatEnable((boolean) contactIthPosition.get("isChatEnable"));
          mContactData.add(contact);
        }
      }
      if (mContactData.size() == 0) {

        messageContact.setVisibility(View.VISIBLE);
      } else if (mContactData.size() > 0) {
        Collections.sort(mContactData, new SortMembersToAdd());

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
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

      numberOfContacts.setText(getString(R.string.AddMemberSubTitle, mContactData.size() +
              getString(R.string.double_inverted_comma)));
    }
  }

  private void updateRemovedMemberInGroupUI(String memberId) {

    if (memberId.equals(AppController.getInstance().getUserId())) {

      /*
       * If the user himself has been removed from the group
       */

      for (int i = 0; i < mContactData.size(); i++) {

        AddMemberItem contact = mContactData.get(i);

        contact.setAllowedToAdd(false);
        mContactData.set(i, contact);

        final int k = i;
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            mAdapter.notifyItemChanged(k);
          }
        });
      }
    } else {

      int pos = -1;

      for (int i = 0; i < memberIds.size(); i++) {

        if (memberIds.get(i).equals(memberId)) {
          memberIds.remove(i);
          pos = i;
          break;
        }
      }
      if (pos != -1) {

        groupMembers.remove(pos);

        for (int i = 0; i < mContactData.size(); i++) {

          if (mContactData.get(i).getContactUid().equals(memberId)) {

            AddMemberItem contact = mContactData.get(i);

            contact.setSelected(false);
            mContactData.set(i, contact);

            final int k = i;
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                mAdapter.notifyItemChanged(k);
              }
            });
          }
        }
      }
    }
  }


  /*
   * To add a member to the group
   */

  @SuppressWarnings("TryWithIdenticalCatches")
  private void addMemberToGroup(String text, final String memberId, final String memberIdentifier,
      final String memberImage, final String memberStatus, final String memberName,
      final boolean isStar) {

    ArrayList<String> members = new ArrayList<>();

    members.add(memberId);

    final ProgressDialog pDialog = new ProgressDialog(AddMember.this, 0);

    pDialog.setCancelable(false);

    pDialog.setMessage(text);
    pDialog.show();

    ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);

    bar.getIndeterminateDrawable()
        .setColorFilter(ContextCompat.getColor(AddMember.this, R.color.color_black),
            PorterDuff.Mode.SRC_IN);

    JSONObject obj = new JSONObject();

    try {

      obj.put("members", new JSONArray(members));
      obj.put("chatId", groupId);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.POST, ApiOnServer.GROUP_MEMBER + "?chatId=" + groupId,
            obj, new com.android.volley.Response.Listener<JSONObject>() {

          @Override
          public void onResponse(JSONObject response) {

            try {

              /*
               * Progress dialog has been put in 3 places intentionally
               */

              switch (response.getInt("code")) {

                case 200: {
                  /*
                   * Member updated group icon successfully
                   */

                  informAllOfNewMemberAdded(groupId, memberId, memberIdentifier, memberImage,
                      memberStatus, memberName, isStar);
                  break;
                }

                default: {

                  if (pDialog.isShowing()) {

                    // pDialog.dismiss();
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
                  if (root != null) {

                    Snackbar snackbar =
                        Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);

                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(
                        com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                  }
                }
              }

              //  hideProgressDialog();
              if (pDialog.isShowing()) {

                // pDialog.dismiss();
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
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        }, new com.android.volley.Response.ErrorListener() {

          @Override
          public void onErrorResponse(VolleyError error) {
            // error.printStackTrace();

            if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
              try {
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
                            addMemberToGroup(text, memberId, memberIdentifier, memberImage,
                                memberStatus, memberName, isStar);
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
              } catch (NullPointerException e) {
                error.printStackTrace();
              }
            } else if (pDialog.isShowing()) {

              //  pDialog.dismiss();
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
          }
        }) {
          @Override
          public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("authorization", AppController.getInstance().getApiToken());
            headers.put("lang", Constants.LANGUAGE);

            return headers;
          }
        };

    jsonObjReq.setRetryPolicy(
        new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add the request to the RequestQueue.*/
    AppController.getInstance().addToRequestQueue(jsonObjReq, "addMemberApi");
  }

  @SuppressWarnings("unchecked")
  private void informAllOfNewMemberAdded(String groupId, String memberId, String memberIdentifier,
      String memberImage, String memberStatus, String memberName, boolean isStar) {

    try {
      JSONObject obj = new JSONObject();

      String userIdentifier = AppController.getInstance().getUserIdentifier();
      String userId = AppController.getInstance().getUserId();

      String tsInGmt = Utilities.tsInGmt();
      String messageId = String.valueOf(Utilities.getGmtEpoch());

      try {

        obj.put("memberIsStar", isStar);

        obj.put("memberId", memberId);
        obj.put("memberIdentifier", memberIdentifier);
        obj.put("memberImage", memberImage);
        obj.put("memberStatus", memberStatus);
        obj.put("memberIsAdmin", false);
        obj.put("groupId", groupId);
        obj.put("type", 1);
        obj.put("initiatorId", userId);
        obj.put("initiatorIdentifier", userIdentifier);

        obj.put("timestamp", tsInGmt);
        obj.put("id", String.valueOf(messageId));
      } catch (JSONException e) {
        e.printStackTrace();
      }


      /*
       * To inform the members of the group creation
       */

      if (AppController.getInstance().canPublish()) {

        CouchDbController db = AppController.getInstance().getDbController();

        Map<String, Object> member = new HashMap<>();

        member.put("memberId", memberId);
        member.put("memberIdentifier", memberIdentifier);

        member.put("memberIsStar", isStar);
        member.put("memberImage", memberImage);
        member.put("memberStatus", memberStatus);

        member.put("memberIsAdmin", false);
        db.addGroupMember(groupMembersDocId, member, memberId);

        Map<String, Object> membersInfo = db.fetchGroupInfo(groupMembersDocId);

        groupMembers = (ArrayList<Map<String, Object>>) membersInfo.get("groupMembersArray");

        memberIds.clear();





        /*
         * To add the message to the group chat messages document
         */

        Map<String, Object> map = new HashMap<>();

        map.put("messageType", "98");

        map.put("isSelf", true);
        map.put("from", groupId);
        map.put("Ts", tsInGmt);
        map.put("id", messageId);

        map.put("type", 1);

        map.put("initiatorId", userId);
        map.put("initiatorIdentifier", userIdentifier);

        map.put("memberId", memberId);
        map.put("memberIdentifier", memberIdentifier);

        map.put("deliveryStatus", "0");

        db.addNewChatMessageAndSort(groupMessagesDocId, map, tsInGmt, "");

        String currentMemberId;
        for (int i = 0; i < groupMembers.size(); i++) {

          currentMemberId = (String) groupMembers.get(i).get("memberId");

          if (currentMemberId.equals(userId)) {

            try {
              JSONObject obj2 = new JSONObject(obj.toString());
              obj2.put("self", true);
              obj2.put("timestamp", tsInGmt);
              obj2.put("id", messageId);
              obj2.put("message", getString(R.string.You)
                  + " "
                  + getString(R.string.AddedMember, memberName)
                  + " "
                  + getString(R.string.ToGroup));
              obj2.put("memberName", memberName);
              obj2.put("eventName", MqttEvents.GroupChats.value + "/" + userId);
              bus.post(obj2);
            } catch (JSONException e) {
              e.printStackTrace();
            }
          } else if (currentMemberId.equals(memberId)) {

            try {
              JSONObject obj2 = new JSONObject(obj.toString());

              obj2.put("members", convertMembersListToJSONArray(groupMembers));
              obj2.put("groupSubject", groupName);
              if (groupImageUrl != null && !groupImageUrl.isEmpty()) {

                obj2.put("groupImageUrl", groupImageUrl);
              }

              obj2.put("createdByMemberId", membersInfo.get("createdByMemberId"));
              obj2.put("createdByMemberIdentifier", membersInfo.get("createdByMemberIdentifier"));
              obj2.put("createdAt", membersInfo.get("createdAt"));

              AppController.getInstance()
                  .publish(MqttEvents.GroupChats.value + "/" + memberId, obj2, 1, false);
            } catch (JSONException e) {
              e.printStackTrace();
            }
          } else {

            AppController.getInstance()
                .publish(MqttEvents.GroupChats.value + "/" + currentMemberId, obj, 1, false);
          }

          memberIds.add(currentMemberId);
        }

        updateContactStateIfAlreadyAdded(obj, true);
      } else {
        if (root != null) {

          Snackbar snackbar =
              Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param object containing the details of the member added to the group
   */

  private void updateContactStateIfAlreadyAdded(JSONObject object, boolean isSelf) {
    try {
      String memberId = object.getString("memberId");

      for (int i = 0; i < mContactData.size(); i++) {
        if (mContactData.get(i).getContactUid().equals(memberId)) {

          AddMemberItem member = mContactData.get(i);

          member.setSelected(true);

          final int k = i;
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              mAdapter.notifyItemChanged(k);
            }
          });
          break;
        }
      }

      if (!isSelf) {
        Map<String, Object> member = new HashMap<>();
        member.put("memberId", object.getString("memberId"));
        member.put("memberIdentifier", object.getString("memberIdentifier"));
        member.put("memberImage", object.getString("memberImage"));
        member.put("memberStatus", object.getString("memberStatus"));
        member.put("memberIsAdmin", false);

        member.put("memberIsStar", object.getBoolean("memberIsStar"));
        groupMembers.add(member);

        memberIds.add(memberId);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  /*
   *To convert the list of the members to the JSON array which can be exchanged
   */
  private JSONArray convertMembersListToJSONArray(ArrayList<Map<String, Object>> membersList) {
    JSONObject member;

    Map<String, Object> map;

    JSONArray membersArray = new JSONArray();
    for (int i = 0; i < membersList.size(); i++) {

      map = membersList.get(i);

      member = new JSONObject();
      try {
        member.put("memberId", map.get("memberId"));
        member.put("memberIdentifier", map.get("memberIdentifier"));
        member.put("memberImage", map.get("memberImage"));
        member.put("memberStatus", map.get("memberStatus"));
        member.put("memberIsAdmin", map.get("memberIsAdmin"));
        member.put("memberIsStar", map.get("memberIsStar"));
      } catch (JSONException e) {
        e.printStackTrace();
      }

      membersArray.put(member);
    }
    return membersArray;
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
}
