package chat.hola.com.app.profileScreen.editProfile.editDetail;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.NumberVerification.ChooseCountry;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.profileScreen.business.address.BusinessAddressActivity;
import chat.hola.com.app.profileScreen.business.category.BusinessCategoryActivity;
import chat.hola.com.app.star_configuration.VerificationActivity;

/**
 * This screen performs activity to edit details of user profile
 *
 * @author Shaktisinh Jadeja
 * @since 17th May 2021
 */

public class EditDetailActivity extends BaseActivity implements EditDetailContract.View {

    private static final int MAX_NAME_LIMIT = 3;
    private static final int FETCH_ADDRESS = 10011;
    private static final int FETCH_CATEGORY = 10012;
    private static final int FETCH_COUNTRY = 10013;
    private static final int MAX_PHONE_LIMIT = 15;

    @Inject
    EditDetailPresenter presenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.layoutAlert)
    View layoutAlert;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvMessageTitle)
    TextView tvMessageTitle;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvMessage)
    TextView tvMessage;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnSave)
    Button btnSave;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnVerify)
    Button btnVerify;

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
    @BindView(R.id.etBusinessAddress)
    TextInputEditText etBusinessAddress;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.titleBusinessAddress)
    TextInputLayout titleBusinessAddress;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etBusinessCategory)
    TextInputEditText etBusinessCategory;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.titleBusinessCategory)
    TextInputLayout titleBusinessCategory;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etBusinessEmail)
    TextInputEditText etBusinessEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.titleBusinessEmail)
    TextInputLayout titleBusinessEmail;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etEmail)
    TextInputEditText etEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.titleEmail)
    TextInputLayout titleEmail;

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
    private String city, street, zipcode;
    private String categoryId;
    private String countryCode;
    private String cCode;
    private boolean verified;
    private Map<String, Object> map;
    private Loader loader;
    private InputMethodManager imm;
    private EditText curruntFocusedOn;
    private boolean iSBusiness;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_detail;
    }

    @Override
    public void initView() {
        super.initView();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        presenter.attach(this);
        map = new HashMap<>();
        loader = new Loader(this);
        tvTitle.setText(getString(R.string.edit_detail));

        businessUniqueId = getIntent().getStringExtra("businessUniqueId");
        key = getIntent().getStringExtra("key");
        value = getIntent().getStringExtra("value");
        city = getIntent().getStringExtra("city");
        street = getIntent().getStringExtra("street");
        zipcode = getIntent().getStringExtra("zipcode");
        categoryId = getIntent().getStringExtra("categoryId");
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
                etBusinessWebsite.setSelection(etBusinessWebsite.getText().length());
                showKeyboard(etBusinessWebsite);
                break;
            case "businessAddress":
                tvTitle.setText(Objects.requireNonNull(titleBusinessAddress.getHint()).toString());
                titleBusinessAddress.setVisibility(View.VISIBLE);
                etBusinessAddress.setText(value);
                break;
            case "businessCategory":
                tvTitle.setText(Objects.requireNonNull(titleBusinessCategory.getHint()).toString());
                titleBusinessCategory.setVisibility(View.VISIBLE);
                etBusinessCategory.setText(value);
                break;
            case "businessEmail":
                tvTitle.setText(Objects.requireNonNull(titleBusinessEmail.getHint()).toString());
                titleBusinessEmail.setVisibility(View.VISIBLE);
                etBusinessEmail.setText(value);
                etBusinessEmail.setSelection(etBusinessEmail.getText().length());
                showKeyboard(etBusinessEmail);

                titleBusinessEmail.setError(getString(verified ? R.string.verified : R.string.not_verified));
                if (verified) titleBusinessEmail.setErrorTextAppearance(R.style.inputErrorGreen);
                enableVerified(!value.isEmpty() && !verified);
                break;
            case "email":
                tvTitle.setText(Objects.requireNonNull(titleEmail.getHint()).toString());
                titleEmail.setVisibility(View.VISIBLE);
                etEmail.setText(value);
                etEmail.setSelection(etEmail.getText().length());
                showKeyboard(etEmail);

                titleEmail.setError(getString(verified ? R.string.verified : R.string.not_verified));
                if (verified) titleEmail.setErrorTextAppearance(R.style.inputErrorGreen);
                enableVerified(!value.isEmpty() && !verified);
                break;
            case "businessPhone":
                iSBusiness = true;
                etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PHONE_LIMIT)});
                tvTitle.setText(Objects.requireNonNull(tvTitleMobile.getText()).toString());
                rlPhone.setVisibility(View.VISIBLE);

                tvRegion.setText(countryCode);
                etPhone.setText(value);
                etPhone.setSelection(etPhone.getText().length());
                showKeyboard(etPhone);

                enableVerified(!value.isEmpty() && !verified);
            case "mobile":
                iSBusiness = false;
                etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PHONE_LIMIT)});
                tvTitle.setText(getString(R.string.mobile_number));
                rlPhone.setVisibility(View.VISIBLE);

                tvRegion.setText(countryCode);
                etPhone.setText(value);
                etPhone.setSelection(etPhone.getText().length());
                showKeyboard(etPhone);

                enableVerified(!value.isEmpty() && !verified);
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
                map.put("businessName", newValue);
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
                map.put("businessBio", newValue);
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
                    map.put("businessWebsite", newValue);
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
                    presenter.verifyIsEmailRegistered(newValue, true);
                } else {
                    enableVerified(false);
                }
            }
        });

        //email
        etEmail.addTextChangedListener(new TextWatcher() {
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
                    presenter.verifyIsEmailRegistered(newValue, false);
                } else {
                    enableVerified(false);
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
                    presenter.verifyIsPhoneRegistered(countryCode, newValue, iSBusiness);
                } else {
                    enableVerified(false);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case FETCH_ADDRESS:
                    if (data != null) {
                        city = data.getStringExtra("city");
                        street = data.getStringExtra("street");
                        zipcode = data.getStringExtra("zipcode");
                        String lat = data.getStringExtra("lat");
                        String lng = data.getStringExtra("lng");

                        newValue = data.getStringExtra("address");
                        etBusinessAddress.setText(newValue);

                        map.put("businessAddress", newValue);
                        map.put("businessStreet", street);
                        map.put("businessCity", city);
                        map.put("businessZipCode", zipcode);
                        map.put("businessLat", lat);
                        map.put("businessLng", lng);
                        enableSave(true);
                    }
                    break;
                case FETCH_CATEGORY:
                    if (data != null) {
                        categoryId = data.getStringExtra("category_id");
                        newValue = data.getStringExtra("category");
                        etBusinessCategory.setText(newValue);

                        map.put("businessCategoryId", categoryId);
                        enableSave(!value.equals(newValue));
                    }
                    break;
                case FETCH_COUNTRY:
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

                    presenter.verifyIsPhoneRegistered(countryCode, newValue, iSBusiness);
//                    enableSave(!isSame);
//                    enableVerified(isSame);
                    break;
            }
    }

    /**
     * To show and hide Save button
     *
     * @param enable : true= show, false= hide
     */
    private void enableSave(boolean enable) {
        btnSave.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    /**
     * To show and hide Verify button
     *
     * @param enable : true= show, false= hide
     */
    private void enableVerified(boolean enable) {
        btnVerify.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tvRegion)
    public void selectRegion() {
        Intent intent = new Intent(this, ChooseCountry.class);
        startActivityForResult(intent, FETCH_COUNTRY);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.etBusinessCategory)
    public void category() {
        Intent intent = new Intent(this, BusinessCategoryActivity.class);
        intent.putExtra("category", categoryId);
        startActivityForResult(intent, FETCH_CATEGORY);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.etBusinessAddress)
    public void address() {
        Intent intent = new Intent(this, BusinessAddressActivity.class);
        intent.putExtra("city", city);
        intent.putExtra("street", street);
        intent.putExtra("zipcode", zipcode);
        startActivityForResult(intent, FETCH_ADDRESS);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnVerify)
    public void verify() {
        if (cCode != null && !cCode.isEmpty())
            countryCode = cCode;

        if (newValue == null || newValue.isEmpty())
            newValue = value;

        switch (key) {
            case "businessEmail":
                presenter.verifyBusinessEmail(newValue);
                break;
            case "email":
                presenter.verifyEmail(newValue);
                break;
            case "mobile":
                map.put("countryCode", countryCode);
                presenter.verifyPhone(countryCode, newValue);
                break;
            case "businessPhone":
                map.put("countryCode", countryCode);
                presenter.verifyBusinessPhone(countryCode, newValue);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnSave)
    public void save() {
        map.put("businessUniqueId", businessUniqueId);
        presenter.updateDetail(map);
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
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    public void userNameAvailable(boolean b) {
        tilBusinessUserName.setError(b ? null : getString(R.string.usernameWarning));
        map.put("businessUserName", newValue);
        enableSave(b);
    }

    @Override
    public void emailAvailable(boolean b) {
        if (b)
            btnSave.setText(getString(R.string.save_and_verify));
        titleEmail.setError(b ? null : getString(R.string.emailWarning));
        map.put("email", newValue);
        enableSave(b);
        enableVerified(false);
    }

    @Override
    public void phoneAvailable(boolean b) {
        if (b)
            btnSave.setText(getString(R.string.save_and_verify));
        tvError.setText(b ? "" : getString(R.string.phoneNumberWarning));
        map.put("mobile", newValue);
        map.put("countryCode", countryCode);
        enableSave(b);
        enableVerified(false);
    }

    @Override
    public void businessPhoneAvailable(boolean b) {
        if (b)
            btnSave.setText(getString(R.string.save_and_verify));
        tvError.setText(b ? "" : getString(R.string.phoneNumberWarning));
        map.put("businessPhone", newValue);
        map.put("businessCountryCode", countryCode);
        enableSave(b);
        enableVerified(false);
    }

    @Override
    public void businessEmailAvailable(boolean b) {
        if (b)
            btnSave.setText(getString(R.string.save_and_verify));
        titleBusinessEmail.setError(b ? null : getString(R.string.emailWarning));
        map.put("businessEmail", newValue);
        enableSave(b);
        enableVerified(false);
    }

    @Override
    public void success() {
        hideKeyBoard();
        Intent intent = new Intent();
        intent.putExtra(key, newValue);
        switch (key) {
            case "businessCategory":
                intent.putExtra("category_id", categoryId);
                intent.putExtra("category", newValue);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case "businessAddress":
                intent.putExtra("city", city);
                intent.putExtra("street", street);
                intent.putExtra("zipcode", zipcode);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case "businessEmail":
                verified = false;
                titleBusinessEmail.setError(getString(R.string.not_verified));
                intent.putExtra("businessEmail", newValue);
                intent.putExtra("verified", verified);
                setResult(RESULT_OK, intent);
                verify();
//                enableSave(false);
//                enableVerified(true);
                break;
            case "email":
                verified = false;
                titleEmail.setError(getString(R.string.not_verified));
                intent.putExtra("email", newValue);
                intent.putExtra("verified", verified);
                setResult(RESULT_OK, intent);
                verify();
                break;
            case "businessPhone":
                verified = false;
                intent.putExtra("countryCode", countryCode);
                intent.putExtra("businessPhone", newValue);
                intent.putExtra("verified", verified);
                setResult(RESULT_OK, intent);
                verify();
//                enableSave(false);
//                enableVerified(true);
                break;
            case "mobile":
                verified = false;
                intent.putExtra("countryCode", countryCode);
                intent.putExtra("mobile", newValue);
                intent.putExtra("verified", verified);
                setResult(RESULT_OK, intent);
                verify();
            default:
                message(getString(R.string.profileUpdated));
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }

    @Override
    public void businessEmailVerificationSent(String message) {
        layoutAlert.setVisibility(View.VISIBLE);
        tvMessageTitle.setText(getString(R.string.check_your_mail));
        tvMessage.setText(getString(R.string.check_your_mail_message));
    }

    @Override
    public void emailVerificationSent(String message) {
        layoutAlert.setVisibility(View.VISIBLE);
        tvMessageTitle.setText(getString(R.string.check_your_mail));
        tvMessage.setText(getString(R.string.check_your_mail_message));
    }

    @Override
    public void businessPhoneVerificationSent(String otpId) {
        Intent intent = new Intent(this, VerificationActivity.class);
        intent.putExtra("type", 2);
        intent.putExtra("phoneNumber", newValue);
        intent.putExtra("countryCode", countryCode);
        intent.putExtra("otpId", otpId);
        intent.putExtra("businessUniqueId", businessUniqueId);
        intent.putExtra("call", Constants.BUSINESS);
        startActivity(intent);
        finish();
    }

    @Override
    public void phoneVerificationSent(String otpId) {
        Intent intent = new Intent(this, VerificationActivity.class);
        intent.putExtra("type", 2);
        intent.putExtra("phoneNumber", newValue);
        intent.putExtra("countryCode", countryCode);
        intent.putExtra("otpId", otpId);
//        intent.putExtra("businessUniqueId", businessUniqueId);
        intent.putExtra("call", Constants.EDIT_PROFILE);
        startActivity(intent);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btnOk)
    public void okButton() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        hideKeyBoard();
        super.onBackPressed();
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
        if (imm != null && curruntFocusedOn != null)
            imm.hideSoftInputFromWindow(curruntFocusedOn.getWindowToken(), 0);
    }
}
