package chat.hola.com.app.live_stream.Home.live_users;

import android.os.Handler;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.live_stream.Observable.AllStreamsObservable;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.ResponcePojo.LiveBroadCasterResponse;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>LiveUsersContract</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0.
 * @since 26/7/2019.
 */

public class LiveUsersPresenter implements LiveUsersContract.Presenter {
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;

    @Inject
    SessionManager manager;
    @Inject
    LiveStreamService apiServices;
    @Inject
    HowdooService service;
    @Inject
    Gson gson;
    @Inject
    LiveUsersContract.View broadcasterView;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    public LiveUsersPresenter() {
    }

    @Override
    public void callLiveBroadcaster(int offset, int limit) {
        isLoading = true;
        Observable<Response<ResponseBody>> observable = apiServices.getLiveStreamers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, offset, limit);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        int code = responseBodyResponse.code();
                        try {
                            if (code == 200) {

                                String response = responseBodyResponse.body().string();

                                LiveBroadCasterResponse liveBroadCaster = gson.fromJson(response, LiveBroadCasterResponse.class);

                                isLastPage = liveBroadCaster.getData().getStreams()==null
                                        || liveBroadCaster.getData().getStreams().size() < PAGE_SIZE;

                                if (broadcasterView != null)
                                    broadcasterView.liveBroadCasterData(liveBroadCaster.getData().getStreams());

                            } else {
                                String responseError = responseBodyResponse.errorBody().string();
//                                if (broadcasterView != null)
//                                    broadcasterView.showAlert(new JSONObject(responseError).getString("message"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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
    public void allStreamDataRxJava() {

        Observer<AllStreamsData> observer = new Observer<AllStreamsData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AllStreamsData dataStream) {

                if (broadcasterView != null)
                    broadcasterView.onAllStreamDataReceived(dataStream);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        AllStreamsObservable.getInstance().subscribe(observer);

    }

    @Override
    public void follow(String followingId) {
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", followingId);
        service.follow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
//                            case 200:
//                                postObserver.postData(true);
//                                break;
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void unfollow(String followingId) {
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", followingId);
        service.unfollow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
//                            case 200:
//                                postObserver.postData(true);
//                                break;
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
                                                        unfollow(followingId);
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

    @Override
    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++;
                callLiveBroadcaster(PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }

    @Override
    public void getWalletBalance() {
    }

    @Override
    public void following(AllStreamsData dataStream) {
        service.following(AppController.getInstance().getApiToken(), Constants.LANGUAGE, dataStream.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        if (response.code() == 200) {
                            //follow status code : 1=following, 2=requested
                            assert response.body() != null;
                            dataStream.setFollowing(response.body().isFollowing());
                            dataStream.setFollowStatus(response.body().getStatusCode());
                            dataStream.setPrivate(response.body().isPrivate());
                            broadcasterView.setData(dataStream);
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
