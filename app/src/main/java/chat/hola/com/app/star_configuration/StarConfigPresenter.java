package chat.hola.com.app.star_configuration;

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

public class StarConfigPresenter implements StarConfigContract.Presenter {

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    public StarConfigPresenter() {
    }

    @Inject
    StarConfigContract.View view;
    @Inject
    HowdooService service;

    @Override
    public void attachView(StarConfigContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void makeChatVisibility(boolean isChecked) {

        Map<String, Object> map = new HashMap<>();
        map.put("isChatVisible", isChecked);

        service.makeChatVisibity(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
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
                                                        makeChatVisibility(isChecked);
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
}
