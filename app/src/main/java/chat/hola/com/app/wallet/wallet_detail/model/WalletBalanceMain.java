package chat.hola.com.app.wallet.wallet_detail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletBalanceMain {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private WalletBalanceData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WalletBalanceData getData() {
        return data;
    }

    public void setData(WalletBalanceData data) {
        this.data = data;
    }
}
