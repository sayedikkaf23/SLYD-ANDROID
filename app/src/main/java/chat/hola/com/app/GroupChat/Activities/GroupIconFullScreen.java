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
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DownloadFile.FileUploadService;
import chat.hola.com.app.DownloadFile.FileUtils;
import chat.hola.com.app.DownloadFile.ServiceGenerator;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TouchImageView;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.SessionObserver;
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
 * Created by moda on 23/09/17.
 */


/**
 * Activity to show the group icon on the fullscreen
 */

public class GroupIconFullScreen extends AppCompatActivity {

    private SessionApiCall sessionApiCall = new SessionApiCall();
    private TouchImageView imgDisplay;
    private Bus bus = AppController.getBus();
    private Uri imageUri;

    private RelativeLayout root;
    private static final int RESULT_CAPTURE_IMAGE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private String picturePath = null;

    private Bitmap bitmap;
    private ImageView edit;
    private static final int IMAGE_QUALITY = 50;//change it to higher level if want,but then slower image uploading/downloading
    private Intent intent;
    private ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gc_groupicon_fullscreen);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        root = (RelativeLayout) findViewById(R.id.root);
        imgDisplay = (TouchImageView) findViewById(R.id.imgDisplay);


        intent = getIntent();
        edit = (ImageView) findViewById(R.id.edit);

        setupActivity(intent);
        ImageView close = (ImageView) findViewById(R.id.close);


        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage();

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                onBackPressed();

            }
        });


        TextView title = (TextView) findViewById(R.id.title);

        title.setTypeface(AppController.getInstance().getRegularFont(), Typeface.BOLD);


        bus.register(this);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;
        setupActivity(intent);


    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void setupActivity(Intent intent) {


        Bundle extras = intent.getExtras();
        if (extras != null) {


            if (extras.getBoolean("canEditImage")) {

                edit.setVisibility(View.VISIBLE);
            } else {

                edit.setVisibility(View.GONE);
            }

            updateGroupIcon(extras.getString("groupImageUrl"));
        }


    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(GroupIconFullScreen.this, ChatMessageScreen.class);

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
            } else if (object.getString("eventName").equals(MqttEvents.GroupChats.value + "/" + AppController.getInstance().getUserId())) {


                if (!object.has("payload")) {


                    switch (object.getInt("type")) {

                        case 5: {
                            /*
                             *
                             *Group icon updated
                             */

                            updateGroupIcon(object.getString("groupImageUrl"));

                            break;
                        }
                        case 1: {

                            /*
                             *Member added
                             */
                            if (object.getString("groupId").equals(intent.getExtras().getString("groupId"))) {


                                if (object.getString("memberId").equals(AppController.getInstance().getUserId())) {
                                    edit.setVisibility(View.VISIBLE);
                                }

                            }
                            break;
                        }
                        case 2: {
                            /*
                             *Member removed
                             */

                            if (object.getString("groupId").equals(intent.getExtras().getString("groupId"))) {


                                if (object.getString("memberId").equals(AppController.getInstance().getUserId())) {
                                    edit.setVisibility(View.GONE);
                                }

                            }
                            break;
                        }


                    }
                }
            }


        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

    private void selectImage() {


        androidx.appcompat.app.AlertDialog.Builder builder;
        builder = new androidx.appcompat.app.AlertDialog.Builder(GroupIconFullScreen.this);
        builder.setTitle(R.string.string_255);
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
        if (ActivityCompat.checkSelfPermission(GroupIconFullScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GroupIconFullScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
            if (ActivityCompat.shouldShowRequestPermissionRationale(GroupIconFullScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(GroupIconFullScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_222,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(GroupIconFullScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                26);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(GroupIconFullScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        26);
            }
        } else if (k == 0) {




            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(GroupIconFullScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(GroupIconFullScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_1218,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(GroupIconFullScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                27);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(GroupIconFullScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        27);
            }


        }
    }

    /**
     * Check access camera permission
     */
    private void checkCameraPermissionImage() {
        if (ActivityCompat.checkSelfPermission(GroupIconFullScreen
                .this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(GroupIconFullScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(GroupIconFullScreen.this,
                Manifest.permission.CAMERA)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_221,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions(GroupIconFullScreen.this, new String[]{Manifest.permission.CAMERA},
                            24);
                }
            });


            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);


        } else {


            ActivityCompat.requestPermissions(GroupIconFullScreen.this, new String[]{Manifest.permission.CAMERA},
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
        Uri imgUri = FileProvider.getUriForFile(GroupIconFullScreen.this, getApplicationContext().getPackageName() + ".provider", file);
        this.imageUri = imgUri;

        this.picturePath = file.getAbsolutePath();


        name = null;
        folder = null;
        file = null;


        return imgUri;
    }

    /**
     * Result of the permission request
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == 24) {


            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.checkSelfPermission(GroupIconFullScreen.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(GroupIconFullScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

                if (ActivityCompat.checkSelfPermission(GroupIconFullScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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

                if (ActivityCompat.checkSelfPermission(GroupIconFullScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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


    /*
     * To save the byte array received in to file
     */
    @SuppressWarnings("all")
    public File convertByteArrayToFile(byte[] data, String name, String extension) {


        File file = null;

        try {


            File folder = new File(getExternalFilesDir(null)  + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER);

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


                    picturePath = getPath(GroupIconFullScreen.this, data.getData());

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


                    picturePath = getPath(GroupIconFullScreen.this, result.getUri());

                    if (picturePath != null) {


                        bitmap = BitmapFactory.decodeFile(picturePath);


                        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {

                            imgDisplay.setImageBitmap(bitmap);

                            uploadProfilePic();
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


    /*
     * Uploading images and video and audio to  the server
     */
    @SuppressWarnings("TryWithIdenticalCatches,all")
    private void uploadProfilePic() {


        if (picturePath != null) {

            final Uri fileUri;

            final String name = AppController.getInstance().getUserId() + System.currentTimeMillis();//String.valueOf(System.currentTimeMillis());
            if (bitmap != null) {


                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, baos);


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

                final ProgressDialog pDialog = new ProgressDialog(GroupIconFullScreen.this, 0);
                pDialog.setCancelable(false);

                //pDialog.setMessage(getString(R.string.Save_Picture));
                //pDialog.show();

                ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);

                try {
                    bar.getIndeterminateDrawable().setColorFilter(
                            ContextCompat.getColor(GroupIconFullScreen.this, R.color.color_black),
                            PorterDuff.Mode.SRC_IN);
                } catch (Exception e) {
                    e.getStackTrace();
                }

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
                         * Has to get url from the server in response
                         *
                         *
                         * */


                        if (response.code() == 200) {


                            String url = null;


                            url = name + ".jpg";
                            uploadGroupIconDetailsToServer(ApiOnServer.PROFILEPIC_UPLOAD_PATH + url);

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
        }


    }


    /**
     * @param url the new group icon url to be sent to server
     */


    @SuppressWarnings("TryWithIdenticalCatches")
    private void uploadGroupIconDetailsToServer(final String url) {


        Bundle extras = intent.getExtras();


        final String groupId = extras.getString("groupId");


        final String groupMembersDocId = extras.getString("groupMembersDocId");


        final String groupMessagesDocId = extras.getString("groupMessagesDocId");


        pDialog = new ProgressDialog(GroupIconFullScreen.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.SavingIcon));

        if (root != null) {

            pDialog.show();
        }

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(GroupIconFullScreen.this, R.color.color_black),
                PorterDuff.Mode.SRC_IN);


        JSONObject obj = new JSONObject();


        try {


            obj.put("image", url);


            obj.put("chatId", groupId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
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
                             * Member updated group icon successfully
                             */

                            informAllMembersOfGroupIcon(url, groupId, groupMembersDocId, groupMessagesDocId);
                            break;

                        }


                        default: {

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


            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

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
                                            uploadGroupIconDetailsToServer(url);
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

                headers.put("X-HTTP-Method-Override", "PATCH");

                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "updateGroupInfoApi");


    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void informAllMembersOfGroupIcon(String url, String groupId, String groupMembersDocId, String groupMessagesDocId) {




        /*
         * Intentionally fetching list of members from db again
         */
        String userId = AppController.getInstance().getUserId();
        String tsInGmt = Utilities.tsInGmt();

        String messageId = String.valueOf(Utilities.getGmtEpoch());
        JSONObject obj = new JSONObject();
        try {
            obj.put("initiatorId", userId);

            obj.put("initiatorIdentifier", AppController.getInstance().getUserIdentifier());
            obj.put("type", 5);
            obj.put("groupId", groupId);

            obj.put("groupImageUrl", url);
            obj.put("id", messageId);
            obj.put("timestamp", tsInGmt);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();

        map.put("messageType", "98");


        map.put("isSelf", true);
        map.put("from", groupId);
        map.put("Ts", tsInGmt);
        map.put("id", messageId);

        map.put("type", 5);
        map.put("deliveryStatus", "0");


        map.put("groupImageUrl", url);


        map.put("initiatorId", userId);
        map.put("initiatorIdentifier", AppController.getInstance().getUserIdentifier());

        AppController.getInstance().getDbController().addNewChatMessageAndSort(groupMessagesDocId, map, tsInGmt, "");

        ArrayList<Map<String, Object>> groupMembers = AppController.getInstance().getDbController().fetchGroupMember(groupMembersDocId);

        if (AppController.getInstance().canPublish()) {
            AppController.getInstance().getDbController().updateGroupIcon(groupMessagesDocId, url);
            for (int i = 0; i < groupMembers.size(); i++) {


                String memberId = (String) groupMembers.get(i).get("memberId");


                if (memberId.equals(userId)) {


                    try {
                        JSONObject obj2 = new JSONObject(obj.toString());
                        obj2.put("self", true);
                        obj2.put("timestamp", tsInGmt);


                        obj2.put("groupImageUrl", url);
                        obj2.put("id", messageId);
                        obj2.put("message", getString(R.string.You) + " " + getString(R.string.UpdatedGroupIcon));

                        obj2.put("eventName", MqttEvents.GroupChats.value + "/" + userId);
                        bus.post(obj2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    AppController.getInstance().publish(MqttEvents.GroupChats.value + "/" + memberId,
                            obj, 1, false);
                }

            }


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


            supportFinishAfterTransition();

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

    @SuppressWarnings("TryWithIdenticalCatches")
    private void updateGroupIcon(String path) {
        try {


            Glide
                    .with(GroupIconFullScreen.this)
                    .load(path)


                    .crossFade()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.avatar_group_large)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            imgDisplay.setBackgroundColor(ContextCompat.getColor(GroupIconFullScreen.this, R.color.color_black));
                            return false;
                        }
                    })
                    .into(imgDisplay);


        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


}
