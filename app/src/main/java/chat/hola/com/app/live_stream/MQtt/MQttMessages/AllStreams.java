package chat.hola.com.app.live_stream.MQtt.MQttMessages;

import com.google.gson.Gson;

import org.json.JSONObject;

import chat.hola.com.app.live_stream.Observable.AllStreamsObservable;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;

public class AllStreams {

    public void handleAllStreamsMessage(String jsonObject, Gson gson) {

        try {

                String jsonData = new JSONObject(jsonObject).getJSONObject("data").toString();
                AllStreamsData dataStreams = gson.fromJson(jsonData, AllStreamsData.class);
                AllStreamsObservable.getInstance().emitAllStreamsData(dataStreams);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
