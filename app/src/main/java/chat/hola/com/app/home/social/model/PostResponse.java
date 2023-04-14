package chat.hola.com.app.home.social.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>PostResponse</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 23/3/18.
 */

public class PostResponse implements Serializable {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private chat.hola.com.app.home.model.Data data = null;

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

    public chat.hola.com.app.home.model.Data getData() {
        return data;
    }

    public void setData(chat.hola.com.app.home.model.Data data) {
        this.data = data;
    }
}
