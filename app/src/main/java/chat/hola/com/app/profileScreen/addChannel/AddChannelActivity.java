package chat.hola.com.app.profileScreen.addChannel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.ConnectivityReceiver;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.category.CategoryActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.post.model.CategoryData;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;

import dagger.android.support.DaggerAppCompatActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ankit on 19/2/18.
 */

public class AddChannelActivity extends DaggerAppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener, AddChannelContract.View {

    private final String TAG = AppCompatActivity.class.getSimpleName();
    private int CAMERA_REQ_CODE = 24;
    private int READ_STORAGE_REQ_CODE = 26;
    private int WRITE_STORAGE_REQ_CODE = 27;

    private static final int RESULT_CAPTURE_IMAGE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;

    @Inject
    AddChannelPresenter presenter;

    @Inject
    TypefaceManager typefaceManager;

    //    @Inject
    //    CategoryDialog categoryDialog;
    //
    //    @Inject
    //    CategoryAdapter categoryPicker;

    @Inject
    ImageSourcePicker imageSourcePicker;

    @Inject
    SessionManager sessionManager;

    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.tvCreateChannel)
    TextView tvCreateChannel;

    @BindView(R.id.btnUploadPhoto)
    Button btnUploadPhoto;
    @BindView(R.id.tvPrivateChannel)
    TextView tvPrivateChannel;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.flAddProfile)
    FrameLayout flAddProfile;
    @BindView(R.id.etChannelName)
    EditText etChannelName;
    @BindView(R.id.switchPrivateChannel)
    SwitchCompat switchPrivateChannel;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.tvCategoryName)
    TextView tvCategoryName;

    boolean isEdit;
    private Unbinder unbinder;
    private Bitmap bitmap, bitmapToUpload;
    private String categoryId = null;
    private ProgressDialog dialog;
    private InputMethodManager imm;
    private String channelId;
    @Inject
    BlockDialog dialog1;
    private String imageUrl = null;
    private String default_image =
            "http://res.cloudinary.com/dqodmo1yc/image/upload/v1534845919/default/200.jpg";

    @Override
    public void userBlocked() {
        dialog1.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchannel);
        unbinder = ButterKnife.bind(this);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        ChannelData data = (ChannelData) getIntent().getSerializableExtra("data");

        Glide.with(getBaseContext())
                .load(default_image)
                .asBitmap()
                .centerCrop()
                //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .into(ivProfile);

        btnSave.setText(isEdit ? "Update" : "Create");
        if (isEdit) {
            tvCreateChannel.setText("Update Channel");
            try {

                Glide.with(getBaseContext())
                        .load(data.getChannelImageUrl())
                        .asBitmap()
                        .centerCrop()
                        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .into(ivProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            etChannelName.setText(data.getChannelName());
            tvCategoryName.setText(data.getCategoryName());
            categoryId = data.getCategoryId();
            channelId = data.getId();
            imageUrl = data.getChannelImageUrl();
            switchPrivateChannel.setChecked(data.getPrivate());
        } else {
            imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        }
        showKeyboard();
        presenter.init();
        progressBarSetup();
    }

    private void showKeyboard() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                etChannelName.requestFocus();
                if (imm != null) {
                    imm.showSoftInput(etChannelName, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    private void hideKeyboard() {
        if (imm != null) {
            imm.hideSoftInputFromWindow(etChannelName.getWindowToken(), 0);
            //            imm.hideSoftInputFromWindow(etDescription.getWindowToken(), 0);
        }
    }

    private void progressBarSetup() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.channelProgressMsg));
        dialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.ivProfile, R.id.btnUploadPhoto})
    public void setIvProfile() {
        hideKeyboard();
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
                //nothing to do
            }
        };
        imageSourcePicker.setOnSelectImageSource(callback);
        imageSourcePicker.show();
    }

    @OnClick(R.id.tvCategoryName)
    public void tvCategoryName() {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId", categoryId);
        hideKeyBoard();
        startActivityForResult(intent, 222);
    }
    @OnClick(R.id.tvCategory)
    public void category() {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("categoryId", categoryId);
        hideKeyBoard();
        startActivityForResult(intent, 222);
    }

    @Override
    public void applyFont() {
        btnCancel.setTypeface(typefaceManager.getSemiboldFont());
        btnSave.setTypeface(typefaceManager.getSemiboldFont());
        btnUploadPhoto.setTypeface(typefaceManager.getMediumFont());
        tvPrivateChannel.setTypeface(typefaceManager.getMediumFont());
        tvCreateChannel.setTypeface(typefaceManager.getSemiboldFont());
        //tvAddCategory.setTypeface(typefaceManager.getRegularFont());
        tvCategory.setTypeface(typefaceManager.getMediumFont());
        tvCategoryName.setTypeface(typefaceManager.getSemiboldFont());
        etChannelName.setTypeface(typefaceManager.getMediumFont());
        //        etDescription.setTypeface(typefaceManager.getMediumFont());
    }

    @OnClick(R.id.btnCancel)
    public void cancel() {
        onBackPressed();
        hideKeyBoard();
    }

    @OnClick(R.id.btnSave)
    public void save() {
        //need to call some api method
        if (etChannelName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(categoryId)) {
            Toast.makeText(this, "Please select category", Toast.LENGTH_SHORT).show();
        } else {
            if (imageUrl == null || imageUrl.isEmpty()) imageUrl = default_image;
            if (isEdit) {
                presenter.updateChannel(imageUrl, channelId, etChannelName.getText().toString(),
                        etChannelName.getText().toString(), switchPrivateChannel.isChecked(), categoryId);
            } else {
                //                if (etDescription.getText().toString().isEmpty())
                //                    etDescription.setText(etChannelName.getText().toString());
                presenter.addChannel(etChannelName.getText().toString(), etChannelName.getText().toString(),
                        switchPrivateChannel.isChecked(), categoryId);
            }
        }
        //onBackPressed();
    }

    //    @OnClick(R.id.tvCategory)
    //    public void category() {
    ////        categoryDialog.setCategorySelectCallback(this);
    ////        categoryDialog.show(getSupportFragmentManager(), "categoryDialog");
    ////        categoryPicker.notifyDataSetChanged();
    //        hideKeyBoard();
    //        startActivityForResult(new Intent(AddChannelActivity.this, CategoryActivity.class),222);
    //    }

    private void hideKeyBoard() {
        if (imm != null) {
            imm.hideSoftInputFromWindow(etChannelName.getWindowToken(), 0);
            //            imm.hideSoftInputFromWindow(etDescription.getWindowToken(), 0);
        }
    }

    @Override
    public void showCategories(List<CategoryData> categories) {
        //        this.categoryId = categories.get(0).getId();
        //        tvCategory.setText(categories.get(0).getCategoryName());
        //        categoryPicker.setData(getBaseContext(), categories);
    }

    @Override
    public void showProgress(boolean show) {
        if (show && dialog != null && !dialog.isShowing()) {
            dialog.show();
        } else if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void launchCamera(Intent intent) {
        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
    }

    @Override
    public void showSnackMsg(int msgId) {
        String msg = getResources().getString(msgId);
        Snackbar snackbar = Snackbar.make(root, "" + msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        ((TextView) view.findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(
                Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void launchImagePicker(Intent intent) {
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void launchCropImage(Uri data) {
        CropImage.activity(data).start(this);
    }

    @Override
    public void setProfileImage(Bitmap bitmap) {
        ivProfile.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        super.onBackPressed();
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
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @OnClick(R.id.flAddProfile)
    void addProfile() {
        //launch camera or gallary.
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

    private void checkCameraPermissionImage() {
        if (ActivityCompat.checkSelfPermission(AddChannelActivity
                .this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(AddChannelActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

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

    private void requestCameraPermissionImage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddChannelActivity.this,
                Manifest.permission.CAMERA)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_221, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.string_580), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ActivityCompat.requestPermissions(AddChannelActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, 24);
                        }
                    });

            snackbar.show();

            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {

            ActivityCompat.requestPermissions(AddChannelActivity.this,
                    new String[]{Manifest.permission.CAMERA}, 24);
        }
    }

    /**
     * Check access gallery permission
     */
    private void checkReadImage() {
        if (ActivityCompat.checkSelfPermission(AddChannelActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AddChannelActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            presenter.launchImagePicker();
        } else {
            requestReadImagePermission(1);
        }
    }

    private void requestReadImagePermission(int k) {
        if (k == 1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddChannelActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddChannelActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_222, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.string_580), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(AddChannelActivity.this, new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }, READ_STORAGE_REQ_CODE);
                            }
                        });
                snackbar.show();
                View view = snackbar.getView();
                ((TextView) view.findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(
                        Gravity.CENTER_HORIZONTAL);
            } else {
                ActivityCompat.requestPermissions(AddChannelActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, READ_STORAGE_REQ_CODE);
            }
        } else if (k == 0) {
            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddChannelActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(AddChannelActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_1218, Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.string_580), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                ActivityCompat.requestPermissions(AddChannelActivity.this, new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                }, WRITE_STORAGE_REQ_CODE);
                            }
                        });

                snackbar.show();
                View view = snackbar.getView();
                ((TextView) view.findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(
                        Gravity.CENTER_HORIZONTAL);
            } else {
                ActivityCompat.requestPermissions(AddChannelActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, WRITE_STORAGE_REQ_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 222 && resultCode == RESULT_OK) {
            tvCategoryName.setText(data.getStringExtra("category"));
            categoryId = data.getStringExtra("category_id");
        } else if (requestCode == RESULT_LOAD_IMAGE) {

            presenter.parseSelectedImage(requestCode, resultCode, data);
        } else if (requestCode == RESULT_CAPTURE_IMAGE) {

            presenter.parseCapturedImage(requestCode, resultCode, data);
            imageUrl = null;
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            presenter.parseCropedImage(requestCode, resultCode, data);
        }
    }

    /**
     * Result of the permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQ_CODE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(AddChannelActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(AddChannelActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        presenter.launchCamera(getPackageManager());
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

                if (ActivityCompat.checkSelfPermission(AddChannelActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    presenter.launchImagePicker();
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

                if (ActivityCompat.checkSelfPermission(AddChannelActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    presenter.launchCamera(getPackageManager());
                } else {
                    //Access storage permission denied
                    showSnackMsg(R.string.string_1006);
                }
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    }

    @Override
    public void reload() {

    }
}
