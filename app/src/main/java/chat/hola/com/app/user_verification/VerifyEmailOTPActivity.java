package chat.hola.com.app.user_verification;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import chat.hola.com.app.authentication.newpassword.NewPasswordActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;

import chat.hola.com.app.profileScreen.editProfile.EditNameActivity;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileResponse;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class VerifyEmailOTPActivity extends DaggerAppCompatActivity implements View.OnFocusChangeListener, TextWatcher {

  @BindView(R.id.tV_msg)
  TextView tV_msg;
  @BindView(R.id.tV_timer)
  TextView tV_timer;
  @BindView(R.id.et_otp)
  EditText et_otp;
  @BindView(R.id.root)
  RelativeLayout root;
  @BindView(R.id.tV_resend)
  TextView tV_resend;
  @BindView(R.id.tv_number)
  TextView tv_number;

  @BindView(R.id.et_otp1)
  EditText et_otp1;
  @BindView(R.id.et_otp2)
  EditText et_otp2;
  @BindView(R.id.et_otp3)
  EditText et_otp3;
  @BindView(R.id.et_otp4)
  EditText et_otp4;

  Unbinder unbinder;

  private String type;
  String phoneNumber, email, countryCode;
  private CountDownTimer cTimer;
  private boolean isLogin = false;
  private SessionApiCall sessionApiCall = new SessionApiCall();
  private InputMethodManager imm;
  private SessionManager sessionManager;
  @Inject
  HowdooService service;

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verification);
    unbinder = ButterKnife.bind(this);
    sessionManager = new SessionManager(this);
    tV_resend.setVisibility(View.GONE);
    imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

      initViews();

    isLogin = getIntent().getBooleanExtra("isLogin", false);
    email = getIntent().getStringExtra("email");
    type = getIntent().getStringExtra("type");
    type = type != null && !type.isEmpty() ? type : (isLogin ? "2" : "1");
    //isEmailVisible = getIntent().getBooleanExtra("isEmailVisible", false);
    String msg =
        getString(R.string.verification_code_sent_email) + " \"" + email + " \"" + getString(
            R.string.please_enter_code_below);
    tV_msg.setText(msg);

    if (BuildConfig.DEBUG || ApiOnServer.ALLOW_DEFAULT_OTP){
      et_otp.setText(getString(R.string.text_1111));
    }
    showKeyboard();

    cTimer = new CountDownTimer(60000, 1000) {

      public void onTick(long millisUntilFinished) {
        tV_resend.setVisibility(View.GONE);
        long sec = millisUntilFinished / 1000;
        if (sec > 9) {
          tV_timer.setText(getString(R.string.string_296) + getString(R.string.double_inverted_comma) + sec);
        } else {
          tV_timer.setText(getString(R.string.string_297) + getString(R.string.double_inverted_comma) + sec);
        }
      }

      public void onFinish() {
        tV_msg.setText(getString(R.string.resend_otp_msg));
        tV_resend.setVisibility(View.VISIBLE);
        //                    timer.setText(R.string.string_298);
        // resend.setEnabled(true);
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
    super.onDestroy();
  }

  @OnClick(R.id.rL_next)
  public void next() {

    hideKeyBoard();
    //OTP number
    String otpNumber = (et_otp1.getText().toString() + "" + et_otp2.getText().toString() + "" +
            et_otp3.getText().toString() + "" + et_otp4.getText().toString());
    if (!et_otp1.getText().toString().isEmpty() && !et_otp2.getText().toString().isEmpty()
            && !et_otp3.getText().toString().isEmpty() && !et_otp4.getText().toString().isEmpty()) {
      verifyEmail(otpNumber);
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

  @OnClick(R.id.tV_resend)
  public void resend() {
    makeResendOtpReqToEmail();
  }

  /**
   * To request OTP from the server
   */

  @SuppressWarnings("TryWithIdenticalCatches")
  private void makeResendOtpReqToEmail() {

    JSONObject obj = new JSONObject();

    try {
      if (type.equals("3")) obj.put("userId", AppController.getInstance().getUserId());
      obj.put("type", type);
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
                                  makeResendOtpReqToEmail();
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
                headers.put("authorization", Utilities.getThings());
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

  @OnClick(R.id.iV_back)
  public void back() {
    hideKeyBoard();
    onBackPressed();
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

    imageView.setImageDrawable(getDrawable(R.drawable.ic_email_verified));
    textView.setText(getString(R.string.verified_email_msg));

    rL_ok.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
        Intent data = new Intent();
        data.putExtra("emailVerified", true);
        data.putExtra("email", email);
        setResult(RESULT_OK, data);
        onBackPressed();
      }
    });

    dialog.show();
  }

  public void verifyEmail(String code) {


    /*
     * If request was for verifying otp on the server
     */

    JSONObject obj = new JSONObject();

    try {
      obj.put("otp", code);
      obj.put("email", email);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.POST, ApiOnServer.VERIFY_OTP_EMAIL, obj,
            new Response.Listener<JSONObject>() {

              @Override
              public void onResponse(JSONObject response) {

                //openSuccessDialog();
                try {
                  if (response.getString("message").equalsIgnoreCase("success")) {
                    if (isLogin) {
                      changePassword();
                    } else {
                      updateEmailToProfile(email);
                    }
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
            headers.put("authorization", sessionManager.getGuestToken());
            headers.put("lang", Constants.LANGUAGE);

            return headers;
          }
        };

    jsonObjReq.setRetryPolicy(
        new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add the request to the RequestQueue.*/
    AppController.getInstance().addToRequestQueue(jsonObjReq, "verifyOtpApi");
  }

  private void updateEmailToProfile(String email) {
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    service.editProfile(AppController.getInstance().getApiToken(), "en", map)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DisposableObserver<retrofit2.Response<EditProfileResponse>>() {
              @Override
              public void onNext(retrofit2.Response<EditProfileResponse> response) {
                if (response.code() == 200) {
                  openSuccessDialog();
                }
              }

              @Override
              public void onError(Throwable e) {
                e.printStackTrace();
              }

              @Override
              public void onComplete() {

              }
            });
  }

  private void changePassword() {
    hideKeyBoard();
    startActivity(new Intent(this, NewPasswordActivity.class).putExtra("userName", email));
    finish();
  }

  private void showKeyboard() {
    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
      @Override
      public void run() {
        et_otp.requestFocus();
        if (imm != null) {
          imm.showSoftInput(et_otp, InputMethodManager.SHOW_FORCED);
        }
      }
    }, 200);
  }

  private void hideKeyBoard() {
    if (imm != null) {

      if(et_otp!=null)
      imm.hideSoftInputFromWindow(et_otp.getWindowToken(), 0);
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
    if(s==et_otp1.getEditableText())
    {
      if(et_otp1.getText().toString().length()==1)
        et_otp2.requestFocus();
    }
    else if(s==et_otp2.getEditableText())
    {
      if(et_otp2.getText().toString().length()==1)
        et_otp3.requestFocus();
    }

    else if(s==et_otp3.getEditableText())
    {
      if(et_otp3.getText().toString().length()==1)
        et_otp4.requestFocus();
    }
    else if(s==et_otp4.getEditableText())
    {
      if(et_otp4.getText().toString().length()==1){

      }
      //methodToVerifyOTP();
    }
  }

  @Override
  public void onFocusChange(View v, boolean hasFocus) {

  }
}
