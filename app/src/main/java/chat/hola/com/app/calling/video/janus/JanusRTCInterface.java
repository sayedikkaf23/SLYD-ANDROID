package chat.hola.com.app.calling.video.janus;


import org.json.JSONObject;

import java.math.BigInteger;

public interface JanusRTCInterface {

    void onPublisherJoined(BigInteger handleId);
    void onPublisherRemoteJsep(BigInteger handleId, JSONObject jsep);
    void subscriberHandleRemoteJsep(BigInteger handleId, JSONObject jsep);
    void onLeaving(BigInteger handleId);

}
