package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import chat.hola.com.app.live_stream.ResponcePojo.Gifts;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 05 August 2019
 */
public class GiftData implements Serializable {
    @SerializedName("_id")
    @Expose
    String id;

    @SerializedName("name")
    @Expose
    String name;

    public Gifts getGifts() {
        return gifts;
    }

    public void setGifts(Gifts gifts) {
        this.gifts = gifts;
    }

    @SerializedName("gifts")
    @Expose
    Gifts gifts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
