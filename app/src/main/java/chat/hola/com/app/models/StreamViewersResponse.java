package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import chat.hola.com.app.home.stories.model.Viewer;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 07 August 2019
 */
public class StreamViewersResponse implements Serializable {
    @SerializedName("_id")
    @Expose
    String _id;
    @SerializedName("noOfSessions")
    @Expose
    Integer noOfSessions;
    @SerializedName("data")
    @Expose
    Data data;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getNoOfSessions() {
        return noOfSessions;
    }

    public void setNoOfSessions(Integer noOfSessions) {
        this.noOfSessions = noOfSessions;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        @SerializedName("subscribers")
        @Expose
        List<Viewer> viewers;

        public List<Viewer> getViewers() {
            return viewers;
        }

        public void setViewers(List<Viewer> viewers) {
            this.viewers = viewers;
        }
    }
}
