package chat.hola.com.app.star_configuration;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.NumberVerification.ChooseCountry;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.model.Data;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static chat.hola.com.app.Utilities.CountryCodeUtil.readEncodedJsonString;

public class NumberVerifyActivity extends AppCompatActivity {

  @BindView(R.id.ivFlag)
  ImageView ivFlag;
  @BindView(R.id.tvCountryCode)
  TextView tvCountryCode;
  @BindView(R.id.rlCountryCode)
  RelativeLayout rlCountryCode;
  @BindView(R.id.eT_number)
  EditText eT_number;
  @BindView(R.id.root)
  RelativeLayout root;
  @BindView(R.id.switchNumber)
  SwitchCompat switchNumber;
  @BindView(R.id.rL_next)
  RelativeLayout rL_next;

  private Unbinder unbinder;
  private AlertDialog.Builder builder;
  private String mobileNumber;
  private String countryCode;
  private String flag;
  private Data profileData;
  private boolean isVisible;
  private SessionApiCall sessionApiCall = new SessionApiCall();

  private String call;
  private Data.BusinessProfile businessProfile;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_number_verify);
    unbinder = ButterKnife.bind(this);
    call = getIntent().getStringExtra("call");
    businessProfile = (Data.BusinessProfile) getIntent().getSerializableExtra("business");
    profileData = (Data) getIntent().getSerializableExtra("profileData");

    if (call != null && call.equals(Constants.BUSINESS) && businessProfile != null) {
      rL_next.setSelected(true);

      countryCode = businessProfile.getPhone().getCountryCode();
      mobileNumber = businessProfile.getPhone().getNumber();
      isVisible = businessProfile.getPhone().getVisible() == 1;

      eT_number.setText(mobileNumber);
      switchNumber.setChecked(isVisible);

      eT_number.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          rL_next.setSelected(mobileNumber.equalsIgnoreCase(eT_number.getText().toString().trim()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
      });

      switchNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
          rL_next.setSelected(isVisible == isChecked);
        }
      });
    } else if (profileData != null
        && profileData.getStarRequest() != null
        && profileData.getStarRequest().getStarUserPhoneNumber() != null) {
      eT_number.setText(profileData.getStarRequest().getPhoneNumber());
      isVisible = profileData.getStarRequest().isNumberVisible();
      switchNumber.setChecked(isVisible);
    }

    loadCurrentCountryCode();
    eT_number.requestFocus();
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

  @OnClick({ R.id.rlCountryCode, R.id.tvCountryCode, R.id.ivFlag })
  public void countryCodePicker() {
    Intent intent = new Intent(NumberVerifyActivity.this, ChooseCountry.class);
    startActivityForResult(intent, 0);
  }

  @OnClick(R.id.rL_next)
  public void next() {
    if (!rL_next.isSelected()) {
      try {
        /* Hide the keyboard here */
        final InputMethodManager inputMethodManager =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
      /* Run the code to verify the number from the server */
      //   showAlertDialog();
      verify();
    }
  }

  private void loadCurrentCountryCode() {
    String locale;
    TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    String countryCodeValue = tm.getNetworkCountryIso();
    if (countryCodeValue == null || countryCodeValue.isEmpty()) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        locale = getResources().getConfiguration().getLocales().get(0).getCountry().toUpperCase();
      } else {
        locale = getResources().getConfiguration().locale.getCountry().toUpperCase();
      }
    } else {
      locale = countryCodeValue.toUpperCase();
    }
    Log.w("country_code>", locale);

    try {
      String allCountriesCode = readEncodedJsonString(NumberVerifyActivity.this);
      JSONArray countryArray = new JSONArray(allCountriesCode);
      for (int i = 0; i < countryArray.length(); i++) {
        if (locale.equals(countryArray.getJSONObject(i).getString("code"))) {
          //Log.w(TAG, "found country: "+locale);
          flag = "flag_" + locale.toLowerCase();
          int id = getResources().getIdentifier(flag, "drawable", getPackageName());
          Drawable flag = getResources().getDrawable(id);
          showCurCountry(flag, countryArray.getJSONObject(i).getString("dial_code"));
          return;
        }
      }
    } catch (IOException | JSONException e) {
      e.printStackTrace();
    }
  }

  private void showCurCountry(Drawable flag, String dial_code) {
    ivFlag.setImageDrawable(flag);
    tvCountryCode.setText(dial_code);
  }

  private void verify() {
    try {
      mobileNumber =
          "" + tvCountryCode.getText().toString().trim() + eT_number.getText().toString().trim();
      PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
      Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(mobileNumber, null);
      boolean isValid = phoneUtil.isValidNumber(phoneNumberProto); // returns true if valid
      if (isValid) {
        if (call.equals(Constants.BUSINESS)) {
          makeBusinessPhoneOtpReq();
        } else {
          makeOtpReq();
        }
      } else {
        Snackbar.make(root, getString(R.string.string_980), Snackbar.LENGTH_LONG).show();
        //tvError.setText(getString(R.string.string_980));
      }
    } catch (Exception ignored) {
    }
  }

  private void makeBusinessPhoneOtpReq() {
    //        businessPhoneVerification
    JSONObject obj = new JSONObject();
    countryCode = tvCountryCode.getText().toString().trim();
    try {
      obj.put("businessPhone", eT_number.getText().toString().trim());
      obj.put("countryCode", countryCode);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP_BUSINESS, obj,
            new Response.Listener<JSONObject>() {

              @Override
              public void onResponse(JSONObject response) {

                try {
                  Intent intent = new Intent(NumberVerifyActivity.this, VerificationActivity.class);
                  intent.putExtra("isNumberVisible", switchNumber.isChecked());
                  intent.putExtra("type", 2);    // 1 = email , 2 = phone number
                  intent.putExtra("call", Constants.BUSINESS);
                  intent.putExtra("countryCode", countryCode);
                  intent.putExtra("phoneNumber", eT_number.getText().toString().trim());
                  startActivityForResult(intent, 552);
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

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    /* check if the request code is same as what is passed here it is 0 */
    if (resultCode != RESULT_CANCELED && requestCode == 0) {
      String message = data.getStringExtra("MESSAGE");
      String code = data.getStringExtra("CODE");
      int flag = data.getIntExtra("FLAG", R.drawable.flag_in);

      ivFlag.setImageResource(flag);
      tvCountryCode.setText(message);

      /*
       *
       * Since + is coming as well in country code selected
       *
       * */
      tvCountryCode.setText("+" + code.substring(1));
      rL_next.setSelected(countryCode.equalsIgnoreCase(tvCountryCode.getText().toString().trim()));
    } else if (resultCode == 552) {
      if (data.getBooleanExtra("phoneVerified", true)) {
        Intent intent = new Intent();
        intent.putExtras(data);
        intent.putExtra("countryCode", tvCountryCode.getText().toString());
        if (call.equals(Constants.BUSINESS)) {
          intent.putExtra("phoneNumber", eT_number.getText().toString());
        } else {
          intent.putExtra("phoneNumber", mobileNumber);
        }
        intent.putExtra("isVisible",switchNumber.isChecked());
        setResult(552, intent);
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

      obj.put("deviceId", AppController.getInstance().getDeviceId());
      obj.put("development", BuildConfig.DEBUG);
      obj.put("hashKey", Utilities.getHashCode(this));
      obj.put("phoneNumber", eT_number.getText().toString().trim());
      obj.put("countryCode", tvCountryCode.getText().toString().trim());
      obj.put("type", 2);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP, obj,
            new Response.Listener<JSONObject>() {

              @Override
              public void onResponse(JSONObject response) {

                try {
                  if (response.getString("message").equals("success")) {
                    Intent intent =
                        new Intent(NumberVerifyActivity.this, VerificationActivity.class);
                    intent.putExtra("phoneNumber", eT_number.getText().toString().trim());
                    intent.putExtra("countryCode", tvCountryCode.getText().toString().trim());
                    intent.putExtra("isNumberVisible", switchNumber.isChecked());
                    intent.putExtra("type", 2);// 1 = email , 2 = phone number
                    intent.putExtra("call", call);
                    startActivityForResult(intent, 552);
                  } else {

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
}
