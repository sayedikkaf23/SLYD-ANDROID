package chat.hola.com.app.qr_code_scanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.ezcall.android.R;
import com.google.gson.Gson;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.transfer_to_friend.TransferToFriendActivity;
import chat.hola.com.app.ui.qrcode.WalletQrCodeActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class QRCodeActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private Unbinder unbinder;
    private ProgressDialog pDialog;
    private CodeScannerView scannerView;
    private SessionApiCall sessionApiCall = new SessionApiCall();
    private String from;
    private SessionManager sessionManager;
    private Data user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        unbinder = ButterKnife.bind(this);

        from = getIntent().getStringExtra("from");
        sessionManager = new SessionManager(this);
        pDialog = new ProgressDialog(this, 0);
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setCancelable(true);

        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                QRCodeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (from != null && from.equals("wallet")) {
                            fetchUserInfo(result.getText());

//                            setResult(Constants.SCAN_QR_CODE, new Intent().putExtra("userId"));
                        } else {
                            sendRequest(result.getText());
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());

        RelativeLayout rlMyQrCode = findViewById(R.id.rlMyQrCode);
        rlMyQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from != null && from.equals("wallet")) {
                    startActivity(new Intent(QRCodeActivity.this, WalletQrCodeActivity.class));
                } else {
                    QRCodeActivity.super.onBackPressed();
                }
            }
        });
    }


    private void fetchUserInfo(String userId) {
        pDialog.show();
        mCodeScanner.releaseResources();
        mCodeScanner.startPreview();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ApiOnServer.OTHER_USER_PROFILE + "?memberId=" + userId, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("message").equals("success")) {
                        Log.i("User", response.toString());
                        Profile profile = new Gson().fromJson(response.toString(), Profile.class);
                        user = profile.getData().get(0);
                        if (!user.getId().equals(AppController.getInstance().getUserId())) {
                            startActivity(new Intent(QRCodeActivity.this, TransferToFriendActivity.class).putExtra(Constants.USER, user));
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QRCodeActivity.this, "Oops something went wrong.", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
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
                                            sendRequest(userId);
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "addFriendByQRCode");
    }

//    private void checkWalletAvailability() {
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ApiOnServer.WALLET_CHECK + "?userId=" + user.getId() + "&userName=" + user.getUserName() + "&userType=" + Constants.APP_TYPE, null, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    if (response.getString("message").toLowerCase().replace(".", "").trim().equals("success")) {
//                        startActivity(new Intent(QRCodeActivity.this, RechargeActivity.class)
//                                .putExtra("currencySymbol", sessionManager.getCurrencySymbol())
//                                .putExtra("userId", user.getId())
//                                .putExtra("user", user));
//                        finish();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                pDialog.dismiss();
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pDialog.dismiss();
//                if (error == null || error.networkResponse == null) {
//                    return;
//                }
//
//                String body;
//                //get status code here
//                final String statusCode = String.valueOf(error.networkResponse.statusCode);
//                try {
//                    body = new String(error.networkResponse.data, "UTF-8");
//                    JSONObject object = new JSONObject(body);
//                    Toast.makeText(QRCodeActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
//                } catch (UnsupportedEncodingException | JSONException e) {
//                    // exception
//                }
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("authorization", AppController.getInstance().getApiToken());
//                headers.put("lan", Constants.LANGUAGE);
//
//                return headers;
//            }
//        };
//
//
//        /* Add the request to the RequestQueue.*/
//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        AppController.getInstance().addToRequestQueue(jsonObjReq, "checkWalletAvailibility");
//    }

    private void sendRequest(String userId) {
        pDialog.show();
        mCodeScanner.releaseResources();
        mCodeScanner.startPreview();

        JSONObject obj = new JSONObject();
        try {
            obj.put("targetUserId", userId);
            obj.put("message", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ApiOnServer.FRIEND_API, obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    if (response.getString("message").equals("success")) {
                        String message = response.getString("messageToShow");
                        Toast.makeText(QRCodeActivity.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(QRCodeActivity.this, ProfileActivity.class).putExtra("userId", userId));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QRCodeActivity.this, "Request already sent...", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
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
                                            sendRequest(userId);
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
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "addFriendByQRCode");
    }

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

}
