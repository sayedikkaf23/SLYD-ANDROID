package chat.hola.com.app.user_verification.phone;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.authentication.newpassword.NewPasswordActivity;
import chat.hola.com.app.authentication.signup.CongratulationsActivity;
import chat.hola.com.app.authentication.signup.PrivacySettingsActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.models.Register;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.webScreen.WebActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class VerifyNumberOTPActivity extends DaggerAppCompatActivity
        implements VerifyNumberContract.View, View.OnFocusChangeListener, TextWatcher {

    @BindView(R.id.tV_msg)
    TextView tV_msg;
    @BindView(R.id.text_login_in)
    TextView text_login_in;
    @BindView(R.id.text_subtext)
    TextView text_subtext;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tV_timer)
    TextView tV_timer;
    @BindView(R.id.et_otp)
    EditText et_otp;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.tV_resend)
    TextView tV_resend;
    @BindView(R.id.rL_next)
    RelativeLayout rL_next;

    @BindView(R.id.etOtp)
    EditText etOtp;

    @BindView(R.id.et_otp1)
    EditText et_otp1;
    @BindView(R.id.et_otp2)
    EditText et_otp2;
    @BindView(R.id.et_otp3)
    EditText et_otp3;
    @BindView(R.id.et_otp4)
    EditText et_otp4;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    //    @BindView(R.id.videoView)
