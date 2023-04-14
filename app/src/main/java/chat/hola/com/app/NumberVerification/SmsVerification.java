package chat.hola.com.app.NumberVerification;
/*
 * Created by moda on 15/07/16.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.BuildConfig;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Dialog.PromptDialog;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.account.AccountGeneral;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.notification.UserIdUpdateHandler;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


/* This class verify the number of the user.
 * A SMS reader service is started to automatically detect
 * the SMS received from the server. Else user is prompted
 * to enter the verification code manually.
 *
 *
 */

public class SmsVerification extends AppCompatActivity {

    private SessionApiCall sessionApiCall = new SessionApiCall();
    private Button resend;
    private ImageButton ibBack, ibNext;
    private EditText etOtp;
    private TextView tvError, tvMobileNumber, tvVerification;

    private Bus bus = AppController.getBus();
    private IntentFilter intentFilter;
    private ProgressDialog pDialog2, pDialog;
    private RelativeLayout root;
    private ReadSms readSms;
    private int type;
    private CountDownTimer cTimer;
    private Bundle bundle;
    private TypefaceManager typefaceManager;

    private String flag, county_code, mobile_number;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_verification);
        Utilities.initSmsRetriever(this);
        bundle = getIntent().getExtras();

        flag = getIntent().getStringExtra("flag");
        county_code = getIntent().getStringExtra("countryCode");
        mobile_number = getIntent().getStringExtra("mobileNumber");

        typefaceManager = new TypefaceManager(this);
        root = findViewById(R.id.smsVerification);
        etOtp = findViewById(R.id.etOtp);
        resend = findViewById(R.id.btnResend);
        tvError = findViewById(R.id.tvError);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);
        tvVerification = findViewById(R.id.tvVerification);
        ibBack = findViewById(R.id.ibBack);
        ibNext = findViewById(R.id.ibNext);

        tvMobileNumber.setTypeface(typefaceManager.getBoldFont());
        tvVerification.setTypeface(typefaceManager.getBoldFont());
        etOtp.setTypeface(typefaceManager.getRegularFont());
        tvError.setTypeface(typefaceManager.getRegularFont());
        resend.setTypeface(typefaceManager.getSemiboldFont());

        ibBack.setOnClickListener(v -> onBackPressed());

//        timer = (TextView) findViewById(R.id.timer);
//        timer.setTypeface(fontMedium);

        /*
         *
         *
         * Type determines whether we have choosed to automatically detect the sms or get the sms code manually
         *
         *
         *
         * Type-0
         *
         *
         *
         * Type-1
         *
         * */
        type = bundle.getInt("type");


        if (type == 0) {

            cTimer = new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    long sec = millisUntilFinished / 1000;
//                    if (sec > 9) {
//                        timer.setText(getString(R.string.string_296) + "" + sec);
//                    } else {
//                        timer.setText(getString(R.string.string_297) + "" + sec);
//                    }
                }

                public void onFinish() {
//                    timer.setText(R.string.string_298);
                    resend.setEnabled(true);
                }
            };


            /*
             *
             *
             * Have to show the timer
             *
             * */

//            timer.setVisibility(View.VISIBLE);
            cTimer.start();
            intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
            intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);


            readSms = new ReadSms() {

                @Override
                protected void onSmsReceived(String s) {
                    /* Call the verify SMS code API */
                    // instead of verifying number on the server we will will verify number here itsef
//
//                    if (s.equals(code))
//
//                    {

                    /*load the homescreen in app after sending the signin details to the server*/
                    makeRequest(s);

//                    } else {
//
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                cTimer.cancel();
//                                cTimer.onFinish();
//                                timer.setText(R.string.string_298);
//                                etOtp.setEnabled(false);
//                                etOtp.setetOtpType(etOtpType.TYPE_NULL);
//                                etOtp.setFocusableInTouchMode(false);
//                                etOtp.clearFocus();
//
//                                Snackbar snackbar = Snackbar.make(root, R.string.string_149, Snackbar.LENGTH_SHORT);
//
//
//                                snackbar.show();
//                                View view = snackbar.getView();
//                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//
//                            }
//                        });
//
//
//                    }


                }
            };


        } else {


//            timer.setVisibility(View.GONE);

            tvError.setText(getString(R.string.string_295));

        }


