package chat.hola.com.app.live_stream.MQtt.MQttMessages;


import com.google.gson.Gson;

import chat.hola.com.app.live_stream.Observable.ParticularStreamRestartEventObservable;
import chat.hola.com.app.live_stream.ResponcePojo.StreamRestartEvent;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 02 August 2019
 */
public class ParticularStreamRestart {

    public void handleParticularStreamRestartEvent(String jsonObject, Gson gson) {
        try {

            try {

                StreamRestartEvent streamRestartEvent = gson.fromJson(jsonObject, StreamRestartEvent.class);

                ParticularStreamRestartEventObservable.getInstance().emitStreamRestartEvent(streamRestartEvent);

            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}