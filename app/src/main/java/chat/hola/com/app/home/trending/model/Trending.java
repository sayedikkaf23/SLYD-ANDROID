package chat.hola.com.app.home.trending.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Trending implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ArrayList<TrendingResponse> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<TrendingResponse> getData() {
        return data;
    }

    public void setData(ArrayList<TrendingResponse> data) {
        this.data = data;
    }
}