//          /* Initialise the progress dialog */
//


        pDialog = new ProgressDialog(this, 0);
        pDialog.setMessage(getString(R.string.Verify_Otp));
        pDialog.setCancelable(true);

        pDialog2 = new ProgressDialog(this, 0);
        pDialog2.setMessage(getString(R.string.string_547));
        pDialog2.setCancelable(true);


//        editNumber.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                /* Move back to verify phone number screen */
//                Intent intent = new Intent(SmsVerification.this,
//                        VerifyPhoneNumber.class);
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(SmsVerification.this).toBundle());
//                supportFinishAfterTransition();
//            }
//        });


        /*
         *
         *
         * resend button have to be initially hidden but once the autodetect time of SMS expires then it has to be made visible again
         *
         * */

        resend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                etOtp.setText(getString(R.string.double_inverted_comma));

                startActivity(new Intent(SmsVerification.this, ReVerify.class).putExtra("flag", flag).putExtra("country_code", county_code).putExtra("mobile_number", mobile_number));

                //requestOtpAgain();

            }
        });


        etOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etOtp.getText().toString().trim().length() == 4) {
//                    etOtpMethodManager imm = (etOtpMethodManager) getSystemService(Activity.etOtp_METHOD_SERVICE);
//                    imm.toggleSoftetOtp(etOtpMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    //   makeRequest(etOtp.getText().toString().trim());
                }
            }


            /**
             *
             * assuming always length 4 otp comes
             *
             *
             * */
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bus.register(this);

        //TODO - remove when you will implement sms OTP
        if (BuildConfig.DEBUG || ApiOnServer.ALLOW_DEFAULT_OTP)
            etOtp.setText(getString(R.string.text_1111));
