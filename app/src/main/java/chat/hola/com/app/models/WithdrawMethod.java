package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WithdrawMethod implements Serializable {
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("pgId")
    @Expose
    private String pgId;
    @SerializedName("pgName")
    @Expose
    private String name;
    @SerializedName("pgIcon")
    @Expose
    private String imgUrl;

    public String getPgId() {
        return pgId;
    }

    public void setPgId(String pgId) {
        this.pgId = pgId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
