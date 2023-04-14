package chat.hola.com.app.authentication.signup;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.ezcall.android.BuildConfig;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Error;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.Utilities.aws.UploadFileAmazonS3;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.models.Register;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class SignUpPresenter implements SignUpContract.Presenter {

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    SignUpContract.View view;
    @Inject
    SignUpModel model;

    @Inject
    SignUpPresenter() {
    }

    @Override
    public void register(Context context, Register register, UploadFileAmazonS3 amazonS3, File mFileTemp) {
        view.enableSave(false);
        view.showLoader();
        service.signUp(Utilities.getThings(), register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Login>>() {
                    @Override
                    public void onNext(Response<Login> response) {

                        switch (response.code()) {
                            case 201:
                                model.setData(response.body().getResponse());
                                view.registered(response.body().getResponse());
                               /* if (mFileTemp != null) {
                                    amazonUpload(context, amazonS3, mFileTemp, register.getMobileNumber(), register.getCountryCode(), response.body().getResponse().getUserId(), register.getUserName(), response);
                                } else {
                                    model.setData(response.body().getResponse());
                                    view.registered(response.body().getResponse());
                                }*/
                                break;
                            case 409:
                            case 400:
                            case 422:
                                view.message(Error.getErrorMessage(response.errorBody()));
                                view.completed();
                                view.enableSave(true);
                                view.hideLoader();
                                break;
                            case 406:
                                view.hideLoader();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            view.hideLoader();
                            view.completed();
                            view.enableSave(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null)
                            view.hideLoader();
                        ;
                        view.completed();
                    }
                });
    }

    @Override
    public void amazonUpload(Context context, UploadFileAmazonS3 amazonS3, File mFileTemp, String phone, String countryCode, String userId, String userName, Response<Login> response) {
        final String imageUrl = ApiOnServer.BUCKET + "/" + ApiOnServer.PROFILE_PIC_FOLDER + "/" + userId;
        Log.d("amazon", "amzonUpload: " + imageUrl);
//        amazonS3.UploadDataWithName(ApiOnServer.BUCKET, ApiOnServer.PROFILE_PIC_FOLDER + "/" + userId, mFileTemp, new UploadFileAmazonS3.UploadCallBack() {
//            @Override
//            public void sucess(String success) {
//                view.setProfilePic(imageUrl);
//                model.setData(response.body().getResponse());
//                view.registered(response.body().getResponse());
//                //AppController.getInstance().setSignedIn(false, userId, userName, phone, 1);
//                //AppController.getInstance().setSignStatusChanged(false);
//                //view.registered();
//            }
//
//            @Override
//            public void error(String errormsg) {
//                view.hideLoader();
//                ;
//                view.message("Profile pic is not updated");
//                view.completed();
//                view.enableSave(true);
//            }
//        });

        amazonS3.Upload_data(context, ApiOnServer.PROFILE_PIC_FOLDER+ "/" + userId,
                mFileTemp, new UploadFileAmazonS3.UploadCallBack() {
                    @Override
                    public void sucess(String success) {
                        view.setProfilePic(imageUrl);
                        model.setData(response.body().getResponse());
                        view.registered(response.body().getResponse());
                        //AppController.getInstance().setSignedIn(false, userId, userName, phone, 1);
                        //AppController.getInstance().setSignStatusChanged(false);
                        //view.registered();
                    }

                    @Override
                    public void error(String errormsg) {
                        view.hideLoader();
                        ;
                        view.message("Profile pic is not updated");
                        view.completed();
                        view.enableSave(true);
                    }
                });
    }

    @Override
    public void validate(Register register) {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("userName", register.getUserName());
//        map.put("email", register.getEmail());
//        map.put("userReferralCode", register.getReferralCode());
//        map.put("number", register.getMobileNumber());
//        if(register.getMobileNumber().equalsIgnoreCase("")){
//            map.put("countryCode", "");
//
//        }else {
//            map.put("countryCode", register.getCountryCode());
//
//        }
        service.verifySignUp(Utilities.getThings(), map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                view.verifyPhone();
                                break;
                            case 406:
                                view.message("Invalid referral code");
                                break;
                            default:
                                try {
                                    assert response.errorBody() != null;
                                    view.message(Error.getErrorMessage(response.errorBody()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        view.enableSave(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            view.enableSave(true);
                            view.hideLoader();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void signUp(Context context, Register register, UploadFileAmazonS3 uploadFileAmazonS3, String profilePic) {
        register(context, register, uploadFileAmazonS3, profilePic != null ? new File(profilePic) : null);
    }

    @Override
    public void socialSignIn(String token, String language, FirebaseUser user, String loginType) {
        view.showLoader();
        Map<String, String> map = new HashMap<>();

        map.put("deviceId", AppController.getInstance().getDeviceId());
        map.put("deviceName", Build.DEVICE);
        map.put("deviceOs", Build.VERSION.RELEASE);
        map.put("modelNumber", Build.MODEL);
        map.put("deviceType", "2"); // for android = 2
        map.put("appVersion", BuildConfig.VERSION_NAME);

        if (loginType.equals(Constants.LoginType.GOOGLE))
            map.put("googleId", user.getUid());
        else if (loginType.equals(Constants.LoginType.FACEBOOK))
            map.put("facebookId", user.getUid());

        service.socialSignIn(Utilities.getThings(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Login>>() {
                    @Override
                    public void onNext(Response<Login> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                if (response.body() != null) {
                                    model.setData(response.body().getResponse());
                                    view.socialLoginSuccess(response.body().getResponse());
                                } else {
                                    view.message("Something went wrong");
                                }
                                break;
                            case 401:
                                //AppController.getInstance().guestSignIn(service, dataSource);
                                break;
                            case 204:
                                view.socialIdNotRegistered(user, loginType);
                                break;
                            case 403:
                                //view.blocked(Error.getErrorMessage(response.errorBody()));
                                break;
                            case 409:
                            case 412:
                            case 422:
                                view.message(Error.getErrorMessage(response.errorBody()));
                                break;

                            case 406:
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
    public void verifyIsEmailRegistered(String email) {
        service.verifyIsEmailRegistered(Utilities.getThings(), email, Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        switch (response.code()) {

                            case 200:
                                view.emailalreadyRegistered(email);
                                break;
                            case 204:
                                view.emailNotRegistered("Email number is not registered");
                                break;
                            case 406:
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
    public void verifyIsUserNameRegistered(String userName) {
        service.verifyIsUserNameRegistered(Utilities.getThings(), userName, Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        switch (response.code()) {

                            case 200:
                                view.userNamealreadyRegistered(userName);
                                break;
                            case 204:
                                view.userNameNotRegistered("UserName is already in use");
                                break;
                            case 406:
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
    public void verifyIsMobileRegistered(String phoneNumber, String countryCode) {
        service.verifyIsMobileRegistered(Utilities.getThings(), countryCode, phoneNumber, Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                view.numberAlreadyRegistered(phoneNumber);
                                break;
                            case 204:
                                view.numberNotRegistered("Mobile number is not registered");
                                break;
                            case 406:
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