//        if (etOtp.getText().toString().length() == 4)

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest(etOtp.getText().toString().trim());
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (type == 0)

            this.registerReceiver(readSms, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (type == 0)
            (this).unregisterReceiver(readSms);
    }


    @SuppressWarnings("unchecked")

    public void makeRequest(String code) {


        /*
         * If request was for verifying otp on the server
         */


        JSONObject obj = new JSONObject();


        try {

            obj.put("otp", code);


            // obj.put("phoneNumber", bundle.getString("phoneNumber"));
            obj.put("deviceId", AppController.getInstance().getDeviceId());

            obj.put("phoneNumber", bundle.getString("mobileNumber"));
            obj.put("countryCode", bundle.getString("countryCode"));
            obj.put("deviceName", Build.DEVICE);
            obj.put("deviceOs", Build.VERSION.RELEASE);
            obj.put("modelNumber", Build.MODEL);
            obj.put("deviceType", "2");
            try {
                PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                String version = pInfo.versionName;
                obj.put("appVersion", version);
            } catch (PackageManager.NameNotFoundException e) {
                obj.put("appVersion", "123");
            }

            //  obj.put("username", "davidwarner");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showProgressDialog();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ApiOnServer.VERIFY_OTP, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {

                    switch (response.getInt("code")) {

                        case 403:
                            BlockDialog blockDialog = new BlockDialog(SmsVerification.this);
                            blockDialog.show();
                            break;

                        case 200:

                            /*
                             *
                             * To start the Profile screen
                             */


                            response = response.getJSONObject("response");

                            String profilePic = "", userName = "", firstName = "", lastName = "";
                            // String token = response.getString("token");


                            CouchDbController db = AppController.getInstance().getDbController();

                            Map<String, Object> map = new HashMap<>();


                            if (response.has("profilePic")) {
                                profilePic = response.getString("profilePic");
                                map.put("userImageUrl", response.getString("profilePic"));

                            } else {
                                map.put("userImageUrl", "");
                            }
                            if (response.has("userName")) {
                                userName = response.getString("userName");
                                map.put("userName", response.getString("userName"));
                            }

                            if (response.has("firstName")) {
                                firstName = response.getString("firstName");
                                map.put("firstName", response.getString("firstName"));
                            }

                            if (response.has("lastName")) {
                                lastName = response.getString("lastName");
                                map.put("lastName", response.getString("lastName"));
                            }

                            String userStatus = getString(R.string.default_status);


                            map.put("userId", response.getString("userId"));

                            try {
                                map.put("private", response.getInt("private"));
                            } catch (JSONException e) {

                            } catch (Exception ignored) {
                            }


                            /*
                             * To save the social status as the text value in the properties
                             *
                             */

                            if (response.has("socialStatus")) {

                                userStatus = response.getString("socialStatus");

                            }
                            map.put("socialStatus", userStatus);
                            map.put("userIdentifier", bundle.getString("phoneNumber"));
                            map.put("apiToken", response.getString("token"));

                            try {
                                Account mAccount = new Account(bundle.getString("phoneNumber"), AccountGeneral.ACCOUNT_TYPE);
                                AccountManager mAccountManager = AccountManager.get(getApplicationContext());
                                mAccountManager.addAccountExplicitly(mAccount, etOtp.getText().toString(), null);

                                Account[] accounts = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
                                if (accounts.length != 0)
                                    mAccountManager.setAuthToken(mAccount, AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY, response.getString("token"));
                            } catch (Exception e) {
                                Log.e("", "" + e.toString());
                            }
                            AppController.getInstance().getSharedPreferences().edit().putString("token", response.getString("token")).apply();

                            /*
                             * By phone number verification
                             */

                            map.put("userLoginType", 1);
                            map.put("excludedFilterIds", new ArrayList<Integer>());
                            if (!db.checkUserDocExists(AppController.getInstance().getIndexDocId(), response.getString("userId"))) {


                                String userDocId = db.createUserInformationDocument(map);

                                db.addToIndexDocument(AppController.getInstance().getIndexDocId(), response.getString("userId"), userDocId);


                            } else {


                                db.updateUserDetails(db.getUserDocId(response.getString("userId"), AppController.getInstance().getIndexDocId()), map);

                            }
                            if (!userName.isEmpty()) {

                                db.updateIndexDocumentOnSignIn(AppController.getInstance().getIndexDocId(), response.getString("userId"), 1, true);
                            } else {
                                db.updateIndexDocumentOnSignIn(AppController.getInstance().getIndexDocId(), response.getString("userId"), 1, false);
                            }
                            /*
                             * To update myself as available for call
                             */

                            AppController.getInstance()
                                .setSignedIn(true, response.getString("userId"), userName,
                                    bundle.getString("phoneNumber"),profilePic, 1, null, null, null, null, null,
                                    null,null);
                            AppController.getInstance().setSignStatusChanged(true);

                            String topic = "/topics/" + response.getString("userId");
                            FirebaseMessaging.getInstance().subscribeToTopic(topic);
                            OneSignal.setExternalUserId(response.getString("oneSignalId"), new UserIdUpdateHandler());

//                            if (userName.isEmpty()) {
//
//                                Intent i = new Intent(SmsVerification.this, SaveProfile.class);
//                                //   i.putExtra("userId", response.getString("userId"));
//                                if (!profilePic.isEmpty())
//                                    i.putExtra("profilePic", response.getString("profilePic"));
//                                if (!userName.isEmpty())
//                                    i.putExtra("userName", response.getString("userName"));
//                                if (!firstName.isEmpty())
//                                    i.putExtra("firstName", response.getString("firstName"));
//                                if (!lastName.isEmpty())
//                                    i.putExtra("lastName", response.getString("lastName"));
//
//                                try {
//                                    i.putExtra("private", response.getInt("private"));
//                                } catch (JSONException e) {
//                                    Log.i("", "onResponse: " + e.getMessage());
//                                } catch (Exception ignored) {
//                                }
////                            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                startActivity(i);
//                            } else {
                            SessionManager sessionManager = new SessionManager(SmsVerification.this);
                            sessionManager.setUserName(userName);
                            sessionManager.setFirstName(firstName);
                            sessionManager.setLastsName(lastName);
//                            sessionManager.setFacebookAccessToken(AccessToken.getCurrentAccessToken());
                            sessionManager.setUserProfilePic(profilePic,true);

                            Intent i = new Intent(SmsVerification.this, DiscoverActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("caller", "SaveProfile");
                            startActivity(i);
                            finish();
//                            supportFinishAfterTransition();
                            break;


                        case 138:


                            /*
                             *
                             *
                             * If code is incorrect then we have to unlock option for resending of the code
                             *
                             * */
                            // resend.setVisibility(View.VISIBLE);
                            resend.setEnabled(true);
//                            timer.setVisibility(View.GONE);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

//                                    etOtp.setEnabled(false);
//                                    etOtp.setetOtpType(etOtpType.TYPE_NULL);
//                                    etOtp.setFocusableInTouchMode(false);
//                                    etOtp.clearFocus();
                                    Snackbar snackbar = Snackbar.make(root, R.string.string_149, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                                }
                            });

                            break;
                        default:

                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                            showErrorDialog(getString(R.string.string_550));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception ignored) {

                }

                hideProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
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
                                            makeRequest(code);
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
                    TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "verifyOtpApi");


    }


    /*
     *
     * Error dialog in case failed to send the OTP
     * */
    @SuppressWarnings("TryWithIdenticalCatches")
    public void showErrorDialog2() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SmsVerification.this);
        alertDialog.setTitle(R.string.string_356);
        alertDialog.setMessage(getString(R.string.string_549));

        alertDialog.setNegativeButton(R.string.string_580,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                        if (context instanceof Activity) {


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                    dialog.dismiss();
                                }
                            } else {


                                if (!((Activity) context).isFinishing()) {
                                    dialog.dismiss();
                                }
                            }
                        } else {


                            try {
                                dialog.dismiss();
                            } catch (final IllegalArgumentException e) {
                                e.printStackTrace();

                            } catch (final Exception e) {
                                e.printStackTrace();

                            }
                        }


                    }
                });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        });
    }


    /**
     * To show the progress dialog
     */

    private void showProgressDialog2() {
        if (pDialog2 != null && !pDialog2.isShowing()) {
            pDialog2.show();
            ProgressBar bar = (ProgressBar) pDialog2.findViewById(android.R.id.progress);


            bar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(SmsVerification.this, R.color.color_black),
                    android.graphics.PorterDuff.Mode.SRC_IN);

        }
    }


    /**
     * To hide the progress dialog
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void hideProgressDialog2() {
        if (pDialog2.isShowing()) {

            Context context = ((ContextWrapper) (pDialog2).getContext()).getBaseContext();


            if (context instanceof Activity) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        pDialog2.dismiss();
                    }
                } else {


                    if (!((Activity) context).isFinishing()) {
                        pDialog2.dismiss();
                    }
                }
            } else {


                try {
                    pDialog2.dismiss();
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();

                } catch (final Exception e) {
                    e.printStackTrace();

                }
            }


        }
    }


    /**
     * To request resend OTP incase wrong OTP was entered by the user or the the 60 seconds expired before the OTP was received/entered by the user
     */
    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void requestOtpAgain() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SmsVerification.this);
        alertDialog.setTitle(R.string.string_356);


        //alertDialog.setMessage("Verification Code Will Be Sent To :\n\n" + mobile_number.getText() + "\n\nIs This OK, Or Would You Like To Edit The Number?");


        //alertDialog.setMessage("Number verification code will be sent to phone number " + mobile_number.getText().toString() + " via SMS");


        alertDialog.setMessage(getString(R.string.string_552));
        alertDialog.setNegativeButton(R.string.string_594,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                        if (context instanceof Activity) {


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                    dialog.dismiss();
                                }
                            } else {


                                if (!((Activity) context).isFinishing()) {
                                    dialog.dismiss();
                                }
                            }
                        } else {


                            try {
                                dialog.dismiss();
                            } catch (final IllegalArgumentException e) {
                                e.printStackTrace();

                            } catch (final Exception e) {
                                e.printStackTrace();

                            }
                        }


                        Intent intent = new Intent(SmsVerification.this,
                                VerifyPhoneNumber.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(SmsVerification.this).toBundle());
                        supportFinishAfterTransition();


                    }
                });

        alertDialog.setPositiveButton(R.string.string_578,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        resend.setEnabled(false);
                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                        if (context instanceof Activity) {


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                    dialog.dismiss();
                                }
                            } else {


                                if (!((Activity) context).isFinishing()) {
                                    dialog.dismiss();
                                }
                            }
                        } else {


                            try {
                                dialog.dismiss();
                            } catch (final IllegalArgumentException e) {
                                e.printStackTrace();

                            } catch (final Exception e) {
                                e.printStackTrace();

                            }
                        }


                        /*make a request to serve r to send for otp*/
                        makeOtpReq();


                    }
                });


        alertDialog.show();


    }


    /**
     * To request OTP from the server
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void makeOtpReq() {


        JSONObject obj = new JSONObject();


        try {

            obj.put("deviceId", AppController.getInstance().getDeviceId());
            obj.put("phoneNumber", bundle.getString("mobileNumber"));
            obj.put("countryCode", "+" + bundle.getString("countryCode"));
            obj.put("development", BuildConfig.DEBUG);
            obj.put("hashKey", Utilities.getHashCode(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        showProgressDialog2();


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ApiOnServer.REQUEST_OTP, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                resend.setVisibility(View.GONE);


                hideProgressDialog2();


                try {
                    switch (response.getInt("code")) {
                        case 200:
                            etOtp.setEnabled(true);
                            etOtp.setFocusableInTouchMode(true);
                            etOtp.requestFocus();


                            if (type == 0) {




                                /*
                                 *
                                 * To automatically detect the SMS,have to start the timer again
                                 *
                                 *
                                 * */


