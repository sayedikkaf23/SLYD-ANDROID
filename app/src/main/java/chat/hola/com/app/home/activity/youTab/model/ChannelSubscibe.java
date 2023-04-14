package chat.hola.com.app.home.activity.youTab.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 4/27/2018.
 */

public class ChannelSubscibe implements Serializable {
    @SerializedName("data")
    @Expose
    private List<RequestedChannels> requestedChannels;

    public List<RequestedChannels> getRequestedChannels() {
        return requestedChannels;
    }

    public void setRequestedChannels(List<RequestedChannels> requestedChannels) {
        this.requestedChannels = requestedChannels;
    }
}
