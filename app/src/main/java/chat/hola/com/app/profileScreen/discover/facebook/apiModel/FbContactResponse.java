package chat.hola.com.app.profileScreen.discover.facebook.apiModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ankit on 27/3/18.
 */

public class FbContactResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("messag")
    @Expose
    private String messag;
    @SerializedName("data")
    @Expose
    private ArrayList<Data> data = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessag() {
        return messag;
    }

    public void setMessag(String messag) {
        this.messag = messag;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

}
