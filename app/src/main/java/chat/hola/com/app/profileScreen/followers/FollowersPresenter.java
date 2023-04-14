package chat.hola.com.app.profileScreen.followers;

import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import chat.hola.com.app.profileScreen.followers.Model.FollowersResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Created by ankit on 19/3/18.
 */

public class FollowersPresenter implements FollowersContract.Presenter {

    static final String TAG = FollowersPresenter.class.getSimpleName();
    final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    FollowersContract.View view;
    @Inject
    HowdooService service;
    @Inject
    NetworkConnector networkConnector;
    @Inject
    PostObserver postObserver;

    @Inject
    public FollowersPresenter() {
    }

    @Override
    public void init() {
        page = 0;
        view.applyFont();
        view.recyclerViewSetup();
    }

    @Override
    public void loadFollowersData(int skip, int limit, String userId) {
        isLoading = true;
        view.isDataLoading(true);
        service.getFollowers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId,
                skip, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {

                        switch (followersResponse.code()) {
                            case 204:
                                view.clearList(true);
                                view.showEmpty();
                                break;
                            case 200:
                                isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                Log.w(TAG, "followers fetched successfully");
                                view.clearList(skip == 0);
                                view.showFollowers(followersResponse.body().getData());
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loadFollowersData(skip, limit, userId);
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
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());

                        view.isDataLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void loadFolloweesData(int skip, int limit, String userId) {
        isLoading = true;
        view.isDataLoading(true);
        service.getFollowees(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId,
                skip, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {
                        switch (followersResponse.code()) {
                            case 204:
                                view.clearList(true);
                                view.showEmpty();
                                break;
                            case 200:
                                isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                Log.w(TAG, "followees fetched successfully");
                                view.clearList(skip == 0);
                                view.showFollowees(followersResponse.body().getData());
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loadFolloweesData(skip, limit, userId);
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
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());

                        view.isDataLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    /*
     * Bug Title: following-If user unfollows every user then the posts should not be displayed in the following page
     * Bug Id: XXXXXXXX
     * Fix Description: add observer
     * Developer Name: Hardik
     * Fix Date: 9/4/2021
     * */

    @Override
    public void follow(String followingId) {
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", followingId);
        service.follow(AppController.getInstance().getApiToken(), "en", map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> body) {
                        switch (body.code()) {
                            case 200:
                                postObserver.postFollowObservableEmitter(new Pair<>(true,followingId));
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        follow(followingId);
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
                        view.isInternetAvailable(networkConnector.isConnected());

                        //view.invalidateBtn(index,false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*
     * Bug Title: following-If user unfollows every user then the posts should not be displayed in the following page
     * Bug Id: XXXXXXXX
     * Fix Description: add observer
     * Developer Name: Hardik
     * Fix Date: 9/4/2021
     * */

    @Override
    public void unFollow(String followingId) {
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", followingId);
        service.unfollow(AppController.getInstance().getApiToken(), "en", map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> body) {
                        switch (body.code()) {
                            case 200:
                                postObserver.postFollowObservableEmitter(new Pair<>(false,followingId));
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        unFollow(followingId);
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
                        view.isInternetAvailable(networkConnector.isConnected());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void viewers(String postId, int skip, int limit) {
        isLoading = true;
        view.isDataLoading(true);
        service.viewers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId, skip,
                limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {
                        switch (followersResponse.code()) {
                            case 204:
                                view.clearList(true);
                                view.showEmpty();
                                break;
                            case 200:
                                view.clearList(skip == 0);
                                isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                Log.w(TAG, "viewers fetched successfully");
                                view.showFollowers(followersResponse.body().getData());
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        viewers(postId, skip, limit);
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
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());

                        view.isDataLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void searchViewers(String postId, int skip, int limit, String searchText) {
        isLoading = true;
        view.isDataLoading(true);
        service.searchViewers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId,
                searchText, skip, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {
                        switch (followersResponse.code()) {
                            case 204:
                                view.clearList(true);
                                view.showEmpty();
                                break;
                            case 200:
                                isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                Log.w(TAG, "searchViewers fetched successfully");
                                view.clearList(skip == 0);
                                view.showFollowers(followersResponse.body().getData());
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        searchViewers(postId, skip, limit, searchText);
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
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());

                        view.isDataLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    public void callApiOnScroll(int call, String userId, int firstVisibleItemPosition,
                                int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                page++;
                if (call == 1) {
                    loadFollowersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                } else if (call == 2) {
                    loadFolloweesData(PAGE_SIZE * page, PAGE_SIZE, userId);
                }else if(call==4){
                    loadSubscribersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                } else {
                    //here userId is postId
                    likers(userId, PAGE_SIZE * page, PAGE_SIZE);
                }
            }
        }
    }

    public void likers(String postId, int skip, int limit) {
        isLoading = true;
        view.isDataLoading(true);
        service.likers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId, skip,
                limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {
                        switch (followersResponse.code()) {
                            case 204:
                                view.clearList(true);
                                view.showEmpty();
                                break;
                            case 200:
                                view.clearList(skip == 0);
                                isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                Log.w(TAG, "likers fetched successfully");
                                view.showFollowers(followersResponse.body().getData());
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        likers(postId, skip, limit);
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
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());

                        view.isDataLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    public void callApiOnScroll(int call, String userId, int firstVisibleItemPosition,
                                int visibleItemCount, int totalItemCount, String searchText) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                page++;
                switch (call) {
                    case 0:
                        likers(userId, PAGE_SIZE * page, PAGE_SIZE);
                        break;
                    case 11:
                        searchLikers(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                        break;
                    case 3:
                        viewers(userId, PAGE_SIZE * page, PAGE_SIZE);
                        break;
                    case 31:
                        searchViewers(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                        break;
                    case 1:
                        loadFollowersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                        break;
                    case 2:
                        loadFolloweesData(PAGE_SIZE * page, PAGE_SIZE, userId);
                        break;
                    case 21:
                        searchFollwers(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                        break;
                    case 22:
                        searchFollwees(userId, PAGE_SIZE * page, PAGE_SIZE, searchText);
                        break;
                    case 4:
                        loadSubscribersData(PAGE_SIZE * page, PAGE_SIZE, userId);
                        break;
                }
            }
        }
    }

    public void searchFollwees(String userId, int skip, int limit, String searchText) {
        isLoading = true;
        view.isDataLoading(true);
        service.searchFollowees(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId,
                searchText, skip, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {
                        switch (followersResponse.code()) {
                            case 204:
                                view.clearList(true);
                                view.showEmpty();
                                break;
                            case 200:
                                isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                view.clearList(skip == 0);
                                view.showFollowers(followersResponse.body().getData());
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        searchFollwees(userId, skip, limit, searchText);
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
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    public void searchFollwers(String userId, int skip, int limit, String searchText) {
        isLoading = true;
        view.isDataLoading(true);
        service.searchFollowers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId,
                searchText, skip, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {
                        switch (followersResponse.code()) {
                             case 204:
                                 view.clearList(true);
                                view.showEmpty();
                                break;
                            case 200:
                                isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                view.clearList(skip == 0);
                                view.showFollowers(followersResponse.body().getData());
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        searchFollwers(userId, skip, limit, searchText);
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
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    public void searchLikers(String postId, int skip, int limit, String searchText) {
        isLoading = true;
        view.isDataLoading(true);
        service.searchLikers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId,
                searchText, skip, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {
                        switch (followersResponse.code()) {
                            case 200:
                                isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                view.clearList(skip == 0);
                                view.showFollowers(followersResponse.body().getData());
                                break;
                            case 204:
                                view.clearList(true);
                                view.showEmpty();
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        searchLikers(postId, skip, limit, searchText);
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
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());

                        view.isDataLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void loadSubscribersData(int skip, int limit, String userId) {
        isLoading = true;
        view.isDataLoading(true);
        service.getSubscribers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, 2,
                skip, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {

                        switch (followersResponse.code()) {
                            case 200:
                                if(followersResponse.body().getData()!=null) {
                                    isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                    Log.w(TAG, "subcribers fetched successfully");
                                    view.clearList(skip == 0);
                                    view.showSubscribers(followersResponse.body().getData());
                                }
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        loadSubscribersData(skip, limit, userId);
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
                        view.isDataLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());

                        view.isDataLoading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }
}
