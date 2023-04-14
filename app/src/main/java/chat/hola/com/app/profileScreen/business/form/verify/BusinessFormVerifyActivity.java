package chat.hola.com.app.profileScreen.business.form.verify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.NumberVerification.ChooseCountry;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.base.BaseActivity;

public class BusinessFormVerifyActivity extends BaseActivity implements BusinessFormVerifyContract.View {

    private static final int MAX_NAME_LIMIT = 3;
    private static final int FETCH_ADDRESS = 10011;
    private static final int FETCH_CATEGORY = 10012;
    private static final int FETCH_COUNTRY = 10013;
    private static final int MAX_PHONE_LIMIT = 15;

    @Inject
    BusinessFormVerifyPresenter presenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnSave)
    Button btnSave;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etBusinessName)
    TextInputEditText etBusinessName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tilBusinessName)
    TextInputLayout tilBusinessName;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etBusinessUserName)
    TextInputEditText etBusinessUserName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tilBusinessUserName)
    TextInputLayout tilBusinessUserName;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etBusinessBio)
    TextInputEditText etBusinessBio;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.titleBusinessBio)
    TextInputLayout titleBusinessBio;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etBusinessWebsite)
    TextInputEditText etBusinessWebsite;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.titleBusinessWebsite)
    TextInputLayout titleBusinessWebsite;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etBusinessEmail)
    TextInputEditText etBusinessEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.titleBusinessEmail)
    TextInputLayout titleBusinessEmail;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rlPhone)
    RelativeLayout rlPhone;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvRegion)
    TextView tvRegion;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etPhone)
    EditText etPhone;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvTitleMobile)
    TextView tvTitleMobile;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvError)
    TextView tvError;

    private String businessUniqueId;
    private String key;
    private String value;
    private String newValue;
    private String countryCode;
    private String cCode;
    private boolean verified;

    private Loader loader;
    private InputMethodManager imm;
    private EditText curruntFocusedOn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_business_form_verify;
    }

    @Override
    public void initView() {
        super.initView();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        presenter.attach(this);
        loader = new Loader(this);
        tvTitle.setText(getString(R.string.edit_detail));

        businessUniqueId = getIntent().getStringExtra("businessUniqueId");
        key = getIntent().getStringExtra("key");
        value = getIntent().getStringExtra("value");

        countryCode = getIntent().getStringExtra("countryCode");
        verified = getIntent().getBooleanExtra("verified", false);

        showFiled(key);
        textChange();
    }

    /**
     * Sets value and validates data based on @key
     *
     * @param key : fieldValued to change/ update
     */
    private void showFiled(String key) {
        switch (key) {
            case "businessName":
                tvTitle.setText(Objects.requireNonNull(tilBusinessName.getHint()).toString());
                tilBusinessName.setVisibility(View.VISIBLE);
                etBusinessName.setText(value);
                etBusinessName.setSelection(etBusinessName.getText().length());
                showKeyboard(etBusinessName);
                break;
            case "businessUsername":
                tvTitle.setText(Objects.requireNonNull(tilBusinessUserName.getHint()).toString());
                tilBusinessUserName.setVisibility(View.VISIBLE);
                etBusinessUserName.setText(value);
                etBusinessUserName.setSelection(etBusinessUserName.getText().length());
                showKeyboard(etBusinessUserName);
                break;
            case "businessBio":
                tvTitle.setText(Objects.requireNonNull(titleBusinessBio.getHint()).toString());
                titleBusinessBio.setVisibility(View.VISIBLE);
                etBusinessBio.setText(value);
                etBusinessBio.setSelection(etBusinessBio.getText().length());
                showKeyboard(etBusinessBio);
                break;
            case "businessWebsite":
                tvTitle.setText(Objects.requireNonNull(titleBusinessWebsite.getHint()).toString());
                titleBusinessWebsite.setVisibility(View.VISIBLE);
                etBusinessWebsite.setText(value);
                if (value.isEmpty()) {
                    etBusinessWebsite.setText(getString(R.string.www));
                    etBusinessWebsite.setSelection(etBusinessWebsite.getText().length());
                }
                showKeyboard(etBusinessWebsite);
                break;
            case "businessEmail":
                tvTitle.setText(Objects.requireNonNull(titleBusinessEmail.getHint()).toString());
                titleBusinessEmail.setVisibility(View.VISIBLE);
                etBusinessEmail.setText(value);
                etBusinessEmail.setSelection(etBusinessEmail.getText().length());
                showKeyboard(etBusinessEmail);
                break;
            case "businessPhone":
                etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PHONE_LIMIT)});
                tvTitle.setText(Objects.requireNonNull(tvTitleMobile.getText()).toString());
                rlPhone.setVisibility(View.VISIBLE);

                tvRegion.setText(countryCode);
                etPhone.setText(value);
                etPhone.setSelection(etPhone.getText().length());
                showKeyboard(etPhone);
                break;
        }
    }

    /**
     * To perform action based on text change
     */
    private void textChange() {
        //business username
        etBusinessUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newValue = s.toString();
                if (newValue.length() > 3)
                    presenter.verifyIsUserNameRegistered(newValue);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //business name
        etBusinessName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSave(!newValue.trim().equals(value) && newValue.length() > MAX_NAME_LIMIT);
            }
        });

        //business description
        etBusinessBio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSave(!newValue.trim().equals(value) && newValue.length() > 10);
            }
        });

        //business website
        etBusinessWebsite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utilities.isValidWebsite(newValue)) {
                    enableSave(!newValue.trim().equals(value) && newValue.length() > 10);
                }
            }
        });

        //business email
        etBusinessEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newValue = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utilities.isValidEmail(newValue)) {
                    presenter.verifyIsEmailRegistered(newValue);
                }
            }
        });

        //business email
        etPhone.addTextChangedListener(new TextWatcher() {
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
                    presenter.verifyIsPhoneRegistered(countryCode, newValue);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        hideKeyBoard();
        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
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
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader.isShowing())
            loader.dismiss();
    }

    @Override
    public void userNameAvailable(boolean b) {
        tilBusinessUserName.setError(b ? null : getString(R.string.usernameWarning));
        enableSave(b);
    }

    @Override
    public void businessPhoneAvailable(boolean b) {
        tvError.setText(b ? "" : getString(R.string.phoneNumberWarning));
        enableSave(b);
    }

    @Override
    public void businessEmailAvailable(boolean b) {
        titleBusinessEmail.setError(b ? null : getString(R.string.emailWarning));
        enableSave(b);
    }

    /**
     * To open keyboard
     *
     * @param editText : edit text reference
     */
    private void showKeyboard(EditText editText) {
        this.curruntFocusedOn = editText;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            curruntFocusedOn.requestFocus(curruntFocusedOn.getText().length());
            if (imm != null) {
                imm.showSoftInput(curruntFocusedOn, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }

    /**
     * To hide keyboard
     */
    private void hideKeyBoard() {
        if (imm != null && imm.isActive() && getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnSave)
    public void save() {
        hideKeyBoard();
        Intent intent = new Intent();
        intent.putExtra(key, newValue);
        if ("businessPhone".equals(key)) {
            verified = false;
            intent.putExtra("countryCode", countryCode);
            intent.putExtra("businessPhone", newValue);
            intent.putExtra("verified", verified);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * To show and hide Save button
     *
     * @param enable : true= show, false= hide
     */
    private void enableSave(boolean enable) {
        btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvRegion)
    public void selectRegion() {
        Intent intent = new Intent(this, ChooseCountry.class);
        startActivityForResult(intent, FETCH_COUNTRY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            if (requestCode == FETCH_COUNTRY) {
                String code = data.getStringExtra("CODE");
                assert code != null;
                cCode = "+" + code.substring(1);
                tvRegion.setText(cCode);

                boolean isSame = countryCode.trim().equals(cCode.trim());

                if (!isSame)
                    btnSave.setText(getString(R.string.save_and_verify));
                if (cCode != null && !cCode.isEmpty())
                    countryCode = cCode;
                if (newValue == null || newValue.isEmpty())
                    newValue = value;

                presenter.verifyIsPhoneRegistered(countryCode, newValue);
            }
    }

}
