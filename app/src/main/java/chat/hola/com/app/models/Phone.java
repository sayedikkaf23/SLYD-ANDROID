package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 19 July 2019
 */
public class Phone implements Serializable {
    @SerializedName("countryCode")
    @Expose(serialize = false, deserialize = true)
    String countryCode;

    @SerializedName("phone")
    @Expose(serialize = false, deserialize = true)
    String phone;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
