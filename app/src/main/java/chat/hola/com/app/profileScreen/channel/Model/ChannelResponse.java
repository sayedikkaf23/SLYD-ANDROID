package chat.hola.com.app.profileScreen.channel.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ankit on 31/3/18.
 */

public class ChannelResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("messgae")
    @Expose
    private String messgae;
    @SerializedName("data")
    @Expose
    private ArrayList<ChannelData> data = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }

    public ArrayList<ChannelData> getData() {
        return data;
    }

    public void setData(ArrayList<ChannelData> data) {
        this.data = data;
    }
}
