package chat.hola.com.app.Profile;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
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
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DownloadFile.FileUploadService;
import chat.hola.com.app.DownloadFile.FileUtils;
import chat.hola.com.app.DownloadFile.ServiceGenerator;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.SessionObserver;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moda on 02/08/17.
 */

public class OwnProfileDetails extends AppCompatActivity {

  private ImageView profileImage;

  private RelativeLayout root;
  private static final int RESULT_CAPTURE_IMAGE = 0;
  private static final int RESULT_LOAD_IMAGE = 1;

  private static final int RESULT_UPDATE_NAME = 2;
  private static final int RESULT_UPDATE_STATUS = 3;

  private String picturePath = null;

  private Uri imageUri;
  private Bus bus = AppController.getBus();

  private TextView userName, userStatus, userIdentifier;
  private SessionApiCall sessionApiCall = new SessionApiCall();
  private static final int IMAGE_QUALITY = 50;
      //change it to higher level if want,but then slower image uploading/downloading

  @SuppressWarnings("unchecked,TryWithIdenticalCatches")
  @Override

  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);

    setContentView(R.layout.profile_screen);
    root = (RelativeLayout) findViewById(R.id.root);

    userIdentifier = (TextView) findViewById(R.id.phoneNumber);
    userName = (TextView) findViewById(R.id.profileStatus);

    userStatus = (TextView) findViewById(R.id.status);

    ImageView selectImage = (ImageView) findViewById(R.id.profileImageSelector);

    selectImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        selectImage();
      }
    });
    ImageView editName = (ImageView) findViewById(R.id.profilePenStatus);
    profileImage = (ImageView) findViewById(R.id.profileImage);

    setUpActivity();




    /*
     * Start activity to update the status
     */
    editName.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {


        /*
         * To set the current status value
         */

        Intent i = new Intent(OwnProfileDetails.this, WriteNewNameOrStatus.class);

        i.putExtra("currentValue", AppController.getInstance().getUserName());

        i.putExtra("type", 0);
        startActivityForResult(i, RESULT_UPDATE_NAME);
      }
    });

    ImageView close = (ImageView) findViewById(R.id.close);

    ImageView editStatus = (ImageView) findViewById(R.id.editStatus);
    editStatus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        Intent i = new Intent(OwnProfileDetails.this, UpdateStatus.class);

        startActivityForResult(i, RESULT_UPDATE_STATUS);
      }
    });

    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
      }
    });


    /*
     * To set the typeface values
     */
    Typeface tf = AppController.getInstance().getRegularFont();

    TextView title = (TextView) findViewById(R.id.title);
    TextView userDetails = (TextView) findViewById(R.id.phonetext);
    TextView statusHeading = (TextView) findViewById(R.id.tv);

    TextView notUrName = (TextView) findViewById(R.id.textView_notURUserName);

    title.setTypeface(tf, Typeface.BOLD);
    userDetails.setTypeface(tf, Typeface.NORMAL);
    notUrName.setTypeface(tf, Typeface.NORMAL);

    statusHeading.setTypeface(tf, Typeface.NORMAL);
    userName.setTypeface(tf, Typeface.NORMAL);
    userStatus.setTypeface(tf, Typeface.NORMAL);
    userIdentifier.setTypeface(tf, Typeface.NORMAL);
    bus.register(this);
  }

  /**
   * Result of the permission request
   */

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {

    if (requestCode == 24) {

      if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        if (ActivityCompat.checkSelfPermission(OwnProfileDetails.this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
          if (ActivityCompat.checkSelfPermission(OwnProfileDetails.this,
              Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {

              intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                intent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
              } else {

                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                  String packageName = resolveInfo.activityInfo.packageName;
                  grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                      | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
              }
              startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
            } else {
              Snackbar snackbar = Snackbar.make(root, R.string.string_61, Snackbar.LENGTH_SHORT);
              snackbar.show();

              View view = snackbar.getView();
              TextView txtv =
                  (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
              txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
          } else {

            requestReadImagePermission(0);
          }
        } else {

          Snackbar snackbar = Snackbar.make(root, R.string.string_62, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
      } else {

        Snackbar snackbar = Snackbar.make(root, R.string.string_62, Snackbar.LENGTH_SHORT);

        snackbar.show();
        View view = snackbar.getView();
        TextView txtv =
            (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
      }
    } else if (requestCode == 26) {

      if (grantResults.length == 2
          && grantResults[0] == PackageManager.PERMISSION_GRANTED
          && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

        if (ActivityCompat.checkSelfPermission(OwnProfileDetails.this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.string_1020)),
                RESULT_LOAD_IMAGE);
          } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.string_1020)),
                RESULT_LOAD_IMAGE);
          }
        } else {

          Snackbar snackbar = Snackbar.make(root, R.string.string_1006, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
      } else {

        Snackbar snackbar = Snackbar.make(root, R.string.string_1006, Snackbar.LENGTH_SHORT);

        snackbar.show();
        View view = snackbar.getView();
        TextView txtv =
            (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
      }
    } else if (requestCode == 27) {
      if (grantResults.length == 2
          && grantResults[0] == PackageManager.PERMISSION_GRANTED
          && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

        if (ActivityCompat.checkSelfPermission(OwnProfileDetails.this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          if (intent.resolveActivity(getPackageManager()) != null) {

            intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

              intent.addFlags(
                  Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {

              List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                  PackageManager.MATCH_DEFAULT_ONLY);
              for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, imageUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
              }
            }

            startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
          } else {
            Snackbar snackbar = Snackbar.make(root, R.string.string_61, Snackbar.LENGTH_SHORT);
            snackbar.show();

            View view = snackbar.getView();
            TextView txtv =
                (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
          }
        } else {

          Snackbar snackbar = Snackbar.make(root, R.string.string_1006, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
      } else {

        Snackbar snackbar = Snackbar.make(root, R.string.string_1006, Snackbar.LENGTH_SHORT);

        snackbar.show();
        View view = snackbar.getView();
        TextView txtv =
            (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
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

      File folder = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER);

      if (!folder.exists() && !folder.isDirectory()) {
        folder.mkdirs();
      }

      file = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER,
          name + extension);
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
        final Uri contentUri =
            ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                Long.valueOf(id));

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
        final String[] selectionArgs = new String[] {
            split[1]
        };

        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    } else if ("content".equalsIgnoreCase(uri.getScheme())) {

      if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();

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
   * @param context The context.
   * @param uri The Uri to query.
   * @param selection (Optional) Filter used in the query.
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
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst()) {
        final int index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(index);
      }
    } finally {
      if (cursor != null) cursor.close();
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

          picturePath = getPath(OwnProfileDetails.this, data.getData());

          if (picturePath != null) {

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);

            //                        bitmap = BitmapFactory.decodeFile(picturePath);
            //                        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {

            if (options.outWidth > 0 && options.outHeight > 0) {
              //                            profileImage.setImageBitmap(bitmap);

              /*
               * Have to start the intent for the image cropping
               */
              CropImage.activity(data.getData()).start(this);
            } else {

              if (root != null) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_31, Snackbar.LENGTH_SHORT);

                snackbar.show();
                View view = snackbar.getView();
                TextView txtv =
                    (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
              }
            }
          } else {

            if (root != null) {

              Snackbar snackbar = Snackbar.make(root, R.string.string_31, Snackbar.LENGTH_SHORT);

              snackbar.show();
              View view = snackbar.getView();
              TextView txtv =
                  (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
              txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
          }
        } catch (OutOfMemoryError e) {

          Snackbar snackbar = Snackbar.make(root, R.string.string_15, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
      } else {
        if (resultCode == Activity.RESULT_CANCELED) {

          Snackbar snackbar = Snackbar.make(root, R.string.string_16, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {

          Snackbar snackbar = Snackbar.make(root, R.string.string_113, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
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
            //                    bitmap = BitmapFactory.decodeFile(picturePath);
            //
            //
            //                    if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            //                        profileImage.setImageBitmap(bitmap);

            CropImage.activity(imageUri).start(this);
          } else {

            picturePath = null;
            Snackbar snackbar = Snackbar.make(root, R.string.string_17, Snackbar.LENGTH_SHORT);

            snackbar.show();
            View view = snackbar.getView();
            TextView txtv =
                (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
          }
        } catch (OutOfMemoryError e) {

          picturePath = null;
          Snackbar snackbar = Snackbar.make(root, R.string.string_15, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
      } else {

        if (resultCode == Activity.RESULT_CANCELED) {

          picturePath = null;
          Snackbar snackbar = Snackbar.make(root, R.string.string_18, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {

          picturePath = null;
          Snackbar snackbar = Snackbar.make(root, R.string.string_17, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
      }
    } else if (requestCode == RESULT_UPDATE_NAME) {

      /*
       *
       * For return of the update user name activity
       */

      if (resultCode == Activity.RESULT_OK) {

        String updatedName = data.getExtras().getString("updatedValue");

        userName.setText(updatedName);


        /*
         * Hit the put Profile api on server in background
         */
        updateTheValueOnServer(updatedName, 0);
      }
      //            else {
      //                /*
      //                 * Update name canceled
      //                 */
      //
      //
      //            }
    } else if (requestCode == RESULT_UPDATE_STATUS) {

      /*
       *
       * For return of the update user name activity
       */

      if (resultCode == Activity.RESULT_OK) {

        String updatedStatus = data.getExtras().getString("updatedValue");

        userStatus.setText(updatedStatus);


        /*
         * Hit the put Profile api on server in background
         */
        updateTheStatusOnServer(updatedStatus);
      }
      //            else {
      //                /*
      //                 * Update status canceled
      //                 */
      //
      //
      //            }
    } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

      try {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {

          picturePath = getPath(OwnProfileDetails.this, result.getUri());

          if (picturePath != null) {
            Bitmap bitmapToUpload = BitmapFactory.decodeFile(picturePath);

            Bitmap bitmap = getCircleBitmap(bitmapToUpload);

            if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {

              profileImage.setImageBitmap(bitmap);
              /*
               * To start uploading of the bitmap on the background thread
               */

              uploadProfilePic(bitmapToUpload);
            } else {

              picturePath = null;
              if (root != null) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_19, Snackbar.LENGTH_SHORT);

                snackbar.show();
                View view = snackbar.getView();
                TextView txtv =
                    (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
              }
            }
          } else {

            picturePath = null;
            if (root != null) {

              Snackbar snackbar = Snackbar.make(root, R.string.string_19, Snackbar.LENGTH_SHORT);

              snackbar.show();
              View view = snackbar.getView();
              TextView txtv =
                  (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
              txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
          }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

          picturePath = null;
          if (root != null) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_19, Snackbar.LENGTH_SHORT);

            snackbar.show();
            View view = snackbar.getView();
            TextView txtv =
                (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
          }
        }
      } catch (OutOfMemoryError e) {

        picturePath = null;
        Snackbar snackbar = Snackbar.make(root, R.string.string_15, Snackbar.LENGTH_SHORT);

        snackbar.show();
        View view = snackbar.getView();
        TextView txtv =
            (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
      }
    }
  }

  private void selectImage() {

    androidx.appcompat.app.AlertDialog.Builder builder;
    builder = new androidx.appcompat.app.AlertDialog.Builder(OwnProfileDetails.this);
    builder.setTitle(R.string.string_255);
    builder.setIcon(R.drawable.orca_attach_camera_pressed);
    builder.setItems(new CharSequence[] {
        getString(R.string.string_1021), getString(R.string.string_1022), getString(R.string.cancel)
    }, new DialogInterface.OnClickListener() {
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
    if (ActivityCompat.checkSelfPermission(OwnProfileDetails.this,
        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(OwnProfileDetails.this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.string_1020)),
            RESULT_LOAD_IMAGE);
      } else {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.string_1020)),
            RESULT_LOAD_IMAGE);
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
      if (ActivityCompat.shouldShowRequestPermissionRationale(OwnProfileDetails.this,
          Manifest.permission.READ_EXTERNAL_STORAGE)
          || ActivityCompat.shouldShowRequestPermissionRationale(OwnProfileDetails.this,
          Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        Snackbar snackbar = Snackbar.make(root, R.string.string_222, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.string_580), new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                ActivityCompat.requestPermissions(OwnProfileDetails.this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 26);
              }
            });

        snackbar.show();

        View view = snackbar.getView();
        TextView txtv =
            (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
      } else {

        ActivityCompat.requestPermissions(OwnProfileDetails.this, new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 26);
      }
    } else if (k == 0) {




      /*
       * For capturing the image permission
       */
      if (ActivityCompat.shouldShowRequestPermissionRationale(OwnProfileDetails.this,
          Manifest.permission.READ_EXTERNAL_STORAGE)
          || ActivityCompat.shouldShowRequestPermissionRationale(OwnProfileDetails.this,
          Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        Snackbar snackbar = Snackbar.make(root, R.string.string_1218, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.string_580), new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                ActivityCompat.requestPermissions(OwnProfileDetails.this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 27);
              }
            });

        snackbar.show();

        View view = snackbar.getView();
        TextView txtv =
            (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
      } else {

        ActivityCompat.requestPermissions(OwnProfileDetails.this, new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 27);
      }
    }
  }

  /**
   * Check access camera permission
   */
  private void checkCameraPermissionImage() {
    if (ActivityCompat.checkSelfPermission(OwnProfileDetails
        .this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

      if (ActivityCompat.checkSelfPermission(OwnProfileDetails.this,
          Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
          intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
          } else {

            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
              String packageName = resolveInfo.activityInfo.packageName;
              grantUriPermission(packageName, imageUri,
                  Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
          }

          startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
        } else {
          Snackbar snackbar = Snackbar.make(root, R.string.string_61, Snackbar.LENGTH_SHORT);
          snackbar.show();

          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
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
    if (ActivityCompat.shouldShowRequestPermissionRationale(OwnProfileDetails.this,
        Manifest.permission.CAMERA)) {

      Snackbar snackbar = Snackbar.make(root, R.string.string_221, Snackbar.LENGTH_INDEFINITE)
          .setAction(getString(R.string.string_580), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              ActivityCompat.requestPermissions(OwnProfileDetails.this,
                  new String[] { Manifest.permission.CAMERA }, 24);
            }
          });

      snackbar.show();

      View view = snackbar.getView();
      TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
      txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    } else {

      ActivityCompat.requestPermissions(OwnProfileDetails.this,
          new String[] { Manifest.permission.CAMERA }, 24);
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
    Uri imgUri = FileProvider.getUriForFile(OwnProfileDetails.this,
        getApplicationContext().getPackageName() + ".provider", file);
    this.imageUri = imgUri;

    this.picturePath = file.getAbsolutePath();

    name = null;
    folder = null;
    file = null;

    return imgUri;
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    setUpActivity();
  }

  @SuppressWarnings("TryWithIdenticalCatches")
  private void setUpActivity() {

    userName.setText(AppController.getInstance().getUserName());

    userIdentifier.setText(AppController.getInstance().getUserIdentifier());

    userStatus.setText(AppController.getInstance().getUserStatus());

    try {

      String userImageUrl = AppController.getInstance().getUserImageUrl();

      /*
       *To load the new image everytime
       *
       */

      if (userImageUrl != null && !userImageUrl.isEmpty()) {
        Glide.with(OwnProfileDetails.this)

            .load(userImageUrl).asBitmap()

            //                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)

            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
            .signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
            .centerCrop()

            .placeholder(R.drawable.chat_attachment_profile_default_image_frame).
            into(new BitmapImageViewTarget(profileImage) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                profileImage.setImageDrawable(circularBitmapDrawable);
              }
            });
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  private void updateTheValueOnServer(final String valueToUpdate, final int type) {
    /*
     * Hit the put Profile api
     */

    JSONObject obj = new JSONObject();

    try {

      obj.put("deviceId", AppController.getInstance().getDeviceId());

      /*
       * DeviceType  1-ios,2-android
       */

      obj.put("deviceType", 2);
      obj.put("deviceModel", Build.MODEL);
      obj.put("deviceMake", Build.MANUFACTURER);
      obj.put("deviceOs", String.valueOf(Build.VERSION.SDK_INT));
      obj.put("pushToken", AppController.getInstance().getPushToken());
      obj.put("appVersion", BuildConfig.VERSION_NAME);
      obj.put("versionCode", BuildConfig.VERSION_CODE);

      switch (type) {
        case 0:
          obj.put("userName", valueToUpdate);
          break;

        case 1:
          obj.put("userName", AppController.getInstance().getUserName());
          obj.put("profilePic", valueToUpdate);
          break;

        //                case 2:
        //                    obj.put("socialStatus", valueToUpdate);
        //                    break;

      }

      /*
       *
       *For update of the Profile pic details
       * */

      //            if (profilePic != null) {
      //                obj.put("profilePic", profilePic);
      //            } else {
      ///*
      // * Might be possible that Profile pic was there but was deleted later,So server guy will delete if any such file exists for given user
      // */
      ////                obj.put("profilePic", "");
      //
      //
      //                if (userAlreadyHasImage) {
      //                    obj.put("profilePic", userImageUrl);
      //                } else {
      //                    obj.put("profilePic", "null");
      //                }
      //            }

    } catch (JSONException e) {
      e.printStackTrace();
    }
    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.PUT, ApiOnServer.USER_PROFILE, obj,
            new com.android.volley.Response.Listener<JSONObject>() {

              @Override
              public void onResponse(JSONObject response) {

                try {

                  switch (response.getInt("code")) {

                    case 200:

                      switch (type) {

                        case 0:
                          /*
                           * Name updated successfully
                           */
                          AppController.getInstance().setUserName(valueToUpdate);

                          JSONObject obj = new JSONObject();

                          obj.put("eventName", "nameUpdated");
                          obj.put("name", valueToUpdate);

                          bus.post(obj);
                          break;

                        case 1:
                          /*
                           * Profile pic updated successfully
                           */
                          AppController.getInstance().setUserImageUrl(valueToUpdate);

                          JSONObject obj2 = new JSONObject();

                          obj2.put("eventName", "profilePicUpdated");
                          obj2.put("profilePicUrl", valueToUpdate);

                          bus.post(obj2);

                          break;

                        //
                        //                                case 2:
                        //
                        //                                      /*
                        //                 * Status updated successfully
                        //                  */
                        //                                    AppController.getInstance().setUserStatus(valueToUpdate);
                        //
                        //                                    break;

                      }

                      break;
                    default:

                      if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, response.getString("message"),
                            Snackbar.LENGTH_SHORT);

                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(
                            com.google.android.material.R.id.snackbar_text);
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
            if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
              SessionObserver sessionObserver = new SessionObserver();
              sessionObserver.getObservable()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean flag) {
                      Handler handler = new Handler();
                      handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          updateTheValueOnServer(valueToUpdate, type);
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
            } else if (root != null) {

              Snackbar snackbar =
                  Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                      Snackbar.LENGTH_SHORT);

              snackbar.show();
              View view = snackbar.getView();
              TextView txtv =
                  (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
              txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
          }
        }) {
          @Override
          public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("authorization", AppController.getInstance().getApiToken());
            headers.put("lang", Constants.LANGUAGE);

            return headers;
          }
        };

    jsonObjReq.setRetryPolicy(
        new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add the request to the RequestQueue.*/
    AppController.getInstance().addToRequestQueue(jsonObjReq, "updateProfileApi");
  }

  private Bitmap getCircleBitmap(Bitmap bitmap) {
    try {
      final Bitmap circuleBitmap =
          Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
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

  @SuppressWarnings("TryWithIdenticalCatches")

  private void uploadProfilePic(Bitmap bitmap) {

    if (picturePath != null) {

      final Uri fileUri;

      final String name = AppController.getInstance().getUserId()
          + System.currentTimeMillis();//String.valueOf(System.currentTimeMillis());
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

        //                final ProgressDialog pDialog = new ProgressDialog(OwnProfileDetails.this, 0);
        //
        //
        //                pDialog.setCancelable(false);
        //
        //
        //                pDialog.setMessage(getString(R.string.Save_Picture));
        //                pDialog.show();
        //
        //                ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);
        //
        //
        //                bar.getIndeterminateDrawable().setColorFilter(
        //                        ContextCompat.getColor(OwnProfileDetails.this, R.color.color_black),
        //                        android.graphics.PorterDuff.Mode.SRC_IN);

        FileUploadService service = ServiceGenerator.createService(FileUploadService.class);

        final File file = FileUtils.getFile(this, fileUri);

        String url = null;

        url = name + ".jpg";

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", url, requestFile);

        String descriptionString = getString(R.string.string_803);
        RequestBody description =
            RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        Call<ResponseBody> call = service.uploadProfilePic(description, body);

        call.enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            //                        if (pDialog.isShowing()) {
            //                            // pDialog.dismiss();
            //                            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();
            //
            //
            //                            if (context instanceof Activity) {
            //
            //
            //                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
            //                                        pDialog.dismiss();
            //                                    }
            //                                } else {
            //
            //
            //                                    if (!((Activity) context).isFinishing()) {
            //                                        pDialog.dismiss();
            //                                    }
            //                                }
            //                            } else {
            //
            //
            //                                try {
            //                                    pDialog.dismiss();
            //                                } catch (final IllegalArgumentException e) {
            //                                    e.printStackTrace();
            //
            //                                } catch (final Exception e) {
            //                                    e.printStackTrace();
            //
            //                                }
            //                            }
            //
            //                        }
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
              updateTheValueOnServer(ApiOnServer.PROFILEPIC_UPLOAD_PATH + url, 1);

              File fdelete = new File(fileUri.getPath());
              if (fdelete.exists()) fdelete.delete();
            } else {

              if (root != null) {

                Snackbar snackbar =
                    Snackbar.make(root, R.string.Upload_Failed, Snackbar.LENGTH_SHORT);

                snackbar.show();
                View view = snackbar.getView();
                TextView txtv =
                    (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
              }
            }
          }

          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
            //                        if (pDialog.isShowing()) {
            //                            // pDialog.dismiss();
            //                            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();
            //
            //
            //                            if (context instanceof Activity) {
            //
            //
            //                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
            //                                        pDialog.dismiss();
            //                                    }
            //                                } else {
            //
            //
            //                                    if (!((Activity) context).isFinishing()) {
            //                                        pDialog.dismiss();
            //                                    }
            //                                }
            //                            } else {
            //
            //
            //                                try {
            //                                    pDialog.dismiss();
            //                                } catch (final IllegalArgumentException e) {
            //                                    e.printStackTrace();
            //
            //                                } catch (final Exception e) {
            //                                    e.printStackTrace();
            //
            //                                }
            //                            }
            //
            //                        }

            if (root != null) {

              Snackbar snackbar =
                  Snackbar.make(root, R.string.Upload_Failed, Snackbar.LENGTH_SHORT);

              snackbar.show();
              View view = snackbar.getView();
              TextView txtv =
                  (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
              txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
          }
        });
      } else {
        if (root != null) {

          Snackbar snackbar = Snackbar.make(root, R.string.Upload_Failed, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
      }
    }
  }

  private void updateTheStatusOnServer(final String socialStatus) {

    JSONObject obj = new JSONObject();

    try {

      obj.put("socialStatus", socialStatus);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.PUT, ApiOnServer.USER_STATUS, obj,
            new com.android.volley.Response.Listener<JSONObject>() {

              @Override
              public void onResponse(JSONObject response) {

                try {

                  switch (response.getInt("code")) {

                    case 200:




                      /*
                       * Status updated successfully
                       */
                      AppController.getInstance().setUserStatus(socialStatus);

                      break;
                    default:

                      if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, response.getString("message"),
                            Snackbar.LENGTH_SHORT);

                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(
                            com.google.android.material.R.id.snackbar_text);
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
              sessionObserver.getObservable()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean flag) {
                      Handler handler = new Handler();
                      handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          updateTheStatusOnServer(socialStatus);
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
            } else if (root != null) {

              Snackbar snackbar3 = Snackbar.make(root, getString(R.string.UpdateStatusFailed),
                  Snackbar.LENGTH_SHORT);

              snackbar3.show();
              View view3 = snackbar3.getView();
              TextView txtv3 =
                  (TextView) view3.findViewById(com.google.android.material.R.id.snackbar_text);
              txtv3.setGravity(Gravity.CENTER_HORIZONTAL);
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

    jsonObjReq.setRetryPolicy(
        new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add the request to the RequestQueue.*/
    AppController.getInstance().addToRequestQueue(jsonObjReq, "updateStatusApi");
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

  private void minimizeCallScreen(JSONObject obj) {
    try {
      Intent intent = new Intent(OwnProfileDetails.this, ChatMessageScreen.class);

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
  protected void onDestroy() {
    super.onDestroy();

    bus.unregister(this);
  }
}
