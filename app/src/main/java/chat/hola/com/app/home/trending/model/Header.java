package chat.hola.com.app.home.trending.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>TrendingResponse</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 6/18/2018.
 */

public class Header implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("categoryActiveIconUrl")
    @Expose
    private String categoryActiveIconUrl;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("backgroundImage ")
    @Expose
    private String backgroundImage;

    private boolean isSelected =false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryActiveIconUrl() {
        return categoryActiveIconUrl;
    }

    public void setCategoryActiveIconUrl(String categoryActiveIconUrl) {
        this.categoryActiveIconUrl = categoryActiveIconUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
