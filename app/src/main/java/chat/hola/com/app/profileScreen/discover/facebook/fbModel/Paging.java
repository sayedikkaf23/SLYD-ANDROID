package chat.hola.com.app.profileScreen.discover.facebook.fbModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ankit on 26/3/18.
 */

public class Paging {

    @SerializedName("cursors")
    @Expose
    private Cursors cursors;

    public Cursors getCursors() {
        return cursors;
    }

    public void setCursors(Cursors cursors) {
        this.cursors = cursors;
    }
}
