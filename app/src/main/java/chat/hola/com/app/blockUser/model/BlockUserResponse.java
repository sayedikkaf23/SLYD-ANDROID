package chat.hola.com.app.blockUser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <h1>BlockUserResponse</h1>
 *
 * @author 3embed
 * @since 4/9/2018
 * @version 1.0
 */

public class BlockUserResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private User comments;

    public User getComments() {
        return comments;
    }

    public void setComments(User comments) {
        this.comments = comments;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
