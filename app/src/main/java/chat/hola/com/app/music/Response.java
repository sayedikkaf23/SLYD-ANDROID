package chat.hola.com.app.music;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import chat.hola.com.app.dubly.Dub;
import chat.hola.com.app.home.model.Data;

public class Response implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("totalPosts")
    @Expose
    private Long totalPosts;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("musicData")
    @Expose
    private Dub musicData;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Dub getMusicData() {
        return musicData;
    }

    public void setMusicData(Dub musicData) {
        this.musicData = musicData;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(Long totalPosts) {
        this.totalPosts = totalPosts;
    }
}
