package chat.hola.com.app.authentication.login;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.NumberVerification.ChooseCountry;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.authentication.forgotpassword.ForgotPasswordActivity;
import chat.hola.com.app.authentication.signup.SignUp1Activity;
import chat.hola.com.app.authentication.signup.SignUpActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.user_verification.phone.VerifyNumberOTPActivity;
import chat.hola.com.app.webScreen.WebActivity;
import com.ezcall.android.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import dagger.android.support.DaggerAppCompatActivity;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import org.json.JSONArray;
import org.json.JSONException;

import static chat.hola.com.app.Utilities.CountryCodeUtil.readEncodedJsonString;

public class LoginActivity extends DaggerAppCompatActivity implements LoginContract.View {


    private static final int FACEBOOK_APP_REQUEST_CODE = 6135;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    LoginContract.Presenter presenter;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.textAccount)
    TextView textAccount;
    @BindView(R.id.textRegion)
    TextView textRegion;
    @BindView(R.id.tvRegion)
    TextView tvRegion;
    @BindView(R.id.textPhone)
    TextView textPhone;
    @BindView(R.id.textPassword)
    TextView textPassword;
    @BindView(R.id.textOtp)
    TextView textOtp;
    @BindView(R.id.loginSwitch)
    TextView loginSwitch;
    @BindView(R.id.tvNotMember)
    TextView tvNotMember;
    @BindView(R.id.tvSignUp)
    TextView tvSignUp;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.loginByStarChatId)
    TextView loginByStarChatId;
    @BindView(R.id.done)
    Button done;

    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etOtp)
    EditText etOtp;

    @BindView(R.id.llUserName)
    LinearLayout llUserName;
    @BindView(R.id.llRegion)
    LinearLayout llRegion;
    @BindView(R.id.llPhone)
    RelativeLayout llPhone;
    @BindView(R.id.llPassword)
    LinearLayout llPassword;
    @BindView(R.id.llotp)
    LinearLayout llotp;
    @BindView(R.id.ic_send)
    ImageView ic_send;
    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.ivClose_pswd)
    ImageView ivClose_pswd;

    @BindView(R.id.tv_login_terms)
    TextView tv_login_terms;




    @BindView(R.id.otpDivider)
    View otpDivider;
    @BindView(R.id.passwordDivider)
    View passwordDivider;
    @BindView(R.id.phoneDivider)
    View phoneDivider;
    @BindView(R.id.userNameDivider)
    View userNameDivider;
    @BindView(R.id.btnForgotPassword)
    Button btnForgotPassword;
    @BindView(R.id.tbPassword)
    ToggleButton tbPassword;

    @BindView((R.id.tbPassword_email))
    ToggleButton tbPassword_email;

    @BindView(R.id.loginemail)
    RelativeLayout loginemail;

    @BindView(R.id.loginphone)
    RelativeLayout loginphone;

    @BindView(R.id.llEmail)
    RelativeLayout llEmail;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.ic_send_email)
    ImageView ic_send_email;

    //password enter fields
    @BindView(R.id.ll_password_UI)
    LinearLayout ll_password_UI;
    @BindView(R.id.ll_Login_UI)
    LinearLayout ll_Login_UI;
    @BindView(R.id.etPassword_email)
    EditText etPassword_email;
    @BindView(R.id.rL_next)
    RelativeLayout rL_next;
    @BindView(R.id.tv_email)
    TextView tv_email;
    @BindView(R.id.tV_forgotpswd)
    TextView tV_forgotpswd;
    @BindView(R.id.rl_forget_mail_sent)
    RelativeLayout rl_forget_mail_sent;
    @BindView(R.id.tv_sent_msg)
    TextView tv_sent_msg;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
