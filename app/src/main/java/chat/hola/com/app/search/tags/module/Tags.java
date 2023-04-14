package chat.hola.com.app.search.tags.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by DELL on 4/17/2018.
 */

public class Tags implements Serializable {
    @SerializedName("hashTags")
    @Expose
    private String hashTags;
    @SerializedName("totalPublicPosts")
    @Expose
    private Integer totalPublicPosts;
    @SerializedName("image")
    @Expose
    private String image;

    public String getHashTags() {
        return hashTags;
    }

    public void setHashTags(String hashTags) {
        this.hashTags = hashTags;
    }

    public Integer getTotalPublicPosts() {
        return totalPublicPosts;
    }

    public void setTotalPublicPosts(Integer totalPublicPosts) {
        this.totalPublicPosts = totalPublicPosts;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
