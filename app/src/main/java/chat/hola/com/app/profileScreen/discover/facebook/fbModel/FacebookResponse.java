package chat.hola.com.app.profileScreen.discover.facebook.fbModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ankit on 26/3/18.
 */

public class FacebookResponse {

    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("summary")
    @Expose
    private Summary summary;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

}
