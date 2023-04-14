package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TranResponse implements Serializable {
    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("data")
    @Expose
    WalletTransactionData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WalletTransactionData getData() {
        return data;
    }

    public void setData(WalletTransactionData data) {
        this.data = data;
    }
}
