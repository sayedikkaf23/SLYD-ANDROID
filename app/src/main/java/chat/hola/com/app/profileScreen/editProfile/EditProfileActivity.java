package chat.hola.com.app.profileScreen.editProfile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.otto.Bus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Dialog.DatePickerFragment;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.ImageCropper.CropImageView;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.authentication.newpassword.NewPasswordActivity;
import chat.hola.com.app.authentication.verifyEmail.VerifyEmailActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.editProfile.editDetail.EditDetailActivity;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileBody;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.user_verification.RequestEmailOTPActivity;
import chat.hola.com.app.user_verification.RequestNumberOTPActivity;
import chat.hola.com.app.user_verification.phone.VerifyNumberOTPActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h>EditProfileActivity</h>
 * <p>This fragment allow user to edit their profile detail like username, status etc.</p>
 *
 * @author 3Embed
 * @since 19/2/18.
 */
public class EditProfileActivity extends DaggerAppCompatActivity
        implements EditProfileContract.View, View.OnFocusChangeListener {

    private static final String TAG = EditProfileActivity.class.getSimpleName();
    //constants
    private static final int EDIT_EMAIL_REQ_CODE = 333;
    private static final int STATUS_UPDATE_REQ_CODE = 555;
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
    private static final int VERIFY_BUSINESS_PHONE = 1122;
    private static final int VERIFY_BUSINESS_EMAIL = 1123;
    private static final int FETCH_BUSINESS_COUNTRY = 156;

    private static final int EDIT_FIRST_LAST_NAME = 11;
    private static final int EDIT_USER_NAME = 12;
    private static final int EDIT_BIO_STATUS = 655;
    private static final int EDIT_KNOWN_AS = 656;

    private static final int BUSINESS_NAME = 1100;
    private static final int BUSINESS_USER_NAME = 1101;
    private static final int BUSINESS_BIO = 1102;
    private static final int BUSINESS_WEBSITE = 1103;
    private static final int BUSINESS_ADDRESS = 1104;
    private static final int BUSINESS_EMAIL = 1105;
    private static final int BUSINESS_PHONE = 1106;

    private static final int EMAIL = 1107;
    private static final int PHONE = 1108;

    @Inject
    EditProfilePresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    DatePickerFragment datePickerFragment;
    ImageSourcePicker imageSourcePicker;
    @Inject
    SessionManager sessionManager;
    @Inject
    BlockDialog dialog1;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.tvEditProfile)
    TextView tvEditProfile;
    @BindView(R.id.flAddProfile)
    FrameLayout flAddProfile;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.ivProfileBg)
    ImageView ivCover;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.titlePhone)
    TextInputLayout titlePhone;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.etPhone)
    TextInputEditText etPhone;
//    @BindView(R.id.tvCountryCode)
//    TextView tvCountryCode;

    @BindView(R.id.titleStatus)
    TextInputLayout titleStatus;
    @BindView(R.id.etStatus)
    TextInputEditText etStatus;
    @BindView(R.id.titleEmail)
    TextInputLayout titleEmail;
    @BindView(R.id.titleBusinessBio)
    TextInputLayout titleBusinessBio;

    @BindView(R.id.etEmail)
    TextInputEditText etEmail;

    @BindView(R.id.etKnownAs)
    TextInputEditText etKnownAs;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilUserName)
    TextInputLayout tilUserName;
    @BindView(R.id.tilKnownAs)
    TextInputLayout tilKnownAs;
    @BindView(R.id.isPrivate)
    SwitchCompat isPrivate;
    @BindView(R.id.llStar)
    LinearLayout llStar;

    @BindView(R.id.llBusiness)
    LinearLayout llBusiness;
    @BindView(R.id.llPersonalDetail)
    LinearLayout llPersonalDetail;
    @BindView(R.id.etBusinessBio)
    TextInputEditText etBusinessBio;
    @BindView(R.id.etBusinessCategory)
    TextInputEditText etBusinessCategory;
    @BindView(R.id.etBusinessPhone)
    TextInputEditText etBusinessPhone;
    @BindView(R.id.etBusinessEmail)
    TextInputEditText etBusinessEmail;
    @BindView(R.id.etBusinessName)
    TextInputEditText etBusinessName;
    @BindView(R.id.etBusinessWebsite)
    TextInputEditText etBusinessWebsite;
    @BindView(R.id.etBusinessUserName)
    TextInputEditText etBusinessUserName;
    @BindView(R.id.etBusinessAddress)
    TextInputEditText etBusinessAddress;

    @BindView(R.id.titleBusinessEmail)
    TextInputLayout titleBusinessEmail;
    @BindView(R.id.titleBusinessPhone)
    TextInputLayout titleBusinessPhone;


    @BindView(R.id.rlSwitchAccountPrivacy)
    RelativeLayout rlSwitchAccountPrivacy;
    @BindView(R.id.btnSave)
    Button btnSave;

