package chat.hola.com.app.calling.video.janus;

import org.json.JSONObject;

interface TransactionCallbackSuccess {
    void success(JSONObject jo);
}

interface TransactionCallbackError {
    void error(JSONObject jo);
}

public class JanusTransaction {
    public String tid;
    public TransactionCallbackSuccess success;
    public TransactionCallbackError error;
}
