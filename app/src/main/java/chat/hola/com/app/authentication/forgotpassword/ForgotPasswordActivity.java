package chat.hola.com.app.authentication.forgotpassword;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.user_verification.VerifyEmailOTPActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class ForgotPasswordActivity extends DaggerAppCompatActivity implements ForgotPasswordContract.View {

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    ForgotPasswordContract.Presenter presenter;

    @BindView(R.id.ivClose)
    ImageView ivClose;

    @BindView(R.id.eT_email)
    EditText etEmail;
    @BindView(R.id.tV_title)
    TextView tV_title;

    @BindView(R.id.ll_forgotpswd_UI)
    LinearLayout ll_forgotpswd_UI;
    @BindView(R.id.ll_Otp_sent_UI)
    RelativeLayout ll_Otp_sent_UI;
    @BindView(R.id.tv_otp_text)
    TextView tv_otp_text;
    @BindView(R.id.tv_registered)
    TextView tv_registered;
    @BindView(R.id.rl_otp_send)
    RelativeLayout rl_otp_enter;

    private InputMethodManager imm;
    private String phone, email, userName;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        phone = getIntent().getStringExtra("phone");
        userName = getIntent().getStringExtra("userName");
        email = getIntent().getStringExtra("email");
        if(email!=null && !email.equalsIgnoreCase("")){
            etEmail.setText(email);
            //send();
        }
        etEmail.setTypeface(typefaceManager.getRegularFont());
        // showKeyboard();

//        tV_title.setTypeface(se);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }


    @OnClick({R.id.iV_back,R.id.ivClose})
    public void back() {
        hideKeyBoard();
        super.onBackPressed();
    }


    //opening emial OTP screen
    @OnClick(R.id.rl_otp_send)
    public void openEmailOtpScreen() {
        tv_otp_text.setText(getString(R.string.resetPasswordsent));
        ll_Otp_sent_UI.setVisibility(View.GONE);
//        startActivity(new Intent(this, VerifyEmailOTPActivity.class).putExtra("isLogin", true).
//                putExtra("email", email).putExtra("userName", userName));
    }

    @OnClick(R.id.rL_next)
    public void send() {
        hideKeyBoard();
        tv_registered.setVisibility(View.GONE);
        email = etEmail.getText().toString().trim();
        if (Utilities.isValidEmail(email)) {
            presenter.verifyIsEmailRegistered(email);

//            presenter.forgotPassword(userName, phone, email);
        } else {
            message("Please enter valid email address");
        }
    }

    @Override
    public void emailRegisterd(String email) {
        presenter.forgotPassword(email);
    }

    @Override
    public void emailNotRegisterd() {
        tv_registered.setVisibility(View.VISIBLE);
    }

    @Override
    public void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success() {
        Intent intent = new Intent();
        intent.putExtra("msg","");// if required then pass here
        intent.putExtra("email","");// if required then pass here
        setResult(RESULT_OK,intent);
        finish();
        //on Success hide email enter Ui and show confirmation UI
        ll_forgotpswd_UI.setVisibility(View.VISIBLE);
        ll_Otp_sent_UI.setVisibility(View.GONE);
        tv_otp_text.setText(getString(R.string.resetPasswordsent));

        // hiding hear adding this on click of ok buuton in OTP sent ui
        /*startActivity(new Intent(this, VerifyEmailOTPActivity.class).putExtra("isLogin", true).
                putExtra("email", email).putExtra("userName", userName));*/
    }


    @Override
    public void progress(boolean b) {
       /* if (b)
            dialog.show();
        else
            dialog.dismiss();*/
    }

    private void showKeyboard() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                etEmail.requestFocus();
                if (imm != null) {
                    imm.showSoftInput(etEmail, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    private void hideKeyBoard() {
        if (imm != null)
            imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
    }

}
