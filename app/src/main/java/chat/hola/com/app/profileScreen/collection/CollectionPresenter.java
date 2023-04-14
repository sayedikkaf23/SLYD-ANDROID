package chat.hola.com.app.profileScreen.collection;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.collections.model.CollectionResponse;
import chat.hola.com.app.home.stories.StoriesPresenter;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.PostUpdateData;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class CollectionPresenter implements CollectionContract.Presenter {

    private static final String TAG = StoriesPresenter.class.getSimpleName();
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    private SessionApiCall sessionApiCall = new SessionApiCall();
    @Nullable
    CollectionContract.View view;
    @Inject
    HowdooService service;
    @Inject
    NetworkConnector networkConnector;
    @Inject
    PostObserver postObserver;

    @Inject
    CollectionPresenter() {
    }

    @Override
    public void init() {
        if (view != null) view.setupRecyclerView();
        postObserver();
    }

    public void postObserver() {
        postObserver.getSavedObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostUpdateData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PostUpdateData likeUnlike) {
                        Log.d(TAG, "onNext: "+likeUnlike.getFrom()+" "+likeUnlike.getPostId());
                        page = 0;
                        getCollections(page*PAGE_SIZE,PAGE_SIZE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void attachView(CollectionContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount,
                                int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                page++;
                getCollections(page*PAGE_SIZE,PAGE_SIZE);
            }
        }
    }

    @Override
    public void getCollections(int offSet, int limit) {
        isLoading = true;
        if(view!=null)view.isLoading(true);
        service.getCollections(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE,
                offSet, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CollectionResponse>>() {
                    @Override
                    public void onNext(Response<CollectionResponse> response) {
                        isLoading = false;
                        if(view!=null)view.isLoading(false);
                        switch (response.code()) {
                            case 200:
                                isLastPage = response.body().getData().size() < PAGE_SIZE;
                                if(view!=null)view.onSuccess(response.body().getData(),offSet==0);
                                break;
                            case 204:
                                if(view!=null)view.showEmptyUi(offSet==0);
                                break;
                            case 401:
                                if(view!=null)view.sessionExpired();
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
                                            }

                                            @Override
                                            public void onComplete() {
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                        if(view!=null)view.isLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }
}
