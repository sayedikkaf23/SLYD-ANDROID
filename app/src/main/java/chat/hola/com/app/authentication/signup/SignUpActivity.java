package chat.hola.com.app.authentication.signup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.NumberVerification.ChooseCountry;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.CountryCodeUtil;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.RoundedImageView;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.authentication.login.LoginActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.models.Register;
import chat.hola.com.app.notification.UserIdUpdateHandler;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.user_verification.phone.VerifyNumberOTPActivity;

import com.ezcall.android.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jio.consumer.domain.interactor.user.handler.UserHandler;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.onesignal.OneSignal;

import dagger.android.support.DaggerAppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;

public class SignUpActivity extends DaggerAppCompatActivity implements SignUpContract.View {

    public static final int REQUEST_CODE = 123;
    private static final int RESULT_LOAD_PROFILE_IMAGE = 555;
    private static final int RESULT_CAPTURE_PROFILE_IMAGE = 444;
    private static final int FACEBOOK_APP_REQUEST_CODE = 61351;
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9876;

    Register register;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    SignUpContract.Presenter presenter;
    @Inject
    ImageSourcePicker imageSourcePicker;

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ibAdd)
    ImageButton ibAdd;
    @BindView(R.id.ivProfilePic)
    RoundedImageView ivProfilePic;
    /*@BindView(R.id.textRegion)
    TextView textRegion;*/
    @BindView(R.id.tvRegion)
    TextView tvRegion;
    /*@BindView(R.id.textPhone)
    TextView textPhone;*/
    @BindView(R.id.textUserName)
    TextView textUserName;
    @BindView(R.id.textPassword)
    TextView textPassword;
    @BindView(R.id.textConfirmPassword)
    TextView textConfirmPassword;
    @BindView(R.id.textReferralCode)
    TextView textReferralCode;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etLname)
    EditText etLname;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.etReferralCode)
    EditText etReferralCode;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.tvAlreadyMember)
    TextView tvAlreadyMember;
    @BindView(R.id.tvSignIn)
    TextView tvSignIn;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    /*@BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textLName)
    TextView textLName;*/
    @BindView(R.id.singup_text)
    TextView singup_text;
    @BindView(R.id.textEmail)
    TextView textEmail;
    @BindView(R.id.isPrivate)
    Switch isPrivate;
    @BindView(R.id.done)
    Button done;
    @BindView(R.id.tbPassword)
    ToggleButton tbPassword;
    @BindView(R.id.tbConfirmPassword)
    ToggleButton tbConfirmPassword;
    @BindView(R.id.cbTerms)
    CheckBox cbTerms;
    @BindView(R.id.root)
    RelativeLayout root;

    @BindView(R.id.iv_removeUsernameText)
    ImageView iv_removeUsernameText;
    @BindView(R.id.iv_removeEmailText)
    ImageView iv_removeEmailText;

    @BindView(R.id.iV_emailClear)
    ImageView iV_emailCheck;
    @BindView(R.id.iV_userNameCheck)
    ImageView iV_userNameCheck;
    @BindView(R.id.iV_phoneNumberCheck)
    ImageView iV_phoneNumberCheck;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.rl_reg_Mob)
    RelativeLayout rl_reg_Mob;
    @BindView(R.id.videoView)
    VideoView videoView;


    @BindView(R.id.tvUserNamewarning)
    TextView tvUserNamewarning;
    @BindView(R.id.tvUserPhonewarning)
    TextView tvUserPhonewarning;
    @BindView(R.id.tvEmailNamewarning)
    TextView tvEmailNamewarning;

    @BindString(R.string.privacyPolicy)
    String privacyPolicy;
    @BindString(R.string.termsofservice)
    String termsCondition;


    @BindView(R.id.ll_email)
    LinearLayout ll_email;
    @BindView(R.id.tv_terms_conditions)
    TextView tv_terms_conditions;

    @BindView(R.id.ll_signup_UI)
    LinearLayout ll_signup_UI;

    //user name enter fields
    @BindView(R.id.ll_Username_UI)
    LinearLayout ll_Username_UI;
    @BindView(R.id.ivClose_username)
    ImageView ivClose_username;
    @BindView(R.id.etUserName_register)
    EditText etUserName_register;
    @BindView(R.id.rL_next_username)
    RelativeLayout rL_next_username;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;


    protected String countryCode;
    protected String countryName;
    private String profilePic;
    private String userName;
    private String register_userName;
    private String phoneNumber;
    private String userMobileNumber;
    private String referralCode;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String emailAddress;
    protected int max_digits = 16;
    protected int min_digits = 8;
    private UploadFileAmazonS3 uploadFileAmazonS3;
    //private Loader loader;
    private boolean uploadProfilePic = false;

    private FirebaseUser firebaseUser;
    private String loginType = Constants.LoginType.NORMAL;
    @BindView(R.id.googleSignIn)
    Button googleSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    @BindView(R.id.fbSignIn)
    LoginButton fbSignIn;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private String picturePath;
    @Inject
    UserHandler mUserHandler;

    private InputMethodManager imm;
    private String countryCodeName;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //need to change the terms and condition and privacy policy texts color
        termsConditiontext();

        /*
         * Bug Title:Social signup hide
         * Desc: done as required google facebook signup options hidden temporarily
         * Developer name:Ankit K Tiwary
         * Fixed date:8-April-2021*/


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .requestProfile()
//            .build();

