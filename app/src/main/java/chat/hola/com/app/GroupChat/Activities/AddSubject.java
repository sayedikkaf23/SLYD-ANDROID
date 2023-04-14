package chat.hola.com.app.GroupChat.Activities;

import android.Manifest;
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
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.DownloadFile.FileUploadService;
import chat.hola.com.app.DownloadFile.FileUtils;
import chat.hola.com.app.DownloadFile.ServiceGenerator;
import chat.hola.com.app.ForwardMessage.Forward_ContactItem;
import chat.hola.com.app.ForwardMessage.SortContactsToForward;
import chat.hola.com.app.GroupChat.Adapters.SelectedMembersGridAdapter;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;
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
 * Created by moda on 19/09/17.
 */

public class AddSubject extends AppCompatActivity {

    private SessionApiCall sessionApiCall = new SessionApiCall();
    private RelativeLayout root;
    private static final int RESULT_CAPTURE_IMAGE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int IMAGE_QUALITY = 50;//change it to higher level if want,but then slower image uploading/downloading

    private Uri imageUri;
    private Bus bus = AppController.getBus();

    private String picturePath;
    private Bitmap bitmap, bitmapToUpload;

    private ImageView groupPic, deleteImage;


    private TextView participantCount;

    private Intent intent;
    private JSONArray members;
    private ArrayList<Forward_ContactItem> mMembersData = new ArrayList<>();
    private SelectedMembersGridAdapter mAdapter;
    private EmojiconEditText enterSubject;

    private ProgressDialog pDialog;

    private TextView maxLength;

    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        setContentView(R.layout.gc_add_subject);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        intent = getIntent();

        enterSubject = (EmojiconEditText) findViewById(R.id.et);
        maxLength = (TextView) findViewById(R.id.maxLength);
        enterSubject.addTextChangedListener(new MyTextWatcher(enterSubject));

        root = (RelativeLayout) findViewById(R.id.root);

        ImageView backButton = (ImageView) findViewById(R.id.close);

        groupPic = (ImageView) findViewById(R.id.groupIcon);


        deleteImage = (ImageView) findViewById(R.id.delete);


        participantCount = (TextView) findViewById(R.id.participantCount);

        FloatingActionButton createGroup = (FloatingActionButton) findViewById(R.id.fab);


        final ImageView selEmojicon = (ImageView) findViewById(R.id.emojiButton);
        final ImageView selKeyboard = (ImageView) findViewById(R.id.chat_keyboard_icon);
        final EmojiconsPopup popup = new EmojiconsPopup(root, this);


        RecyclerView selectedMembers = (RecyclerView) findViewById(R.id.rvContactsSelected);

        selectedMembers.setHasFixedSize(true);
        mAdapter = new SelectedMembersGridAdapter(AddSubject.this, mMembersData);
        selectedMembers.setItemAnimator(new DefaultItemAnimator());
//        selectedMembers.setLayoutManager(new GridLayoutManager(AddSubject.this, 3));


        selectedMembers.setLayoutManager(new GridLayoutManager(AddSubject.this, 4));
        selectedMembers.setAdapter(mAdapter);

