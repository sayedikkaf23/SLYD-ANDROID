package chat.hola.com.app.profileScreen.business.form;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.util.Log;

import androidx.core.content.FileProvider;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.UriUtil;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * <h1>BusinessProfileFormPresenter</h1>
 *
 * @author Shaktisnh Jadeja
 * @version 1.0
 * @since 14 August 2019
 */
public class BusinessProfileFormPresenter implements BusinessProfileFormContract.Presenter {

    @Inject
    Context context;
    @Inject
    HowdooService service;
    @Inject
    BusinessProfileFormContract.View view;
    @Inject
    SessionManager sessionManager;
    private String name;
    private String picturePath = null;
    private String coverPath = null;
    private String profilePath = null;
    private Uri imageUri;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    BusinessProfileFormPresenter() {
    }

    @Override
    public void applyBusinessProfile(Map<String, Object> params) {
        if (view != null) view.showProgress(true);
        params.put("businessProfilePic", profilePath);
        params.put("businessProfileCoverImage", coverPath);
        service.applyBusinessProfile(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        if (view != null) view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (view != null) {
                                    view.showMessage(null, R.string.sucess_business_profile_created);
                                    view.profile();
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
                                                        applyBusinessProfile(params);
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
                                break;
                            default:
                                Gson gson = new GsonBuilder().create();
                                Error error = new Error();
                                try {
                                    if (response.errorBody() != null) {
                                        error = gson.fromJson(response.errorBody().string(), Error.class);
                                        if (view != null && error != null)
                                            view.showMessage(error.getMessage(), -1);
                                    }
                                } catch (IOException ignored) {
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            view.showProgress(false);
                            view.showMessage("Oops something went wrong!!!", -1);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) view.showProgress(false);
                    }
                });
    }

    @Override
    public void businessEmailVerificationCode(String email) {
        if (view != null) view.showProgress(true);
        Map<String, String> params = new HashMap<>();
        params.put("bussinessEmailId", email);
        service.businessEmailVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (view != null) view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (view != null) view.verifyEmailAddress();
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
                                                        businessEmailVerificationCode(email);
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
                                break;
                            default:
                                if (view != null)
                                    view.showMessage("Oops something went wrong!!!", -1);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            view.showProgress(false);
                            view.showMessage("Oops something went wrong!!!", -1);
                        }
                    }

                    @Override
                    public void onComplete() {

                        if (view != null) view.showProgress(false);
                    }
                });
    }

    @Override
    public void businessPhoneVerificationCode(String countryCode, String phone) {
        if (view != null) view.showProgress(true);
        Map<String, String> params = new HashMap<>();
        params.put("countryCode", countryCode);
        params.put("businessPhone", phone);
        service.businessPhoneVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (view != null) view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (view != null) view.verifyMobile();
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
                                                        businessPhoneVerificationCode(countryCode, phone);
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
                                break;
                            default:
                                if (view != null)
                                    view.showMessage("Oops something went wrong!!!", -1);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            view.showProgress(false);
                            view.showMessage("Oops something went wrong!!!", -1);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) view.showProgress(false);
                    }
                });
    }

    @Override
    public void launchCamera(PackageManager packageManager, boolean isProfile) {
        if (view != null) view.launchCamera(isProfile);
    }

    @Override
    public void launchImagePicker(boolean isProfile) {
        if (view != null) view.launchImagePicker(isProfile);
    }

