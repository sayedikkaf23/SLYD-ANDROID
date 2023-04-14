package chat.hola.com.app.home.stories.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>Viewer</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/29/2018.
 */

public class Viewer implements Serializable {
    @SerializedName(value = "userId", alternate = "_id")
    @Expose
    private String userId;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName(value = "profilePic", alternate = "userImage")
    @Expose
    private String profilePic;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName(value = "timestamp", alternate = "started")
    @Expose
    private String timestamp;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("following")
    @Expose
    private boolean following;
    @SerializedName("isPrivate")
    @Expose
    private boolean isPrivate;
    @SerializedName("status")
    @Expose
    private int followStatus;

    public boolean isFollowing() {
        return following;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
