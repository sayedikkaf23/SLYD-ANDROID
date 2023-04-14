package chat.hola.com.app.collections.collection;

import android.os.Handler;
import javax.inject.Inject;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.collections.model.PostResponse;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class CollectionPresenter implements CollectionContract.Presenter {

    @Inject
    SessionApiCall sessionApiCall;

    @Inject
    public CollectionPresenter() {
    }

    @Inject
    CollectionContract.View view;
    @Inject
    HowdooService service;


    @Override
    public void attachView(CollectionContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void getAllPost(int offset, int limit) {
        view.showProgress(true);
        service.getAllPost(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE,
                offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<PostResponse>>() {
                    @Override
                    public void onNext(Response<PostResponse> response) {
                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:

                              if( response.body()  != null){
                                view.collectionPostResponse(response.body().getData());}
                                break;
                            case 204:
                                view.showEmpty();
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
                                                        getAllPost(offset,limit);
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

    @Override
    public void getCollectionPost(String collectionId, int offset, int limit) {
        view.showProgress(true);
        service.getCollectionPost(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE,
                collectionId,
                offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<PostResponse>>() {
                    @Override
                    public void onNext(Response<PostResponse> response) {
                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                              if( response.body()  != null){
                                view.collectionPostResponse(response.body().getData());}
                                break;
                            case 204:
                                view.showEmpty();
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
                                                        getCollectionPost(collectionId,offset,limit);
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
