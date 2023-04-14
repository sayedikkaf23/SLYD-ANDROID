package chat.hola.com.app.wallet.transaction.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TransactionMain implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<TransactionData> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransactionData> getData() {
        return data;
    }

    public void setData(List<TransactionData> data) {
        this.data = data;
    }

}
