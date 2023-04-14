package chat.hola.com.app.search.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import chat.hola.com.app.home.contact.StarData;

/**
 * Created by DELL on 3/7/2018.
 */

public class SearchData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("registeredOn")
    @Expose
    private String registeredOn;
    @SerializedName("private")
    @Expose
    private Integer _private;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("businessProfile")
    @Expose
    private Boolean businessProfile;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("followStatus")
    @Expose
    private Integer followStatus;
    @SerializedName("friendStatusCode")
    @Expose
    private Integer friendStatusCode;
    @SerializedName("isStar")
    @Expose
    private boolean isStar;
    @SerializedName("starRequest")
    @Expose
    private StarData starData;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public Integer getPrivate() {
        return _private;
    }

    public void setPrivate(Integer _private) {
        this._private = _private;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getBusinessProfile() {
        return businessProfile;
    }

    public void setBusinessProfile(Boolean businessProfile) {
        this.businessProfile = businessProfile;
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

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public Integer getFriendStatusCode() {
        return friendStatusCode;
    }

    public void setFriendStatusCode(Integer friendStatusCode) {
        this.friendStatusCode = friendStatusCode;
    }
}
