package chat.hola.com.app.home.activity.youTab.followrequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 7/6/2018.
 */

public class ResponseModel implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ReuestData> data = null;
    @SerializedName("totalRequest")
    @Expose
    private Integer totalRequest;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ReuestData> getData() {
        return data;
    }

    public void setData(List<ReuestData> data) {
        this.data = data;
    }

    public Integer getTotalRequest() {
        return totalRequest;
    }

    public void setTotalRequest(Integer totalRequest) {
        this.totalRequest = totalRequest;
    }
}
