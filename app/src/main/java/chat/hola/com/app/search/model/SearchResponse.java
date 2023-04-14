package chat.hola.com.app.search.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import chat.hola.com.app.home.contact.Friend;

/**
 * Created by DELL on 3/7/2018.
 */

public class SearchResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Friend> data = null;

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

    public List<Friend> getData() {
        return data;
    }

    public void setData(List<Friend> data) {
        this.data = data;
    }
}
