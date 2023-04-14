package chat.hola.com.app.request_star_profile.star_category;

/**
 * <h1>{@link chat.hola.com.app.request_star_profile.star_category.StarCatResponse}</h1>
 * <p>Model class of star category response getting from api</p>
 * @author: Hardik Karkar
 * @since : 23rd May 2019
 * {@link chat.hola.com.app.request_star_profile.star_category.Data}
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StarCatResponse implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

}
