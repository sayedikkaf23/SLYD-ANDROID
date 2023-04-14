package chat.hola.com.app.collections.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import chat.hola.com.app.home.model.Data;

public class CollectionData  implements Serializable {
    @SerializedName("collectionName")
    @Expose
    private String collectionName;
    @SerializedName("images")
    @Expose
    private List<String> images = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("coverImage")
    @Expose
    private String coverImage;
    @SerializedName("postIds")
    @Expose
    private List<String> postIds = null;
    @SerializedName("posts")
    @Expose
    private List<Data> posts = null;

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public List<String> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<String> postIds) {
        this.postIds = postIds;
    }

    public List<Data> getPosts() {
        return posts;
    }

    public void setPosts(List<Data> posts) {
        this.posts = posts;
    }
}
