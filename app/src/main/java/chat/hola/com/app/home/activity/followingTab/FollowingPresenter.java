package chat.hola.com.app.home.activity.followingTab;

import android.os.Handler;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.activity.followingTab.model.ClickListner;
import chat.hola.com.app.home.activity.followingTab.model.FollowingModel;
import chat.hola.com.app.home.activity.followingTab.model.FollowingResponse;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author 3Embed.
 * @since 22/2/18.
 */

public class FollowingPresenter implements FollowingContract.Presenter, ClickListner {

    @Nullable
    FollowingContract.View view;
    @Inject
    HowdooService service;
    @Inject
    FollowingModel model;
    @Inject
    NetworkConnector networkConnector;
    private SessionApiCall sessionApiCall = new SessionApiCall();
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;

    @Inject
    public FollowingPresenter() {

    }

    @Override
    public void attachView(FollowingContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void init() {
        if (view != null)
            view.initPostRecycler();
    }

    @Override
    public void loadFollowing(int skip, int limit) {
        isLoading = true;

        if (skip == 0) {
            page = 0;
            isLastPage = false;
        }

        if (view != null)
            view.loading(true);
        service.followingActivities(AppController.getInstance().getApiToken(), Constants.LANGUAGE, skip, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowingResponse>>() {
                    @Override
                    public void onNext(Response<FollowingResponse> response) {
                        isLoading = false;
                        if (view != null) {
                            switch (response.code()) {
                                case 200:
                                    /*
                                    * Bug Id:DUBAND079
                                    * Bug Title:Following Activity page crash
                                    * Bug Desc:exception handled from app side if null
                                    * Developer name:Ankit K Tiwary
                                    * Fixed Date:14-April-2021*/
                                    if (response.body() != null && response.body().getData() != null){
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    view.showFollowings(response.body().getData(),skip==0);
                                    }
                                    break;
                                case 204:
                                    isLastPage=true;
                                    /*
                                     * BugId: #2732
                                     * Bug Title: no place holder for following activity
                                     * Bug Desc: handle no data view
                                     * Developer name:Hardik
                                     * Fixed Date:23/6/2021*/
                                    view.noData(skip==0);
                                    break;
                                case 401:
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
                                                            loadFollowing(skip, limit);
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
                            view.isInternetAvailable(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                        if (view != null) {
                            view.isInternetAvailable(networkConnector.isConnected());
                            view.loading(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null)
                            view.loading(false);
                    }
                });
    }

    @Override
    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                page++;
                loadFollowing(PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }

    @Override
    public void onUserClicked(String userId) {
        if (view != null) {
            view.onUserClicked(userId);
        }
    }

    @Override
    public void onMediaClick(int position, android.view.View v) {
        if (view != null) {
            view.onMediaClick(position, v);
        }
    }
}
