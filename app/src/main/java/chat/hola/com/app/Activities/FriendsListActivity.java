package chat.hola.com.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ForwardMessage.Forward_ContactItem;
import chat.hola.com.app.ForwardMessage.Forward_ContactsAdapter;
import chat.hola.com.app.ForwardMessage.SortContactsToForward;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;

public class FriendsListActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_CONTACTS = 4;
    private Forward_ContactsAdapter mAdapter;
//    private String chatName;

    private ArrayList<Forward_ContactItem> mContactData = new ArrayList<>();

    private Bus bus = AppController.getBus();

    private LinearLayout llEmpty;

    private TextView messageContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        RecyclerView recyclerViewUsers = (RecyclerView) findViewById(R.id.list_of_users);
        recyclerViewUsers.setHasFixedSize(true);
        mAdapter = new Forward_ContactsAdapter(FriendsListActivity.this, mContactData);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setLayoutManager(new CustomLinearLayoutManager(FriendsListActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewUsers.setAdapter(mAdapter);

        llEmpty = (LinearLayout) findViewById(R.id.llEmpty);

        messageContact = (TextView) findViewById(R.id.noMatch);


        loadContacts();

        recyclerViewUsers.addOnItemTouchListener(new RecyclerItemClickListener(FriendsListActivity.this, recyclerViewUsers, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {


                if (position >= 0) {


                    /*
                     * To add a conversation,we add a members nested collection
                     */


                    Forward_ContactItem item = mContactData.get(position);
                    Intent i = new Intent();
                    i.putExtra("identifier", item.getContactIdentifier());
                    i.putExtra("userId", item.getContactUid());
                    i.putExtra("userImage", item.getContactImage());
                    i.putExtra("name", item.getContactName());
                    setResult(REQUEST_CODE_CONTACTS,i);
                    onBackPressed();
                    supportFinishAfterTransition();

                }


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        TextView title = (TextView) findViewById(R.id.title);


        Typeface tf = AppController.getInstance().getRegularFont();
        title.setText(getString(R.string.select_contact));

        title.setTypeface(tf, Typeface.BOLD);
        ImageView close = (ImageView) findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bus.register(this);
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

    @SuppressWarnings("TryWithIdenticalCatches")
    private void loadContacts() {
        final ProgressDialog pDialog = new ProgressDialog(FriendsListActivity.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.Load_Contacts));
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(FriendsListActivity.this, R.color.color_black),
                android.graphics.PorterDuff.Mode.SRC_IN);


        ArrayList<Map<String, Object>> contacts = AppController.getInstance().getDbController().loadFriends(AppController.getInstance().getFriendsDocId());

        Forward_ContactItem contact;
        Map<String, Object> contactIthPosition;


        if (mContactData != null) {


            for (int i = 0; i < contacts.size(); i++) {

                contactIthPosition = contacts.get(i);

                if (!contactIthPosition.containsKey("blocked")) {
                    contact = new Forward_ContactItem();
                    contact.setSelected(false);
                    contact.setContactIdentifier((String) contactIthPosition.get("userName"));

                    contact.setContactImage((String) contactIthPosition.get("profilePic"));
                    contact.setContactName(contactIthPosition.get("firstName") + " " + contactIthPosition.get("lastName"));


                    contact.setContactStatus((String) contactIthPosition.get("userName"));
                    contact.setStar((boolean) contactIthPosition.get("isStar"));
                    contact.setContactUid((String) contactIthPosition.get("userId"));
                    mContactData.add(contact);


                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mAdapter.notifyItemInserted(mContactData.size() - 1);
//                    }
//                });
            }
            if (mContactData.size() == 0) {


                messageContact.setVisibility(View.VISIBLE);
                messageContact.setText(getString(R.string.SecretChatNoFriends));

            } else if (mContactData.size() > 0) {
                Collections.sort(mContactData, new SortContactsToForward());

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

        } else {
            llEmpty.setVisibility(View.VISIBLE);
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


        if (mContactData.size() == 0) {
            llEmpty.setVisibility(View.VISIBLE);


        }


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
        loadContacts();
    }


    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(FriendsListActivity.this, ChatMessageScreen.class);

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

            }


        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
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
