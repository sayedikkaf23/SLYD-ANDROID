package chat.hola.com.app.profileScreen.followers.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ankit on 20/3/18.
 */

public class Data {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("followee")
    @Expose
    private String followee;
    @SerializedName("follower")
    @Expose
    private String follower;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private Boolean end;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("followsBack")
    @Expose
    private Boolean followsBack;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("userName")
    @Expose
    private String username;
//    @SerializedName("businessProfile")
//    @Expose
//    private Boolean businessProfile;
    @SerializedName("isStar")
    @Expose
    private Boolean star = false;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("userStatus")
    @Expose
    private String userStatus;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("starUserKnownBy")
    @Expose
    private String starUserKnownBy;

    private boolean isFollowing;
    private boolean isSent = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFollowee() {
        return followee;
    }

    public void setFollowee(String followee) {
        this.followee = followee;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Boolean getFollowsBack() {
        return followsBack;
    }

    public void setFollowsBack(Boolean followsBack) {
        this.followsBack = followsBack;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public Boolean getBusinessProfile() {
//        return businessProfile;
//    }
//
//    public void setBusinessProfile(Boolean businessProfile) {
//        this.businessProfile = businessProfile;
//    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public Boolean getStar() {
        return star;
    }

    public void setStar(Boolean star) {
        this.star = star;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStarUserKnownBy() {
        return starUserKnownBy;
    }

    public void setStarUserKnownBy(String starUserKnownBy) {
        this.starUserKnownBy = starUserKnownBy;
    }
}
