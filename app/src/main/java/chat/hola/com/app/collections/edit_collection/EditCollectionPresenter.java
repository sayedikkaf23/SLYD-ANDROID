package chat.hola.com.app.collections.edit_collection;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class EditCollectionPresenter implements EditCollectionContract.Presenter {

    @Inject
    PostObserver postObserver;

    @Inject
    public EditCollectionPresenter() {
    }

    @Inject
    HowdooService service;
    @Inject
    SessionApiCall sessionApiCall;
    @Inject
    EditCollectionContract.View view;

    @Override
    public void attachView(EditCollectionContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void editCollection(String collectionId, String collectionName, String coverImage) {

        view.showProgress(true);

        Map<String, Object> map = new HashMap<>();
        map.put("coverImage", coverImage);
        map.put("collectionId", collectionId);
        map.put("collectionName", collectionName);

        service.editCollection(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {

                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                view.collectionEdited();
                                postObserver.postData(true);
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
                                                        editCollection(collectionId, collectionName, coverImage);
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
    public void deleteCollection(String collectionId) {

        view.showProgress(true);

        service.deleteCollection(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE, collectionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                view.deletedCollection();
                                postObserver.postData(true);
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
                                                        deleteCollection(collectionId);
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
