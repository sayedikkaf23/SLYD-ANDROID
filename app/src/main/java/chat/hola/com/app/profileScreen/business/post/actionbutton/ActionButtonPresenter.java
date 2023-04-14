package chat.hola.com.app.profileScreen.business.post.actionbutton;


import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.ActionButtonResponse;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>ActionButtonPresenter</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 09 September 2019
 */
public class ActionButtonPresenter implements ActionButtonContract.Presenter {

    private ActionButtonContract.View view;

    @Inject
    HowdooService service;
    @Inject
    ActionButtonModel model;
    private SessionApiCall sessionApiCall;

    @Inject
    public ActionButtonPresenter() {
        sessionApiCall = new SessionApiCall();
    }

    @Override
    public void attacheView(ActionButtonContract.View view) {
        this.view = view;
    }

    @Override
    public void detachedView() {
        this.view = null;
    }

    @Override
    public void buttonText() {
        service.buttonText(AppController.getInstance().getApiToken(), Constants.LANGUAGE, 0, 60).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ActionButtonResponse>>() {
                    @Override
                    public void onNext(Response<ActionButtonResponse> response) {
                        switch (response.code()) {
                            case 200:
                                model.setData(response.body().getDataList());
                                if (!response.body().getDataList().isEmpty())
                                    view.defaultItemSelected(response.body().getDataList().get(0));
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
                                                        buttonText();
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