//                                timer.setVisibility(View.VISIBLE);


                                cTimer.start();


                                resend.setVisibility(View.GONE);


                            } else {

                                /*
                                 *
                                 *
                                 * To detect the SMS manually
                                 *
                                 * */

//                                timer.setVisibility(View.GONE);
                                resend.setVisibility(View.GONE);

                            }


                            break;


                        case 137:


                            final PromptDialog p2 = new PromptDialog(SmsVerification.this);


                            p2.setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                                    .setTitleText(R.string.string_354).setContentText(R.string.string_970)
                                    .setPositiveListener(R.string.string_580, new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {


                                            Context context = ((ContextWrapper) (dialog).getContext()).getBaseContext();


                                            if (context instanceof Activity) {


                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                        dialog.dismiss();
                                                    }
                                                } else {


                                                    if (!((Activity) context).isFinishing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            } else {


                                                try {
                                                    dialog.dismiss();
                                                } catch (final IllegalArgumentException e) {
                                                    e.printStackTrace();

                                                } catch (final Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }


                                        }
                                    }).show();


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (p2.isShowing()) {
                                        //   p.dismiss();

                                        Context context = ((ContextWrapper) (p2).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    p2.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    p2.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                p2.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }


                                }
                            }, 2000);


                            break;


                        case 138:

                            //same device 6 times succesfull
                            final PromptDialog p3 = new PromptDialog(SmsVerification.this);


                            p3.setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                                    .setTitleText(R.string.string_354).setContentText(R.string.string_971)
                                    .setPositiveListener(R.string.string_580, new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {


                                            Context context = ((ContextWrapper) (dialog).getContext()).getBaseContext();


                                            if (context instanceof Activity) {


                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                        dialog.dismiss();
                                                    }
                                                } else {


                                                    if (!((Activity) context).isFinishing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            } else {


                                                try {
                                                    dialog.dismiss();
                                                } catch (final IllegalArgumentException e) {
                                                    e.printStackTrace();

                                                } catch (final Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }


                                        }
                                    }).show();


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (p3.isShowing()) {
                                        //   p.dismiss();

                                        Context context = ((ContextWrapper) (p3).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    p3.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    p3.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                p3.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }


                                }
                            }, 2000);

                            break;


                        case 139:
                            //same device 3 failed request for the week
                            final PromptDialog p4 = new PromptDialog(SmsVerification.this);


                            p4.setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                                    .setTitleText(R.string.string_354).setContentText(R.string.string_972)
                                    .setPositiveListener(R.string.string_580, new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {


                                            Context context = ((ContextWrapper) (dialog).getContext()).getBaseContext();


                                            if (context instanceof Activity) {


                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                        dialog.dismiss();
                                                    }
                                                } else {


                                                    if (!((Activity) context).isFinishing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            } else {


                                                try {
                                                    dialog.dismiss();
                                                } catch (final IllegalArgumentException e) {
                                                    e.printStackTrace();

                                                } catch (final Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }


                                        }
                                    }).show();


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (p4.isShowing()) {
                                        //   p.dismiss();

                                        Context context = ((ContextWrapper) (p4).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    p4.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    p4.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                p4.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }


                                }
                            }, 2000);

                            break;


                        case 140:
                            //abuse of device
                            final PromptDialog p5 = new PromptDialog(SmsVerification.this);


                            p5.setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                                    .setTitleText(R.string.string_354).setContentText(R.string.string_973)
                                    .setPositiveListener(R.string.string_580, new PromptDialog.OnPositiveListener() {
                                        @Override
                                        public void onClick(PromptDialog dialog) {


                                            Context context = ((ContextWrapper) (dialog).getContext()).getBaseContext();


                                            if (context instanceof Activity) {


                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                        dialog.dismiss();
                                                    }
                                                } else {


                                                    if (!((Activity) context).isFinishing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            } else {


                                                try {
                                                    dialog.dismiss();
                                                } catch (final IllegalArgumentException e) {
                                                    e.printStackTrace();

                                                } catch (final Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }


                                        }
                                    }).show();


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    if (p5.isShowing()) {
                                        //   p.dismiss();

                                        Context context = ((ContextWrapper) (p5).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    p5.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    p5.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                p5.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }


                                }
                            }, 2000);
                            break;
                        default:


                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                showErrorDialog2();
                            }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

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
                                            makeOtpReq();
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
                    TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

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
        /* Add the request to the RequestQueue.*/


        jsonObjReq.setRetryPolicy(new

                DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        AppController.getInstance().

                addToRequestQueue(jsonObjReq, "verifyPhoneNumberApiRequest");

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
     * Error dialog incase failed to register on server
     */
    @SuppressWarnings("TryWithIdenticalCatches")

    public void showErrorDialog(String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                SmsVerification.this);
        alertDialog.setTitle(R.string.string_406);
        alertDialog.setMessage(message);

        alertDialog.setNegativeButton(R.string.string_580,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  dialog.dismiss();


                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                        if (context instanceof Activity) {


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                    dialog.dismiss();
                                }
                            } else {


                                if (!((Activity) context).isFinishing()) {
                                    dialog.dismiss();
                                }
                            }
                        } else {


                            try {
                                dialog.dismiss();
                            } catch (final IllegalArgumentException e) {
                                e.printStackTrace();

                            } catch (final Exception e) {
                                e.printStackTrace();

                            }
                        }


                    }
                });
        alertDialog.show();
    }


    /**
     * To show the progres dialog
     */

    private void showProgressDialog() {
        if (pDialog != null && !pDialog.isShowing()) {
            pDialog.show();
            ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);

            bar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(SmsVerification.this, R.color.color_black),
                    android.graphics.PorterDuff.Mode.SRC_IN);

        }
    }


    /**
     * To hide the progress dialog
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void hideProgressDialog() {
        if (pDialog.isShowing()) {
            //    pDialog.dismiss();

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
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            }else  if(object.getString("eventName").equals("OTP_RECEIVED")){
                etOtp.setText(object.getString("otp"));
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(SmsVerification.this, ChatMessageScreen.class);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }
}