//    singup_text.setText(getString(R.string.signupdesc)+" "+getString(R.string.app_name));
//
//    // Build a GoogleSignInClient with the options specified by gso.
//    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//    callbackManager = CallbackManager.Factory.create();
//    fbSignIn = (LoginButton) findViewById(R.id.fbSignIn);
//    fbSignIn.setReadPermissions(Arrays.asList("public_profile"));
//    // If you are using in a fragment, call loginButton.setFragment(this);
//
//    // Callback registration
//    fbSignIn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//      @Override
//      public void onSuccess(LoginResult loginResult) {
//        // App code
//        //executeGraph(loginResult.getAccessToken());
//        handleFacebookAccessToken(loginResult.getAccessToken());
//      }
//
//      @Override
//      public void onCancel() {
//        // App code
//        System.out.println(TAG + " fb cancel");
//      }
//
//      @Override
//      public void onError(FacebookException exception) {
//        // App code
//        System.out.println(TAG + " fb:" + exception.getLocalizedMessage());
//      }
//    });

        uploadFileAmazonS3 = new UploadFileAmazonS3(this);
        register = new Register();

        countryCode = getIntent().getStringExtra("countryCode");
        countryName = getIntent().getStringExtra("country");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        // String email = getIntent().getStringExtra("emailId");
        String userName = getIntent().getStringExtra("username");
        countryCodeName = getIntent().getStringExtra("countryCodeName");
        etUserName.setText(userName);


