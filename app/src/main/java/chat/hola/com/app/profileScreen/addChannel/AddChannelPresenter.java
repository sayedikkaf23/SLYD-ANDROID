package chat.hola.com.app.profileScreen.addChannel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.post.model.CategoryResponse;
import chat.hola.com.app.profileScreen.addChannel.model.AddChannelResponse;
import chat.hola.com.app.profileScreen.addChannel.model.ChannelBody;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ankit on 22/2/18.
 */

public class AddChannelPresenter implements AddChannelContract.Presenter {

  private final String TAG = AddChannelPresenter.class.getSimpleName();

  @Inject
  AddChannelContract.View view;

  @Inject
  HowdooService service;

  @Inject
  Context context;
  @Inject
  NetworkConnector networkConnector;
  UploadCallback uploadCallback;

  private boolean isPhotoChanged = false;
  private String requestId;
  private String path;
  private String type;
  private String picturePath;
  private Uri imageUri;
  private Bitmap bitmapToUpload, bitmap;
  private boolean userAlreadyHasImage;
  private Object userImageUrl;
  private SessionApiCall sessionApiCall = new SessionApiCall();
  private ChannelBody channelBody = new ChannelBody();

  @Inject
  public AddChannelPresenter() {
  }

  @Override
  public void init() {
    view.applyFont();
  }

