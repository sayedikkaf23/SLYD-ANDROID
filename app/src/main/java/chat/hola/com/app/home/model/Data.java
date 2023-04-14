package chat.hola.com.app.home.model;

import android.annotation.SuppressLint;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.dubly.Dub;
import chat.hola.com.app.models.Business;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2/28/2018.
 */

@SuppressLint("ParcelCreator")
public class Data implements Serializable {
  @SerializedName("_id")
  @Expose
  private String id;
  @SerializedName("followee")
  @Expose
  private String followee;
  @SerializedName("followStatus")
  @Expose
  private Integer followStatus = 0;
  @SerializedName("follower")
  @Expose
  private String follower;
  @SerializedName("start")
  @Expose
  private String start;
  @SerializedName("end")
  @Expose
  private String end;
  //    @SerializedName("type")
  //    @Expose
  //    private Type type;
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
  private String userId="";
  @SerializedName("likesCount")
  @Expose
  private String likesCount;
  @SerializedName(value = "profilePic", alternate = "profilepic")
  @Expose
  private String profilepic = "";
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
  @SerializedName(value = "totalComments", alternate = "commentCount")
  @Expose
  private String commentCount = "0";
  @SerializedName("distinctViews")
  @Expose
  private String distinctViews = "0";
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
  private boolean liked = false;
  @SerializedName("channelImageUrl")
  @Expose
  private String channelImageUrl;
  @SerializedName("location")
  @Expose
  private Location location;
  @SerializedName("categoryUrl")
  @Expose
  private String categoryImageUrl;
  @SerializedName("firstName")
  @Expose
  private String firstName;
  @SerializedName("lastName")
  @Expose
  private String lastName;
  @SerializedName("isStar")
  @Expose
  private boolean star;
  @SerializedName("musicData")
  @Expose
  private Dub dub;
  @SerializedName("comments")
  @Expose
  private List<Comment> comments;

  @SerializedName("placeId")
  @Expose
  private String placeId;

  @SerializedName("business")
  @Expose
  private Business business;

  @SerializedName("isBookMarked")
  @Expose
  private boolean isBookMarked;

  @SerializedName("orientation")
  @Expose
  private Integer orientation = 0;
  @SerializedName("trending_score")
  @Expose
  private String trending_score;
  @SerializedName("productIds")
  @Expose
  private ArrayList<String> productIds;

  private boolean selected;
  //private boolean hideCover = false;

  //private boolean paused;
  //private long seekTime;

  public String getTrending_score() {
    return trending_score;
  }

  public void setTrending_score(String trending_score) {
    this.trending_score = trending_score;
  }

  public Business getBusiness() {
    return business;
  }

  public void setBusiness(Business business) {
    this.business = business;
  }

  //public boolean isHideCover() {
  //    return hideCover;
  //}

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

  //    public Type getType() {
  //        return type;
  //    }
  //
  //    public void setType(Type type) {
  //        this.type = type;
  //    }

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

  public boolean isLiked() {
    return liked;
  }

  public void setLiked(boolean liked) {
    this.liked = liked;
  }

  public String getChannelImageUrl() {
    return channelImageUrl;
  }

