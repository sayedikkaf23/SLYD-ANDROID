package chat.hola.com.app.home.contact;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Friend implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("socialStatus")
    @Expose
    private String status;
    @SerializedName("isStar")
    @Expose
    private boolean isStar;
    @SerializedName("starRequest")
    @Expose
    private StarData starData;
    @SerializedName("private")
    @Expose
    private Integer _private;
    @SerializedName("friendStatusCode")
    @Expose
    private Integer friendStatusCode;
    @SerializedName("followStatus")
    @Expose
    private Integer followStatus;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("isChatEnable")
    @Expose
    private boolean isChatEnable;

    private boolean isFriendRequest = false;
    private boolean isTitle = false;
    private String title = "#";
    private boolean isFollowing;
    private boolean isSent = false;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setIsTitle(boolean title) {
        isTitle = title;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    public StarData getStarData() {
        return starData;
    }

    public void setStarData(StarData starData) {
        this.starData = starData;
    }


    public Integer getPrivate() {
        return _private;
    }

    public void setPrivate(Integer _private) {
        this._private = _private;
    }

    public Integer getFriendStatusCode() {
        return friendStatusCode;
    }

    public void setFriendStatusCode(Integer friendStatusCode) {
        this.friendStatusCode = friendStatusCode;
    }

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public boolean isFriendRequest() {
        return isFriendRequest;
    }

    public void setFriendRequest(boolean friendRequest) {
        isFriendRequest = friendRequest;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isChatEnable() {
        return isChatEnable;
    }

    public void setChatEnable(boolean chatEnable) {
        isChatEnable = chatEnable;
    }
}