//    @BindView(R.id.adView)
//    AdView adView;

    private Unbinder unbinder;
    private Data profileData;
    private ProgressDialog dialog;
    private String call = "";
    private Bus bus = AppController.getBus();
    private boolean isProfile;
    private String coverPicture;
    private String profilePicture;
    private boolean isPicChange = false;
    public static Data.BusinessProfile businessProfile;
    private boolean isUsernameVerified = true;
    private boolean isBusinessEmailVerified;
    private boolean isEmailVerified;
    private boolean isBusinessPhoneVerified;
    private boolean isPhoneVerified;
    private String verifiedBusinessEmail = "";
    private String verifiedBusinessPhone = "";
    private String businessAddress = "";
    private String businessCountryCode = "";
    private String countryCode = "";
    private int max_digits = 15;
    private int business_max_digits = 15;
    private String businessCategoryId;
    private String businessUniqueId;
    private boolean changed = false;

    @Override
    public void userBlocked() {
        dialog1.show();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        unbinder = ButterKnife.bind(this);
        bus.register(this);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        profileData = (Data) bundle.getSerializable("profile_data");
        call = getIntent().getStringExtra("call");

        presenter.init();

        setProfileData(sessionManager.isBusinessProfileAvailable());

//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);

    }

    /**
     * It will open business contact option page.
     */
    @OnClick(R.id.tvProfileDetailsChangePass)
    public void changePassword() {
        startActivity(new Intent(this, NewPasswordActivity.class).putExtra("call", "setting"));
    }

    private void progressBarSetup() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.editProfileProgressMsg));
        dialog.setCancelable(false);
    }

    private void setProfileData(boolean isBusinessProfile) {
        if (profileData != null) {
            if (isBusinessProfile) {
                //business profile
                businessProfileData();
            } else {
                llPersonalDetail.setVisibility(!profileData.isActiveBussinessProfile() ? View.VISIBLE : View.GONE);
                llBusiness.setVisibility(profileData.isActiveBussinessProfile() ? View.VISIBLE : View.GONE);
                llStar.setVisibility(profileData.isStar() && !profileData.isActiveBussinessProfile() ? View.VISIBLE : View.GONE);

                isEmailVerified = profileData.isVerifiedEmail();
                isPhoneVerified = profileData.isVerifiedNumber();

                titleEmail.setError(getString(isEmailVerified ? R.string.verified : R.string.not_verified));
                if (isEmailVerified) {
                    titleEmail.setErrorTextAppearance(R.style.inputErrorGreen);
                    titleEmail.setHelperTextTextAppearance(R.style.inputErrorGreen);
                } else {
                    titleEmail.setErrorTextAppearance(R.style.inputErrorRed);
                    titleEmail.setHelperTextTextAppearance(R.style.inputErrorRed);
                }

                titlePhone.setError(getString(isPhoneVerified ? R.string.verified : R.string.not_verified));
                if (isPhoneVerified) {
                    titlePhone.setErrorTextAppearance(R.style.inputErrorGreen);
                    titlePhone.setHintTextAppearance(R.style.inputErrorGreen);
                } else {
                    titlePhone.setErrorTextAppearance(R.style.inputErrorRed);
                    titlePhone.setHintTextAppearance(R.style.inputErrorRed);
                }
                tilUserName.setError(getString(isUsernameVerified ? R.string.verified : R.string.not_verified));
                if(isUsernameVerified){
                    tilUserName.setErrorTextAppearance(R.style.inputErrorGreen);
                    tilUserName.setHintTextAppearance(R.style.inputErrorGreen);
                } else {
                    tilUserName.setErrorTextAppearance(R.style.inputErrorRed);
                    tilUserName.setHintTextAppearance(R.style.inputErrorRed);
                }

                etUsername.setText(profileData.getUserName());
                etKnownAs.setText(profileData.getStarRequest().getStarUserKnownBy());
                String firstName = profileData.getFirstName();
                String lastName = profileData.getLastName();
                String name =
                        ((firstName == null) ? "" : firstName);
                String lName = ((lastName == null) ? "" : " " + lastName);
                etName.setText(firstName /*+ " " + lastName*/);
                etLastName.setText(lastName);
                etStatus.setText(profileData.getStatus().replace("dub.ly", getString(R.string.app_name))
                        .replace("Dub.ly", getString(R.string.app_name)).replace("BeSocial",getString(R.string.app_name)));
                etEmail.setText(profileData.getEmail());
                etEmail.setKeyListener(null);
                etPhone.setText(profileData.getCountryCode() + "" + profileData.getNumber());
//                tvCountryCode.setText(profileData.getCountryCode());

                rlSwitchAccountPrivacy.setVisibility(profileData.isStar() ? View.GONE : View.VISIBLE);
                isPrivate.setChecked(profileData.getPrivate().equals("1"));

                profilePicture = profileData.getProfilePic();
                coverPicture = profileData.getProfileCoverImage();
                if (!TextUtils.isEmpty(profileData.getProfilePic())) {
                    Glide.with(getBaseContext())
                            .load(profileData.getProfilePic())
                            .asBitmap()
                            .centerCrop()
                            .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .into(new BitmapImageViewTarget(ivProfile) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    super.setResource(resource);
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivProfile.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } else {
                    Utilities.setTextRoundDrawable(getBaseContext(), profileData.getFirstName(), profileData.getLastName(), ivProfile);
                }
                Glide.with(getBaseContext()).load(profileData.getProfileCoverImage()).asBitmap()
                        .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .centerCrop().into(ivCover);

//                etEmail.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(EditProfileActivity.this, RequestEmailOTPActivity.class);
//                        intent.putExtra("profileData", profileData);
//                        intent.putExtra("type", "3");
//                        startActivityForResult(intent, Constants.Verification.EMAIL_VERIFICATION_REQ);
//                    }
//                });

                etUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EditProfileActivity.this, EditUserNameActivity.class);
                        intent.putExtra(Constants.PROFILE_USERNAME, etUsername.getText().toString());
                        startActivityForResult(intent, EDIT_USER_NAME);
                    }
                });

                etStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(EditProfileActivity.this, EditStatusActivity.class);
                        intent.putExtra(Constants.PROFILE_STATUS, etStatus.getText().toString());
                        startActivityForResult(intent, EDIT_BIO_STATUS);
                    }

                });


