package chat.hola.com.app.dublycategory.modules;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 14 August 2019
 */
public class CategoriesCombo implements Serializable {
    public List<DubCategory> getDubCategories() {
        return dubCategories;
    }

    public void setDubCategories(ArrayList<DubCategory> dubCategories) {
        this.dubCategories = dubCategories;
    }

    @SerializedName("catrgoryCombo")
    @Expose
    ArrayList<DubCategory> dubCategories;
}
