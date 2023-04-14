package chat.hola.com.app.home.activity.youTab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import chat.hola.com.app.home.model.PostData;

/**
 * Created by ankit on 5/4/18.
 */

public class Data {

    @SerializedName("postData")
    @Expose
    private PostData postData;
    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userName")
    @Expose
    private String username;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("am_I_Following")
    private Boolean amIFollowing = false;

    public void setAmIFollowing(Boolean amIFollowing) {
        this.amIFollowing = amIFollowing;
    }

    public Boolean getAmIFollowing() {
        return amIFollowing;
    }

    public PostData getPostData() {
        return postData;
    }

    public void setPostData(PostData postData) {
        this.postData = postData;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}