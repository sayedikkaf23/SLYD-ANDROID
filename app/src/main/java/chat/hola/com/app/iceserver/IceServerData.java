package chat.hola.com.app.iceserver;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IceServerData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("iceServers")
    @Expose
    private List<IceServer> iceServers = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<IceServer> getIceServers() {
        return iceServers;
    }

    public void setIceServers(List<IceServer> iceServers) {
        this.iceServers = iceServers;
    }
}
