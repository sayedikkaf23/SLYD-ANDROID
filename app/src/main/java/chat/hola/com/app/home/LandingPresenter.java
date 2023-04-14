package chat.hola.com.app.home;

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
import android.util.Log;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.UnsafeOkHttpClient;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.ConnectivityReceiver;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.SocialShare;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.calling.model.PendingCallsResponse;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.contact.GetFriends;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.GuestTokenResponse;
import chat.hola.com.app.models.SessionObserver;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.profileScreen.model.Profile;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

/**
 * <h1>LandingPresenter</h1>
 *
 * <p></p>
 *
 * @author Shaktisinh
 * @version 1.0
 * @since 21/2/18.
 */
public class LandingPresenter
        implements LandingContract.Presenter, ConnectivityReceiver.ConnectivityReceiverListener {

    @Inject
    LandingContract.View view;
    @Inject
    SocialShare socialShare;
    @Inject
    Context context;
    @Inject
    HowdooService service;
    SessionApiCall sessionApiCall = new SessionApiCall();
    private String picturePath;
    private LandingActivity contactSyncLandingPage;
    private String TAG = LandingPresenter.class.getSimpleName();
    @Inject
    SessionManager sessionManager;

    @Inject
    public LandingPresenter(LandingActivity context) {
        contactSyncLandingPage = context;
    }

    @Override
    public void instagramShare(String type, String path) {
        if (socialShare.checkAppinstall(socialShare.INSTA_PACKAGE)) {
            socialShare.createInstagramIntent(type, null, path);
        } else {
            view.showMessage(null, R.string.installInstaMsg);
        }
    }

    @Override
    public LandingActivity getActivity() {
        return contactSyncLandingPage;
    }

    @Override
    public void parseMedia(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                if (uri == null) return;

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

                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //  picturePath = ImageFilePath.getPathAboveN(context, result.getUri());
                //} else {

                picturePath = ImageFilePath.getPath(context, result.getUri());
                //}

                if (picturePath != null) {
                    Bitmap bitmapToUpload = BitmapFactory.decodeFile(picturePath);
                    Bitmap bitmap = getCircleBitmap(bitmapToUpload);
                    if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                        view.launchPostActivity(new CameraOutputModel(0, picturePath));
                    } else {
                        picturePath = null;
                        view.showSnackMsg(R.string.string_19);
                    }
                } else {
                    picturePath = null;
                    view.showSnackMsg(R.string.string_19);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                picturePath = null;
                view.showSnackMsg(R.string.string_19);
            }
        } catch (OutOfMemoryError e) {
            picturePath = null;
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
    public void onNetworkConnectionChanged(boolean isConnected) {
        view.intenetStatusChanged(isConnected);
    }

    @Override
    public void friends() {
        if (AppController.getInstance().isGuest()) return;
        service.getFollowersFollowee(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GetFriends>>() {
                    @Override
                    public void onNext(Response<GetFriends> response) {

                        switch (response.code()) {
                            case 200:
                                //                                model.clearList();

                                assert response.body() != null;
                                if (response.body().getData() != null) {

                                    //                                    model.setFriendList(response.body().getData());
                                }

                                if (!AppController.getInstance().isFriendsFetched()) {
                                    AppController.getInstance().setFriendsFetched(true);
                                }

                                if (!AppController.getInstance().getChatSynced()) {

                                    try {
                                        JSONObject object = new JSONObject();

                                        object.put("eventName", "SyncChats");

                                        AppController.getBus().post(object);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                break;
                            case 204:

                                if (!AppController.getInstance().getChatSynced()) {

                                    if (!AppController.getInstance().isFriendsFetched()) {
                                        AppController.getInstance().setFriendsFetched(true);
                                    }

                                    try {
                                        JSONObject object = new JSONObject();

                                        object.put("eventName", "SyncChats");

                                        AppController.getBus().post(object);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
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
                                                        friends();
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
    public void getUserProfile() {
        if (AppController.getInstance().isGuest()) return;
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
                                if (response.body() != null && view != null) {
                                    Data data = response.body().getData().get(0);
                                    sessionManager.setIsStar(data.isStar());
                                    view.profilepic(data, data.getProfilePic(), data.isStar(), data.getUserName(), data.isActiveBussinessProfile(), data.isBusinessProfileApproved());
                                }
                                break;
                            case 401:
                                if (view != null) view.sessionExpired();
                                break;
                            case 403:
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
                                                        getUserProfile();
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
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void saveFriendsToDb(List<Friend> data) {

        try {

            ArrayList<Map<String, Object>> friends = new ArrayList<>();

            for (Friend f : data) {

                Map<String, Object> friend = new HashMap<>();

                friend.put("userId", f.getId());
                friend.put("userName", f.getUserName());
                friend.put("countryCode", f.getCountryCode());
                friend.put("number", f.getNumber());
                friend.put("profilePic", f.getProfilePic());
                friend.put("firstName", f.getFirstName());
                friend.put("lastName", f.getLastName());
                friend.put("socialStatus", f.getStatus());
                friend.put("isStar", f.isStar());
                friend.put("starRequest", f.getStarData());
                friend.put("private", f.getPrivate());
                friend.put("friendStatusCode", f.getFriendStatusCode());
                friend.put("followStatus", f.getFollowStatus());
                friend.put("timestamp", f.getTimestamp());
                friend.put("message", f.getMessage());
                friend.put("isChatEnable",f.isChatEnable());

                //            Log.d("log1","friends data-->"+f.isStar());

                friends.add(friend);

                friend = null;
            }

//            AppController.getInstance()
//                    .getDbController()
//                    .insertFriendsInfo(friends, AppController.getInstance().getFriendsDocId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void kycVerification() {
        if (AppController.getInstance().isGuest()) return;
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
                                view.gotoWalletDashboard(response.body().getVerificationStatus());
                                break;
                            case 404:
                                //not applied for kyc
                                view.gotoWalletDashboard(-1);
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

    @Override
    public void getWalletBalance() {
        if (AppController.getInstance().isGuest()) return;
        service.walletBalance(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                AppController.getInstance().getUserId(), Constants.APP_TYPE)
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
                                    } else if (wallet.getCurrency().equals(Constants.WALLET_COIN)) {
                                        view.showCoinBalance(wallet);
                                    }
                                }
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

    /*
    * Bug Title: we cannot cut the call once we minimize the application and maximize the application on call,
    * when the receiver minimizes the callers call, the video call is struck in the callers end to receive and cut call not working instantaneously,
    * pending call api
    * Fix Description: have implemented new pending call api when this activity launch
    * Developer Name: Hardik
    * Fix Date: 5/4/2021
    * */
    @Override
    public void getPendingCalls() {
        if (AppController.getInstance().isGuest())
            return;

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
                AppController.getInstance().getApplicationContext());
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiOnServer.CALLING_BASE)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(HowdooService.class)
                .getPendingCalls(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<PendingCallsResponse>>() {
                    @Override
                    public void onNext(@NotNull Response<PendingCallsResponse> response) {
                        if (response.code() == 200) {
                            if (response.body() != null && response.body().getData() != null) {
                                if (view != null) {
                                    view.showPendingCalls(response.body().getData());
                                }
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
    public void patchLatLong(String apiToken, double latitude, double longitude) {
        Map<String, Object> map = new HashMap<>();
        map.put("lat", latitude);
        map.put("long", longitude);
        service.patchLatLong(apiToken, Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        Log.d(TAG, "location update" + response.code());
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
    public void getGuestToken() {
        Map<String, Object> map = new HashMap<>();

        map.put("IpAddress", sessionManager.getIpAdress());
        map.put("deviceId", AppController.getInstance().getDeviceId());
        map.put("deviceName", Build.DEVICE);
        map.put("deviceOs", Build.VERSION.RELEASE);
        map.put("modelNumber", Build.MODEL);
        map.put("deviceType", "2");
        map.put("appVersion", BuildConfig.VERSION_NAME);
        map.put("countryName", sessionManager.getCountryName());

        service.getGuestToken(Utilities.getThings(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GuestTokenResponse>>() {
                    @Override
                    public void onNext(@NonNull Response<GuestTokenResponse> response) {
                        if (response.code() == 200) {
                            view.guestTokenFetched();
                            sessionManager.setGuestToken(response.body().getData().getAccessToken());
                            sessionManager.setTokenExpired(response.body().getData().getAccessExpireAt());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
