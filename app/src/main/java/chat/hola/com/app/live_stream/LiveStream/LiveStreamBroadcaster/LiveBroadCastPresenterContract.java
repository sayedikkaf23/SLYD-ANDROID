package chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.home.stories.model.Viewer;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.ResponcePojo.GiftEvent;
import chat.hola.com.app.live_stream.ResponcePojo.LikeEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;
import chat.hola.com.app.live_stream.ResponcePojo.StreamPresenceEvent;
import chat.hola.com.app.live_stream.utility.BasePresenter;
import chat.hola.com.app.live_stream.utility.BaseViewImpl;
import chat.hola.com.app.models.StreamStats;

/**
 * Created by moda on 12/3/2018.
 */
public interface LiveBroadCastPresenterContract {

  interface LiveBroadCastPresenter extends BasePresenter {
    void callApiOnScroll(String streamId, int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    void callMessageApiOnScroll(String streamId, int firstVisibleItemPosition, int visibleItemCount,
        int totalItemCount);

    void subscribeStream(String streamId, String streamType, String thumbnail, String streamName,
        boolean saveStream, long mElapsedTime, String country, String city, String place,
        String lat, String lng, int width, int height);

    void publishChatMessage(String streamId, String msg);

    void streamChatMessageRxJava();

    void streamPresenceEventRxJava();

    void likeEventRxJAva();

    void giftEventRxJAva();

    void sendRestartMessage(String streamId);

    void getLiveStreamsChatHistory(String streamId, int offset, int limit);

    void getActiveViewers(String streamId);

    void broadcastViewer(String streamId, int offset, int limit);

    void follow(String userId);

    void unfollow(String userId);

    void endStream();

    void streamStats(String streamId);

    void allStreamEventRxJAva();

    void clearObservables();
  }

  interface LiveBroadCastView extends BaseViewImpl {

    void onStreamSubscribed(String streamId);

    void onFailedToSubscribeStream(String message);

    void onFailedToFetchChatHistory(String message);

    void onAllPastChatMessagesReceived(ArrayList<StreamChatMessage> data);

    void onActiveViewersReceived(int viewers);

    void onFailedToFetchActiveViewers(String message);

    void onStreamChatMessageReceived(StreamChatMessage message);

    void onStreamPresenceEvent(StreamPresenceEvent streamPresenceEvent);

    void showFailedToPublishMessageAlert(String message);

    void onLikeEvent(LikeEvent likeEvent);

    void onGiftEvent(GiftEvent giftEvent);

    void broadcastViewer(List<Viewer> viewers);

    void showStats(StreamStats.Stats stats);

    void onAllStreamEventReceived(AllStreamsData streamData);

    void streamOffline();
  }
}
