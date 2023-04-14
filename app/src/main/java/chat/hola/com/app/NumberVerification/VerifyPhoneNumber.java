package chat.hola.com.app.NumberVerification;
/*
 * Created by moda on 15/07/16.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.PromptDialog;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.CountryCode;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity to allow user to enter number to be verified for signup/phone number update
 */

public class VerifyPhoneNumber extends DaggerAppCompatActivity {
  private SessionApiCall sessionApiCall = new SessionApiCall();

  @BindView(R.id.ivFlag)
  ImageView ivFlag;
  @BindView(R.id.tvCountryCode)
  TextView tvCountryCode;
  @BindView(R.id.rlCountryCode)
  RelativeLayout rlCountryCode;
  @BindView(R.id.etPhoneNumber)
  EditText etPhoneNumber;
  @BindView(R.id.root)
  RelativeLayout root;

  @BindView(R.id.tvEnter)
  TextView tvEnter;
  @BindView(R.id.tvNumber)
  TextView tvNumber;
  @BindView(R.id.tvError)
  TextView tvError;
  @BindView(R.id.etReferCode)
  EditText etReferCode;

  @Inject
  Bus bus;
  @Inject
  TypefaceManager typefaceManager;

  private ProgressDialog pDialog;
  private Unbinder unbinder;
  private AlertDialog.Builder builder;
  private String mobileNumber;
  private String countryCode;
  private String flag;
  private String call;
  private SessionManager sessionManager;

