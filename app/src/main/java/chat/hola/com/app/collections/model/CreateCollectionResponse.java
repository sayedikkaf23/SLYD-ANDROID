package chat.hola.com.app.collections.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateCollectionResponse implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("collectionId")
    @Expose
    private String collectionId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }
}
