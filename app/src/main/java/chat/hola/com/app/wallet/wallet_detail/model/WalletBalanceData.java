package chat.hola.com.app.wallet.wallet_detail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WalletBalanceData implements Serializable {

    @SerializedName("balance")
    @Expose
    private Integer balance;
    @SerializedName("pendingBalance")
    @Expose
    private Integer pendingBalance;

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(Integer pendingBalance) {
        this.pendingBalance = pendingBalance;
    }
}
