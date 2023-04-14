package chat.hola.com.app.authentication.newpassword;

import android.os.Handler;

import com.ezcall.android.R;

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

public class NewPasswordPresenter implements NewPasswordContract.Presenter {

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    NewPasswordContract.View view;
    @Inject
    SessionManager sessionManager;

    @Inject
    NewPasswordPresenter() {
    }

    @Override
    public void changePassword(Map<String, String> params) {
        service.resetPassword(sessionManager.getGuestToken(), params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
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
                                assert response.errorBody() != null;
                                view.message(chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()));
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.message("Unable to reset password");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

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
                                view.passwordNotMatched();
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
    public void updatePassword(String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("newPassword", password);
        service.changePassword(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        switch (response.code()) {
                            case 200:
                                view.success();
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
                                                        updatePassword(password);
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
                        view.message("Unable to reset password");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