//                etContactNumber.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(EditProfileActivity.this, RequestNumberOTPActivity.class);
//                        intent.putExtra("profileData", profileData);
//                        startActivityForResult(intent, Constants.Verification.NUMBER_VERIFICATION_REQ);
//                    }
//                });
            }

            EditProfileBody editProfileBody = new EditProfileBody();
            isPrivate.setOnCheckedChangeListener((buttonView, isChecked) -> {
                editProfileBody.set_private(isChecked ? 1 : 0);
                presenter.updateProfileDataToServer(editProfileBody);
            });

            if (call != null) etName.setSelection(etName.getText().toString().trim().length());
        }
    }

    @SuppressLint("SetTextI18n")
    private void businessProfileData() {
        llBusiness.setVisibility(View.VISIBLE);
        rlSwitchAccountPrivacy.setVisibility(View.GONE);
        isPrivate.setChecked(false);

        businessProfile = profileData.getBusinessProfiles().get(0);
        businessUniqueId = businessProfile.getBusinessUniqueId();

        etBusinessUserName.setText(businessProfile.getBusinessUserName());
        etBusinessName.setText(businessProfile.getBusinessName());
        etBusinessBio.setText(businessProfile.getBusinessBio());
        etBusinessCategory.setText(businessProfile.getBusinessCategory());
        etBusinessEmail.setText(businessProfile.getEmail().getId());
        etBusinessWebsite.setText(businessProfile.getWebsite());

        businessCountryCode = businessProfile.getPhone().getCountryCode();
        etBusinessPhone.setText(businessProfile.getPhone().getNumber().replace(businessCountryCode, ""));

        etBusinessCategory.setText(businessProfile.getBusinessCategory());
        businessCategoryId = businessProfile.getBussinessId();

        businessAddress = businessProfile.getAddress();
        etBusinessAddress.setText(businessAddress);

        isBusinessPhoneVerified = businessProfile.getPhone().getVerified() == 1;
        isBusinessEmailVerified = businessProfile.getEmail().getVerified() == 1;

        titleBusinessEmail.setError(getString(isBusinessEmailVerified ? R.string.verified : R.string.not_verified));
        if (isBusinessEmailVerified) {
            verifiedBusinessEmail = Objects.requireNonNull(etBusinessEmail.getText()).toString().trim();
            titleBusinessEmail.setErrorTextAppearance(R.style.inputErrorGreen);
            titleBusinessEmail.setHelperTextTextAppearance(R.style.inputErrorGreen);
        } else {
            titleBusinessEmail.setErrorTextAppearance(R.style.inputErrorRed);
            titleBusinessEmail.setHelperTextTextAppearance(R.style.inputErrorRed);
        }

        titleBusinessPhone.setError(getString(isBusinessPhoneVerified ? R.string.verified : R.string.not_verified));
        if (isBusinessPhoneVerified) {
            verifiedBusinessPhone = Objects.requireNonNull(etBusinessPhone.getText()).toString().trim();
            titleBusinessPhone.setErrorTextAppearance(R.style.inputErrorGreen);
            titleBusinessPhone.setHintTextAppearance(R.style.inputErrorGreen);
        } else {
            titleBusinessPhone.setErrorTextAppearance(R.style.inputErrorRed);
            titleBusinessPhone.setHintTextAppearance(R.style.inputErrorRed);
        }


        profilePicture = businessProfile.getBusinessProfilePic();
        coverPicture = businessProfile.getBusinessProfileCoverImage();
        if (!TextUtils.isEmpty(businessProfile.getBusinessProfilePic())) {
            Glide.with(getBaseContext()).load(businessProfile.getBusinessProfilePic()).asBitmap()
                    .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    super.setResource(resource);
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivProfile.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            Utilities.setTextRoundDrawable(getBaseContext(), businessProfile.getBusinessName(), "", ivProfile);
        }
        Glide.with(getBaseContext()).load(businessProfile.getBusinessProfileCoverImage()).asBitmap()
                .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .centerCrop().into(ivCover);
    }

    @Override
    public void applyFont() {
        btnCancel.setTypeface(typefaceManager.getSemiboldFont());
        tvEditProfile.setTypeface(typefaceManager.getSemiboldFont());
    }

    @Override
    public void finishActivity(boolean success, boolean imageUpdated) {

        Intent intent = new Intent();

        if (success) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("eventName", "profileUpdated");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bus.post(obj);
            if (imageUpdated) {
                AppController.getInstance().getSessionManager().setUserProfilePicUpdateTime();
            }

            intent.putExtra("imageUpdated", imageUpdated);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }

        finish();
    }

    @Override
    public void showProgress(boolean show) {
        try {
            if (show && dialog != null && !dialog.isShowing()) {
                dialog.show();
            } else if (!show && dialog != null && dialog.isShowing()) dialog.dismiss();
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public void setEmail(String email) {
        etEmail.setText(email);
        titleEmail.setVisibility(email != null && TextUtils.isEmpty(email) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void launchCamera(Intent intent, boolean isProfile) {
        startActivityForResult(intent,
                isProfile ? RESULT_CAPTURE_PROFILE_IMAGE : RESULT_CAPTURE_COVER_IMAGE);
    }

    @Override
    public void launchImagePicker(Intent intent, boolean isProfile) {
        startActivityForResult(intent, isProfile ? RESULT_LOAD_PROFILE_IMAGE : RESULT_LOAD_COVER_IMAGE);
    }

    @OnClick({R.id.ivProfile, R.id.ivAdd})
    void addProfile() {
        isProfile = true;
        imageSourcePicker = new ImageSourcePicker(this, true, true);
        imageSourcePicker.setOnSelectImage(callbackProfile);
        imageSourcePicker.show();
    }

    @OnClick(R.id.ivChangeCover)
    void changeCover() {
        isProfile = false;
        imageSourcePicker = new ImageSourcePicker(this, true, false);
        imageSourcePicker.setOnSelectImageSource(callbackCover);
        imageSourcePicker.show();
    }

    ImageSourcePicker.OnSelectImageSource callbackCover =
            new ImageSourcePicker.OnSelectImageSource() {
                @Override
                public void onCamera() {
                    checkCameraPermissionImage(true);
                }

                @Override
                public void onGallary() {
                    checkReadImage(true);
                }

                @Override
                public void onCancel() {
                    //nothing to do.
                }
            };

    ImageSourcePicker.OnSelectImage callbackProfile = new ImageSourcePicker.OnSelectImage() {

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

    /**
     * It will open firstname lastname edit page
     */
    @OnClick({R.id.etName, R.id.etLastName})
    public void editName() {
        Intent intent = new Intent(this, EditNameActivity.class);
        intent.putExtra("LastName", etLastName.getText().toString());
        intent.putExtra("FirstName", etName.getText().toString());
        startActivityForResult(intent, EDIT_FIRST_LAST_NAME);
    }

    /*@OnClick(R.id.btnCancel)
    public void cancel() {
        backPress();
    }*/

    @OnClick(R.id.ivBack)
    public void cancel() {
        backPress();
    }

    @OnClick(R.id.btnSave)
    public void save() {
        //need to call some api method
        EditProfileBody editProfileBody = new EditProfileBody();
        if (profileData.isActiveBussinessProfile()) {
            if (businessProfile != null) {
                editProfileBody.setBusinessUniqueId(businessUniqueId);
            }
        } else {
            editProfileBody.setFirstName(profileData.getFirstName());
            editProfileBody.setLastName(etLastName.getText().toString());
            editProfileBody.setUserName(etUsername.getText().toString());
            editProfileBody.setKnownAs(etKnownAs.getText().toString());
            String status = etStatus.getText().toString();
            editProfileBody.setStatus((status.isEmpty()) ? getResources().getString(R.string.default_status) : status);
            editProfileBody.setEmail(etEmail.getText().toString());
            editProfileBody.set_private(isPrivate.isChecked() ? 1 : 0);
            //editProfileBody.setImgUrl(profilePicture);
            //editProfileBody.setProfileCoverImage(coverPicture);
        }

        UploadFileAmazonS3 amazonS3 = new UploadFileAmazonS3(this);
        presenter.initUpdateProfile(editProfileBody, profileData, amazonS3, isPicChange,
                profileData.isActiveBussinessProfile());
    }

    private void backPress() {
        if (changed) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Forgot to save?");
            builder.setMessage("Hey.. you forgot to save changes!!");
            builder.setPositiveButton("Save", (dialog, which) -> save());
            builder.setNegativeButton("Discard", (dialog, which) -> super.onBackPressed());
            builder.create().show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        backPress();
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
    public void showSnackMsg(int msgId) {
        String msg = getResources().getString(msgId);
        Snackbar snackbar = Snackbar.make(root, "" + msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
    }


    /*
     * Bug Title: Cropping tool aspect ratio is not same as image view size of banner
     * Bug Id: #2719
     * Fix Dev: Hardik
     * Fix Desc: change ratio
     * Fix Date: 24/6/21
     * */
    @Override
    public void launchCropImage(Uri data) {
        //        .setCropShape(isProfile ? CropImageView.CropShape.OVAL : CropImageView.CropShape.RECTANGLE)
        if (isProfile) {
            CropImage.activity(data).setCropShape(CropImageView.CropShape.OVAL)
                    .setFixAspectRatio(true)
                    .setAspectRatio(Constants.Profile.PROFILE_PIC_SIZE, Constants.Profile.PROFILE_PIC_SIZE)
                    .start(this);
        } else {
            CropImage.activity(data).setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setFixAspectRatio(true)
                    .setAspectRatio(Constants.Profile.WIDTH, Constants.Profile.HEIGHT)
                    .start(this);
        }
    }


    @Override
    public void setProfileImage(Bitmap bitmap) {
        ivProfile.setImageBitmap(bitmap);
        saveEnable(true);
    }

    @Override
    public void setCover(Bitmap bitmap) {
        ivCover.setImageBitmap(bitmap);
        saveEnable(true);
    }

    @Override
    public void saveEnable(boolean enable) {
        changed = true;
        if (btnSave != null)
            btnSave.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void setProfilePic(String profilePath) {
        profilePicture = profilePath;
        sessionManager.setUserProfilePic(profilePath, false);
        try {
            if (!TextUtils.isEmpty(profilePicture)) {
                Glide.with(getBaseContext())
                        .load(profilePicture)
                        .asBitmap()
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .centerCrop()
                        .into(new BitmapImageViewTarget(ivProfile) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                super.setResource(resource);
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivProfile.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                Utilities.setTextRoundDrawable(getBaseContext(), profileData.getFirstName(), profileData.getLastName(), ivProfile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verifyMobile() {
        Intent intent = new Intent(this, VerifyNumberOTPActivity.class);
        intent.putExtra("call", Constants.BUSINESS);
        intent.putExtra("countryCode", businessCountryCode);
        intent.putExtra("phoneNumber", etBusinessPhone.getText().toString().trim());
        startActivityForResult(intent, VERIFY_BUSINESS_PHONE);
    }

    @Override
    public void showProfileData(Profile data) {
        profileData = data.getData().get(0);
        setProfileData(sessionManager.isBusinessProfileAvailable());
    }

    @Override
    public void verifyEmailAddress() {
        Intent intent = new Intent(this, VerifyEmailActivity.class);
        intent.putExtra("call", Constants.BUSINESS);
        intent.putExtra("email", Objects.requireNonNull(etBusinessEmail.getText()).toString());
        startActivityForResult(intent, VERIFY_BUSINESS_EMAIL);
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        if (!flag) Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
    }

    private void checkCameraPermissionImage(boolean isProfile) {
        if (ActivityCompat.checkSelfPermission(EditProfileActivity
                .this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

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
        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                Manifest.permission.CAMERA)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_221, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.string_580), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(EditProfileActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQ_CODE);
                        }
                    });
            snackbar.show();
            View view = snackbar.getView();
            ((TextView) view.findViewById(R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            ActivityCompat.requestPermissions(EditProfileActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQ_CODE);
        }
    }

    private void checkReadImage(boolean isProfile) {
        if (ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            presenter.launchImagePicker(isProfile);
        } else {
            requestReadImagePermission(1);
        }
    }

    private void requestReadImagePermission(int k) {
        if (k == 1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_222, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.string_580), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }, READ_STORAGE_REQ_CODE);
                            }
                        });
                snackbar.show();
                View view = snackbar.getView();
                ((TextView) view.findViewById(R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, READ_STORAGE_REQ_CODE);
            }
        } else if (k == 0) {
            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_1218, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.string_580), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }, WRITE_STORAGE_REQ_CODE);
                            }
                        });

                snackbar.show();
                View view = snackbar.getView();
                ((TextView) view.findViewById(R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, WRITE_STORAGE_REQ_CODE);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1001) {
            /*Contact option result*/
            businessProfile = (Data.BusinessProfile) data.getSerializableExtra("businessProfile");
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 888:
                    if (data != null) {
                        String bio = data.getStringExtra("bio");
                        businessProfile.setBusinessBio(bio);
                        etBusinessBio.setText(bio);
                    }
                    break;
                case EDIT_EMAIL_REQ_CODE:
                    if (data != null) {
                        String newEmail = data.getStringExtra("email");
                        if (newEmail != null && !newEmail.isEmpty()) {
                            etEmail.setText(newEmail);
                        } else {
                            etEmail.setText(getResources().getString(R.string.enterEmail));
                        }
                    }

                    break;

                case EDIT_FIRST_LAST_NAME:
                    if (data != null) {
                        String editedName = data.getStringExtra("Name");
                        String firstName = data.getStringExtra("FirstName");
                        String lasttName = data.getStringExtra("LastName");
                        if(firstName != null){
                            etName.setText(firstName);
                        }
                        if(lasttName != null){
                            etLastName.setText(lasttName);
                        }
                        /*if (editedName != null) {
                            String[] name = editedName.split("\\s+");
                            etName.setText((name[0] == null) ? "" : name[0]);
//                            etName.setText(editedName);
                            etLastName.setText(name.length > 1 ? (name[1] == null) ? "" : name[1] : "");
                        }*/
                    }
                    break;

                case EDIT_USER_NAME:
                    if (data != null) {
                        String editedUserName = data.getStringExtra(Constants.PROFILE_USERNAME);
                        if (editedUserName != null) {
                            etUsername.setText(editedUserName);
                        }
                    }
                    break;

                case EDIT_BIO_STATUS:
                    if (data != null) {
                        String newStatus = data.getStringExtra(Constants.PROFILE_STATUS);
                        if (newStatus != null && !newStatus.isEmpty()) {
                            etStatus.setText(newStatus);
                        } else {
                            etStatus.setText(getResources().getString(R.string.default_status));
                        }
                    }
                    break;

                case EDIT_KNOWN_AS:
                    if (data != null) {
                        String newStatus = data.getStringExtra(Constants.KNOWN_AS);
                        if (newStatus != null && !newStatus.isEmpty()) {
                            etKnownAs.setText(newStatus);
                        }
                    }
                    break;

                case Constants.Verification.EMAIL_VERIFICATION_REQ:
                    if (data != null && data.getBooleanExtra("emailVerified", true)) {
                        String email = data.getStringExtra("email");
                        etEmail.setText(email);
                    }
                    break;

                case Constants.Verification.NUMBER_VERIFICATION_REQ:
                    if (data != null && data.getBooleanExtra("numberVerified", true)) {
                        String phoneNumber = data.getStringExtra("phoneNumber");
                        String editedCountryCode = data.getStringExtra("countryCode");
//                        tvCountryCode.setText(editedCountryCode);
                        etPhone.setText(phoneNumber);
                    }
                    break;
                case STATUS_UPDATE_REQ_CODE:
                    if (data != null) {
                        String newStatus = data.getStringExtra("updatedValue");
                        if (newStatus != null && !newStatus.isEmpty()) {
                            etStatus.setText(newStatus);
                        } else {
                            etStatus.setText(getResources().getString(R.string.default_status));
                        }
                    }
                    break;

                case FETCH_ADDRESS:
                    //                    if (data != null) {
                    //                        city = data.getStringExtra("city");
                    //                        street = data.getStringExtra("street");
                    //                        zipcode = data.getStringExtra("zipcode");
                    //                        String address = street + "," + city + "-" + zipcode;
                    //                        etAddress.setText(address);
                    //                    }
                    break;
                case FETCH_CATEGORY:
                    if (data != null) {
                        String categoryId = data.getStringExtra("category_id");
                        String category = data.getStringExtra("category");
                        etBusinessCategory.setText(category);
                        businessProfile.setBusinessCategory(category);
                        businessProfile.setBussinessId(categoryId);
                    }
                    break;
                case FETCH_BUSINESS_COUNTRY:
                    if (data != null) {
                        String code = data.getStringExtra("CODE");
                        assert code != null;
                        businessCountryCode = "+" + code.substring(1);
                        etBusinessPhone.setText(businessCountryCode + "" + businessProfile.getPhone().getNumber().replace(businessCountryCode, ""));
                        etBusinessPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(business_max_digits)});
                    }
                    break;
                case FETCH_COUNTRY:
                    if (data != null) {
                        String code = data.getStringExtra("CODE");
                        countryCode = "+" + code.substring(1);
                        etPhone.setText(countryCode + "" + sessionManager.getMobileNumber().replace(countryCode, ""));
                        etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_digits)});
                    }
                    break;
                case VERIFY_BUSINESS_EMAIL:
                    verifiedBusinessEmail = etBusinessEmail.getText().toString().trim();
                    isBusinessEmailVerified = true;
                    break;
                case VERIFY_BUSINESS_PHONE:
                    verifiedBusinessPhone = businessCountryCode + etBusinessPhone.getText().toString().trim();
                    isBusinessPhoneVerified = true;
                    break;

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

                case BUSINESS_NAME:
                    etBusinessName.setText(data.getStringExtra("businessName"));
                    break;
                case BUSINESS_USER_NAME:
                    etBusinessUserName.setText(data.getStringExtra("businessUsername"));
                    break;
                case BUSINESS_BIO:
                    etBusinessBio.setText(data.getStringExtra("businessBio"));
                    break;
                case BUSINESS_WEBSITE:
                    etBusinessWebsite.setText(data.getStringExtra("businessWebsite"));
                    break;
                case BUSINESS_ADDRESS:
                    String city = data.getStringExtra("city");
                    String street = data.getStringExtra("street");
                    String zipcode = data.getStringExtra("zipcode");
                    String address = street + "," + city + "-" + zipcode;
                    etBusinessAddress.setText(address);
                    break;
                case BUSINESS_EMAIL:
                    isBusinessEmailVerified = data.getBooleanExtra("verified", isBusinessEmailVerified);
                    etBusinessEmail.setText(data.getStringExtra("businessEmail"));
                    titleBusinessEmail.setError(getString(isBusinessEmailVerified ? R.string.verified : R.string.not_verified));
                    if (isBusinessEmailVerified) {
                        verifiedBusinessEmail = Objects.requireNonNull(etBusinessEmail.getText()).toString().trim();
                        titleBusinessEmail.setErrorTextAppearance(R.style.inputErrorGreen);
                        titleBusinessEmail.setHelperTextTextAppearance(R.style.inputErrorGreen);
                    } else {
                        titleBusinessEmail.setErrorTextAppearance(R.style.inputErrorRed);
                        titleBusinessEmail.setHelperTextTextAppearance(R.style.inputErrorRed);
                    }
                    break;
                case BUSINESS_PHONE:
                    isBusinessPhoneVerified = data.getBooleanExtra("verified", isBusinessPhoneVerified);
                    businessCountryCode = data.getStringExtra("countryCode");
                    etBusinessPhone.setText(businessCountryCode + "" + data.getStringExtra("businessPhone"));
                    titleBusinessPhone.setError(getString(isBusinessPhoneVerified ? R.string.verified : R.string.not_verified));
                    if (isBusinessPhoneVerified) {
                        verifiedBusinessPhone = Objects.requireNonNull(etBusinessPhone.getText()).toString().trim();
                        titleBusinessPhone.setErrorTextAppearance(R.style.inputErrorGreen);
                        titleBusinessPhone.setHintTextAppearance(R.style.inputErrorGreen);
                    } else {
                        titleBusinessPhone.setErrorTextAppearance(R.style.inputErrorRed);
                        titleBusinessPhone.setHintTextAppearance(R.style.inputErrorRed);
                    }
                    break;
                case PHONE:
                    isPhoneVerified = data.getBooleanExtra("verified", isPhoneVerified);
                    countryCode = data.getStringExtra("countryCode");
                    etPhone.setText(countryCode + "" + data.getStringExtra("mobile"));
                    titlePhone.setError(getString(isPhoneVerified ? R.string.verified : R.string.not_verified));
                    if (isPhoneVerified) {
                        titlePhone.setErrorTextAppearance(R.style.inputErrorGreen);
                        titlePhone.setHintTextAppearance(R.style.inputErrorGreen);
                    } else {
                        titlePhone.setErrorTextAppearance(R.style.inputErrorRed);
                        titlePhone.setHintTextAppearance(R.style.inputErrorRed);
                    }
                    break;
            }
        }
    }

    /**
     * Result of the permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQ_CODE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        presenter.launchCamera(getPackageManager(), isProfile);
                    } else {
                        requestReadImagePermission(0);
                    }
                } else {
                    //camera permission denied msg
                    showSnackMsg(R.string.string_62);
                }
            } else {
                //camera permission denied msg
                showSnackMsg(R.string.string_62);
            }
        } else if (requestCode == READ_STORAGE_REQ_CODE) {

            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    presenter.launchImagePicker(isProfile);
                } else {
                    //Access storage permission denied
                    showSnackMsg(R.string.string_1006);
                }
            } else {
                //Access storage permission denied
                showSnackMsg(R.string.string_1006);
            }
        } else if (requestCode == WRITE_STORAGE_REQ_CODE) {
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(EditProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    presenter.launchCamera(getPackageManager(), isProfile);
                } else {
                    //Access storage permission denied
                    showSnackMsg(R.string.string_1006);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.etName:
                etName.setSelection(etName.getText().toString().trim().length());
                break;
            case R.id.etLastName:
                etLastName.setSelection(etLastName.getText().toString().trim().length());
                break;
            case R.id.etUsername:
                etUsername.setSelection(etUsername.getText().toString().trim().length());
                break;
            case R.id.etStatus:
                etStatus.setSelection(etStatus.getText().toString().trim().length());
                break;
            case R.id.etEmail:
                etStatus.setSelection(etEmail.getText().toString().trim().length());
                break;
            case R.id.etPhone:
                etPhone.setSelection(etPhone.getText().toString().trim().length());
                break;
        }
    }

    @Override
    public void reload() {

    }

    @OnClick(R.id.etBusinessName)
    public void businessName() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "businessName");
        intent.putExtra("value", businessProfile.getBusinessName());
        intent.putExtra("businessUniqueId", businessUniqueId);
        startActivityForResult(intent, BUSINESS_NAME);
    }

    @OnClick(R.id.etBusinessUserName)
    public void businessUsername() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "businessUsername");
        intent.putExtra("value", businessProfile.getBusinessUserName());
        intent.putExtra("businessUniqueId", businessUniqueId);
        startActivityForResult(intent, BUSINESS_USER_NAME);
    }

    @OnClick(R.id.etBusinessBio)
    public void businessBio() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "businessBio");
        intent.putExtra("value", businessProfile.getBusinessBio());
        intent.putExtra("businessUniqueId", businessUniqueId);
        startActivityForResult(intent, BUSINESS_BIO);
    }

    @OnClick(R.id.etBusinessWebsite)
    public void businessWebsite() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "businessWebsite");
        intent.putExtra("value", businessProfile.getWebsite());
        intent.putExtra("businessUniqueId", businessUniqueId);
        startActivityForResult(intent, BUSINESS_WEBSITE);
    }

    @OnClick(R.id.etBusinessCategory)
    public void businessCategory() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "businessCategory");
        intent.putExtra("value", businessProfile.getBusinessCategory());
        intent.putExtra("categoryId", businessProfile.getBussinessId());
        intent.putExtra("businessUniqueId", businessUniqueId);
        startActivityForResult(intent, FETCH_CATEGORY);
    }

    @OnClick(R.id.etBusinessAddress)
    public void businessAddress() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "businessAddress");
        intent.putExtra("value", etBusinessAddress.getText().toString());
        intent.putExtra("city", businessProfile.getBusinessCity());
        intent.putExtra("street", businessProfile.getBusinessStreet());
        intent.putExtra("zipcode", businessProfile.getBusinessZipCode());
        intent.putExtra("businessUniqueId", businessUniqueId);
        startActivityForResult(intent, BUSINESS_ADDRESS);
    }

    @OnClick(R.id.etBusinessEmail)
    public void businessEmail() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "businessEmail");
        intent.putExtra("value", businessProfile.getEmail().getId());
        intent.putExtra("verified", isBusinessEmailVerified);
        intent.putExtra("businessUniqueId", businessUniqueId);
        startActivityForResult(intent, BUSINESS_EMAIL);
    }

    @OnClick(R.id.etEmail)
    public void email() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "email");
        intent.putExtra("value", profileData.getEmail());
        intent.putExtra("verified", isEmailVerified);
        startActivityForResult(intent, EMAIL);
    }

    @OnClick(R.id.etBusinessPhone)
    public void businessPhone() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "businessPhone");
        intent.putExtra("value", businessProfile.getPhone().getNumber().replace(businessCountryCode, ""));
        intent.putExtra("countryCode", businessProfile.getPhone().getCountryCode());
        intent.putExtra("verified", isBusinessPhoneVerified);
        intent.putExtra("businessUniqueId", businessUniqueId);
        startActivityForResult(intent, BUSINESS_PHONE);
    }


    @OnClick(R.id.etPhone)
    public void phone() {
        Intent intent = new Intent(this, EditDetailActivity.class);
        intent.putExtra("key", "mobile");
        intent.putExtra("value", profileData.getNumber().replace(countryCode, ""));
        intent.putExtra("countryCode", profileData.getCountryCode());
        intent.putExtra("verified", isPhoneVerified);
//        intent.putExtra("businessUniqueId", businessUniqueId);
        startActivityForResult(intent, PHONE);
    }

    @OnClick(R.id.etKnownAs)
    public void setEditKnownAs() {
        Intent intent = new Intent(this, EditKnownAsActivity.class);
        intent.putExtra(Constants.KNOWN_AS, profileData.getStarRequest().getStarUserKnownBy());
        startActivityForResult(intent, EDIT_KNOWN_AS);
    }
}
