package chat.hola.com.app.profileScreen.discover.contact.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author 3Embed.
 * @since 02/03/18.
 */

public class Contact implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("localNumber")
    @Expose
    private String localNumber;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("firstName")
    @Expose
    private String firstName = "";
    @SerializedName("lastName")
    @Expose
    private String lastName = "";
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("follow")
    @Expose
    private Follow follow;
    @SerializedName("isAllFollow")
    @Expose
    private boolean isAllFollow;
    @SerializedName("private")
    @Expose
    private Integer _private;
    @SerializedName("followStatus")
    @Expose
    private Integer followStatus;
    @SerializedName("friendStatusCode")
    @Expose
    private Integer friendStatusCode;
    @SerializedName("isStar")
    @Expose
    private boolean isStar = false;
    @SerializedName("isInvite")
    @Expose
    private Boolean isInvite;

    public Boolean isInvite() {
        return isInvite;
    }

    public void setInvite(Boolean invite) {
        isInvite = invite;
    }

    private String fullName = "";
    private String title = "#";
    private boolean isTitle = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalNumber() {
        return localNumber;
    }

    public void setLocalNumber(String localNumber) {
        this.localNumber = localNumber;
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

    private int profileImg;
    private String profileName;
    private int isFollowing;

    public Contact() {
        fullName = firstName + " " + lastName;
    }

    public Contact(int profileImg, String profileName, int isFollowing) {
        this.profileImg = profileImg;
        this.profileName = profileName;
        this.isFollowing = isFollowing;
        this.fullName = firstName + " " + lastName;
    }

    public int getProfileImg() {
        return profileImg;
    }

    public String getProfileName() {
        return profileName;
    }

    @SerializedName("type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int isFollowing() {
        return isFollowing;
    }

    public void setFollowing(int following) {
        isFollowing = following;
    }

    public boolean isAllFollow() {
        return isAllFollow;
    }

    public void setAllFollow(boolean allFollow) {
        isAllFollow = allFollow;
    }

    public Integer get_private() {
        return _private;
    }

    public void set_private(Integer _private) {
        this._private = _private;
    }

    public Integer getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsTitle() {
        return isTitle;
    }

    public void setIsTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    public Integer getFriendStatusCode() {
        return friendStatusCode;
    }

    public void setFriendStatusCode(Integer friendStatusCode) {
        this.friendStatusCode = friendStatusCode;
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

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

//    public void setFullName() {
//        if (firstName != null && !firstName.isEmpty())
//            fullName = fullName + firstName;
//
//        if (lastName != null && !lastName.isEmpty())
//            fullName = fullName + " " + lastName;
//    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fname, String lname) {
        fullName = fname;
        if (lname != null && !lname.isEmpty())
            fullName = fullName + " " + lastName;
    }
}
