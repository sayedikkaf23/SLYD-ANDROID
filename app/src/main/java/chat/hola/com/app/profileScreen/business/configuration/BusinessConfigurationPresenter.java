package chat.hola.com.app.profileScreen.business.configuration;


import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>BusinessConfigurationPresenter</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 03 September 2019
 */
public class BusinessConfigurationPresenter implements BusinessConfigurationContract.Presenter {

    @Inject
    HowdooService service;
    @Inject
    BusinessConfigurationContract.View view;
    private SessionApiCall sessionApiCall;

    @Inject
    public BusinessConfigurationPresenter() {
        sessionApiCall = new SessionApiCall();
    }

    @Override
    public void configureBusiness(boolean enableEmail, boolean enableCall, String businessCategoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("isPhoneNumberVisible", enableCall ? 1 : 0);
        params.put("isEmailVisible", enableEmail ? 1 : 0);
        params.put("businessCategoryId", businessCategoryId);
        service.businessConfiguration(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                view.showMessage("Configuration saved");
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
                                                        configureBusiness(enableEmail, enableCall, businessCategoryId);
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
                                view.showMessage(chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()));
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
