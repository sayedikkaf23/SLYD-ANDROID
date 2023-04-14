package chat.hola.com.app.home.stories;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.cameraActivities.SandriosCamera;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.home.stories.model.ClickListner;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.home.stories.model.StoryModel;
import chat.hola.com.app.home.stories.model.StoryObserver;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.home.stories.model.StoryResponse;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import com.ezcall.android.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * <h1>StoriesPresenter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/26/2018
 */

public class StoriesPresenter
    implements StoriesContract.Presenter, ClickListner, SandriosCamera.CameraCallback {

  @Inject
  StoryModel model;
  @Inject
  HowdooService service;
  @Inject
  StoryObserver storyObserver;
  @Inject
  NetworkConnector networkConnector;
  private SessionApiCall sessionApiCall = new SessionApiCall();
  @Nullable
  private StoriesContract.View view;
  private String picturePath;
  private Context context;

  @Inject
  public StoriesPresenter(Context context) {
    this.context = context;
  }

  public void setView(@Nullable StoriesContract.View view) {
    this.view = view;
  }

  @Override
  public void myStories() {

    service.getMyStories(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<StoryResponse>>() {
          @Override
          public void onNext(Response<StoryResponse> response) {
            switch (response.code()) {
              case 200:
                if (response.body().getData().size() > 0) {
                  try {
                    List<StoryPost> dataList = response.body().getData().get(0).getPosts();
                    if (model.myStories(dataList)) {
                      if(view!=null)
                      view.myStories(dataList.get(0).getUrlPath(), dataList.get(0).getTimestamp());
                    }
                  } catch (Exception ignored) {

                  }
                }
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
                            myStories();
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
            if (view != null) {
              view.isInternetAvailable(networkConnector.isConnected());
            }
            Log.e(getClass().getName(), "onError: " + e.toString());
          }

          @Override
          public void onComplete() {

          }
        });
  }

  @Override
  public void stories() {

    service.getStories(AppController.getInstance().getApiToken(), Constants.LANGUAGE,0, 100)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<StoryResponse>>() {
          @Override
          public void onNext(Response<StoryResponse> response) {
            try {
              switch (response.code()) {
                case 200:
                  if (response.body().getData().size() > 0) {
                    model.stories(response.body().getData());
                    if (view != null) {
                      view.isDataAvailable(response.body().getData().isEmpty());
                    }
                  }
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
                                  stories();
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
//                  sessionApiCall.getNewSession(service, sessionObserver);
                  break;
                case 404:
                  if (view != null) {
                    view.isDataAvailable(true);
//                    Toast.makeText(context, "No data found " + response.code(), Toast.LENGTH_SHORT).show();
                  }
                  break;
                case 500:
                  if (view != null) {
                    view.isDataAvailable(true);
//                    Toast.makeText(context, "No data found " + response.code(), Toast.LENGTH_SHORT).show();
                  }
                  break;
              }
            } catch (Exception ignored) {

            }
          }

          @Override
          public void onError(Throwable e) {
            if (view != null) {
              view.isInternetAvailable(networkConnector.isConnected());
            }
          }

          @Override
          public void onComplete() {

          }
        });
  }

  @Override
  public void onItemClick(int position) {
    assert view != null;
    view.preview(position);
  }

  @Override
  public void onComplete(CameraOutputModel cameraOutputModel) {
    assert view != null;
    view.onComplete(model.getCameraOutputModel(cameraOutputModel));
  }

  @Override
  public void attachView(StoriesContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {
    this.view = null;
  }

  public void launchImagePicker() {
    assert view != null;
    view.launchImagePicker(model.launchImahePicker());
  }

  @Override
  public void parseMedia(int resultCode, Intent data) {
    if (resultCode == RESULT_OK) {
      try {
        Uri uri = data.getData();
        if (uri == null) return;

        //picturePath = UriUtil.getPath(context, uri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          picturePath = ImageFilePath.getPathAboveN(context, uri);
        } else {

          picturePath = ImageFilePath.getPath(context, uri);
        }
        if (picturePath != null) {

          String extension = picturePath.substring(picturePath.lastIndexOf(".") + 1);
          if (extension.equalsIgnoreCase("mp4")) {
            File file = new File(picturePath);

            if (file.length() <= (Constants.Camera.FILE_SIZE * 1024 * 1024)) {
              assert view != null;
              view.launchActivity(new CameraOutputModel(1, picturePath));
            } else {
              assert view != null;
              view.showMessage(null, R.string.video_size_message);
            }
          } else {
            //image
            parseSelectedImage(uri, picturePath);
          }
        } else {
          //image can't be selected try another
          assert view != null;
          view.showMessage(null, R.string.string_31);
        }
      } catch (OutOfMemoryError e) {
        //out of mem try again
        assert view != null;
        assert view != null;
        view.showMessage(null, R.string.string_15);
      }
    } else if (resultCode == Activity.RESULT_CANCELED) {
      //image selection canceled.
      assert view != null;
      view.showMessage(null, R.string.string_16);
    } else {
      //failed to select image.
      assert view != null;
      view.showMessage(null, R.string.string_113);
    }
  }

  @Override
  public void parseSelectedImage(Uri uri, String picturePath) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(picturePath, options);

    if (options.outWidth > 0 && options.outHeight > 0) {
      //launch crop image
      assert view != null;
      view.launchCropImage(uri);
    } else {
      //image can't be selected try another
      assert view != null;
      view.showMessage(null, R.string.string_31);
    }
  }

  @Override
  public void parseCropedImage(int resultCode, Intent data) {
    try {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == RESULT_OK) {
        //picturePath = UriUtil.getPath(context, result.getUri());
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        //  picturePath = ImageFilePath.getPathAboveN(context, result.getUri());
        //} else {

          picturePath = ImageFilePath.getPath(context, result.getUri());
        //}
        if (picturePath != null) {
          Bitmap bitmapToUpload = BitmapFactory.decodeFile(picturePath);
          Bitmap bitmap = model.getCircleBitmap(bitmapToUpload);
          if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            assert view != null;
            view.launchActivity(new CameraOutputModel(0, picturePath));
          } else {
            picturePath = null;
            assert view != null;
            view.showMessage(null, R.string.string_19);
          }
        } else {
          picturePath = null;
          assert view != null;
          view.showMessage(null, R.string.string_19);
        }
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        picturePath = null;
        assert view != null;
        view.showMessage(null, R.string.string_19);
      }
    } catch (OutOfMemoryError e) {
      picturePath = null;
      assert view != null;
      view.showMessage(null, R.string.string_15);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    if (requestCode == StoriesFrag.READ_STORAGE_REQ_CODE) {
      if (grantResults.length == 2
          && grantResults[0] == PackageManager.PERMISSION_GRANTED
          && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
          launchImagePicker();
        } else {
          assert view != null;
          view.showMessage(null, R.string.string_1006);
        }
      } else {
        assert view != null;
        view.showMessage(null, R.string.string_1006);
      }
    }
  }

  @Override
  public void storyObserver() {
    storyObserver.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<StoryPost>() {
          @Override
          public void onNext(StoryPost data) {

            if (data.isSuccess()) {
              myStories();
              //                            model.addStory(data);
              //                            assert view != null;
              //                            view.myStories(data.getUrlPath(), data.getTimestamp());
            }
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {
          }
        });
  }

  @Override
  public StoryData getStoryData(int position) {
    return model.getStoryData(position);
  }

  @Override
  public List<StoryPost> getStoryPosts() {
    return model.getStoryPosts();
  }

  @Override
  public List<StoryPost> getStoryPosts(int position) {
    return model.getStoryPosts(position);
  }

  @Override
  public List<StoryData> getAllStoryData() {
    return model.getAllStoryData();
  }

  public void updateViewed(int position) {
    model.updateData(position);
  }

  public void viewedAll() {
    model.updateAll();
  }
}
