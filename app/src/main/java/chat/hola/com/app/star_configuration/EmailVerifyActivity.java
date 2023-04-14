package chat.hola.com.app.star_configuration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
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
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

public class EmailVerifyActivity extends AppCompatActivity {

  @BindView(R.id.eT_email)
  EditText eT_email;
  @BindView(R.id.switchEmail)
  SwitchCompat switchEmail;
  @BindView(R.id.root)
  RelativeLayout root;
  @BindView(R.id.rL_next)
  RelativeLayout rL_next;
  @Inject
  HowdooService service;

  Unbinder unbinder;
  String email = "";
  boolean isVisible = false;

  private Data profileData;
  private Data.BusinessProfile businessProfile;
  private SessionApiCall sessionApiCall = new SessionApiCall();
  String call;
  private SessionManager sessionManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_email_verify);
    sessionManager = new SessionManager(this);
    unbinder = ButterKnife.bind(this);

    profileData = (Data) getIntent().getSerializableExtra("profileData");
    businessProfile = (Data.BusinessProfile) getIntent().getSerializableExtra("business");
    call = getIntent().getStringExtra("call");

    if (call != null && call.equals(Constants.BUSINESS) && businessProfile != null) {
      rL_next.setSelected(true);
      email = businessProfile.getEmail().getId();
      isVisible = businessProfile.getEmail().getVisible() == 1;

      eT_email.setText(email);
      switchEmail.setChecked(isVisible);

      eT_email.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          rL_next.setSelected(email.equalsIgnoreCase(eT_email.getText().toString().trim()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
      });

      switchEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          rL_next.setSelected(isVisible == isChecked);
        }
      });
    } else if (profileData != null
        && profileData.getStarRequest() != null
        && profileData.getStarRequest().getStarUserEmail() != null) {
      eT_email.setText(profileData.getStarRequest().getStarUserEmail());
      isVisible = profileData.getStarRequest().isEmailVisible();
      switchEmail.setChecked(isVisible);
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

  @OnClick(R.id.rL_next)
  public void verifyEmail() {
    if (!rL_next.isSelected()) {
      email = eT_email.getText().toString().trim();
      if (CommonClass.validateEmail(email)) {
          if (call.equals(Constants.BUSINESS)) {
              requestOtpBusiness();
          } else {
              makeOtpReq();
          }
      } else {
        Snackbar mSnackBar = Snackbar.make(root, "Invalid Email !", Snackbar.LENGTH_LONG);
        View view = mSnackBar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        mSnackBar.show();
        //Snackbar.make(root,"Invalid Email !",Snackbar.LENGTH_LONG).show();
      }
    }
  }

  private void requestOtpBusiness() {
    //        businessEmailVerification
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
                  Intent intent = new Intent(EmailVerifyActivity.this, VerificationActivity.class);
                  intent.putExtras(getIntent());
                  intent.putExtra("type", 1);    // 1 = email , 2 = phone number
                  intent.putExtra("email", email);
                  intent.putExtra("isEmailVisible", switchEmail.isChecked());
                  intent.putExtra("call", call);
                  startActivityForResult(intent, 551);
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
              TextView txtv =
                  (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == 551) {
      if (data.getBooleanExtra("emailVerified", true)) {
        Intent intent = new Intent();
        intent.putExtras(data);
        intent.putExtra("email", email);
        intent.putExtra("isVisible",switchEmail.isChecked());
        setResult(551, intent);
        onBackPressed();
      }
    }
  }

  /**
   * To request OTP from the server
   */

  @SuppressWarnings("TryWithIdenticalCatches")
  private void makeOtpReq() {
    JSONObject obj = new JSONObject();
    try {
      obj.put("starUserEmailId", email);
      if (call.equals(Constants.STAR)) obj.put("userId", profileData.getId());
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
                        new Intent(EmailVerifyActivity.this, VerificationActivity.class);
                    intent.putExtras(getIntent());
                    intent.putExtra("type", 1);// 1 = email , 2 = phone number
                    intent.putExtra("email", email);
                    intent.putExtra("call", call);
                    intent.putExtra("isEmailVisible", switchEmail.isChecked());
                    startActivityForResult(intent, 551);
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
    jsonObjReq.setRetryPolicy(
        new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    AppController.getInstance().addToRequestQueue(jsonObjReq, "verifyPhoneNumberApiRequest");
  }
}
