package chat.hola.com.app.authentication.newpassword;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.authentication.login.LoginActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class NewPasswordActivity extends DaggerAppCompatActivity implements NewPasswordContract.View {


    @Inject
    TypefaceManager typefaceManager;
    @Inject
    NewPasswordContract.Presenter presenter;



    @BindView(R.id.etOldPasswordLayout)
    TextInputLayout etOldPasswordLayout;
    @BindView(R.id.etOldPassword)
    EditText etOldPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    private InputMethodManager imm;
    private String call, email = "", countryCode, phoneNumber, password, userName;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvCenterCategoryName)
    TextView tvHeaderTitle;
    @BindView(R.id.tvOldPasswordWarning)
    TextView tvOldPasswordWarning;

    boolean changePassword = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_password_activity);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        call = getIntent().getStringExtra("call");
        email = getIntent().getStringExtra("email");
        userName = getIntent().getStringExtra("userName");
        countryCode = getIntent().getStringExtra("countryCode");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        if (call != null && call.equalsIgnoreCase("setting")) {
            tvHeaderTitle.setText(getString(R.string.change_password));
            etOldPassword.requestFocus();
        } else {
            tvHeaderTitle.setText(getString(R.string.reset_password));
            etNewPassword.requestFocus();
        }

        if (call != null && call.equalsIgnoreCase("setting")) {
            etOldPasswordLayout.setVisibility(View.VISIBLE);
        } else {
            etOldPasswordLayout.setVisibility(View.GONE);
        }

        etNewPassword.setTypeface(typefaceManager.getRegularFont());
        etConfirmPassword.setTypeface(typefaceManager.getRegularFont());
        // showKeyboard();
    }

    @OnClick({R.id.iV_back, R.id.ivBack})
    public void back() {
        hideKeyBoard();
        super.onBackPressed();
    }

    @OnClick(R.id.rL_next)
    public void resend() {

        if (validate()) {
            Map<String, String> params = new HashMap<>();
            params.put("password", password);
            params.put("confirmPassword", password);
            if (userName != null && !userName.isEmpty()) {
                params.put("userName", userName);
                presenter.changePassword(params);
            } else if (email != null && !email.isEmpty()) {
                params.put("email", email);
                presenter.changePassword(params);
            } else if (phoneNumber != null && !phoneNumber.isEmpty()) {
                params.put("countryCode", countryCode);
                params.put("phoneNumber", phoneNumber);
                presenter.changePassword(params);
            } else if (call != null && call.equalsIgnoreCase("setting") && changePassword==true){
                presenter.updatePassword(password);
            }
        }
    }

    private boolean validate() {
        String newPwd = etNewPassword.getText().toString();
        String cnfrmPwd = etConfirmPassword.getText().toString();
        if (newPwd.isEmpty()) {
            message("Please enter new password");
            return false;
        }

        if (newPwd.length() < 6) {
            message("Please enter at least 6 letters of password");
            return false;
        }

        if (!newPwd.equals(cnfrmPwd)) {
            message("The confirm password does not match the password set");
            return false;
        }

        password = newPwd;
        return true;
    }

    @Override
    public void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void message(int message) {
        message(getString(message));
    }

    @Override
    public void showLoader() {
        // loader.show();
    }

    @Override
    public void hideLoader() {
        /*if (loader.isShowing())
            loader.dismiss();*/
    }

    @Override
    public void success() {
        hideKeyBoard();
//        if (email != null && !email.isEmpty()) {
//        message("Password changed successfully, please login again");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("Password changed successfully.")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //startActivity(new Intent(NewPasswordActivity.this, LoginActivity.class).putExtra("call", call));
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
//        } else {

//        }
    }

    @Override
    public void passwordMatched() {
        changePassword = true;
        tvOldPasswordWarning.setVisibility(View.INVISIBLE);
    }

    @Override
    public void passwordNotMatched() {
        tvOldPasswordWarning.setVisibility(View.VISIBLE);
        tvOldPasswordWarning.setText(getString(R.string.invalid_password));
        // etOldPassword.setError(getString(R.string.invalid_password));
    }

    private void showKeyboard() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                etNewPassword.requestFocus();
                if (imm != null) {
                    imm.showSoftInput(etNewPassword, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    private void hideKeyBoard() {
        if (imm != null) {
            imm.hideSoftInputFromWindow(etNewPassword.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(etConfirmPassword.getWindowToken(), 0);
        }
    }



    @OnFocusChange(R.id.etOldPassword)
    void onFocusChangeEvent(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.etOldPassword:
                if(!hasFocus) {
                    String password = etOldPassword.getText().toString();
                    if (password.equalsIgnoreCase("")) {
                        // etOldPassword.setError(getString(R.string.enter_password));
                        tvOldPasswordWarning.setVisibility(View.VISIBLE);
                        tvOldPasswordWarning.setText(getString(R.string.enter_password));
                    } else {
                        presenter.checkPassword(password);
                    }
                }
                break;
        }
    }
}
