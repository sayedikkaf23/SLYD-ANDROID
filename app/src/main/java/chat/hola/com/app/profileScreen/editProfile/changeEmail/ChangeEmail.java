package chat.hola.com.app.profileScreen.editProfile.changeEmail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h>ChangeEmail</h>
 * <p>
 * This Activity provide editing of user email address.
 *
 * @author 3Embed.
 * @since 19/3/18.
 */

public class ChangeEmail extends DaggerAppCompatActivity implements ChangeEmailContract.View {

    @Inject
    ChangeEmailPresenter presenter;

    @Inject
    TypefaceManager typefaceManager;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.etEmail)
    EditText etEmail;

    private Unbinder unbinder;
    @Inject
    SessionManager sessionManager;
    InputMethodManager imm;
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }
    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_change_email);
        unbinder = ButterKnife.bind(this);
        presenter.init();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        String email = bundle.getString("email");
        etEmail.setText(email);
        etEmail.setSelection(etEmail.getText().length());
        etEmail.requestFocus();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etEmail, InputMethodManager.SHOW_FORCED);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null && !msg.isEmpty()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else if (msgId != 0) {
            Toast.makeText(this, getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void applyFont() {
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        etEmail.setTypeface(typefaceManager.getMediumFont());
    }

    @OnClick(R.id.ivBack)
    public void back() {
        /*Intent intent = new Intent();
        intent.putExtra("email",etEmail.getText().toString());
        setResult(RESULT_OK, intent);*/
        setResult(0, null);
        etEmail.clearFocus();
        onBackPressed();
    }

    @OnClick(R.id.ivDone)
    public void Done() {
        //we need to validate then set
        if (presenter.isEmailValidated(etEmail.getText().toString())) {
            imm.showSoftInput(etEmail, InputMethodManager.HIDE_NOT_ALWAYS);
            Intent intent = new Intent();
            intent.putExtra("email", etEmail.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
        //onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void reload() {

    }
}
