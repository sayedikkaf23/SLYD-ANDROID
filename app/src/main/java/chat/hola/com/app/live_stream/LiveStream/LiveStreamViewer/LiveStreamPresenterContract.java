package chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.home.stories.model.Viewer;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.ResponcePojo.GiftEvent;
import chat.hola.com.app.live_stream.ResponcePojo.LikeEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;
import chat.hola.com.app.live_stream.ResponcePojo.StreamPresenceEvent;
import chat.hola.com.app.live_stream.ResponcePojo.StreamRestartEvent;
import chat.hola.com.app.live_stream.gift.GiftDataResponse;
import chat.hola.com.app.live_stream.utility.BasePresenter;
import chat.hola.com.app.live_stream.utility.BaseViewImpl;
import chat.hola.com.app.models.GiftCategories;
import chat.hola.com.app.models.WalletResponse;

/**
 * Created by moda on 12/21/2018.
 */
public interface LiveStreamPresenterContract {
    interface LiveStreamPresenter extends BasePresenter {

        void callApiOnScroll(String streamId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void callMessageApiOnScroll(String streamId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void publishChatMessage(String streamId, String msg);

        void getLiveStreamsChatHistory(String streamId, int offset, int limit);

        void getActiveViewers(String streamId);

        void subscribeToStream(String streamId, String action);

        void getWalletBalance();

        void streamChatMessageRxJava();

        void allStreamEventRxJAva();

        void streamPresenceEventRxJAva();

        void streamRestartEventRxJAva();

        void fetchGifts();

        void likeEventRxJAva();

        void giftEventRxJAva();

        void likeStream(String streamId);

        void sendGift(String streamId, GiftDataResponse.Data.Gift giftData, AllStreamsData streamsData);

        void follow(String userId);

        void unfollow(String userId);

        void broadcastViewer(String streamId, int offset, int limit);

        void clearObservables();

        void giftCategories();

        void gifts(String categoryId);
    }

    interface LiveStreamView extends BaseViewImpl {

        void onStreamUnsubscribed();

        void onFailedToSubscribeStream(String message);

        void onAllPastChatMessagesReceived(ArrayList<StreamChatMessage> data);

        void onStreamPresenceEvent(StreamPresenceEvent streamPresenceEvent);

        void onStreamRestartEvent(StreamRestartEvent streamRestartEvent);

        void showFailedToPublishMessageAlert(String message);

        void onStreamChatMessageReceived(StreamChatMessage messages);

        void onAllStreamEventReceived(AllStreamsData streamData);

        void onFailedToFetchChatHistory(String message);

        void onFailedToLike(String message);

        void onFailedToSendGift(String message);

        void onLikeEvent(LikeEvent likeEvent);

        void onGiftEvent(GiftEvent giftEvent);

        void updateViewersCount(int count);


        void onActiveViewersReceived(int viewers);

        void onFailedToFetchActiveViewers(String message);

        void insufficientBalance();

        void showBalance(WalletResponse data);

        void startCoinAnimation(String coin);

        void hideLoader();

        void broadcastViewer(List<Viewer> viewers);

        void streamOffline();

        void giftCategories(List<GiftCategories.Data.Category> categories);

        void setCoinBalance(String balance);
    }
}
