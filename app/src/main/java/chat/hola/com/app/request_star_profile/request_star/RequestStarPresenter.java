package chat.hola.com.app.request_star_profile.request_star;

/**
 * Created by Hardik
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.request_star_profile.model.StarStatusResponse;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

public class RequestStarPresenter implements RequestStarContract.Presenter {

    private static final String TAG = RequestStarPresenter.class.getSimpleName();
    @Inject
    RequestStarContract.View view;
    @Inject
    Context context;
    private Uri imageUri;
    private String picturePath = null;
    private String type;
    private String requestId;
    private UploadCallback uploadCallback;
    SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    public RequestStarPresenter() {
    }

    @Inject
    HowdooService service;

    @Override
    public void attachView(RequestStarContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void makeStarProfileRequest(String categorieId, String starUserEmail,
                                       String starUserPhoneNumber, String starUserIdProof, String starUserKnownBy,
                                       String description) {
        if (view != null) view.showProgress(true);
        Map<String, Object> map = new HashMap<>();
        map.put("categorieId", categorieId);
        map.put("starUserEmail", starUserEmail);
        map.put("starUserPhoneNumber", starUserPhoneNumber);
        map.put("starUserIdProof", starUserIdProof);
        map.put("starUserKnownBy", starUserKnownBy);
        map.put("description", description);
        service.requestStarProfile(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        if (view != null) view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (view != null) view.requestDone();

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
                                                        makeStarProfileRequest(categorieId, starUserEmail, starUserPhoneNumber,
                                                                starUserIdProof, starUserKnownBy, description);
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
                            default:
                                if (view != null) {
                                    if (response.body() != null) {
                                        view.showMessage(response.body().getMessage(), 0);
                                    }
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) view.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) view.showProgress(false);
                    }
                });
    }

    /*launch the camera*/
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
            if (view != null) view.launchCamera(intent);
        } else {
            if (view != null) view.showSnackMsg(R.string.string_61);
        }
    }

    /*this method is return selected image uri*/
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
        name = null;
        folder = null;
        file = null;
        return imgUri;
    }

    @Override
    public void launchImagePicker() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("image/*");
        } else {
            intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        if (view != null) view.launchImagePicker(intent);
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
                        if (view != null) view.launchCropImage(data.getData());
                    } else {
                        //image can't be selected try another
                        if (view != null) view.showSnackMsg(R.string.string_31);
                    }
                } else {
                    //image can't be selected try another
                    if (view != null) view.showSnackMsg(R.string.string_31);
                }
            } catch (OutOfMemoryError e) {
                //out of mem try again
                if (view != null) view.showSnackMsg(R.string.string_15);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //image selection canceled.
            if (view != null) view.showSnackMsg(R.string.string_16);
        } else {
            //failed to select image.
            if (view != null) view.showSnackMsg(R.string.string_113);
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
                    if (view != null) view.launchCropImage(imageUri);
                } else {
                    //failed to capture image.
                    picturePath = null;
                    if (view != null) view.showSnackMsg(R.string.string_17);
                }
            } catch (OutOfMemoryError e) {
                //out of mem try again
                picturePath = null;
                if (view != null) view.showSnackMsg(R.string.string_15);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            picturePath = null;
            //img capture canceled.
            if (view != null) view.showSnackMsg(R.string.string_18);
        } else {
            //sorry failed to capture
            picturePath = null;
            if (view != null) view.showSnackMsg(R.string.string_17);
        }
    }

    @Override
    public void parseCropedImage(int requestCode, int resultCode, Intent data) {
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
                    //if (view!=null)view.showSnackMsg(R.string.done);
                    uploadPicture(picturePath);
                } else {
                    //sorry failed to capture
                    picturePath = null;
                    if (view != null) view.showSnackMsg(R.string.string_19);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                //sorry failed to capture
                picturePath = null;
                if (view != null) view.showSnackMsg(R.string.string_19);
            }
        } catch (OutOfMemoryError e) {
            //out of mem try again
            picturePath = null;
            if (view != null) view.showSnackMsg(R.string.string_15);
        }
    }

    private void uploadPicture(String picturePath) {
        if (view != null) view.showProgress(true);
        uploadCallback = new UploadCallback() {
            @Override
            public void onStart(String requestId) {
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                if (view != null) view.showProgress(false);
                Log.w(TAG, "upload pic successful!!");
                String imgUrl = (String) resultData.get(Constants.Post.URL);

                if (view != null) view.imageUploadSuccess(imgUrl);
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                if (view != null) view.showProgress(false);
                Log.e(TAG, "failed to upload pic!! " + error.getDescription());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (view != null) view.showProgress(false);
                    }
                });
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
            }
        };

        //if (view!=null)view.showProgress(true);
        try {
            requestId = MediaManager.get()
                    .upload(picturePath)
                    .option(Constants.Post.FOLDER,Constants.Cloudinary.others)
                    .option(Constants.Post.RESOURCE_TYPE, Constants.Post.IMAGE)
                    .callback(uploadCallback)
                    .constrain(TimeWindow.immediate())
                    .dispatch();
        } catch (Exception ignored) {
            if (view != null) view.showProgress(false);
            ignored.printStackTrace();
        }
    }

    @Override
    public void loadProfileData() {

        if (view != null) view.showProgress(true);
        service.getUserProfile(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Profile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Profile> response) {

                        if (view != null) view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (response.body() != null) {
                                    if (view != null) view.showStarData(response.body());
                                }
                                break;
                            case 401:
                                if (view != null) view.sessionExpired();
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
                                                        loadProfileData();
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
                        if (view != null) view.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) view.showProgress(false);
                    }
                });
    }

    @Override
    public void getStarStatus() {
        if (view != null) view.showProgress(true);

        service.getStarStatus(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<StarStatusResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<StarStatusResponse> response) {

                        if (view != null) view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (response.body() != null) {
                                    if (view != null) {
                                        view.updateStatus(response.body().getData());
                                    }
                                }
                                break;
                            case 401:
                                if (view != null) view.sessionExpired();
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
                                                        getStarStatus();
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
                        if (view != null) view.showProgress(false);
                        if (view != null) {
                            view.showMessage(e.getMessage(), 0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) view.showProgress(false);
                    }
                });
    }
}
