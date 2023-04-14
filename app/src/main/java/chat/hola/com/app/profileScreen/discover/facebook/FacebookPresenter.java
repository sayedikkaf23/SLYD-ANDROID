package chat.hola.com.app.profileScreen.discover.facebook;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import chat.hola.com.app.profileScreen.discover.follow.Follow;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h>FacebookPresenter</h>
 *
 * @author 3Embed.
 * @since 02/03/18.
 */

public class FacebookPresenter implements FacebookContract.Presenter {
    private SessionApiCall sessionApiCall = new SessionApiCall();
    private static final String TAG = FacebookPresenter.class.getSimpleName();

    @Nullable
    private FacebookContract.View view;

    @Inject
    HowdooService service;


    @Inject
    DiscoverActivity activity;


    @Inject
    public FacebookPresenter() {
    }


    @Override
    public void attachView(FacebookContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void init() {
        if (view != null) {
            view.applyFont();
            view.initPostRecycler();
            view.setFbVisibility(true);
            view.setContactVisibility(false);
        }
    }

    @Override
    public void loadFollows() {

        //need to load data from api

        ArrayList<Contact> contacts = new ArrayList<>();
        Follow follow = new Follow(200, contacts);
        if (view != null)
            view.showFollows(follow);
    }

    @Override
    public void setFbVisibility(boolean show) {
        if (view != null)
            view.setFbVisibility(show);
    }

    @Override
    public void setContactVisibility(boolean show) {
        if (view != null)
            view.setContactVisibility(show);
    }

    @Override
    public void fbLogin() {

    }

    @Override
    public void returnFbResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void follow(String followingId) {
        Map<String,Object> map = new HashMap<>();
        map.put("followingId",followingId);
        service.follow(AppController.getInstance().getApiToken(), "en", map)
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

        Map<String,Object> map = new HashMap<>();
        map.put("followingId",followingId);
        service.unfollow(AppController.getInstance().getApiToken(), "en",
                map)
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
    public void followAll(List<String> strings) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("followeeId", strings);
        service.followAll(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
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
                                                        followAll(strings);
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
                            case 200:
                                view.followedAll(true);
                                break;
                            case 401:
                                view.sessionExpired();
                            default:
                                view.followedAll(false);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.followedAll(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void fetchFriendList() {
        if (view != null) {
            view.isDataLoading(true);
            view.showEmptyUi(false);
        }
    }

    @Override
    public void checkForFbLogin() {

    }


}
