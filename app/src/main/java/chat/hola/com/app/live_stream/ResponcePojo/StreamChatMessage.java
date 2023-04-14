package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;

/**
 * Created by moda on 12/24/2018.
 */
public class StreamChatMessage implements Serializable {
 /*{"data":{"message":"Hi how are you",
"userId":"5c1a5cd7b995d95f2f494ac2","userName":"akbar","userImage":""}}*/

    private String message, userId, userName, userImage, streamId;

    public StreamChatMessage(String message, String userId, String userName, String userImage) {
        this.message = message;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
    }

    public String getMessage() {
        return message;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }
}