//    @Override
//    public void initAddBusiness(Map<String, Object> params, UploadFileAmazonS3 amazonS3,
//                                boolean isPicChange) {
//        if (view != null) view.showProgress(true);
//        if (profilePath != null) params.put("businessProfilePic", profilePath);
//        if (coverPath != null) params.put("businessProfileCoverImage", coverPath);
//
//        applyBusinessProfile(params, amazonS3, isPicChange);
//    }

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

    @Override
    public void parseSelectedImage(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                //TODO: it will prevent further crash ( getting uri as null on android 7.0 Mi phone).
                if (uri == null) return;
                //here
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
                        if (view != null) view.showMessage(null, R.string.string_31);
                    }
                } else {
                    //image can't be selected try another
                    if (view != null) view.showMessage(null, R.string.string_31);
                }
            } catch (OutOfMemoryError e) {
                //out of mem try again
                if (view != null) view.showMessage(null, R.string.string_15);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //image selection canceled.
            if (view != null) view.showMessage(null, R.string.string_16);
        } else {
            //failed to select image.
            if (view != null) view.showMessage(null, R.string.string_113);
        }
    }

    @Override
    public void parseCapturedImage(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {

                picturePath = data.getStringExtra("imagePath");
                imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider",
                        new File(picturePath));

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, options);
                if (options.outWidth > 0 && options.outHeight > 0) {
                    //launch crop image
                    //here
                    if (view != null) view.launchCropImage(imageUri);
                } else {
                    //failed to capture image.
                    picturePath = null;
                    if (view != null) view.showMessage(null, R.string.string_17);
                }
            } catch (OutOfMemoryError e) {
                //out of mem try again
                picturePath = null;
                if (view != null) view.showMessage(null, R.string.string_15);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            picturePath = null;
            //img capture canceled.
            if (view != null) view.showMessage(null, R.string.string_18);
        } else {
            //sorry failed to capture
            picturePath = null;
            if (view != null) view.showMessage(null, R.string.string_17);
        }
    }

    @Override
    public void parseCropedImage(int requestCode, int resultCode, Intent data, boolean isProfile) {
        try {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap bitmapToUpload;
                if (isProfile) {
                    String profilePath1 = UriUtil.getPath(context, result.getUri());
                    if (profilePath1 != null) {

                        bitmapToUpload = BitmapFactory.decodeFile(profilePath1);
                        Bitmap bitmap = getCircleBitmap(bitmapToUpload);
                        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                            if (view != null) view.setProfileImage(bitmap);
                            profilePath = profilePath1;
                            uploadImage(profilePath, true);
                        } else {
                            if (view != null) view.showMessage(null, R.string.string_19);
                        }
                    } else {
                        if (view != null) view.showMessage(null, R.string.string_19);
                    }
                } else {
                    coverPath = UriUtil.getPath(context, result.getUri());
                    if (coverPath != null) {
                        bitmapToUpload = BitmapFactory.decodeFile(coverPath);
                        if (bitmapToUpload != null
                                && bitmapToUpload.getWidth() > 0
                                && bitmapToUpload.getHeight() > 0) {
                            if (view != null) view.setCover(bitmapToUpload);
                            uploadImage(coverPath, false);
                        } else {
                            if (view != null) view.showMessage(null, R.string.string_19);
                        }
                    } else {
                        if (view != null) view.showMessage(null, R.string.string_19);
                    }
                }
            }
        } catch (OutOfMemoryError e) {
            //out of mem try again
            if (isProfile) {
                profilePath = null;
            } else {
                coverPath = null;
            }
            if (view != null) view.showMessage(null, R.string.string_15);
        }
    }

    private void uploadImage(String image, boolean isProfilePic) {
        MediaManager.get()
                .upload(image)
                .option(Constants.Post.FOLDER, isProfilePic ? Constants.BUSINESS_PROFILE : Constants.BUSINESS_COVER)
                .option(Constants.Post.PUBLIC_ID, sessionManager.getUserId())
                .option(Constants.Post.RESOURCE_TYPE, Constants.Post.IMAGE)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String path = String.valueOf(resultData.get("url")).replace("http", "https");
                        if (isProfilePic)
                            profilePath = path;
                        else
                            coverPath = path;
                        Log.i("cloudinary2", path);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .constrain(TimeWindow.immediate())
                .dispatch();
    }

    @Override
    public void verifyIsEmailRegistered(String email) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("type", "1");

        service.emailPhoneVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        view.emailAvailable(response.code() == 204);
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
    public void verifyIsUserNameRegistered(String userName) {
        service.verifyIsUserNameRegistered(Utilities.getThings(), userName, Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {

                            case 200:
                                view.userNameAvailable(false);
                                break;
                            case 204:
                                view.userNameAvailable(true);
                                break;
                            case 406:
                                break;
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
    public void verifyIsPhoneRegistered(String phone, String countryCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("countryCode", countryCode);
        map.put("type", "2");

        service.emailPhoneVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        view.phoneAvailable(response.code() == 204);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