//    @BindView(R.id.videoView)
//    VideoView videoView;



    FirebaseUser user;
    String loginType = "";

    @BindString(R.string.privacyPolicy)
    String privacyPolicy;
    @BindString(R.string.termsofservice)
    String termsCondition;

    private int login_mode;
    private String countryCode;
    private String country;
    private Login login;
    /*Bug Title:max input restricted to 15
     * Bug Desc:required to limit user input of username and phone number to 5
     * Developer name :Ankit K Tiwary
     * Fixed Date:14-April-2021*/
    private int max_digits = 15;
    private boolean isLogin;
    private boolean isForgotPassword = false;
    private String call;

    private String emailPassword;
    String appVersion;

    @BindView(R.id.googleSignIn)
    Button googleSignIn;
    @BindView(R.id.backButton)
    AppCompatImageView backButton;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    @BindView(R.id.fbSignIn)
    LoginButton fbSignIn;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9876;
    private static final int EMAIL_PSWD_IN = 9877;
    private InputMethodManager imm;
    private String countryCodeName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        etPhone.requestFocus();
        showKeyboard();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        termsConditiontext();


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(getString(R.string.default_web_client_id))
//        .requestEmail()
//        .requestProfile()
//        .build();
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//
//        callbackManager = CallbackManager.Factory.create();
//        fbSignIn = (LoginButton) findViewById(R.id.fbSignIn);
//        fbSignIn.setReadPermissions(Arrays.asList("public_profile"));
//        // If you are using in a fragment, call loginButton.setFragment(this);
//
//        // Callback registration
//        fbSignIn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//     @Override
//     public void onSuccess(LoginResult loginResult) {
//        // App code
//        //executeGraph(loginResult.getAccessToken());
//        handleFacebookAccessToken(loginResult.getAccessToken());
//        }
//
//     @Override
//     public void onCancel() {
//        // App code
//        System.out.println(TAG + " fb cancel");
//        }
//
//        @Override
//       public void onError(FacebookException exception) {
//        // App code
//        System.out.println(TAG + " fb:" + exception.getLocalizedMessage());
//        }
//        });

        //loader = new Loader(this);
