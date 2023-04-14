package chat.hola.com.app.collections.add_to_collection;

import android.os.Handler;

import com.ezcall.android.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.collections.model.CreateCollectionResponse;
import chat.hola.com.app.collections.model.PostResponse;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AddToCollectionPresenter implements AddToCollectionContract.Presenter{

    @Inject
    public AddToCollectionPresenter() {
    }
    @Inject
    HowdooService service;
    @Inject
    AddToCollectionContract.View view;
    @Inject
    SessionApiCall sessionApiCall;

    @Override
    public void attachView(AddToCollectionContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void getCollectionPost(String collectionId, int offset, int limit) {
        view.showProgress(true);
        service.getAllBookMarkById(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE,
                collectionId,offset,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<PostResponse>>() {
                    @Override
                    public void onNext(Response<PostResponse> response) {
                        view.showProgress(false);
                        switch (response.code()) {
                            case 200:
                              if( view != null){
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

    @Override
    public void addPostToCollection(String collectionId, List<String> selectedPostIds) {
        view.showProgress(true);
        Map<String,Object> map = new HashMap<>();
        map.put("postIds",selectedPostIds);
        map.put("collectionId",collectionId);
        service.addToCollection(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE,
                map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.showProgress(false);
                        switch (response.code()){
                            case 200:
                                view.successfullyAdded();
                                break;
                            case 502:
                                if(view!=null) view.showMessage("", R.string.alredy_added_in_collection);
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
                                                        addPostToCollection(collectionId,selectedPostIds);
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
                              if( view != null){
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
    public void createCollection(String collectionName, String coverImage, List<String> selectedPostIds) {
        view.showProgress(true);
        Map<String, Object> map = new HashMap<>();
        map.put("collectionName", collectionName);
        map.put("coverImage", coverImage);
        map.put("postId", selectedPostIds);
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
                                                        createCollection(collectionName,coverImage,selectedPostIds);
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
