package chat.hola.com.app.dubly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 7/16/2018.
 */

public class Dub implements Serializable {
    @SerializedName("_id")
    @Expose
     String id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("duration")
    @Expose
    String duration;
    @SerializedName("path")
    @Expose
    String path;
    @SerializedName("imageUrl")
    @Expose
    String imageUrl;
    @SerializedName("musicCategory_id")
    @Expose
    String musicCategoryId;
    @SerializedName("musicCategoryName")
    @Expose
    String musicCategoryName;
    @SerializedName("musicCategoryImageUrl")
    @Expose
    String musicCategoryImageUrl;
    @SerializedName("isFavourite")
    @Expose
    Integer myFavourite = 0;
    @SerializedName("totalVideos")
    @Expose
    Integer totalVideos = 0;
    @SerializedName("artist")
    @Expose
    String artist;

    boolean isPlaying;

    public Dub(String id, String name, String duration, String path, String imageUrl) {
        this.name = name;
        this.id = id;
        this.duration = duration;
        this.path = path;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public Integer isMyFavourite() {
        return myFavourite;
    }

    public void setMyFavourite(Integer myFavourite) {
        this.myFavourite = myFavourite;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getMusicCategoryId() {
        return musicCategoryId;
    }

    public void setMusicCategoryId(String musicCategoryId) {
        this.musicCategoryId = musicCategoryId;
    }

    public String getMusicCategoryName() {
        return musicCategoryName;
    }

    public void setMusicCategoryName(String musicCategoryName) {
        this.musicCategoryName = musicCategoryName;
    }

    public String getMusicCategoryImageUrl() {
        return musicCategoryImageUrl;
    }

    public void setMusicCategoryImageUrl(String musicCategoryImageUrl) {
        this.musicCategoryImageUrl = musicCategoryImageUrl;
    }

    public Integer getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(Integer totalVideos) {
        this.totalVideos = totalVideos;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
