package chat.hola.com.app.home.activity.youTab.channelrequesters.model;

import java.io.Serializable;

/**
 * <h1></h1>
 *
 * @author DELL
 * @since 4/28/2018.
 */

public class DataList implements Serializable {

    private boolean isParent;

    //header
    private String channelId;
    private String channelName;
    private Boolean _private;
    private String channelImageUrl;
    private Double channelCreatedOn;

//    public DataList(String channelId,
//                    String channelName,
//                    Boolean _private,
//                    String channelImageUrl,
//                    Double channelCreatedOn,
//                    boolean isParent) {
//        this.channelId = channelId;
//        this.channelName = channelName;
//        this._private = _private;
//        this.channelImageUrl = channelImageUrl;
//        this.channelCreatedOn = channelCreatedOn;
//        this.isParent = isParent;
//
//    }

    //data
    private String userId;
    private String countryCode;
    private String number;
    private String profilePic;
    private String userName;
    private Long   requestedTimestamp;

//    public DataList(String channelId,String userId,
//                    String countryCode,
//                    String number,
//                    String profilePic,
//                    String userName,
//                    Long requestedTimestamp, boolean isParent) {
//        this.channelId=channelId;
//        this.userId = userId;
//        this.countryCode = countryCode;
//        this.number = number;
//        this.profilePic = profilePic;
//        this.userName = userName;
//        this.requestedTimestamp = requestedTimestamp;
//        this.isParent = isParent;
//
//    }


    public boolean isParent() {
        return isParent;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public Boolean get_private() {
        return _private;
    }

    public String getChannelImageUrl() {
        return channelImageUrl;
    }

    public Double getChannelCreatedOn() {
        return channelCreatedOn;
    }

    public String getUserId() {
        return userId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNumber() {
        return number;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public Long getRequestedTimestamp() {
        return requestedTimestamp;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void set_private(Boolean _private) {
        this._private = _private;
    }

    public void setChannelImageUrl(String channelImageUrl) {
        this.channelImageUrl = channelImageUrl;
    }

    public void setChannelCreatedOn(Double channelCreatedOn) {
        this.channelCreatedOn = channelCreatedOn;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRequestedTimestamp(Long requestedTimestamp) {
        this.requestedTimestamp = requestedTimestamp;
    }
}
