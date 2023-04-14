package chat.hola.com.app.socialDetail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import chat.hola.com.app.profileScreen.model.Follow;

/**
 * <h1>Data</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 23/3/18.
 */

public class Data implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("imageUrl1")
    @Expose
    private String imageUrl1;
    @SerializedName("thumbnailUrl1")
    @Expose
    private String thumbnailUrl1;
    @SerializedName("mediaType1")
    @Expose
    private String mediaType1;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("countrySname")
    @Expose
    private String countrySname;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("channelId")
    @Expose
    private String channelId;
    @SerializedName("userPhoneNumber")
    @Expose
    private String userPhoneNumber;
    @SerializedName("registeredOn")
    @Expose
    private String registeredOn;
    @SerializedName("postedBy")
    @Expose
    private String postedBy;
    @SerializedName("distinctViews")
    @Expose
    private String distinctViews;
    @SerializedName("commentData")
    @Expose
    private List<List<String>> commentData = null;
    @SerializedName("channelName")
    @Expose
    private String channelName;
    @SerializedName("channelImageUrl")
    @Expose
    private String channelImageUrl;
    @SerializedName("followData")
    @Expose
    private List<Follow> followData = null;
    @SerializedName("hashTags")
    @Expose
    private List<String> hashTags = null;
    @SerializedName("userProfilePic")
    @Expose
    private String profilePic;
    @SerializedName("totalLikes")
    @Expose
    private Integer likeCounts = 0;
    @SerializedName("totalComments")
    @Expose
    private Integer totalComments = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public String getThumbnailUrl1() {
        return thumbnailUrl1;
    }

    public void setThumbnailUrl1(String thumbnailUrl1) {
        this.thumbnailUrl1 = thumbnailUrl1;
    }

    public String getMediaType1() {
        return mediaType1;
    }

    public void setMediaType1(String mediaType1) {
        this.mediaType1 = mediaType1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCountrySname() {
        return countrySname;
    }

    public void setCountrySname(String countrySname) {
        this.countrySname = countrySname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getDistinctViews() {
        return distinctViews;
    }

    public void setDistinctViews(String distinctViews) {
        this.distinctViews = distinctViews;
    }

    public List<List<String>> getCommentData() {
        return commentData;
    }

    public void setCommentData(List<List<String>> commentData) {
        this.commentData = commentData;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelImageUrl() {
        return channelImageUrl;
    }

    public void setChannelImageUrl(String channelImageUrl) {
        this.channelImageUrl = channelImageUrl;
    }

    public List<Follow> getFollowData() {
        return followData;
    }

    public void setFollowData(List<Follow> followData) {
        this.followData = followData;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Integer getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(Integer likeCounts) {
        this.likeCounts = likeCounts;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }
}
