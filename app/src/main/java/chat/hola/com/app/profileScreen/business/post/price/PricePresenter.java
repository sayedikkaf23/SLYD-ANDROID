package chat.hola.com.app.profileScreen.business.post.price;


import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.CurrencyResponse;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>PricePresenter</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 09 September 2019
 */
public class PricePresenter implements PriceContract.Presenter {

    private PriceContract.View view;

    @Inject
    HowdooService service;
    private SessionApiCall sessionApiCall;

    @Inject
    public PricePresenter() {
        sessionApiCall = new SessionApiCall();
    }

    @Override
    public void attacheView(PriceContract.View view) {
        this.view = view;
    }

    @Override
    public void detachedView() {
        this.view = null;
    }

    @Override
    public void currency() {
        service.currency(AppController.getInstance().getApiToken(), Constants.LANGUAGE, 0, 60).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CurrencyResponse>>() {
                    @Override
                    public void onNext(Response<CurrencyResponse> response) {
                        switch (response.code()) {
                            case 200:
                                view.currency(response.body().getCurrency());
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
                                                        currency();
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
