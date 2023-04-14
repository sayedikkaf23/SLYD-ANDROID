package chat.hola.com.app.live_stream.ResponcePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class AllGiftsData implements Serializable {
   /* {
        "_id": "5ccd305fa7b3ce552ec7e45b",
            "name": "Emoji",
            "gifts": [
        {
            "id": "5ccd3130a7b3ce552ec7e45c",
                "name": "one",
                "image": "http://streamgift.karry.xyz/1.png",
                "gif": "http://streamgift.karry.xyz/1.gif",
                "coin": "0"
        },
        {
            "id": "5ccd313d66fa1d552dcd4023",
                "name": "two",
                "image": "http://streamgift.karry.xyz/2.png",
                "gif": "http://streamgift.karry.xyz/2.gif",
                "coin": "0"
        }
      ]
    }*/

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("webThumbnail")
    @Expose
    String webThumbnail;
    @SerializedName("image")
    @Expose
    String image;
    @SerializedName("gifts")
    @Expose
    ArrayList<Gifts> gifts;

    private boolean selected = false;

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Gifts> getGifts() {
        return gifts;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
