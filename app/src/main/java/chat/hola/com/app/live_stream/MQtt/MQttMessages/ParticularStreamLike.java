package chat.hola.com.app.live_stream.MQtt.MQttMessages;

import com.google.gson.Gson;

import chat.hola.com.app.live_stream.Observable.LikeEventObservable;
import chat.hola.com.app.live_stream.ResponcePojo.LikeEvent;

public class ParticularStreamLike {
    public void handleParticularStreamLikeEvent(String jsonData, Gson gson) {

        try {

            LikeEvent likeEvent = gson.fromJson(jsonData, LikeEvent.class);

            LikeEventObservable.getInstance().emitLikeEvent(likeEvent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
