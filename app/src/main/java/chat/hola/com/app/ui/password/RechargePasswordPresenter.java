package chat.hola.com.app.ui.password;

import android.os.Build;
import android.os.Handler;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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
import chat.hola.com.app.models.Login;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.TransferResponse;
import chat.hola.com.app.models.WithdrawSuccess;
import chat.hola.com.app.ui.recharge.RechargeActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>RechargePasswordPresenter</h1>
 *
 * <p>This is implemented presenter of {@link RechargeActivity},
 * it will call apis and handles apis responses and
 * communicate between models and {@link  RechargeActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public class RechargePasswordPresenter implements RechargePasswordContract.Presenter {
    private SessionApiCall sessionApiCall = new SessionApiCall();
    private RechargePasswordContract.View view;

    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;

    @Inject
    public RechargePasswordPresenter() {

    }

    @Override
    public void attach(RechargePasswordContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void checkPassword(String password) {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("password", password);
        service.isPasswordMatch(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                view.passwordMatched();
                                break;
                            case 204:
                                view.message(R.string.invalid_password);
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
                                                        checkPassword(password);
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
                                view.message(response.message());
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
    public void transferTo(String receiverUid, String receiverUserName, String reason, String amount) {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("fromUserName", sessionManager.getUserName());
        map.put("fromUserId", sessionManager.getUserId());
        map.put("fromUserType", Constants.APP_TYPE);
        map.put("fromCurrency", sessionManager.getCurrency());
        map.put("toUserName", receiverUserName);
        map.put("toUserId", receiverUid);
        map.put("toUserType", Constants.APP_TYPE);
        map.put("toCurrency", sessionManager.getCurrency());
        map.put("amount", amount);
        map.put("description", reason);
        map.put("notes", reason);
        map.put("senderImage", "https://s3.ap-south-1.amazonaws.com/dubly/profile_image/" + sessionManager.getUserId());
        map.put("receiverImage", "https://s3.ap-south-1.amazonaws.com/dubly/profile_image/" + receiverUid);
        service.transferTo(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<TransferResponse>>() {
                    @Override
                    public void onNext(Response<TransferResponse> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                view.transferred(response.body().getData());
                                break;
                            case 412:
                                view.message("Insufficient Balance");
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
                                                        transferTo(receiverUid, receiverUserName, reason, amount);
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
                                            view.message(error.getMessage());
                                    }
                                } catch (IOException ignored) {
                                }
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
    public void withdraw(Map<String,Object> map) {
        service.withdraw(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WithdrawSuccess>>() {
                    @Override
                    public void onNext(Response<WithdrawSuccess> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                view.withdrawSuccess(response.body().getData());
                                break;
//                            case 412:
//                            case 404:
//                                view.message("Your transaction was not successful. Reason: Insufficient Funds");
//                                break;
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
                                                        withdraw(map);
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
                                            view.message(error.getMessage());
                                    }
                                } catch (IOException ignored) {
                                }
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
    public void requestOtp() {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("phoneNumber", sessionManager.getMobileNumber().replace(sessionManager.getCountryCode(), ""));
        map.put("countryCode", sessionManager.getCountryCode());
        map.put("deviceId", AppController.getInstance().getDeviceId());
        map.put("development", BuildConfig.DEBUG);
        map.put("hashKey", Utilities.getHashCode(AppController.getInstance()));
        map.put("type", 3);
        service.requestOtp(Constants.AUTH, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        if (response.code() == 200) {
                            view.startTimer();
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
    public void verifyOtp(String verificationCode) {
        view.showLoader();
        Login login = new Login();
        login.setPhoneNumber(sessionManager.getMobileNumber().replace(sessionManager.getCountryCode(), ""));
        login.setCountryCode(sessionManager.getCountryCode());
        login.setType(3);
        login.setOtp(verificationCode);
        login.setDeviceId(AppController.getInstance().getDeviceId());
        login.setDeviceName(Build.DEVICE);
        login.setDeviceOs(Build.VERSION.RELEASE);
        login.setModelNumber(Build.MODEL);
        login.setAppVersion("123");
        login.setDeviceType("2");
        service.loginByOtp(Constants.AUTH, login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Login>>() {
                    @Override
                    public void onNext(Response<Login> loginResponse) {
                        view.hideLoader();
                        if (loginResponse.code() == 200) {
                            view.passwordMatched();
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
