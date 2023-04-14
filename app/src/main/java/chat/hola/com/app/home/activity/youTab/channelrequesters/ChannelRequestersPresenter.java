package chat.hola.com.app.home.activity.youTab.channelrequesters;

import android.content.Intent;
import android.os.Handler;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.activity.youTab.channelrequesters.model.ChannelRequestModel;
import chat.hola.com.app.home.activity.youTab.channelrequesters.model.ClickListner;
import chat.hola.com.app.home.activity.youTab.model.ChannelSubscibe;
import chat.hola.com.app.home.activity.youTab.model.RequestedChannels;
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

public class ChannelRequestersPresenter implements ChannelRequestersContract.Presenter, ClickListner {

    @Inject
    HowdooService service;
    @Inject
    ChannelRequestersContract.View view;
    @Inject
    ChannelRequestModel model;
    @Inject
    NetworkConnector networkConnector;

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    ChannelRequestersPresenter() {
    }

    @Override
    public void setData(List<RequestedChannels> data) {
        model.setData(data);
    }

    @Override
    public void requestAction(String channelId) {

    }

    @Override
    public ClickListner getPresenter() {
        return this;
    }

    @Override
    public void loadData(Intent intent) {
        if (intent.getStringExtra("call") != null && intent.getStringExtra("call").equals("notification")) {
            getData();
        } else {
            setData((List<RequestedChannels>) intent.getSerializableExtra("data"));
        }
    }


    @Override
    public void onRequestAction(int position, boolean flag) {
        action(model.getChannelId(position), model.getUserId(position), flag, position);
    }

    private void action(String channelId, String userId, boolean flag, int position) {
        service.channelSubsciptionAction(AppController.getInstance().getApiToken(), Constants.LANGUAGE, model.getParams(channelId, userId, flag))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                model.accpted(position);
                                break;
                            case 401:
                                view.sessionExpired();
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
                                                        action(channelId, userId, flag, position);
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
    public void onChannelClick(int position) {
        view.callChannel(model.getChannelId(position));
    }

    @Override
    public void getData() {

        service.getRequestedChannels(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ChannelSubscibe>>() {
                    @Override
                    public void onNext(Response<ChannelSubscibe> response) {
                        switch (response.code()) {
                            case 200:
                                setData(response.body().getRequestedChannels());
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
                                                        getData();
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
}
