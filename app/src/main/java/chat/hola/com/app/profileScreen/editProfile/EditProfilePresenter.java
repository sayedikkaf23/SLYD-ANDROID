package chat.hola.com.app.profileScreen.editProfile;

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

import chat.hola.com.app.Activities.ChatCameraActivity;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileBody;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileResponse;
import chat.hola.com.app.profileScreen.model.Data;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;

import chat.hola.com.app.profileScreen.model.Profile;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * <h>EditProfilePresenter</h>
 *
 * @author 3Embed
 * @since 22/2/18.
 */

public class EditProfilePresenter implements EditProfileContract.Presenter {
    private SessionApiCall sessionApiCall = new SessionApiCall();
    private static final String TAG = EditProfilePresenter.class.getSimpleName();
    @Inject
    EditProfileContract.View view;
    @Inject
    Context context;
    @Inject
    NetworkConnector networkConnector;

    @Inject
    HowdooService service;
    private String picturePath = null;
    private String coverPath = null;
    private String profilePath = null;
    private Uri imageUri;
    private String name;
    private boolean isBusiness;
    private String imageUrl;

    @Inject
    public EditProfilePresenter() {
    }

    @Override
    public void init() {
        view.applyFont();
        profile();
    }

    private void profile() {
        service.getUserProfile(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Profile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Profile> response) {
                        view.showProfileData(response.body());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void initUpdateProfile(final EditProfileBody profileBody, Data profileData, UploadFileAmazonS3 amazonS3, boolean isPicChange, boolean isBusiness) {
        view.showProgress(true);
        updateProfile(profileBody, isPicChange, isBusiness);
    }

    @Override
    public void launchCamera(PackageManager packageManager, boolean isProfile) {
        Intent intent = new Intent(context, ChatCameraActivity.class);
        intent.putExtra("requestType", "EditProfile");
        view.launchCamera(intent, isProfile);
    }

    @Override
    public void launchImagePicker(boolean isProfile) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("image/*");

        view.launchImagePicker(intent, isProfile);
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

                picturePath = data.getStringExtra("imagePath");
                imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider",
                        new File(picturePath));

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, options);
                if (options.outWidth > 0 && options.outHeight > 0) {
                    //launch crop image
                    //here
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
    public void parseCropedImage(int requestCode, int resultCode, Intent data, boolean isProfile) {
        try {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap bitmapToUpload;
                if (isProfile) {
                    String profilePath1;
                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //  profilePath1 = ImageFilePath.getPathAboveN(context, result.getUri());
                    //} else {

                    profilePath1 = ImageFilePath.getPath(context, result.getUri());
                    //}

                    if (profilePath1 != null) {

                        bitmapToUpload = BitmapFactory.decodeFile(profilePath1);
                        Bitmap bitmap = getCircleBitmap(bitmapToUpload);
                        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                            view.setProfileImage(bitmap);
                            profilePath = profilePath1;
                        } else {
                            view.showSnackMsg(R.string.string_19);
                        }
                    } else {
                        view.showSnackMsg(R.string.string_19);
                    }
                } else {
                    //coverPath = UriUtil.getPath(context, result.getUri());

                    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //  coverPath = ImageFilePath.getPathAboveN(context, result.getUri());
                    //} else {

                    coverPath = ImageFilePath.getPath(context, result.getUri());
                    //}
                    if (coverPath != null) {
                        bitmapToUpload = BitmapFactory.decodeFile(coverPath);
                        if (bitmapToUpload != null
                                && bitmapToUpload.getWidth() > 0
                                && bitmapToUpload.getHeight() > 0) {
                            view.setCover(bitmapToUpload);
                        } else {
                            view.showSnackMsg(R.string.string_19);
                        }
                    } else {
                        view.showSnackMsg(R.string.string_19);
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

    private void updateProfile(EditProfileBody profileBody, boolean isPicChange, boolean isBusiness) {
        this.isBusiness = isBusiness;
        if (isPicChange) {
            if (profilePath != null)
                profilePicUpload(profilePath, profileBody, false);
            if (coverPath != null)
                profilePicUpload(coverPath, profileBody, true);
        } else {
            updateProfileDataToServer(profileBody);
        }
    }

    /*
     * Bug Title: Edit the banner image and save it , on clicking back button it should be updated on the profile page
     * Bug Id: DUBAND142
     * Fix Desc: method
     * Fix Dev: hardik
     * Fix Date: 6/5/21
     * */
    private void profilePicUpload(String filePath, EditProfileBody profileBody, boolean isCover) {
        File file = new File(filePath);
        MediaManager.get()
                .upload(file.getAbsolutePath())
                .option(Constants.Post.FOLDER, isCover ? Constants.Cloudinary.cover_image : Constants.Cloudinary.profile_image)
                .option(Constants.Post.RESOURCE_TYPE, Constants.Post.IMAGE)
                .option(Constants.Post.PUBLIC_ID, AppController.getInstance().getUserId())
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        //                            Double progress = (double) bytes / totalBytes;
                        //                            Log.i("Cloudinary", "onProgress: " + progress);
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        view.saveEnable(false);
                        if (isCover) {
                            coverPath = (String) resultData.get(Constants.Post.URL);
                            Log.d("cover profile", coverPath);
                            if (isBusiness)
                                profileBody.setBusinessProfileCoverImage(coverPath);
                            else
                                profileBody.setProfileCoverImage(coverPath);
                        } else {
                            imageUrl = (String) resultData.get(Constants.Post.URL);
                            Log.d("profile", imageUrl);
                            if (isBusiness)
                                profileBody.setBusinessProfilePic(imageUrl);
                            else
                                profileBody.setImgUrl(imageUrl);
                            view.setProfilePic(imageUrl);
                        }
                        updateProfileDataToServer(profileBody);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.d("profile", "cloudinary onError: ");
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                    }
                })
                .constrain(TimeWindow.immediate())
                .dispatch();
    }

    public void updateProfileDataToServer(EditProfileBody profileBody) {
        service.editProfile(AppController.getInstance().getApiToken(), "en", profileBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<EditProfileResponse>>() {
                    @Override
                    public void onNext(Response<EditProfileResponse> editProfileResponse) {
                        view.showProgress(false);
                        switch (editProfileResponse.code()) {
                            case 200:
                                view.showMessage(null, R.string.profileUpdated);
                                view.finishActivity(true, false);
                                break;
                            case 409:
                                view.showMessage("User name is already taken, please choose another username", 0);
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
                                                        updateProfileDataToServer(profileBody);
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
                        Log.e(TAG, "edit Profile Failed!!");
                        view.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        // view.finishActivity();
                    }
                });
    }

    private boolean invalidateProfileField(EditProfileBody profileBody) {

        if (profileBody.getFirstName() == null || profileBody.getFirstName().isEmpty()) {
            view.showMessage(null, R.string.enterFirstName);
            return false;
        }

        if (profileBody.getLastName() == null) {
            profileBody.setLastName("");
            //return true;
        }

        if (profileBody.getUserName() == null || profileBody.getUserName().isEmpty()) {
            view.showMessage(null, R.string.enterUserName);
            return false;
        }

        if (profileBody.getStatus() == null) {
            profileBody.setStatus("");
            //return true;
        }
        return true;
    }

    private boolean detectProfileChange(EditProfileBody profileBody, Data profileData) {
        if (picturePath != null) {
            return true;
        }
        if (!profileBody.getFirstName().equals(profileData.getFirstName())) return true;

        if (!profileBody.getLastName().equals(profileData.getLastName())) return true;

        if (!profileBody.getUserName().equals(profileData.getUserName())) return true;

        if (!profileBody.getStatus().equalsIgnoreCase(profileData.getStatus())) {
            return true;
        }

        return false;
    }

    @Override
    public void businessEmailVerificationCode(String email) {
        view.showProgress(true);
        Map<String, String> params = new HashMap<>();
        params.put("bussinessEmailId", email);
        service.businessEmailVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                view.verifyEmailAddress();
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
                                view.showMessage("Oops something went wrong!!!", -1);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showProgress(false);
                        view.showMessage("Oops something went wrong!!!", -1);
                    }

                    @Override
                    public void onComplete() {
                        view.showProgress(false);
                    }
                });
    }

    @Override
    public void businessPhoneVerificationCode(String countryCode, String phone) {
        view.showProgress(true);
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
                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                view.verifyMobile();
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
                                view.showMessage("Oops something went wrong!!!", -1);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showProgress(false);
                        view.showMessage("Oops something went wrong!!!", -1);
                    }

                    @Override
                    public void onComplete() {
                        view.showProgress(false);
                    }
                });
    }
}
