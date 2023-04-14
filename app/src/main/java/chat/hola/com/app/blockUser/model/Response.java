package chat.hola.com.app.blockUser.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <h1>Response</h1>
 *
 * @author 3embed
 * @since 4/9/2018
 * @version 1.0
 */

public class Response {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<User> comments = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<User> getData() {
        return comments;
    }

    public void setData(List<User> data) {
        this.comments = data;
    }

}
