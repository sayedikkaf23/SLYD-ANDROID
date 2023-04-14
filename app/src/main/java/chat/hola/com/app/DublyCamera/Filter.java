package chat.hola.com.app.DublyCamera;

public class Filter {

    private int filterImage;
    private String filterColor;
    private boolean selected;

    public Filter(int filterImage, String filterColor, boolean selected) {
        this.filterImage = filterImage;
        this.filterColor = filterColor;
        this.selected = selected;
    }

    public int getFilterImage() {
        return filterImage;
    }

    public void setFilterImage(int filterImage) {
        this.filterImage = filterImage;
    }

    public String getFilterColor() {
        return filterColor;
    }

    public void setFilterColor(String filterColor) {
        this.filterColor = filterColor;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
