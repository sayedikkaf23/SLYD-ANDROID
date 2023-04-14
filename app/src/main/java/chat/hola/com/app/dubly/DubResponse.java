package chat.hola.com.app.dubly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 7/16/2018.
 */

public class DubResponse implements Serializable {
    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("data")
    @Expose
    List<Dub> dubs;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Dub> getDubs() {
        return dubs;
    }

    public void setDubs(List<Dub> dubs) {
        this.dubs = dubs;
    }
}
