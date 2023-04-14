package chat.hola.com.app.authentication.forgotpassword;


import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    ForgotPasswordContract.View view;
    @Inject
    SessionManager sessionManager;

    @Inject
    ForgotPasswordPresenter() {
    }

    @Override
    public void verifyIsEmailRegistered(String email) {
        view.progress(true);
        service.verifyIsEmailRegistered(Utilities.getThings(), email, Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.progress(false);
                        switch (response.code()) {
                            case 200:
                                view.emailRegisterd(email);
                                break;
                            case 204:
                                view.emailNotRegisterd();
                                //view.message("Enter email address not register with us.");
                                break;
                            case 406:
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.progress(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void forgotPassword(String email) {
        view.progress(true);
        Map<String, Object> params = new HashMap<>();
        params.put("verifyType", 1); // 1 email, 2 mobile
        params.put("email", email);
//        params.put("mobile", "");
//        params.put("countryCode", "");
        service.forgotPassword(sessionManager.getGuestToken(),Constants.LANGUAGE, params)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        switch (response.code()) {
                            case 200:
                                view.success();
                                break;
                            case 204:
                                view.message("Your email address is not matching, please check it");
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
    public void requestOtp(String email) {
        view.progress(true);
        Map<String, Object> params = new HashMap<>();
        params.put("starUserEmailId", email);
        params.put("type", 2);
        service.requestOtpForEmail(sessionManager.getGuestToken(), params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                view.success();
                                break;
                            case 204:
                                view.message("Your email address is not matching, please check it");
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
}
