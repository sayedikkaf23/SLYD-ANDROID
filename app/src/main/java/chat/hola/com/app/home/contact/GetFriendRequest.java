package chat.hola.com.app.home.contact;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetFriendRequest implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data implements Serializable {
        @SerializedName("myFriendRequest")
        @Expose
        private List<Friend> friendRequests = null;

        @SerializedName("friendRequestBy")
        @Expose
        private List<Friend> sentFriendRequest = null;

        public List<Friend> getFriendRequests() {
            return friendRequests;
        }

        public void setFriendRequests(List<Friend> friendRequests) {
            this.friendRequests = friendRequests;
        }

        public List<Friend> getSentFriendRequest() {
            return sentFriendRequest;
        }

        public void setSentFriendRequest(List<Friend> sentFriendRequest) {
            this.sentFriendRequest = sentFriendRequest;
        }
    }
}
