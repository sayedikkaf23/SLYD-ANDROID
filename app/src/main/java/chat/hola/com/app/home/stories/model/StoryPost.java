package chat.hola.com.app.home.stories.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>StoriesPresenter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/26/2018
 */

public class StoryPost implements Serializable {
    @SerializedName("storyId")
    @Expose
    private String storyId;
    @SerializedName("streamId")
    @Expose
    private String streamId;
    @SerializedName("type")
    @Expose
    private String type = "2"; //2= video, 1= image
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName(value = "urlPath", alternate = "video")
    @Expose
    private String urlPath;
    @SerializedName("isPrivate")
    @Expose
    private Boolean isPrivate;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("uniqueViewCount")
    @Expose
    private String uniqueViewCount="0";
    @SerializedName("viewed")
    @Expose
    private boolean viewed;
    @SerializedName("viewdList")
    @Expose
    private List<Viewer> viewerList;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName(("backgroundColor"))
    @Expose
    private String backgroundColor;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("fontType")
    @Expose
    private String fontType;
    @SerializedName("caption")
    @Expose
    private String caption;

    private boolean success;
    private boolean isMine = false;

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUniqueViewCount() {
        return uniqueViewCount;
    }

    public void setUniqueViewCount(String uniqueViewCount) {
        this.uniqueViewCount = uniqueViewCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }


    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public List<Viewer> getViewerList() {
        return viewerList;
    }

    public void setViewerList(List<Viewer> viewerList) {
        this.viewerList = viewerList;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getFontType() {
        return fontType;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }
}
