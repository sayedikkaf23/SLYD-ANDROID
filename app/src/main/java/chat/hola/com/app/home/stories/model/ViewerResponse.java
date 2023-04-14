package chat.hola.com.app.home.stories.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ViewerResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Viewer> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Viewer> getData() {
        return data;
    }

    public void setData(List<Viewer> data) {
        this.data = data;
    }

}
