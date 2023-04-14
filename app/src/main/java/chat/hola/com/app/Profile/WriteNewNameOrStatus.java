package chat.hola.com.app.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;

/**
 * Created by moda on 02/08/17.
 */


/*
 * Activity to let user enter the new status or name which can both contain text and emoticons
 */
public class WriteNewNameOrStatus extends AppCompatActivity {


    private TextView title;

    private EditText editText;

    private TextInputLayout editText_til;


    /**
     * editType 0-name edit
     * <p>
     * 1- stratus edit
     */

    private int editType;

    private Bus bus = AppController.getBus();
    private ImageView selEmoji, selKeybord;


    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_edit_name_status);


        title = (TextView) findViewById(R.id.title);


        selEmoji = (ImageView) findViewById(R.id.emojiButton);
        selKeybord = (ImageView) findViewById(R.id.chat_keyboard_icon);

        editText = (EditText) findViewById(R.id.et);


        final RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
        editText_til = (TextInputLayout) findViewById(R.id.input_layout_name2);
        final EmojiconsPopup popup = new EmojiconsPopup(root, this);
        RelativeLayout save = (RelativeLayout) findViewById(R.id.rl7);
        ImageView close = (ImageView) findViewById(R.id.close);
        setUpActivity(getIntent());


        editText.addTextChangedListener(new MyTextWatcher(editText));



        /*
         * To save the new status or name
         */


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                try {
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                /*
                 * To save the updated details locally
                 */

                if (editText.getText().toString().trim().length() > 4) {
       /*
        * Eventually we may be required to update name or status directly from this page
        */


                    if (editType == 1) {


                        AppController.getInstance().getDbController().addNewStatus(AppController.getInstance().getStatusDocId(), editText.getText().toString().trim());
                    }

                    Intent intent = new Intent();
                    intent.putExtra("updatedValue", editText.getText().toString().trim());


                    setResult(RESULT_OK, intent);


                    supportFinishAfterTransition();


                } else {

                    switch (editType) {
                        case 0:

                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, getString(R.string.name_save), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view2 = snackbar.getView();
                                TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }

                            break;


                        case 1:

                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, getString(R.string.status_save), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view3 = snackbar.getView();
                                TextView txtv = (TextView) view3.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }

                            break;

                    }
                }

            }
        });


        popup.setSizeForSoftKeyboard();


        popup.setOnDismissListener(new PopupWindow.OnDismissListener()

                                   {

                                       @Override
                                       public void onDismiss() {


                                           selKeybord.setVisibility(View.GONE);
                                           selEmoji.setVisibility(View.VISIBLE);

                                       }
                                   }

        );


        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener()

                                                 {

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


        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener()

                                           {

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


        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener()

                                                    {

                                                        @Override
                                                        public void onEmojiconBackspaceClicked(View v) {
                                                            KeyEvent event = new KeyEvent(
                                                                    0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                                                            editText.dispatchKeyEvent(event);
                                                        }
                                                    }

        );


        selEmoji.setOnClickListener(new View.OnClickListener()

                                    {

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


        selKeybord.setOnClickListener(new View.OnClickListener()

                                      {
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


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        /*
         * To set the typeface values
         */
        Typeface tf = AppController.getInstance().getRegularFont();


        title.setTypeface(tf, Typeface.BOLD);

        bus.register(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setUpActivity(intent);
    }


    private void setUpActivity(Intent intent) {

/*
 *    type 0- name change
 *    type 1- status change
 */
        Bundle extras = intent.getExtras();

        editType = extras.getInt("type");
        switch (editType) {
            case 0:

                title.setText(getString(R.string.NewName));


                editText.setHint(getString(R.string.enterUserName));

                break;


            case 1:

                title.setText(getString(R.string.NewStatus));

                editText.setHint(getString(R.string.EnterStatus));

                break;

        }

        String currentValue = extras.getString("currentValue");

        if (currentValue != null && !currentValue.isEmpty()) {

            editText.setText(currentValue);
            editText.setSelection(editText.getText().length());
        }
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
                    validateNameOrStatus();
                    break;

            }
        }
    }


    private boolean validateNameOrStatus() {
        if (editText.getText().toString().trim().isEmpty()) {


            if (editType == 0) {

                /*
                 * Name change
                 */

                editText_til.setError(getString(R.string.name_empty));
            } else {

                /*
                 *Status change
                 */

                editText_til.setError(getString(R.string.status_empty));
            }


            requestFocus(editText);
            return false;
        } else if (editText.getText().toString().trim().length() < 4) {

            if (editType == 0) {
                /*
                 * Name change
                 */

                editText_til.setError(getString(R.string.name_atleast));
            } else {

                 /*
                 *Status change
                 */
                editText_til.setError(getString(R.string.status_atleast));
            }

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


    @Override
    public void onBackPressed() {


        if (AppController.getInstance().isActiveOnACall()) {
            if (AppController.getInstance().isCallMinimized()) {
                Intent intent = new Intent();

                setResult(RESULT_CANCELED, intent);
                supportFinishAfterTransition();
            }
        } else {
            Intent intent = new Intent();

            setResult(RESULT_CANCELED, intent);
            supportFinishAfterTransition();
        }

    }


    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(WriteNewNameOrStatus.this, ChatMessageScreen.class);

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
                JSONException e)

        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

}
