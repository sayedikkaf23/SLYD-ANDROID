package chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster;

import android.os.Handler;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.live_stream.Observable.AllStreamsObservable;
import chat.hola.com.app.live_stream.Observable.GiftEventObservable;
import chat.hola.com.app.live_stream.Observable.LikeEventObservable;
import chat.hola.com.app.live_stream.Observable.ParticularStreamChatMessageObservable;
import chat.hola.com.app.live_stream.Observable.ParticularStreamPresenceEventObservable;
import chat.hola.com.app.live_stream.ResponcePojo.AllReceivedMessage;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.ResponcePojo.GiftEvent;
import chat.hola.com.app.live_stream.ResponcePojo.LikeEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;
import chat.hola.com.app.live_stream.ResponcePojo.StreamPresenceEvent;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.pubsub.MqttEvents;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.StreamStats;
import chat.hola.com.app.models.StreamViewersResponse;
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
 * Created by moda on 12/3/2018.
 */
public class LiveBroadCastPresenterImpl
        implements LiveBroadCastPresenterContract.LiveBroadCastPresenter {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;

    static final int MESSAGE_PAGE_SIZE = 50;
    private boolean isMessageLoading = false;
    private boolean isMessageLastPage = false;
    public static int message_page = 0;
    private Observer<AllStreamsData> allStreamsObserver;
    private static final String TAG = LiveBroadCastPresenterImpl.class.getSimpleName();
    @Inject
    LiveStreamService apiServices;
    @Inject
    SessionManager manager;
    @Inject
    MQTTManager mqttManager;
    @Inject
    LiveBroadCastPresenterContract.LiveBroadCastView view;
    @Inject
    HowdooService service;
    @Inject
    Gson gson;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    public LiveBroadCastPresenterImpl() {
    }

    @Override
    public void connectView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void publishChatMessage(String streamId, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("streamId", streamId);
        map.put("message", msg);
        Observable<Response<ResponseBody>> observer =
                apiServices.postApiChatStream(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                        map);
        observer.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();

                        try {

                            if (code != 200) {

                                String responseError = responseBodyResponse.errorBody().string();

                                view.showFailedToPublishMessageAlert(
                                        new JSONObject(responseError).getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
    public void subscribeStream(String streamId, String streamType, String thumbnail,
                                String streamName, boolean saveStream, long duration, String country, String city,
                                String place, String lat, String lng, int width, int height) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", streamId);
        map.put("type", streamType);
        map.put("streamName", streamName);
        map.put("thumbnail", thumbnail);
        map.put("record", saveStream);
        map.put("detection", false);
        map.put("duration", duration);

        map.put("imageUrl1Width", String.valueOf(width));
        map.put("imageUrl1Height", String.valueOf(height));
        map.put("place", place);
        map.put("countrySname", country);
        map.put("city", city);
        map.put("longitude", Double.parseDouble(lng));
        map.put("latitude", Double.parseDouble(lat));

        Observable<Response<ResponseBody>> observable =
                apiServices.postApiCallStream(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                        map);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();

                        try {
                            if (code == 200) {
                                String streamId =
                                        new JSONObject(responseBodyResponse.body().string()).getJSONObject("data")
                                                .getString("_id");

                                view.onStreamSubscribed(streamId);
                            } else {

                                view.onFailedToSubscribeStream(responseBodyResponse.errorBody().string());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
    public void streamChatMessageRxJava() {

        Observer<StreamChatMessage> observer = new Observer<StreamChatMessage>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(StreamChatMessage message) {

                if (message.getUserId().equals(manager.getUserId())) {

                    message.setUserName("You");
                }
                view.onStreamChatMessageReceived(message);
            }

            @Override
            public void onError(Throwable e) {

                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
        ParticularStreamChatMessageObservable.getInstance().subscribe(observer);
    }

    @Override
    public void streamPresenceEventRxJava() {
        Observer<StreamPresenceEvent> observer = new Observer<StreamPresenceEvent>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(StreamPresenceEvent streamPresenceEvent) {
                if (streamPresenceEvent.getId() != null && streamPresenceEvent.getId()
                        .equals(manager.getUserId())) {

                    streamPresenceEvent.setName("You");
                }
                view.onStreamPresenceEvent(streamPresenceEvent);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        ParticularStreamPresenceEventObservable.getInstance().subscribe(observer);
    }

    @Override
    public void likeEventRxJAva() {
        Observer<LikeEvent> observer = new Observer<LikeEvent>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LikeEvent likeEvent) {

                if (likeEvent.getUserId() != null) {
                    if (!likeEvent.getUserId().equals(manager.getUserId())) {
                        view.onLikeEvent(likeEvent);
                    }
                } else {
                    //For the ios
                    view.onLikeEvent(likeEvent);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        LikeEventObservable.getInstance().subscribe(observer);
    }

    @Override
    public void giftEventRxJAva() {
        Observer<GiftEvent> observer = new Observer<GiftEvent>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GiftEvent giftEvent) {

                if (giftEvent.getUserId() != null) {
                    if (giftEvent.getUserId().equals(manager.getUserId())) {

                        giftEvent.setUserName("You");
                    }
                    view.onGiftEvent(giftEvent);
                } else {

                    //IOS
                    giftEvent.setUserName("UNKNOWN");
                    view.onGiftEvent(giftEvent);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        GiftEventObservable.getInstance().subscribe(observer);
    }

    @Override
    public void getLiveStreamsChatHistory(String streamId, int offset, int limit) {
        isLoading = true;

        //Observable<Response<ResponseBody>> observable = apiServices.getApiChat(AppController.getInstance().getApiToken()(), Constants.LANGUAGE,
        //        streamId, (System.currentTimeMillis() / 1000)    );

        Observable<Response<AllReceivedMessage>> observable =
                apiServices.getApiChat(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                        streamId, "0", offset, limit);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<AllReceivedMessage>>() {
                    @Override
                    public void onNext(Response<AllReceivedMessage> responseBodyResponse) {
                        int code = responseBodyResponse.code();
                        isLoading = false;
                        String response;
                        try {
                            if (code == 200) {
                                //                                response = responseBodyResponse.body().string();
                                //                                AllReceivedMessage allReceivedMessage = gson.fromJson(response, AllReceivedMessage.class);
                                isLastPage = responseBodyResponse.body().getData().size() < MESSAGE_PAGE_SIZE;

                                ArrayList<StreamChatMessage> messages = responseBodyResponse.body().getData();
                                int size = messages.size();
                                for (int i = 0; i < size; i++) {

                                    if (messages.get(i).getUserId().equals(manager.getUserId())) {
                                        StreamChatMessage streamChatMessage = messages.get(i);
                                        streamChatMessage.setUserName("You");
                                        messages.set(i, streamChatMessage);
                                    }
                                }

                                view.onAllPastChatMessagesReceived(messages);
                            } else {
                                view.onFailedToFetchChatHistory(responseBodyResponse.errorBody().string());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void getActiveViewers(String streamId) {

        Observable<Response<ResponseBody>> observable =
                apiServices.getActiveViewers(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                        streamId);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        int code = responseBodyResponse.code();

                        String response;
                        try {
                            if (code == 200) {

                                response = responseBodyResponse.body().string();

                                JSONObject jsonObject = new JSONObject(response);

                                try {

                                    JSONObject streams = jsonObject.getJSONObject("data").getJSONObject("streams");

                                    if (streams.getString("action").equals("start")) {
                                        view.onActiveViewersReceived(streams.getInt("viewers"));
                                    } else {

                                        view.streamOffline();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {

                                view.onFailedToFetchActiveViewers(responseBodyResponse.errorBody().string());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void broadcastViewer(String streamId, int offset, int limit) {
        isLoading = true;
        apiServices.viewers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, streamId,
                AppController.getInstance().getUserId(), offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<StreamViewersResponse>>() {
                    @Override
                    public void onNext(Response<StreamViewersResponse> response) {
                        switch (response.code()) {
                            case 200:
                                isLastPage = response.body().getData().getViewers().size() < PAGE_SIZE;
                                view.broadcastViewer(response.body().getData().getViewers());
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
    public void sendRestartMessage(String streamId) {
        JSONObject obj = new JSONObject();
        try {

            obj.put("streamID", streamId);
            obj.put("messageType", "0");

            mqttManager.publish(MqttEvents.ParticularStreamPresenceEvents.value + "/" + streamId, obj, 0,
                    false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                                sessionApiCall.getNewSession(sessionObserver);
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
                                sessionApiCall.getNewSession(sessionObserver);
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
    public void endStream() {
        apiServices.endStream(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

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
    public void streamStats(String streamId) {
        apiServices.getLiveStreamStats(AppController.getInstance().getApiToken(), Constants.LANGUAGE, streamId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<StreamStats>>() {
                    @Override
                    public void onNext(Response<StreamStats> response) {
                        if (response.code() == 200) {
                            view.showStats(response.body().getStats());
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
    public void callApiOnScroll(String streamId, int firstVisibleItemPosition, int visibleItemCount,
                                int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                page++;
                broadcastViewer(streamId, PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }

    @Override
    public void callMessageApiOnScroll(String streamId, int firstVisibleItemPosition,
                                       int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= MESSAGE_PAGE_SIZE) {
                message_page++;
                getLiveStreamsChatHistory(streamId, MESSAGE_PAGE_SIZE * message_page, MESSAGE_PAGE_SIZE);
            }
        }
    }

    @Override
    public void allStreamEventRxJAva() {
        allStreamsObserver = new Observer<AllStreamsData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AllStreamsData dataStream) {

                view.onAllStreamEventReceived(dataStream);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
        AllStreamsObservable.getInstance().subscribe(allStreamsObserver);
    }

    @Override
    public void clearObservables() {
        try {
            if (allStreamsObserver != null) {
                AllStreamsObservable.getInstance().removeObserver(allStreamsObserver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
