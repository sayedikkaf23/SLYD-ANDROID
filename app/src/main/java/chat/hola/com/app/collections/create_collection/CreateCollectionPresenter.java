package chat.hola.com.app.collections.create_collection;

import android.os.Handler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.collections.model.CreateCollectionResponse;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class CreateCollectionPresenter implements CreateCollectionContract.Presenter {

    @Inject
    public CreateCollectionPresenter() {
    }

    @Inject
    CreateCollectionContract.View view;
    @Inject
    HowdooService service;
    @Inject
    SessionApiCall sessionApiCall;

    @Override
    public void attachView(CreateCollectionContract.View view) {

    }

    @Override
    public void detachView() {
    }

    @Override
    public void createCollection(String collectionName) {
        view.showProgress(true);
        Map<String, Object> map = new HashMap<>();
        map.put("collectionName", collectionName);
        service.createCollection(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CreateCollectionResponse>>() {
                    @Override
                    public void onNext(Response<CreateCollectionResponse> response) {
                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                view.createdSuccess(response.body());
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
                                                        createCollection(collectionName);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                view.showProgress(false);
                                            }

                                            @Override
                                            public void onComplete() {
                                                view.showProgress(false);
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        view.showProgress(false);
                    }
                });
    }
}
