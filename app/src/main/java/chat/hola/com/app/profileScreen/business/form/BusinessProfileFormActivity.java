package chat.hola.com.app.profileScreen.business.form;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnFocusChange;
import chat.hola.com.app.AppController;

import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Activities.ChatCameraActivity;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.ImageCropper.CropImageView;
import chat.hola.com.app.NumberVerification.ChooseCountry;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.CountryCode;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.authentication.verifyEmail.VerifyEmailActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.business.address.BusinessAddressActivity;
import chat.hola.com.app.profileScreen.business.category.BusinessCategoryActivity;
import chat.hola.com.app.profileScreen.business.form.verify.BusinessFormVerifyActivity;
import chat.hola.com.app.user_verification.phone.VerifyNumberOTPActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>BusinessProfileFormActivity</h1>
 *
 * <p>User enters all required fields like business name, email, phone number and other information and
 * creates the business profile.</p>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 14 August 2019
 */
public class BusinessProfileFormActivity extends DaggerAppCompatActivity implements BusinessProfileFormContract.View {


    private static final int CAMERA_REQ_CODE = 24;
    private static final int READ_STORAGE_REQ_CODE = 26;
    private static final int WRITE_STORAGE_REQ_CODE = 27;

    private static final int RESULT_CAPTURE_PROFILE_IMAGE = 0;
    private static final int RESULT_LOAD_PROFILE_IMAGE = 1;
    private static final int RESULT_CAPTURE_COVER_IMAGE = 2;
    private static final int RESULT_LOAD_COVER_IMAGE = 3;

    private static final int FETCH_ADDRESS = 135;
    private static final int FETCH_CATEGORY = 145;
    private static final int FETCH_COUNTRY = 155;
    private static final int VERIFY_PHONE = 1122;
    private static final int VERIFY_EMAIL = 1123;

    private static final int BUSINESS_USERNAME = 1110;
    private static final int BUSINESS_NAME = 1111;
    private static final int BUSINESS_EMAIL = 1112;
    private static final int BUSINESS_PHONE = 1113;
    private static final int BUSINESS_WEBSITE = 1114;
    private static final int BUSINESS_BIO = 1115;

    private Dialog dialog;
    private boolean isEmailValid = false;
    private boolean isUserNameValid = false;
    private boolean isPhoneValid = false;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    BusinessProfileFormContract.Presenter presenter;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etWebsite)
    EditText etWebsite;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etBusinessCategory)
    EditText etBusinessCategory;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.tvCountryCode)
    TextView tvCountryCode;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.private_title)
    TextView private_title;
    @BindView(R.id.message1)
    TextView message1;
    @BindView(R.id.btnNext)
    Button btnNext;
    @BindView(R.id.ivFlag)
    ImageView ivFlag;
    @BindView(R.id.etBio)
    EditText etBio;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.input_layout_email)
    TextInputLayout input_layout_email;
    @BindView(R.id.input_layout_user_name)
    TextInputLayout input_layout_user_name;
    @BindView(R.id.input_layout_phone)
    TextInputLayout input_layout_phone;
    @BindView(R.id.ivProfileBg)
    ImageView ivProfileBg;
    private InputMethodManager imm;
    private String coverPicture;
    private String profilePicture;
    private String city, street, zipcode, lat, lng, categoryId, category;
    private String countryCode;
    private String flag;
    private int max_digits = 15;
    private String website;
    private boolean isMobileNumberVerified;
    private boolean isEmailVerified;
    private String verifiedEmail = "";
    private String verifiedPhone = "";
    private boolean isProfile;
    ImageSourcePicker imageSourcePicker;
    private boolean isPicChange = false;
    private String phonenumber;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_form);
        ButterKnife.bind(this);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        applyFont();
        loadCurrentCountryCode();

