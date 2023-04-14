package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;

/**
 * Created by moda on 12/24/2018.
 */
public class StreamPresenceEvent implements Serializable {
    /*{"data":{"id":"5c1a5cd7b995d95f2f494ac2","name":"akbar","action":"subscribe","subscribersCount":1,
"activeViewwers":1}}
*/

    private String id;
    private String name;
    private String action;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    private String streamId;
    private int subscribersCount;

    private int activeViewwers;

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public int getSubscribersCount() {
        return subscribersCount;
    }

    public int getActiveViewwers() {
        return activeViewwers;
    }
}
