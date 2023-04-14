package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;

public class LikeEvent implements Serializable {



/*{

            "streamID":"5ccd313d66023fa1d552dcd4",
            "message":"0",
            "userId": "5ccd2dcd4023313d66fa1d55",
            "userName":"Test User",

 "userImage":"https://admin.loademup.xyz/pics/user.jpg"
    }*/


    private String message;
    private String streamID;
    private String userId;
    private String userName;

    public String getUserImage() {
        return userImage;
    }

    private String userImage;

    public String getMessage() {
        return message;
    }


    public String getStreamID() {
        return streamID;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }


}