//    VideoView videoView;
    @BindView(R.id.btn_verify)
    Button btn_verify;

    @BindView(R.id.rel_btn_next)
    RelativeLayout rel_btn_next;

    @BindView(R.id.btn_next)
    Button btn_next;


    Unbinder unbinder;
    @Inject
    VerifyNumberContract.Presenter presenter;

    String phoneNumber, email, countryCode, country;
    private CountDownTimer cTimer;
    private boolean isNumberVisible = false;
    private boolean isEmailVisible = false;
    private boolean isLogin = false;
    private boolean isFromSignUp = false;
    private boolean isForgotPassword = false;

    private String otpId;
    private String countryCodeName;

    private Register register;
    private UploadFileAmazonS3 uploadFileAmazonS3;
    private String profilePic;
    //private Loader dialog;
    private String call;
    private static Bus bus = AppController.getBus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Utilities.initSmsRetriever(this);
        bus.register(this);
        unbinder = ButterKnife.bind(this);
        uploadFileAmazonS3 = new UploadFileAmazonS3(this);
        //dialog = new Loader(this);

        initViews();
        etOtp.requestFocus();

        tV_resend.setVisibility(View.GONE);
        if (getIntent() != null) {
            countryCodeName = getIntent().getStringExtra("countryCodeName");
            call = getIntent().getStringExtra("call");
            phoneNumber = getIntent().getStringExtra("phoneNumber");
            countryCode = getIntent().getStringExtra("countryCode");
            country = getIntent().getStringExtra("country");
            isNumberVisible = getIntent().getBooleanExtra("isNumberVisible", false);
           /* String msg = getString(R.string.verification_code_sent_phone)
                    + " \""
                    + countryCode
                    + ""
                    + phoneNumber
                    + "\" "
                    + getString(R.string.please_enter_code_below);*/
            String msg = getString(R.string.verification_code_sent_phone);
            isFromSignUp = getIntent().getBooleanExtra("isFromSignUp", false);
            tV_msg.setText(msg);
            tv_number.setVisibility(View.VISIBLE);
            tv_number.setText(countryCode + "" + phoneNumber);
            isLogin = getIntent().getBooleanExtra("isLogin", false);
            isForgotPassword = getIntent().getBooleanExtra("isForgotPassword", false);

            profilePic = getIntent().getStringExtra("profilePic");
            register = (Register) getIntent().getSerializableExtra("register");
            uploadFileAmazonS3 = new UploadFileAmazonS3(this);

            if(isFromSignUp){
                btn_verify.setVisibility(View.GONE);
                rel_btn_next.setVisibility(View.VISIBLE);
                text_login_in.setText(getString(R.string.sign_up));
                text_subtext.setText(getString(R.string.signup_otp_text));
            }
        }

        /*if (BuildConfig.DEBUG || ApiOnServer.ALLOW_DEFAULT_OTP) {
            et_otp.setText(getString(R.string.text_1111));
        }
*/
        if (isFromSignUp) {
            Map<String, Object> params = new HashMap<>();
            params.put("deviceId", AppController.getInstance().getDeviceId());
            params.put("phoneNumber", phoneNumber);
            params.put("countryCode", countryCode);
            params.put("type", 1);
            params.put("development", BuildConfig.DEBUG);
            params.put("hashKey", Utilities.getHashCode(this));
            presenter.makeReqToNumber(params);
        }

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
                tV_timer.setVisibility(View.GONE);
            }
        };

        startTimer();
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

        /*
         * Bug Title: hard code “1111” for verification Code
         * Bug Id: #2690
         * Fix Desc: set text
         * Fix Dev: Hardik, raised by rahul sir
         * Fix Date: 22/6/21
         * */
        if (ApiOnServer.ALLOW_DEFAULT_OTP) {
            et_otp1.setText("1");
            et_otp2.setText("1");
            et_otp3.setText("1");
            et_otp4.setText("1");
        }

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

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(boolean show) {
        if (!isFinishing()) {
            try {
                if (progress_bar != null)
                    progress_bar.setVisibility(show ? View.VISIBLE : View.GONE);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void numberNotRegistered(String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(message + "\n\nDo you want to SignUp with this number ?");
        builder.setPositiveButton("SignUp", (dialog, which) -> {
            Intent intent = new Intent(VerifyNumberOTPActivity.this, WebActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("url", getResources().getString(R.string.privacyPolicyUrl));
            bundle.putString("title", getResources().getString(R.string.privacyPolicy));
            bundle.putString("action", "accept");
            startActivity(intent.putExtra("url_data", bundle));
            finish();
        });
        builder.setNegativeButton("Change", (dialog, which) -> {
            super.onBackPressed();
            finish();
        });
        builder.show();
    }

    @Override
    public void openSuccessDialog() {
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

        imageView.setImageDrawable(getDrawable(R.drawable.ic_phone_number_verified));
        textView.setText(getString(R.string.verified_phone_num_msg));

        rL_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent data = new Intent();
                data.putExtra("numberVerified", true);
                setResult(Constants.Verification.NUMBER_VERIFICATION_REQ, data);
                onBackPressed();
            }
        });

        dialog.show();
    }

    @Override
    public void newPassword() {
        startActivity(new Intent(this, NewPasswordActivity.class).putExtra("countryCode", countryCode)
                .putExtra("phoneNumber", phoneNumber));
        finish();
    }

    @Override
    public void registered(Login.LoginResponse response, boolean isSignUp) {
        if (isSignUp) {
            Intent intent = new Intent(this, CongratulationsActivity.class);
            startActivity(intent);
//            Intent i = new Intent(this, DiscoverActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            i.putExtra("caller", "SaveProfile");
//            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(this, LandingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
            supportFinishAfterTransition();
        }
    }

    @Override
    public void startTimer() {
        cTimer.start();
    }

    @Override
    public void businessPhoneVerified() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void showNext() {
//        if (rL_next != null) rL_next.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iV_back)
    public void back() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        cTimer.cancel();
        if (bus != null)
            bus.unregister(this);
        super.onDestroy();
    }

//    @OnClick(R.id.btn_next)
//    public void onNextClick(){
//        Intent intent = new Intent(this, PrivacySettingsActivity.class);
//        startActivity(intent);
//    }

    @OnClick({R.id.rL_next,R.id.btn_verify,R.id.btn_next})
    public void verify() {

        //OTP number
//        String otpNumber = (et_otp1.getText().toString() + "" + et_otp2.getText().toString() + "" +
//                et_otp3.getText().toString() + "" + et_otp4.getText().toString());

        String otpNumber = etOtp.getText().toString();

//        if (!et_otp1.getText().toString().isEmpty() && !et_otp2.getText().toString().isEmpty()
//                && !et_otp3.getText().toString().isEmpty() && !et_otp4.getText().toString().isEmpty()) {
            if (!etOtp.getText().toString().isEmpty()) {
            if (call != null && call.equals(Constants.BUSINESS)) {
                presenter.verifyBusinessPhoneNumber(otpNumber, countryCode, phoneNumber);
            } else {
                //                if (!BuildConfig.DEBUG)
                //                    cTimer.start();

                Login login = new Login();
                login.setOtp(otpNumber);
                login.setDeviceId(AppController.getInstance().getDeviceId());
                login.setPhoneNumber(phoneNumber);
                if (phoneNumber.equalsIgnoreCase("")) {
                    login.setCountryCode("");
                } else {
                    login.setCountryCode(countryCode);
                }
                login.setCountry(country);
                login.setDeviceName(Build.DEVICE);
                login.setDeviceOs(Build.VERSION.RELEASE);
                login.setModelNumber(Build.MODEL);
                login.setDeviceType("2"); // here 2= android
                try {
                    PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;
                    login.setAppVersion(version);
                } catch (PackageManager.NameNotFoundException e) {
                    login.setAppVersion("123");
                }
                login.setType(
                        isLogin || isFromSignUp ? 1 : 2); // here type 1 for verify user, 2 for star user verify
                int move = 0;
                if (isFromSignUp) {
                    login.setIgnUp(true);
                    move = 1;
                } else if (isLogin) {
                    move = 2;
                } else if (isForgotPassword) {
                    move = 3;
                } else {
                    openSuccessDialog();
                }

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
                    register.setCountry(country);
                    register.setCountryCodeName(countryCodeName);
                }
                presenter.verifyPhoneNumber(login, move, register, uploadFileAmazonS3,
                        profilePic != null ? new File(profilePic) : null);
            }
        } else {
            showMessage("Enter verification code that sent to your register phone number");
        }
    }

    @OnClick(R.id.tV_resend)
    public void resend() {
//        rL_next.setVisibility(View.GONE);
        tV_timer.setVisibility(View.VISIBLE);
        if (call != null && call.equals(Constants.BUSINESS)) {
            //TODO verify business mobile number
        } else {
            et_otp1.getText().clear();
            et_otp2.getText().clear();
            et_otp3.getText().clear();
            et_otp4.getText().clear();
            Map<String, Object> params = new HashMap<>();
            params.put("deviceId", AppController.getInstance().getDeviceId());
            params.put("phoneNumber", phoneNumber);
            params.put("countryCode", countryCode);
            params.put("type", 1);
            params.put("development", BuildConfig.DEBUG);
            params.put("hashKey", Utilities.getHashCode(this));
            presenter.makeReqToNumber(params);
        }
    }

    @Subscribe
    public void getMessage(JSONObject obj) {
        try {
            if (obj.getString("eventName").equals("OTP_RECEIVED")) {
                //  et_otp.setText(obj.getString("otp"));

                //splitting and displaying OTP Number into editText
                String otpNumber = obj.getString("otp");
                String splitted[] = otpNumber.split(" ");
                if (splitted[0].length() == 4) {
                    String one = splitted[0].charAt(0) + "";
                    String tw = splitted[0].charAt(1) + "";
                    String thr = splitted[0].charAt(2) + "";
                    String fur = splitted[0].charAt(3) + "";
                    et_otp1.setText(one);
                    et_otp2.setText(tw);
                    et_otp3.setText(thr);
                    et_otp4.setText(fur);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
        if (s == et_otp1.getEditableText()) {
            if (et_otp1.getText().toString().length() == 1)
                et_otp2.requestFocus();
        } else if (s == et_otp2.getEditableText()) {
            if (et_otp2.getText().toString().length() == 1)
                et_otp3.requestFocus();
        } else if (s == et_otp3.getEditableText()) {
            if (et_otp3.getText().toString().length() == 1)
                et_otp4.requestFocus();
        } else if (s == et_otp4.getEditableText()) {
            if (et_otp4.getText().toString().length() == 1) {

            }
            //methodToVerifyOTP();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    protected void onResume() {
        super.onResume();
//        playVideo();
//        root.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        root.setVisibility(View.GONE);
    }
}