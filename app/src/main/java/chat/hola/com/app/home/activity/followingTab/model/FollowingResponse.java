package chat.hola.com.app.home.activity.followingTab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/14/2018.
 */

public class FollowingResponse implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<Following> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Following> getData() {
        return data;
    }

    public void setData(ArrayList<Following> data) {
        this.data = data;
    }
}
