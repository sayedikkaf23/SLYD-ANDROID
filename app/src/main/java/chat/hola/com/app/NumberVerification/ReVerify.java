package chat.hola.com.app.NumberVerification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.PromptDialog;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ReVerify extends AppCompatActivity {
    private SessionApiCall sessionApiCall = new SessionApiCall();
    @BindView(R.id.tvTitle1)
    TextView tvTitle1;
    @BindView(R.id.tvTitle2)
    TextView tvTitle2;
    @BindView(R.id.tvVerifyMobile)
    TextView tvVerifyMobile;
    @BindView(R.id.tvMobileNumber)
    TextView tvMobileNumber;
    @BindView(R.id.tvSMS)
    TextView tvSMS;
    @BindView(R.id.btnResend)
    Button btnResend;
    private ProgressDialog pDialog2;
    private String mobileNumber, countryCode, flag;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverify);
        ButterKnife.bind(this);

        TypefaceManager typefaceManager = new TypefaceManager(this);

        mobileNumber = getIntent().getStringExtra("mobile_number");
        countryCode = getIntent().getStringExtra("country_code");
        flag = getIntent().getStringExtra("flag");

        tvTitle1.setTypeface(typefaceManager.getBoldFont());
        tvTitle2.setTypeface(typefaceManager.getBoldFont());
        tvVerifyMobile.setTypeface(typefaceManager.getRegularFont());
        tvMobileNumber.setTypeface(typefaceManager.getRegularFont());
        tvSMS.setTypeface(typefaceManager.getRegularFont());
        btnResend.setTypeface(typefaceManager.getSemiboldFont());

        tvMobileNumber.setText(countryCode + getString(R.string.double_inverted_comma) + mobileNumber);

        pDialog2 = new ProgressDialog(this, 0);
        pDialog2.setMessage(getString(R.string.string_547));
        pDialog2.setCancelable(true);

        startTimer();
    }

    @OnClick(R.id.ibNext)
    public void next() {
        onBackPressed();
    }

    @OnClick(R.id.btnResend)
    public void resend() {
        startTimer();
    }

    @OnClick(R.id.tvMobileNumber)
    public void mobile_number() {
        startActivity(new Intent(this, VerifyPhoneNumber.class)
                .putExtra("mobile_number", mobileNumber)
                .putExtra("country_code", countryCode)
                .putExtra("flag", flag));
        finish();
    }

    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void startTimer() {
        btnResend.setEnabled(false);
        new CountDownTimer(30000, 1000) {

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                long sec = millisUntilFinished / 1000;
                btnResend.setText(getString(sec > 9 ? R.string.string_296 : R.string.string_297) +
                        getString(R.string.double_inverted_comma) + sec);
            }

            @Override
            public void onFinish() {
                btnResend.setText(getString(R.string.resend_otp));
                btnResend.setEnabled(true);
                setAnimation();
            }
        }.start();
    }

    private void setAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shaking_animation);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {}
//            @Override
//            public void onAnimationEnd(Animation animation) {
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {}
//        });
        btnResend.setAnimation(animation);
        animation.start();
    }

    private void makeOtpReq() {


        JSONObject obj = new JSONObject();


        try {

            obj.put("deviceId", AppController.getInstance().getDeviceId());

            obj.put("phoneNumber", mobileNumber);
            obj.put("countryCode", "+" + countryCode);
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

                hideProgressDialog2();

                try {
                    switch (response.getInt("code")) {
                        case 200:


                            break;


                        case 137:


                            final PromptDialog p2 = new PromptDialog(ReVerify.this);


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
                            final PromptDialog p3 = new PromptDialog(ReVerify.this);


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
                            final PromptDialog p4 = new PromptDialog(ReVerify.this);


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
                            final PromptDialog p5 = new PromptDialog(ReVerify.this);


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

                            Toast.makeText(ReVerify.this, response.getString("message"), Toast.LENGTH_SHORT).show();

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

    /**
     * To show the progress dialog
     */

    private void showProgressDialog2() {
        if (pDialog2 != null && !pDialog2.isShowing()) {
            pDialog2.show();
            ProgressBar bar = (ProgressBar) pDialog2.findViewById(android.R.id.progress);


            bar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(ReVerify.this, R.color.color_black),
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
}
