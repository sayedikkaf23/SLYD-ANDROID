package chat.hola.com.app.profileScreen.discover.facebook.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Follow;

/**
 * Created by ankit on 27/3/18.
 */

public class Data {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("follow")
    @Expose
    private Follow follow;

    private boolean isFollowing =false;

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Follow getFollow() {
        return follow;
    }

    public void setFollow(Follow follow) {
        this.follow = follow;
    }
}
