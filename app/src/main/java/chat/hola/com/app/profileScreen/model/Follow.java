package chat.hola.com.app.profileScreen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 3/7/2018.
 */

public class Follow {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("followee")
    @Expose
    private String followee;
    @SerializedName("follower")
    @Expose
    private String follower;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private Boolean end;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("followsBack")
    @Expose
    private Boolean followsBack;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFollowee() {
        return followee;
    }

    public void setFollowee(String followee) {
        this.followee = followee;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Boolean getFollowsBack() {
        return followsBack;
    }

    public void setFollowsBack(Boolean followsBack) {
        this.followsBack = followsBack;
    }
}
