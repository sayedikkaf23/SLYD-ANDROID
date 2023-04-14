package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GiftCategories implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data implements Serializable {

        @SerializedName("data")
        @Expose
        private List<Category> data = null;
        @SerializedName("totalCount")
        @Expose
        private int totalCount;

        public List<Category> getCategories() {
            return data;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public class Category implements Serializable {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("categoryTitle")
            @Expose
            private String title;
            @SerializedName("mobileThumbnail")
            @Expose
            private String thumbnail;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }
        }
    }

}
