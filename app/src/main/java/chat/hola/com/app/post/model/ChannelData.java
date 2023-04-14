package chat.hola.com.app.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 3/6/2018.
 */

public class ChannelData {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("channelName")
    @Expose
    private String channelName;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("channelImageUrl")
    @Expose
    private String channelImageUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    private boolean isChecked = false;

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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