//        presenter.test();

        Utilities.printHashKey(this);

        call = getIntent().getStringExtra("call");
        if (call == null)
            call = "";

        login = new Login();
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("countryCode") != null) {
            etPhone.setText(intent.getStringExtra("phoneNumber"));
            countryCode = intent.getStringExtra("countryCode");
            countryCodeName = intent.getStringExtra("countryCodeName");
        } else {
            loadCurrentCountryCode();
        }
        applyFont();

        tbPassword_email.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etPassword_email.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
                etPassword_email.setSelection(etPassword_email.getText().toString().length());
            }
        });

        tbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etPassword.setTransformationMethod(!isChecked ? PasswordTransformationMethod.getInstance() : HideReturnsTransformationMethod.getInstance());
                etPassword.setSelection(etPassword.getText().toString().length());
            }
        });

        setupUI(root);

        loginMode(etPhone.getText().toString().trim().isEmpty() ? 0 : 2);
    }

    private void playVideo() {
        try {
            Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.onboard);
            //MediaController media_control = new MediaController(this);
            //videoView.setMediaController(media_control);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            RelativeLayout.LayoutParams params =
//                    (RelativeLayout.LayoutParams) videoView.getLayoutParams();
//            params.width = metrics.widthPixels;
//            params.height = metrics.heightPixels;
//            params.leftMargin = 0;
//            videoView.setLayoutParams(params);
//            videoView.setZOrderMediaOverlay(true);
//            videoView.setVideoURI(uri);
//            videoView.start();
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.setLooping(true);
//                }
//            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //need to change the terms and condition and privacy policy texts color
    private void termsConditiontext() {
        setTermsCondition(tv_login_terms, privacyPolicy);
        setTermsCondition(tv_login_terms, termsCondition);
        setOnclickHighlighted(tv_login_terms, privacyPolicy.toLowerCase(),
                view -> callTermsAndCondition(getString(R.string.privacyPolicyUrl)));
        setOnclickHighlighted(tv_login_terms, termsCondition.toLowerCase(),
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

    private void showKeyboard() {
        //phonenumber edittext automatically popup

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imm != null) {
                    imm.showSoftInput(etPhone, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    //calling login with email Api
    @OnClick(R.id.rL_next)
    public void loginwithEmailApi() {
        if (etPassword_email.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else {
            presenter.loginWithEmailId(etEmail.getText().toString().trim(), etPassword_email.getText().toString().trim(), appVersion, country);
        }

    }

    @OnClick(R.id.backButton)
    public void backPressed(){
        onBackPressed();
    }

    //opening terms and conditions screen
    @OnClick(R.id.tv_login_terms)
    public void openTermsandConditions() {
        String url = getString(R.string.termsUrl);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    @OnClick(R.id.tV_forgotpswd)
    public void forgotPasswordApi() {
        Intent intent1 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        intent1.putExtra("userName", "");
        intent1.putExtra("email", etEmail.getText().toString().trim());
        startActivityForResult(intent1, Constants.REQ_FORGOT_PASSWORD_EMAIL);
    }


    //on click of login with email - hide phone number filed
    @OnClick(R.id.loginemail)
    public void loginwithEmailClick() {
        loginemail.setVisibility(View.GONE);
        loginphone.setVisibility(View.VISIBLE);
        llEmail.setVisibility(View.VISIBLE);
        llPhone.setVisibility(View.GONE);
        etEmail.requestFocus();
        showKeyboard();
    }

    //on click of login with phone - hide email entering filed
    @OnClick(R.id.loginphone)
    public void loginwithPhoneClick() {
        loginemail.setVisibility(View.VISIBLE);
        loginphone.setVisibility(View.GONE);
        llEmail.setVisibility(View.GONE);
        llPhone.setVisibility(View.VISIBLE);
        etPhone.requestFocus();
        showKeyboard();
    }

    @OnClick({R.id.fBCustom, R.id.loginwithfacbookbuttonrlout})
    public void fbCustomButtonClick() {
        fbSignIn.performClick();
    }

    @OnClick({R.id.googleSignIn, R.id.loginwithgooglebuttonrlout})
    public void googleSignIn() {
        showLoader();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @OnClick(R.id.btnForgotPassword)
    public void forgotPasswordButton() {
        if (login_mode == 0)
            isLogin = false;
        isForgotPassword = true;
        verifyPhone();
    }

    @OnClick(R.id.tvSend)
    public void send() {
        tvSend.setText(getString(R.string.TryAgain));
        tvSend.setEnabled(false);
        tvSend.setTextColor(getResources().getColor(R.color.star_grey));
//        presenter.requestOtp(etPhone.getText().toString(), countryCode);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    tvSend.setText(getString(R.string.send));
                    tvSend.setEnabled(true);
                    tvSend.setTextColor(getResources().getColor(R.color.base_color));
                });
            }
        }, 30000);
    }

    //showing loginUi and hiding email password UI - on click of close button in password screen
    @OnClick({R.id.ivClose_pswd, R.id.tv_email})
    public void showLoginUI() {
        ll_Login_UI.setVisibility(View.VISIBLE);
        ll_password_UI.setVisibility(View.GONE);
    }

    @OnClick({R.id.ivBack, R.id.ivClose})
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (!call.equals(getString(R.string.action_settings)))
            super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
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
            String allCountriesCode = readEncodedJsonString(this);
            JSONArray countryArray = new JSONArray(allCountriesCode);
            for (int i = 0; i < countryArray.length(); i++) {
                if (locale.equals(countryArray.getJSONObject(i).getString("code"))) {
                    countryCodeName = countryArray.getJSONObject(i).getString("code");
                    countryCode = countryArray.getJSONObject(i).getString("dial_code");
                    country = countryArray.getJSONObject(i).getString("name");
                    etPhone.setText(getString(R.string.double_inverted_comma));
                    etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_digits)});
//                    ic_send.setVisibility(View.GONE);
                    tvRegion.setText(countryCode);
                    return;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ic_send,R.id.btn_send})
    public void switchLoginEmailMode() {
        loginMode(0);
        etEmail.setText("");
            /*if (login_mode == 3)
                loginMode(0);
            else
                loginMode(3);*/
    }

    @OnClick(R.id.ic_send_email)
    public void switchLoginPhoneMode() {
        loginMode(3);
        etPhone.setText("");
       /* if (login_mode == 0)
            loginMode(3);
        else
            loginMode(0);*/
    }

    @OnClick({R.id.done, R.id.ic_send, R.id.ic_send_email, R.id.btn_send})
    public void login() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            appVersion = version;
        } catch (PackageManager.NameNotFoundException e) {
            appVersion = "123";
        }

        switch (login_mode) {
            case 0:
                if (etPhone.getText().toString().trim().isEmpty())
                    message("Please enter valid mobile number");
                else if (countryCode == null || countryCode.isEmpty())
                    message("Please select region");
                else {
                    isLogin = true;
                    isForgotPassword = false;
                    verifyPhone();
                }
                break;
            //commenting this for new UI flow
                /*if (etPhone.getText().toString().trim().isEmpty() || etPhone.getText().toString().trim().length() > max_digits)
                    message("Please enter valid mobile number");
                else if (etPassword.getText().toString().trim().isEmpty())
                    message("Please enter password");
                else if (countryCode != null && countryCode.isEmpty())
                    message("Please select region");
                else {
                    presenter.loginByPassword(countryCode, etPhone.getText().toString().trim(), etPassword.getText().toString().trim(), appVersion, country);
                }
                break;*/
            case 1:
                if (etAccount.getText().toString().trim().isEmpty())
                    message("Please enter Epic Shout Out ID");
                else if (etPassword.getText().toString().trim().isEmpty())
                    message("Please enter password");
                else {
                    presenter.loginWithEmailId(etAccount.getText().toString().trim(), etPassword.getText().toString().trim(), appVersion, country);
                }
                break;
            case 2:
                if (etPhone.getText().toString().trim().isEmpty())
                    message("Please enter valid mobile number");
                else if (countryCode == null || countryCode.isEmpty())
                    message("Please select region");
                else {
                    isLogin = true;
                    isForgotPassword = false;
                    verifyPhone();
                }
                break;
            case 3:
                isLogin = true;
                isForgotPassword = false;
                verifyEmail();
                break;
        }
    }

    @OnClick(R.id.tvRegion)
    public void selectRegion() {
        Intent intent = new Intent(LoginActivity.this, ChooseCountry.class);
        startActivityForResult(intent, 0);
    }


    @OnClick(R.id.loginByStarChatId)
    public void changeLoginMode() {
        if (login_mode == 1)
            loginMode(0);
        else
            loginMode(1);
    }

    @OnClick(R.id.loginSwitch)
    public void loginMode() {
        if (login_mode == 2)
            loginMode(0);
        else
            loginMode(2);
    }

    /**
     * @param login_mode : 0=phone number & password, 1=username & password , 2= phone number and otp
     */
    private void loginMode(int login_mode) {
        this.login_mode = login_mode;

        switch (login_mode) {
            case 0:
                done.setText(getString(R.string.next));
                //comenting for new UI flow
               /* llRegion.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.VISIBLE);
                llPassword.setVisibility(View.VISIBLE);
                llUserName.setVisibility(View.GONE);
                otpDivider.setVisibility(View.GONE);
                userNameDivider.setVisibility(View.GONE);
                phoneDivider.setVisibility(View.VISIBLE);
                passwordDivider.setVisibility(View.VISIBLE);
                btnForgotPassword.setVisibility(View.VISIBLE);


                loginSwitch.setText(getString(R.string.login_via_otp));
                loginSwitch.setVisibility(View.VISIBLE);
                loginByStarChatId.setVisibility(View.VISIBLE);*/

                llRegion.setVisibility(View.GONE);
                llPhone.setVisibility(View.VISIBLE);
                llEmail.setVisibility(View.GONE);
                llPassword.setVisibility(View.GONE);
                llUserName.setVisibility(View.GONE);
                otpDivider.setVisibility(View.GONE);
                userNameDivider.setVisibility(View.GONE);
                phoneDivider.setVisibility(View.GONE);
                passwordDivider.setVisibility(View.GONE);
                btnForgotPassword.setVisibility(View.GONE);


                loginSwitch.setText(getString(R.string.login_via_otp));
                loginSwitch.setVisibility(View.GONE);
                loginByStarChatId.setVisibility(View.GONE);
                etPhone.requestFocus();
                break;
            case 1:
                done.setText(getString(R.string.next));
                llRegion.setVisibility(View.GONE);
                llPhone.setVisibility(View.GONE);
                llPassword.setVisibility(View.VISIBLE);
                llUserName.setVisibility(View.VISIBLE);
                otpDivider.setVisibility(View.GONE);
                userNameDivider.setVisibility(View.VISIBLE);
                phoneDivider.setVisibility(View.GONE);
                passwordDivider.setVisibility(View.VISIBLE);
                btnForgotPassword.setVisibility(View.VISIBLE);

                loginSwitch.setText(getString(R.string.login_via_phone_no));
                loginSwitch.setVisibility(View.VISIBLE);
                loginByStarChatId.setVisibility(View.GONE);
                etAccount.requestFocus();
                break;
            case 2:
                done.setText(getString(R.string.next));
                llRegion.setVisibility(View.VISIBLE);
                llPhone.setVisibility(View.VISIBLE);
                llPassword.setVisibility(View.GONE);
                llUserName.setVisibility(View.GONE);
                otpDivider.setVisibility(View.VISIBLE);
                userNameDivider.setVisibility(View.GONE);
                phoneDivider.setVisibility(View.VISIBLE);
                passwordDivider.setVisibility(View.VISIBLE);
                btnForgotPassword.setVisibility(View.GONE);

                loginSwitch.setText(R.string.login_via_password);
                loginSwitch.setVisibility(View.VISIBLE);
                loginByStarChatId.setVisibility(View.VISIBLE);
                etPhone.requestFocus();
                break;
            case 3:
                done.setText(getString(R.string.next));
                llRegion.setVisibility(View.GONE);
                llPhone.setVisibility(View.GONE);
                llEmail.setVisibility(View.VISIBLE);
                llPassword.setVisibility(View.GONE);
                llUserName.setVisibility(View.GONE);
                otpDivider.setVisibility(View.GONE);
                userNameDivider.setVisibility(View.GONE);
                phoneDivider.setVisibility(View.GONE);
                passwordDivider.setVisibility(View.GONE);
                btnForgotPassword.setVisibility(View.GONE);


                loginSwitch.setText(getString(R.string.login_via_otp));
                loginSwitch.setVisibility(View.GONE);
                loginByStarChatId.setVisibility(View.GONE);
                etEmail.requestFocus();
                break;
        }
    }

    @OnClick(R.id.tvSignUp)
    public void signUp() {
        Intent intent = new Intent(LoginActivity.this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", getResources().getString(R.string.privacyPolicyUrl));
        bundle.putString("title", getResources().getString(R.string.privacyPolicy));
        bundle.putString("action", "accept");
        startActivity(intent.putExtra("url_data", bundle));
    }

    private void applyFont() {
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        textAccount.setTypeface(typefaceManager.getSemiboldFont());
        textRegion.setTypeface(typefaceManager.getSemiboldFont());
        textPhone.setTypeface(typefaceManager.getSemiboldFont());
        textPassword.setTypeface(typefaceManager.getSemiboldFont());
        textOtp.setTypeface(typefaceManager.getSemiboldFont());
        loginSwitch.setTypeface(typefaceManager.getSemiboldFont());
        tvNotMember.setTypeface(typefaceManager.getSemiboldFont());
        tvSignUp.setTypeface(typefaceManager.getSemiboldFont());
        tvSend.setTypeface(typefaceManager.getSemiboldFont());
        loginByStarChatId.setTypeface(typefaceManager.getSemiboldFont());
        btnForgotPassword.setTypeface(typefaceManager.getSemiboldFont());
        done.setTypeface(typefaceManager.getSemiboldFont());
        tvRegion.setTypeface(typefaceManager.getRegularFont());
        etAccount.setTypeface(typefaceManager.getRegularFont());
        etPhone.setTypeface(typefaceManager.getRegularFont());
        etPassword.setTypeface(typefaceManager.getRegularFont());
        etOtp.setTypeface(typefaceManager.getRegularFont());

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
//                    ic_send.setVisibility(View.VISIBLE);
                    Log.i("TEST", "TEST APP");
                } else {
//                    ic_send.setVisibility(View.GONE);
                    Log.i("TEST", "HIDE TEST APP");
                }

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //vaildating email
                if (s.toString().length() > 8) {
                    if (CommonClass.validatingEmail(s.toString())) {

                        ic_send_email.setVisibility(View.GONE);

                    } else {
                        ic_send_email.setVisibility(View.VISIBLE);
                    }

                } else {
                    ic_send_email.setVisibility(View.GONE);
                }

            }
        });

    }

    @Override
    public void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginSuccessfully() {
        Intent i = new Intent(this, LandingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        supportFinishAfterTransition();
    }

    @Override
    public void emailNotRegistered(String error) {
        if (!isFinishing()) {

            //directly navigating to signup screen insted of webview activity
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("emailId", etEmail.getText().toString());
            startActivity(intent);
            supportFinishAfterTransition();
        }
    }

    @Override
    public void numberNotRegistered(String message) {
        if (!isFinishing()) {

            //directly navigating to signup screen insted of webview activity
            Intent intent = new Intent(this, SignUp1Activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("countryCode", tvRegion.getText().toString());
            intent.putExtra("countryCodeName", countryCodeName);
            if (!country.equalsIgnoreCase("")) {
                intent.putExtra("country", country);
            } else {
                intent.putExtra("country", "IN");
            }
            intent.putExtra("phoneNumber", etPhone.getText().toString());
            intent.putExtra("country", country);
            startActivity(intent);
            supportFinishAfterTransition();


            //directly showing signup page insted of showing dialog
            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setMessage(message + "\n\nDo you want to SignUp with this number ?");
            builder.setPositiveButton("SignUp", (dialog, which) ->
            {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", getResources().getString(R.string.privacyPolicyUrl));
                bundle.putString("title", getResources().getString(R.string.privacyPolicy));
                bundle.putString("action", "accept");
                startActivity(intent.putExtra("url_data", bundle));
            });
            builder.setNegativeButton("Change", (dialog, which) -> dialog.cancel());
            builder.show();*/
        }
    }


    @Override
    public void blocked(String errorMessage) {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setCancelable(false);
        confirm.setMessage(errorMessage);
        confirm.setPositiveButton(R.string.ok, (dialog, w) -> finish());
        confirm.create().show();
    }

    @Override
    public void verifyOtp(String phoneNumber, String countryCode) {

        Intent intent = new Intent(this, VerifyNumberOTPActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("countryCode", countryCode);
        intent.putExtra("country", country);
        intent.putExtra("isLogin", isLogin);
        intent.putExtra("isForgotPassword", isForgotPassword);
        startActivity(intent);
    }

    @Override
    public void enterEmailPswd(String emailId) {

        //hiding loginUi and showing email password to enter
        tv_email.setText(emailId);
        ll_Login_UI.setVisibility(View.GONE);
        ll_password_UI.setVisibility(View.VISIBLE);
        etPassword_email.requestFocus();
        /*Intent emailPswdIntent = new Intent(this, EnterPasswordActivity.class);
        emailPswdIntent.putExtra("emailId", emailId);
        startActivityForResult(emailPswdIntent, EMAIL_PSWD_IN);*/
    }

    @Override
    public void showLoader() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        if (progress_bar != null)
            progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void forgotPassword() {
        Intent intent1 = new Intent(this, ForgotPasswordActivity.class);
        intent1.putExtra("userName", etAccount.getText().toString());
        startActivity(intent1);
    }

    public void verifyPhone() {
        String phone = etPhone.getText().toString();
        if (phone.length() < 8 || phone.length() > max_digits){
            message("Please enter valid mobile number");
        } else {
            if (verifyFields(phone, countryCode))
                presenter.verifyIsMobileRegistered(phone, countryCode, isForgotPassword);
            //            presenter.requestOtp(phone, countryCode);
        }
    }

    //to verify weather the registed email is registred with us or not
    public void verifyEmail() {
        String email = etEmail.getText().toString();
        presenter.verifyIsEmailRegistered(email, isForgotPassword);
    }


    private boolean verifyFields(String phone, String countryCode) {

        if (countryCode == null || countryCode.isEmpty()) {
            message("Please select a country");
            return false;
        }

        if (phone == null || phone.isEmpty()) {
            message("Please enter a valid phone number");
            return false;
        }

        return true;
    }

    @Override
    public void socialLoginSuccess(Login.LoginResponse response) {
        loginSuccessfully();
    }

    @Override
    public void socialIdNotRegistered(FirebaseUser user, String loginType) {

        //in social login if user is not registred then show signupscreen and asking user to enter username
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra("loginType", loginType);
        intent.putExtra("fireBaseUser", user);
        startActivity(intent);
        supportFinishAfterTransition();

        /*Intent intent = new Intent(LoginActivity.this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", getResources().getString(R.string.privacyPolicyUrl));
        bundle.putString("title", getResources().getString(R.string.privacyPolicy));
        bundle.putString("action", "accept");
        bundle.putParcelable("fireBaseUser", user);
        bundle.putString("loginType", loginType);
        startActivity(intent.putExtra("url_data", bundle));*/
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        /*
         * BugTitle:crash on selecting country code
         * Bug Desc:getting extras call back cause crash not required for now
         * Developer name:Ankit K Tiwary
         * Fixed Date:14-April-2021*/
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 0 && resultCode == RESULT_OK) {
            countryCodeName = data.getStringExtra("CODE_NAME");
            country = data.getStringExtra("MESSAGE");
            String code = data.getStringExtra("CODE");
            countryCode = "+" + code.substring(1);
            etPhone.setText(getString(R.string.double_inverted_comma));
//            ic_send.setVisibility(View.GONE);
            etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_digits)});
            tvRegion.setText(countryCode);
        } else if (requestCode == EMAIL_PSWD_IN) {
            emailPassword = data.getStringExtra("PASSWORD");
            Log.i(TAG, "emailpswd " + emailPassword);
            presenter.loginWithEmailId(etEmail.getText().toString(), emailPassword, appVersion, country);
        } else if (requestCode == Constants.REQ_FORGOT_PASSWORD_EMAIL && resultCode == RESULT_OK) {
            String msg = data.getStringExtra("msg");
            String email = data.getStringExtra("email");
            if (msg != null && !msg.isEmpty())
                tv_sent_msg.setText(msg);
            rl_forget_mail_sent.setVisibility(View.VISIBLE);
        }
//                else if (requestCode == RC_SIGN_IN) {
//                        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//                        // The Task returned from this call is always completed, no need to attach
//                        // a listener.
//                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                        //handleSignInResult(task);
//                        try {
//                                // Google Sign In was successful, authenticate with Firebase
//                                GoogleSignInAccount account = task.getResult(ApiException.class);
//                                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
//                                firebaseAuthWithGoogle(account.getIdToken());
//                        } catch (ApiException e) {
//                                // Google Sign In failed, update UI appropriately
//                                Log.w(TAG, "Google sign in failed", e);
//                                hideLoader();
//                                // ...
//                        }
//                }
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

    @Override
    protected void onResume() {
        super.onResume();
//        playVideo();
        hideLoader();
        root.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideLoader();
        root.setVisibility(View.GONE);
    }

    @OnClick(R.id.rl_sent_ok)
    public void sentMailOk() {
        rl_forget_mail_sent.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                if (!isFinishing()) Utilities.hideSoftKeyboard(LoginActivity.this);
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



