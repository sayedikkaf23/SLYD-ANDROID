package chat.hola.com.app.home.stories.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>StoryData</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/26/2018
 */

public class StoryData implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName(value = "posts", alternate = "data")
    @Expose
    private List<StoryPost> posts = null;
    @SerializedName("viewedAll")
    @Expose
    private boolean viewedAll;


    private boolean isHeader = false;
    private String headerTitle;
    private boolean isMine = false;

    private boolean isSuccess;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<StoryPost> getPosts() {
        return posts;
    }

    public void setPosts(List<StoryPost> posts) {
        this.posts = posts;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public boolean isViewedAll() {
        return viewedAll;
    }

    public void setViewedAll(boolean viewedAll) {
        this.viewedAll = viewedAll;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
