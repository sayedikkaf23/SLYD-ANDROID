package chat.hola.com.app.post.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <h1>CategoryData</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 28/3/18
 */

public class CategoryData {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("createdOn")
    @Expose
    private String createdOn;
    @SerializedName("categoryActiveIconUrl")
    @Expose
    private String categoryActiveIconUrl;
    @SerializedName("categoryInactiveIconUrl")
    @Expose
    private String categoryInactiveIconUrl;

    private boolean isSelected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCategoryActiveIconUrl() {
        return categoryActiveIconUrl;
    }

    public void setCategoryActiveIconUrl(String categoryActiveIconUrl) {
        this.categoryActiveIconUrl = categoryActiveIconUrl;
    }

    public String getCategoryInactiveIconUrl() {
        return categoryInactiveIconUrl;
    }

    public void setCategoryInactiveIconUrl(String categoryInactiveIconUrl) {
        this.categoryInactiveIconUrl = categoryInactiveIconUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
