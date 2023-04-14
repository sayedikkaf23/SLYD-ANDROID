package chat.hola.com.app.home.activity.youTab.followrequest;

import android.content.Intent;
import android.os.Handler;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>ChannelRequestersPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class FollowRequestPresenter implements FollowRequestContract.Presenter, ItemClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    FollowRequestContract.View view;
    @Inject
    FollowRequestModel model;
    @Inject
    NetworkConnector networkConnector;

    @Inject
    FollowRequestPresenter() {
    }

    @Override
    public void setData(List<ReuestData> data) {
        model.setData(data);
    }

    @Override
    public void requestAction(String channelId) {

    }

    @Override
    public ItemClickListner getPresenter() {
        return this;
    }

    @Override
    public void loadData(Intent intent) {
    }

    @Override
    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++;
                getData(PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }

    @Override
    public void onRequestAction(int position, int status) {
        action(model.getUserId(position), status, position);
    }

    private void action(String userId, int status, int position) {
        service.requestAction(AppController.getInstance().getApiToken(), Constants.LANGUAGE, model.getParams(userId, status))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                model.accepted(position);
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
                                                        action(userId, status, position);
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
    public void onUserClicked(int position) {
        view.callUser(model.getUserId(position));
    }

    @Override
    public void getData(int offset, int limit) {
        service.getfollowRequest(AppController.getInstance().getApiToken(), Constants.LANGUAGE, offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseModel>>() {
                    @Override
                    public void onNext(Response<ResponseModel> response) {
                        switch (response.code()) {
                            case 200:
                                isLastPage = response.body().getData().size() < PAGE_SIZE;
                                if (offset == 0)
                                    model.clearList();
                                setData(response.body().getData());
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
                                                        getData(offset, limit);
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
