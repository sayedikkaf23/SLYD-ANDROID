package chat.hola.com.app.profileScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.util.Pair;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.post.ReportReason;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.wallet.wallet_detail.model.WalletBalanceMain;

import com.ezcall.android.R;

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
 * @author Shaktisinh Jadeja
 * @since 23/5/2019
 */

public class ProfilePresenter implements ProfileContract.Presenter {

    @Inject
    ProfileContract.View view;
    @Inject
    HowdooService service;
    @Inject
    NetworkConnector networkConnector;
    @Inject
    Context context;
    @Inject
    PostObserver postObserver;
    @Inject
    SessionManager sessionManager;
    SessionApiCall sessionApiCall = new SessionApiCall();
    private String picturePath;
    private Bitmap bitmapToUpload;
    private boolean observerAlreadyAdded = false;

    @Inject
    public ProfilePresenter() {
    }

    @Override
    public void init() {

        if (view != null) view.applyFont();
        if (!observerAlreadyAdded) {
            observerAlreadyAdded = true;
            postObserver.getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
//              loadProfileData();
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

    @Override
    public void loadProfileData() {

        view.isLoading(true);
        service.getUserProfile(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Profile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Profile> response) {

                        switch (response.code()) {
                            case 200:
                                if (response.body() != null && view != null)
                                    view.showProfileData(response.body());
                                break;
                            case 401:
                                if (view != null) view.sessionExpired();
                                break;
                            case 403:
                                if (view != null) view.noProfile(response.body().getMessage());
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
                        if (view != null) {
                            view.isInternetAvailable(networkConnector.isConnected());
                            view.isLoading(false);
                            view.showMessage(e.getMessage(), 0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        view.isLoading(false);
                    }
                });
    }

    @Override
    public void loadMemberData(String userId) {

        view.isLoading(true);
        service.getMember(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Profile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<Profile> response) {

                        switch (response.code()) {
                            case 200:
                                if (response.body() != null) {
                                    if (view != null) view.showProfileData(response.body());
                                }
                                break;
                            case 204:
                                if (view != null) view.showMessage(context.getString(R.string.user_does_not_exits), 204);
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
                                                        loadMemberData(userId);
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
                            view.isLoading(false);
                            view.showMessage(e.getMessage(), 0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) view.isLoading(false);
                    }
                });
    }

    /*
     * Bug Title: the user is shown as following in the home page even after we unfollow the user in the
     * Bug Id: DUBAND012
     * Fix Description: setup new observer to listen user action
     * Developer Name: Hardik
     * Fix Date: 6/4/2021
     * */
    @Override
    public void follow(String followingId) {

        // Map<String, String> map = new HashMap<>();
        //map.put("followerId", AppController.getInstance().getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", followingId);
        service.follow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<FollowResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
                            case 200:
                            case 201:
                                view.isFollowing(true);
                                postObserver.postFollowObservableEmitter(new Pair<>(true, followingId));
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
                                                        follow(followingId);
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
                        view.showMessage(e.getMessage(), 0);
                        view.isFollowing(false);
                    }

                    @Override
                    public void onComplete() {
                        view.isInternetAvailable(networkConnector.isConnected());
                    }
                });
    }

    /*
     * Bug Title: the user is shown as following in the home page even after we unfollow the user in the
     * Bug Id: DUBAND012
     * Fix Description: setup new observer to listen user action
     * Developer Name: Hardik
     * Fix Date: 6/4/2021
     * */
    @Override
    public void unfollow(String followingId) {
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", followingId);
        service.unfollow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<FollowResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
                            case 200:
                                view.isFollowing(false);
                                postObserver.postFollowObservableEmitter(new Pair<>(false, followingId));
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
                                                        unfollow(followingId);
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
                        view.isInternetAvailable(networkConnector.isConnected());
                        //   view.showMessage(e.getMessage(),0);
                        view.isFollowing(true);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void launchCustomCamera() {
        if (view != null) view.launchCustomCamera();
    }

    @Override
    public void launchGallery() {
        if (view != null) view.checkReadImage();
    }

    @Override
    public void launchImagePicker() {
        Intent intent = null;
        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("*/*");

        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
        view.launchImagePicker(intent);
    }

    @Override
    public void parseMedia(int requestCode, int resultCode, Intent data) {
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
                    String extension = picturePath.substring(picturePath.lastIndexOf(".") + 1);
                    if (extension.equalsIgnoreCase("mp4")) {
                        //video
                        File file = new File(picturePath);
                        if (file.length() <= (Constants.Camera.FILE_SIZE * 1024 * 1024)) {
                            view.launchPostActivity(new CameraOutputModel(1, picturePath));
                        } else {
                            view.showSnackMsg(R.string.video_size_message);
                        }
                    } else {
                        //image
                        parseSelectedImage(uri, picturePath);
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
    public void parseSelectedImage(Uri uri, String picturePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);

        if (options.outWidth > 0 && options.outHeight > 0) {

            //launch crop image
            view.launchCropImage(uri);
        } else {
            //image can't be selected try another
            view.showSnackMsg(R.string.string_31);
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
                    bitmapToUpload = BitmapFactory.decodeFile(picturePath);
                    Bitmap bitmap = getCircleBitmap(bitmapToUpload);
                    if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                        //TODO:got the picture need to launch postActivity.
                        //view.setProfileImage(bitmap);
                        view.launchPostActivity(new CameraOutputModel(0, picturePath));
                        //userAlreadyHasImage = false;
                        //userImageUrl = null;

                    } else {
                        //sorry failed to capture
                        picturePath = null;
                        view.showSnackMsg(R.string.string_19);
                    }
                } else {
                    //sorry failed to capture
                    picturePath = null;
                    view.showSnackMsg(R.string.string_19);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                //sorry failed to capture
                picturePath = null;
                view.showSnackMsg(R.string.string_19);
            }
        } catch (OutOfMemoryError e) {
            //out of mem try again
            picturePath = null;
            view.showSnackMsg(R.string.string_15);
        }
    }

    @Override
    public void getWalletBalance() {
        service.walletBalance(AppController.getInstance().getApiToken(), Constants.LANGUAGE, AppController.getInstance().getUserId(), Constants.APP_TYPE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WalletResponse>>() {
                    @Override
                    public void onNext(Response<WalletResponse> response) {
                        if (response.code() == 200) {
                            try {
                                assert response.body() != null;
                                for (WalletResponse.Data.Wallet wallet : response.body().getData().getWalletData()) {
                                    if (wallet.getCurrency().equals(sessionManager.getCurrency())) {
                                        view.showBalance(wallet);
                                    }
                                    if (wallet.getCurrency().equals(Constants.WALLET_COIN)) {
                                        sessionManager.setCoinBalance(wallet.getBalance());
                                        view.showCoinBalance(wallet);
                                    }
                                }
                                view.showBalance(response.body().getData().getWalletData().get(0));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
    public void kycVerification() {
        view.showLoader();
        service.kycVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                //applied for kyc
                                view.moveNext(response.body().getVerificationStatus());
                                break;
                            case 404:
                                //not applied for kyc
                                view.moveNext(-1);
                                break;
                            case 406:
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
                                                        kycVerification();
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
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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

    public void getReportReasons() {
        service.userReportReasons(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ReportReason>>() {
                    @Override
                    public void onNext(Response<ReportReason> response) {
                        switch (response.code()) {
                            case 200:
                                view.addToReportList(response.body().getData());
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
                                                        getReportReasons();
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
                    }
                });
    }

    public void reportUser(String userId, String reason, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("targetUserId", userId);
        map.put("reason", reason);
        map.put("message", message);
        service.reportUser(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                view.showMessage(null, R.string.reported_profile);
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
                                                        reportUser(userId, reason, message);
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
                    }
                });
    }

    public void getBlockReasons() {
        service.getBlockReason(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ReportReason>>() {
                    @Override
                    public void onNext(Response<ReportReason> response) {
                        switch (response.code()) {
                            case 200:
                                view.addToBlockList(response.body().getData());
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
                                                        getBlockReasons();
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
                    }
                });
    }

    public void block(String userId, String block, String reason) {
        Map<String, String> map = new HashMap<>();
        map.put("reason", reason);
        map.put("targetId", userId);
        map.put("type", block);

        service.block(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                view.block(block.equals("block"));
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
                                                        block(userId, block, reason);
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
                    }
                });
    }

    public void unfriend(String userId) {
        service.unfriend(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                view.unfriend();
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
                                                        unfriend(userId);
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
                    }
                });
    }

    @Override
    public void subscribeStarUser(boolean isChecked, String id) {
        if (isChecked) {
            Map<String, Object> map = new HashMap<>();
            map.put("userIdToFollow", id);
            service.subscribeStarUSer(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                        @Override
                        public void onNext(Response<ResponseBody> response) {
                            switch (response.code()) {
                                case 200:
                                    if (view != null) view.onSuccessSubscribe(isChecked);
                                    postObserver.postPaidPostsObservableEmitter(new Pair("isFor","1"));
                                    break;
                                case 403:
                                    /*INSUFFICIENT BALANCE*/
                                    if (view != null) view.insufficientBalance();
                                    break;
                                case 405:
                                    if (view != null)
                                        view.showMessage(chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()), 0);
                                    break;
                                default:
                                    if (view != null) view.onSuccessSubscribe(false);
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
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("beneficiaryId", id);
            map.put("reason", "");
            service.unsubscribeStarUSer(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                        @Override
                        public void onNext(Response<ResponseBody> response) {
                            switch (response.code()) {
                                case 200:
                                    if (view != null) view.onSuccessSubscribe(isChecked);
                                    break;
                                case 403:
                                    /*INSUFFICIENT BALANCE*/
                                    if (view != null) view.insufficientBalance();
                                    break;
                                default:
                                    if (view != null) view.onSuccessSubscribe(false);
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
    }
}
