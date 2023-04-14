package chat.hola.com.app.user_verification;

/**
 * <h1>{@link chat.hola.com.app.user_verification.RequestEmailOTPActivity}</h1>
 * <p>This activity class is used request verification code on email.</p>
 *
 * @author: Hardik Karkar
 * @since : 29th May 2019
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.model.Data;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestEmailOTPActivity extends AppCompatActivity {

    @BindView(R.id.eT_email)
    EditText eT_email;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.tV_title)
    TextView tV_title;
    @BindView(R.id.rL_next)
    RelativeLayout rL_next;

    Unbinder unbinder;
    String email = ""; // holds the user email input.
    private boolean isLogin = false;
    private SessionApiCall sessionApiCall = new SessionApiCall();
    private String type;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_email_otp);
        unbinder = ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        isLogin = getIntent().getBooleanExtra("isLogin", false);
        type = getIntent().getStringExtra("type");

        if (type.equals("3")) {
            tV_title.setText(getString(R.string.update_email));
        }

        type = type != null && !type.isEmpty() ? type : (isLogin ? "2" : "1");

        Data profileData = (Data) getIntent().getSerializableExtra("profileData");
        if (profileData != null) {
            if (profileData.getEmail() != null) eT_email.setText(profileData.getEmail());
        }
    }

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rL_next.setEnabled(true);
    }

    @OnClick(R.id.rL_next)
    public void verifyEmail() {
        email = eT_email.getText().toString().trim();
        if (CommonClass.validateEmail(email)) {
            makeOtpReq();
        } else {
            Snackbar mSnackBar = Snackbar.make(root, "Invalid Email !", Snackbar.LENGTH_LONG);
            View view = mSnackBar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            mSnackBar.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.Verification.EMAIL_VERIFICATION_REQ && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("emailVerified", true)) {
                Intent intent = new Intent();
                intent.putExtras(data);
                intent.putExtra("email", email);
                intent.putExtra("emailVerified", true);
                setResult(RESULT_OK, intent);
                onBackPressed();
            }
        }
    }

    /**
     * <p>To request OTP from the server</p>
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void makeOtpReq() {
        rL_next.setEnabled(false);
        JSONObject obj = new JSONObject();

        try {
            if (type.equals("3")) obj.put("userId", AppController.getInstance().getUserId());
            obj.put("type", type); // 1 for star user, 2 for verify email, 3 edit email verification
            obj.put("starUserEmailId", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP_EMAIL, obj,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    if (response.getString("message").equals("success")) {
                                        Intent intent =
                                                new Intent(RequestEmailOTPActivity.this, VerifyEmailOTPActivity.class);
                                        intent.putExtras(getIntent());
                                        intent.putExtra("email", email);
                                        intent.putExtra("isLogin", isLogin);
                                        intent.putExtra("type", type);
                                        startActivityForResult(intent, Constants.Verification.EMAIL_VERIFICATION_REQ);
                                    }
                                } catch (Exception e) {
                                    rL_next.setEnabled(true);
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        rL_next.setEnabled(true);
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

                            Snackbar snackbar =
                                    Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                                            Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv =
                                    (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            if (error.networkResponse != null && error.networkResponse.statusCode == 409) {
                                txtv.setText(getString(R.string.email_address_already_exists));
                            }
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

                addToRequestQueue(jsonObjReq, "requestEmailApiRequest");
    }
}
