package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>Currency</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 05 September 2019
 */
public class Currency implements Serializable {


    @SerializedName(value = "currency", alternate = "currencyCode")
    @Expose
    String currency;
    @SerializedName(value = "symbol", alternate = "currencySymbol")
    @Expose
    String symbol;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
