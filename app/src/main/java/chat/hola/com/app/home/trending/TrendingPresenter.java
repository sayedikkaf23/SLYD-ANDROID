package chat.hola.com.app.home.trending;

import android.os.Handler;

import android.util.Log;
import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.HowdooServiceTrending;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.trending.model.HeaderAdapter;
import chat.hola.com.app.home.trending.model.HeaderResponse;
import chat.hola.com.app.home.trending.model.Trending;
import chat.hola.com.app.home.trending.model.TrendingModel;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>TrendingPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 19/6/18.
 */

public class TrendingPresenter implements TrendingContract.Presenter, HeaderAdapter.ClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    private TrendingContract.View view;
    @Inject
    HowdooServiceTrending trending;
    @Inject
    TrendingModel model;
    @Inject
    HowdooService service;
    @Inject
    PostObserver postObserver;
    @Inject
    SessionApiCall sessionApiCall;
    @Inject
    SessionManager sessionManager;
  private boolean observerAlreadyAdded=false;

    String selectedCategoryId;

    @Inject
    TrendingPresenter() {
    }

    @Override
    public void attachView(TrendingContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }


    @Override
    public void init() {
        if (view != null) {
            view.initHeaderRecycler();
            view.initContentRecycler();
        }
      if(!observerAlreadyAdded){
        observerAlreadyAdded=true;
        postObserver.getObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                page = 0;
                                loadContent(0, 20);
//                                callApiOnScroll(PAGE_SIZE * page, PAGE_SIZE, PAGE_SIZE);
                            }
                        }, 2000);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });}
    }

    @Override
    public void loadHeader() {
        trending.getCategories(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<HeaderResponse>>() {
                    @Override
                    public void onNext(Response<HeaderResponse> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body().getHeaders() != null && !response.body().getHeaders().isEmpty()) {
                                        assert response.body() != null;
                                        view.showHeader(model.setHeader(response.body().getHeaders()));
                                        view.isContentAvailable(true);
                                    }
                                    break;
                                case 204:
                                    view.isContentAvailable(false);
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
                                                            loadHeader();
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
                        } catch (NullPointerException ignored) {
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

    @Override
    public void loadContent(int skip, int limit) {
        isLoading = true;
        trending.getTrending(AppController.getInstance().getApiToken(), Constants.LANGUAGE, skip, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Trending>>() {
                    @Override
                    public void onNext(Response<Trending> response) {
                        switch (response.code()) {
                            case 200:

                                isLastPage = response.body().getData().size() < PAGE_SIZE;
                                if (skip == 0)
                                    model.clear();
                                model.setData(response.body().getData());
                                if (view != null)
                                    view.isLoading(false);
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
                                                        loadContent(skip, limit);
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

                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void onItemClick(String categoryId, String categoryName) {
        selectedCategoryId = categoryId;
        view.setCategoryId(categoryId,categoryName);
    }

    @Override
    public void onStarClick() {
        view.onStarCLick();
    }

    @Override
    public void onLiveStreamClick() {
        view.onLiveStreamCLick();
    }

    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++;
                loadContent(PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }
}
