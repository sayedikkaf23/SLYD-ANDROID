package chat.hola.com.app.star_configuration;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.business.configuration.BusinessConfigurationActivity;
import chat.hola.com.app.profileScreen.editProfile.EditProfileActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import chat.hola.com.app.profileScreen.editProfile.editDetail.EditDetailActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class VerificationActivity extends AppCompatActivity
        implements View.OnFocusChangeListener, TextWatcher {

    @BindView(R.id.tV_msg)
    TextView tV_msg;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tV_timer)
    TextView tV_timer;
    @BindView(R.id.et_otp)
    EditText et_otp;
    @BindView(R.id.etOtp)
    EditText etOtp;
    @BindView(R.id.btn_verify)
    Button btn_verify;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.tV_resend)
    TextView tV_resend;
    @BindView(R.id.rL_next)
    RelativeLayout rL_next;

    @BindView(R.id.et_otp1)
    EditText et_otp1;
    @BindView(R.id.et_otp2)
    EditText et_otp2;
    @BindView(R.id.et_otp3)
    EditText et_otp3;
    @BindView(R.id.et_otp4)
    EditText et_otp4;

    private String call;
    Unbinder unbinder;
    private SessionApiCall sessionApiCall = new SessionApiCall();
    // 1 = email , 2 = phone number
    int type;
    String phoneNumber, email, countryCode, otpId;
    private CountDownTimer cTimer;
    private boolean isNumberVisible = false;
    private boolean isEmailVisible = false;
    private static Bus bus = AppController.getBus();
    SessionManager sessionManager;
    private String businessUniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        unbinder = ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        bus.register(this);
        Utilities.initSmsRetriever(this);
        call = getIntent().getStringExtra("call");
        type = getIntent().getIntExtra("type", 2);
        tV_resend.setVisibility(View.GONE);
        etOtp.requestFocus();

        initViews();

        if (type == 2) {
            otpId = getIntent().getStringExtra("otpId");
            businessUniqueId = getIntent().getStringExtra("businessUniqueId");
            phoneNumber = getIntent().getStringExtra("phoneNumber");
            countryCode = getIntent().getStringExtra("countryCode");
            isNumberVisible = getIntent().getBooleanExtra("isNumberVisible", false);
            String msg = getString(R.string.verification_code_sent_phone) + " " + getString(R.string.please_enter_code_below);
            tV_msg.setText(msg);
        } else {
            email = getIntent().getStringExtra("email");
            isEmailVisible = getIntent().getBooleanExtra("isEmailVisible", false);
            String msg = getString(R.string.verification_code_sent_email) + " " + getString(
                    R.string.please_enter_code_below);
            tV_msg.setText(msg);
        }

    /*if (BuildConfig.DEBUG)
      et_otp.setText("1111");*/
        //        } else {
        //
        //        }
        cTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                tV_resend.setVisibility(View.GONE);
                long sec = millisUntilFinished / 1000;
                if (sec > 9) {
                    tV_timer.setText(getString(R.string.string_296) + "" + sec);
                } else {
                    tV_timer.setText(getString(R.string.string_297) + "" + sec);
                }
            }

            public void onFinish() {
                tV_msg.setText(R.string.resend_otp_msg);
                tV_resend.setVisibility(View.VISIBLE);
            }
        };
        cTimer.start();
    }

    private void initViews() {
        et_otp1.setOnFocusChangeListener(this);
        et_otp2.setOnFocusChangeListener(this);
        et_otp3.setOnFocusChangeListener(this);
        et_otp4.setOnFocusChangeListener(this);
        et_otp1.addTextChangedListener(this);
        et_otp2.addTextChangedListener(this);
        et_otp3.addTextChangedListener(this);
        et_otp4.addTextChangedListener(this);
        et_otp1.requestFocus();

        et_otp1.addTextChangedListener(this);
        et_otp2.addTextChangedListener(this);
        et_otp3.addTextChangedListener(this);
        et_otp4.addTextChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        cTimer.cancel();
        if (bus != null)
            bus.unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.rL_next,R.id.btn_verify})
    public void next() {
        if (type == 2) {
//            String otpNumber = (et_otp1.getText().toString() + "" + et_otp2.getText().toString() + "" +
//                    et_otp3.getText().toString() + "" + et_otp4.getText().toString());
//            if (!et_otp1.getText().toString().isEmpty() && !et_otp2.getText().toString().isEmpty() && !et_otp3.getText().toString().isEmpty() && !et_otp4.getText().toString().isEmpty()) {
            String otpNumber = etOtp.getText().toString();
            if (!etOtp.getText().toString().isEmpty()) {
                if (call.equals(Constants.BUSINESS)) {
                    verifyBusinessNumber(otpNumber);
                } else if (call.equals(Constants.EDIT_PROFILE)) {
                    verifyNumber(otpNumber);
                } else {
                    verifyPhoneNumber(otpNumber);
                }
            } else {
                Snackbar mSnackBar =
                        Snackbar.make(root, getString(R.string.enter_otp2), Snackbar.LENGTH_LONG);
                View view = mSnackBar.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
                mSnackBar.show();
            }
        } else if (type == 1) {
            String otpNumber = (et_otp1.getText().toString() + "" + et_otp2.getText().toString() + "" +
                    et_otp3.getText().toString() + "" + et_otp4.getText().toString());
            if (!et_otp1.getText().toString().isEmpty() && !et_otp2.getText().toString().isEmpty()
                    && !et_otp3.getText().toString().isEmpty() && !et_otp4.getText().toString().isEmpty()) {
                if (call.equals(Constants.BUSINESS)) {
                    verifyBusinessEmail(otpNumber);
                } else {
                    verifyEmail(otpNumber);
                }
            } else {
                Snackbar mSnackBar =
                        Snackbar.make(root, getString(R.string.enter_otp1), Snackbar.LENGTH_LONG);
                View view = mSnackBar.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
                mSnackBar.show();
            }
        }
    }

    private void verifyNumber(String otp) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("mobile", phoneNumber);
            obj.put("countryCode", countryCode);
            obj.put("otpCode", otp);
            obj.put("otpId", otpId);
            obj.put("type", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.VERIFY_OTP_PHONE, obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(VerificationActivity.this, "Verified successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (root != null) {
                            Snackbar snackbar =
                                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                            Snackbar.LENGTH_SHORT);
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
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "businessPhoneOtpVerify");
    }

    private void verifyBusinessEmail(String code) {
        /*
         * If request was for verifying otp on the server
         */

        JSONObject obj = new JSONObject();

        try {

            obj.put("otp", code);
            obj.put("bussinessEmailId", email);
            obj.put("isVisible", isEmailVisible);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.BUSINESS_EMAIL_OTP_VERIFY, obj,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    if (EditProfileActivity.businessProfile != null) {
                                        EditProfileActivity.businessProfile.getEmail().setVerified(1);
                                    }
                                    if (BusinessConfigurationActivity.businessProfile != null) {
                                        BusinessConfigurationActivity.businessProfile.getEmail().setVerified(1);
                                    }
                                    openSuccessDialog();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                                                    verifyBusinessEmail(code);
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

                            Snackbar snackbar =
                                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                            Snackbar.LENGTH_SHORT);

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

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "verifyOtpApi");
    }

    @OnClick(R.id.tV_resend)
    public void resend() {

        switch (type) {
            case 1:
                if (!call.equals(Constants.BUSINESS)) {
                    makeResendOtpToEmail();
                } else {
                    requestOtpBusiness();
                }
                break;
            case 2:
                requestOTP();
                break;
            default:
                Snackbar mSnackBar =
                        Snackbar.make(root, "Please go back and try again !", Snackbar.LENGTH_LONG);
                View view = mSnackBar.getView();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                params.gravity = Gravity.TOP;
                view.setLayoutParams(params);
                mSnackBar.show();
                break;
        }
    }

    private void requestOTP() {
        switch (call) {
            case Constants.BUSINESS:
                requestOtpBusinessNumber();
                break;
            case Constants.EDIT_PROFILE:
                requestOtpNumber();
                break;
            default:
                makeResendOtpReqToNumber();
                break;
        }
    }

    private void requestOtpNumber() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("countryCode", countryCode);
            obj.put("mobile", phoneNumber);
            obj.put("type", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP_EDIT, obj,
                        (Response.Listener<JSONObject>) response -> {
                            try {
                                if (response.getString("message").equals("success")) {
                                    cTimer.start();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }, (Response.ErrorListener) error -> {
                    if (root != null) {
                        Snackbar snackbar =
                                Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                        Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("authorization", AppController.getInstance().getApiToken());
                        headers.put("lang", Constants.LANGUAGE);
                        return headers;
                    }
                };

        /* Add the request to the RequestQueue.*/
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "phoneVerification");
    }

    private void requestOtpBusinessNumber() {
        //        businessPhoneVerification
        JSONObject obj = new JSONObject();
        try {
            obj.put("countryCode", countryCode);
            obj.put("businessPhone", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP_BUSINESS, obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.getString("message").equals("success")) {
                                        cTimer.start();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (root != null) {
                            Snackbar snackbar =
                                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                            Snackbar.LENGTH_SHORT);
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
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "businessPhoneVerification");
    }

    private void verifyBusinessNumber(String otp) {
        //        businessPhoneOtpVerify
        JSONObject obj = new JSONObject();
        try {
            obj.put("mobile", phoneNumber);
            obj.put("countryCode", countryCode);
            obj.put("otpCode", otp);
            obj.put("otpId", otpId);
            obj.put("type", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.VERIFY_OTP_PHONE_BUSINESS, obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Toast.makeText(VerificationActivity.this, "Verified successfully", Toast.LENGTH_SHORT).show();
                                finish();
//                                try {
//                                    if (EditProfileActivity.businessProfile != null) {
//                                        EditProfileActivity.businessProfile.getPhone().setVerified(1);
//                                    }
//                                    if (BusinessConfigurationActivity.businessProfile != null) {
//                                        BusinessConfigurationActivity.businessProfile.getPhone().setVerified(1);
//                                    }
//                                    openSuccessDialog();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (root != null) {
                            Snackbar snackbar =
                                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                            Snackbar.LENGTH_SHORT);
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
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "businessPhoneOtpVerify");
    }

    private void requestOtpBusiness() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("bussinessEmailId", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP_EMAIL_BUSINES, obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.getString("message").equals("success")) {
                                        cTimer.start();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                                                    requestOtpBusiness();
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
                            Snackbar snackbar =
                                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                            Snackbar.LENGTH_SHORT);
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
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "bussinessEmailId");
    }

    /**
     * To request OTP from the server for email
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void makeResendOtpToEmail() {

        JSONObject obj = new JSONObject();

        try {
            obj.put("starUserEmailId", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP_EMAIL, obj,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                cTimer.start();
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
                                                    makeResendOtpToEmail();
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

                            Snackbar snackbar =
                                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                            Snackbar.LENGTH_SHORT);

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
                        headers.put("authorization", sessionManager.getGuestToken());
                        headers.put("lang", Constants.LANGUAGE);

                        return headers;
                    }
                };

        /* Add the request to the RequestQueue.*/

        jsonObjReq.setRetryPolicy(new

                DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        AppController.getInstance().

                addToRequestQueue(jsonObjReq, "verifyPhoneNumberApiRequest");
    }

    /**
     * To request OTP from the server for phone number
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void makeResendOtpReqToNumber() {

        JSONObject obj = new JSONObject();

        try {

            obj.put("deviceId", AppController.getInstance().getDeviceId());
            obj.put("development", BuildConfig.DEBUG);
            obj.put("hashKey", Utilities.getHashCode(this));
            obj.put("phoneNumber", phoneNumber);
            obj.put("countryCode", countryCode);
            obj.put("type", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP, obj,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                System.out.println("request otp res:" + response.toString());
                                cTimer.start();
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
                                                    makeResendOtpReqToNumber();
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

                            Snackbar snackbar =
                                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                            Snackbar.LENGTH_SHORT);

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

                DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        AppController.getInstance().

                addToRequestQueue(jsonObjReq, "verifyPhoneNumberApiRequest");
    }

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (call.equals(Constants.BUSINESS)) {
            Intent intent = new Intent(this, EditDetailActivity.class);
            intent.putExtra("key", "businessPhone");
            intent.putExtra("value", phoneNumber);
            intent.putExtra("countryCode", countryCode);
            intent.putExtra("verified", false);
            intent.putExtra("businessUniqueId", businessUniqueId);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void openSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verified_star_email_phone_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()
                .setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView textView = dialog.findViewById(R.id.tV_msg);
        RelativeLayout rL_ok = dialog.findViewById(R.id.rL_ok);
        if (type == 1) {
            imageView.setImageDrawable(getDrawable(R.drawable.ic_email_verified));
            textView.setText(getString(R.string.verified_email_msg));
        } else {
            imageView.setImageDrawable(getDrawable(R.drawable.ic_phone_number_verified));
            textView.setText(getString(R.string.verified_phone_num_msg));
        }
        rL_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent data = new Intent();

                if (type == 1) {
                    data.putExtra("isVisible", isEmailVisible ? 1 : 0);
                    data.putExtra("emailVerified", true);
                    setResult(551, data);
                } else {
                    data.putExtra("isVisible", isNumberVisible ? 1 : 0);
                    data.putExtra("numberVerified", true);
                    setResult(552, data);
                }
                onBackPressed();
            }
        });

        dialog.show();
    }

    public void verifyPhoneNumber(String code) {


        /*
         * If request was for verifying otp on the server
         */

        JSONObject obj = new JSONObject();

        try {

            obj.put("otp", Integer.parseInt(code));
            obj.put("numberWithOutCountryCode", phoneNumber);
            obj.put("countryCode", countryCode);
            obj.put("isVisible", isNumberVisible);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ApiOnServer.SUP_API_MAIN_LINK + "verifyStarNumber", obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response.getString("message").equals("success")) {
                                openSuccessDialog();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

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

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "verifyOtpApi");
    }

    public void verifyEmail(String code) {


        /*
         * If request was for verifying otp on the server
         */

        JSONObject obj = new JSONObject();

        try {

            obj.put("otp", code);
            obj.put("starUserEmailId", email);
            obj.put("isVisible", isEmailVisible);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.VERIFY_EMAIL, obj,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.getString("message").equals("success")) {
                                        openSuccessDialog();
                                    } else {

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
                                                    verifyEmail(code);
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

                            Snackbar snackbar =
                                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                            Snackbar.LENGTH_SHORT);

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

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "verifyOtpApi");
    }

    @Subscribe
    public void getMessage(JSONObject obj) {
        try {
            if (obj.getString("eventName").equals("OTP_RECEIVED")) {
                //  et_otp.setText(obj.getString("otp"));

                //splitting and displaying OTP Number into editText
                String otpNumber = obj.getString("otp");
                String splitted[] = otpNumber.split(" ");
                if (splitted[0].length() == 4) {
                    String one = splitted[0].charAt(0) + "";
                    String tw = splitted[0].charAt(1) + "";
                    String thr = splitted[0].charAt(2) + "";
                    String fur = splitted[0].charAt(3) + "";
                    et_otp1.setText(one);
                    et_otp2.setText(tw);
                    et_otp3.setText(thr);
                    et_otp4.setText(fur);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s == et_otp1.getEditableText()) {
            if (et_otp1.getText().toString().length() == 1)
                et_otp2.requestFocus();
        } else if (s == et_otp2.getEditableText()) {
            if (et_otp2.getText().toString().length() == 1)
                et_otp3.requestFocus();
        } else if (s == et_otp3.getEditableText()) {
            if (et_otp3.getText().toString().length() == 1)
                et_otp4.requestFocus();
        } else if (s == et_otp4.getEditableText()) {
            if (et_otp4.getText().toString().length() == 1) {

            }
            //methodToVerifyOTP();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

}