  public void setChannelImageUrl(String channelImageUrl) {
    this.channelImageUrl = channelImageUrl;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getCategoryImageUrl() {
    return categoryImageUrl;
  }

  public void setCategoryImageUrl(String categoryImageUrl) {
    this.categoryImageUrl = categoryImageUrl;
  }

  public Dub getDub() {
    return dub;
  }

  public void setDub(Dub dub) {
    this.dub = dub;
  }

  public Integer getFollowStatus() {
    return followStatus;
  }

  public void setFollowStatus(Integer followStatus) {
    this.followStatus = followStatus;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public boolean isSelected() {
    return selected;
  }

  //public boolean hideCover() {
  //    return hideCover;
  //}
  //
  //public void setHideCover(boolean hideCover) {
  //    this.hideCover = hideCover;
  //}

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public boolean isStar() {
    return star;
  }

  public void setStar(boolean star) {
    this.star = star;
  }

  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  public boolean getBookMarked() {
    return isBookMarked;
  }

  public void setBookMarked(boolean bookMarked) {
    isBookMarked = bookMarked;
  }

  public Integer getOrientation() {
    return orientation;
  }

  public void setOrientation(Integer orientation) {
    this.orientation = orientation;
  }

  //public boolean isPaused() {
  //  return paused;
  //}
  //
  //public void setPaused(boolean paused) {
  //  this.paused = paused;
  //}
  //
  //public long getSeekTime() {
  //  return seekTime;
  //}
  //
  //public void setSeekTime(long seekTime) {
  //  this.seekTime = seekTime;
  //}
  @SerializedName("allowComment")
  @Expose
  private Boolean allowComments;
  @SerializedName("allowDownload")
  @Expose
  private Boolean allowDownload;
  @SerializedName("allowDuet")
  @Expose
  private Boolean allowDuet;

  public boolean isAllowComments() {

    if (allowComments == null) {
      //For the older posts which doesn't have this tag
      return true;
    } else {
      return allowComments;
    }
  }

  public boolean isAllowDownload() {
    if (allowDownload == null) {
      //For the older posts which doesn't have this tag
      return true;
    } else {
      return allowDownload;
    }
  }

  public Boolean isAllowDuet() {

    if (allowDuet == null) {
      //For the older posts which doesn't have this tag
      return true;
    } else {
      return allowDuet;
    }
  }

  public void setAllowComments(Boolean allowComments) {
    this.allowComments = allowComments;
  }

  public void setAllowDownload(Boolean allowDownload) {
    this.allowDownload = allowDownload;
  }

  public void setAllowDuet(Boolean allowDuet) {
    this.allowDuet = allowDuet;
  }

  public ArrayList<String> getProductIds() {
    return productIds;
  }

  public void setProductIds(ArrayList<String> productIds) {
    this.productIds = productIds;
  }

  @SerializedName("isPrivate")
  @Expose
  private Integer isPrivateUser;
  @SerializedName("isPaid")
  @Expose
  private Boolean isPaid= false;
  @SerializedName("isPurchased")
  @Expose
  private Boolean isPurchased = true;
  @SerializedName("postAmount")
  @Expose
  private String postAmount;
  @SerializedName("subscriptionAmount")
  @Expose
  private Double subscriptionAmount;
  @SerializedName("TipsAmount")
  @Expose
  private Double TipsAmount;

  public Integer isPrivateUser() {
    return isPrivateUser;
  }

  public Boolean getPaid() {
    return isPaid;
  }

  public Boolean getPurchased() {
    return isPurchased;
  }

  public void setPurchased(Boolean purchased) {
    isPurchased = purchased;
  }

  public void setPostAmount(String postAmount) {
    this.postAmount = postAmount;
  }

  public String getPostAmount() {
    return postAmount;
  }

  public void setSubscriptionAmount(Double subscriptionAmount) {
    this.subscriptionAmount = subscriptionAmount;
  }

  public Double getSubscriptionAmount() {
    return subscriptionAmount;
  }

  public void setTipsAmount(Double tipsAmount) {
    TipsAmount = tipsAmount;
  }

  public Double getTipsAmount() {
    return TipsAmount;
  }

  private boolean videoMuted;

  public boolean isVideoMuted() {
    return videoMuted;
  }

  public void setVideoMuted(boolean videoMuted) {
    this.videoMuted = videoMuted;
  }

  @SerializedName("alreadyReported")
  @Expose
  private Boolean alreadyReported = false;

  public Boolean getAlreadyReported() {
    return alreadyReported;
  }

  public void setAlreadyReported(Boolean alreadyReported) {
    this.alreadyReported = alreadyReported;
  }
}


