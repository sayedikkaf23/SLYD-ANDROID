package chat.hola.com.app.search.people;

import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import javax.inject.Inject;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import chat.hola.com.app.search.model.SearchResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by ankit on 24/2/18.
 */

public class PeoplePresenter implements PeopleContract.Presenter {

    @Nullable
    private PeopleContract.View view;
    @Inject
    HowdooService service;
    private SessionApiCall sessionApiCall = new SessionApiCall();
    @Inject
    PostObserver postObserver;

    @Inject
    PeoplePresenter() {
    }

    @Override
    public void attachView(PeopleContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void search(CharSequence charSequence) {
        service.search(AppController.getInstance().getApiToken(), "en", charSequence.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<SearchResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<SearchResponse> response) {

                        switch (response.code()) {
                            case 200:
                                if (response.body().getData() != null && response.body().getData().size() > 0) {
                                    if (view != null)
                                        view.showData(response.body().getData());
                                }
                                break;
                            case 204:
                                if (view != null) {
                                    view.noData();
                                }
                                break;
                            case 401:
                                if (view != null) {
                                    view.sessionExpired();
                                }
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
                                                        search(charSequence);
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
                        Log.e("search", "onError: " + e.getMessage());
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
    public void follow(final int pos, String followingId) {
        Map<String,Object> map = new HashMap<>();
        map.put("followingId",followingId);
        service.follow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        if (view != null) {
                            switch (response.code()) {
                                case 200:
                                    view.isFollowing(pos, 2);// private
                                    break;
                                case 201:
                                    view.isFollowing(pos, 1);
                                    postObserver.postFollowObservableEmitter(new Pair<>(true,followingId));
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
                                                            follow(pos, followingId);
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
    public void unfollow(final int pos, String followingId) {
        Map<String,Object> map = new HashMap<>();
        map.put("followingId",followingId);
        service.unfollow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {

                        switch (response.code()) {
                            case 200:
                                if (view != null) {
                                    view.isFollowing(pos, 0);
                                }
                                postObserver.postFollowObservableEmitter(new Pair<>(false,followingId));
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
                                                        follow(pos, followingId);
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
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
