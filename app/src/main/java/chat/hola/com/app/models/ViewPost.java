package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ViewPost implements Serializable {
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("postId")
    @Expose
    private String postId;
    @SerializedName("postType")
    @Expose
    private String postType;
    @SerializedName("videoDuration")
    @Expose
    private String videoDuration;
    @SerializedName("viewerId")
    @Expose
    private String viewerId;
    @SerializedName("watchedDuration")
    @Expose
    private String watchedDuration;

    public ViewPost(String city, String country, String latitude, String longitude, String postId, String postType, String videoDuration, String viewerId, String watchedDuration) {
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postId = postId;
        this.postType = postType;
        this.videoDuration = videoDuration;
        this.viewerId = viewerId;
        this.watchedDuration = watchedDuration;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}