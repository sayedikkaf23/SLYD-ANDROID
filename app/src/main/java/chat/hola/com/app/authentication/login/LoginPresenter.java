package chat.hola.com.app.authentication.login;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.ezcall.android.BuildConfig;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Error;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Login;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.kotlintestgradle.remote.RemoteConstants.PLATFORM;

public class LoginPresenter implements LoginContract.Presenter {

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    LoginModel model;
    @Inject
    LoginContract.View view;

    @Inject
    LoginPresenter() {
    }

    @Override
    public void loginWithEmailId(String userName, String password, String appVersion, String country) {
        view.showLoader();
        Map<String, String> map = new HashMap<>();
        map.put("emailId", userName);
        map.put("password", password);
        map.put("deviceName", Build.DEVICE);
        map.put("deviceOs", Build.VERSION.RELEASE);
        map.put("modelNumber", Build.MODEL);
        map.put("deviceType", "2");
        map.put("deviceId", AppController.getInstance().getDeviceId());
        map.put("appVersion", appVersion);
        map.put("countryName", country);

        service.loginByUserName(Utilities.getThings(), map)
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
                                    view.loginSuccessfully();
                                } else {
                                    view.message("Something went wrong");
                                }
                                break;
                            case 404:
                                view.numberNotRegistered(Error.getErrorMessage(response.errorBody()));
                                break;
                            case 403:
                                view.blocked(Error.getErrorMessage(response.errorBody()));
                                break;
                            case 409:
                            case 412:
                            case 422:
                                view.message(Error.getErrorMessage(response.errorBody()));
                                break;
                            case 406:
                                break;
                            default:
                                view.message(Error.getErrorMessage(response.errorBody()));
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
    public void loginByPassword(String countryCode, String phoneNumber, String password, String appVersion, String country) {
        view.showLoader();
        Map<String, String> map = new HashMap<>();
        map.put("countryCode", countryCode);
        map.put("number", phoneNumber);
        map.put("password", password);
        map.put("deviceName", Build.DEVICE);
        map.put("deviceOs", Build.VERSION.RELEASE);
        map.put("modelNumber", Build.MODEL);
        map.put("deviceType", "2");
        map.put("deviceId", AppController.getInstance().getDeviceId());
        map.put("appVersion", appVersion);
        map.put("countryName", country);

        service.loginByPassword(Utilities.getThings(), map)
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
                                    view.loginSuccessfully();
                                } else {
                                    view.message("Something went wrong");
                                }
                                break;
                            case 404:
                                view.numberNotRegistered(Error.getErrorMessage(response.errorBody()));
                                break;
                            case 403:
                                view.blocked(Error.getErrorMessage(response.errorBody()));
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
    public void loginByOtp(Login login) {
        view.showLoader();
        service.loginByOtp(Constants.AUTH, login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Login>>() {
                    @Override
                    public void onNext(Response<Login> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                assert response.body() != null;
                                model.setData(response.body().getResponse());
                                view.loginSuccessfully();
                                break;
                            case 404:
                                view.numberNotRegistered(Error.getErrorMessage(response.errorBody()));
                                break;
                            case 403:
                                view.blocked(Error.getErrorMessage(response.errorBody()));
                                break;
                            case 409:
                            case 412:
                            case 422:
                                view.message(Error.getErrorMessage(response.errorBody()));
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
    public void requestOtp(String phoneNumber, String countryCode) {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("phoneNumber", phoneNumber);
        map.put("countryCode", countryCode);
        map.put("deviceId", AppController.getInstance().getDeviceId());
        map.put("development", BuildConfig.DEBUG);
        map.put("hashKey", Utilities.getHashCode(AppController.getInstance()));
        map.put("type", 1);
        service.requestOtp(Constants.AUTH, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        if (response.code() == 200) {
                            view.verifyOtp(phoneNumber, countryCode);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoader();
                    }
                });
    }

    @Override
    public void verifyIsMobileRegistered(String phone, String countryCode, boolean isForgotPassword) {
        view.showLoader();
        service.verifyIsMobileRegistered(Utilities.getThings(), countryCode, phone, Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                if (isForgotPassword)
                                    view.forgotPassword();
                                else
                                    requestOtp(phone, countryCode);
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
                        view.hideLoader();
                    }
                });
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

        if(loginType.equals(Constants.LoginType.GOOGLE))
            map.put("googleId", user.getUid());
        else if(loginType.equals(Constants.LoginType.FACEBOOK))
            map.put("facebookId", user.getUid());

        service.socialSignIn(token, Constants.LANGUAGE,map)
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
                                //AppController.getInstance().guestSignIn(service,dataSource);
                                break;
                            case 204:
                                view.socialIdNotRegistered(user,loginType);
                                break;
                            case 403:
                                view.blocked(Error.getErrorMessage(response.errorBody()));
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
    public void verifyIsEmailRegistered(String email, boolean isForgotPassword) {
        view.showLoader();
        service.verifyIsEmailRegistered(Utilities.getThings(), email, Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                if (isForgotPassword)
                                    view.forgotPassword();
                                else
                                    Log.i("TEST"," not registred");
                                view.enterEmailPswd(email);
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
}
