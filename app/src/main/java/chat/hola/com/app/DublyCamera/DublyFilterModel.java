package chat.hola.com.app.DublyCamera;

public class DublyFilterModel {

    private FilterType filterType;
    private boolean isSelected;

    private String filterName;

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterId(FilterType filterType) {
        this.filterType = filterType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
}
