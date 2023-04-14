package chat.hola.com.app.dublycategory.modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>DubCategoryResponse</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 7/16/2018.
 */

public class DubCategoryResponse implements Serializable {
    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("data")
    @Expose
    List<DubCategory> dubs;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DubCategory> getDubs() {
        return dubs;
    }

    public void setDubs(List<DubCategory> dubs) {
        this.dubs = dubs;
    }
}
