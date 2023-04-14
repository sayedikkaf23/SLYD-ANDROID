package chat.hola.com.app.request_star_profile.star_category;

/**
 * <h1>{@link chat.hola.com.app.request_star_profile.star_category.StarCatPresenter}</h1>
 * <p>PricePresenter class for star category.</p>
 *
 * @Author: Hardik Karkar
 * @Since: 23rd May 2019
 * {@link chat.hola.com.app.request_star_profile.star_category.StarCatContract.View}
 * {@link chat.hola.com.app.Networking.HowdooService}
 * {@link chat.hola.com.app.models.NetworkConnector}
 */

import android.os.Handler;
import android.util.Log;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class StarCatPresenter implements StarCatContract.Presenter {

    @Inject
    public StarCatPresenter() {
    }

    @Inject
    HowdooService service;
    @Inject
    NetworkConnector networkConnector;
    @Inject
    StarCatContract.View view;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Override
    public void attachView(StarCatContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void getStarCategory() {

        service.getStarCategory(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<StarCatResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<StarCatResponse> response) {

                        switch (response.code()) {
                            case 200:
                                if (response.body() != null) {
                                    if (view != null) {
                                        view.getCategorySuccuss(response.body().getData());
                                    }
                                }
                                break;
                            case 401:
                                if (view != null)
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
                                                        getStarCategory();
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
                        if (view != null) {
                            view.isInternetAvailable(networkConnector.isConnected());

                            view.showMessage(e.getMessage(), 0);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
