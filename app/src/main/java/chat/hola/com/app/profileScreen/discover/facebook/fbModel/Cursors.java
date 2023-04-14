package chat.hola.com.app.profileScreen.discover.facebook.fbModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ankit on 26/3/18.
 */

public class Cursors {

    @SerializedName("before")
    @Expose
    private String before;
    @SerializedName("after")
    @Expose
    private String after;

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
