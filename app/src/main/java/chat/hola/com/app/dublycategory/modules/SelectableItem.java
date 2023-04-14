package chat.hola.com.app.dublycategory.modules;

import chat.hola.com.app.dubly.Dub;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 8/6/2018.
 */
public class SelectableItem extends Dub {
    private boolean isSelected = false;


    public SelectableItem(Dub item, boolean isSelected) {
        super(item.getId(),item.getName(),item.getDuration(),item.getPath(),item.getImageUrl());
        this.isSelected = isSelected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
