package chat.hola.com.app.comment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>User</h1>
 *
 * @author 3embed
 * @since 4/9/2018
 * @version 1.0
 */

public class Comment implements Serializable{
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("imageUrl1")
    @Expose
    private String imageUrl1;
    @SerializedName("thumbnailUrl1")
    @Expose
    private String thumbnailUrl1;
    @SerializedName("commentCount")
    @Expose
    private Integer commentCount;
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("postedBy")
    @Expose
    private String postedBy;
    @SerializedName("commentedByUserId")
    @Expose
    private String commentedByUserId;
    @SerializedName("commentedOn")
    @Expose
    private String commentedOn;
    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("commentedByFirstName")
    @Expose
    private String commentedByFirstName;
    @SerializedName("commentedByLastName")
    @Expose
    private String commentedByLastName;
    @SerializedName("isLiked")
    @Expose
    private boolean isLiked;
    @SerializedName("likeCount")
    @Expose
    private Integer likeCount;
    @SerializedName("mentionedUsers")
    @Expose
    private List<String> mentionedUsers = null;
    @SerializedName("hashtags")
    @Expose
    private List<String> hashtags = null;
    @SerializedName("commentedBy")
    @Expose
    private String commentedBy;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("canDeleteComment")
    @Expose
    private boolean canDeleteComment;

    private boolean selected =false;

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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getCommentedByUserId() {
        return commentedByUserId;
    }

    public void setCommentedByUserId(String commentedByUserId) {
        this.commentedByUserId = commentedByUserId;
    }

    public String getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(String commentedOn) {
        this.commentedOn = commentedOn;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getMentionedUsers() {
        return mentionedUsers;
    }

    public void setMentionedUsers(List<String> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public String getCommentedByFirstName() {
        return commentedByFirstName;
    }

    public void setCommentedByFirstName(String commentedByFirstName) {
        this.commentedByFirstName = commentedByFirstName;
    }

    public String getCommentedByLastName() {
        return commentedByLastName;
    }

    public void setCommentedByLastName(String commentedByLastName) {
        this.commentedByLastName = commentedByLastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public boolean canDeleteComment() {
        return canDeleteComment;
    }

    public void setCanDeleteComment(boolean canDeleteComment) {
        this.canDeleteComment = canDeleteComment;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
