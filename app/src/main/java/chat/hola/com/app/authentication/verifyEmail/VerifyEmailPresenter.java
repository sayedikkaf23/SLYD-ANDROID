package chat.hola.com.app.authentication.verifyEmail;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class VerifyEmailPresenter implements VerifyEmailContract.Presenter {

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    VerifyEmailContract.View view;
    @Inject
    SessionManager sessionManager;

    @Inject
    VerifyEmailPresenter() {
    }

    @Override
    public void verify(String otp, String email) {
        view.progress(true);
        Map<String, String> params = new HashMap<>();
        params.put("otp", otp);
        params.put("email", email);
        service.verifyOtpForgotPws(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        switch (response.code()) {
                            case 200:
                                view.otpVerified();
                                break;
                            case 406:
                                break;
                            default:
                                view.message(chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()));
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        view.progress(false);
                    }
                });

    }

    @Override
    public void verifyBusinessEmail(String otp, String email, boolean isVisible) {
        view.progress(true);
        Map<String, Object> params = new HashMap<>();
        params.put("bussinessEmailId", email);
        params.put("otp", otp);
        params.put("isVisible", isVisible);
        service.businessEmailOtpVerify(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                view.businessEmailVerified();
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
                                                        verifyBusinessEmail(otp, email, isVisible);
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
                                view.message(chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()));
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        view.progress(false);
                    }
                });
    }

    @Override
    public void resend(String phone, String email) {
        view.progress(true);
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("email", email);
        service.forgotPassword(sessionManager.getGuestToken(),Constants.LANGUAGE,params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        switch (response.code()) {
                            case 200:
                                view.success();
                                break;
                            case 406:
                                break;
                            default:
                                view.message(chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()));
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        view.progress(false);
                    }
                });
    }

    @Override
    public void businessEmailVerificationCode(String email) {
        view.progress(true);
        Map<String, String> params = new HashMap<>();
        params.put("bussinessEmailId", email);
        service.businessEmailVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.progress(false);
                        switch (response.code()) {
                            case 200:
                                //view.message("Verification code sent successfully");
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
                                view.message("Oops something went wrong!!!");
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.progress(false);
                        view.message("Oops something went wrong!!!");
                    }

                    @Override
                    public void onComplete() {
                        view.progress(false);
                    }
                });
    }
}
