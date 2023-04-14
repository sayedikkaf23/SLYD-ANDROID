package chat.hola.com.app.home.activity.followingTab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import chat.hola.com.app.home.model.Data;

/**
 * @author 3Embed.
 * @since 28/2/18.
 */

public class Following {

    @SerializedName("_id")
    @Expose
    private String id;
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
    private String userName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("targetUserName")
    @Expose
    private String targetUserName;
    @SerializedName("targetProfilePic")
    @Expose
    private String targetProfilePic;
    @SerializedName("targetId")
    @Expose
    private String targetId;
    @SerializedName("postData")
    @Expose
    private Data data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public String getTargetProfilePic() {
        return targetProfilePic;
    }

    public void setTargetProfilePic(String targetProfilePic) {
        this.targetProfilePic = targetProfilePic;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
