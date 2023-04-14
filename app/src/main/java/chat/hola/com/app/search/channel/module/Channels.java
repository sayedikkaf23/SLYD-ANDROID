package chat.hola.com.app.search.channel.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DELL on 4/17/2018.
 */

public class Channels implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("channelName")
    @Expose
    private String channelName;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("subscriber")
    @Expose
    private String subscriber;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("channelImageUrl")
    @Expose
    private String channelImageUrl;
    @SerializedName("subscribeStatus")
    @Expose
    private Integer subscribeStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelImageUrl() {
        return channelImageUrl;
    }

    public void setChannelImageUrl(String channelImageUrl) {
        this.channelImageUrl = channelImageUrl;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public Integer getSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(Integer subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }
}
