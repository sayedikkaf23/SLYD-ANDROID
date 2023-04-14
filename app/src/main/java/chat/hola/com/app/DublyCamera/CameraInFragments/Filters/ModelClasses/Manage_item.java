package chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ModelClasses;

/**
 * Created by moda on 22/05/17.
 */

public class Manage_item {


    private String filterName, filterImageUrl;

    private boolean isSelected;


    private int filterId;

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getFilterImageUrl() {
        return filterImageUrl;
    }

    public void setFilterImageUrl(String filterImageUrl) {
        this.filterImageUrl = filterImageUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
