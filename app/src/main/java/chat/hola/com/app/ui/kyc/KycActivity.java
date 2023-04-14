package chat.hola.com.app.ui.kyc;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Activities.ChatCameraActivity;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.ui.validate.ValidateActivity;

/**
 * <h1>KycActivity</h1>
 * <p>All KYC functionalities</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 23 Jan 2020
 */
public class KycActivity extends BaseActivity implements KycContract.View, Constants.Image {

    @Inject
    TypefaceManager font;
    @Inject
    Loader loader;
    @Inject
    KycPresenter presenter;
    @Inject
    SessionManager sessionManager;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.spDocType)
    Spinner spDocType;
    @BindView(R.id.etDocumentNumber)
    EditText etDocumentNumber;
    @BindView(R.id.etHolderName)
    EditText etHolderName;
    @BindView(R.id.ivFrontSideDoc)
    ImageView ivFrontSideDoc;
    @BindView(R.id.ibRemoveFrontSideDoc)
    ImageView ibRemoveFrontSideDoc;
    @BindView(R.id.ivBackSideDoc)
    ImageView ivBackSideDoc;
    @BindView(R.id.ibRemoveBackSideDoc)
    ImageView ibRemoveBackSideDoc;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private Map<String, Object> params;
    private String documentName, documentNumber, documentHolderName, documentFrontImage = "", documentBackImage = "";
    private ImageSourcePicker imageSourcePicker;
    private boolean isFrontSide;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_kyc;
    }

    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(font.getSemiboldFont());
    }

    @Override
    public void initView() {
        super.initView();
        showLoader();
        presenter.attach(this);
        params = (Map<String, Object>) getIntent().getSerializableExtra("params");
        presenter.typesOfDocuments();
        tvTitle.setText(R.string.kyc_title);

        spDocType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0)
                    UiValidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etHolderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etHolderName.getText().toString().length() > 5)
                    UiValidate();
            }
        });

        etDocumentNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etDocumentNumber.getText().toString().length() > 5)
                    UiValidate();
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case LOAD_FRONT_IMAGE:
                case LOAD_BACK_IMAGE:
                    presenter.parseSelectedImage(requestCode, resultCode, data);
                    break;

                case CAPTURE_FRONT_IMAGE:
                case CAPTURE_BACK_IMAGE:
                    presenter.parseCapturedImage(requestCode, resultCode, data);
                    break;

                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                    presenter.parseCropedImage(requestCode, resultCode, data, isFrontSide);
                    break;
            }
    }

    @OnClick(R.id.ibBack)
    public void backPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.btnSubmit)
    public void verify() {
        if (validate()) {
            params = new HashMap<>();
            params.put("userId", sessionManager.getUserId());
            params.put("userType", "user");
            params.put("userName", sessionManager.getUserName());
            params.put("email", sessionManager.getEmail());
            params.put("firstName", sessionManager.getFirstName());
            params.put("lastName", sessionManager.getLastName());
            params.put("phoneNo", sessionManager.getMobileNumber());
            params.put("documentName", documentName);
            params.put("documentNumber", documentNumber);
            params.put("documentHolderName", documentHolderName);
            presenter.updateUserDetail(params);
        }
    }

    @OnClick(R.id.ivFrontSideDoc)
    public void uploadFrontDoc() {
        isFrontSide = true;
        cameraAndGalleryPermissions();
    }

    @OnClick(R.id.ivBackSideDoc)
    public void uploadBackDoc() {
        isFrontSide = false;
        cameraAndGalleryPermissions();
    }

    // check if camera and gallery access permission, if not then ask
    private void cameraAndGalleryPermissions() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            imageSourcePicker = new ImageSourcePicker(KycActivity.this, true, true);
                            imageSourcePicker.setOnSelectImage(callbackProfile);
                            imageSourcePicker.show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            message(R.string.permission_requires);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }


    /**
     * Validates all fields
     *
     * @return true: all fields are valid, false: data is not valid
     */
    private boolean validate() {
        documentName = spDocType.getSelectedItem().toString();
        documentNumber = etDocumentNumber.getText().toString();
        documentHolderName = etHolderName.getText().toString();
        if (documentName.isEmpty() || documentName.equals(getString(R.string.select_document))) {
            message(getString(R.string.error_select_doc));
            return false;
        }
        if (documentNumber.isEmpty()) {
            etDocumentNumber.setError(getString(R.string.error_mandatory));
            return false;
        }
        if (documentHolderName.isEmpty()) {
            etHolderName.setError(getString(R.string.error_mandatory));
            return false;
        }
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
        loader.show();
        btnSubmit.setEnabled(false);
    }

    @Override
    public void hideLoader() {
        if (loader.isShowing())
            loader.dismiss();
        btnSubmit.setEnabled(true);
    }

    @Override
    public void moveNext() {
        hideLoader();
        startActivity(new Intent(this, ValidateActivity.class)
                .putExtra("image", R.drawable.ic_wallet_image)
                .putExtra("title", getString(R.string.kyc_applied))
                .putExtra("message", getString(R.string.kyc_applied_message)));
        finish();
    }

    @Override
    public void typesOfDocuments(List<String> response) {
        String[] documents = new String[response.size() + 1];
        int i = 0;
        documents[i++] = getString(R.string.select_document);
        for (String s : response) {
            documents[i++] = s;
        }
        hideLoader();
        ArrayAdapter<String> docAdapter = new ArrayAdapter<>(this, R.layout.select_dialog_item, documents);
        spDocType.setAdapter(docAdapter);
    }

    ImageSourcePicker.OnSelectImage callbackProfile = new ImageSourcePicker.OnSelectImage() {

        @Override
        public void onCamera() {
            Intent intent = new Intent(KycActivity.this, ChatCameraActivity.class);
            intent.putExtra("requestType", "EditProfile");
            startActivityForResult(intent, isFrontSide ? CAPTURE_FRONT_IMAGE : CAPTURE_BACK_IMAGE);
        }

        @Override
        public void onGallary() {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("image/*");
            startActivityForResult(intent, isFrontSide ? LOAD_FRONT_IMAGE : LOAD_BACK_IMAGE);
        }

        @Override
        public void onCancel() {
            //nothing to do.
        }
    };

    @Override
    public void launchCropImage(Uri data) {
        CropImage.activity(data).setFixAspectRatio(false).start(this);
    }

    @Override
    public void setFrontImage(Bitmap bitmap, String uri) {
        documentFrontImage = uri;
        ivFrontSideDoc.setImageBitmap(bitmap);
        ibRemoveFrontSideDoc.setVisibility(View.VISIBLE);
        UiValidate();
    }

    @Override
    public void setBackImage(Bitmap bitmap, String uri) {
        documentBackImage = uri;
        ivBackSideDoc.setImageBitmap(bitmap);
        ibRemoveBackSideDoc.setVisibility(View.VISIBLE);
        UiValidate();
    }

    @OnClick(R.id.ibRemoveBackSideDoc)
    public void removeBackDoc() {
        documentBackImage = "";
        ivBackSideDoc.setImageDrawable(getDrawable(R.drawable.ic_image));
        ibRemoveBackSideDoc.setVisibility(View.GONE);
        UiValidate();
    }

    @OnClick(R.id.ibRemoveFrontSideDoc)
    public void removeFrontDoc() {
        documentFrontImage = "";
        ivFrontSideDoc.setImageDrawable(getDrawable(R.drawable.ic_image));
        ibRemoveFrontSideDoc.setVisibility(View.GONE);
        UiValidate();
    }

    // Handles btnSubmit, sets visible if all mandatory fields are filled
    private void UiValidate() {
        boolean isVisible = !documentFrontImage.isEmpty() &&
                !documentBackImage.isEmpty() &&
                !spDocType.getSelectedItem().toString().equals(getString(R.string.select_document)) &&
                !etDocumentNumber.getText().toString().isEmpty() &&
                !etHolderName.getText().toString().isEmpty();
        btnSubmit.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
