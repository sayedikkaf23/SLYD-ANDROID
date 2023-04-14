package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>CurrencyResponse</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
public class CurrencyResponse implements Serializable {
    @SerializedName("data")
    @Expose
    List<Currency> currency;

    public List<Currency> getCurrency() {
        return currency;
    }

    public void setCurrency(List<Currency> currency) {
        this.currency = currency;
    }
}
