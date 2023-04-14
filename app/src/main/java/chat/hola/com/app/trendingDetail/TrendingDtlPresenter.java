package chat.hola.com.app.trendingDetail;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.ezcall.android.R;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.HowdooServiceTrending;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.model.Location;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;
import chat.hola.com.app.trendingDetail.model.ClickListner;
import chat.hola.com.app.trendingDetail.model.TrendingDtlModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>TrendingDtlPresenter</h1>
 *
 * @author 3Embed.
 * @version 1.0
 * @since 21/2/18.
 */

public class TrendingDtlPresenter implements TrendingDtlContract.Presenter, ClickListner {

    @Inject
    HowdooService service;
    @Inject
    HowdooServiceTrending howdooServiceTrending;
    @Inject
    TrendingDtlModel model;
    @Inject
    TrendingDtlContract.View view;
    @Inject
    NetworkConnector networkConnector;
    private Location location;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLastPage = false;
    public int page = 0;
    private Intent intent;

    @Inject
    TrendingDtlPresenter() {
    }

    @Override
    public void init() {
        view.applyingFont();
        view.initDetailRecycler();
    }

    @Override
    public void callApiOnScroll(String postId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++;
                if(intent!=null)selectType(intent);
            }
        }
    }

    @Override
    public void subscribeChannel(String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        service.subscribeChannel(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.invalidateSubsButton(response.code() == 200);
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
                                                        subscribeChannel(channelId);
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
                        view.invalidateSubsButton(false);
                    }

                    @Override
                    public void onComplete() {
                        this.dispose();
                    }
                });

    }

    @Override
    public void unSubscribeChannel(String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        service.unSubscribeChannel(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.invalidateUnSubsButton(response.code() == 200);
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
                                                        unSubscribeChannel(channelId);
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
                        view.invalidateUnSubsButton(true);
                    }

                    @Override
                    public void onComplete() {
                        this.dispose();
                    }
                });
    }


    @Override
    public void getChannelData(String channelId) {

        service.getChannelById(AppController.getInstance().getApiToken(), Constants.LANGUAGE, channelId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<chat.hola.com.app.trendingDetail.model.ChannelResponse>>() {
                    @Override
                    public void onNext(Response<chat.hola.com.app.trendingDetail.model.ChannelResponse> response) {
                        switch (response.code()) {
                            case 200:
                                model.setData(response.body().getData(), true);
                                showData(response.body().getData(), response.body().getData().getChannelName(), -1);
                                String userId = response.body().getData().getUserId();
                                view.tbSubscribeVisibility(!userId.equals(AppController.getInstance().getUserId()));
                                view.llSubscribeVisibility(!userId.equals(AppController.getInstance().getUserId()));
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
                                                        getChannelData(channelId);
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
                        this.dispose();
                    }
                });
    }

    @Override
    public void getHashTagData(String hashtag) {

        service.getPostByHashtag(AppController.getInstance().getApiToken(), Constants.LANGUAGE, hashtag)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ChannelData>>() {
                    @Override
                    public void onNext(Response<ChannelData> response) {
                        switch (response.code()) {
                            case 200:
                                model.setData(response.body(), true);
                                showData(response.body(), hashtag, R.drawable.ic_def_hashtag);
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
                                                        getHashTagData(hashtag);
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
                        this.dispose();
                    }
                });
    }


    @Override
    public void getLocationData(String location, String placeId) {

        service.getPostByLocation(AppController.getInstance().getApiToken(), Constants.LANGUAGE, placeId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ChannelData>>() {
                    @Override
                    public void onNext(Response<ChannelData> response) {
                        switch (response.code()) {
                            case 200:
                                model.setData(response.body(), true);
                                if (!response.body().getData().isEmpty() && response.body().getData().get(0) != null)
                                    view.setLocation(response.body().getData().get(0).getLocation());
                                showData(response.body(), location, R.drawable.ic_location_red);
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
                                                        getLocationData(location, placeId);
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
                        this.dispose();
                    }
                });
    }

    @Override
    public void getCategoryData(String category, int skip, int limit) {

        howdooServiceTrending.getPostByCategory1(AppController.getInstance().getApiToken(), Constants.LANGUAGE, category, skip, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ChannelData>>() {
                    @Override
                    public void onNext(Response<ChannelData> response) {
                        switch (response.code()) {
                            case 200:
                                if (response.body() != null) {
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    model.setData(response.body(), skip == 0);
                                    showData(response.body(), response.body().getChannelName(), R.drawable.ic_category_default);
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
                                                        getCategoryData(category, page * PAGE_SIZE, PAGE_SIZE);
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        this.dispose();
                    }
                });
    }

    @Override
    public void onItemClick(int position, View view) {
        this.view.itemClick(position, view,model.getData());
    }


    @Override
    public void setData(ChannelData data) {
        model.setData(data, true);
    }

    @Override
    public void selectType(Intent intent) {
        this.intent = intent;
        switch (intent.getStringExtra("call")) {
            case "music":
                getMusicData(intent.getStringExtra("musicId"));
                view.llSubscribeVisibility(false);
                break;
            case "channel":
                // String channelId = ;
                getChannelData(intent.getStringExtra("channelId"));
                // view.tbSubscribeVisibility(!channelId.equals(AppController.getInstance().getUserId()));
                //view.llSubscribeVisibility(!channelId.equals(AppController.getInstance().getUserId()));
                break;
            case "category":
                getCategoryData(intent.getStringExtra("categoryId"),page*PAGE_SIZE,PAGE_SIZE);
                view.tbSubscribeVisibility(false);
                view.llSubscribeVisibility(false);
                break;
            case "hashtag":
                getHashTagData(intent.getStringExtra("hashtag"));
                view.llSubscribeVisibility(false);
                break;
            case "location":
                getLocationData(intent.getStringExtra("location"), intent.getStringExtra("placeId"));
                //   view.setLocation((Location) intent.getSerializableExtra("latlong"));
                view.mapVisibility(true);
                view.llSubscribeVisibility(false);
                break;
            default:
                ChannelData data = (ChannelData) intent.getSerializableExtra("data");
                view.tbSubscribeVisibility(!data.getUserId().equals(AppController.getInstance().getUserId()));
                view.llSubscribeVisibility(!data.getUserId().equals(AppController.getInstance().getUserId()));
                model.setData(data, true);
                showData(data, "", -1);
        }
    }

    private void getMusicData(String musicId) {
        service.getPostByMusicId(AppController.getInstance().getApiToken(), Constants.LANGUAGE, musicId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<chat.hola.com.app.music.Response>>() {
                    @Override
                    public void onNext(Response<chat.hola.com.app.music.Response> postsResponse) {
                        switch (postsResponse.code()) {
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
                                                        getMusicData(musicId);
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


    private void showData(ChannelData data, String text, int drawable) {
        int onText = -1, offText = -1;
        boolean isChecked = false;
        if (data.isSubscribed() != null) {
            switch (data.isSubscribed()) {
                case 0:
                    //not subscribed
                    offText = R.string.subscribe;
                    onText = data.getPrivate() ? R.string.requested : R.string.subscribed;
                    break;
                case 1:
                    // subscribed
                    onText = R.string.subscribed;
                    offText = R.string.subscribe;
                    isChecked = true;
                    break;
                case 2:
                    // requested
                    onText = R.string.requested;
                    offText = R.string.request;
                    isChecked = true;
                    break;
            }
        }

        view.showData(data, text, drawable, onText, offText, isChecked);
    }
}
