package chat.hola.com.app.profileScreen.discover.contact.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>FollowResponse</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 5/2/2018.
 */

public class FollowResponse implements Serializable {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("isAllFollow")
    @Expose
    private Boolean isAllFollow;
    @SerializedName("followStatus")
    @Expose
    private Integer status;
    @SerializedName("isPrivate")
    @Expose
    private Integer isPrivate;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsAllFollow() {
        return isAllFollow;
    }

    public void setIsAllFollow(Boolean isAllFollow) {
        this.isAllFollow = isAllFollow;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
