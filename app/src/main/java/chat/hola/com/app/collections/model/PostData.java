package chat.hola.com.app.collections.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostData implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("collectionName")
    @Expose
    private String collectionName;
    @SerializedName("thumbnailUrl1")
    @Expose
    private String thumbnailUrl;

    private boolean isSelected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl1) {
        this.thumbnailUrl = thumbnailUrl1;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
