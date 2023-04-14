package chat.hola.com.app.request_star_profile.request_star;

/**
 * <h1>{@link chat.hola.com.app.request_star_profile.request_star.RequestStarProfileActivity}</h1>
 * <p>This activity class is used to post the details of a star profile , and call the post startProfile API</p>
 * @author: Hardik Karkar
 * @since : 23rd May 2019
 * {@link chat.hola.com.app.request_star_profile.request_star.RequestStarPresenter}
 * {@link chat.hola.com.app.Dialog.ImageSourcePicker}
 *
 */


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.request_star_profile.star_category.Data;
import chat.hola.com.app.request_star_profile.star_category.StarCategoryActivity;
import chat.hola.com.app.star_configuration.EmailVerifyActivity;
import chat.hola.com.app.star_configuration.NumberVerifyActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class RequestStarProfileActivity extends DaggerAppCompatActivity implements RequestStarContract.View {

    private static final int RESULT_CAPTURE_IMAGE = 0;  // this is to check for the camera permissions
    private static final int RESULT_LOAD_IMAGE = 1;  // this is to check permissions to access the library
    Unbinder unbinder;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.rL_category)
    RelativeLayout rL_category;
    @BindView(R.id.tV_category)
    TextView tV_category;
    @BindView(R.id.tV_chooseFile)
    TextView tV_chooseFile;
    @BindView(R.id.tV_status)
    TextView tV_status;
    @BindView(R.id.rL_request)
    RelativeLayout rL_request;
    @BindView(R.id.iV_idProof)
    ImageView iV_idProof;
    @BindView(R.id.eT_userName)
    EditText eT_userName;
    @BindView(R.id.eT_fullName)
    EditText eT_fullName;
    @BindView(R.id.eT_knownAs)
    EditText eT_knownAs;
    @BindView(R.id.eT_email)
    EditText eT_email;
    @BindView(R.id.eT_number)
    EditText eT_number;
    @BindView(R.id.eT_desc)
    EditText eT_desc;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.cbTerms)
    CheckBox cbTerms;

    @Inject
    RequestStarPresenter presenter;
    @Inject
    ImageSourcePicker imageSourcePicker;
    private chat.hola.com.app.profileScreen.model.Data profileData;
    private int CAMERA_REQ_CODE = 24;
    private int READ_STORAGE_REQ_CODE = 26;
    private int WRITE_STORAGE_REQ_CODE = 27;


    /* Call back for taking picture either by camera or gallery*/

    ImageSourcePicker.OnSelectImageSource callback = new ImageSourcePicker.OnSelectImageSource() {
        @Override
        public void onCamera() {
            checkCameraPermissionImage();
        }

        @Override
        public void onGallary() {
            checkReadImage();
        }

        @Override
        public void onCancel() {
            //nothing to do.
        }
    };


    // params which is require for star profile
    /*
     * categorieId - this is the star category id
     * starUserIdProof - this is the id proof that a user needs to upload for getting approved as a star
     */

    private String categorieId, starUserEmail, starUserPhoneNumber, starUserIdProof, starUserKnownBy, description;

    // this is used to validate if the document ( starUserIdProof ) was uploaded correctly or not
    private boolean isImageUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_star_profile);
        unbinder = ButterKnife.bind(this);
        if(presenter!=null)
            presenter.attachView(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        profileData = (chat.hola.com.app.profileScreen.model.Data) getIntent().getSerializableExtra("profileData");

        eT_email.setKeyListener(null);
        eT_number.setKeyListener(null);

        presenter.loadProfileData();
        presenter.getStarStatus();


        SpannableString ss = new SpannableString(cbTerms.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = getString(R.string.termsUrlStar); //change url here
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.color_black));
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }
        };
        ss.setSpan(clickableSpan, 22, 42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        cbTerms.setText(ss);
        cbTerms.setMovementMethod(LinkMovementMethod.getInstance());
        cbTerms.setHighlightColor(Color.BLUE);

    }

    /*click event for making star request*/
    @OnClick(R.id.rL_request)
    public void makeStarReuest() {

        starUserEmail = eT_email.getText().toString().trim();
        starUserPhoneNumber = eT_number.getText().toString().trim();
        starUserKnownBy = eT_knownAs.getText().toString().trim();
        description = eT_desc.getText().toString().trim();

        if (isValidate()) {
            presenter.makeStarProfileRequest(categorieId, starUserEmail, starUserPhoneNumber, starUserIdProof, starUserKnownBy, description);
            rL_request.setEnabled(false);
        }
    }

    /*
    *This method is validating email address
    * */
    public boolean isValidate() {
        if (starUserEmail == null || starUserEmail.isEmpty()) {
            showMessage("Please verify email !",-1);
            return false;
        } else if (starUserPhoneNumber == null || starUserPhoneNumber.isEmpty()) {
            showMessage("Please verify phone number !",-1);
            return false;
        } else if (starUserKnownBy == null || starUserKnownBy.isEmpty()) {
            showMessage("Please enter known as !",-1);
            return false;
        } else if (categorieId == null || categorieId.isEmpty()) {
            showMessage("Please select category !",-1);
            return false;
        } else if (!isImageUpload) {
            showMessage("Please upload id proof !",-1);
            return false;
        } else if (!cbTerms.isChecked()) {
            showMessage("Please accept Star Profile Tems and Services", -1);
            return false;
        } else {
            return true;
        }
    }

    /*click event for email*/
    @OnClick(R.id.eT_email)
    public void verifyEmail() {

        Intent intent = new Intent(RequestStarProfileActivity.this, EmailVerifyActivity.class);
        intent.putExtra("call", Constants.STAR);
        intent.putExtra("profileData", profileData);
        startActivityForResult(intent, 551);
    }

    /*click event for phone number*/
    @OnClick(R.id.eT_number)
    public void verifyPhoneNumber() {
        Intent intent = new Intent(RequestStarProfileActivity.this, NumberVerifyActivity.class);
        intent.putExtra("call", Constants.STAR);
        intent.putExtra("profileData", profileData);
        startActivityForResult(intent, 552);

    }

    /*click event for select category*/
    @OnClick(R.id.rL_category)
    public void starCategory() {
        Intent intent = new Intent(RequestStarProfileActivity.this, StarCategoryActivity.class);
        startActivityForResult(intent, 201);
    }

    @OnClick(R.id.iV_back)
    public void ivBack() {
        onBackPressed();
    }

    /*click event to open dialog of document image*/
    @OnClick(R.id.tV_chooseFile)
    public void chooseFile() {
        imageSourcePicker.setOnSelectImageSource(callback);
        imageSourcePicker.show();
    }



    /* This is method is check runtime camera permission
    *   if not granted then @requestCameraPermissionImage call
    * */
    private void checkCameraPermissionImage() {
        if (ActivityCompat.checkSelfPermission(RequestStarProfileActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(RequestStarProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                presenter.launchCamera(getPackageManager());
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

    /*This method is requesting the camera permission*/
    private void requestCameraPermissionImage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(RequestStarProfileActivity.this,
                Manifest.permission.CAMERA)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_221,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(RequestStarProfileActivity.this, new String[]{Manifest.permission.CAMERA},
                            CAMERA_REQ_CODE);
                }
            });
            snackbar.show();
            View view = snackbar.getView();
            ((TextView) view.findViewById(com.google.android.material.R.id.snackbar_text))
                    .setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            ActivityCompat.requestPermissions(RequestStarProfileActivity.this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQ_CODE);
        }
    }

    /*check the permission for gallery image selection*/
    private void checkReadImage() {
        if (ActivityCompat.checkSelfPermission(RequestStarProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RequestStarProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            presenter.launchImagePicker();
        } else {
            requestReadImagePermission(1);
        }
    }

    /*Requesting read storage permission
    * k=1 for gallery k=0 for camra*/
    private void requestReadImagePermission(int k) {
        if (k == 1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RequestStarProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(RequestStarProfileActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_222,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(RequestStarProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                READ_STORAGE_REQ_CODE);
                    }
                });
                snackbar.show();
                View view = snackbar.getView();
                ((TextView) view.findViewById(com.google.android.material.R.id.snackbar_text))
                        .setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                ActivityCompat.requestPermissions(RequestStarProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        READ_STORAGE_REQ_CODE);
            }
        } else if (k == 0) {
            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(RequestStarProfileActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(RequestStarProfileActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_1218,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(RequestStarProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_STORAGE_REQ_CODE);
                    }
                });

                snackbar.show();
                View view = snackbar.getView();
                ((TextView) view.findViewById(com.google.android.material.R.id.snackbar_text))
                        .setGravity(Gravity.CENTER_HORIZONTAL);
            } else {
                ActivityCompat.requestPermissions(RequestStarProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_STORAGE_REQ_CODE);
            }
        }
    }


    @Override
    protected void onDestroy() {
        unbinder.unbind();
        if(presenter!=null) presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 201 && data != null) {
            /*callback category selection*/
            Data categoryData = (Data) data.getSerializableExtra("categoryData");
            categorieId = categoryData.getId();
            tV_category.setText(categoryData.getCategorie());
        } else if (resultCode == 551) {
            /*callback email verify*/
            eT_email.setText(data.getStringExtra("email"));
        } else if (resultCode == 552) {
            /*callback phone number verify*/
            eT_number.setText(data.getStringExtra("phoneNumber"));
        }

        /*These are the permissions callback*/
        else if (requestCode == RESULT_LOAD_IMAGE) {

            presenter.parseSelectedImage(requestCode, resultCode, data);

        } else if (requestCode == RESULT_CAPTURE_IMAGE) {
            presenter.parseCapturedImage(requestCode, resultCode, data);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            presenter.parseCropedImage(requestCode, resultCode, data);
        }

    }

    @Override
    public void launchCamera(Intent intent) {
        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
    }

    @Override
    public void showSnackMsg(int msgId) {
        String msg = getResources().getString(msgId);
        Snackbar snackbar = Snackbar.make(root, "" + msg,
                Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        ((TextView) view.findViewById(com.google.android.material.R.id.snackbar_text))
                .setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void launchImagePicker(Intent intent) {
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void launchCropImage(Uri data) {
        CropImage.activity(data)
                .start(this);
    }

    @Override
    public void showProgress(boolean b) {

        if(progressBar!=null) {
            if (b)
                progressBar.setVisibility(View.VISIBLE);
            else
                progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void imageUploadSuccess(String imgUrl) {
        isImageUpload = true;
        starUserIdProof = imgUrl;

        if (starUserIdProof != null) {
            try {
                Glide.with(this)
                        .load(starUserIdProof)
                        .centerCrop()
                        .into(iV_idProof);
            }catch (IllegalArgumentException | NullPointerException e){
            }
            iV_idProof.setVisibility(View.VISIBLE);
        }
        tV_chooseFile.setText(getString(R.string.edit));
    }

    @Override
    public void requestDone() {
        openSuccessDialog();
    }

    private void openSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verified_star_email_phone_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.imageView);

        TextView textView = dialog.findViewById(R.id.tV_msg);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        RelativeLayout rL_ok = dialog.findViewById(R.id.rL_ok);

        //imageView.setImageDrawable(getDrawable(R.drawable.ic_email_verified));
        imageView.setVisibility(View.GONE);
        textView.setText(getString(R.string.star_request_done));

        rL_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.show();

    }

    @Override
    public void showStarData(Profile body) {
        profileData = body.getData().get(0);

        if (profileData.getUserName() != null)
            eT_userName.setText(profileData.getUserName());

        if (profileData.getFirstName() != null)
            eT_fullName.setText(CommonClass.createFullName(profileData.getFirstName(),profileData.getLastName()));

        if (profileData.getEmail() != null)
            eT_email.setText(profileData.getEmail());

        if (profileData.getNumber() != null )
            eT_number.setText(profileData.getNumber());


    }

    // In this method we made ui changes.
    // starUserProfileStatusCode	1,         2,        3,      4
    // starUserProfileStatusText	pending,notApproved,suspend,approved
    @Override
    public void updateStatus(chat.hola.com.app.request_star_profile.model.Data data) {

        if (data.getStarUserProfileStatusText() != null)
            tV_status.setText(data.getStarUserProfileStatusText());

        if (data.getStarUserKnownBy() != null)
            eT_knownAs.setText(data.getStarUserKnownBy());

        if (data.getStarUserEmail() != null)
            eT_email.setText(data.getStarUserEmail());

        if (data.getStarUserPhoneNumber() != null)
            eT_number.setText(data.getStarUserPhoneNumber());

        if (data.getStarUserIdProof() != null)
            starUserIdProof = data.getStarUserIdProof();

        if (data.getCategorieId() != null)
            categorieId = data.getCategorieId();

        if(data.getCategoryName()!=null)
            tV_category.setText(data.getCategoryName());

        if(data.getDescription()!=null)
            eT_desc.setText(data.getDescription());

        if (starUserIdProof != null) {
            isImageUpload = true;
            tV_chooseFile.setText(getString(R.string.edit));
            Glide.with(this)
                    .load(starUserIdProof)
                    .centerCrop()
                    .into(iV_idProof);
            iV_idProof.setVisibility(View.VISIBLE);
        }

       /* int code = 0; // this  variable denotes the status of the star profile

        if (data.getStarUserProfileStatusCode() != null)
            code = data.getStarUserProfileStatusCode();

        if (code == 1 || code == 3) {
            eT_email.setEnabled(false);
            eT_number.setEnabled(false);
            eT_knownAs.setEnabled(false);
            rL_request.setEnabled(false);
            tV_chooseFile.setEnabled(false);
        }
        else if (code == 4) {
            eT_knownAs.setEnabled(false);
            rL_request.setEnabled(false);
            tV_chooseFile.setEnabled(false);
            tV_chooseFile.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if(!isFinishing()) {
            Snackbar mSnackBar = Snackbar.make(root, msg, Snackbar.LENGTH_LONG);
            View view = mSnackBar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            mSnackBar.show();
        }
    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void userBlocked() {

    }

    @Override
    public void reload() {

    }
}
