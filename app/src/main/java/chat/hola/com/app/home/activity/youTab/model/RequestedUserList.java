package chat.hola.com.app.home.activity.youTab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DELL on 4/27/2018.
 */

public class RequestedUserList implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("channelId")
    @Expose
    private String channelId;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("requestedTimestamp")
    @Expose
    private Long requestedTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getRequestedTimestamp() {
        return requestedTimestamp;
    }

    public void setRequestedTimestamp(Long requestedTimestamp) {
        this.requestedTimestamp = requestedTimestamp;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
