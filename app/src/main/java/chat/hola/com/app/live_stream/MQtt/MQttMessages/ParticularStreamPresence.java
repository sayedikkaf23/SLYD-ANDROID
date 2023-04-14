package chat.hola.com.app.live_stream.MQtt.MQttMessages;

import com.google.gson.Gson;

import org.json.JSONObject;

import chat.hola.com.app.live_stream.Observable.ParticularStreamPresenceEventObservable;
import chat.hola.com.app.live_stream.ResponcePojo.StreamPresenceEvent;

public class ParticularStreamPresence {

    public void handleParticularStreamPresenceEvent(String jsonObject, Gson gson,String streamId) {
        try {
            String jsonData = new JSONObject(jsonObject).getJSONObject("data").toString();
            StreamPresenceEvent streamPresenceEvent = gson.fromJson(jsonData, StreamPresenceEvent.class);

            streamPresenceEvent.setStreamId(streamId);
            ParticularStreamPresenceEventObservable.getInstance().emitStreamPresenceEvent(streamPresenceEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