  @SuppressWarnings("TryWithIdenticalCatches")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.verify_phone_number);
    unbinder = ButterKnife.bind(this);
    sessionManager = new SessionManager(this);
    builder = new AlertDialog.Builder(this);
    builder.setMessage("We need permission to read OTP, please grant");

    applyFont();
    loadCurrentCountryCode();
    etPhoneNumber.requestFocus();
    pDialog = new ProgressDialog(this, 0);
    pDialog.setCancelable(false);
    bus.register(this);

    mobileNumber = getIntent().getStringExtra("mobile_number");
    countryCode = getIntent().getStringExtra("country_code");
    call = getIntent().getStringExtra("call");
    etReferCode.setFilters(new InputFilter[] { new InputFilter.AllCaps() });

    flag = getIntent().getStringExtra("flag");

    if (mobileNumber != null) etPhoneNumber.setText(mobileNumber);
    if (flag != null && countryCode != null) {
      int id = getResources().getIdentifier(flag, "drawable", getPackageName());
      Drawable flagDrawable = getResources().getDrawable(id);
      showCurCountry(flagDrawable, countryCode);
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
      String allCountriesCode = readEncodedJsonString(VerifyPhoneNumber.this);
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

  private void applyFont() {
    tvEnter.setTypeface(typefaceManager.getBoldFont());
    tvNumber.setTypeface(typefaceManager.getBoldFont());
    tvError.setTypeface(typefaceManager.getRegularFont());
    tvCountryCode.setTypeface(typefaceManager.getRegularFont());
    etPhoneNumber.setTypeface(typefaceManager.getRegularFont());
    etReferCode.setTypeface(typefaceManager.getRegularFont());
    tvCountryCode.setTypeface(typefaceManager.getRegularFont());
  }

  @OnClick({ R.id.rlCountryCode, R.id.tvCountryCode, R.id.ivFlag })
  public void countryCodePicker() {
    Intent intent = new Intent(VerifyPhoneNumber.this, ChooseCountry.class);
    startActivityForResult(intent, 0);
  }

  @OnClick(R.id.ibNext)
  public void done() {
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

  private void verify() {
    try {
      String phoneNumberE164Format =
          "" + tvCountryCode.getText().toString().trim() + etPhoneNumber.getText()
              .toString()
              .trim();
      PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
      Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumberE164Format, null);
      boolean isValid = phoneUtil.isValidNumber(phoneNumberProto); // returns true if valid
      if (isValid) {
        String referralCode = etReferCode.getText().toString().trim();
          if (referralCode.isEmpty()) {
              showAlertDialog();
          } else {
              verifyReferralCode(referralCode);
          }
      } else {
        tvError.setText(getString(R.string.string_980));
      }
    } catch (Exception ignored) {
    }
  }

  private void verifyReferralCode(String referral_code) {
    showProgressDialog(getString(R.string.validating_referral_code));

    JSONObject object = new JSONObject();
    try {
      object.put("referralCode", referral_code);
    } catch (Exception e) {
    }

    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.GET, ApiOnServer.VERIFY_REFERRAL_CODE, object,
            new Response.Listener<JSONObject>() {

              @Override
              public void onResponse(JSONObject response) {
                hideProgressDialog();

                try {
                  if (response.getString("message").equals("success")) showAlertDialog();
                } catch (Exception ignored) {

                }
              }
            }, new Response.ErrorListener() {

          @Override
          public void onErrorResponse(VolleyError error) {
            hideProgressDialog();
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
                          verifyReferralCode(referral_code);
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

    jsonObjReq.setRetryPolicy(new

        DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add the request to the RequestQueue.*/
    AppController.getInstance().

        addToRequestQueue(jsonObjReq, "verifyReferralCodeRequest");
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
    }
  }

  @SuppressWarnings("TryWithIdenticalCatches")
  private void showAlertDialog() {

    // Actions to perform if the number is valid
    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(VerifyPhoneNumber.this);
    alertDialog.setMessage(getString(R.string.weWillVerifying)
        + "\n\n"
        + ""
        + tvCountryCode.getText().toString()
        + " "
        + etPhoneNumber.getText().toString()
        + "\n\n"
        + getString(R.string.isThisOk));
    alertDialog.setNegativeButton(R.string.string_594, new DialogInterface.OnClickListener() {
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

    alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        //                                Log.d("exe", "click ok");
                          /*  TextView continueTv=(TextView) dialogView.findViewById(R.id.continueTv);
                            TextView notNowTv=(TextView)dialogView.findViewById(R.id.notNowTv);
                                continueTv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                                notNowTv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });*/

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
        /*make a request to server to send for otp*/


        /*
         * *
         *
         * type 0 permission granted,1 not granted
         *
         * */

        //                                if (ActivityCompat.checkSelfPermission(VerifyPhoneNumber.this, Manifest.permission.READ_SMS)
        //                                        == PackageManager.PERMISSION_GRANTED) {
        makeOtpReq(0);
        //                                    Log.d("exe", "requestSmsReceivingPermission 0");

        //                                } else {
        // Permission is missing and must be requested.
        //                                    requestSmsReceivingPermission();
        //                                    Log.d("exe", "requestSmsReceivingPermission");
        //                                }
      }
    });
    alertDialog.show();
  }

  /**
   * To request OTP from the server
   *
   * @param type 0-read SMS permission granted
   * 1-read SMS permission denied
   */

  @SuppressWarnings("unchecked,TryWithIdenticalCatches")
  private void makeOtpReq(final int type) {

    JSONObject obj = new JSONObject();
    try {
      obj.put("phoneNumber", etPhoneNumber.getText().toString().trim());
      obj.put("countryCode", tvCountryCode.getText().toString().trim());
      obj.put("development", BuildConfig.DEBUG);
      obj.put("hashKey", Utilities.getHashCode(this));
      obj.put("deviceId", AppController.getInstance().getDeviceId());
    } catch (JSONException e) {
      e.printStackTrace();
    }

    showProgressDialog(getString(R.string.string_547));

    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.POST, ApiOnServer.REQUEST_OTP, obj,
            new Response.Listener<JSONObject>() {

              @Override
              public void onResponse(JSONObject response) {

                hideProgressDialog();

                try {

                  switch (response.getInt("code")) {

                    case 200:

                      /*
                       * OTP requested successfully
                       */

                      //                            response = response.getJSONObject("response");
                      //                            String otp = response.getString("otp");
                      //                            String otp = "";
                      //
                      //
                      //                            if (response.has("otp"))
                      //                                otp = response.getString("otp");

                      //   if (otp != null) {

                      Intent intent = new Intent(VerifyPhoneNumber.this, SmsVerification.class);

                      intent.putExtra("phoneNumber",
                          "" + tvCountryCode.getText().toString().trim() + etPhoneNumber.getText()
                              .toString()
                              .trim());
                      //  intent.putExtra("code", otp);
                      intent.putExtra("type", type);

                      intent.putExtra("countryCode", tvCountryCode.getText().toString().trim());
                      intent.putExtra("mobileNumber", etPhoneNumber.getText().toString().trim());
                      intent.putExtra("flag", flag);

                      intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                      // startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(VerifyPhoneNumber.this).toBundle());
                      startActivity(intent);
                      supportFinishAfterTransition();
                      //                            } else {
                      //                                showErrorDialog();
                      //                            }

                      break;

                    //
                    //
                    //                        case 189:
                    ///*
                    // *Phone number already registered
                    // */
                    //
                    //                            final PromptDialog p = new PromptDialog(VerifyPhoneNumber.this);
                    //
                    //
                    //                            p.setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                    //                                    .setTitleText(R.string.string_354).setContentText(R.string.string_357)
                    //                                    .setPositiveListener(R.string.string_580, new PromptDialog.OnPositiveListener() {
                    //                                        @Override
                    //                                        public void onClick(PromptDialog dialog) {
                    //
                    //
                    //                                            Context context = ((ContextWrapper) (dialog).getContext()).getBaseContext();
                    //
                    //
                    //                                            if (context instanceof Activity) {
                    //
                    //
                    //                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    //                                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                    //                                                        dialog.dismiss();
                    //                                                    }
                    //                                                } else {
                    //
                    //
                    //                                                    if (!((Activity) context).isFinishing()) {
                    //                                                        dialog.dismiss();
                    //                                                    }
                    //                                                }
                    //                                            } else {
                    //
                    //
                    //                                                try {
                    //                                                    dialog.dismiss();
                    //                                                } catch (final IllegalArgumentException e) {
                    //                                                    e.printStackTrace();
                    //
                    //                                                } catch (final Exception e) {
                    //                                                    e.printStackTrace();
                    //
                    //                                                }
                    //                                            }
                    //
                    //
                    //                                        }
                    //                                    }).show();
                    //
                    //
                    //                            new Handler().postDelayed(new Runnable() {
                    //                                @Override
                    //                                public void run() {
                    //
                    //                                    if (p.isShowing()) {
                    //                                        //   p.dismiss();
                    //
                    //                                        Context context = ((ContextWrapper) (p).getContext()).getBaseContext();
                    //
                    //
                    //                                        if (context instanceof Activity) {
                    //
                    //
                    //                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    //                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                    //                                                    p.dismiss();
                    //                                                }
                    //                                            } else {
                    //
                    //
                    //                                                if (!((Activity) context).isFinishing()) {
                    //                                                    p.dismiss();
                    //                                                }
                    //                                            }
                    //                                        } else {
                    //
                    //
                    //                                            try {
                    //                                                p.dismiss();
                    //                                            } catch (final IllegalArgumentException e) {
                    //                                                e.printStackTrace();
                    //
                    //                                            } catch (final Exception e) {
                    //                                                e.printStackTrace();
                    //
                    //                                            }
                    //                                        }
                    //
                    //
                    //                                    }
                    //
                    //
                    //                                }
                    //                            }, 2000);
                    //
                    //                            break;
                    //
                    //

                    case 137:
                      //same device same phone number 3 times

                      final PromptDialog p2 = new PromptDialog(VerifyPhoneNumber.this);

                      p2.setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                          .setTitleText(R.string.string_354)
                          .setContentText(R.string.string_970)
                          .setPositiveListener(R.string.string_580,
                              new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {

                                  Context context =
                                      ((ContextWrapper) (dialog).getContext()).getBaseContext();

                                  if (context instanceof Activity) {

                                    if (Build.VERSION.SDK_INT
                                        >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                      if (!((Activity) context).isFinishing()
                                          && !((Activity) context).isDestroyed()) {
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
                              })
                          .show();
                      new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                          if (p2.isShowing()) {
                            //   p.dismiss();

                            Context context = ((ContextWrapper) (p2).getContext()).getBaseContext();

                            if (context instanceof Activity) {

                              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing()
                                    && !((Activity) context).isDestroyed()) {
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
                      final PromptDialog p3 = new PromptDialog(VerifyPhoneNumber.this);

                      p3.setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                          .setTitleText(R.string.string_354)
                          .setContentText(R.string.string_971)
                          .setPositiveListener(R.string.string_580,
                              new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {

                                  Context context =
                                      ((ContextWrapper) (dialog).getContext()).getBaseContext();

                                  if (context instanceof Activity) {

                                    if (Build.VERSION.SDK_INT
                                        >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                      if (!((Activity) context).isFinishing()
                                          && !((Activity) context).isDestroyed()) {
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
                              })
                          .show();

                      new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                          if (p3.isShowing()) {
                            //   p.dismiss();

                            Context context = ((ContextWrapper) (p3).getContext()).getBaseContext();

                            if (context instanceof Activity) {

                              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing()
                                    && !((Activity) context).isDestroyed()) {
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
                      final PromptDialog p4 = new PromptDialog(VerifyPhoneNumber.this);

                      p4.setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                          .setTitleText(R.string.string_354)
                          .setContentText(R.string.string_972)
                          .setPositiveListener(R.string.string_580,
                              new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {

                                  Context context =
                                      ((ContextWrapper) (dialog).getContext()).getBaseContext();

                                  if (context instanceof Activity) {

                                    if (Build.VERSION.SDK_INT
                                        >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                      if (!((Activity) context).isFinishing()
                                          && !((Activity) context).isDestroyed()) {
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
                              })
                          .show();

                      new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                          if (p4.isShowing()) {
                            //   p.dismiss();

                            Context context = ((ContextWrapper) (p4).getContext()).getBaseContext();

                            if (context instanceof Activity) {

                              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing()
                                    && !((Activity) context).isDestroyed()) {
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
                      final PromptDialog p5 = new PromptDialog(VerifyPhoneNumber.this);

                      p5.setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                          .setTitleText(R.string.string_354)
                          .setContentText(R.string.string_973)
                          .setPositiveListener(R.string.string_580,
                              new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {

                                  Context context =
                                      ((ContextWrapper) (dialog).getContext()).getBaseContext();

                                  if (context instanceof Activity) {

                                    if (Build.VERSION.SDK_INT
                                        >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                      if (!((Activity) context).isFinishing()
                                          && !((Activity) context).isDestroyed()) {
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
                              })
                          .show();

                      new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                          if (p5.isShowing()) {
                            //   p.dismiss();

                            Context context = ((ContextWrapper) (p5).getContext()).getBaseContext();

                            if (context instanceof Activity) {

                              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing()
                                    && !((Activity) context).isDestroyed()) {
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
                        tvError.setText(response.getString("message"));
                        //
                        //                                Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);
                        //
                        //
                        //                                snackbar.show();
                        //                                View view = snackbar.getView();
                        //                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        //                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                      }
                      showErrorDialog(getString(R.string.string_548));
                  }
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            }, new Response.ErrorListener() {

          @Override
          public void onErrorResponse(VolleyError volleyError) {

            hideProgressDialog();

            if (volleyError.networkResponse != null
                && volleyError.networkResponse.statusCode == 406) {
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
                          makeOtpReq(type);
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

              String message = null;
              if (volleyError instanceof NetworkError) {
                message = "Cannot connect to Internet...Please check your connection!";
              } else if (volleyError instanceof ServerError) {
                message = "The server could not be found. Please try again after some time!!";
              } else if (volleyError instanceof AuthFailureError) {
                message = "Cannot connect to Internet...Please check your connection!";
              } else if (volleyError instanceof ParseError) {
                message = "Parsing error! Please try again after some time!!";
              } else if (volleyError instanceof NoConnectionError) {
                message = "Cannot connect to Internet...Please check your connection!";
              } else if (volleyError instanceof TimeoutError) {
                message = "Connection TimeOut! Please check your internet connection.";
              } else {
                message = getString(R.string.string_548);
              }
              showErrorDialog(message);
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
    AppController.getInstance().addToRequestQueue(jsonObjReq, "verifyPhoneNumberApiRequest");
  }

  /**
   * To show error dialog incase failed to send OTP
   */
  @SuppressWarnings("TryWithIdenticalCatches")
  public void showErrorDialog(String message) {

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(VerifyPhoneNumber.this);
    alertDialog.setTitle(R.string.string_356);
    alertDialog.setMessage(message);

    alertDialog.setNegativeButton(R.string.string_580, new DialogInterface.OnClickListener() {
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
    alertDialog.show();
  }

  /**
   * To show progress dialog
   */

  private void showProgressDialog(String message) {
    pDialog.setMessage(message);
    if (pDialog != null && !pDialog.isShowing()) {
      pDialog.show();
      ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);

      bar.getIndeterminateDrawable()
          .setColorFilter(ContextCompat.getColor(VerifyPhoneNumber.this, R.color.color_black),
              android.graphics.PorterDuff.Mode.SRC_IN);
    }
  }

  /**
   * To hide progress dialog
   */

  @SuppressWarnings("TryWithIdenticalCatches")
  private void hideProgressDialog() {
    if (pDialog.isShowing()) {
      Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();

      if (context instanceof Activity) {

        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
          pDialog.dismiss();
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

  /**
   * To request read SMS permission to automatically detect verification received via SMS
   */
  private void requestSmsReceivingPermission() {
    ActivityCompat.requestPermissions(VerifyPhoneNumber.this,
        new String[] { Manifest.permission.READ_SMS }, 0);

    //        // Permission has not been granted and must be requested.
    //        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
    //                Manifest.permission.READ_SMS)) {
    //            // Provide an additional rationale to the user if the permission was not granted
    //            // and the user would benefit from additional context for the use of the permission.
    //            // Display a SnackBar with a button to request the missing permission.
    //
    //            RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
    //            Snackbar.make(root, R.string.string_85,
    //                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_905), new View.OnClickListener() {
    //
    //                @Override
    //                public void onClick(View view) {
    //                    // Request the permission
    //                    ActivityCompat.requestPermissions(VerifyPhoneNumber.this,
    //                            new String[]{Manifest.permission.READ_SMS},
    //                            0);
    //
    //                }
    //            }).show();
    //
    //        } else {
    //            // Request the permission. The result will be received in onRequestPermissionResult().
    //            ActivityCompat.requestPermissions(VerifyPhoneNumber.this, new String[]{Manifest.permission.READ_SMS},
    //                    0);
    //            Log.d("exe", "executed msg permission");
    //            //showAlertDialog();
    //
    //            // Dialog dialog = new Dialog(this);
    //            // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    //            //   dialog.setContentView(R.layout.verifynumber_read_sms_dialog);
    //
    //            //dialog.show();
    //
    //
    //        }
  }

  /**
   * Result of read SMS permission request
   */

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    //        // BEGIN_INCLUDE(onRequestPermissionsResult)
    //        if (requestCode == 0) {
    //
    //            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //
    //
    //                makeOtpReq(0);
    //
    //            } else {
    //
    //                makeOtpReq(1);
    //            }
    //        }

    if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
          == PackageManager.PERMISSION_GRANTED) {
        makeOtpReq(0);
      }
    } else {

      // permission was not granted
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
        showPermissionMessage();
      } else {
        gotoSettings();
      }
    }
  }

  private void gotoSettings() {

    builder.setPositiveButton("Setting", (dialogInterface, i) -> {
      Intent intent = new Intent();
      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      Uri uri = Uri.fromParts("package", getPackageName(), null);
      intent.setData(uri);
      startActivity(intent);
    });
    builder.show();
  }

  private void showPermissionMessage() {
    builder.setPositiveButton("Retry",
        (dialogInterface, i) -> ActivityCompat.requestPermissions(VerifyPhoneNumber.this,
            new String[] { Manifest.permission.READ_SMS }, 0));
    builder.show();
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

  private static String readEncodedJsonString(Context context) throws IOException {
    //        String base64 = context.getResources().getString(R.string.countries_code);
    byte[] data = Base64.decode(CountryCode.ENCODED_COUNTRY_CODE, Base64.DEFAULT);
    return new String(data, "UTF-8");
  }

  @Subscribe
  public void getMessage(JSONObject object) {
    try {
      if (object.getString("eventName").equals("callMinimized")) {

        minimizeCallScreen(object);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void minimizeCallScreen(JSONObject obj) {
    try {
      Intent intent = new Intent(VerifyPhoneNumber.this, ChatMessageScreen.class);
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
    if (unbinder != null) unbinder.unbind();
  }
}