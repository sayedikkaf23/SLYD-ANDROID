package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;
import java.util.ArrayList;

public class AllGiftsResponse implements Serializable {
    private String message;
    private ArrayList<AllGiftsData> data;

    public String getMessage() {
        return message;
    }

    public ArrayList<AllGiftsData> getData() {
        return data;
    }
}
