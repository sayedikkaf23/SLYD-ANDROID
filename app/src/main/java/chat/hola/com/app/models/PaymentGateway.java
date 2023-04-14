package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1>PaymentGateway</h1>
 * <p></p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 30 Jan 2020
 */
public class PaymentGateway implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("payoutAvailable")
    @Expose
    private Boolean payoutAvailable;
    @SerializedName("pgCommission")
    @Expose
    private Integer pgCommission;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("pgCommissionfixed")
    @Expose
    private Integer pgCommissionfixed;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("statusString")
    @Expose
    private String statusString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPayoutAvailable() {
        return payoutAvailable;
    }

    public void setPayoutAvailable(Boolean payoutAvailable) {
        this.payoutAvailable = payoutAvailable;
    }

    public Integer getPgCommission() {
        return pgCommission;
    }

    public void setPgCommission(Integer pgCommission) {
        this.pgCommission = pgCommission;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPgCommissionfixed() {
        return pgCommissionfixed;
    }

    public void setPgCommissionfixed(Integer pgCommissionfixed) {
        this.pgCommissionfixed = pgCommissionfixed;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

}
