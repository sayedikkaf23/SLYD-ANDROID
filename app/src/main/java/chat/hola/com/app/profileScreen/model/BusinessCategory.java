package chat.hola.com.app.profileScreen.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>BusinessCategory</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 16 August 2019
 */
public class BusinessCategory implements Serializable {

    @SerializedName("data")
    @Expose
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable {
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("type")
        @Expose
        private String name;
        @SerializedName("statusCode")
        @Expose
        private String statusCode;

        private boolean selected = false;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
