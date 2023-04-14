package chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer;

import android.os.Handler;
import android.util.Log;

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
import chat.hola.com.app.live_stream.Observable.ParticularStreamRestartEventObservable;
import chat.hola.com.app.live_stream.ResponcePojo.AllReceivedMessage;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.ResponcePojo.GiftEvent;
import chat.hola.com.app.live_stream.ResponcePojo.LikeEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;
import chat.hola.com.app.live_stream.ResponcePojo.StreamPresenceEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamRestartEvent;
import chat.hola.com.app.live_stream.gift.GiftDataResponse;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.pubsub.MqttEvents;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.GiftCategories;
import chat.hola.com.app.models.GiftResponse;
import chat.hola.com.app.models.SessionObserver;
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
 * Created by moda on 12/21/2018.
 */
public class LiveStreamPlayerPresenterImpl
        implements LiveStreamPresenterContract.LiveStreamPresenter {
    static final int MESSAGE_PAGE_SIZE = 50;
    private boolean isMessageLoading = false;
    private boolean isMessageLastPage = false;
    public static int message_page = 0;
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    @Inject
    LiveStreamService apiServices;
    @Inject
    HowdooService service;
    @Inject
    Gson gson;
    @Inject
    SessionManager manager;
    @Inject
    MQTTManager mqttManager;
    SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    LiveStreamPresenterContract.LiveStreamView view;

    @Inject
    public LiveStreamPlayerPresenterImpl() {
    }

    private Observer<AllStreamsData> allStreamsObserver;

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

                            switch (code) {
                                case 200:
                                    //                                    String responseError = responseBodyResponse.errorBody().string();
                                    //
                                    //                                    view.showFailedToPublishMessageAlert(
                                    //                                            new JSONObject(responseError).getString("message"));
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
                                                            publishChatMessage(streamId, msg);
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

                                default:

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
    public void getLiveStreamsChatHistory(String streamId, int offset, int limit) {
        isMessageLoading = true;
        //Observable<Response<ResponseBody>> observable = apiServices.getApiChat(AppController.getInstance().getApiToken()(), Constants.LANGUAGE,
        //        streamId, (System.currentTimeMillis() / 1000) );
        Observable<Response<AllReceivedMessage>> observable =
                apiServices.getApiChat(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                        streamId, "0", offset, limit);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<AllReceivedMessage>>() {
                    @Override
                    public void onNext(Response<AllReceivedMessage> responseBodyResponse) {
                        int code = responseBodyResponse.code();

                        String response;
                        try {
                            isMessageLoading = false;
                            switch (code) {
                                case 200:
                                    //                                    response = responseBodyResponse.body().string();
                                    //                                    AllReceivedMessage allReceivedMessage = gson.fromJson(response, AllReceivedMessage.class);
                                    isMessageLastPage =
                                            responseBodyResponse.body().getData().size() < MESSAGE_PAGE_SIZE;

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

                                    view.onAllPastChatMessagesReceived(messages);
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
                                                            getLiveStreamsChatHistory(streamId, offset, limit);
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
                                default:
                                    view.onFailedToFetchChatHistory(responseBodyResponse.errorBody().string());
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isMessageLoading = false;
                    }

                    @Override
                    public void onComplete() {
                        isMessageLoading = false;
                    }
                });
    }

    @Override
    public void subscribeToStream(String streamId, String action) {
        Map<String, Object> map = new HashMap<>();

        map.put("id", streamId);
        map.put("action", action);
        Observable<Response<ResponseBody>> observable =
                apiServices.postApiSubscribe(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                        map);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {

                        int code = responseBodyResponse.code();

                        try {
                            String response;
                            switch (code) {
                                case 200:
                                    response = responseBodyResponse.body().string();

                                    if (action.equals("stop")) {
                                        view.onStreamUnsubscribed();
                                    } else {
                                        JSONObject obj = new JSONObject(response);
                                        obj = obj.getJSONObject("data");
                                        if (obj.has("viewers")) {
                                            view.updateViewersCount(obj.getInt("viewers"));
                                        }
                                    }
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
                                                            subscribeToStream(streamId, action);
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
                                default:
                                    if (action.equals("stop")) {
                                        view.onStreamUnsubscribed();
                                    } else {
                                        view.onFailedToSubscribeStream(responseBodyResponse.errorBody().string());
                                    }
                                    break;
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
    public void connectView() {

    }

    @Override
    public void detachView() {

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
    public void streamPresenceEventRxJAva() {
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

                        giftEvent.setUserName("YOU");
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
    public void fetchGifts() {
    }

    @Override
    public void likeStream(String streamId) {

        publishLikeMessage(streamId);
        Map<String, Object> map = new HashMap<>();
        map.put("type", 0);
        map.put("message", "0");
        map.put("streamId", streamId);
        Observable<Response<GiftResponse>> observer =
                apiServices.sendLikeOrGift(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                        map);

        observer.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GiftResponse>>() {
                    @Override
                    public void onNext(Response<GiftResponse> response) {

                        int code = response.code();

                        try {

                            switch (code) {
                                case 200:
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
                                                            likeStream(streamId);
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
                                default:
                                    String responseError = response.errorBody().string();

                                    if (view != null) {
                                        view.onFailedToLike(new JSONObject(responseError).getString("message"));
                                    }
                                    break;
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
    public void sendGift(String streamId, GiftDataResponse.Data.Gift giftData, AllStreamsData streamsData) {
        Map<String, Object> map = new HashMap<>();
        map.put("senderId", AppController.getInstance().getUserId());
        map.put("senderName", manager.getFirstName() + " " + manager.getLastName());
        map.put("senderType", Constants.APP_TYPE);
        map.put("receiverId", streamsData.getUserId());
        map.put("receiverName", streamsData.getUserName());
        map.put("receiverType", Constants.APP_TYPE);
        map.put("giftId", giftData.getId());
        map.put("notes", "gift");
        map.put("description", "gift");
        map.put("streamId", streamId);
        Observable<Response<GiftResponse>> observer = service.sendGift(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map);
        observer.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GiftResponse>>() {
                    @Override
                    public void onNext(Response<GiftResponse> responseBodyResponse) {

                        int code = responseBodyResponse.code();
                        try {
                            view.hideLoader();
                            switch (code) {
                                case 200:
                                    publishGiftMessage(streamId, giftData);
                                    view.setCoinBalance(responseBodyResponse.body().getBalance());
                                    //view.startCoinAnimation(responseBodyResponse.body().getBalance());
                                    //                                    getWalletBalance();
                                    break;
                                case 403:
                                    view.insufficientBalance();
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
                                                            sendGift(streamId, giftData, streamsData);
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
                                default:
                                    String responseError = responseBodyResponse.errorBody().string();

                                    if (view != null) {
                                        view.onFailedToSendGift(new JSONObject(responseError).getString("message"));
                                    }
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void publishLikeMessage(String streamId) {

        JSONObject obj = new JSONObject();
        try {

            obj.put("streamID", streamId);
            obj.put("message", "0");
            obj.put("userId", manager.getUserId());
            obj.put("userName", manager.getFirstName());
            obj.put("userImage", "");

            mqttManager.publish(MqttEvents.ParticularStreamLikeEvent.value + "/" + streamId, obj, 1,
                    false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void publishGiftMessage(String streamId, GiftDataResponse.Data.Gift giftData) {

        JSONObject obj = new JSONObject();
        try {
            obj.put("streamID", streamId);
            obj.put("message", "0");
            obj.put("image", giftData.getMobileThumbnail());
            obj.put("gifs", giftData.getGifUrl());
            obj.put("name", giftData.getGiftTitle());
            obj.put("id", giftData.getId());
            obj.put("coin", giftData.getGiftCost());
            obj.put("userId", manager.getUserId());
            obj.put("userName", manager.getFirstName());
            obj.put("userImage", "");

            mqttManager.publish(MqttEvents.ParticularStreamGiftEvent.value + "/" + streamId, obj, 1,
                    false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                            switch (code) {
                                case 200:
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
                                                            getActiveViewers(streamId);
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
                                default:
                                    String responseError = responseBodyResponse.errorBody().string();

                                    if (view != null) {
                                        view.onFailedToFetchActiveViewers(
                                                new JSONObject(responseError).getString("message"));
                                    }
                                    break;
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
    public void streamRestartEventRxJAva() {
        Observer<StreamRestartEvent> observer = new Observer<StreamRestartEvent>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(StreamRestartEvent streamRestartEvent) {

                view.onStreamRestartEvent(streamRestartEvent);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        ParticularStreamRestartEventObservable.getInstance().subscribe(observer);
    }

    @Override
    public void getWalletBalance() {

    }

    @Override
    public void callMessageApiOnScroll(String streamId, int firstVisibleItemPosition,
                                       int visibleItemCount, int totalItemCount) {
        if (!isMessageLoading && !isMessageLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= MESSAGE_PAGE_SIZE) {
                message_page++;
                getLiveStreamsChatHistory(streamId, MESSAGE_PAGE_SIZE * message_page, MESSAGE_PAGE_SIZE);
            }
        }
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
    public void clearObservables() {
        try {
            if (allStreamsObserver != null) {
                AllStreamsObservable.getInstance().removeObserver(allStreamsObserver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void giftCategories() {
        apiServices.giftCategories(AppController.getInstance().getApiToken(), Constants.LANGUAGE, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GiftCategories>>() {
                    @Override
                    public void onNext(Response<GiftCategories> response) {
                        if (response.code() == 200) {
                            if (response.body() != null && response.body().getData() != null)
                                view.giftCategories(response.body().getData().getCategories());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Error", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void gifts(String categoryId) {

    }
}
