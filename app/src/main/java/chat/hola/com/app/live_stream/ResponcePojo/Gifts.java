package chat.hola.com.app.live_stream.ResponcePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Gifts implements Serializable {

    @SerializedName("_id")
    @Expose
    String id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("webThumbnail")
    @Expose
    String image;
    @SerializedName("gif")
    @Expose
    String gif;
    @SerializedName("coin")
    @Expose
    String coin;

    private String streamId;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getGif() {
        return gif;
    }

    public String getCoin() {
        return coin;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }
}
