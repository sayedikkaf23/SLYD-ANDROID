package chat.hola.com.app.live_stream.MQtt.MQttMessages;

import com.google.gson.Gson;

import chat.hola.com.app.live_stream.Observable.GiftEventObservable;
import chat.hola.com.app.live_stream.ResponcePojo.GiftEvent;

public class ParticularStreamGift {
    public void handleParticularStreamGiftEvent(String jsonObject, Gson gson) {

        try {

            GiftEvent giftEvent = gson.fromJson(jsonObject, GiftEvent.class);

            GiftEventObservable.getInstance().emitGiftEvent(giftEvent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
