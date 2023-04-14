package chat.hola.com.app.home.model;

/**
 * Created by DELL on 4/5/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 2/28/2018.
 */

public class PostData implements Serializable {
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
    private String end;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("imageUrl1")
    @Expose
    private String imageUrl1;
    @SerializedName("thumbnailUrl1")
    @Expose
    private String thumbnailUrl1;
    @SerializedName("mediaType1")
    @Expose
    private Integer mediaType1;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("channelId")
    @Expose
    private String channelId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("likesCount")
    @Expose
    private String likesCount;
    @SerializedName("profilePic")
    @Expose
    private String profilepic;
    @SerializedName("userName")
    @Expose
    private String username;
    @SerializedName("hashTags")
    @Expose
    private List<String> hashTags = null;
    @SerializedName("imageUrl1Height")
    @Expose
    private String imageUrl1Height;
    @SerializedName("imageUrl1Width")
    @Expose
    private String imageUrl1Width;
    @SerializedName("totalComments")
    @Expose
    private String commentCount;
    @SerializedName("distinctViews")
    @Expose
    private String distinctViews;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("channelName")
    @Expose
    private String channelName;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("liked")
    @Expose
    private String liked;


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

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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

    public Integer getMediaType1() {
        return mediaType1;
    }

    public void setMediaType1(Integer mediaType1) {
        this.mediaType1 = mediaType1;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    public String getImageUrl1Height() {
        return imageUrl1Height;
    }

    public void setImageUrl1Height(String imageUrl1Height) {
        this.imageUrl1Height = imageUrl1Height;
    }

    public String getImageUrl1Width() {
        return imageUrl1Width;
    }

    public void setImageUrl1Width(String imageUrl1Width) {
        this.imageUrl1Width = imageUrl1Width;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getDistinctViews() {
        return distinctViews;
    }

    public void setDistinctViews(String distinctViews) {
        this.distinctViews = distinctViews;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }
}

