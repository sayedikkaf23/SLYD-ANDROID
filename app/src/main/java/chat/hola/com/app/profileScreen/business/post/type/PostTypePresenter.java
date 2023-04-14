package chat.hola.com.app.profileScreen.business.post.type;


import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.ActionButtonResponse;
import chat.hola.com.app.models.PostTypeResponse;
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
public class PostTypePresenter implements PostTypeContract.Presenter {

    private PostTypeContract.View view;

    @Inject
    HowdooService service;
    @Inject
    PostTypeModel model;
    private SessionApiCall sessionApiCall;

    @Inject
    public PostTypePresenter() {
        sessionApiCall = new SessionApiCall();
    }

    @Override
    public void attacheView(PostTypeContract.View view) {
        this.view = view;
    }

    @Override
    public void detachedView() {
        this.view = null;
    }

    @Override
    public void postType() {
        service.postType(AppController.getInstance().getApiToken(), Constants.LANGUAGE, 0, 60).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<PostTypeResponse>>() {
                    @Override
                    public void onNext(Response<PostTypeResponse> response) {
                        switch (response.code()) {
                            case 200:
                                model.setData(response.body().getPostTypes());
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(() -> postType(), 1000);
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
