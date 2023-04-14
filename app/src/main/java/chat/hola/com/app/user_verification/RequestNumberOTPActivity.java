package chat.hola.com.app.user_verification;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.observer.ApiServiceGenerator;
import chat.hola.com.app.NumberVerification.ChooseCountry;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.TranResponse;
import chat.hola.com.app.models.WalletTransactionData;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.user_verification.phone.VerifyNumberOTPActivity;

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
import retrofit2.Call;
import retrofit2.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static chat.hola.com.app.Utilities.CountryCodeUtil.readEncodedJsonString;

/**
 * <h1>{@link chat.hola.com.app.user_verification.RequestNumberOTPActivity}</h1>
 * <p>This activity class is used request verification code on phone number.</p>
 *
 * @author: Hardik Karkar
 * @since : 29th May 2019
 */
public class RequestNumberOTPActivity extends AppCompatActivity {
    private SessionApiCall sessionApiCall = new SessionApiCall();
    @BindView(R.id.tV_disableMsg)
    TextView tV_disableMsg;
    @BindView(R.id.rL_phoneNumber)
    RelativeLayout rL_phoneNumber;
    private Unbinder unbinder;

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
    @BindView(R.id.llContact)
    LinearLayout llContact;
    @BindView(R.id.rL_next)
    RelativeLayout rL_next;
    private String mobileNumber;
    private String countryCode;
    private String flag;
    private Data profileData;
    String newValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verify);
        unbinder = ButterKnife.bind(this);

        tV_disableMsg.setVisibility(View.GONE);
        rL_phoneNumber.setVisibility(View.GONE);

        profileData = (Data) getIntent().getSerializableExtra("profileData");
        mobileNumber = getIntent().getStringExtra("mobileNumber");

        /**if we open this activity from{@link chat.hola.com.app.authentication.signup.SignUpActivity} then we will not pass profile data and hide llContainer */
        llContact.setVisibility(profileData != null ? View.VISIBLE : View.GONE);

        if (profileData != null) {
            if (profileData.getStarRequest() != null
                    && profileData.getStarRequest().getStarUserPhoneNumber() != null) {
                eT_number.setText(profileData.getStarRequest().getPhoneNumber());
                newValue = profileData.getNumber();
                countryCode = profileData.getCountryCode();
            }
        } else if (profileData.getNumber() != null) {
            // eT_number.setText(profileData.getNumber());
        }

        loadCurrentCountryCode();
        eT_number.requestFocus();

        rL_next.setVisibility(View.GONE);
        eT_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (newValue.length() > 7 && newValue.length() < 15) {
                    verifyIsPhoneRegistered(countryCode, newValue);
                } else {
                    rL_next.setVisibility(View.GONE);
                }
            }
        });
    }

    private void verifyIsPhoneRegistered(String countryCode, String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("countryCode", countryCode);
        map.put("type", "2");

        HowdooService service = ApiServiceGenerator.createService(HowdooService.class);
        Call<Error> call = service.emailPhoneVerification1(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map);
        call.enqueue(new Callback<Error>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<Error> call, @NotNull retrofit2.Response<Error> response) {
                rL_next.setVisibility(response.code() == 204 ? View.VISIBLE : View.GONE);
                if (response.code() == 200) {
                    Toast.makeText(RequestNumberOTPActivity.this, "Phone number is already in use", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Error> call, @NotNull Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @OnClick({R.id.rlCountryCode, R.id.tvCountryCode, R.id.ivFlag})
    public void countryCodePicker() {
        Intent intent = new Intent(RequestNumberOTPActivity.this, ChooseCountry.class);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.rL_next)
    public void next() {
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

    /**
     * <p>This method is used to load a country code at time activity launch</p>
     */
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
            String allCountriesCode = readEncodedJsonString(RequestNumberOTPActivity.this);
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

    /**
     * <p>This method set the country code and flag to UI.</p>
     */
    private void showCurCountry(Drawable flag, String dial_code) {
        ivFlag.setImageDrawable(flag);
        tvCountryCode.setText(dial_code);
        countryCode = dial_code;
    }

    /**
     * <p>This method check user input phone number is valid or not then request API call.</p>
     */
    private void verify() {
        try {
            mobileNumber =
                    "" + tvCountryCode.getText().toString().trim() + eT_number.getText().toString().trim();
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(mobileNumber, null);
            boolean isValid = phoneUtil.isValidNumber(phoneNumberProto); // returns true if valid
            if (isValid) {
                makeOtpReq();
            } else {
                Snackbar.make(root, getString(R.string.string_980), Snackbar.LENGTH_LONG).show();
                //tvError.setText(getString(R.string.string_980));
            }
        } catch (Exception ignored) {
        }
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
            tvCountryCode.setText(getString(R.string.string_743) + code.substring(1));
        } else if (resultCode == Constants.Verification.NUMBER_VERIFICATION_REQ) {
            if (data.getBooleanExtra("phoneVerified", true)) {
                Intent intent = new Intent();
                intent.putExtras(data);
                intent.putExtra("phoneNumber", eT_number.getText().toString().trim());
                intent.putExtra("countryCode", tvCountryCode.getText().toString().trim());
                setResult(Constants.Verification.NUMBER_VERIFICATION_REQ, intent);
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
            obj.put("type", 1); // here type 1 for verify user, 2 for star user verify
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP, obj,
                        (Response.Listener<JSONObject>) response -> {

                            try {
                                if (response.getString("message").equals("success")) {
                                    Intent intent =
                                            new Intent(RequestNumberOTPActivity.this, VerifyNumberOTPActivity.class);
                                    intent.putExtra("phoneNumber", eT_number.getText().toString().trim());
                                    intent.putExtra("countryCode", tvCountryCode.getText().toString().trim());
                                    intent.putExtra("isFromSignUp", profileData == null);
                                    startActivityForResult(intent, Constants.Verification.NUMBER_VERIFICATION_REQ);
                                } else {

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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
