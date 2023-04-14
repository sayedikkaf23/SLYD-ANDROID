package chat.hola.com.app.collections.saved;

import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.collections.model.CollectionResponse;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SavedPresenter implements SavedContract.Presenter {

    @Inject
    public SavedPresenter() {
    }

    @Inject
    SavedContract.View view;
    @Inject
    HowdooService service;
    @Inject
    SessionApiCall sessionApiCall;

    @Override
    public void attachView(SavedContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void getCollections(int offSet, int limit) {
        view.showProgress(true);
        service.getCollections(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE,
                offSet, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CollectionResponse>>() {
                    @Override
                    public void onNext(Response<CollectionResponse> response) {
                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                view.onSuccess(response.body().getData());
                                break;
                            case 204:
                                view.showEmpty();
                                break;
                            case 401:
                                view.sessionExpired();
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
                                                        getCollections(offSet, limit);
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
