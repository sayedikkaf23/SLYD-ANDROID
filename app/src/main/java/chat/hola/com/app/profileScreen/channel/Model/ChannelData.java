package chat.hola.com.app.profileScreen.channel.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import chat.hola.com.app.dubly.Dub;
import chat.hola.com.app.home.model.Data;

/**
 * Created by ankit on 31/3/18.
 */

public class ChannelData implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("channelName")
    @Expose
    private String channelName;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;

    @SerializedName("private")
    @Expose
    private Boolean _private = false;

    @SerializedName("isSubscribed")
    @Expose
    private Integer isSubscribed;

    @SerializedName("channelImageUrl")
    @Expose
    private String channelImageUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("subscriber")
    @Expose
    private Integer totalSubscribers;
    @SerializedName("musicData")
    @Expose
    private Dub musicData;


    public void setSubscribed(Integer subscribed) {
        isSubscribed = subscribed;
    }

    public Integer isSubscribed() {
        return isSubscribed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getTotalSubscribers() {
        return totalSubscribers;
    }

    public void setTotalSubscribers(Integer totalSubscribers) {
        this.totalSubscribers = totalSubscribers;
    }

    public Dub getMusicData() {
        return musicData;
    }

    public void setMusicData(Dub musicData) {
        this.musicData = musicData;
    }
}

