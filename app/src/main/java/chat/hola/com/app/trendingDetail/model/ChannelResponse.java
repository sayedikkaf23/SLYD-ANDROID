package chat.hola.com.app.trendingDetail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import chat.hola.com.app.profileScreen.channel.Model.ChannelData;

/**
 * <h1>ChannelResponse</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 5/2/2018.
 */

public class ChannelResponse implements Serializable {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ChannelData data;

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

    public ChannelData getData() {
        return data;
    }

    public void setData(ChannelData data) {
        this.data = data;
    }
}
