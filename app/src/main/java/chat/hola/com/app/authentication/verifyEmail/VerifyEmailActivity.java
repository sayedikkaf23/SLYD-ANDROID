package chat.hola.com.app.authentication.verifyEmail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.authentication.newpassword.NewPasswordActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class VerifyEmailActivity extends DaggerAppCompatActivity implements VerifyEmailContract.View {

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    VerifyEmailContract.Presenter presenter;

    @BindView(R.id.et_otp)
    EditText et_otp;
    @BindView(R.id.tV_resend)
    TextView tV_resend;


    private String phone, email, userName;
    private Dialog dialog;
    private String call;
    private boolean isVisible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        ButterKnife.bind(this);

        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        userName = getIntent().getStringExtra("userName");
        call = getIntent().getStringExtra("call");
        isVisible = getIntent().getBooleanExtra("isEmailVisible", false);

        if (BuildConfig.DEBUG || ApiOnServer.ALLOW_DEFAULT_OTP)
            et_otp.setText(getString(R.string.text_1111));

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        starCounter();
    }


    @OnClick(R.id.iV_back)
    public void back() {
        super.onBackPressed();
    }


    @OnClick(R.id.rL_next)
    public void verify() {
        String otp = et_otp.getText().toString();
        if (!otp.isEmpty() && otp.length() == 4) {
            if (call != null && call.equals(Constants.BUSINESS)) {
                presenter.verifyBusinessEmail(otp, email, isVisible);
            } else {
                presenter.verify(otp, email);
            }
        } else
            message("Invalid verification code");
    }

    @OnClick(R.id.tV_resend)
    public void resend() {
        if (call != null && call.equals(Constants.BUSINESS)) {
            presenter.businessEmailVerificationCode(email);
        } else {
            presenter.resend(phone, email);
        }
        starCounter();
    }

    @Override
    public void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success() {

    }

    private void starCounter() {
        tV_resend.setVisibility(View.VISIBLE);
        tV_resend.setEnabled(false);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tV_resend.setText(getString(R.string.string_296) + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tV_resend.setText(getString(R.string.string_724));
                tV_resend.setEnabled(true);
            }

        }.start();
    }

    @Override
    public void progress(boolean b) {
        if (b)
            dialog.show();
        else
            dialog.dismiss();
    }

    @Override
    public void otpVerified() {
        startActivity(new Intent(this, NewPasswordActivity.class).putExtra("email", email).putExtra("userName", userName));
        finish();
    }

    @Override
    public void businessEmailVerified() {
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }
}
