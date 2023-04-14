package chat.hola.com.app.home.trending.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.home.model.Data;

/**
 * <h1>TrendingResponse</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 6/18/2018.
 */

public class TrendingContentResponse implements Serializable {
    @SerializedName("mesage")
    @Expose
    private String mesage;
    @SerializedName("data")
    @Expose
    private ArrayList<Data> data = null;
    @SerializedName("hashTags")
    @Expose
    private List<String> hashTags;

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }
}