//        etWebsite.setText(getString(R.string.www));
//        etWebsite.setSelection(etWebsite.getText().length());

        //setup dialog
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @OnClick({R.id.ivProfile, R.id.ivAdd})
    void addProfile() {
        isProfile = true;
        imageSourcePicker = new ImageSourcePicker(this, true, true);
        imageSourcePicker.setOnSelectImage(callbackProfile);
        imageSourcePicker.show();
    }

    @OnClick(R.id.ivChangeCover)
    void addCover() {
        isProfile = false;
        imageSourcePicker = new ImageSourcePicker(this, true, true);
        imageSourcePicker.setOnSelectImage(callbackProfile);
        imageSourcePicker.show();
    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @OnClick(R.id.btnNext)
    public void next() {
        if (validate()) {
            Map<String, Object> params = new HashMap<>();
            params.put("businessCategoryId", categoryId);
            params.put("countryCode", countryCode);

            params.put("phone", etPhone.getText().toString().trim().replace(countryCode, ""));
            params.put("email", etEmail.getText().toString());
            params.put("businessUsername", etUserName.getText().toString());
            params.put("businessName", etName.getText().toString());
            params.put("websiteURL", website);
            params.put("address", etAddress.getText().toString());
            params.put("businessBio", etBio.getText().toString());
            params.put("privateAccount", false);
            params.put("isVisiblePhone", 1); // 0 = not visible, 1 = visible
            params.put("isVisibleEmail", 1); // 0 = not visible, 1 = visible
            params.put("isEmailVerified", isEmailVerified ? 1 : 0);
            params.put("isPhoneNumberVerified", isMobileNumberVerified ? 1 : 0);

            params.put("businessStreet", street);
            params.put("businessCity", city);
            params.put("businessZipCode", zipcode);
            params.put("businessLat", lat);
            params.put("businessLng", lng);

            presenter.applyBusinessProfile(params);
        }
    }

    @OnClick(R.id.etBusinessCategory)
    public void category() {
        Intent intent = new Intent(this, BusinessCategoryActivity.class);
        intent.putExtra("category", categoryId);
        startActivityForResult(intent, FETCH_CATEGORY);
    }

    @OnClick(R.id.etAddress)
    public void address() {
        Intent intent = new Intent(this, BusinessAddressActivity.class);
        intent.putExtra("city", city);
        intent.putExtra("street", street);
        intent.putExtra("zipcode", zipcode);
        startActivityForResult(intent, FETCH_ADDRESS);
    }

    @OnClick({R.id.rlCountryCode, R.id.tvCountryCode, R.id.ivFlag})
    public void countryCodePicker() {
        Intent intent = new Intent(this, ChooseCountry.class);
        startActivityForResult(intent, FETCH_COUNTRY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case RESULT_LOAD_PROFILE_IMAGE:
                case RESULT_LOAD_COVER_IMAGE:
                    presenter.parseSelectedImage(requestCode, resultCode, data);
                    break;

                case RESULT_CAPTURE_PROFILE_IMAGE:
                case RESULT_CAPTURE_COVER_IMAGE:
                    presenter.parseCapturedImage(requestCode, resultCode, data);
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    isPicChange = true;
                    presenter.parseCropedImage(requestCode, resultCode, data, isProfile);
                    break;

                case FETCH_ADDRESS:
                    if (data != null) {
                        city = data.getStringExtra("city");
                        street = data.getStringExtra("street");
                        zipcode = data.getStringExtra("zipcode");
                        lat = data.getStringExtra("lat");
                        lng = data.getStringExtra("lng");

                        String address = data.getStringExtra("address");
                        etAddress.setText(address);
                    }
                    break;
                case FETCH_CATEGORY:
                    if (data != null) {
                        categoryId = data.getStringExtra("category_id");
                        category = data.getStringExtra("category");
                        etBusinessCategory.setText(category);
                    }
                    break;
                case FETCH_COUNTRY:
                    if (data != null) {
                        String code = data.getStringExtra("CODE");
                        int flag = data.getIntExtra("FLAG", R.drawable.flag_in);
//                        max_digits = data.getIntExtra("MAX", 6);
                        countryCode = "+" + code.substring(1);
                        ivFlag.setImageResource(flag);
                        tvCountryCode.setText(countryCode);
//                        etPhone.setText(sessionManager.getMobileNumber().replace(countryCode, ""));
                        etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_digits)});

//                        String number = countryCode + etPhone.getText().toString().trim();
//                        etPhone.setSelected(number.equals(verifiedPhone));
                    }
                    break;
                case VERIFY_EMAIL:
                    verifiedEmail = etEmail.getText().toString().trim();
                    isEmailVerified = true;
                    etEmail.setSelected(isEmailVerified);
                    break;
                case VERIFY_PHONE:
                    verifiedPhone = countryCode + etPhone.getText().toString().trim();
                    isMobileNumberVerified = true;
                    etPhone.setSelected(isMobileNumberVerified);
                    break;
                case BUSINESS_NAME:
                    etName.setText(data.getStringExtra("businessName"));
                    break;
                case BUSINESS_USERNAME:
                    etUserName.setText(data.getStringExtra("businessUsername"));
                    break;
                case BUSINESS_BIO:
                    etBio.setText(data.getStringExtra("businessBio"));
                    break;
                case BUSINESS_WEBSITE:
                    etWebsite.setText(data.getStringExtra("businessWebsite"));
                    break;
                case BUSINESS_EMAIL:
                    etEmail.setText(data.getStringExtra("businessEmail"));
                    break;
                case BUSINESS_PHONE:
                    countryCode = data.getStringExtra("countryCode");
                    etPhone.setText(countryCode + "" + data.getStringExtra("businessPhone"));
                    phonenumber = data.getStringExtra("businessPhone");
                    break;
            }
    }

    /**
     * set the font
     */
    private void applyFont() {
        title.setTypeface(typefaceManager.getSemiboldFont());
        header.setTypeface(typefaceManager.getRegularFont());
        etEmail.setTypeface(typefaceManager.getRegularFont());
        etName.setTypeface(typefaceManager.getRegularFont());
        etUserName.setTypeface(typefaceManager.getRegularFont());
        etWebsite.setTypeface(typefaceManager.getRegularFont());
        etPhone.setTypeface(typefaceManager.getRegularFont());
        etBio.setTypeface(typefaceManager.getRegularFont());
        etAddress.setTypeface(typefaceManager.getRegularFont());
        etBusinessCategory.setTypeface(typefaceManager.getRegularFont());
        tvCountryCode.setTypeface(typefaceManager.getRegularFont());
        message.setTypeface(typefaceManager.getRegularFont());
        message1.setTypeface(typefaceManager.getRegularFont());
        private_title.setTypeface(typefaceManager.getSemiboldFont());
        btnNext.setTypeface(typefaceManager.getSemiboldFont());
    }

    /**
     * fetch the country code based on your sim card
     */
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
            String allCountriesCode = readEncodedJsonString();
            JSONArray countryArray = new JSONArray(allCountriesCode);
            for (int i = 0; i < countryArray.length(); i++) {
                if (locale.equals(countryArray.getJSONObject(i).getString("code"))) {
                    flag = "flag_" + locale.toLowerCase();
                    int id = getResources().getIdentifier(flag, "drawable", getPackageName());
                    Drawable flag = getResources().getDrawable(id);
                    showCurCountry(flag, countryArray.getJSONObject(i).getString("dial_code"));
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * reads the base64 country code data and returns it in a string
     */
    private static String readEncodedJsonString() {
        byte[] data = Base64.decode(CountryCode.ENCODED_COUNTRY_CODE, Base64.DEFAULT);
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * Sets the flag and country code to ui
     *
     * @param flag      : flag drawable
     * @param dial_code : country code
     */
    private void showCurCountry(Drawable flag, String dial_code) {
        ivFlag.setImageDrawable(flag);
        countryCode = dial_code;
//        tvCountryCode.setText(dial_code);
//        etPhone.setText(sessionManager.getMobileNumber().replace(countryCode, ""));
        etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_digits)});
    }

    /**
     * Validates all the fields
     *
     * @return : true if all fields are correct otherwise false
     */
    private boolean validate() {

        if (etName.getText().toString().isEmpty()) {
            showMessage("Please enter business name", -1);
            return false;
        }
        if (etUserName.getText().toString().isEmpty()) {
            showMessage("Please enter business User Name", -1);
            return false;
        }

        if (countryCode == null || countryCode.isEmpty() || etPhone.getText().toString().isEmpty()) {
            showMessage("Please enter valid mobile number", -1);
            return false;
        }

        if (etEmail.getText().toString().isEmpty()) {
            showMessage("Please enter valid email address", -1);
            return false;
        }

        if (category == null || categoryId == null || categoryId.isEmpty() || category.isEmpty()) {
            showMessage("Please select business category", -1);
            return false;
        }

        if (etAddress.getText().toString().isEmpty()) {
            showMessage("Please enter your business address", -1);
            return false;
        }

        website = etWebsite.getText().toString().toLowerCase().trim();
        if (website.isEmpty()) {
            showMessage("Please enter website", -1);
            return false;
        }

        if (!website.contains("http://"))
            website = "http://" + website;

        if (!Patterns.WEB_URL.matcher(website).matches()) {
            showMessage("Please enter valid website", -1);
            return false;
        } else {
            website = website.replace("http://", "");
        }
//
//        if (!isEmailVerified) {
//            showMessage(null, R.string.verify_email_error);
//            return false;
//        }
//
//        if (!isMobileNumberVerified) {
//            showMessage(null, R.string.verify_phone_error);
//            return false;
//        }
        return true;
    }

    @Override
    public void showProgress(boolean show) {
        if (dialog != null) {
            if (show)
                dialog.show();
            else if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    @Override
    public void profile() {
//        startActivity(new Intent(this, ProfileActivity.class).putExtra("isBusiness", true));
        finish();
    }

    @Override
    public void verifyMobile() {
        Intent intent = new Intent(this, VerifyNumberOTPActivity.class);
        intent.putExtra("call", Constants.BUSINESS);
        intent.putExtra("countryCode", countryCode);
        intent.putExtra("phoneNumber", etPhone.getText().toString().trim());
        startActivityForResult(intent, VERIFY_PHONE);
    }

    @Override
    public void verifyEmailAddress() {
        Intent intent = new Intent(this, VerifyEmailActivity.class);
        intent.putExtra("call", Constants.BUSINESS);
        intent.putExtra("email", etEmail.getText().toString());
        startActivityForResult(intent, VERIFY_EMAIL);
    }

    @Override
    public void launchCamera(boolean isProfile) {
        Intent intent = new Intent(this, ChatCameraActivity.class);
        intent.putExtra("requestType", "EditProfile");
        startActivityForResult(intent, isProfile ? RESULT_CAPTURE_PROFILE_IMAGE : RESULT_CAPTURE_COVER_IMAGE);
    }

    @Override
    public void launchImagePicker(boolean isProfile) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("image/*");
        startActivityForResult(intent, isProfile ? RESULT_LOAD_PROFILE_IMAGE : RESULT_LOAD_COVER_IMAGE);
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msgId == -1 ? msg : getString(msgId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(this);
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        if (flag)
            showMessage(null, R.string.no_internet);
    }

    @Override
    public void userBlocked() {
        sessionManager.sessionExpired(this);
    }

    @Override
    public void reload() {
        //nothing to do
    }

    ImageSourcePicker.OnSelectImageSource callbackCover = new ImageSourcePicker.OnSelectImageSource() {
        @Override
        public void onCamera() {
            checkCameraPermissionImage(false);
        }

        @Override
        public void onGallary() {
            checkReadImage(false);
        }

        @Override
        public void onCancel() {
            //nothing to do.
        }
    };

    ImageSourcePicker.OnSelectImage callbackProfile = new ImageSourcePicker.OnSelectImage() {

        @Override
        public void onCamera() {
            checkCameraPermissionImage(isProfile);
        }

        @Override
        public void onGallary() {
            checkReadImage(isProfile);
        }

        @Override
        public void onCancel() {
            //nothing to do.
        }
    };

    private void checkCameraPermissionImage(boolean isProfile) {
        if (ActivityCompat.checkSelfPermission(BusinessProfileFormActivity
                .this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(BusinessProfileFormActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                presenter.launchCamera(getPackageManager(), isProfile);
            } else {
                /*
                 *permission required to save the image captured
                 */
                requestReadImagePermission(0);
            }
        } else {
            requestCameraPermissionImage();
        }
    }

    private void requestCameraPermissionImage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessProfileFormActivity.this,
                Manifest.permission.CAMERA)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_221,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(BusinessProfileFormActivity.this, new String[]{Manifest.permission.CAMERA},
                            CAMERA_REQ_CODE);
                }
            });
            snackbar.show();
            View view = snackbar.getView();
            ((TextView) view.findViewById(R.id.snackbar_text))
                    .setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            ActivityCompat.requestPermissions(BusinessProfileFormActivity.this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQ_CODE);
        }
    }

    private void checkReadImage(boolean isProfile) {
        if (ActivityCompat.checkSelfPermission(BusinessProfileFormActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BusinessProfileFormActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            presenter.launchImagePicker(isProfile);
        } else {
            requestReadImagePermission(1);
        }
    }

    private void requestReadImagePermission(int k) {
        if (k == 1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessProfileFormActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(BusinessProfileFormActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_222,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(BusinessProfileFormActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                READ_STORAGE_REQ_CODE);
                    }
                });
                snackbar.show();
                View view = snackbar.getView();
                ((TextView) view.findViewById(R.id.snackbar_text))
                        .setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                ActivityCompat.requestPermissions(BusinessProfileFormActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        READ_STORAGE_REQ_CODE);
            }
        } else if (k == 0) {
            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(BusinessProfileFormActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(BusinessProfileFormActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_1218,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(BusinessProfileFormActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_STORAGE_REQ_CODE);
                    }
                });

                snackbar.show();
                View view = snackbar.getView();
                ((TextView) view.findViewById(R.id.snackbar_text))
                        .setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                ActivityCompat.requestPermissions(BusinessProfileFormActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_STORAGE_REQ_CODE);
            }
        }
    }

    @Override
    public void setProfilePic(String profilePath) {
        profilePicture = profilePath;
        try {
            Glide.with(getBaseContext()).load(profilePicture).asBitmap()
                    .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .centerCrop().into(ivProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finishActivity(boolean success) {
        setResult(success ? RESULT_OK : 0);
        finish();
    }

    @Override
    public void setProfileImage(Bitmap bitmap) {
        ivProfile.setImageBitmap(bitmap);
    }

    @Override
    public void setCover(Bitmap bitmap) {
        ivProfileBg.setImageBitmap(bitmap);
    }

    @Override
    public void launchCropImage(Uri data) {
        if (isProfile){
            CropImage.activity(data).setCropShape(CropImageView.CropShape.OVAL)
                    .setFixAspectRatio(true)
                    .setAspectRatio(Constants.Profile.PROFILE_PIC_SIZE , Constants.Profile.PROFILE_PIC_SIZE )
                    .start(this);
        }else{
            CropImage.activity(data).setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setFixAspectRatio(true)
                    .setAspectRatio( Constants.Profile.WIDTH, Constants.Profile.HEIGHT)
                    .start(this);
        }
    }

    @Override
    public void userNameAvailable(boolean b) {
        if (!b) {
            isUserNameValid = false;
            input_layout_user_name.setError("User name is already in use");
        } else {
            isUserNameValid = true;
            input_layout_user_name.setError(null);
        }
    }

    @Override
    public void emailAvailable(boolean b) {
        if (!b) {
            isEmailValid = false;
            input_layout_email.setError("Email is already in use");
        } else {
            isEmailValid = true;
            input_layout_email.setError(null);
        }
    }

    @Override
    public void phoneAvailable(boolean b) {
        if (!b) {
            isPhoneValid = false;
            input_layout_phone.setError("Phone number is already in use");
        } else {
            isPhoneValid = true;
            input_layout_phone.setError(null);
        }
    }

    @OnFocusChange({R.id.etEmail, R.id.etUserName, R.id.etPhone})
    void onFocusChangeEvent(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.etEmail:
                if (!hasFocus) {
                    String email = etEmail.getText().toString();
                    if (email.equalsIgnoreCase("")) {
                        isEmailValid = false;
                        input_layout_email.setError(getString(R.string.emptyEmail));
                    } else if (!email.equalsIgnoreCase("") && CommonClass.validatingEmail(email)) {
                        isEmailValid = false;
                        input_layout_email.setError(getString(R.string.enter_valid_email));
                    } else {
                        presenter.verifyIsEmailRegistered(email);
                    }
                }
                break;
            case R.id.etUserName:
                if (!hasFocus) {
                    String userName = etUserName.getText().toString();
                    if (userName.equalsIgnoreCase("")) {
                        input_layout_user_name.setError(getString(R.string.emptyUsername));
                        isUserNameValid = false;
                    } else {
                        presenter.verifyIsUserNameRegistered(userName);
                    }
                }
                break;
            case R.id.etPhone:
                if (!hasFocus) {
                    String phone = etPhone.getText().toString();
                    if (phone.equalsIgnoreCase("")) {
                        input_layout_phone.setError(getString(R.string.emptyPhoneNumber));
                        isPhoneValid = false;
                    } else if (phone.length() < 8) {
                        input_layout_phone.setError(getString(R.string.invalidPhone));
                        isPhoneValid = false;
                    } else {
                        presenter.verifyIsPhoneRegistered(phone, countryCode);
                    }
                }
                break;
            case R.id.etWebsite:
                if (!hasFocus) {
                    website = etWebsite.getText().toString().toLowerCase().trim();
                    if (website.isEmpty()) {
                        showMessage("Please enter website", -1);
                    } else {
                        if (!Patterns.WEB_URL.matcher(website).matches()) {
                            etWebsite.setText(getString(R.string.www));
                            showMessage("Please enter valid website", -1);
                        }
                    }
                    break;
                }
        }
    }

    /**
     * Bug title:in business profile page always keep keyboard open to type
     * Bug description:Forcefully opening keyboard on load of activity
     * Developer name:Shaktisinh
     * Fixed date:11, May 2021
     */
    private void showKeyboard() {
        etUserName.requestFocus();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imm != null) {
                    imm.showSoftInput(etUserName, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    @OnClick(R.id.etUserName)
    public void businessUsername() {
        Intent intent = new Intent(this, BusinessFormVerifyActivity.class);
        intent.putExtra("key", "businessUsername");
        intent.putExtra("value", etUserName.getText().toString());
        startActivityForResult(intent, BUSINESS_USERNAME);
    }

    @OnClick(R.id.etName)
    public void businessName() {
        Intent intent = new Intent(this, BusinessFormVerifyActivity.class);
        intent.putExtra("key", "businessName");
        intent.putExtra("value", etName.getText().toString());
        startActivityForResult(intent, BUSINESS_NAME);
    }

    @OnClick(R.id.etEmail)
    public void businessEmail() {
        Intent intent = new Intent(this, BusinessFormVerifyActivity.class);
        intent.putExtra("key", "businessEmail");
        intent.putExtra("value", etEmail.getText().toString());
        startActivityForResult(intent, BUSINESS_EMAIL);
    }

    @OnClick(R.id.etPhone)
    public void businessPhone() {
        Intent intent = new Intent(this, BusinessFormVerifyActivity.class);
        intent.putExtra("key", "businessPhone");
        intent.putExtra("value", phonenumber);
        intent.putExtra("countryCode", countryCode);
        startActivityForResult(intent, BUSINESS_PHONE);
    }

    @OnClick(R.id.etWebsite)
    public void businessWebsite() {
        Intent intent = new Intent(this, BusinessFormVerifyActivity.class);
        intent.putExtra("key", "businessWebsite");
        intent.putExtra("value", etWebsite.getText().toString());
        startActivityForResult(intent, BUSINESS_WEBSITE);
    }

    @OnClick(R.id.etBio)
    public void businessBio() {
        Intent intent = new Intent(this, BusinessFormVerifyActivity.class);
        intent.putExtra("key", "businessBio");
        intent.putExtra("value", etBio.getText().toString());
        startActivityForResult(intent, BUSINESS_BIO);
    }

    /**
     * To hide keyboard
     */
    private void hideKeyBoard() {
        if (imm != null && imm.isActive() && getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
