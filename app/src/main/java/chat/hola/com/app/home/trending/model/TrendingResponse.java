package chat.hola.com.app.home.trending.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import chat.hola.com.app.home.model.Data;

/**
 * <h1>TrendingResponse</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 6/18/2018.
 */

public class TrendingResponse implements Serializable {

    @SerializedName("hashTagImage")
    @Expose
    private String hashTagImage;
    @SerializedName("hashTags")
    @Expose
    private String hashTag;
    @SerializedName("totalPosts")
    @Expose
    private long totalPosts;
    @SerializedName("posts")
    @Expose
    private ArrayList<Data> data = null;

    public String getHashTagImage() {
        return hashTagImage;
    }

    public void setHashTagImage(String hashTagImage) {
        this.hashTagImage = hashTagImage;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public long getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(long totalPosts) {
        this.totalPosts = totalPosts;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

}
