package chat.hola.com.app.live_stream.gift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GiftDataResponse implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        @SerializedName("data")
        @Expose
        private List<Gift> gifts = null;
        @SerializedName("totalCount")
        @Expose
        private int totalCount;

        public List<Gift> getGifts() {
            return gifts;
        }

        public void setGifts(List<Gift> gifts) {
            this.gifts = gifts;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }


        public class Gift implements Serializable {

            @SerializedName("_id")
            @Expose
            private String id;
            @SerializedName("categoryId")
            @Expose
            private String categoryId;
            @SerializedName("mobileThumbnail")
            @Expose
            private String mobileThumbnail;
            @SerializedName("gifUrl")
            @Expose
            private String gifUrl;
            @SerializedName("giftCost")
            @Expose
            private String giftCost;
            @SerializedName("statusCode")
            @Expose
            private int statusCode;
            @SerializedName("statusText")
            @Expose
            private String statusText;
            @SerializedName("giftTitle")
            @Expose
            private String giftTitle;
            @SerializedName("categoryName")
            @Expose
            private String categoryName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getMobileThumbnail() {
                return mobileThumbnail;
            }

            public void setMobileThumbnail(String mobileThumbnail) {
                this.mobileThumbnail = mobileThumbnail;
            }

            public String getGifUrl() {
                return gifUrl;
            }

            public void setGifUrl(String gifUrl) {
                this.gifUrl = gifUrl;
            }

            public String getGiftCost() {
                return giftCost;
            }

            public void setGiftCost(String giftCost) {
                this.giftCost = giftCost;
            }

            public int getStatusCode() {
                return statusCode;
            }

            public void setStatusCode(int statusCode) {
                this.statusCode = statusCode;
            }

            public String getStatusText() {
                return statusText;
            }

            public void setStatusText(String statusText) {
                this.statusText = statusText;
            }

            public String getGiftTitle() {
                return giftTitle;
            }

            public void setGiftTitle(String giftTitle) {
                this.giftTitle = giftTitle;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }
        }
    }
}
