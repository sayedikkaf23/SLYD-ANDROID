package chat.hola.com.app.live_stream.ResponcePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by moda on 2/27/2019.
 */
public class AllReceivedMessage implements Serializable {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    ArrayList<StreamChatMessage> data;

    public ArrayList<StreamChatMessage> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
