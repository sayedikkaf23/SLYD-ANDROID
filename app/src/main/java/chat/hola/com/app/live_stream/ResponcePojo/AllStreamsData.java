package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;

/**
 * Created by moda on 12/19/2018.
 */
public class AllStreamsData implements Serializable {
    /*"id":"5c1796e2907f1a5e39471b7f",
"name":"rtmpstream-1545049826518",
"userId":"5c04f70f34ea6318eb6c420a",
"action":"start",
"userName":"mahi",
"userImage":"",
"thumbnail":"/storage/emulated/0/YourDirectName/1545049826535.jpg",
"started":1545049826,
"viewers":0,
"allViewers":0*/

    private String streamId, id, name, userId, action, userName, userImage, thumbnail, viewers, allViewers, streamName, message;
    private long started;
    private boolean following, isPrivate;
    private int followStatus;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public long getStarted() {
        return started;
    }

    public String getViewers() {
        return viewers;
    }

    public String getAllViewers() {
        return allViewers;
    }

    public boolean isFollowing() {
        return following;
    }

    public String getMessage() {
        return message;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setViewers(String viewers) {
        this.viewers = viewers;
    }

    public void setAllViewers(String allViewers) {
        this.allViewers = allViewers;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStarted(long started) {
        this.started = started;
    }
}
