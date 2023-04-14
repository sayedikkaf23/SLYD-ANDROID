package chat.hola.com.app.GroupChat.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.SessionObserver;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by moda on 23/09/17.
 */

public class EditGroupSubject extends AppCompatActivity {


    private Bus bus = AppController.getBus();
    private SessionApiCall sessionApiCall = new SessionApiCall();

    private RelativeLayout root;

    private Intent intent;
    private EditText editText;

    private TextInputLayout editText_til;

    private ImageView selEmoji, selKeybord;
    private ProgressDialog pDialog;


    private TextView maxLength;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gc_update_subject);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        selEmoji = (ImageView) findViewById(R.id.emojiButton);
        selKeybord = (ImageView) findViewById(R.id.chat_keyboard_icon);
        root = (RelativeLayout) findViewById(R.id.root);
        editText = (EditText) findViewById(R.id.et);


        final RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
        editText_til = (TextInputLayout) findViewById(R.id.input_layout_name2);
        final EmojiconsPopup popup = new EmojiconsPopup(root, this);
        intent = getIntent();


        RelativeLayout ok = (RelativeLayout) findViewById(R.id.ok_rl);

        RelativeLayout cancel = (RelativeLayout) findViewById(R.id.cancel_rl);
        maxLength = (TextView) findViewById(R.id.maxLength);
        editText.addTextChangedListener(new MyTextWatcher(editText));

        setupActivity(intent);


        popup.setSizeForSoftKeyboard();


        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                       @Override
                                       public void onDismiss() {


                                           selKeybord.setVisibility(View.GONE);
                                           selEmoji.setVisibility(View.VISIBLE);

                                       }
                                   }

        );


        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

                                                     @Override
                                                     public void onKeyboardOpen(int keyBoardHeight) {

                                                     }

                                                     @Override
                                                     public void onKeyboardClose() {


                                                         if (popup.isShowing())
                                                             popup.dismiss();

                                                     }
                                                 }

        );


        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

                                               @Override
                                               public void onEmojiconClicked(Emojicon emojicon) {
                                                   if (editText == null || emojicon == null) {
                                                       return;
                                                   }

                                                   int start = editText.getSelectionStart();
                                                   int end = editText.getSelectionEnd();
                                                   if (start < 0) {
                                                       editText.append(emojicon.getEmoji());
                                                   } else {
                                                       editText.getText().replace(Math.min(start, end),
                                                               Math.max(start, end), emojicon.getEmoji(), 0,
                                                               emojicon.getEmoji().length());
                                                   }
                                               }
                                           }

        );


        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

                                                        @Override
                                                        public void onEmojiconBackspaceClicked(View v) {
                                                            KeyEvent event = new KeyEvent(
                                                                    0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                                                            editText.dispatchKeyEvent(event);
                                                        }
                                                    }

        );


        selEmoji.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {


                                            selKeybord.setVisibility(View.VISIBLE);
                                            selEmoji.setVisibility(View.GONE);


                                            if (!popup.isShowing()) {


                                                if (popup.isKeyBoardOpen()) {
                                                    popup.showAtBottom();

                                                } else {
                                                    editText.setFocusableInTouchMode(true);
                                                    editText.requestFocus();
                                                    popup.showAtBottomPending();
                                                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

                                                }
                                            } else {
                                                popup.dismiss();
                                            }


                                        }
                                    }

        );


        selKeybord.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {


                                              selKeybord.setVisibility(View.GONE);
                                              selEmoji.setVisibility(View.VISIBLE);


                                              if (!popup.isShowing()) {


                                                  editText.setFocusableInTouchMode(true);
                                                  editText.requestFocus();
                                                  popup.showAtBottomPending();
                                                  final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                  inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

                                              } else {
                                                  popup.dismiss();

                                              }

                                          }
                                      }

        );

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                onBackPressed();

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (validateSubject()) {

                    requestUpdateGroupSubjectApi(editText.getText().toString().trim());
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                onBackPressed();

            }
        });

        TextView title = (TextView) findViewById(R.id.title);

        title.setTypeface(AppController.getInstance().getRegularFont(), Typeface.BOLD);


        TextView okTv = (TextView) findViewById(R.id.ok_tv);

        okTv.setTypeface(AppController.getInstance().getRegularFont(), Typeface.BOLD);

        TextView cancelTv = (TextView) findViewById(R.id.cancel_tv);

        cancelTv.setTypeface(AppController.getInstance().getRegularFont(), Typeface.BOLD);
        maxLength.setTypeface(AppController.getInstance().getRegularFont(), Typeface.NORMAL);

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;
        setupActivity(intent);


    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void setupActivity(Intent intent) {


        Bundle extras = intent.getExtras();
        if (extras != null) {


            editText.setText(extras.getString("groupSubject"));
        }


    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(EditGroupSubject.this, ChatMessageScreen.class);

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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.et:
                    validateSubject();
                    break;

            }


            maxLength.setText(String.valueOf(25 - editable.length()));

        }
    }


    private boolean validateSubject() {
        if (editText.getText().toString().trim().isEmpty()) {

            editText_til.setError(getString(R.string.GroupName));


            requestFocus(editText);
            return false;
        } else {
            editText_til.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private void requestUpdateGroupSubjectApi(final String groupSubject) {

        Bundle extras = intent.getExtras();


        final String groupId = extras.getString("groupId");


        final String groupMembersDocId = extras.getString("groupMembersDocId");


        final String groupMessagesDocId = extras.getString("groupMessagesDocId");


        pDialog = new ProgressDialog(EditGroupSubject.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.SavingSubject));
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(EditGroupSubject.this, R.color.color_black),
                PorterDuff.Mode.SRC_IN);


        JSONObject obj = new JSONObject();


        try {


            obj.put("subject", groupSubject);


            obj.put("chatId", groupId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                ApiOnServer.CREATE_GROUP, obj, new com.android.volley.Response.Listener<JSONObject>() {

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

                            informAllMembersOfGroupSubject(groupSubject, groupId, groupMembersDocId, groupMessagesDocId);
                            break;

                        }


                        default: {

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
                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                        }

                    }

//  hideProgressDialog();
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
                                            requestUpdateGroupSubjectApi(groupSubject);
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
                } else if (pDialog != null && pDialog.isShowing()) {

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

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.No_Internet_Connection_Available), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lang", Constants.LANGUAGE);


                headers.put("X-HTTP-Method-Override", "PATCH");


                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "updateGroupInfoApi");
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private void informAllMembersOfGroupSubject(String groupSubject, String groupId, String groupMembersDocId, String groupMessagesDocId) {




        /*
         * Intentionally fetching list of members from db again
         */
        String userId = AppController.getInstance().getUserId();
        String tsInGmt = Utilities.tsInGmt();

        String messageId = String.valueOf(Utilities.getGmtEpoch());
        JSONObject obj = new JSONObject();
        try {
            obj.put("initiatorId", userId);

            obj.put("initiatorIdentifier", AppController.getInstance().getUserIdentifier());
            obj.put("type", 4);
            obj.put("groupId", groupId);

            obj.put("groupSubject", groupSubject);
            obj.put("id", messageId);
            obj.put("timestamp", tsInGmt);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();

        map.put("messageType", "98");


        map.put("isSelf", true);
        map.put("from", groupId);
        map.put("Ts", tsInGmt);
        map.put("id", messageId);

        map.put("type", 4);
        map.put("deliveryStatus", "0");


        map.put("groupSubject", groupSubject);


        map.put("initiatorId", userId);
        map.put("initiatorIdentifier", AppController.getInstance().getUserIdentifier());

        AppController.getInstance().getDbController().addNewChatMessageAndSort(groupMessagesDocId, map, tsInGmt, "");

        ArrayList<Map<String, Object>> groupMembers = AppController.getInstance().getDbController().fetchGroupMember(groupMembersDocId);

        if (AppController.getInstance().canPublish()) {
            AppController.getInstance().getDbController().updateGroupSubject(groupMessagesDocId, groupSubject);
            for (int i = 0; i < groupMembers.size(); i++) {

                String memberId = (String) groupMembers.get(i).get("memberId");


                if (memberId.equals(userId)) {


                    try {
                        JSONObject obj2 = new JSONObject(obj.toString());
                        obj2.put("self", true);
                        obj2.put("timestamp", tsInGmt);

                        obj2.put("groupSubject", groupSubject);
                        obj2.put("id", messageId);
                        obj2.put("message", getString(R.string.You) + " " + getString(R.string.UpdatedGroupSubject, groupSubject));

                        obj2.put("eventName", MqttEvents.GroupChats.value + "/" + userId);
                        bus.post(obj2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    AppController.getInstance().publish(MqttEvents.GroupChats.value + "/" + memberId,
                            obj, 1, false);
                }

            }


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


            supportFinishAfterTransition();

        } else {
            if (root != null) {


                Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }


    }
}
