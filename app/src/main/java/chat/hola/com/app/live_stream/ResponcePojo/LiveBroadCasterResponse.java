package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;

/**
 * Created by moda on 12/3/2018.
 */
public class LiveBroadCasterResponse implements Serializable
{
    /*{"message":"Success.","data":{"streams":[]}}*/

    private String message;
    private LiveBroadCasterData data;

    public String getMessage() {
        return message;
    }

    public LiveBroadCasterData getData() {
        return data;
    }
}
