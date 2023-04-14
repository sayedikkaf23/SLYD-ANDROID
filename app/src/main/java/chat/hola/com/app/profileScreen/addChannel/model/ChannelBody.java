package chat.hola.com.app.profileScreen.addChannel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ankit on 17/3/18.
 */

public class ChannelBody {
    @Expose
    @SerializedName("channelId")
    String channelId;

    @Expose
    @SerializedName("channelImageUrl")
    String ChannelPhotoUrl;

    @Expose
    @SerializedName("channelName")
    String ChannelName;

    @Expose
    @SerializedName("description")
    String ChannelDesc;

    @Expose
    @SerializedName("isPrivate")
    Boolean isPrivate;

    @Expose
    @SerializedName("categoryId")
    String CategoryId;

    @Expose
    @SerializedName("private")
    private Boolean _private;

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setChannelPhotoUrl(String channelPhotoUrl) {
        ChannelPhotoUrl = channelPhotoUrl;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public void setChannelDesc(String channelDesc) {
        ChannelDesc = channelDesc;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Boolean get_private() {
        return _private;
    }

    public void set_private(Boolean _private) {
        this._private = _private;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelPhotoUrl() {
        return ChannelPhotoUrl;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public String getChannelDesc() {
        return ChannelDesc;
    }

    public String getCategoryId() {
        return CategoryId;
    }
}
