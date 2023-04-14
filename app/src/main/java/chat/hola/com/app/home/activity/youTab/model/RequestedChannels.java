package chat.hola.com.app.home.activity.youTab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 4/27/2018.
 */

public class RequestedChannels implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("channelName")
    @Expose
    private String channelName;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("channelImageUrl")
    @Expose
    private String channelImageUrl;
    @SerializedName("channelCreatedOn")
    @Expose
    private Double channelCreatedOn;
    @SerializedName("requestedUserList")
    @Expose
    private List<RequestedUserList> requestedUserList = null;

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

    public String getChannelImageUrl() {
        return channelImageUrl;
    }

    public void setChannelImageUrl(String channelImageUrl) {
        this.channelImageUrl = channelImageUrl;
    }

    public Double getChannelCreatedOn() {
        return channelCreatedOn;
    }

    public void setChannelCreatedOn(Double channelCreatedOn) {
        this.channelCreatedOn = channelCreatedOn;
    }

    public List<RequestedUserList> getRequestedUserList() {
        return requestedUserList;
    }

    public void setRequestedUserList(List<RequestedUserList> requestedUserList) {
        this.requestedUserList = requestedUserList;
    }
}
