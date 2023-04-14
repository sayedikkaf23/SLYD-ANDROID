package chat.hola.com.app.Profile;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Adapters.SelectUserAdapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ModelClasses.SelectUserItem;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.SortUsers;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by moda on 12/10/17.
 */

public class BlockedUser extends AppCompatActivity {

    /*
     * To display the list of the blocked users
     */

    private SessionApiCall sessionApiCall = new SessionApiCall();
    private SelectUserAdapter mAdapter;
//    private String chatName;

    private ArrayList<SelectUserItem> mUserData = new ArrayList<>();

    private Bus bus = AppController.getBus();
    private RelativeLayout root;
    private LinearLayout llEmpty;
    RecyclerView recyclerViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);

        llEmpty = (LinearLayout) findViewById(R.id.llEmpty);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.list_of_users);
        recyclerViewUsers.setHasFixedSize(true);
        mAdapter = new SelectUserAdapter(BlockedUser.this, mUserData);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setLayoutManager(new CustomLinearLayoutManager(BlockedUser.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewUsers.setAdapter(mAdapter);


        root = (RelativeLayout) findViewById(R.id.root);

        loadContacts();

        recyclerViewUsers.addOnItemTouchListener(new RecyclerItemClickListener(BlockedUser.this, recyclerViewUsers, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {


                if (position >= 0) {

                    /*
                     *To unblock the user
                     */


                    final Dialog dialog = new Dialog(BlockedUser.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.gc_admin_options);
                    dialog.show();


                    TextView removeMember = (TextView) dialog.findViewById(R.id.removeMember);


                    RelativeLayout makeAdmin_rl = (RelativeLayout) dialog.findViewById(R.id.rl1);
                    RelativeLayout removeMember_rl = (RelativeLayout) dialog.findViewById(R.id.rl2);


                    makeAdmin_rl.setVisibility(View.GONE);


                    removeMember.setText(getString(R.string.Unblock));

                    removeMember_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            dialog.dismiss();
                            requestBlockFeatureOnServer(mUserData.get(position).getUserId(), position);

                        }
                    });


                }


            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        TextView title = (TextView) findViewById(R.id.title);


        Typeface tf = AppController.getInstance().getRegularFont();


        title.setText(getString(R.string.BlockedUsers));
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
        final ProgressDialog pDialog = new ProgressDialog(BlockedUser.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.Load_Contacts));
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(BlockedUser.this, R.color.color_black),
                android.graphics.PorterDuff.Mode.SRC_IN);


        Map<String, Object> map;


        SelectUserItem item;


        ArrayList<Map<String, Object>> blockedUsers = AppController.getInstance().getDbController().loadBlockedUsers(
                AppController.getInstance().getBlockedDocId());


        String contactDocId = AppController.getInstance().getFriendsDocId();


        Map<String, Object> contactInfo;
        for (int i = 0; i < blockedUsers.size(); i++) {

            map = blockedUsers.get(i);


            /*
             * Display only the list of the users blocked by me
             */


            if ((boolean) map.get("self")) {
                item = new SelectUserItem();

                item.setUserId((String) map.get("userId"));


                /*
                 * Since email is shown as the lower part of each of the contacts row in case of the contact sync
                 */

                item.setUserIdentifier((String) map.get("userIdentifier"));
                contactInfo = AppController.getInstance().getDbController().getFriendInfoFromUid(contactDocId,
                        (String) map.get("userId"));

                if (contactInfo != null) {
                    item.setUserName(contactInfo.get("firstName") + " " + contactInfo.get("lastName"));


                    String image = (String) contactInfo.get("profilePic");


                    if (image != null && !image.isEmpty()) {
                        item.setUserImage(image);
                    } else {

                        item.setUserImage("");
                    }


                } else {


                    /*
                     * If userId doesn't exists in contact
                     */
                    item.setUserName((String) map.get("userIdentifier"));


                    item.setUserImage("");
                }


                mUserData.add(item);

            }
        }


        Collections.sort(mUserData, new SortUsers());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mAdapter.notifyDataSetChanged();

            }
        });


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


        if (mUserData.size() == 0) {
            recyclerViewUsers.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);


            if (root != null) {


                Snackbar snackbar = Snackbar.make(root, R.string.none_blocked,
                        Snackbar.LENGTH_SHORT);
                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mUserData.clear();


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
            Intent intent = new Intent(BlockedUser.this, ChatMessageScreen.class);

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


    /**
     * To hit the block user api on the server
     */

    private void requestBlockFeatureOnServer(final String userId, final int position) {


        final ProgressDialog pDialog = new ProgressDialog(BlockedUser.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.BlockUser, "Unblocking"));


        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(BlockedUser.this, R.color.color_black),
                PorterDuff.Mode.SRC_IN);

        JSONObject obj = new JSONObject();
        try {
            obj.put("opponentId", userId);
            obj.put("type", "unblock");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ApiOnServer.BLOCK_USER, obj,
                new com.android.volley.Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            if (response.getInt("code") == 200) {


                                AppController.getInstance().getDbController().removeUnblockedUser(
                                        AppController.getInstance().getFriendsDocId(), userId);


                                /*
                                 *  Successfully unblocked previously blocked user
                                 */


                                JSONObject obj = new JSONObject();

                                obj.put("eventName", "UserUnblocked");

                                obj.put("opponentId", userId);

                                bus.post(obj);


                                obj = new JSONObject();
                                obj.put("type", 6);

                                obj.put("blocked", false);

                                obj.put("initiatorId", AppController.getInstance().getUserId());

                                AppController.getInstance().publish(MqttEvents.UserUpdates.value + "/" + userId, obj, 1, false);


                                try {

                                    mUserData.remove(position);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (position == 0) {
                                                mAdapter.notifyDataSetChanged();
                                            } else {
                                                mAdapter.notifyItemRemoved(position);
                                            }
                                        }
                                    });


                                } catch (Exception e) {
                                    onBackPressed();
                                }
                                if (mUserData.size() == 0) {

                                    recyclerViewUsers.setVisibility(View.GONE);
                                    llEmpty.setVisibility(View.VISIBLE);


                                }


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


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse!=null && error.networkResponse.statusCode == 406) {
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
                                            requestBlockFeatureOnServer(userId, position);
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
                } else if (pDialog.isShowing()) {


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


                    Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);


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


                return headers;
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "opponentBlockApiRequest");


    }

}
