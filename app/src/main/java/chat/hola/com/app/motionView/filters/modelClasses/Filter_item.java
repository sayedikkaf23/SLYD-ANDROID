package chat.hola.com.app.motionView.filters.modelClasses;

/**
 * Created by moda on 18/05/17.
 */

public class Filter_item {
    /**
     * itemType-0-normal filter
     * 1-manage filter
     */
    private String itemType;
    private String filterName;
    private int filterId;

    private boolean selected;


    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }


}
