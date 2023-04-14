package chat.hola.com.app.mystories;


import android.os.Handler;

import java.io.IOException;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by embed on 13/12/18.
 */

public class MyStoriesPresenter implements MyStoriesPresenterImpl.MyStoriesPresent {

    @Inject
    HowdooService service;
    @Inject
    MyStoriesPresenterImpl.MyStoriesPresenterView view;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    public MyStoriesPresenter() {
    }

    @Override
    public void attachView(Object view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void onToDeleteStory(String storyId, int position) {
        Observable<Response<ResponseBody>> observable = service.deleteStory(AppController.getInstance().getApiToken(), Constants.LANGUAGE, storyId);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {

                        int code = response.code();
                        String res;
                        try {
                            switch (code) {
                                case 200:
                                    view.onStoriesDeleteSuccess(position);
                                    res = response.body().string();
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
                                                            onToDeleteStory(storyId, position);
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
                                    sessionApiCall.getNewSession(service, sessionObserver);
                                    break;
                                default:
                                    res = response.errorBody().string();
                                    break;

                            }


                        } catch (IOException e) {
                            e.printStackTrace();
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