//    if(phone!=null && !phone.equalsIgnoreCase("")){
//      rl_reg_Mob.setVisibility(View.GONE);
//      ll_email.setVisibility(View.VISIBLE);
//      tvEmailNamewarning.setVisibility(View.INVISIBLE);
//      etName.requestFocus();
//    }else {
//      rl_reg_Mob.setVisibility(View.VISIBLE);
//      ll_email.setVisibility(View.GONE);
//      tvEmailNamewarning.setVisibility(View.GONE);
//      etPhone.requestFocus();
//    }

        showKeyboard();

        //etUserName.setFilters(new InputFilter[] { ignoreFirstWhiteSpace() });

        getFireBaseUser();

        iv_removeUsernameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUserName.setText("");
            }
        });

        iv_removeEmailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail.setText("");
            }
        });

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                String userName = etUserName.getText().toString();
                if (userName.equalsIgnoreCase("")) {
                    iV_userNameCheck.setVisibility(View.GONE);
                    iv_removeUsernameText.setVisibility(View.VISIBLE);
                    tvUserNamewarning.setVisibility(View.VISIBLE);
                    tvUserNamewarning.setText(getString(R.string.emptyUsername));
                } else {
                    tvUserNamewarning.setVisibility(View.INVISIBLE);
                    presenter.verifyIsUserNameRegistered(userName);
                }

            }
        });


    /*etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
        } else {
          String email = etEmail.getText().toString();
          if (CommonClass.validatingEmail(email)) {
            tvEmailNamewarning.setVisibility(View.VISIBLE);
            iV_emailCheck.setVisibility(View.GONE);
            tvEmailNamewarning.setText(getString(R.string.enter_valid_email));
          }else {
            tvEmailNamewarning.setVisibility(View.INVISIBLE);
            iV_emailCheck.setVisibility(View.GONE);
            presenter.verifyIsEmailRegistered(email);
          }
        }
      }
    });

    etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
        } else {
          String userName = etUserName.getText().toString();
          presenter.verifyIsUserNameRegistered(userName);
        }
      }
    });*/

        //opening terms and conditions screen
    /*tv_terms_conditions.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String url = getString(R.string.termsUrl);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
      }
    });*/

        SpannableString ss = new SpannableString(cbTerms.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = getString(R.string.termsUrl);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.color_black));
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }
        };
        ss.setSpan(clickableSpan, 20, cbTerms.getText().toString().length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        cbTerms.setText(ss);
        cbTerms.setMovementMethod(LinkMovementMethod.getInstance());
        cbTerms.setHighlightColor(Color.BLUE);

//    if (cCode != null && !cCode.isEmpty() && cName != null && !cName.isEmpty()) {
//      countryCode = cCode;
//      tvRegion.setText(countryCode);
//    } else {
//      loadCurrentCountryCode();
//    }

//    if (phone != null && !phone.isEmpty()) {
//      etPhone.setText(phone);
//    }

//    if(email!=null && !email.isEmpty()){
//      etEmail.setText(email);
//    }

//    tbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//      @Override
//      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        etPassword.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance()
//                : HideReturnsTransformationMethod.getInstance());
//        etPassword.setSelection(etPassword.getText().toString().length());
//      }
//    });

//    tbConfirmPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//      @Override
//      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        etConfirmPassword.setTransformationMethod(
//                !isChecked ? PasswordTransformationMethod.getInstance()
//                        : HideReturnsTransformationMethod.getInstance());
//        etConfirmPassword.setSelection(etConfirmPassword.getText().toString().length());
//      }
//    });

//    isPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//      @Override
//      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        register.setPrivate(isChecked);
//        isPrivate.setText(isChecked ? "Private" : "Public");
//      }
//    });

        applyFont();

        //loader = new Loader(this);

        setupUI(root);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String referralCode = deepLink.getLastPathSegment();
                            etReferralCode.setText(referralCode);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }

    private void playVideo() {
        try {
            Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.onboard);
            //MediaController media_control = new MediaController(this);
            //videoView.setMediaController(media_control);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) videoView.getLayoutParams();
            params.width = metrics.widthPixels;
            params.height = metrics.heightPixels;
            params.leftMargin = 0;
            videoView.setLayoutParams(params);
            videoView.setZOrderMediaOverlay(true);
            videoView.setVideoURI(uri);
            videoView.start();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //need to change the terms and condition and privacy policy texts color
    private void termsConditiontext() {
        setTermsCondition(tv_terms_conditions, privacyPolicy);
        setTermsCondition(tv_terms_conditions, termsCondition);
        setOnclickHighlighted(tv_terms_conditions, privacyPolicy.toLowerCase(),
                view -> callTermsAndCondition(getString(R.string.privacyPolicyUrl)));
        setOnclickHighlighted(tv_terms_conditions, termsCondition.toLowerCase(),
                view -> callTermsAndCondition(getString(R.string.termsUrl)));
    }

    //opening webui on click of privacy policy or terms of policy text.
    private void callTermsAndCondition(String Url) {
        String url = Url;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void setOnclickHighlighted(TextView tv, String textToHighlight, View.OnClickListener onClickListener) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(textToHighlight, 0);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (onClickListener != null) onClickListener.onClick(textView);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(0xff0000ff);
                ds.setUnderlineText(true);
            }
        };
        SpannableString wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1)
                break;
            else {
                wordToSpan.setSpan(clickableSpan, ofe, ofe + textToHighlight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    //changing text color for termsof condition and privacypolicy text
    private void setTermsCondition(TextView tv, String privacyTerms) {
        String tvt = tv.getText().toString();
        int ofe = tvt.indexOf(privacyTerms, 0);
        Spannable wordToSpan = new SpannableString(tv.getText());
        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(privacyTerms, ofs);
            if (ofe == -1)
                break;
            else {
                wordToSpan.setSpan(new BackgroundColorSpan(0xFFFFFFFF), ofe, ofe + privacyTerms.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }

    //phonenumber/firstname edittext automatically popup
    private void showKeyboard() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imm != null) {
                    imm.showSoftInput(etPhone, InputMethodManager.SHOW_FORCED);
          /*if(phone.equalsIgnoreCase("")){
            etPhone.requestFocus();
            imm.showSoftInput(etPhone, InputMethodManager.SHOW_FORCED);
          }else {
            etName.requestFocus();
            imm.showSoftInput(etName, InputMethodManager.SHOW_FORCED);
          }*/
                }
            }
        }, 200);
    }

    private InputFilter ignoreFirstWhiteSpace() {
        return (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    if (dstart == 0) return "";
                }
            }
            return null;
        };
    }

    ImageSourcePicker.OnSelectImageSource callback = new ImageSourcePicker.OnSelectImageSource() {
        @Override
        public void onCamera() {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, RESULT_CAPTURE_PROFILE_IMAGE);
        }

        @Override
        public void onGallary() {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("image/*");
            startActivityForResult(intent, RESULT_LOAD_PROFILE_IMAGE);
        }

        @Override
        public void onCancel() {
            //nothing to do.
        }
    };

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

        try {
            String allCountriesCode = CountryCodeUtil.readEncodedJsonString(this);
            JSONArray countryArray = new JSONArray(allCountriesCode);
            for (int i = 0; i < countryArray.length(); i++) {
                if (locale.equals(countryArray.getJSONObject(i).getString("code"))) {
                    countryCodeName = countryArray.getJSONObject(i).getString("code");
                    countryCode = countryArray.getJSONObject(i).getString("dial_code");
                    String country = countryArray.getJSONObject(i).getString("name");
                    etPhone.setText("");
                    etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_digits)});
                    tvRegion.setText(countryCode);
                    return;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private Uri getImageUri(Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
    }

    public String getPath(Uri uri) {

        String mediaPath;//= ImageFilePath.getPath(this, uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ImageFilePath.getPathAboveN(this, uri);
        } else {

            return ImageFilePath.getPath(this, uri);
        }
    }

    @OnClick(R.id.tvRegion)
    public void selectRegion() {
        Intent intent = new Intent(SignUpActivity.this, ChooseCountry.class);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.ibAdd)
    public void add() {
        photoPermission();
    }

    private void photoPermission() {
        Dexter.withActivity(SignUpActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            imageSourcePicker.setOnSelectImageSource(callback);
                            imageSourcePicker.show();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Snackbar snackbar = Snackbar.make(root,
                                    "This app needs permission to use this feature. You can grant them in app settings.",
                                    Snackbar.LENGTH_INDEFINITE)
                                    .setAction("GOTO SETTINGS", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            openSettings();
                                        }
                                    });
                            snackbar.setActionTextColor(getResources().getColor(R.color.base_color));
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar.make(root,
                                    "Need permission to access your photo please accept the permission",
                                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    photoPermission();
                                }
                            });
                            snackbar.setActionTextColor(getResources().getColor(R.color.base_color));
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @OnClick(R.id.tvSignIn)
    public void gotoSignIn() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick({R.id.ivBack, R.id.ivClose, R.id.ivClose_username})
    public void back() {
        super.onBackPressed();
    }

    //apply fonts
    private void applyFont() {
        tvAlreadyMember.setTypeface(typefaceManager.getSemiboldFont());
        tvSignIn.setTypeface(typefaceManager.getSemiboldFont());
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        textConfirmPassword.setTypeface(typefaceManager.getSemiboldFont());
        textPassword.setTypeface(typefaceManager.getSemiboldFont());
        // textPhone.setTypeface(typefaceManager.getSemiboldFont());
        textReferralCode.setTypeface(typefaceManager.getSemiboldFont());
        //  textRegion.setTypeface(typefaceManager.getSemiboldFont());
        textUserName.setTypeface(typefaceManager.getSemiboldFont());
        //  textName.setTypeface(typefaceManager.getSemiboldFont());
        //  textLName.setTypeface(typefaceManager.getSemiboldFont());
        textEmail.setTypeface(typefaceManager.getSemiboldFont());

//    etEmail.setTypeface(typefaceManager.getRegularFont());
//    etPhone.setTypeface(typefaceManager.getRegularFont());
        etUserName.setTypeface(typefaceManager.getRegularFont());
//    etConfirmPassword.setTypeface(typefaceManager.getRegularFont());
//    etPassword.setTypeface(typefaceManager.getRegularFont());
        etName.setTypeface(typefaceManager.getRegularFont());
        etLname.setTypeface(typefaceManager.getRegularFont());
//    etReferralCode.setTypeface(typefaceManager.getRegularFont());
        done.setTypeface(typefaceManager.getSemiboldFont());
    }


    @OnClick({R.id.done, R.id.tv_verify, R.id.rL_next_username})
    public void signUp() {
        if (validate()) {
//      register.setPrivate(isPrivate.isChecked());
            register.setCountryCode(countryCode);
            register.setCountry(countryName);
            register.setFirstName(firstName);
            register.setLastName(lastName);
            register.setUserName(userName);
            register.setMobileNumber(phoneNumber);
            //if we are signup through social creds then get user name values from ll_Username_UI->etUserName_register
      /*if(loginType.equals(Constants.LoginType.GOOGLE) || loginType.equals(Constants.LoginType.FACEBOOK) ) {
        register.setUserName(register_userName);
      }else {
        register.setUserName(userName);
      }*/
      /*//passing username field as firstandlast name
        register.setUserName(firstName+" "+lastName);*/
//      register.setMobileNumber(phoneNumber);
//      register.setPassword(confirmPassword);
//      register.setUploadProfilePic(uploadProfilePic);
//      register.setProfilePic(profilePic);
//      register.setReferralCode(referralCode);
//      register.setEmail(emailAddress);

//      register.setLoginType(loginType);
//      if (loginType.equals(Constants.LoginType.GOOGLE))
//        register.setGoogleId(firebaseUser.getUid());
//      else if (loginType.equals(Constants.LoginType.FACEBOOK))
//        register.setFacebookId(firebaseUser.getUid());

            ivBack.setEnabled(false);
            ibAdd.setEnabled(false);
            done.setEnabled(false);
            presenter.validate(register);//, uploadFileAmazonS3, new File(profilePic));
        } else {
            enableSave(true);
        }
    }


    @OnClick({R.id.ivClose})
    public void closeActivity() {
        finish();
    }

  /*@OnClick({ R.id.iV_emailClear })
  public void emailClear() {
    etEmail.setText(getString(R.string.double_inverted_comma));
    iV_emailClear.setVisibility(View.GONE);
  }*/

    /**
     * Validates all the required fields
     *
     * @return true if all fields are valid otherwise false
     */
    private boolean validate() {
        String fullName = etName.getText().toString().trim();
        firstName = etName.getText().toString().trim();
        lastName = etLname.getText().toString().trim();
        userName = etUserName.getText().toString().trim();
//    String password = etPassword.getText().toString().trim();
//    referralCode = etReferralCode.getText().toString().trim();
//    phoneNumber = etPhone.getText().toString().trim();
//    confirmPassword = etPassword.getText().toString().trim();

        //  confirmPassword = etConfirmPassword.getText().toString().trim();
        //userName = etUserName.getText().toString().trim();
        register_userName = etUserName_register.getText().toString();
//    emailAddress = etEmail.getText().toString().trim();

    /*//if we signup with social logins then need to check only weather username is empty or not.in other cases need to check all
    if(loginType.equals(Constants.LoginType.GOOGLE) || loginType.equals(Constants.LoginType.FACEBOOK) ){
      if (register_userName.isEmpty()) {
        message("Please enter user name");
        return false;
      }
    }else {*/
//      if (countryCode == null || countryCode.isEmpty()) {
//        message("Please select region");
//        return false;
//      }

        if (firstName.isEmpty()) {
            message("Please enter your first name");
            return false;
        }

        if (userName.isEmpty()) {
            message("Please enter user name");
            return false;
        }

        if (lastName.isEmpty()) {
            message("Please enter last name");
            return false;
        }


//      if (phoneNumber.isEmpty()) {
//        message("Please enter phone number");
//        return false;
//      }

//      if (phoneNumber.length() < min_digits || phoneNumber.length() > max_digits) {
//        message("Please enter valid mobile number");
//        return false;
//      }

      /*if (!Utilities.isValidEmail(emailAddress)) {
        message("Please enter valid email address");
        iV_emailClear.setVisibility(View.VISIBLE);
        return false;
      }*/

//      if (password.isEmpty()) {
//        message("Please enter password");
//        return false;
//      }
//
//      if (password.length() < 8) {
//        message("Please enter at least 8 letter password");
//        return false;
//      }
//    if (profilePic == null) {
//      uploadProfilePic = true;
//    }

        // lastName = etLname.getText().toString().trim();

        return true;
        //commeting this for new UI
    /*if (confirmPassword.isEmpty()) {
      message("Please confirm password");
      return false;
    }

    //commenting this code as in newUI we don't have confirmPassword field
    if (!confirmPassword.equals(password)) {
      message("Confirm password must match your password");
      return false;
    }*/

//    if (referralCode.isEmpty()) {
//      message("Please enter referral code");
//      return false;
//    }


//    if (!cbTerms.isChecked()) {
//      message("Please accept Shoutout Terms of Service and Privacy policy");
//      return false;
//    }

//    String[] fullname = fullName.split("\\s+");
//    firstName = fullname[0];
//    if (fullname.length > 1 && !fullname[1].isEmpty()) {
//      lastName = fullname[1];
//    } else {
//      lastName = "";
//    }


    }

    @Override
    public void registered(Login.LoginResponse response) {
        hideLoader();
        //        nextFlow(response);
        enableSave(true);
        Intent intent = new Intent(this, DiscoverActivity.class);
        intent.putExtra("call", "SaveProfile");
        startActivity(intent);
        finish();
        supportFinishAfterTransition();
    }

    private void nextFlow(Login.LoginResponse response) {
        /*
         *
         * To start the Profile screen
         */
        try {

            //response = response.getJSONObject("response");

            String profilePic = "", userName = "", firstName = "", lastName = "";
            // String token = response.getString("token");

            CouchDbController db = AppController.getInstance().getDbController();

            Map<String, Object> map = new HashMap<>();

            if (response.getProfilePic() != null) {
                profilePic = response.getProfilePic();
                map.put("userImageUrl", response.getProfilePic());
            } else {
                map.put("userImageUrl", "");
            }
            if (response.getUserName() != null) {
                userName = response.getUserName();
                map.put("userName", response.getUserName());
            }

            if (response.getFirstName() != null) {
                firstName = response.getFirstName();
                map.put("firstName", response.getFirstName());
            }

            if (response.getLastName() != null) {
                lastName = response.getLastName();
                map.put("lastName", response.getLastName());
            }

            String userStatus = getString(R.string.default_status);

            map.put("userId", response.getUserId());

            try {
                map.put("private", response.get_private());
            } catch (Exception ignored) {
            }


            /*
             * To save the social status as the text value in the properties
             *
             */

            if (response.getSocialStatus() != null) {

                userStatus = response.getSocialStatus();
            }
            map.put("socialStatus", userStatus);
            map.put("userIdentifier", phoneNumber);
            map.put("apiToken", response.getToken());

            AppController.getInstance()
                    .getSharedPreferences()
                    .edit()
                    .putString("token", response.getToken())
                    .apply();

            /*
             * By phone number verification
             */

            map.put("userLoginType", 1);
            map.put("excludedFilterIds", new ArrayList<Integer>());
            if (!db.checkUserDocExists(AppController.getInstance().getIndexDocId(),
                    response.getUserId())) {

                String userDocId = db.createUserInformationDocument(map);

                db.addToIndexDocument(AppController.getInstance().getIndexDocId(), response.getUserId(),
                        userDocId);
            } else {

                db.updateUserDetails(
                        db.getUserDocId(response.getUserId(), AppController.getInstance().getIndexDocId()),
                        map);
            }
            if (!userName.isEmpty()) {

                db.updateIndexDocumentOnSignIn(AppController.getInstance().getIndexDocId(),
                        response.getUserId(), 1, true);
            } else {
                db.updateIndexDocumentOnSignIn(AppController.getInstance().getIndexDocId(),
                        response.getUserId(), 1, false);
            }
            /*
             * To update myself as available for call
             */
            mUserHandler.setAuthToken(response.getToken());
            mUserHandler.setUserId(response.getUserId());
      /*AppController.getInstance().setSignedIn(true, response.getUserId(),
              userName, phoneNumber, 1);*/
            AppController.getInstance().setSignedIn(true, response.getUserId(), userName, phoneNumber, profilePic, 1, response.getAccountId(), response.getProjectId(),
                    response.getKeysetId(), response.getLicenseKey(), response.getRtcAppId(),
                    response.getArFiltersAppId(), response.getGroupCallStreamId());
            AppController.getInstance().setSignStatusChanged(true);

            AppController.getInstance().setSignStatusChanged(true);

            String topic = response.getUserId();
            FirebaseMessaging.getInstance().subscribeToTopic(topic);

            SessionManager sessionManager = new SessionManager(this);
            sessionManager.setUserName(userName);
            sessionManager.setFirstName(firstName);
            sessionManager.setLastsName(lastName);
            //            sessionManager.setFacebookAccessToken(AccessToken.getCurrentAccessToken());
            sessionManager.setUserProfilePic(profilePic, true);

            Intent i = new Intent(this, DiscoverActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("caller", "SaveProfile");
            startActivity(i);
            finish();
            supportFinishAfterTransition();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Display toast message
     */
    @Override
    public void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void completed() {
        ivBack.setEnabled(true);
        ibAdd.setEnabled(true);
        done.setEnabled(true);
    }

    @Override
    public void setProfilePic(String url) {
        profilePic = url;
        sessionManager.setUserProfilePic(url, true);
    }

    @Override
    public void enableSave(boolean enable) {
        done.setEnabled(enable);
        done.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showProgress(boolean show) {
        if (progress_bar != null)
            progress_bar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void verifyPhone() {

        //if we are doing social logins then call signup api else call verifyOTP screen
        if (loginType.equals(Constants.LoginType.GOOGLE) || loginType.equals(Constants.LoginType.FACEBOOK)) {
            callSignupApi();
        } else {
            Intent intent = new Intent(this, VerifyNumberOTPActivity.class);
            if (phoneNumber.equalsIgnoreCase("")) {
                intent.putExtra("countryCode", "");
            } else {
                intent.putExtra("countryCode", countryCode);
            }
            intent.putExtra("countryCodeName", countryCodeName);
            intent.putExtra("country", countryName);
            intent.putExtra("countryCode", countryCode);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("isFromSignUp", true);
            intent.putExtra("profilePic", profilePic);
            intent.putExtra("register", register);
            startActivity(intent);
        }


        //        startActivityForResult(intent, Constants.Verification.NUMBER_VERIFICATION_REQ);
    }

    private void callSignupApi() {
        register.setPrivate(isPrivate.isChecked());
//    if(phoneNumber.equalsIgnoreCase("")){
//      register.setCountryCode("");
//    }else {
//      register.setCountryCode(countryCode);
//    }

        register.setFirstName(firstName);
        register.setLastName(lastName);
        register.setMobileNumber(phoneNumber);
        //if we are signup through social creds then get user name values from ll_Username_UI->etUserName_register
//    if(loginType.equals(Constants.LoginType.GOOGLE) || loginType.equals(Constants.LoginType.FACEBOOK) ) {
//      register.setUserName(userName);
//    }else {
        register.setUserName(userName);

        /*Bug Title:max input restricted to 15
         * Bug Desc:required to limit user input of username and phone number to 5
         * Developer name :Ankit K Tiwary
         * Fixed Date:14-April-2021*/
        //etUserName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_digits)});
        //}
      /*//passing username field as firstandlast name
        register.setUserName(firstName+" "+lastName);*/
//    register.setMobileNumber(phoneNumber);
//    register.setPassword(confirmPassword);
////      register.setUploadProfilePic(uploadProfilePic);
//    register.setProfilePic(profilePic);
//    register.setReferralCode(referralCode);
//    register.setEmail(emailAddress);

        register.setLoginType(loginType);
        if (loginType.equals(Constants.LoginType.GOOGLE))
            register.setGoogleId(firebaseUser.getUid());
        else if (loginType.equals(Constants.LoginType.FACEBOOK))
            register.setFacebookId(firebaseUser.getUid());

        String appVersion;
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            appVersion = "123";
        }

        if (register != null) {
            register.setDeviceName(Build.DEVICE);
            register.setDeviceOs(Build.VERSION.RELEASE);
            register.setModelNumber(Build.MODEL);
            register.setDeviceType("2");
            register.setDeviceId(AppController.getInstance().getDeviceId());
            register.setAppVersion(appVersion);
            register.setCountry("");
            register.setCountryCodeName(countryCodeName);
        }
        presenter.signUp(this, register, uploadFileAmazonS3, profilePic);
    }

    @Override
    public void showLoader() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        progress_bar.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        // callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 101) {
                photoPermission();
            } else if (requestCode == Constants.Verification.NUMBER_VERIFICATION_REQ) {
                enableSave(true);
                if (data.getBooleanExtra("isVerified", true)) {
                    presenter.signUp(this, register, uploadFileAmazonS3, profilePic);
                }
            } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                profilePic = data.getStringExtra("profilePic");
                Bitmap bitmap = BitmapFactory.decodeFile(profilePic);
                ivProfilePic.setImageBitmap(bitmap);
            } else if (requestCode == 0 && resultCode == RESULT_OK) {
                countryCodeName = data.getStringExtra("CODE_NAME");
                String code = data.getStringExtra("CODE");
                countryCode = "+" + code.substring(1);
                etPhone.setText(getString(R.string.double_inverted_comma));
                etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_digits)});
                tvRegion.setText(countryCode);
            } else if (requestCode == RESULT_LOAD_PROFILE_IMAGE) {
                Uri uri = data.getData();
                profilePic = getPath(uri);
                if (profilePic == null) {

                    Toast.makeText(this, getString(R.string.image_selection_failed), Toast.LENGTH_SHORT)
                            .show();
                } else {

                    Bitmap bitmap = BitmapFactory.decodeFile(profilePic);
                    ivProfilePic.setImageBitmap(bitmap);
                }
            } else if (requestCode == RESULT_CAPTURE_PROFILE_IMAGE) {
                try {
                    Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    if (photo != null) {
                        ivProfilePic.setImageBitmap(photo);
                        Uri uri = getImageUri(photo);
                        profilePic = getPath(uri);
                        if (profilePic == null) {

                            Toast.makeText(this, getString(R.string.image_selection_failed), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == RC_SIGN_IN) {
                // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                //handleSignInResult(task);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                    hideLoader();
                    // ...
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fBCustom)
    public void fbCustomButtonClick() {
        fbSignIn.performClick();
    }

    @OnClick({R.id.googleSignIn})
    public void googleSignIn() {
        showLoader();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void getFireBaseUser() {
        firebaseUser = getIntent().getParcelableExtra("fireBaseUser");
        if (getIntent().hasExtra("loginType"))
            loginType = getIntent().getStringExtra("loginType");
        //if user try to login through social login and if that id not registred with us then asking user to enter user name
        // directly by hiding the signup page
   /* if(loginType.equals(Constants.LoginType.GOOGLE) || loginType.equals(Constants.LoginType.FACEBOOK) ){
      ll_signup_UI.setVisibility(View.GONE);
      ll_Username_UI.setVisibility(View.VISIBLE);
      etUserName_register.requestFocus();
    }*/
        if (firebaseUser != null) {
            etEmail.setText(firebaseUser.getEmail());
            // etName.setText(firebaseUser.getDisplayName());
            String[] name = firebaseUser.getDisplayName().split("\\s+");
            if (!name.equals(" ")) {
                etName.setText((name[0] == null) ? "" : name[0]);
                etLname.setText(name.length > 1 ? (name[1] == null) ? "" : name[1] : "");
            }
        }
    }

    @Override
    public void socialLoginSuccess(Login.LoginResponse response) {
        Intent i = new Intent(this, DiscoverActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("caller", "SaveProfile");
        startActivity(i);
        finish();
        supportFinishAfterTransition();
    }

    @Override
    public void socialIdNotRegistered(FirebaseUser user, String loginType) {
        this.firebaseUser = user;
        this.loginType = loginType;
        if (firebaseUser != null) {
            etEmail.setText(firebaseUser.getEmail());
            etName.setText(firebaseUser.getDisplayName());
        }
    }

    @Override
    public void emailalreadyRegistered(String email) {
        iV_emailCheck.setVisibility(View.GONE);
        iv_removeEmailText.setVisibility(View.VISIBLE);
        tvEmailNamewarning.setVisibility(View.VISIBLE);
    }

    @Override
    public void emailNotRegistered(String email_number_is_not_registered) {
        iV_emailCheck.setVisibility(View.VISIBLE);
        iv_removeEmailText.setVisibility(View.GONE);
        tvEmailNamewarning.setVisibility(View.INVISIBLE);

    }

    @Override
    public void userNamealreadyRegistered(String userName) {
        iV_userNameCheck.setVisibility(View.GONE);
        iv_removeUsernameText.setVisibility(View.VISIBLE);
        tvUserNamewarning.setVisibility(View.VISIBLE);
    }

    @Override
    public void userNameNotRegistered(String userName_is_already_in_use) {
        iV_userNameCheck.setVisibility(View.VISIBLE);
        iv_removeUsernameText.setVisibility(View.GONE);
        tvUserNamewarning.setVisibility(View.INVISIBLE);
    }

    @Override
    public void numberAlreadyRegistered(String phoneNumber) {
        iV_phoneNumberCheck.setVisibility(View.GONE);
        tvUserPhonewarning.setVisibility(View.VISIBLE);
    }

    @Override
    public void numberNotRegistered(String mobile_number_is_not_registered) {
        iV_phoneNumberCheck.setVisibility(View.VISIBLE);
        tvUserPhonewarning.setVisibility(View.INVISIBLE);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoader();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                presenter.socialSignIn(Utilities.getThings(), Constants.LANGUAGE, user, Constants.LoginType.GOOGLE);
                            }
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(e -> hideLoader())
                .addOnCanceledListener(this::hideLoader);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideLoader();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                presenter.socialSignIn(Utilities.getThings(), Constants.LANGUAGE, user, Constants.LoginType.FACEBOOK);
                            }
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            /*Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);*/
                        }

                        // ...
                    }
                }).addOnCanceledListener(this::hideLoader)
                .addOnFailureListener(e -> hideLoader());
    }

    @OnFocusChange({R.id.etEmail, R.id.etUserName, R.id.etPhone})
    void onFocusChangeEvent(View view, boolean hasFocus) {
        switch (view.getId()) {
//      case R.id.etEmail:
//        if(!hasFocus) {
//          String email = etEmail.getText().toString();
//          if(email.equalsIgnoreCase("")){
//            iV_emailCheck.setVisibility(View.GONE);
//            tvEmailNamewarning.setVisibility(View.VISIBLE);
//            tvEmailNamewarning.setText(getString(R.string.emptyEmail));
//          }else if (!email.equalsIgnoreCase("") && CommonClass.validatingEmail(email)) {
//            tvEmailNamewarning.setVisibility(View.VISIBLE);
//            iV_emailCheck.setVisibility(View.GONE);
//            tvEmailNamewarning.setText(getString(R.string.enter_valid_email));
//          }else {
//            tvEmailNamewarning.setVisibility(View.INVISIBLE);
//            iV_emailCheck.setVisibility(View.GONE);
//            presenter.verifyIsEmailRegistered(email);
//          }
//
//
//        }
//        break;
            case R.id.etUserName:
                if (!hasFocus) {
                    String userName = etUserName.getText().toString();
                    if (userName.equalsIgnoreCase("")) {
                        iV_userNameCheck.setVisibility(View.GONE);
                        iv_removeUsernameText.setVisibility(View.VISIBLE);
                        tvUserNamewarning.setVisibility(View.VISIBLE);
                        tvUserNamewarning.setText(getString(R.string.emptyUsername));
                    } else {
                        tvUserNamewarning.setVisibility(View.INVISIBLE);
//                        presenter.verifyIsUserNameRegistered(userName);
                    }
                }
                break;
//      case R.id.etPhone:
//        if(!hasFocus) {
//          String phoneNumber = etPhone.getText().toString().trim();
//          if(phoneNumber.equalsIgnoreCase("")){
//            iV_phoneNumberCheck.setVisibility(View.GONE);
//            tvUserPhonewarning.setVisibility(View.VISIBLE);
//            tvUserPhonewarning.setText(getString(R.string.emptyPhoneNumber));
//          }else {
//            tvUserPhonewarning.setVisibility(View.INVISIBLE);
//            presenter.verifyIsMobileRegistered(phoneNumber, countryCode);
//          }
//
//        }
//        break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        playVideo();
        root.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        root.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                Utilities.hideSoftKeyboard(SignUpActivity.this);
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

}


