package chat.hola.com.app.live_stream.MQtt.MQttMessages;

import com.google.gson.Gson;

import org.json.JSONObject;

import chat.hola.com.app.live_stream.Observable.ParticularStreamChatMessageObservable;
import chat.hola.com.app.live_stream.ResponcePojo.StreamChatMessage;

public class ParticularStreamChat {


    public void handleParticularStreamChatMessage(String jsonObject, Gson gson, String streamId) {

        try {
            String jsonData = new JSONObject(jsonObject).getJSONObject("data").toString();
            StreamChatMessage streamChatMessage = gson.fromJson(jsonData, StreamChatMessage.class);
            streamChatMessage.setStreamId(streamId);
            ParticularStreamChatMessageObservable.getInstance().emitStreamChatMessage(streamChatMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
