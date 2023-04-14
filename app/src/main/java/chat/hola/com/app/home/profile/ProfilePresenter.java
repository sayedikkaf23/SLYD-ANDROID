package chat.hola.com.app.home.profile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;

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

    ProfileContract.View view;
    @Inject
    HowdooService service;
    @Inject
    NetworkConnector networkConnector;
    @Inject
    Context context;
    @Inject
    SessionManager sessionManager;
    @Inject
    PostObserver postObserver;
    SessionApiCall sessionApiCall = new SessionApiCall();
    private String picturePath;
    private Bitmap bitmapToUpload;

    @Inject
    public ProfilePresenter() {
    }

    private boolean observerAlreadyAdded = false;

    @Override
    public void init() {
        if (view != null) this.view.applyFont();

        if (!observerAlreadyAdded) {
            observerAlreadyAdded = true;
            postObserver.getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
//                            loadProfileData();
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
        if (view != null) view.isLoading(true);
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
                        if (view != null) view.isLoading(false);
                    }
                });
    }

    @Override
    public void loadMemberData(String userId) {
        if (view != null) view.isLoading(true);
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
                                if (view != null) view.isFollowing(true);
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
                        if (view != null) {
                            view.showMessage(e.getMessage(), 0);
                            view.isFollowing(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) {
                            view.isInternetAvailable(networkConnector.isConnected());
                        }
                    }
                });
    }

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
                                if (view != null) view.isFollowing(false);
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
                        if (view != null) {
                            view.isInternetAvailable(networkConnector.isConnected());
                            //   view.showMessage(e.getMessage(),0);
                            view.isFollowing(true);
                        }
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
        if (view != null) view.launchImagePicker(intent);
    }

    @Override
    public void parseMedia(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                //TODO: it will prevent further crash ( getting uri as null on android 7.0 Mi phone).
                if (uri == null) return;

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
                            if (view != null)
                                view.launchPostActivity(new CameraOutputModel(1, picturePath));
                        } else {
                            if (view != null) view.showSnackMsg(R.string.video_size_message);
                        }
                    } else {
                        //image
                        parseSelectedImage(uri, picturePath);
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
    public void parseSelectedImage(Uri uri, String picturePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);

        if (options.outWidth > 0 && options.outHeight > 0) {

            //launch crop image
            if (view != null) view.launchCropImage(uri);
        } else {
            //image can't be selected try another
            if (view != null) view.showSnackMsg(R.string.string_31);
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
                        if (view != null)
                            view.launchPostActivity(new CameraOutputModel(0, picturePath));
                        //userAlreadyHasImage = false;
                        //userImageUrl = null;
                    } else {
                        //sorry failed to capture
                        picturePath = null;
                        if (view != null) view.showSnackMsg(R.string.string_19);
                    }
                } else {
                    //sorry failed to capture

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

    @Override
    public void getWalletBalance() {
        service.walletBalance(AppController.getInstance().getApiToken(), Constants.LANGUAGE, sessionManager.getUserId(), Constants.APP_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WalletResponse>>() {
                    @Override
                    public void onNext(Response<WalletResponse> response) {
                        switch (response.code()) {
                            case 200:
                                if (response.body() != null && response.body().getData() != null) {
                                    if (view != null && response.body().getData() != null && !response.body().getData().getWalletData().isEmpty()) {
                                        for (WalletResponse.Data.Wallet wallet : response.body().getData().getWalletData()) {
                                            if (wallet.getCurrency().equals(sessionManager.getCurrency())) {
                                                view.showBalance(wallet);
                                            }
                                            if (wallet.getCurrency().equals(Constants.WALLET_COIN)) {
                                                sessionManager.setCoinBalance(wallet.getBalance());
                                                view.showCoinBalance(wallet);
                                            }
                                        }
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
                                                        getWalletBalance();
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
                                if (view != null) view.addToReportList(response.body().getData());
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
                                if (view != null) view.showMessage(null, R.string.reported_profile);
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
                                if (view != null) view.addToBlockList(response.body().getData());
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
                                if (view != null) view.block(block.equals("block"));
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
                                if (view != null) view.unfriend();
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
    public void attachView(ProfileContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    public void kycVerification() {
        view.isLoading(true);
        service.kycVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        view.isLoading(false);
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
                        view.isLoading(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void logout(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Are you sure want to Log out? ");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        logoutAccount();
                    }
                });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    private void logoutAccount() {
        view.showLoader();
        service.logout(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                view.logout();
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
                                                        logoutAccount();
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

}
