package chat.hola.com.app.user_verification.phone;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

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
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>VerifyNumberPresenter</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 22 August 2019
 */
public class VerifyNumberPresenter implements VerifyNumberContract.Presenter {
    @Inject
    Context context;
    @Inject
    HowdooService service;
    @Inject
    VerifyNumberContract.View view;
    @Inject
    VerifyNumberModel model;

    private SessionApiCall sessionApiCall;
    @Inject
    SessionManager sessionManager;

    @Inject
    VerifyNumberPresenter() {
        sessionApiCall = new SessionApiCall();
    }

    @Override
    public void verifyPhoneNumber(Login login, final int move, Register register,
                                  UploadFileAmazonS3 uploadFileAmazonS3, File mFile) {
        Log.i("1", "verifyPhoneNumber: ");
        view.showProgress(true);
        service.loginByOtp(sessionManager.getGuestToken(), login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Login>>() {
                    @Override
                    public void onNext(Response<Login> response) {
                        view.showProgress(false);
                        Log.i("2", "verifyPhoneNumber: ");
                        switch (response.code()) {
                            case 200:
                                Log.i("3", "verifyPhoneNumber: ");
                                if (response.body() != null && response.body().getCode() == 138) {
                                    view.showMessage(response.body().getMessage());
                                } else {
                                    switch (move) {
                                        case 1:
                                            signUp(register, uploadFileAmazonS3, mFile);
                                            break;
                                        case 2:
                                            Log.i("4", "verifyPhoneNumber: ");
                                            if (model.setData(response.body().getResponse())) {
                                                view.registered(response.body().getResponse(),false);
                                            }
                                            break;
                                        case 3:
                                            view.newPassword();
                                            break;
                                        default:
                                            view.openSuccessDialog();
                                            break;
                                    }
                                }
                                break;
                            case 204:
                            case 404:
                                view.numberNotRegistered("Number not registered");
                                break;
                            case 403:
                            case 409:
                            case 412:
                            case 422:
                                view.showMessage(Error.getErrorMessage(response.errorBody()));
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
                                                        verifyPhoneNumber(login, move, register, uploadFileAmazonS3, mFile);
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
                        Log.i("7", "onError: " + e.getMessage());
                        e.printStackTrace();
                        view.showProgress(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void verifyBusinessPhoneNumber(String otp, String countryCode, String phoneNumber) {
        Map<String, Object> params = new HashMap<>();
        params.put("businessPhone", phoneNumber);
        params.put("countryCode", countryCode);
        params.put("otp", otp);
        params.put("isVisible", true);
        service.businessPhoneOtpVerify(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                view.businessPhoneVerified();
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
                                                        verifyBusinessPhoneNumber(otp, countryCode, phoneNumber);
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
                                view.showMessage(
                                        chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()));
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
    public void makeReqToNumber(Map<String, Object> params) {
        view.showProgress(true);
        service.requestOtp(sessionManager.getGuestToken(), params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.showNext();
                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                view.startTimer();
                                //view.showMessage("Verification code sent");
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
                                                        makeReqToNumber(params);
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
                        view.showProgress(false);
                        view.showNext();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void signUp(Register register, UploadFileAmazonS3 amazonS3, File mFileTemp) {
        view.showProgress(true);
        service.signUp(Utilities.getThings(), register)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<retrofit2.Response<Login>>() {
                    @Override
                    public void onNext(Response<Login> response) {
                        view.showProgress(false);
                        switch (response.code()) {
                            case 201:
                                if (mFileTemp != null) {
                                    amazonUpload(amazonS3, mFileTemp, response.body().getResponse().getUserId(),
                                            response.body());
                                } else {
                                    if (model.setData(response.body().getResponse())) {
                                        view.registered(response.body().getResponse(), true);
                                    }
                                }
                                break;
                            case 409:
                            case 400:
                            case 422:
                                view.showMessage(Error.getErrorMessage(response.errorBody()));
                                break;
                            case 406:
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            view.showProgress(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) view.showProgress(false);
                    }
                });
    }

    @Override
    public void amazonUpload(UploadFileAmazonS3 amazonS3, File mFileTemp, String userId,
                             Login response) {
        final String imageUrl = ApiOnServer.BASE_URL
                + "/"
                + ApiOnServer.BUCKET
                + "/"
                + ApiOnServer.PROFILE_PIC_FOLDER
                + "/"
                + userId;
        Log.d("amazon", "amzonUpload: " + imageUrl);

        amazonS3.Upload_data(context, ApiOnServer.PROFILE_PIC_FOLDER + "/"
                        + userId,
                mFileTemp,
                new UploadFileAmazonS3.UploadCallBack() {
                    @Override
                    public void sucess(String success) {
                        if (model.setData(response.getResponse())) {
                            model.sessionManager.setUserProfilePic(imageUrl, true);
                            view.showProgress(false);
                            view.registered(response.getResponse(), false);
                        }
                    }

                    @Override
                    public void error(String errormsg) {
                        view.showProgress(false);
                    }
                });
    }
}