        setupActivity();
        popup.setSizeForSoftKeyboard();
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                selKeyboard.setVisibility(View.GONE);
                selEmojicon.setVisibility(View.VISIBLE);

            }
        });

        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

            @Override
            public void onKeyboardOpen(int keyBoardHeight) {

            }

            @Override
            public void onKeyboardClose() {
                if (popup.isShowing())
                    popup.dismiss();

            }
        });


        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

            @Override
            public void onEmojiconClicked(Emojicon emojicon) {
                if (enterSubject == null || emojicon == null) {
                    return;
                }

                int start = enterSubject.getSelectionStart();
                int end = enterSubject.getSelectionEnd();
                if (start < 0) {
                    enterSubject.append(emojicon.getEmoji());
                } else {
                    enterSubject.getText().replace(Math.min(start, end),
                            Math.max(start, end), emojicon.getEmoji(), 0,
                            emojicon.getEmoji().length());
                }
            }
        });


        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

            @Override
            public void onEmojiconBackspaceClicked(View v) {
                KeyEvent event = new KeyEvent(
                        0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                enterSubject.dispatchKeyEvent(event);
            }
        });


        selEmojicon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selKeyboard.setVisibility(View.VISIBLE);
                selEmojicon.setVisibility(View.GONE);


                if (!popup.isShowing()) {


                    if (popup.isKeyBoardOpen()) {
                        popup.showAtBottom();

                    } else {
                        enterSubject.setFocusableInTouchMode(true);
                        enterSubject.requestFocus();
                        popup.showAtBottomPending();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(enterSubject, InputMethodManager.SHOW_IMPLICIT);

                    }
                } else {
                    popup.dismiss();
                }
            }
        });


        selKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selKeyboard.setVisibility(View.GONE);
                selEmojicon.setVisibility(View.VISIBLE);


                if (!popup.isShowing()) {


                    enterSubject.setFocusableInTouchMode(true);
                    enterSubject.requestFocus();
                    popup.showAtBottomPending();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(enterSubject, InputMethodManager.SHOW_IMPLICIT);

                } else {
                    popup.dismiss();

                }


            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        groupPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (validateSubject()) {

                    /*
                     * To hide the soft keyboard
                     */
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    if (picturePath != null) {


                        uploadGroupPic();


                    } else {
                        createNewGroup(null);
                    }

                } else {

                    if (root != null) {


                        Snackbar snackbar = Snackbar.make(root, R.string.EmptySubject, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }


                }


            }
        });

        deleteImage.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {


                                               try {


                                                   if (picturePath != null) {


                                                       groupPic.setImageResource(R.drawable.chat_attachment_profile_default_image_frame);

                                                       //     profilePic.setBackgroundColor(ContextCompat.getColor(SaveProfile.this, R.color.color_gray_background));

                                                   }

                                                   if (bitmap != null)
                                                       bitmap.recycle();


                                                   if (bitmapToUpload != null)
                                                       bitmapToUpload.recycle();

                                               } catch (OutOfMemoryError e) {
                                                   e.printStackTrace();
                                               }

                                               picturePath = null;


                                               deleteImage.setVisibility(View.GONE);
                                           }
                                       }

        );
        maxLength.setTypeface(AppController.getInstance().getRegularFont(), Typeface.NORMAL);
        bus.register(this);
    }


    private void selectImage() {


        androidx.appcompat.app.AlertDialog.Builder builder;
        builder = new androidx.appcompat.app.AlertDialog.Builder(AddSubject.this);
        builder.setTitle(R.string.GroupIcon);
        builder.setIcon(R.drawable.orca_attach_camera_pressed);
        builder.setItems(new CharSequence[]{getString(R.string.string_1021),
                        getString(R.string.string_1022), getString(R.string.cancel)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 1:


                                checkCameraPermissionImage();


                                dialog.cancel();
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
     * Check access gallery permission
     */
    private void checkReadImage() {
        if (ActivityCompat.checkSelfPermission(AddSubject.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddSubject.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddSubject.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(AddSubject.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_222,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(AddSubject.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                26);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(AddSubject.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        26);
            }
        } else if (k == 0) {




            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddSubject.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(AddSubject.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_1218,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(AddSubject.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                27);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(AddSubject.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        27);
            }


        }
    }

    /**
     * Check access camera permission
     */
    private void checkCameraPermissionImage() {
        if (ActivityCompat.checkSelfPermission(AddSubject
                .this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(AddSubject.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddSubject.this,
                Manifest.permission.CAMERA)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_221,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions(AddSubject.this, new String[]{Manifest.permission.CAMERA},
                            24);
                }
            });


            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);


        } else {


            ActivityCompat.requestPermissions(AddSubject.this, new String[]{Manifest.permission.CAMERA},
                    24);
        }
    }

    @SuppressWarnings("all")
    private Uri setImageUri() {
        String name = Utilities.tsInGmt();
        name = new Utilities().gmtToEpoch(name);


        File folder = new File(getExternalFilesDir(null)  + ApiOnServer.IMAGE_CAPTURE_URI);

        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }


        File file = new File(getExternalFilesDir(null)  + ApiOnServer.IMAGE_CAPTURE_URI, name + ".jpg");


        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Uri imgUri = FileProvider.getUriForFile(AddSubject.this, getApplicationContext().getPackageName() + ".provider", file);
        this.imageUri = imgUri;

        this.picturePath = file.getAbsolutePath();


        name = null;
        folder = null;
        file = null;


        return imgUri;
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


                    picturePath = getPath(AddSubject.this, data.getData());

                    if (picturePath != null) {


                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(picturePath, options);

                        if (options.outWidth > 0 && options.outHeight > 0) {




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


                    picturePath = getPath(AddSubject.this, result.getUri());

                    if (picturePath != null) {


                        bitmapToUpload = BitmapFactory.decodeFile(picturePath);
                        bitmap = getCircleBitmap(bitmapToUpload);


                        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {

                            groupPic.setImageBitmap(bitmap);

                            deleteImage.setVisibility(View.VISIBLE);
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
                    return context.getExternalFilesDir(null)  + "/" + split[1];
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;


        if (mMembersData.size() > 0) {


            mMembersData.clear();


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }


        setupActivity();
    }


    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(AddSubject.this, ChatMessageScreen.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("receiverUid", obj.getString("receiverUid"));
            intent.putExtra("receiverName", obj.getString("receiverName"));
            intent.putExtra("documentId", obj.getString("documentId"));
            intent.putExtra("isStar", obj.getBoolean("isStar"));
            intent.putExtra("receiverImage", obj.getString("receiverImage"));
            intent.putExtra("colorCode", obj.getString("colorCode"));

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            }
        } catch (JSONException e) {
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

    /**
     * Result of the permission request
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 24) {


            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.checkSelfPermission(AddSubject.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(AddSubject.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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


                        requestReadImagePermission(0);
                    }
                } else {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_62,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {


                Snackbar snackbar = Snackbar.make(root, R.string.string_62,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        } else if (requestCode == 26) {

            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(AddSubject.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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

                    Snackbar snackbar = Snackbar.make(root, R.string.string_1006,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_1006,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 27) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(AddSubject.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

                    Snackbar snackbar = Snackbar.make(root, R.string.string_1006,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_1006,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }
    }


    /**
     * @param groupPicUrl url of the group icon incase available otherwise null
     */


    @SuppressWarnings("TryWithIdenticalCatches")

    private void createNewGroup(final String groupPicUrl) {


        pDialog = new ProgressDialog(AddSubject.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.CreatingGroup));
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(AddSubject.this, R.color.color_black),
                PorterDuff.Mode.SRC_IN);


        JSONObject obj = new JSONObject();


        ArrayList<String> membersArray = new ArrayList<>();

        try {


            for (int i = 0; i < members.length(); i++) {
                membersArray.add(members.getJSONObject(i).getString("memberId"));


            }


            obj.put("members", new JSONArray(membersArray));


            obj.put("subject", enterSubject.getText().toString().trim());

            if (groupPicUrl != null) {


                obj.put("image", groupPicUrl);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                ApiOnServer.CREATE_GROUP, obj, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {

                    /*
                     * Progress dialog has been put in 3 places intentionally
                     */

                    switch (response.getInt("code")) {


                        case 200: {
                            /*
                             * Group created successfully
                             */
                            if (response.has("invalid")) {

                                JSONArray invalidMembers = response.getJSONArray("invalid");


                                for (int i = 0; i < invalidMembers.length(); i++) {

                                    for (int j = 0; j < members.length(); j++) {


                                        /*
                                         * To remove the members which failed to get added
                                         */


                                        if (members.getJSONObject(j).getString("memberId").equals(invalidMembers.get(i))) {


                                            members = Utilities.removeElementFromJSONArray(members, j);


                                        }
                                    }
                                }


                            }


                            informOtherMembersOfGroupCreation(response.getJSONObject("data").getString("chatId"),
                                    groupPicUrl, enterSubject.getText().toString().trim());


                            break;

                        }
                        case 202: {
                            /*
                             * No valid members
                             */
//  hideProgressDialog();
                            if (pDialog != null && pDialog.isShowing()) {

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
                            break;
                        }

                        default: {
//  hideProgressDialog();
                            if (pDialog != null && pDialog.isShowing()) {

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

                                Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                        }

                    }

//  hideProgressDialog();
                    if (pDialog != null && pDialog.isShowing()) {

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
                if (error.networkResponse!=null && error.networkResponse.statusCode == 406) {
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
                                            createNewGroup(groupPicUrl);
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
                } else if (pDialog != null && pDialog.isShowing()) {

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

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.No_Internet_Connection_Available), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "createGroupApi");


    }

    @SuppressWarnings("unchecked")
    private void setupActivity() {


        Bundle extras = intent.getExtras();


        if (extras != null) {

            try {
                members = new JSONArray(extras.getString("members"));


                participantCount.setText(getString(R.string.ParticipantCount, members.length()
                        + getString(R.string.double_inverted_comma)));

                Forward_ContactItem member;
                for (int i = 0; i < members.length(); i++) {

                    member = new Forward_ContactItem();


                    member.setContactImage(members.getJSONObject(i).getString("memberImage"));
                    member.setContactName(members.getJSONObject(i).getString("memberLocalName"));
                    member.setStar(members.getJSONObject(i).getBoolean("memberIsStar"));


                    mMembersData.add(member);


//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mAdapter.notifyItemInserted(mMembersData.size() - 1);
//                        }
//                    });

                }


                Collections.sort(mMembersData, new SortContactsToForward());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    private boolean validateSubject() {
        if (!enterSubject.getText().toString().trim().isEmpty()) {


            return true;
        }

        return false;
    }


    @SuppressWarnings("TryWithIdenticalCatches")

    private void uploadGroupPic() {


        final Uri fileUri;

        final String name = AppController.getInstance().getUserId() + System.currentTimeMillis();
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


            final ProgressDialog pDialog = new ProgressDialog(AddSubject.this, 0);


            pDialog.setCancelable(false);


            pDialog.setMessage(getString(R.string.SaveGroupIcon));
            pDialog.show();

            ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


            bar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(AddSubject.this, R.color.color_black),
                    PorterDuff.Mode.SRC_IN);


            FileUploadService service =
                    ServiceGenerator.createService(FileUploadService.class);


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
                        createNewGroup(ApiOnServer.PROFILEPIC_UPLOAD_PATH + url);

                        File fdelete = new File(fileUri.getPath());
                        if (fdelete.exists()) fdelete.delete();


                    } else {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.UploadGroupPicFailed, Snackbar.LENGTH_SHORT);


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

                        Snackbar snackbar = Snackbar.make(root, R.string.UploadGroupPicFailed, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                }


            });

        } else {
            if (root != null) {

                Snackbar snackbar = Snackbar.make(root, R.string.UploadGroupPicFailed, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }

    }

    /*
     * To save the byte array received in to file
     */
    @SuppressWarnings("all")
    public File convertByteArrayToFile(byte[] data, String name, String extension) {


        File file = null;

        try {


            File folder = new File(getExternalFilesDir(null)   + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER);

            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }


            file = new File(getExternalFilesDir(null)  + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER, name + extension);
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


    @SuppressWarnings("TryWithIdenticalCatches")
    private void informOtherMembersOfGroupCreation(String groupId, String groupPicUrl, String groupName) {


        JSONObject obj = new JSONObject();

        long createdAt = 0;
        String tsInGmt = Utilities.tsInGmt();


        String userIdentifier = AppController.getInstance().getUserIdentifier();
        String userId = AppController.getInstance().getUserId();

        boolean isStar = new SessionManager(AddSubject.this).isStar();

        try {

            obj.put("memberId", userId);
            obj.put("memberIdentifier", userIdentifier);

            obj.put("memberIsStar", isStar);
            obj.put("memberImage", AppController.getInstance().getUserImageUrl());
            obj.put("memberStatus", AppController.getInstance().getUserIdentifier());

            obj.put("memberIsAdmin", true);


            members.put(obj);

            obj = new JSONObject();

            obj.put("groupSubject", groupName);

            if (groupPicUrl != null) {


                obj.put("groupImageUrl", groupPicUrl);

            } else {

                groupPicUrl = "";

            }


            obj.put("initiatorId", userId);
            obj.put("initiatorIdentifier", userIdentifier);


            obj.put("members", members);
            obj.put("groupId", groupId);
            obj.put("type", 0);


            obj.put("createdByMemberId", userId);
            obj.put("createdByMemberIdentifier", userIdentifier);
            createdAt = Utilities.getGmtEpoch();
            obj.put("createdAt", createdAt);
            obj.put("timestamp", tsInGmt);
            obj.put("id", String.valueOf(createdAt));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*
         * To inform the members of the group creation
         */


        if (AppController.getInstance().canPublish()) {


            ArrayList<Map<String, Object>> memberArray = new ArrayList<>();

            Map<String, Object> member;


            JSONObject jsonObject;
            for (int i = 0; i < members.length() - 1; i++) {
                try {


                    jsonObject = members.getJSONObject(i);
                    AppController.getInstance().publish(MqttEvents.GroupChats.value + "/" + jsonObject.getString("memberId"),
                            obj, 1, false);

                    member = new HashMap<>();

                    member.put("memberId", jsonObject.getString("memberId"));
                    member.put("memberIdentifier", jsonObject.getString("memberIdentifier"));


                    member.put("memberIsStar", jsonObject.getBoolean("memberIsStar"));


                    member.put("memberImage", jsonObject.getString("memberImage"));
                    member.put("memberStatus", jsonObject.getString("memberStatus"));

                    member.put("memberIsAdmin", jsonObject.getBoolean("memberIsAdmin"));


                    memberArray.add(member);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            /*
             * For adding own details
             */

            member = new HashMap<>();

            member.put("memberId", userId);
            member.put("memberIdentifier", userIdentifier);


            member.put("memberIsStar", isStar);


            member.put("memberImage", AppController.getInstance().getUserImageUrl());
            member.put("memberStatus", AppController.getInstance().getUserIdentifier());

            member.put("memberIsAdmin", true);

            memberArray.add(member);







            /*
             * To create the local couch db document for the group creation
             */

            /*
             * Here document structure is same as normal or secret chat except that receiverId is replaced by the groupId
             *
             */


            CouchDbController db = AppController.getInstance().getDbController();


            String groupDocId = db.createGroupMembersDocument();

            db.addGroupMembersDetails(groupDocId, memberArray, userId, userIdentifier, createdAt, true);


            db.addGroupChat(AppController.getInstance().getGroupChatsDocId(), groupId, groupDocId);


            String docId = AppController.getInstance().findDocumentIdOfReceiver(groupId, "");
            String time = Utilities.tsInGmt();


            /*
             * To create a new group document for messages
             */
            if (docId.isEmpty()) {


                /*
                 * Chat id is same as groupid but have intentionally put it as empty
                 */


//                docId = AppController.findDocumentIdOfReceiver(groupId, time, enterSubject.getText().toString().trim(),
//                        groupPicUrl, "", false, groupId, groupId, true);


                docId = AppController.findDocumentIdOfReceiver(groupId, time, groupName,
                        groupPicUrl, "", false, groupId, "", true, false);


                Map<String, Object> map = new HashMap<>();

                map.put("messageType", "98");


                map.put("isSelf", true);
                map.put("from", groupId);
                map.put("Ts", tsInGmt);
                map.put("id", String.valueOf(createdAt));

                map.put("type", 0);

                map.put("groupName", groupName);

                map.put("initiatorId", userId);
                map.put("initiatorIdentifier", userIdentifier);

                map.put("deliveryStatus", "0");


                db.addNewChatMessageAndSort(docId, map, tsInGmt, "");

                if (pDialog != null && pDialog.isShowing()) {

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


                JSONObject object2 = new JSONObject();
                try {

                    /*
                     * To close the previous activity
                     */


                    object2.put("eventName", "groupCreated");


                    bus.post(object2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(AddSubject.this, GroupChatMessageScreen.class);

                intent.putExtra("receiverUid", groupId);
                intent.putExtra("receiverName", enterSubject.getText().toString().trim());
                intent.putExtra("documentId", docId);

                intent.putExtra("receiverIdentifier", groupId);


                intent.putExtra("receiverImage", groupPicUrl);
                intent.putExtra("colorCode", AppController.getInstance().getColorCode(5));


                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                startActivity(intent);


                supportFinishAfterTransition();
            }


        } else {
            if (root != null) {


                Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {


            maxLength.setText(String.valueOf(25 - editable.length()));

        }
    }

}
