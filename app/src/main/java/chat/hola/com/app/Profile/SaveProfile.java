package chat.hola.com.app.Profile;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.DownloadFile.FileUploadService;
import chat.hola.com.app.DownloadFile.FileUtils;
import chat.hola.com.app.DownloadFile.ServiceGenerator;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.RoundedImageView;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.account.AccountGeneral;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.onboarding.OnBoardingActivity;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moda on 25/07/17.
 */

public class SaveProfile extends DaggerAppCompatActivity implements UploadCallback {
    private static final int MULTIPLE_PERMISSIONS = 1001;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    private static final int RESULT_CAPTURE_IMAGE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private String picturePath = null;
    private String requestId;
    private Bitmap bitmap, bitmapToUpload;
    private Uri imageUri;
    private Bus bus = AppController.getBus();
    private boolean userAlreadyHasImage = false;
    private String userImageUrl = null;
    private static final int IMAGE_QUALITY = 50;//change it to higher level if want,but then slower image uploading/downloading
    private InputMethodManager imm;
    private AlertDialog.Builder builder;
    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private RelativeLayout root;
    private TextView tvSetup, tvProfile;
    private EditText etFullName, etUserName;
    private RoundedImageView profilePic;
    private RadioButton rbPrivate;
    private ImageButton ibNext;
    private String firstName = "", lastName = "", userName;
    private TypefaceManager typefaceManager;

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_profile);

        profilePic = (RoundedImageView) findViewById(R.id.selectImage);
        root = (RelativeLayout) findViewById(R.id.root);
        etFullName = (EditText) findViewById(R.id.etFullName);
        etUserName = (EditText) findViewById(R.id.etUserName);
        tvProfile = (TextView) findViewById(R.id.tvProfile);
        tvSetup = (TextView) findViewById(R.id.tvSetup);
        rbPrivate = (RadioButton) findViewById(R.id.rbPrivate);
        ibNext = (ImageButton) findViewById(R.id.ibNext);

        typefaceManager = new TypefaceManager(this);
        tvSetup.setTypeface(typefaceManager.getBoldFont());
        tvProfile.setTypeface(typefaceManager.getBoldFont());
        etUserName.setTypeface(typefaceManager.getRegularFont());
        etFullName.setTypeface(typefaceManager.getRegularFont());
        rbPrivate.setTypeface(typefaceManager.getRegularFont());

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permission to access your photo, please grant");
        String profilePicPath = getIntent().getStringExtra("path");

        etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String[] fullname = etFullName.getText().toString().split("\\s+");
                firstName = fullname[0];
                if (fullname.length > 1 && !fullname[1].isEmpty())
                    lastName = fullname[1];
                else
                    lastName = "";
            }
        });

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                userName = etUserName.getText().toString();
            }
        });

        rbPrivate.setChecked(getIntent().getIntExtra("private", 0) == 1);

        if (profilePicPath != null) {
            profilePic.setImageBitmap(BitmapFactory.decodeFile(profilePicPath));
            try {
                requestId = MediaManager.get().upload(profilePicPath)
                        .option(Constants.Post.RESOURCE_TYPE, "image")
                        .option("folder", "profile")
                        .callback(this)
                        .constrain(TimeWindow.immediate())
                        .dispatch();
            } catch (Exception ignored) {
            }
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            SessionManager sessionManager = new SessionManager(this);

            rbPrivate.setChecked(extras.getInt("private") == 1);

            String name = "";
            if ((extras.containsKey("firstName") && !extras.getString("firstName").equals("") && !extras.getString("firstName").equals("null"))) {
                firstName = extras.getString("firstName");
                name = firstName;
            }

            if ((extras.containsKey("lastName") && !extras.getString("lastName").equals("") && !extras.getString("lastName").equals("null"))) {
                lastName = extras.getString("lastName");
                name = firstName + " " + lastName;
            }

            etFullName.setText(name);
            etFullName.setSelection(etFullName.getText().length());
            if (extras.containsKey("userName")) {
                userName = extras.getString("userName");
//                if (userName == null || userName.isEmpty()) {
//                    enterName.setEnabled(true);
//                } else {
//                    enterName.setEnabled(false);}
                if (userName != null || !userName.isEmpty()) {
                    etUserName.setText(userName);
                }

            }
            if (extras.containsKey("profilePic")) {
                userAlreadyHasImage = true;
                try {
                    userImageUrl = extras.getString("profilePic");
                    sessionManager.setUserProfilePic(userImageUrl, true);
                    Glide.with(this).load(userImageUrl).asBitmap().centerCrop()
                            .placeholder(R.drawable.profile_one).into(new BitmapImageViewTarget(profilePic));
//                    deleteImage.setVisibility(View.VISIBLE);
//                    addImage.setVisibility(View.GONE);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } else {

                userAlreadyHasImage = false;
                userImageUrl = null;
            }
        } else {
            fetchUserDetailsFromServer();
        }

        ibNext.setEnabled(true);
        ibNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if (validateUserDetails()) {

                    //TODO remove below after api changes
                    if (picturePath == null && (!userAlreadyHasImage)) {
                        uploadProfileDetailsToserver(null);

                    } else {

                        uploadProfilePic();
                    }

                }

            }
        });


        profilePic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideKeyboard();
                Dexter.withActivity(SaveProfile.this)
                        .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            etFullName.requestFocus();
                                            if (imm != null) {
                                                imm.hideSoftInputFromWindow(SaveProfile.this.getCurrentFocus().getWindowToken(), 0);
                                            }
                                        }
                                    }, 200);
                                    Intent intent = new Intent(getApplicationContext(), DeeparFiltersTabCameraActivity.class);
                                    intent.putExtra("call", "SaveProfile");
                                    if (!TextUtils.isEmpty(userName))
                                        intent.putExtra("userName", userName);
                                    if (!TextUtils.isEmpty(firstName))
                                        intent.putExtra("firstName", firstName);
                                    if (!TextUtils.isEmpty(lastName))
                                        intent.putExtra("lastName", lastName);
                                    intent.putExtra("private", rbPrivate.isChecked());

                                    startActivity(intent);
                                }

                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    // permission is denied permenantly, navigate user to app settings
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
        });


        bus.register(this);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                etFullName.requestFocus();
                if (imm != null) {
                    imm.showSoftInput(etFullName, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void hideKeyboard() {
        if (imm != null) {
            imm.hideSoftInputFromWindow(etFullName.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("file_uri", imageUri);
        outState.putString("file_path", picturePath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable("file_uri");
            picturePath = savedInstanceState.getString("file_path");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMAGE) {

            if (resultCode == Activity.RESULT_OK) {
                try {


                    picturePath = getPath(SaveProfile.this, data.getData());

                    if (picturePath != null) {
//                        bitmap = BitmapFactory.decodeFile(picturePath);
//                        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {

                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(picturePath, options);
//                        bitmap = BitmapFactory.decodeFile(picturePath, options);

//                        BitmapFactory.decodeFile(picturePath, options);
                        if (options.outWidth > 0 && options.outHeight > 0) {


//                            profilePic.setImageBitmap(bitmap);
//
//                            userAlreadyHasImage = false;
//
//
//                            userImageUrl = null;
//
//                            deleteImage.setVisibility(View.VISIBLE);
                            //        addImage.setVisibility(View.GONE);

                            /*
                             * Have to start the intent for the image cropping
                             */
                            CropImage.activity(data.getData())
                                    .start(this);


                        } else {

                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, R.string.string_31, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }


                        }

                    } else {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_31, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }
                } catch (OutOfMemoryError e) {
                    Snackbar snackbar = Snackbar.make(root, R.string.string_15, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            } else {
                if (resultCode == Activity.RESULT_CANCELED) {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_16, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                } else {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_113, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            }
        } else if (requestCode == RESULT_CAPTURE_IMAGE) {


            if (resultCode == Activity.RESULT_OK) {
                try {
                    // picturePath = getPath(Deal_Add.this, imageUri);


                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(picturePath, options);


                    if (options.outWidth > 0 && options.outHeight > 0) {


                        CropImage.activity(imageUri)
                                .start(this);


                    } else {


                        picturePath = null;
                        Snackbar snackbar = Snackbar.make(root, R.string.string_17, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                    }
                } catch (OutOfMemoryError e) {

                    picturePath = null;
                    Snackbar snackbar = Snackbar.make(root, R.string.string_15, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            } else {


                if (resultCode == Activity.RESULT_CANCELED) {

                    picturePath = null;
                    Snackbar snackbar = Snackbar.make(root, R.string.string_18, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                } else {

                    picturePath = null;
                    Snackbar snackbar = Snackbar.make(root, R.string.string_17, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            try {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    picturePath = getPath(SaveProfile.this, result.getUri());

                    if (picturePath != null) {
                        bitmapToUpload = BitmapFactory.decodeFile(picturePath);
                        bitmap = getCircleBitmap(bitmapToUpload);
                        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                            profilePic.setImageBitmap(bitmap);
                            userAlreadyHasImage = false;
                            userImageUrl = null;
//                            deleteImage.setVisibility(View.VISIBLE);
//                            addImage.setVisibility(View.GONE);
                        } else {
                            picturePath = null;
                            if (root != null) {
                                Snackbar snackbar = Snackbar.make(root, R.string.string_19, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                        }
                    } else {
                        picturePath = null;
                        if (root != null) {
                            Snackbar snackbar = Snackbar.make(root, R.string.string_19, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    picturePath = null;
                    if (root != null) {
                        Snackbar snackbar = Snackbar.make(root, R.string.string_19, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                }

            } catch (OutOfMemoryError e) {
                picturePath = null;
                Snackbar snackbar = Snackbar.make(root, R.string.string_15, Snackbar.LENGTH_SHORT);
                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            }

        }

    }

    private void selectImage() {
        hideKeyboard();
        Intent intent = new Intent(this, DeeparFiltersTabCameraActivity.class);
        intent.putExtra("call", "SaveProfile");
        if (!TextUtils.isEmpty(userName))
            intent.putExtra("userName", userName);
        if (!TextUtils.isEmpty(firstName))
            intent.putExtra("firstName", firstName);
        if (!TextUtils.isEmpty(lastName))
            intent.putExtra("lastName", lastName);
        else
            intent.putExtra("lastName", "");
        intent.putExtra("private", rbPrivate.isChecked());

        startActivity(intent);
        androidx.appcompat.app.AlertDialog.Builder builder;
        builder = new androidx.appcompat.app.AlertDialog.Builder(SaveProfile.this);
        builder.setTitle(R.string.string_255);
        // builder.setIcon(R.drawable.orca_attach_camera_pressed);
        builder.setItems(new CharSequence[]{getString(R.string.string_1021),
                        getString(R.string.string_1022), getString(R.string.cancel)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 1:
                                checkCameraPermissionImage();
                                dialog.dismiss();
                                break;
                            case 0:
                                checkReadImage();
                                break;
                            case 2:
                                /* Do Nothing here */
                                break;
                        }
                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Check access gallery permission\
     */
    private void checkReadImage() {
        if (ActivityCompat.checkSelfPermission(SaveProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SaveProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getString(R.string.string_1020)), RESULT_LOAD_IMAGE);
            } else {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.string_1020)), RESULT_LOAD_IMAGE);
            }


        } else {

            requestReadImagePermission(1);
        }

    }

    /**
     * Request access gallery permission
     */
    private void requestReadImagePermission(int k) {

        if (k == 1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SaveProfile.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SaveProfile.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_222,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SaveProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                26);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SaveProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        26);
            }
        } else if (k == 0) {




            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(SaveProfile.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SaveProfile.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_1218,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SaveProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                27);
                    }
                });

                snackbar.show();

                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SaveProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        27);
            }


        }
    }

    /**
     * Check access camera permission
     */
    private void checkCameraPermissionImage() {
        if (ActivityCompat.checkSelfPermission(SaveProfile.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(SaveProfile.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                    } else {


                        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }


                    }

                    startActivityForResult(intent, RESULT_CAPTURE_IMAGE);

                } else {
                    Snackbar snackbar = Snackbar.make(root, R.string.string_61,
                            Snackbar.LENGTH_SHORT);
                    snackbar.show();


                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
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

    /**
     * Request access camera permission
     */
    private void requestCameraPermissionImage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SaveProfile.this,
                Manifest.permission.CAMERA)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_221,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions(SaveProfile.this, new String[]{Manifest.permission.CAMERA},
                            24);
                }
            });


            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);


        } else {


            ActivityCompat.requestPermissions(SaveProfile.this, new String[]{Manifest.permission.CAMERA},
                    24);
        }
    }

    @SuppressWarnings("all")
    private Uri setImageUri() {
        String name = Utilities.tsInGmt();
        name = new Utilities().gmtToEpoch(name);


        File folder = new File(getExternalFilesDir(null) + ApiOnServer.IMAGE_CAPTURE_URI);

        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }


        File file = new File(getExternalFilesDir(null) + ApiOnServer.IMAGE_CAPTURE_URI, name + ".jpg");


        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri imgUri = FileProvider.getUriForFile(SaveProfile.this, getApplicationContext().getPackageName() + ".provider", file);
        this.imageUri = imgUri;

        this.picturePath = file.getAbsolutePath();


        name = null;
        folder = null;
        file = null;


        SessionManager sessionManager = new SessionManager(this);
        sessionManager.setUserProfilePic(String.valueOf(imageUri), true);
        return imgUri;
    }

    private boolean validateUserDetails() {

        /*
         * To validate the user details
         */


        if (etUserName.getText().toString().trim().isEmpty()) {

            if (root != null) {
                Snackbar snackbar = Snackbar.make(root, R.string.Enter_Name, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            }
            return false;
        }

        if (etUserName.getText().length() < 4) {
            if (root != null) {
                Snackbar snackbar = Snackbar.make(root, R.string.Invalid_Name, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            }
            return false;
        }
        return true;
    }

    private void gotoSettings() {

        builder.setPositiveButton("Setting", (dialogInterface, i) -> {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        builder.show();


    }

    private void showPermissionMessage() {
        builder.setPositiveButton("Retry", (dialogInterface, i) -> checkPermissions());
        builder.show();
    }

    /**
     * Result of the permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:
                try {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        selectImage();
                    } else {
                        // permission was not granted
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                            showPermissionMessage();
                        } else {
                            gotoSettings();
                        }
                        StringBuilder p = new StringBuilder();
                        for (String per : permissions) {
                            p.append("\n").append(per);
                        }

                    }
                    break;
                } catch (Exception ignored) {

                }
        }
//        if (requestCode == 24) {
//
//
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//
//                if (ActivityCompat.checkSelfPermission(SaveProfile.this, Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.checkSelfPermission(SaveProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_GRANTED)
//
//
//                    {
//
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        if (intent.resolveActivity(getPackageManager()) != null) {
//
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//
//                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
//
//                            } else {
//
//
//                                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                                for (ResolveInfo resolveInfo : resInfoList) {
//                                    String packageName = resolveInfo.activityInfo.packageName;
//                                    grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                }
//
//
//                            }
//                            startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
//
//
//                        } else {
//                            Snackbar snackbar = Snackbar.make(root, R.string.string_61,
//                                    Snackbar.LENGTH_SHORT);
//                            snackbar.show();
//
//
//                            View view = snackbar.getView();
//                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                        }
//                    } else {
//
//
//                        requestReadImagePermission(0);
//                    }
//                } else {
//
//
//                    Snackbar snackbar = Snackbar.make(root, R.string.string_62,
//                            Snackbar.LENGTH_SHORT);
//
//
//                    snackbar.show();
//                    View view = snackbar.getView();
//                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                }
//            } else {
//
//
//                Snackbar snackbar = Snackbar.make(root, R.string.string_62,
//                        Snackbar.LENGTH_SHORT);
//
//
//                snackbar.show();
//                View view = snackbar.getView();
//                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//            }
//
//        } else if (requestCode == 26) {
//
//            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                if (ActivityCompat.checkSelfPermission(SaveProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        == PackageManager.PERMISSION_GRANTED) {
//
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//                        intent.setType("image/*");
//                        startActivityForResult(Intent.createChooser(intent, getString(R.string.string_1020)), RESULT_LOAD_IMAGE);
//                    } else {
//                        Intent intent = new Intent();
//                        intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(Intent.createChooser(intent, getString(R.string.string_1020)), RESULT_LOAD_IMAGE);
//                    }
//
//
//                } else {
//
//                    Snackbar snackbar = Snackbar.make(root, R.string.string_1006,
//                            Snackbar.LENGTH_SHORT);
//
//
//                    snackbar.show();
//                    View view = snackbar.getView();
//                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                }
//            } else {
//
//                Snackbar snackbar = Snackbar.make(root, R.string.string_1006,
//                        Snackbar.LENGTH_SHORT);
//
//
//                snackbar.show();
//                View view = snackbar.getView();
//                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//            }
//        } else if (requestCode == 27) {
//            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                if (ActivityCompat.checkSelfPermission(SaveProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        == PackageManager.PERMISSION_GRANTED) {
//
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (intent.resolveActivity(getPackageManager()) != null) {
//
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
//
//                        } else {
//
//
//                            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                            for (ResolveInfo resolveInfo : resInfoList) {
//                                String packageName = resolveInfo.activityInfo.packageName;
//                                grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            }
//
//
//                        }
//
//                        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
//                    } else {
//                        Snackbar snackbar = Snackbar.make(root, R.string.string_61,
//                                Snackbar.LENGTH_SHORT);
//                        snackbar.show();
//
//
//                        View view = snackbar.getView();
//                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                    }
//                } else {
//
//                    Snackbar snackbar = Snackbar.make(root, R.string.string_1006,
//                            Snackbar.LENGTH_SHORT);
//
//
//                    snackbar.show();
//                    View view = snackbar.getView();
//                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                }
//
//            } else {
//
//                Snackbar snackbar = Snackbar.make(root, R.string.string_1006,
//                        Snackbar.LENGTH_SHORT);
//
//
//                snackbar.show();
//                View view = snackbar.getView();
//                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//            }
//
//        }
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private void uploadProfileDetailsToserver(final String profilePic) {

        if (etFullName.getText().toString().trim().equals("") || etUserName.getText().toString().trim().equals(""))
            Toast.makeText(SaveProfile.this, "Please fill the data", Toast.LENGTH_SHORT).show();
        else {

            final ProgressDialog pDialog = new ProgressDialog(SaveProfile.this, 0);

            pDialog.setCancelable(false);
            pDialog.setMessage(getString(R.string.userProfileSetup));

            try {
                pDialog.show();
            } catch (Exception e) {
                // WindowManager$BadTokenException will be caught and the app would not display
                // the 'Force Close' message
            }


            ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);
            bar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(SaveProfile.this, R.color.color_black),
                    PorterDuff.Mode.SRC_IN);

            JSONObject obj = new JSONObject();


            try {
                obj.put("userName", etUserName.getText().toString().trim());
                String[] name = etFullName.getText().toString().split("\\s+");
                try {
                    obj.put("firstName", name[0]);
                    firstName = name[0];
                    if (name.length > 1) {
                        if (name[1] != null) {
                            obj.put("lastName", name[1]);
                            lastName = name[1];
                        } else {
                            obj.put("lastName", " ");
                            lastName = "";
                        }
                    } else {
                        obj.put("lastName", " ");
                        lastName = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                obj.put("private", rbPrivate.isChecked() ? 1 : 0);

                if (profilePic != null) {
                    obj.put("profilePic", profilePic);
                } else {

                    if (userAlreadyHasImage) {
                        obj.put("profilePic", userImageUrl);
                    } else {
                        obj.put("profilePic", Constants.DEFAULT_PROFILE_PIC_LINK);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            AccountManager mAccountManager = AccountManager.get(getApplicationContext());
            Account[] accounts = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    ApiOnServer.USER_PROFILE, obj, new com.android.volley.Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {


                    //  hideProgressDialog();


                    try {


                        switch (response.getInt("code")) {

                            case 200:

                                Map<String, Object> map = new HashMap<>();
                                SessionManager sessionManager = new SessionManager(getApplicationContext());
                                if (userImageUrl != null || profilePic != null) {
                                    if (userImageUrl != null) {
                                        map.put("userImageUrl", userImageUrl);
                                        AppController.getInstance().setUserImageUrl(userImageUrl);
                                        sessionManager.setUserProfilePic(userImageUrl, true);
                                    } else {
                                        map.put("userImageUrl", profilePic);
                                        AppController.getInstance().setUserImageUrl(profilePic);
                                        sessionManager.setUserProfilePic(profilePic, true);
                                    }

                                } else {
                                    map.put("userImageUrl", "");
                                    AppController.getInstance().setUserImageUrl("");
                                }
                                map.put("userLoginType", 1);
                                map.put("userName", etUserName.getText().toString().trim());
                                map.put("userId", AppController.getInstance().getUserId());
                                map.put("userIdentifier", AppController.getInstance().getUserIdentifier());
                                map.put("apiToken", AppController.getInstance().getApiToken());
                                CouchDbController db = AppController.getInstance().getDbController();
                                db.updateUserDetails(db.getUserDocId(AppController.getInstance().getUserId(), AppController.getInstance().getIndexDocId()), map);
                                db.updateIndexDocumentOnSignIn(AppController.getInstance().getIndexDocId(), AppController.getInstance().getUserId(), 1, true);
                                if (pDialog.isShowing()) {
                                    // pDialog.dismiss();
                                    Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();
                                    if (context instanceof Activity) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                            if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                pDialog.dismiss();
                                            }
                                        } else {
                                            if (!((Activity) context).isFinishing()) {
                                                pDialog.dismiss();
                                            }
                                        }
                                    } else {
                                        try {
                                            pDialog.dismiss();
                                        } catch (final IllegalArgumentException e) {
                                            e.printStackTrace();

                                        } catch (final Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                AppController.getInstance().setUserName(etUserName.getText().toString().trim());
                                AppController.getInstance().setProfileSaved(true);

                                sessionManager.setUserName(etUserName.getText().toString());
                                sessionManager.setFirstName(firstName);
                                sessionManager.setLastsName(lastName);
//                                sessionManager.setFacebookAccessToken(AccessToken.getCurrentAccessToken());

                                Intent i = new Intent(SaveProfile.this, OnBoardingActivity.class);
//                                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("caller", "SaveProfile");
                                startActivity(i);
//                                supportFinishAfterTransition();
                                finish();
                                break;

                            case 412:
                                Toast.makeText(SaveProfile.this, "Username is already exist", Toast.LENGTH_SHORT).show();
                                break;
                            default:


                                if (root != null) {

                                    Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                }
                        }

                        if (pDialog.isShowing()) {

                            // pDialog.dismiss();
                            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


                            if (context instanceof Activity) {


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                        pDialog.dismiss();
                                    }
                                } else {


                                    if (!((Activity) context).isFinishing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            } else {
                                try {
                                    pDialog.dismiss();
                                } catch (final IllegalArgumentException e) {
                                    e.printStackTrace();

                                } catch (final Exception e) {
                                    e.printStackTrace();

                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //  requesting = false;
                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // requesting = false;

                    if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                        SessionObserver sessionObserver = new SessionObserver();
                        sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<Boolean>() {
                                    @Override
                                    public void onNext(Boolean flag) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                uploadProfileDetailsToserver(profilePic);
                                            }
                                        }, 1000);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                    }
                                });
                        sessionApiCall.getNewSession(sessionObserver);
                    } else {
                        if (pDialog.isShowing()) {

                            //  pDialog.dismiss();
                            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


                            if (context instanceof Activity) {


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                        pDialog.dismiss();
                                    }
                                } else {


                                    if (!((Activity) context).isFinishing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            } else {


                                try {
                                    pDialog.dismiss();
                                } catch (final IllegalArgumentException e) {
                                    e.printStackTrace();

                                } catch (final Exception e) {
                                    e.printStackTrace();

                                }
                            }
                        }

                        if (root != null) {
                            Snackbar snackbar = Snackbar.make(root, R.string.UpdateProfile_Failed, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("authorization", AppController.getInstance().getApiToken());
                    headers.put("lang", Constants.LANGUAGE);


                    return headers;
                }
            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            /* Add the request to the RequestQueue.*/
            AppController.getInstance().addToRequestQueue(jsonObjReq, "updateProfileApi");
        }

    }

    /*
     * Uploading images and video and audio to  the server
     */
    @SuppressWarnings("TryWithIdenticalCatches,all")
    private void uploadProfilePic() {

        if (picturePath != null) {

            final Uri fileUri;

            final String name = AppController.getInstance().getUserId() + System.currentTimeMillis();//String.valueOf(System.currentTimeMillis());
            if (bitmapToUpload != null) {


                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmapToUpload.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, baos);


                // bm = null;
                byte[] b = baos.toByteArray();

                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                baos = null;


                File f = convertByteArrayToFile(b, name, ".jpg");
                b = null;

                fileUri = Uri.fromFile(f);
                f = null;


                final ProgressDialog pDialog = new ProgressDialog(SaveProfile.this, 0);


                pDialog.setCancelable(false);

                //pDialog.setMessage(getString(R.string.Save_Picture));
                //pDialog.show();

//                ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);
//
//
//                bar.getIndeterminateDrawable().setColorFilter(
//                        ContextCompat.getColor(SaveProfile.this, R.color.color_black),
//                        PorterDuff.Mode.SRC_IN);


                FileUploadService service = ServiceGenerator.createService(FileUploadService.class);


                final File file = FileUtils.getFile(this, fileUri);

                String url = null;


                url = name + ".jpg";


                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);


                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("photo", url, requestFile);


                String descriptionString = getString(R.string.string_803);
                RequestBody description =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), descriptionString);


                Call<ResponseBody> call = service.uploadProfilePic(description, body);


                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {


                        if (pDialog.isShowing()) {
                            // pDialog.dismiss();
                            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


                            if (context instanceof Activity) {


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                        pDialog.dismiss();
                                    }
                                } else {


                                    if (!((Activity) context).isFinishing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            } else {


                                try {
                                    pDialog.dismiss();
                                } catch (final IllegalArgumentException e) {
                                    e.printStackTrace();

                                } catch (final Exception e) {
                                    e.printStackTrace();

                                }
                            }

                        }
                        /*
                         *
                         *
                         * has to get url from the server in response
                         *
                         *
                         * */


                        if (response.code() == 200) {


                            String url = null;


                            url = name + ".jpg";

                            uploadProfileDetailsToserver(ApiOnServer.PROFILEPIC_UPLOAD_PATH + url);

                            File fdelete = new File(fileUri.getPath());
                            if (fdelete.exists()) fdelete.delete();


                        } else {


                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, R.string.Upload_Failed, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (pDialog.isShowing()) {
                            // pDialog.dismiss();
                            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


                            if (context instanceof Activity) {


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                        pDialog.dismiss();
                                    }
                                } else {


                                    if (!((Activity) context).isFinishing()) {
                                        pDialog.dismiss();
                                    }
                                }
                            } else {


                                try {
                                    pDialog.dismiss();
                                } catch (final IllegalArgumentException e) {
                                    e.printStackTrace();

                                } catch (final Exception e) {
                                    e.printStackTrace();

                                }
                            }

                        }

                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.Upload_Failed, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }


                });

            } else {
                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, R.string.Upload_Failed, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            }
        } else {

            uploadProfileDetailsToserver(null);


        }


    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void fetchUserDetailsFromServer() {


        final ProgressDialog pDialog = new ProgressDialog(SaveProfile.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.Fetch_Details));
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(SaveProfile.this, R.color.color_black),
                PorterDuff.Mode.SRC_IN);

        JSONObject obj = new JSONObject();
        try {
            obj.put("userId", AppController.getInstance().getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ApiOnServer.USER_PROFILE, obj, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                //  hideProgressDialog();
                if (pDialog.isShowing()) {

                    // pDialog.dismiss();
                    Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


                    if (context instanceof Activity) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                pDialog.dismiss();
                            }
                        } else {


                            if (!((Activity) context).isFinishing()) {
                                pDialog.dismiss();
                            }
                        }
                    } else {


                        try {
                            pDialog.dismiss();
                        } catch (final IllegalArgumentException e) {
                            e.printStackTrace();

                        } catch (final Exception e) {
                            e.printStackTrace();

                        }
                    }


                }

                try {


                    switch (response.getInt("code")) {


                        case 200:
                            response = response.getJSONObject("response");
                            if (response.has("userName"))
                                etUserName.setText(response.getString("userName"));


                            if (response.has("profilePic")) {

                                String pic = response.getString("profilePic");

                                if (pic != null && !pic.isEmpty()) {
//                                    deleteImage.setVisibility(View.VISIBLE);
//                                    addImage.setVisibility(View.GONE);
                                    userAlreadyHasImage = true;

                                    try {

                                        userImageUrl = response.getString("profilePic");

                                        Glide.with(SaveProfile.this)
                                                .load(userImageUrl).asBitmap()
                                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                                .centerCrop()
                                                .placeholder(R.drawable.chat_attachment_profile_default_image_frame)
                                                .into(profilePic);
//                                        new BitmapImageViewTarget(profilePic) {
//                                                    @Override
//                                                    protected void setResource(Bitmap resource) {
//                                                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
//                                                        circularBitmapDrawable.setCircular(true);
//                                                        profilePic.setImageDrawable(circularBitmapDrawable);
//                                                    }
//                                                });
                                    } catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    userAlreadyHasImage = false;


                                    userImageUrl = null;

                                }
                            } else {

                                userAlreadyHasImage = false;


                                userImageUrl = null;


                            }

                            break;


                        case 204:
                            /*
                             * User doesn't exist already
                             */

                            break;


                        default:


                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();


                }

                //  requesting = false;
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // requesting = false;
                if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                    SessionObserver sessionObserver = new SessionObserver();
                    sessionObserver.getObservable().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableObserver<Boolean>() {
                                @Override
                                public void onNext(Boolean flag) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            fetchUserDetailsFromServer();
                                        }
                                    }, 1000);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {
                                }
                            });
                    sessionApiCall.getNewSession(sessionObserver);
                } else {

                    if (pDialog.isShowing()) {

                        //  pDialog.dismiss();
                        Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


                        if (context instanceof Activity) {


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                    pDialog.dismiss();
                                }
                            } else {


                                if (!((Activity) context).isFinishing()) {
                                    pDialog.dismiss();
                                }
                            }
                        } else {


                            try {
                                pDialog.dismiss();
                            } catch (final IllegalArgumentException e) {
                                e.printStackTrace();

                            } catch (final Exception e) {
                                e.printStackTrace();

                            }
                        }


                    }

                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lang", Constants.LANGUAGE);
                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "fetchProfileApi");


    }

    /*
     * To save the byte array received in to file
     */
    @SuppressWarnings("all")
    public File convertByteArrayToFile(byte[] data, String name, String extension) {


        File file = null;

        try {


            File folder = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER);

            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }


            file = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER, name + extension);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return file;

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;


        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            final String documentId = DocumentsContract.getDocumentId(uri);
            if (documentId.startsWith("raw:")) {
                return documentId.replaceFirst("raw:", "");
            }
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return context.getExternalFilesDir(null) + "/" + split[1];
                }


            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {


            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }




    /*
     *To allow user to select the method by which he would like to add the new Profile image
     */

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    private Bitmap getCircleBitmap(Bitmap bitmap) {


        try {

            final Bitmap circuleBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getWidth(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(circuleBitmap);

            final int color = Color.GRAY;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);


            return circuleBitmap;
        } catch (Exception e) {
            return null;
        }
    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(SaveProfile.this, ChatMessageScreen.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("receiverUid", obj.getString("receiverUid"));
            intent.putExtra("receiverName", obj.getString("receiverName"));
            intent.putExtra("documentId", obj.getString("documentId"));
            intent.putExtra("isStar", obj.getBoolean("isStar"));
            intent.putExtra("receiverImage", obj.getString("receiverImage"));
            intent.putExtra("colorCode", obj.getString("colorCode"));

            startActivity(intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {


        if (AppController.getInstance().isActiveOnACall()) {
            if (AppController.getInstance().isCallMinimized()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        } else {
            super.onBackPressed();
            supportFinishAfterTransition();
        }

    }

    @Override
    public void onStart(String requestId) {
        ibNext.setEnabled(false);
    }

    @Override
    public void onProgress(String requestId, long bytes, long totalBytes) {
        Double progress = (double) bytes / totalBytes;

    }

    @Override
    public void onSuccess(String requestId, Map resultData) {
        userImageUrl = String.valueOf(resultData.get("url"));
        userAlreadyHasImage = true;
        ibNext.setEnabled(true);
    }

    @Override
    public void onError(String requestId, ErrorInfo error) {
        ibNext.setEnabled(true);
    }

    @Override
    public void onReschedule(String requestId, ErrorInfo error) {

    }
}
