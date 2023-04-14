package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;

public class GiftEvent implements Serializable {

      /*{

            "streamID":"5ccd313d66023fa1d552dcd4",
            "id": "5ccd313d66fa1d552dcd4023",
            "name": "two",
            "image": "http://streamgift.karry.xyz/2.png",
            "gifs": "http://streamgift.karry.xyz/2.gif",
            "coin": "3",
            "message":"0",
            "userId": "5ccd2dcd4023313d66fa1d55",
            "userName":"Test User",
            "userImage":"https://admin.loademup.xyz/pics/user.jpg"


    }*/


    private String id;
    private String name;
    private String image;
    private String gifs;
    private String coin;
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getGifs() {
        return gifs;
    }

    public String getCoin() {
        return coin;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
