package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 19 August 2019
 */
public class GiftResponse implements Serializable {
    @SerializedName("remainingBalance")
    @Expose
    String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
