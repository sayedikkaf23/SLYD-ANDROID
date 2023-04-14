package chat.hola.com.app.home.stories.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>StoryResponse</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/26/2018
 */


public class StoryResponse implements Serializable {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<StoryData> data = null;

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

    public List<StoryData> getData() {
        return data;
    }

    public void setData(List<StoryData> data) {
        this.data = data;
    }
}
