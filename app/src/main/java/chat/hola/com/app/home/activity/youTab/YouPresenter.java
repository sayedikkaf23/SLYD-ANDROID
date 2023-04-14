package chat.hola.com.app.home.activity.youTab;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.activity.youTab.followrequest.ResponseModel;
import chat.hola.com.app.home.activity.youTab.model.ChannelSubscibe;
import chat.hola.com.app.home.activity.youTab.model.YouModel;
import chat.hola.com.app.home.activity.youTab.model.YouResponse;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by ankit on 22/2/18.
 */

public class YouPresenter implements YouContract.Presenter {

    private static final String TAG = YouPresenter.class.getSimpleName();
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;

    @Nullable
    private YouContract.View view;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    NetworkConnector networkConnector;

    @Inject
    public YouPresenter() {
    }

    @Override
    public void attachView(YouContract.View view) {
        if (view != null) this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadYouData(int offset, int limit) {
        isLoading = true;

        if (offset == 0) {
            page = 0;
            isLastPage = false;
        }
        if (view != null) {

            view.isDataLoading(true);
        }
        service.getYouActivity(AppController.getInstance().getApiToken(), Constants.LANGUAGE, offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<YouResponse>>() {
                    @Override
                    public void onNext(Response<YouResponse> youResponse) {
                        isLoading = false;
                        if (view != null) {
                            view.isDataLoading(false);
                            switch (youResponse.code()) {
                                case 200:
                                    isLastPage = youResponse.body().getData().size() < PAGE_SIZE;
                                    if (youResponse.body().getData() == null || youResponse.body().getData().isEmpty())
                                        view.showErrorLayout(true);
                                    else {
                                        view.showYouActivity(youResponse.body().getData(), offset==0);
                                        view.showErrorLayout(false);
                                    }
                                    view.isInternetAvailable(true);
                                    break;
                                case 204:
                                    isLastPage = true;
                                    view.showErrorLayout(true);
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
                                                            loadYouData(offset, limit);
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                        if (view != null) {
                            view.isInternetAvailable(networkConnector.isConnected());
                            view.isDataLoading(false);
                            view.showErrorLayout(true);
                        }
                    }

                    @Override
                    public void onComplete() {
                        this.dispose();
                    }
                });
    }

    @Override
    public void setModel(YouModel model) {

    }

    @Override
    public void follow(String followingId) {
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", followingId);
        service.follow(AppController.getInstance().getApiToken(), "en", map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> followResponseResponse) {
                        switch (followResponseResponse.code()) {
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
                        Log.e(TAG, "failed to follow: " + e.getMessage());
                        //view.invalidateBtn(index,false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

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
                        Log.e(TAG, "failed to unFollow: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

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
                loadYouData(PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }

    public void channelRequest() {
        service.getRequestedChannels(AppController.getInstance().getApiToken(), "en")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ChannelSubscibe>>() {
                    @Override
                    public void onNext(Response<ChannelSubscibe> response) {

                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body().getRequestedChannels() != null)
                                        view.channelRequest(response.body().getRequestedChannels());
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
                                                            channelRequest();
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

    public void followRequest() {
        service.getfollowRequest(AppController.getInstance().getApiToken(), "en", 0, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseModel>>() {
                    @Override
                    public void onNext(Response<ResponseModel> response) {

                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body().getData() != null)
                                        view.followRequest(response.body().getData(), response.body().getData().size());
                                    break;
                                case 204:
                                    if (view != null)
                                        view.showEmptyRequest(true);
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
                                                            followRequest();
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
}
