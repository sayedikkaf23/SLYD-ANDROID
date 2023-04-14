package chat.hola.com.app.post.model;

import java.util.ArrayList;

/**
 * Created by DELL on 3/26/2018.
 */

public class Post {
    private String id;
    private boolean story;
    private String hashTags;
    private String latitude;
    private String longitude;
    private String location;
    private String countrySname;
    private String city;
    private String title;
    private String pathForCloudinary;
    private String typeForCloudinary;
    private String channelId;
    private String categoryId;
    private String imageUrl1;
    private String thumbnailUrl1;
    private String hasAudio1;
    private Integer mediaType1;
    private String cloudinaryPublicId1;
    private String imageUrl1Width;
    private String imageUrl1Height;
    private String musicId;
    private boolean isDub = false;
    private ArrayList<String> files;
    private String audioFile;
    private String filterColor;
    private boolean privateStory;
    private String duration;
    private String caption;
    private boolean gallery;
    private String placeId;
    private String businessPostType;
    private Double businessPrice;
    private String businessUrl;
    private String businessCurrency;
    private String businessButtonText;
    private String businessButtonColor;
    protected Integer orientation;
    private String cityForPost;
    private String countryForPost;
    private Double latForPost;
    private Double longForPost;
    private boolean isPaid;
    private Double postAmount=0.00;


    public String getCityForPost() {
        return cityForPost;
    }

    public void setCityForPost(String cityForPost) {
        this.cityForPost = cityForPost;
    }

    public String getCountryForPost() {
        return countryForPost;
    }

    public void setCountryForPost(String countryForPost) {
        this.countryForPost = countryForPost;
    }

    public Double getLatForPost() {
        return latForPost;
    }

    public void setLatForPost(Double latForPost) {
        this.latForPost = latForPost;
    }

    public Double getLongForPost() {
        return longForPost;
    }

    public void setLongForPost(Double longForPost) {
        this.longForPost = longForPost;
    }

    public String getBusinessPostType() {
        return businessPostType;
    }

    public void setBusinessPostType(String businessPostType) {
        this.businessPostType = businessPostType;
    }

    public Double getBusinessPrice() {
        return businessPrice;
    }

    public void setBusinessPrice(Double businessPrice) {
        this.businessPrice = businessPrice;
    }

    public String getBusinessUrl() {
        return businessUrl;
    }

    public void setBusinessUrl(String businessUrl) {
        this.businessUrl = businessUrl;
    }

    public String getBusinessCurrency() {
        return businessCurrency;
    }

    public void setBusinessCurrency(String businessCurrency) {
        this.businessCurrency = businessCurrency;
    }

    public String getBusinessButtonText() {
        return businessButtonText;
    }

    public void setBusinessButtonText(String businessButtonText) {
        this.businessButtonText = businessButtonText;
    }

    public boolean isStory() {
        return story;
    }

    public void setStory(boolean story) {
        this.story = story;
    }

    public String getHashTags() {
        return hashTags;
    }

    public void setHashTags(String hashTags) {
        this.hashTags = hashTags;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getPathForCloudinary() {
        return pathForCloudinary;
    }

    public void setPathForCloudinary(String pathForCloudinary) {
        this.pathForCloudinary = pathForCloudinary;
    }

    public String getTypeForCloudinary() {
        return typeForCloudinary;
    }

    public void setTypeForCloudinary(String typeForCloudinary) {
        this.typeForCloudinary = typeForCloudinary;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getCategoryId() {
        return categoryId;
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

    public String getHasAudio1() {
        return hasAudio1;
    }

    public void setHasAudio1(String hasAudio1) {
        this.hasAudio1 = hasAudio1;
    }

    public Integer getMediaType1() {
        return mediaType1;
    }

    public void setMediaType1(Integer mediaType1) {
        this.mediaType1 = mediaType1;
    }

    public String getCloudinaryPublicId1() {
        return cloudinaryPublicId1;
    }

    public void setCloudinaryPublicId1(String cloudinaryPublicId1) {
        this.cloudinaryPublicId1 = cloudinaryPublicId1;
    }

    public String getImageUrl1Width() {
        return imageUrl1Width;
    }

    public void setImageUrl1Width(String imageUrl1Width) {
        this.imageUrl1Width = imageUrl1Width;
    }

    public String getImageUrl1Height() {
        return imageUrl1Height;
    }

    public void setImageUrl1Height(String imageUrl1Height) {
        this.imageUrl1Height = imageUrl1Height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public boolean isDub() {
        return isDub;
    }

    public void setDub(boolean dub) {
        isDub = dub;
    }


    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public String getFilterColor() {
        return filterColor;
    }

    public void setFilterColor(String filterColor) {
        this.filterColor = filterColor;
    }

    public boolean isPrivateStory() {
        return privateStory;
    }

    public void setPrivateStory(boolean privateStory) {
        this.privateStory = privateStory;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isGallery() {
        return gallery;
    }

    public void setGallery(boolean gallery) {
        this.gallery = gallery;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getBusinessButtonColor() {
        return businessButtonColor;
    }

    public void setBusinessButtonColor(String businessButtonColor) {
        this.businessButtonColor = businessButtonColor;
    }

    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    public Integer getOrientation() {
        return orientation;
    }

    private boolean allowComments,allowDownload,allowDuet;

    public boolean isAllowComments() {
        return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    public boolean isAllowDownload() {
        return allowDownload;
    }

    public void setAllowDownload(boolean allowDownload) {
        this.allowDownload = allowDownload;
    }

    public boolean isAllowDuet() {
        return allowDuet;
    }

    public void setAllowDuet(boolean allowDuet) {
        this.allowDuet = allowDuet;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Double getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(Double postAmount) {
        this.postAmount = postAmount;
    }
}