  @Override
  public void addChannel(@NonNull final String channelName, final String channelDesc,
      final Boolean isPrivate, String categoryId) {

    channelBody.setChannelDesc(channelDesc);
    channelBody.setCategoryId(categoryId);
    channelBody.set_private(isPrivate);
    channelBody.setChannelName(channelName);

    if (!validateChannelData(picturePath, channelName, channelDesc)) return;
    uploadCallback = new UploadCallback() {
      @Override
      public void onStart(String requestId) {
      }

      @Override
      public void onProgress(String requestId, long bytes, long totalBytes) {
      }

      @Override
      public void onSuccess(final String requestId, Map resultData) {
        if (resultData.get(Constants.Post.RESOURCE_TYPE).equals("video")) {
          type = "1";
        } else {
          type = "0";
        }
        String pictureUrl = (String) resultData.get(Constants.Post.URL);

        if (pictureUrl != null && !pictureUrl.isEmpty()) {
          channelBody.setChannelPhotoUrl(pictureUrl);
          createChannel();
        }
      }

      @Override
      public void onError(String requestId, ErrorInfo error) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
          @Override
          public void run() {
            view.showProgress(false);
          }
        });
      }

      @Override
      public void onReschedule(String requestId, ErrorInfo error) {

      }
    };

    uploadPicture(picturePath);
  }

  private void createChannel() {
    service.createChannel(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
        channelBody)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<AddChannelResponse>>() {
          @Override
          public void onNext(Response<AddChannelResponse> response) {
            switch (response.code()) {
              case 200:
                if (view != null) view.showMessage(null, R.string.channelCreated);
                Log.w(TAG, "channel created successfully!!");
                break;
              case 401:
                view.sessionExpired();
                break;
              case 406:
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
                            addChannel(channelBody.getChannelName(), channelBody.getChannelDesc(),
                                channelBody.get_private(), channelBody.getCategoryId());
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
                sessionApiCall.getNewSession(service, sessionObserver);
                break;
            }

            if (view != null) {
              view.showProgress(false);
              view.finishActivity();
            }
          }

          @Override
          public void onError(Throwable e) {

            if (view != null) {
              view.showMessage(null, R.string.channelCreateFailed);
              view.showProgress(false);
            }
          }

          @Override
          public void onComplete() {
            this.dispose();
          }
        });
  }

  private void uploadPicture(String picturePath) {
    Log.w(TAG, picturePath);
    view.showProgress(true);
    try {
      requestId = MediaManager.get()
          .upload(picturePath)
          .option(Constants.Post.FOLDER,Constants.Cloudinary.channel_image)
          .option(Constants.Post.RESOURCE_TYPE, type)
          .callback(uploadCallback)
          .constrain(TimeWindow.immediate())
          .dispatch();
    } catch (Exception ignored) {
    }
  }

  private boolean validateChannelData(String picturePath, String channelName, String channelDesc) {
    if (picturePath == null || picturePath.isEmpty()) {
      view.showMessage(null, R.string.addChannelPic);
      return false;
    }

    if (channelName == null || channelName.isEmpty()) {
      view.showMessage(null, R.string.msgAddChannelName);
      return false;
    }

    //        if (categoryId == null || categoryId.isEmpty()) {
    //            view.showMessage(null,R.string.msgAddChannelCategory);
    //            return false;
    //        }

    if (channelDesc == null || channelDesc.isEmpty()) {
      view.showMessage(null, R.string.msgAddChannelDesc);
      return false;
    }
    return true;
  }

  @Override
  public void loadCategories() {

    service.getCategories(AppController.getInstance().getApiToken(), "en")
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<CategoryResponse>>() {
          @Override
          public void onNext(Response<CategoryResponse> categoryResponse) {
            switch (categoryResponse.code()) {
              case 200:
                view.showCategories(categoryResponse.body().getData());
                break;
              case 401:
                view.sessionExpired();
                break;
              case 406:
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
                            loadCategories();
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
                sessionApiCall.getNewSession(service, sessionObserver);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {
            Log.w(TAG, "getCategory response completed!: ");
          }
        });
  }

  @Override
  public void launchCamera(PackageManager packageManager) {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (intent.resolveActivity(packageManager) != null) {
      intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      } else {
        List<ResolveInfo> resInfoList =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
          String packageName = resolveInfo.activityInfo.packageName;
          context.grantUriPermission(packageName, imageUri,
              Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
      }
      view.launchCamera(intent);
    } else {
      view.showSnackMsg(R.string.string_61);
    }
  }

  @Override
  public void launchImagePicker() {
    Intent intent = null;
    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.setType("image/*");

    view.launchImagePicker(intent);
  }

  @Override
  public void parseSelectedImage(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      try {
        Uri uri = data.getData();
        //TODO: it will prevent further crash ( getting uri as null on android 7.0 Mi phone).
        if (uri == null) return;
        //picturePath = UriUtil.getPath(context, uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          picturePath = ImageFilePath.getPathAboveN(context, uri);
        } else {

          picturePath = ImageFilePath.getPath(context, uri);
        }
        if (picturePath != null) {
          final BitmapFactory.Options options = new BitmapFactory.Options();
          options.inJustDecodeBounds = true;
          BitmapFactory.decodeFile(picturePath, options);

          if (options.outWidth > 0 && options.outHeight > 0) {

            //launch crop image
            view.launchCropImage(data.getData());
          } else {
            //image can't be selected try another
            view.showSnackMsg(R.string.string_31);
          }
        } else {
          //image can't be selected try another
          view.showSnackMsg(R.string.string_31);
        }
      } catch (OutOfMemoryError e) {
        //out of mem try again
        view.showSnackMsg(R.string.string_15);
      }
    } else if (resultCode == Activity.RESULT_CANCELED) {
      //image selection canceled.
      view.showSnackMsg(R.string.string_16);
    } else {
      //failed to select image.
      view.showSnackMsg(R.string.string_113);
    }
  }

  @Override
  public void parseCapturedImage(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      try {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        if (options.outWidth > 0 && options.outHeight > 0) {
          //launch crop image
          view.launchCropImage(imageUri);
        } else {
          //failed to capture image.
          picturePath = null;
          view.showSnackMsg(R.string.string_17);
        }
      } catch (OutOfMemoryError e) {
        //out of mem try again
        picturePath = null;
        view.showSnackMsg(R.string.string_15);
      }
    } else if (resultCode == Activity.RESULT_CANCELED) {
      picturePath = null;
      //img capture canceled.
      view.showSnackMsg(R.string.string_18);
    } else {
      //sorry failed to capture
      picturePath = null;
      view.showSnackMsg(R.string.string_17);
    }
  }

  @Override
  public void parseCropedImage(int requestCode, int resultCode, Intent data) {
    try {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == RESULT_OK) {
        //picturePath = UriUtil.getPath(context, result.getUri());
        isPhotoChanged = true;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //  picturePath = ImageFilePath.getPathAboveN(context, result.getUri());
        //} else {

          picturePath = ImageFilePath.getPath(context, result.getUri());
        //}

        if (picturePath != null) {
          bitmapToUpload = BitmapFactory.decodeFile(picturePath);
          bitmap = getCircleBitmap(bitmapToUpload);
          if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            view.setProfileImage(bitmap);
            userAlreadyHasImage = false;
            userImageUrl = null;
          } else {
            //sorry failed to capture
            picturePath = null;
            isPhotoChanged = false;
            view.showSnackMsg(R.string.string_19);
          }
        } else {
          //sorry failed to capture
          picturePath = null;
          isPhotoChanged = false;
          view.showSnackMsg(R.string.string_19);
        }
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

        //sorry failed to capture
        picturePath = null;
        isPhotoChanged = false;
        view.showSnackMsg(R.string.string_19);
      }
    } catch (OutOfMemoryError e) {
      //out of mem try again
      picturePath = null;
      isPhotoChanged = false;
      view.showSnackMsg(R.string.string_15);
    }
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

  private Uri setImageUri() {
    String name = Utilities.tsInGmt();
    name = new Utilities().gmtToEpoch(name);
    File folder = new File(context.getExternalFilesDir(null) + ApiOnServer.IMAGE_CAPTURE_URI);
    if (!folder.exists() && !folder.isDirectory()) {
      folder.mkdirs();
    }
    File file =
        new File(context.getExternalFilesDir(null) + ApiOnServer.IMAGE_CAPTURE_URI, name + ".jpg");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    Uri imgUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    this.imageUri = imgUri;
    this.picturePath = file.getAbsolutePath();
    isPhotoChanged = true;
    return imgUri;
  }

  public void updateChannel(String image, String channelId, String channelName, String channelDesc,
      boolean isPrivate, String categoryId) {

    if (image == null || isPhotoChanged) {
      if (!validateChannelData(picturePath, channelName, channelDesc)) return;
      uploadCallback = new UploadCallback() {
        @Override
        public void onStart(String requestId) {
        }

        @Override
        public void onProgress(String requestId, long bytes, long totalBytes) {
        }

        @Override
        public void onSuccess(final String requestId, Map resultData) {
          if (resultData.get(Constants.Post.RESOURCE_TYPE).equals("video")) {
            type = "1";
          } else {
            type = "0";
          }
          String pictureUrl = (String) resultData.get(Constants.Post.URL);
          update(pictureUrl, channelId, categoryId, channelName, channelDesc, isPrivate);
        }

        @Override
        public void onError(String requestId, ErrorInfo error) {

          new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
              view.showProgress(false);
            }
          });
        }

        @Override
        public void onReschedule(String requestId, ErrorInfo error) {

        }
      };

      uploadPicture(picturePath);
    } else {
      update(image, channelId, categoryId, channelName, channelDesc, isPrivate);
    }
  }

  private void update(String pictureUrl, String channelId, String categoryId, String channelName,
      String channelDesc, boolean isPrivate) {
    if (pictureUrl != null && !pictureUrl.isEmpty()) {

      Map<String, Object> map = new HashMap<>();
      map.put("channelName", channelName);
      map.put("channelImageUrl", pictureUrl);
      map.put("description", channelDesc);
      map.put("categoryId", categoryId);
      map.put("isPrivate", isPrivate);
      map.put("channelId", channelId);

      //            ChannelBody channelBody = new ChannelBody();
      //            channelBody.setChannelName(channelName);
      //            channelBody.setChannelPhotoUrl(pictureUrl);
      //            channelBody.setChannelDesc(channelDesc);
      //            channelBody.setCategoryId(categoryId);
      //            channelBody.setPrivate(isPrivate);
      //            channelBody.setChannelId(channelId);
      service.updateChannel(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
          .subscribeOn(Schedulers.newThread())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new DisposableObserver<Response<AddChannelResponse>>() {
            @Override
            public void onNext(Response<AddChannelResponse> response) {
              switch (response.code()) {
                case 200:
                  if (view != null) view.showMessage(null, R.string.channelUpdated);
                  break;
                case 401:
                  view.sessionExpired();
                  break;

                case 406:
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
                              update(pictureUrl, channelId, categoryId, channelName, channelDesc,
                                  isPrivate);
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
                  sessionApiCall.getNewSession(service, sessionObserver);
                  break;
              }

              if (view != null) {
                view.showProgress(false);
                view.finishActivity();
              }
            }

            @Override
            public void onError(Throwable e) {

              if (view != null) {
                view.showMessage(null, R.string.channelCreateFailed);
                view.showProgress(false);
              }
            }

            @Override
            public void onComplete() {
              this.dispose();
            }
          });
    }
  }
}
