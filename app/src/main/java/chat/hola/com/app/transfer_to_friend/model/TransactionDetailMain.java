package chat.hola.com.app.transfer_to_friend.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionDetailMain implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private TransactionDetailData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TransactionDetailData getData() {
        return data;
    }

    public void setData(TransactionDetailData data) {
        this.data = data;
    }
}
