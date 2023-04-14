package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import chat.hola.com.app.live_stream.ResponcePojo.Gifts;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 August 2019
 */
public class GiftCombo implements Serializable {
    @SerializedName("giftCombo")
    @Expose
    private ArrayList<Gifts> gifts;


    public ArrayList<Gifts> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<Gifts> gifts) {
        this.gifts = gifts;
    }
}
